package semantic;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Defines user interface. It contains methods for running
 * the transformation process from object-oriented model
 * into a semantic model.
 * 
 * @author Jakub Krauz
 */
public interface ModelToSemantic {
	
	/**
	 * Runs the transformation process.
	 * Data must be set in constructor before calling this method.
	 * @return stream containing semantic
	 * @throws UnknownDataException if the data list wasn't set before transformation
	 */
	public InputStream createSemantic() throws IOException, OntologyException;
	
	/**
	 * Runs the transformation process.
	 * This method sets the data if there is used constructor without parameters.
	 * @param dataList list of objects (object-oriented model)
	 * @param namespace namespace value
	 * @return stream containing semantic
	 * @throws UnknownDataException if the data list wasn't set before transformation
	 */
	public InputStream createSemantic(List<Object> dataList, String namespace)
							throws IOException, OntologyException;
	
}
