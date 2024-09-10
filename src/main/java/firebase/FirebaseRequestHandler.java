package firebase;

/*
############## THE HOLY GRAILS OF MAKING THIS THING WORK ##############
https://cloud.google.com/identity-platform/docs/reference/rest
https://firebase.google.com/docs/firestore/use-rest-api
https://firebase.google.com/docs/firestore/reference/rest/
######################################################################
*/

import com.google.api.Http;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.http.HttpResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// This class handles making requests to Firebase through REST API requests.
// Down the line this might be replaced with making requests to cloud functions for security purposes.
public class FirebaseRequestHandler {

    public static void main(String[] args) throws Exception{
        //TrySignup("nathcl0804@gmail.com", "N@than21012", "Nathan", true);
        //TryLogin("admin@admin.admin", "adminadmin", false);
    }

    // TODO: make my api key not exposed lol
    private static final String API_KEY = "AIzaSyA6q25fgqzmNdyO0jAYlWnSj259Aw7Dhr8";

    // Attempts login with provided credentials, if successful then returns true and stores UID and other user information.
    // TODO: integrate with LoginApplication or LoginController
    // TODO: USE THE createAuthUri RESOURCE TO **VERIFY** THAT THERE IS AN ACCOUNT WITH THE EMAIL FOR EXTRA SECURITY BEFORE SENDING PASSWORD INFO
    public static Boolean TryLogin(String email, String pass, Boolean bPrintResponse) throws Exception {
        try{
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            String firebaseUrl = String.format("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=%s", API_KEY);

            // Create payload
            Map<String, String> credentials = new HashMap<>();
            credentials.put("email", email);
            credentials.put("password", pass);
            credentials.put("returnSecureToken", "true");

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, credentials);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();
            String responseBody = response.parseAsString();

            // Handle response (print response body to console if bPrintResponse is true.
            if(bPrintResponse && response.getStatusCode() == 200) {
                System.out.println(responseBody);
            }

            // Unpack the user's UID and email and save to FirebaseDataStorage, also save their ID and refresh tokens as well.
            FirebaseJSONUnpacker.ExtractBasicUserInformationFromAuth(responseBody, true);
            FirebaseJSONUnpacker.ExtractTokens(responseBody);

            // Get the user's project list
            //GetProjectIds();
            SetUpNewUser();

            return response.getStatusCode() == 200; // 200 response code means OK, everything else is treated as a login error

        } catch (HttpResponseException e) {
            // Print to console if anything goes wrong
            System.out.printf("Error signing in: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            return false;
        }
    }

    // Attempts to create an account with the provided credentials, if successful then returns true,
    // if not then returns false and prints the error to the console.
    public static Boolean TrySignup(String email, String pass, String username, Boolean bPrintResponse) throws Exception {
        try{
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            String firebaseUrl = String.format("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=%s", API_KEY);

            // Create payload
            Map<String, String> signUpInfo = new HashMap<>();
            signUpInfo.put("email", email);
            signUpInfo.put("password", pass);
            signUpInfo.put("displayName", username);
            signUpInfo.put("returnSecureToken", "true");

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, signUpInfo);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            // Handle response (print response body to console if bPrintResponse is true.
            if(bPrintResponse) {
                String responseBody = response.parseAsString();
                System.out.println(responseBody);
            }

            // TODO: unpack data to see if the operation was a success or not.
            // TODO: sign in after making an account
            // TODO: send email after sign in

            return true;

        } catch (HttpResponseException e){
            // 400 = bad request
            System.out.printf("Error creating account: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            return false;
        }
    }

    // WIP - Gets the list of the current user's projects.
    public static Boolean GetProjectIds() throws Exception {
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            String firebaseUrl = String.format("https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/Users/%s/", FirebaseDataStorage.getUid());

            // Create payload
            Map<String, String> requestInfo = new HashMap<>();
            //requestInfo.put("Authorization", FirebaseDataStorage.getUserTokens().getKey());

            // Make GET request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpResponse response = requestFactory.buildGetRequest(url).execute();
            String responseBody = response.parseAsString();

            // Save list of project IDs to storage
            FirebaseJSONUnpacker.SaveUserProjectIDs(responseBody);
            return true;

        } catch (HttpResponseException e) {
            System.out.printf("Error getting user projects: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            return false;
        }
    }

    private static Boolean SetUpNewUser() throws Exception{
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            // Use test url for now
            String firebaseUrl = String.format("https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/Users", "TEST");

            // Create payload
            Map<String, Object> requestInfo = new HashMap<String, Object>();
            HashMap<String, Object> fields = new HashMap<String, Object>();
            requestInfo.put("fields", fields);
            fields.put("test", "testValue");

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, requestInfo);

            // Handle response
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();
            String responseBody = response.parseAsString();

            return true;

        } catch (HttpResponseException e) {
            //System.out.printf("Error setting up new user: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            e.printStackTrace();
            return false;
        }

    }
}
