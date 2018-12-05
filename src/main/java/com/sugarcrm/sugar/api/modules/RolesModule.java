package com.sugarcrm.sugar.api.modules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.ApiUtils;

public class RolesModule
{
   JSONObject jsonObjectDelta;
   JSONObject jsonObjectDefault;
   JSONObject jsonObjectDriver;
   Object obj1 = null;
   ApiUtils myUtils = new ApiUtils();
   Response response;
   String Strresponse ="";
   private String[] accessGUIName = new String[334];

   // Creates a Role and Access Matrix from given values in the JSON file
   // passed in testData. The values passed in testData are for the main Role
   // attributes (Role Name, Description etc.) and the values for the access
   // matrix that are not equal to the default values of "Not
   // Set". The Full matrix is referenced as "Default" variables and the
   // changes
   // Matrix are referenced as "Delta" variables
   public void CreateRole(String adminUserID, String testData) throws FileNotFoundException, IOException,
         ParseException
   {

      // Un-comment for debugging via proxy
      // HttpHost proxy = new HttpHost("localhost", 8080);
      // RequestConfig config =
      // RequestConfig.custom().setProxy(proxy).build();

      // Only the values not equal to Not Set are passed to replace the
      // defaults
      jsonObjectDelta = myUtils.getJson(testData);
      jsonObjectDefault = myUtils.getJson("rest/base/adminDefaultRoleAccess.json");
      // Need to read global variables for URL
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
      // BWC PHP Forms requests
      HttpPost postRequest = new HttpPost(baseURI + basePath + "/oauth2/bwc/login");
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      postRequest.addHeader("OAuth-Token", adminUserID);

      HttpResponse myResponse = httpClient.execute(postRequest);

      HttpEntity responseEntity = myResponse.getEntity();
      if (responseEntity != null)
      {
         Strresponse = EntityUtils.toString(responseEntity);
        // System.out.println("RESPONSE FROM ROLESCALL 1 " + Strresponse);
      }

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      // This request will get the CSRF Token, needed as a variable for the
      // update
      postRequest = new HttpPost(baseURI + "/?module=Roles&action=index&bwcFrame=1");
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      postRequest.addHeader("OAuth-Token", adminUserID);

      myResponse = httpClient.execute(postRequest);
      responseEntity = myResponse.getEntity();
      if (responseEntity != null)
      {
         Strresponse = EntityUtils.toString(responseEntity);
       //  System.out.println("RESPONSE FROM ROLESCALL 2 " + Strresponse);
      }

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }
      

      // Extract csrf_token from - <input type="hidden" name="csrf_token"
      // value="A1HDMvwbwWohSFxOd3CqtkM_2j__GRbs" />

      //String content = EntityUtils.toString(responseEntity);
     
      String csrfToken = "";
      Pattern p = Pattern.compile("<input type=\\\"hidden\\\" name=\\\"csrf_token\\\" value=\\\"(.*)\\\".*");
      Matcher m = p.matcher(Strresponse);
      if (m.find())
      {
         VoodooUtils.voodoo.log.info("FOUND CSRF Token " + m.group(1));
         csrfToken = m.group(1);

      } else
      {
         VoodooUtils.voodoo.log.info("NOT FOUND CSRF Token ");
      }

      postRequest.releaseConnection();

      // This request will create a basic Role
      postRequest = new HttpPost(baseURI + "index.php");
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
      JSONArray myJsonArrayDelta = (JSONArray) jsonObjectDelta.get("roleFormParameters");
      Iterator<JSONObject> iteratorParms = myJsonArrayDelta.iterator();
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

