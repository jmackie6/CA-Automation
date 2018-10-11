package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetAllCss extends API {
    public GetAllCss() throws SQLException {
        super("ws/v1/getAllCss", ControllerType.PREVIEWAPI, "");
    }
}
