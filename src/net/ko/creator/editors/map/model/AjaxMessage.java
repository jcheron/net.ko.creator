package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxMessage;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxMessage extends AjaxObject {
	private static final long serialVersionUID = 1L;

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxMessage ajaxMessage = (KAjaxMessage) ajaxObject;
		Object result = "";
		switch (id) {
		case "message":
			result = ajaxMessage.getMessage();
			break;
		case "targetId":
			result = ajaxMessage.getTargetId();
			break;
		case "title":
			result = ajaxMessage.getTitle();
			break;
		case "timeIn":
			result = ajaxMessage.getTimein() + "";
			break;
		case "timeOut":
			result = ajaxMessage.getTimeout() + "";
			break;
		case "condition":
			result = ajaxMessage.getCondition();
			break;
		case "javaInterface":
			result = ajaxMessage.getAjaxObjectInterface();
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxMessage ajaxMessage = (KAjaxMessage) ajaxObject;
		switch (id) {
		case "message":
			ajaxMessage.setMessage(value + "");
			break;
		case "targetId":
			ajaxMessage.setTargetId(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "title":
			ajaxMessage.setTitle(value + "");
			break;
		case "timeIn":
			try {
				ajaxMessage.setTimein(Integer.valueOf(value + ""));
			} catch (Exception e) {
			}

			break;
		case "timeOut":
			try {
				ajaxMessage.setTimeout(Integer.valueOf(value + ""));
			} catch (Exception e) {
			}

			break;
		case "condition":
			ajaxMessage.setCondition(value + "");
			break;

		case "javaInterface":
			ajaxMessage.setAjaxObjectInterface(value + "");
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyText("message"));
		properties.add(PropertiesDescriptor.getPropertyText("targetId"));
		properties.add(PropertiesDescriptor.getPropertyInt("timeIn"));
		properties.add(PropertiesDescriptor.getPropertyInt("timeOut"));
		properties.add(PropertiesDescriptor.getPropertyText("title"));
		properties.add(PropertiesDescriptor.getPropertyText("transition"));
		properties.add(PropertiesDescriptor.getPropertyText("condition"));
		properties.add(PropertiesDescriptor.getPropertyContentAssistText("javaInterface"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.MESSAGE;
	}
}
