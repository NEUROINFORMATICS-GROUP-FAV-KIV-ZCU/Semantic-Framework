package tools;

import java.util.Arrays;

/**
 * This class is used to set the ontology header.<br>
 * An instance of this class can be used to create ontology axioms.
 * Available properties can be set using appropriate setters.
 * The instance must be passed on to the JenaBeanExtension
 * using the <code>setOntology()</code> method in {@link JenaBeanExtension}
 * so as the ontology header would be included in the ontology document.
 * 
 * @author Jakub Krauz
 */
public class Ontology {
	
	/** URI of the ontology */
	private String uri;
	
	/** ontology version info */
	private String versionInfo;
	
	/** prior version of the ontology that is backward compatible */
	private String backwardCompatible;
	
	/** incompatible prior version */
	private String incompatibleWith;
	
	/** prior version */
	private String priorVersion;
	
	/** some comment to the ontology */
	private String comment;
	
	/** imported ontologies */
	private String[] imports;
	
	/** human-readable label for the ontology */
	private String label;
	
	/** some relevant reference */
	private String seeAlso;

	
	/**
	 * Creates the <code>owl:Ontology</code> element with a defined URI.
	 * @param ontologyUri URI of the ontology
	 */
	public Ontology(String ontologyUri) {
		this.uri = ontologyUri;
	}
	
	
	/**
	 * Returns ontology's URI.
	 * @return URI
	 */
	public String getUri() {
		return uri;
	}
	
	
	/**
	 * Returns URI of the prior version that is backward compatible.
	 * @return URI of the compatible prior version
	 */
	public String getBackwardCompatibleWith() {
		return backwardCompatible;
	}
	
	
	/**
	 * Sets the <code>owl:backwardCompatibleWith</code> element.
	 * @param uri URI of the compatible prior version
	 */
	public void setBackwardCompatibleWith(String uri) {
		this.backwardCompatible = uri;
	}
	
	
	/**
	 * Returns version info.
	 * @return version info
	 */
	public String getVersionInfo() {
		return versionInfo;
	}

	
	/**
	 * Sets the <code>owl:versionInfo</code> element.
	 * @param versionInfo version
	 */
	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	
	/**
	 * Returns URI of the incompatible prior version.
	 * @return URI of the incompatible prior version
	 */
	public String getIncompatibleWith() {
		return incompatibleWith;
	}

	
	/**
	 * Sets the <code>owl:incompatibleWith</code> element.
	 * @param uri URI of the incompatible prior version
	 */
	public void setIncompatibleWith(String uri) {
		this.incompatibleWith = uri;
	}

	
	/**
	 * Returns URI of the prior version.
	 * @return URI of the prior version
	 */
	public String getPriorVersion() {
		return priorVersion;
	}

	
	/**
	 * Sets the <code>owl:priorVersion</code> element.
	 * @param uri URI of the prior version
	 */
	public void setPriorVersion(String uri) {
		this.priorVersion = uri;
	}

	
	/**
	 * Returns comment of this ontology.
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}

	
	/**
	 * Sets the <code>rdfs:comment</code> element.
	 * @param comment comment to this ontology
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	
	/**
	 * Returns URIs of imported ontologies.
	 * @return array of URIs
	 */
	public String[] getImports() {
		return imports;
	}

	
	/**
	 * Sets the <code>owl:imports</code> statement for every
	 * element of the array.
	 * @param imports array of URIs of imported ontologies
	 */
	public void setImports(String[] imports) {
		this.imports = imports;
	}
	
	
	/**
	 * Sets the <code>owl:imports</code> element.
	 * @param uri URI of the imported ontology
	 */
	public void addImport(String uri) {
		if (imports == null)
			imports = new String[1];
		else
			imports = Arrays.copyOf(imports, imports.length + 1);
		imports[imports.length - 1] = uri;
	}

	
	/**
	 * Returns the human-readable label of this ontology.
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	
	/**
	 * Sets the human-readable label of this ontology.
	 * @param label human-readable label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	
	/**
	 * Returns the defined reference to some relevant resource.
	 * @return reference to some resource
	 */
	public String getSeeAlso() {
		return seeAlso;
	}

	
	/**
	 * Sets the <code>rdfs:seeAlso</code> element.
	 * @param seeAlso
	 */
	public void setSeeAlso(String seeAlso) {
		this.seeAlso = seeAlso;
	}

	
}