      // Create a basic Role
      postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));

      // The create Role response contains the id of the Role and the dynamic
      // name variables needed to create the subsequent Access matrix for the
      // Role

      myResponse = httpClient.execute(postRequest);
      
      responseEntity = myResponse.getEntity();
      if (responseEntity != null)
      {
         Strresponse = EntityUtils.toString(responseEntity);
      // System.out.println("RESPONSE FROM ROLESCALL 3 " + Strresponse);
      }

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }


      //
      // Read the contents of an entity and return it as a String.
      //
      
      // Extract record id from - <input type="hidden" name="record"
      // value="fc780bcc-8f2a-5f26-9a73-54b80b1a1b3f">
      String roleID = "";
      p = Pattern.compile("<input type=\\\"hidden\\\" name=\\\"record\\\" value=\\\"(.*)\\\".*");
      m = p.matcher(Strresponse);
      if (m.find())
      {
         System.out.println("FOUND Role ID " + m.group(1));
         roleID = m.group(1);

      }
      else
      {
         System.out.println("NOT FOUND Role ID " + m.group(1));
      }

      String findThis = "(select name=')([^']*)";
      // To verify disabled links - String findThis =
      // "(select DISABLED name=')([^']*)";
      Pattern myPattern = Pattern.compile(findThis);
      Matcher matcher = myPattern.matcher(Strresponse);
      Integer myCount = 0;

      boolean found = false;
      while (matcher.find())
      {
         // System.out.format("I found the text" + " \"%s\" starting at "
         // + "index %d and ending at index %d.%n", matcher.group(2),
         // matcher.start(), matcher.end());
         found = true;
         accessGUIName[myCount] = matcher.group(2);
         System.out.println(myCount + "---  " + accessGUIName[myCount]);
         myCount++;
      }
      if (!found)
      {
         System.out.print("No match found");
      }
      
      int countArray = myCount;
      
      System.out.print("NUMBER FOUND = " + countArray);
      
      findThis = "(id=\"ACL)(.*)(\")";      // To verify All ACL links - String findThis =
      // "(select DISABLED name=')([^']*)";
      myPattern = Pattern.compile(findThis);
     matcher = myPattern.matcher(Strresponse);
      myCount = 0;

      found = false;
      while (matcher.find())
      {
         // System.out.format("I found the text" + " \"%s\" starting at "
         // + "index %d and ending at index %d.%n", matcher.group(2),
         // matcher.start(), matcher.end());
         found = true;
         System.out.println("ACL " + " " + matcher.group(2));
         myCount++;
      }
      if (!found)
      {
         System.out.print("DISABLED "
               + "No match found");
      }
      
     countArray = myCount;
      
      System.out.print("NUMBER FOUND = " + countArray);


      postRequest.releaseConnection();

      // This request will update the Access Matrix for the Role just created
      postRequest = new HttpPost(baseURI + "index.php");
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      urlParameters = new ArrayList<NameValuePair>();
      // Add the basic form variables
      urlParameters.add(new BasicNameValuePair("record", roleID));
      urlParameters.add(new BasicNameValuePair("module", "ACLRoles"));
      urlParameters.add(new BasicNameValuePair("action", "Save"));
      urlParameters.add(new BasicNameValuePair("return_record", roleID));
      urlParameters.add(new BasicNameValuePair("record_action", "DetailView"));
      urlParameters.add(new BasicNameValuePair("return_module", "ACLRoles"));

      JSONArray myJsonArrayDefault = (JSONArray) jsonObjectDefault.get("roleAccessformParameters");
      Iterator<JSONObject> iteratorParmsDefault = myJsonArrayDefault.iterator();

      myJsonArrayDelta = (JSONArray) jsonObjectDelta.get("roleAccessformParameters");
      Iterator<JSONObject> iteratorParmsDelta = myJsonArrayDelta.iterator();

      Integer readDelta = 1;
      String myName1 = "";
      String myValue = "";
      JSONObject jsonObjectParmsDelta = jsonObjectDelta;

      // Add the Matrix parameters with the dynamic GUIDs previously retrieved
      // The delta values repalce the Not set Default Values
      myCount = 0;
      while (iteratorParmsDefault.hasNext())
      {
         JSONObject jsonObjectParms = iteratorParmsDefault.next();
       //  System.out.println("The Json Object = " + jsonObjectParms);
         if (readDelta > 0)
         {
            readDelta = 0;
            // So as not to read past the Delta array
            if (iteratorParmsDelta.hasNext())
            {
               jsonObjectParmsDelta = iteratorParmsDelta.next();
               myName1 = jsonObjectParmsDelta.get("name").toString();
            }
         }
         String myName = jsonObjectParms.get("name").toString();
        // System.out.println("myName: " + myName + " myName1: " + myName1);
         if (myName.equals(myName1))
         {
            readDelta = 1;
            myValue = jsonObjectParmsDelta.get("value").toString();
         } else
         {
            myValue = jsonObjectParms.get("value").toString();
         }

       //  System.out.println("BEFORE - PAIR FOUND  " + myCount + " " + accessGUIName[myCount] + " " + convertAccessType(myValue));
         urlParameters.add(new BasicNameValuePair(accessGUIName[myCount], convertAccessType(myValue)));
        // System.out.println("PAIR FOUND  " + myCount + " " + accessGUIName[myCount] + " " + convertAccessType(myValue));
         myCount++;
      }
      
      System.out.print("NUMBER FOUND = " + countArray);

      /*
       * Add the CRSF Token - Note this needs to be broken down to individual
       * HTTP Requests and Code needs REFACTORING for HTTP Client access
       */
      urlParameters.add(new BasicNameValuePair("csrf_token", csrfToken));

      // Update the Role with the Access Matrix
      postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));

      // The create Role response contains the id of the Role and the dynamic
      // name variables needed to create the subsequent Access matrix for the
      // Role

      myResponse = httpClient.execute(postRequest);
      responseEntity = myResponse.getEntity();
      if (responseEntity != null)
      {
         Strresponse = EntityUtils.toString(responseEntity);
         //System.out.println("RESPONSE FROM ROLESCALL 4 " + Strresponse);
      }

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      postRequest.releaseConnection();

      // This POST Sets category_name to All - This is what the GUI POSTS
      // after the above update
      postRequest = new HttpPost(baseURI + "index.php");
      // Un-comment for debugging via proxy
      // postRequest.setConfig(config);

      urlParameters = new ArrayList<NameValuePair>();
      // Add the basic form variables
      urlParameters.add(new BasicNameValuePair("module", "ACLRoles"));
      urlParameters.add(new BasicNameValuePair("action", "EditRole"));
      urlParameters.add(new BasicNameValuePair("record", roleID));
      urlParameters.add(new BasicNameValuePair("category_name", "All"));

      /*
       * Add the CRSF Token - Note this needs to be broken down to individual
       * HTTP Requests
       */
      urlParameters.add(new BasicNameValuePair("csrf_token", csrfToken));

      postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
      myResponse = httpClient.execute(postRequest);

      responseEntity = myResponse.getEntity();
      if (responseEntity != null)
      {
         Strresponse = EntityUtils.toString(responseEntity);
         //System.out.println("RESPONSE FROM ROLESCALL 5 " + Strresponse);
      }

      if (myResponse.getStatusLine().getStatusCode() != 200)
      {
         throw new RuntimeException("Failed : HTTP error code : " + myResponse.getStatusLine().getStatusCode());
      }

      postRequest.releaseConnection();
      httpClient.close();

   }

   private String convertAccessType(String accessString)
   {

      String accessType = "";

      switch (accessString)
      {
      case "All":
         accessType = "90";
         break;
      case "Owner & Selected Teams":
         accessType = "78";
         break;
      case "Admin":
         accessType = "99";
         break;
      case "Admin & Developer":
         accessType = "100";
         break;
      case "Developer":
         accessType = "95";
         break;
      case "Disabled":
         accessType = "-98";
         break;
      case "Enabled":
         accessType = "89";
         break;
      case "None":
         accessType = "99";
         break;
      case "Normal":
         accessType = "1";
         break;
      case "Not Set":
         accessType = "0";
         break;
      case "Owner":
         accessType = "75";
         break;
      default:
      }
      return accessType;
   }

}
