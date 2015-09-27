package net.ko.creator.editors.map.part;

import net.ko.creator.editors.map.figure.ToggleFigure;
import net.ko.creator.editors.map.model.ToggleNode;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class TogglePart extends AbstractGraphicalEditPart {

	public TogglePart() {
		super();
	}

	@Override
	protected IFigure createFigure() {
		return new ToggleFigure();
	}

	@Override
	protected void createEditPolicies() {
	}

	@Override
	public void setSelected(int value) {
		if (value == 0)
			super.setSelected(value);
	}

	public void expandCollapse() {
		ToggleNode model = (ToggleNode) getModel();
		model.expandCollapse();
	}

	@Override
	public void performRequest(Request req) {
	}

}
