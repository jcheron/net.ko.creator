package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.AjaxObject;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.gef.commands.Command;

public class MoveChildNodeCommand extends Command {
	private Node childToMove;
	private Node newParent;
	private Node oldParent;
	private boolean childExist;

	@Override
	public boolean canExecute() {
		return childToMove != null && newParent != null;
	}

	@Override
	public boolean canUndo() {
		return canExecute();
	}

	@Override
	public void execute() {
		childExist = newParent.contains(childToMove);
		newParent.addChildAndFire(childToMove);
	}

	@Override
	public void undo() {

		if (childExist) {
			int newIndex = newParent.getChildrenArray().indexOf(childToMove);
			if (newIndex != -1) {
				Node newChildNode = newParent.getChildrenArray().get(newIndex);
				for (Node child : childToMove.getChildrenArray())
					if (child instanceof AjaxObject)
						newChildNode.removeChild(child);
			}
		} else
			newParent.removeChild(childToMove);
		oldParent.addChildAndFire(childToMove, childToMove.getDeletedIndex());
	}

	public Node getChildToMove() {
		return childToMove;
	}

	public void setChildToMove(Node childToMove) {
		this.childToMove = childToMove;
		oldParent = childToMove.getParent();
	}

	public Node getNewParent() {
		return newParent;
	}

	public void setNewParent(Node newParent) {
		this.newParent = newParent;
	}

	@Override
	public void redo() {
		super.redo();
		oldParent.removeChild(childToMove);
	}
}
