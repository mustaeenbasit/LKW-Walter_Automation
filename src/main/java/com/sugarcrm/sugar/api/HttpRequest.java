package com.sugarcrm.sugar.api;

import static com.jayway.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooUtils;

//import org.apache.http.client.config.RequestConfig;

public class HttpRequest
{
   String requestURL;
   String requestType;
   String requestUser;
   JSONObject requestBody;
   JSONObject validateResponse;
   String oauthToken;
   Long requestStatusL;
   Integer requestStatus;
   ApiUtils myUtils;

   public HttpRequest(JSONObject myJson, ArrayList<RuntimeRecords> recordIDs, ArrayList<UserTokens> UserTokensArray)
         throws ParseException
   {
      myUtils = new ApiUtils();
      requestUser = (String) myJson.get("requestUser");
      oauthToken = myUtils.getUserToken(UserTokensArray, requestUser);
      VoodooUtils.voodoo.log.info("Request - oauthToken " + oauthToken);
      requestURL = (String) myJson.get("requestURL");
      requestBody = (JSONObject) myJson.get("requestBody");
      requestType = myJson.get("requestType").toString();
      requestStatusL = (long) myJson.get("requestStatus");
      requestStatus = (int) (long) requestStatusL;
      validateResponse = (JSONObject) myJson.get("validation");

      JSONArray myJsonArray = (JSONArray) myJson.get("substitute");
      Iterator<JSONObject> iteratorReplace = myJsonArray.iterator();
      String str = (String) myJson.get("requestURL");
      while (iteratorReplace.hasNext())
      {
         JSONObject jsonObjectReplace = iteratorReplace.next();
         String type = jsonObjectReplace.get("type").toString();
         switch (type)
         {
         case "id":
            String myID = myUtils.getID(jsonObjectReplace, recordIDs);
            String destination = jsonObjectReplace.get("destination").toString();
            if (destination.equals("requestURL"))
            {
               String targetField = (String) jsonObjectReplace.get("field");
               requestURL = str.replace(targetField, myID);
               String text = requestURL.replace("\n", "").replace("\r", "");
               requestURL = text;
               str = requestURL;
            }
            if (destination.equals("requestBody"))
            {
               String targetField = (String) jsonObjectReplace.get("field");
               requestBody.put(targetField, myID);
            }
            break;
         case "oauthToken":
            String targetField = (String) jsonObjectReplace.get("field");
            requestURL = str.replace(targetField, oauthToken);
            String text = requestURL.replace("\n", "").replace("\r", "");
            requestURL = text;
            str = requestURL;
            break;
         case "replaceStringID":
            myID = myUtils.getID(jsonObjectReplace, recordIDs);
            VoodooUtils.voodoo.log.info("ID Found " + myID);
            destination = jsonObjectReplace.get("destination").toString();
            String fromString = jsonObjectReplace.get("fromString").toString();
            if (destination.equals("requestBody"))
            {
               String requestBodyString = requestBody.toString();
               VoodooUtils.voodoo.log.info("To Replace " + " " + fromString + " " + myID);
               // String newRequestBodyString = requestBodyString.replace("\""
               // + fromString + "\"", "\"" + myID + "\"");
               String newRequestBodyString = requestBodyString.replace(fromString, myID);
               Object myObj;
               JSONParser parser = new JSONParser();
               myObj = parser.parse(newRequestBodyString);
               requestBody = (JSONObject) myObj;
            }
            break;
         case "date":
            String myValue = jsonObjectReplace.get("value").toString();
            String myDate = myUtils.getDate(myValue);
            str = (String) myJson.get("requestURL");
            destination = jsonObjectReplace.get("destination").toString();
            String myField = jsonObjectReplace.get("field").toString();
            if (destination.equals("requestURL"))
            {
               requestURL = str.replace(myField, myDate);
            }
            if (destination.equals("requestBody"))
            {
               requestBody.put(myField, myDate);
            }
            break;
         case "justdate":
            myValue = jsonObjectReplace.get("value").toString();
            myDate = myUtils.getJustDate(myValue);
            str = (String) myJson.get("requestURL");
            destination = jsonObjectReplace.get("destination").toString();
            myField = jsonObjectReplace.get("field").toString();
            if (destination.equals("requestURL"))
            {
               requestURL = str.replace(myField, myDate);
            }
            if (destination.equals("requestBody"))
            {
               requestBody.put(myField, myDate);
            }
            break;
         case "milliseconds":
            str = (String) myJson.get("requestURL");
            destination = jsonObjectReplace.get("destination").toString();
            String myprefix = jsonObjectReplace.get("prefix").toString();
            myField = jsonObjectReplace.get("field").toString();
            long millisecs = System.currentTimeMillis();
            String myStrmillisecs = myprefix + millisecs;
            if (destination.equals("requestURL"))
            {
               requestURL = str.replace(myField, myStrmillisecs);
            }
            if (destination.equals("requestBody"))
            {
               requestBody.put(myField, myStrmillisecs);
            }
            break;
         default:
            destination = jsonObjectReplace.get("destination").toString();
            System.out.println("destination " + destination);
         }
      }
   }

