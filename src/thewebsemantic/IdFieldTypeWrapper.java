package thewebsemantic;

import static thewebsemantic.Util.last;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import thewebsemantic.annotations.Transient;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * IdFieldTypeWrapper represents a TypeWrapper that wraps bean's attribute
 * that has the Id annotation which represents attribute's values as a key
 * values that are used as unicate keys when storing data to Model.
 */
public class IdFieldTypeWrapper extends TypeWrapper {

	private Log logger = LogFactory.getLog(getClass());
	private Field idfield;
	private Field[] fields;
	private boolean uriid = false;
	//private Constructor uriConstructor;
	
	public IdFieldTypeWrapper(Class<?> c, Field f, Field[] fields) {
		super(c);
		idfield = f;
		idfield.setAccessible(true);
		uriid = idfield.getType().equals(URI.class);
		this.fields = fields;
	}

        /**
         * Returns the value of this field specified by bean in param
         * @param bean
         * @return
         */
	@Override
	public String id(Object bean) {
		Object result=null;
		try {
			if (! idfield.isAccessible() )
				idfield.setAccessible(true);
			result = idfield.get(bean);
		} catch (Exception e) {
			logger.warn("Error retrieving id field value.", e);
		}
		return result.toString();
	}

	
	@Override
	public String uri(String id) {
		if (uriid)
			return id;
		else
			return  typeUri() + '_' + urlencode(id);
	}

	private String urlencode(String id) {
		try {
			return URLEncoder.encode(id, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

        @Override
 	public String[] collections() {
		Collection<String> results = new LinkedList<String>();
		for (Field field : fields)
			if (field.getType().equals(Collection.class))
				results.add(field.getName());
		return results.toArray(new String[0]);
	}

        @Override
	public ValuesContext getProperty(String name) {
		try {
			Field f = c.getDeclaredField(name);
			return new FieldContext(null, f, f.equals(idfield));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

        @Override
  	public ValuesContext[] getValueContexts(Object o) {
		ArrayList<FieldContext> values = new ArrayList<FieldContext>();
		for (Field field : fields) {
			if (field.equals(idfield) && uriid)
				continue;
			if (field.isAnnotationPresent(Transient.class))
				 continue;
			if (!Modifier.isTransient(field.getModifiers()))
				values.add(new FieldContext(o, field, field.equals(idfield)));
		}
		return values.toArray(new ValuesContext[0]);
	}

        @Override
	public Object toBean(String uri) {
		try {
			// last gets the id off the end of the URI
			return (constructor != null) ? constructor.newInstance(last(uri))
					: newinstance(uri);
		} catch (Exception e) {
			logger.warn("Could not instantiate bean.", e);

		}
		return null;
	}
	
	private Object newinstance(String uri) throws Exception {
		Object o = c.newInstance();
		
		if (uriid && uri!=null)
			idfield.set(o, URI.create(uri));
		return o;
	}
	
	public Object toProxyBean(Resource source, AnnotationHelper jpa) {
		try {
			Class<?> cls = jpa.getProxy(c);
			Object obj = cls.newInstance();
			if (uriid)
				idfield.set(obj, URI.create(source.getURI()));
			return obj;
		} catch (Exception e) {
			logger.warn("Exception caught while invoking default constructor on " + c, e);
		}
		return null;
	}

}
