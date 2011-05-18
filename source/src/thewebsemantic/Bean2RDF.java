package thewebsemantic;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;

import static thewebsemantic.JenaHelper.toLiteral;
import static thewebsemantic.PrimitiveWrapper.isPrimitive;
import static thewebsemantic.TypeWrapper.instanceURI;
import static thewebsemantic.TypeWrapper.type;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import thewebsemantic.semantAnnot.AnnotationValues;

/**
 * <p>
 * Converts a simple java bean to RDF, provided it's annotated with
 * <code>Namespace</code>. To make a bean persitable by jenabean, you are    
 * required to add the Namespace annotation. By default public bean properties
 * are converted to rdf properties by appending "has" and proper casing the
 * property name. For example, a bean with methods getName() and setName() would
 * result in the RDF property "hasName", with the namespace given in the classes
 * Namespace annotation.
 * </p>
 * <p>
 * The default behavior for rdf property naming is overridden by using the
 * RdfProperty annotation along with the getter method. The value supplied to
 * the RdfProperty annotation is taken as the full RDF property URI. <br/>
 * </p>
 * <p>
 * The bean itself is typed using the Namespace annotation along with the bean
 * name, for example, Book.class with namespace "http://example.org/" becomes
 * rdf type "http://example.org/Book". <br/>
 * </p>
 * <p>
 * Here's a simple example of a bean that's ready to be saved:
 * <code>
 * <pre>
 * package org.example;
 * import thewebsemantic.Id;
 * public Book {
 *    private String name;
 *    public void setName(String s) { name=s;}
 *    &#064;Id
 *    public String getName() {return name;}
 * }
 * </pre>
 * </code>
 * </p>
 * @see Namespace
 * @see Id
 * @see RdfProperty
 */
public class Bean2RDF extends Base {
	private static final String UNSUPPORTED_TYPE = "UNSUPPORTED_TYPE";
	private ArrayList<Object> cycle;
	private boolean forceDeep = false;
        public static Logger logger = Logger.getLogger("com.thewebsemantic");
        ResourceBundle bundle = ResourceBundle.getBundle("thewebsemantic.messages");
        private AnnotationHelper jpa;

	/**
	 * construct a new instance bound to OntModel <code>m</code>.
	 * 
	 * @param m
	 * Jena OntModel instance
	 */
	public Bean2RDF(Model m) {
		super(m);
		jpa = new NullJPAHelper();
	}

	public Bean2RDF(Model m, AnnotationHelper jpa) {
		super(m);
		this.jpa = jpa;
		
	}	
	/**
	 * Saves <code>bean</code> to jena model.
	 * 
	 * @param bean
	 * @return jena resource representing <tt>bean</tt> within the model
	 */
	public Resource save(Object bean) {
		return write(bean, false);
	}

	/**
	 * Deletes <code>bean</code> from the model.
	 * 
	 * @param bean
	 */
	public void delete(Object bean) {
		Resource i = m.getResource(instanceURI(bean));
		m.removeAll(null, null, i).removeAll(i, null, null);
	}

	/**
	 * Saves the entire object graph starting with <tt>bean</tt>.
	 * 
	 * @param bean
	 * @return
	 */
	public Resource saveDeep(Object bean) {
		return write(bean, true);
	}

	/**
	 * Writes a bean to the triple store.
	 * 
	 * @param bean
	 * @return
	 */
	private synchronized Resource write(Object bean, boolean forceDeep) {
		try {
			m.enterCriticalSection(Lock.WRITE);
			this.forceDeep = forceDeep;
			cycle = new ArrayList<Object>();
			return _write(bean, false);
		} finally {
			m.leaveCriticalSection();
		}
	}

	private Resource _write(Object bean, boolean shallow) {
		return (cycle.contains(bean)) ? existing(bean) : write(bean,
				toResource(bean), shallow);
	}

        /**
         * Converts a Java Bean to Jena Resource
         * @param bean
         * @return
         */
	private Resource toResource(Object bean) {
		String uri = instanceURI(bean);
		Resource type = getRDFSClass(bean);
		if (jpa.isEmbedded(bean) || uri==null)
			return m.createResource(type); 
		else
			return m.createResource(uri, type);
	}

        /**
         * Insert a new bean as Resource to Jena Graph if do not exist,
         * otherwise nothing will be changed
         *
         * @param bean
         * @return
         */
	private Resource existing(Object bean) {
		return m.createResource(instanceURI(bean));
	}

