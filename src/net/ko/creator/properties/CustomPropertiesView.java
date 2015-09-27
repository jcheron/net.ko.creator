package net.ko.creator.properties;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

public class CustomPropertiesView extends PropertySheet{

	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		// if (part.getSite().getId().equals("net.ko.wizard.kowizard.list"))
		if (part.getSite().getId().equals("net.ko.creator.editors.Template"))
		return true;
			  return false;
	}

}
