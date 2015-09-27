package net.ko.creator.editors.map.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class TextDataPropertyDescriptor extends PropertyDescriptor {
	private String title;
	private String message;

	public TextDataPropertyDescriptor(Object id, String displayName, String title, String message) {
		super(id, displayName);
		this.title = title;
		this.message = message;
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new TextDialogCellEditor(parent, title, message);
		if (getValidator() != null)
			editor.setValidator(getValidator());
		return editor;

	}

}