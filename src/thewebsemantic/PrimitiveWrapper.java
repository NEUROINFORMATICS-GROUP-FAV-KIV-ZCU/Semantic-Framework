package thewebsemantic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 * This class determines if selected class or object is a primitive type or
 * a data type, that internally supports Jena in its framework.
 * 
 */

public class PrimitiveWrapper {

	//private static final Set<Class<?>> WRAPPERS = new HashSet<Class<?>>();
	
	private static final HashMap<Class<?>, Resource> TYPE_2_RESOURCE = new HashMap<Class<?>, Resource>();
	private static final HashMap<Class<?>, Resource> CLASS_2_RESOURCE = new HashMap<Class<?>, Resource>();

	/*static {
		WRAPPERS.add(Byte.class);
		WRAPPERS.add(Short.class);
		WRAPPERS.add(Character.class);
		WRAPPERS.add(Integer.class);
		WRAPPERS.add(Long.class);
		WRAPPERS.add(Float.class);
		WRAPPERS.add(Double.class);
		WRAPPERS.add(Boolean.class);
		WRAPPERS.add(String.class);
		WRAPPERS.add(Date.class);
		WRAPPERS.add(Calendar.class);
		WRAPPERS.add(BigDecimal.class);
		WRAPPERS.add(BigInteger.class);
		WRAPPERS.add(LocalizedString.class);
	}*/
	
	static {
		TYPE_2_RESOURCE.put(Boolean.TYPE, XSD.xboolean);
		TYPE_2_RESOURCE.put(Byte.TYPE, XSD.xbyte);
		TYPE_2_RESOURCE.put(Character.TYPE, XSD.xstring);
		TYPE_2_RESOURCE.put(Short.TYPE, XSD.xshort);
		TYPE_2_RESOURCE.put(Integer.TYPE, XSD.xint);
		TYPE_2_RESOURCE.put(Long.TYPE, XSD.xlong);
		TYPE_2_RESOURCE.put(Float.TYPE, XSD.xfloat);
		TYPE_2_RESOURCE.put(Double.TYPE, XSD.xdouble);
	}
	
	static {
		CLASS_2_RESOURCE.put(Byte.class, XSD.xbyte);
		CLASS_2_RESOURCE.put(Short.class, XSD.xshort);
		CLASS_2_RESOURCE.put(Character.class, XSD.xstring);
		CLASS_2_RESOURCE.put(Integer.class, XSD.xint);
		CLASS_2_RESOURCE.put(Long.class, XSD.xlong);
		CLASS_2_RESOURCE.put(Float.class, XSD.xfloat);
		CLASS_2_RESOURCE.put(Double.class, XSD.xdouble);
		CLASS_2_RESOURCE.put(Boolean.class, XSD.xboolean);
		CLASS_2_RESOURCE.put(String.class, XSD.xstring);
		CLASS_2_RESOURCE.put(Date.class, XSD.dateTime);
		CLASS_2_RESOURCE.put(Calendar.class, XSD.dateTime);
		CLASS_2_RESOURCE.put(BigDecimal.class, XSD.xdouble);
		CLASS_2_RESOURCE.put(BigInteger.class, XSD.integer);
		CLASS_2_RESOURCE.put(LocalizedString.class, XSD.xstring);
	}


	/**
	 * Test if param class is a primitive type or it's descendant or is it
	 * a type that Jena internal support.
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isPrimitive(Class<?> c) {
		//return c.isPrimitive() || WRAPPERS.contains(c) || WRAPPERS.contains(c.getSuperclass());
		return c.isPrimitive() || CLASS_2_RESOURCE.containsKey(c) || CLASS_2_RESOURCE.containsKey(c.getSuperclass());
	}


	public static boolean isPrimitive(Object o) {
		return isPrimitive(o.getClass());
	}
	
	
	/**
	 * Returns Jena's Resource to which the primitive type is mapped.
	 * 
	 * @param c
	 * @return
	 */
	public static Resource getPrimitiveResource(Class<?> c) {
		if (c.isPrimitive())
			return TYPE_2_RESOURCE.get(c);
		Resource res = null;
		while (c != null) {
			if ((res = CLASS_2_RESOURCE.get(c)) != null)
				break;
			c = c.getSuperclass();
		}
		return res;
	}
}
