package net.ko.creator.editors.map.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ContentAssistPropertyDescriptor extends PropertyDescriptor {

	public ContentAssistPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new ContentAssistCellEditor(parent);
		if (getValidator() != null)
			editor.setValidator(getValidator());
		return editor;

	}

}