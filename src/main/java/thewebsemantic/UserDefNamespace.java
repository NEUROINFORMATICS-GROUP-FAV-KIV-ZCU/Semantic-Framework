package thewebsemantic;

/**
 * This class is used to set a user-defined namespace to whole Jena model. All
 * objects created in model gathered from POJO data (excepting annotation
 * gahtered data) will have this namespace. The only allowed exception is using
 * the Namespace or RdfProperty annotation to change the object's namespace or the whole
 * URI.
 * 
 * @author Jakub Krauz
 */
public class UserDefNamespace {

	private static String namespace;


	/**
	 * Sets the namespace value for the whole Jena model.
	 * 
	 * @param url namespace value
	 */
	public static void set(String url) {
		if (url == null)
			return;
		namespace = url.startsWith("http://") ? url : "http://" + url;
	}


	/**
	 * Answears if the namespace was set by user.
	 * 
	 * @return true if set, false otherwise
	 */
	public static boolean isSet() {
		return namespace != null;
	}


	/**
	 * Getter for the namespace value.
	 * 
	 * @return namespace value
	 */
	public static String getNamespace() {
		return namespace;
	}

}
