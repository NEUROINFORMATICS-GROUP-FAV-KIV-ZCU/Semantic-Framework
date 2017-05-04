package thewebsemantic;

import thewebsemantic.annotations.Uri;

/**
 * This class represents a Resource object analogic to Jena's Resource, but
 * contain only the basic information a operation over it
 *
 */
public class Resource {
	String uri;

	public Resource(String s) {
		uri = s;
	}
	
	@Uri
	public String getUri() {
		return uri;
	}

        @Override
	public String toString() {
		return uri;
	}
}
