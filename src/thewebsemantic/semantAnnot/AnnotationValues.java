package thewebsemantic.semantAnnot;

import thewebsemantic.VersionInfo;
import thewebsemantic.Comment;
import thewebsemantic.DataRange;
import thewebsemantic.SeeAlso;
import java.lang.reflect.AccessibleObject;

import thewebsemantic.AllDifferent;
import thewebsemantic.AllValuesFrom;
import thewebsemantic.Cardinality;
import thewebsemantic.DifferentFrom;
import thewebsemantic.EquivalentClass;
import thewebsemantic.EquivalentProperty;
import thewebsemantic.Inverse;
import thewebsemantic.IsDefinedBy;
import thewebsemantic.Label;
import thewebsemantic.OnProperty;
import thewebsemantic.SameAs;
import thewebsemantic.SomeValuesFrom;

/**
 * This class contains methods returning annotation values
 * bounded to their attributes
 *
 * @author Filip Markvart
 */
public class AnnotationValues {

    /**
     * Method returns String representation of inverse value of annotaion
     *
     * @param o target attribut
     * @return String representation of value or null if not present
     */
    public static String getInverseOf(AccessibleObject o) {

        if (o.isAnnotationPresent(Inverse.class)) {

            return o.getAnnotation(Inverse.class).value();
        }
        return null;
    }

    /**
     * Method returns String representation of inverse value of annotaion of bean
     *
     * @param o target bean
     * @return String representation of value or null if not present
     */
    public static String getBeanInverseOf(Object o) {

        if (o.getClass().isAnnotationPresent(Inverse.class)) {

            return o.getClass().getAnnotation(Inverse.class).value();
        }
        return null;
    }

     /**
     * Method returns String representation of Version info value of annotaion of bean
     *
     * @param o target bean
     * @return String representation of value or null if not present
     */
    public static String getBeanVersionInfo(Object o) {

        if (o.getClass().isAnnotationPresent(VersionInfo.class)) {

            return o.getClass().getAnnotation(VersionInfo.class).value();
        }
        return null;
    }

    /**
     * Method returns a String representation of Data range restriction
     * value of target annotation
     *
     * @param o Target attribute
     * @return String representation of data range value or null if not present
     */
     public static String getDataRange(AccessibleObject o) {

        if (o.isAnnotationPresent(DataRange.class)) {

            return o.getAnnotation(DataRange.class).value();
        }
        return null;
    }

     /**
     * Method returns a String representation of Version info
     * value of target annotation
     *
     * @param o Target attribute
     * @return String representation of Version info value or null if not present
     */
     public static String getVersionInfo(AccessibleObject o) {

        if (o.isAnnotationPresent(VersionInfo.class)) {

            return o.getAnnotation(VersionInfo.class).value();
        }
        return null;
    }


     /**
     * Method returns a String representation of Comment
     * value of target annotation
     *
     * @param o Target attribute
     * @return String representation of Comment value or null if not present
     */
     public static String getComment(AccessibleObject o) {

         if (o.isAnnotationPresent(Comment.class)) {
            
             return o.getAnnotation(Comment.class).value();
         }
         return null;
     }

     /**
     * Method returns String representation of Comment value of annotaion of bean
     *
     * @param o target bean
     * @return String representation of value or null if not present
     */
     public static String getBeanComment(Object o) {

        if (o.getClass().isAnnotationPresent(Comment.class)) {

            return o.getClass().getAnnotation(Comment.class).value();
        }
        return null;
     }
     
     /**
      * Method returns String representation of Comment value of annotaion of bean
      *
      * @param o target bean
      * @return String representation of value or null if not present
      */
      public static String getBeanSeeAlso(Object o) {

         if (o.getClass().isAnnotationPresent(SeeAlso.class)) {
             return o.getClass().getAnnotation(SeeAlso.class).value();
         }
         return null;
      }

    /**
      * Method returns a String representation of See also
      * value of target annotation
      *
      * @param o Target attribute
      * @return String representation of See also value or null if not present
      */
     public static String getSeeAlso(AccessibleObject o) {
    	 
    	 if (o.isAnnotationPresent(SeeAlso.class)) {
             return o.getAnnotation(SeeAlso.class).value();
         }
         return null;
     }
     
     /**
      * Returns a String representation of Label value of target
      * annotation
      * @param o Target object
      * @return String representation of Label value or null if not present
      */
     public static String getLabel(AccessibleObject o) {
    	 if (o.isAnnotationPresent(Label.class)) {
    		 return o.getAnnotation(Label.class).value();
    	 }
    	 return null;
     }


