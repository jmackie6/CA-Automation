package org.lds.cm.content.automation.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lds.cm.content.automation.model.QALanguage;
import org.lds.cm.content.automation.util.JDBCUtils;

public class QALanguageService {
	private static Map<Integer, QALanguage> languageCodeMap = new HashMap<> ();
	private static Map<String, QALanguage> languageIsoMap = new HashMap<> ();
	
	public static List<QALanguage>  getAllLanguages() throws SQLException, IOException {
		String sql = "SELECT * FROM LANGUAGE ORDER BY LANGUAGE_CODE";
		//String sql = "SELECT * FROM LANGUAGE where ROWNUM <=100 ORDER BY LANGUAGE_CODE";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs);
	}
	
	public static QALanguage findLangByISO(String isoPart3Code) throws SQLException, IOException {
		if (languageIsoMap.containsKey(isoPart3Code)) {
			return languageIsoMap.get(isoPart3Code);
		}
		
		String sql = "select ISO_LANG_CD_PART3, LANGUAGE_CODE, LANG_NAME from language WHERE ISO_LANG_CD_PART3 = "
				+ "'" + isoPart3Code + "'";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		QALanguage lang = listFromResultSet(rs).get(0);
		languageIsoMap.put(lang.getIsoPart3Code(), lang);
		
			  return lang;
			

	}

	public static QALanguage findLangByLangCode(int languageCode) throws SQLException, IOException {
		if (languageCodeMap.containsKey(languageCode)) {
			return languageCodeMap.get(languageCode);
		}
		
		String sql = "SELECT ISO_LANG_CD_PART3, LANGUAGE_CODE, LANG_NAME FROM LANGUAGE WHERE LANGUAGE_CODE = "
				+ "'" + languageCode + "'";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		QALanguage lang = listFromResultSet(rs).get(0);
		
		languageCodeMap.put(languageCode, lang);
		
		return lang;
	}
	
	public static QALanguage findLangByLangName(String langName) throws SQLException, IOException {
		String sql = "SELECT ISO_LANG_CD_PART3, LANGUAGE_CODE, LANG_NAME FROM LANGUAGE WHERE LANG_NAME = "
				+ "'" + langName + "'";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs).get(0);
	}
	
	private static List<QALanguage> listFromResultSet(ResultSet rs) throws SQLException, IOException {
		List<QALanguage> documents = new ArrayList<>();
		while ((rs.next())) {
			QALanguage doc = new QALanguage();
			
			doc.setIsoPart3Code(rs.getString("ISO_LANG_CD_PART3"));
			doc.setLanguageCode(rs.getInt("LANGUAGE_CODE"));
			doc.setLangName(rs.getString("LANG_NAME"));
	
			documents.add(doc);
		}
		
		rs.close();
		return documents;
	}
	
}
