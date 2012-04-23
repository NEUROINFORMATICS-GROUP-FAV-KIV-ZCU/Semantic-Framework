package tools;

import java.io.InputStream;
import java.io.OutputStream;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Defines user interface for working with the OWL API library.
 * This library can be used to convert the ontology document
 * from RDF/XML to OWL/XML or OWL Functional-Style.
 * 
 * @author Jakub Krauz
 */
public interface OwlApi {
	
	/**
	 * Gets the ontology document in a required syntax.
	 * Possible syntaxes are RDF/XML, OWL/XML, Turtle or OWL Functional-Style.
	 * 
	 * @param syntax Required syntax.
	 * @return serialization of the ontology in the required syntax
	 * @throws OWLOntologyStorageException if there occurred problems with the format
	 */
	public InputStream getOntologyDocument(String syntax) throws OWLOntologyStorageException;
	
	
	/**
	 * Writes the ontology document into the given output stream.
	 * Syntax of the serialization is given by the second argument.
	 * Possible syntaxes are RDF/XML, OWL/XML, Turtle or OWL Functional-Style.
	 * 
	 * @param out Output stream to which write the serialization.
	 * @param syntax Required syntax.
	 * @throws OWLOntologyStorageException if there occurred problems with the format
	 */
	public void writeOntologyDocument(OutputStream out, String syntax)
					throws OWLOntologyStorageException;
	
}
