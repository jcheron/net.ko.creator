package net.ko.creator.properties;

import net.ko.bean.ZTypedRegion;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ZoneElementProperties implements IPropertySource {
	private ZTypedRegion element;
	private Object PropertiesTable[][];
	public ZoneElementProperties(ZTypedRegion element){
		super();
		this.element=element;
		PropertiesTable=element.getZone().getPropertyDescriptor();
	}
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(Object propertyName) {
		return element.getPropertyValue((String)propertyName);
	}

	@Override
	public boolean isPropertySet(Object propertyName) {
		return true;
	}

	@Override
	public void resetPropertyValue(Object propertyName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object propertyName, Object value) {
		element.setPropertyValue((String)propertyName, (String)value);

	}
	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// Create the property vector.
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[PropertiesTable.length];

		for (int i=0;i<PropertiesTable.length;i++) {
			PropertyDescriptor descriptor;

			descriptor = (PropertyDescriptor)PropertiesTable[i][1];
			propertyDescriptors[i] = (IPropertyDescriptor)descriptor;
			descriptor.setCategory("General");
		}
		return propertyDescriptors;

	}

}
