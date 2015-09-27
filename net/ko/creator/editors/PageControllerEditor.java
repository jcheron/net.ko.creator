package net.ko.creator.editors;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import net.ko.bean.MessageEr;
import net.ko.creator.WorkbenchUtils;
import net.ko.utils.KProperties;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class PageControllerEditor extends Composite {
	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
	private String controllerFile="";  //  @jve:decl-index=0:
	private String messagesFile="";  //  @jve:decl-index=0:
	private String erFile="";  //  @jve:decl-index=0:
	private final String WARNING="warning.png";  //  @jve:decl-index=0:
	private final String ERROR="error.gif";  //  @jve:decl-index=0:
	private Composite compoMain = null;  //  @jve:decl-index=0:visual-constraint="10,520"
	private Composite compoLeft1 = null;
	private MultiPageEditor multiPageEditor;  //  @jve:decl-index=0:
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
	private TableViewer tableViewer = null;
	private FormToolkit formToolkit = null;   //  @jve:decl-index=0:visual-constraint=""
	private Section sectionFiles = null;
	private Label lblEreg = null;
	private Section sectionContentFile = null;
	private Composite compoSectionContentFile = null;
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
	private Button btDel = null;
	private Button btValid = null;
	private boolean isDirty = false;
	private Label lblEr = null;
	private Label lblId = null;
	private Text txtTestEr = null;
	private Label lblResultTest = null;
	private Label lblTest = null;
	
	public PageControllerEditor(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new FillLayout());
		setSize(new Point(800, 500));
		createForm();
		createCompoMain();
	}
	
	/**
	 * This method initializes compoMain	
	 *
	 */
	private void createCompoMain() {
		GridLayout glMain = new GridLayout();
		glMain.numColumns = 2;
		glMain.marginWidth = 0;
		compoMain = getFormToolkit().createComposite(form.getBody(), SWT.NONE);
		compoMain.setSize(new Point(800, 554));
		compoMain.setLayout(glMain);
		createCompoLeft1();
		createCompoRight1();
	}	

	public void setMultiPageEditor(MultiPageEditor multiPageEditor){
		this.multiPageEditor=multiPageEditor;
	}

	/**
	 * This method initializes compoLeft1	
	 *
	 */
	private void createCompoLeft1() {
		GridData gridData18 = new GridData();
		gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 1;
		gridLayout3.marginHeight = 5;
		gridLayout3.marginWidth = 5;
		compoLeft1 = getFormToolkit().createComposite(compoMain, SWT.NONE);
		compoLeft1.setLayout(gridLayout3);
		compoLeft1.setLayoutData(gridData18);
		createSectionFiles();
	}

	public void openFile(String fileName){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	    IFile file=WorkbenchUtils.getActiveProject().getFile(fileName);
		try {
			IDE.openEditor(page, file);
		} catch (PartInitException e1) {
			//showError(e1.getMessage(), WARNING);
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
		imgController.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/accept.png")));
		lblController = getFormToolkit().createLabel(compoLeft1_, "Contrôleur :");
		lkController = getFormToolkit().createHyperlink(compoLeft1_,"Fichier controller", SWT.NONE);
		lkController.setToolTipText("Modifier le fichier xml");
		lkController.setLayoutData(gridData4);
		lkController.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
						openFile(getBaseFolder()+controllerFile);
					}
				});
		btController = new Button(compoLeft1_, SWT.NONE);
		btController.setText("...");
		btController.setLayoutData(gridData12);
		btController
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				        String rep=showFileDialog("Fichier contrôleur", "*.xml", controllerFile);
						if(rep!=null){
				        	setControllerFile(rep);
				        	multiPageEditor.updateFile();
				        }
					}
				});
		imgMessages = getFormToolkit().createLabel(compoLeft1_, "");
		imgMessages.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/accept.png")));
		lblMessages = getFormToolkit().createLabel(compoLeft1_, "Messages");
		lkMessages = getFormToolkit().createHyperlink(compoLeft1_,"Fichier de messages", SWT.NONE);
		lkMessages.setLayoutData(gridData);
		lkMessages.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				for(MessageEr msg:openMessageErs()){
					tableViewer.add(msg);
				}
			}
		});
		btMessages = new Button(compoLeft1_, SWT.NONE);
		btMessages.setText("...");
		btMessages.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
		        String rep=showFileDialog("Fichier de messages", "*.properties", messagesFile);
				if(rep!=null){
		        	setMessagesFile(rep);
		        	multiPageEditor.updateFile();
		        }
			}
		});
		imgEr = getFormToolkit().createLabel(compoLeft1_, "");
		imgEr.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/accept.png")));
		lblEreg = getFormToolkit().createLabel(compoLeft1_, "Exp. Reg. :");
		lkER = getFormToolkit().createHyperlink(compoLeft1_,"Fichier ER", SWT.NONE);
		lkER.setLayoutData(gridData1);
		btER = new Button(compoLeft1_, SWT.NONE);
		btER.setText("...");
		btER.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
		        String rep=showFileDialog("Fichier des expressions régulières", "*.properties", erFile);
				if(rep!=null){
		        	setErFile(rep);
		        	multiPageEditor.updateFile();
		        }
			}
		});
	}
	protected Properties loadPropertyFile(String relativePath){
		KProperties kp=new KProperties();
		try {
			kp.loadFromFile(WorkbenchUtils.getActiveProject().getLocation().toOSString()+"/"+relativePath);
		} catch (IOException e) {
			return new Properties();
		}
		return kp.properties;		
	}
	protected ArrayList<MessageEr> openMessageErs(){
		ArrayList<MessageEr> messages=new ArrayList<MessageEr>();
		Properties messagesP;
		Properties erP;
		String baseFolder=getBaseFolder();
		messagesP = loadPropertyFile(baseFolder+messagesFile);
		erP=loadPropertyFile(baseFolder+erFile);
		for(Map.Entry<Object, Object> entry:messagesP.entrySet()){
			messages.add(new MessageEr((String)entry.getKey(), (String)entry.getValue(), erP.getProperty((String)entry.getKey(),"")));		
		}
		return messages;
	}
	protected String showFileDialog(String text,String filter,String fileName){
		String result=null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IProject project=WorkbenchUtils.getActiveProject();
		MyFileElementTreeSelectionDialog mf=new MyFileElementTreeSelectionDialog(window.getShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
		mf.setParams(project, text, filter);
		if(mf.open()== ResourceSelectionDialog.OK){
			Object[] selected = mf.getResult();
			IFile file = (IFile) selected[0];
			result=file.getProjectRelativePath().toString();
		}
        return result;
	}
	public String getBaseFolder(){
		String result="";
		if(WorkbenchUtils.isDynamicWebProject())
			result="WebContent/";
		return result;
	}
	protected void updateFile(Hyperlink lk,Label img,String fileName){
		boolean noFile=false;
		IFile f=null;
		try{
			f=WorkbenchUtils.getActiveProject().getFile(fileName);
			noFile=!f.exists();}
		catch(Exception e){noFile=true;}
		if(!noFile && f.exists()){
			lk.setText(f.getName());
			lk.setToolTipText("Ouvrir le fichier "+fileName);
			img.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/accept.png")));
			lk.setEnabled(true);
		}else{
			lk.setText("Fichier absent");
			lk.setToolTipText("fichier absent");
			img.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/warning.png")));
			lk.setEnabled(false);
		}
		loadErMessages();
	}
	public void setControllerFile(String fileName) {
		if(fileName!=null && WorkbenchUtils.getActiveProject()!=null){
			String baseFolder=getBaseFolder();
			String newFileName=fileName.replace(baseFolder, "");
			if(!controllerFile.equals(newFileName)){
				controllerFile=newFileName;
				updateFile(lkController, imgController,  baseFolder+controllerFile);
			}
		}
	}

	public void setMessagesFile(String fileName) {
		if(fileName!=null && WorkbenchUtils.getActiveProject()!=null){
			String baseFolder=getBaseFolder();
			String newFileName=fileName.replace(baseFolder, "");
			if(!messagesFile.equals(newFileName)){
				messagesFile=newFileName;
				updateFile(lkMessages, imgMessages, baseFolder+messagesFile);
				isDirty=false;
			}
		}
	}

	public void setErFile(String fileName) {
		if(fileName!=null && WorkbenchUtils.getActiveProject()!=null){
			String baseFolder=getBaseFolder();
			String newFileName=fileName.replace(baseFolder, "");
			if(!erFile.equals(newFileName)){
				erFile=newFileName;
				updateFile(lkER, imgEr,  baseFolder+erFile);
				isDirty=false;
			}
		}
	}	
	public String getControllerFile() {
		return controllerFile;
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
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData2 = new GridData();
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		compoRight1 = getFormToolkit().createComposite(compoMain, SWT.NONE);
		compoRight1.setLayoutData(gridData2);
		compoRight1.setLayout(gridLayout1);
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
		sectionFiles = getFormToolkit().createSection(compoLeft1, Section.DESCRIPTION | Section.TITLE_BAR);
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
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.horizontalSpan = 2;
		sectionContentFile = getFormToolkit().createSection(compoRight1, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.CLIENT_INDENT);
		sectionContentFile.setExpanded(true);
		sectionContentFile.setDescription("Messages et expressions régulières");
		sectionContentFile.setLayoutData(gridData3);
		createCompoSectionContentFile();
		sectionContentFile.setText("Contrôles");
		sectionContentFile.setClient(compoSectionContentFile);
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
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 2;
		compoSectionContentFile = getFormToolkit().createComposite(
				sectionContentFile);
		compoSectionContentFile.setLayout(gridLayout5);
		tableFileContent = getFormToolkit().createTable(compoSectionContentFile, SWT.FULL_SELECTION);
		tableFileContent.setHeaderVisible(true);
		tableFileContent.setLayoutData(gridData5);
		tableFileContent.setLinesVisible(true);
		btAdd = getFormToolkit().createButton(compoSectionContentFile, "Ajouter...", SWT.PUSH);
		btAdd.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				sectionAddUpdate.setExpanded(true);
				txtId.setText("newId");
				txtMessage.setText("");
				txtEr.setText("");
				txtId.setSelection(0, txtId.getTextLimit());
				txtId.setFocus();
			}
		});
		btDel = getFormToolkit().createButton(compoSectionContentFile, "Supprimer", SWT.PUSH);
		btDel.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				tableViewer.getTable().remove(tableViewer.getTable().getSelectionIndex());
				isDirty=true;
				multiPageEditor.updateFile();
			}
		});
		createSectionAddUpdate();
		tableViewer = new TableViewer(tableFileContent);
		  String[] COLUMNS = new String[] { "id","message"};
		      for( String element : COLUMNS ) {
		          TableColumn col = new TableColumn( tableViewer.getTable(), SWT.LEFT );
		          col.setText( element);
		      }
		      TableLayout tlayout = new TableLayout();
		          tlayout.addColumnData( new ColumnPixelData( 80, true ));
		         // tlayout.addColumnData( new ColumnWeightData( 40, 100, true ));
		          tlayout.addColumnData( new ColumnPixelData( 240, true ));
		          tableFileContent.setLayout( tlayout );

		          tableFileContent.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
		          	public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
		          		if(e.item.getData()!=null){
		          			MessageEr msg=(MessageEr)e.item.getData();
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
				String text="";
				switch (index) {
				case 0:
					text=((MessageEr)o).getId();
					break;
				case 1:
					text=((MessageEr)o).getMessage();
					break;
				default:
					break;
				}
					return text;
			}
			
			@Override
			public Image getColumnImage(Object arg0, int arg1) {
				// TODO Auto-generated method stub
				Display display=Display.getCurrent();
				Image img = new Image(display,getClass().getResourceAsStream("bullet_orange.png"));
				if (arg1==0)
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
		form.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/plugin.png")));
		form.setText("Contrôleur");
		getFormToolkit().decorateFormHeading(form);
		form.setFont(new Font(Display.getDefault(), "Segoe UI", 10, SWT.BOLD));
	}

	/**
	 * This method initializes sectionAddUpdate	
	 *
	 */
	private void createSectionAddUpdate() {
		GridData gridData6 = new GridData();
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData6.horizontalSpan = 2;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sectionAddUpdate = getFormToolkit().createSection(
				compoSectionContentFile,
				ExpandableComposite.TWISTIE | Section.DESCRIPTION
						| ExpandableComposite.TITLE_BAR);
		
		sectionAddUpdate.setDescription("Entrer les valeurs");
		createCompoAddUpdate();
		sectionAddUpdate.setText("Ajout/modification");
		sectionAddUpdate.setLayoutData(gridData6);
		sectionAddUpdate.setClient(compoAddUpdate);
		//sectionAddUpdate.setExpanded(true);
		sectionAddUpdate.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				sectionAddUpdate.setExpanded(false);
			}
		});
	}

	/**
	 * This method initializes compoAddUpdate	
	 *
	 */
	private void createCompoAddUpdate() {
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
				if(txtTestEr.getText().matches(txtEr.getText())){
					lblResultTest.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/accept.png")));	
				}else
					lblResultTest.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/exclamation.png")));	
			}
		});
		txtTestEr.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				lblResultTest.setVisible(false);
			}
		});
		lblResultTest = getFormToolkit().createLabel(compoAddUpdate, "Label");
		lblResultTest.setVisible(false);
		lblResultTest.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/net/ko/creator/editors/exclamation.png")));
	}

	protected void addUpdateErMessage() {
		String id=txtId.getText();
		int index=getMessageErById(id);
		MessageEr message=null;
		if(index!=-1){
			message=(MessageEr) tableFileContent.getItem(index).getData();
			message.setId(txtId.getText());
			message.setMessage(txtMessage.getText());
			message.setEr(txtEr.getText());
			tableFileContent.getItem(index).setData(message);
			tableViewer.refresh(message);
		}else{
			message=new MessageEr(txtId.getText(),txtMessage.getText(),txtEr.getText());
			tableViewer.add(message);
			tableFileContent.setSelection(tableFileContent.getItemCount()-1);
		}		
		isDirty=true;
		multiPageEditor.updateFile();
		
	}
	protected int getMessageErById(String id){
		boolean find=false;
		int i=0;
		while(i<tableFileContent.getItemCount() && !find){
			find=id.equals(((MessageEr)tableFileContent.getItem(i).getData()).getId());
			i++;
		}
		if(find)
			return i-1;
		else
			return -1;
	}
	protected void loadErMessages(){
		tableViewer.getTable().clearAll();
		tableViewer.refresh();
		IProject project=WorkbenchUtils.getActiveProject();
		sectionAddUpdate.setEnabled(false);
		String baseFolder=getBaseFolder();
		if(project!=null){
			try{
				if(project.getFile(baseFolder+messagesFile).exists() && project.getFile(baseFolder+erFile).exists()){
					for(MessageEr msg:openMessageErs()){
						tableViewer.add(msg);
					}
					sectionAddUpdate.setEnabled(true);
				}
			}
			catch(Exception e){}
		}
	}
	public void saveErMessages(){
		Properties pMessages=new Properties();
		Properties pEr=new Properties();
		for(TableItem item:tableViewer.getTable().getItems()){
			MessageEr msg=(MessageEr) item.getData();
			pMessages.put(msg.getId(),msg.getMessage());
			pEr.put(msg.getId(),msg.getEr());
		}
		KProperties kpMessages=new KProperties(pMessages);
		KProperties kpEr=new KProperties(pEr);
		try {
			String projectPath=WorkbenchUtils.getActiveProject().getLocation().toOSString()+"/";
			kpMessages.saveAs(projectPath+messagesFile);
			kpEr.saveAs(projectPath+erFile);
			isDirty=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
