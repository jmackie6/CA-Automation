package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetDocByDAMId extends API {
    public GetDocByDAMId() throws SQLException {
        super("ws/v1/getDocumentByDamIdAndVersion", ControllerType.PUBLICAPI, "?damId=81f4cb91e583010c07b309e1f38873d4bb56c83a&version=1");
    }
}
