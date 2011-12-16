package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:EquivalentProperty</code> property.<br>
 * This property links two properties to be equivalent, which means they have
 * the same property extension (the same values). It does not necessarily mean
 * that they are equal (have the same meaning). In OWL Full property equality
 * can be expressed using the <code>owl:sameAs</code> element, but OWL-DL cannot
 * express property equality.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * public class Bus {
 *   ...
 *   @EquivalentProperty("http://some.address.com/ontology#numberOfPlaces")
 *   private int capacity;
 * }
 * </pre></code>
 * </p>
 * 
 * @author Jan Hrbek, Jakub Krauz
 * 
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EquivalentProperty {
    String value();
}