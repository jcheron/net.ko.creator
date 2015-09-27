package net.ko.creator.editors.map.policies;

import net.ko.creator.editors.map.commands.ConnectionCreateCommand;
import net.ko.creator.editors.map.commands.ConnectionReconnectCommand;
import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

public class AppConnectionPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
		Node targetNode = (Node) getHost().getModel();
		cmd.setTargetNode(targetNode);
		return cmd;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand cmd = new ConnectionCreateCommand();
		Node sourceNode = (Node) getHost().getModel();
		cmd.setSourceNode(sourceNode);
		request.setStartCommand(cmd);
		return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Connection conn = (Connection) request.getConnectionEditPart().getModel();
		Node sourceNode = (Node) getHost().getModel();
		ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
		cmd.setNewSourceNode(sourceNode);
		return cmd;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Connection conn = (Connection) request.getConnectionEditPart().getModel();
		Node targetNode = (Node) getHost().getModel();
		ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
		cmd.setNewTargetNode(targetNode);
		return cmd;
	}
}
