package net.ko.creator.editors.templates;

import java.io.IOException;

import net.ko.bean.TemplateType;
import net.ko.bean.ZoneList;
import net.ko.bean.ZoneType;
import net.ko.bean.ZoneXml;
import net.ko.creator.WorkbenchUtils;
import net.ko.creator.controls.ExpandBarZone;
import net.ko.creator.controls.KstyledText;
import net.ko.utils.KProperties;
import net.ko.utils.KString;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wb.swt.SWTResourceManager;

public class TemplateEditor extends Composite {
	private KstyledText textTemplate;
	private ExpandBarZone expandBar;
	private TemplateType templateType;
	private String className;
	private IProject project;
	private boolean isDirty=false;
	private SashForm sashForm;
	
	public TemplateEditor(Composite parent, int style) {
		super(parent, style);

		setLayout(new FillLayout(SWT.VERTICAL));
		
		sashForm = new SashForm(this, SWT.SMOOTH);
		
		expandBar = new ExpandBarZone(sashForm, SWT.NONE);
		expandBar.setBackgroundMode(SWT.INHERIT_DEFAULT);
		expandBar.setBackgroundImage(null);
		expandBar.setBackground(SWTResourceManager.getColor(105, 105, 105));
		
		textTemplate = new KstyledText(sashForm, SWT.BORDER | SWT.WRAP);
		//sashForm.setWeights(new int[] {1, 2});
        this.addListener(SWT.Resize, new Listener() {
			
			@Override
            public void handleEvent(Event e) {
                int percent = 150*100 / getSize().x;
                if (percent <= 100) {
                    sashForm.setWeights(new int[] {percent, 100-percent});
                }
                expandBar.reajustContent();
            }});
		expandBar.setTextTemplate(textTemplate);
		expandBar.addTableMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Table table=(Table)e.getSource();
				if(table.getSelectionIndex()!=-1){
					TableItem ti=table.getItem(table.getSelectionIndex());
	          		if(ti.getData()!=null){
	          			ZoneXml z=(ZoneXml)ti.getData();
	          			insertZoneFrom(z,table);
	          		}
				}
			}
		});
	}
	public String getText(){
		return textTemplate.getText();
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
	public void setFileName(String fileName) throws IOException{
		String templateTypeName="custom";
		if(fileName.endsWith(".view")||fileName.endsWith(".list")){
			className=getPackageName()+".K"+KString.capitalizeFirstLetter(fileName.substring(0,fileName.lastIndexOf(".")));
			templateTypeName=fileName.substring(fileName.lastIndexOf(".")+1);
		}else{
			className="";
		}
		templateType=TemplateType.getType(templateTypeName);
		expandBar.setTemplateType(templateType);
		expandBar.setClassName(className);
	}
	private String getPackageName() throws IOException{
		String result="";
		String strFolder="";
		if(WorkbenchUtils.isDynamicWebProject(project))
			strFolder="WebContent/";
		strFolder=project.getLocation().toOSString()+"/"+strFolder;
		KProperties kp=new KProperties();
		kp.loadFromFile(strFolder+"config.ko");
		result=kp.getProperty("package");
		return result;
	}
	public void setProject(IProject project) {
		this.project = project;
		expandBar.setProject(project);
	}
	public KstyledText getTextTemplate() {
		return textTemplate;
	}
	public boolean isDirty() {
		return isDirty;
	}
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
	public ZoneList getZoneList() {
		return expandBar.getZoneList();
	}
}
