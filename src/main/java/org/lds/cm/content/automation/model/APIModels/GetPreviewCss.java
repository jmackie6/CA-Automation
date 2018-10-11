package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetPreviewCss extends API {
    public GetPreviewCss() throws SQLException {
        super("ws/v1/getPreviewCss", ControllerType.PREVIEWAPI, "?cssId=5");
    }
}
