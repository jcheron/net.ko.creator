package net.ko.creator.editors.map.policies;

import net.ko.creator.editors.map.actions.RelatedNodesVisibleAction;
import net.ko.creator.editors.map.actions.VisibleAction;
import net.ko.creator.editors.map.commands.NodeVisible;
import net.ko.creator.editors.map.commands.RelatedNodesVisibles;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;

public class NodeRolePolicy extends ComponentEditPolicy {
	@Override
	public Command getCommand(Request request) {
		if (request.getType().equals(VisibleAction.ID))
			return createVisibleCommand(request);
		if (request.getType().equals(RelatedNodesVisibleAction.ID_VISIBLE))
			return createRelatedNodesVisiblesCommand(request, true);
		else if (request.getType().equals(RelatedNodesVisibleAction.ID_INVISIBLE))
			return createRelatedNodesVisiblesCommand(request, false);
		return null;
	}

	protected Command createVisibleCommand(Request request) {
		NodeVisible command = new NodeVisible();
		command.setModel((Node) getHost().getModel());
		return command;
	}

	protected Command createRelatedNodesVisiblesCommand(Request request, boolean visible) {
		RelatedNodesVisibles command = new RelatedNodesVisibles();
		command.setModel((Node) getHost().getModel());
		command.setVisible(visible);
		return command;
	}
}
