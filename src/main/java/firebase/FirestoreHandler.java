package firebase;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Map;

// This class is used to abstract certain functionality for use in interacting with Firestore DB (mostly CRUD operations)
public class FirestoreHandler {

    // Interacting with Documents
    public static Boolean CreateDocument(String collection, String document, Map<String, Object> data) throws Exception{
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            // URL to send our request to
            String firebaseUrl = "https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/" + collection + "?documentId=" + document;

            // Create payload (for some reason this is what firebase requires to create two fields - one empty array called projectIDs and one string for the username
            FirebaseJSONPackage p = new FirebaseJSONPackage();
            Map<String, Object> packageData = p
                    .AddKVP("fields", data)
                    .getData();

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, packageData);

            // Handle response
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();
            String responseBody = response.parseAsString();

            //PopulateNewUserDocument();
            return true;

        } catch (HttpResponseException e) {
            //System.out.printf("Error setting up new user: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean GetDocumentContents(String name) {
        return false;
    }

    public static Boolean DeleteDocument(String name) {
        return false;
    }

    // Interacting with Fields
    public static Boolean CreateField(String name, Map<String, Object> data) {
        return false;
    }

    public static Boolean ReadFieldValue(String name) {
        return false;
    }

    public static Boolean ModifyFieldValue(String name, String newValue) {
        return false;
    }

    public static Boolean DeleteField(String name) {
        return false;
    }

    public static Boolean CheckFieldExists(String name) {
        return false;
    }

}
