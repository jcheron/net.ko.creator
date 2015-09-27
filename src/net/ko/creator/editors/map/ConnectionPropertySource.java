package net.ko.creator.editors.map;

import net.ko.creator.editors.map.model.Connection;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class ConnectionPropertySource implements IPropertySource {
	protected Connection model;

	public ConnectionPropertySource(Connection model) {
		super();
		this.model = model;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return model.getPropertyDescriptors();
	}

	@Override
	public Object getPropertyValue(Object id) {
		return model.getPropertyValue(id + "");
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		model.setPropertyValue(id + "", value);
	}

}
