package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class PreviewFileByUriAndLanguage extends API {
    public PreviewFileByUriAndLanguage() throws SQLException {
        super("ws/v1/previewFileByUriAndLanguage", ControllerType.PREVIEWAPI, "?lang=eng&uri=/youth/learn/yw/self-reliance/converted");
    }
}
