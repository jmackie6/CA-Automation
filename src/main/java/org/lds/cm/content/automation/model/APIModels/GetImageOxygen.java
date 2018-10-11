package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetImageOxygen extends API {
    public GetImageOxygen() throws SQLException {
        super("ws/v1/getImageOxygen", ControllerType.PUBLICAPI, "");
    }
}
