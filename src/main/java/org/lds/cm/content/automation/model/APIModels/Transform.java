package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class Transform extends API {
    public Transform() throws SQLException {
        super("transform", ControllerType.TRANSFORMAPI, "");
    }
}
