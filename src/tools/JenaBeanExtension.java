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
	
	
	/**
	 * <p>Sets the base package of POJO objects. This value is used in the
	 * RDF/XML ontology document to abbreviate resources' URIs using
	 * the <code>xml:base</code> element and relative URIs.</p>
	 * 
	 * @param basePackage - POJO package
	 */
	public void setBasePackage(String basePackage);
	
	
	/**
	 * <p>Adds the ontology header to the ontology document.
	 * An ontology header consists of the <code>owl:Ontology</code>
	 * element with axioms about the ontology.</p>
	 * <p>Ontology axioms are set acording to values contained in the
	 * <code>Ontology</code> instance passed to this method.</p>
	 * 
	 * @param ontology - object containing ontology properties
	 */
	public void setOntology(Ontology ontology);
	

}
