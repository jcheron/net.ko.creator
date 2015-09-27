package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxMessageDialog;
import net.ko.utils.KString;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxMessageDialog extends AjaxObject {

	private static final long serialVersionUID = 1L;

	public AjaxMessageDialog() {
		super();
		addToogle();
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
		addChilds(((KAjaxMessageDialog) ajaxObject).getAjaxButtons());
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxMessageDialog ajaxMessageDialog = (KAjaxMessageDialog) ajaxObject;
		Object result = "";
		switch (id) {
		case "message":
			result = ajaxMessageDialog.getMessage();
			break;
		case "title":
			result = ajaxMessageDialog.getTitle();
			break;
		case "modal":
			result = (ajaxMessageDialog.isModal()) ? 1 : 0;
			break;
		case "condition":
			result = ajaxMessageDialog.getCondition();
			break;
		case "transition":
			result = ajaxMessageDialog.getTransition();
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxMessageDialog ajaxMessageDialog = (KAjaxMessageDialog) ajaxObject;
		switch (id) {
		case "message":
			ajaxMessageDialog.setMessage(value + "");
			break;
		case "title":
			ajaxMessageDialog.setTitle(value + "");
			updateProperty(Node.PROPERTY_DISPLAY, oldValue, value);
			break;
		case "modal":
			ajaxMessageDialog.setModal(KString.isBooleanTrue(value + ""));
			break;
		case "condition":
			ajaxMessageDialog.setCondition(value + "");
			break;
		case "transition":
			ajaxMessageDialog.setTransition(value + "");
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyMultiLineText("message", "Saisie de message", "Entrer le message à afficher"));
		properties.add(PropertiesDescriptor.getPropertyText("title"));
		properties.add(PropertiesDescriptor.getPropertyBoolean("modal"));
		properties.add(PropertiesDescriptor.getPropertyTransition("transition"));
		properties.add(PropertiesDescriptor.getPropertyText("condition"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.MESSAGE_DIALOG;
	}

}
