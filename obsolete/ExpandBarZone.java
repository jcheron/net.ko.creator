package net.ko.creator.controls;

import java.util.ArrayList;

import net.ko.bean.TemplateType;
import net.ko.bean.ZoneList;
import net.ko.bean.ZoneType;
import net.ko.bean.ZoneXml;
import net.ko.creator.KJavaProject;
import net.ko.creator.WorkbenchUtils;
import net.ko.inheritance.KReflectObject;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ExpandBarZone extends ExpandBar {
	private Table tableFields;
	private TableViewer tvFields;
	private Table tableZones;
	private TableViewer tvZones;
	private Table tableHTML;
	private TableViewer tvHTML;
	private Table tableFunctions;
	private TableViewer tvFunctions;
	private IProject project;
	
	private ZoneList zoneList;
	
	private TemplateType templateType=TemplateType.ttNone;
	private String className="";
	private KstyledText textTemplate;
	
	public ExpandBarZone(Composite parent, int style) {
		super(parent, style);
		createContent();
		zoneList=new ZoneList();
		zoneList.loadZonesList();
		showZonesIn(ZoneType.ztHTML, tvHTML);
	}
	public void addTableMouseListener(MouseAdapter mouseAdapter){
		tableFields.addMouseListener(mouseAdapter);
		tableZones.addMouseListener(mouseAdapter);
		tableHTML.addMouseListener(mouseAdapter);
		tableFunctions.addMouseListener(mouseAdapter);
	}
	public void addTableSelectionListener(SelectionAdapter selectionListener){
		tableFields.addSelectionListener(selectionListener);
		tableZones.addSelectionListener(selectionListener);
		tableHTML.addSelectionListener(selectionListener);
		tableFunctions.addSelectionListener(selectionListener);
	}
	public void reajustContent(){
		Display.getCurrent().asyncExec(new Runnable() { 
			public void run () {
				ExpandItem[] items = getItems(); 
				Rectangle area = getClientArea(); 
				int spacing = getSpacing();
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
	private void createContent(){
		Display display=Display.getCurrent();
		Listener listener = new Listener() { 
			final Display display = Display.getCurrent();
			public void handleEvent(Event e) { 
				ExpandItem item = (ExpandItem)e.item;
				final ExpandBar expandBar=item.getParent();
				reajustContent();
				/*display.asyncExec(new Runnable() { 
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
				}); */
			} 
			};
		addListener(SWT.Expand, listener); 
		addListener(SWT.Collapse, listener); 
		ExpandItem xpndtmChamps = new ExpandItem(this, SWT.NONE,0);
		xpndtmChamps.setImage(new Image(display,getClass().getResourceAsStream("/icons/field.png")));
		xpndtmChamps.setExpanded(true);
		xpndtmChamps.setText("Champs");
		
		tableFields = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		xpndtmChamps.setControl(tableFields);
		createTableViewer(tableFields,ZoneType.ztField);

		xpndtmChamps.setHeight(xpndtmChamps.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		ExpandItem xpndtmZones = new ExpandItem(this, SWT.NONE,1);
		xpndtmZones.setImage(new Image(display,getClass().getResourceAsStream("/icons/zone.png")));
		xpndtmZones.setExpanded(true);
		xpndtmZones.setText("Zones");
		
		tableZones = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		xpndtmZones.setControl(tableZones);
		createTableViewer(tableZones,ZoneType.ztZone);

		xpndtmZones.setHeight(xpndtmZones.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		ExpandItem xpndtmHtml = new ExpandItem(this, SWT.NONE,2);
		xpndtmHtml.setText("HTML");
		
		tableHTML = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		xpndtmHtml.setControl(tableHTML);
		createTableViewer(tableHTML,ZoneType.ztHTML);

		xpndtmHtml.setHeight(xpndtmHtml.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		xpndtmHtml.setImage(new Image(display,getClass().getResourceAsStream("/icons/html.png")));
		
		ExpandItem xpndtmFonctions = new ExpandItem(this, SWT.NONE,3);
		xpndtmFonctions.setImage(new Image(display,getClass().getResourceAsStream("/icons/func.png")));
		xpndtmFonctions.setExpanded(true);
		xpndtmFonctions.setText("Fonctions");
		
		tableFunctions = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);

		xpndtmFonctions.setControl(tableFunctions);
		createTableViewer(tableFunctions,ZoneType.ztFunc);

		xpndtmFonctions.setHeight(xpndtmFonctions.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}
	@Override
	protected void checkSubclass(){
		
	}
	private TableViewer createTableViewer(Table table,ZoneType zoneType){
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
		table.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Table table=(Table)e.widget;
				table.getColumn(0).setWidth(table.getSize().x-table.getVerticalBar().getSize().x-2*table.getBorderWidth());
            }});
		return result;
	}
	private void showZonesIn(ZoneType zoneType,TableViewer tableViewer){
		try{
			tableViewer.getTable().clearAll();
			tableViewer.refresh();
			for(ZoneXml z:zoneList.getZones()){
				if(z.getTemplateType().equals(templateType) || TemplateType.ttNone.equals(z.getTemplateType())){
					if(z.getType().equals(zoneType)){
						//if(!z.getId().equals("_defaultTemplate"))
							tableViewer.add(z);
							if(textTemplate!=null)
								textTemplate.getColorizedItems().add(z.getRegExp(), z.getType().getColor());
						//else
						//	textTemplate.setText(z.getValue());
					}
				}

			}
		}catch(Exception e){}
	}
	private void loadZones(){
		showZonesIn(ZoneType.ztZone, tvZones);
	}

	private void loadFields() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		tvFields.getTable().clearAll();
		tvFields.refresh();
		if(!"".equals(className)){
			try{
			Class<?> clazz=KJavaProject.getClassInstance(className, getProject());
			ArrayList<String> fields=KReflectObject.getFieldNames(clazz);
			for(String f:fields){
				ZoneXml z=new ZoneXml(f,ZoneType.ztField,templateType);
				z.setSelection(new String[]{"_after"});
				tvFields.add(z);
				if(textTemplate!=null)
					textTemplate.getColorizedItems().add(z.getRegExp(), z.getType().getColor());
			}
			}catch(Exception e){e.printStackTrace();}
		}
	}
	private IProject getProject() {
		if(project==null)
			return WorkbenchUtils.getActiveProject();
		else
			return project;
	}
	public TemplateType getTemplateType() {
		return templateType;
	}
	public void setTemplateType(TemplateType templateType) {
		if(!this.templateType.equals(templateType)){
			this.templateType = templateType;
			if(templateType.equals(TemplateType.ttList)|| templateType.equals(TemplateType.ttView)){
				loadZones();
			}
			showZonesIn(ZoneType.ztFunc, tvFunctions);
		}
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		if(!className.equals(this.className)){
			this.className = className;
			if(!"".equals(className)){
				try {
					loadFields();
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		updateZones();
	}
	private void updateZones(){
		boolean classExist=!"".equals(className);
		getItem(0).setExpanded(classExist);
		getItem(1).setExpanded(classExist);
		getItem(2).setExpanded(!classExist);
		tableFields.setEnabled(classExist);
		tableZones.setEnabled(classExist);
	}
	public void setTextTemplate(KstyledText textTemplate) {
		this.textTemplate = textTemplate;
	}
	public void setProject(IProject project) {
		this.project = project;
	}
	public ZoneList getZoneList() {
		return zoneList;
	}
	
}
