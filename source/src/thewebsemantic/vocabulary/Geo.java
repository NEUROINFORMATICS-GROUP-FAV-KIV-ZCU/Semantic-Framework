package thewebsemantic.vocabulary;

import thewebsemantic.As;
import thewebsemantic.Functional;
import thewebsemantic.Namespace;

/**
 * This class is not used in actual version of JenaBean.
 *
 */
@Namespace("http://www.w3.org/2003/01/geo/wgs84_pos#")
public interface Geo extends As {
	
	interface Point extends Geo{}

	@Functional
	Geo lat(float l);
	Float lat();

	@Functional
	Geo long_(float l);
	Float long_();

}
