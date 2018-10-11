package org.lds.cm.content.automation.tests.endpoints;

import org.json.simple.JSONObject;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.XPathUtils;
import org.springframework.util.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UriServiceTest {

    //Be sure to change this back when you get the system setup again.
//    private static final String broadcastUri = "https://publish-stage.ldschurch.org/content_automation/ws/v1/generateBroadcastUri";
    private static final String broadcastUri = Constants.epBroadcastUri;

    @Test(enabled=true)
    public static void generateBroadcastWWDUriTest() throws Exception{
        String valueString = "worldwide-devotional, 2017, 01, 11,uchtdorf";
        String keyString = "category, year, month, speakerId, speakerSurname";
        String expectedUri = "/broadcasts/worldwide-devotional/2017/01/11uchtdorf";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));
    }

    @Test(enabled=true)
    public static void generateBroadcastEWGAUriTest() throws Exception{
        String valueString = "evening-with-a-general-authority, 2017, 08, 12, eyring";
        String keyString = "category, year, month, speakerId, speakerSurname";
        String expectedUri = "/broadcasts/evening-with-a-general-authority/2017/08/12eyring";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));
    }

    @Test(enabled=true)
    public static void generateBroadcastFPCDTest() throws Exception{
        String valueString = "first-presidency-christmas-devotional, 2017, 12, 12, monson";
        String keyString = "category, year, month, speakerId, speakerSurname";
        String expectedUri = "/broadcasts/first-presidency-christmas-devotional/2017/12/12monson";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));
    }

    @Test(enabled=true)
    public static void generateGeneralConferenceManifestTest() throws Exception{
        String valueString = "general-conference, 2017, 04, manifest";
        String keyString = "category, year, month, session";
        String expectedUri = "/general-conference/2017/04/_manifest";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));
    }

    @Test(enabled=true)
    public static void generateGeneralConferenceSessionTest() throws Exception{
        String valueString = "general-conference, 2017, 04, priesthood-session";
        String keyString = "category, year, month, session";
        String expectedUri = "/general-conference/2017/04/priesthood-session";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));

    }

    @Test(enabled=true)
    public static void generateGeneralConferenceTalkTest() throws Exception{
        String valueString = "general-conference, 2017, 04, 15, oaks";
        String keyString = "category, year, month, speakerId, speakerSurname";
        String expectedUri = "/general-conference/2017/04/15oaks";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));
    }

    @Test(enabled=true)
    public static void generateBroadcastWWDUriManifestTest() throws Exception{
        String valueString = "worldwide-devotional, 2017, 01, manifest";
        String keyString = "category, year, month, session";
        String expectedUri = "/broadcasts/worldwide-devotional/2017/01/_manifest";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));
    }

    @Test(enabled=true)
    public static void generateBroadcastEWGAUriManifestTest() throws Exception{
        String valueString = "evening-with-a-general-authority, 2017, 08, manifest";
        String keyString = "category, year, month, session";
        String expectedUri = "/broadcasts/evening-with-a-general-authority/2017/08/_manifest";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));
    }

    @Test(enabled=true)
    public static void generateBroadcastFPCDManifestTest() throws Exception{
        String valueString = "first-presidency-christmas-devotional, 2017, 12, manifest";
        String keyString = "category, year, month, session";
        String expectedUri = "/broadcasts/first-presidency-christmas-devotional/2017/12/_manifest";

        String uri = runUriTest(keyString, valueString, broadcastUri);
        Assert.isTrue(uri.equals(expectedUri));
    }

    private static String runUriTest(String keys, String values, String endpointUrl) throws Exception{
        String[] stringKeyArray = keys.split(",");
        String[] stringValueArray = values.split(",");

        Map<String, String> parameters = new HashMap<>();
        for(int i = 0; i < stringKeyArray.length; i++){
            parameters.put(stringKeyArray[i].trim(), stringValueArray[i].trim());
        }

        Document currentDocument = NetUtils.httpGetRequestWithParams(endpointUrl, parameters);
        return XPathUtils.getStringValue("/uri", currentDocument, false);
    }
}
