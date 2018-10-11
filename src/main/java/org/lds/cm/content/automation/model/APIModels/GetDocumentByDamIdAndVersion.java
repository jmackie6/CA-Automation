package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

/**
 * Model API for the getDocumentByDamIdAndVersion endpoint
 */
public class GetDocumentByDamIdAndVersion extends API {
    public GetDocumentByDamIdAndVersion() throws SQLException {
        super("getDocumentByDamIdAndVersion", ControllerType.PREVIEWAPI, "?damId=3461c5f947b43c7bd81181a0f46adf41ff82edd7&version=1");
    }
}
