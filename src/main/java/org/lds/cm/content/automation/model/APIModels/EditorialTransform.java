package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class EditorialTransform extends API{

    public EditorialTransform() throws SQLException {
        super("editorial/transform", ControllerType.TRANSFORMAPI, "");
    }
}
