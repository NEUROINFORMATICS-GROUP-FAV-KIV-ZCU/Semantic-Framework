package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:TransitiveProperty</code> element.<br>
 * This element is used to specify a property to be transitive. It is useful
 * primarily for inferencing. Transitive property means that if we have two
 * pairs (X, Y) and (Y, Z) as instances of a transitive property, then the pair
 * (X, Z) is also an instance of this property.
 * <code>Owl:TransitiveProperty</code> is a subclass of
 * <code>owl:ObjectProperty</code>.
 * </p>
 * <p>
 * This annotation has no argument. If a field is marked by this annotation, the
 * resulting property will be specified as an <code>owl:TransitiveProperty</code>
 * instance.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   &#64;Transitive
 *   private Person classmate;
 * }
 * </pre> </code>
 * The <code>classmate</code> field will be mapped into:
 * <code> <pre>
 * &lt;owl:TransitiveProperty rdf:about="#classmate"&gt;
 *   ...
 * &lt;/owl:TransitiveProperty&gt;
 * </pre> </code>
 * </p>
 * 
 * @author Jakub Krauz
 * 
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Transitive {

}
