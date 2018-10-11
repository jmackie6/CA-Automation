package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class ValidateHTML5 extends API {
    public ValidateHTML5() throws SQLException {
        super("ws/v1/validateHtml5", ControllerType.PUBLICAPI, "");
    }
}
