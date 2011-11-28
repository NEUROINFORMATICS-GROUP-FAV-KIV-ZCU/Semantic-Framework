package tools;

import java.io.IOException;
import java.io.InputStream;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Defines user interface for working with the OwlApi library.
 * This library can be used to convert the semantic model into
 * one of the standard semantic formats.
 * @author Jakub Krauz
 */
public interface OwlApi {
	
	/**
	 * Converts the semantic model into chosen semantic standard.
	 * @param type semantic standard
	 * @return XML description of the semantic in the chosen format
	 * @throws IOException if there occurred problems creating the input stream
	 * @throws OWLOntologyStorageException if there occurred problems with the format
	 */
	public abstract InputStream convertToSemanticStandard(String type)
    		throws IOException, OWLOntologyStorageException;
	
}
