package thewebsemantic;

import com.hp.hpl.jena.rdf.model.Literal;
/**
 * This class is used to determinate language of gathered Literal, if the
 * language of Literal is defined.
 * 
 */

public class LocalizedString {
	private String lang;
	private String value;
	
	public LocalizedString(String value, String lang) {
		this.lang = lang;
		this.value = value;
	}
	
	public LocalizedString(Literal l) {
		this((String)l.getValue(), l.getLanguage());
	}

   public String getLang() {
   	return lang;
   }

	public String toString() {
		return value;
	}
}
