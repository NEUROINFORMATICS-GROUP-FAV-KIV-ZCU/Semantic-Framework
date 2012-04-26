package tools;

import java.lang.reflect.Field;

/**
 * Constants for defining syntax of the ontology serialization.
 * 
 * @author Jakub Krauz
 */
public class Syntax {
	
	/**
	 * RDF/XML syntax. This is the default one.<br>
	 * This version produces the output from Jena efficiently, but it is not
	 * so readable. For a readable output use <code>RDF_XML_ABBREV</code>.
	 * @see <a href="http://www.w3.org/TR/rdf-syntax-grammar/">RDF/XML syntax specification</a>
	 */
	public static final String RDF_XML = "RDF/XML";
	
	/**
	 * RDF/XML syntax.<br>
	 * This version produces a readable output, but not so
	 * efficiently as <code>RDF_XML</code>.
	 * @see <a href="http://www.w3.org/TR/rdf-syntax-grammar/">RDF/XML syntax specification</a>
	 */
	public static final String RDF_XML_ABBREV = "RDF/XML-ABBREV";
	
	/**
	 * OWL/XML syntax.<br>
	 * This syntax is not supported by Jena, only by the OWL API.
	 * @see <a href="http://www.w3.org/TR/2009/REC-owl2-xml-serialization-20091027">OWL/XML Serialization</a>
	 */
	public static final String OWL_XML = "OWL/XML";
	
	/**
	 * OWL Functional-Style syntax.<br>
	 * This syntax is not supported by Jena, only by the OWL API.
	 * @see <a href="http://www.w3.org/TR/2009/REC-owl2-syntax-20091027">Functional-Style Syntax</a>
	 */
	public static final String OWL_FUNCTIONAL = "OWL_FUNCTIONAL";
	
	/** 
	 * RDF Core's Triple Notation.
	 * @see <a href="http://www.w3.org/TR/rdf-testcases/#ntriples">N-Triples</a>
	 */
	public static final String N_TRIPLE = "N-TRIPLE";
	
	/**
	 * Terse RDF Triple language.
	 * @see <a href="http://www.w3.org/TeamSubmission/turtle/">Turtle</a>
	 */
	public static final String TURTLE = "TURTLE";
	
	/**
	 * Tim Berners-Lee's N3 (Notation 3) language.
	 * @see <a href="http://www.w3.org/2000/10/swap/Primer.html">N3</a>
	 */
	public static final String N3 = "N3";
	
	/**
	 * Tim Berners-Lee's N3 (Notation 3) language - full version.
	 * @see <a href="http://www.w3.org/2000/10/swap/Primer.html">N3</a>
	 */
	public static final String N3_PP = "N3-PP";
	
	/**
	 * Tim Berners-Lee's N3 (Notation 3) language - plain version.<br>
	 * This version of the N3 language does not nest bNode structures
	 * but does write record-like groups of all properties for a subject.
	 * @see <a href="http://www.w3.org/2000/10/swap/Primer.html">N3</a>
	 */
	public static final String N3_PLAIN = "N3-PLAIN";
	
	/**
	 * Tim Berners-Lee's N3 (Notation 3) language - triple version.<br>
	 * This version of the N3 language writes one statement per line,
	 * like N-TRIPLES, but also does qname conversion of URIrefs.
	 * @see <a href="http://www.w3.org/2000/10/swap/Primer.html">N3</a>
	 */
	public static final String N3_TRIPLE = "N3-TRIPLE";
	
	
	/**
	 * Checks if the given string equals one of the predefined syntax
	 * names.
	 * @param syntaxName - checked syntax name
	 * @return true if the string is a valid syntax name, else false
	 */
	public static boolean isValidSyntaxName(String syntaxName) {
		Field[] fields = Syntax.class.getDeclaredFields();
		for (Field field : fields)
			try {
				if (field.get(null).equals(syntaxName))
					return true;
			} catch (Exception e) {
			}
		return false;
	}
	

}
