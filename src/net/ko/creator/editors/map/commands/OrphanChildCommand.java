package net.ko.creator.editors.map.commands;

import java.util.List;

import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.commands.Command;

public class OrphanChildCommand extends Command {

	private Node parent;
	private Node child;

	@Override
	public void execute() {
		List<Node> children = parent.getChildrenArray();
		child.setDeletedIndex(children.indexOf(child));
		parent.removeChild(child);
	}

	@Override
	public void redo() {
		// parent.removeChild(child);
	}

	public void setChild(Node child) {
		this.child = child;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public void undo() {
		// parent.addChild(child, index);
	}

	@Override
	public boolean canUndo() {
		return child != null && parent != null;
	}

}
