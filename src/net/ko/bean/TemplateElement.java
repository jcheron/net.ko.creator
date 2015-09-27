package net.ko.bean;

import org.eclipse.jface.text.templates.Template;

public class TemplateElement extends Template {
	private ZoneType zType;
	public TemplateElement() {
		super();
		zType=ZoneType.ztField;
	}

	public TemplateElement(ZoneType zType,String name, String description,String contextTypeId, String pattern, boolean isAutoInsertable) {
		super(name, description, contextTypeId, pattern, isAutoInsertable);
		this.zType=zType;
	}

	public ZoneType getzType() {
		return zType;
	}

	public void setzType(ZoneType zType) {
		this.zType = zType;
	}

}
