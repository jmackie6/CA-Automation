package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class DocumentContentTypes extends API {
    public DocumentContentTypes() throws SQLException {
        super("ws/v1/documentContentTypes", ControllerType.PUBLICAPI, "");
    }
}
