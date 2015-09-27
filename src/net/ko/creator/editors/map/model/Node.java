package net.ko.creator.editors.map.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ko.creator.editors.map.NodePropertySource;
import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;
import net.ko.mapping.IAjaxObject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public abstract class Node implements Serializable, IAdaptable {

	private static final long serialVersionUID = 1L;
	protected transient String name;
	protected String id;
	protected Rectangle layout;
	protected boolean visible;
	protected boolean expanded;
	protected AppearenceConfig appearenceConfig;
	protected transient AppearenceConfig defaultAppearence;
	protected List<Node> children;
	protected transient Node parent;
	protected transient int deletedIndex;
	protected transient ToggleNode toggleNode;
	protected transient boolean toRemove;
	protected transient PropertyChangeSupport listeners;
	protected List<Connection> sourceConnections;
	protected List<Connection> targetConnections;
	public static final String PROPERTY_LAYOUT = "NodeLayout";
	public static final String PROPERTY_ADD = "NodeAddChild";
	public static final String PROPERTY_REMOVE = "NodeRemoveChild";
	public static final String PROPERTY_VISIBLE = "NodeVisible";
	public static final String PROPERTY_DISPLAY = "DisplayCaption";
	public static final String PROPERTY_APPEARENCE = "appearence";
	public static final String PROPERTY_DISPLAY_PARENT = "DisplayCaptionParent";
	public static final String PROPERTY_EXPANDED = "NodeExpanded";
	public static final String SOURCE_CONNECTION = "SourceConnectionAdded";
	public static final String SOURCE_CONNECTION_UPDATED = "SourceConnectionUpdated";
	public static final String TARGET_CONNECTION = "TargetConnectionAdded";
	public static final String VISUAL_PARAMETERS = "visualParameters";
	public static final String URL_CHANGE = "TargetURLChange";
	public static final String HIGH_LIGHT = "highlight";
	public static final String SELECT = "select";
	public static final String SET_FOCUS = "setFocus";

	protected transient IPropertySource propertySource = null;
	protected final static transient List<Class<? extends Node>> propertySourceUpdated = new ArrayList<>();

	public Node() {
		this.name = "Unknown";
		this.layout = new Rectangle(10, 10, 100, 100);
		this.children = new ArrayList<Node>();
		this.parent = null;
		this.listeners = new PropertyChangeSupport(this);
		this.id = null;
		this.visible = true;
		this.expanded = true;
		this.toRemove = false;
		sourceConnections = new ArrayList<>();
		targetConnections = new ArrayList<>();
		appearenceConfig = new AppearenceConfig();
		defaultAppearence = new AppearenceConfig();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setLayout(Rectangle layout) {
		Rectangle oldLayout = this.layout;
		this.layout = layout;
		getListeners().firePropertyChange(PROPERTY_LAYOUT, oldLayout, layout);
	}

	public void autoSize() {
		Rectangle newLayout = layout.getCopy();
		newLayout.setHeight(-1);
		newLayout.setWidth(-1);
		setLayout(newLayout);
	}

	public Rectangle getLayout() {
		return this.layout;
	}

	public boolean addChild(Node child) {
		return addChild(child, children.size());
	}

	public boolean addChild(Node child, int index) {
		return _addChild(child, index);
	}

	protected boolean _addChild(Node child, int index) {
		boolean result = (index <= children.size() && index >= 0);
		if (result) {
			this.children.add(index, child);
			if (result) {
				child.setParent(this);
			}
		}
		return result;
	}

	public AjaxObject getFirstChild(Class<? extends AjaxObject> clazz) {
		for (Node child : children) {
			if (child.getClass().equals(clazz))
				return (AjaxObject) child;
		}
		return null;
	}

	public boolean replaceFirstChild(AjaxObject newChild) {
		boolean result = false;
		AjaxObject objToReplace = getFirstChild(newChild.getClass());
		if (objToReplace != null) {
			int index = children.indexOf(objToReplace);
			children.remove(index);
			_addChild(newChild, index);
			result = true;
		} else
			_addChild(newChild, children.size());
		return result;
	}

	public boolean addChildAndFire(Node child) {
		return addChildAndFire(child, children.size());
	}

	public boolean addChildAndFire(Node child, int index) {
		boolean result = addChild(child, index);
		if (result) {
			addXmlChild(child.getXmlObject());
			if (!(this instanceof MoxFile)) {
				AjaxRequest ajaxRequest = getBaseRequest();
				if (ajaxRequest != null)
					ajaxRequest.autoSize();
			}
			if (child instanceof AjaxIncludeDialog)
				((AjaxIncludeDialog) child).updateXmlChildren();
			getListeners().firePropertyChange(PROPERTY_ADD, null, child);
		}
		return result;
	}

	public boolean removeChild(Node child) {
		boolean result = this.children.remove(child);
		if (result) {
			child.setToRemove(true);
			removeXmlChild(child.getXmlObject());
			getListeners().firePropertyChange(PROPERTY_REMOVE, child, null);
		}
		return result;
	}

	public List<Node> getChildrenArray() {
		return this.children;
	}

	public int childCount() {
		return this.children.size();
	}

	public List<Node> getAllChildrenArray() {
		List<Node> result = new ArrayList<>();
		result.addAll(this.children);
		if (this.children != null) {
			for (Node n : this.children) {
				List<Node> nodes = n.getAllChildrenArray();
				if (nodes != null)
					result.addAll(nodes);
			}
		}
		return result;
	}

	public void setParent(Node parent) {
		this.parent = parent;
		if (parent != null) {
			setId(parent.getId() + "-" + parent.childCount());
		}
	}

	public Node getParent() {
		return this.parent;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Node getNodeById(String id) {
		return getNodeById(id, this);
	}

	private Node getNodeById(String id, Node parentNode) {
		Node result = null;
		for (Node n : parentNode.getChildrenArray()) {
			if (n.getId() != null && n.getId().equals(id)) {
				result = n;
				break;
			}
			result = getNodeById(id, n);
			if (result != null)
				break;
		}
		return result;
	}

	public void addChilds(List<IAjaxObject> ajaxObjects) {
		for (IAjaxObject ajaxObject : ajaxObjects) {
			String cls = ajaxObject.getClass().getSimpleName();
			AjaxObject model = null;
			switch (cls) {
			case "KAjaxInclude":
				model = new AjaxInclude();
				break;
			case "KAjaxMessage":
				model = new AjaxMessage();
				break;
			case "KAjaxSelector":
				model = new AjaxSelector();
				break;
			case "KAjaxEvent":
				model = new AjaxFireEvent();
				break;
			case "KAjaxDeleteOne":
				model = new AjaxDeleteOne();
				break;
			case "KAjaxDeleteMulti":
				model = new AjaxDeleteMulti();
				break;
			case "KAjaxUpdateOne":
				model = new AjaxUpdateOne();
				break;
			case "KAjaxUpdateOneField":
				model = new AjaxUpdateOneField();
				break;
			case "KAjaxFunction":
				model = new AjaxFunction();
				break;
			case "KAjaxSubmitForm":
				model = new AjaxSubmitForm();
				break;
			case "KAjaxRefreshControl":
				model = new AjaxRefreshControl();
				break;
			case "KAjaxRefreshFormValues":
				model = new AjaxRefreshFormValues();
				break;
			case "KAjaxAccordion":
				model = new AjaxAccordion();
				break;
			case "KAjaxMessageDialog":
				model = new AjaxMessageDialog();
				break;
			case "KAjaxDialogButton":
				model = new AjaxDialogButton();
				break;
			case "KAjaxIncludeDialog":
				model = new AjaxIncludeDialog();
				break;
			case "KCssTransition":
				model = new CssTransition();
				break;
			case "KCssOneTransition":
				model = new CssOneTransition();
				break;
			case "KAjaxShowHide":
				model = new AjaxShowhide();
				break;
			default:
				break;
			}
			if (model != null) {
				model.setAjaxObject(ajaxObject);
				addChild(model);
			}
		}
	}

	public boolean contains(Node child) {
		return children.contains(child);
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			if (propertySource == null || propertySourceUpdated.contains(this.getClass())) {
				propertySource = new NodePropertySource(this);
				propertySourceUpdated.remove(this.getClass());
			}
			return propertySource;
		}
		return null;
	}

	public abstract Object getPropertyValue(String id);

	public abstract void setPropertyValue(String id, Object value);

	public abstract IPropertyDescriptor[] getPropertyDescriptors();

	protected abstract void updateProperty(String id, Object oldValue, Object value);

	public MoxFile getMoxFile() {
		return getMoxFile(this);
	}

	protected MoxFile getMoxFile(Node node) {
		if (node instanceof MoxFile)
			return (MoxFile) node;
		return getMoxFile(node.getParent());
	}

	public AjaxRequest getBaseRequest() {
		return getBaseRequest(this);
	}

	protected AjaxRequest getBaseRequest(Node node) {
		if (node instanceof AjaxRequest)
			return (AjaxRequest) node;
		return getBaseRequest(node.getParent());
	}

	public abstract String getImage();

	public abstract Object getXmlObject();

	public abstract void setXmlObject(Object o);

	public abstract List<Object> getXmlChildren();

	public abstract boolean removeXmlChild(Object child);

	public abstract void addXmlChild(Object element);

	public static List<Class<?>> getPossibleClassChildren(String className) {
		List<Class<?>> classes = new ArrayList<>();
		switch (className) {
		case "AjaxInclude":
		case "AjaxMessage":
		case "AjaxFireEvent":
		case "AjaxSelector":
		case "AjaxUpdateOne":
		case "AjaxDeleteOne":
		case "AjaxDeleteMulti":
		case "AjaxFunction":
		case "AjaxShowhide":
		case "AjaxSubmitForm":
		case "AjaxRefreshControl":
		case "AjaxRefreshFormValues":
		case "AjaxAccordion":
		case "AjaxMessageDialog":
		case "AjaxIncludeDialog":
		case "CssTransition":
			classes.addAll(Arrays.asList(new Class<?>[] { JS.class, AjaxInclude.class, AjaxSelector.class, AjaxUpdateOne.class, AjaxDeleteOne.class, AjaxDeleteMulti.class, AjaxSubmitForm.class, AjaxRefreshFormValues.class, AjaxDialogButton.class }));
			break;
		case "JS":
			classes.add(AjaxRequest.class);
			break;
		case "CssOneTransition":
			classes.add(CssTransition.class);
			break;
		case "AjaxUpdateOneField":
			classes.add(AjaxUpdateOne.class);
			break;

		default:
			break;
		}

		switch (className) {
		case "AjaxInclude":
		case "AjaxMessage":
			classes.add(AjaxAccordion.class);
			classes.add(AjaxIncludeDialog.class);
			break;
		case "AjaxDialogButton":
			classes = Arrays.asList(new Class<?>[] { AjaxMessageDialog.class, AjaxIncludeDialog.class });

		default:
			break;
		}
		if ("AjaxInclude".equals(className))
			classes.add(AjaxShowhide.class);
		return classes;
	}

	public static void addPropertySourceUpdated(Class<? extends Node>[] classes) {
		propertySourceUpdated.addAll(Arrays.asList(classes));
	}

	public boolean isVisible() {
		boolean result = visible;
		if (!result)
			return result;
		Node parentNode = this;
		do {
			parentNode = parentNode.parent;
			if (parentNode != null)
				result = result && parentNode.isVisible();
		} while (parentNode != null && result);
		return result;
	}

	public void setVisible(boolean visible) {
		if (visible != this.visible) {
			this.visible = visible;
			getListeners().firePropertyChange(PROPERTY_VISIBLE, !visible, visible);
		}
	}

	public void setVisible(boolean visible, boolean recursive) {
		this.visible = visible;
		getListeners().firePropertyChange(PROPERTY_VISIBLE, !visible, visible);
		if (recursive) {
			for (Node child : children) {
				child.setVisible(visible, recursive);
			}
		}
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		if (expanded != this.expanded) {
			this.expanded = expanded;
			getListeners().firePropertyChange(PROPERTY_EXPANDED, !expanded, expanded);
			fireChildrenSourceConnectionChange();
		}
	}

	public boolean hasChildren() {
		int result = children.size();
		if (toggleNode != null)
			result--;
		return result > 0;

	}

	public boolean isCollapsedFromParent() {
		boolean result = false;
		Node parentNode = this;
		do {
			parentNode = parentNode.parent;
			if (parentNode != null)
				result = !parentNode.isExpanded();
		} while (parentNode != null && !result);
		return result;
	}

	public boolean addConnection(Connection conn) {
		if (conn.getSourceNode() == this) {
			if (!sourceConnections.contains(conn)) {
				if (sourceConnections.add(conn)) {
					conn.setId(id);
					getListeners().firePropertyChange(SOURCE_CONNECTION, null, conn);
					return true;
				}
				return false;
			}
		}
		else if (conn.getTargetNode() == this) {
			if (!targetConnections.contains(conn)) {
				if (targetConnections.add(conn)) {
					getListeners().firePropertyChange(TARGET_CONNECTION, null, conn);
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public boolean removeConnection(Connection conn) {
		if (conn.getSourceNode() == this) {
			if (sourceConnections.contains(conn)) {
				if (sourceConnections.remove(conn)) {
					getListeners().firePropertyChange(SOURCE_CONNECTION, null, conn);
					return true;
				}
				return false;
			}
		}
		else if (conn.getTargetNode() == this) {
			if (targetConnections.contains(conn)) {
				if (targetConnections.remove(conn)) {
					getListeners().firePropertyChange(TARGET_CONNECTION, null, conn);
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public List<Connection> getSourceConnectionsArray() {
		return this.sourceConnections;
	}

	public List<Connection> getTargetConnectionsArray() {
		return this.targetConnections;
	}

	public void fireChildrenSourceConnectionChange() {
		for (Node child : children) {
			if (child instanceof AjaxObject || child instanceof JS) {
				Connection conn = child.getConnectionSource();
				if (conn != null) {
					child.getListeners().firePropertyChange(SOURCE_CONNECTION_UPDATED, null, conn);
				}
				child.fireChildrenSourceConnectionChange();
			}
		}
	}

	public void fireRefreshChildren() {
		getListeners().firePropertyChange(Node.PROPERTY_ADD, null, true);
	}

	public void fireRefreshVisuals() {
		getListeners().firePropertyChange(Node.PROPERTY_LAYOUT, null, true);
	}

	public void fireRefreshVisuals(boolean recursive) {
		getListeners().firePropertyChange(Node.PROPERTY_LAYOUT, null, true);
		if (recursive) {
			for (Node n : children)
				n.fireRefreshVisuals(true);
		}
	}

	public void refreshParameters() {
		getListeners().firePropertyChange(Node.VISUAL_PARAMETERS, null, true);
	}

	public void refreshParameters(boolean recursive) {
		getListeners().firePropertyChange(Node.VISUAL_PARAMETERS, null, true);
		if (getConnectionSource() != null)
			getConnectionSource().getListeners().firePropertyChange(Node.VISUAL_PARAMETERS, null, true);
		if (recursive) {
			for (Node n : children) {
				if (!(n instanceof ToggleNode))
					n.refreshParameters(true);
			}
		}
	}

	public Connection getConnectionSource() {
		if (sourceConnections.size() > 0)
			return sourceConnections.get(0);
		return null;
	}

	public List<Node> getRelatedNodes() {
		List<Node> result = new ArrayList<>();
		Connection connSource = getConnectionSource();
		if (connSource != null)
			result.add(connSource.getTargetNode().getBaseRequest());
		for (Connection conn : getTargetConnectionsArray()) {
			result.add(conn.getSourceNode().getBaseRequest());
		}
		for (Node child : children)
			result.addAll(child.getRelatedNodes());
		return result;
	}

	public MoxFile getRoot() {
		Node result = this;
		if (result instanceof MoxFile)
			return (MoxFile) result;
		boolean flag = false;
		do {
			result = result.parent;
			flag = result instanceof MoxFile;
		} while (!flag && result != null);
		return (MoxFile) result;
	}

	protected void addToogle() {
		toggleNode = new ToggleNode();
		addChild(toggleNode);
	}

	public void highlight(Boolean highlight) {
		getListeners().firePropertyChange(Node.HIGH_LIGHT, null, highlight);
	}

	public void setDirty(boolean dirty) {
		getMoxFile().setDirty(dirty);
	}

	public int getDeletedIndex() {
		return deletedIndex;
	}

	public void setDeletedIndex(int deletedIndex) {
		this.deletedIndex = deletedIndex;
	}

	public void setFocus() {
		getListeners().firePropertyChange(Node.SET_FOCUS, null, true);
	}

	public boolean isToRemove() {
		return toRemove;
	}

	public void setToRemove(boolean toRemove) {
		this.toRemove = toRemove;
	}

	public abstract String getTooltipMessage();

	public boolean hasAppearence() {
		return appearenceConfig.isEnabled();
	}

	public AppearenceConfig getAppearenceConfig() {
		if (appearenceConfig.isEnabled())
			return appearenceConfig;
		else
			return defaultAppearence;
	}

	public void setAppearenceConfig(AppearenceConfig appearenceConfig) {
		this.appearenceConfig = appearenceConfig;
	}
}
