package net.ko.creator.editors.map.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.ko.creator.editors.map.figure.AjaxRequestFigure;
import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.policies.AppConnectionPolicy;
import net.ko.creator.editors.map.policies.AppDeletePolicy;
import net.ko.creator.editors.map.policies.AppEditLayoutPolicies;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.SWT;

public class AjaxRequestPart extends AppAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		figure = new AjaxRequestFigure();
		ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setAntialias(SWT.ON);
		setFigureAppearence(figure);
		refreshParameters();
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
		AjaxRequestFigure figure = (AjaxRequestFigure) getFigure();
		AjaxRequest model = (AjaxRequest) getModel();
		figure.setCaption(model.getName());
		figure.setLayout(model.getLayout());
		super.refreshVisuals();
	}

	public List<Node> getModelChildren() {
		AjaxRequest model = (AjaxRequest) getModel();
		return model.getChildrenArray();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		String prop = evt.getPropertyName();
		if (prop.equals("requestURL"))
			refreshVisuals();
		if (evt.getPropertyName().equals(Node.TARGET_CONNECTION))
			refreshTargetConnections();
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
