package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * This annotation interface enables user to add a isDefinedBy element
 * to selected field
 *
 *
 * @author Miroslav Masek
 *
 *
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsDefinedBy {
    String value();

}
