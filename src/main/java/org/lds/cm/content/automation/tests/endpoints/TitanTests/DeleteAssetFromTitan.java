package org.lds.cm.content.automation.tests.endpoints.TitanTests;

import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.util.QATitanUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class DeleteAssetFromTitan {


    @Test
    public static void deleteAssetFromTitan() throws Exception {
        QATitanUtils titanUtils = new QATitanUtils();
        String fileIdPartial = "10734_000";
        String fileIdComplete = "10734_000_000";
        titanUtils.deleteTitanAsset(fileIdComplete);
    }
}
