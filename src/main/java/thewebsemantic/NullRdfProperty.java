package thewebsemantic;

import java.lang.annotation.Annotation;

import thewebsemantic.annotations.RdfProperty;
/**
 * Interface represents default RdfProperty annotation values, if the
 * RdfProperty annotation is not present over the attribut or getter method
 * 
 */

class NullRdfProperty implements RdfProperty {
	public boolean symmetric() {
		return false;
	}

	public boolean transitive() {
		return false;
	}

	public String value() {
		return "";
	}

	public Class<? extends Annotation> annotationType() {
		return null;
	}

	public String inverseOf() {
		return "";
	}
}