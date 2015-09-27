package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.Connection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public final class DeleteBendPointCommand extends Command {

	private Connection link;
	private int index;
	private Point location;

	@Override
	public boolean canExecute() {
		return (link != null) && (link.getBendpoints().size() > index);
	}

	@Override
	public void execute() {
		location = link.getBendpoints().get(index);
		link.getBendpoints().remove(index);
		link.forceRefresh();
	}

	@Override
	public void undo() {
		link.getBendpoints().add(index, location);
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public void setConnection(final Connection link) {
		this.link = link;
	}
}