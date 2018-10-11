package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class ContentTypes extends API {
    public ContentTypes() throws SQLException {
        super("ws/v1/contentTypes", ControllerType.PUBLICAPI, "");
    }
}
