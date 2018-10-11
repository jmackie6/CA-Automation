package deprecated;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.simple.JSONObject;
//import org.lds.cm.content.automation.transform.util.QATransformationResult;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.NetUtils;
//JMS For test only

public class ContentAutomationQAEndpointUtil {

	private static final String[] MEDIA_XML_TYPES = {"conference-media", "gospel-library-media", "scripture-media"};
	
	/**
	 * Upload and transform a file. This method assumes the file exists.
	 * @param file file to transform
	 * @return jsonResponse response from call to transform endpoint
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws org.json.simple.parser.ParseException 
	 */
//	public static QATransformationResult transformFile(File file) throws ClientProtocolException, IOException, org.json.simple.parser.ParseException { //TODO: Just throw exceptions!
//		JSONObject jsonResponse = null;
//		try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
//			// build request
//			HttpPost httppost = new HttpPost(Constants.epTransform);
//			HttpEntity entity1 = MultipartEntityBuilder.create().addBinaryBody("file", file).build();
//			httppost.setEntity(entity1);
//
//			try(CloseableHttpResponse response = httpClient.execute(httppost)) {
//				// get response
//				HttpEntity entity2 = response.getEntity();
//				String responseString = EntityUtils.toString(entity2);
//				jsonResponse = (JSONObject) new JSONParser().parse(responseString);
//
//				EntityUtils.consume(entity2);
//			}	
//		}
//		return QATransformationResult.fromJSON(jsonResponse);
//	}

	
	/**
	 * Finds the Media Type of a Media XML file by checking the XML. Currently throws all exceptions. Callers might
	 * want to catch IllegalArgumentException if traversing directories where not all files are Media XML.
	 * @param fil XML File to check
	 * @return Valid Media XML Type from array MEDIA_XML_TYPES
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws IllegalArgumentException
	 */
//	private static String findMediaXMLType(File fil) throws XPathExpressionException, SAXException, IOException,
//	 ParserConfigurationException, IllegalArgumentException  {
//		try {
//			for (String s:MEDIA_XML_TYPES) {
//				if (XMLUtils.getNodeFromXpath(fil,"//" + s) != null) {
//					System.out.println("File:" + fil.getAbsolutePath() + "type: " + s);
//					return (s);
//				}
//			}
//			throw new IllegalArgumentException("        Invalid Media XML file type" + fil.getAbsolutePath());
//		} catch (XPathExpressionException | SAXException | IOException
//				| ParserConfigurationException e1) {
//			System.out.println("Exception for: " + fil.getAbsolutePath());
//			throw e1;
//		}
//	}
	
	/**
	 * Upload a Media XML file to the DB. This method assumes the file exists.
	 * @param file file to transform
	 * @param mediaType must be "scripture-media", "conference-media" or "gospel-library-media"
	 * @return jsonResponse response from call to transform endpoint
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws IllegalArgumentException 
	 * @throws XPathExpressionException 
	 */
//	public static boolean uploadMediaXML(File file) throws ClientProtocolException, IOException, org.json.simple.parser.ParseException, 
//		XPathExpressionException, IllegalArgumentException, SAXException, ParserConfigurationException {
//		JSONObject jsonResponse = null;
//		try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
//			// build request
//			HttpPost httppost = new HttpPost(Constants.epUploadMediaXML);
//			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//			builder.addBinaryBody("file", file);
//			builder.addTextBody("mediaType",findMediaXMLType(file));
//			HttpEntity entity1 = builder.build();
//			httppost.setEntity(entity1);
//
//			try(CloseableHttpResponse response = httpClient.execute(httppost)) {
//				// get response
//				HttpEntity entity2 = response.getEntity();
//				String responseString = EntityUtils.toString(entity2);
//				jsonResponse = (JSONObject) new JSONParser().parse(responseString);
//
//				EntityUtils.consume(entity2);	
//			} 
//			//Catch is to debug
//			catch (Exception e1) {
//				System.out.println("Exception for: " + file.getAbsolutePath());
//				throw e1;
//			}
//		} 
//		//Expecting:   "success": [true]
//		JSONArray ja = (JSONArray)jsonResponse.get("success");		
//		return((Boolean)ja.get(0));
//	}
//	
	

	
	
	/**
	 * queries DAM using damID
	 * @param damID id needed to query DAM. Found in content_publish table
	 * @return response from DAM
	 */
	public static JSONObject queryDAM(String damID) {
		JSONObject jsonResponse = null;
		
		try {
			jsonResponse = NetUtils.getJson(Constants.epQueryDAM + damID);
		} catch (ParseException | org.json.simple.parser.ParseException | IOException e) {
			e.printStackTrace();
		}
		
		return jsonResponse;
	}
}
