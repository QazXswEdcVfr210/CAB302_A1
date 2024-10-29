package firebase;

/*
############## THE HOLY GRAILS OF MAKING THIS THING WORK ##############
https://cloud.google.com/identity-platform/docs/reference/rest
https://firebase.google.com/docs/firestore/use-rest-api
https://firebase.google.com/docs/firestore/reference/rest/
######################################################################
*/

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.json.JsonHttpContent;

import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

import org.apache.commons.lang3.RandomStringUtils;

/*
    This class handles making requests to Firebase through REST API requests.
    Down the line this might be replaced with making requests to cloud functions for security purposes.
    The purpose of this class is to abstract complex backend functionality into easy to use functions for frontend use.
 */

public class FirebaseRequestHandler {

    public static void main(String[] args) throws Exception{
        TryLogin("admin@admin.admin", "adminadmin", false);
    }

    // Not a security risk as auth keys are distributed on user login
    private static final String FirebaseID = "AIzaSyA6q25fgqzmNdyO0jAYlWnSj259Aw7Dhr8";

    // Attempts login with provided credentials, if successful then returns true and stores UID and other user information.
    public static Boolean TryLogin(String email, String pass, Boolean bPrintResponse) throws Exception {
        try{
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            String firebaseUrl = String.format("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=%s", FirebaseID);

            // Create payload
            FirebaseJSONPackage p = new FirebaseJSONPackage();
            Map<String, Object> data = p
                    .AddKVP("email", email)
                    .AddKVP("password", pass)
                    .AddKVP("returnSecureToken", "true")
                    .getData();

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, data);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            // Handle response (print response body to console if bPrintResponse is true.
            String responseBody = response.parseAsString();
            if(bPrintResponse && response.getStatusCode() == 200) {
                System.out.println(responseBody);
            }

            // Unpack the user's UID and email and save to FirebaseDataStorage, also save their ID and refresh tokens as well.
            FirebaseJSONUnpacker.ExtractBasicUserInformationFromAuth(responseBody, true);
            FirebaseJSONUnpacker.ExtractTokens(responseBody);

            // Get the user's project list
            GetProjectIds();

            // ######################### DEBUG GOES HERE #########################

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
            String firebaseUrl = String.format("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=%s", FirebaseID);

            // Create payload
            FirebaseJSONPackage p = new FirebaseJSONPackage();
            Map<String, Object> data = p
                    .AddKVP("email", email)
                    .AddKVP("password", pass)
                    .AddKVP("displayName", username)
                    .AddKVP("returnSecureToken", "true")
                    .getData();


            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, data);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            // Handle response (print response body to console if bPrintResponse is true.
            if(bPrintResponse) {
                String responseBody = response.parseAsString();
                System.out.println(responseBody);
            }

            // TODO: unpack data to see if the operation was a success or not.
            // TODO: sign in after making an account
            // TODO: send email after sign in

            SetUpNewUser();

            return true;

        } catch (HttpResponseException e){
            // 400 = bad request
            System.out.printf("Error creating account: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            return false;
        }
    }

    // Creates a new project, the returned string is either representative of success or an error code
    public static String CreateProject(String _projectName, String _projectDescription) throws Exception {
        try{

            // Generate random project ID and check if project exists
            String projectID = "";
            boolean cont = true;
            while(cont) {
                projectID = RandomStringUtils.randomAlphabetic(16);
                cont = FirestoreHandler.CheckDocumentExists(projectID);
            }

            // Create payload
            Map<String, Object> projectFields = new HashMap<String, Object>();

            // Add project name to payload
            Map<String, Object> projectName = new HashMap<String, Object>();
            projectFields.put("projectName", projectName);
            projectName.put("stringValue", _projectName);

            // Add project description to payload
            Map<String, Object> projectDescription = new HashMap<String, Object>();
            projectFields.put("projectDescription", projectDescription);
            projectDescription.put("stringValue", _projectDescription);

            // Add project steps to payload
            Map<String, Object> projectSteps = new HashMap<String, Object>();
            projectFields.put("projectSteps", projectSteps);
            projectSteps.put("arrayValue", new HashMap<String, Object>());

            // Create a new document with a randomly-generated projectID using the data provided in the projectFields payload
            Pair<Boolean, String> results = FirestoreHandler.CreateDocument("Projects", projectID, projectFields);

            // TODO: Add reference to this project in Users/{uid}/projectIDs
            // If the document was successfully created, add a reference to the project in the user's list of projectIDs
            if(results.getKey()) {
                Map<String, Object> userFields = new HashMap<String, Object>();     // Main kvp to be sent
                Map<String, Object> projectIDs = new HashMap<String, Object>();     // projectIDs field data
                userFields.put("projectIDs", projectIDs);              // Add projectIDs to main kvp
                projectIDs.put("arrayValue", projectID);               // Add projectID to projectIDs as an arrayValue

                FirestoreHandler.ModifyFieldValue("Users", FirebaseDataStorage.getUid(), "projectIDs", userFields);
            }

            return "success";

        } catch (HttpResponseException e) {
            // Print to console if anything goes wrong
            return FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent());
        }
    }

    // TODO: Creates a new project step and adds it to a project
    public static Boolean CreateProjectStep(String _projectID, String _projectStepName, String _projectStepDescription) throws Exception {
        return false;
    }

    // ######## these functions aren't used outside of this script and now live at the bottom of this page ########

    // Gets the list of the current user's projects.
    private static void GetProjectIds() throws Exception {
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            String firebaseUrl = "https://firestore.googleapis.com/v1/projects/cab302a1/databases/projectdb/documents/Users/" + FirebaseDataStorage.getUid();

            // Make GET request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpResponse response = requestFactory.buildGetRequest(url).execute();
            String responseBody = response.parseAsString();

            // Save list of project IDs to storage
            FirebaseJSONUnpacker.SaveUserProjectIDs(responseBody);

        } catch (HttpResponseException e) {
            System.out.printf("Error getting user projects: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
        }
    }

    // Creates a new user document, then populates the new document with required fields.
    private static void SetUpNewUser() throws Exception {
        try {
            // Create payload (for some reason this is what firebase requires to create two fields - one empty array called projectIDs and one string for the username
            Map<String, Object> fields = new HashMap<String, Object>();
            Map<String, Object> username = new HashMap<String, Object>();
            Map<String, Object> projectIDs = new HashMap<String, Object>();

            fields.put("username", username);
            username.put("stringValue", "testUsername");

            fields.put("projectIDs", projectIDs);
            projectIDs.put("arrayValue", new HashMap<String, Object>());

            FirestoreHandler.CreateDocument("Users", FirebaseDataStorage.getUid(), fields);

        } catch (HttpResponseException e) {
            System.out.printf("Error setting up new user: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
        }
    }
}
