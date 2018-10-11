package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;


/**
 * Model API for the generateBroadcastFileId endpoint
 */
public class GenerateBroadcastFileId extends API {
    public GenerateBroadcastFileId() throws SQLException {
        super("generateBroadcastFileId", ControllerType.PREVIEWAPI, "?pdNumber=PD234567&ldsLang=000&fileName=11andersen.html");
    }
}