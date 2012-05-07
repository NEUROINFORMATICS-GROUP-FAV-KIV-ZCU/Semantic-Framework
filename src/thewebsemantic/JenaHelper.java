package thewebsemantic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class JenaHelper {

	/**
	 * Converts Literal representaion of Jena tripleStored Node to propriate
	 * internal Java data type specified as class
	 * 
	 * @param node Source Node
	 * @param c Target data type
	 * @return converted Literal
	 */
	public static Object convertLiteral(RDFNode node, Class<?> c) {
		return convertLiteral(node.as(Literal.class), c);
	}


	/**
	 * Method converts Jena Literal to internal Java data type specified
	 * as class param
	 * 
	 * @param l Converted literal
	 * @param c target data type
	 * @return required Java datatype
	 */
	public static Object convertLiteral(Literal l, Class<?> c) {
		if (c.equals(Date.class)) {
			return date(l);
		} else if (c.equals(Calendar.class)) {
			return ((XSDDateTime) l.getValue()).asCalendar();
		} else if (c.equals(BigDecimal.class)) {
			return bigDecimal(l);
		} else if (Long.TYPE.equals(c)) {
			return l.getLong();
		} else if (Double.TYPE.equals(c)) {
			return l.getDouble();
		} else if (Character.TYPE.equals(c)) {
			return l.getValue().toString().charAt(0);
		} else if (Short.TYPE.equals(c)) {
			return l.getShort();
		} else if (LocalizedString.class.equals(c)) {
			return new LocalizedString(l);
		} else
			return l.getValue();
	}


	/**
	 * Converts literal to propriate Date format type to store in Jena
	 * Model
	 * 
	 * @param l
	 * @return instance of Date
	 */
	public static Date date(Literal l) {
		XSDDateTime date = (XSDDateTime) l.getValue();
		return date.asCalendar().getTime();
	}


	public static Object bigDecimal(Literal l) {
		Object o = l.getDouble();
		System.out.println(o.getClass());
		return null;
	}


	/**
	 * Method creates in Jena Model m a new TypedLiteral that encapsulates
	 * param Object o and return this created Literal.
	 * Object is encapsulated bease on descendant type of the param
	 * object so the Literal is created with propriate data type
	 * 
	 * @param m Target Jena Model
	 * @param o object that will be inster as Literal to Model
	 * @return created Literal
	 */
	public static Literal toLiteral(Model m, Object o) {
		if (o instanceof String)
			return m.createTypedLiteral(o.toString());
		else if (o instanceof Date) {
			Calendar c = Calendar.getInstance();
			c.setTime((Date) o);
			return m.createTypedLiteral(c);
		} else if (o instanceof Integer)
			return m.createTypedLiteral(((Integer) o).intValue());
		else if (o instanceof Long)
			return m.createTypedLiteral(((Long) o).longValue());
		else if (o instanceof Short)
			return m.createTypedLiteral((Short) o);
		else if (o instanceof Float)
			return m.createTypedLiteral(((Float) o).floatValue());
		else if (o instanceof Double)
			return m.createTypedLiteral(((Double) o).doubleValue());
		else if (o instanceof Character)
			return m.createTypedLiteral(((Character) o).charValue());
		else if (o instanceof Boolean)
			return m.createTypedLiteral(((Boolean) o).booleanValue());
		else if (o instanceof Calendar)
			return m.createTypedLiteral((Calendar) o);
		else if (o instanceof BigDecimal)
			return m.createTypedLiteral(((BigDecimal) o).doubleValue(), XSDDatatype.XSDdouble);
		else if (o instanceof BigInteger)
			return m.createTypedLiteral((BigInteger) o);
		else if (o instanceof URI)
			return m.createTypedLiteral(o, XSDDatatype.XSDanyURI);
		return null;
	}

}
