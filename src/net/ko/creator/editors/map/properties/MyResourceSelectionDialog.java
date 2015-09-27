package net.ko.creator.editors.map.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

@SuppressWarnings("restriction")
public class MyResourceSelectionDialog extends ElementTreeSelectionDialog {
	private List<String> filters = new ArrayList<>();

	public MyResourceSelectionDialog(Shell parent, String[] filters) {
		super(parent, new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
		this.filters = Arrays.asList(filters);
		setMessage("Sélectionner la ressource à mapper :");
		addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof File) {
					File f = ((File) element);
					String ext = f.getFileExtension();
					return MyResourceSelectionDialog.this.filters.indexOf(ext) != -1;
				} else if (element instanceof Folder) {
					Folder f = (Folder) element;
					try {
						for (IResource r : f.members()) {
							if (select(viewer, element, r))
								return true;
						}
					} catch (CoreException e1) {
						return false;
					}
				}
				return false;
			}
		});
	}

	@Override
	protected Control createContents(Composite parent) {
		return super.createContents(parent);
	}

}
