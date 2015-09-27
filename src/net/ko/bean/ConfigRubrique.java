package net.ko.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigRubrique extends ConfigBean{
	private HashMap<String,ConfigVariable> variables;
	public ConfigRubrique(String name) {
		super(name);
		variables=new HashMap<>();
	}
	public ArrayList<ConfigVariable> getVariables() {
		return new ArrayList<>(variables.values());
	}
	@Override
	public boolean hasChildren() {
		return variables.size()>0;
	}
	@Override
	public Object[] getChildren() {
		return getVariables().toArray();
	}
	@Override
	public ConfigBean getParent() {
		return null;
	}
	public ConfigVariable setVariableValue(String name,String value){
		ConfigVariable result=null;
		if(variables.containsKey(name)){
			result=variables.get(name);
			result.setValue(value);
		}
		else{
			result=new ConfigVariable(name,value);
			variables.put(name, result);
		}
		return result;
	}
}
