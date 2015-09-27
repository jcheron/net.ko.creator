package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxSelector;
import net.ko.mapping.KAjaxWithChilds;
import net.ko.utils.KString;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxSelector extends AjaxObject {
	private static final long serialVersionUID = 1L;

	public AjaxSelector() {
		super();
		addToogle();
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		this.ajaxObject = ajaxObject;
		setName(ajaxObject.getDisplayCaption());
		if (ajaxObject instanceof KAjaxWithChilds)
			addChilds(((KAjaxWithChilds) ajaxObject).getChilds());
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxSelector ajaxSelector = (KAjaxSelector) ajaxObject;
		Object result = "";
		switch (id) {
		case "selector":
			result = ajaxSelector.getSelector();
			break;
		case "event":
			result = ajaxSelector.getEvent();
			break;
		case "allowNull":
			result = (ajaxSelector.isAllowNull()) ? 1 : 0;
			break;
		case "startIndex":
			result = ajaxSelector.getStartIndex() + "";
			break;
		case "selectedStyle":
			result = ajaxSelector.getSelectedStyle();
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxSelector selector = (KAjaxSelector) ajaxObject;
		switch (id) {
		case "selector":
			selector.setSelector(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "event":
			selector.setEvent(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "allowNull":
			selector.setAllowNull(KString.isBooleanTrue(value + ""));
			break;
		case "startIndex":
			selector.setStartIndex(Integer.valueOf(value + ""));
			break;
		case "selectedStyle":
			selector.setSelectedStyle(value + "");
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyText("selector"));
		properties.add(PropertiesDescriptor.getPropertyEvents("event"));
		properties.add(PropertiesDescriptor.getPropertyBoolean("allowNull"));
		properties.add(PropertiesDescriptor.getPropertyInt("startIndex"));
		properties.add(PropertiesDescriptor.getPropertyText("selectedStyle"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.SELECTOR;
	}
}
