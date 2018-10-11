package org.lds.cm.content.automation.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lds.cm.content.automation.model.QAValidationError;
import org.lds.cm.content.automation.util.JDBCUtils;

public class QAValidationErrorService {
	
	public static List<QAValidationError> getAllValidationErrors() throws SQLException, IOException {
		String sql = "SELECT * FROM VALIDATION_ERROR";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs);
	}


	public static List<QAValidationError> fromValidationErrorID(long validationErrorID) throws SQLException, IOException {
		String sql = "SELECT VALIDATION_ERROR_ID FROM VALIDATION_ERROR";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs);
	}

	public static List<QAValidationError> fromValidationType(long validationType) throws SQLException, IOException {
		String sql = "SELECT VALIDATION_TYPE FROM VALIDATION_ERROR";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs);
	}

	public static List<QAValidationError> fromCreatedDate(long createdDate) throws SQLException, IOException {
		String sql = "SELECT CREATED_DATE FROM VALIDATION_ERROR";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs);
	}

	public static List<QAValidationError> fromSeverityID(long severityID) throws SQLException, IOException {
		String sql = "SELECT SEVERITY_ID FROM VALIDATION_ERROR";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs);
	}

	public static List<QAValidationError> fromDocumentID(long documentID) throws SQLException, IOException {
		String sql = "SELECT DOCUMENT_ID FROM VALIDATION_ERROR";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs);
	}

	private static List<QAValidationError> listFromResultSet(ResultSet rs) throws SQLException, IOException {
		List<QAValidationError> documents = new ArrayList<>();
		while ((rs.next())) {
			QAValidationError doc = new QAValidationError();

			doc.setValidationErrorID(rs.getLong("VALIDATION_ERROR_ID"));
			doc.setValidationType(rs.getString("VALIDATION_TYPE"));
			doc.setValidationMessage(rs.getString("VALIDATION_MESSAGE"));
			doc.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
			doc.setLineNumber(rs.getLong("LINE_NUMBER"));
			doc.setSeverityID(rs.getLong("SEVERITY_ID"));
			doc.setDocumentID(rs.getLong("DOCUMENT_ID"));

			documents.add(doc);
		}
		return documents;
	}

}
