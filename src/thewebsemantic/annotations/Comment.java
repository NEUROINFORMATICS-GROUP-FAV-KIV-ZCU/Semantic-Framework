package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>rdfs:comment</code> element.<br>
 * This element is used to add some comment to a class or property.
 * Language of the comment can be optionally set using the
 * <code>lang</code> parameter.
 * </p>
 * 
 * @author Jakub Krauz
 *
 **/
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
	
	/** comment to the annotated element */
    String value();
    
    /** language of the comment, the value must be a valid
     * language identifier as defined the for <code>xml:lang</code> tag */
    String lang() default "";
    
}
