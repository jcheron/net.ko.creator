package net.ko.creator.editors.map.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.figure.ConnexionFigure;
import net.ko.creator.editors.map.model.AjaxObject;
import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.policies.AppConnectionBendPointEditPolicy;
import net.ko.creator.editors.map.policies.AppConnectionDeleteEditPolicy;
import net.ko.creator.editors.map.policies.ContainerHighlightEditPolicy;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.graphics.Image;

public class ConnectionPart extends AppAbstractConnectionEditPart {

	protected IFigure createFigure() {
		Connection model = (Connection) getModel();
		ConnexionFigure figure = new ConnexionFigure(model.getCaption());
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new AppConnectionDeleteEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new AppConnectionBendPointEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		switch (propName) {
		case Node.SOURCE_CONNECTION_UPDATED:
			refreshSourceAnchor();
			break;
		case "caption":
		case "visible":
		case "captionVisible":
		case "refresh":
			refreshVisuals();
			break;
		case Node.VISUAL_PARAMETERS:
			refreshParameters();
			break;
		default:
			break;
		}
	}

	@Override
	public Image getImage() {
		return Images.getImage(getObjectModel().getImage());
	}

	@Override
	public String getCaption() {
		return getObjectModel().getName();
	}

	private AjaxObject getObjectModel() {
		return (AjaxObject) ((Connection) getModel()).getSourceNode();
	}

	private ConnexionFigure getConnexionFigure() {
		return (ConnexionFigure) figure;
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		ConnexionFigure figure = getConnexionFigure();
		Connection conn = (Connection) getModel();
		figure.setCaption(conn.getCaption());
		figure.setTooltipText(conn.getCaption());
		figure.setVisible(conn.isVisible());
		figure.setCaptionVisible(conn.isCaptionVisible());
		List<Point> modelConstraint = ((Connection) getModel()).getBendpoints();
		List<AbsoluteBendpoint> figureConstraint = new ArrayList<AbsoluteBendpoint>();
		for (Point p : modelConstraint) {
			figureConstraint.add(new AbsoluteBendpoint(p));
		}
		figure.setRoutingConstraint(figureConstraint);
	}

	public void refreshParameters() {
		ConnexionFigure figure = getConnexionFigure();
		figure.refreshParameters();
	}

	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		if (value == 0) {
			((Connection) getModel()).getSourceNode().getListeners().firePropertyChange(Node.SELECT, null, false);
			((Connection) getModel()).getTargetNode().getListeners().firePropertyChange(Node.SELECT, null, false);
			getConnexionFigure().select(false);
		} else {
			((Connection) getModel()).getSourceNode().getListeners().firePropertyChange(Node.SELECT, null, true);
			((Connection) getModel()).getTargetNode().getListeners().firePropertyChange(Node.SELECT, null, true);
			getConnexionFigure().select(true);
		}
	}

}
