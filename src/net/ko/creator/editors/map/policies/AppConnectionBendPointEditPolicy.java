package net.ko.creator.editors.map.policies;

import net.ko.creator.editors.map.commands.CreateBendPointCommand;
import net.ko.creator.editors.map.commands.DeleteBendPointCommand;
import net.ko.creator.editors.map.commands.MoveBendPointCommand;
import net.ko.creator.editors.map.model.Connection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

public class AppConnectionBendPointEditPolicy extends BendpointEditPolicy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Command getCreateBendpointCommand(final BendpointRequest request) {
		CreateBendPointCommand command = new CreateBendPointCommand();

		Point p = request.getLocation();

		command.setConnection((Connection) request.getSource().getModel());
		command.setLocation(p);
		command.setIndex(request.getIndex());

		return command;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Command getMoveBendpointCommand(final BendpointRequest request) {
		MoveBendPointCommand command = new MoveBendPointCommand();

		Point p = request.getLocation();

		command.setConnection((Connection) request.getSource().getModel());
		command.setLocation(p);
		command.setIndex(request.getIndex());

		return command;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Command getDeleteBendpointCommand(final BendpointRequest request) {
		DeleteBendPointCommand command = new DeleteBendPointCommand();

		command.setConnection((Connection) request.getSource().getModel());
		command.setIndex(request.getIndex());
		return command;
	}
}
