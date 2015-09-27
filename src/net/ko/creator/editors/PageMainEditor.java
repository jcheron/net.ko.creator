package net.ko.creator.editors;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import net.ko.bean.ContentOutlineBeanElement;
import net.ko.creator.Activator;
import net.ko.creator.KernelCreator;
import net.ko.creator.WorkbenchUtils;
import net.ko.creator.editors.images.Images;
import net.ko.db.KDataBase;
import net.ko.db.provider.KDBMysql;
import net.ko.db.provider.KDBOdbc;
import net.ko.db.provider.KDBOracle;
import net.ko.events.EventFile;
import net.ko.events.EventFileListener;
import net.ko.kobject.KSession;
import net.ko.utils.KString;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.osgi.framework.Bundle;

public class PageMainEditor extends EditorComposite {
	private Composite compoMain = null;
	private Composite compoMain_1;
	private KSession ks = null; // @jve:decl-index=0:
	private CLabel cLabel1 = null;
	private Table table = null;
	private TableViewer tableViewer = null;
	private Composite compoLeft1 = null;
	private Composite compoLeft1_1;
	private Composite compoLeft1_ = null;
	private Composite compoLeft1__1;
	private Label host = null;
	private Text txtHost = null;
	private Label lblPort = null;
	private Text txtPort = null;
	private CLabel cLabel = null;
	private Text txtUser = null;
	private Label filler = null;
	private Label label = null;
	private Text txtPassword = null;
	private Label filler2 = null;
	private Label filler3 = null;
	private Link lkConnect = null;
	private Composite compoLeft2 = null;
	private Composite compoLeft2_1;
	private Composite compoRight1 = null;
	private Composite compoRight1_1;
	private Composite compoRight1_ = null;
	private Label lblPackage = null;
	private Text txtPackage = null;
	private Composite compoRight2 = null;
	private Composite compoRight2_1;
	private Composite compoRight2_ = null;
	private Combo cmbBases = null;
	private Label lblBases = null;
	private Button ckAllTables = null;
	private Composite thisComposite = null;
	private Label imgGenerate = null;
	private Link lkGenerate = null;
	private Table lstclasses = null;
	private TableViewer tableViewer2 = null;
	private String packageName = "";
	private Label lblBuildPath = null;
	private Button button = null;
	private Label imgBuildPath = null;
	private Label lblDbType = null;
	private Combo cmbDbType = null;
	private Button ckEcrase = null;
	private Link lkCache = null;
	private Label imgCache = null;
	private Text txtDbOptions = null;
	private Label lblOptions = null;
	private FormToolkit formToolkit = null; // @jve:decl-index=0:visual-constraint=""
	private Form form = null;
	private Section sectionSGBD = null;
	private Section sectionOptions = null;
	private Section sectionTables = null;
	private Section sectionGeneration = null;
	private Composite compoLeft2_ = null;
	private Label lblUser = null;
	private Label lblPwd = null;
	private Button ckGenerateController = null;
	private Label imgPackage = null;

	public PageMainEditor(Composite parent, int style) {
		super(parent, style);
		initialize();
		cobElements.add(new ContentOutlineBeanElement("Serveur", "", txtHost));
		cobElements.add(new ContentOutlineBeanElement("Port", "", txtPort));
		cobElements.add(new ContentOutlineBeanElement("User", "", txtUser));
		cobElements.add(new ContentOutlineBeanElement("Base", "", cmbBases));
		cobElements.add(new ContentOutlineBeanElement("Package", "", txtPackage));
	}

