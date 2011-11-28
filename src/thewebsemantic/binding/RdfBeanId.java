package thewebsemantic.binding;

import java.rmi.server.UID;

import thewebsemantic.annotations.Id;

public class RdfBeanId<T> extends RdfBean<T> {
	@Id
	protected String id;

	public RdfBeanId() {
		this.id = new UID().toString();
	}

	public RdfBeanId(String id) {
		this.id = id;
	}

	
	public String id() {return id;}

}
