package net.ko.wizard;

import net.ko.creator.Activator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public abstract class KoWizardPage extends WizardPage {
	protected ISelection selection;
	protected IContainer fileContainer;
	protected IProject project;
	
	protected KoWizardPage(String pageName) {
		super(pageName);
		setImageDescriptor(ImageDescriptor.createFromURL(Platform.getBundle(Activator.PLUGIN_ID).getResource("/icons/kowizard.png")));
	}

	@Override
	public void createControl(Composite arg0) {

	}
	protected void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	protected void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				if (obj instanceof IContainer)
					fileContainer = (IContainer) obj;
				else
					fileContainer = ((IResource) obj).getParent();
			}
		}
	}
	public void dialogChanged(){
		if (getContainerName().length() == 0) {
			updateStatus("Le dossier doit être spécifié ou être valide");
			return;
		}
		if (fileContainer == null
				|| (fileContainer.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!fileContainer.isAccessible()) {
			updateStatus("Le projet n'est pas accessible");
			return;
		}
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
		initialize();
	}
	public String getContainerName(){
		String result="";
		if(fileContainer!=null)
			result=fileContainer.getFullPath().toString();
		return result;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
