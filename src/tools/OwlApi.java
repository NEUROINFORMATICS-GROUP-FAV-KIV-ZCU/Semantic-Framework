package tools;

import java.io.IOException;
import java.io.InputStream;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Defines user interface for working with the OWL API library.
 * This library can be used to convert the ontology document
 * from RDF/XML to OWL/XML.
 * 
 * @author Jakub Krauz
 */
public interface OwlApi {
	
	/**
	 * Gets the ontology document in a required syntax.
	 * 
	 * @param syntax Required syntax.
	 * @return serialization of the ontology in the required syntax
	 * @throws IOException if there occurred problems creating the input stream
	 * @throws OWLOntologyStorageException if there occurred problems with the format
	 */
	public InputStream getOntologyDocument(String syntax)
    		throws IOException, OWLOntologyStorageException;
	
}
