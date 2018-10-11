package org.lds.cm.content.automation.util;

import static org.testng.Assert.fail;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.lds.cm.content.automation.model.QAValidationError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class QATransformationResult {
	
	public String errorMessage;
	public String previewURL;
	public Long processLogId;
	public  List<QAValidationError> validationErrors;
	public final List<String> transformResults = new ArrayList<>();
	public  ArrayList<String> docIds; // For the document Sandbox transformation
	private  Map<String, Integer> transformErrorResult;
	private boolean successful = false;

	QATransformationResult(){

	}

	private QATransformationResult(String errorMessage, String previewURL, List<QAValidationError> validationErrors, Long processLogId, ArrayList<String>docIds) {
		this.errorMessage = errorMessage;
		this.previewURL = previewURL;
		this.validationErrors = validationErrors;
		this.processLogId = processLogId;
		this.docIds = docIds;	
	}

	public void setProcessLogId(long newProcessId){
		this.processLogId = newProcessId;
	}

	public void setPreviewURL(String newUrl){
		this.previewURL = newUrl;
	}

	public void setValidationErrors(List<QAValidationError> newErrorList){
		this.validationErrors = newErrorList;
	}

	public void setTransformResults(List<String> newTransformResults){
		for (int i = 0; i < newTransformResults.size(); i++) {
			transformResults.add(i, newTransformResults.get(i));
		}
	}

	public void setDocIds(ArrayList<String> newDocIds){
		for (int i = 0; i < newDocIds.size(); i++) {
			docIds.add(i, newDocIds.get(i));
		}
	}




	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String getPreviewURL(){
		return previewURL;
	}
	
	public Long getProcessLogId() {
		return processLogId;
	}

	public int getValidationErrorCount(){
		return validationErrors.size();
	}

	public List<QAValidationError> getValidationError(){
		List<QAValidationError> returnList = new ArrayList<>(validationErrors);
		return returnList;
	}
	
	public List<String> getTransformResults(){
		return transformResults;
	}
	
	public ArrayList<String> getDocIds(){
		return docIds;
	}

	public Map<String, Integer> getErrorMapping(){
		return transformErrorResult;
	}

	private void setSuccessful(boolean success){
		this.successful = success;
	}

	public boolean wasSuccessful() {
		return successful;
	}
	
	public static QATransformationResult fromJSON(JSONObject json) {
		boolean wasSuccessful = (boolean) json.get("transformSuccess");

		String previewURL = (String) json.get("previewUrl");
		String errorMessage = (String) json.get("error");
		Long processLogId = (Long) json.get("processLogId");
		if(!wasSuccessful && errorMessage == null) {
			System.err.println("Failed Transform: One or more of the documents is locked or already published. If transform isn't successful, there should be an error message\n  Response: " + json.toJSONString());
			fail();
		}
		JSONArray rawValidationErrors = (JSONArray) json.get("validationErrors");
		List<QAValidationError> validationErrors = QAValidationError.listFromJSONArray(rawValidationErrors);
		
		JSONArray urlJSONList = (JSONArray) json.get("urls");
		ArrayList <String> docIDs = new ArrayList<>();		
		if (urlJSONList.size() > 0) {

			for (int i = 0; i < urlJSONList.size(); i++) {
				String testJSON = (String) urlJSONList.get(i);
				String [] urlParts = testJSON.split("=|&");
				String docId = urlParts[1];
				docIDs.add(docId);
			}
		}		
		QATransformationResult returnResult = new QATransformationResult(errorMessage, previewURL, validationErrors, processLogId, docIDs);
		returnResult.setSuccessful(wasSuccessful);
		return returnResult;
	}

	public static QATransformationResult fromJSONToMap(JSONObject jObj) throws ParseException{


		final QATransformationResult returnObj = new QATransformationResult();
		returnObj.setProcessLogId((Long) jObj.get("processLogId"));
		returnObj.setPreviewURL((String)jObj.get("previewUrl"));
		returnObj.setSuccessful((boolean) jObj.get("transformSuccess"));

		final JSONArray errorArray = (JSONArray) jObj.get("validationErrors");
		returnObj.transformErrorResult = convertJsonArrayToErrorMap(errorArray);
		returnObj.setValidationErrors(getErrorList(errorArray));
        return returnObj;


	}

	private static List<QAValidationError> getErrorList(JSONArray errorArray){
		List<QAValidationError> returnList = new ArrayList<>();

		for(int i = 0; i < errorArray.size(); i++){
			QAValidationError validationError = new QAValidationError();
			JSONObject jObj = (JSONObject) errorArray.get(i);
			validationError.setValidationSeverity((String)jObj.get("validationErrorSeverity"));
			validationError.setValidationMessage((String)jObj.get("validationMessage"));
			returnList.add(validationError);

		}
		return returnList;
	}

	private static Map<String, Integer> convertJsonArrayToErrorMap(final JSONArray errorsToMap) throws ParseException {
		final Map<String, Integer> returnMap = new HashMap<>();
		final JSONParser jParser = new JSONParser();

		for (int i = 0; i < errorsToMap.size(); i++) {
			String jsonString = errorsToMap.get(i).toString();
			JSONObject jObj = (JSONObject)jParser.parse(jsonString);
			String validationSeverity = (String)jObj.get("validationErrorSeverity");
			JSONObject tempObj = (JSONObject) jObj.get("validationType");
			String validationType = (String)tempObj.get("validationType");
			String containKey = String.join("-", validationType, validationSeverity);
			if(!returnMap.containsKey(containKey)){

			    returnMap.put(containKey, 1);
			}else{
                int j = returnMap.get(containKey).intValue();
				j++;
				returnMap.put(containKey, j);
			}
		}
		return returnMap;
	}

	private static List<String> extractTransformErrorPieces(String transformError){
		return new ArrayList<String>();
	}


	public static QATransformationResult fromXML(final String xmlString) throws ParserConfigurationException, IOException, SAXException {
		final Document currentResults = XMLUtils.getDocumentFromString(xmlString);
		String xPathExpression = "/Validation-errors/validation_error"; //[validation-error-severity='" + expectedSeverity.name() + "' and validation_type/validationType = '" + expectedError.name() + "']";
		final NodeList nodeList = XMLUtils.getNodeListFromXpath(currentResults,xPathExpression, null);
		final List<QAValidationError> currentErrorList = new ArrayList<>();

		final Map<Integer, Map<Integer, Integer>> errorMapping = new HashMap<>();

		for(int i = 0; i < nodeList.getLength(); i++){

			Element currentElement = (Element) nodeList.item(i);
			QAValidationError currentError = QAValidationError.fromXML(currentElement);
			currentErrorList.add(currentError);
		}
		QATransformationResult returnResult = new QATransformationResult(null, null, currentErrorList, null, null);
			return new QATransformationResult(null, null, currentErrorList, null, null);
	}

	public static QATransformationResult fromXMLFiltered(final String xmlString, final ErrorTypes expectedError, final SeverityTypes expectedSeverity) throws ParserConfigurationException, IOException, SAXException {
		final Document currentResults = XMLUtils.getDocumentFromString(xmlString);
		String xPathExpression = "/Validation-errors/validation_error [validation-error-severity='" + expectedSeverity.name() + "' and validation_type/validationType = '" + expectedError.name() + "']";
		String newXPathExpression = "//Validation-errors/validation_error[validation_type[not(validationType = following::validation_type/validationType)]]/validation_type/validationType";
		final NodeList nodeList = XMLUtils.getNodeListFromXpath(currentResults,xPathExpression, null);
		final NodeList newNodeList = XMLUtils.getNodeListFromXpath(currentResults, newXPathExpression, null);

		final List<QAValidationError> currentErrorList = new ArrayList<>();

		final Map<String, Integer> errorMapping = XMLUtils.formatErrorXML(xmlString);

		for(int i = 0; i < nodeList.getLength(); i++){


			Element currentElement = (Element) nodeList.item(i);
			QAValidationError currentError = QAValidationError.fromXML(currentElement);
			currentErrorList.add(currentError);
		}
		QATransformationResult returnResult = new QATransformationResult(null, null, currentErrorList, null, null);
		return new QATransformationResult(null, null, currentErrorList, null, null);
	}

	public static QATransformationResult fromXMLFilteredWithMaps(final String xmlString) throws ParserConfigurationException, IOException, SAXException {
		final Map<String, Integer> errorMapping = XMLUtils.formatErrorXML(xmlString);

		QATransformationResult returnResult = new QATransformationResult();
		returnResult.transformErrorResult = errorMapping;
		return returnResult;

	}

	protected void printErrors(){
//			for(Map.Entry<String, Integer> entry : transformErrorResult.entrySet()){
//				String[] errorPieces = entry.getKey().split("-");
//				System.out.println("*****************************************");
//				System.out.println("Error Type: " + errorPieces[0]);
//				System.out.println("Severity: " + errorPieces[1]);
//				System.out.println("Number of Errors: " + entry.getValue());
//			}
		if(transformErrorResult != null ) {
			for ( Map.Entry<String, Integer> error : transformErrorResult.entrySet()) {
				System.out.println("*****************************************");
				System.out.printf("Error Severity - Number of Errors: %s - %s \n", error.getKey(), error.getValue());
			}
		}
	}

	
	
}
