package firebase;

import java.util.HashMap;
import java.util.Map;

/*
    This class is used to create custom JSON packages to send through REST API requests to pass data to various Firebase services.
    This is only used when a lot of custom data needs to be uploaded to the database, smaller scale interactions will be hardcoded.

    This was originally implemented by using google's gson.JsonArray and gson.JsonObject, however
    attempting to send JSON via a JsonObject instance results in no actual data sent when it is
    packaged with the JsonHttpContent class, however packaging JSON in a Map works fine.

    This class is an attempt at refactoring some of the code to make it more readable.
 */

public class FirebaseJSONPackage {

    // Read-only outside of this class.
    private Map<String, Object> data = new HashMap<String, Object>();

    public FirebaseJSONPackage() {
        data = new HashMap<String, Object>();
    }

    // Get data once all key value pairs have been added to the package.
    public Map<String, Object> getData() {
        return this.data;
    }

    // Adds a new field to our JSON package.
    public FirebaseJSONPackage AddKVP(String key, Object value) {
        data.put(key, value);
        return this;
    }

}
