package net.ko.creator.editors.map.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.ko.creator.editors.map.figure.AjaxObjectFigure;
import net.ko.creator.editors.map.figure.BaseFigure;
import net.ko.creator.editors.map.model.AjaxObject;
import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.policies.AppConnectionPolicy;
import net.ko.creator.editors.map.policies.AppDeletePolicy;
import net.ko.creator.editors.map.policies.AppEditLayoutPolicies;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.SWT;

public class AjaxObjectPart extends AppAbstractEditPart {
	protected String type;

	public AjaxObjectPart(String type) {
		super();
		this.type = type;
	}

	@Override
	protected IFigure createFigure() {
		figure = new AjaxObjectFigure(type);
		ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setAntialias(SWT.ON);
		setFigureAppearence(figure);
		refreshAppearence();
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new AppEditLayoutPolicies());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new AppDeletePolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new AppConnectionPolicy());
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		BaseFigure figure = (BaseFigure) getFigure();
		Node model = (Node) getModel();
		figure.setCaption(model.getName());
		figure.setLayout(model.getLayout());
	}

	public List<Node> getModelChildren() {
		Node model = (Node) getModel();
		return model.getChildrenArray();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		String prop = evt.getPropertyName();
		switch (prop) {
		case "triggerEvent":
		case "triggerId":
		case "targetId":
		case "targetURL":
		case "triggerSelector":
		case "targetSelector":
		case "event":
		case "kobjectShortClassName":
		case "virtualURL":
		case "operation":
		case "visible":
		case "name":
		case "controlType":
			refreshVisuals();
			break;

		default:
			break;
		}
		if (evt.getPropertyName().equals(Node.SOURCE_CONNECTION))
			refreshSourceConnections();
		if (evt.getPropertyName().equals(Node.SOURCE_CONNECTION_UPDATED)) {
			Connection conn = ((Node) getModel()).getConnectionSource();
			conn.getListeners().firePropertyChange(Node.SOURCE_CONNECTION_UPDATED, null, conn.getSourceNode());
		}
		if (evt.getPropertyName().equals(Node.URL_CHANGE)) {
			refreshVisuals();
		}
		if (evt.getPropertyName().equals(Node.PROPERTY_DISPLAY_PARENT))
			updateAndRefreshParameters();
	}

	protected void updateAndRefreshParameters() {
		Node model = (Node) getModel();
		model.setName(((AjaxObject) model).getAjaxObject().getDisplayCaption());
		refreshVisuals();
	}

	@Override
	public List getModelSourceConnections() {
		return ((Node) getModel()).getSourceConnectionsArray();
	}

	@Override
	public List getModelTargetConnections() {
		return ((Node) getModel()).getTargetConnectionsArray();
	}
}
