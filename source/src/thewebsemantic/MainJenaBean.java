package thewebsemantic;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.util.ArrayList;
import thewebsemantic.binding.Jenabean;

/**
 * This class uses Jeana to create an empty ontology Model and than
 * reads all POJO objects which are written to model with all containing
 * neccessary onotologies and semantic informations added by annotations
 * over POJO classes attributes.
 *
 * @author Dominik Šmíd
 */
public class MainJenaBean {

    private final String OUTPUTJENABEAN = "outputJenaBean.rdf";
    
    /**
     * Constructor runs a creates an empty Jena Ontology model, binds it
     * and writes to it all POJO containig data through the binded model
     *
     * @param objectList
     *          input data saved as ArrayList Objects
     * @param myNamespace
     *          A String defining namespace
     */
    public MainJenaBean(ArrayList<Object> objectList, String myNamespace) {
        System.out.println("- JENABEAN -");
        
        testList(objectList);
        
        MyNamespace namespace = new MyNamespace(myNamespace); // static instance
                                                    //of namespace definetion

        Model m = ModelFactory.createOntologyModel();

        Jenabean.instance().bind(m);

        for (int i = 0; i < objectList.size(); i++) {
            Jenabean.instance().writer().save(objectList.get(i));
        }
        
        Jenabean.instance().writer().outFile(OUTPUTJENABEAN);
    }

    /**
     * Method tests if the ArrayList containig model is not empty
     *
     */
    public void testList(ArrayList<Object> objectList) {
        if (objectList.isEmpty() == true) {
            System.out.println("It was no object.");
            System.exit(1);
        }
    }
}
