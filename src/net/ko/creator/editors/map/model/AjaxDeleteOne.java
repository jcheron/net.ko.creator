package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxDeleteOne;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxDeleteOne extends AjaxObject implements IHasConnectorURL {

	private static final long serialVersionUID = 1L;

	public AjaxDeleteOne() {
		super();
		addToogle();
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxDeleteOne ajaxUpdateOne = (KAjaxDeleteOne) ajaxObject;
		Object result = "";
		switch (id) {
		case "virtualURL":
			result = getURL();
			break;
		case "kobjectShortClassName":
			result = ajaxUpdateOne.getKobjectShortClassName();
			break;
		case "targetId":
			result = ajaxUpdateOne.getTargetId();
			break;
		case "method":
			result = ajaxUpdateOne.getMethod();
			break;
		case "targetParams":
			result = ajaxUpdateOne.getoTargetParams();
			break;
		case "targetFunction":
			result = ajaxUpdateOne.getTargetFunction();
			break;
		case "timeIn":
			result = ajaxUpdateOne.getTimein() + "";
			break;
		case "timeOut":
			result = ajaxUpdateOne.getTimeout() + "";
			break;
		case "keyValues":
			result = ajaxUpdateOne.getKeyValues();
		}
		if (result == null)
			result = "";
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxDeleteOne ajaxUpdateOne = (KAjaxDeleteOne) ajaxObject;
		switch (id) {
		case "targetId":
			ajaxUpdateOne.setTargetId(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "virtualURL":
			setURL(value + "");
			break;
		case "kobjectShortClassName":
			ajaxUpdateOne.setKobjectShortClassName(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "method":
			ajaxUpdateOne.setMethod(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "targetParams":
			ajaxUpdateOne.setoTargetParams(value + "");
			break;
		case "targetFunction":
			ajaxUpdateOne.setTargetFunction(value + "");
			break;
		case "timeIn":
			ajaxUpdateOne.setTimein(Integer.valueOf(value + ""));
			break;
		case "timeOut":
			ajaxUpdateOne.setTimeout(Integer.valueOf(value + ""));
			break;
		case "keyValues":
			ajaxUpdateOne.setKeyValues(value + "");
			break;
		default:
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyAllURLs("virtualURL", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyText("targetId"));
		properties.add(PropertiesDescriptor.getPropertyText("targetFunction"));
		properties.add(PropertiesDescriptor.getPropertyText("targetParams"));
		properties.add(PropertiesDescriptor.getPropertyMethod("method"));
		properties.add(PropertiesDescriptor.getPropertyInt("timeIn"));
		properties.add(PropertiesDescriptor.getPropertyInt("timeOut"));
		properties.add(PropertiesDescriptor.getPropertyText("condition"));
		properties.add(PropertiesDescriptor.getPropertyText("keyValues"));
		properties.add(PropertiesDescriptor.getPropertyKClasses("kobjectShortClassName", getMoxFile()));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.DELETE_ONE;
	}

	private KAjaxDeleteOne getAjaxDeleteOne() {
		return (KAjaxDeleteOne) ajaxObject;
	}

	@Override
	public void setURL(String newURL) {
		setURL(newURL, false);
	}

	@Override
	public String getURL() {
		return getAjaxDeleteOne().getVirtualURL();
	}

	@Override
	public void setURL(String newURL, boolean fromRequest) {
		String oldURL = getURL();
		if (!newURL.equals(oldURL)) {
			getAjaxDeleteOne().setVirtualURL(newURL);
			updateProperty("virtualURL", oldURL, newURL);
			if (!fromRequest)
				updateConnection(newURL);
			else
				refreshSourceConnection();
		}
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		updateProperty("virtualURL", null, ((KAjaxDeleteOne) ajaxObject).getVirtualURL());
		addChilds(((KAjaxDeleteOne) ajaxObject).getChilds());
	}

}
