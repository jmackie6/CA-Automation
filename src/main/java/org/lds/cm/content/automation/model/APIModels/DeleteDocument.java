package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

/**
 * Model API for the deleteDocument endpoint
 */
public class DeleteDocument extends API {
    public DeleteDocument() throws SQLException {
        super("ws/v1/deleteDocument", ControllerType.TRANSFORMAPI, "?docId=1115659");
    }
}
