package net.ko.creator.editors.map.policies;

import java.util.List;

import net.ko.creator.editors.map.commands.OrphanChildCommand;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

public class AppContainerEditPolicy extends ContainerEditPolicy
{

	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	@Override
	public Command getOrphanChildrenCommand(GroupRequest request) {
		List parts = request.getEditParts();
		CompoundCommand result = new CompoundCommand();
		for (int i = 0; i < parts.size(); i++) {
			OrphanChildCommand orphan = new OrphanChildCommand();
			orphan.setChild((Node) ((EditPart) parts.get(i)).getModel());
			orphan.setParent((Node) getHost().getModel());
			result.add(orphan);
		}
		return result.unwrap();
	}
}
