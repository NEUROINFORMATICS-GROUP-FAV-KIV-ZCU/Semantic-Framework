package thewebsemantic;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.ParameterizedType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Encapsulates bean's properties (accessor methods).
 * Enables retrieving present annotations and data.
 */
class PropertyContext extends ValuesContext {
	
	/** logger */
	private Log logger = LogFactory.getLog(getClass());
	
	/** encapsulated property */
	PropertyDescriptor property;
	
	TypeWrapper type;
	
	// encapsulated property is set as ID
	boolean idmethod = false;


	public PropertyContext(Object bean, PropertyDescriptor p) {
		subject = bean;
		property = p;
		type = TypeWrapper.type(bean);
	}


	public PropertyContext(Object bean, PropertyDescriptor p, boolean b) {
		this(bean, p);
		idmethod = b;
	}
	
	
	@Override
	public boolean isAnnotatedBy(Class<? extends Annotation> annotationClass) {
		return property.getReadMethod().isAnnotationPresent(annotationClass);
	}
	
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return property.getReadMethod().getAnnotation(annotationClass);
	}


	@Override
	public String uri() {
		return type.uri(property.getReadMethod(), property.getName());
	}


	@Override
	public boolean existsInModel(Model m) {
		return m.getGraph().contains(Node.createURI(uri()), Node.ANY, Node.ANY);
	}


	@Override
	public Object invokeGetter() {		
		Object result = null;
		try {
			result = property.getReadMethod().invoke(subject);
		} catch (Exception e) {
			logger.warn("Error calling read method.", e);
		}
		
		// check if the property is char type and has null value
		if (property.getPropertyType() == char.class || property.getPropertyType() == Character.class) {
			if (((Character) result).charValue() == (char) 0x00)
				return null;
		}
		
		return result;
	}


	@Override
	public void setProperty(Object v) {
		try {
			property.getWriteMethod().invoke(subject, v);
		} catch (Exception e) {
			logger.warn("Error calling write method.", e);
		}
	}


	@Override
	public boolean isPrimitive() {
		return PrimitiveWrapper.isPrimitive(property.getPropertyType());
	}


	@Override
	public Class<?> type() {
		return property.getPropertyType();
	}


	@Override
	public String getName() {
		return property.getName();
	}

	
	@Override
	public Class<?> t() {
		// na portale byl problem s pretypovavanim u objektu od hibernate
		// prozatim vyreseno zachycenim vyjimky
		try {
			ParameterizedType type = (ParameterizedType) property.getReadMethod().getGenericReturnType();
			return getGenericType(type);
		} catch (ClassCastException e) {
			logger.error("Cannot cast to ParameterizedType: " + property.getReadMethod().getGenericReturnType());
			return null;
		}
	}


	@Override
	public boolean isId() {
		return idmethod;
	}


	@Override
	public AccessibleObject getAccessibleObject() {
		return property.getReadMethod();
	}
	
	
	public TypeWrapper tw() {
		return type;
	}


}
