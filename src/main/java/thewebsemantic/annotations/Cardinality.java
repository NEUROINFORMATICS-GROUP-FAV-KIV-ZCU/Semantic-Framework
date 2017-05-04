package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <p>
 * Implements the <code>owl:cardinality</code> property.<br>
 * It is a property restriction. It indicates that all individuals of the
 * restriction class have exactly N different values of the property concerned.
 * In fact, this statement has exactly the same meaning as using both
 * <code>@MaxCardinality</code> and <code>@MinCardinality</code> with the
 * same value (N).
 * </p>
 * <p>
 * Argument of this annotation is an integer value.
 * Problem of this implementation consists in the difference between an object
 * code and a RDF-based ontology. We can say that some property's cardinality
 * is 5 using <code>@Cardinality(5)</code> for the field under consideration. This statement
 * will appear in the ontology document after the transformation process. But we
 * can't create instances in Java that correspond to this statement (i.e. every
 * instance has exactly 5 different values of that field at the same time).
 * That is why this annotation should be used only for collections or arrays.
 * The meaning is then "number of elements" of the collection. If used for other
 * types, the only meaningful values are 0 and 1. This must be arranged by the
 * programmer that annotates the source code.
 * </p>
 * <p>
 * NOTE: The <code>@Cardinality</code> annotation doesn't force the annotated
 * collection to meet this constraint. It must be arranged by a programmer.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * public class Person {
 *   ...
 *   &#64;Cardinality(3)
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
 *       &lt;owl:cardinality rdf:datatype="http://www.w3.org/2001/XMLSchema#int"&gt;3&lt;/owl:cardinality&gt;
 *     &lt;/owl:Restriction&gt;
 *   &lt;/rdfs:subClassOf&gt;
 * &lt;/owl:Class&gt;
 * </pre></code>
 * This class describes those persons that have exactly 3 children.
 * </p>
 *               
 * @author Jakub Krauz
 * 
 **/
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cardinality {
	
	int value();
}
