package org.lds.cm.content.automation.service;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.lds.cm.content.automation.enums.MarkLogicDatabase;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.MarkLogicUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;

public class QACrossRefService {
	
	@Inject
	private NamedParameterJdbcTemplate npJdbcTemplate;

	
	/*Deleting
	 Has to be done on the source and the destination*/

	/** Pretty sure that nothing is actually returned in the result set.  If it needs to be verified, add the verification later. */
	// Delete documents from Oracle
	
		public static void deleteFromValidationError(String file_id) throws SQLException {
			final String sql = "delete from validation_error where document_id in (select document_id from document where file_id = ?";
			ArrayList<String> fillIn = new ArrayList<>();
			fillIn.add(file_id);
			ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
			rs.close();
			return;
		}	
	
		public static void deleteFromPublishValidationError(String file_id) throws SQLException {
			final String sql = "delete from publish_validation_error where content_publish_id in "
					+ "(select content_publish_id from content_publish where document_id in "
					+ "(select document_id from document where file_id = ?";
			ArrayList<String> fillIn = new ArrayList<>();
			fillIn.add(file_id);
			ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
			rs.close();
			return;
		}
		
		public static void deleteFromContentPublish(String file_id) throws SQLException {
			final String sql = "delete from content_publish where document_id in (select document_id from document where file_id = ?";
			ArrayList<String> fillIn = new ArrayList<>();
			fillIn.add(file_id);
			ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
			rs.close();
			return;
		}
		
		public static void deleteFromDocumentContentGroup(String file_id) throws SQLException {
			final String sql = "delete from document_content_group where document_id in (select document_id from document where file_id = ?";
			ArrayList<String> fillIn = new ArrayList<>();
			fillIn.add(file_id);
			ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
			rs.close();
			return;
		}
		
		// Delete the documents and then commit
		public static void deleteFromDocument(String file_id) throws SQLException {
			String sql = "delete from document where document_id in (select document_id from document where file_id = ?";
			ArrayList<String> fillIn = new ArrayList<>();
			fillIn.add(file_id);
			ResultSet rs = JDBCUtils.getResultSet(sql);
			rs.close();
			return;
		}

	
		// Delete doc-map 
		
		public static void deleteDocMap(String dataAid) {
			QADocMapService.deleteDocMap(dataAid);
		}
		
		// Delete URI-MAPPING from ML
		
		
	public static void verifyFilesInURIMapping (String fileName) throws IOException {
		String pathToCheck = MarkLogicDatabase.URI_MAPPING.getContentRoot()  + fileName;
		System.out.println("pathToCheck is: " + pathToCheck);
	
		File tempFile = MarkLogicUtils.readFile(pathToCheck);
		String fileContents = org.apache.commons.io.FileUtils.readFileToString(tempFile);
		//printDebug(fileContents);
		
		Assert.assertTrue(StringUtils.isNotEmpty(fileContents));
	
		// second try, call utility method to check for existence
	
		Assert.assertTrue(MarkLogicUtils.docExists(pathToCheck), "Call to ML.docExists()");
	}
		
	
	public static void deleteURIMapping(String fileName) {
		String URIPath = MarkLogicDatabase.URI_MAPPING.getContentRoot()  + fileName;
		String PublishedPath = MarkLogicDatabase.PUBLISHED.getContentRoot()  + fileName;
	
		// use this to delete with MarkLogic Java API, this does not delete folders, only files 
		MarkLogicUtils.deleteFileFromMarkLogic(MarkLogicDatabase.URI_MAPPING, URIPath);
		MarkLogicUtils.deleteFileFromMarkLogic(MarkLogicDatabase.PUBLISHED, PublishedPath);
	}
	
	//4. Delete annotations

	public static void deleteAnnotations(String string) {
		// TODO Auto-generated method stub
		
	}
	
