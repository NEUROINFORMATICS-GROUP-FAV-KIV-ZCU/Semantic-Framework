package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:allValuesFrom</code> element.<br>
 * This property is a value constraint, it gives a range to the property
 * under consideration, but unlike the <code>rdfs:range</code> property this
 * constraint concerns only the restriction class. The statement says that
 * all values of the property concerned must belong to a defined range.
 * The range itself can be either a class or a data range.
 * </p>
 * <p>
 * Argument of this annotation must be a well-formed URI referencing the range class.
 * Defining simple data range values is not supported in this implementation.
 * In the generated ontology the Java class containing the annotated field will
 * inherit from an anonymous restriction class with this constraint.
 * </p>
 * <p>
 * NOTE: The <code>@AllValuesFrom</code> annotation doesn't force the annotated
 * field to meet this constraint. It must be arranged by a programmer.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   @AllValuesFrom("http://some.ontology#Dog")
 *   public Set&lt;Animal&gt; pets;
 * }
 * </pre> </code>
 * This class will be mapped into:
 * <code> <pre>
 * &lt;owl:Class rdf:about="#Person"&gt;
 *   ...
 *   &lt;rdfs:subClassOf&gt;
 *     &lt;owl:Restriction&gt;
 *       &lt;owl:onProperty rdf:resource="#pets" /&gt;
 *       &lt;owl:allValuesFrom rdf:resource="http://some.ontology#Dog" /&gt;
 *     &lt;/owl:Restriction&gt;
 *   &lt;/rdfs:subClassOf&gt;
 * &lt;/owl:Class&gt;
 * </pre> </code>
 * This class describes only those persons whose pet is a dog
 * (or more dogs, but no other animal).
 * </p>
 * 
 * @author Jakub Krauz
 * 
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllValuesFrom {
    String value();
}
