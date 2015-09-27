package net.ko.wizard.display;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.xml.parsers.ParserConfigurationException;

import net.ko.bean.display.KoxDisplay;
import net.ko.creator.Activator;
import net.ko.creator.WorkbenchUtils;
import net.ko.creator.utils.FrameworkUtils;
import net.ko.utils.KProperties;
import net.ko.utils.KString;
import net.ko.utils.KXmlFile;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Folder;
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
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("restriction")
public class AssoDisplaySelectionDialog extends CheckedTreeSelectionDialog {
	private Group grpOptions = null;
	private StubTypeContext fSuperClassStubTypeContext;
	private String fCurrType;
	private StringButtonDialogField sbdf;
	private Text textDisplay;
	private FormToolkit formToolkit;
	private Table table;
	private TableViewer tableViewer;
	private KXmlFile xmlFile;
	private boolean xmlUpdated;
	private String validationFileName;
	private IProject project;

	public AssoDisplaySelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
		setContainerMode(true);
		setMessage("A associer aux templates :");
		addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof File) {
					File f = ((File) element);
					String ext = f.getFileExtension();
					return "view".equals(ext) || "list".equals(ext) || "show".equals(ext);
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
	protected Control createDialogArea(Composite parent) {
		Composite fComposite = createComposite(parent);
		sbdf = new StringButtonDialogField(new IStringButtonAdapter() {

			@Override
			public void changeControlPressed(final DialogField arg0) {

			}

		});
		Label dispLbl = new Label(fComposite, SWT.NONE);
		dispLbl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dispLbl.setText("Classe du display :");
		textDisplay = sbdf.getTextControl(fComposite);
		textDisplay.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textDisplay.setMessage("Entrer un display...");
		textDisplay.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent evt) {
				updateOKStatus();
			}
		});
		if (fCurrType != null)
			textDisplay.setText(fCurrType);
		JavaTypeCompletionProcessor superClassCompletionProcessor = new JavaTypeCompletionProcessor(false, false, true);
		superClassCompletionProcessor.setCompletionContextRequestor(new CompletionContextRequestor() {
			public StubTypeContext getStubTypeContext() {
				return getSuperClassStubTypeContext();
			}
		});
		ControlContentAssistHelper.createTextContentAssistant(textDisplay, superClassCompletionProcessor);
		Control control = super.createDialogArea(parent);
		Composite composite = createComposite(parent);

		grpOptions = new Group(composite, SWT.NONE);
		grpOptions.setLayout(new GridLayout());
		grpOptions.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grpOptions.setText("Affectation aux classes dans kox.xml :");

		GridData gridDataTable = new GridData(GridData.FILL_HORIZONTAL);
		// gridDataTable.widthHint = 300;
		gridDataTable.horizontalIndent = 1;
		gridDataTable.heightHint = 125;
		table = getFormToolkit().createTable(grpOptions, SWT.MULTI | SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		table.setHeaderVisible(false);
		table.setLayoutData(gridDataTable);
		table.setLinesVisible(false);
		tableViewer = new TableViewer(table);
		tableViewer.setLabelProvider(new DisplayClassLabelProvider());
		String[] COLUMNS = new String[] { "Classe" };
		for (String element : COLUMNS) {
			TableColumn col = new TableColumn(table, SWT.CENTER);
			col.setText(element);
		}
		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(0));
		table.setLayout(tlayout);
		List<KoxDisplay> classes;
		try {
			classes = openXML();
			for (KoxDisplay koxD : classes) {
				tableViewer.add(koxD);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// table.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return control;
	}

	private FormToolkit getFormToolkit() {
		if (formToolkit == null) {
			formToolkit = new FormToolkit(Display.getCurrent());
		}
		return formToolkit;
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

	public void setSelectionDisplay(IStructuredSelection selection) throws JavaModelException {
		IType iType = null;
		IEditorPart activeEditor = WorkbenchUtils.getActiveEditor();
		if (activeEditor != null)
			if (activeEditor instanceof JavaEditor)
				iType = SelectionConverter.getTypeAtOffset((JavaEditor) activeEditor);
		if (iType == null)
			iType = getSelectedType(selection);
		if (iType != null) {
			if ("KObjectDisplay".equals(iType.getSuperclassName()))
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

	private void updateStatus(int severity, String message) {
		updateStatus(new Status(severity, Activator.PLUGIN_ID, severity, message, null));
	}

	@Override
	protected void updateOKStatus() {
		String displayText = textDisplay.getText();
		if (KString.isNotNull(displayText)) {
			try {
				IType displayType = getJavaProject().findType(displayText);
				if (displayType != null && "KObjectDisplay".equals(displayType.getSuperclassName()))
					super.updateOKStatus();
				else
					updateStatus(IStatus.ERROR, textDisplay.getText() + " n'est pas un display valide");
			} catch (JavaModelException e) {
				updateStatus(IStatus.ERROR, "Impossible d'instancier " + textDisplay.getText());
			}
		} else {
			updateStatus(IStatus.ERROR, "Aucune classe de display sélectionnée");
		}
	}

	public List<KoxDisplay> openXML() throws IOException {

		KProperties properties = FrameworkUtils.koConfigFile();
		validationFileName = properties.getProperty("validationFile", "conf/kox.xml");
		List<KoxDisplay> result = new ArrayList<>();
		xmlFile = new KXmlFile();
		try {
			xmlFile.loadFromFile(FrameworkUtils.getCompleteFileName(validationFileName));
			NodeList nodes = xmlFile.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeName().equals("class")) {
					String className = ((Element) nodes.item(i)).getAttribute("name");
					String display = ((Element) nodes.item(i)).getAttribute("display");
					if (className != null && className != "") {
						result.add(new KoxDisplay(className, display));
					}
				}
			}
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void updateAll(String newDisplay, List<KoxDisplay> koxDisplays, List<File> files) {
		for (File f : files) {
			try {
				updateOneTemplate(f, newDisplay);
			} catch (CoreException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		xmlUpdated = false;
		NodeList nodes = xmlFile.getXmlObject().getElementsByTagName("class");
		for (KoxDisplay kDisplay : koxDisplays) {
			for (int j = 0; j < nodes.getLength(); j++) {
				Element e = (Element) nodes.item(j);
				if (e.getAttribute("name").equals(kDisplay.getClassName())) {
					if (!newDisplay.equals(e.getAttribute("display"))) {
						e.setAttribute("display", newDisplay);
						xmlUpdated = true;
					}
				}
			}
		}
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (xmlUpdated) {
					String fileName = FrameworkUtils.getCompleteFileName(validationFileName);
					xmlFile.saveAs(fileName);
					try {
						project.getFile(FrameworkUtils.getBaseFolder() + validationFileName).refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e) {
					}
				}
			}
		});
	}

	private void updateOneTemplate(File aFile, String newDisplay) throws CoreException, IOException {
		InputStream in = aFile.getContents();
		InputStreamReader is = new InputStreamReader(in);
		boolean fileUpdated = false;
		String content = FrameworkUtils.convertStreamToString(is);
		is.close();
		if (!content.contains("{#koDisplay:" + newDisplay + "#}")) {
			fileUpdated = true;
			if (content.contains("{#koDisplay:")) {
				content = content.replaceFirst("\\{\\#koDisplay\\:(.*?)\\#\\}", Matcher.quoteReplacement("{#koDisplay:") + newDisplay + Matcher.quoteReplacement("#}"));
			} else {
				content = "{#koDisplay:" + newDisplay + "#}\n" + content;
			}
		}
		if (fileUpdated) {
			ByteArrayInputStream in2 = new ByteArrayInputStream(content.getBytes());
			aFile.setContents(in2, true, true, null);
			in2.close();
		}
	}

	@Override
	protected void okPressed() {
		final String newDisplay = textDisplay.getText();
		final List<KoxDisplay> koxDisplays = new ArrayList<>();
		for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
			if (table.getItem(i).getChecked()) {
				KoxDisplay kDisplay = (KoxDisplay) tableViewer.getElementAt(i);
				koxDisplays.add(kDisplay);
			}
		}
		List<File> files = new ArrayList<>();
		for (Object element : getTreeViewer().getCheckedElements()) {
			if (element instanceof File) {
				files.add((File) element);
			}
		}
		updateAll(newDisplay, koxDisplays, files);
		super.okPressed();
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
