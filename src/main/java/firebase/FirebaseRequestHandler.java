package firebase;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.json.JsonHttpContent;

import java.util.HashMap;
import java.util.Map;

// This class handles making requests to Firebase through REST API requests.
// Down the line this might be replaced with making requests to cloud functions for security purposes.
public class FirebaseRequestHandler {

    public static void main(String[] args) throws Exception {
        TryLogin("nathcl0804@gmail.com", "N@than21012", true);
    }

    // Attempts login with provided credentials, if successful then returns true and stores UID and other user information.
    // TODO:
    // - integrate with LoginApplication or LoginController
    // - sign up functionality
    // probably move this to Cloud Functions if we want some extra marks for improving security
    public static Boolean TryLogin(String uname, String pass, Boolean bPrintResponse) throws Exception {
        try{
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();  // Using GsonFactory from google-http-client-gson

            // TODO: make my api key not exposed lol
            String firebaseAuthUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyA6q25fgqzmNdyO0jAYlWnSj259Aw7Dhr8";

            // Create payload
            Map<String, String> credentials = new HashMap<>();
            credentials.put("email", uname);
            credentials.put("password", pass);
            credentials.put("returnSecureToken", "true");

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseAuthUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, credentials);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            // Handle response (print response body to console if bPrintResponse is true.
            if(bPrintResponse) {
                String responseBody = response.parseAsString();
                System.out.println(responseBody);
            }

            // Unpack the user's UID and email and save to FirebaseDataStorage
            FirebaseJSONUnpacker.ExtractBasicUserInformationFromAuth(response.parseAsString(), true);

            return response.getStatusCode() == 200; // 200 response code means OK, everything else is treated as a login error

        } catch (Exception e) {
            // print to console if anything goes wrong
            e.printStackTrace();
            return false;
        }
    }
}
