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
	 * Loads data from the object-oriented model into the ontology model.
	 * If the <code>structureOnly</code> parameter is set true, then
	 * the ontology model does not contain any data, only their structure
	 * (i.e. classes, properties and their relations).
	 * 
	 * @param dataList - list of objects (object-oriented model)
	 * @param structureOnly - true if we do not need data (only structure)
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
	 * @return ontology document
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
	 * <p>Creates a serialization of the ontology model schema 
	 * in a specified language. Predefined values can be found in {@link Syntax}.
	 * The schema describes structure of the ontology (i.e. classes and properties)
	 * and contains no data.</p>
	 * <p>This method doesn't run the transformation process itself, it only
	 * creates the XML description of the ontology model.</p>
	 * 
	 * @param lang Language of the ontology schema document.
	 * @return ontology schema document
	 * @throws IOException if there occurred problems creating the stream
	 * @see Syntax
	 */
	public InputStream getOntologySchema(String lang);
	
	
	/**
	 * <p>Writes a serialization of the ontology model schema into the given
	 * output stream. The schema describes structure of the ontology
	 * (i.e. classes and properties) and contains no data. The language
	 * in which to write the schema document is specified by the <code>lang</code>
	 * argument. Predefined values can be found in {@link Syntax}.</p>
	 * <p>This method doesn't run the transformation process itself, it only
	 * creates the XML description of the ontology model.</p>
	 * 
	 * @param lang Language of the ontology schema document.
	 * @return ontology schema document
	 * @throws IOException if there occurred problems creating the stream
	 * @see Syntax
	 */
	public void writeOntologySchema(OutputStream out, String lang);
	
	
	/**
	 * <p>Loads statements from a specified document and adds them to
	 * the ontology model. This can be used especially for adding
	 * elements that cannot be gathered from the object-oriented model,
	 * such as an ontology header. Another way to set the ontology
	 * header is to use the <code>setOntology()</code> method.</p>
	 * 
	 * <p>NOTE: If the file contains the ontology header (which is
	 * the main intent to use it) this method must be invoked
	 * before loading the object-oriented model so as the ontology
	 * properties (such as ontology namespace) take effect.</p>
	 * 
	 * @param filePath - filename of the ontology document to be loaded
	 */
	public void loadStatements(InputStream ontologyDocument);
	
	
	/**
	 * <p>Adds the <code>owl:AllDisjointClasses</code> statement declaring
	 * all classes contained in the model to be different. This is
	 * an OWL2 language construct.</p>
	 */
	public void declareAllClassesDisjoint();
	
	
	/**
	 * <p>Sets the default namespace for the whole ontology model. That means that
	 * every entity will have this namespace except for those that have
	 * explicitly defined namespace using <code>@Namespace</code>.</p>
	 * 
	 * <p>NOTE: This method must be invoked before loading the object-oriented
	 * model so as the namespace value take effect for the ontology.</p>
	 * 
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
