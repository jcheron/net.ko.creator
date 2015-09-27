package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxEvent;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxFireEvent extends AjaxObject {
	private static final long serialVersionUID = 1L;

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
		// setId(ajaxObject.getUniqueIdHash(PropertyUtils.getRandomId()));
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyEvents("triggerEvent"));
		properties.add(PropertiesDescriptor.getPropertyText("triggerId"));
		properties.add(PropertiesDescriptor.getPropertyInt("timeIn"));
		properties.add(PropertiesDescriptor.getPropertyText("condition"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(String id) {
		Object result = "";
		KAjaxEvent evt = (KAjaxEvent) ajaxObject;
		switch (id) {
		case "triggerEvent":
			result = evt.getTriggerEvent();
			break;
		case "triggerId":
			result = evt.getTriggerId();
			break;
		case "timeIn":
			result = evt.getTimein() + "";
			break;
		case "condition":
			result = evt.getCondition();
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxEvent evt = (KAjaxEvent) ajaxObject;
		switch (id) {
		case "triggerEvent":
			evt.setTriggerEvent(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "triggerId":
			evt.setTriggerId(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "timeIn":
			try {
				evt.setTimein(Integer.valueOf(value + ""));
			} catch (Exception e) {
			}

			break;
		case "condition":
			evt.setCondition(value + "");
			break;
		default:
			break;
		}

	}

	@Override
	public String getImage() {
		return Images.FIRE_EVENT;
	}
}
