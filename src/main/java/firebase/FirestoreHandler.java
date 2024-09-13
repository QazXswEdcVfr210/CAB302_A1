package firebase;

// This class is used to abstract certain functionality for use in interacting with Firestore DB (mostly CRUD operations)
public class FirestoreHandler {

    // Interacting with Documents
    public static Boolean CreateDocument(String name) {
        return false;
    }

    public static Boolean GetDocumentContents(String name) {
        return false;
    }

    public static Boolean DeleteDocument(String name) {
        return false;
    }

    // Interacting with Fields
    public static Boolean CreateField(String name, String valueType, String value) {
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
