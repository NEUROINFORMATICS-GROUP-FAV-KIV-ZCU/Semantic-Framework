package thewebsemantic;

import static thewebsemantic.JenaHelper.toLiteral;
import static thewebsemantic.PrimitiveWrapper.isPrimitive;
import static thewebsemantic.TypeWrapper.instanceURI;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import thewebsemantic.annotations.AllDifferent;
import thewebsemantic.annotations.Comment;
import thewebsemantic.annotations.DifferentFrom;
import thewebsemantic.annotations.EquivalentClass;
import thewebsemantic.annotations.Id;
import thewebsemantic.annotations.IsDefinedBy;
import thewebsemantic.annotations.Label;
import thewebsemantic.annotations.SameAs;
import thewebsemantic.annotations.SeeAlso;
import thewebsemantic.annotations.VersionInfo;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

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
@SuppressWarnings("deprecation")
public class Bean2RDF extends Base {
	
	private ArrayList<Object> cycle;
	private boolean forceDeep = false;
	private Log logger = LogFactory.getLog(getClass());
    private AnnotationHelper jpa;
    
    private boolean structureOnly = false;

    
	/**
	 * Constructs a new instance bound to OntModel <code>m</code>.
	 * 
	 * @param m Jena OntModel instance
	 */
	public Bean2RDF(Model m) {
		super(m);
		jpa = new NullJPAHelper();
	}
	
	
	/**
	 * Constructs a new instance bound to OntModel <code>m</code>.
	 * If the <code>structureOnly</code> parameter is true, then
	 * the ontology model contains only the static structure of
	 * data, otherwise contains also the data.
	 * 
	 * @param m Jena OntModel instance
	 * @param structureOnly if true the model does not load data
	 */
	public Bean2RDF(Model m, boolean structureOnly) {
		super(m);
		this.structureOnly = structureOnly;
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
		
		// retrieve original class if proxy received
		/*if (bean instanceof HibernateProxy) {
		    // since Portal uses Hibernate only proxies are received, but this causes exceptions unfortunatelly
			bean = ((HibernateProxy) bean).getHibernateLazyInitializer().getImplementation();
		}*/
		
		return (cycle.contains(bean)) ? existing(bean) : write(bean, toResource(bean), shallow);
	}
	