	private ModifyListener txtModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			setDirty(true);
			updateOutlinePage();
		}
	};

	private void initialize() {
		this.setLayout(new FillLayout());
		this.setBounds(new Rectangle(0, 0, 900, 600));
		createForm();
		createCompoMain();
	}

	/**
	 * This method initializes compoMain
	 * 
	 */
	private void createCompoMain() {
		compoMain_1 = getFormToolkit().createComposite(form.getBody(), SWT.NONE);
		createCompoLeft1();
		createCompoRight1();
		createCompoLeft2();
		createCompoRight2();
		addEvents();
	}

	private void addEvents() {
		txtHost.addModifyListener(txtModifyListener);
		txtPort.addModifyListener(txtModifyListener);
		txtDbOptions.addModifyListener(txtModifyListener);
		txtPackage.addModifyListener(txtModifyListener);
		txtPassword.addModifyListener(txtModifyListener);
		txtUser.addModifyListener(txtModifyListener);
		cmbBases.addModifyListener(txtModifyListener);
	}

	public void setLblBuildPath(String path) {
		lblBuildPath.setText(path);
	}

	private void showError(String message, int image) {
		message = KString.getBefore(message, "\n");
		form.setMessage(message, IMessageProvider.ERROR);
	}

	private void cleanError() {
		form.setMessage("");
	}

	public String getHost() {
		return txtHost.getText();
	}

	public void setHost(String host) {
		if (host != null)
			this.txtHost.setText(host);
	}

	public String getPort() {
		return txtPort.getText();
	}

	public void setPort(String port) {
		if (port != null)
			this.txtPort.setText(port);
	}

	public String getBase() {
		return cmbBases.getText();
	}

	public void setBase(String base) {
		if (base != null)
			this.cmbBases.setText(base);
	}

	public String getUser() {
		return txtUser.getText();
	}

	public void setUser(String user) {
		if (user != null)
			this.txtUser.setText(user);
	}

	public String getPassword() {
		return txtPassword.getText();
	}

	public void setPassword(String password) {
		if (password != null)
			this.txtPassword.setText(password);
	}

	public String getPackage() {
		return txtPackage.getText();
	}

	public void setPackage(String pack) {
		if (pack != null)
			this.txtPackage.setText(pack);
	}

	public String getOptions() {
		return txtDbOptions.getText();
	}

	public void setOptions(String dbOptions) {
		if (dbOptions != null)
			this.txtDbOptions.setText(dbOptions);
	}

	public String getClasses() {
		String result = "";
		for (int i = 0; i < table.getItemCount(); i++) {
			if (table.getItem(i).getChecked())
				result += table.getItem(i).getText() + ";";
		}
		if (result.endsWith(";"))
			result = result.substring(0, result.length() - 1);
		return result;
	}

	public void setClasses(String classes) {
		for (int i = 0; i < table.getItemCount(); i++)
			table.getItem(i).setChecked(false);
		if (classes != null) {
			String[] cls = classes.split(";");
			for (int i = 0; i < cls.length; i++) {
				// table.
				int j = getItemByText(cls[i]);
				if (j != -1)
					table.getItem(j).setChecked(true);
			}
		}
	}

	private int getItemByText(String text) {
		int i = 0;
		boolean trouve = false;
		while (i < table.getItemCount() && !trouve) {
			trouve = text.equalsIgnoreCase(table.getItem(i).getText());
			i++;
		}
		if (trouve)
			return i - 1;
		else
			return -1;
	}

	private KDataBase connectToDb(KSession ks, String host, String user, String pass, String base, String port, String options) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		KDataBase result = null;
		switch (cmbDbType.getSelectionIndex()) {
		case 0:
			if (base.equals(""))
				base = "mysql";
			result = ks.connect(new KDBMysql(), host, user, pass, base, port, options);
			break;
		case 1:
			result = ks.connect(new KDBOracle(), host, user, pass, base, port, options);
			break;
		case 2:
			result = ks.connect(new KDBOdbc(), host, user, pass, base, port, options);
			break;
		default:
			result = ks.connect(new KDBMysql(), host, user, pass, base, port, options);
			break;
		}
		return result;
	}

	private KDataBase connectToDb(KSession ks, Boolean noBase) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		String host = this.getHost();
		String user = this.getUser();
		String pass = this.getPassword();
		String base = this.getBase();
		String port = this.getPort();
		String options = this.getOptions();
		if (noBase)
			base = "";
		KDataBase db = connectToDb(ks, host, user, pass, base, port, options);
		ks.setDb(db);
		return db;
	}

	private KDataBase connectToDb(KSession ks) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		return connectToDb(ks, false);
	}

	private KDataBase connectToDb() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		ks = new KSession();
		return connectToDb(ks);

	}

	private void listDb() {
		ks = new KSession();
		try {
			KDataBase db = connectToDb(ks, true);
			ArrayList<String> oItems = db.getDbNames();
			String[] items = new String[oItems.size()];
			String base = cmbBases.getText();
			cmbBases.setItems(oItems.toArray(items));
			cmbBases.setText(base);
			cmbBases.setEnabled(true);
			db.close();
			cleanError();
		} catch (Exception e) {
			showError(e.getMessage(), IMessageProvider.WARNING);
			lkConnect.setEnabled(false);
		}
	}

	private void addTablesToList() {
		ArrayList<String> tableList = null;
		try {
			table.removeAll();
			tableList = ks.getDb().getTableNames(getBase());
			for (String t : tableList) {
				tableViewer.add(t);
				String classes = ";" + this.multiPageEditor.getProperty("classes") + ";";
				if (classes.contains(";" + t + ";"))
					table.getItem(table.getItemCount() - 1).setChecked(true);
			}
			cleanError();

		} catch (SQLException e) {
			showError(e.getMessage(), IMessageProvider.ERROR);
		}

	}

	public String getBaseFolder() {
		String result = "";
		if (WorkbenchUtils.isDynamicWebProject())
			result = "WebContent/";
		return result;
	}

	public void checkCache() {
		IFile ehcacheFile = WorkbenchUtils.getActiveProject().getFile("/src/ehcache.xml");
		if (ehcacheFile.exists()) {
			imgCache.setImage(Images.getImage(Images.DB_CACHE));
			lkCache.setText("<a>Gestion du cache</a>");
			lkCache.setEnabled(true);
		}
		else {
			imgCache.setImage(Images.getImage(Images.EXCLAMATION));
			lkCache.setText("ehcache.xml absent");
			lkCache.setEnabled(false);
		}
	}

	/**
	 * This method initializes compoLeft1
	 * 
	 */
	private void createCompoLeft1() {
		compoMain_1.setLayout(new FormLayout());
		compoLeft1_1 = getFormToolkit().createComposite(compoMain_1, SWT.NONE);
		FormData fd_compoLeft1 = new FormData();
		fd_compoLeft1.right = new FormAttachment(0, 369);
		fd_compoLeft1.top = new FormAttachment(0, 10);
		fd_compoLeft1.left = new FormAttachment(0, 10);
		compoLeft1_1.setLayoutData(fd_compoLeft1);
		createSectionSGBD();
	}

	/**
	 * This method initializes compoLeft1_
	 * 
	 */
	private void createCompoLeft1_() {
		GridData gridData22 = new GridData();
		gridData22.widthHint = -1;
		GridData gridData33 = new GridData();
		gridData33.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData32 = new GridData();
		gridData32.horizontalSpan = 3;
		gridData32.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData29 = new GridData();
		gridData29.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData28 = new GridData();
		gridData28.horizontalIndent = 10;
		GridData gridData13 = new GridData();
		gridData13.horizontalIndent = 1;
		gridData13.widthHint = -1;
		GridData gridData4 = new GridData();
		gridData4.horizontalSpan = 3;
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = GridData.END;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.END;
		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = GridData.END;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = GridData.END;
		GridLayout glLeft1 = new GridLayout();
		glLeft1.numColumns = 4;
		glLeft1.marginWidth = 10;
		compoLeft1__1 = getFormToolkit().createComposite(sectionSGBD, SWT.NONE);
		compoLeft1__1.setLayout(glLeft1);
		compoLeft1__1.setLayoutData(gridData13);
		lblDbType = getFormToolkit().createLabel(compoLeft1__1, "Fournisseur :");
		lblDbType.setLayoutData(gridData29);
		createCmbDbType();
		host = getFormToolkit().createLabel(compoLeft1__1, "Serveur :");
		host.setLayoutData(gridData10);
		txtHost = getFormToolkit().createText(compoLeft1__1, "");

		lblPort = getFormToolkit().createLabel(compoLeft1__1, "Port :");
		lblPort.setLayoutData(gridData12);
		txtPort = getFormToolkit().createText(compoLeft1__1, "");
		txtPort.setLayoutData(gridData22);

		lblUser = getFormToolkit().createLabel(compoLeft1__1, "User :");
		lblUser.setLayoutData(gridData8);
		txtUser = new Text(compoLeft1__1, SWT.BORDER);
		Label filler1 = new Label(compoLeft1__1, SWT.NONE);
		filler = new Label(compoLeft1__1, SWT.NONE);
		lblPwd = getFormToolkit().createLabel(compoLeft1__1, "Mot de passe :");
		lblPwd.setLayoutData(gridData9);
		txtPassword = new Text(compoLeft1__1, SWT.BORDER);
		txtPassword.setEchoChar('*');
		txtPassword.setTextLimit(250);
		filler2 = new Label(compoLeft1__1, SWT.NONE);
		filler3 = new Label(compoLeft1__1, SWT.NONE);
		lblBases = getFormToolkit().createLabel(compoLeft1__1, "Base de données :");
		lblBases.setLayoutData(gridData28);
		createCmbBases();
		lblOptions = getFormToolkit().createLabel(compoLeft1__1, "Options :");
		lblOptions.setLayoutData(gridData33);
		txtDbOptions = new Text(compoLeft1__1, SWT.BORDER);
		txtDbOptions.setLayoutData(gridData32);
		createImgConnect();

		Label label_1 = getFormToolkit().createLabel(compoLeft1__1, "", SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setImage(Images.getImage(Images.DB_CONNECT));
		lkConnect = new Link(compoLeft1__1, SWT.NONE);
		lkConnect.setText("<a>Connexion à la base de données</a>");
		lkConnect.setLayoutData(gridData4);

		lkConnect.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				connectToDbAndAddTables();
			}
		});

	}

	public void connectToDbAndAddTables() {
		try {
			connectToDb();
			addTablesToList();
			cleanError();
			lkGenerate.setEnabled(true);
		} catch (Exception e1) {
			showError(e1.getMessage(), IMessageProvider.ERROR);
			lkGenerate.setEnabled(false);
		}

	}

	/**
	 * This method initializes imgConnect
	 * 
	 */
	private void createImgConnect() {
	}

	/**
	 * This method initializes compoLeft2
	 * 
	 */
	private void createCompoLeft2() {
		compoLeft2_1 = getFormToolkit().createComposite(compoMain_1, SWT.NONE);
		FormData fd_compoLeft2 = new FormData();
		fd_compoLeft2.top = new FormAttachment(0, 245);
		fd_compoLeft2.left = new FormAttachment(0, 10);
		compoLeft2_1.setLayoutData(fd_compoLeft2);
		compoLeft2_1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		createSectionTables();

	}

	public void selectAllTables(boolean value) {
		for (int i = 0; i < table.getItemCount(); i++)
			table.getItem(i).setChecked(value);
	}

	/**
	 * This method initializes compoRight1
	 * 
	 */
	private void createCompoRight1() {
		compoRight1_1 = getFormToolkit().createComposite(compoMain_1, SWT.NONE);
		FormData fd_compoRight1 = new FormData();
		fd_compoRight1.right = new FormAttachment(100, -20);
		fd_compoRight1.top = new FormAttachment(0, 10);
		fd_compoRight1.left = new FormAttachment(0, 374);
		compoRight1_1.setLayoutData(fd_compoRight1);
		createSectionOptions();
	}

	/**
	 * This method initializes compoRight1_
	 * 
	 */
	private void createCompoRight1_() {
		GridData gridData16 = new GridData();
		gridData16.horizontalIndent = 3;
		GridData gridData14 = new GridData();
		gridData14.horizontalSpan = 4;
		gridData14.horizontalIndent = 3;
		GridData gridData31 = new GridData();
		gridData31.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData31.horizontalIndent = 3;
		GridData gridData210 = new GridData();
		gridData210.horizontalSpan = 3;
		GridData gridData111 = new GridData();
		gridData111.horizontalIndent = 3;
		gridData111.horizontalSpan = 4;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData2.horizontalIndent = 3;
		GridData gridData110 = new GridData();
		gridData110.grabExcessHorizontalSpace = true;
		gridData110.horizontalSpan = 2;
		gridData110.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData15 = new GridData();
		gridData15.horizontalIndent = 10;
		gridData15.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData15.grabExcessHorizontalSpace = true;
		gridData15.widthHint = 320;
		GridData gridData = new GridData();
		gridData.widthHint = -1;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.horizontalSpan = 2;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 4;
		compoRight1_ = getFormToolkit().createComposite(sectionOptions,
				SWT.NONE);
		compoRight1_.setLayout(gridLayout1);
		compoRight1_.setLayoutData(gridData15);
		imgPackage = getFormToolkit().createLabel(compoRight1_, "label");
		imgPackage.setImage(Images.getImage(Images.PACKAGE));
		imgPackage.setLayoutData(gridData16);
		lblPackage = getFormToolkit().createLabel(compoRight1_, "Package :");
		txtPackage = new Text(compoRight1_, SWT.BORDER);
		txtPackage.setLayoutData(gridData);

		ckEcrase = getFormToolkit().createButton(compoRight1_, "Ecraser les fichiers de classe existants", SWT.CHECK);
		ckGenerateController = getFormToolkit().createButton(compoRight1_,
				"Générer le fichier de validation kox.xml", SWT.CHECK);
		ckGenerateController.setLayoutData(gridData14);
		ckGenerateController.setSelection(true);
		imgBuildPath = new Label(compoRight1_, SWT.NONE);
		imgBuildPath.setText("");
		imgBuildPath.setLayoutData(gridData2);
		imgBuildPath.setImage(Images.getImage(Images.SUCCESS));
		lblBuildPath = new Label(compoRight1_, SWT.NONE);
		lblBuildPath.setText("KoLibrary absente");
		lblBuildPath.setLayoutData(gridData110);
		button = getFormToolkit().createButton(compoRight1_, "Build path...", SWT.FLAT);
		ckEcrase.setLayoutData(gridData111);
		ckEcrase.setForeground(JFaceResources.getColorRegistry().get(
				JFacePreferences.HYPERLINK_COLOR));
		imgCache = new Label(compoRight1_, SWT.NONE);
		imgCache.setText("Label");
		imgCache.setImage(Images.getImage(Images.DB_CACHE));
		imgCache.setLayoutData(gridData31);
		lkCache = new Link(compoRight1_, SWT.NONE);
		lkCache.setText("<a>Gestion du cache</a>");
		lkCache.setLayoutData(gridData210);
		lkCache.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				openFile("/src/ehcache.xml");
			}
		});
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog fd = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
				fd.setText("Ajout de Kobject-library dans le build path");
				Object cpE = WorkbenchUtils.getKobjectEntry(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor());
				if (cpE != null && cpE instanceof IClasspathEntry)
					fd.setFilterPath(((IClasspathEntry) cpE).getPath().toString());
				String[] filterExt = { "*.jar", "*.*" };
				fd.setFilterExtensions(filterExt);
				final String selected = fd.open();
				if (selected != null) {
					WorkbenchUtils.addClassPathEntry(selected);
					IProject project = WorkbenchUtils.getActiveProject();
					try {
						if (project != null)
							project.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e1) {
					}
					updateBuildPathStatus(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor());
				}
			}
		});

		txtPackage.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusGained(org.eclipse.swt.events.FocusEvent e) {
				packageName = txtPackage.getText();
			}

			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (!packageName.equals(txtPackage.getText()))
					refreshLstClasses();
			}
		});
	}

	public void openFile(String fileName) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFile file = WorkbenchUtils.getActiveProject().getFile(fileName);
		try {
			IDE.openEditor(page, file);
			cleanError();
		} catch (PartInitException e1) {
			showError(e1.getMessage(), IMessageProvider.WARNING);
		}
	}

	public void updateBuildPathStatus(IEditorPart editor) {
		String koFile = WorkbenchUtils.getKobjectEntryName(editor);
		String img = Images.SUCCESS;
		if (koFile.equals("")) {
			setLblBuildPath("KoLibrary absente");
			img = Images.EXCLAMATION;
		}
		else
			setLblBuildPath(koFile);
		imgBuildPath.setImage(Images.getImage(img));
	}

	/**
	 * This method initializes compoRight2
	 * 
	 */
	private void createCompoRight2() {
		compoRight2_1 = getFormToolkit().createComposite(compoMain_1, SWT.NONE);
		FormData fd_compoRight2 = new FormData();
		fd_compoRight2.right = new FormAttachment(100, -20);
		fd_compoRight2.top = new FormAttachment(0, 245);
		fd_compoRight2.left = new FormAttachment(0, 374);
		compoRight2_1.setLayoutData(fd_compoRight2);
		createSectionGeneration();
	}

	/**
	 * This method initializes compoRight2_
	 * 
	 */
	private void createCompoRight2_() {
		GridData gridData26 = new GridData();
		gridData26.grabExcessHorizontalSpace = true;
		gridData26.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		compoRight2_ = getFormToolkit().createComposite(sectionGeneration, SWT.NONE);
		compoRight2_.setLayoutData(gridData26);
		compoRight2_.setLayout(new FormLayout());
		imgGenerate = new Label(compoRight2_, SWT.NONE);
		FormData fd_imgGenerate = new FormData();
		fd_imgGenerate.top = new FormAttachment(0, 5);
		fd_imgGenerate.left = new FormAttachment(0, 6);
		imgGenerate.setLayoutData(fd_imgGenerate);
		imgGenerate.setText("Label");
		imgGenerate.setImage(Images.getImage(Images.ORM));
		lkGenerate = new Link(compoRight2_, SWT.NONE);
		FormData fd_lkGenerate = new FormData();
		fd_lkGenerate.top = new FormAttachment(0, 5);
		fd_lkGenerate.left = new FormAttachment(0, 27);
		lkGenerate.setLayoutData(fd_lkGenerate);
		lkGenerate.setText("<a>Générer les classes</a>");
		lkGenerate.setEnabled(false);
		lstclasses = getFormToolkit().createTable(compoRight2_, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_lstclasses = new FormData();
		fd_lstclasses.right = new FormAttachment(100, -10);
		fd_lstclasses.top = new FormAttachment(0, 26);
		fd_lstclasses.left = new FormAttachment(0, 6);
		lstclasses.setLayoutData(fd_lstclasses);
		lstclasses.setHeaderVisible(false);
		lstclasses.setLinesVisible(false);
		lstclasses.setLayout(new TableLayout());
		lstclasses.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				Point pt = new Point(e.x, e.y);
				TableItem item = lstclasses.getItem(pt);
				if (item != null) {
					String fileName = "/src/" + WorkbenchUtils.packageToFolder(getPackage()) + "/" + item.getText();
					if (WorkbenchUtils.getActiveProject().getFile(fileName).isAccessible())
						openFile(fileName);
				}
			}
		});
		tableViewer2 = new TableViewer(lstclasses);
		tableViewer2.setLabelProvider(new WorkbenchLabelProvider());
		tableViewer2.setContentProvider(new ArrayContentProvider());

		lkGenerate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createClasses();
			}
		});
	}

	private ArrayList<String> getSelectedTables() {
		ArrayList<String> result = new ArrayList<String>();
		for (TableItem item : table.getItems()) {
			if (item.getChecked())
				result.add(item.getText());
		}
		return result;
	}

	public void createClasses() {
		try {
			String toCreate = "";
			ArrayList<String> tables = getSelectedTables();
			if (tables.size() == 0)
				toCreate = ".*";
			KernelCreator knl = new KernelCreator(toCreate, tables);
			knl.connect(ks.getDb());
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			Path path = new Path("net/ko/templates/java");
			URL fileURL = FileLocator.find(bundle, path, null);
			knl.addFileListener(new EventFileListener() {

				@Override
				public void fileExist(EventFile e) {
					e.doit = ckEcrase.getSelection();
					System.out.println((File) e.getSource());

				}
			});
			knl.setHasController(ckGenerateController.getSelection());
			knl.createClasses(fileURL.toURI().toString());

			IProject project = WorkbenchUtils.getActiveProject();
			String savePath = project.getLocationURI().getPath();
			knl.setControllerFullPath(WorkbenchUtils.cPath(savePath + "/" + getBaseFolder() + "/conf/kox.xml"));
			savePath += "/src/";
			savePath = WorkbenchUtils.cPath(savePath);

			knl.saveAs(savePath, txtPackage.getText());
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			if (ckGenerateController.getSelection()) {
				// String
				// p=knl.getControllerFullPath().replace(project.getLocation().toOSString()+getBaseFolder(),
				// "");
				multiPageEditor.getPageController().setValidationFile("conf/kox.xml");
				multiPageEditor.setKoxFile("conf/kox.xml");
				multiPageEditor.updateFile();
			}

			cleanError();
		} catch (Exception ex) {
			ex.printStackTrace();
			showError(ex.getMessage(), IMessageProvider.ERROR);
		}

	}

	public void refreshLstClasses() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				IFolder folder = WorkbenchUtils.getFolder("src/" + WorkbenchUtils.packageToFolder(txtPackage.getText()));
				IResource resources[];
				tableViewer2.getTable().clearAll();
				tableViewer2.refresh();
				if (folder.exists()) {
					try {
						resources = folder.members();
						for (int i = 0; i < resources.length; i++) {
							if (resources[i].exists()) {
								if (tableViewer2.testFindItem(resources[i]) != null)
									tableViewer2.replace(resources[i], i);
								else
									tableViewer2.add(resources[i]);
							}
						}
						cleanError();
					} catch (CoreException e) {
						showError(e.getMessage(), IMessageProvider.WARNING);
					}
				}

			}
		});
	}

	/**
	 * This method initializes cmbBases
	 * 
	 */
	private void createCmbBases() {
		GridData gridData20 = new GridData();
		gridData20.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData20.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData20.horizontalSpan = 3;
		cmbBases = new Combo(compoLeft1__1, SWT.NONE);
		cmbBases.setLayoutData(gridData20);
		cmbBases.addVerifyListener(new org.eclipse.swt.events.VerifyListener() {
			public void verifyText(org.eclipse.swt.events.VerifyEvent e) {
				lkConnect.setEnabled(!e.text.equals(""));
				IAction act = WorkbenchUtils.getAction("connectToDb");
				if (act != null)
					act.setEnabled(!e.text.equals(""));
			}
		});
		cmbBases.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusGained(org.eclipse.swt.events.FocusEvent e) {
				listDb();
			}
		});
	}

	/**
	 * This method initializes cmbDbType
	 * 
	 */
	private void createCmbDbType() {
		GridData gridData30 = new GridData();
		gridData30.horizontalSpan = 3;
		gridData30.horizontalIndent = 0;
		gridData30.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		cmbDbType = new Combo(compoLeft1__1, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbDbType.setLayoutData(gridData30);
		// comboViewer = new ComboViewer(cmbDbType);
		cmbDbType.add("Mysql");
		cmbDbType.add("Oracle");
		cmbDbType.add("ODBC");
		cmbDbType.select(0);
	}

	/**
	 * This method initializes formToolkit
	 * 
	 * @return org.eclipse.ui.forms.widgets.FormToolkit
	 */
	private FormToolkit getFormToolkit() {
		if (formToolkit == null) {
			formToolkit = new FormToolkit(Display.getCurrent());
		}
		return formToolkit;
	}

	public Form getForm() {
		return form;
	}

	/**
	 * This method initializes form
	 * 
	 */
	private void createForm() {
		form = getFormToolkit().createForm(this);
		form.getBody().setLayout(new FillLayout());
		form.setImage(Images.getImage(Images.ORM));
		form.setText("Mapping relationnel/Objet");
		getFormToolkit().decorateFormHeading(form);
		form.setFont(new Font(Display.getDefault(), "Segoe UI", 10, SWT.BOLD));
		form.addMessageHyperlinkListener(new HyperlinkAdapter());
	}

	/**
	 * This method initializes sectionSGBD
	 * 
	 */
	private void createSectionSGBD() {
		compoLeft1_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		sectionSGBD = getFormToolkit().createSection(compoLeft1_1, ExpandableComposite.TWISTIE | Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.CLIENT_INDENT);
		sectionSGBD.setDescription("Entrer les paramètres de la base de données");
		sectionSGBD.setText("Base de données");
		createCompoLeft1_();
		sectionSGBD.setClient(compoLeft1__1);
		sectionSGBD.setExpanded(true);
	}

	/**
	 * This method initializes sectionOptions
	 * 
	 */
	private void createSectionOptions() {
		compoRight1_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		sectionOptions = getFormToolkit().createSection(
				compoRight1_1,
				Section.CLIENT_INDENT | Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		sectionOptions.setText("Options");
		sectionOptions.setDescription("Options de création");
		createCompoRight1_();
		sectionOptions.setClient(compoRight1_);
		sectionOptions.setExpanded(true);
	}

	/**
	 * This method initializes sectionTables
	 * 
	 */
	private void createSectionTables() {
		compoLeft2_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		sectionTables = getFormToolkit().createSection(compoLeft2_1, ExpandableComposite.TWISTIE | Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.CLIENT_INDENT);

		sectionTables.setDescription("Sélectionner les tables à inclure");
		sectionTables.setText("Tables");
		createCompoLeft2_();
		sectionTables.setClient(compoLeft2_);
		sectionTables.setExpanded(true);
	}

	/**
	 * This method initializes sectionGeneration
	 * 
	 */
	private void createSectionGeneration() {
		compoRight2_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		sectionGeneration = getFormToolkit().createSection(compoRight2_1, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.CLIENT_INDENT);
		sectionGeneration.setText("Génération");
		createCompoRight2_();
		sectionGeneration.setClient(compoRight2_);
		sectionGeneration.setDescription("Génération des classes de mapping");
	}

	/**
	 * This method initializes compoLeft2_
	 * 
	 */
	private void createCompoLeft2_() {
		compoLeft2_ = getFormToolkit().createComposite(sectionTables);
		compoLeft2_.setLayout(new GridLayout());
		GridData gridData21 = new GridData();
		gridData21.horizontalIndent = 1;
		ckAllTables = getFormToolkit().createButton(compoLeft2_, "(Dé)Sélectionner toutes les tables", SWT.FLAT | SWT.CHECK);
		ckAllTables.setForeground(JFaceResources.getColorRegistry().get(JFacePreferences.HYPERLINK_COLOR));
		ckAllTables.setLayoutData(gridData21);
		ckAllTables.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				selectAllTables(ckAllTables.getSelection());
			}
		});
		GridData gridDataTable = new GridData();
		gridDataTable.widthHint = 300;
		gridDataTable.horizontalIndent = 1;
		gridDataTable.heightHint = 100;
		table = getFormToolkit().createTable(compoLeft2_, SWT.MULTI | SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		table.setHeaderVisible(false);
		table.setLayoutData(gridDataTable);
		table.setLinesVisible(false);
		tableViewer = new TableViewer(table);
		String[] COLUMNS = new String[] { "Table" };
		for (String element : COLUMNS) {
			TableColumn col = new TableColumn(tableViewer.getTable(), SWT.CENTER);
			col.setText(element);
		}
		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnPixelData(200, true));
		table.setLayout(tlayout);

		table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (e.detail == SWT.CHECK)
					multiPageEditor.updateFile();
			}
		});
		table.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Table table = (Table) e.widget;
				table.getColumn(0).setWidth(table.getSize().x - table.getVerticalBar().getSize().x - 2 * table.getBorderWidth());
			}
		});
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void addListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getColumnText(Object arg0, int arg1) {
				if (arg1 == 0)
					return arg0.toString();
				else
					return "";
			}

			@Override
			public Image getColumnImage(Object arg0, int arg1) {
				Image img = Images.getImage(Images.DB_TABLE);
				if (arg1 == 0)
					return img;
				else
					return null;
			}
		});
	}

	@Override
	public void updateCobElements() {
		// TODO Auto-generated method stub

	}
} // @jve:decl-index=0:visual-constraint="10,10"
