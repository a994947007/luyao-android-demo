/*
package com.jny.android.demo.plugin.compiler;

import com.google.auto.service.AutoService;
import com.jny.android.demo.plugin.annotations.InjectModule;
import com.jny.android.demo.plugin.annotations.Plugin;
import com.jny.android.demo.plugin.annotations.PluginClassGetter;
import com.jny.android.demo.plugin.annotations.ProcessorConfig;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.lang.reflect.Type;
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

@Deprecated
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.jny.android.demo.plugin.annotations.InjectModule"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions({ProcessorConfig.APT_PACKAGE})
public class InjectModuleProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private Filer filer;

    private String aptPackage; // 各个模块传递过来的目录 用于存放 apt生成的文件

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        aptPackage = processingEnv.getOptions().get(ProcessorConfig.APT_PACKAGE);
        if (aptPackage == null || "".equals(aptPackage)) {
            aptPackage = ProcessorConfig.DEFAULT_PLUGIN_RESULT_PATH;
        }
        messager.printMessage(Diagnostic.Kind.NOTE, ">>>>>>>>> InjectModuleProcessor init successful");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(InjectModule.class);

        TypeElement pluginType = elementUtils.getTypeElement(ProcessorConfig.PLUGIN_PATH);
        TypeMirror pluginMirror = pluginType.asType();
        TypeName returnType = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(Plugin.class))),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(Plugin.class)))
        );
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConfig.GET_PLUGIN_MAP_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType);
        methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                ClassName.get(Map.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(Plugin.class))),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(Plugin.class))),
                ProcessorConfig.RESULT_NAME,
                ClassName.get(HashMap.class));
        for (Element element : elements) {
            TypeMirror elementMirror = element.asType();
            if (!typeUtils.isSubtype(elementMirror, pluginMirror)) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;
            List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
            if (interfaces == null || interfaces.size() == 0) {
                continue;
            }
            methodBuilder.addStatement("$N.put($T.class, $T.class)",
                    ProcessorConfig.RESULT_NAME,
                    ClassName.get(interfaces.get(0)),
                    ClassName.get(typeElement));
        }
        methodBuilder.addStatement("return $N",
                ProcessorConfig.RESULT_NAME);

        TypeSpec typeSpec = TypeSpec.classBuilder(ProcessorConfig.GET_PLUGIN_CLASS_NAME)
                .addSuperinterface(PluginClassGetter.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodBuilder.build())
                .build();

        try {
            JavaFile.builder(aptPackage, typeSpec)
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
*/
