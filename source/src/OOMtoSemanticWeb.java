
import java.util.ArrayList;
import owlApi.MainOwlApi;
import thewebsemantic.MainJenaBean;

/**
 * Hlavní třída transformační knihovny.
 *
 * @author Dominik Šmíd, Jakub Krauz
 */
public class OOMtoSemanticWeb {
	
	/**
	 * Spusti transformacni proces objektoveho modelu do RDF.
	 * @param seznamObjektu seznam objektu urcenych k transformaci
	 * @param nameSpace jmenny prostor
	 */
	public static void toRDF(ArrayList<Object> seznamObjektu, String nameSpace) {
		new MainJenaBean(seznamObjektu, nameSpace);
	}
	
	/**
	 * Spusti transformacni proces objektoveho modelu do OWL.
	 * Mezivystupem je RDF model (stejny jako se ziska metodou toRDF()).
	 * @param seznamObjektu seznam objektu urcenych k transformaci
	 * @param nameSpace jmenny prostor
	 */
	public static void toOWL(ArrayList<Object> seznamObjektu, String nameSpace) {
		new MainJenaBean(seznamObjektu, nameSpace);
		new MainOwlApi("semanticweb.owl");
	}
	
	
    /**
     * Konstruktor, který nám spustí program.
     * 
     * @deprecated Starsi verze spousteni procesu, neumoznuje volbu mezi RDF nebo OWL
     * 			   vystupem. Novejsi ekvivalentni reseni je pouziti staticke metody
     * 			   toRDF() se stejnymi parametry. Pro OWL vystup je urcena metoda toOWL().
     *
     * @param poleObjektu vstupní data uložená v ArrayListu
     * @param myNamespace String, který nám definuje namespace
     */
	@Deprecated
    public OOMtoSemanticWeb(ArrayList<Object> poleObjektu, String myNamespace) {
        new MainJenaBean(poleObjektu, myNamespace);
    }
    
}
