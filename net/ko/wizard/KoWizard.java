package net.ko.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class KoWizard extends Wizard implements IWorkbenchWizard{

	protected WizardPageOne one;

	public KoWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		super.addPages();
		one = new WizardPageOne("test");
		addPage(one);
	}

	@Override
	public boolean performFinish() {
		// Print the result to the console
		System.out.println(one.getText1());
		//System.out.println(two.getText1());

		return true;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
	}
}
