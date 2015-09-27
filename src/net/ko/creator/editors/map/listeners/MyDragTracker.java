package net.ko.creator.editors.map.listeners;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.DragEditPartsTracker;

public class MyDragTracker extends DragEditPartsTracker {

	public MyDragTracker(EditPart sourceEditPart) {
		super(sourceEditPart);
	}

	@Override
	protected Command getCommand() {
		return super.getCommand();
	}

	@Override
	protected boolean isMove() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected EditPart getTargetEditPart() {
		// TODO Auto-generated method stub
		return super.getTargetEditPart();
	}

}
