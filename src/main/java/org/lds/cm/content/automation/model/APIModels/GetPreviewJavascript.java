package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetPreviewJavascript extends API {
    public GetPreviewJavascript() throws SQLException {
        super("ws/v1getPreviewJavascript", ControllerType.PREVIEWAPI, "?cssId=5");
    }
}
