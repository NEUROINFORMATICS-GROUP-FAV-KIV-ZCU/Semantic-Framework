
import java.util.ArrayList;


/**
 * Třída, která plní funkci "uživatelského programu".
 * Zde se vytvoří objekty určené k transformaci a zavolá se metoda, která proces spustí.
 *
 * @author Jakub Krauz
 */
public class Hlavni {

    public static void main(String[] args) {
    	
    	/* vytvoreni objektu - musi provadet uzivatel knihovny sam */
    	Example ex = new Example();
    	ArrayList<Object> poleObjektu = ex.getVstupniPole();
    	String nameSpace = ex.getNamespaceZcu();
    	
    	/* demonstrace pouziti knihovny uzivatelem */
    	OOMtoSemanticWeb.toOWL(poleObjektu, nameSpace); // transformace do OWL
    }
}
