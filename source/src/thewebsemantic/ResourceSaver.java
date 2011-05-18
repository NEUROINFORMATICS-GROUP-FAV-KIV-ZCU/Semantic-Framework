package thewebsemantic;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class ResourceSaver extends Saver {


        /**
         * Method removes property bounded to subject Resource and insert it
         * again as property, that means a predicate and Object o as an object
         * that is associated to this resource.
         * 
         * @param writer
         * @param subject
         * @param property
         * @param o
         */
	@Override
	public void save(Bean2RDF writer, Resource subject, Property property, Object o) {
		if (o==null) {
			subject.removeAll(property);
			return;
		}
		Model m = subject.getModel();
		subject.removeAll(property).addProperty(property,
				m.getResource(o.toString()));
	}

}
