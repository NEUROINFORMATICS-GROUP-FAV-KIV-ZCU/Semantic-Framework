package thewebsemantic;

import static thewebsemantic.TypeWrapper.type;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;

import org.hibernate.proxy.HibernateProxy;

import thewebsemantic.annotations.AllDifferent;
import thewebsemantic.annotations.AllValuesFrom;
import thewebsemantic.annotations.Cardinality;
import thewebsemantic.annotations.Comment;
import thewebsemantic.annotations.DataRange;
import thewebsemantic.annotations.DifferentFrom;
import thewebsemantic.annotations.EquivalentProperty;
import thewebsemantic.annotations.FunctionalProperty;
import thewebsemantic.annotations.HasValue;
import thewebsemantic.annotations.InverseFunctionalProperty;
import thewebsemantic.annotations.InverseOf;
import thewebsemantic.annotations.IsDefinedBy;
import thewebsemantic.annotations.Label;
import thewebsemantic.annotations.MaxCardinality;
import thewebsemantic.annotations.MinCardinality;
import thewebsemantic.annotations.SameAs;
import thewebsemantic.annotations.SeeAlso;
import thewebsemantic.annotations.SomeValuesFrom;
import thewebsemantic.annotations.SymmetricProperty;
import thewebsemantic.annotations.TransitiveProperty;
import thewebsemantic.annotations.VersionInfo;
import thewebsemantic.binder.Binder;
import thewebsemantic.binder.BinderImp;

import org.apache.jena.ontology.AllValuesFromRestriction;
import org.apache.jena.ontology.CardinalityRestriction;
import org.apache.jena.ontology.HasValueRestriction;
import org.apache.jena.ontology.MaxCardinalityRestriction;
import org.apache.jena.ontology.MinCardinalityRestriction;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.SomeValuesFromRestriction;
import org.apache.jena.ontology.impl.OntModelImpl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.Lock;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import thewebsemantic.semantAnnot.ResourceCreator;

/**
 * Provides functionality common to both {@link RDF2Bean} and {@link Bean2RDF}
 * Contains the instance of Jena Model, OntModel and Binder instance.
 * 
 */
@SuppressWarnings("deprecation")
public class Base {

	public static final String JAVACLASS = "http://thewebsemantic.com#javaclass";
	public static final String SEQUENCE = "http://thewebsemantic.com#sequence";
	protected OntModel om;
	protected Model m;
	protected Binder binder;
	protected Property javaclass;
	protected Property sequence;


	protected Base(Model m) {
		this.m = m;
		binder = BinderImp.instance();
		
		// OntModel is necesary for extended annotation processing
		om = (m instanceof OntModel) ? (OntModel) m : new OntModelImpl(OntModelSpec.OWL_DL_MEM, m);
		
		m.enterCriticalSection(Lock.WRITE);
		javaclass = m.createProperty(JAVACLASS);
		javaclass.addProperty(RDF.type, OWL.AnnotationProperty);
		javaclass.addProperty(RDFS.label, "Java class");
		javaclass.addProperty(RDFS.comment, "This property determines the Java class that was mapped into declaring resource.");
		sequence = m.createProperty(SEQUENCE);
		m.setNsPrefix("semantic", "http://thewebsemantic.com#");
		m.leaveCriticalSection();
	}

	
	/**
	 * Returns the RDF model.
	 * @return RDF model
	 */
	public Model getModel() {
		return m;
	}


	/**
	 * Method writes into the model a param ctx object gathered from bean.
	 * 
	 * @param ctx
	 * @return property to which the attribute is mapped
	 */
	protected Property toRdfProperty(ValuesContext ctx) {
		return ctx.existsInModel(m) ? ctx.property(m) : applyEntailments(ctx);
	}


