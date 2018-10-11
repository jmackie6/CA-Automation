package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class PreviewFileByUri extends API {
    public PreviewFileByUri() throws SQLException {
        super("ws/v1/previewFileByUri", ControllerType.PREVIEWAPI, "?cssId=1&languageId=0&uri=/youth/learn/yw/self-reliance/converted");
    }
}
