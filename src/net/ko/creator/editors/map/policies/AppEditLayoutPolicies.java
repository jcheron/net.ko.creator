package net.ko.creator.editors.map.policies;

import java.util.List;

import net.ko.creator.editors.map.commands.AbstractLayoutCommand;
import net.ko.creator.editors.map.commands.AjaxObjectCreateCommand;
import net.ko.creator.editors.map.commands.AjaxRequestCreateCommand;
import net.ko.creator.editors.map.commands.JsCreateCommand;
import net.ko.creator.editors.map.commands.MappingChangeLayoutCommand;
import net.ko.creator.editors.map.commands.MoveChildNodeCommand;
import net.ko.creator.editors.map.figure.BaseFigure;
import net.ko.creator.editors.map.model.AjaxObject;
import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.creator.editors.map.model.JS;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.part.AjaxRequestPart;
import net.ko.creator.editors.map.part.MoxFilePart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public class AppEditLayoutPolicies extends XYLayoutEditPolicy {
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {

		AbstractLayoutCommand command = null;

		if (child instanceof AjaxRequestPart) {
			command = new MappingChangeLayoutCommand();
		}

		/*
		 * if (child instanceof JsPart) { command = new
		 * MappingChangeLayoutCommand(); }
		 */
		command.setModel(child.getModel());
		command.setConstraint((Rectangle) constraint);
		return command;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (request.getType() == REQ_CREATE) {
			Object newObject = request.getNewObject();
			if (getHost() instanceof MoxFilePart && newObject instanceof AjaxRequest) {
				AjaxRequestCreateCommand cmd = new AjaxRequestCreateCommand();

				cmd.setMoxFile(getHost().getModel());
				cmd.setAjaxRequest(newObject);

				Rectangle constraint = (Rectangle) getConstraintFor(request);
				constraint.x = (constraint.x < 0) ? 0 : constraint.x;
				constraint.y = (constraint.y < 0) ? 0 : constraint.y;
				constraint.width = (constraint.width <= 0) ? BaseFigure.DEF_WIDTH : constraint.width;
				constraint.height = (constraint.height <= 0) ? BaseFigure.DEF_HEIGHT : constraint.height;
				cmd.setLayout(constraint);
				return cmd;
			}
			if (getHost() instanceof AjaxRequestPart && newObject instanceof JS) {
				JsCreateCommand cmd = new JsCreateCommand();

				cmd.setAjaxRequest(getHost().getModel());
				cmd.setJs(newObject);
				return cmd;
			}
			if (newObject instanceof AjaxObject) {
				String newClass = newObject.getClass().getSimpleName();
				List<Class<?>> possibleClasses = Node.getPossibleClassChildren(newClass);
				Class<?> hostClass = getHost().getModel().getClass();
				if (possibleClasses.contains(hostClass)) {
					AjaxObjectCreateCommand cmd = new AjaxObjectCreateCommand();
					cmd.setAjaxParent(getHost().getModel());
					cmd.setAjaxNew(request.getNewObject());
					return cmd;
				}
			}
		}
		return null;
	}

	@Override
	protected Command getAddCommand(Request generic) {
		ChangeBoundsRequest request = (ChangeBoundsRequest) generic;
		List editParts = request.getEditParts();
		CompoundCommand command = new CompoundCommand();
		GraphicalEditPart childPart;

		for (int i = 0; i < editParts.size(); i++) {
			childPart = (GraphicalEditPart) editParts.get(i);
			command.add(createAddCommand(generic, childPart));
		}
		return command.unwrap();
	}

	protected Command createAddCommand(Request request, EditPart childEditPart) {
		Command add = null;
		Node movedChild = (Node) childEditPart.getModel();
		Node possibleParent = (Node) getHost().getModel();
		possibleParent.setExpanded(true);
		if (Node.getPossibleClassChildren(movedChild.getClass().getSimpleName()).contains(possibleParent.getClass())) {
			/*
			 * if (movedChild instanceof AjaxObject) { add = new
			 * AjaxObjectCreateCommand(); ((AjaxObjectCreateCommand)
			 * add).setAjaxParent(possibleParent); ((AjaxObjectCreateCommand)
			 * add).setAjaxNew(movedChild); } if (movedChild instanceof JS) {
			 * add = new JsCreateCommand(); ((JsCreateCommand)
			 * add).setAjaxRequest(possibleParent); ((JsCreateCommand)
			 * add).setJs(movedChild); }
			 */
			add = new MoveChildNodeCommand();
			((MoveChildNodeCommand) add).setNewParent(possibleParent);
			((MoveChildNodeCommand) add).setChildToMove(movedChild);
		}
		return add;
	}
}
