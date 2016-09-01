package thewebsemantic;

import java.lang.reflect.Array;

import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Seq;
import org.apache.jena.shared.PropertyNotFoundException;

public class ArraySaver extends Saver {

    /**
     * Method adds items of param array to Model, these items are bounded
     * to propriate subjcet, that is specified in params
     *
     * @param writer Model Bean2Rdf writer
     * @param subject Target subject
     * @param property
     * @param array Array of items to insert to Model
     */

	@Override
	public void save(Bean2RDF writer, Resource subject, Property property,
			Object array) {
		// don't remove children unless we get a 0 length list
		if ( array==null)
			return;
		Seq s = getSeq(subject, property);
		int len = Array.getLength(array);
		for (int i = 0; i < len; i++) {
			Object o = Array.get(array, i);
			if (o==null)
				continue;
			s.add(writer.toRDFNode(o));
		}
	}

        /**
         * Returns RDF Sequence container of param subjcet, that had added
         * param property
         * @param subject
         * @param property
         * @return sequence
         */
	protected Seq getSeq(Resource subject, Property property) {
		try {
			Seq s = subject.getRequiredProperty(property).getSeq();
			NodeIterator it = s.iterator();
			while (it.hasNext()) {
				RDFNode node = it.nextNode();
				if (node.isAnon())
					node.as(Resource.class).removeProperties();
			}
			it.close();
			s.removeProperties();
			subject.removeAll(property);
		} catch (PropertyNotFoundException e) {}
		Seq seq = subject.getModel().createSeq();
		subject.addProperty(property, seq);
		return seq;
	}

}
