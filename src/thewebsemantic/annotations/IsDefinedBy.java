package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>rdfs:isDefinedBy</code> element.<br>
 * This property is used to set reference to a resource that defines
 * the resource concerned. It is a subproperty of
 * <code>rdfs:seeAlso</code>.
 * </p>
 * <p>
 * Argument of this annotation must be a well-formed URI referencing
 * a resource that defines the annotated field or class.
 * </p>
 *
 * @author Jakub Krauz
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsDefinedBy {
	
    String value();
}
