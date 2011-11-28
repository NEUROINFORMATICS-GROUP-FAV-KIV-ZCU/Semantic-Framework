
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import tools.JenaBeanExtensionTool;
import tools.OwlApiTool;


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
    	//ExampleHodneDat ex = new ExampleHodneDat(10);
    	Example ex = new Example();
    	List<Object> dataList = ex.getVstupniPole();
    	
    	
    	JenaBeanExtensionTool jenaBean;
    	OwlApiTool owlApi;
		try {
			long time1 = System.currentTimeMillis();
			jenaBean = new JenaBeanExtensionTool(dataList);
			long time2 = System.currentTimeMillis();
			vypisTrvani(time2 - time1, "JenaBean loading");
			is = jenaBean.getOntologyDocument();
			long time3 = System.currentTimeMillis();
			vypisTrvani(time3 - time2, "JenaBean creating output");
			//owlApi = new OwlApiTool(is);
			long time4 = System.currentTimeMillis();
			vypisTrvani(time4 - time3, "OwlApi loading");
			//is = owlApi.convertToSemanticStandard("owl");
			long time5 = System.currentTimeMillis();
			vypisTrvani(time5 - time4, "OwlApi creating output");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    	
    	writeSemanticToFile("document3.owl");

    }
    
    
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
    
    
    private static void vypisTrvani(long millis, String usek) {
		long min = millis / 60000;
		long sec = (millis - min * 60000) / 1000;
		System.out.println(usek + ": " + min + " min, " + sec + " sec");
    }
    
}
