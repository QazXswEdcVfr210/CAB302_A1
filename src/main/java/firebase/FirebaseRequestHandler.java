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

    // Not a security risk as auth keys are distributed on user login
    private static final String FirebaseID = "AIzaSyA6q25fgqzmNdyO0jAYlWnSj259Aw7Dhr8";

    // Attempts login with provided credentials, if successful then returns true and stores UID and other user information.
    // TODO: USE THE createAuthUri RESOURCE TO **VERIFY** THAT THERE IS AN ACCOUNT WITH THE EMAIL FOR EXTRA SECURITY BEFORE SENDING PASSWORD INFO
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



    // Creates a new project step
    public static Boolean CreateProjectStep(String _projectName, String _projectStepName, String _projectStepDescription) throws Exception {
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

            username.put("stringValue", "testUsername"); // TODO: remove placeholder


            fields.put("projectIDs", projectIDs);
            projectIDs.put("arrayValue", new HashMap<String, Object>());

            FirestoreHandler.CreateDocument("Users", FirebaseDataStorage.getUid(), fields);

        } catch (HttpResponseException e) {
            System.out.printf("Error setting up new user: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
        }
    }


    // Creates a new project, the returned string is either representative of success or an error code
    public static String CreateProject(String _projectName, String _projectDescription) throws Exception {
        try{

            // Generate random project ID and check if project exists
            String projectID = "";
            boolean cont = false;
            while(cont) {
                projectID = RandomStringUtils.randomAlphabetic(16);
                cont = FirestoreHandler.CheckDocumentExists(projectID);
            }

            // Create payload
            Map<String, Object> fields = new HashMap<String, Object>();

            Map<String, Object> projectName = new HashMap<String, Object>();
            fields.put("projectName", projectName);
            projectName.put("stringValue", _projectName);

            Map<String, Object> projectDescription = new HashMap<String, Object>();
            fields.put("projectDescription", projectDescription);
            projectDescription.put("stringValue", _projectDescription);

            Map<String, Object> projectSteps = new HashMap<String, Object>();
            fields.put("projectSteps", projectSteps);
            projectSteps.put("arrayValue", new HashMap<String, Object>());

            Pair<Boolean, String> results = FirestoreHandler.CreateDocument("Projects", projectID, fields);

            return "success";

        } catch (HttpResponseException e) {
            // Print to console if anything goes wrong
            return FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent());
        }
    }



    // TODO: Create functions MakePostRequest(), MakeGetRequest()
}
