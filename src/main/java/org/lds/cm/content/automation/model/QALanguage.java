package org.lds.cm.content.automation.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.lds.cm.content.automation.util.JDBCUtils;

public class QALanguage {
	
	private String isoPart3Code;
	private int languageCode;
	private String langName;
   

	public String getIsoPart3Code() {
		return isoPart3Code;
	}
	public void setIsoPart3Code(String isoPart3Code) {
		this.isoPart3Code = isoPart3Code;
	}
	public String getLanguageCode() {
		return String.format("%03d", languageCode);	
		//return languageCode;
	}
	public void setLanguageCode(int languageCode) {
		this.languageCode = languageCode;
	}
	public String getLangName() {
		return langName;
	}
	public void setLangName(String langName) {
		this.langName = langName;
	}
	
    private static final Map<Integer, QALanguage> CONTENT_CENTRAL_ID_MAP = new HashMap<>();
    private static final Map<String, QALanguage> ISO3_ALPHA_CODE_MAP = new HashMap<>();
    static {
        try {
            ResultSet rs = JDBCUtils.getResultSet("select * from language");
            while(rs.next()) {
                String iso3AlphaCode = rs.getString("iso_lang_cd_alpha3");
                int contentCentralID = rs.getInt("language_code");
                QALanguage language = new QALanguage();
                ISO3_ALPHA_CODE_MAP.put(iso3AlphaCode, language);
                CONTENT_CENTRAL_ID_MAP.put(contentCentralID, language);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to fetch languages from the database: " + e.getMessage());
        }
    }

    public static QALanguage fromContentCentralID(int id) {
        return CONTENT_CENTRAL_ID_MAP.get(id);
    }

	

}
