package net.ko.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

public class MyTreeSelectionDialog extends ElementTreeSelectionDialog {

	public MyTreeSelectionDialog(Shell parent, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
	}
	public void setParams(IFolder folder,String title,String message){
		setTitle(title);
		setMessage(message);
		setInput(ResourcesPlugin.getWorkspace().getRoot());
		setInitialSelections(new Object[]{folder});
		addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				boolean result=true;
				if (element instanceof IFile)
					result=false;
				return result;
			}
		});
	}
}