     /**
     * returns an existing Resource or creates a new one adding an important
     * annotation indicating the original java class.
     *
     * @param bean
     *            the bean we are saving or updating to the triple store
     * @return
     */
    private Resource getRDFSClass(Object bean) {


        // Transitive property to class
        if (bean.getClass().isAnnotationPresent(Transitive.class)) {

            OntProperty op = om.createOntProperty(getURI(bean));
            op.convertToTransitiveProperty();
        }

        // Symmetric property to class
        if (bean.getClass().isAnnotationPresent(Symmetric.class)) {

            OntProperty op = om.createOntProperty(getURI(bean));
            op.convertToSymmetricProperty();
        }

        // Inverse property to class
        if (bean.getClass().isAnnotationPresent(Inverse.class)) {

            OntProperty op = om.createOntProperty(getURI(bean));
            Property qc = ResourceFactory.createProperty(AnnotationValues.getBeanInverseOf(bean));
            op.addInverseOf(qc);
        }

        // Version info to class
        if (bean.getClass().isAnnotationPresent(VersionInfo.class)) {

            OntProperty op = om.createOntProperty(getURI(bean));
            op.setVersionInfo(AnnotationValues.getBeanVersionInfo(bean));
        }

        // Comment to class
        if (bean.getClass().isAnnotationPresent(Comment.class)) {
        	
            OntProperty op = om.createOntProperty(getURI(bean));
            op.setComment(AnnotationValues.getBeanComment(bean),null);
        }
        
        // SeeAlso to class
        if (bean.getClass().isAnnotationPresent(SeeAlso.class)) {
        	
            OntProperty op = om.createOntProperty(getURI(bean));
            Resource res = ResourceFactory.createResource(AnnotationValues.getBeanSeeAlso(bean));
            op.setSeeAlso(res);
        }
        
        // Label to class
        if (bean.getClass().isAnnotationPresent(Label.class)) {
        	OntProperty op = om.createOntProperty(getURI(bean));
        	op.setLabel(AnnotationValues.getBeanLabel(bean), null);
        }

       // IsDefinedBy to class
        if (bean.getClass().isAnnotationPresent(IsDefinedBy.class)) {
            OntProperty op = om.createOntProperty(getURI(bean));
            Resource res = ResourceFactory.createResource(AnnotationValues.getBeanIsDefinedBy(bean));
            op.setIsDefinedBy(res);
        }

        // All values from
        if (bean.getClass().isAnnotationPresent(AllValuesFrom.class)) {
                /*
                if (bean.getClass().isAnnotationPresent(OnProperty.class)){
                    OntProperty op = om.createOntProperty(AnnotationValues.getBeanOnProperty(bean));
                    Resource res = ResourceFactory.createResource(AnnotationValues.getBeanAllValuesFrom(bean));
                    if (op != null ){
                        om.createAllValuesFromRestriction(getURI(bean),op,res);
                    }
               }
               */
            if (bean.getClass().isAnnotationPresent(OnProperty.class)){
                    ObjectProperty op = om.createObjectProperty(AnnotationValues.getBeanOnProperty(bean));
                    Resource res = ResourceFactory.createResource(AnnotationValues.getBeanAllValuesFrom(bean));
                    if (op != null ){
                        om.createAllValuesFromRestriction(getURI(bean),op,res);

                    }
               }
        }
        
        // Cardinality
        if (bean.getClass().isAnnotationPresent(Cardinality.class)) {
        	if (bean.getClass().isAnnotationPresent(OnProperty.class)) {
                OntProperty op = om.createOntProperty(AnnotationValues.getBeanOnProperty(bean));
                int cardinalityValue = AnnotationValues.getBeanCardinality(bean);
                if ((op != null) && (cardinalityValue >= 0)){
                    om.createCardinalityRestriction(getURI(bean),op,cardinalityValue);
                }
        	}
        }
        
        // SomeValuesFrom
        if (bean.getClass().isAnnotationPresent(SomeValuesFrom.class)) {
        	if (bean.getClass().isAnnotationPresent(OnProperty.class)) {
                OntProperty op = om.createOntProperty(AnnotationValues.getBeanOnProperty(bean));
                Resource res = ResourceFactory.createResource(AnnotationValues.getBeanSomeValuesFrom(bean));
                if (op != null) {
                	om.createSomeValuesFromRestriction(getURI(bean), op, res);
                }
        	}
        }

        /*
         *
         * (In)Equality Annotations for classes
         *
         */
        // EquivalentClass
        if (bean.getClass().isAnnotationPresent(EquivalentClass.class)){
            //OntProperty op = om.createOntProperty(getURI(bean));
            //OntProperty op2 = om.createOntProperty(AnnotationValues.getBeanEquivalentClass(bean));
            //op.addEquivalentProperty(op2);

            OntClass oc = om.createClass(getURI(bean));
            OntClass oc2 = om.createClass(AnnotationValues.getBeanEquivalentClass(bean));
            oc.addEquivalentClass(oc2);
        }
         // EquivalentProperty
        if (bean.getClass().isAnnotationPresent(EquivalentProperty.class)){
            //OntProperty op = om.createOntProperty(getURI(bean));
            //OntProperty op2 = om.createOntProperty(AnnotationValues.getBeanEquivalentClass(bean));
            //op.addEquivalentProperty(op2);

            ObjectProperty opr = om.createObjectProperty(getURI(bean));
            ObjectProperty opr2 = om.createObjectProperty(AnnotationValues.getBeanEquivalentProperty(bean));
            opr.addEquivalentProperty(opr2);
        }
        
        // SameAs
        if (bean.getClass().isAnnotationPresent(SameAs.class)){
            ObjectProperty opr = om.createObjectProperty(getURI(bean));
            Resource res = ResourceFactory.createResource(AnnotationValues.getBeanSameAs(bean));
            opr.addSameAs(res);
        }
        
        // DifferentFrom
        if (bean.getClass().isAnnotationPresent(DifferentFrom.class)) {
        	OntProperty op = om.createOntProperty(getURI(bean));
        	Resource res = ResourceFactory.createResource(AnnotationValues.getBeanDifferentFrom(bean));
        	op.addDifferentFrom(res);
        }
        
        // AllDifferent
        if (bean.getClass().isAnnotationPresent(AllDifferent.class)) {
        	com.hp.hpl.jena.ontology.AllDifferent allDif = om.createAllDifferent();
        	String[] values = AnnotationValues.getBeanAllDifferent(bean);
        	Resource res;
        	for (String value : values) {
        		res = ResourceFactory.createResource(value);
        		allDif.addDistinctMember(res);
        	}
        }




        return m.getResource(getURI(bean)).
                addProperty(RDF.type, RDFS.Class).
                addProperty(javaclass, bean.getClass().getName());
        }

