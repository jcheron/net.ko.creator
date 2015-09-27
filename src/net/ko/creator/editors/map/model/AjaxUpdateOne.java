package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.KAjaxUpdateOne;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxUpdateOne extends AjaxDeleteOne {

	private static final long serialVersionUID = 1L;

	public AjaxUpdateOne() {
		super();
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxUpdateOne ajaxUpdateOne = (KAjaxUpdateOne) ajaxObject;
		Object result = super.getPropertyValue(id);
		switch (id) {
		case "operation":
			result = ajaxUpdateOne.getOperation();
			break;
		}
		if (result == null)
			result = "";
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxUpdateOne ajaxUpdateOne = (KAjaxUpdateOne) ajaxObject;
		super.setPropertyValue(id, value);
		switch (id) {
		case "operation":
			ajaxUpdateOne.setOperation(value + "");
			updateProperty(id, oldValue, value);
			break;
		default:
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> properties = new ArrayList<>(Arrays.asList(super.getPropertyDescriptors()));
		properties.add(PropertiesDescriptor.getPropertyUpdateOneOp("operation"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.UPDATE_ONE;
	}
}
