package tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Defines user interface for working with the JenaBeanExtension library.
 * 
 * @author Jakub Krauz
 */
public interface JenaBeanExtension {
	
	
	/**
	 * Loads the object-oriented model and transforms it into the
	 * ontology model.
	 * 
	 * @param dataList - object-oriented model (list of data objects)
	 * @param structureOnly - if true the ontology model contains no data
	 * 						  itself, only their structure
	 */
	public void loadOOM(List<Object> dataList, boolean structureOnly);

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
	 * <p>Loads statements from a specified document and adds them to
	 * the ontology model. This can be used especially for adding
	 * elements that cannot be gathered from the object-oriented model,
	 * such as an ontology header. Another way to set the ontology
	 * header is to use the <code>setOntology()</code> method.</p>
	 * 
	 * @param filePath - filename of the ontology document to be loaded
	 */
	public void loadStatements(String filePath);
	
	
	/**
	 * Sets the default namespace for the whole ontology model. That means that
	 * every entity will have this namespace except for those that have
	 * explicitly defined namespace using <code>@Namespace</code>.
	 * @param namespace - default namespace for the whole model
	 */
	public void setNamespace(String namespace);
	
	
	/**
	 * <p>Sets the base package of POJO objects. This value is used in the
	 * RDF/XML ontology document to abbreviate resources' URIs using
	 * the <code>xml:base</code> element and relative URIs.</p>
	 * 
	 * @param basePackage - POJO package
	 */
	public void setBase(String base);
	
	
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
