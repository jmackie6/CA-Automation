package org.lds.cm.content.automation.tests.endpoints;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.testng.annotations.Test;

import java.io.IOException;

public class MediaServiceDetails {

@Test(groups="endpoints")
    public static void getRendition() throws IOException {

    // Checking status for POST to http://10.118.49.42:8080/media-services/{bcBucket}/renditions

    System.out.println("\nChecking Media Service Endpoints for 200 Status\n");
    System.out.println("\nMaking POST request to http://10.118.49.42:8080/media-services/{bcBucket}/renditions\n");
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost("http://mediasrv-stage.lds.org/media-services/CES/renditions");

    String json = "{\"bcId\": \"5453423869001\"}";
    StringEntity entity = new StringEntity(json);
    httpPost.setEntity(entity);
    httpPost.setHeader("Content-type", "application/json");

    CloseableHttpResponse response = client.execute(httpPost);

    String responseBody = EntityUtils.toString(response.getEntity());

    if (response.getStatusLine().getStatusCode() == 200 && !responseBody.contains("error")) {
        System.out.println("\nStatus Code = " + response.getStatusLine().getStatusCode() + "\n");

    } else {
        System.out.println("\n*** ERROR ***\n");
        System.out.println("\n" + responseBody + "\n");
    }
}

@Test(groups="endpoints")
    public static void getDetails() throws IOException {

    // Checking status for GET to http://mediasrv-stage.lds.org/media-services/{bcBucket}/details/{bcVideoId}

    String details = NetUtils.getHTML("http://10.118.49.42:8080/media-services/CES/details/5453423869001");
    System.out.println("\nMaking GET request to http://mediasrv-stage.lds.org/media-services/{bcBucket}/details/{bcVideoId}\n");

    int details_status = NetUtils.getResponseStatus("http://10.118.49.42:8080/media-services/CES/details/5453423869001");

    if (details_status == 200 && !details.contains("error")) {
        System.out.println("\nStatus Code = " + details_status + "\n");
    } else if (details_status != 200) {
        System.out.println("\n*** RECEIVED STATUS OF " + details_status + " ***\n");
    } else {
        System.out.println("\n*** ERROR ***\n");
        System.out.println("\n" + details + "\n");
    }
}

@Test(groups="endpoints")
    public static void getThumbnail() throws IOException {
    // Checking status for GET to http://10.118.49.42:8080/media-services/CES/thumbnail/5453423869001

    String thumbnail = NetUtils.getHTML("http://10.118.49.42:8080/media-services/CES/thumbnail/5453423869001");
    System.out.println("\nMaking GET request to http://10.118.49.42:8080/media-services/{bcBucket}/thumbnail/{bcVideoId}\n");

    int thumbnail_status = NetUtils.getResponseStatus("http://10.118.49.42:8080/media-services/CES/thumbnail/5453423869001");

    if (thumbnail_status == 200 && !thumbnail.contains("error")) {
        System.out.println("\nStatus Code = " + thumbnail_status + "\n");
    } else if (thumbnail_status != 200) {
        System.out.println("\n*** RECEIVED STATUS OF " + thumbnail_status + " ***\n");
    } else {
        System.out.println("\n*** ERROR ***\n");
        System.out.println("\n" + thumbnail + "\n");
    }

}

@Test(groups="endpoints")
    public static void getVideoStill() throws IOException {
    // Checking status for GET to http://10.118.49.42:8080/media-services/{bcBucket}/videoStill/{bcVideoId}

    String videoStill = NetUtils.getHTML("http://10.118.49.42:8080/media-services/CES/videoStill/5453423869001");
    System.out.println("\nMaking GET request to http://10.118.49.42:8080/media-services/{bcBucket}/videoStill/{bcVideoId}\n");

    int video_still_status = NetUtils.getResponseStatus("http://10.118.49.42:8080/media-services/CES/videoStill/5453423869001");

    if (video_still_status == 200 && !videoStill.contains("error")) {
        System.out.println("\nStatus Code = " + video_still_status + "\n");
    } else if (video_still_status != 200) {
        System.out.println("\n*** RECEIVED STATUS OF " + video_still_status + " ***\n");
    } else {
        System.out.println("\n*** ERROR ***\n");
        System.out.println("\n" + videoStill + "\n");
    }

}

@Test(groups="endpoints")
    public static void getMatch() throws IOException {
    // Checking status for GET to http://10.118.49.42:8080/media-services/{bcBucket}/match/{videoId}/{width}/{height}

    String match = NetUtils.getHTML("http://10.118.49.42:8080/media-services/CES/match/5453423869001/640/360");
    System.out.println("\nMaking GET request to http://10.118.49.42:8080/media-services/{bcBucket}/match/{videoId}/{width}/{height}\n");

    int match_status = NetUtils.getResponseStatus("http://10.118.49.42:8080/media-services/CES/match/5453423869001/640/360");

    if (match_status == 200 && !match.contains("error")) {
        System.out.println("\nStatus Code = " + match_status + "\n");
    } else if (match_status != 200) {
        System.out.println("\n*** RECEIVED STATUS OF " + match_status + " ***\n");
    } else {
        System.out.println("\n*** ERROR ***\n");
        System.out.println("\n" + match + "\n");
    }
}

@Test(groups="endpoints")
 public static void getSize() throws IOException {
        // Checking status for GET to http://10.118.49.42:8080/media-services/{bcBucket}/size/{videoId}/{width}/{height}

        String size = NetUtils.getHTML("http://10.118.49.42:8080/media-services/CES/size/5453423869001/480/268/359022");
        System.out.println("\nMaking GET request to http://10.118.49.42:8080/media-services/{bcBucket}/size/{videoId}/{width}/{height}/{encRate}\n");

        int size_status = NetUtils.getResponseStatus("http://10.118.49.42:8080/media-services/CES/size/5453423869001/640/360/772082");

        if (size_status == 200 && !size.contains("error")) {
           System.out.println("\nStatus Code = " + size_status + "\n");
        } else if (size_status != 200){
           System.out.println("\n*** RECEIVED STATUS OF " + size_status + " ***\n");
        } else {
          System.out.println("\n*** ERROR ***\n");
         System.out.println("\n" + size + "\n");
        }

    }
}
