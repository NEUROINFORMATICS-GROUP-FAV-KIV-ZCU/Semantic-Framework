
import java.util.ArrayList;
import owlApi.MainOwlApi;
import thewebsemantic.MainJenaBean;

/**
 * Hlavní třída celého programu
 *
 * @author Dominik Šmíd
 */
public class OOMtoSemanticWeb {

    /**
     * Konstruktor, který nám spustí demonstrační příklad. Vstupní data jsou ve třídě Example.
     *
     */
    public OOMtoSemanticWeb() {
        Example ex = new Example();
        new MainJenaBean(ex.getVstupniPole(), ex.getNamespaceZcu()); // export Jena modelu
        new MainOwlApi(ex.getVystup()); // transformace z Jena modelu do OWL
    }

    /**
     * Konstruktor, který nám spustí program.
     *
     * @param poleObjektu
     *          vstupní data uložená v ArrayListu
     * @param myNamespace
     *          String, který nám definuje namespace
     */
    public OOMtoSemanticWeb(ArrayList<Object> poleObjektu, String myNamespace) {
        new MainJenaBean(poleObjektu, myNamespace);
    }
}
