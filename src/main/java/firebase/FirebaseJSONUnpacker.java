package firebase;

import javafx.util.Pair;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

// This is a class that unpacks JSON responses received from Firebase and extracts context-specific data.
public class FirebaseJSONUnpacker {

    // This extracts the UID and email address of the user from an authentication response. Used for user profile data.
    // Set bSaveToStorage if we want to save the extracted data to FirebaseDataStorage.
    public static Pair<String, String> ExtractBasicUserInformationFromAuth(String json, Boolean bSaveToStorage) throws Exception {
        try{
            // Google-recommended way to parse JSON
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            // Extract the things we need
            String uid = jsonObject.get("localId").getAsString();
            String email = jsonObject.get("email").getAsString();

            // Save to FirebaseDataStorage if bSaveToStorage is true.
            if(bSaveToStorage) {
                FirebaseDataStorage.setUid(uid);
                FirebaseDataStorage.setUserEmail(email);
            }

            return new Pair<>(uid, email);

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String ExtractBadRequestErrorMessage(String json) throws Exception {
        try{
            // Google-recommended way to parse JSON
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            // Extract the things we need
            JsonObject error = jsonObject.get("error").getAsJsonObject();
            return error.get("message").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
