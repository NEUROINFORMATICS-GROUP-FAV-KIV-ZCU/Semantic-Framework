package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation implements the built-in OWL class
 * <code>owl:AllDifferent</code>. This element indicates that a group of
 * individuals are mutually different. It has the same meaning as
 * <code>owl:differentFrom</code>, but that property describes only two
 * individuals. This is a more convenient way to describe a group of mutually
 * different individuals.
 * </p>
 * <p>
 * <code>Owl:AllDifferent</code> is in fact a special class containing the
 * <code>owl:distinctMembers</code> property. This property contains a list of
 * individuals that are pairwise different.
 * </p>
 * <p>
 * For example:
 * <code>
 * <pre>
 * <owl:AllDifferent>
 *   <owl:distinctMembers rdf:parseType="Collection">
 *     <Car rdf:about="#Mercedes"/>
 *     <Car rdf:about="#Ford"/>
 *     <Car rdf:about="#Renault"/>
 *     <Car rdf:about="#Volvo"/>
 *   </owl:distinctMembers>
 * </owl:AllDifferent>
 * </pre>
 * </code>
 * This states that there are four different cars.
 * </p>
 * <p>
 * <b>NOTE:</b> In OWL Full classes and properties can be treated as individuals
 * that is why we can express class inequality and property inequality using
 * <code>owl:AllDifferent</code>. However, such a statement cannot be used in
 * OWL Lite or DL which implies that there cannot be expressed class inequality
 * or property inequality in OWL Lite or DL.
 * </p>
 * 
 * @see AllDifferent
 * @author Jan Hrbek, Jakub Krauz
 * 
 * @deprecated This annotation shouldn't be used because of above mentioned NOTE.
 *             If we use it for a class, we treat this class as an instance.
 *             That means the ontology document will be in OWL Full, which is
 *             not compatible with reasoner processing.
 */
@Deprecated
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllDifferent {
	String[] value();
}