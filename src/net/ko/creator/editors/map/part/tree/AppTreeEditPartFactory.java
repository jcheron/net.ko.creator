package net.ko.creator.editors.map.part.tree;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class AppTreeEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = new AppTreeEditPart();

		if (part != null)
			part.setModel(model);

		return part;
	}
}
