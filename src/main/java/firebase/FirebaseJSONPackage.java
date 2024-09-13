package firebase;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

// This class is used to create custom JSON packages to send through REST API requests to pass data to various Firebase services.
// This is only used when a lot of custom data needs to be uploaded to the database, smaller scale interactions will be hardcoded.
public class FirebaseJSONPackage {

    // Read-only outside of this class.
    private JsonObject jsonObject = new JsonObject();

    // Public getter
    public JsonObject getData() {
        return this.jsonObject;
    }

    // Adds a new string value to our JSON package.
    public void AddString(String name, String value) {
        JsonObject data = new JsonObject();
        data.addProperty("stringValue", value);
        jsonObject.add(name, data);
    }

    // Adds a new boolean value to our JSON package.
    public void AddBool(String name, Boolean value) {
        JsonObject data = new JsonObject();
        data.addProperty("integerValue", value);
        jsonObject.add(name, data);
    }

    // Adds a new integer value to our JSON package.
    public void AddInteger(String name, Integer value) {
        JsonObject data = new JsonObject();
        data.addProperty("booleanValue", value);
        jsonObject.add(name, data);
    }

    // Adds a new list/array of strings to our JSON package.
    public void AddList(String name, List<String> value) {
        JsonObject data = new JsonObject();
        JsonArray newArray = new JsonArray();

        for(String element:value) {
            JsonObject dataElement = new JsonObject();
            dataElement.addProperty("stringValue", element);
            newArray.add(dataElement);
        }

        data.add("arrayValue", newArray);
        jsonObject.add(name, data);
    }

}
