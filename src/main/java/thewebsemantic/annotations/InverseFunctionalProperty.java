package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * Implements the <code>owl:InverseFunctionalProperty</code> element.<br>
 * This element is used to specify a property to be inverse-functional.
 * An inverse-functional property can have only one subject for a given
 * object. It means that the object of the property unequivocally
 * determines the subject. No two subjects can have the same object for
 * the property.
 * </p>
 * <p>
 * This annotation has no argument. If a field is marked by this annotation, the
 * resulting property will be specified as an <code>owl:InverseFunctionalProperty</code>
 * instance. JenaBeanExtension does not check if the annotation is used properly
 * and the property really meets the condition.
 * </p>
 * <p>
 * NOTE: Because <code>owl:InverseFunctionalProperty</code> is a subclass of
 * <code>owl:ObjectProperty</code>, it can be used only for those attributes
 * that are mapped to instances of <code>owl:ObjectProperty</code> (non-literals).
 * A datatype property can be declared inverse-functional only in OWL Full.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   &#64;InverseFunctionalProperty  
 *   private BirthNumber birthNumber;
 * }
 * </pre> </code>
 * The <code>birthNumber</code> field will be mapped to:
 * <code> <pre>
 * &lt;owl:InverseFunctionalProperty rdf:about="#birthNumber"&gt;
 *   ...
 * &lt;/owl:InverseFunctionalProperty&gt;
 * </pre> </code>
 * </p>
 * 
 * @author Jakub Krauz
 * 
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InverseFunctionalProperty {

}
