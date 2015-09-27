package net.ko.creator.editors.map.part;

import java.beans.PropertyChangeListener;

import net.ko.creator.editors.map.model.Connection;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public abstract class AppAbstractConnectionEditPart extends
		AbstractConnectionEditPart implements PropertyChangeListener,
		ConnectionEditPart, IGeneralPart {
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return super.createFigure();
	}

	@Override
	public void activate() {
		super.activate();
		((Connection) getModel()).addPropertyChangeListener(this);
		refreshVisuals();
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((Connection) getModel()).removePropertyChangeListener(this);
	}

	@Override
	protected void createEditPolicies() {
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
}
