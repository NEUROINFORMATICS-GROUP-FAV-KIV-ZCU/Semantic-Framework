package thewebsemantic;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.Collection;
import java.util.Date;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Model;

class NullPropertyContext extends ValuesContext {

	PropertyDescriptor property;
	TypeWrapper type;


	public NullPropertyContext(TypeWrapper t, PropertyDescriptor p) {
		property = p;
		type = t;
	}
	
	
	@Override
	public boolean isAnnotatedBy(Class<? extends Annotation> annotationClass) {
		return property.getReadMethod().isAnnotationPresent(annotationClass);
	}
	
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return null;
	}


	@Override
	public String uri() {
		return type.uri(property.getReadMethod(), property.getName());
	}


	@Override
	public boolean existsInModel(Model m) {
		return m.getGraph().contains(NodeFactory.createURI(uri()), Node.ANY, Node.ANY);
	}


	@Override
	public Object invokeGetter() {
		throw new UnsupportedOperationException();
	}


	@Override
	public void setProperty(Object v) {
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean isURI() {
		return property.getPropertyType().equals(URI.class);
	}


	@Override
	public boolean isPrimitive() {
		return PrimitiveWrapper.isPrimitive(property.getPropertyType());
	}


	@Override
	public boolean isCollection() {
		return property.getPropertyType().equals(Collection.class);
	}


	@Override
	public boolean isArray() {
		return property.getPropertyType().isArray();
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
		ParameterizedType type = (ParameterizedType)
				property.getReadMethod().getGenericReturnType();
		return getGenericType(type);
	}


	@Override
	public boolean isId() {
		return false;
	}


	@Override
	public AccessibleObject getAccessibleObject() {
		return property.getReadMethod();
	}
	
	
	public TypeWrapper tw() {
		return type;
	}
	
	
	public boolean isDate() {
		return property.getPropertyType().equals(Date.class);
	}

	

}
