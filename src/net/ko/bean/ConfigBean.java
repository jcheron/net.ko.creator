package net.ko.bean;

public abstract class ConfigBean {
	protected String name;
	public abstract boolean hasChildren();
	public abstract Object[] getChildren();
	public abstract ConfigBean getParent();
	public String getName() {
		return name;
	}
	public ConfigBean(String name) {
		super();
		this.name = name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
}
