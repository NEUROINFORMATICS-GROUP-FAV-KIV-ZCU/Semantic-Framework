package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation implements the built-in OWL property <code>owl:differentFrom</code>.
 * It is the opposite of <code>owl:sameAs</code>. This property indicates that two
 * <b>individuals</b> are not the same. We can use it to express that two URIs refer to
 * different individuals.
 * </p>
 * <p>
 * An example:
 * <code>
 * <pre>
 * &lt;Car rdf:ID="Ford"/&gt;
 * &lt;Car rdf:ID="Mercedes"&gt;
 *   &lt;owl:differentFrom rdf:resource="#Ford"/&gt;
 * &lt;/Car&gt;
 * </pre>
 * </code>
 * This states that there are two different cars.
 * </p>
 * <p>
 * <b>NOTE:</b> In OWL Full classes and properties can be treated as individuals
 * that is why we can express class inequality and property inequality using
 * <code>owl:differentFrom</code>. However, such a statement cannot be used in OWL Lite
 * or DL which implies that there cannot be expressed class inequality or property
 * inequality in OWL Lite or DL.
 * </p>
 * 
 * @see AllDifferent
 * @author Jan Hrbek, Jakub Krauz
 * 
 * @deprecated This annotation shouldn't be used because of above mentioned NOTE.
 *             If we use it for a class, we treat this class as an instance.
 *             That means the ontology document will be in OWL Full, which is
 *             not compatible with reasoner processing.
 */
@Deprecated
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentFrom {
    String value();
}
