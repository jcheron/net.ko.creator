package net.ko.wizard.mappingcontroller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import net.ko.bean.mapping.MoxController;
import net.ko.creator.Activator;
import net.ko.creator.WorkbenchUtils;
import net.ko.creator.utils.FrameworkUtils;
import net.ko.utils.KProperties;
import net.ko.utils.KString;
import net.ko.utils.KXmlFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.StubTypeContext;
import org.eclipse.jdt.internal.corext.refactoring.TypeContextChecker;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.CompletionContextRequestor;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("restriction")
public class AssoMappingSelectionDialog extends ElementListSelectionDialog {
	private StringButtonDialogField sbdf;
	private Text textController;
	private StubTypeContext fSuperClassStubTypeContext;
	private String fCurrType;
	protected IProject project;
	private KXmlFile xmlFile;
	private boolean xmlUpdated;
	private String mappingFileName;
	private String controlClass;
	private Class baseClass;
	private URLClassLoader urlClassLoader;
	private List<MoxController> elements;
	private List<MoxController> elementsToRemove;

	public AssoMappingSelectionDialog(Shell parent, ILabelProvider renderer) {
		super(parent, renderer);
		setMultipleSelection(true);
		setMessage("A associer aux mappings :");
	}

	public void setProject(IProject project) {
		this.project = project;
		if (project != null) {
			try {
				urlClassLoader = WorkbenchUtils.getURLClassPathLoader(JavaCore.create(project));
				baseClass = Class.forName("net.ko.mapping.IMappingControl", true, urlClassLoader);
			} catch (MalformedURLException | CoreException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	protected Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginLeft = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginWidth = 0;
		layout.marginRight = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(layout);
		return composite;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite fComposite = createComposite(parent);
		sbdf = new StringButtonDialogField(new IStringButtonAdapter() {

			@Override
			public void changeControlPressed(final DialogField arg0) {

			}

		});
		Label dispLbl = new Label(fComposite, SWT.NONE);
		dispLbl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dispLbl.setText("Classe contrôleur :");
		textController = sbdf.getTextControl(fComposite);
		textController.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textController.setMessage("Entrer un contrôleur...");

		JavaTypeCompletionProcessor superClassCompletionProcessor = new JavaTypeCompletionProcessor(false, false, true);
		superClassCompletionProcessor.setCompletionContextRequestor(new CompletionContextRequestor() {
			public StubTypeContext getStubTypeContext() {
				return getSuperClassStubTypeContext();
			}
		});
		ControlContentAssistHelper.createTextContentAssistant(textController, superClassCompletionProcessor);
		Control control = super.createDialogArea(parent);
		Composite fCompositeBt = createComposite(parent);
		Button btDeleteCtrl = new Button(fCompositeBt, SWT.NONE);
		btDeleteCtrl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btDeleteCtrl.setText("Supprimer les informations de contrôle");
		btDeleteCtrl.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent evt) {
				removeCtrl();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent evt) {
				// TODO Auto-generated method stub

			}
		});
		if (fCurrType != null) {
			textController.setText(fCurrType);
			updateOkState();
		}
		textController.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				updateOkState();
			}
		});
		setEmptyListMessage("Aucun mapping trouvé");
		setEmptySelectionMessage("Aucun mapping sélectionné");
		return control;
	}

	private void removeCtrl() {
		for (Object o : getSelectedElements()) {
			MoxController mController = (MoxController) o;
			mController.setClassControl("");
			mController.setMainControl(false);
			elementsToRemove.add(mController);
		}
		setListElements(elements.toArray());
		// updateOkState();
	}

	private void updateStatus(int severity, String message) {
		updateStatus(new Status(severity, Activator.PLUGIN_ID, severity, message, null));
	}

	private StubTypeContext getSuperClassStubTypeContext() {
		if (fSuperClassStubTypeContext == null) {
			fSuperClassStubTypeContext = TypeContextChecker.createSuperClassStubTypeContext(JavaTypeCompletionProcessor.DUMMY_CLASS_NAME, null, getDefaultPackage(getJavaProject()));
		}
		return fSuperClassStubTypeContext;
	}

	private IJavaProject getJavaProject() {
		if (project != null)
			return JavaCore.create(project);
		else
			return WorkbenchUtils.getJavaProject();
	}

	private IPackageFragment getDefaultPackage(IJavaProject proj) {
		try {
			return proj.getPackageFragmentRoots()[0].createPackageFragment("", true, null);
		} catch (JavaModelException e) {
		}
		return null;
	}

	public void setSelectionMapping(IStructuredSelection selection) throws JavaModelException {
		IType iType = null;
		IEditorPart activeEditor = WorkbenchUtils.getActiveEditor();
		if (activeEditor != null)
			if (activeEditor instanceof JavaEditor)
				iType = SelectionConverter.getTypeAtOffset((JavaEditor) activeEditor);
		if (iType == null)
			iType = getSelectedType(selection);
		if (iType != null) {
			if (implementsIMappingControl(iType.getFullyQualifiedName(), getJavaProject()))
				fCurrType = iType.getFullyQualifiedName();
		}
	}

	private IType getSelectedType(final IStructuredSelection selection) throws JavaModelException {
		if (selection.size() == 1 && selection.getFirstElement() instanceof IType) {
			final IType type = (IType) selection.getFirstElement();
			if (type.getCompilationUnit() != null && type.isClass())
				return type;
		} else if (selection.getFirstElement() instanceof ICompilationUnit) {
			final ICompilationUnit unit = (ICompilationUnit) selection.getFirstElement();
			final IType type = unit.findPrimaryType();
			if (type != null && type.isClass())
				return type;
		}
		return null;
	}

	public List<MoxController> openXML() throws IOException {

		KProperties properties = FrameworkUtils.koConfigFile();
		mappingFileName = properties.getProperty("mappingFile", "conf/mox.xml");
		controlClass = properties.getProperty("controlClass", "");
		List<MoxController> result = new ArrayList<>();
		xmlFile = new KXmlFile();
		try {
			xmlFile.loadFromFile(FrameworkUtils.getCompleteFileName(mappingFileName));
			NodeList nodes = xmlFile.getXmlObject().getElementsByTagName("mapping");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				String requestURL = element.getAttribute("requestURL");
				String responseURL = element.getAttribute("responseURL");
				String classControl = element.getAttribute("classControl");
				boolean mainControl = KString.isBooleanTrue(element.getAttribute("mainControl"));
				if (mainControl)
					classControl = controlClass;
				result.add(new MoxController(element, requestURL, responseURL, mainControl, classControl));
			}
			nodes = xmlFile.getXmlObject().getElementsByTagName("virtualMapping");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				String requestURL = element.getAttribute("requestURL");
				String responseURL = "[" + element.getAttribute("mappingFor") + "]";
				String classControl = element.getAttribute("classControl");
				boolean mainControl = KString.isBooleanTrue(element.getAttribute("mainControl"));
				if (mainControl)
					classControl = controlClass;
				result.add(new MoxController(element, requestURL, responseURL, mainControl, classControl));
			}
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void init() {
		elements = new ArrayList<>();
		elementsToRemove = new ArrayList<>();
		try {
			elements = openXML();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setElements(elements.toArray());
	}

	@Override
	protected void updateOkState() {
		if (verify() || elementsToRemove.size() > 0)
			super.updateOkState();
	}

	@Override
	protected boolean validateCurrentSelection() {
		return verify();
	}

	public boolean implementsIMappingControl(String jType, IJavaProject javaProject) {
		if (KString.isNotNull(jType) && baseClass != null) {
			try {
				Class clazz = Class.forName(jType, true, urlClassLoader);
				return baseClass.isAssignableFrom(clazz);
			} catch (ClassNotFoundException e) {
				return false;
			}
		}
		return false;
	}

	private boolean verify() {
		String controllerText = textController.getText();
		if (KString.isNotNull(controllerText)) {
			if (implementsIMappingControl(controllerText, getJavaProject())) {
				updateStatus(IStatus.OK, "Contrôleur valide");
				return true;
			}
			else
				updateStatus(IStatus.ERROR, controllerText + " n'est pas un contrôleur valide");
		} else {
			updateStatus(IStatus.ERROR, "Aucune classe contrôleur sélectionnée");
		}
		return false;
	}

	private void updateNode(String newController, MoxController mController) {

		Element e = mController.getElement();
		if (implementsIMappingControl(newController, getJavaProject())) {
			if (newController.equals(controlClass)) {
				if (!KString.isBooleanTrue(e.getAttribute("mainControl"))) {
					e.setAttribute("mainControl", "true");
					xmlUpdated = true;
				}
				if (KString.isNotNull(e.getAttribute("classControl"))) {
					e.removeAttribute("classControl");
					xmlUpdated = true;
				}
			} else {
				if (KString.isBooleanTrue(e.getAttribute("mainControl"))) {
					e.removeAttribute("mainControl");
					xmlUpdated = true;
				}
				if (!newController.equals(e.getAttribute("classControl"))) {
					e.setAttribute("classControl", newController);
					xmlUpdated = true;
				}
			}
			elementsToRemove.remove(mController);
		}

	}

	public void updateAll(String newController, List<MoxController> moxControllers) {

		xmlUpdated = false;
		for (MoxController mController : moxControllers) {
			updateNode(newController, mController);
		}
		for (MoxController mController : elementsToRemove) {
			deleteControllerNode(mController);
		}

		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (xmlUpdated) {
					String fileName = FrameworkUtils.getCompleteFileName(mappingFileName);
					xmlFile.saveAs(fileName);
					try {
						project.getFile(FrameworkUtils.getBaseFolder() + mappingFileName).refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e) {
					}
				}
			}
		});
	}

	private void deleteControllerNode(MoxController mController) {
		Element element = mController.getElement();
		element.removeAttribute("mainControl");
		element.removeAttribute("classControl");
		xmlUpdated = true;
	}

	@Override
	protected void okPressed() {
		List<MoxController> moxControllers = new ArrayList<>();
		for (Object o : getSelectedElements()) {
			moxControllers.add((MoxController) o);
		}
		updateAll(textController.getText(), moxControllers);
		super.okPressed();
	}
}
