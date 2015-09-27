package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxSubmitForm;
import net.ko.utils.KString;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxSubmitForm extends AjaxDeleteOne {

	private static final long serialVersionUID = 1L;

	public AjaxSubmitForm() {
		super();
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxSubmitForm ajaxSubmitForm = (KAjaxSubmitForm) ajaxObject;
		Object result = null;
		switch (id) {
		case "virtualURL":
			result = getURL();
			break;
		case "kobjectShortClassName":
			result = ajaxSubmitForm.getKobjectShortClassName();
			break;
		case "targetId":
			result = ajaxSubmitForm.getTargetId();
			break;
		case "method":
			result = ajaxSubmitForm.getMethod();
			break;
		case "targetParams":
			result = ajaxSubmitForm.getoTargetParams();
			break;
		case "targetFunction":
			result = ajaxSubmitForm.getTargetFunction();
			break;
		case "timeIn":
			result = ajaxSubmitForm.getTimein() + "";
			break;
		case "timeOut":
			result = ajaxSubmitForm.getTimeout() + "";
			break;
		case "formName":
			result = ajaxSubmitForm.getFormName();
			break;
		case "validation":
			result = (ajaxSubmitForm.isValidation()) ? 1 : 0;
			break;
		case "condition":
			result = ajaxSubmitForm.getoCondition();
			break;
		}
		if (result == null)
			result = "";
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxSubmitForm ajaxSubmitForm = (KAjaxSubmitForm) ajaxObject;
		switch (id) {
		case "targetId":
			ajaxSubmitForm.setTargetId(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "virtualURL":
			setURL(value + "");
			break;
		case "kobjectShortClassName":
			ajaxSubmitForm.setKobjectShortClassName(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "method":
			ajaxSubmitForm.setMethod(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "targetParams":
			ajaxSubmitForm.setoTargetParams(value + "");
			break;
		case "targetFunction":
			ajaxSubmitForm.setTargetFunction(value + "");
			break;
		case "timeIn":
			ajaxSubmitForm.setTimein(Integer.valueOf(value + ""));
			break;
		case "timeOut":
			ajaxSubmitForm.setTimeout(Integer.valueOf(value + ""));
			break;
		case "condition":
			ajaxSubmitForm.setoCondition(value + "");
			break;
		case "formName":
			ajaxSubmitForm.setFormName(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "validation":
			ajaxSubmitForm.setValidation(KString.isBooleanTrue(value + ""));
			break;
		default:
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> properties = new ArrayList<>();
		properties.add(PropertiesDescriptor.getPropertyAllURLs("virtualURL", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyText("targetId"));
		properties.add(PropertiesDescriptor.getPropertyText("targetFunction"));
		properties.add(PropertiesDescriptor.getPropertyText("targetParams"));
		properties.add(PropertiesDescriptor.getPropertyMethod("method"));
		properties.add(PropertiesDescriptor.getPropertyInt("timeIn"));
		properties.add(PropertiesDescriptor.getPropertyInt("timeOut"));
		properties.add(PropertiesDescriptor.getPropertyText("condition"));
		properties.add(PropertiesDescriptor.getPropertyKClasses("kobjectShortClassName", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyText("formName"));
		properties.add(PropertiesDescriptor.getPropertyBoolean("validation"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.SUBMIT_FORM;
	}

	public KAjaxSubmitForm getAjaxSubmitForm() {
		return (KAjaxSubmitForm) ajaxObject;
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		this.ajaxObject = ajaxObject;
		updateProperty("virtualURL", null, ((KAjaxSubmitForm) ajaxObject).getVirtualURL());
	}

	@Override
	public String getURL() {
		return getAjaxSubmitForm().getVirtualURL();
	}

	@Override
	public void setURL(String newURL, boolean fromRequest) {
		String oldURL = getURL();
		if (!newURL.equals(oldURL)) {
			getAjaxSubmitForm().setVirtualURL(newURL);
			updateProperty("virtualURL", oldURL, newURL);
			if (!fromRequest)
				updateConnection(newURL);
			else
				refreshSourceConnection();
		}
	}
}
