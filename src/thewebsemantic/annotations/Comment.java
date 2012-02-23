package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>rdfs:comment</code> element.<br>
 * This element is used to add some comment to a class or property.
 * </p>
 * 
 * @author Jan Hrbek, Jakub Krauz
 *
 **/
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
	
    String value();
}
