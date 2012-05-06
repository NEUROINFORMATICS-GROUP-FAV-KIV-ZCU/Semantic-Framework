package thewebsemantic;

import static thewebsemantic.Util.last;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.proxy.HibernateProxy;

import thewebsemantic.annotations.Ignore;
import thewebsemantic.annotations.Namespace;
import thewebsemantic.annotations.RdfProperty;
import thewebsemantic.binding.Persistable;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Retrieves annotation information as well as other type related operations on
 * Classes. Keeps cached versions to minimize introspection work.
 * 
 */
public abstract class TypeWrapper {

	public static final String JENABEAN_PREFIX = "jenabean.prefix";
	private Log logger = LogFactory.getLog(getClass());
	private String NS;
	protected Class<?> c;
	protected BeanInfo info;
	protected Constructor<?> constructor;
	protected PropertyDescriptor[] descriptors;
	private static HashMap<Class<?>, TypeWrapper> cache = new HashMap<Class<?>, TypeWrapper>();
	private String prefix = null;


	/**
	 * Constructor retrives all neccasary informations of the bean that is
	 * wrapped.
	 * 
	 * @param <T> parent class of the bean
	 * @param c bean instance of the class
	 */
	protected <T> TypeWrapper(Class<T> c) {
		prefix = System.getProperty(JENABEAN_PREFIX);
		this.c = c;
		info = beanInfo(c);
		Namespace nsa = c.getAnnotation(Namespace.class);
		NS = (nsa != null) ? nsa.value() : getNamespace(c);
		try {
			constructor = c.getConstructor(String.class);
		} catch (Exception e) {
		}
		cache.put(c, this);
	}


	/**
	 * Method returns a String representaion namespace of selected class by
	 * adding a static defined prefix in class UserDefNamespace (if set) or a
	 * prefix extracted from class' package.
	 * 
	 * @param c Target class
	 * @return String representaion of classes namespace
	 */
	private String getNamespace(Class<?> c) {
		return UserDefNamespace.isSet() ? UserDefNamespace.getNamespace()
				: getNamespaceFromPackage(c);
	}


	/**
	 * Method returns a String representaion namespace of selected class by
	 * adding an unified prefix to classes full name
	 * 
	 * @param c Target class
	 * @return String representaion of classes namespace
	 */
	private String getNamespaceFromPackage(Class<?> c) {
		return (c.getPackage() == null) ? "http://default.package" : "http://"
				+ c.getPackage().getName();
	}


	/**
	 * Method returns a TypeWrapper instance based on object's bean class and
	 * its info.
	 * 
	 * @param o instance of bean
	 * @return instance of TypeWrapper
	 */
	public static synchronized TypeWrapper type(Object o) {
		if (o instanceof HibernateProxy) {
			return wrap(o.getClass().getSuperclass());
		}		
		else if (o instanceof Persistable)
			return wrap(o.getClass().getSuperclass());
		else
			return wrap(o.getClass());
	}

	
	/**
	 * Returns the ID of the bean.
	 * @param o - bean
	 * @return ID of the bean
	 */
	public static String getId(Object o) {
		return type(o).id(o);
	}

	
	/**
	 * Method retrives nonTransient (not annotated by @Ignore) getters
	 * of this class and returns it as a field of properties wrapped
	 * as instances of ValueContext that can JenaBean use.
	 * 
	 * @param o - bean
	 * @return Field of ValueContext of classes getters
	 */
	public static ValuesContext[] valueContexts(Object o) {
		return type(o).getValueContexts(o);
	}


	/**
	 * Method retrives nonTransient (not annotated by @Ignore) getters
	 * of this class and returns it as a field of properties wrapped
	 * as instances of ValueContext that can JenaBean use.
	 * 
	 * @param o
	 * @return Field of ValueContext of classes getters
	 */
	public ValuesContext[] getValueContexts(Object o) {

		ArrayList<ValuesContext> values = new ArrayList<ValuesContext>();
		for (PropertyDescriptor property : descriptors()) {
			if (property.getReadMethod().isAnnotationPresent(Ignore.class))
				continue;
			values.add(new PropertyContext(o, property));
		}			
		return values.toArray(new ValuesContext[0]);

	}


	/**
	 * Method returns an instance of NullPropertyContext based on the param name
	 * of property if this class includes it. Otherwise returns null.
	 * 
	 * @param name Name of the property
	 * @return new instance of NullPropertyContext
	 */
	public ValuesContext getProperty(String name) {
		for (PropertyDescriptor p : descriptors()) {
			if (p.getName().equals(name))
				return new NullPropertyContext(this, p);
		}
		return null;
	}


	/**
	 * Method uses TypeWrapperFactory to wrap selected bean class as an instance
	 * of TypeWrapper based on bean info
	 * 
	 * @param c Target bean
	 * @return wrapped bean
	 */
	public static synchronized TypeWrapper wrap(Class<?> c) {
		return (cache.containsKey(c)) ? cache.get(c) : TypeWrapperFactory.newwrapper(c);
	}


	/**
	 * <p>
	 * Returns URI name of this class.<br>
	 * The URI consists of <code>namespace</code> and <code>className</code> according
	 * to the following schema: <code>http://namespace#className</code>.
	 * </p>
	 * <p>
	 * There are three ways how the <code>namespace</code> can be set:<br>
	 * - using the <code>@Namespace</code> annotation for the target class (the highest priority)<br>
	 * - setting the "global" <code>namespace</code> in the JenaBeanExtensionTool constructor<br>
	 * - if not set explicitly, the default <code>namespace</code> from the class' package is created
	 * 
	 * @return URI of the class
	 */
	public String typeUri() {
		return NS + "#" + Util.getRdfType(c);
	}


