package com.jny.android.demo.arouter_compiler;

import com.google.auto.service.AutoService;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.jny.android.demo.arouter_annotations.ARouter"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions("student")
public class ARouterProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();

        messager.printMessage(Diagnostic.Kind.NOTE, ">>>>> ARouterProcessor init");
    }

    // 一旦有地方使用对应注解，注解才会打印
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(ARouter.class);
        for (Element element : elementsAnnotatedWith) {
            // 方法
            ARouter aRouter = element.getAnnotation(ARouter.class);
            MethodSpec methodSpec = MethodSpec.methodBuilder("findClass")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(Class.class)
                    .addParameter(String.class, "path")
                    .addStatement("return path.equals($S) ? $T.class : null", aRouter.path(), ClassName.get((TypeElement) element))
                    .build();
            // 类
            String simpleClassName = element.getSimpleName().toString();
            TypeSpec testClass = TypeSpec.classBuilder(simpleClassName + "$ARouter")
                    .addMethod(methodSpec)
                    .addModifiers(Modifier.PUBLIC)
                    .build();
            // 包
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            JavaFile packagef = JavaFile.builder(packageName, testClass)
                    .build();

            try {
                packagef.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}