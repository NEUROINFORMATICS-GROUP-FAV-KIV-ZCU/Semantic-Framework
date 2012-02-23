package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:EquivalentClass</code> property.<br>
 * This property expresses that two classes have the same class extension (the
 * same set of individuals), but not necessarily the same concepts. That means
 * although two equivalent classes have the same instances, they does not have
 * to be equal. In OWL Full class equality can be expressed using the
 * <code>owl:sameAs</code> element, but OWL-DL cannot express class equality.
 * </p>
 * <p>
 * Argument of this annotation must be a well-formed URI referencing the
 * equivalent class.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * @EquivalentClass("http://some.ontology#Man")
 * public class Person {
 *   ...
 * }
 * </pre></code>
 * This class will be mapped into:
 * <code><pre>
 * &lt;owl:Class rdf:about="#Person"&gt;
 *   ...
 *   &lt;owl:equivalentClass&gt;
 *     &lt;owl:Class rdf:about="http://some.ontology#Man"/&gt;
 *   &lt;/owl:equivalentClass&gt;
 * &lt;/owl:Class&gt;
 * </pre></code>
 * </p>
 * 
 * @author Jakub Krauz
 * 
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EquivalentClass {

	String value();
}
