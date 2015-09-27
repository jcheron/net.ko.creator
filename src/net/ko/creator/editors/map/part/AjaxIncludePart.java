package net.ko.creator.editors.map.part;

import net.ko.creator.editors.map.model.AjaxIncludeDialog;
import net.ko.creator.editors.map.model.Node;

public class AjaxIncludePart extends AjaxObjectPart {

	public AjaxIncludePart(String type) {
		super(type);
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		Node model = (Node) getModel();
		if (model.getParent() instanceof AjaxIncludeDialog)
			model.getParent().getListeners().firePropertyChange(Node.PROPERTY_DISPLAY_PARENT, null, true);
	}

}
