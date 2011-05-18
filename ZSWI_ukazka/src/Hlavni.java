import java.util.*;


public class Hlavni {
	
	
	
	public static void main(String[] args){
		ArrayList<Object> osoby = new ArrayList<Object>();
		Person franta = new Person(1,"Vokurka",'M');
		Person zdenek = new Person(2,"Slepièka",'M');
		
		osoby.add((Object)franta);
		osoby.add((Object)zdenek);
		OOMtoSemanticWeb.toOWL(osoby, "www.kiv.zcu.cz/eeg");
	}
	
}
