package net.ko.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigRubriqueList {
	private HashMap<String, ConfigRubrique> rubriques;
	
	public ConfigRubriqueList() {
		rubriques=new HashMap<>();
	}
	public void createRubriques() {
		rubriques=new HashMap<>();
		getRubrique("BDD");
		getRubrique("Css");
		getRubrique("Divers");
	}
	public ConfigRubrique getRubrique(String name){
		ConfigRubrique result=null;
		if(rubriques.containsKey(name))
			result=rubriques.get(name);
		else{
			result=new ConfigRubrique(name);
			rubriques.put(name, result);
		}
		return result;
	}
	public ArrayList<ConfigRubrique> getRubriques(){
		return new ArrayList<>(rubriques.values());
	}
	public ArrayList<ConfigVariable> getVariables(){
		ArrayList<ConfigVariable> result=new ArrayList<>();
		for(Map.Entry<String,ConfigRubrique> eRub:rubriques.entrySet()){
			result.addAll(eRub.getValue().getVariables());
		}
		return result;
	}
	public void setRubriqueValue(String rubriqueName,String VariableName,String value){
		getRubrique(rubriqueName).setVariableValue(VariableName, value);
	}
}
