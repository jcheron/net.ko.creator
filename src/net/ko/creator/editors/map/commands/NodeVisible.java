package net.ko.creator.editors.map.commands;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.IHasConnectorURL;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.commands.Command;

public class NodeVisible extends Command {
	private Node model;
	private List<Connection> conns = new ArrayList<>();

	@Override
	public boolean canExecute() {
		if (model == null)
			return false;
		return true;
	}

	@Override
	public void execute() {
		model.setVisible(!model.isVisible());
		fireSourceConnection(model);
		fireSourceConnections();
		for (Connection conn : model.getTargetConnectionsArray()) {
			fireVisibleconn(conn, false);
		}
	}

	@Override
	public boolean canUndo() {
		if (model != null)
			return false;
		return true;
	}

	@Override
	public void undo() {
		model.setVisible(!model.isVisible());
	}

	public Node getModel() {
		return model;
	}

	public void setModel(Node model) {
		this.model = model;
	}

	protected void fireVisibleconn(Connection conn, boolean visible) {
		conn.getListeners().firePropertyChange("visible", !visible, visible);
	}

	protected void fireSourceConnection(Node aNode) {
		if (aNode instanceof IHasConnectorURL) {
			Connection conn = aNode.getConnectionSource();
			if (conn != null) {
				conns.add(conn);
				fireVisibleconn(conn, false);
			}
		}
	}

	protected void fireSourceConnections() {
		for (Node n : model.getAllChildrenArray()) {
			fireSourceConnection(n);
		}
	}
}
