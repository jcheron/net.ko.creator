package net.ko.wizard.display;

import net.ko.creator.Activator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

public class DisplayWizardPage extends NewTypeWizardPage {
	private final static String PAGE_NAME = "NewClassWizardPage"; //$NON-NLS-1$

	private final static String SETTINGS_CREATEMAIN = "create_main"; //$NON-NLS-1$
	private final static String SETTINGS_CREATECONSTR = "create_constructor"; //$NON-NLS-1$
	private final static String SETTINGS_CREATEUNIMPLEMENTED = "create_unimplemented"; //$NON-NLS-1$

	/**
	 * Creates a new <code>NewClassWizardPage</code>
	 */
	public DisplayWizardPage(boolean isClass, String pageName) {
		super(true, PAGE_NAME);
		setImageDescriptor(ImageDescriptor.createFromURL(Platform.getBundle(Activator.PLUGIN_ID).getResource("/icons/kowizard.png")));
		setTitle("Display class");
		setDescription("Création d'un nouveau display");

		String[] buttonNames3 = new String[] {
				NewWizardMessages.NewClassWizardPage_methods_main, NewWizardMessages.NewClassWizardPage_methods_constructors,
				NewWizardMessages.NewClassWizardPage_methods_inherited
		};
	}

	// -------- Initialization ---------

	/**
	 * The wizard owning this page is responsible for calling this method with
	 * the current selection. The selection is used to initialize the fields of
	 * the wizard page.
	 * 
	 * @param selection
	 *            used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		doStatusUpdate();

		boolean createMain = false;
		boolean createConstructors = false;
		boolean createUnimplemented = true;
		IDialogSettings dialogSettings = getDialogSettings();
		if (dialogSettings != null) {
			IDialogSettings section = dialogSettings.getSection(PAGE_NAME);
			if (section != null) {
				createMain = section.getBoolean(SETTINGS_CREATEMAIN);
				createConstructors = section.getBoolean(SETTINGS_CREATECONSTR);
				createUnimplemented = section.getBoolean(SETTINGS_CREATEUNIMPLEMENTED);
			}
		}
	}

	// ------ validation --------
	private void doStatusUpdate() {
		// status of all used components
		IStatus[] status = new IStatus[] {
				fContainerStatus,
				isEnclosingTypeSelected() ? fEnclosingTypeStatus : fPackageStatus,
				fTypeNameStatus,
				fModifierStatus,
				fSuperClassStatus,
				fSuperInterfacesStatus
		};

		// the mode severe status will be displayed and the OK button
		// enabled/disabled.
		updateStatus(status);
	}

	/*
	 * @see NewContainerWizardPage#handleFieldChanged
	 */
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);

		doStatusUpdate();
	}

	// ------ UI --------

	/*
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		// pick & choose the wanted UI components

		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);
		// createEnclosingTypeControls(composite, nColumns);

		createSeparator(composite, nColumns);

		createTypeNameControls(composite, nColumns);
		// createModifierControls(composite, nColumns);

		// createSuperClassControls(composite, nColumns);

		// createSuperInterfacesControls(composite, nColumns);

		// createMethodStubSelectionControls(composite, nColumns);

		// createCommentControls(composite, nColumns);
		// enableCommentControl(true);

		setControl(composite);

		Dialog.applyDialogFont(composite);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IJavaHelpContextIds.NEW_CLASS_WIZARD_PAGE);
	}

	/*
	 * @see WizardPage#becomesVisible
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setFocus();
		} else {
			IDialogSettings dialogSettings = getDialogSettings();
			if (dialogSettings != null) {
				IDialogSettings section = dialogSettings.getSection(PAGE_NAME);
				if (section == null) {
					section = dialogSettings.addNewSection(PAGE_NAME);
				}
				// section.put(SETTINGS_CREATEMAIN, isCreateMain());
				// section.put(SETTINGS_CREATECONSTR, isCreateConstructors());
			}
		}
	}

	@Override
	public String getSuperClass() {
		return "net.ko.displays.KObjectDisplay";
	}

}
