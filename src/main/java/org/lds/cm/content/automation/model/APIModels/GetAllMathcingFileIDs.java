package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;

import java.sql.SQLException;

public class GetAllMathcingFileIDs extends API {
    public GetAllMathcingFileIDs() throws SQLException {
        super("ws/v1/getAllMatchingFileIDs", ControllerType.PUBLICAPI, "?fileId=13768_000_67aoyagi");
    }
}
