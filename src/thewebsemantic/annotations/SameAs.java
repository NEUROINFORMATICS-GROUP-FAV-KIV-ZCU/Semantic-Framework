package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation implements the built-in OWL property <code>owl:sameAs</code>.
 * This property states that two <b>individuals</b> are equal. It is the opposite of
 * <code>owl:differentFrom<code>. We can use it to express that two different URIs
 * refer to the same thing.
 * </p>
 * <p>
 * For example:
 * <code>
 * <pre>
 * &lt;rdf:Description rdf:about="#Andula"&gt;
 *   &lt;owl:sameAs rdf:resource="#Antonov_AN2"/&gt;
 * &lt;/rdf:Description&gt;
 * </pre>
 * </code>
 * This states that the two URIs refer to the same thing (it's a type of aeroplane).
 * </p>
 * <p>
 * <b>NOTE:</b> In OWL Full classes and properties can be treated as individuals
 * that is why we can express class equality and property equality using
 * <code>owl:sameAs</code>. However, such a statement cannot be used in OWL Lite
 * or DL which implies that there cannot be expressed class equality or property
 * equality in OWL Lite or DL.
 * </p>
 * 
 * @see EquivalentClass, EquivalentProperty
 * @author Jan Hrbek, Jakub Krauz
 * 
 * @deprecated This annotation shouldn't be used because of above mentioned NOTE.
 *             If we use it for a class, we treat this class as an instance.
 *             That means the ontology document will be in OWL Full, which is
 *             not compatible with reasoner processing.
 */
@Deprecated
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SameAs {
	String value();
}