	 public int lockContent (String uriPattern, List<String> languages, Long appUserId) {
			StringBuilder sql = new StringBuilder("update document set");
			sql.append(" status = 'LOCKED', status_change_date = sysdate, status_app_user_id = :sourceLockUser,");
			sql.append(" modified_date = sysdate, modified_by_app_user_id = :modifiedUser");
			sql.append(" where document_id in (");
			sql.append(" select doc.document_id from document doc, language lang, document_uri_vw dv");
			sql.append(" where doc.language_id = lang.language_code");
			sql.append(" and doc.document_id = dv.document_id");
			sql.append(" and lang.ISO_LANG_CD_ALPHA3 in (:languages)");
			sql.append(" and dv.uri like :uriPattern)");
			
			MapSqlParameterSource sqlParametersMap = new MapSqlParameterSource();
			sqlParametersMap.addValue("sourceLockUser", appUserId);
			sqlParametersMap.addValue("modifiedUser", appUserId);
			sqlParametersMap.addValue("languages", languages);
			sqlParametersMap.addValue("uriPattern", uriPattern);
			
			int rowsUpdated = npJdbcTemplate.update(sql.toString(), sqlParametersMap);
			
			return rowsUpdated;
		}
	 
	 public int bulkLockContent (String uriPattern, List<String> languages, Long appUserId) {
			StringBuilder sql = new StringBuilder("update document set");
			sql.append(" status = 'LOCKED', status_change_date = sysdate, status_app_user_id = :sourceLockUser,");
			sql.append(" modified_date = sysdate, modified_by_app_user_id = :modifiedUser");
			sql.append(" where document_id in (");
			sql.append(" select doc.document_id from document doc, language lang, document_uri_vw dv");
			sql.append(" where doc.language_id = lang.language_code");
			sql.append(" and doc.document_id = dv.document_id");
			sql.append(" and lang.ISO_LANG_CD_ALPHA3 in (:languages)");
			sql.append(" and dv.uri like :uriPattern)");
			
			MapSqlParameterSource sqlParametersMap = new MapSqlParameterSource();
			sqlParametersMap.addValue("sourceLockUser", appUserId);
			sqlParametersMap.addValue("modifiedUser", appUserId);
			sqlParametersMap.addValue("languages", languages);
			sqlParametersMap.addValue("uriPattern", uriPattern);
			
			int rowsUpdated = npJdbcTemplate.update(sql.toString(), sqlParametersMap);
			
			return rowsUpdated;
		}
	 
	 public int unlockContent (String uriPattern, List<String> languages, Long appUserId) {
			StringBuilder sql = new StringBuilder("update document set");
			sql.append(" status = 'VALIDATED', status_change_date = sysdate, status_app_user_id = :sourceLockUser,");
			sql.append(" modified_date = sysdate, modified_by_app_user_id = :modifiedUser");
			sql.append(" where document_id in (");
			sql.append(" select doc.document_id from document doc, language lang, document_uri_vw dv");
			sql.append(" where doc.language_id = lang.language_code");
			sql.append(" and doc.document_id = dv.document_id");
			sql.append(" and lang.ISO_LANG_CD_ALPHA3 in (:languages)");
			sql.append(" and dv.uri like :uriPattern)");
			
			MapSqlParameterSource sqlParametersMap = new MapSqlParameterSource();
			sqlParametersMap.addValue("sourceLockUser", appUserId);
			sqlParametersMap.addValue("modifiedUser", appUserId);
			sqlParametersMap.addValue("languages", languages);
			sqlParametersMap.addValue("uriPattern", uriPattern);
			
			int rowsUpdated = npJdbcTemplate.update(sql.toString(), sqlParametersMap);
			
			return rowsUpdated;
		}
	 
	 public int bulkUnlockContent (String uriPattern, List<String> languages, Long appUserId) {
			StringBuilder sql = new StringBuilder("update document set");
			sql.append(" status = 'VALIDATED', status_change_date = sysdate, status_app_user_id = :sourceLockUser,");
			sql.append(" modified_date = sysdate, modified_by_app_user_id = :modifiedUser");
			sql.append(" where document_id in (");
			sql.append(" select doc.document_id from document doc, language lang, document_uri_vw dv");
			sql.append(" where doc.language_id = lang.language_code");
			sql.append(" and doc.document_id = dv.document_id");
			sql.append(" and lang.ISO_LANG_CD_ALPHA3 in (:languages)");
			sql.append(" and dv.uri like :uriPattern)");
			
			MapSqlParameterSource sqlParametersMap = new MapSqlParameterSource();
			sqlParametersMap.addValue("sourceLockUser", appUserId);
			sqlParametersMap.addValue("modifiedUser", appUserId);
			sqlParametersMap.addValue("languages", languages);
			sqlParametersMap.addValue("uriPattern", uriPattern);
			
			int rowsUpdated = npJdbcTemplate.update(sql.toString(), sqlParametersMap);
			
			return rowsUpdated;
		}

}
