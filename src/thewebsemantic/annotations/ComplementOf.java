package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * Implements the <code>owl:complementOf</code> property.<br>
 * If class A is a complement of class B, then every individual that does
 * not belong to B must belong to A and vice versa. Classes have no common
 * individual. This relation can be expressed as a logical negation.<br>
 * Argument of this annotation must be a well-formed URI referencing the
 * complement class.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * &#64;ComplementOf("http://some.ontology#EverythingExceptPersons")
 * public class Person {
 *   ...
 * }
 * </pre></code>
 * This class will be mapped to:
 * <code><pre>
 * &lt;owl:Class rdf:about="#Person"&gt;
 *   ...
 *   &lt;owl:complementOf&gt;
 *     &lt;owl:Class rdf:about="http://some.ontology#EverythingExceptPersons"/&gt;
 *   &lt;/owl:complementOfs&gt;
 * &lt;/owl:Class&gt;
 * </pre></code>
 * </p>
 * 
 * @author Jakub Krauz
 * 
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComplementOf {
	
	String value();
	
}
