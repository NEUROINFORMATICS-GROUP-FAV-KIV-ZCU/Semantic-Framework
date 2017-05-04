package thewebsemantic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.rdf.model.Resource;

public class DefaultTypeWrapper extends TypeWrapper {
	
	private Log logger = LogFactory.getLog(getClass());

	public DefaultTypeWrapper(Class<?> c) {
		super(c);
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

}
