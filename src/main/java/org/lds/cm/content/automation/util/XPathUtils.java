package org.lds.cm.content.automation.util;



import org.lds.cm.content.automation.util.xml.LDSNamespace;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XPathUtils {

    private static XPath init(){
        final XPathFactory factory = XPathFactory.newInstance();
        return factory.newXPath();

    }

    public static NodeList getElements(final String xPathString, final Document currentDoc) throws XPathExpressionException {
        final XPath xPathObj = init();
        return (NodeList) xPathObj.evaluate(xPathString, currentDoc, XPathConstants.NODESET);
    }

    public static String getStringValue(final String xPathString, final Document currentDoc, boolean ldsNamespace) throws XPathExpressionException{
        final XPath xPathObj = init();
        if(ldsNamespace){
            xPathObj.setNamespaceContext(new LDSNamespace());
        }
        final Node returnNode = (Node)xPathObj.evaluate(xPathString, currentDoc, XPathConstants.NODE);
        return returnNode.getTextContent();
    }

    public static Node getElement(final String xPathString, final Document currentDoc) throws XPathExpressionException{
        final XPath xPathObj = init();
        return (Node) xPathObj.evaluate(xPathString, currentDoc, XPathConstants.NODE);
    }








}
