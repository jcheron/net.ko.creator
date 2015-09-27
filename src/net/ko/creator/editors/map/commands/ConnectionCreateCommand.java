package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.AjaxObject;
import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.IHasConnectorURL;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.commands.Command;

public class ConnectionCreateCommand extends Command {

	private Node sourceNode, targetNode;
	private Connection conn;

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	@Override
	public boolean canExecute() {
		if (sourceNode == null || targetNode == null)
			return false;
		if (sourceNode.equals(targetNode))
			return false;
		if (!(sourceNode instanceof AjaxObject) || !(targetNode instanceof AjaxRequest))
			return false;
		return true;
	}

	@Override
	public void execute() {
		conn = new Connection(sourceNode, targetNode);
		String newURL = ((AjaxRequest) targetNode).getAjaxRequest().getRequestURL();
		((IHasConnectorURL) sourceNode).setURL(newURL);
		// conn.connect();
	}

	@Override
	public boolean canUndo() {
		if (sourceNode == null || targetNode == null || conn == null)
			return false;
		return true;
	}

	@Override
	public void undo() {
		conn.disconnect();
	}

}