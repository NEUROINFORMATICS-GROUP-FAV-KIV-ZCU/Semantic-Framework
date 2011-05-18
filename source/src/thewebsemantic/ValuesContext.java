package thewebsemantic;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import thewebsemantic.Base.NullType;
import thewebsemantic.semantAnnot.AnnotationValues;

/**
 * ValuesContext class represents the base for descendant classes that
 * encapsulates Bean reflection gatehered attributes or methods and it
 * provides all neccesaty methods to gather data in propriate format and
 * mostly all bounded Annoatations in their values.
 *
 */

public abstract class ValuesContext {

	protected Object subject;

	public abstract AccessibleObject  getAccessibleObject();
	/**
         * Returns if Uri annotation is preset over this object
         * @return
         */
	public abstract String uri();
	/**
         * Returns if Id annotation is preset over this object
         * @return
         */
	public abstract boolean isId();

	public abstract boolean isSymmetric();

	public abstract boolean isTransitive();
	
	public abstract boolean isInverse();
	
	public abstract String inverseOf();

        public abstract boolean isDataRange(); //Data range annotation test

        public abstract String dataRange(); // Data range value

        public abstract boolean isVersionInfo(); //Version info annotation test

        public abstract boolean isComment(); // Comment annotation test

        public abstract String comment(); // Comment value

        public abstract String versionInfo(); // Version info value
        
        public abstract boolean isSeeAlso(); // See also annotation test
        
        public abstract String seeAlso(); // See also value
        
        public abstract boolean isLabel(); // Label annotation test
        
        public abstract String label(); // Label value

        public abstract boolean isIsDefinedBy(); // isDefinedBy annotation test

        public abstract String isDefinedBy(); //isDefinedBy value

	public abstract boolean existsInModel(Model m);

	public abstract Object invokeGetter();

	public abstract void setProperty(Object v);

	public abstract boolean isPrimitive();

    public boolean isCollection() {
       return type().equals(Collection.class);
    }
    
    public boolean isCollectionOrSet() {
    	return isCollection() || isSet();
    }

    public boolean isSet() {
        return type().equals(Set.class);
    }
    
    public boolean isCollectionType() {
		return Collection.class.isAssignableFrom(type());
	}

	public boolean isURI() {
	   return type().equals(URI.class);
	}
	
	public abstract String getName();

	public abstract Class<?> type();

	public abstract Class<?> t();
	
	public Class<?> getGenericType(ParameterizedType type) {
		return (type == null) ? NullType.class : (Class<?>) type
				.getActualTypeArguments()[0];		
	}

	public boolean isTransitive(AccessibleObject o) {
            return o.isAnnotationPresent(Transitive.class);
	}

	public boolean isInverse(AccessibleObject o) {
		String inverseProperty = 
			AnnotationValues.getInverseOf(o);
		return !"".equals(inverseProperty);
	}
	/**
         * Retrieves URI name of inverse item from annotation
         * @param o
         * @return
         */
        public String inverseOf(AccessibleObject o) {
		return AnnotationValues.getInverseOf(o);
	}

        public String dataRange(AccessibleObject o) { // get Annotation value
		return AnnotationValues.getDataRange(o);
	}

        public String comment(AccessibleObject o){
            return AnnotationValues.getComment(o);
        }
        
        public String versionInfo(AccessibleObject o) { // get Annotation value
		return AnnotationValues.getVersionInfo(o);
	}
        
        public String seeAlso(AccessibleObject o) { // get Annotation value
        	return AnnotationValues.getSeeAlso(o);
        }

        public String label(AccessibleObject o) { // get label annotation value
            return AnnotationValues.getLabel(o);
        }

        public String isDefinedBy(AccessibleObject o){  // get IsDefinedBy Annotation value
            return AnnotationValues.getIsDefinedBy(o);
        }

	public Property property(Model m) { 
		return m.getProperty(uri());
	}

	public boolean isList() {
		return type().equals(List.class);
	}
	
	public boolean isAggregateType() {
		return isCollectionType() || isArray();
	}

	public boolean isArray() {
		return type().isArray();
	}
	
	public String toString() {return getName();}


}