package thewebsemantic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Jan Hrbek
 *
 * This annotation interface enables user to add a
 * AllDifferent
 *
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllDifferent {
    String[] value();
}