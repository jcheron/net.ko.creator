package net.ko.creator.editors.map.properties;

import net.ko.creator.utils.FrameworkUtils;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;

public class FileCellEditor extends DialogCellEditor {

	private String orgPath;

	private String[] filters;

	public FileCellEditor(Composite parent) {
		super(parent);
	}

	public FileCellEditor(Composite parent, String orgPath) {
		this(parent);
		this.orgPath = orgPath;
	}

	public FileCellEditor(Composite parent, String orgPath, String[] filters) {
		this(parent);
		this.orgPath = orgPath;
		this.filters = filters;
	}

	public FileCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		FileDialog dialog = new FileDialog(cellEditorWindow.getShell());
		if (orgPath != null && !"".equals(orgPath)) { //$NON-NLS-1$
			dialog.setFileName(FrameworkUtils.getOSPath(orgPath));
		}
		if (filters != null && filters.length > 0) {
			dialog.setFilterExtensions(filters);
		}
		String path = dialog.open();
		if (path != null) {
			path = FrameworkUtils.getPortablePath(path);
		}
		return path;
	}

}