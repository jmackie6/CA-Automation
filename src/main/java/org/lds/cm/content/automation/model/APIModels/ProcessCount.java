package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class ProcessCount extends API {
    public ProcessCount() throws SQLException {
        super("ws/v1/processCount", ControllerType.PUBLICAPI, "");
    }
}
