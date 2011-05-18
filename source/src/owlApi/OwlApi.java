package owlApi;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.net.UnknownHostException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.UnloadableImportException;

public class OwlApi {

    public String inputFile;
    public String outputFile;
    public int typeofOutput;

    public OwlApi(String input, String output, int type) {
        this.inputFile = input;
        this.outputFile = output;
        this.typeofOutput = type;
        convert(type);
    }

    public void convert(int typeofOutput) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            File input = new File(inputFile);
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(input);
            System.out.println("Loaded ontology: " + ontology);
            File output = new File(outputFile);
            IRI documentIRIoutput = IRI.create(output);

            try {

                System.out.println("Creating the file: " + output + ".");
                if (typeofOutput == 0) {
                    manager.saveOntology(ontology, new OWLXMLOntologyFormat(), documentIRIoutput);
                } else if (typeofOutput == 1) {
                    manager.saveOntology(ontology, new RDFXMLOntologyFormat(), documentIRIoutput);
                }
                System.out.println("The file " + output + " was created.");
            } catch (OWLOntologyStorageException ex) {
                Logger.getLogger(OwlApi.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (OWLOntologyCreationIOException e) {
            // IOExceptions during loading get wrapped in an OWLOntologyCreationIOException
            IOException ioException = e.getCause();
            if (ioException instanceof FileNotFoundException) {
                System.out.println("Could not load ontology. File not found: " + ioException.getMessage());
            } else if (ioException instanceof UnknownHostException) {
                System.out.println("Could not load ontology. Unknown host: " + ioException.getMessage());
            } else {
                System.out.println("Could not load ontology: " + ioException.getClass().getSimpleName() + " " + ioException.getMessage());
            }
        } catch (UnparsableOntologyException e) {
            // If there was a problem loading an ontology because there are syntax errors in the document (file) that
            // represents the ontology then an UnparsableOntologyException is thrown
            System.out.println("Could not parse the ontology: " + e.getMessage());
            // A map of errors can be obtained from the exception
            Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
            // The map describes which parsers were tried and what the errors were
            for (OWLParser parser : exceptions.keySet()) {
                System.out.println("Tried to parse the ontology with the " + parser.getClass().getSimpleName() + " parser");
                System.out.println("Failed because: " + exceptions.get(parser).getMessage());
            }
        } catch (UnloadableImportException e) {
            // If our ontology contains imports and one or more of the imports could not be loaded then an
            // UnloadableImportException will be thrown (depending on the missing imports handling policy)
            System.out.println("Could not load import: " + e.getImportsDeclaration());
            // The reason for this is specified and an OWLOntologyCreationException
            OWLOntologyCreationException cause = e.getOntologyCreationException();
            System.out.println("Reason: " + cause.getMessage());
        } catch (OWLOntologyCreationException e) {
            System.out.println("Could not load ontology: " + e.getMessage());
        }

    }
}
