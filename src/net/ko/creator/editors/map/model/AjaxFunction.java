package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxFunction;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxFunction extends AjaxObject {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getPropertyValue(String id) {
		if (id.equals("script"))
			return ((KAjaxFunction) ajaxObject).getScript();
		return null;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		if (id.equals("script"))
			((KAjaxFunction) ajaxObject).setScript(value + "");

	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyMultiLineText("script", "Saisie de script", "Entrer le script javascript"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.FUNCTION;
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName("function");
	}

}
