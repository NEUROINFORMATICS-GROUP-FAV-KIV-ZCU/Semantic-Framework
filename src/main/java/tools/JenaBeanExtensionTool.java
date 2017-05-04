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

import thewebsemantic.Base;
import thewebsemantic.Bean2RDF;
import thewebsemantic.UserDefNamespace;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * This tool controls the transformation from OOP to OWL.<br>
 * User can transform an object-oriented data model into an OWL ontology.
 * The RDF (OWL) model is created in Jena. Serialization of the ontology
 * is provided in several syntaxes (RDF/XML, TURTLE, N-TRIPLE, N3).
 * 
 * @author Jakub Krauz
 */
public class JenaBeanExtensionTool implements JenaBeanExtension {
	
	/** Default syntax of the ontology document. */
	public static final String DEFAULT_SYNTAX = Syntax.RDF_XML;
	
	/** logger */
	private Log log = LogFactory.getLog(getClass());
	
	/** loaded ontology model */
	private OntModel model;
	
	/** defines the xml:base value for the ontology document */
	private String xmlBase;


	/**
	 * Creates an empty ontology model in the default specification
	 * (OWL-DL without inferencing).
	 */
	public JenaBeanExtensionTool() {
		/* parameter OntModelSpec.OWL_DL_MEM disables reasoner included
		 * in ModelFactory.createOntologyModel() as default,
		 * which led to a very slow computation */
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	}
	
	
	/**
	 * Creates an empty ontology model in the given specification.<br>
	 * The specification can be e.g. OWL Full, OWL-DL, OWL-Lite,
	 * without reasoning or with an inferencer etc.
	 * If the <code>specification</code> argument is null, the default specification
	 * <code>OntModelSpec.OWL_DL_MEM</code> is used.<br>
	 * 
	 * @param specification specification of the ontology model
	 */
	public JenaBeanExtensionTool(OntModelSpec specification) {
		OntModelSpec spec = (specification == null) ? OntModelSpec.OWL_DL_MEM : specification;
		model = ModelFactory.createOntologyModel(spec);
	}
	
	
	/**
	 * Creates an ontology model and loads statements from
	 * a specified ontology document.<br>
	 * This document can contain additional statements
	 * that cannot be gathered from the object-oriented model, such as the ontology
	 * header etc. Syntax of the ontology document (or serialization of a RDF-based graph)
	 * is specified by the <code>syntax</code> argument.
	 * 
	 * @param ontologyDocument stream containing statements to be loaded
	 * @param syntax syntax of the serialization of statements
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
			Object o = dataList.get(i);
			log.debug("Processing bean: " + o);
			loader.save(o);
		}
		log.debug("Ontology model was created.");
	}


	@Override
	public InputStream getOntologyDocument() {
		return getOntologyDocument(DEFAULT_SYNTAX);
	}
	
	
	@Override
	public InputStream getOntologyDocument(String syntax) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeOntologyDocument(out, syntax);
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	
	@Override
	public void writeOntologyDocument(OutputStream out) {
		writeOntologyDocument(out, DEFAULT_SYNTAX);
	}
	
	
	@Override
	public void writeOntologyDocument(OutputStream out, String syntax) {
		if (! Syntax.isValidSyntaxName(syntax)) {
			log.error("Unsupported syntax name: " + syntax + "! Writing ontology in the default syntax...");
			syntax = DEFAULT_SYNTAX;
		}
		RDFWriter writer = model.getWriter(syntax);
		if (syntax.contains("XML")) {
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
	public InputStream getOntologySchema(String syntax) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeOntologySchema(out, syntax);
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	
	@Override
	public void writeOntologySchema(OutputStream out, String syntax) {
		if (! Syntax.isValidSyntaxName(syntax)) {
			log.error("Unsupported syntax name: " + syntax + "! Writing ontology in the default syntax...");
			syntax = DEFAULT_SYNTAX;
		}		
		
		OntModel schema = ModelFactory.createOntologyModel(model.getSpecification());
		schema.add(model);
		for (String prefix : model.getNsPrefixMap().keySet())
			if (!prefix.startsWith("j."))
				schema.setNsPrefix(prefix, model.getNsPrefixMap().get(prefix));
		
		// remove all individuals from the schema
		ExtendedIterator<Individual> individuals = schema.listIndividuals();
		Selector selector;
		while (individuals.hasNext()) {
			selector = new SimpleSelector(individuals.next(), null, (RDFNode) null);
			schema.remove(schema.listStatements(selector));
		}

		RDFWriter writer = schema.getWriter(syntax);
		if (syntax.contains("XML")) {
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
		declareAllClassesDisjoint(model);
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
	
	
	
	/**
	 * Declares all classes in model <code>m</code> disjoint.
	 * 
	 * @param m - model in which to declare disjoint classes
	 */
	private void declareAllClassesDisjoint(OntModel m) {
		
		/* create list with root classes from the OOM */
		ExtendedIterator<OntClass> iterator = m.listHierarchyRootClasses();
		RDFList list = m.createList();
		OntClass ontClass;
		Property javaclass = m.createProperty(Base.JAVACLASS);
		while (iterator.hasNext()) {
			ontClass = iterator.next();
			if (ontClass.hasProperty(javaclass))
				list = list.with(ontClass);
		}
		
		/* declare root classes disjoint */
		Resource res = ResourceFactory.createResource();
		m.add(res, RDF.type, OWL2.AllDisjointClasses);
		m.add(res, OWL2.members, list);
		
		/* declare subclasses of one class disjoint */
		List<Statement> stmtList;
		for (OntClass ontCls : m.listNamedClasses().toList()) {
			stmtList = m.listStatements(new SimpleSelector(null, RDFS.subClassOf, ontCls)).toList();
			if (stmtList.size() >= 2) {
				list = m.createList();
				for (Statement stmt : stmtList)
					list = list.with(stmt.getSubject());
				res = ResourceFactory.createResource();
				m.add(res, RDF.type, OWL2.AllDisjointClasses);
				m.add(res, OWL2.members, list);
			}
		}
		
	}
	

}
