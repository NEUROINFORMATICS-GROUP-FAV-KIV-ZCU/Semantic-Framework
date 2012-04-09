
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.hp.hpl.jena.ontology.OntModelSpec;

import tools.JenaBeanExtensionTool;
import tools.Ontology;
import tools.OwlApiTool;
import tools.Syntax;


/**
 * Třída, která plní funkci "uživatelského programu".
 * Zde se vytvoří objekty určené k transformaci a zavolá se metoda, která proces spustí.
 *
 * @author Jakub Krauz
 */
public class Hlavni {
	
	private static InputStream is;

    public static void main(String[] args) {    	
    	
    	/* vytvoreni objektu - musi provadet uzivatel knihovny sam */
    	Example ex = new Example();
    	List<Object> dataList = ex.getVstupniPole();
    	
    	JenaBeanExtensionTool jenaBean;
    	OwlApiTool owlApi;
		try {
			jenaBean = new JenaBeanExtensionTool();
			jenaBean.loadStatements(new FileInputStream("ontology.owl"), Syntax.RDF_XML_ABBREV);
			jenaBean.loadOOM(dataList, false);
			jenaBean.declareAllClassesDisjoint();
			/*is = jenaBean.getOntologyDocument(Syntax.TURTLE);
			is = checkChars(is);
			jenaBean = new JenaBeanExtensionTool();
			jenaBean.loadStatements(is, Syntax.TURTLE);*/
			is = jenaBean.getOntologyDocument(Syntax.RDF_XML_ABBREV);
			//is = jenaBean.getOntologySchema(Syntax.RDF_XML_ABBREV);
			
//			owlApi = new OwlApiTool(is);
//			is = owlApi.convertToSemanticStandard("rdf");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	writeSemanticToFile("ontDocument.owl");

    }
    
    
    @SuppressWarnings("unused")
	private static void writeSemanticToFile(String fileName) {
    	System.out.println("- USER -");
    	System.out.println("Creating file " + fileName + "...");
    	try {
			FileOutputStream out = new FileOutputStream(new File(fileName));
			int pom;
			while ((pom = is.read()) != -1) {
				out.write(pom);
			}
			out.close();
			System.out.println("File " + fileName + " was created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    @SuppressWarnings("unused")
	private static void vypisTrvani(long millis, String usek) {
		long min = millis / 60000;
		long sec = (millis - min * 60000) / 1000;
		System.out.println(usek + ": " + min + " min, " + sec + " sec");
    }
    
    
    private static InputStream checkChars(InputStream is) {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	int pom;
    	try {
			while ((pom = is.read()) != -1) {
				if (pom == 0x0)
					continue;
				else
					out.write(pom);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return new ByteArrayInputStream(out.toByteArray());
    }
    
}
