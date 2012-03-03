package tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.*;

/**
 * This tool controls the OwlApi library.
 * User can convert among standard semantic formats.
 * @author Jakub Krauz
 */
public class OwlApiTool implements OwlApi {
	
	/** RDF/XML format */
	public static final String RDF = "rdf";
	
	/** OWL/XML format */
	public static final String OWL = "owl";
	
	/** Turtle format */
	public static final String TTL = "ttl";
	
	/** logger */
	private Log log = LogFactory.getLog(getClass());
	
	/** provides operations over ontology */
	private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	
	/** ontology - semantic model */
	private OWLOntology ontology;

	
	/**
	 * Loads ontology from the ontology document.
	 * @param ontologyDocument document describing ontology
	 * @throws OWLOntologyCreationException if problems loading ontology occurred
	 */
	public OwlApiTool(InputStream ontologyDocument) throws OWLOntologyCreationException {
        log.debug("Started loading ontology.");
        ontology = manager.loadOntologyFromOntologyDocument(ontologyDocument);
        log.debug("Ontology was loaded.");
    }

	
	@Override
	/**
	 * Possible standards are RDF/XML, OWL/XML, Turtle/XML.
	 */
    public InputStream convertToSemanticStandard(String standard)
        					throws IOException, OWLOntologyStorageException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (standard.equalsIgnoreCase(RDF)) {
            log.debug("Converting to RDF/XML standard.");
            manager.saveOntology(ontology, new RDFXMLOntologyFormat(), out);
        } else if (standard.equalsIgnoreCase(OWL)) {
            log.debug("Converting to OWL/XML standard.");
            manager.saveOntology(ontology, new OWLXMLOntologyFormat(), out);
        } else if (standard.equalsIgnoreCase(TTL)) {
            log.debug("Converting to TTL/XML standard.");
            manager.saveOntology(ontology, new TurtleOntologyFormat(), out);
        } else {
            log.error("Unknown semantic standard requested.");
        }
        out.flush();
        InputStream output = new ByteArrayInputStream(out.toByteArray());
        out.close();
        return output;
    }


}
