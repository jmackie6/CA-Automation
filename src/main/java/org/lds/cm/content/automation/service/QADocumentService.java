package org.lds.cm.content.automation.service;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.lds.cm.content.automation.enums.DocumentSource;
import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.model.ReturnObj;
import org.testng.Assert;

import javax.print.Doc;

public class QADocumentService {
    // Connect to Database
    private static Connection conn = null;
    private static ReturnObj returnObj = new ReturnObj();

    public static QAHtml5Document fromDocumentId(long documentId) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            //String sql = "SELECT d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob FROM document d WHERE document_id = " + documentId;
            final String sql = "SELECT d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob FROM document d WHERE document_id = (?)";
            //ResultSet rs = JDBCUtils.getResultSet(sql);
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, documentId);
            rs = stmt.executeQuery();
            return listFromResultSet(rs).get(0);
        } finally {
            if(stmt!= null)
                stmt.close();
            if(rs!= null)
                rs.close();
        }
    }

    public static List<QAHtml5Document> fromDataAid(String dataAid) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where DATA_AID = " + "'" + dataAid + "'";
        final String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where DATA_AID = (?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, dataAid);
        ResultSet rs = stmt.executeQuery();
        List<QAHtml5Document> docList = listFromResultSet(rs);
        stmt.close();
        rs.close();
        return docList;
    }

    /**
     * Checks the database using getDocumentSource() for the source of the given document and compares it to the
     * expected document source
     *
     * @param fileId
     * @param expectedSource
     * @param sandbox True if it is a sandbox transform.
     *                Tells it to check the document_sandbox table or the document table
     * @throws SQLException
     */
    public static void verifySourceByFileId(String fileId, DocumentSource expectedSource, boolean sandbox) throws SQLException {

        String source = getDocumentSource(fileId, sandbox);

        System.out.println("\nExpected source: " + expectedSource);
        System.out.println("Actual source: " + source + "\n");

        if (source == null) {
            Assert.fail("Document not found in doc table");
        } else {
            Assert.assertTrue(source.equals(expectedSource.toString()), "Incorrect source for " + fileId);
        }
    }

    /**
     * Queries the document or document_sandbox table for the source of the document with the matching fileId
     * @param fileId
     * @param sandbox True if it is a sandbox transform.
     *                Tells it to check the document_sandbox table or the document table
     * @return document_source value as it is in the document or document_sandbox table
     * @throws SQLException
     */
    public static String getDocumentSource(String fileId, boolean sandbox) throws SQLException {
        String source;

        ResultSet results;
        if (sandbox) {
            results = JDBCUtils.getResultSet("SELECT SOURCE FROM DOCUMENT_SANDBOX WHERE FILE_ID ='" + fileId + "'");
        } else {
            results = JDBCUtils.getResultSet("SELECT SOURCE FROM DOCUMENT WHERE FILE_ID ='" + fileId + "'");
        }

        if (results.next()) {
            source = results.getNString("SOURCE");
        } else {
            source = null;
        }

        return source;
    }

    /**
     * The number you supply will be a percent of total documents you want returned. If there are 500,000 total documents, and you
     * pass in 10 as the samplePercent, you'll get up to 50,000 documents back.
     *
     * @param samplePercent
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static List<QAHtml5Document> getSampleDocuments(double samplePercent) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        final String sql = "select document.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT sample (?)"; // It is not working for me

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, samplePercent);
        ResultSet rs = stmt.executeQuery();
        List<QAHtml5Document> docList = listFromResultSet(rs);
        stmt.close();
        rs.close();
        return docList;
    }

    public static List<QAHtml5Document> getSampleDocumentsByState(double samplePercent, String documentStateIds) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //		String sql = "select document.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT sample(" + samplePercent + ") where document_state_id in " + documentStateIds;
        String sql = "select document.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT sample ? where document_state_id in ?";  // it is  not working

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, samplePercent);
        stmt.setString(2, documentStateIds);
        ResultSet rs = stmt.executeQuery();
        List<QAHtml5Document> docList = listFromResultSet(rs);
        stmt.close();
        rs.close();
        return docList;
    }

    public static List<QAHtml5Document> getAllDocuments() throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        ResultSet rs = null;
        try {
            final String sql = "select document.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT";
            rs = JDBCUtils.getResultSet(sql);
            return listFromResultSet(rs);
        } finally {
            if(rs!=null)
                rs.close();
        }
    }

    public Long getDocumentCount() throws SQLException {
        conn = JDBCUtils.getConnection();
        final String sql = "SELECT COUNT(*) AS count FROM document";
        Long documentCount = null;

        ResultSet rs = JDBCUtils.getResultSet(sql);
        while (rs.next()) {
            documentCount = rs.getLong("count");
        }

        return documentCount;
    }

    public static Long getDocumentCountByState(String documentStateIds) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "SELECT COUNT(*) AS count FROM document WHERE document_state_id in " + documentStateIds;
        final String sql = "SELECT COUNT(*) AS count FROM document WHERE document_state_id in (?)";

        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(documentStateIds);

        Long documentCount = null;

        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);

        if (returnObj.resultSet != null) {
            while (returnObj.resultSet.next()) {
                documentCount = returnObj.resultSet.getLong("count");
                System.out.println("documentCount : " + documentCount);
            }
        }
        fillInData.clear();
        return documentCount;
    }

    public static QAHtml5Document fromFileId(String fileId) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where FILE_ID = " + "'" + fileId + "'";

        final String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where FILE_ID = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, fileId);

        ResultSet rs = preparedStatement.executeQuery();

        // ResultSet rs = JDBCUtils.getResultSet(sql);
        QAHtml5Document returnDoc = listFromResultSet(rs).get(0);
        preparedStatement.close();
        rs.close();
        return returnDoc;
    }

    public static List<QAHtml5Document> fromFileName(String fileName) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        final String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where FILE_NAME = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, fileName);

        ResultSet rs = preparedStatement.executeQuery();
        //ResultSet rs = JDBCUtils.getResultSet(sql);
        List<QAHtml5Document> returnDocs = listFromResultSet(rs);
        preparedStatement.close();
        rs.close();
        return returnDocs;
    }

    public static List<QAHtml5Document> fromContentTypeAndFileId(String contentType, String fileId) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where CONTENT_TYPE = " + "'" + contentType + "' AND FILE_ID = " + "'" + fileId + "'";
        final String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where CONTENT_TYPE = ? AND FILE_ID = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, contentType);
        preparedStatement.setString(2, fileId);

        ResultSet rs = preparedStatement.executeQuery();
        // ResultSet rs = JDBCUtils.getResultSet(sql);
        List<QAHtml5Document> returnDocs = listFromResultSet(rs);
        preparedStatement.close();
        rs.close();
        return returnDocs;
    }

    public static List<QAHtml5Document> fromURIAndFileId(String uri, String fileId) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        final String sql = "SELECT d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d join Document_URI_VW u on u.document_id = d.document_id  "
                + " where u.uri =  " + "'" + uri + "' and d.file_id =  " + "'" + fileId + "'";
        ResultSet rs = JDBCUtils.getResultSet(sql);
        List<QAHtml5Document> returnDocs = listFromResultSet(rs);
        rs.close();
        return returnDocs;
    }

    public static QAHtml5Document fromURIAndLanguage(String uri, int langId) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            //String sql = "SELECT d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d join Document_URI_VW u on u.document_id = d.document_id  "
            //		+ " where u.uri =  " + "'" + uri + "' and d.language_id =  " + "'" + langId + "'";
            final String sql = "SELECT d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d join Document_URI_VW u on u.document_id = d.document_id  "
                    + " where u.uri =  ? and d.language_id =  ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, uri);
            preparedStatement.setInt(2, langId);

            rs = preparedStatement.executeQuery();
            //    ResultSet rs = JDBCUtils.getResultSet(sql);
            //Added control flow to handle cases where no documents are returned
            if (!rs.isBeforeFirst()) {
                return null;
            } else {
                return listFromResultSet(rs).get(0);
            }
        } finally {
            if(preparedStatement!= null)
                preparedStatement.close();
            if(rs!=null)
                rs.close();
        }
    }

    public static List<QAHtml5Document> fromOwnerAppUserID(Long ownerAppUserID) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where OWNER_APP_USER_ID = " + "'" + ownerAppUserID + "'";
        final String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where OWNER_APP_USER_ID = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setLong(1, ownerAppUserID);

        ResultSet rs = preparedStatement.executeQuery();

        //  ResultSet rs = JDBCUtils.getResultSet(sql);
        List<QAHtml5Document> returnDocs = listFromResultSet(rs);
        preparedStatement.close();
        rs.close();
        return returnDocs;
    }

    public static List<QAHtml5Document> fromMediaXmlID(Long mediaXmlID) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT dwhere MEDIA_XML_ID = " + "'" + mediaXmlID + "'";
        final String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT dwhere MEDIA_XML_ID = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setLong(1, mediaXmlID);

        ResultSet rs = preparedStatement.executeQuery();
        //ResultSet rs = JDBCUtils.getResultSet(sql);
        List<QAHtml5Document> returnDocs = listFromResultSet(rs);
        preparedStatement.close();
        rs.close();
        return returnDocs;
    }

    public static List<QAHtml5Document> fromTransformProcessLogID(Long transformProcessLogID) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where TRANSFORM_PROCESS_LOG_ID = " + "'" + transformProcessLogID + "'";
        final String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where TRANSFORM_PROCESS_LOG_ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setLong(1, transformProcessLogID);

        ResultSet rs = preparedStatement.executeQuery();
        //ResultSet rs = JDBCUtils.getResultSet(sql);
        List<QAHtml5Document> returnDocs = listFromResultSet(rs);
        preparedStatement.close();
        rs.close();
        return returnDocs;
    }

    public static List<QAHtml5Document> fromFolderID(Long folderID) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where FOLDER_ID = " + "'" + folderID + "'";
        final String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where FOLDER_ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setLong(1, folderID);

        ResultSet rs = preparedStatement.executeQuery();

        //ResultSet rs = JDBCUtils.getResultSet(sql);
        List<QAHtml5Document> returnDocs = listFromResultSet(rs);
        preparedStatement.close();
        rs.close();
        return returnDocs;
    }

    public static String getURIFromFileIDLike(String fileIDStart) throws SQLException {
        conn = JDBCUtils.getConnection();
        //String sql = "select v.URI from document_uri_vw v join document d on d.document_id = v.document_id and d.file_id like '" + fileIDStart + "%'";
        final String sql = "select v.URI from document_uri_vw v join document d on d.document_id = v.document_id and d.file_id like (?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, fileIDStart + "%");

        ResultSet rs = preparedStatement.executeQuery();

        //ResultSet rs = JDBCUtils.getResultSet(sql);
        String returning = getURIFromResultSet(rs).get(0);
        preparedStatement.close();
        rs.close();
        return returning;
    }

    public static String getURIFromFileID(String fileID) throws SQLException {
        conn = JDBCUtils.getConnection();
        //String sql = "select v.URI from document_uri_vw v join document d on d.document_id = v.document_id and d.file_id = '" + fileID + "'";
        final String sql = "select v.URI from document_uri_vw v join document d on d.document_id = v.document_id and d.file_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, fileID);

        ResultSet rs = preparedStatement.executeQuery();

        // ResultSet rs = JDBCUtils.getResultSet(sql);
        String returning = getURIFromResultSet(rs).get(0);
        preparedStatement.close();
        rs.close();
        return returning;
    }

    public static String getURIFromFileIDandType(String contentType, String fileId) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        //String sql = "select v.URI from document_uri_vw v join document d on d.document_id = v.document_id where d.CONTENT_TYPE = " + "'" + contentType + "' AND d.FILE_ID = " + "'" + fileId + "'";
        final String sql = "select v.URI from document_uri_vw v join document d on d.document_id = v.document_id where d.CONTENT_TYPE = (?) AND d.FILE_ID = (?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, contentType);
        preparedStatement.setString(2, fileId);

        ResultSet rs = preparedStatement.executeQuery();

        //  ResultSet rs = JDBCUtils.getResultSet(sql);
        String returning = getURIFromResultSet(rs).get(0);
        preparedStatement.close();
        rs.close();
        return returning;
    }

    public static List<String> getURIFromResultSet(ResultSet rs) throws SQLException {
        conn = JDBCUtils.getConnection();
        List<String> documents = new ArrayList<>();
        while (rs.next()) {
            documents.add(rs.getString("URI"));
        }
        rs.close();
        return documents;
    }

    public static String getGuidFromProcessLogId(Long plID) throws SQLException {
        conn = JDBCUtils.getConnection();
        Assert.assertNotNull(plID, "ERROR: Invalid process log ID");
        String guid = "temp";

        String query = "select batch_guid from process_log where process_log_id = " + plID;
        ResultSet rs = JDBCUtils.getResultSet(query);

        while (rs.next()) {
            guid = rs.getString("BATCH_GUID");
            break;
        }
        rs.close();
        return (guid == "temp" ? null : guid);
    }

    private static List<QAHtml5Document> listFromResultSet(ResultSet rs) throws SQLException, IOException {
        conn = JDBCUtils.getConnection();
        List<QAHtml5Document> documents = new ArrayList<>();
        while ((rs.next())) {
            QAHtml5Document doc = new QAHtml5Document();
            doc.setCitation(rs.getString("CITATION"));
            doc.setContentType(rs.getString("CONTENT_TYPE"));
            doc.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
            doc.setDataAid(rs.getString("DATA_AID"));
            doc.setDefaultPreviewFlag(rs.getBoolean("DEFAULT_PREVIEW_FLAG"));
            doc.setDocumentId(rs.getLong("DOCUMENT_ID"));

            Clob temp = rs.getClob("document_xml_clob");
            String clobAsString = JDBCUtils.clobToString(temp);
            doc.setDocumentXML(clobAsString);

            doc.setFileId(rs.getString("FILE_ID"));
            doc.setFileName(rs.getString("FILE_NAME"));
            doc.setFolderID(rs.getLong("FOLDER_ID"));
            doc.setLanguageID(rs.getInt("LANGUAGE_ID"));
            doc.setMediaXmlID(rs.getLong("MEDIA_XML_ID"));
            doc.setModifiedByAppUserID(rs.getLong("MODIFIED_BY_APP_USER_ID"));
            doc.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
            doc.setOwnerAppUserID(rs.getLong("OWNER_APP_USER_ID"));
            doc.setOxygenEditAppUserId(rs.getLong("OXYGEN_EDIT_APP_USER_ID"));
            doc.setOxygenEditDate(rs.getTimestamp("OXYGEN_EDIT_DATE"));
            doc.setDocumentStateID(rs.getInt("DOCUMENT_STATE_ID"));
            doc.setStatusAppUserID(rs.getLong("STATUS_APP_USER_ID"));
            doc.setStatusChangeDate(rs.getTimestamp("STATUS_CHANGE_DATE"));
            doc.setTransformProcessLogID(rs.getLong("TRANSFORM_PROCESS_LOG_ID"));
            doc.setValidated(rs.getBoolean("VALIDATED"));

            documents.add(doc);
        }
        rs.close();
        return documents;
    }

    public static int numApprovedByFileId(String file_id) throws SQLException {

        conn = JDBCUtils.getConnection();
        final String sql = "select count(*) AS rowcount from document where APPROVED = 1 and file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int approvedCount = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Approved Count: " + approvedCount);
        return approvedCount;
    }

    public static int numLockedByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        final String sql = "select count(*) AS rowcount from document where document_state_id = 2 and file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int lockedCount = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Locked Count: " + lockedCount);
        return lockedCount;
    }

    public static int numUnlockedByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        final String sql = "select count(*) AS rowcount from document where document_state_id = 3 and file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int unlockedCount = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Unlocked Count: " + unlockedCount);

        return unlockedCount;
    }

    public static int numAddedByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        final String sql = "select count(*) AS rowcount from document where document_state_id = 1 and file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int unlockedCount = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Added Count: " + unlockedCount);

        return unlockedCount;
    }

    public static int numValidatedByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        final String sql = "select count(*) AS rowcount from document where validated = 1 and file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int validatedCount = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Validated Count: " + validatedCount);
        return validatedCount;
    }

    public static int numPublishedByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        final String sql = "select count(*) AS rowcount from CONTENT_AUTO.CONTENT_PUBLISH where DOCUMENT_ID "
                + "in (select document_id from document where file_id like (?)) and status = 'COMPLETE'";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int numPublished = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Published Count: " + numPublished);
        return numPublished;
    }

    public static int numValidationErrorsByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();

        final String sql = "select count(*) AS rowcount from validation_error where document_id "
                + "in (select document_id from document where file_id like (?))";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int validatedErrorsNum = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Validation Error Count: " + validatedErrorsNum);
        return validatedErrorsNum;
    }

    public static int numUpdatedGroupByFileIdConference(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String sql = "select count(*) AS rowcount from CONTENT_AUTO.DOCUMENT_CONTENT_GROUP "
                + "where DOCUMENT_ID in (select document_id from document where file_id like (?)) and CONTENT_GROUP_ID = '1'";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int groupNum = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Number of documents with general conference group: " + groupNum);
        return groupNum;
    }

    public static int numUpdatedGroupByFileIdLiahona(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String sql = "select count(*) AS rowcount from CONTENT_AUTO.DOCUMENT_CONTENT_GROUP where DOCUMENT_ID in"
                + " (select document_id from document where file_id like (?)) and CONTENT_GROUP_ID = '3'";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, true);
        returnObj.resultSet.next();
        int groupNumLiahona = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();
        System.out.println("Number of documents with liahona group: " + groupNumLiahona);
        return groupNumLiahona;
    }

    public static int lockByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        final String updateQuery = "update document set document_state_id = '2' where file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("Unlocked: " + update);
        return update;
    }

    public static int unlockByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "update document set document_state_id = '3' where file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("Unlocked: " + update);
        return update;
    }

    public static int addedByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "update document set document_state_id = '1' where file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("Added: " + update);
        return update;
    }

    public static int validatedByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "update document set validated = '1' where file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("Validated: " + update);
        return update;
    }

    public static int unvalidatedByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "update document set validated = '0' where file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("Unvalidated: " + update);
        return update;
    }

    public static int approveByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "update document set APPROVED = '1' where file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("Approved: " + update);
        return update;
    }

    public static int unapproveByFileId(String file_id) throws SQLException, InterruptedException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "update document set APPROVED = '0' where file_id like (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("Unapproved: " + update);
        return update;
    }

    public static int updateGroupByFileIdGeneralConference(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "update CONTENT_AUTO.DOCUMENT_CONTENT_GROUP set content_group_id = 1 where DOCUMENT_ID "
                + "in (select document_id from document where file_id like (?))";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("GroupUpdate for Conference: " + update);
        return update;
    }

    public static int updateGroupByFileIdLiahona(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "update CONTENT_AUTO.DOCUMENT_CONTENT_GROUP set content_group_id = 3 "
                + "where DOCUMENT_ID in (select document_id from document where file_id like (?))";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("GroupUpdate for Conference: " + update);
        return update;
    }

    public static int deleteValidationErrorsByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "delete from validation_error where document_id in (select document_id from document where file_id like (?))";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, true);
        int update = returnObj.intVal;
        System.out.println("Validation Errors Deleted: " + update);
        return update;
    }

    public static int deleteFatalValidateErrorsByFileId(String file_id) throws SQLException {
        conn = JDBCUtils.getConnection();
        String updateQuery = "delete from validation_error where document_id in (select document_id from document where file_id like (?)) and severity_id = 4";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        PreparedStatement stmt = conn.prepareStatement(updateQuery);
        stmt.setString(1, file_id + "%");
        int update = stmt.executeUpdate();
        System.out.println("Fatal Validation Errors Deleted: " + update);
        return update;
    }
}