          /**
     * Method returns a String representation of IsDefinedBy
     * value of target annotation
     *
     * @param o Target attribute
     * @return String representation of IsDefinedBy value or null if not present
     */
     public static String getIsDefinedBy(AccessibleObject o) {

         if (o.isAnnotationPresent(IsDefinedBy.class)) {

             return o.getAnnotation(IsDefinedBy.class).value();
         }
         return null;
     }



     
     /**
      * Method returns String representation of Label value of annotaion of bean
      *
      * @param o target bean
      * @return String representation of value or null if not present
      */ 
 	public static String getBeanLabel(Object o) {
 		if (o.getClass().isAnnotationPresent(Label.class)) {
            return o.getClass().getAnnotation(Label.class).value();
        }
        return null;
	}

    /**
      * Method returns String representation of AllValuesFrom value of annotation of bean
      *
      * @param o target bean
      * @return String representation of value or null if not present
      */

    public static String getBeanAllValuesFrom(Object o) {
        if (o.getClass().isAnnotationPresent(AllValuesFrom.class)) {
            return o.getClass().getAnnotation(AllValuesFrom.class).value();
        }
        return null;
    }

    /**
      * Method returns String representation of OnProperty value of annotation of bean
      *
      * @param o target bean
      * @return String representation of value or null if not present
      */
    public static String getBeanOnProperty(Object o) {
        if (o.getClass().isAnnotationPresent(OnProperty.class)) {
            return o.getClass().getAnnotation(OnProperty.class).value();
        }
        return null;
    }
    
    /**
     * Method returns Integer representation of Cardinality value of annotation of bean
     * 
     * @param o target bean
     * @return Integer representation of value or -1 if not present
     */
    public static int getBeanCardinality(Object o) {
        if (o.getClass().isAnnotationPresent(Cardinality.class)) {
            return o.getClass().getAnnotation(Cardinality.class).value();
        }
        return -1;
    }
    
    /**
     * Method returns String representation of SomeValuesFrom value of annotation of bean
     *
     * @param o target bean
     * @return String representation of value or null if not present
     */
    public static String getBeanSomeValuesFrom(Object o) {
        if (o.getClass().isAnnotationPresent(SomeValuesFrom.class)) {
            return o.getClass().getAnnotation(SomeValuesFrom.class).value();
        }
        return null;
    }
    
    /**
     * Method returns String representation of IsDefinedBy value of annotation of bean
     *
     * @param o target bean
     * @return String representation of value or null if not present
     */
    public static String getBeanIsDefinedBy(Object o) {
       if (o.getClass().isAnnotationPresent(IsDefinedBy.class)) {
            return o.getClass().getAnnotation(IsDefinedBy.class).value();
        }
        return null;
    }

    /**
     * Method returns String representation of EquivalentClass value of annotation of bean
     *
     * @param o target bean
     * @return String representation of value or null if not present
     */
    public static String getBeanEquivalentClass(Object o) {
        if (o.getClass().isAnnotationPresent(EquivalentClass.class)) {
            return o.getClass().getAnnotation(EquivalentClass.class).value();
        }
        return null;
    }
    
    /**
     * Method returns String representation of EquivalentProperty value of annotation of bean
     *
     * @param o target bean
     * @return String representation of value or null if not present
     */
    public static String getBeanEquivalentProperty(Object o) {
        if (o.getClass().isAnnotationPresent(EquivalentProperty.class)) {
            return o.getClass().getAnnotation(EquivalentProperty.class).value();
        }
        return null;
    }
    
    /**
     * Method returns String representation of SameClass value of annotation of bean
     *
     * @param o target bean
     * @return String representation of value or null if not present
     */
    public static String getBeanSameAs(Object o) {
       if (o.getClass().isAnnotationPresent(SameAs.class)) {
            return o.getClass().getAnnotation(SameAs.class).value();
        }
        return null; 
    }
    
    /**
     * Returns String representation of DifferentFrom annotation value of the bean.
     * @param o target bean
     * @return String value of annotation (or null if not present)
     */
    public static String getBeanDifferentFrom(Object o) {
    	if (o.getClass().isAnnotationPresent(DifferentFrom.class)) {
    		return o.getClass().getAnnotation(DifferentFrom.class).value();
    	}
    	return null;
    }
    
    /**
     * Returns field of Strings representing AllDifferent values.
     * @param o target bean
     * @return String values of annotation (or null if not present)
     */
    public static String[] getBeanAllDifferent(Object o) {
    	if (o.getClass().isAnnotationPresent(AllDifferent.class)) {
    		return o.getClass().getAnnotation(AllDifferent.class).value();
    	}
    	return null;
    }


}
