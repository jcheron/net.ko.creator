package net.ko.creator.editors.map.listeners;

import net.ko.creator.editors.map.factory.RequestDropFactory;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public class MappingTransferDropTargetListener extends
		AbstractTransferDropTargetListener {
	private RequestDropFactory factory = new RequestDropFactory();

	public MappingTransferDropTargetListener(EditPartViewer viewer) {
		super(viewer, TextTransfer.getInstance());
	}

	public MappingTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}

	@Override
	protected void updateTargetRequest() {
		((CreateRequest) getTargetRequest()).setLocation(getDropLocation());
	}

	@Override
	protected Request createTargetRequest() {
		CreateRequest request = new CreateRequest();
		request.setFactory(factory);
		return request;
	}

	@Override
	protected void handleDragOver() {
		getCurrentEvent().detail = DND.DROP_COPY;
		super.handleDragOver();
	}

	@Override
	protected void handleDrop() {
		String s = getCurrentEvent().data + "";
		factory.setRequestURL(s);
		super.handleDrop();
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		super.dropAccept(event);
	}
}
