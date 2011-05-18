package thewebsemantic;

import static thewebsemantic.Bean2RDF.logger;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.logging.Level;


import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * FieldContext represents a descendent of ValuesContext class which
 * encapsulates beans attributes and its annotations.
 * It overides abstract methods to gather annotation informations from
 * encapsulated attributes in direct way or calling parent class methods if
 * they are enough common to retrieve data.
 * 
 */
class FieldContext extends ValuesContext {

    Field field;
    TypeWrapper type;
    boolean idField;

    public FieldContext(Object bean, Field p, boolean id) {
        subject = bean;
        field = p;
        //if (subject==null)
        type = TypeWrapper.wrap(p.getDeclaringClass());
        //else
        //	type = TypeWrapper.type(bean);
        idField = id;
    }

    public String uri() {
        return type.uri(field, field.getName());
    }

    public boolean isSymmetric() {
        return isSymmetric(field);
    }

    private boolean isSymmetric(Field p) {
        return (field.isAnnotationPresent(Symmetric.class)) ? true
                : TypeWrapper.getRDFAnnotation(field).symmetric();
    }

    public boolean isInverse() {
        return isInverse(field);
    }

    private boolean isInverse(Field p) {
        return (field.isAnnotationPresent(Inverse.class));
    }

    public String inverseOf() { // inverse item Uri
        return inverseOf(field);
    }

    public boolean isDataRange() { // tests if Data range annotation present
        return isDataRange(field);
    }

    private boolean isDataRange(Field p) { // tests if Data range annotation present
        return (field.isAnnotationPresent(DataRange.class));
    }

    public String dataRange() { // returns data range restriction value
        return dataRange(field);
    }

    public boolean isVersionInfo() { // tests if version info annotation present
        return isVersionInfo(field);
    }

    private boolean isVersionInfo(Field p) { // tests if version info annotation present
        return (field.isAnnotationPresent(VersionInfo.class));
    }

    public String versionInfo() { // returns version info value
        return versionInfo(field);
    }


    public boolean isComment() { // tests if comment annotation present
        return isComment(field);
    }

    private boolean isComment(Field p) { // tests if comment annotation present
        return (field.isAnnotationPresent(Comment.class));
    }

    public String comment() { // returns version info value
        return comment(field);
    }


    public boolean isSeeAlso() { // tests if see also annotation present
	return isSeeAlso(field);
    }

    private boolean isSeeAlso(Field p) { // tests if see also annotation present
        return (field.isAnnotationPresent(SeeAlso.class));
    }


    public String seeAlso() { // returns see also value
        return seeAlso(field);
    }

    public boolean isLabel() {	// tests if label annotation is present
        return isLabel(field);
    }

    private boolean isLabel(Field p) {	// tests if label annotation present
        return (field.isAnnotationPresent(Label.class));
    }

    public String label() { // returns label value
        return label(field);
    }

    public boolean isIsDefinedBy(){ //tests if definedBy annotation is present
        return isIsDefinedBy(field);
    }

    public boolean isIsDefinedBy(Field p){  //tests if definedBy annotation present
        return (field.isAnnotationPresent(IsDefinedBy.class));
    }

    public String isDefinedBy(){  //return IsdefinedBy value
        return isDefinedBy(field);
    }

    public boolean existsInModel(Model m) {
        return m.getGraph().contains(Node.createURI(uri()), Node.ANY, Node.ANY);
    }

    public Object invokeGetter() {
        Object result = null;
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            result = field.get(subject);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error retrieving field value.", e);
        }
        return result;
    }

    public void setProperty(Object v) {
        try {
            field.setAccessible(true);
            field.set(subject, v);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not set bean field " + field.getName(), e);
        }
    }

    public boolean isDate() {
        return type().equals(Date.class);
    }

    public boolean isPrimitive() {
        return PrimitiveWrapper.isPrimitive(field.getType());
    }


    public Class<?> type() {
        return field.getType();
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> t() {

        return getGenericType((ParameterizedType) field.getGenericType());
    }

    @Override
    public boolean isTransitive() {

        return isTransitive(field);
    }

    /**
     * Returns if Id annotation is preset over this object
     * @return
     */
    @Override
    public boolean isId() {
        return idField;
    }


    @Override
    public AccessibleObject getAccessibleObject() {
        return field;
    }

    

}
