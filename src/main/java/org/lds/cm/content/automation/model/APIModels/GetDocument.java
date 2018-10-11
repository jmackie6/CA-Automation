package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetDocument extends API {
    public GetDocument() throws SQLException {
        super("ws/v1/getDocument", ControllerType.PUBLICAPI, "?fileId=PD50021411_140_0040&contentType=general-conference-talk");
    }
}
