package thewebsemantic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.semanticweb.owlapi.vocab.OWL2Datatype;

import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.XSD;

/**
 * This class determines if selected class or object is a primitive type or
 * a data type, that internally supports Jena in its framework.
 * 
 * Also provides mapping for this datatypes.
 * 
 */
public class PrimitiveWrapper {

	private static final HashMap<Class<?>, Resource> TYPE_2_RESOURCE = new HashMap<Class<?>, Resource>();
	private static final HashMap<Class<?>, Resource> CLASS_2_RESOURCE = new HashMap<Class<?>, Resource>();

	/*
	 * static {
	 * TYPE_2_RESOURCE.put(Boolean.TYPE, XSD.xboolean);
	 * TYPE_2_RESOURCE.put(Byte.TYPE, XSD.xbyte);
	 * TYPE_2_RESOURCE.put(Character.TYPE, XSD.xstring);
	 * TYPE_2_RESOURCE.put(Short.TYPE, XSD.xshort);
	 * TYPE_2_RESOURCE.put(Integer.TYPE, XSD.xint);
	 * TYPE_2_RESOURCE.put(Long.TYPE, XSD.xlong);
	 * TYPE_2_RESOURCE.put(Float.TYPE, XSD.xfloat);
	 * TYPE_2_RESOURCE.put(Double.TYPE, XSD.xdouble);
	 * }
	 */

	static {
		TYPE_2_RESOURCE.put(Boolean.TYPE, XSD.integer);
		TYPE_2_RESOURCE.put(Byte.TYPE, XSD.integer);
		TYPE_2_RESOURCE.put(Character.TYPE, XSD.xstring);
		TYPE_2_RESOURCE.put(Short.TYPE, XSD.integer);
		TYPE_2_RESOURCE.put(Integer.TYPE, XSD.integer);
		TYPE_2_RESOURCE.put(Long.TYPE, XSD.integer);
		TYPE_2_RESOURCE.put(Float.TYPE,
				ResourceFactory.createResource(OWL2Datatype.OWL_REAL.getURI().toString()));
		TYPE_2_RESOURCE.put(Double.TYPE,
				ResourceFactory.createResource(OWL2Datatype.OWL_REAL.getURI().toString()));
	}

	/*
	 * static {
	 * CLASS_2_RESOURCE.put(Byte.class, XSD.xbyte);
	 * CLASS_2_RESOURCE.put(Short.class, XSD.xshort);
	 * CLASS_2_RESOURCE.put(Character.class, XSD.xstring);
	 * CLASS_2_RESOURCE.put(Integer.class, XSD.xint);
	 * CLASS_2_RESOURCE.put(Long.class, XSD.xlong);
	 * CLASS_2_RESOURCE.put(Float.class, XSD.xfloat);
	 * CLASS_2_RESOURCE.put(Double.class, XSD.xdouble);
	 * CLASS_2_RESOURCE.put(Boolean.class, XSD.xboolean);
	 * CLASS_2_RESOURCE.put(String.class, XSD.xstring);
	 * CLASS_2_RESOURCE.put(Date.class, XSD.dateTime);
	 * CLASS_2_RESOURCE.put(Calendar.class, XSD.dateTime);
	 * CLASS_2_RESOURCE.put(BigDecimal.class, XSD.xdouble);
	 * CLASS_2_RESOURCE.put(BigInteger.class, XSD.integer);
	 * CLASS_2_RESOURCE.put(LocalizedString.class, XSD.xstring);
	 * }
	 */

	static {
		CLASS_2_RESOURCE.put(Byte.class, XSD.integer);
		CLASS_2_RESOURCE.put(Short.class, XSD.integer);
		CLASS_2_RESOURCE.put(Character.class, XSD.xstring);
		CLASS_2_RESOURCE.put(Integer.class, XSD.integer);
		CLASS_2_RESOURCE.put(Long.class, XSD.integer);
		CLASS_2_RESOURCE.put(Float.class,
				ResourceFactory.createResource(OWL2Datatype.OWL_REAL.getURI().toString()));
		CLASS_2_RESOURCE.put(Double.class,
				ResourceFactory.createResource(OWL2Datatype.OWL_REAL.getURI().toString()));
		CLASS_2_RESOURCE.put(Boolean.class, XSD.integer);
		CLASS_2_RESOURCE.put(String.class, XSD.xstring);
		CLASS_2_RESOURCE.put(Date.class, XSD.dateTime);
		CLASS_2_RESOURCE.put(Calendar.class, XSD.dateTime);
		CLASS_2_RESOURCE.put(BigDecimal.class,
				ResourceFactory.createResource(OWL2Datatype.OWL_REAL.getURI().toString()));
		CLASS_2_RESOURCE.put(BigInteger.class, XSD.integer);
		CLASS_2_RESOURCE.put(LocalizedString.class, XSD.xstring);
	}


	/**
	 * Test if param class is a primitive type or it's descendant or is it
	 * a type that Jena internal support.
	 * 
	 * @param c - class to test
	 * @return true if primitive type, false otherwise
	 */
	public static boolean isPrimitive(Class<?> c) {
		return c.isPrimitive() || CLASS_2_RESOURCE.containsKey(c)
				|| CLASS_2_RESOURCE.containsKey(c.getSuperclass());
	}

	
	/**
	 * Test if param object is a primitive type or it's descendant or is it
	 * a type that Jena internal support.
	 * 
	 * @param o - object to test
	 * @return true if primitive, false otherwise
	 */
	public static boolean isPrimitive(Object o) {
		return isPrimitive(o.getClass());
	}


	/**
	 * Returns Jena's Resource to which the primitive type is mapped.
	 * 
	 * @param c
	 * @return resource representing primitive type
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
	
	
	/**
	 * Method creates a new TypedLiteral that encapsulates
	 * param Object o and return this created Literal.
	 * 
	 * Declared type of the literal corresponds
	 * to the datatype mapping used by this class.
	 * Type can be found out using getPrimitiveResource() method.
	 * 
	 * @param o object that will be encapsulated as a literal
	 * @return created Literal
	 */
	public static Literal toLiteral(Object o) {

		
		if (! isPrimitive(o.getClass()))
			return null;
		
		if (o instanceof Date) {
			Calendar c = Calendar.getInstance();
			c.setTime((Date) o);
			return ResourceFactory.createTypedLiteral(c);
		} else if (o instanceof Calendar) {
			return ResourceFactory.createTypedLiteral((Calendar) o);
		} else if (o instanceof Boolean) {
			boolean bool = ((Boolean) o).booleanValue();
			RDFDatatype type = new BaseDatatype(getPrimitiveResource(boolean.class).getURI());
			return ResourceFactory.createTypedLiteral(bool ? "1" : "0", type);
		} else {
			RDFDatatype type = new BaseDatatype(getPrimitiveResource(o.getClass()).getURI());
			return ResourceFactory.createTypedLiteral(o.toString(), type);
		}
	}
}