   public Response execute(ArrayList<RuntimeRecords> recordIDs) throws ParseException
   {
      Response response = null;
      VoodooUtils.voodoo.log.info("Request - User " + requestUser);
      VoodooUtils.voodoo.log.info("Request - Type " + requestType);
      VoodooUtils.voodoo.log.info("Request - URL " + requestURL);
      VoodooUtils.voodoo.log.info("Request - Body " + requestBody);
      switch (requestType)
      {
      case "post":
         response = given().header("OAuth-Token", oauthToken).body(requestBody).expect().statusCode(requestStatus)
               .when().post(requestURL);
         String recordID = "";
         if (myUtils.IsJson(response))
         {
            recordID = response.path("id");
         } else
         {
            recordID = response.asString();
            recordID = recordID.replaceAll("\"", "");
         }
         // Save the record ID for relationships and to Delete in clean
         // up
         RuntimeRecords moduleID = new RuntimeRecords("ID", requestURL, "", recordID);
         recordIDs.add(moduleID);
         break;
      case "postForm":
         try
         {
            response = postForm(recordIDs);
         } catch (Exception e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         break;
      case "postImportProcessDefinition":
         try
         {
            response = postImportProcessDefinition(recordIDs);
         } catch (Exception e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         break;
      case "postFormMultiPart":
         try
         {
            response = postFormMultiPart(recordIDs);
         } catch (Exception e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         break;
      case "get":
         response = given().header("OAuth-Token", oauthToken).expect().statusCode(requestStatus).when().get(requestURL);
         break;
      case "getURL":
         // Store and un-set REST basePath
         String oldBasePath = RestAssured.basePath;
         RestAssured.basePath = "";
         response = given().header("OAuth-Token", oauthToken).expect().statusCode(requestStatus).when().get(requestURL);
         // Restore REST basePath
         RestAssured.basePath = oldBasePath;
         break;
      case "put":
         response = given().header("OAuth-Token", oauthToken).body(requestBody).expect().statusCode(requestStatus)
               .when().put(requestURL);
         break;
      case "delete":
         response = given().header("OAuth-Token", oauthToken).expect().statusCode(requestStatus).when()
               .delete(requestURL);
         break;
      // Allows for a wait between HTTP requests
      case "wait":
         VoodooUtils.voodoo.log.info("Start Wait");
         try
         {
            Thread.sleep(3000);
         } catch (InterruptedException e)
         {
         }
         VoodooUtils.voodoo.log.info("End Wait");
         break;
      default:
      }
      if (response != null)
      {
         VoodooUtils.voodoo.log.info("Response - Body " + response.asString());
      }
      return response;
   }

   public Response postForm(ArrayList<RuntimeRecords> recordIDs) throws Exception, IOException
   {
      Response response = null;

      // Un-comment for debugging via proxy
      // HttpHost proxy = new HttpHost("localhost", 8087);
      // RequestConfig config =
      // RequestConfig.custom().setProxy(proxy).build();

      String baseURI = "";
      String basePath = "";

      try
      {
         baseURI = new SugarUrl().getBaseUrl();
      } catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      try
      {
         basePath = new SugarUrl().getRestRelativeUrl();

      } catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      // Automatically follows redirects with the returned location being
      // processed
      CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
            .build();

      // This request will set the PHP Session ID in the Cookie, needed for
      // BWC PHP Forms
      HttpPost postRequest = new HttpPost(baseURI + basePath + "/oauth2/bwc/login");
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      postRequest.addHeader("OAuth-Token", oauthToken);

      HttpResponse myResponse = httpClient.execute(postRequest);

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }
      
   // This request will get the CSRF Token, needed as a variable for the
      // update
      postRequest = new HttpPost(baseURI + "/?module=Roles&action=index&bwcFrame=1");
      // Un-comment for debugging via proxy
      //postRequest.setConfig(config);

      postRequest.addHeader("OAuth-Token", oauthToken);

      myResponse = httpClient.execute(postRequest);

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      // Extract csrf_token from - <input type="hidden" name="csrf_token"
      // value="A1HDMvwbwWohSFxOd3CqtkM_2j__GRbs" />

      HttpEntity entity = myResponse.getEntity();
      String content = EntityUtils.toString(entity);
      String csrfToken = "";
      Pattern p = Pattern.compile("<input type=\\\"hidden\\\" name=\\\"csrf_token\\\" value=\\\"(.*)\\\".*");
      Matcher m = p.matcher(content);
      if (m.find())
      {
         VoodooUtils.voodoo.log.info("FOUND CSRF Token " + m.group(1));
         csrfToken = m.group(1);

      } else
      {
         VoodooUtils.voodoo.log.info("NOT FOUND CSRF Token ");
      }

      postRequest = new HttpPost(baseURI + requestURL);
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

      JSONArray myJsonArray = (JSONArray) requestBody.get("formParameters");
      Iterator<JSONObject> iteratorParms = myJsonArray.iterator();

      while (iteratorParms.hasNext())
      {
         JSONObject jsonObjectParms = iteratorParms.next();
         String myName = jsonObjectParms.get("name").toString();
         String myValue = jsonObjectParms.get("value").toString();
         urlParameters.add(new BasicNameValuePair(myName, myValue));
      }
      
      /*
       * Add the CRSF Token - Note this needs to be broken down to individual
       * HTTP Requests
       */
      urlParameters.add(new BasicNameValuePair("csrf_token", csrfToken));

      postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));

      myResponse = httpClient.execute(postRequest);

      if (myResponse.getStatusLine().getStatusCode() != requestStatus)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      httpClient.close();
      return response;
   }

