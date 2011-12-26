package thewebsemantic;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * FieldContext represents a descendent of ValuesContext class which
 * encapsulates beans attributes and its annotations. It overides abstract
 * methods to gather annotation informations from encapsulated attributes in
 * direct way or calling parent class methods if they are enough common to
 * retrieve data.
 * 
 */
class FieldContext extends ValuesContext {
	
	private Log logger = LogFactory.getLog(getClass());

	// loaded attribute
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
		return m.getGraph().contains(Node.createURI(uri()), Node.ANY, Node.ANY);
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
