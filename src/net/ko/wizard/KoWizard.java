package net.ko.wizard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import net.ko.bean.TemplateType;
import net.ko.creator.WorkbenchUtils;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class KoWizard extends Wizard implements IWorkbenchWizard{
	protected KoWizardPage1 one;
	protected KoWizardPage2 tow;
	protected IProject project=null;
	private ISelection selection;

	public KoWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new KoWizardPage1();
		tow=new KoWizardPage2();
		if(project!=null){
			one.setProject(project);
			tow.setProject(project);
		}
		if(selection!=null){
			one.setSelection(selection);
			tow.setSelection(selection);
		}
		addPage(one);
		addPage(tow);

	}

	@Override
	 public boolean performFinish() {
        final String containerName = one.getFolderName();
        final String fileName = tow.getFileName();
        KoWizardCompo2 compo2=tow.getComposite();
        if(!compo2.getFileNameFromClassName().equals(fileName)&&!compo2.getTemplateType().equals(TemplateType.ttCustom)){
        	compo2.insertIn(0, "{#className:"+compo2.getClassName()+"#}\n");
        }
        final String content=tow.getTextTemplate();

        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(containerName, fileName, monitor,content);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

	private void doFinish(String containerName, String fileName,
			IProgressMonitor monitor,String content) throws CoreException {

		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));

		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName
					+ "\" does not exist.");
		}
		IContainer container = (IContainer) resource;

		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = new ByteArrayInputStream(content.getBytes());

			try {
				if (file.exists()) {
					file.setContents(stream, true, true, monitor);
				} else {
					file.create(stream, true, monitor);
				}
			} finally {
				stream.close();
			}

		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Ouverture du template...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		if (selection != null && !selection.isEmpty()) {
			project=WorkbenchUtils.getActiveProject();
		}
	}

	public KoWizardPage2 getTow() {
		return tow;
	}

	public void setTow(KoWizardPage2 tow) {
		this.tow = tow;
	}
    private void addFileToProject(IContainer container, Path path,
            InputStream contentStream, IProgressMonitor monitor)
            throws CoreException {
        final IFile file = container.getFile(path);

        if (file.exists()) {
            file.setContents(contentStream, true, true, monitor);
        } else {
            file.create(contentStream, true, monitor);
        }

    }
	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "NewFileWizard", IStatus.OK,
				message, null);
		throw new CoreException(status);
	}
}
