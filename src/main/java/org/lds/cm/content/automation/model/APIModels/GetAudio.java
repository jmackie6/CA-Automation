package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetAudio extends API {

    public GetAudio() throws SQLException {
        super("ws/v1/getAudio", ControllerType.PUBLICAPI, "?audioId=4");
    }
}
