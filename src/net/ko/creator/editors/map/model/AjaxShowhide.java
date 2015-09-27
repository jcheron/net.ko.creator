package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxShowHide;
import net.ko.utils.KString;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxShowhide extends AjaxObject {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getPropertyValue(String id) {
		KAjaxShowHide ajaxShowhide = (KAjaxShowHide) ajaxObject;
		Object result = "";
		switch (id) {
		case "visible":
			result = KString.isBooleanTrue(ajaxShowhide.getVisible()) ? 1 : 0;
			break;
		case "targetSelector":
			result = ajaxShowhide.getTargetSelector();
			break;
		case "targetContext":
			result = ajaxShowhide.getTargetContext();
			break;
		case "condition":
			result = ajaxShowhide.getCondition();
			break;
		case "timeIn":
			result = ajaxShowhide.getTimein() + "";
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxShowHide ajaxShowhide = (KAjaxShowHide) ajaxObject;
		switch (id) {
		case "visible":
			ajaxShowhide.setVisible(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "targetSelector":
			ajaxShowhide.setTargetSelector(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "targetContext":
			ajaxShowhide.setTargetContext(value + "");
			break;
		case "condition":
			ajaxShowhide.setCondition(value + "");
			break;
		case "timeIn":
			ajaxShowhide.setTimein(Integer.valueOf(value + ""));
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyBoolean("visible"));
		properties.add(PropertiesDescriptor.getPropertyText("targetSelector"));
		properties.add(PropertiesDescriptor.getPropertyInt("timeIn"));
		properties.add(PropertiesDescriptor.getPropertyText("targetContext"));
		properties.add(PropertiesDescriptor.getPropertyText("condition"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.SHOW_HIDE;
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		setName(ajaxObject.getDisplayCaption());
		getListeners().firePropertyChange(id, oldValue, value);
	}
}
