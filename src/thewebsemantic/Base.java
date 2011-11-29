package thewebsemantic;

import static thewebsemantic.TypeWrapper.type;

import com.hp.hpl.jena.rdf.model.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;

import thewebsemantic.annotations.AllValuesFrom;
import thewebsemantic.annotations.Comment;
import thewebsemantic.annotations.DataRange;
import thewebsemantic.annotations.EquivalentProperty;
import thewebsemantic.annotations.Inverse;
import thewebsemantic.annotations.IsDefinedBy;
import thewebsemantic.annotations.Label;
import thewebsemantic.annotations.SeeAlso;
import thewebsemantic.annotations.SomeValuesFrom;
import thewebsemantic.annotations.Symmetric;
import thewebsemantic.annotations.Transitive;
import thewebsemantic.annotations.VersionInfo;
import thewebsemantic.binder.Binder;
import thewebsemantic.binder.BinderImp;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import thewebsemantic.semantAnnot.ResourceCreator;

/**
 * Provides functionality common to both {@link RDF2Bean} and {@link Bean2RDF}
 * Contains the instance of Jena Model, OntModel and Binder instance.
 * 
 */
public class Base {

	public static final String JAVACLASS = "http://thewebsemantic.com/javaclass";
	public static final String SEQUENCE = "http://thewebsemantic.com/sequence";
	// ResourceBundle bundle =
	// ResourceBundle.getBundle("thewebsemantic.messages");
	protected OntModel om;
	protected Model m;
	protected Binder binder;
	protected Property javaclass;
	protected Property sequence;


	protected Base(Model m) {
		this.m = m;
		binder = BinderImp.instance();

		if (m instanceof OntModel) {
			om = (OntModel) m;
		} else {
			// OntModel is necesary for added annotation processing
			om = new OntModelImpl(OntModelSpec.getDefaultSpec(ProfileRegistry.OWL_LANG), m);
		}
		m.enterCriticalSection(Lock.WRITE);
		javaclass = m.createProperty(JAVACLASS);
		sequence = m.createProperty(SEQUENCE);
		javaclass.addProperty(RDF.type, OWL.AnnotationProperty);
		m.leaveCriticalSection();
	}


	public Model getModel() {
		return m;
	}


	/**
	 * Method writes into the model a param ctx object gathered from bean.
	 * 
	 * @param ctx
	 * @return
	 */
	protected Property toRdfProperty(ValuesContext ctx) {
		return ctx.existsInModel(m) ? ctx.property(m) : applyEntailments(ctx);
	}


	/**
	 * Checks attributes's or method's annotations and if they are present
	 * annotation rules are applied.
	 * 
	 * @param ctx Loaded attribut or method
	 * @return new created OntProperty
	 */
	private Property applyEntailments(ValuesContext ctx) {
		
		OntProperty op = om.createOntProperty(ctx.uri());

		// Structural change enables having symmetric, transitive and
		// inverse property simultaneously

		if (ctx.isAnnotatedBy(Symmetric.class) ||
				TypeWrapper.getRDFAnnotation(ctx.getAccessibleObject()).symmetric()) {
			op.convertToSymmetricProperty();
		}

		if (ctx.isAnnotatedBy(Transitive.class)) {
			op.convertToTransitiveProperty();
		}

		if (ctx.isAnnotatedBy(Inverse.class)) {
			Property qc = ResourceFactory.createProperty(ctx.getAnnotation(Inverse.class).value());
			op.addInverseOf(qc);
		}

		if (ctx.isAnnotatedBy(Comment.class)) {
			op.setComment(ctx.getAnnotation(Comment.class).value(), null);
		}

		if (ctx.isAnnotatedBy(VersionInfo.class)) {
			op.setVersionInfo(ctx.getAnnotation(VersionInfo.class).value());
		}

		if (ctx.isAnnotatedBy(DataRange.class)) {
			Resource res = ResourceCreator.crDataRangeRest(ctx.getAnnotation(DataRange.class).value());
			if (res != null) {
				op.addRange(res);
			}
		}

		if (ctx.isAnnotatedBy(SeeAlso.class)) {
			Resource res = ResourceFactory.createResource(ctx.getAnnotation(SeeAlso.class).value());
			if (res != null)
				op.addSeeAlso(res);
		}

		if (ctx.isAnnotatedBy(Label.class)) {
			op.setLabel(ctx.getAnnotation(Label.class).value(), null);
		}
		
		if (ctx.isAnnotatedBy(EquivalentProperty.class)) {
			String uri = ctx.getAnnotation(EquivalentProperty.class).value();
			Property eqProp = ResourceFactory.createProperty(uri);	
			op.addEquivalentProperty(eqProp);
		}
		
		if (ctx.isAnnotatedBy(AllValuesFrom.class)) {
			Resource range = ResourceFactory.createResource(ctx.getAnnotation(AllValuesFrom.class).value());
			// property patri tride "subject" - pouze v ni plati dana restrikce
			om.createAllValuesFromRestriction(getURI(ctx.subject), op, range);
		}
		
		if (ctx.isAnnotatedBy(SomeValuesFrom.class)) {
			Resource range = ResourceFactory.createResource(ctx.getAnnotation(SomeValuesFrom.class).value());
			// property patri tride "subject" - pouze v ni plati dana restrikce
			om.createSomeValuesFromRestriction(getURI(ctx.subject), op, range);
		}

		if (ctx.isAnnotatedBy(IsDefinedBy.class)) {
			Resource res = ResourceFactory.createResource(ctx.getAnnotation(IsDefinedBy.class).value());
			if (res != null)
				op.setIsDefinedBy(res);
		}

		return op;
	}


	/**
	 * Method retrives propertyDescriptor's getter methods Type params if any
	 * exists
	 * 
	 * @param propDesc
	 * @return
	 */
	protected Class<?> t(PropertyDescriptor propDesc) {
		ParameterizedType type = (ParameterizedType) propDesc.getReadMethod()
				.getGenericReturnType();
		return (type == null) ? NullType.class : (Class<?>) type.getActualTypeArguments()[0];
	}
	
	
	protected String getURI(Object bean) {
        return (isBound(bean)) ? binder.getUri(bean) : type(bean).typeUri();
	}


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