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

public class FirebaseLogin {

    // Attempts login with provided credentials, if successful then returns true and stores UID and other user information.
    // TODO:
    // - actually store user info/UID
    // - integrate with LoginApplication or LoginController
    // - sign up functionality
    private Boolean TryLogin(String uname, String pass) throws Exception {
        try{
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();  // Using GsonFactory from google-http-client-gson

            String firebaseAuthUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyA6q25fgqzmNdyO0jAYlWnSj259Aw7Dhr8";

            Map<String, String> credentials = new HashMap<>();
            credentials.put("email", uname);
            credentials.put("password", pass);
            credentials.put("returnSecureToken", "true");

            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseAuthUrl);

            HttpContent content = new JsonHttpContent(jsonFactory, credentials);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            String responseBody = response.parseAsString();
            System.out.println(responseBody);

            // 200 response code means OK, everything else is treated as a login error
            return response.getStatusCode() == 200;

        } catch (Exception e) {
            // print to console if anything goes wrong
            e.printStackTrace();
            return false;
        }
    }
}
