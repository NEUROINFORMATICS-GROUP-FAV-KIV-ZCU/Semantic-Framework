package owlApi;

/**
 * Třída, která slouží k ovládání nástroje OwlApi.
 *
 * @author Dominik šmíd
 */
public class MainOwlApi {

    /* konstanta určující typ výstupu (0 - owl; 1 - rdf) */
    private int typeOfOutput;
    private final String INPUT = "outputJenaBean.rdf";
    private String[] poleStringu = new String[2];

    public MainOwlApi(String outputFile) {
        System.out.println("- OWL API -");
        //test(outputFile);
        zjistiVystup(outputFile);
        new OwlApi(INPUT, outputFile, typeOfOutput);
    }

    public int zjistiVystup(String outputFile) {
        String owl = "owl";
        String rdf = "rdf";
        poleStringu = outputFile.split("[.]");

        if (poleStringu[1].equals(owl) == true) {
            typeOfOutput = 0;
        } else if (poleStringu[1].equals(rdf) == true) {
            typeOfOutput = 1;
        } else {
            System.out.println("Bad format of output file.");
            System.exit(1);
        }

        return typeOfOutput;
    }
}
