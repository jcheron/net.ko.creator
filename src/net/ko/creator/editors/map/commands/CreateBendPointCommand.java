package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.Connection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public final class CreateBendPointCommand extends Command {

	private int index;
	private Point location;
	private Connection link;

	@Override
	public void execute() {
		link.getBendpoints().add(index, location);
		link.forceRefresh();
	}

	@Override
	public void undo() {
		link.getBendpoints().remove(index);
	}

	public void setIndex(final int index) {
		if (index > link.getBendpoints().size())
			this.index = link.getBendpoints().size();
		else
			this.index = index;
	}

	public void setLocation(final Point location) {
		this.location = location;
	}

	public void setConnection(final Connection link) {
		this.link = link;
	}
}
