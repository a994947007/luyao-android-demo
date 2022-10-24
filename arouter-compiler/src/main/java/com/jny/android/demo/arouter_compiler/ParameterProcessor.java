package com.jny.android.demo.arouter_compiler;

import com.google.auto.service.AutoService;
import com.jny.android.demo.ClassUtils;
import com.jny.android.demo.ProcessorConfig;
import com.jny.android.demo.RouterBeanLoader;
import com.jny.android.demo.api.ParameterLoad;
import com.jny.android.demo.api.Res;
import com.jny.android.demo.arouter_annotations.Parameter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.jny.android.demo.arouter_annotations.Parameter"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ParameterProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private Filer filer;

    private Map<TypeElement, List<Element>> mParams = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Parameter.class);
        for (Element element : elements) {
            TypeElement classElement = (TypeElement) element.getEnclosingElement();
            TypeElement activityElement = elementUtils.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE);
            if (!typeUtils.isSubtype(classElement.asType(), activityElement.asType())) {
                throw new RuntimeException("@Parameter注解目前仅限用于Activity类之上");
            }
            List<Element> paramElements = mParams.get(classElement);
            if (paramElements == null) {
                paramElements = new ArrayList<>();
                mParams.put(classElement, paramElements);
            }
            paramElements.add(element);
        }

        if (mParams.isEmpty()) {
            return false;
        }

        TypeElement resType = elementUtils.getTypeElement(ProcessorConfig.RES_PACKAGE);
        TypeMirror resMirror = resType.asType();

        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, ProcessorConfig.LOAD_PARAM_METHOD_PARAM_NAME)
                .build();
        for (Map.Entry<TypeElement, List<Element>> entry : mParams.entrySet()) {
            TypeElement typeElement = entry.getKey();
            List<Element> elementParams = entry.getValue();
            ClassName className = ClassName.get(typeElement);

            MethodSpec.Builder builder = MethodSpec.methodBuilder(ProcessorConfig.LOAD_PARAM_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(parameterSpec)
                    .addStatement("$T t = ($T)" + ProcessorConfig.LOAD_PARAM_METHOD_PARAM_NAME,
                            className,
                            className);
            for (Element elementParam : elementParams) {
                Parameter annotation = elementParam.getAnnotation(Parameter.class);
                String keyName = annotation.value();
                String fieldName = elementParam.getSimpleName().toString();
                if (keyName == null || "".equals(keyName)) {
                    keyName = fieldName;
                }
                String arg = "t." + fieldName;
                String reg = arg;
                TypeMirror typeMirror = elementParam.asType();
                if (typeUtils.isSubtype(typeMirror, resMirror)) {
                    reg += " = ($T)$T.newInstance($T.getInstance().loadRouterBean($S))";
                    builder.addStatement(reg,
                            ClassName.get(Res.class),
                            ClassName.get(ClassUtils.class),
                            ClassName.get(RouterBeanLoader.class),
                            keyName);
                } else {
                    reg += " = t.getIntent().";
                    int type = typeMirror.getKind().ordinal();
                    if (type == TypeKind.INT.ordinal()) {
                        reg += "getIntExtra($S, " + arg + ")";
                    } else if (type == TypeKind.BOOLEAN.ordinal()) {
                        reg += "getBooleanExtra($S, " + arg + ")";
                    } else {
                        if (typeMirror.toString().equalsIgnoreCase(ProcessorConfig.STRING)) {
                            reg += "getStringExtra($S)";
                        }
                    }
                    builder.addStatement(reg, keyName);
                }

            }

            TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName().toString() + ProcessorConfig.PARAMETER_LOADER_CLASS_NAME)
                    .addSuperinterface(ParameterLoad.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(builder.build())
                    .build();

            try {
                JavaFile.builder(className.packageName(), typeSpec)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
