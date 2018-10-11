package Interfaces;


import com.sun.jna.platform.win32.Sspi;
import org.lds.cm.content.automation.model.QAValidationError;

import java.sql.Timestamp;

public class QAValidationErrorBuilderImpl implements QAValidationErrorBuilder{

    private QAValidationError error;

    public QAValidationErrorBuilderImpl() {
        this.error = new QAValidationError();
    }

    @Override
    public QAValidationError build() {
        return error;
    }

    @Override
    public QAValidationErrorBuilder setValidationErrorID(long validationErrorID) {
        error.setValidationErrorID(validationErrorID);
        return this;
    }

    @Override
    public QAValidationErrorBuilder setValidationType(String validationType ) {
        error.setValidationType(validationType);
        return this;
    }

    @Override
    public QAValidationErrorBuilder setValidationMessage(String validationType) {
        error.setValidationMessage(validationType);
        return this;
    }

    @Override
    public QAValidationErrorBuilder setCreatedDate(Timestamp createdDate) {
        error.setCreatedDate(createdDate);
        return this;
    }

    @Override
    public QAValidationErrorBuilder setLineNumber(long lineNumber) {
        error.setLineNumber(lineNumber);
        return this;
    }

    @Override
    public QAValidationErrorBuilder setSeverityID(long severityID) {
        error.setSeverityID(severityID);
        return this;
    }

    @Override
    public QAValidationErrorBuilder setDocumentID(long documentID) {
        error.setDocumentID(documentID);
        return this;
    }

    @Override
    public QAValidationErrorBuilder setValidationSeverity(String validationSeverity) {
        error.setValidationSeverity(validationSeverity);
        return this;
    }

    @Override
    public QAValidationErrorBuilder setValidationTypeID(long validationTypeID) {
        error.setValidationTypeId(validationTypeID);
        return this;
    }
}
