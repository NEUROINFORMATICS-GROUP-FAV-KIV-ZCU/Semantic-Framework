package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:EquivalentProperty</code> element.<br>
 * This statement links two properties to be equivalent, which means they have
 * the same property extension (the same values). It does not necessarily mean
 * that they are equal (have the same meaning). In OWL Full property equality
 * can be expressed using the <code>owl:sameAs</code> element, but OWL-DL cannot
 * express property equality.
 * </p>
 * <p>
 * Argument of this annotation must be a well-formed URI referencing the
 * equivalent property.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * public class Person {
 *   ...
 *   @EquivalentProperty("http://some.ontology#givenName")
 *   private String name;
 * }
 * </pre></code>
 * The <code>name</code> field will be mapped into:
 * <code><pre>
 * &lt;owl:DatatypeProperty rdf:about="#name"&gt;
 *   ...
 *   &lt;owl:equivalentProperty rdf:resource="http://some.ontology#givenName" /&gt;
 * &lt;/owl:DatatypeProperty&gt;
 * </pre></code>
 * </p>
 * 
 * @author Jakub Krauz
 * 
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EquivalentProperty {
	
    String value();
}