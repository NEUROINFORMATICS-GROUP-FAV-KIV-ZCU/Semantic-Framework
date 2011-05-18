package thewebsemantic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*
* This annotation interface enables user add Cardinality
* restriction 
*
* @author Libor Vachal
**/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cardinality {
	int value();
}
