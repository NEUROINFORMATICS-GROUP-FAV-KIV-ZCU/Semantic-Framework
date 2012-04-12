package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasValue {
	public static final char DEFAULT_CHAR = 0x00;
	public static final int DEFAULT_INT = Integer.MIN_VALUE;
	
	/** individual's URI */
	String uri() default "";
	
	/** string value */
	String stringValue() default "";
	
	/** character value */
	char charValue() default DEFAULT_CHAR;
	
	/** integer value */
	int intValue() default DEFAULT_INT;
	
}
