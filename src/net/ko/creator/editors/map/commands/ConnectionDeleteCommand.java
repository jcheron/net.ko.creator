package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.IHasConnectorURL;

import org.eclipse.gef.commands.Command;

public class ConnectionDeleteCommand extends Command {

	private Connection conn;
	private String oldURL;

	public void setLink(Object model) {
		this.conn = (Connection) model;
	}

	@Override
	public boolean canExecute() {
		if (conn == null)
			return false;
		return true;
	}

	@Override
	public void execute() {
		oldURL = ((IHasConnectorURL) conn.getSourceNode()).getURL();
		((IHasConnectorURL) conn.getSourceNode()).setURL("");
	}

	@Override
	public boolean canUndo() {
		if (conn == null)
			return false;
		return true;
	}

	@Override
	public void undo() {
		((IHasConnectorURL) conn.getSourceNode()).setURL(oldURL);
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