   public Response postFormMultiPart(ArrayList<RuntimeRecords> recordIDs) throws Exception, IOException
   {
      Response response = null;

      // Un-comment for debugging via proxy
     // HttpHost proxy = new HttpHost("localhost", 8087);
      //RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

      String baseURI = "";
      String basePath = "";

      try
      {
         baseURI = new SugarUrl().getBaseUrl();
      } catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      try
      {
         basePath = new SugarUrl().getRestRelativeUrl();

      } catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      // Automatically follows redirects with the returned location being
      // processed
      CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
            .build();

      // This request will set the PHP Session ID in the Cookie, needed for
      // BWC PHP Forms
      HttpPost postRequest = new HttpPost(baseURI + basePath + "/oauth2/bwc/login");
      // Un-comment for debugging via proxy
     // postRequest.setConfig(config);

      postRequest.addHeader("OAuth-Token", oauthToken);

      HttpResponse myResponse = httpClient.execute(postRequest);

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      // This request will get the CSRF Token, needed as a variable for the
      // update
      postRequest = new HttpPost(baseURI + "/?module=Users&action=index&bwcFrame=1");
      // Un-comment for debugging via proxy
      //postRequest.setConfig(config);

      postRequest.addHeader("OAuth-Token", oauthToken);

      myResponse = httpClient.execute(postRequest);

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      // Extract csrf_token from - <input type="hidden" name="csrf_token"
      // value="A1HDMvwbwWohSFxOd3CqtkM_2j__GRbs" />

      HttpEntity entity = myResponse.getEntity();
      String content = EntityUtils.toString(entity);
      String csrfToken = "";
      Pattern p = Pattern.compile("<input type=\\\"hidden\\\" name=\\\"csrf_token\\\" value=\\\"(.*)\\\".*");
      Matcher m = p.matcher(content);
      if (m.find())
      {
         VoodooUtils.voodoo.log.info("FOUND CSRF Token " + m.group(1));
         csrfToken = m.group(1);

      } else
      {
         VoodooUtils.voodoo.log.info("NOT FOUND CSRF Token ");
      }

      postRequest = new HttpPost(baseURI + requestURL);
      // Un-comment for debugging via proxy
      //postRequest.setConfig(config);

      List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

      JSONArray myJsonArray = (JSONArray) requestBody.get("formParameters");
      Iterator<JSONObject> iteratorParms = myJsonArray.iterator();
      MultipartEntity MPentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

      while (iteratorParms.hasNext())
      {
         JSONObject jsonObjectParms = iteratorParms.next();
         String myName = jsonObjectParms.get("name").toString();
         String myValue = jsonObjectParms.get("value").toString();
         MPentity.addPart(myName, new StringBody(myValue));
      }

      /*
       * Add the CRSF Token - Note this needs to be broken down to individual
       * HTTP Requests
       */
      MPentity.addPart("csrf_token", new StringBody(csrfToken));

      postRequest.setEntity(MPentity);

      myResponse = httpClient.execute(postRequest);

      if (myResponse.getStatusLine().getStatusCode() != requestStatus)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      httpClient.close();
      return response;
   }

