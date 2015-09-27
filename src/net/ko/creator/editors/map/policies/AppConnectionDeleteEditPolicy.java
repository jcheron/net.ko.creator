package net.ko.creator.editors.map.policies;

import net.ko.creator.editors.map.commands.ConnectionDeleteCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class AppConnectionDeleteEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest arg0) {
		ConnectionDeleteCommand command = new ConnectionDeleteCommand();
		command.setLink(getHost().getModel());
		return command;
	}
}
