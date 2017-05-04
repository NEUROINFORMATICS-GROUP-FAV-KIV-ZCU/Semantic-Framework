package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * Implements the <code>owl:FunctionalProperty</code> element.<br>
 * This element is used to specify a property to be functional.
 * A functional property can have only one value for a given subject.
 * </p>
 * <p>
 * This annotation has no argument. If a field is marked by this annotation, the
 * resulting property will be specified as an <code>owl:FunctionalProperty</code>
 * instance. JenaBeanExtension does not check if the annotation is used properly
 * and the property really has only one unique value.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   &#64;FunctionalProperty  
 *   private String surname;
 * }
 * </pre> </code>
 * The <code>friend</code> field will be mapped to:
 * <code> <pre>
 * &lt;owl:FunctionalProperty rdf:about="#surname"&gt;
 *   ...
 * &lt;/owl:FunctionalProperty&gt;
 * </pre> </code>
 * </p>
 * 
 * @author Jakub Krauz
 * 
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionalProperty {

}
