package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetPrintCss extends API {
    public GetPrintCss() throws SQLException {
        super("ws/v1/getPrintCss", ControllerType.PREVIEWAPI, "?cssId=16");
    }
}
