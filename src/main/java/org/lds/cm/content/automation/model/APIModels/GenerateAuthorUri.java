package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

/**
 * Model API for the generateAuthorUri endpoint
 */
public class GenerateAuthorUri extends API {
    public GenerateAuthorUri() throws SQLException {
        super("generateAuthorUri", ControllerType.PREVIEWAPI, "?authorName=Jose%20Smith");
    }
}
