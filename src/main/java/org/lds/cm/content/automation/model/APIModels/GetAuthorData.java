package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetAuthorData extends API {
    public GetAuthorData() throws SQLException {
        super("ws/v1/getAuthorData", ControllerType.PUBLICAPI, "?authorName=Thomas%20S.%20Monson&eventDate=20040110");
    }
}
