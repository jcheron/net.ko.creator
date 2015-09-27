package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.KAjaxJs;
import net.ko.mapping.KAjaxObject;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class JS extends Node {
	private static final long serialVersionUID = 1L;
	private transient KAjaxJs ajaxJs;

	public JS() {
		super();
		defaultAppearence = AppearenceConfig.defaultJs();
		addToogle();
	}

	public KAjaxJs getAjaxJs() {
		return ajaxJs;
	}

	public void setAjaxJs(KAjaxJs ajaxJs) {
		this.ajaxJs = ajaxJs;
		setName(ajaxJs.getTriggerSelector() + "." + ajaxJs.getTriggerEvent() + "()");
		addChilds(ajaxJs.getChilds());
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyText("triggerSelector"));
		properties.add(PropertiesDescriptor.getPropertyEvents("triggerEvent"));
		properties.add(PropertiesDescriptor.getPropertyText("triggerContext"));
		properties.add(PropertiesDescriptor.getPropertyInt("keyCode"));
		properties.add(PropertiesDescriptor.getPropertyBoolean("unique"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(String id) {
		Object result = "";
		switch (id) {
		case "triggerSelector":
			result = ajaxJs.getTriggerSelector();
			break;
		case "triggerEvent":
			result = ajaxJs.getTriggerEvent();
			break;
		case "triggerContext":
			result = ajaxJs.getTriggerContext();
			break;
		case "keyCode":
			result = ajaxJs.getKeyCode() + "";
			break;
		case "unique":
			result = ajaxJs.isUnique() + "";
		default:
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		switch (id) {
		case "triggerSelector":
			ajaxJs.setTriggerSelector(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "triggerEvent":
			ajaxJs.setTriggerEvent(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "triggerContext":
			ajaxJs.setTriggerContext(value + "");
			break;
		case "keyCode":
			try {
				ajaxJs.setKeyCode(Integer.valueOf(value + ""));
			} catch (Exception e) {
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		setName(ajaxJs.getTriggerSelector() + "." + ajaxJs.getTriggerEvent() + "()");
		getListeners().firePropertyChange(id, oldValue, value);
	}

	@Override
	public String getImage() {
		return Images.JS;
	}

	@Override
	public Object getXmlObject() {
		return ajaxJs;
	}

	@Override
	public List<Object> getXmlChildren() {
		return new ArrayList<Object>(ajaxJs.getChilds());
	}

	@Override
	public boolean removeXmlChild(Object child) {
		return ajaxJs.getChilds().remove(child);
	}

	@Override
	public void addXmlChild(Object element) {
		if (element instanceof KAjaxObject)
			ajaxJs.addChild((KAjaxObject) element);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		if (ajaxJs != null)
			return ajaxJs.equals(((JS) obj).getAjaxJs());
		return false;
	}

	@Override
	public int hashCode() {
		if (ajaxJs != null)
			return ajaxJs.hashCode();
		return super.hashCode();
	}

	@Override
	public void setXmlObject(Object o) {
		if (o instanceof JS)
			setAjaxJs((KAjaxJs) o);

	}

	@Override
	public String getTooltipMessage() {
		return ajaxJs.getName();
	}
}
