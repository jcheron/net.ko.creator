package net.ko.creator.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

public class MyFileElementTreeSelectionDialog extends
		ElementTreeSelectionDialog {

	public MyFileElementTreeSelectionDialog(Shell parent,
			ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
		// TODO Auto-generated constructor stub
	}
	public void setParams(IProject project,String title,final String filter){
		setInput(project);
		setTitle(title);
		addFilter(new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				boolean result=false;
				if (element instanceof IFolder)
					result=true;
				if (element instanceof IFile){
					String regex = filter.replace(".", "\\.").replace("?", ".?").replace("*", ".*?");
					result=((IFile)element).getName().matches(regex);
				}
				return result;
			}
		});
	}
}
