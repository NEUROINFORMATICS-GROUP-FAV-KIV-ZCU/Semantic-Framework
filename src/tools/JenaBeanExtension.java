package tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Defines user interface for working with the JenaBeanExtension library.
 * 
 * @author Jakub Krauz
 */
public interface JenaBeanExtension {

	/**
	 * <p>Creates a serialization of the ontology model as a RDF/XML document.</p>
	 * <p>This method doesn't run the transformation process itself, it only
	 * creates the XML description of the ontology model.</p>
	 * 
	 * @return RDF/XML ontology document
	 * @throws IOException if there occurred problems creating the stream
	 */
	public InputStream getOntologyDocument() throws IOException;


	/**
	 * <p>Creates a serialization of the ontology model as an ontology document
	 * in a specified language. Predefined values can be found in {@link Syntax}.</p>
	 * <p>This method doesn't run the transformation process itself, it only
	 * creates the XML description of the ontology model.</p>
	 * 
	 * @param lang Language of the ontology document.
	 * @return RDF/XML ontology document
	 * @throws IOException if there occurred problems creating the stream
	 * @see Syntax
	 */
	public InputStream getOntologyDocument(String lang) throws IOException;
	
	
	/**
	 * <p>Writes a serialization of the ontology model as a RDF/XML document
	 * into a given output stream.</p>
	 * <p>This method doesn't run the transformation process itself, it only
	 * creates the XML description of the ontology model.</p>
	 * 
	 * @param out The output stream to which the RDF is written.
	 */
	public void writeOntologyDocument(OutputStream out);
	
	
	/**
	 * <p>Writes a serialization of the ontology model as an ontology document
	 * into a given output stream. The language in which to write the document
	 * is specified by the <code>lang</code> argument. Predefined values
	 * can be found in {@link Syntax}.</p>
	 * <p>This method doesn't run the transformation process itself, it only
	 * creates the XML description of the ontology model.</p>
	 * 
	 * @param out The output stream to which the RDF is written.
	 * @param lang Language of the ontology document.
	 * @see Syntax
	 */
	public void writeOntologyDocument(OutputStream out, String lang);
	

}
