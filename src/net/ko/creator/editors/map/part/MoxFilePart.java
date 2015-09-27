package net.ko.creator.editors.map.part;

import java.util.List;

import net.ko.creator.editors.map.figure.MoxFileFigure;
import net.ko.creator.editors.map.model.MoxFile;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.policies.AppEditLayoutPolicies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class MoxFilePart extends AppAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		return new MoxFileFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new AppEditLayoutPolicies());
	}

	@Override
	protected void refreshVisuals() {
		MoxFileFigure figure = (MoxFileFigure) getFigure();
		MoxFile model = (MoxFile) getModel();
		figure.setCaption(model.getFileName());
	}

	@Override
	public List<Node> getModelChildren() {
		return ((MoxFile) getModel()).getChildrenArray();
	}

	@Override
	protected void refreshChildren() {
		// TODO Auto-generated method stub
		super.refreshChildren();
	}

	@Override
	public void activate() {
		super.activate();
		MoxFile moxFile = (MoxFile) getModel();
		moxFile.setDirty(false);
	}

}
