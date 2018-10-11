package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetLanguage extends API {
    public GetLanguage() throws SQLException {
        super("ws/v1/getLanguageXml", ControllerType.PUBLICAPI, "?isoCodes=0,2,059,173");
    }
}
