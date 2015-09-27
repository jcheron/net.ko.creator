package net.ko.wizard.display;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;

public class ClassCompo extends Composite {
	private Table table;

	public ClassCompo(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		ListViewer listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		FormData fd_list = new FormData();
		fd_list.bottom = new FormAttachment(0, 167);
		fd_list.top = new FormAttachment(0, 10);
		fd_list.left = new FormAttachment(0, 10);
		fd_list.right = new FormAttachment(0, 440);
		list.setLayoutData(fd_list);

		CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(this, SWT.BORDER | SWT.FULL_SELECTION);
		checkboxTableViewer.setColumnProperties(new String[] { "a", "b" });
		table = checkboxTableViewer.getTable();
		FormData fd_table = new FormData();
		fd_table.right = new FormAttachment(0, 440);
		fd_table.top = new FormAttachment(list, 6);
		fd_table.left = new FormAttachment(0, 10);
		table.setLayoutData(fd_table);
		// TODO Auto-generated constructor stub
	}
}
