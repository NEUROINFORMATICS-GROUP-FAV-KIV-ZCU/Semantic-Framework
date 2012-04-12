package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:inverseOf</code> element.<br>
 * This statement links two properties that are inverse each
 * other. It means they describe the same relation from the
 * other side (some parent has a child, this child has that parent).
 * </p>
 * <p>
 * Argument of this annotation must be a well-formed URI referencing
 * the inverse property.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   @Inverse("data.pojo#child")
 *   private Person parent;
 * }
 * </pre> </code>
 * The <code>parent</code> field will be mapped into:
 * <code> <pre>
 * &lt;owl:ObjectProperty rdf:about="#parent"&gt;
 *   ...
 *   &lt;owl:inverseOf rdf:resource="http://data.pojo#child"&gt;
 * &lt;/owl:ObjectProperty&gt;
 * </pre> </code>
 * </p>
 * 
 * @author Jakub Krauz
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InverseOf {
	
	String value();
}
