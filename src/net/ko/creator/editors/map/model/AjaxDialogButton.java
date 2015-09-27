package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxDialogButton;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxDialogButton extends AjaxObject {

	private static final long serialVersionUID = 1L;

	public AjaxDialogButton() {
		super();
		addToogle();
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
		addChilds(((KAjaxDialogButton) ajaxObject).getChilds());
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxDialogButton ajaxDialogButton = (KAjaxDialogButton) ajaxObject;
		Object result = "";
		switch (id) {
		case "caption":
			result = ajaxDialogButton.getCaption();
			break;
		case "id":
			result = ajaxDialogButton.getId();
			break;
		case "keyCode":
			result = ajaxDialogButton.getKeyCode();
			break;
		case "glyphIcon":
			ajaxDialogButton.getGlyphIcon();
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxDialogButton ajaxDialogButton = (KAjaxDialogButton) ajaxObject;
		switch (id) {
		case "caption":
			ajaxDialogButton.setCaption(value + "");
			updateProperty(Node.PROPERTY_DISPLAY, oldValue, value);
			break;
		case "id":
			ajaxDialogButton.setId(value + "");
			break;
		case "keyCode":
			ajaxDialogButton.setKeyCode(Integer.valueOf(value + ""));
			break;
		case "glyphIcon":
			ajaxDialogButton.setGlyphIcon(value + "");
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyText("caption"));
		properties.add(PropertiesDescriptor.getPropertyText("id"));
		properties.add(PropertiesDescriptor.getPropertyInt("keyCode"));
		properties.add(PropertiesDescriptor.getPropertyText("glyphIcon"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.DIALOG_BUTTON;
	}

}
