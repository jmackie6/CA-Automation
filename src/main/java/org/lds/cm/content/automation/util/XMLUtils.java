package org.lds.cm.content.automation.util;

import static org.lds.cm.content.automation.util.LogUtils.getLogger;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lds.cm.content.automation.model.QADocInfo;
import org.slf4j.Logger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;
import org.lds.cm.content.automation.enums.ContentType;
import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QALanguageService;
import org.lds.cm.content.automation.util.xml.LDSNamespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLUtils {
	private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder docBuilder;
	private static XPathFactory factory = XPathFactory.newInstance();
	private static XPath xpath = factory.newXPath();
	private static final Logger LOG = getLogger();
	
	
	// Generic handlers for getting something from XPath
	
	static {
		try {
			docFactory.setNamespaceAware(true);
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		docFactory.setExpandEntityReferences(false);
		xpath.setNamespaceContext(new LDSNamespace());
	}

	private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException{
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setExpandEntityReferences(false);
		return factory.newDocumentBuilder();
	}


	///////////////////// Get dom document object ///////////////////////////////////
	public static Document getDocumentFromString (String xml) throws ParserConfigurationException, SAXException, IOException {
        return docBuilder.parse(new InputSource(new StringReader(xml)));
	}

	public static Document getDocumentFromStringHTML(String xml) throws ParserConfigurationException, SAXException, IOException{
		final DocumentBuilder db = getDocumentBuilder();
		return db.parse(xml);
	}

	public static Document getDocumentFromFilePath(String xml) throws ParserConfigurationException, SAXException, IOException, FileNotFoundException{
        final File tempFile = new File(xml);
		final DocumentBuilder db = getDocumentBuilder();
		return db.parse(tempFile);
	}

	public static Document getDocumentFromFilePathWithFile(String xml, File file) throws ParserConfigurationException, SAXException, IOException, FileNotFoundException{
		final DocumentBuilder db = getDocumentBuilder();
		return db.parse(file);
	}

	public static Document getDocumentFromFile (File input) throws ParserConfigurationException, SAXParseException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setExpandEntityReferences(false);
		factory.setValidating(false);
		factory.setFeature("http://xml.org/sax/features/namespaces", false);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(input);
		return doc;
	}
	
	///////////////////// Perform XPath Checks ///////////////////////////////////
	public static NodeList getNodeListFromXpath (String xml, String xpathExpression, NamespaceContext namespaceContext) {
		NodeList nodeList = null;

		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			docFactory.setExpandEntityReferences(false);
			Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));
			nodeList = getNodeListFromXpath(doc, xpathExpression, namespaceContext);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return nodeList;
	}
	
	public static NodeList getNodeListFromXpath(File xml, String xpathExpression) {
		NodeList nodeList = null;

		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			docFactory.setExpandEntityReferences(false);
			Document doc = docBuilder.parse(xml.getAbsolutePath());
			nodeList = getNodeListFromXpath(doc, xpathExpression, null);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return nodeList;

	}
	
	public static NodeList getNodeListFromXpath(Document doc, String xpathExpression, NamespaceContext namespaceContext) {
		NodeList nodeList = null;
		
		try {
			if (null != namespaceContext) {
				xpath.setNamespaceContext(namespaceContext);
			}
			nodeList = (NodeList) xpath.evaluate(xpathExpression, doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return nodeList;
	}

//	public static NodeList getNodeListFromXpath2(File file, String xpathExpression, NamespaceContext namespaceContext) {
//		NodeList nodeList = null;
//
//		try {
//			if (null != namespaceContext) {
//				xpath.setNamespaceContext(namespaceContext);
//			}
//			nodeList = (NodeList) xpath.evaluate(xpathExpression, file, XPathConstants.NODESET);
//		} catch (XPathExpressionException e) {
//			e.printStackTrace();
//		}
//
//		return nodeList;
//	}
	
	public static NodeList getNodeListFromXpath(Element startingElement, String xpathExpression, NamespaceContext namespaceContext) {
		NodeList nodeList = null;
		
		try {
			if (null != namespaceContext) {
				xpath.setNamespaceContext(namespaceContext);
			}
			nodeList = (NodeList) xpath.evaluate(xpathExpression, startingElement, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return nodeList;
	}

	
	///////////////////// Convert doc to string ///////////////////////////////////
	public static String writeDocToString (Document doc) throws TransformerException { //TODO: Just throw exceptions!
    	DOMSource domSource = new DOMSource(doc);
    	StringWriter writer = new StringWriter();
    	StreamResult result = new StreamResult(writer);
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.transform(domSource, result);
       	return writer.toString();
	}

	///////////////////// QAHtml5Document Methods ///////////////////////////////////
	public static QADocInfo getDocInfo(QAHtml5Document html5Document) {
		if (html5Document == null) {
			return null;
		}
		
		Document document = null;
		
		try {
			document = getDocumentFromString(html5Document.getDocumentXML());
		} catch (ParserConfigurationException | SAXException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		QADocInfo docInfo = new QADocInfo();
		
		// set some values based on database record
		docInfo.setCitation(html5Document.getCitation());
		docInfo.setFileId(html5Document.getFileId());
		
		try {
			docInfo.setLanguageId(html5Document.getLanguageID());
			docInfo.setLanguage(QALanguageService.findLangByLangCode(docInfo.getLanguageId()).getIsoPart3Code());
		} catch (SQLException | IOException e1) {
			e1.printStackTrace();
		}
		
		
		// set some values based on document_xml
		NodeList titles = getNodeListFromXpath(document, "//lds:meta/lds:title", null);
		for(int i = 0; i < titles.getLength(); i++) {
			Node node = titles.item(i);
			NamedNodeMap attrs = node.getAttributes();
			Node typeAttr = attrs.getNamedItem("type");
			if(null != typeAttr) {
				if("file".equalsIgnoreCase(typeAttr.getNodeValue())) {
					docInfo.setXmlFileId(node.getTextContent());
				} else if ("citation".equalsIgnoreCase(typeAttr.getNodeValue())) {
					docInfo.setCitation(node.getTextContent());
				}
			}
		}
		
		NodeList html5HeadTag = getNodeListFromXpath(html5Document.getDocumentXML(), "/html", null);
		if(html5HeadTag.getLength() > 0) {
			Node node = html5HeadTag.item(0);
			NamedNodeMap attrs = node.getAttributes();
			Node idNode = attrs.getNamedItem("data-uri");
			if(null != idNode) {
				docInfo.setURI(idNode.getNodeValue());
			} else {
				throw new IllegalArgumentException("Source file is missing a data-uri attribute.");
			}
			
			Node langNode = attrs.getNamedItem("lang");
			if(null != langNode) {
				docInfo.setXmlLanguage(langNode.getNodeValue());
			} else {
				throw new IllegalArgumentException("Source file is missing a language attribute.");
			}
			
			Node versionNode = attrs.getNamedItem("data-aid-version");
			if(null != versionNode) {
				docInfo.setXmlVersion(versionNode.getNodeValue());
			} else {
				docInfo.setXmlVersion("");
			}
			
			Node dataContentTypeNode = attrs.getNamedItem("data-content-type");
			if(null != dataContentTypeNode) {
				docInfo.setXmlDataContentType(dataContentTypeNode.getNodeValue());
			} else {
				docInfo.setXmlDataContentType("");
			}
			
			Node aIdNode = attrs.getNamedItem("data-aid");
			if(null != aIdNode) {
				docInfo.setaID(aIdNode.getNodeValue());
			} else {
//				throw new IllegalArgumentException("Source file is missing a data-aid attribute.");
			}
			
			try {
				// Note:  This uses the URI value set above and this call must come after that one.
				docInfo.setXmlContentType(getContentTypeFromUri(docInfo.getURI()));
			} catch (Exception e) {
				// do nothing.
			}
		}
		
		return docInfo;
	}
		
	public static ContentType getContentTypeFromUri (String uri) {
		if (StringUtils.isBlank(uri)) {
			return null;
		}
		
		if (uri.startsWith("/")) {
			uri = uri.substring(1);
		}
		
		String content = uri.substring(0, uri.indexOf("/")).toLowerCase();
		
		if (content.equals("ensign") || content.equals("liahona") || content.equals("friend") || content.equals("new-era")) {
			return ContentType.MAGAZINE;
		} else if (content.equals("scriptures")) {
			return ContentType.SCRIPTURES;
		} else if (content.equals("broadcast")) {
			return ContentType.BROADCAST;
		} else if (content.equals("general-conference")) {
			return ContentType.CONFERENCE;
		} else {
			return ContentType.MANUAL;
		}
	}
	
	

	public static File convertDocxToXML(File docXFile, String outputFilePath) throws JAXBException, Docx4JException, SQLException {

		String inputFilePath = docXFile.getPath();
		//inputFilePath.replaceAll("\\s+","");
		
			WordprocessingMLPackage wmlPackage = Docx4J.load(new java.io.File(inputFilePath));
			Docx4J.save(wmlPackage, new File(outputFilePath), Docx4J.FLAG_SAVE_FLAT_XML);
			
			MainDocumentPart documentPart = wmlPackage.getMainDocumentPart();
			String xpath = "//w:tr/w:tc/w:p/w:r/w:t";
			List<Object> list = documentPart.getJAXBNodesViaXPath(xpath, false);
			ArrayList<String> arrlist = new ArrayList<>();
			for (Object obj : list) {
				Text text = (Text) ((JAXBElement<?>)obj).getValue();
		        String textValue = text.getValue();
		        arrlist.add(textValue);
			      }

			StringBuilder out = new StringBuilder();
			for (Object o : arrlist)
			{
			  out.append(o.toString());
			  out.append("\t");
			}
			 out.toString();
			
			String patternString1 = ("[a-zA-Z0-9]*_\\d{3}_");

	        Pattern pattern = Pattern.compile(patternString1);
	        Matcher regexMatcher = pattern.matcher(out);

	        List<String> returnValue= new ArrayList<>();

	        while(regexMatcher.find())
	            if(regexMatcher.group().length() != 0)
	                returnValue.add(regexMatcher.group());

	        for(int i=0; i<returnValue.size(); i++) {
	        	String fileID = returnValue.get(i);
	            System.out.println(fileID);
	            QADeleteService.deleteFromAnnotationsMLAndDB(fileID);
	        }

		return null;
	}
	
	/*Removes all data-aids from a file*/
	public static Document removeDataAids(Document document) throws IOException, SAXException {
		
		
		NodeList elements = getNodeListFromXpath(document, "//*[@data-aid]", null);
		
		
		for(int i = 0; i < elements.getLength(); i++) {
			if(elements.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) elements.item(i);
				el.removeAttribute("data-aid");
			}
		}
		
		return document;
		
	}

	/*Removes all data-aids from a file*/
	public static File removeDataAids(String document, File file) throws IOException, SAXException, ParserConfigurationException, TransformerException, FileNotFoundException {

//		final Document currentDocument = XMLUtils.getDocumentFromFilePath(document);
//		final Document currentDocument = XMLUtils.getDocumentFromFilePathWithFile(document, file);
		final Document currentDocument = XMLUtils.getDocumentFromFile(file);

		final NodeList elements = getNodeListFromXpath(currentDocument, "//*[@data-aid]", null);


		for(int i = 0; i < elements.getLength(); i++) {
			if(elements.item(i).getNodeType() == Node.ELEMENT_NODE) {
				final Element el = (Element) elements.item(i);
				el.removeAttribute("data-aid");
			}
		}

		DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();

		DOMSource source = new DOMSource(currentDocument);
//		FileWriter writer = new FileWriter(file);
		StringWriter stringWriter = new StringWriter();
//		StreamResult result = new StreamResult(writer);
		StreamResult result = new StreamResult(stringWriter);


		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
//		transformer.transform(source, result);

		transformer.transform(source, result);
		String xmlString = stringWriter.toString();
//		return currentDocument;

		try (PrintWriter out = new PrintWriter(file)) {
			out.println(xmlString);
		}
		return file;

	}

	public static Map< String, Integer> formatErrorXML(final String xmlString) throws ParserConfigurationException, IOException, SAXException{
		Map<String, Integer> returnMapping = new HashMap<>();
		String xPathGetValidationTypes = "//validation_error[validation_type[not(validationType = following::validation_type/validationType)]]/validation_type/validationType";
		String xPathGetErrorSeverity = "//validation_error[validation_type/validationType = '%s']/validation-error-severity";
		String xPathGetErrorTypeAndSeverityCount = "//validation_error[validation_type/validationType = '%s' and validation-error-severity='%s']";
		final Document errorDoc = XMLUtils.getDocumentFromString(xmlString);

		final NodeList validationTypeList = getNodeListFromXpath(errorDoc, xPathGetValidationTypes, null);

		for(int i = 0; i < validationTypeList.getLength(); i++){
			String validationType = validationTypeList.item(i).getTextContent();
			String xPathGetErrorSeverityFormatted = String.format(xPathGetErrorSeverity, validationType);
			NodeList tempList = getNodeListFromXpath(errorDoc, xPathGetErrorSeverityFormatted, null );

			if(tempList.getLength() < 1) {
				for (int j = 0; j < tempList.getLength(); j++) {

					String xPathCount = String.format(xPathGetErrorTypeAndSeverityCount, validationType, tempList.item(j).getTextContent());
					NodeList nl = getNodeListFromXpath(errorDoc, xPathCount, null);
					int errorCount = Integer.parseInt(nl.item(0).getTextContent());
					returnMapping.put( String.join("-", validationType, nl.item(0).getTextContent()), errorCount );

				}

			}else{
					String xPathCount2 = String.format(xPathGetErrorTypeAndSeverityCount, validationType, tempList.item(0).getTextContent());
					NodeList nl2 = getNodeListFromXpath(errorDoc, xPathCount2, null);
					//int errorCount2 = Integer.parseInt(nl2.item(0).getTextContent());
                    String tempString = String.join("-", validationType, tempList.item(0).getTextContent());
                    returnMapping.put(tempString,nl2.getLength()) ;
                }
        }


		return returnMapping;
	}

	public static void printNodes(Document doc) throws XPathExpressionException {

		String data_aid = "//@data-aid";
		String data_aid_version = "//@data-aid-version";
		String data_content_type = "//@data-content-type";
		String data_uri = "//@data-uri";
		String lang = "//@lang";
		String title = "//title/text()";

		NodeList data_aid_node = XMLUtils.getNodeListFromXpath(doc, data_aid, null);
		NodeList data_aid_version_node = XMLUtils.getNodeListFromXpath(doc, data_aid_version, null);
		NodeList data_content_type_node = XMLUtils.getNodeListFromXpath(doc, data_content_type, null);
		NodeList data_uri_node = XMLUtils.getNodeListFromXpath(doc, data_uri, null);
		NodeList lang_node = XMLUtils.getNodeListFromXpath(doc, lang, null);
		NodeList title_node = XMLUtils.getNodeListFromXpath(doc, title, null);



		for (int i = 0; i < lang_node.getLength(); i++) {
			System.out.println("Data Aid: " + data_aid_node.item(i).getNodeValue());
			System.out.println("Data Aid Version: " + data_aid_version_node.item(i).getNodeValue());
			System.out.println("Data Content Type: " + data_content_type_node.item(i).getNodeValue());
			System.out.println("Data URI: " + data_uri_node.item(i).getNodeValue());
			System.out.println("Lang: " + lang_node.item(i).getNodeValue());
			System.out.println("Title: " + title_node.item(i).getNodeValue());
		}


	}
}
