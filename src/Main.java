
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import tools.JenaBeanExtension;
import tools.JenaBeanExtensionTool;
import tools.OwlApi;
import tools.OwlApiTool;
import tools.Syntax;


/**
 * This class demonstrates use of the JenaBeanExtension API.
 * Testing data are created and transformed to OWL ontology.
 *
 * @author Jakub Krauz
 */
public class Main {

    public static void main(String[] args) {
    	
    	/* create testing data */
    	Example ex = new Example();
    	List<Object> dataList = ex.getData();
    	
    	JenaBeanExtension jbe;  // transformation tool
    	OwlApi owlApi;			// OWL API tool
    	
    	OutputStream out;
    	    	
		try {
			jbe = new JenaBeanExtensionTool();
			
			/* load the ontology header from a file */
			jbe.loadStatements(new FileInputStream("ontologyHeader.owl"), Syntax.RDF_XML_ABBREV);
			/* load and transform the OOM */
			jbe.loadOOM(dataList, false);
			/* declare all classes disjoint - OWL 2 construct */
			jbe.declareAllClassesDisjoint();
			
			/* get the ontology document in RDF/XML */
			out = new FileOutputStream("ontologyDocument.owl");
			jbe.writeOntologyDocument(out, Syntax.RDF_XML_ABBREV);
			
			/* get the ontology schema in RDF/XML */
			out = new FileOutputStream("ontologySchema.owl");
			jbe.writeOntologySchema(out, Syntax.RDF_XML_ABBREV);
			
			/* convert the ontology to OWL/XML using OWL API */
			InputStream in = jbe.getOntologyDocument(Syntax.RDF_XML_ABBREV);
			owlApi = new OwlApiTool(in);
			out = new FileOutputStream("ontologyDocumentOwlApi.owl");
			owlApi.writeOntologyDocument(out, Syntax.OWL_XML);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Transformation finished.");
    }
    
    
}