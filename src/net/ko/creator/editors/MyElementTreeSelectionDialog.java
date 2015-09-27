package net.ko.creator.editors;

import net.ko.creator.WorkbenchUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

public class MyElementTreeSelectionDialog extends ElementTreeSelectionDialog {

	@Override
	protected void updateStatus(IStatus status) {
		super.updateStatus(status);
		boolean web=false;
		Object[] result = getResult();
		if(result.length>0){
			IProject project = (IProject) result[0];
			if(project!=null){
				String baseFolder="/";
				if(WorkbenchUtils.isDynamicWebProject(project)){
					web=true;
					baseFolder="/WebContent/";
				}
				createConfigFile=checkFileExists(project, ckCreateConfigFile, baseFolder+"config.ko", "Mettre à jour le fichier de configuration (existant)", "Créer le fichier de configuration config.ko");
				createCacheFile=checkFileExists(project, ckCreateCacheFile, "/src/ehcache.xml", "Remplacer le fichier de gestion du cache (existant)", "Créer le fichier de gestion du cache");
				createControllerFile=checkFileExists(project, ckCreateControllerFile, baseFolder+"controller/mox.xml", "Remplacer le fichier contrôleur (existant)", "Créer le fichier contrôleur");
				createErFile=checkFileExists(project, ckCreateErFile, baseFolder+"controller/er.properties", "Remplacer le fichier d'expressions régulières (existant)", "Créer le fichier d'expressions régulières");
				createMessagesFile=checkFileExists(project, ckCreateMessagesFile, baseFolder+"controller/messages.properties", "Remplacer le fichier de messages (existant)", "Créer le fichier de messages par défaut");
				createCssHTMLFile=checkFileExists(project, ckCreateCssHTMLFile, baseFolder+"css/css.properties", "Remplacer les fichiers HTML et css (existants)", "Créer les fichiers HTML par défaut");
				ckCreateCssHTMLFile.setVisible(web);
			}
		}
	}
	private boolean checkFileExists(IProject project,Button ck,String fileName,String messageExists,String messageNotExists){
		IFile file = project.getFile(fileName);
		if(!file.exists())
			ck.setText(messageNotExists);
		else
			ck.setText(messageExists);
		ck.setSelection(!file.exists());
		return !file.exists();
	}
	private Group grpOptions = null;
	private Button ckCreateConfigFile = null;
	private Button ckCreateControllerFile = null;
	private Button ckCreateMessagesFile = null;
	private Button ckCreateCacheFile=null;
	private Button ckCreateErFile=null;
	private Button ckCreateCssHTMLFile=null;
	private boolean createControllerFile=false;
	private boolean createCacheFile=false;
	private boolean createMessagesFile=false;
	private boolean createErFile=false;
	private boolean createConfigFile=false;
	private boolean createCssHTMLFile=false;
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Control ctrl=super.createDialogArea(parent);
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginLeft = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginWidth = 0;
		layout.marginRight = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(layout);
		
		grpOptions = new Group(composite, SWT.NONE);
		grpOptions.setLayout(new GridLayout());
		grpOptions.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grpOptions.setText("Options de création :");
		
		ckCreateConfigFile = new Button(grpOptions, SWT.CHECK);
		ckCreateConfigFile.setText("Créer le fichier de configuration config.ko");
		ckCreateConfigFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ckCreateConfigFile.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createConfigFile=ckCreateConfigFile.getSelection();
			}
		});		
		ckCreateControllerFile = new Button(grpOptions, SWT.CHECK);
		ckCreateControllerFile.setText("Ajouter le contrôleur par défaut");
		ckCreateControllerFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ckCreateControllerFile.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createControllerFile=ckCreateControllerFile.getSelection();
			}
		});
		ckCreateMessagesFile = new Button(grpOptions, SWT.CHECK);
		ckCreateMessagesFile.setText("Créer le fichier de messages par défaut");
		ckCreateMessagesFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ckCreateMessagesFile.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createMessagesFile=ckCreateMessagesFile.getSelection();
			}
		});
		ckCreateErFile = new Button(grpOptions, SWT.CHECK);
		ckCreateErFile.setText("Créer le fichier des expressions régulières");
		ckCreateErFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ckCreateErFile.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createErFile=ckCreateErFile.getSelection();
			}
		});
		ckCreateCacheFile = new Button(grpOptions, SWT.CHECK);
		ckCreateCacheFile.setText("Créer le fichier de gestion du cache");
		ckCreateCacheFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ckCreateCacheFile.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createCacheFile=ckCreateCacheFile.getSelection();
			}
		});
		
		ckCreateCssHTMLFile = new Button(grpOptions, SWT.CHECK);
		ckCreateCssHTMLFile.setText("Créer les fichiers HTML et Css");
		ckCreateCssHTMLFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ckCreateCssHTMLFile.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createCssHTMLFile=ckCreateCssHTMLFile.getSelection();
			}
		});
		return ctrl;
	}
	public boolean isConfigFileSelected(){
		return createConfigFile;
	}
	public boolean isCacheFileSelected(){
		return createCacheFile;
	}
	public boolean isControllerFileSelected(){
		return createControllerFile;
	}
	public boolean isMessagesFileSelected(){
		return createMessagesFile;
	}
	public boolean isErFileSelected(){
		return createErFile;
	}	
	public MyElementTreeSelectionDialog(Shell parent,
			ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
		
	}
	public boolean isCssHTMLFileSelected() {
		return createCssHTMLFile;
	}

}
