package org.lds.cm.content.automation.util;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jmeter.gui.action.Close;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;


import javax.swing.text.html.parser.Entity;

import static org.lds.cm.content.automation.util.XMLUtils.getDocumentFromString;

public class NetUtils {

	/**
	 * Fires a GET request to url and returns the status code.
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static int getResponseStatus(String url) throws IOException {
		return HttpClients.createDefault().execute(new HttpGet(url)).getStatusLine().getStatusCode();
	}
	
	/**
	 * Fires a GET request to url and returns a JSON object
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws org.json.simple.parser.ParseException
	 * @throws IOException
	 */
	public static JSONObject getJson(String url) throws ParseException, org.json.simple.parser.ParseException, IOException {
		return (JSONObject) new JSONParser().parse(EntityUtils.toString(HttpClients.createDefault().execute(new HttpGet(url)).getEntity()));
	}

	public static JSONArray getJsonArray(String url) throws ParseException, org.json.simple.parser.ParseException, IOException {
		return (JSONArray) new JSONParser().parse(EntityUtils.toString(HttpClients.createDefault().execute(new HttpGet(url)).getEntity()));
	}




	public static Document httpPostRequestXML(String url, List<NameValuePair> parameters) throws Exception{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

		try{
			String response = EntityUtils.toString(httpResponse.getEntity());
			return getDocumentFromString(response);
		}finally{
			httpResponse.close();
		}


	}

	public static JSONObject httpPostRequestJSON(String url, List<NameValuePair> parameters) throws Exception{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
//		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

		try(CloseableHttpResponse httpResponse = httpClient.execute(httpPost)){
//			String response = EntityUtils.toString(httpResponse.getEntity());
			return JSONUtils.getJSONFromResponse(httpResponse);
		}


	}

	public static Document getXML(String url) throws Exception{
	    CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);

		try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet);){
			String response = EntityUtils.toString(httpResponse.getEntity());
			return getDocumentFromString(response);
		}


	}

	public static Document httpDeleteRequestXML(String url ) throws Exception{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(url);
		CloseableHttpResponse httpResponse = httpClient.execute(httpDelete);
		try {
			String response = EntityUtils.toString(httpResponse.getEntity());
			return getDocumentFromString(response);
		}finally{
			httpResponse.close();
		}
	}

	/**
	  * Returns the html content of an HTTP request
	  * @param url
	  * @return
	  * @throws ParseException
	  * @throws ClientProtocolException
	  * @throws IOException
	  */
	public static String getHTML(String url) throws ParseException, IOException {
		HttpGet getRequest = new HttpGet(url);
		/*getRequest.addHeader("client_id", Constants.apiClientId);
		getRequest.addHeader("client_secret", Constants.apiClientSecret);*/
		return EntityUtils.toString(HttpClients.createDefault().execute(getRequest).getEntity());
	}

	/**
	 * Returns the html content of an HTTP request
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static CloseableHttpResponse getFullHTMLResponse(String url) throws ParseException, IOException {
		HttpGet getRequest = new HttpGet(url);
		/*getRequest.addHeader("client_id", Constants.apiClientId);
		getRequest.addHeader("client_secret", Constants.apiClientSecret);*/
		return HttpClients.createDefault().execute(getRequest);
	}

	public static CloseableHttpResponse getPost(String url) throws ParseException, IOException {
		HttpPost postRequest = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		HttpEntity multipart = builder.build();
		postRequest.setEntity(multipart);
		/*getRequest.addHeader("client_id", Constants.apiClientId);
		getRequest.addHeader("client_secret", Constants.apiClientSecret);*/
		return HttpClients.createDefault().execute(postRequest);
	}

	public static JSONObject getJSONFromResponse(CloseableHttpResponse response) throws IOException, org.json.simple.parser.ParseException{
		String responseString = EntityUtils.toString(response.getEntity());
		final JSONParser parser = new JSONParser();
		final JSONObject returnObject = (JSONObject) parser.parse(responseString);
		return returnObject;

	}

	public static CloseableHttpResponse getPostHttpClient(String url) throws IOException, ClientProtocolException{

		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		return client.execute(post);




	}

	/** found how to do this on https://stackoverflow.com/questions/1378920/how-can-i-make-a-multipart-form-data-post-request-using-java
	 *
	 * @param url - that you will be posting to
	 * @param values - a map with the key,value pairs to put in the body of the http call
	 * @return - the request body
	 */
	public static String postHTML(String url, Map<String, Object> values) throws ParseException, IOException
	{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		Iterator it = values.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String type = (String)pair.getKey();
			if(type.compareTo("file") == 0) {
				File f = (File) pair.getValue();
				builder.addBinaryBody("file", new FileInputStream(f),
						ContentType.APPLICATION_OCTET_STREAM, f.getName());
			}
			it.remove();
		}

		HttpEntity multipart = builder.build();
		postRequest.setEntity(multipart);
		CloseableHttpResponse response = client.execute(postRequest);
		HttpEntity responseEntity = response.getEntity();

		if(responseEntity != null) {
			System.out.println(responseEntity.toString());
			System.out.println(responseEntity.getContentLength());

			String theString = IOUtils.toString(responseEntity.getContent(), "UTF-8");
			System.out.println(theString);
			return theString;
		}

		//if the responseEntity is null return an empty string
		return "";
	}



	public static Document httpGetRequestWithParams(String url, Map<String, String> parameters) throws Exception{
		String urlComplete = addParamsToRequest(url, parameters);
		return getXML(urlComplete);
	}

	public static JSONObject httpGetJSONWithParams(String url, Map<String, String> parameters) throws Exception{
		String urlComplete = addParamsToRequest(url, parameters);
		return getJson(urlComplete);
	}

	public static Document httpDeleteRequestWithParams(String url, Map<String, String> parameters) throws Exception {
		String urlComplete = addParamsToRequest(url, parameters);
		return XMLUtils.getDocumentFromString(urlComplete);

	}

	private static String addParamsToRequest(String urlOriginal, Map<String, String> parameters){
		StringBuilder sb = new StringBuilder(urlOriginal);
		if(parameters.size() > 0) {
			sb.append("?");
		}
		int index = 0;
		for(Map.Entry<String, String> param: parameters.entrySet()){

			sb.append(param.getKey()).append("=").append(param.getValue());
			if(index < parameters.size() - 1){
				sb.append("&");
				index++;
			}
		}
		 return(sb.toString());
	}

}
