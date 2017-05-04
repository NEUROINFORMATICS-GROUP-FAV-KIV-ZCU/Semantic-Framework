package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Implements the <code>rdfs:range</code> element.
 * This annotation interface enables user to add a XSD data range
 * restriction to selected attribute.
 * 
 * @author Filip Markvart
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataRange {
    String value();
}
