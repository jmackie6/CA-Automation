package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class SandboxTransform extends API {
    public SandboxTransform() throws SQLException {
        super("ws/v1/sandbox/transform", ControllerType.TRANSFORMAPI, "");
    }
}
