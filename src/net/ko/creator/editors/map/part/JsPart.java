package net.ko.creator.editors.map.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.ko.creator.editors.map.figure.JsFigure;
import net.ko.creator.editors.map.model.JS;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.policies.AppDeletePolicy;
import net.ko.creator.editors.map.policies.AppEditLayoutPolicies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class JsPart extends AppAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		figure = new JsFigure();
		setFigureAppearence(figure);
		refreshParameters();
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new AppEditLayoutPolicies());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new AppDeletePolicy());
	}

	protected void refreshVisuals() {
		JsFigure figure = (JsFigure) getFigure();
		JS model = (JS) getModel();
		figure.setCaption(model.getName());
		figure.setLayout(model.getLayout());
		super.refreshVisuals();
	}

	public List<Node> getModelChildren() {
		JS model = (JS) getModel();
		return model.getChildrenArray();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		String prop = evt.getPropertyName();
		if (prop.equals("triggerSelector") || prop.equals("triggerEvent"))
			refreshVisuals();
	}
}
