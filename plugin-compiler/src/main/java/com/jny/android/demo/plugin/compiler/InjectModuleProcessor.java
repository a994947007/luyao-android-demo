package com.jny.android.demo.plugin.compiler;

import com.google.auto.service.AutoService;
import com.jny.android.demo.plugin.PluginCenter;
import com.jny.android.demo.plugin.PluginInit;
import com.jny.android.demo.plugin.PluginUtils;
import com.jny.android.demo.plugin.annotations.InjectModule;
import com.jny.android.demo.plugin.annotations.PluginClassGetter;
import com.jny.android.demo.plugin.annotations.ProcessorConfig;
import com.jny.android.demo.plugin.annotations.ProcessorConfigV2;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
@SupportedAnnotationTypes({"com.jny.android.demo.plugin.annotations.InjectModule"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions({ProcessorConfigV2.APT_PACKAGE, ProcessorConfigV2.MODULE_NAME})
public class InjectModuleProcessor extends AbstractProcessor{
    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private Filer filer;

    private String aptPackage;
    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        aptPackage = processingEnv.getOptions().get(ProcessorConfigV2.APT_PACKAGE);
        moduleName = processingEnv.getOptions().get(ProcessorConfigV2.MODULE_NAME);
        if (aptPackage == null || "".equals(aptPackage)) {
            aptPackage = ProcessorConfigV2.DEFAULT_PLUGIN_RESULT_PATH;
        }
        messager.printMessage(Diagnostic.Kind.NOTE, ">>>>>>>>> InjectModuleProcessor init successful");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(InjectModule.class);
        TypeElement pluginType = elementUtils.getTypeElement(ProcessorConfigV2.PLUGIN_PATH);
        TypeMirror pluginMirror = pluginType.asType();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConfigV2.GET_PLUGIN_MAP_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
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
            methodBuilder.addStatement("$T.register($T.class, $T.class)",
                    ClassName.get(PluginCenter.class),
                    interfaces.get(0),
                    typeElement);
        }
        TypeSpec typeSpec = TypeSpec.classBuilder(className())
                .addSuperinterface(PluginInit.class)
                .addAnnotation(AnnotationSpec.builder(AutoService.class)
                        .addMember("value", "{$T.class}", PluginInit.class)
                        .build())
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

    private String className() {
        return PluginUtils.makeModuleClassName(moduleName);
    }
}
