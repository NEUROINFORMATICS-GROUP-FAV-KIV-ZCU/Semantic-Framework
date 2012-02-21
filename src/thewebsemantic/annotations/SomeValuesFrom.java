package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation implements the <code>owl:someValuesFrom</code> element.<br>
 * This property is a value constraint, its meaning is similar to the
 * <code>owl:allValuesFrom</code> element, but this one is less restrictive
 * – it describes all individuals for which at least one value of the property
 * under consideration has the defined range. The range itself can be either
 * a class or a data range.
 * </p>
 * <p>
 * Argument of this annotation must be a well-formed URI referencing the range class.
 * Defining simple data range values is not supported in this implementation.
 * In the generated ontology the Java class containing the annotated field will
 * inherit from an anonymous restriction class with this constraint.
 * </p>
 * <p>
 * NOTE: The <code>@SomeValuesFrom</code> annotation doesn't force the annotated
 * field to meet this constraint. It must be arranged by a programmer.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   @SomeValuesFrom("http://some.ontology#Dog")
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
 *       &lt;owl:someValuesFrom rdf:resource="http://some.ontology#Dog" /&gt;
 *     &lt;/owl:Restriction&gt;
 *   &lt;/rdfs:subClassOf&gt;
 * &lt;/owl:Class&gt;
 * </pre> </code>
 * This class describes those persons who have at least one dog
 * (and can have other animals).
 * </p>
 * 
 * @author Jakub Krauz
 * 
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SomeValuesFrom {
	String value();
}
