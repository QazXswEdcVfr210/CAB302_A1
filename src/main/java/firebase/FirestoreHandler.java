package firebase;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

// The purpose of this class is to abstract CRUD operations for use in other backend functions when interacting with Firestore
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
            Map<String, Object> packageData = new HashMap<>();
            packageData.put("fields", data);

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, packageData);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            // Handle response and return the name of the new document
            String responseBody = response.parseAsString();
            return new Pair<>(true, responseBody);

        } catch (HttpResponseException e) {
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
            return new Pair<>(false, "ERROR CODE GOES HERE");
        }
    }

    // Deletes a document
    public static Pair<Boolean, String> DeleteDocument(String collection, String document) throws Exception {
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();

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
            return new Pair<>(false, "ERROR DELETING DOCUMENT");
        }

    }

    // Checks to see if a certain document exists
    public static Boolean CheckDocumentExists(String collection, String document) throws Exception {
        try{
            Pair<Boolean, String> results = GetDocument(collection, document);
            return results.getKey();
        } catch (Exception e) {
            return false;
        }
    }

    // Modifies a field value
    public static Pair<Boolean, String> ModifyFieldValue(String collection, String document, String field, Map<String, Object> data) throws Exception {

        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            // URL to send our request to
            String firebaseUrl = "https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/" + collection + "/" + document + "?updateMask.fieldPaths=" + field;

            // Create Payload
            Map<String, Object> fields = new HashMap<>();
            fields.put("fields", data);

            // Build PATCH request - because PATCH is not supported by our client we must build the request as a POST request and then override it
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, fields);
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

    // Deletes a field from a document
    public static Pair<Boolean, String> DeleteField(String collection, String document, String field) throws Exception {
        try{
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();

            // URL to send our request to
            String firebaseUrl = "https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/" + collection + "/" + document + "?updateMask.fieldPaths=" + field;

            // Make PATCH request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(new GsonFactory(), new HashMap<>());
            HttpRequest request = requestFactory.buildPostRequest(url, content);
            request.getHeaders().set("X-HTTP-Method-Override", "PATCH");
            HttpResponse response = request.execute();

            // Handle response and return the name of the new document
            String responseBody = response.parseAsString();
            return new Pair<>(true, responseBody);

        } catch (Exception e) {
            e.printStackTrace();
            return new Pair<>(false, "ERROR DELETING FIELD");
        }
    }

}
