package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.AjaxObject;
import net.ko.creator.editors.map.model.Node;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class AjaxObjectCreateCommand extends Command {
	private AjaxObject ajaxNew;
	private Node ajaxParent;

	public void setAjaxNew(Object obj) {
		if (obj instanceof AjaxObject)
			this.ajaxNew = (AjaxObject) obj;
	}

	public void setAjaxParent(Object obj) {
		if (obj instanceof Node)
			this.ajaxParent = (Node) obj;
	}

	public void setLayout(Rectangle r) {
		if (ajaxNew == null)
			return;
		ajaxNew.setLayout(r);
	}

	@Override
	public boolean canExecute() {
		if (ajaxNew == null || ajaxParent == null)
			return false;
		return true;
	}

	@Override
	public void execute() {
		ajaxParent.addChildAndFire(ajaxNew);
	}

	@Override
	public boolean canUndo() {
		if (ajaxNew == null || ajaxParent == null)
			return false;
		return ajaxParent.contains(ajaxNew);
	}

	@Override
	public void undo() {
		ajaxParent.removeChild(ajaxNew);
	}
}
