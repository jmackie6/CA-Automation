package org.lds.cm.content.automation.model;

import org.lds.cm.content.automation.enums.ProcessStatus;
import org.lds.cm.content.automation.util.JDBCUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class QAProcessLog {

    private BigDecimal processLogId;
    private String processName;
    private ProcessStatus processStatus;
    private String note;
    private String fileName;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp modifiedDate;
    private BigDecimal createdByAppUserId;
    private String batchGuid;
    private String source;

    public BigDecimal getProcessLogId() {
        return processLogId;
    }

    public void setProcessLogId(BigDecimal processLogId) {
        this.processLogId = processLogId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate)
    {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate)
    {
        this.endDate = endDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setCreatedByAppUserId(BigDecimal createdByAppUserId) {
        this.createdByAppUserId = createdByAppUserId;
    }

    public BigDecimal getCreatedByAppUserId() {
        return createdByAppUserId;
    }

    public String getBatchGuid() {
        return batchGuid;
    }

    public void setBatchGuid(String batchGuid) {
        this.batchGuid = batchGuid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }


    public static QAProcessLog getLogFromID(long logID) throws SQLException{
        String processLogSql = "SELECT pl.PROCESS_LOG_ID"
        + " , pl.PROCESS_NAME "
        + " , pl.STATUS "
        + " , pl.NOTE "
        + " , pl.FILE_NAME "
        + " , pl.START_DATE "
        + " , pl.END_DATE "
        + " , pl.MODIFIED_DATE "
        + " , pl.CREATED_BY_APP_USER_ID "
        + " , pl.BATCH_GUID "
        + " , pl.SOURCE "
        + " FROM PROCESS_LOG pl "
        + " WHERE pl.PROCESS_LOG_ID=" + String.valueOf(logID);
        final ResultSet rs = JDBCUtils.getResultSet(processLogSql);
        final QAProcessLog returnLog = new QAProcessLog();
        boolean returnObj = false;

        if(rs.next()){
            returnLog.setProcessLogId(rs.getBigDecimal("PROCESS_LOG_ID"));
            returnLog.setProcessName(rs.getString("PROCESS_NAME"));
            returnLog.setProcessStatus(ProcessStatus.getFromString(rs.getString("STATUS")));
            returnLog.setNote(rs.getString("NOTE"));
            returnLog.setFileName(rs.getString("FILE_NAME"));
            returnLog.setStartDate(rs.getTimestamp("START_DATE"));
            returnLog.setEndDate(rs.getTimestamp("END_DATE"));
            returnLog.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
            returnLog.setCreatedByAppUserId(rs.getBigDecimal("BATCH_GUID"));
            returnLog.setSource(rs.getString("SOURCE"));
            returnObj = true;
        }

        return returnObj == true ? returnLog : null;

    }

    public boolean wasSuccessfullyPublished(){
        if(processStatus.equals("SUCCESSFUL")){
           return true;
        }

        return false;
    }

}
