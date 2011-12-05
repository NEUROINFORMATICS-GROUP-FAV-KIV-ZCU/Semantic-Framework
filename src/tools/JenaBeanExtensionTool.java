package tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import thewebsemantic.UserDefNamespace;
import thewebsemantic.binding.Jenabean;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

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

	/** provides operations over semantic model */
	private Jenabean jenaBean = Jenabean.instance();

	/** XML document describing semantic model */
	private ByteArrayOutputStream ontologyDocument;
	
	/** language of the ontology document */
	private String ontDocumentLang;


	/**
	 * Loads data from a list of objects and creates the semantic model.
	 * 
	 * @param dataList list of objects - object-orinted model
	 */
	public JenaBeanExtensionTool(List<Object> dataList) {
		createModel(dataList);
	}


	/**
	 * Loads data from a list of objects and creates the semantic model.
	 * Sets the default namespace for the whole model.
	 * 
	 * @param dataList list of objects - object-oriented model
	 * @param namespace namespace for the whole model
	 */
	public JenaBeanExtensionTool(List<Object> dataList, String namespace) {
		UserDefNamespace.set(namespace);
		createModel(dataList);
	}


	@Override
	public InputStream getOntologyDocument() {
		return getOntologyDocument(DEFAULT_LANG);
	}
	
	
	@Override
	public InputStream getOntologyDocument(String lang) {
		if (ontologyDocument == null || !ontDocumentLang.equals(lang)) {
			createOntologyDocument(lang);
			ontDocumentLang = lang;
		}
		InputStream is = new ByteArrayInputStream(ontologyDocument.toByteArray());
		return is;
	}
	
	
	@Override
	public void writeOntologyDocument(OutputStream out) {
		jenaBean.writeModel(out, DEFAULT_LANG);
	}
	
	
	@Override
	public void writeOntologyDocument(OutputStream out, String lang) {
		jenaBean.writeModel(out, lang);
	}


	/**
	 * Creates the semantic model and fills it with the user data
	 * 
	 * @param dataList list of user objects
	 */
	private void createModel(List<Object> dataList) {
		log.debug("Started creating ontology model.");

		/* parameter OntModelSpec.OWL_DL_MEM disables reasoner included in
		 * ModelFactory.createOntologyModel(); as default,
		 * which led to a very slow computation */
		jenaBean.bind(ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM));
		
		for (int i = 0; i < dataList.size(); i++) {
			jenaBean.writer().save(dataList.get(i));
		}
		log.debug("Ontology model was created.");
	}
	
	
	/**
	 * Creates the ontology document in a specified language.
	 * 
	 * @param lang Required language for the ontology document.
	 * 			   If null, the default language RDF/XML is set.
	 */
	private void createOntologyDocument(String lang) {
		ontologyDocument = new ByteArrayOutputStream();
		jenaBean.writeModel(ontologyDocument, lang);
	}

}
