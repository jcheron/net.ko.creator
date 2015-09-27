package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxAccordion;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxAccordion extends AjaxObject {

	private static final long serialVersionUID = 1L;

	public AjaxAccordion() {
		super();
		addToogle();
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxAccordion ajaxAccordion = (KAjaxAccordion) ajaxObject;
		Object result = "";
		switch (id) {
		case "parent":
			result = ajaxAccordion.getParent();
			break;
		case "containerId":
			result = ajaxAccordion.getContainerId();
			break;
		case "event":
			result = ajaxAccordion.getEvent();
			break;
		case "type":
			result = ajaxAccordion.getType();
			break;
		case "selectedIndex":
			result = ajaxAccordion.getSelectedIndex() + "";
			break;
		case "options":
			result = ajaxAccordion.getOptions();
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxAccordion ajaxAccordion = (KAjaxAccordion) ajaxObject;
		switch (id) {
		case "parent":
			ajaxAccordion.setParent(value + "");
			break;
		case "containerId":
			ajaxAccordion.setContainerId(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "event":
			ajaxAccordion.setEvent(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "type":
			ajaxAccordion.setType(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "selectedIndex":
			try {
				ajaxAccordion.setSelectedIndex(Integer.valueOf(value + ""));
			} catch (Exception e) {
			}

			break;
		case "options":
			ajaxAccordion.setOptions(value + "");
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> properties = new ArrayList<>();
		properties.add(PropertiesDescriptor.getPropertyText("parent"));
		properties.add(PropertiesDescriptor.getPropertyText("containerId"));
		properties.add(PropertiesDescriptor.getPropertyEvents("event"));
		properties.add(PropertiesDescriptor.getPropertyAccordionType("type"));
		properties.add(PropertiesDescriptor.getPropertyInt("selectedIndex"));
		properties.add(PropertiesDescriptor.getPropertyText("options"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
		addChilds(((KAjaxAccordion) ajaxObject).getChilds());
	}

	@Override
	public String getImage() {
		return Images.ACCORDION;
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		setName(ajaxObject.getDisplayCaption());
		getListeners().firePropertyChange(Node.PROPERTY_DISPLAY, oldValue, value);
	}

}