	private String getURI(Object bean) {

            return (isBound(bean)) ? binder.getUri(bean) : type(bean).typeUri();
	}

	private Resource write(Object bean, Resource subject, boolean shallow) {
		
                cycle.add(bean);
		for (ValuesContext p : TypeWrapper.valueContexts(bean))
			if (!(shallow && p.type().isAssignableFrom(Collection.class)) || forceDeep)
				saveOrUpdate(subject, p);
		return subject;
	}

	private void saveOrUpdate(Resource subject, ValuesContext pc) {
		Object o = pc.invokeGetter();
		Property property = toRdfProperty(pc);
		if ( Saver.supports(pc.type()))
			Saver.of(pc.type()).save(this,subject, property, o);
		else if (o == null)
			subject.removeAll(property);
		else if (pc.isPrimitive())
			subject.removeAll(property).addProperty(property, toLiteral(m, o));
		else if (isNormalObject(o))
			setPropertyValue(subject, property, o);
		else 
			logger.log(Level.WARNING, MessageFormat.format(bundle
					.getString(UNSUPPORTED_TYPE), pc.type(),pc.subject.getClass()));
	}

	private boolean isNormalObject(Object o) {
		return !o.getClass().isArray() && !(o instanceof Collection) && !(o instanceof Map);
	}

	protected RDFNode toRDFNode(Object o) {
		if (isPrimitive(o)) 
			return toLiteral(m, o);
		else if (o instanceof URI || o instanceof thewebsemantic.Resource)
			return m.createResource(o.toString());
		else
			return _write(o, true);
	}
	
	/**
	 * Update or persist a domain object outside String, Date, and the usual
	 * primitive types. We set the write style to shallow=true, causing an end
	 * of recursive traversal of the object graph.
	 * 
	 * @param subject
	 * @param property
	 * @param o
	 */
	private void setPropertyValue(Resource subject, Property property, Object o) {
		Statement s = subject.getProperty(property);
		Resource existing=null;
		if (s!=null ) {
			existing = s.getResource();
			if (existing.isAnon())
				existing.removeProperties();
		}
		subject.removeAll(property).addProperty(property, _write(o, true));
	}
	
	public void n3() {
		m.write(System.out,"N3");
	}
	

    /**
     * Method writes a created Jena model to specified file
     *
     * @param outputFile Target file
     */
    public void outFile(String outputFile) {
        try {
            System.out.println("Creating the file: " + outputFile + ".");
            m.write(new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), "UTF-8")));
            System.out.println("The file " + outputFile + " was created.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
/*
 * Copyright (c) 2007
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */