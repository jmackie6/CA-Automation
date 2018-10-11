package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class SandboxPreviewFile extends API {
    public SandboxPreviewFile() throws SQLException {
        super("ws/v1/sandbox/previewFile", ControllerType.PREVIEWAPI, "?docId=406&uri=/general-conference/2016/10/valiant-in-the-testimony-of-jesus");
    }
}
