package thewebsemantic;

import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * This class represents wrapper of classes methods that has URI annoatation
 * instead of non-deprecated id annotation.
 *
 */
public class UriMethodTypeWrapper extends TypeWrapper {
	
	private Log logger = LogFactory.getLog(getClass());
	private Method uriMethod;
	
	public UriMethodTypeWrapper(Class<?> c, Method m) {
		super(c);
		uriMethod = m;
	}

	@Override
	public String uri(Object bean) {
		return invokeMethod(bean, uriMethod);
	}

	@Override
	public String uri(String id) {
		return id;
	}

	@Override
	public String id(Object bean) {
		return invokeMethod(bean, uriMethod);
	}

	/**
	 * This implementation of toBean() supplies the URI to a constructor, if it
	 * exists.  This enables the loading of pre-existing RDF what wasn't created
	 * with Jenabean managed URI's.  
	 */
	@Override
	public Object toBean(String uri) {
		try {
			return (constructor != null) ?
				constructor.newInstance(uri):c.newInstance();
		} catch (Exception e) {
			logger.warn("Exception caught while instantiating " + c, e);
		}
		return null;
	}

	@Override
	public Object toProxyBean(Resource source, AnnotationHelper jpa) {
		throw new UnsupportedOperationException();
	}

}
