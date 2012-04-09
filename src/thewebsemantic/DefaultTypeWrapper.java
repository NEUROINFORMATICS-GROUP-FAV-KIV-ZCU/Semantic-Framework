package thewebsemantic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.rdf.model.Resource;

public class DefaultTypeWrapper extends TypeWrapper {
	
	private Log logger = LogFactory.getLog(getClass());
	//private Field[] fields;

	public DefaultTypeWrapper(Class<?> c) {
		super(c);
		//fields = Util.getDeclaredFields(c);
	}
	
	@Override
	public String uri(String id) {
		return typeUri() + '_' + id;
	}

	public String id(Object bean) {
		return String.valueOf(bean.hashCode());
	}

	@Override
	public Object toProxyBean(Resource source, AnnotationHelper jpa) {
		try {
			return jpa.getProxy(c).newInstance();
		} catch (Exception e) {
			logger.warn("Exception caught while invoking default constructor on " + c, e);
		}
		return null;
	}
	
	
	/*@Override
	public ValuesContext[] getValueContexts(Object o) {
		ArrayList<ValuesContext> values = new ArrayList<ValuesContext>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Ignore.class))
				 continue;
			if (!Modifier.isTransient(field.getModifiers()))
				values.add(new FieldContext(o, field, false));
		}
		return values.toArray(new ValuesContext[0]);
	}*/



}
