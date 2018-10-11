package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class UpdateDocumentMedia extends API {
    public UpdateDocumentMedia() throws SQLException {
        super("ws/v1/updateDocumentMedia", ControllerType.PUBLICAPI, "");
    }
}
