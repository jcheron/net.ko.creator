package net.ko.bean;


import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TypedRegion;

public class ZTypedRegion extends TypedRegion {
	private ZoneXml zone;
	private String expression;
	private IDocument document;
	private ITypedRegion region;
	
	private ZTypedRegion(int offset, int length, String type) {
		super(offset, length, type);
	}

	public ZTypedRegion(ITypedRegion region) {
		super(region.getOffset(), region.getLength(), region.getType());
		this.region=region;
	}

	public String getPropertyValue(String propertyName){
		String result="";
		if(zone!=null){
			result=zone.getPropertyValue(propertyName, expression);
		}
		return result;
	}

	public void setPropertyValue(String propertyName,String newValue){
		if(zone!=null){
			expression=zone.setPropertyValue(propertyName,newValue, expression);
			try {
				document.replace(region.getOffset(), region.getLength(), expression);
			} catch (BadLocationException e) {
			}
		}
	}

	public ZoneXml getZone() {
		return zone;
	}

	public void setZone(ZoneXml zone) {
		this.zone = zone;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setDocument(IDocument document) {
		this.document=document;
		
	}
}