   public Response postImportProcessDefinition(ArrayList<RuntimeRecords> recordIDs) throws Exception, IOException
   {
      Response response = null;

      // Un-comment for debugging via proxy
      // HttpHost proxy = new HttpHost("localhost", 8087);
      // RequestConfig config =
      // RequestConfig.custom().setProxy(proxy).build();

      String myFile = requestBody.get("fileLocation").toString();

      String baseURI = "";
      String basePath = "";

      try
      {
         baseURI = new SugarUrl().getBaseUrl();
      } catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      try
      {
         basePath = new SugarUrl().getRestRelativeUrl();

      } catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      // Automatically follows redirects with the returned location being
      // processed
      CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
            .build();

      // This request will set the PHP Session ID in the Cookie, needed for
      // BWC PHP Forms
      HttpPost postRequest = new HttpPost(baseURI + basePath + "/oauth2/bwc/login");
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      postRequest.addHeader("OAuth-Token", oauthToken);

      HttpResponse myResponse = httpClient.execute(postRequest);

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      postRequest = new HttpPost(baseURI
            + "rest/v10/pmse_Project/file/project_import?format=sugar-html-json&delete_if_fails=true&oauth_token="
            + oauthToken + "&platform=base");
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

      JSONArray myJsonArray = (JSONArray) requestBody.get("formParameters");
      // Iterator<JSONObject> iteratorParms = myJsonArray.iterator();
      MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

      entity.addPart("OAuth-Token", new StringBody(oauthToken));
      entity.addPart("X-Requested-With", new StringBody("IFrame"));

      File fileToUpload = new File(myFile);
      FileBody fileBody = new FileBody(fileToUpload, "application/octet-stream");
      entity.addPart("project_import", fileBody);

      postRequest.setEntity(entity);

      myResponse = httpClient.execute(postRequest);

      if (myResponse.getStatusLine().getStatusCode() != requestStatus)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      httpClient.close();
      return response;
   }

   private static String convertStreamToString(InputStream is)
   {

      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();

      String line = null;
      try
      {
         while ((line = reader.readLine()) != null)
         {
            sb.append(line + "\n");
         }
      } catch (IOException e)
      {
         e.printStackTrace();
      } finally
      {
         try
         {
            is.close();
         } catch (IOException e)
         {
            e.printStackTrace();
         }
      }
      return sb.toString();
   }

}
