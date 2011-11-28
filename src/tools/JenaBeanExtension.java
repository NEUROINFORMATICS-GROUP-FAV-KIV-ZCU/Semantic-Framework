package tools;

import java.io.IOException;
import java.io.InputStream;

/**
 * Defines user interface for working with the JenaBeanExtension library.
 * @author Jakub Krauz
 */
public interface JenaBeanExtension {
	
	/**
	 * Writes a serialization of the ontology model as a RDF/XML document.
	 * This method doesn't run the transformation process itself, it only
	 * creates the XML description of the ontology model.
	 * @return RDF/XML ontology document
	 * @throws IOException if there occurred problems creating the stream
	 */
	public InputStream getOntologyDocument() throws IOException;
	
}
