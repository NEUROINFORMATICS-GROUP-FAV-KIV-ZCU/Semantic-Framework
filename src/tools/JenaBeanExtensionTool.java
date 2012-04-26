package tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import thewebsemantic.Bean2RDF;
import thewebsemantic.UserDefNamespace;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * This tool controls the transformation library. User can transform an
 * object-oriented data model into OWL ontology.
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
	 */
	public JenaBeanExtensionTool() {
		/* parameter OntModelSpec.OWL_DL_MEM disables reasoner included
		 * in ModelFactory.createOntologyModel() as default,
		 * which led to a very slow computation */
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	}
	
	
	/**
	 * Creates an empty ontology model in the given specification
	 * (e.g. OWL Full, OWL-DL, OWL-Lite, without reasoning or with an inferencer etc.).
	 * If the <code>specification</code> argument is null, the default specification
	 * <code>OntModelSpec.OWL_DL_MEM</code> is used.<br>
	 *
	 * @see OntModelSpec
	 * 
	 * @param specification - specification of the ontology model
	 */
	public JenaBeanExtensionTool(OntModelSpec specification) {
		OntModelSpec spec = (specification == null) ? OntModelSpec.OWL_DL_MEM : specification;
		model = ModelFactory.createOntologyModel(spec);
	}
	
	
	/**
	 * Creates an ontology model in the given specification and loads statements from
	 * a specified ontology document. This document can contain additional statements
	 * that cannot be gathered from the object-oriented model, such as an ontology
	 * header etc. Syntax of the ontology document (or serialization of a RDF-based graph)
	 * is specified by the <code>syntax</code> argument.
	 * 
	 * @param ontologyDocument - stream containing statements to be loaded
	 * @param syntax - syntax of the serialization of statements
	 */
	public JenaBeanExtensionTool(InputStream ontologyDocument, String syntax) {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		loadStatements(ontologyDocument, syntax);
	}
	
	
	@Override
	public void loadOOM(List<Object> dataList) {
		loadOOM(dataList, false);
	}
	
	
	@Override
	public void loadOOM(List<Object> dataList, boolean structureOnly) {
		log.debug("Started loading object-oriented model.");		
		Bean2RDF loader = new Bean2RDF(model, structureOnly);
		for (int i = 0; i < dataList.size(); i++) {
			loader.save(dataList.get(i));
		}
		log.debug("Ontology model was created.");
	}


	@Override
	public InputStream getOntologyDocument() {
		return getOntologyDocument(DEFAULT_LANG);
	}
	
	
	@Override
	public InputStream getOntologyDocument(String syntax) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeOntologyDocument(out, syntax);
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
			writer.setProperty("xmlbase", xmlBase + "#");
			//writer.setProperty("allowBadURIs", true);
			writer.setProperty("showDoctypeDeclaration", true);
			writer.setProperty("relativeURIs", "");
			//writer.setProperty("blockRules", "idAttr");
		}
		writer.write(model, out, xmlBase);
	}
	
	
	@Override
	public InputStream getOntologySchema(String lang) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeOntologySchema(out, lang);
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	
	@Override
	public void writeOntologySchema(OutputStream out, String lang) {
		if (! Syntax.isValidSyntaxName(lang)) {
			log.error("Unsupported syntax name: " + lang + "! Writing ontology in the default syntax...");
			lang = DEFAULT_LANG;
		}
		
		// lenghty creation of the schema in order to get rid of ballast generated due to proxy classes in the Portal
		// can be replaced by commented block below when resolved
		
		OntModel schema = ModelFactory.createOntologyModel(model.getSpecification());
		ExtendedIterator<OntClass> classes = model.listClasses();
		Selector selector;
		while (classes.hasNext()) {
			selector = new SimpleSelector(classes.next(), null, (RDFNode) null);
			schema.add(model.listStatements(selector));
		}
		// not very clear solution to declare all disjoint classes in the schema if defined in model
		if (model.listStatements(null, RDF.type, OWL2.AllDisjointClasses).hasNext()) {
			ExtendedIterator<OntClass> iterator = schema.listClasses();
			RDFList list = schema.createList();
			while(iterator.hasNext())
				list = list.with(iterator.next());
			Resource res = ResourceFactory.createResource();
			schema.add(res, RDF.type, OWL2.AllDisjointClasses);
			schema.add(res, OWL2.members, list);
		}
		ExtendedIterator<OntProperty> properties = model.listAllOntProperties();
		while (properties.hasNext()) {
			selector = new SimpleSelector(properties.next(), null, (RDFNode) null);
			schema.add(model.listStatements(selector));
		}
		if (model.listOntologies().hasNext()) {
			selector = new SimpleSelector(model.listOntologies().next(), null, (RDFNode) null);
			schema.add(model.listStatements(selector));
		}
		
		
		/*OntModel schema = ModelFactory.createOntologyModel(model.getSpecification());
		schema.add(model);
		
		// remove all individuals from the schema
		ExtendedIterator<Individual> individuals = schema.listIndividuals();
		Selector selector;
		while (individuals.hasNext()) {
			selector = new SimpleSelector(individuals.next(), null, (RDFNode) null);
			schema.remove(schema.listStatements(selector));
		}*/

		RDFWriter writer = schema.getWriter(lang);
		if (lang.contains("XML")) {
			writer.setProperty("showXmlDeclaration", true);
			writer.setProperty("xmlbase", xmlBase + "#");
			writer.setProperty("relativeURIs", "");
		}
		writer.write(schema, out, xmlBase);
	}
	
	
	@Override
	public void loadStatements(InputStream ontologyDocument, String syntax) {
		model.getReader(syntax).read(model, ontologyDocument, null);
		try {
			ontologyDocument.close();
		} catch (IOException e) {
			log.error("I/O error occured when closing the input stream associated with the ontology configuration document.");
		}
		try {
			com.hp.hpl.jena.ontology.Ontology ont = model.listOntologies().next();
			UserDefNamespace.set(ont.getURI());
			setBase(ont.getURI());
			if (ont.getVersionInfo() == null)
				ont.setVersionInfo(DateFormat.getDateInstance(DateFormat.LONG, Locale.UK).format(new Date()));
		} catch (NoSuchElementException e) {
			log.error("No ontology header found in the ontology configuration document!");
		}
	}
	
	
	@Override
	public void declareAllClassesDisjoint() {
		ExtendedIterator<OntClass> iterator = model.listClasses();
		RDFList list = model.createList();
		while(iterator.hasNext())
			list = list.with(iterator.next());

		Resource res = ResourceFactory.createResource();
		model.add(res, RDF.type, OWL2.AllDisjointClasses);
		model.add(res, OWL2.members, list);
	}
	
	
	@Override
	public void setBase(String base) {
		xmlBase = base.startsWith("http://") ? base : "http://" + base;
	}
	
	
	@Override
	public void setNamespace(String namespace) {
		UserDefNamespace.set(namespace);
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
	

}
