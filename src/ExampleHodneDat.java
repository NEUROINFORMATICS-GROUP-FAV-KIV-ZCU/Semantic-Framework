import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import data.pojo.Data;
import data.pojo.Hardware;
import data.pojo.MeasAddParamsValues;
import data.pojo.Measuration;
import data.pojo.Person;
import data.pojo.ResearchGroup;
import data.pojo.Scenario;
import data.pojo.Weather;


public class ExampleHodneDat {

    private ArrayList<Object> vstupniPole = new ArrayList<Object>();
    
    private Set<Measuration> measurations = new HashSet<Measuration>(0);
    private Set<MeasAddParamsValues> measAddParamsValues = new HashSet<MeasAddParamsValues>(0);
    private Set<Hardware> hardwares = new HashSet<Hardware>(0);
    private Set<Person> persons = new HashSet<Person>(0);
    private Set<Data> datas = new HashSet<Data>(0);

    
    public ExampleHodneDat(int cyklusCount) {
    	
    	for (int i = 1; i <= cyklusCount; i++) {
    		Timestamp start = Timestamp.valueOf("2011-09-22 12:23:0" + (i % 10));
    		Timestamp konec = Timestamp.valueOf("2011-09-22 15:26:0" + (i % 10));
    		Weather weather1 = new Weather(i * 2 - 1, "popis" + i, "nazev" + i, measurations);
    		Weather weather2 = new Weather(i * 2, "jinejPopis" + i, "jinejNazev" + i, measurations);
    		Person person1 = new Person(i * 2 - 1, "jmeno" + i, 'M');
    		Person person2 = new Person(i * 2, "jmeno" + i, 'Z');
    		ResearchGroup group = new ResearchGroup(i, person1, "titul" + i, "popis" + i);
    		Scenario scenario = new Scenario(i, person1, group);
    		Measuration measuration1 = new Measuration(i, weather1, scenario, person1, group, person2,
    				start, konec, i % 30, "weathernote" + i, measAddParamsValues, hardwares, persons, datas);
    		Measuration measuration2 = new Measuration(i, weather2, scenario, person2, group, person1,
    				start, konec, i % 30, "weathernote" + i, measAddParamsValues, hardwares, persons, datas);
    		Hardware hardware1 = new Hardware(i, "title" + i, "type" + i);
    		Hardware hardware2 = new Hardware(i, "title" + i, "type" + i);
    		
    		vstupniPole.add(weather1);
    		vstupniPole.add(weather2);
    		vstupniPole.add(person1);
    		vstupniPole.add(person2);
    		vstupniPole.add(group);
    		vstupniPole.add(scenario);
    		vstupniPole.add(measuration1);
    		vstupniPole.add(measuration2);
    		vstupniPole.add(hardware1);
    		vstupniPole.add(hardware2);
    		
    	}

    	//Blob b = new SerialBlob(new byte[]{10, 20, 30, 120});
    	//Data d = new Data(1, measuration1, 3.1, b, "Mime type.", "Filename 1");
        
        //System.out.println("Hodne dat inicializovano.");
    }


    public ArrayList<Object> getVstupniPole() {
        return vstupniPole;
    }
}
