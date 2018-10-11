package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetPrintJavascript extends API {
    public GetPrintJavascript() throws SQLException {
        super("ws/v1/getPrintJavascript", ControllerType.PUBLICAPI, "?cssId=5");
    }
}
