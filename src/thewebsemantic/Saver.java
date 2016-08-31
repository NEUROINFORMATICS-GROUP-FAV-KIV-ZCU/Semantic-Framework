package thewebsemantic;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.*;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Class contains methods to detect Collection type of intput data, and
 * if the type is supported, it uses propriate Saver descendant class to save
 * data to Jena Model in a way defined in each descendant.
 * 
 */

public abstract class Saver {

	private static Map<Class<?>, Saver> lookup = new HashMap<Class<?>, Saver>();

	static {
		lookup.put(thewebsemantic.Resource.class, new ResourceSaver());
		lookup.put(Collection.class, new CollectionSaver());
		lookup.put(Set.class, new CollectionSaver());
		lookup.put(Vector.class, new CollectionSaver());
		lookup.put(List.class, new ListSaver());
		lookup.put(Array.class, new ArraySaver());
		lookup.put(URI.class, new ResourceSaver());
	}


	/**
	 * Test if Saver support param class type.
	 * 
	 * @param type
	 * @return true if saver supports the type, otherwise false
	 */
	public static boolean supports(Class<?> type) {
		return (type.isArray()) ? true : lookup.containsKey(type);
	}


	/**
	 * Returns propriate Saver descendant that supports param class data
	 * type
	 * 
	 * @param type
	 * @return saver for type
	 */
	public static Saver of(Class<?> type) {
		return (type.isArray()) ? lookup.get(Array.class) : lookup.get(type);
	}


	/**
	 * Saved object data to Model
	 * 
	 * @param writer
	 * @param subject
	 * @param property
	 * @param o
	 */
	public abstract void save(Bean2RDF writer, Resource subject, Property property, Object o);
}
