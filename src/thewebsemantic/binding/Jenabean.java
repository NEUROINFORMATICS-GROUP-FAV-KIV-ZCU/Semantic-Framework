package thewebsemantic.binding;

import java.io.OutputStream;
import java.util.Collection;

import thewebsemantic.Bean2RDF;
import thewebsemantic.Includer;
import thewebsemantic.NotFoundException;
import thewebsemantic.RDF2Bean;
import thewebsemantic.Sparql;
import thewebsemantic.binder.Binder;
import thewebsemantic.binder.BinderImp;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * This class provides read and write operations over Jena Model using JenaBean
 * classes Bean2Rdf and Rdf2Bean to convert data from Java Beans to Rdf triples
 * and conversely.
 * It encapsules all basic operations over Model
 */
public class Jenabean  {
	
	private Model model;
	private Bean2RDF writer;
	private RDF2Bean reader;
	private Binder binder = BinderImp.instance();
	
	private static Jenabean myself = new Jenabean();
	
	public static synchronized Jenabean instance() {
		return myself;
	}

	private Jenabean() {

	}
	
	/**
     * Constructor creates a new instances of classes used to write and
     * read data over the forwarded Jena model
     *
     * @param m Jena model or OntModel
     */
	public void bind(Model m) {
		model = m;
		reader = new RDF2Bean(m);
		writer = new Bean2RDF(m);
	}
	
	public Model model() {
		return model;
	}
	
	/**
	 * Writes a serialization of the model as an ontology document
	 * in a specified language.
	 * @param out The output stream to which the XML will be written.
	 * @param lang Language of the ontology document.
	 */
	public void writeModel(OutputStream out, String lang) {
		model.write(out, lang);
	}
	
	public Bean2RDF writer() {
		return writer;
	}
	
	public RDF2Bean reader() {
		return reader;
	}
	
	public Binding bind(OntClass oc) {
		return new Binding(binder,oc);
	}

	public Binding bind(String ontClassUri) {
		return new Binding(binder,ontClassUri);
	}
	
	public boolean isBound(Class<?> c) {
		return binder.isBound(c);
	}
	
	public boolean isBound(OntClass c) {
		return binder.isBound(c);
	}
	
	public Class<?> getClass(String uri) {
		return binder.getClass(uri);	
	}
	
	public static boolean exists(Class<?> c, String id) {
		return instance().reader().exists(c, id);
	}

	public static <E> E load(Class<E> c, String id) throws NotFoundException{
		return myself.reader().load(c, id);
	}
	
	public static <E> Collection<E> load(Class<E> c) {
		return myself.reader().load(c);
	}
	
	public static <E> Collection<E> loadDeep(Class<E> c) {
		return myself.reader().loadDeep(c);
	}
	
	public static <E> E load(Class<E> c, int id) throws NotFoundException {
		return myself.reader().load(c, id);
	}	
	public static Includer include(String s) {
		return new Includer(s, myself.reader());
	}
	public static <T> Collection<T> query(Class<T> c,String query) {
		return Sparql.exec(myself.model, c, query);
	}

	public void bindAll(String... s) {
		reader.bindAll(s);
	}
}
