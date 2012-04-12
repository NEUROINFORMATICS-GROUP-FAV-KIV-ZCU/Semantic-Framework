package thewebsemantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotated attribute will be used as a unique key to idetify bean.<br>
 * Naming pattern for created individuals in OWL is
 * <code>"className_id"</code>. If no attribute is set as ID, bean's
 * hash code will be used instead.
 * </p>
 * 
 * <p>
 * <code>@Id</code> also determines, whether JenaBeanExtension searches for property
 * annotations over fields or methods. If ID is field, JenaBeanExtension
 * searches for field annotations, otherwise for method annotations.
 * </p>
 * 
 * <p>
 * Example of use<br>
 * If we consider this class definition (other fields and accessor
 * methods are omitted):
 * <code> <pre>
 * public class Person {
 *   &#64;Id
 *   private String name;
 *   ...
 * }
 * </pre> </code>
 * Then if we have some bean:
 * <code> <pre>
 * Person p = new Person();
 * p.setName("Novak");
 * </pre> </code>
 * Then the bean will be mapped to something like:
 * <code> <pre>
 * &lt;ex:Person rdf:about="#Person_Novak"&gt;
 *   ...
 * &lt;/ex:Person&gt;
 * </pre> </code>
 * </p>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME) 
public @interface Id {

}
/*
	Copyright (c) 2007 
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
*/