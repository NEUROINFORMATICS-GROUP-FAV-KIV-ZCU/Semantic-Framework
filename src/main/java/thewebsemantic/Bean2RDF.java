package thewebsemantic;

import static thewebsemantic.PrimitiveWrapper.isPrimitive;
import static thewebsemantic.TypeWrapper.instanceURI;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.proxy.HibernateProxy;

import thewebsemantic.annotations.AllDifferent;
import thewebsemantic.annotations.Comment;
import thewebsemantic.annotations.ComplementOf;
import thewebsemantic.annotations.DifferentFrom;
import thewebsemantic.annotations.DisjointWith;
import thewebsemantic.annotations.EquivalentClass;
import thewebsemantic.annotations.Id;
import thewebsemantic.annotations.IsDefinedBy;
import thewebsemantic.annotations.Label;
import thewebsemantic.annotations.Namespace;
import thewebsemantic.annotations.RdfProperty;
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
	 * @return resource to which the bean is mapped
	 */
	public Resource saveDeep(Object bean) {
		return write(bean, true);
	}

	
	/**
	 * Writes a bean to the triple store.
	 * 
	 * @param bean saved bean
	 * @return resource referencing saved bean
	 */
	private synchronized Resource write(Object bean, boolean forceDeep) {
		try {
			logger.debug("Processing bean: "  + bean);
			m.enterCriticalSection(Lock.WRITE);
			logger.debug("In critical section, with bean: " + bean);
			this.forceDeep = forceDeep;
			cycle = new ArrayList<Object>();
			return _write(bean, false);
		} finally {
			m.leaveCriticalSection();
			logger.debug("leaved critical section, returned bean: " + bean);
		}
	}

	
	/**
	 * Writes bean to the model.
	 * 
	 * @param bean - saved bean
	 * @param shallow
	 * @return resource referencing saved bean
	 */
	private Resource _write(Object bean, boolean shallow) {		
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
     * @throws MyException 
     */
    private Resource getOWLClass(Object bean) {
    	OntClass owlClass = om.createClass(getURI(bean));    	
    	addSuperClasses(bean.getClass(), owlClass);
        applyAnnotations(bean.getClass(), owlClass);
        
        String className = (bean instanceof HibernateProxy) ? 
				bean.getClass().getSuperclass().getName() : bean.getClass().getName();
        return m.getResource(getURI(bean)).addProperty(javaclass, className);
    }
    
    
    /**
     * Adds definitions of superclasses.
     * 
     * @param cls - Java class (which is mapped)
     * @param owlClass - resource in which the Java class is mapped
     * @return OWL class
     */
    private Resource addSuperClasses(Class<?> cls, OntClass owlClass) {
    	
    	if (HibernateProxy.class.isAssignableFrom(cls))
    		return addSuperClasses(cls.getSuperclass(), owlClass);
    	
    	Class<?> superClass = cls.getSuperclass();
    	Class<?>[] interfaces = cls.getInterfaces();
    	try {
    		if (superClass.getPackage().equals(cls.getPackage())) {
    			OntClass owlSuperClass = (OntClass) getOWLClass(superClass);
    			applyAnnotations(superClass, owlSuperClass);
    			owlClass.addSuperClass(owlSuperClass);
    			addSuperClasses(superClass, owlSuperClass);
    		}
    		for (Class<?> interfaceCls : interfaces) {
    			if (interfaceCls.getPackage().equals(cls.getPackage())) {
    				OntClass owlSuperClass = (OntClass) getOWLClass(interfaceCls);
    				applyAnnotations(interfaceCls, owlSuperClass);
    				owlClass.addSuperClass(owlSuperClass);
    			}
    		}
    	} catch (Exception e) {
    		// nothing, just continue
    	}
    	
    	return owlClass;
    }
    
    
    /**
     * Adds class axioms determined by present annotations.
     * 
     * @param cls - Java class (which contains annotations)
     * @param owlClass - OWL class in which the Java class is mapped
     * @return OWL class
     */
    private Resource applyAnnotations(Class<?> cls, OntClass owlClass) {
    	
    	// Version info
        if (cls.isAnnotationPresent(VersionInfo.class)) {
            owlClass.setVersionInfo(cls.getAnnotation(VersionInfo.class).value());
        }

        // Comment
        if (cls.isAnnotationPresent(Comment.class)) {
        	String language = cls.getAnnotation(Comment.class).lang();
            owlClass.setComment(cls.getAnnotation(Comment.class).value(),
            		language.equals("") ? null : language);
        }
        
        // SeeAlso
        if (cls.isAnnotationPresent(SeeAlso.class)) {
        	String seeAlso = cls.getAnnotation(SeeAlso.class).value();
            Resource res = ResourceFactory.createResource(seeAlso);
            owlClass.setSeeAlso(res);
        }
        
        // Label
        if (cls.isAnnotationPresent(Label.class)) {
        	String language = cls.getAnnotation(Label.class).lang();
        	owlClass.setLabel(cls.getAnnotation(Label.class).value(),
        			language.equals("") ? null : language);
        }

        // IsDefinedBy
        if (cls.isAnnotationPresent(IsDefinedBy.class)) {
        	String value = cls.getAnnotation(IsDefinedBy.class).value();
            Resource res = ResourceFactory.createResource(value);
            owlClass.setIsDefinedBy(res);
        }
        
        // EquivalentClass
        if (cls.isAnnotationPresent(EquivalentClass.class)) {
            OntClass eqClass = om.createClass(cls.getAnnotation(EquivalentClass.class).value());
            owlClass.setEquivalentClass(eqClass);
        }
        
        // DisjointWith
        if (cls.isAnnotationPresent(DisjointWith.class)) {
        	OntClass disClass = om.createClass(cls.getAnnotation(DisjointWith.class).value());
        	owlClass.setDisjointWith(disClass);
        }
        
        // ComplementOf
        if (cls.isAnnotationPresent(ComplementOf.class)) {
        	OntClass comClass = om.createClass(cls.getAnnotation(ComplementOf.class).value());
        	owlClass.convertToComplementClass(comClass);
        }
        
        // Deprecated
        if (cls.isAnnotationPresent(Deprecated.class)) {
        	owlClass.addProperty(RDF.type, OWL.DeprecatedClass);
        }
        
        // SameAs
        if (cls.isAnnotationPresent(SameAs.class)) {
            String same = cls.getAnnotation(SameAs.class).value();
            Resource res = ResourceFactory.createResource(same);
            owlClass.setSameAs(res);
        }
        
        // DifferentFrom
        if (cls.isAnnotationPresent(DifferentFrom.class)) {
        	String different = cls.getAnnotation(DifferentFrom.class).value();
        	Resource res = ResourceFactory.createResource(different);
        	owlClass.setDifferentFrom(res);
        }
        
        // AllDifferent
        if (cls.isAnnotationPresent(AllDifferent.class)) {
        	com.hp.hpl.jena.ontology.AllDifferent allDif = om.createAllDifferent();
        	allDif.addDistinctMember(owlClass);  // add annotated class
        	String[] values = cls.getAnnotation(AllDifferent.class).value();
        	Resource res;
        	for (String value : values) {
        		res = ResourceFactory.createResource(value);
        		allDif.addDistinctMember(res);
        	}
        }
        
    	return owlClass;
    }

	
    
    /**
     * Writes bean's attributes to the model.
     * 
     * @param bean saved bean
     * @param subject resource to which the bean is mapped
     * @param shallow
     * @return resource referencing saved bean
     */
	private Resource write(Object bean, Resource subject, boolean shallow) {		
		cycle.add(bean);
		for (ValuesContext p : TypeWrapper.valueContexts(bean)) {
			logger.debug("ValuesContext: " + p);
			if (!(shallow && (p.type().isAssignableFrom(Collection.class)) && p.type().isArray() ) || forceDeep)
				saveOrUpdate(subject, p);
		}	
		return subject;
	}

	
	/**
	 * Saves values of attributes to the model.
	 * 
	 * @param subject - resource which owns the property and value
	 * @param pc - saved attribute
	 */
	private void saveOrUpdate(Resource subject, ValuesContext pc) {
		Object o = pc.invokeGetter();
		logger.debug("ValuesContext: " + pc +  ", getter: " + o);
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
			Saver.of(o.getClass()).save(this, subject, property, o);

			//logger.warn(MessageFormat.format("Skipped unsupported property type {0} on {1}", pc.type(), pc.subject.getClass()));

		// removing data if we need only their structure
		if (structureOnly) {
			subject.removeProperties();
		}
	}

	
	/**
	 * Determines whether <code>o</code> is normal object (which means it is neither array,
	 * nor collection, nor map).
	 * @param o tested object
	 * @return true if the object is normal object
	 */
	private boolean isNormalObject(Object o) {
		return !o.getClass().isArray() && !(o instanceof Collection) && !(o instanceof Map) && !(o instanceof Vector) && !(o instanceof ArrayList && !(o instanceof Object));
	}

	
	/**
	 * Returns RDFNode representing object <code>o</code>.
	 * If the object is a bean, it is saved to the model.
	 * 
	 * @param o object
	 * @return RDFNode representing the object
	 */
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
		Resource existing = null;
		if (s != null) {
			existing = s.getResource();
			if (existing.isAnon())
				existing.removeProperties();
		}
		subject.removeAll(property).addProperty(property, _write(o, true));
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