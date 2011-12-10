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
 * <code>@MaxCardinality</code> and <code>MinCardinality</code> with the
 * same value (N).
 * </p>
 * <p>
 * Problem of this implementation consists in the difference between an object
 * code and a RDF ontology. We can say that some property's cardinality is 5
 * using <code>@Cardinality(5)</code> for the field under consideration. This
 * statement will appear in the ontology document afterwards. But we can't
 * create instances in Java that correspond to this statement (i.e. every
 * instance has exactly 5 different values of that field at the same time).
 * </p>
 * <p>
 * Solution consists in using this annotation for collections or arrays. The
 * meaning is then "number of elements" of the collection.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * public class Car {
 *   ...
 *   @Cardinality(1)
 *   public String brand;
 * }
 * </pre></code>
 * This states that every car is of exactly one brand.
 * </p>
 *               
 * @author Jakub Krauz
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cardinality {

	int value();
}
