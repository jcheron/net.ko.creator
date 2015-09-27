package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.Node;

import org.eclipse.draw2d.geometry.Rectangle;

public class MappingChangeLayoutCommand extends AbstractLayoutCommand {

	private Node model;
	private Rectangle layout;

	public void execute() {
		model.setLayout(layout);
	}

	public void setConstraint(Rectangle rect) {
		this.layout = rect;
	}

	public void setModel(Object model) {
		this.model = (Node) model;
	}
}
