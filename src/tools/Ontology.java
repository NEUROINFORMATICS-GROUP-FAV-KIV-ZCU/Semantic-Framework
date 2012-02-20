package tools;

/**
 * This class is used to set the ontology header.<br>
 * Available properties can be set using appropriate setters.
 * The instance must be passed on to the JenaBeanExtension
 * using the <code>setOntology()</code> method so as the ontology
 * header would be included in the ontology document.
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
	private String imports;  //TODO vice importu?
	
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
	 * Returns the prior version that is backward compatible.
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

	
	public String getIncompatibleWith() {
		return incompatibleWith;
	}

	
	public void setIncompatibleWith(String uri) {
		this.incompatibleWith = uri;
	}


	public String getPriorVersion() {
		return priorVersion;
	}

	
	public void setPriorVersion(String uri) {
		this.priorVersion = uri;
	}


	public String getComment() {
		return comment;
	}

	
	public void setComment(String comment) {
		this.comment = comment;
	}

	
	public String getImports() {
		return imports;
	}

	
	public void setImports(String imports) {
		this.imports = imports;
	}

	
	public String getLabel() {
		return label;
	}

	
	public void setLabel(String label) {
		this.label = label;
	}

	
	public String getSeeAlso() {
		return seeAlso;
	}


	public void setSeeAlso(String seeAlso) {
		this.seeAlso = seeAlso;
	}

	
}
