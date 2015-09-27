package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.design.KCssOneTransition;
import net.ko.mapping.IAjaxObject;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class CssOneTransition extends AjaxObject {

	private static final long serialVersionUID = 1L;

	public CssOneTransition() {
		super();
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
	}

	@Override
	public Object getPropertyValue(String id) {
		KCssOneTransition cssOneTransition = (KCssOneTransition) ajaxObject;
		Object result = "";
		switch (id) {
		case "property":
			result = cssOneTransition.getProperty();
			break;
		case "startValue":
			result = cssOneTransition.getStartValue();
			break;
		case "endValue":
			result = cssOneTransition.getEndValue();
			break;
		case "duration":
			result = cssOneTransition.getDuration();
			break;
		case "timing":
			result = cssOneTransition.getTiming();
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KCssOneTransition cssOneTransition = (KCssOneTransition) ajaxObject;
		switch (id) {
		case "property":
			cssOneTransition.setProperty(value + "");
			updateProperty(Node.PROPERTY_DISPLAY, oldValue, value);
			break;
		case "startValue":
			cssOneTransition.setStartValue(value + "");
			updateProperty(Node.PROPERTY_DISPLAY, oldValue, value);
			break;
		case "endValue":
			cssOneTransition.setEndValue(value + "");
			updateProperty(Node.PROPERTY_DISPLAY, oldValue, value);
			break;
		case "duration":
			cssOneTransition.setDuration(value + "");
			break;
		case "timing":
			cssOneTransition.setTiming(value + "");
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyCssProperty("property"));
		properties.add(PropertiesDescriptor.getPropertyText("startValue"));
		properties.add(PropertiesDescriptor.getPropertyText("endValue"));
		properties.add(PropertiesDescriptor.getPropertyText("duration"));
		properties.add(PropertiesDescriptor.getPropertyCssTiming("timing"));

		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.ONE_TRANSITION;
	}

}
