package semantic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import thewebsemantic.UserDefNamespace;
import thewebsemantic.binding.Jenabean;

/**
 * Transforms object-oriented model into the semantic model
 * in a RDF format (RDF = Resource Description Format).
 * 
 * @author Jakub Krauz
 */
public class ModelToRDF implements ModelToSemantic {
	
	/** provides operations over model */
	protected Jenabean jenaBean = Jenabean.instance();
	
	/** logger */
	private Log log = LogFactory.getLog(getClass());
	
	/** semantic model */
	protected Model model;
	
	/** object-oriented model */
	protected List<Object> dataList;
	
	/** namespace value */
	protected String namespace;
	
	/** Jenabean output - RDF ontology model */
	protected InputStream output;
	
	
	/**
	 * If using this nonparametric constructor, the parametric
	 * version of method createSemantic() must be called.
	 */
	public ModelToRDF() {
		
	}
	
	
	/**
	 * Sets the data. Nonparametric version of createSemantic() can
	 * be used afterwards.
	 * @param dataList object-oriented model
	 * @param namespace namespace value
	 * @throws IOException 
	 * @throws UnknownDataException 
	 */
	public ModelToRDF(List<Object> dataList, String namespace) throws IOException {
		this.dataList = dataList;
		this.namespace = namespace;
		toRDF();
	}
	
	
	/**
	 * @return stream containing RDF/XML describing semantic
	 */
	@Override
	public InputStream createSemantic(List<Object> dataList, String namespace)
								throws IOException, OntologyException {
		this.dataList = dataList;
		this.namespace = namespace;
		toRDF();
		return output;
	}
	
	
	/**
	 * @return stream containing RDF/XML describing semantic
	 */
	@Override
	public InputStream createSemantic() throws IOException, OntologyException {
		return output;
	}
	
	
	/**
	 * Creates a model in RDF format.
	 * @return stream containing RDF/XML
	 * @throws UnknownDataException if the data list wasn't set before transformation
	 */
	protected void toRDF() throws IOException {
		log.debug("- JENABEAN Extension -");
		UserDefNamespace.set(namespace);
		model = ModelFactory.createOntologyModel();
		jenaBean.bind(model);
		
		for (int i = 0; i < dataList.size(); i++) {
			jenaBean.writer().save(dataList.get(i));
		}
		log.debug("Ontology model was created.");
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		model.write(out);
		out.flush();
        output = new ByteArrayInputStream(out.toByteArray());
        out.close();
        output.close();
	}

}
