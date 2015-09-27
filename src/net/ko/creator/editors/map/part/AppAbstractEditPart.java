package net.ko.creator.editors.map.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.ko.creator.editors.map.figure.BaseFigure;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.policies.AppContainerEditPolicy;
import net.ko.creator.editors.map.policies.NodeRolePolicy;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public abstract class AppAbstractEditPart extends AbstractGraphicalEditPart
		implements PropertyChangeListener, IGeneralPart, NodeEditPart {
	@Override
	public void activate() {
		super.activate();
		((Node) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((Node) getModel()).removePropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Node.VISUAL_PARAMETERS))
			refreshParameters();
		if (evt.getPropertyName().equals(Node.PROPERTY_LAYOUT))
			refreshVisuals();
		if (evt.getPropertyName().equals(Node.PROPERTY_APPEARENCE))
			refreshAppearence();
		if (evt.getPropertyName().equals(Node.PROPERTY_DISPLAY))
			refreshVisuals();
		if (evt.getPropertyName().equals(Node.PROPERTY_VISIBLE)) {
			refreshVisuals();
			if (!(Boolean) evt.getNewValue())
				setSelected(0);
		}
		if (evt.getPropertyName().equals(Node.SET_FOCUS))
			setSelected(SELECTED_PRIMARY);
		if (evt.getPropertyName().equals(Node.PROPERTY_EXPANDED)) {
			refreshVisuals();
		}
		if (evt.getPropertyName().equals(Node.PROPERTY_ADD))
			refreshChildren();
		if (evt.getPropertyName().equals(Node.PROPERTY_REMOVE))
			refreshChildren();
		if (evt.getPropertyName().equals(Node.HIGH_LIGHT)) {
			((BaseFigure) figure).highlight((Boolean) evt.getNewValue());
		}
		if (evt.getPropertyName().equals(Node.SELECT)) {
			((BaseFigure) figure).select((Boolean) evt.getNewValue());
		}
	}

	@Override
	public Image getImage() {
		BaseFigure figure = (BaseFigure) getFigure();
		return figure.getImage();
	}

	@Override
	public String getCaption() {
		Node model = (Node) getModel();
		return model.getName();
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				page.showView(IPageLayout.ID_PROP_SHEET);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.NODE_ROLE, new NodeRolePolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new AppContainerEditPolicy());
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		Node model = (Node) getModel();
		BaseFigure bFigure = (BaseFigure) figure;
		figure.setVisible(model.isVisible());
		bFigure.setExpanded(model.isExpanded());
		bFigure.showCollapse(model.hasChildren());
		bFigure.setTooltipText(model.getTooltipMessage());
		model.setDirty(true);
	}

	protected void refreshAppearence() {
		BaseFigure bFigure = (BaseFigure) figure;
		bFigure.refreshAppearence();
	}

	@Override
	public void setSelected(int value) {
		BaseFigure basefigure = (BaseFigure) figure;
		if (value == 0 || (basefigure.isVisible() && !basefigure.isCollapsedFromParent()))
			super.setSelected(value);
	}

	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		Node model = (Node) getModel();
		((BaseFigure) figure).showCollapse(model.hasChildren());
		model.setDirty(true);
	}

	protected void refreshParameters() {
		((BaseFigure) figure).refreshParameters();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		BaseFigure figure = (BaseFigure) getFigure();
		figure = figure.getVisibleParent();
		return new ChopboxAnchor(figure);
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		BaseFigure figure = (BaseFigure) getFigure();
		figure = figure.getVisibleParent();
		return new ChopboxAnchor(figure);
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

	protected void setFigureAppearence(IFigure figure) {
		((BaseFigure) figure).setAppearenceConfig(((Node) getModel()).getAppearenceConfig());
	}
}
