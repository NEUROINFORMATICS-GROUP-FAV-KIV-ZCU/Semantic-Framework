package tools;

import java.io.InputStream;
import java.io.OutputStream;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Defines user interface that controls the OWL API library.<br>
 * It can be used to convert the ontology document
 * from RDF/XML to OWL/XML or OWL Functional-Style.
 * 
 * @author Jakub Krauz
 */
public interface OwlApi {
	
	/**
	 * Gets the ontology document in the required syntax.<br>
	 * Possible syntaxes are RDF/XML, OWL/XML, Turtle or OWL Functional-Style.
	 * 
	 * @param syntax required syntax
	 * @return serialization of the ontology in the required syntax
	 * @throws OWLOntologyStorageException if there occurred problems with the format
	 */
	public InputStream getOntologyDocument(String syntax) throws OWLOntologyStorageException;
	
	
	/**
	 * Writes the ontology document into the given output stream.<br>
	 * Syntax of the serialization is given by the <code>syntax</code> argument.
	 * Possible syntaxes are RDF/XML, OWL/XML, Turtle or OWL Functional-Style.
	 * 
	 * @param out output stream to which the serialization is written
	 * @param syntax required syntax
	 * @throws OWLOntologyStorageException if there occurred problems with the format
	 */
	public void writeOntologyDocument(OutputStream out, String syntax)
					throws OWLOntologyStorageException;
	
}
