package Interfaces;

import org.lds.cm.content.automation.model.QAValidationError;

import java.sql.Timestamp;

public interface QAValidationErrorBuilder {

     QAValidationError build();

     QAValidationErrorBuilder setValidationErrorID(long validationErrorID);

     QAValidationErrorBuilder setValidationType(String validationType);
     QAValidationErrorBuilder setValidationMessage(String validationMessage);
     QAValidationErrorBuilder setCreatedDate(Timestamp dateTime);
     QAValidationErrorBuilder setLineNumber(long lineNumber);
     QAValidationErrorBuilder setSeverityID(long severityID);
     QAValidationErrorBuilder setDocumentID(long documentID);
     QAValidationErrorBuilder setValidationSeverity(String validationSeverity);
     QAValidationErrorBuilder setValidationTypeID(long validationTypeID);




}
