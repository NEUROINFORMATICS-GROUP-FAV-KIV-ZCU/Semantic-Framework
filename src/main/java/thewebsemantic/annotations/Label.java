package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>rdfs:label</code> element.<br>
 * This element is used to addd a human-readable name
 * to a class or property.
 * Language of the label can be optionally set using the
 * <code>lang</code> parameter.
 * </p>
 * 
 * @author Jakub Krauz
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Label {
	
	/** label of the annotated element */
	String value();
	
	/** language of the label, the value must be a valid
     * language identifier as defined the for <code>xml:lang</code> tag */
	String lang() default "";
	
}
