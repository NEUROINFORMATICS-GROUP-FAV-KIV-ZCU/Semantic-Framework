package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation implements the <code>owl:maxCardinality</code> property.<br>
 * It is a property restriction. It indicates that all individuals of the
 * restriction class have at most N different values of the property concerned.
 * If used in combination with the <code>@MinCardinality</code> annotation, it defines an
 * interval to which the number of property's different values must belong.
 * </p>
 * <p>Problem of this implementation consists in the
 * difference between an object code and a RDF ontology. We can say that some
 * property's maxCardinality is 5 using <code>@MaxCardinality(5)</code> for the
 * field under consideration. This statement will appear in the ontology
 * document afterwards. But for all that we cannot create instances in Java that
 * have more than one value for the field under consideration. This would permit
 * the only values of 0 or 1. But the solution is to use this annotation for
 * collections or arrays in the meaning "maximal number of elements" of the
 * collection.
 * </p>
 * <p>
 * Example of use:
 * <code><pre>
 * public class Train {
 *   ...
 *   @MaxCardinality(8)
 *   public Set<Car> cars;
 * }
 * </pre></code>
 * This class describes all trains that are not composed of more than 8 cars.
 * </p>
 * 
 * @author Jakub Krauz 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxCardinality {

	int value();
}
