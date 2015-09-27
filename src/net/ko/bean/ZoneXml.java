package net.ko.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ko.creator.preferencepage.KoTemplateContextType;
import net.ko.utils.KString;
import net.ko.utils.KStrings;

import org.eclipse.jface.text.templates.Template;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ZoneXml {
	private String id;
	private String caption;
	private ZoneType type;
	private ZoneType destType;
	private TemplateType templateType;
	private String description;
	private String content;
	private String defaultValue;
	private String[] selection;
	private String[] params;
	private String[] toolTip;
	
	public ZoneXml(){
		this("", ZoneType.ztFunc, null);
	}
	public ZoneXml(String caption, ZoneType zType,TemplateType tType) {
		this(caption, caption, "{"+caption+"}", ZoneType.ztField, tType);
	}
	public ZoneXml(String id, String caption, String content,ZoneType zType,TemplateType tType) {
		this.id=id;
		this.caption=caption;
		this.content=content;
		this.type=zType;
		this.templateType=tType;
		this.defaultValue="";
		this.description="";
		this.selection=new String[]{};
		this.params=new String[]{};
		this.toolTip=new String[]{};
	}

	public String getValue(){
		return content.replace("{defaultValue}", defaultValue);
	}
	public String getTemplateValue(){
		String result= content.replace("{defaultValue}", defaultValue);
		for(int i=0;i<selection.length;i++){
			if(!"".equals(selection[i]))
				result=result.replace(selection[i], "${"+selection[i]+"}");
		}
		return result+"${cursor}";
	}
	public String getRegExp(){
		String result=content.replace("{defaultValue}", defaultValue);
		if(selection.length>0){
			for(int i=0;i<selection.length;i++){
				result=result.replace(selection[i], "|");
			}
			String[] parties=result.split("\\|");
			for(int i=0;i<parties.length;i++){
				parties[i]=Pattern.quote(parties[i]);
			}
			result=KStrings.implode("((\\s|.)*?)", parties);
		}else
			result=Pattern.quote(result);
		return result;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public ZoneType getType() {
		return type;
	}

	public void setType(ZoneType type) {
		this.type = type;
	}

	public void setType(String type) {
		this.type = ZoneType.getType(type);
	}

	public ZoneType getDestType() {
		return destType;
	}

	public void setDestType(ZoneType destType) {
		this.destType = destType;
	}
	public void setDestType(String destType) {
		this.destType = ZoneType.getType(destType);
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String[] getSelection() {
		return selection;
	}

	public void setSelection(String[] selection) {
		this.selection=selection;
	}

	public void initFromXML(Node item) {
		Element element=(Element) item;
		id=element.getAttribute("id");
		caption=element.getAttribute("caption");
		setType(element.getAttribute("type"));
		setDestType(element.getAttribute("destType"));
		setTemplateType(element.getAttribute("templateType"));
		NodeList map=item.getChildNodes();
		for(int i=0;i<map.getLength();i++){
			Node n=map.item(i);
			if(n.getNodeName().equalsIgnoreCase("description"))
				description=n.getTextContent();
			if(n.getNodeName().equalsIgnoreCase("content"))
				content=n.getTextContent();
			if(n.getNodeName().equalsIgnoreCase("defaultValue"))
				defaultValue=n.getTextContent();
			if(n.getNodeName().equalsIgnoreCase("selection")){
				selection=n.getTextContent().split("\\|");
			}
			if(n.getNodeName().equalsIgnoreCase("toolTip")){
				toolTip=n.getTextContent().split("\\|");
			}
			if(n.getNodeName().equalsIgnoreCase("params")){
				params=n.getTextContent().split("\\|");
			}
		}
		if(toolTip.length==0)
			toolTip=new String[]{description};
	}

	public TemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = TemplateType.getType(templateType);
	}
	public String[] getToolTip() {
		return toolTip;
	}
	public void setToolTip(String[] toolTip) {
		this.toolTip = toolTip;
	}
	public String getFirstToolTip() {
		String result="";
		if(toolTip.length>0)
			result=toolTip[0];
		return result;
	}
	public Template getTemplate(){
		Template tmpl=new TemplateElement(type, caption, description, KoTemplateContextType.CONTEXT_TYPE, getTemplateValue(), true);
		return tmpl;
	}
	public boolean matchWith(String value){
		value=Matcher.quoteReplacement(value);
		Pattern p = Pattern.compile(getRegExp(),Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(value);
		return m.matches();
	}
	public String getPropertyValue(String propertyName,String expression){
		String result=expression;
		int index=getPropertyIndex(propertyName);
		if(index!=-1){
			Pattern p = Pattern.compile(getRegExp(),Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(expression);
			if(m.find())
				if(m.groupCount()>2*index+1)
					result=m.group(2*index+1);
		}
		return result;
	}
	public String setPropertyValue(String propertyName,String newValue,String expression){
		String result=expression;
		int index=getPropertyIndex(propertyName);
		if(index!=-1){
			Pattern p = Pattern.compile(getRegExp(),Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(expression);
			if(m.find())
				if(m.groupCount()>2*index+1){
					result=expression.substring(0, m.start(2*index+1))+newValue+expression.substring(m.end(2*index+1));
				}
		}
		return result;
	}
	public int getPropertyIndex(String propertyName){
		int result=-1;
		for(int i=0;i<params.length;i++){
			if(params[i].equalsIgnoreCase(propertyName)){
				result=i;
				break;
			}
		}
		return result;
	}
	public Object[][] getPropertyDescriptor(){
		int length=params.length;
		Object result[][]=null;
		if(length>0){
			result=new Object[params.length][2];
			for(int i=0;i<length;i++){
				String s=params[i];
				result[i][0]=s;
				result[i][1]=new TextPropertyDescriptor(s,KString.capitalizeFirstLetter(s));
			}
		}else{
			result=new Object[1][2];
			result[0][0]="value";
			result[0][1]=new PropertyDescriptor("value", "Value");
		}
		return result;
	}
}
