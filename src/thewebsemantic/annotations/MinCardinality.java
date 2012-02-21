package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:minCardinality</code> property.<br>
 * It is a property restriction. It indicates that all individuals of the
 * restriction class have at least N different values of the property concerned.
 * If used in combination with the <code>@MaxCardinality</code> annotation, it
 * defines an interval to which the number of property's different values must
 * belong.
 * </p>
 * <p>
 * Problem of this implementation consists in the difference between an object
 * code and a RDF-based ontology. We can say that some property's minCardinality
 * is 2 using <code>@MinCardinality(2)</code> for the field under consideration.
 * This statement will appear in the ontology document after the transformation
 * process. But in Java we cannot create instances that meet this restriction
 * (i.e. have at least two different values of the field under consideration at
 * the same time). That would permit the only values of 0 and 1. That is why
 * this annotation should be used only for collections or arrays in the meaning
 * of "minimal number of elements" of the collection. A programmer should also
 * arrange that the real size of the Java collection meets this restriction.
 * </p>
 * <p>
 * NOTE: The <code>@MinCardinality</code> annotation doesn't force the annotated
 * collection to meet this constraint. It must be arranged by a programmer.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * public class Person {
 *   ...
 *   @MinCardinality(2)
 *   private Set&lt;Person&gt; children;
 * }
 * </pre></code>
 * This class will be mapped into:
 * <code><pre>
 * &lt;owl:Class rdf:about="Person"&gt;
 *   ...
 *   &lt;rdfs:subClassOf&gt;
 *     &lt;owl:Restriction&gt;
 *       &lt;owl:onProperty rdf:resource="#children" /&gt;
 *       &lt;owl:minCardinality rdf:datatype="http://www.w3.org/2001/XMLSchema#int"&gt;2&lt;/owl:minCardinality&gt;
 *     &lt;/owl:Restriction&gt;
 *   &lt;/rdfs:subClassOf&gt;
 * &lt;/owl:Class&gt;
 * </pre></code>
 * This class describes all persons that have at least two children.
 * </p>
 * 
 * @author Jakub Krauz
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MinCardinality {

	int value();
}
