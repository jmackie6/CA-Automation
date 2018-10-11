package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetMediaIdsByFileId extends API {
    public GetMediaIdsByFileId() throws SQLException {
        super("ws/v1/getMediaIdsByFileId", ControllerType.PUBLICAPI, "?fileId=PD10031248_002_000");
    }
}
