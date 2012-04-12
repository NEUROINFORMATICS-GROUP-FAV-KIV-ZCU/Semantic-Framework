package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * Implements the <code>owl:hasValue</code> element.<br>
 * This property is a value constraint, it says that at least one
 * value of the property concerned must be semantically equal to value V.
 * The value V can be either an individual or a data value.
 * </p>
 * <p>
 * Individual<br>
 * The <code>uri</code> parameter of this annotation must be a well-formed URI
 * referencing the individual. In the generated ontology the Java class containing
 * the annotated attribute will inherit from an anonymous restriction class
 * with this constraint.
 * </p>
 * <p>
 * Data value<br>
 * There are three possible parameters that set the data value:
 * <ul>
 * 		<li><code>stringValue</code> is used to set a string value
 * 		<li><code>intValue</code> to set integer value
 * 		<li><code>charValue</code> to set character value
 * </ul>
 * In the generated ontology the Java class containing the annotated attribute
 * will inherit from an anonymous restriction class with this constraint.
 * </p>
 * <p>
 * NOTE: The <code>@HasValue</code> annotation doesn't force the annotated
 * field to meet this constraint. It must be arranged by a programmer.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   &#64;HasValue(stringValue="Jakub")
 *   public String name;
 * }
 * </pre> </code>
 * This class will be mapped to:
 * <code> <pre>
 * &lt;owl:Class rdf:about="#Person"&gt;
 *   ...
 *   &lt;rdfs:subClassOf&gt;
 *     &lt;owl:Restriction&gt;
 *       &lt;owl:onProperty rdf:resource="#name" /&gt;
 *       &lt;owl:hasValue rdf:datatype="http://www.w3.org/2001/XMLSchema#string"&gt;Jakub&lt;/owl:hasValue&gt;
 *     &lt;/owl:Restriction&gt;
 *   &lt;/rdfs:subClassOf&gt;
 * &lt;/owl:Class&gt;
 * </pre> </code>
 * This class describes only those persons whose name is Jakub
 * (or one of their names, if they have more than one).
 * </p>
 * 
 * @author Jakub Krauz
 * 
 **/
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasValue {
	
	public static final char DEFAULT_CHAR = 0x00;
	public static final int DEFAULT_INT = Integer.MIN_VALUE;
	
	/** individual's URI */
	String uri() default "";
	
	/** string value */
	String stringValue() default "";
	
	/** character value */
	char charValue() default DEFAULT_CHAR;
	
	/** integer value */
	int intValue() default DEFAULT_INT;
	
}
