package thewebsemantic.semantAnnot;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * This class contains static methods creating model Resources from gathered
 * data
 *
 * @author Filip Markvart
 */
public class ResourceCreator {

    /**
     * Method creates a dataRange restriction Resource constructed
     * by String value
     * 
     * @param value Restriction value of XSD dataType restriction
     * @return restriction Resource
     */
    public static Resource crDataRangeRest(String value) {

        if (value.equals("")) return null;

        try {
            @SuppressWarnings("rawtypes")
			Class dataTypes = Class.forName("com.hp.hpl.jena.vocabulary.XSD");
            Resource res = (Resource) dataTypes.getDeclaredField(value).get(null);
            return res;

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (NoSuchFieldException ex) {
            
            // URI adress for user defined XSD style
            return getUserDefXSDRes(value);

        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {

            System.out.println("Error: Can not find Jena vocabulary XSD class.");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Method returns Resource containing URI to user defined
     * XSD style uesed as data Restriction
     *
     * @param value String representation of URI XSD style
     * @return Resource containing URI to xsd style
     */
    private static Resource getUserDefXSDRes(String value) {
        
        return ResourceFactory.createResource(value);
    }
}
