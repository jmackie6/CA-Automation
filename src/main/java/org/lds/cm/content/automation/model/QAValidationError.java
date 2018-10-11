package org.lds.cm.content.automation.model;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mongodb.util.JSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class QAValidationError {
		
	private long validationErrorID;
	private String validationType;
	private String validationMessage;
	private Timestamp createdDate;
	private long lineNumber;
	private long validationTypeID;
	private long severityID;
	private long documentID;
	private String validationSeverity;


	public QAValidationError() {
		
	}

	public QAValidationError(long validationErrorID, String validationMessage, String validationType, Timestamp createdDate,String newValidationSeverity ) {
		this.validationErrorID = validationErrorID;
		this.validationType = validationType;
		this.validationMessage = validationMessage;
		this.createdDate = createdDate;
		this.validationSeverity = newValidationSeverity;
	}

    public QAValidationError(long validationErrorID, String validationType, String validationMessage, Timestamp createdDate) {
        this.validationErrorID = validationErrorID;
        this.validationType = validationType;
        this.validationMessage = validationMessage;
        this.createdDate = createdDate;
    }

	public QAValidationError(long validationErrorID, String validationMessage, Timestamp createdDate) {
		this.validationErrorID = validationErrorID;
		this.validationMessage = validationMessage;
		this.createdDate = createdDate;
	}

	
	public long getValidationErrorID() {
		return validationErrorID;
	}
	
	public void setValidationErrorID(long validationErrorID) {
		this.validationErrorID = validationErrorID;
	}

	public String getValidationType() {
		return validationType;
	}
	
	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getValidationMessage() {
		return validationMessage;
	}
	
	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public long getLineNumber() {
		return lineNumber;
	}
	
	public void setLineNumber(long lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public long getSeverityID() {
		return severityID;
	}
	
	public void setSeverityID(long severityID) {
		this.severityID = severityID;
	}
	
	public long getDocumentID() {
		return documentID;
	}
	
	public void setDocumentID(long documentID) {
		this.documentID = documentID;
	}

	public void setValidationTypeId(long validationTypeID) {this.validationTypeID = validationTypeID; }

	public void setValidationSeverity(String newValidationSeverity){
	    this.validationSeverity = newValidationSeverity;
    }
    public String getValidationSeverity(){
	    return validationSeverity;
    }
	
    public static QAValidationError fromJSON(JSONObject o) {
        long id = (long) o.get("validationErrorId");
        String message = (String) o.get("validationMessage");
        JSONObject validationObject = (JSONObject)o.get("validationType");
        String type = (String) validationObject.get("validationType");
		String severity = (String) o.get("validationErrorSeverity");
        long rawCreatedDate = (long) o.get("createdDate");
        Timestamp createdDate = new Timestamp(rawCreatedDate);

        return new QAValidationError(id, message, type, createdDate, severity);
//		return new QAValidationError(id, message, createdDate);

	}

	public static QAValidationError fromXML(final Element currentElement) throws ParserConfigurationException, IOException, SAXException {

		//create error to return
		final QAValidationError currentError = new QAValidationError();
		final Element tempElement = (Element)currentElement.getElementsByTagName("validation_type").item(0);
		String validationType = tempElement.getElementsByTagName("validationType").item(0).getTextContent();
		String errorMessage = currentElement.getElementsByTagName("validation_message").item(0).getTextContent();
		String lineNumberString = currentElement.getElementsByTagName("line_number").item(0).getTextContent();
		long lineNumber = !lineNumberString.isEmpty() ? Long.parseLong(lineNumberString) :  0L;
		String validationSeverity = currentElement.getElementsByTagName("validation-error-severity").item(0).getTextContent();
		final Timestamp createdDate = new Timestamp(System.currentTimeMillis());

		currentError.setValidationType(validationType);
		currentError.setValidationMessage(errorMessage);
		currentError.setLineNumber(lineNumber);
		currentError.setValidationSeverity(validationSeverity);
		currentError.setCreatedDate(createdDate);

		return currentError;

//		throw new UnsupportedOperationException();

	}

	public static Map<Integer, Map<ErrorTypes, SeverityTypes>> getErrorMapFromNodeList(NodeList nl){
        String newXPathExpression = "//Validation-errors/validation_error[validation_type[not(validationType = following::validation_type/validationType)]]/validation_type/validationType";

		//for()
		return null;
	}
    public static List<QAValidationError> listFromJSONArray(JSONArray a) {
        List<QAValidationError> errors = new ArrayList<>();
        Iterator<?> iterator = a.iterator();

        a.stream().forEach(item -> {
        	JSONObject element = (JSONObject) item;
        	errors.add(QAValidationError.fromJSON(element));
		});

        while(iterator.hasNext()) {
            JSONObject element = (JSONObject) iterator.next();
            errors.add(QAValidationError.fromJSON(element));
        }
        return errors;
    }

	@Override
	public String toString()
	{
		return "Error ID: " + validationErrorID + "\n" +
		"Type: " + validationType + "\n" +
		"Message: " + validationMessage + "\n" +
		"Severity: " + validationSeverity + "\n" +
		"Line Number " + lineNumber + "\n";


	}

	public String print()
	{
		return "Error ID: " + validationErrorID + "\n" +
				"Type: " + validationType + "\n" +
				"Message: " + validationMessage + "\n" +
				"Severity: " + validationSeverity + "\n" +
				"Line Number: " + lineNumber + "\n";


	}

//    public void setValidationTypeId(long validationTypeID) {
//    }
}
