package net.ko.wizard.display;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.actions.OverrideMethodsAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class KoDisplayWizard extends Wizard implements IWorkbenchWizard {
	protected DisplayWizardPage one;
	protected IStructuredSelection sel;

	@Override
	public void init(IWorkbench workBench, IStructuredSelection sel) {
		this.sel = sel;
	}

	@Override
	public boolean performFinish() {
		if (one.isPageComplete()) {
			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					try {
						doFinish(monitor);
					} catch (CoreException | InterruptedException e) {
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
		}
		return true;
	}

	@Override
	public void addPages() {
		one = new DisplayWizardPage(true, "Création de display");
		if (sel != null)
			one.init(sel);
		addPage(one);
	}

	private void doFinish(IProgressMonitor monitor) throws CoreException, InterruptedException {
		one.createType(monitor);
		monitor.worked(1);
		monitor.setTaskName("Ouverture du fichier...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IResource resource = one.getModifiedResource();
				if (resource != null) {
					try {
						IDE.openEditor(page, (IFile) resource, true);
					} catch (PartInitException e) {
					}
				}
			}
		});
		monitor.worked(1);
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				OverrideMethodsAction oa = new OverrideMethodsAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite());
				IStructuredSelection selection = new IStructuredSelection() {

					@Override
					public boolean isEmpty() {
						return false;
					}

					@Override
					public List toList() {
						return Arrays.asList(one.getCreatedType());
					}

					@Override
					public Object[] toArray() {
						return toList().toArray();
					}

					@Override
					public int size() {
						return 1;
					}

					@Override
					public Iterator iterator() {
						return toList().iterator();
					}

					@Override
					public Object getFirstElement() {
						return one.getCreatedType();
					}
				};
				oa.run(selection);
			}
		});
	}
}
