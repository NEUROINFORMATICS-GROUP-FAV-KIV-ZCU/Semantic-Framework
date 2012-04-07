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
 * Class<br>
 * The <code>uri</code> parameter of this annotation must be a well-formed URI
 * referencing the range class. In the generated ontology the Java class containing
 * the annotated field will inherit from an anonymous restriction class with this constraint.
 * </p>
 * <p>
 * Data range<br>
 * There are three possible parameters that set the data range:
 * <ul>
 * 		<li><code>stringValues</code> is used to set an enumeration of string values
 * 		<li><code>intValues</code> to set enumeration of integer values
 * 		<li><code>charValues</code> to set enumeration of character values
 * </ul>
 * In the generated ontology the Java class containing the annotated field will inherit
 * from an anonymous restriction class with this constraint.
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
 *   @SomeValuesFrom(uri="http://some.ontology#Dog")
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
	
	/** URIref of the class that some values of the annotated property belong to */
	String uri() default "";
	
	/** enumeration of string values */
	String[] stringValues() default {};
	
	/** enumeration of integer values */
	int[] intValues() default {};
	
	/** enumeration of character values */
	char[] charValues() default {};
	
}
