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
 * This tool controls the OWL API library.
 * User can convert among serialization syntaxes.
 * 
 * @author Jakub Krauz
 */
public class OwlApiTool implements OwlApi {
	
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

	
	/**
	 * Gets the ontology document in a required syntax.
	 * Possible syntaxes are RDF/XML, OWL/XML, Turtle.
	 */
	@Override
    public InputStream getOntologyDocument(String syntax)
        					throws IOException, OWLOntologyStorageException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (syntax.equalsIgnoreCase(Syntax.RDF_XML)) {
            log.debug("Serializing in RDF/XML syntax.");
            manager.saveOntology(ontology, new RDFXMLOntologyFormat(), out);
        } else if (syntax.equalsIgnoreCase(Syntax.OWL_XML)) {
            log.debug("Serializing in OWL/XML syntax.");
            manager.saveOntology(ontology, new OWLXMLOntologyFormat(), out);
        } else if (syntax.equalsIgnoreCase(Syntax.OWL_FUNCTIONAL)) {
        	log.debug("Serializing in OWL Functional-Style syntax.");
        } else if (syntax.equalsIgnoreCase(Syntax.TURTLE)) {
            log.debug("Serializing in TURTLE syntax.");
            manager.saveOntology(ontology, new TurtleOntologyFormat(), out);
        } else {
            log.error("Unknown syntax requested.");
        }
        out.flush();
        InputStream output = new ByteArrayInputStream(out.toByteArray());
        out.close();
        return output;
    }


}
