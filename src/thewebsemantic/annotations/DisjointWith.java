package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * Implements the <code>owl:disjointWith</code> property.<br>
 * This property expresses that two classes have no common individuals.<br>
 * Argument of this annotation must be a well-formed URI referencing the
 * other class.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * @DisjointWith("http://some.ontology#Animal")
 * public class Person {
 *   ...
 * }
 * </pre></code>
 * This class will be mapped to:
 * <code><pre>
 * &lt;owl:Class rdf:about="#Person"&gt;
 *   ...
 *   &lt;owl:disjointWith&gt;
 *     &lt;owl:Class rdf:about="http://some.ontology#Animal"/&gt;
 *   &lt;/owl:disjointWith&gt;
 * &lt;/owl:Class&gt;
 * </pre></code>
 * </p>
 * 
 * @author Jakub Krauz
 * 
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisjointWith {
	
	String value();
	
}
