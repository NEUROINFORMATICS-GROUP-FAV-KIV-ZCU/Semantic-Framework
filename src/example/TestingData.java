package example;


import example.pojo.*;
import example.pojo.Experiment;
import odml.core.Property;
import odml.core.Section;
import odml.core.Value;

import java.sql.Timestamp;
import java.util.*;

/**
 * This class creates testing data.
 */
public class TestingData {

    // list of data objects
    private ArrayList<Object> dataList = new ArrayList<Object>();

    private Set<Experiment> measurations = new HashSet<Experiment>(0);
    private Set<MeasAddParamsValues> measAddParamsValueses = new HashSet<MeasAddParamsValues>(0);
    private Set<Hardware> hardwares = new HashSet<Hardware>(0);
    private Set<Person> persons = new HashSet<Person>(0);
    private Set<Data> datas = new HashSet<Data>(0);


    public TestingData() {

        Timestamp startMereni1 = Timestamp.valueOf("2008-11-20 10:20:00");
        Timestamp startMereni2 = Timestamp.valueOf("2009-04-15 09:35:00");
        Timestamp startMereni3 = Timestamp.valueOf("2010-01-06 13:30:00");
        Timestamp startMereni4 = Timestamp.valueOf("2010-02-19 10:30:00");
        Timestamp konecMereni1 = Timestamp.valueOf("2008-11-20 11:35:00");
        Timestamp konecMereni2 = Timestamp.valueOf("2009-04-15 10:40:00");
        Timestamp konecMereni3 = Timestamp.valueOf("2010-01-06 14:10:00");
        Timestamp konecMereni4 = Timestamp.valueOf("2010-02-19 11:25:00");

        Weather w1 = new Weather(1, "Temperature is about -1 centigrade. It's cloudy.", "The weather from 2008.11.20.", measurations);
        Weather w2 = new Weather(2, "Temperature is about 20 centigrade. It's sunny", "The weather from 2009.04.15", measurations);
        Weather w3 = new Weather(3, "Temperature is about -8 centigrade. It's winter and windy", "The weather from 2010.01.06", measurations);
        Weather w4 = new Weather(4, "Temperature is about -3 centigrade", "The weather from 2010.02.19", measurations);

        Person p1 = new Person(1, "Smith", 'M');
        Person p2 = new Person(2, "Hawkins", 'M');
        Person p3 = new Person(3, "Jones", 'M');
        Person p4 = new Person(4, "Green", 'F');

        // add eyes defects to p4
        EyesDefect e1 = new EyesDefect(1, "cataract");
        EyesDefect e2 = new EyesDefect(2, "short-sightedness");
        Set<EyesDefect> se = new HashSet<EyesDefect>();
        se.add(e1);
        se.add(e2);
        p4.setEyesDefects(se);

        ResearchGroup rg1 = new ResearchGroup(1, p1, "Bc", "Students from first, second and third class.");
        ResearchGroup rg2 = new ResearchGroup(2, p2, "Bc", "Students from first, second and third class.");
        ResearchGroup rg3 = new ResearchGroup(3, p3, "EEG", "Students from EEG project.");
        ResearchGroup rg4 = new ResearchGroup(4, p4, "EEG", "Students from EEG project.");

        Scenario s1 = new Scenario(1, p1, rg1);
        Scenario s2 = new Scenario(2, p2, rg1);
        Scenario s3 = new Scenario(3, p3, rg2);
        Scenario s4 = new Scenario(4, p4, rg2);

        Experiment m1 = new Experiment(1, w1, s1, p1, rg1, p4, startMereni1, konecMereni1, -1, "weathernote", measAddParamsValueses, hardwares, persons, datas);
        Experiment m2 = new Experiment(2, w2, s2, p2, rg2, p3, startMereni2, konecMereni2, 20, "weathernote", measAddParamsValueses, hardwares, persons, datas);
        Experiment m3 = new Experiment(3, w3, s3, p3, rg3, p2, startMereni3, konecMereni3, -8, "weathernote", measAddParamsValueses, hardwares, persons, datas);
        Experiment m4 = new Experiment(4, w4, s4, p4, rg4, p1, startMereni4, konecMereni4, -3, "weathernote", measAddParamsValueses, hardwares, persons, datas);

        Vector<String> testVector = new Vector<String>();
        for(int i = 0; i < 10; i++) {
            testVector.add("TestVector:" + i);
        }
        m1.setTestVector(testVector);



        try {
            Section s = new Section("OdmlSection");
            Section subsection = new Section("OdmlSubsection");
            s.add(subsection);
            Property p = new Property("TestOdmlProperty", "TestOdmlValue", "String");

            subsection.add(p);
           // m1.setMetadata(s);
            dataList.add(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
/*

        dataList.add((Object) m1);
       dataList.add((Object) m2);
        dataList.add((Object) m3);
        dataList.add((Object) m4);
*/



    }

    /**
     * Returns the list of data objects.
     *
     * @return - list of data objects
     */
    public ArrayList<Object> getDataList() {
        return dataList;
    }
}
