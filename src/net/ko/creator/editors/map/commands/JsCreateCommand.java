package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.creator.editors.map.model.JS;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class JsCreateCommand extends Command {
	private JS js;
	private AjaxRequest ajaxRequest;

	public void setJs(Object obj) {
		if (obj instanceof JS)
			this.js = (JS) obj;
	}

	public void setAjaxRequest(Object obj) {
		if (obj instanceof AjaxRequest)
			this.ajaxRequest = (AjaxRequest) obj;
	}

	public void setLayout(Rectangle r) {
		if (js == null)
			return;
		js.setLayout(r);
	}

	@Override
	public boolean canExecute() {
		if (ajaxRequest == null || js == null)
			return false;
		return true;
	}

	@Override
	public void execute() {
		ajaxRequest.addChildAndFire(js);
	}

	@Override
	public boolean canUndo() {
		if (js == null || ajaxRequest == null)
			return false;
		return ajaxRequest.contains(js);
	}

	@Override
	public void undo() {
		ajaxRequest.removeChild(js);
	}
}
