package net.ko.creator.editors.map.properties;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class TextDialogCellEditor extends DialogCellEditor {
	protected String title;
	protected String message;

	protected TextDialogCellEditor(Composite parent, String title, String message) {
		super(parent);
		this.title = title;
		this.message = message;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), title, message,
				(String) getValue(), null) {

			/**
			 * Override this method to make the text field multilined and give
			 * it a scroll bar. But...
			 */
			@Override
			protected int getInputTextStyle() {
				return SWT.MULTI | SWT.BORDER | SWT.V_SCROLL;
			}

			/**
			 * ...it still is just one line high. This hack is not very nice,
			 * but at least it gets the job done... ;o)
			 */
			@Override
			protected Control createDialogArea(Composite parent) {
				Control res = super.createDialogArea(parent);
				((GridData) this.getText().getLayoutData()).heightHint = 100;
				return res;
			}
		};
		dlg.open();
		if (dlg.getReturnCode() == Dialog.OK) {
			setValue(dlg.getValue());
		}
		return getValue();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}