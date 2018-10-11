package org.lds.cm.content.automation.util;

import net.sf.saxon.s9api.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

public class XSLUtils {

    public static String executeTransform(String xslFilepath, String xmlFilepath) throws SaxonApiException{
        Writer outputWriter = new StringWriter();
        String outputPath = determineSystem() + "/tmp123.html";
        Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
//        comp.setSchemaAware(true);
        XsltExecutable exp = comp.compile(new StreamSource(new File(xslFilepath))); //this is where the xsl(s) go
        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(new File(xmlFilepath))); //these are the documents to transform
        Serializer out = proc.newSerializer() ;
        out.setOutputProperty(Serializer.Property.METHOD, "html");
        out.setOutputProperty(Serializer.Property.INDENT, "yes");
//        out.setOutputWriter(outputWriter);
        out.setOutputFile(new File(outputPath));
        XsltTransformer starScream = exp.load();
        starScream.setInitialContextNode(source);
        starScream.setDestination(out);

        starScream.transform();
//        return outputWriter.toString();
        return outputPath;
    }



    private static String determineSystem(){
       if(System.getProperty("os.name").equals("Mac OS X")){
           return "/tmp";
       }
        return "/temp";
    }

}
