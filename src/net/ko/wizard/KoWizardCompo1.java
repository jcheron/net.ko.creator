package net.ko.wizard;

import java.io.IOException;

import net.ko.bean.TemplateType;
import net.ko.creator.WorkbenchUtils;
import net.ko.utils.KProperties;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class KoWizardCompo1 extends Composite {
	private Table tableClasses;
	private TableViewer tvClasses;
	private IProject project = null;
	private TemplateType templateType = TemplateType.ttNone;
	private KoWizardPage1 wizardPage;
	private String className = "";
	private String packageName = "";
	private Text txtFolder;

	public KoWizardCompo1(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		Group grpType = new Group(this, SWT.NONE);
		FormData fd_grpType = new FormData();
		fd_grpType.right = new FormAttachment(100, -75);
		fd_grpType.left = new FormAttachment(0);
		grpType.setLayoutData(fd_grpType);
		grpType.setText("Type :");

		Button btnView = new Button(grpType, SWT.RADIO);
		btnView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setTemplateType(e);
			}
		});
		btnView.setBounds(10, 19, 386, 16);
		btnView.setText("Formulaire d'ajout/modification d'un objet (*.view)");
		btnView.setData(TemplateType.ttView);
		Button btnListe = new Button(grpType, SWT.RADIO);
		btnListe.setBounds(10, 41, 386, 16);
		btnListe.setText("Affichage d'une liste d'objets (*.list)");
		btnListe.setData(TemplateType.ttList);
		btnListe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setTemplateType(e);
			}
		});

		Button btnShow = new Button(grpType, SWT.RADIO);
		btnShow.setBounds(10, 63, 386, 16);
		btnShow.setText("Affichage d'un objet (*.show)");
		btnShow.setData(TemplateType.ttShow);
		btnShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setTemplateType(e);
			}
		});
		Button btnAutreTemplate = new Button(grpType, SWT.RADIO);
		btnAutreTemplate.setBounds(10, 88, 386, 16);
		btnAutreTemplate.setText("Autre template");
		btnAutreTemplate.setData(TemplateType.ttCustom);
		btnAutreTemplate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setTemplateType(e);
			}
		});

		Group grpClasse = new Group(this, SWT.NONE);
		FormData fd_grpClasse = new FormData();
		fd_grpClasse.bottom = new FormAttachment(100, -5);
		fd_grpClasse.top = new FormAttachment(grpType, 6);
		fd_grpClasse.left = new FormAttachment(0);
		fd_grpClasse.right = new FormAttachment(100, -75);
		grpClasse.setLayoutData(fd_grpClasse);
		grpClasse.setText("Classe :");

		tableClasses = new Table(grpClasse, SWT.BORDER | SWT.FULL_SELECTION);
		tableClasses.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				className = packageName + "." + item.getText().replace(".java", "");
				updateStatus();
			}
		});
		tableClasses.setBounds(10, 20, 386, 129);
		tvClasses = new TableViewer(tableClasses);

		Label lblDossier = new Label(this, SWT.NONE);
		FormData fd_lblDossier = new FormData();
		lblDossier.setLayoutData(fd_lblDossier);
		lblDossier.setText("Dossier :");

		txtFolder = new Text(this, SWT.BORDER);
		txtFolder.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				IProject prj = WorkbenchUtils.getProject(txtFolder.getText());
				if (prj != null)
					init(prj);
				wizardPage.dialogChanged();
			}
		});
		fd_grpType.bottom = new FormAttachment(txtFolder, 120, SWT.BOTTOM);
		fd_grpType.top = new FormAttachment(txtFolder, 6);
		fd_lblDossier.top = new FormAttachment(txtFolder, 3, SWT.TOP);
		fd_lblDossier.right = new FormAttachment(txtFolder, -6);
		FormData fd_txtFolder = new FormData();
		fd_txtFolder.right = new FormAttachment(100, -161);
		fd_txtFolder.left = new FormAttachment(0, 60);
		fd_txtFolder.top = new FormAttachment(0, 10);
		txtFolder.setLayoutData(fd_txtFolder);

		Button btnParcourir = new Button(this, SWT.NONE);
		btnParcourir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = showFileDialog("Choisir un dossier", "Sélectionner le dossier de création du template");
				if (path != null && !"".equals(path)) {
					txtFolder.setText(path);
				}
			}
		});
		FormData btParcourir = new FormData();
		btParcourir.right = new FormAttachment(100, -75);
		btParcourir.left = new FormAttachment(txtFolder, 6);
		btParcourir.top = new FormAttachment(lblDossier, -5, SWT.TOP);
		btnParcourir.setLayoutData(btParcourir);
		btnParcourir.setText("Parcourir...");
		tvClasses.setLabelProvider(new WorkbenchLabelProvider());
		tvClasses.setContentProvider(new ArrayContentProvider());
	}

	protected String showFileDialog(String text, String message) {
		String result = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		MyTreeSelectionDialog mf = new MyTreeSelectionDialog(window.getShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
		IFolder f = WorkbenchUtils.getAbsoluteFolder(txtFolder.getText());
		mf.setParams(f, text, message);
		if (mf.open() == ResourceSelectionDialog.OK) {
			Object[] selected = mf.getResult();
			IFolder folder = (IFolder) selected[0];
			result = folder.getFullPath().toString();
		}
		return result;
	}

	public void init(IProject project) {
		this.project = project;
		if (this.project != null) {
			try {
				packageName = getPackageName();
				wizardPage.setKoConfigExist(true);
				wizardPage.setProject(project);
				refreshClasses(packageName);
			} catch (IOException e1) {
				wizardPage.setKoConfigExist(false);
			}
			updateStatus();
		}
	}

	private String getPackageName() throws IOException {
		String result = "";
		String strFolder = "";
		if (WorkbenchUtils.isDynamicWebProject(project))
			strFolder = "WebContent/";
		strFolder = project.getLocation().toOSString() + "/" + strFolder;
		KProperties kp = new KProperties();
		kp.loadFromFile(strFolder + "config.ko");
		result = kp.getProperty("package");
		return result;
	}

	private void refreshClasses(String packageName) {
		IFolder folder = project.getFolder("src/" + WorkbenchUtils.packageToFolder(packageName));
		IResource resources[];
		tvClasses.getTable().clearAll();
		tvClasses.refresh();
		if (folder.exists()) {
			try {
				resources = folder.members();
				for (int i = 0; i < resources.length; i++) {
					if (resources[i].exists()) {
						if (tvClasses.testFindItem(resources[i]) != null)
							tvClasses.replace(resources[i], i);
						else
							tvClasses.add(resources[i]);
					}
				}
				wizardPage.setClassGenerated(true);
			} catch (CoreException e) {
				wizardPage.setClassGenerated(false);
			}
		} else
		{
			wizardPage.setClassGenerated(false);
		}
		setClassName("");
		updateStatus();
	}

	public IProject getProject() {
		return project;
	}

	public TemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(SelectionEvent e) {
		Button btn = (Button) e.getSource();
		this.templateType = (TemplateType) btn.getData();
		tableClasses.setEnabled((!templateType.equals(TemplateType.ttCustom)));
		updateStatus();
	}

	private void updateStatus() {
		wizardPage.setPageComplete(templateType.equals(TemplateType.ttCustom) || (tableClasses.getSelectionIndex() != -1));
		wizardPage.dialogChanged();
	}

	public KoWizardPage1 getWizardPage() {
		return wizardPage;
	}

	public void setWizardPage(KoWizardPage1 wizardPage) {
		this.wizardPage = wizardPage;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Text getTxtFolder() {
		return txtFolder;
	}
}
