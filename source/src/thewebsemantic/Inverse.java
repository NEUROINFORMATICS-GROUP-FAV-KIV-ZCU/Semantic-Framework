package thewebsemantic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Inverse annotation interface used to annotate attributes or whole classes
 * to set an inverse object via object's URI in String attribute
 * 
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) 
public @interface Inverse {
	String value();
}
