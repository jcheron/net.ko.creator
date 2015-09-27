package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.IHasURL;
import net.ko.mapping.KAjaxIncludeDialog;
import net.ko.mapping.KAjaxObject;
import net.ko.mapping.KAjaxWithChilds;

public abstract class AjaxObject extends Node {
	private static final long serialVersionUID = 1L;
	protected transient IAjaxObject ajaxObject;

	public AjaxObject() {
		super();
		defaultAppearence = AppearenceConfig.defaultAjaxObject();
	}

	public IAjaxObject getAjaxObject() {
		return ajaxObject;
	}

	public void setAjaxObject(IAjaxObject ajaxObject) {
		this.ajaxObject = ajaxObject;
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		setName(ajaxObject.getDisplayCaption());
		getListeners().firePropertyChange(id, oldValue, value);
	}

	@Override
	public Object getXmlObject() {
		return ajaxObject;
	}

	@Override
	public List<Object> getXmlChildren() {
		if (ajaxObject instanceof KAjaxWithChilds)
			return new ArrayList<Object>(((KAjaxWithChilds) ajaxObject).getChilds());
		return new ArrayList<>();
	}

	@Override
	public boolean removeXmlChild(Object child) {
		if (ajaxObject instanceof KAjaxWithChilds)
			return ((KAjaxWithChilds) ajaxObject).getChilds().remove(child);
		return false;
	}

	@Override
	public void addXmlChild(Object element) {
		if (element instanceof KAjaxObject)
			if (ajaxObject instanceof KAjaxWithChilds)
				((KAjaxWithChilds) ajaxObject).addChild((KAjaxObject) element);
	}

	public void addConnections() {
		if (ajaxObject instanceof IHasURL) {
			if (!(ajaxObject instanceof KAjaxIncludeDialog)) {
				IHasURL sourceObject = ((IHasURL) ajaxObject);
				Node targetNode = getRoot().getAjaxRequest(sourceObject.getURL());
				if (targetNode != null) {
					Connection conn = new Connection(this, targetNode);
					addConnection(conn);
					targetNode.addConnection(conn);
				}
			}
		}
	}

	public void updateConnection(String newURL) {
		if (ajaxObject instanceof IHasURL) {
			if (!(ajaxObject instanceof KAjaxIncludeDialog)) {
				Connection conn = null;
				Node targetNode = getRoot().getAjaxRequest(newURL);
				if (sourceConnections.size() > 0) {
					conn = sourceConnections.get(0);
				}
				else
					conn = new Connection(this, targetNode);

				if (targetNode != null) {
					if (sourceConnections.contains(conn)) {
						conn.reconnect(this, targetNode);
					}
					else {
						conn.connect(this, targetNode);
					}
				} else {
					conn.disconnect();
				}
			}
		}
	}

	public void refreshSourceConnection() {
		if (sourceConnections.size() > 0) {
			Connection conn = sourceConnections.get(0);
			conn.forceRefresh();
		}
	}

	@Override
	public void setXmlObject(Object o) {
		if (o instanceof IAjaxObject)
			setAjaxObject((IAjaxObject) o);

	}

	@Override
	public String getTooltipMessage() {
		return ajaxObject.getName();
	}
}
