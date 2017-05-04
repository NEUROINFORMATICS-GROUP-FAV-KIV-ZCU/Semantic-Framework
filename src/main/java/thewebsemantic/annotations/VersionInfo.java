package thewebsemantic.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Implements the <code>owl:versionInfo</code> element.
 * This annotation interface enables user to add a version info element
 * to selected attribute or class.
 * 
 * @author Filip Markvart
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VersionInfo {
    String value();
}
