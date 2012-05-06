package thewebsemantic;

import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.hibernate.proxy.HibernateProxy;

import thewebsemantic.annotations.Uri;

@SuppressWarnings("deprecation")
public class TypeWrapperFactory {

	public static Logger logger = Logger.getLogger("com.thewebsemantic");


	/**
	 * Method returns a TypeWrapper descendant instatnce based on bean class
	 * type or classes attributes annotations.
	 * If class is not an Enum or do not have any ID or URI annotatation it
	 * returns a basic TypeWrapper typed as DefaultTypeWrapper
	 * 
	 * @param c a bean class
	 * @return instance of TypeWrapper based on bean gathered info
	 */
	public static TypeWrapper newwrapper(Class<?> c) {
		
		// check if the class is a hibernate proxy
		if (HibernateProxy.class.isAssignableFrom(c))
			c = c.getSuperclass();
		
		BeanInfo info = TypeWrapper.beanInfo(c);
		
		if (c.isEnum())
			return new EnumTypeWrapper(c);
		
		for (MethodDescriptor md : info.getMethodDescriptors())
			if (isId(md))
				return new IdMethodTypeWrapper(c, md.getMethod());
			else if (isUri(md))
				return new UriMethodTypeWrapper(c, md.getMethod());

		// now try fields
		Field[] fields = Util.getDeclaredFields(c);
		for (Field f : fields)
			if (isId(f))
				return new IdFieldTypeWrapper(c, f, fields);

		return new DefaultTypeWrapper(c);
	}


	/**
	 * Return true if target object has annotation Id present
	 * 
	 * @param m target object
	 * @return True if annotation Id is present
	 */
	private static boolean isId(AccessibleObject m) {
		for (Annotation a : m.getAnnotations()) {
			if ("Id".equals(a.annotationType().getSimpleName()))
				return true;
		}
		return false;
	}


	/**
	 * Return true if MethodDescriptor contain an annotation Id
	 * 
	 * @param md Target method descriptor
	 * @return True if annoation is present
	 */
	private static boolean isId(MethodDescriptor md) {
		return isId(md.getMethod());
	}


	/**
	 * Returns if method contain annotation Uri
	 * 
	 * @param m Target method
	 * @return true if annoation is present
	 */
	private static boolean isUri(Method m) {
		return m.isAnnotationPresent(Uri.class);
	}


	/**
	 * Returns true if MethodDescriptor contain an annotation Uri
	 * 
	 * @param md Target method descriptor
	 * @return true if annotation is present
	 */
	private static boolean isUri(MethodDescriptor md) {
		return isUri(md.getMethod());
	}
}