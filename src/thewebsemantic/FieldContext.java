package thewebsemantic;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Model;


/**
 * Encapsulates bean's fields (attributes).
 * Enables retrieving present annotations and data.
 */
class FieldContext extends ValuesContext {
	
	/** logger */
	private Log logger = LogFactory.getLog(getClass());

	/** encapsulated field */
	Field field;
	
	TypeWrapper type;
	
	// loaded attribute is set as ID
	boolean idField;


	public FieldContext(Object bean, Field p, boolean id) {
		subject = bean;
		field = p;
		// if (subject==null)
		type = TypeWrapper.wrap(p.getDeclaringClass());
		// else
		// type = TypeWrapper.type(bean);
		idField = id;
	}

	
	@Override
	public String uri() {
		return type.uri(field, field.getName());
	}


	@Override
	public boolean existsInModel(Model m) {
		return m.getGraph().contains(NodeFactory.createURI(uri()), Node.ANY, Node.ANY);
	}


	@Override
	public Object invokeGetter() {
		Object result = null;
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			result = field.get(subject);
		} catch (Exception e) {
			logger.warn("Error retrieving field value.", e);
		}
		
		// check if the field is char type and has null value
		if (field.getType() == char.class || field.getType() == Character.class) {
			if (((Character) result).charValue() == (char) 0x00)
				return null;
		}
		
		return result;
	}


	@Override
	public void setProperty(Object v) {
		try {
			field.setAccessible(true);
			field.set(subject, v);
		} catch (Exception e) {
			logger.warn("Could not set bean field " + field.getName(), e);
		}
	}


	@Override
	public boolean isPrimitive() {
		return PrimitiveWrapper.isPrimitive(field.getType());
	}

	
	@Override
	public Class<?> type() {
		return field.getType();
	}

	
	@Override
	public String getName() {
		return field.getName();
	}


	@Override
	public Class<?> t() {
		return getGenericType((ParameterizedType) field.getGenericType());
	}


	@Override
	public boolean isId() {
		return idField;
	}


	@Override
	public AccessibleObject getAccessibleObject() {
		return field;
	}
	
	
	@Override
	public boolean isAnnotatedBy(Class<? extends Annotation> annotationClass) {
		return field.isAnnotationPresent(annotationClass);
	}
	
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return field.getAnnotation(annotationClass);
	}
	
	
	public boolean isDate() {
		return type().equals(Date.class);
	}
	

}
