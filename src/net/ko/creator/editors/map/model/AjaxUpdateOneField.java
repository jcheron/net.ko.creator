package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxUpdateOneField;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxUpdateOneField extends AjaxObject {

	private static final long serialVersionUID = 1L;

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxUpdateOneField ajaxUpdateOneField = (KAjaxUpdateOneField) ajaxObject;
		Object result = "";
		switch (id) {
		case "fieldName":
			result = ajaxUpdateOneField.getFieldName();
			break;
		case "fieldValue":
			result = ajaxUpdateOneField.getFieldValue();
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxUpdateOneField ajaxUpdateOneField = (KAjaxUpdateOneField) ajaxObject;
		switch (id) {
		case "fieldName":
			ajaxUpdateOneField.setFieldName(value + "");
			updateProperty(id, oldValue, value + "");
			break;
		case "fieldValue":
			ajaxUpdateOneField.setFieldValue(value + "");
			updateProperty(id, oldValue, value + "");
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyText("fieldName"));
		properties.add(PropertiesDescriptor.getPropertyText("fieldValue"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.UPDATE_ONE_FIELD;
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		String name = ajaxObject.getDisplayCaption();
		setName(name);
		getListeners().firePropertyChange(Node.PROPERTY_DISPLAY, oldValue, value);
	}

}
