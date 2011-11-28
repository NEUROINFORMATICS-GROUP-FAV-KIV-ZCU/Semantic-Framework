package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation implements the <code>owl:someValuesFrom</code> value constraint.
 * It can be used to express that the annotated property (attribute) has at least
 * one value of some class.
 * The value has to be a valid class' URI.
 * 
 * @author Jakub Krauz
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SomeValuesFrom {
	String value();
}
