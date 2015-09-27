package net.ko.creator.handlers;

import net.ko.creator.WorkbenchUtils;
import net.ko.wizard.KoWizard;
import net.ko.wizard.display.AssoDisplaySelectionDialog;
import net.ko.wizard.display.KoDisplayWizard;
import net.ko.wizard.display.TemplateDisplayLabelProvider;
import net.ko.wizard.mappingcontroller.AssoMappingSelectionDialog;
import net.ko.wizard.mappingcontroller.KoMappingControllerWizard;
import net.ko.wizard.mappingcontroller.MappingClassLabelProvider;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;

public class KoWizardHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IProject project = WorkbenchUtils.getActiveProject();
		if (project != null) {
			IStructuredSelection selection = null;
			selection = (IStructuredSelection) window.getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");
			if (!(selection instanceof IStructuredSelection))
				selection = (IStructuredSelection) window.getSelectionService().getSelection("org.eclipse.ui.navigator.ProjectExplorer");

			switch (event.getCommand().getId()) {
			case "net.ko.creator.commands.koTemplate":
				KoWizard wizard = new KoWizard();
				wizard.init(PlatformUI.getWorkbench(), selection);
				WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
				dialog.open();
				break;
			case "net.ko.creator.commands.kodisplay":
				KoDisplayWizard wizardDisplay = new KoDisplayWizard();
				wizardDisplay.init(PlatformUI.getWorkbench(), selection);
				WizardDialog dialogD = new WizardDialog(window.getShell(), wizardDisplay);
				dialogD.open();
				break;
			case "net.ko.creator.commands.komappingcontroller":
				KoMappingControllerWizard wizardMapping = new KoMappingControllerWizard();
				wizardMapping.setProject(project);
				wizardMapping.init(PlatformUI.getWorkbench(), selection);
				WizardDialog dialogC = new WizardDialog(window.getShell(), wizardMapping);
				dialogC.open();
				break;
			case "net.ko.creator.commands.assodisplays":
				AssoDisplaySelectionDialog dsd = new AssoDisplaySelectionDialog(window.getShell(), new TemplateDisplayLabelProvider(), new BaseWorkbenchContentProvider());
				dsd.setInput(project.getFolder("WebContent"));
				dsd.setProject(project);
				try {
					dsd.setSelectionDisplay(selection);
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
				dsd.setTitle("Association des displays");
				dsd.open();
				break;
			case "net.ko.creator.commands.assomappings":
				AssoMappingSelectionDialog msd = new AssoMappingSelectionDialog(window.getShell(), new MappingClassLabelProvider());
				msd.setProject(project);
				try {
					msd.setSelectionMapping(selection);
				} catch (JavaModelException e) {
				}
				msd.init();
				msd.open();
				break;
			}

		} else {
			MessageDialog.openWarning(window.getShell(), "Warning", "Vous devez sélectionner un projet");
		}
		return null;
	}

}
