package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Implements the <code>owl:maxCardinality</code> property.<br>
 * It is a property restriction. It indicates that all individuals of the
 * restriction class have at most N different values of the property concerned.
 * If used in combination with the <code>@MinCardinality</code> annotation, it
 * defines an interval to which the number of property's different values must
 * belong.
 * </p>
 * <p>
 * Argument of this annotation is an integer value. Problem of this
 * implementation consists in the difference between an object code and a
 * RDF-based ontology. We can say that some property's maxCardinality is 5 using
 * <code>@MaxCardinality(5)</code> for the field under consideration. This
 * statement will appear in the ontology document afterwards. But we cannot
 * create instances in Java that have more than one value for the field under
 * consideration. This would permit the only values of 0 or 1. That is why this
 * annotation should be used only for collections or arrays in the meaning of
 * "maximal number of elements" of the collection. A programmer should also
 * arrange that the real size of the Java collection meets this restriction.
 * </p>
 * <p>
 * NOTE: The <code>@MaxCardinality</code> annotation doesn't force the annotated
 * collection to meet this constraint. It must be arranged by a programmer.
 * </p>
 * <p>
 * Example of use:
 * <code> <pre>
 * public class Person {
 *   ...
 *   @MaxCardinality(5)
 *   private Set&lt;Car&gt; cars;
 * }
 * </pre> </code>
 * This class will be mapped into:
 * <code> <pre>
 * &lt;owl:Class rdf:about="#Person"&gt;
 *   ...
 *   &lt;rdfs:subClassOf&gt;
 *     &lt;owl:Restriction&gt;
 *       &lt;owl:onProperty rdf:resource="#cars" /&gt;
 *       &lt;owl:maxCardinality rdf:datatype="http://www.w3.org/2001/XMLSchema#int"&gt;5&lt;/owl:maxCardinality&gt;
 *     &lt;/owl:Restriction&gt;
 *   &lt;/rdfs:subClassOf&gt;
 * &lt;/owl:Class&gt;
 * </pre> </code>
 * This class describes all those persons that owns at most 5 cars.
 * </p>
 * 
 * @author Jakub Krauz
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxCardinality {

	int value();
}
