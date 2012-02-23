package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * Implements the <code>rdfs:seeAlso</code> element.<br>
 * This property is used to add some relevant reference to the
 * resource concerned. The referenced resource can provide
 * some useful information about the subject resource.
 * </p>
 * 
 * @author Jakub Krauz
 * 
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SeeAlso {
	
	String value();
}
