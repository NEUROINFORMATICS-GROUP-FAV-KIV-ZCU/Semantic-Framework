
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import semantic.ModelToOWL;
import semantic.ModelToSemantic;
import semantic.UnknownDataException;


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
    	String namespace = ex.getNamespaceZcu();
    	
    	
    	/* demonstrace pouziti knihovny */
    	ModelToSemantic transform;
    	//transform = new ModelToRDF();
    	transform = new ModelToOWL();
    	
    	try {
    		is = transform.createSemantic(dataList, namespace);
    	} catch(UnknownDataException e) {
    		System.out.println(e.getMessage());
    		System.exit(1);
    	}
    	
    	
    	writeSemanticToFile("baf.owl");

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
    
}