	/**
	 * Converts a Java Bean to Jena Resource
	 * @param bean
	 * @return
	 */
	private Resource toResource(Object bean) {
		String uri = instanceURI(bean);
		Resource type = getOWLClass(bean);
		if (jpa.isEmbedded(bean) || uri == null)
			return m.createResource(type);
		else
			return m.createResource(uri, type);
	}

        
	/**
     * Inserts a new bean as Resource to Jena Graph if it does not exist,
     * otherwise nothing will be changed.
     *
     * @param bean - bean to save
     * @return
     */
	private Resource existing(Object bean) {
		return m.createResource(instanceURI(bean));
	}

	
    /**
     * Returns an existing OntClass or creates a new one.
     * All axioms indicated by present annotations are added as well.
     *
     * @param bean - the bean we are saving or updating to the triple store
     * @return resource referencing saved bean
     */
    private Resource getOWLClass(Object bean) {
    	
    	OntClass owlClass = om.createClass(getURI(bean));
    	
        // Version info
        if (bean.getClass().isAnnotationPresent(VersionInfo.class)) {
            owlClass.setVersionInfo(bean.getClass().getAnnotation(VersionInfo.class).value());
        }

        // Comment
        if (bean.getClass().isAnnotationPresent(Comment.class)) {
        	String language = bean.getClass().getAnnotation(Comment.class).lang();
            owlClass.setComment(bean.getClass().getAnnotation(Comment.class).value(),
            		language.equals("") ? null : language);
        }
        
        // SeeAlso
        if (bean.getClass().isAnnotationPresent(SeeAlso.class)) {
        	String seeAlso = bean.getClass().getAnnotation(SeeAlso.class).value();
            Resource res = ResourceFactory.createResource(seeAlso);
            owlClass.setSeeAlso(res);
        }
        
        // Label
        if (bean.getClass().isAnnotationPresent(Label.class)) {
        	String language = bean.getClass().getAnnotation(Label.class).lang();
        	owlClass.setLabel(bean.getClass().getAnnotation(Label.class).value(),
        			language.equals("") ? null : language);
        }

        // IsDefinedBy
        if (bean.getClass().isAnnotationPresent(IsDefinedBy.class)) {
        	String value = bean.getClass().getAnnotation(IsDefinedBy.class).value();
            Resource res = ResourceFactory.createResource(value);
            owlClass.setIsDefinedBy(res);
        }
        
        // EquivalentClass
        if (bean.getClass().isAnnotationPresent(EquivalentClass.class)) {
            OntClass eqClass = om.createClass(bean.getClass().getAnnotation(EquivalentClass.class).value());
            owlClass.setEquivalentClass(eqClass);
        }
        
        // Deprecated
        if (bean.getClass().isAnnotationPresent(Deprecated.class)) {
        	owlClass.addProperty(RDF.type, OWL.DeprecatedClass);
        }
        
        // SameAs
        if (bean.getClass().isAnnotationPresent(SameAs.class)) {
            String same = bean.getClass().getAnnotation(SameAs.class).value();
            Resource res = ResourceFactory.createResource(same);
            owlClass.setSameAs(res);
        }
        
        // DifferentFrom
        if (bean.getClass().isAnnotationPresent(DifferentFrom.class)) {
        	String different = bean.getClass().getAnnotation(DifferentFrom.class).value();
        	Resource res = ResourceFactory.createResource(different);
        	owlClass.setDifferentFrom(res);
        }
        
        // AllDifferent
        if (bean.getClass().isAnnotationPresent(AllDifferent.class)) {
        	com.hp.hpl.jena.ontology.AllDifferent allDif = om.createAllDifferent();
        	Resource res = om.createResource(getURI(bean));
        	allDif.addDistinctMember(res);  // pridani anotovane tridy
        	String[] values = bean.getClass().getAnnotation(AllDifferent.class).value();
        	for (String value : values) {
        		res = ResourceFactory.createResource(value);
        		allDif.addDistinctMember(res);
        	}
        }
        
        
        // TODO this is a makeshift solution of the problem with proxies
		// check if the name was retrieved from javassist proxy instead of original class
        String className = bean.getClass().getName();
		if (className.contains("_$$_javassist"))
			className = className.substring(0, className.indexOf("_$$_javassist"));
		
        return m.getResource(getURI(bean)).addProperty(javaclass, className);
    }

	
	private Resource write(Object bean, Resource subject, boolean shallow) {		
		cycle.add(bean);
		for (ValuesContext p : TypeWrapper.valueContexts(bean)) {
			
			// TODO remove
			//tohle je jen pomocny vypis
			//System.out.println(p.subject.getClass().getName() + ":  " + p.type() + " (" + p.getName() + ")");
			//logger.error(p.subject.getClass().getName() + ":  " + p.type() + " (" + p.getName() + ")");
			
			if (!(shallow && p.type().isAssignableFrom(Collection.class)) || forceDeep)
				saveOrUpdate(subject, p);
		}
		return subject;
	}

	
	/**
	 * Saves values of attributes do the model.
	 * 
	 * @param subject - resource which owns the property and value
	 * @param pc - saved attribute
	 */
	private void saveOrUpdate(Resource subject, ValuesContext pc) {
		Object o = pc.invokeGetter();		
		Property property = toRdfProperty(pc);  // map the attribute to rdf:Property instance
		
		if ( Saver.supports(pc.type()) )
			Saver.of(pc.type()).save(this, subject, property, o);
		else if (o == null)
			subject.removeAll(property);
		else if (pc.isPrimitive())
			subject.removeAll(property).addProperty(property, PrimitiveWrapper.toLiteral(o));
		else if (isNormalObject(o))
			setPropertyValue(subject, property, o);
		else
			logger.warn(MessageFormat.format("Skipped unsupported property type {0} on {1}", pc.type(), pc.subject.getClass()));

		// removing data if we need only their structure
		if (structureOnly) {
			subject.removeProperties();
		}
	}

	
	private boolean isNormalObject(Object o) {
		return !o.getClass().isArray() && !(o instanceof Collection) && !(o instanceof Map);
	}

	
	protected RDFNode toRDFNode(Object o) {		
		if (isPrimitive(o)) 
			return PrimitiveWrapper.toLiteral(o);
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
	
	
	/*public void n3() {
		m.write(System.out,"N3");
	}*/
	
    
    
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