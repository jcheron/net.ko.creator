package net.ko.creator.editors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import net.ko.bean.ConfigVariable;
import net.ko.bean.ContentOutlineBean;
import net.ko.bean.ContentOutlineBeanElementVar;
import net.ko.bean.MessageEr;
import net.ko.creator.WorkbenchUtils;
import net.ko.creator.editors.images.Images;
import net.ko.utils.KProperties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class PageControllerEditor extends EditorComposite {

	private String validationFile = ""; // @jve:decl-index=0:
	private String messagesFile = ""; // @jve:decl-index=0:
	private String erFile = ""; // @jve:decl-index=0:
	private Composite compoMain = null; // @jve:decl-index=0:visual-constraint="10,520"
	private Composite compoMain_1;
	private Composite compoLeft1 = null;
	private Composite compoLeft1_1;
	private Composite compoLeft1_ = null;
	private Button btController = null;
	private Label imgController = null;
	private Hyperlink lkController = null;
	private Label imgMessages = null;
	private Button btMessages = null;
	private Button btER = null;
	private Hyperlink lkMessages = null;
	private Hyperlink lkER = null;
	private Composite compoRight1 = null;
	private Composite compoRight1_1;
	private TableViewer tableViewer = null;
	private FormToolkit formToolkit = null; // @jve:decl-index=0:visual-constraint=""
	private Section sectionFiles = null;
	private Label lblEreg = null;
	private Section sectionContentFile = null;
	private Composite compoSectionContentFile = null;
	private Composite compoSectionContentFile_1;
	private Table tableFileContent = null;
	private Label lblController = null;
	private Label lblMessages = null;
	private Label imgEr = null;
	private Form form = null;
	private Section sectionAddUpdate = null;
	private Composite compoAddUpdate = null;
	private Text txtEr = null;
	private Label lblMessage = null;
	private Text txtMessage = null;
	private Text txtId = null;
	private Button btAdd = null;
	private Button btAdd_1;
	private Button btDel = null;
	private Button btValid = null;
	private Label lblEr = null;
	private Label lblId = null;
	private Text txtTestEr = null;
	private Label lblResultTest = null;
	private Label lblTest = null;
	private Composite compoSectionTableContentFile;
	private FormData fd_btAdd;

	public PageControllerEditor(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		setSize(new Point(800, 600));
		createForm();
		createCompoMain();
	}

	/**
	 * This method initializes compoMain
	 * 
	 */
	private void createCompoMain() {
		compoMain_1 = getFormToolkit().createComposite(form.getBody(), SWT.NONE);
		compoMain_1.setSize(new Point(800, 554));
		createCompoLeft1();
		createCompoRight1();
	}

	/**
	 * This method initializes compoLeft1
	 * 
	 */
	private void createCompoLeft1() {
		compoMain_1.setLayout(new FormLayout());
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 1;
		gridLayout3.marginHeight = 5;
		gridLayout3.marginWidth = 5;
		compoLeft1_1 = getFormToolkit().createComposite(compoMain_1, SWT.NONE);
		FormData fd_compoLeft1 = new FormData();
		fd_compoLeft1.top = new FormAttachment(0, 5);
		fd_compoLeft1.left = new FormAttachment(0);
		compoLeft1_1.setLayoutData(fd_compoLeft1);
		compoLeft1_1.setLayout(gridLayout3);
		createSectionFiles();
	}

	public void openFile(String fileName) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFile file = WorkbenchUtils.getActiveProject().getFile(fileName);
		try {
			IDE.openEditor(page, file);
		} catch (PartInitException e1) {
		}
	}

	/**
	 * This method initializes compoLeft1_
	 * 
	 */
	private void createCompoLeft1_() {
		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData12.grabExcessHorizontalSpace = false;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new GridData();
		gridData4.widthHint = 200;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 4;
		gridLayout2.marginHeight = 0;
		gridLayout2.verticalSpacing = 0;
		gridLayout2.marginWidth = 10;
		compoLeft1_ = getFormToolkit().createComposite(sectionFiles);
		compoLeft1_.setLayout(gridLayout2);
		imgController = getFormToolkit().createLabel(compoLeft1_, "");
		imgController.setImage(Images.getImage(Images.SUCCESS));
		lblController = getFormToolkit().createLabel(compoLeft1_, "Validation :");
		lkController = getFormToolkit().createHyperlink(compoLeft1_, "Fichier validation", SWT.NONE);
		lkController.setToolTipText("Modifier le fichier xml");
		lkController.setLayoutData(gridData4);
		lkController.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				multiPageEditor.gotoPageKox();
				// openFile(getBaseFolder()+validationFile);
			}
		});
		btController = new Button(compoLeft1_, SWT.NONE);
		btController.setText("...");
		btController.setLayoutData(gridData12);
		btController
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						String rep = showFileDialog("Fichier validation", "*.xml", validationFile);
						if (rep != null) {
							setValidationFile(rep);
							multiPageEditor.updateFile();
							// setValidationFile(getValidationFile());
						}
					}
				});
		imgMessages = getFormToolkit().createLabel(compoLeft1_, "");
		imgMessages.setImage(Images.getImage(Images.SUCCESS));
		lblMessages = getFormToolkit().createLabel(compoLeft1_, "Messages");
		lkMessages = getFormToolkit().createHyperlink(compoLeft1_, "Fichier de messages", SWT.NONE);
		lkMessages.setLayoutData(gridData);
		lkMessages.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				openFile(getBaseFolder() + messagesFile);
				/*
				 * for(MessageEr msg:openMessageErs()){ tableViewer.add(msg); }
				 */
			}
		});
		btMessages = new Button(compoLeft1_, SWT.NONE);
		btMessages.setText("...");
		btMessages.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				String rep = showFileDialog("Fichier de messages", "*.properties", messagesFile);
				if (rep != null) {
					setMessagesFile(rep);
					multiPageEditor.updateFile();
				}
			}
		});
		imgEr = getFormToolkit().createLabel(compoLeft1_, "");
		imgEr.setImage(Images.getImage(Images.SUCCESS));
		lblEreg = getFormToolkit().createLabel(compoLeft1_, "Exp. Reg. :");
		lkER = getFormToolkit().createHyperlink(compoLeft1_, "Fichier ER", SWT.NONE);
		lkER.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				openFile(getBaseFolder() + erFile);
			}
		});
		lkER.setLayoutData(gridData1);
		btER = new Button(compoLeft1_, SWT.NONE);
		btER.setText("...");
		btER.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				String rep = showFileDialog("Fichier des expressions régulières", "*.properties", erFile);
				if (rep != null) {
					setErFile(rep);
					multiPageEditor.updateFile();
				}
			}
		});
	}

	protected Properties loadPropertyFile(String relativePath) {
		KProperties kp = new KProperties();
		try {
			kp.loadFromFile(WorkbenchUtils.getActiveProject().getLocation().toOSString() + "/" + relativePath);
		} catch (IOException e) {
			return new Properties();
		}
		return kp.getProperties();
	}

	protected ArrayList<MessageEr> openMessageErs() {
		ArrayList<MessageEr> messages = new ArrayList<MessageEr>();
		Properties messagesP;
		Properties erP;
		String baseFolder = getBaseFolder();
		messagesP = loadPropertyFile(baseFolder + messagesFile);
		erP = loadPropertyFile(baseFolder + erFile);
		for (Map.Entry<Object, Object> entry : messagesP.entrySet()) {
			messages.add(new MessageEr((String) entry.getKey(), (String) entry.getValue(), erP.getProperty((String) entry.getKey(), "")));
		}
		return messages;
	}

	protected String showFileDialog(String text, String filter, String fileName) {
		String result = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IProject project = WorkbenchUtils.getActiveProject();
		MyFileElementTreeSelectionDialog mf = new MyFileElementTreeSelectionDialog(window.getShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
		mf.setParams(project, text, filter);
		if (mf.open() == ResourceSelectionDialog.OK) {
			Object[] selected = mf.getResult();
			IFile file = (IFile) selected[0];
			result = file.getProjectRelativePath().toString();
		}
		return result;
	}

	public String getBaseFolder() {
		String result = "";
		if (WorkbenchUtils.isDynamicWebProject())
			result = "WebContent/";
		return result;
	}

	protected void updateFile(Hyperlink lk, Label img, String fileName) {
		boolean noFile = false;
		IFile f = null;
		try {
			f = WorkbenchUtils.getActiveProject().getFile(fileName);
			noFile = !f.exists();
		} catch (Exception e) {
			noFile = true;
		}
		if (!noFile && f.exists()) {
			lk.setText(f.getName());
			lk.setToolTipText("Ouvrir le fichier " + fileName);
			img.setImage(Images.getImage(Images.SUCCESS));
			lk.setEnabled(true);
		} else {
			lk.setText("Fichier absent");
			lk.setToolTipText("fichier absent");
			img.setImage(Images.getImage(Images.EXCLAMATION));
			lk.setEnabled(false);
		}
		loadErMessages();
	}

	public void setValidationFile(String fileName) {
		if (fileName != null && WorkbenchUtils.getActiveProject() != null) {
			String baseFolder = getBaseFolder();
			String newFileName = fileName.replace(baseFolder, "");
			if (!validationFile.equals(newFileName)) {
				validationFile = newFileName;
				updateFile(lkController, imgController, baseFolder + validationFile);
				multiPageEditor.setKoxFile(validationFile);
				setCobVar("validationFile", validationFile);
			}
		}
	}

	public void setMessagesFile(String fileName) {
		if (fileName != null && WorkbenchUtils.getActiveProject() != null) {
			String baseFolder = getBaseFolder();
			String newFileName = fileName.replace(baseFolder, "");
			if (!messagesFile.equals(newFileName)) {
				messagesFile = newFileName;
				updateFile(lkMessages, imgMessages, baseFolder + messagesFile);
				setCobVar("messagesFile", messagesFile);
				setDirty(false);
			}
		}
	}

	public void setErFile(String fileName) {
		if (fileName != null && WorkbenchUtils.getActiveProject() != null) {
			String baseFolder = getBaseFolder();
			String newFileName = fileName.replace(baseFolder, "");
			if (!erFile.equals(newFileName)) {
				erFile = newFileName;
				updateFile(lkER, imgEr, baseFolder + erFile);
				setCobVar("ERFile", erFile);
				setDirty(false);
			}
		}
	}

	public String getValidationFile() {
		return validationFile;
	}

	public String getMessagesFile() {
		return messagesFile;
	}

	public String getErFile() {
		return erFile;
	}

	/**
	 * This method initializes compoRight1
	 * 
	 */
	private void createCompoRight1() {
		compoRight1_1 = getFormToolkit().createComposite(compoMain_1, SWT.NONE);
		FormData fd_compoRight1 = new FormData();
		fd_compoRight1.top = new FormAttachment(compoLeft1_1, 0, SWT.TOP);
		fd_compoRight1.bottom = new FormAttachment(100, -15);
		fd_compoRight1.right = new FormAttachment(0, 800);
		fd_compoRight1.left = new FormAttachment(0, 358);
		compoRight1_1.setLayoutData(fd_compoRight1);
		createSectionContentFile();
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

	/**
	 * This method initializes sectionFiles
	 * 
	 */
	private void createSectionFiles() {
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sectionFiles = getFormToolkit().createSection(compoLeft1_1, Section.DESCRIPTION | Section.TITLE_BAR);
		sectionFiles.setDescription("Liste des fichiers");
		sectionFiles.setLayoutData(gridData10);
		sectionFiles.setText("Fichiers");
		createCompoLeft1_();
		sectionFiles.setClient(compoLeft1_);
		sectionFiles.setExpanded(true);
	}

	/**
	 * This method initializes sectionContentFile
	 * 
	 */
	private void createSectionContentFile() {
		compoRight1_1.setLayout(new FormLayout());
		sectionContentFile = getFormToolkit().createSection(compoRight1_1, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.CLIENT_INDENT);
		FormData fd_sectionContentFile = new FormData();
		fd_sectionContentFile.bottom = new FormAttachment(100);
		fd_sectionContentFile.right = new FormAttachment(100, -10);
		fd_sectionContentFile.top = new FormAttachment(0, 5);
		fd_sectionContentFile.left = new FormAttachment(0, 5);
		sectionContentFile.setLayoutData(fd_sectionContentFile);
		sectionContentFile.setExpanded(true);
		sectionContentFile.setDescription("Messages et expressions régulières");
		createCompoSectionContentFile();
		sectionContentFile.setText("Contrôles");
		sectionContentFile.setClient(compoSectionContentFile_1);
		sectionContentFile.setExpanded(true);
	}

	/**
	 * This method initializes compoSectionContentFile
	 * 
	 */
	private void createCompoSectionContentFile() {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.heightHint = 205;
		gridData5.horizontalSpan = 2;
		gridData5.grabExcessHorizontalSpace = true;
		compoSectionContentFile_1 = getFormToolkit().createComposite(sectionContentFile);
		compoSectionContentFile_1.setLayout(new FormLayout());
		compoSectionTableContentFile = new Composite(compoSectionContentFile_1, SWT.NONE);
		FormData fd_compoSectionTableContentFile = new FormData();
		fd_compoSectionTableContentFile.top = new FormAttachment(0, 5);
		fd_compoSectionTableContentFile.right = new FormAttachment(0, 415);
		fd_compoSectionTableContentFile.left = new FormAttachment(0, 5);
		compoSectionTableContentFile.setLayoutData(fd_compoSectionTableContentFile);
		TableColumnLayout layout = new TableColumnLayout();

		tableFileContent = getFormToolkit().createTable(compoSectionTableContentFile, SWT.FULL_SELECTION);
		tableFileContent.setHeaderVisible(true);
		tableFileContent.setLayoutData(gridData5);
		tableFileContent.setLinesVisible(true);
		btAdd_1 = getFormToolkit().createButton(compoSectionContentFile_1, "Ajouter...", SWT.PUSH);
		fd_compoSectionTableContentFile.bottom = new FormAttachment(100, -196);
		fd_btAdd = new FormData();
		fd_btAdd.top = new FormAttachment(compoSectionTableContentFile, 6);
		fd_btAdd.left = new FormAttachment(compoSectionTableContentFile, 0, SWT.LEFT);
		btAdd_1.setLayoutData(fd_btAdd);
		btAdd_1.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				sectionAddUpdate.setExpanded(true);
				txtId.setText("newId");
				txtMessage.setText("");
				txtEr.setText("");
				txtId.setSelection(0, txtId.getTextLimit());
				txtId.setFocus();
			}
		});
		createSectionAddUpdate();
		tableViewer = new TableViewer(tableFileContent);

		TableColumn colId = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		layout.setColumnData(colId, new ColumnWeightData(20));
		colId.setText("Id");

		TableColumn colMessage = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		layout.setColumnData(colMessage, new ColumnWeightData(80));
		colMessage.setText("Message");
		compoSectionTableContentFile.setLayout(layout);
		/*
		 * String[] COLUMNS = new String[] { "id","message"}; for( String
		 * element : COLUMNS ) { TableColumn col = new TableColumn(
		 * tableViewer.getTable(), SWT.LEFT ); col.setText( element); }
		 * TableLayout tlayout = new TableLayout(); tlayout.addColumnData( new
		 * ColumnWeightData(100)); tlayout.addColumnData( new
		 * ColumnWeightData(300)); tableFileContent.setLayout( tlayout );
		 */

		tableFileContent.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (e.item.getData() != null) {
					MessageEr msg = (MessageEr) e.item.getData();
					txtId.setText(msg.getId());
					txtMessage.setText(msg.getMessage());
					txtEr.setText(msg.getEr());
				}
			}
		});
		tableFileContent.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				sectionAddUpdate.setExpanded(true);
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
			public String getColumnText(Object o, int index) {
				String text = "";
				switch (index) {
				case 0:
					text = ((MessageEr) o).getId();
					break;
				case 1:
					text = ((MessageEr) o).getMessage();
					break;
				default:
					break;
				}
				return text;
			}

			@Override
			public Image getColumnImage(Object arg0, int arg1) {
				Image img = Images.getImage(Images.BULLET);
				if (arg1 == 0)
					return img;
				else
					return null;
			}
		});
	}

	/**
	 * This method initializes form
	 * 
	 */
	private void createForm() {
		form = getFormToolkit().createForm(this);
		form.getBody().setLayout(new FillLayout());
		form.setImage(Images.getImage(Images.VALIDATION));
		form.setText("Validation");
		getFormToolkit().decorateFormHeading(form);
		form.setFont(new Font(Display.getDefault(), "Segoe UI", 10, SWT.BOLD));
	}

	/**
	 * This method initializes sectionAddUpdate
	 * 
	 */
	private void createSectionAddUpdate() {
		btDel = getFormToolkit().createButton(compoSectionContentFile_1, "Supprimer", SWT.PUSH);
		FormData fd_btDel = new FormData();
		fd_btDel.top = new FormAttachment(compoSectionTableContentFile, 6);
		fd_btDel.left = new FormAttachment(btAdd_1, 6);
		btDel.setLayoutData(fd_btDel);
		btDel.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				tableViewer.getTable().remove(tableViewer.getTable().getSelectionIndex());
				setDirty(true);
				// multiPageEditor.updateFile();
			}
		});
		sectionAddUpdate = getFormToolkit().createSection(
				compoSectionContentFile_1,
				Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		FormData fd_sectionAddUpdate = new FormData();
		fd_sectionAddUpdate.top = new FormAttachment(btAdd_1, 6);
		fd_sectionAddUpdate.left = new FormAttachment(0, 5);
		fd_sectionAddUpdate.right = new FormAttachment(100);
		fd_sectionAddUpdate.bottom = new FormAttachment(100, -10);
		sectionAddUpdate.setLayoutData(fd_sectionAddUpdate);

		sectionAddUpdate.setDescription("Entrer les valeurs");
		sectionAddUpdate.setText("Ajout/modification");
		// sectionAddUpdate.setClient(compoAddUpdate);
		// sectionAddUpdate.setExpanded(true);
		sectionAddUpdate.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				sectionAddUpdate.setExpanded(false);
			}
		});
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData9 = new GridData();
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalSpan = 2;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData8.horizontalSpan = 2;
		gridData8.grabExcessHorizontalSpace = true;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		compoAddUpdate = getFormToolkit().createComposite(sectionAddUpdate);
		sectionAddUpdate.setClient(compoAddUpdate);
		compoAddUpdate.setLayout(gridLayout);
		lblId = getFormToolkit().createLabel(compoAddUpdate, "Id :");
		txtId = getFormToolkit().createText(compoAddUpdate, null, SWT.SINGLE | SWT.BORDER);
		btValid = getFormToolkit().createButton(compoAddUpdate, "Valider", SWT.PUSH);
		btValid.setLayoutData(gridData11);
		btValid.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				addUpdateErMessage();
				sectionAddUpdate.setExpanded(false);
			}
		});
		lblMessage = getFormToolkit().createLabel(compoAddUpdate, "Message :");
		txtMessage = getFormToolkit().createText(compoAddUpdate, null, SWT.SINGLE | SWT.BORDER);
		txtMessage.setLayoutData(gridData9);
		lblEr = getFormToolkit().createLabel(compoAddUpdate, "Exp. reg. :");
		txtEr = getFormToolkit().createText(compoAddUpdate, null, SWT.SINGLE | SWT.BORDER);
		txtEr.setLayoutData(gridData8);
		lblTest = getFormToolkit().createLabel(compoAddUpdate, "Test :");
		txtTestEr = getFormToolkit().createText(compoAddUpdate, null, SWT.SINGLE | SWT.BORDER);
		txtTestEr.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				lblResultTest.setVisible(true);
				if (txtTestEr.getText().matches(txtEr.getText())) {
					lblResultTest.setImage(Images.getImage(Images.SUCCESS));
				} else
					lblResultTest.setImage(Images.getImage(Images.EXCLAMATION));
			}
		});
		txtTestEr.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				lblResultTest.setVisible(false);
			}
		});
		lblResultTest = getFormToolkit().createLabel(compoAddUpdate, "Label");
		lblResultTest.setVisible(false);
		lblResultTest.setImage(Images.getImage(Images.EXCLAMATION));
		createCompoAddUpdate();
	}

	/**
	 * This method initializes compoAddUpdate
	 * 
	 */
	private void createCompoAddUpdate() {
	}

	protected void addUpdateErMessage() {
		String id = txtId.getText();
		int index = getMessageErById(id);
		MessageEr message = null;
		if (index != -1) {
			message = (MessageEr) tableFileContent.getItem(index).getData();
			message.setId(txtId.getText());
			message.setMessage(txtMessage.getText());
			message.setEr(txtEr.getText());
			tableFileContent.getItem(index).setData(message);
			tableViewer.refresh(message);
		} else {
			message = new MessageEr(txtId.getText(), txtMessage.getText(), txtEr.getText());
			tableViewer.add(message);
			tableFileContent.setSelection(tableFileContent.getItemCount() - 1);
		}
		setDirty(true);
		// multiPageEditor.updateFile();

	}

	protected int getMessageErById(String id) {
		boolean find = false;
		int i = 0;
		while (i < tableFileContent.getItemCount() && !find) {
			find = id.equals(((MessageEr) tableFileContent.getItem(i).getData()).getId());
			i++;
		}
		if (find)
			return i - 1;
		else
			return -1;
	}

	protected void loadErMessages() {
		tableViewer.getTable().clearAll();
		tableViewer.refresh();
		IProject project = WorkbenchUtils.getActiveProject();
		sectionAddUpdate.setEnabled(false);
		String baseFolder = getBaseFolder();
		if (project != null) {
			try {
				if (project.getFile(baseFolder + messagesFile).exists() && project.getFile(baseFolder + erFile).exists()) {
					for (MessageEr msg : openMessageErs()) {
						tableViewer.add(msg);
					}
					sectionAddUpdate.setEnabled(true);
				}
			} catch (Exception e) {
			}
		}
	}

	public void saveErMessages() {
		Properties pMessages = new Properties();
		Properties pEr = new Properties();
		for (TableItem item : tableViewer.getTable().getItems()) {
			MessageEr msg = (MessageEr) item.getData();
			pMessages.put(msg.getId(), msg.getMessage());
			pEr.put(msg.getId(), msg.getEr());
		}
		KProperties kpMessages = new KProperties(pMessages);
		KProperties kpEr = new KProperties(pEr);
		try {
			IProject project = WorkbenchUtils.getActiveProject();
			String baseFolder = getBaseFolder();

			// String
			// projectPath=WorkbenchUtils.getActiveProject().getLocation().toOSString()+"/"+getBaseFolder()+"/";
			kpMessages.saveAs(project.getFile(baseFolder + messagesFile).getLocation().toOSString());
			kpEr.saveAs(project.getFile(baseFolder + erFile).getLocation().toOSString());
			project.getFile(baseFolder + messagesFile).refreshLocal(IResource.DEPTH_INFINITE, null);
			project.getFile(baseFolder + erFile).refreshLocal(IResource.DEPTH_INFINITE, null);
			setDirty(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public IFile getIFile(String fileName) {
		IProject project = WorkbenchUtils.getActiveProject();
		String baseFolder = getBaseFolder();
		if (project != null)
			return project.getFile(baseFolder + fileName);
		else
			return null;
	}

	public void setCobVar(String name, String value) {
		ContentOutlineBeanElementVar c = (ContentOutlineBeanElementVar) getCobByName(name);
		if (c == null) {
			c = new ContentOutlineBeanElementVar(name, Images.BULLET, null, new ConfigVariable(name, value));
			c.setParent(cob);
			cobElements.add(c);
		}
		else
			c.setVariable(new ConfigVariable(name, value));
		updateOutlinePage();
	}

	public ContentOutlineBean getCobByName(String name) {
		ContentOutlineBean result = null;
		for (ContentOutlineBean c : cobElements) {
			if (c.getId().equals(name)) {
				result = c;
				break;
			}
		}
		return result;
	}

	@Override
	public void updateCobElements() {
		// TODO Auto-generated method stub

	}
} // @jve:decl-index=0:visual-constraint="10,10"
