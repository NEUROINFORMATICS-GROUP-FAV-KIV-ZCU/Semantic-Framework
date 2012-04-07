import data.pojo.Data;
import data.pojo.EyesDefect;
import data.pojo.Hardware;
import data.pojo.MeasAddParamsValues;
import data.pojo.Measuration;
import data.pojo.Person;
import data.pojo.ResearchGroup;
import data.pojo.Scenario;
import data.pojo.Weather;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Třída Example obsahuje vstupní data, která se používají v demonstračním příkladě.
 *
 * @author Dominik Šmíd
 */
public class Example {

    private String namespaceZcu = "www.kiv.zcu.cz/eeg";
    private ArrayList<Object> vstupniPole = new ArrayList<Object>();
    
    private Set<Measuration> measurations = new HashSet<Measuration>(0);
    private Set<MeasAddParamsValues> measAddParamsValueses = new HashSet<MeasAddParamsValues>(0);
    private Set<Hardware> hardwares = new HashSet<Hardware>(0);
    private Set<Person> persons = new HashSet<Person>(0);
    private Set<Data> datas = new HashSet<Data>(0);

    
    public Example() {

        //Blob b = new SerialBlob(new byte[]{10, 20, 30, 120});

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

//        vstupniPole.add((Object) w1);
//        vstupniPole.add((Object) w2);
//        vstupniPole.add((Object) w3);
//        vstupniPole.add((Object) w4);

        Person p1 = new Person(1, "Smid", 'M');
        p1.setGivenname("Dominik");
        Person p2 = new Person(2, "Bouda", 'M');
        Person p3 = new Person(3, "Kouba", 'M');
        Person p4 = new Person(4, "Novakova", 'Z');
        
        // pridani ocnich defektu osobe p4
        EyesDefect e1 = new EyesDefect(1, "šedý zákal");
        EyesDefect e2 = new EyesDefect(2, "krátkozrakost");
        Set<EyesDefect> se = new HashSet<EyesDefect>();
        se.add(e1);
        se.add(e2);
        p4.setEyesDefects(se);


//        vstupniPole.add((Object) p1);
//        vstupniPole.add((Object) p2);
//        vstupniPole.add((Object) p3);
//        vstupniPole.add((Object) p4);

        ResearchGroup rg1 = new ResearchGroup(1, p1, "Bc", "Students from first, second and third class.");
        ResearchGroup rg2 = new ResearchGroup(2, p2, "Bc", "Students from first, second and third class.");
        ResearchGroup rg3 = new ResearchGroup(3, p3, "EEG", "Students from EEG project.");
        ResearchGroup rg4 = new ResearchGroup(4, p4, "EEG", "Students from EEG project.");

//        vstupniPole.add((Object) rg1);
//        vstupniPole.add((Object) rg2);

        Scenario s1 = new Scenario(1, p1, rg1);
        Scenario s2 = new Scenario(2, p2, rg1);
        Scenario s3 = new Scenario(3, p3, rg2);
        Scenario s4 = new Scenario(4, p4, rg2);

//        vstupniPole.add((Object) s1);
//        vstupniPole.add((Object) s2);
//        vstupniPole.add((Object) s3);
//        vstupniPole.add((Object) s4);

        Measuration m1 = new Measuration(1, w1, s1, p1, rg1, p4, startMereni1, konecMereni1, -1, "weathernote", measAddParamsValueses, hardwares, persons, datas);
        Measuration m2 = new Measuration(2, w2, s2, p2, rg2, p3, startMereni2, konecMereni2, 20, "weathernote", measAddParamsValueses, hardwares, persons, datas);
        Measuration m3 = new Measuration(3, w3, s3, p3, rg3, p2, startMereni3, konecMereni3, -8, "weathernote", measAddParamsValueses, hardwares, persons, datas);
        Measuration m4 = new Measuration(4, w4, s4, p4, rg4, p1, startMereni4, konecMereni4, -3, "weathernote", measAddParamsValueses, hardwares, persons, datas);

        vstupniPole.add((Object) m1);
        vstupniPole.add((Object) m2);
        vstupniPole.add((Object) m3);
        vstupniPole.add((Object) m4);

        //Data d1 = new Data(1, m1, 3.1, b, "Mime type.", "Filename 1");
        //vstupniPole.add((Object) d1);

        Hardware h1 = new Hardware(1, "Title of Hardware 1", "type 1");
        Hardware h2 = new Hardware(2, "Title of Hardware 2", "type 2");

//        vstupniPole.add((Object) h1);
//        vstupniPole.add((Object) h2);

    }

    public String getNamespaceZcu() {
        return namespaceZcu;
    }

    public ArrayList<Object> getVstupniPole() {
        return vstupniPole;
    }
}
