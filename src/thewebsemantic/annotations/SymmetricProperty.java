package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:SymmetricProperty</code> element.<br>
 * This element is used to specify a property to be symmetric. Symmetric
 * property means that the subject and the object from the triple can be
 * interchanged and the statement is true as well. In other words, if the pair
 * (X, Y) is an instance of a symmetric property, then the pair (Y, X) is
 * an instance of this property, too. It follows that its domain and range must
 * be the same. <code>Owl:SymmetricProperty</code> is a subclass of
 * <code>owl:ObjectProperty</code>.
 * </p>
 * <p>
 * This annotation has no argument. If a field is marked by this annotation, the
 * resulting property will be specified as an <code>owl:SymmetricProperty</code>
 * instance. JenaBeanExtension does not check if the annotation is used properly
 * (domain and range must be the same), it must be arranged by a programmer.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   &#64;SymmetricProperty 
 *   private Person friend;
 * }
 * </pre> </code>
 * The <code>friend</code> field will be mapped into:
 * <code> <pre>
 * &lt;owl:SymmetricProperty rdf:about="#friend"&gt;
 *   ...
 * &lt;/owl:SymmetricProperty&gt;
 * </pre> </code>
 * </p>
 * 
 * @author Jakub Krauz
 * 
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SymmetricProperty {

}
