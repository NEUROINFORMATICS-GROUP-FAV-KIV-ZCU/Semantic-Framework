package thewebsemantic;

/**
 * An interface to make annotation processing within
 * Jenabean agnostic.  When used with JPA the internals
 * will need to support JPA Id and other misc annotations.
 * Classic dependency injection, we now depend on an interface
 * within the same package.
 */
public interface AnnotationHelper {

        /**
         * Method test if annotation Generated.class is preset over the param
         * ValueContext
         *
         * @param ctx Target ValueContext descendant instance
         * @return true if annoataion is preset
         */
	boolean isGenerated(ValuesContext ctx);

        /**
         * Test bean if it is represented es embedded, that means it will
         * be represented as an anonymous resource in the created model.
         *
         * @param bean target bean
         * @return true if bean is represented as embedded
         */
	boolean isEmbedded(Object bean);

        /**
         * Test if new instance of the bean will be created as a proxy bean or
         * the original bean
         * @return
         */
	boolean proxyRequired();

        /**
         * Method returns a proxy class of the bean class
         *
         * @param <T>
         * @param c
         * @return
         * @throws InstantiationException
         * @throws IllegalAccessException
         */
	<T> Class<? extends T> getProxy(Class<T> c) throws InstantiationException, IllegalAccessException;

}
