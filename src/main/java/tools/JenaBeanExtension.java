package tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import thewebsemantic.annotations.Namespace;

/**
 * Defines user interface that controlls the OOP to OWL
 * transformation process.
 * 
 * @author Jakub Krauz
 */
public interface JenaBeanExtension {
	
	/**
	 * <p>
	 * Loads data and their structure from the object-oriented model
	 * into the ontology model.
	 * </p>
	 * 
	 * @param dataList list of objects (object-oriented model)
	 */
	public void loadOOM(List<Object> dataList);
	
	/**
	 * <p>
	 * Loads data and their structure from the object-oriented model
	 * into the ontology model.
	 * If the <code>structureOnly</code> parameter is set true, then
	 * the ontology model does not contain any data, only their structure
	 * (i.e. classes, properties and their relations).
	 * </p>
	 * 
	 * @param dataList list of objects (object-oriented model)
	 * @param structureOnly true if we don't want data to be loaded (only structure)
	 */
	public void loadOOM(List<Object> dataList, boolean structureOnly);

	/**
	 * <p>Creates a serialization of the ontology model in RDF/XML syntax.<br>
	 * This method doesn't run the transformation process itself, it only
	 * creates the XML serialization of the ontology model.</p>
	 * 
	 * @return RDF/XML ontology document
	 * @throws IOException if there occurred problems creating the stream
	 */
	public InputStream getOntologyDocument() throws IOException;


	/**
	 * <p>Creates a serialization of the ontology model in specified syntax.
	 * Predefined values can be found in {@link Syntax}.<br>
	 * This method doesn't run the transformation process itself, it only
	 * creates the serialization of the ontology model.</p>
	 * 
	 * @param syntax syntax of the ontology document
	 * @return ontology document
	 * @throws IOException if there occurred problems creating the stream.
	 * @see Syntax
	 */
	public InputStream getOntologyDocument(String syntax) throws IOException;
	
	
	/**
	 * <p>Writes a serialization of the ontology model in RDF/XML syntax
	 * into the given output stream.<br>
	 * This method doesn't run the transformation process itself, it only
	 * creates the XML serialization of the ontology model.</p>
	 * 
	 * @param out the output stream to which the serialization is written
	 */
	public void writeOntologyDocument(OutputStream out);
	
	
	/**
	 * <p>Writes a serialization of the ontology model into the given output
	 * stream. Syntax of the serialization
	 * is specified by the <code>syntax</code> argument. Predefined values
	 * can be found in {@link Syntax}.<br>
	 * This method doesn't run the transformation process itself, it only
	 * creates the serialization of the ontology model.</p>
	 * 
	 * @param out the output stream to which the ontology is written
	 * @param syntax syntax of the ontology document
	 * @see Syntax
	 */
	public void writeOntologyDocument(OutputStream out, String syntax);
	
	
	/**
	 * <p>Creates a serialization of the ontology schema in specified
	 * syntax. Predefined values can be found in {@link Syntax}.
	 * The schema describes structure of the ontology (i.e. classes and
	 * properties) and contains no data.<br>
	 * This method doesn't run the transformation process itself, it only
	 * creates the serialization of the ontology model.</p>
	 * 
	 * @param syntax syntax of the ontology schema document
	 * @return ontology schema document
	 * @throws IOException if there occurred problems creating the stream
	 * @see Syntax
	 */
	public InputStream getOntologySchema(String syntax);
	
	
	/**
	 * <p>Writes a serialization of the ontology schema into the given
	 * output stream.<br>
	 * The schema describes structure of the ontology
	 * (i.e. classes and properties), but contains no data. Syntax of
	 * the schema document is specified by the <code>syntax</code>
	 * argument. Predefined values can be found in {@link Syntax}.<br>
	 * This method doesn't run the transformation process itself, it only
	 * creates the serialization of the ontology model.</p>
	 * 
	 * @param syntax syntax of the ontology schema document
	 * @throws IOException if there occurred problems creating the stream
	 * @see Syntax
	 */
	public void writeOntologySchema(OutputStream out, String syntax);
	
	
	/**
	 * <p>Loads statements from the specified document and adds them to
	 * the ontology model.<br>
	 * This can be used especially for adding
	 * elements that cannot be gathered from the object-oriented model,
	 * such as the ontology header. Another way to set the ontology
	 * header is to use the {@link #setOntology(Ontology)} method.</p>
	 * 
	 * <p>This method can be used also to load a whole ontology from
	 * a previous serialization.</p>
	 * 
	 * <p>NOTE: If the stream contains the ontology header (which is
	 * the main intent to use it) this method must be invoked
	 * before loading the object-oriented model so as the ontology
	 * properties (such as ontology namespace) take effect.</p>
	 * 
	 * @param ontologyDocument stream containing serialization of RDF-based graph
	 * @param syntax syntax of the serialization
	 */
	public void loadStatements(InputStream ontologyDocument, String syntax);
	
	
	/**
	 * <p>Adds the <code>owl:AllDisjointClasses</code> statement declaring
	 * all classes contained in the ontology to be different. This is
	 * an OWL2 language construct.</p>
	 */
	public void declareAllClassesDisjoint();
	
	
	/**
	 * <p>Sets the default namespace for all entities in the ontology.<br>
	 * It means that
	 * every entity will have this namespace except for those that have
	 * explicitly defined namespace using {@link Namespace}.</p>
	 * 
	 * <p>NOTE: This method must be invoked before loading the object-oriented
	 * model so as the namespace value take effect for the ontology.</p>
	 * 
	 * @param namespace default namespace for the ontology
	 */
	public void setNamespace(String namespace);
	
	
	/**
	 * <p>Sets the base URI of the generated ontology. This value is used in the
	 * RDF/XML ontology document to abbreviate resources' URIs using
	 * the <code>xml:base</code> element and relative URIs.</p>
	 * 
	 * @param base base URI of the ontology document
	 */
	public void setBase(String base);
	
	
	/**
	 * <p>Adds the ontology header to the ontology model.
	 * The ontology header consists of the <code>owl:Ontology</code>
	 * element with axioms about the ontology.<br>
	 * Ontology axioms are set acording to values contained in the
	 * {@link Ontology} instance passed to this method.</p>
	 * 
	 * @param ontology object containing ontology properties
	 */
	public void setOntology(Ontology ontology);
	

}
