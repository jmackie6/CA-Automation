package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GenerateScriptRefs extends API {
    public GenerateScriptRefs() throws SQLException {
        super("generateScriptureRefs", ControllerType.PREVIEWAPI, "?source=I%20was%20reading%201%20Nephi%201%20:%2011%20the%20Sother%20day%20.%20Try%20another%203%20John%203%20:%2016%20,%20hooray%20.%20");
    }
}
