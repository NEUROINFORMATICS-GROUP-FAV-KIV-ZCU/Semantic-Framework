package thewebsemantic;
/**
 * This class is used to set a default namespace to whole Jena model
 * All objects created in model gathered from POJO data (excepting annotation
 * gahtered data)will have this namespace.
 * The only allowed exception is using the Namespace or RdfProperty to change
 * the object's namespace or the whole URI
 *
 * @author Dominik Šmíd
 */
public class MyNamespace {

    public static String myNamespace;

    public static String myNamespaceWithSlash;

    public static String myNamespaceWithSlashAndJavaclass;


    /**
     * Constructor sets the namespace value to all atributes that are used
     * in different pars of the JenaBean
     *
     * @param url String representation of namespace value
     */
    public MyNamespace(String url) {
        
        myNamespace = "http://" + url;
        myNamespaceWithSlash = myNamespace + "/";
        myNamespaceWithSlashAndJavaclass = myNamespaceWithSlash + "javaclass";
    }

}
