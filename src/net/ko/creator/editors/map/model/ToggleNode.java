package net.ko.creator.editors.map.model;

import java.util.List;

import net.ko.creator.editors.map.NodePropertySource;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class ToggleNode extends Node {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getPropertyValue(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getXmlObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getXmlChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeXmlChild(Object child) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addXmlChild(Object element) {
		// TODO Auto-generated method stub

	}

	public void setExpanded(boolean expanded) {
		if (parent != null)
			parent.setExpanded(expanded);
	}

	public void expandCollapse() {
		expanded = !expanded;
		parent.setExpanded(expanded);
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			if (propertySource == null || propertySourceUpdated.contains(this.getClass())) {
				propertySource = new NodePropertySource(this.parent);
				propertySourceUpdated.remove(this.getClass());
			}
			return propertySource;
		}
		return null;
	}

	@Override
	public void setXmlObject(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTooltipMessage() {
		return "";
	}
}
