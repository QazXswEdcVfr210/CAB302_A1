package firebase;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import javafx.util.Pair;

import java.util.Map;

// This class is used to abstract certain functionality for use in interacting with Firestore DB (mostly CRUD operations)
public class FirestoreHandler {

    // Creates a document
    public static Pair<Boolean, String> CreateDocument(String collection, String document, Map<String, Object> data) throws Exception{
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            // URL to send our request to
            String firebaseUrl = "https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/" + collection + "?documentId=" + document;

            // Create payload
            FirebaseJSONPackage p = new FirebaseJSONPackage();
            Map<String, Object> packageData = p
                    .AddKVP("fields", data)
                    .getData();

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, packageData);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            // Handle response and return the name of the new document
            String responseBody = response.parseAsString();
            return new Pair<>(true, responseBody);

        } catch (HttpResponseException e) {
            //System.out.printf("Error setting up new user: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            e.printStackTrace();
            return new Pair<>(false, "ERROR CODE GOES HERE"); // TODO: Extract error code
        }
    }

    // Retrieves the contents of a document
    public static Pair<Boolean, String> GetDocument(String collection, String document) throws Exception {
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            // URL to send our request to
            String firebaseUrl = "https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/" + collection + "/" + document;

            // Make GET request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpResponse response = requestFactory.buildGetRequest(url).execute();

            // Handle response
            String responseBody = response.parseAsString();
            return new Pair<>(true, responseBody);

        } catch (HttpResponseException e) {
            e.printStackTrace();
            return new Pair<>(false, "ERROR CODE GOES HERE"); // TODO: Extract error code
        }
    }

    // Deletes a document
    public static Pair<Boolean, String> DeleteDocument(String collection, String document) throws Exception {
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            // URL to send our request to
            String firebaseUrl = "https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/" + collection + "/" + document;

            // Make DELETE request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpResponse response = requestFactory.buildDeleteRequest(url).execute();

            // Handle response and return the name of the new document
            String responseBody = response.parseAsString();
            return new Pair<>(true, responseBody);

        } catch (Exception e) {
            return new Pair<>(false, "FUNCTION NOT IMPLEMENTED");
        }

    }

    // TODO
    public static Boolean CheckDocumentExists(String name) {
        return false;
    }

    // TODO
    public static Pair<Boolean, String> CreateField(String name, Map<String, Object> data) {
        return new Pair<>(false, "FUNCTION NOT IMPLEMENTED");
    }

    // TODO
    public static Pair<Boolean, String> ReadFieldValue(String name) {
        return new Pair<>(false, "FUNCTION NOT IMPLEMENTED");
    }

    // Modifies a field value
    public static Pair<Boolean, String> ModifyFieldValue(String collection, String document, String field, Map<String, Object> data) throws Exception {

        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            // URL to send our request to
            String firebaseUrl = "https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/" + collection + "/" + document + "?updateMask.fieldPaths=" + field;

            // Create payload (for some reason this is what firebase requires to create two fields - one empty array called projectIDs and one string for the username
            FirebaseJSONPackage p = new FirebaseJSONPackage();
            Map<String, Object> packageData = p
                    .AddKVP("fields", data)
                    .getData();

            // Build PATCH request - because PATCH is not supported by our client we must build the request as a POST request and then override it
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, packageData);
            HttpRequest request = requestFactory.buildPostRequest(url, content);
            request.getHeaders().set("X-HTTP-Method-Override", "PATCH");
            HttpResponse response = request.execute();

            // Handle response and return the name of the new document
            String responseBody = response.parseAsString();
            return new Pair<>(true, responseBody);

        } catch (HttpResponseException e) {
            e.printStackTrace();
            return new Pair<>(false, "");
        }
    }

    // TODO
    public static Pair<Boolean, String> DeleteField(String name) {
        return new Pair<>(false, "FUNCTION NOT IMPLEMENTED");
    }

    // TODO
    public static Boolean CheckFieldExists(String name) {
        return false;
    }

}
