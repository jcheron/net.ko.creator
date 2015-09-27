package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.creator.editors.map.utils.PropertyUtils;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxRefreshFormValues;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxRefreshFormValues extends AjaxInclude {

	private static final long serialVersionUID = 1L;

	public AjaxRefreshFormValues() {
		super();
	}

	@Override
	public Object getPropertyValue(String id) {
		Object result = super.getPropertyValue(id);
		KAjaxRefreshFormValues ajaxRefreshFormValues = (KAjaxRefreshFormValues) ajaxObject;
		switch (id) {
		case "virtualURL":
			result = getURL();
			break;
		case "kobjectShortClassName":
			result = ajaxRefreshFormValues.getClassName();
			break;
		case "keyValues":
			result = ajaxRefreshFormValues.getKeyValues();
			break;
		case "excludedFields":
			result = ajaxRefreshFormValues.getExcludedFields();
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		this.ajaxObject = ajaxObject;
		updateProperty("virtualURL", null, ((KAjaxRefreshFormValues) ajaxObject).getTargetURL());
		addChilds(((KAjaxRefreshFormValues) ajaxObject).getChilds());
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		KAjaxRefreshFormValues ajaxRefreshFormValues = (KAjaxRefreshFormValues) ajaxObject;
		Object oldValue = getPropertyValue(id);
		super.setPropertyValue(id, value);
		switch (id) {
		case "virtualURL":
			setURL(value + "");
			break;
		case "kobjectShortClassName":
			ajaxRefreshFormValues.setClassName(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "keyValues":
			ajaxRefreshFormValues.setKeyValues(value + "");
			break;
		case "excludedFields":
			ajaxRefreshFormValues.setExcludedFields(value + "");
			break;
		default:
			break;
		}
	}

	public KAjaxRefreshFormValues ajaxRefreshFormValues() {
		return (KAjaxRefreshFormValues) ajaxObject;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> properties = new ArrayList<>(Arrays.asList(super.getPropertyDescriptors()));
		PropertyUtils.removeProperty("targetId", properties);
		PropertyUtils.removeProperty("method", properties);
		PropertyUtils.removeProperty("targetURL", properties);
		properties.add(PropertiesDescriptor.getPropertyAllURLs("virtualURL", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyKClasses("kobjectShortClassName", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyText("keyValues"));
		properties.add(PropertiesDescriptor.getPropertyText("excludedFields"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.REFRESH_FORM_VALUES;
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		setName(ajaxObject.getDisplayCaption());
		getListeners().firePropertyChange(id, oldValue, value);
	}
}
