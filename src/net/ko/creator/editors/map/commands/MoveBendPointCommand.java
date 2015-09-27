package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.map.model.Connection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public final class MoveBendPointCommand extends Command {

	private Point oldLocation;
	private Point newLocation;
	private int index;
	private Connection link;

	public void execute() {
		if (oldLocation == null) {
			oldLocation = link.getBendpoints().get(index);
		}
		link.getBendpoints().set(index, newLocation);
		link.forceRefresh();
	}

	@Override
	public void undo() {
		link.getBendpoints().set(index, oldLocation);
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public void setConnection(final Connection link) {
		this.link = link;
	}

	public void setLocation(final Point newLocation) {
		this.newLocation = newLocation;
	}
}