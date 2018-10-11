package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class PreviewFile extends API {
    public PreviewFile() throws SQLException {
        super("ws/v1/previewFile", ControllerType.PREVIEWAPI, "?guid=b713ea17-049a-4ae3-9449-e20178bb1919");
    }
}
