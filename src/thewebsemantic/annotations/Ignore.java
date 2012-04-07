package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to ignore bean's field (or getter method).
 * Fields (or methods) annotated by
 * <code>@Ignore</code> will not appear in the OWL ontology.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
	
}
