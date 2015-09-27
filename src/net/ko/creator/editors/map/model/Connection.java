package net.ko.creator.editors.map.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.map.ConnectionPropertySource;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.utils.KString;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class Connection implements IAdaptable, Serializable {

	private static final long serialVersionUID = 1L;
	protected transient Node sourceNode;
	protected transient Node targetNode;
	protected String id;
	protected String caption;
	protected boolean visible;
	protected boolean captionVisible;
	protected transient PropertyChangeSupport listeners;
	protected transient IPropertySource propertySource = null;
	protected final List<Point> bendpoints = new ArrayList<>();
	protected final static transient List<Class<? extends Node>> propertySourceUpdated = new ArrayList<>();

	public Connection() {
		this.listeners = new PropertyChangeSupport(this);
		visible = true;
		captionVisible = false;
	}

	public Connection(Node sourceNode, Node targetNode) {
		this();
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public void connect() {
		sourceNode.addConnection(this);
		targetNode.addConnection(this);
	}

	public void connect(Node sourceNode, Node targetNode) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		connect();
	}

	public void disconnect() {
		if (sourceNode != null)
			sourceNode.removeConnection(this);
		if (targetNode != null)
			targetNode.removeConnection(this);
	}

	public void reconnect(Node sourceNode, Node targetNode) {
		if (sourceNode == null || targetNode == null || sourceNode == targetNode) {
			throw new IllegalArgumentException();
		}
		disconnect();
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		connect();
	}

	public String getCaption() {
		String result = caption;
		if (KString.isNull(result)) {
			if (sourceNode != null)
				result = sourceNode.getName();
		}
		return result;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			if (propertySource == null || propertySourceUpdated.contains(this.getClass())) {
				propertySource = new ConnectionPropertySource(this);
				propertySourceUpdated.remove(this.getClass());
			}
			return propertySource;
		}
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyText("caption"));
		properties.add(PropertiesDescriptor.getPropertyBoolean("visible"));
		properties.add(PropertiesDescriptor.getPropertyBoolean("captionVisible"));
		properties.add(PropertiesDescriptor.getPropertyAllURLs("targetURL", sourceNode.getMoxFile()));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	public Object getPropertyValue(String id) {
		Object result = null;
		switch (id) {
		case "caption":
			result = getCaption();
			break;
		case "visible":
			result = (visible) ? 1 : 0;
			break;
		case "captionVisible":
			result = (captionVisible) ? 1 : 0;
			break;
		case "targetURL":
			result = "";
			if (sourceNode instanceof IHasConnectorURL) {
				result = ((IHasConnectorURL) sourceNode).getURL();
			}
			break;
		}
		return result;
	}

	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		switch (id) {
		case "caption":
			setCaption(value + "");
			break;
		case "visible":
			setVisible(KString.isBooleanTrue(value + ""));
			break;
		case "captionVisible":
			setCaptionVisible(KString.isBooleanTrue(value + ""));
			break;
		case "targetURL":
			if (sourceNode instanceof IHasConnectorURL) {
				((IHasConnectorURL) sourceNode).setURL(value + "");
				sourceNode.getListeners().firePropertyChange(Node.URL_CHANGE, null, value);
			}
			break;
		default:
			break;
		}
	}

	protected void updateProperty(String id, Object oldValue, Object value) {

	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public PropertyChangeSupport getListeners() {
		return listeners;
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	public boolean isVisible() {
		return visible && (sourceNode.isVisible() && targetNode.isVisible());
	}

	public void setVisible(boolean visible) {
		if (visible != this.visible) {
			this.visible = visible;
			getListeners().firePropertyChange("visible", !visible, visible);
		}
	}

	public void setCaption(String caption) {
		if (!caption.equals(this.caption)) {
			String oldValue = this.caption;
			this.caption = caption;
			getListeners().firePropertyChange("caption", oldValue, caption);
		}
	}

	public boolean isCaptionVisible() {
		return captionVisible && isVisible();
	}

	public void setCaptionVisible(boolean captionVisible) {
		if (captionVisible != this.captionVisible) {
			this.captionVisible = captionVisible;
			getListeners().firePropertyChange("captionVisible", !captionVisible, captionVisible);
		}
	}

	public void forceRefresh() {
		getListeners().firePropertyChange("refresh", null, true);
	}

	public List<Point> getBendpoints() {
		return bendpoints;
	}

	public void setBendPoints(List<Point> bendPoints) {
		this.bendpoints.clear();
		this.bendpoints.addAll(bendPoints);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDirty(boolean dirty) {
		if (sourceNode != null) {
			sourceNode.getMoxFile().setDirty(dirty);
		}
	}
}
