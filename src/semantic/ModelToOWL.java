package semantic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.UnloadableImportException;

/**
 * Transforms object-oriented model into the semantic model
 * in an OWL format (OWL = Web Ontology Language).
 * 
 * @author Jakub Krauz
 */
public class ModelToOWL extends ModelToRDF {
	
	/** logger */
	private Log log = LogFactory.getLog(getClass());
	
	
	/**
	 * If using this nonparametric constructor, the parametric
	 * version of method createSemantic() must be called.
	 */
	public ModelToOWL() {
		
	}
	
	
	/**
	 * Sets the data. Nonparametric version of createSemantic() can
	 * be used afterwards.
	 * @param dataList object-oriented model
	 * @param namespace namespace value
	 * @throws IOException 
	 * @throws UnknownDataException 
	 */
	public ModelToOWL(List<Object> dataList, String namespace) throws IOException {
		super(dataList, namespace);
	}
	
	
	/**
	 * @return stream containing OWL/XML describing semantic
	 */
	@Override
	public InputStream createSemantic(List<Object> dataList, String namespace)
								throws IOException, OntologyException {
		this.dataList = dataList;
		this.namespace = namespace;
		toRDF();
		return toOWL(output);
	}
	
	
	/**
	 * @return stream containing OWL/XML describing semantic
	 */
	@Override
	public InputStream createSemantic() throws OntologyException {
		return toOWL(output);
	}
	
	
	/**
	 * Converts RDF/XML format into OWL/XML.
	 * @param inputStreamRDF stream containing RDF/XML
	 * @return stream containing OWL/XML describing semantic
	 */
	private InputStream toOWL(InputStream inputStreamRDF) throws OntologyException {
		log.debug("- OWL API -");
		if (inputStreamRDF == null) {
			log.debug("Could not load ontology. Input stream does not exist.");
			return null;
		}
		ByteArrayOutputStream owlOutput = new ByteArrayOutputStream();
		
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputStreamRDF);
            log.debug("Loaded ontology: " + ontology);
            try {
                manager.saveOntology(ontology, new OWLXMLOntologyFormat(), owlOutput);
                log.debug("Ontology model converted into OWL format.");
            } catch (OWLOntologyStorageException e) {
            	log.error("Error occurred while converting ontology model!", e);
            	throw new OntologyException(e);
            }
        } catch (OWLOntologyCreationIOException e) {
            // IOExceptions during loading get wrapped in an OWLOntologyCreationIOException
        	log.error("Could not load ontology.", e);
        	throw new OntologyException(e);
        	/* IOException ioException = e.getCause();
            if (ioException instanceof UnknownHostException) {
                log.debug("Could not load ontology. Unknown host: " + ioException.getMessage());
            } else {
                log.debug("Could not load ontology: " + ioException.getClass().getSimpleName() + " " + ioException.getMessage());
            }*/
        } catch (UnparsableOntologyException e) {
            // If there was a problem loading an ontology because there are syntax errors in the document (file) that
            // represents the ontology then an UnparsableOntologyException is thrown
            log.debug("Could not parse the ontology: " + e.getMessage());
            // A map of errors can be obtained from the exception
            Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
            // The map describes which parsers were tried and what the errors were
            for (OWLParser parser : exceptions.keySet()) {
                log.debug("Tried to parse the ontology with the " + parser.getClass().getSimpleName() + " parser");
                log.debug("Failed because: " + exceptions.get(parser).getMessage());
            }
            throw new OntologyException(e);
        } catch (UnloadableImportException e) {
            // If our ontology contains imports and one or more of the imports could not be loaded then an
            // UnloadableImportException will be thrown (depending on the missing imports handling policy)
            log.debug("Could not load import: " + e.getImportsDeclaration());
            // The reason for this is specified and an OWLOntologyCreationException
            OWLOntologyCreationException cause = e.getOntologyCreationException();
            log.debug("Reason: " + cause.getMessage());
            throw new OntologyException(e);
        } catch (OWLOntologyCreationException e) {
            log.debug("Could not load ontology: " + e.getMessage());
            throw new OntologyException(e);
        }
        
        return new ByteArrayInputStream(owlOutput.toByteArray());

    }

	
}
