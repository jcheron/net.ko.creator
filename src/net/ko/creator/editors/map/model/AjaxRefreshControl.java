package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxRefreshControl;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxRefreshControl extends AjaxObject implements IHasConnectorURL {

	private static final long serialVersionUID = 1L;

	public AjaxRefreshControl() {
		super();
		addToogle();
	}

	public KAjaxRefreshControl getAjaxRefreshControl() {
		return (KAjaxRefreshControl) ajaxObject;
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		this.ajaxObject = ajaxObject;
		updateProperty("virtualURL", null, getAjaxRefreshControl().getVirtualURL());
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxRefreshControl ajaxRefreshControl = getAjaxRefreshControl();
		Object result = "";
		switch (id) {
		case "virtualURL":
			result = getURL();
			break;
		case "name":
			result = ajaxRefreshControl._getName();
			break;
		case "kobjectShortClassName":
			result = ajaxRefreshControl.getClassName();
			break;
		case "controlType":
			result = ajaxRefreshControl.getControlType();
			break;
		case "filterList":
			result = ajaxRefreshControl.getFilterList();
			break;
		case "koDisplay":
			result = ajaxRefreshControl.getKoDisplay();
			break;
		case "condition":
			result = ajaxRefreshControl.getCondition();
			break;
		case "value":
			result = ajaxRefreshControl.getValue();
			break;
		case "targetParams":
			result = ajaxRefreshControl.getTargetParams();
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxRefreshControl ajaxRefreshControl = getAjaxRefreshControl();
		switch (id) {
		case "virtualURL":
			setURL(value + "");
			break;
		case "name":
			ajaxRefreshControl.setName(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "kobjectShortClassName":
			ajaxRefreshControl.setClassName(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "controlType":
			ajaxRefreshControl.setControlType(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "filterList":
			ajaxRefreshControl.setFilterList(value + "");
			break;
		case "koDisplay":
			ajaxRefreshControl.setKoDisplay(value + "");
			break;
		case "condition":
			ajaxRefreshControl.setCondition(value + "");
			break;
		case "value":
			ajaxRefreshControl.setValue(value + "");
			break;
		case "targetParams":
			ajaxRefreshControl.setTargetParams(value + "");
			break;
		default:
			break;
		}

	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyAllURLs("virtualURL", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyText("name"));
		properties.add(PropertiesDescriptor.getPropertyKClasses("kobjectShortClassName", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyControlTypes("controlType"));
		properties.add(PropertiesDescriptor.getPropertyText("filterList"));
		properties.add(PropertiesDescriptor.getPropertyContentAssistText("koDisplay"));
		properties.add(PropertiesDescriptor.getPropertyText("condition"));
		properties.add(PropertiesDescriptor.getPropertyText("value"));
		properties.add(PropertiesDescriptor.getPropertyText("targetParams"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.REFRESH_CONTROL;
	}

	@Override
	public void setURL(String newURL) {
		setURL(newURL, false);
	}

	@Override
	public String getURL() {
		return getAjaxRefreshControl().getVirtualURL();
	}

	@Override
	public void setURL(String newURL, boolean fromRequest) {
		String oldURL = getURL();
		if (!newURL.equals(oldURL)) {
			getAjaxRefreshControl().setVirtualURL(newURL);
			updateProperty("targetURL", oldURL, newURL);
			if (!fromRequest)
				updateConnection(newURL);
			else
				refreshSourceConnection();
		}
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		super.updateProperty(id, oldValue, value);
	}

}
