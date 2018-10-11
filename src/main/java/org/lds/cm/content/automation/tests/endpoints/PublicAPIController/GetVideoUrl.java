package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.springframework.util.Assert;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetVideoUrl
{
    private enum VideoType
    {
        Valid, BadVideoId, BadAccountId
    }

    private ResultSet rs = null;
    //Brightcove_video table for videoId, call bright_cove account table for other info
    //VPP method is being deprecated so ignoring it and moving to VA methods

    /**
     * The two getVideo methods are located in the Brightcover service
     *   public synchronized String getVideoUrl(String videoId, String playerId, String playerKey)
     *   public synchronized String getVideoUrl(String videoId, Long accountId)
     */

    @BeforeClass (alwaysRun=true)
    public void setUp() throws SQLException, InterruptedException
    {
        APIRules.fixAPIRule("ws/v1/getVideoUrl");
    }

///////////////////// Passing Test Cases /////////////////////////
    @Test (groups = { "endpoints" })
    public void getVideoUrlVAValidWs() throws SQLException, IOException
    {     setUpCall("/ws/v1", VideoType.Valid);    }

    @Test (groups = { "endpoints" })
    public void getVideoUrlVAValidServ() throws SQLException, IOException
    {     setUpCall("/services/api", VideoType.Valid);    }


///////////////////// Failing Test Cases /////////////////////////
    @Test (groups = { "endpoints" })
    public void getVideoUrlVABadVWs() throws SQLException, IOException
    {     setUpCall("/ws/v1", VideoType.BadVideoId);    }

    @Test (groups = { "endpoints" })
    public void getVideoUrlVABadVServ() throws SQLException, IOException
    {     setUpCall("/services/api", VideoType.BadVideoId);    }

    @Test (groups = { "endpoints" })
    public void getVideoUrlVABadAWs() throws SQLException, IOException
    {     setUpCall("/ws/v1", VideoType.BadAccountId);    }

    @Test (groups = { "endpoints" })
    public void getVideoUrlVABadAServ() throws SQLException, IOException
    {     setUpCall("/services/api", VideoType.BadAccountId);    }




/////////////////// Test Case Guts /////////////////////////
    private void setUpCall(String type, VideoType vt) throws SQLException, IOException
    {
        StringBuilder sb = new StringBuilder(Constants.baseURL + type + "/getVideoUrl?videoId=");
        String videoId = "";
        if(vt == VideoType.Valid || vt == VideoType.BadAccountId)
        {
            //April 2018 - Test doesn't have anything in the brightcove_video database, so use random videoID from StageDB
            if(Constants.environment.compareTo("test") == 0)
                videoId = "2081508025001";
            else
            {
                rs = JDBCUtils.getRandomRows("brightcove_video", 1);
                if(rs.next())
                    videoId = rs.getString("brightcove_video_id");
                rs.close();
            }
        }
        else
            videoId = "0000000000001";

        sb.append(videoId + "&accountId=");
        if(vt == VideoType.Valid || vt == VideoType.BadVideoId)
        {
            rs = JDBCUtils.getRandomRows("brightcove_account", 1);
            if(rs.next()) {
                sb.append(rs.getString("brightcove_account_id"));
                rs.close();
            }
        }
        else {
            sb.append("000000000001");
            videoId = "0";
        }
        getVideoUrl(sb.toString(), videoId, vt == VideoType.Valid);
    }

    private void getVideoUrl(String url, String videoID, boolean Validate) throws IOException
    {
        String response = NetUtils.getHTML(url);
        System.out.println(response);
        //badAccountID urls return a 404 nothingness.  Check for that then do the rest of the testing.
        if(videoID.compareTo("0") == 0)
        {
            Assert.isTrue(response.length() == 0, "get videoURL with bad Account ID returned something\n" + response);
            return;
        }
        Assert.isTrue(response != null, "getVideoURL returned null");
        Assert.isTrue(response.length() > 0, "get videoURL didn't return a url");

        //cut off the url tags that the response comes with
        response = response.substring(5, response.length() - 6);
        //System.out.println(response);
        String finalVerification = verifyUrl(response, videoID, Validate);
        System.out.println(finalVerification);
        Assert.isTrue("success".compareTo(finalVerification) == 0, finalVerification);
    }


    /**take the url response that was returned and verify that it is correct
     *
     * param response - the url that came back from the initial getvideourl call
     * param videoID - the video id that was used to make the origianl call should
     *      be in the response url
     * param validate - if true this should point to a valid video, if not
     *      verify that the error response is present
     *
     * return "success" or an error message
     */
    private String verifyUrl(String response, String videoID, boolean validate)
    {
        System.out.println(response);
        //make sure that the respone url is long enough to hold the videoID
        if (response.length() < videoID.length())
            return "url not long enough";

        //if response does not contain the videoId fail the test
        if(!response.contains(videoID))
            return "url does not contain videoID";

        //call an http get on the response that was returned, verify that it wasn't an error but an actual video
        try{
            String verify = NetUtils.getHTML(response);
            if(verify.contains("Video not found for id"))
            {
                if(!validate)
                    return "success";
                else
                    return "Valid url with error message - " + response;
            }

        }
        catch(IOException e)
        {    return "not a valid url - "+response;   }

        //if you passed all test you made it here :)
        return "success";
    }

    @AfterMethod(alwaysRun=true)
    public static void cleanup()
    {    JDBCUtils.closeUp();    }
}
