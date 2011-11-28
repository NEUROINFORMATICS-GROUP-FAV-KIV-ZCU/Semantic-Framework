package thewebsemantic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class determines if selected class or object is a primitive type or
 * a data type, that internally supports Jena in its framework.
 * 
 */

public class PrimitiveWrapper {
	private static final Set<Class<?>> WRAPPERS = new HashSet<Class<?>>();

	static {
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
	}

        /**
         * Test if param class is a primitive type or it's descendant or is it
         * a type that Jena internal support.
         *
         * @param c
         * @return
         */
	public static boolean isPrimitive(Class<?> c) {
		return c.isPrimitive() || WRAPPERS.contains(c) || WRAPPERS.contains(c.getSuperclass());
	}

	public static boolean isPrimitive(Object o) {
		return isPrimitive(o.getClass());
	}
}
