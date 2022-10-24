package com.jny.android.demo.arouter_compiler;

import com.google.auto.service.AutoService;
import com.jny.android.demo.ProcessorConfig;
import com.jny.android.demo.RouterBean;
import com.jny.android.demo.RouterGroupModule;
import com.jny.android.demo.api.ARouterPath;
import com.jny.android.demo.api.IRouterTab;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.android.demo.ProcessorUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.jny.android.demo.arouter_annotations.ARouter"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions({ProcessorConfig.OPTIONS, ProcessorConfig.APT_PACKAGE})
public class ARouterProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private Filer filer;

    private String options; // 各个模块传递过来的模块名 例如：app order personal
    private String aptPackage; // 各个模块传递过来的目录 用于存放 apt生成的文件

    private final Map<String, RouterBean> mRouterBeanPool = new HashMap<>();

    private final Map<String, List<RouterBean>> mAllPathMap = new HashMap<>();

    private final Map<String, String> mAllGroupMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        options = processingEnv.getOptions().get(ProcessorConfig.OPTIONS);
        aptPackage = processingEnv.getOptions().get(ProcessorConfig.APT_PACKAGE);
        if (options != null && aptPackage != null) {
            messager.printMessage(Diagnostic.Kind.NOTE, "APT 环境搭建完成....");
        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "APT 环境有问题，请检查 options 与 aptPackage 为null...");
        }
    }

    // 一旦有地方使用对应注解，注解才会打印
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        // 通过Element工具类，获取Activity，Callback类型
        TypeElement activityType = elementUtils.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE);
        // 显示类信息（获取被注解的节点，类节点）这也叫自描述 Mirror
        TypeMirror activityMirror = activityType.asType();

        TypeElement fragmentType = elementUtils.getTypeElement(ProcessorConfig.FRAGMENT_PACKAGE);
        TypeMirror fragmentMirror = fragmentType.asType();

        TypeElement resType = elementUtils.getTypeElement(ProcessorConfig.RES_PACKAGE);
        TypeMirror resMirror = resType.asType();
        
        for (Element element : elements) {
            TypeMirror elementMirror = element.asType();

            ARouter aRouterAnnotation = element.getAnnotation(ARouter.class);
            RouterBean.Builder builder = new RouterBean.Builder()
                    .addGroup(aRouterAnnotation.group())
                    .addPath(aRouterAnnotation.path())
                    .addElement(element);

            if (typeUtils.isSubtype(elementMirror, activityMirror)) {
                builder.addType(RouterBean.TypeEnum.ACTIVITY);
            } else if (typeUtils.isSubtype(elementMirror, fragmentMirror)){
                builder.addType(RouterBean.TypeEnum.FRAGMENT);
            } else if (typeUtils.isSubtype(elementMirror, resMirror)) {
                builder.addType(RouterBean.TypeEnum.RES);
            } else {
                throw new RuntimeException("@ARouter注解不支持该类型");
            }

            RouterBean routerBean = builder.build();

            // 校验
            if (!checkRouterPath(routerBean)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范配置，如：/app/MainActivity");
                return false;
            }

            List<RouterBean> routerBeans = mAllPathMap.get(routerBean.getGroup());
            if (routerBeans == null || routerBeans.size() == 0) {
                routerBeans = new ArrayList<>();
                mAllPathMap.put(routerBean.getGroup(), routerBeans);
            }
            routerBeans.add(routerBean);
        }

        TypeElement pathType = elementUtils.getTypeElement(ProcessorConfig.ROUTER_API_PATH);
        TypeElement groupType = elementUtils.getTypeElement(ProcessorConfig.ROUTER_API_GROUP);
        // 生成RouterPath类
        try {
            createPathFile(pathType);
            createGroupFile(groupType, pathType);
            createRouterTab();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void createRouterTab() {
        TypeName routerTabMethodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterGroupModule.class)
        );


        for (Map.Entry<String, List<RouterBean>> entry : mAllPathMap.entrySet()) {
            List<RouterBean> routerBeans = entry.getValue();
            MethodSpec.Builder routerTabMethodSpecBuilder = MethodSpec.methodBuilder(ProcessorConfig.ROUTER_TAB_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(routerTabMethodReturn)
                    .addAnnotation(Override.class)
                    .addStatement("$T<$T, $T> $N = new $T<>()",
                            ClassName.get(Map.class),
                            ClassName.get(String.class),
                            ClassName.get(RouterGroupModule.class),
                            ProcessorConfig.ROUTER_TAB_MAP_NAME,
                            ClassName.get(HashMap.class));
            String group = "";
            for (RouterBean routerBean : routerBeans) {
                routerTabMethodSpecBuilder.addStatement("$N.put($S, $T.create($S, $S))",
                        ProcessorConfig.ROUTER_TAB_MAP_NAME,
                        routerBean.getPath(),
                        ClassName.get(RouterGroupModule.class),
                        aptPackage,
                        options);
                if ("".equals(group)) {
                    group = routerBean.getGroup();
                }
            }
            routerTabMethodSpecBuilder.addStatement("return $N", ProcessorConfig.ROUTER_TAB_MAP_NAME);
            TypeSpec typeSpec = TypeSpec.classBuilder(ProcessorUtils.upperCaseFirstChat(group) + ProcessorConfig.ROUTER_TAB_CLASS)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(routerTabMethodSpecBuilder.build())
                    .addSuperinterface(IRouterTab.class)
                    .build();

            try {
                JavaFile.builder(ProcessorConfig.ROUTER_TAB_CLASS_PACKAGE, typeSpec)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void createGroupFile(TypeElement groupType, TypeElement pathType) throws IOException {
        if (mAllGroupMap.size() == 0 || mAllPathMap.size() == 0) {
            return;
        }
        // Map<String, Class<? extends RouterPah>>
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(ARouterPath.class)))
        );

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConfig.GROUP_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(methodReturns);

        methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(ARouterPath.class))),
                ProcessorConfig.GROUP_MAP_NAME,
                ClassName.get(HashMap.class));

        for (Map.Entry<String, String> entry : mAllGroupMap.entrySet()) {
            methodBuilder.addStatement("$N.put($S, $T.class)",
                    ProcessorConfig.GROUP_MAP_NAME,
                    entry.getKey(),
                    ClassName.get(aptPackage, entry.getValue()));
        }

        methodBuilder.addStatement("return $N", ProcessorConfig.GROUP_MAP_NAME);
        String finalClassName = ProcessorConfig.PREFIX_GROUP_CLASS_NAME
                + String.valueOf(options.charAt(0)).toUpperCase()
                + options.substring(1);

        TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                .addSuperinterface(ClassName.get(groupType))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();

        JavaFile.builder(aptPackage, typeSpec)
                .build()
                .writeTo(filer);
    }

    private void createPathFile(TypeElement pathType) throws IOException {
        if (mAllPathMap.size() == 0) {
            return;
        }

        // Map<String, RouterBean>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class)
        );

        for (Map.Entry<String, List<RouterBean>> entry : mAllPathMap.entrySet()) {
            // 方法
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConfig.PATH_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodReturn)
                    .addStatement("$T<$T, $T> $N = new $T<>()",
                            ClassName.get(Map.class),
                            ClassName.get(String.class),
                            ClassName.get(RouterBean.class),
                            ProcessorConfig.PATH_MAP_NAME,
                            ClassName.get(HashMap.class));

            //TypeEnum type, Class<?> clazz, String path, String group
            List<RouterBean> pathList = entry.getValue();
            for (RouterBean routerBean : pathList) {
                methodBuilder.addStatement("$N.put($S, $T.create($T.$L, $T.class, $S, $S))",
                        ProcessorConfig.PATH_MAP_NAME,
                        routerBean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterBean.TypeEnum.class),
                        routerBean.getTypeEnum(),
                        ClassName.get((TypeElement) routerBean.getElement()),
                        routerBean.getPath(),
                        routerBean.getGroup());
            }
            methodBuilder.addStatement("return $N", ProcessorConfig.PATH_MAP_NAME);

            // 类
            String finalClassName = ProcessorConfig.PREFIX_PATH_CLASS_NAME
                    + String.valueOf(entry.getKey().charAt(0)).toUpperCase()
                    + entry.getKey().substring(1);
            TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(pathType))
                    .addMethod(methodBuilder.build())
                    .build();

            // 包
            JavaFile.builder(aptPackage, typeSpec)
                    .build()
                    .writeTo(filer);
            mAllGroupMap.put(entry.getKey(), finalClassName);
        }
    }



    private boolean checkRouterPath(RouterBean routerBean) {
        String path = routerBean.getPath();
        String group = routerBean.getGroup();

        if (path == null || "".equals(path)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的path值不能为空");
            return false;
        }

        if (group == null || "".equals(group)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "routerPath >> " + path.lastIndexOf("/") + " path " + path);
            if (path.lastIndexOf("/") > 0) {
                if (mRouterBeanPool.containsKey(path)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, path + "已经被注册");
                    return false;
                }
                mRouterBeanPool.put(path, routerBean);
                if (path.startsWith("/")) {
                    routerBean.setGroup(path.substring(1, path.indexOf("/", 1)));
                } else {
                    routerBean.setGroup(path.substring(0, path.indexOf("/", 1)));
                }
            } else {
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                String fullPath = ProcessorUtils.getFullPath(path);
                if (mRouterBeanPool.containsKey(fullPath)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, fullPath + "已经被注册");
                    return false;
                }
                mRouterBeanPool.put(fullPath, routerBean);
                routerBean.setPath(fullPath);
                routerBean.setGroup(ProcessorConfig.DEFAULT_GROUP);
            }
        } else {
            int lastIndex = path.lastIndexOf("/");
            if (lastIndex == 0) {
                routerBean.setPath("/" + group + "/" + path.substring(1));
            } else if (lastIndex < 0){
                routerBean.setPath("/" + group + "/" + path);
            } else {
                if (!path.startsWith("/")) {
                    if (!group.equals(path.substring(0, path.indexOf("/")))) {
                        routerBean.setPath("/" + group + "/" + path);
                    } else {
                        routerBean.setPath("/" + path);
                    }
                }
            }
        }

        return true;
    }
}