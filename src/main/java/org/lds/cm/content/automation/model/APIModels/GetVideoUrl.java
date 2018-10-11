package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetVideoUrl extends API {
    public GetVideoUrl() throws SQLException {
        super("ws/v1/getVideoUrl", ControllerType.PUBLICAPI, "?videoId=4139271040001&playerId=1301988574001&playerKey=AQ~~,AAABFr5dynE~,TSPthyn9f7yrQg338X8PKFxmsL7iPPYN");
    }
}
