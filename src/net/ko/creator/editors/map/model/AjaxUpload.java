package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxUpload;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxUpload extends AjaxObject {

	private static final long serialVersionUID = 1L;

	public AjaxUpload() {
		super();
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		KAjaxUpload ajaxUpload = (KAjaxUpload) ajaxObject;
		setName(ajaxUpload.getDisplayCaption());
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxUpload ajaxUpload = (KAjaxUpload) ajaxObject;
		Object result = "";
		switch (id) {

		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		KAjaxUpload ajaxUpload = (KAjaxUpload) ajaxObject;

	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyAllURLs("targetURL", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyText("targetId"));
		properties.add(PropertiesDescriptor.getPropertyMethod("method"));
		properties.add(PropertiesDescriptor.getPropertyText("targetParams"));
		properties.add(PropertiesDescriptor.getPropertyText("targetFunction"));
		properties.add(PropertiesDescriptor.getPropertyText("interval"));
		properties.add(PropertiesDescriptor.getPropertyTransition("transition"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.BULLET;
	}

}
