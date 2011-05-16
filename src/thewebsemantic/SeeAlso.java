package thewebsemantic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation interface enables user to add a "see also" element
 * to selected attribute or class.
 * 
 * @author Jakub Krauz
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SeeAlso {
	String value();
}
