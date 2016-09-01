package thewebsemantic;

import java.lang.reflect.Array;
import java.util.List;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import thewebsemantic.lazy.Lazy;



public class ListSaver extends Saver {

        /**
         * Method saves data from object to Model bounded to Resource subject
         * using ArraySaver.
         * @param writer
         * @param subject
         * @param property
         * @param o
         */
	@Override
	public void save(Bean2RDF writer, Resource subject, Property property, Object o) {
		// we will not remove children unless we get a 0 length list
		if ( o==null)
			return;
		else if (o instanceof Lazy && ! ((Lazy)o).isConnected()) {
			return;
		} else if (o instanceof Lazy && ! ((Lazy)o).modified()) {
			return;
		}
		Saver.of(Array.class).save(writer, subject, property, ((List)o).toArray());

	}

}
