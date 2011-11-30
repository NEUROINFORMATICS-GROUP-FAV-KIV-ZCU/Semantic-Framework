package tools;

/**
 * Constants for defining syntax of an ontology document
 * produced by the Jena library.
 * 
 * @author Jakub Krauz
 */
public class Syntax {
	
	/**
	 * RDF/XML language. This is the default.<br>
	 * This version produces the output efficiently, but it is not
	 * readable. For readable output use <code>RDF_XML_ABBREV</code>.
	 * @see http://www.w3.org/TR/rdf-syntax-grammar/
	 */
	public static final String RDF_XML = "RDF/XML";
	
	/**
	 * RDF/XML language.<br>
	 * This version produces a readable output, but not so
	 * efficiently as <code>RDF_XML</code>.
	 * @see http://www.w3.org/TR/rdf-syntax-grammar/
	 */
	public static final String RDF_XML_ABBREV = "RDF/XML-ABBREV";
	
	/** 
	 * RDF Core's N-Triples language.
	 * @see http://www.w3.org/TR/rdf-testcases/#ntriples
	 */
	public static final String N_TRIPLE = "N-TRIPLE";
	
	/**
	 * Terse RDF Triple language.
	 * @see http://www.w3.org/TeamSubmission/turtle/
	 */
	public static final String TURTLE = "TURTLE";
	
	/**
	 * Tim Berners-Lee's N3 (Notation 3) language.
	 * @see http://www.w3.org/2000/10/swap/Primer.html
	 */
	public static final String N3 = "N3";
	
	/**
	 * Tim Berners-Lee's N3 (Notation 3) language - full version.
	 * @see http://www.w3.org/2000/10/swap/Primer.html
	 */
	public static final String N3_PP = "N3-PP";
	
	/**
	 * Tim Berners-Lee's N3 (Notation 3) language - plain version.<br>
	 * This version of the N3 language does not nest bNode structures
	 * but does write record-like groups of all properties for a subject.
	 * @see http://www.w3.org/2000/10/swap/Primer.html
	 */
	public static final String N3_PLAIN = "N3-PLAIN";
	
	/**
	 * Tim Berners-Lee's N3 (Notation 3) language - triple version.<br>
	 * This version of the N3 language writes one statement per line,
	 * like N-TRIPLES, but also does qname conversion of URIrefs.
	 * @see http://www.w3.org/2000/10/swap/Primer.html
	 */
	public static final String N3_TRIPLE = "N3-TRIPLE";

}
