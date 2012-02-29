package tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import thewebsemantic.Bean2RDF;
import thewebsemantic.UserDefNamespace;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;

/**
 * This tool controls the transformation library. User can transform an
 * object-oriented data model into a semantic resource.
 * 
 * @author Jakub Krauz
 */
public class JenaBeanExtensionTool implements JenaBeanExtension {
	
	/** default language of the ontology document */
	public static final String DEFAULT_LANG = Syntax.RDF_XML;
	
	/** logger */
	private Log log = LogFactory.getLog(getClass());
	
	/** loaded ontology model */
	private OntModel model;
	
	/** defines the xml:base value for the ontology document */
	private String xmlBase;


	/**
	 * Loads data from the list of objects and creates an ontology model
	 * in the default specification (OWL-DL without inferencing).
	 * 
	 * @param dataList list of objects - object-orinted model
	 */
	public JenaBeanExtensionTool(List<Object> dataList) {
		/* parameter OntModelSpec.OWL_DL_MEM disables reasoner included
		 * in ModelFactory.createOntologyModel() as default,
		 * which led to a very slow computation */
		createModel(dataList, OntModelSpec.OWL_DL_MEM, false);
	}
	
	
	/**
	 * Loads data from the list of objects and creates an ontology model
	 * in the given specification (e.g. OWL Full, OWL-DL, OWL-Lite, without
	 * reasoning or with an inferencer etc.).
	 * @see OntModelSpec
	 * 
	 * @param dataList - list of objects - object-orinted model
	 * @param specification - specification of the ontology model
	 */
	public JenaBeanExtensionTool(List<Object> dataList, OntModelSpec specification) {
		createModel(dataList, specification, false);
	}


	/**
	 * Loads data from the list of objects and creates an ontology model
	 * in the default specification (OWL-DL without inferencing).
	 * Sets the default namespace for the whole model.
	 * 
	 * @param dataList - list of objects - object-oriented model
	 * @param namespace - namespace for the whole model
	 */
	public JenaBeanExtensionTool(List<Object> dataList, String namespace) {
		UserDefNamespace.set(namespace);
		createModel(dataList, OntModelSpec.OWL_DL_MEM, false);
	}
	
	
	/**
	 * Loads data from the list of objects and creates an intology model
	 * in the default specification (OWL-DL without inferencing).
	 * If the <code>structureOnly</code> parameter is set true, then
	 * the ontology model does not contain any data, only their structure
	 * (i.e. classes, properties and their relations).
	 * @param dataList - list of objects (object-oriented model)
	 * @param structureOnly - true if we do not need data (only structure)
	 */
	public JenaBeanExtensionTool(List<Object> dataList, boolean structureOnly) {
		createModel(dataList, OntModelSpec.OWL_DL_MEM, structureOnly);
	}


	@Override
	public InputStream getOntologyDocument() {
		return getOntologyDocument(DEFAULT_LANG);
	}
	
	
	@Override
	public InputStream getOntologyDocument(String lang) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeOntologyDocument(out, lang);
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	
	@Override
	public void writeOntologyDocument(OutputStream out) {
		writeOntologyDocument(out, DEFAULT_LANG);
	}
	
	
	@Override
	public void writeOntologyDocument(OutputStream out, String lang) {
		if (! Syntax.isValidSyntaxName(lang)) {
			log.error("Unsupported syntax name: " + lang + "! Writing ontology in the default syntax...");
			lang = DEFAULT_LANG;
		}
		RDFWriter writer = model.getWriter(lang);
		if (lang.contains("XML")) {
			writer.setProperty("showXmlDeclaration", true);
			writer.setProperty("xmlbase", xmlBase);
		}
		writer.write(model, out, null);
	}
	
	
	@Override
	public void setBasePackage(String basePackage) {
		xmlBase = "http://" + basePackage;
	}
	
	
	@Override
	public void setOntology(Ontology ontology) {		
		com.hp.hpl.jena.ontology.Ontology res = model.createOntology(ontology.getUri());
		String value;
		String[] valueArray;
		if ((value = ontology.getBackwardCompatibleWith()) != null)
			res.addBackwardCompatibleWith(model.createResource(value));
		if ((value = ontology.getIncompatibleWith()) != null)
			res.addIncompatibleWith(model.createResource(value));
		if ((value = ontology.getPriorVersion()) != null)
			res.addPriorVersion(model.createResource(value));
		if ((value = ontology.getVersionInfo()) != null)
			res.addVersionInfo(value);
		if ((value = ontology.getComment()) != null)
			res.addComment(model.createLiteral(value));
		if ((valueArray = ontology.getImports()) != null)
			for (String elem : valueArray)
				res.addImport(model.createResource(elem));
		if ((value = ontology.getLabel()) != null)
			res.addLabel(model.createLiteral(value));
		if ((value = ontology.getSeeAlso()) != null)
			res.addSeeAlso(model.createResource(value));
	}


	/**
	 * Creates the semantic model and fills it with the user data
	 * 
	 * @param dataList - list of user objects
	 * @param spec - specification of the ontology model
	 */
	private void createModel(List<Object> dataList, OntModelSpec spec, boolean structureOnly) {
		log.debug("Started creating ontology model.");
		model = ModelFactory.createOntologyModel(spec);
		
		// true urcuje, ze nacitam pouze staticky model dat
		Bean2RDF loader = new Bean2RDF(model, structureOnly);
		
		for (int i = 0; i < dataList.size(); i++) {
			loader.save(dataList.get(i));
		}
		log.debug("Ontology model was created.");
	}
	

}