	/**
	 * Checks attributes's or method's annotations and if they are present
	 * annotation rules are applied.
	 * 
	 * @param ctx loaded attribut or method
	 * @return newly created OntProperty
	 */
	private Property applyEntailments(ValuesContext ctx) {
		
		OntProperty property = createProperty(ctx);  // map the attribute to rdf:Property
		
		// Annotation processing follows
		
		// SymmetricProperty
		if (ctx.isAnnotatedBy(SymmetricProperty.class) ||
				TypeWrapper.getRDFAnnotation(ctx.getAccessibleObject()).symmetric()) {
			if (property instanceof ObjectProperty)
				property.convertToSymmetricProperty();
		}
		
		// TransitiveProperty
		if (ctx.isAnnotatedBy(TransitiveProperty.class)) {
			if (property instanceof ObjectProperty)
				property.convertToTransitiveProperty();
		}
		
		// FunctionalProperty
		if (ctx.isAnnotatedBy(FunctionalProperty.class)) {
			property.convertToFunctionalProperty();
		}
		
		// InverseFunctionalProperty
		if (ctx.isAnnotatedBy(InverseFunctionalProperty.class)) {
			if (property instanceof ObjectProperty)
				property.convertToInverseFunctionalProperty();
		}
		
		// InverseOf
		if (ctx.isAnnotatedBy(InverseOf.class)) {
			Property qc = ResourceFactory.createProperty(ctx.getAnnotation(InverseOf.class).value());
			property.addInverseOf(qc);
		}
		
		// Comment
		if (ctx.isAnnotatedBy(Comment.class)) {
			String language = ctx.getAnnotation(Comment.class).lang();
			property.setComment(ctx.getAnnotation(Comment.class).value(), language.equals("") ? null : language);
		}

		// VersionInfo
		if (ctx.isAnnotatedBy(VersionInfo.class)) {
			property.setVersionInfo(ctx.getAnnotation(VersionInfo.class).value());
		}
		
		// DataRange
		if (ctx.isAnnotatedBy(DataRange.class)) {
			org.apache.jena.rdf.model.Resource res = ResourceCreator.crDataRangeRest(ctx.getAnnotation(DataRange.class).value());
			if (res != null)
				property.addRange(res);
		}
		
		// SeeAlso
		if (ctx.isAnnotatedBy(SeeAlso.class)) {
			org.apache.jena.rdf.model.Resource res = ResourceFactory.createResource(ctx.getAnnotation(SeeAlso.class).value());
			if (res != null)
				property.addSeeAlso(res);
		}

		// Label
		if (ctx.isAnnotatedBy(Label.class)) {
			String language = ctx.getAnnotation(Label.class).lang();
			property.setLabel(ctx.getAnnotation(Label.class).value(), language.equals("") ? null : language);
		}
		
		// EquivalentProperty
		if (ctx.isAnnotatedBy(EquivalentProperty.class)) {
			String uri = ctx.getAnnotation(EquivalentProperty.class).value();
			Property eqProp = ResourceFactory.createProperty(uri);	
			property.addEquivalentProperty(eqProp);
		}
		
		// IsDefinedBy
		if (ctx.isAnnotatedBy(IsDefinedBy.class)) {
			org.apache.jena.rdf.model.Resource  res = ResourceFactory.createResource(ctx.getAnnotation(IsDefinedBy.class).value());
			if (res != null)
				property.setIsDefinedBy(res);
		}
		
		// AllValuesFrom
		if (ctx.isAnnotatedBy(AllValuesFrom.class)) {
			org.apache.jena.rdf.model.Resource range = createAllValuesFromResource(ctx.getAnnotation(AllValuesFrom.class));
			if (range != null) {
				AllValuesFromRestriction restriction = om.createAllValuesFromRestriction(null, property, range);
				restriction.setSubClass(om.getOntClass(getURI(ctx.subject)));
			}
		}
		
		// SomeValuesFrom
		if (ctx.isAnnotatedBy(SomeValuesFrom.class)) {
			org.apache.jena.rdf.model.Resource range = createSomeValuesFromResource(ctx.getAnnotation(SomeValuesFrom.class));
			if (range != null) {
				SomeValuesFromRestriction restriction = om.createSomeValuesFromRestriction(null, property, range);
				restriction.setSubClass(om.getOntClass(getURI(ctx.subject)));
			}
		}
		
		// HasValue
		if (ctx.isAnnotatedBy(HasValue.class)) {
			RDFNode value = createHasValueNode(ctx.getAnnotation(HasValue.class));
			if (value != null) {
				HasValueRestriction restriction = om.createHasValueRestriction(null, property, value);
				restriction.setSubClass(om.getOntClass(getURI(ctx.subject)));
			}
		}

		// Cardinality
		if (ctx.isAnnotatedBy(Cardinality.class)) {
			int cardinality = ctx.getAnnotation(Cardinality.class).value();
			CardinalityRestriction restriction = om.createCardinalityRestriction(null, property, cardinality);
			restriction.setSubClass(om.getOntClass(getURI(ctx.subject)));
		}
		
		// MaxCardinality
		if (ctx.isAnnotatedBy(MaxCardinality.class)) {
			int cardinality = ctx.getAnnotation(MaxCardinality.class).value();
			MaxCardinalityRestriction restriction = om.createMaxCardinalityRestriction(null, property, cardinality);
			restriction.setSubClass(om.getOntClass(getURI(ctx.subject)));
		}
		
		// MinCardinality
		if (ctx.isAnnotatedBy(MinCardinality.class)) {
			int cardinality = ctx.getAnnotation(MinCardinality.class).value();
			MinCardinalityRestriction restriction = om.createMinCardinalityRestriction(null, property, cardinality);
			restriction.setSubClass(om.getOntClass(getURI(ctx.subject)));
		}
		
		// Deprecated
		if (ctx.isAnnotatedBy(Deprecated.class)) {
			property.addProperty(RDF.type, OWL.DeprecatedProperty);
		}
		
		// AllDifferent
		if (ctx.isAnnotatedBy(AllDifferent.class)) {
			org.apache.jena.ontology.AllDifferent allDif = om.createAllDifferent();
        	allDif.addDistinctMember(property);  // add the annotated property
        	for (String value : ctx.getAnnotation(AllDifferent.class).value())
        		allDif.addDistinctMember(ResourceFactory.createResource(value));
		}
		
		// DifferentFrom
		if (ctx.isAnnotatedBy(DifferentFrom.class)) {
			org.apache.jena.rdf.model.Resource  res = ResourceFactory.createResource(ctx.getAnnotation(DifferentFrom.class).value());
			property.addDifferentFrom(res);
		}
		
		// SameAs
		if (ctx.isAnnotatedBy(SameAs.class)) {
			org.apache.jena.rdf.model.Resource res = ResourceFactory.createResource(ctx.getAnnotation(SameAs.class).value());
			property.addSameAs(res);
		}

		return property;
	}
	
	
	/**
	 * Creates new property representing given attribute.
	 * Also sets rdfs:domain and rdfs:range of the property created.
	 * 
	 * @param ctx - atribute to be mapped to property
	 * @return property representing given attribute
	 */
	private OntProperty createProperty(ValuesContext ctx) {
		OntProperty property;
		
		if (ctx.isPrimitive()) {
			property = om.createDatatypeProperty(ctx.uri());
			// setting rdfs:range of the DatatypeProperty
			org.apache.jena.rdf.model.Resource range;
			if ((range = PrimitiveWrapper.getPrimitiveResource(ctx.type())) != null
					&& !ctx.isAnnotatedBy(DataRange.class))
				property.setRange(range);
		} else {
			property = om.createObjectProperty(ctx.uri());
			// setting rdfs:range of the ObjectProperty
			Class<?> rangeClass = ctx.isCollectionType() ? ctx.t() : ctx.type();
			if (rangeClass != null && rangeClass.getPackage() != null) {
				// only classes from the same package - provisional solution
				if (rangeClass.getPackage().equals(ctx.subject.getClass().getPackage())) {
					property.setRange(getOWLClass(rangeClass));
				}
			}
		}
		
		// setting rdfs:domain of the property
		OntClass domain = om.getOntClass(getURI(ctx.subject));
		property.addDomain(domain);
		
		return property;
	}
	
	
	/**
	 * Creates the resource for owl:allValuesFrom element.
	 * 
	 * @param annotation AllValuesFrom annotation
	 * @return resource for owl:allValuesFrom
	 */
	private org.apache.jena.rdf.model.Resource  createAllValuesFromResource(AllValuesFrom annotation) {
		org.apache.jena.rdf.model.Resource range;
		String uriref = annotation.uri();
		if (!uriref.equals("")) {
			range = ResourceFactory.createResource(uriref);
		} else {
			RDFList list = om.createList();
			String[] stringValues;
			int[] intValues;
			char[] charValues;
			if ((stringValues = annotation.stringValues()).length > 0) {
				for (String value : stringValues)
					list = list.with(om.createTypedLiteral(value));
			} else if ((intValues = annotation.intValues()).length > 0) {
				for (int value : intValues)
					list = list.with(om.createTypedLiteral(value));
			} else if ((charValues = annotation.charValues()).length > 0) {
				for (char value : charValues)
					list = list.with(om.createTypedLiteral(value));
			} else {
				return null;
			}
			range = om.createDataRange(list);
		}
		return range;
	}
	
	
	/**
	 * Creates the resource for owl:someValuesFrom element.
	 * 
	 * @param annotation SomeValuesFrom annotation
	 * @return resource for owl:someValuesFrom
	 */
	private org.apache.jena.rdf.model.Resource createSomeValuesFromResource(SomeValuesFrom annotation) {
		org.apache.jena.rdf.model.Resource  range;
		String uriref = annotation.uri();
		if (!uriref.equals("")) {
			range = ResourceFactory.createResource(uriref);
		} else {
			RDFList list = om.createList();
			String[] stringValues;
			int[] intValues;
			char[] charValues;
			if ((stringValues = annotation.stringValues()).length > 0) {
				for (String value : stringValues)
					list = list.with(om.createTypedLiteral(value));
			} else if ((intValues = annotation.intValues()).length > 0) {
				for (int value : intValues)
					list = list.with(om.createTypedLiteral(value));
			} else if ((charValues = annotation.charValues()).length > 0) {
				for (char value : charValues)
					list = list.with(om.createTypedLiteral(value));
			} else {
				return null;
			}
			range = om.createDataRange(list);
		}
		return range;
	}
	
	
	/**
	 * Creates the resource for owl:hasValue element.
	 * @param annotation HasValue annotation
	 * @return resource for owl:hasValue
	 */
	private RDFNode createHasValueNode(HasValue annotation) {
		RDFNode value;
		if (! annotation.uri().equals(""))
			value = ResourceFactory.createResource(annotation.uri());
		else if (! annotation.stringValue().equals(""))
			value = om.createTypedLiteral(annotation.stringValue());
		else if (annotation.charValue() != HasValue.DEFAULT_CHAR)
			value = om.createTypedLiteral(annotation.charValue());
		else if (annotation.intValue() != HasValue.DEFAULT_INT)
			value = om.createTypedLiteral(annotation.intValue());
		else
			value = null;
		return value;
	}
	
	
	/**
	 * Returns an existing resource representing given class or creates a new one.
	 * 
	 * @param cls - referenced class
	 * @return resource representing given class
	 */
	protected org.apache.jena.rdf.model.Resource getOWLClass(Class<?> cls) {
		OntClass resource = om.createClass(TypeWrapper.typeUri(cls));
		String className = HibernateProxy.class.isAssignableFrom(cls) ? 
								cls.getSuperclass().getName() : cls.getName();
		return resource.addProperty(javaclass, className);
	}


	/**
	 * Method retrives propertyDescriptor's getter methods Type params if any
	 * exists
	 * 
	 * @param propDesc
	 * @return return type of the getter method
	 */
	protected Class<?> t(PropertyDescriptor propDesc) {
		ParameterizedType type = (ParameterizedType) propDesc.getReadMethod()
				.getGenericReturnType();
		return (type == null) ? NullType.class : (Class<?>) type.getActualTypeArguments()[0];
	}
	
	
	/**
	 * Returns URI of a resource that the bean is mapped to.
	 * @param bean - mapped bean
	 * @return URI of the resource
	 */
	protected String getURI(Object bean) {
        return (isBound(bean)) ? binder.getUri(bean) : type(bean).typeUri();
	}

	
	/**
	 * Determines whether the object was already bound.
	 * @param o - mapped object
	 * @return true if the object was already bound, otherwise false
	 */
	protected boolean isBound(Object o) {
		return binder.isBound(o.getClass());
	}
	
	
	class NullType {
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
