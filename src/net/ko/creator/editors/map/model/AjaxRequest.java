package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.WorkbenchUtils;
import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxJs;
import net.ko.mapping.KAjaxRequest;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxRequest extends Node {
	private static final long serialVersionUID = 1L;
	private transient KAjaxRequest ajaxRequest;

	public AjaxRequest() {
		super();
		defaultAppearence = AppearenceConfig.defaultRequest();
		addToogle();
	}

	public KAjaxRequest getAjaxRequest() {
		return ajaxRequest;
	}

	public void setAjaxRequest(KAjaxRequest ajaxRequest) {
		this.ajaxRequest = ajaxRequest;
		setName(ajaxRequest.getRequestURL());
		for (IAjaxObject ajaxObj : ajaxRequest.getAjaxIncludes()) {
			JS js = new JS();
			addChild(js);
			js.setAjaxJs((KAjaxJs) ajaxObj);
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyURLs("requestURL", getMoxFile()));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(String id) {
		String result = "";
		if ("requestURL".equals(id)) {
			result = ajaxRequest.getRequestURL();
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		if ("requestURL".equals(id)) {
			setRequestURL(value + "");
		}
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		// TODO Auto-generated method stub

	}

	public void setRequestURL(String newURL) {
		String oldValue = ajaxRequest.getRequestURL();
		if (!newURL.equals(oldValue)) {
			try {
				IMarker marker = ((IResource) WorkbenchUtils.getActiveEditor().getEditorInput().getAdapter(IResource.class)).createMarker(IMarker.PROBLEM);
				marker.setAttribute(IMarker.MESSAGE, "This a a task");
				marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
				marker.setAttribute(IMarker.MARKER, IMarker.SEVERITY_ERROR);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Connection target : targetConnections) {
				if (target.getSourceNode() instanceof IHasConnectorURL) {
					IHasConnectorURL targetNodeURL = (IHasConnectorURL) target.getSourceNode();
					if (match(targetNodeURL.getURL()))
						targetNodeURL.setURL(newURL, true);
				}
			}
			ajaxRequest.setRequestURL(newURL);
			setName(newURL);
			getListeners().firePropertyChange("requestURL", oldValue, newURL);
			refreshChildren(newURL);
		}
	}

	public boolean match(String url) {
		return ajaxRequest.requestURLMatches(url);
	}

	@Override
	public String getImage() {
		return Images.REQUEST;
	}

	@Override
	public Object getXmlObject() {
		return ajaxRequest;
	}

	@Override
	public List<Object> getXmlChildren() {
		return new ArrayList<Object>(ajaxRequest.getAjaxIncludes());
	}

	@Override
	public boolean removeXmlChild(Object child) {
		return ajaxRequest.getAjaxIncludes().remove(child);
	}

	@Override
	public void addXmlChild(Object element) {
		if (element instanceof KAjaxJs)
			ajaxRequest.addElement((KAjaxJs) element);
	}

	public void refreshChildren() {
		refreshChildren(ajaxRequest.getRequestURL());
	}

	public void refreshChildren(String newURL) {
		List<Node> childs = getMoxFile().getAllChildrenArray();
		for (Node n : childs) {
			if (n instanceof IHasConnectorURL) {
				IHasConnectorURL childURL = (IHasConnectorURL) n;
				if (match(childURL.getURL()))
					((AjaxObject) childURL).updateConnection(newURL);
			}
		}
	}

	@Override
	public boolean addChildAndFire(Node child) {
		boolean result = false;
		Node toAdd = child;
		if (child instanceof JS) {
			if (children.contains(child)) {
				toAdd = children.get(children.indexOf(child));
				for (Node n : child.getChildrenArray()) {
					if (!(n instanceof ToggleNode))
						toAdd.addChildAndFire(n);
				}
				result = true;
			}
		}
		if (!result) {
			result = this.children.add(toAdd);
			if (result) {
				toAdd.setParent(this);
				addXmlChild(toAdd.getXmlObject());
				autoSize();
				getListeners().firePropertyChange(PROPERTY_ADD, null, toAdd);
			}
		}
		return result;
	}

	@Override
	public void setXmlObject(Object o) {
		if (o instanceof KAjaxRequest)
			setAjaxRequest((KAjaxRequest) o);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		if (ajaxRequest != null)
			return ajaxRequest.equals(((AjaxRequest) obj).getAjaxRequest());
		else
			return false;
	}

	@Override
	public int hashCode() {
		if (ajaxRequest != null)
			return ajaxRequest.hashCode();
		return super.hashCode();
	}

	@Override
	public String getTooltipMessage() {
		return ajaxRequest.getName();
	}
}
