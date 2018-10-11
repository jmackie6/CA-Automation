package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

/**
 * Model API for the generateBroadcastUri endpoint
 */
public class GenerateBroadcastUri extends API {
    public GenerateBroadcastUri() throws SQLException {
        super("generateBroadcastUri", ControllerType.PREVIEWAPI, "?category=Genreral_Conference&year=1991&month=08&session=saturday_morning&speakerId=23&speakerSurname=Uchtdorf");
    }
}