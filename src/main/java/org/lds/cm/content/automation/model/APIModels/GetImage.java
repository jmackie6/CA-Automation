package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetImage extends API {
    public GetImage() throws SQLException {
        super("ws/v1/getImage", ControllerType.PUBLICAPI, "?mediaId=1255597");
    }
}
