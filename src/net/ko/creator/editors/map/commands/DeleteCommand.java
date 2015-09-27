package net.ko.creator.editors.map.commands;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.IHasConnectorURL;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.commands.Command;

public class DeleteCommand extends Command {
	private Node model;
	private Node parentModel;
	private List<Connection> deletedConns = new ArrayList<>();

	public void execute() {
		boolean result = this.parentModel.removeChild(model);
		if (result) {
			deleteSourceConnection(model);
			deleteSourceConnections();
			while (model.getTargetConnectionsArray().size() > 0) {
				deleteConn(model.getTargetConnectionsArray().get(0));
			}
		}

	}

	public void setModel(Object model) {
		this.model = (Node) model;
	}

	public void setParentModel(Object model) {
		parentModel = (Node) model;
	}

	public void undo() {
		this.parentModel.addChildAndFire(model);
		for (Connection conn : deletedConns) {
			ConnectionCreateCommand reco = new ConnectionCreateCommand();
			reco.setSourceNode(conn.getSourceNode());
			reco.setTargetNode(conn.getTargetNode());
			reco.execute();
		}
	}

	protected void deleteConn(Connection conn) {
		if (conn != null) {
			deletedConns.add(conn);
			ConnectionDeleteCommand deleteConn = new ConnectionDeleteCommand();
			deleteConn.setConn(conn);
			deleteConn.execute();
		}
	}

	protected void deleteSourceConnections() {
		for (Node n : model.getAllChildrenArray()) {
			deleteSourceConnection(n);
		}
	}

	protected void deleteSourceConnection(Node aNode) {
		if (aNode instanceof IHasConnectorURL) {
			Connection conn = aNode.getConnectionSource();
			if (conn != null) {
				deletedConns.add(conn);
				deleteConn(conn);
			}
		}
	}
}