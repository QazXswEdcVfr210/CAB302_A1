package firebase;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import javafx.util.Pair;

import java.io.File;

// NOTHING IN THIS CLASS IS FUNCTIONAL, GOOGLE MAKES A JAVA SDK FOR THIS ONE SERVICE IN PARTICULAR AND I AM NOW GOING TO MASTER IT IN 2 OR 3 HOURS

// This class is used to abstract certain functionality for use in interacting with Firebase Cloud Storage
public class CloudStorageHandler {

    // Uploads an image to the provided cloudDirectory
    public static void UploadImage(String cloudDirectory, File imageFile) throws Exception {
    }

    // Downloads an image from the provided cloudDirectory and returns it
    public static void DownloadImage(String cloudDirectory) throws Exception {

    }

    // Deletes an image at the provided cloudDirectory
    public static void DeleteImage(String cloudDirectory) throws Exception {

    }
}
