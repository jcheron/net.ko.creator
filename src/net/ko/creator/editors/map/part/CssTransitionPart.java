package net.ko.creator.editors.map.part;

import java.beans.PropertyChangeEvent;

import net.ko.creator.editors.map.model.Node;

public class CssTransitionPart extends AjaxObjectPart {

	public CssTransitionPart(String type) {
		super(type);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getPropertyName().equals(Node.PROPERTY_ADD))
			updateAndRefreshParameters();
		if (evt.getPropertyName().equals(Node.PROPERTY_REMOVE))
			updateAndRefreshParameters();

	}

}