	/**
	 * Returns URI of the target param class.
	 * 
	 * @param c target class
	 * @return URI name
	 */
	public static String typeUri(Class<?> c) {
		return wrap(c).typeUri();
	}


	/**
	 * Method returns a field contatining PropertyDescriptors that has setters
	 * and getters over the classes attributes.
	 * 
	 * @return field of PropertyDescriptors
	 */
	protected PropertyDescriptor[] descriptors() {
		if (descriptors == null) {
			Collection<PropertyDescriptor> results = new LinkedList<PropertyDescriptor>();
			for (PropertyDescriptor p : info.getPropertyDescriptors())
				if (p.getWriteMethod() != null && p.getReadMethod() != null)
					results.add(p);
			descriptors = results.toArray(new PropertyDescriptor[0]);
		}
		return descriptors;
	}


	/**
	 * Method returns a field containig names of wrapped classes setters that
	 * sets to the classes attributes instances of Java.util.Collection.
	 * 
	 * @return field of setter's names
	 */
	public String[] collections() {
		Collection<String> results = new LinkedList<String>();
		for (PropertyDescriptor p : info.getPropertyDescriptors())
			if (p.getWriteMethod() != null && p.getPropertyType().equals(Collection.class))
				results.add(p.getName());
		return results.toArray(new String[0]);
	}


	public String namespace() {
		return NS;
	}


	public abstract String uri(String id);


	/**
	 * Returns URI identificator if target bean.
	 * 
	 * @param bean target bean
	 * @return URI
	 */
	public String uri(Object bean) {
		return uri(id(bean));
	}


	/**
	 * Method returns String represented URI identificator of target object
	 * based on object's annotations. If annotation is not preset it returns a
	 * URI based on param name extended about prefixes
	 * 
	 * @param m target method or attribute of the bean
	 * @param name simple name of the object
	 * @return URI identificator of the object
	 */
	public String uri(AccessibleObject m, String name) {
		RdfProperty rdf = getRDFAnnotation(m);
		return ("".equals(rdf.value())) ? namingPatternUri(name) : rdf.value();
	}


	/**
	 * Method retrives annotation data over the selected bean's object and
	 * returns is as instatnce of RdfProperty annotation.
	 * 
	 * @param m target object of the bean
	 * @return Rdf annotation of the object
	 */
	protected static RdfProperty getRDFAnnotation(AccessibleObject m) {
		return (m.isAnnotationPresent(RdfProperty.class)) ? m.getAnnotation(RdfProperty.class)
				: new NullRdfProperty();
	}


	/**
	 * Method returns a full URI identificator over the selected object using
	 * it's name
	 * 
	 * @param name String name of the target object
	 * @return String represented URI identificator
	 */
	private String namingPatternUri(String name) {
		return namespace() + "#" + prefix(name);
	}


	/**
	 * Method adds a prefix to selected String represented identificator of the
	 * object and returns it in propriate format
	 * 
	 * @param p object's String name
	 * @return object's name with prefix
	 */
	private String prefix(String p) {
		return (prefix != null) ? prefix + Util.toProperCase(p) : p;
	}


	/**
	 * Returns the URI identificator of target bean.
	 * 
	 * @param bean Target bean
	 * @return URI identificator
	 */
	public static String instanceURI(Object bean) {
		return type(bean).uri(bean);
	}


	/**
	 * Returns the ID of the bean
	 * 
	 * @param bean
	 * @return String representation of id
	 */
	public abstract String id(Object bean);


	/**
	 * Returns a beanInfo of selected classi if present
	 * 
	 * @param c target class
	 * @return classes beanInfo if present or null
	 */
	protected static BeanInfo beanInfo(Class<?> c) {
		try {
			return Introspector.getBeanInfo(c);
		} catch (IntrospectionException e1) {
			e1.printStackTrace();
		}
		return null;
	}


	/**
	 * Method invokes selected method over the selected bean and returns the
	 * String representaion of method return object
	 * 
	 * @param bean methods parrent bean class
	 * @param me target method
	 * @return method's returning object
	 */
	protected String invokeMethod(Object bean, Method me) {
		try {
			Object o = me.invoke(bean);
			return (o == null) ? null : o.toString();
		} catch (Exception e) {
			logger.warn("Failed invoking method " + me.getName() + " on class "
					+ bean.getClass(), e);
		}
		return null;
	}


	/**
	 * Method returns a Java bean based on the URI name of selected resource.
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public final Object toBean(Resource source) {
		return toBean(source.getURI());
	}


	/**
	 * Method returns a Java bean based on the URI name of selected resource
	 * using its constructor
	 * 
	 * @param uri String representation of resource's URI
	 * @return java Bean
	 */
	public Object toBean(String uri) {
		try {
			// last gets the id off the end of the URI
			return (constructor != null) ? constructor.newInstance(last(uri)) : c.newInstance();
		} catch (Exception e) {
			logger.warn("Exception caught while invoking default constructor on " + c, e);
		}
		return null;
	}


	public abstract Object toProxyBean(Resource source, AnnotationHelper jpa);

}
