package org.lds.cm.content.automation.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.LogUtils;
import org.slf4j.Logger;
import org.xml.sax.InputSource;

//TODO: Add get and delete by docUri and locale (language)
public class QADocMapService {
    private static final Logger LOG = LogUtils.getLogger();
    static String url = Constants.epAnnotationServerDocMapDocId;
    static String username = Constants.annotationServerUsername;
//		static String password = "b4d7e824-6978-403e-b941-12d977e665e0"; Production password
    static String password = Constants.annotationServerPassword;

    /**
     * Calls annotation server docmap API. Based on code written by Todd. Todd's version returns Result type. I don't know what it
     * is, and am going to ignore it for now. Returning int works for what we are doing .
     *
     * @param dataAid data-aid of published doc you are searching for
     * @return count returned from annotation server. Should be 0 if not found and 1 if found
     */
    //public Result getDocMap(Integer dataAid) {
    public static int getDocMap(String dataAid) {

        String annotationsURL = url + dataAid;
        Integer totalCount = 0;

        CloseableHttpClient client = null;
        String basicAuth = getBasicAuthEncoding(username, password);
        if (null == client) {
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            requestBuilder = requestBuilder.setConnectTimeout(3600000);
            requestBuilder = requestBuilder.setConnectionRequestTimeout(3600000);
            requestBuilder = requestBuilder.setSocketTimeout(3600000);
            HttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();

            client = HttpClientBuilder.create().setConnectionManager(conMgr).disableCookieManagement().setDefaultRequestConfig(requestBuilder.build()).build();
        }

        HttpGet httpGet = new HttpGet(annotationsURL);
        httpGet.addHeader("Authorization", "Basic " + basicAuth);
        httpGet.addHeader("Accept", "application/xml");
        httpGet.addHeader("Content-Type", "application/xml");

        CloseableHttpResponse response = null;
        HttpEntity resEntity = null;
        String result = "";
        InputStream is = null;
        try {
            response = client.execute(httpGet);
            resEntity = response.getEntity();
            is = resEntity.getContent();
            result = IOUtils.toString(is, "UTF-8");
            if (StringUtils.contains(result, "total=")) {
                int start = result.indexOf("total=");
                int end = result.indexOf(">", start);
                String total = StringUtils.substring(result, start, end);
                String count = StringUtils.replace(total, "total=", "");
                count = StringUtils.replace(count, "\"", "");
                count = StringUtils.replace(count, "/", "");
                totalCount = Integer.parseInt(count);
                return totalCount;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            httpGet.reset();
            if (null != client) {
                try {
                    client.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            response = null;
            is = null;
        }

        return totalCount;
    }

    /**
     * Deletes a Doc Map entry
     *
     * @param dataAid data-aid of published doc you want to delete
     * @return count returned from annotation server. Should be 0 if not found and 1 if found
     */
    public static int deleteDocMap(String dataAid) {

        String annotationsURL = url + dataAid;
        Integer numberOfDocs = 0;

        CloseableHttpClient client = null;
        String basicAuth = getBasicAuthEncoding(username, password);
        if (null == client) {
            LOG.info("New client created");
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            requestBuilder = requestBuilder.setConnectTimeout(3600000);
            requestBuilder = requestBuilder.setConnectionRequestTimeout(3600000);
            requestBuilder = requestBuilder.setSocketTimeout(3600000);
            HttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();

            client = HttpClientBuilder.create().setConnectionManager(conMgr).disableCookieManagement().setDefaultRequestConfig(requestBuilder.build()).build();
        }

        HttpDelete httpDelete = new HttpDelete(annotationsURL);
        httpDelete.addHeader("Authorization", "Basic " + basicAuth);
        httpDelete.addHeader("Accept", "application/xml");
        httpDelete.addHeader("Content-Type", "application/xml");

        CloseableHttpResponse response = null;
        HttpEntity resEntity = null;
        InputStream is = null;
        //Result resultObj = null;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            response = client.execute(httpDelete);
            resEntity = response.getEntity();
            is = resEntity.getContent();
            InputSource inputSource = new InputSource(is);
            String numDocsStr = xpath.evaluate("//numberOfDocs", inputSource);
            numberOfDocs = Integer.parseInt(numDocsStr);
            return numberOfDocs;
        } catch (IOException | XPathExpressionException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            httpDelete.reset();
            if (null != client) {
                try {
                    client.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            response = null;
            is = null;
        }
        System.out.println(numberOfDocs);
        return numberOfDocs;
    }

    private static String getBasicAuthEncoding(String username, String password) {
        String credStr = username + ":" + password;
        String encoded = Base64.encodeBase64String(credStr.getBytes());
        return encoded;
    }

    /**
     * Deletes multiple Doc Map entries
     *
     * @param - a list of data-aids and uses get and delete docmap
     * @return returns data-aids and 0 if successful get/delete
     */
    public static void bulkDeleteDocmap(ArrayList<String> filesToDelete) {

        //Get
        System.out.println("GET: \n");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            for (int i = 0; i < filesToDelete.size(); i++) {
                getDocMap(filesToDelete.get(i));
                System.out.println(Constants.epDelete + filesToDelete.get(i));
                System.out.println(getDocMap(filesToDelete.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n");

        //Delete
        System.out.println("DELETE: \n");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            for (int i = 0; i < filesToDelete.size(); i++) {
                int numDelete = deleteDocMap(filesToDelete.get(i));
                System.out.println(Constants.epDelete + filesToDelete.get(i));
                System.out.println(numDelete);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n");

        //Get - to make sure files were deleted
        System.out.println("GET: \n");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            for (int i = 0; i < filesToDelete.size(); i++) {
                getDocMap(filesToDelete.get(i));
                System.out.println(Constants.epDelete + filesToDelete.get(i));
                System.out.println(QADocMapService.getDocMap(filesToDelete.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\n");
    }

    //Delete single docmap
    public static void deleteSingleDocmap(String dataAID) {
        int numDelete = deleteDocMap(dataAID);
        System.out.println(Constants.epDelete + dataAID);
        System.out.println(numDelete + " Docmap(s) Deleted");
    }

}
