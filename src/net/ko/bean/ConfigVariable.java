package net.ko.bean;

public class ConfigVariable extends ConfigBean{
	private String value;
	private ConfigRubrique rubrique;
	
	public ConfigVariable(String name, String value) {
		super(name);
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ConfigRubrique getRubrique() {
		return rubrique;
	}
	public void setRubrique(ConfigRubrique rubrique) {
		this.rubrique = rubrique;
	}
	@Override
	public boolean hasChildren() {
		return false;
	}
	@Override
	public Object[] getChildren() {
		return null;
	}
	@Override
	public ConfigBean getParent() {
		return rubrique;
	}
	@Override
	public String toString() {
		return name+" : ["+value+"]";
	}
}
