package thewebsemantic;

import thewebsemantic.Generated;

/**
 * This class represents a default AnnotationHelper that is used for
 * basic operations while construction a Jena Model.
 * 
 */
public class NullJPAHelper implements AnnotationHelper {

	/**
	 * Method test if annotation Generated is present over the targer ctx
	 * object
	 * 
	 * @param ctx Target wrapped bean object that is tested
	 * @return true if annotation is present
	 */
	public boolean isGenerated(ValuesContext ctx) {
		return ctx.getAccessibleObject().isAnnotationPresent(Generated.class);
	}


	/**
	 * Test bean if it is represented es embedded, that means it will
	 * be represented as an anonymous resource in the created model.
	 * 
	 * @param bean target bean
	 * @return true if bean is represented as embedded
	 */
	public boolean isEmbedded(Object bean) {
		return false;
	}


	/**
	 * Test if new instance of the bean will be created as a proxy bean or
	 * the original bean
	 * 
	 * @return false
	 */
	public boolean proxyRequired() {
		return false;
	}


	public <T> Class<? extends T> getProxy(Class<T> c) throws InstantiationException,
			IllegalAccessException {
		throw new UnsupportedOperationException();
	}

}
