package net.ko.creator.editors.map.commands;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.IHasConnectorURL;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.commands.Command;

public class RelatedNodesVisibles extends Command {
	private Node model;
	private boolean visible;
	private List<Connection> conns = new ArrayList<>();

	@Override
	public void execute() {
		List<Node> nodes = model.getRelatedNodes();
		for (Node node : nodes) {
			executeOne(node);
		}
	}

	public void executeOne(Node relatedNode) {
		relatedNode.setVisible(visible, visible);
		fireSourceConnection(relatedNode);
		fireSourceConnections(relatedNode);
		for (Connection conn : relatedNode.getTargetConnectionsArray()) {
			fireVisibleconn(conn, false);
		}
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

	protected void fireSourceConnections(Node model) {
		for (Node n : model.getAllChildrenArray()) {
			fireSourceConnection(n);
		}
	}

	public Node getModel() {
		return model;
	}

	public void setModel(Node model) {
		this.model = model;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
