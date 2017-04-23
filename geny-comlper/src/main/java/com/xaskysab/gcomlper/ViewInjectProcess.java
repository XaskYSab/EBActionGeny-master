package com.xaskysab.gcomlper;

import com.google.auto.service.AutoService;
import com.xaskysab.gan.ParamActionGeny;
import com.xaskysab.gan.TypeActionGeny;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class ViewInjectProcess extends AbstractProcessor {

    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();

    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> types = new LinkedHashSet<>();
        types.add(TypeActionGeny.class.getCanonicalName());
        types.add(ParamActionGeny.class.getCanonicalName());


        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }



    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment re) {


        Map<String, TypeElement> tmap = handleTypeAn(re);
        writeClsWithTypes(tmap);


        Set<VariableElement> varSet = handleParamAn(re);
        writeClsWithParams(varSet);

        return false;
    }

    private void writeClsWithParams(Set<VariableElement> varSet) {

        for (VariableElement variableElement : varSet) {

            String pn = processingEnv.getElementUtils().getPackageOf(variableElement).getQualifiedName().toString();
            String cn = variableElement.asType().toString().substring(variableElement.asType().toString().lastIndexOf(".")+1);
            String newCn = getActionType(cn);
            String newFCn = pn + "." + newCn;


            Map<String, String> fieldMap = new HashMap();
            fieldMap.put(String.class.getSimpleName(), "keep");
            fieldMap.put(int.class.getSimpleName(), "code");
            fieldMap.put(variableElement.asType().toString(), variableElement.getSimpleName().toString());

            try {

                try {
                    Method method = Class.forName("com.sun.tools.javac.processing.JavacFiler").
                            getDeclaredMethod("checkNameAndExistence",String.class,boolean.class);
                    method.setAccessible(true);
                    method.invoke(filer,newFCn,false);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }

                JavaFileObject javaFileObject = filer.createSourceFile(newFCn);
                Writer writer = javaFileObject.openWriter();

                writeImport(writer, pn, newCn);
                writeStart(writer);
                writerProper(writer, fieldMap);
                WriteUtil.writeCst(writer, newCn, fieldMap, WriteUtil.Modified.PUBLIC);
                writeEnd(writer);

                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private Set<VariableElement> handleParamAn(RoundEnvironment roundEnvironment) {

        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(ParamActionGeny.class);

        return (Set<VariableElement>) elementSet;

    }


    private void writeClsWithTypes(Map<String, TypeElement> tmap) {


        for (String fName : tmap.keySet()) {

            TypeElement typeElement = tmap.get(fName);

            Writer writer;

            try {

                JavaFileObject javaFileObject = filer.createSourceFile(getActionType(fName));

                writer = javaFileObject.openWriter();

                String pn = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();

                String newName = getActionType(typeElement.getSimpleName().toString() );

                writeImport(writer, pn, newName);

                writeStart(writer);

                Map<String, String> fieldMap = new HashMap();
                fieldMap.put(typeElement.asType().toString(), typeElement.getSimpleName().toString());
                fieldMap.put(String.class.getSimpleName(), "keep");
                fieldMap.put(int.class.getSimpleName(), "code");

                writerProper(writer, fieldMap);


                WriteUtil.writeCst(writer, newName, fieldMap, WriteUtil.Modified.PUBLIC);


                writeEnd(writer);

                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void writerProper(Writer writer, Map<String, String> properMap) throws IOException {

        for (String fClsName : properMap.keySet()) {

            String fileName = properMap.get(fClsName);
            WriteUtil.writeProper(writer, fClsName, fileName, WriteUtil.Modified.PRIVATE);
            WriteUtil.writeSet(writer, fClsName, fileName);
            WriteUtil.writeGet(writer, fClsName, fileName);
        }
    }

    private void writeImport(Writer writer, String pkn, String newClassName) throws IOException {
        writer.write("package "+pkn+" ;");
        writer.write("\n\n");
        writer.write("public class "+newClassName);
        writer.write(" implements "+ Serializable.class.getCanonicalName());
        writer.write("\n");

    }

    private void writeStart(Writer writer) throws IOException {
        writer.write("{");
        writer.write("\n");
    }

    private Map<String, TypeElement> handleTypeAn(RoundEnvironment roundEnvironment) {

        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(TypeActionGeny.class);


        Map<String, TypeElement> map = new HashMap<>();

        for (Element element : elementSet) {

            TypeElement typeElement = (TypeElement) element;

            String pn = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();

            String cn = pn + "." + typeElement.getSimpleName().toString();

            map.put(cn, typeElement);

        }

        return map;


    }


    private void writeEnd(Writer writer) throws IOException {

        writer.write(" }");
        writer.write("\n\n");

    }


    public String getActionType(String className) {
        return className + "$Action";
    }


}













