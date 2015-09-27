package net.ko.wizard;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ko.bean.TemplateType;
import net.ko.bean.ZoneList;
import net.ko.bean.ZoneType;
import net.ko.bean.ZoneXml;
import net.ko.creator.KJavaProject;
import net.ko.creator.controls.KstyledText;
import net.ko.inheritance.KReflectObject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;


public class KoWizardCompo2 extends Composite {
	private KoWizardPage2 wizardPage;
	private Table tableFields;
	private TableViewer tvFields;
	private Table tableZones;
	private TableViewer tvZones;
	private Table tableHTML;
	private TableViewer tvHTML;
	private Table tableFunctions;
	private TableViewer tvFunctions;
	private Button btnAjouter;
	private Button btnRetirer;
	private TemplateType templateType=TemplateType.ttNone;
	private KstyledText textTemplate;
	private String className="";

	private Text textFileName;
	private ExpandBar expandBar;
	private Table tableSelected=null;
	private ZoneXml activeZone=null;
	private ZoneList zoneList;

	public KoWizardCompo2(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		Display display=Display.getCurrent();
		zoneList=new ZoneList();
		textTemplate = new KstyledText(this, SWT.BORDER | SWT.WRAP| SWT.V_SCROLL);
		textTemplate.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				wizardPage.dialogChanged();
			}
		});
		
		textTemplate.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
		//textTemplate.setBounds(10, 47, 302, 376);
		FormData fd_textTemplate = new FormData ();
		fd_textTemplate.top = new FormAttachment (0,57);
		fd_textTemplate.right = new FormAttachment (100, -20);
		fd_textTemplate.left = new FormAttachment (0,215);
		fd_textTemplate.bottom = new FormAttachment (100,-51);
		textTemplate.setLayoutData(fd_textTemplate);
		
		Group grpTemplate = new Group(this, SWT.NONE);
		grpTemplate.setText("Template :");
		FormData fd_grpTemplate = new FormData();
		fd_grpTemplate.top = new FormAttachment(0, 10);
		fd_grpTemplate.right = new FormAttachment(100, -10);
		fd_grpTemplate.left = new FormAttachment(0, 201);
		fd_grpTemplate.bottom = new FormAttachment(100, -41);
		grpTemplate.setLayoutData(fd_grpTemplate);
		
		btnAjouter = new Button(this, SWT.NONE);
		btnAjouter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(activeZone!=null&&tableSelected!=null){
					insertZoneFrom(activeZone, tableSelected);
					wizardPage.dialogChanged();
				}
			}
		});
		
		FormData fd_btnAjouter = new FormData();
		fd_btnAjouter.right = new FormAttachment(0, 309);
		fd_btnAjouter.left = new FormAttachment(0, 201);
		fd_btnAjouter.top = new FormAttachment(grpTemplate, 6);
		btnAjouter.setLayoutData(fd_btnAjouter);
		btnAjouter.setText("Ajouter ->");
		
		btnRetirer = new Button(this, SWT.NONE);
		btnRetirer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textTemplate.replaceWord("");
			}
		});
		FormData fd_btnRetirer = new FormData();
		fd_btnRetirer.left = new FormAttachment(btnAjouter, 6);
		fd_btnRetirer.right = new FormAttachment(0, 429);
		fd_btnRetirer.top = new FormAttachment(grpTemplate, 6);
		btnRetirer.setLayoutData(fd_btnRetirer);
		btnRetirer.setText("Retirer");
		
		textFileName = new Text(grpTemplate, SWT.BORDER);
		textFileName.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent evt) {
				if(!"".equals(evt.text)){
					Pattern p=Pattern.compile("^(?i)[a-z0-9\\-_\\/\\.]+$");
					Matcher m=p.matcher(evt.text);
					evt.doit=m.matches();
				}
			}
		});
		textFileName.setEnabled(false);
		textFileName.setBounds(66, 20, 251, 21);
		
		Label lblFichier = new Label(grpTemplate, SWT.NONE);
		lblFichier.setBounds(10, 23, 49, 15);
		lblFichier.setText("Fichier :");
		
		expandBar = new ExpandBar(this, SWT.BORDER);
		expandBar.setBackgroundMode(SWT.INHERIT_DEFAULT);
		expandBar.setBackgroundImage(null);
		expandBar.setBackground(SWTResourceManager.getColor(105, 105, 105));
		FormData fd_expandBar = new FormData();
		fd_expandBar.top = new FormAttachment(grpTemplate, 0, SWT.TOP);
		fd_expandBar.bottom = new FormAttachment(100, -41);
		fd_expandBar.left = new FormAttachment(0, 10);
		fd_expandBar.right = new FormAttachment(grpTemplate, -6);
		expandBar.setLayoutData(fd_expandBar);
		Listener listener = new Listener() { 
			final Display display = Display.getCurrent();
			public void handleEvent(Event e) { 
				ExpandItem item = (ExpandItem)e.item;
				final ExpandBar expandBar=item.getParent();
				display.asyncExec(new Runnable() { 
					public void run () {
						ExpandItem[] items = expandBar.getItems(); 
						Rectangle area = expandBar.getClientArea(); 
						int spacing = expandBar.getSpacing();
						int expPlus=0;
						ArrayList<ExpandItem> expandedItems=new ArrayList<>();
						for(int i=0;i<items.length;i++){
							if(items[i].getExpanded()){
								expandedItems.add(items[i]);
								}
						}
						area.height -= (items.length + 1)*spacing+items[0].getHeaderHeight()*items.length; 
						if(expandedItems.size()>0){
							expPlus=area.height/expandedItems.size();
							for(ExpandItem item:expandedItems){
									item.setHeight(expPlus);
							}
						}
					} 
				}); 
			} 
			};
		expandBar.addListener(SWT.Expand, listener); 
		expandBar.addListener(SWT.Collapse, listener); 
		ExpandItem xpndtmChamps = new ExpandItem(expandBar, SWT.NONE,0);
		xpndtmChamps.setImage(new Image(display,getClass().getResourceAsStream("/icons/field.png")));
		xpndtmChamps.setExpanded(true);
		xpndtmChamps.setText("Champs");
		
		tableFields = new Table(expandBar, SWT.BORDER | SWT.FULL_SELECTION);
		xpndtmChamps.setControl(tableFields);
		createTableViewver(tableFields,ZoneType.ztField);

		xpndtmChamps.setHeight(xpndtmChamps.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		ExpandItem xpndtmZones = new ExpandItem(expandBar, SWT.NONE,1);
		xpndtmZones.setImage(new Image(display,getClass().getResourceAsStream("/icons/zone.png")));
		xpndtmZones.setExpanded(true);
		xpndtmZones.setText("Zones");
		
		tableZones = new Table(expandBar, SWT.BORDER | SWT.FULL_SELECTION);
		xpndtmZones.setControl(tableZones);
		createTableViewver(tableZones,ZoneType.ztZone);

		xpndtmZones.setHeight(xpndtmZones.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		ExpandItem xpndtmHtml = new ExpandItem(expandBar, SWT.NONE,2);
		xpndtmHtml.setText("HTML");
		
		tableHTML = new Table(expandBar, SWT.BORDER | SWT.FULL_SELECTION);
		xpndtmHtml.setControl(tableHTML);
		createTableViewver(tableHTML,ZoneType.ztHTML);

		xpndtmHtml.setHeight(xpndtmHtml.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		xpndtmHtml.setImage(new Image(display,getClass().getResourceAsStream("/icons/html.png")));
		
		ExpandItem xpndtmFonctions = new ExpandItem(expandBar, SWT.NONE,3);
		xpndtmFonctions.setImage(new Image(display,getClass().getResourceAsStream("/icons/func.png")));
		xpndtmFonctions.setExpanded(true);
		xpndtmFonctions.setText("Fonctions");
		
		tableFunctions = new Table(expandBar, SWT.BORDER | SWT.FULL_SELECTION);

		xpndtmFonctions.setControl(tableFunctions);
		createTableViewver(tableFunctions,ZoneType.ztFunc);

		xpndtmFonctions.setHeight(xpndtmFonctions.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		zoneList=new ZoneList();
		zoneList.loadZonesList();
		showZonesIn(ZoneType.ztHTML, tvHTML);
	}
	public KoWizardPage2 getWizardPage() {
		return wizardPage;
	}
	public void setWizardPage(KoWizardPage2 wizardPage) {
		this.wizardPage = wizardPage;
	}
	public TemplateType getTemplateType() {
		return templateType;
	}
	public void setTemplateType(TemplateType templateType) {
		if(!this.templateType.equals(templateType)){
			String fileName=textFileName.getText();
			String label=templateType.getLabel();
			if(!"".equals(label)&&!"custom".equals(label)){
				textFileName.setText(fileName.replace("."+this.templateType.getLabel(), "."+label));
			}
			this.templateType = templateType;
			if(!"".equals(label)&&!"custom".equals(label)){
				loadZones(label);
			}
			
			showZonesIn(ZoneType.ztFunc, tvFunctions);
		}
		

	}
	private void showZonesIn(ZoneType zoneType,TableViewer tableViewer){
		try{
			tableViewer.getTable().clearAll();
			tableViewer.refresh();
			for(ZoneXml z:zoneList.getZones()){
				if(z.getTemplateType().equals(templateType) || TemplateType.ttNone.equals(z.getTemplateType())){
					if(z.getType().equals(zoneType)){
						if(!z.getId().equals("_defaultTemplate"))
							tableViewer.add(z);
						else
							textTemplate.setText(z.getValue());
					}
				}

			}
		}catch(Exception e){}
	}
	private void loadZones(String type){
		showZonesIn(ZoneType.ztZone, tvZones);
	}

	private void loadFields() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		tvFields.getTable().clearAll();
		tvFields.refresh();
		if(!"".equals(className)){
			try{
			Class<?> clazz=KJavaProject.getClassInstance(className, wizardPage.getProject());
			ArrayList<String> fields=KReflectObject.getFieldNames(clazz);
			for(String f:fields){
				ZoneXml z=new ZoneXml(f,ZoneType.ztField,templateType);
				z.setSelection(new String[]{"_after"});
				tvFields.add(z);
			}
			}catch(Exception e){e.printStackTrace();}
		}
	}
	private TableViewer createTableViewver(Table table,ZoneType zoneType){
		TableViewer result=null;
		switch (zoneType) {
		case ztField:
			tvFields = new TableViewer(table);
			result=tvFields;
			break;
		case ztZone:
			tvZones = new TableViewer(table);
			result=tvZones;
			break;
		case ztFunc:
			tvFunctions = new TableViewer(table);
			result=tvFunctions;
			break;
		case ztHTML:
			tvHTML = new TableViewer(table);
			result=tvHTML;
			break;
		default:
			break;
		}
		table.setData("zt", zoneType);
		String[] COLUMNS = new String[] { "id"};
		for( String element : COLUMNS ) {
			TableColumn col = new TableColumn( table, SWT.LEFT );
			col.setText( element);
		}
		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData( new ColumnPixelData( 140, false ));
		table.setLayout(tlayout);
		result.setContentProvider(new ArrayContentProvider());
		result.setLabelProvider(new ITableLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener arg0) {}
			
			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}
			
			@Override
			public void dispose() {}
			
			@Override
			public void addListener(ILabelProviderListener arg0) {}
			
			@Override
			public String getColumnText(Object o, int index) {
				String text="";
				switch (index) {
				case 0:
					text=((ZoneXml)o).getCaption();
					break;
				default:
					break;
				}
					return text;
			}
			
			@Override
			public Image getColumnImage(Object o, int arg1) {
				Image img=null;
				if (arg1==0){
					Display display=Display.getCurrent();
					String strImg=((ZoneXml)o).getType().getImage();
					img = new Image(display,getClass().getResourceAsStream("/icons/"+strImg));
					return img;
				}
				else
				return null;
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Table table=(Table)e.getSource();
				if(table.getSelectionIndex()!=-1){
					TableItem ti=table.getItem(table.getSelectionIndex());
	          		if(ti.getData()!=null){
	          			ZoneXml z=(ZoneXml)ti.getData();
	          			insertZoneFrom(z,table);
	          			wizardPage.dialogChanged();
	          		}
				}
			}
		});
		table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				activeZone=(ZoneXml) e.item.getData();
				if(tableSelected!=null && !tableSelected.equals(e.widget))
					tableSelected.setSelection(-1);
				btnAjouter.setEnabled(true);
				tableSelected=(Table) e.widget;
			}
		});
		table.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseHover(MouseEvent e) {
				Table t=(Table) e.getSource();
				TableItem item=t.getItem(new Point(e.x,e.y));
				if(item!=null){
					t.setToolTipText(((ZoneXml) item.getData()).getDescription());
				}
			}
		});
		return result;
	}
	public void insertZoneFrom(ZoneXml z,Table table){
			String value=z.getValue();
			if(table.getData("zt").equals(ZoneType.ztField)&&templateType.equals(TemplateType.ttList)){
				textTemplate.insertIn("{#mask:", "#}", "<td>"+value+"</td>");
			}else{
				Display display=Display.getCurrent();
				Point range=textTemplate.replaceWord(value,(z.getSelection()),z.getToolTip());
  				textTemplate.setRangeColor(display.getSystemColor(z.getType().getColor()),range);
				textTemplate.setFocus();
			}
			textTemplate.setToolTipText(z.getFirstToolTip());
	}
	public void insertIn(int pos,String value){
		textTemplate.insertIn(pos, value);
	}
	public String getClassName() {
		return className;
	}
	private void updateZones(){
		boolean classExist=!"".equals(className);
		expandBar.getItem(0).setExpanded(classExist);
		expandBar.getItem(1).setExpanded(classExist);
		expandBar.getItem(2).setExpanded(!classExist);
		tableFields.setEnabled(classExist);
		tableZones.setEnabled(classExist);
	}
	public String getFileNameFromClassName(){
		String fileName=className.substring(className.lastIndexOf(".")+1);
		fileName=fileName.toLowerCase().replaceFirst("^k(.+?)", "$1")+"."+templateType.getLabel();
		return fileName;
	}
	public void setClassName(String className) {
		if(!className.equals(this.className)){
			this.className = className;
			if(!"".equals(className)){
				try {
					loadFields();
					textFileName.setText(getFileNameFromClassName());
					textFileName.setEnabled(true);
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				textFileName.setText("newTemplate.tpl");
				textFileName.setEnabled(true);
			}
		}
		updateZones();
	}
	public String getTextTemplate(){
		return textTemplate.getText();
	}
	public String getFileName(){
		return textFileName.getText();
	}
}
