package net.ko.bean;

public enum TemplateType {
	ttView("view"),ttList("list"),ttShow("show"),ttCustom("custom"),ttNone("");
	private String label;
	private TemplateType(String label){
		this.label=label;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public static TemplateType getType(String tType){
		TemplateType result=null;
		for(TemplateType ct:TemplateType.values())
			if(ct.getLabel().equals(tType)){
				result=ct;
				break;
			}
		return result;
	}
}
