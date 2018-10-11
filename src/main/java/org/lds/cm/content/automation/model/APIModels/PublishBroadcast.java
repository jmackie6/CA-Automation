package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class PublishBroadcast extends API {
    public PublishBroadcast() throws SQLException {
        super("ws/v1/publishBroadcast", ControllerType.PUBLICAPI, "");
    }
}
