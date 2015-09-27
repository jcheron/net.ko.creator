package net.ko.creator.editors.templates;

import java.io.IOException;

import net.ko.bean.TemplateType;
import net.ko.bean.ZoneList;
import net.ko.bean.ZoneType;
import net.ko.bean.ZoneXml;
import net.ko.creator.WorkbenchUtils;
import net.ko.creator.controls.ColorizedItemsList;
import net.ko.creator.controls.ExpandBarZone;
import net.ko.kobject.KMask;
import net.ko.utils.KProperties;
import net.ko.utils.KString;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.wb.swt.SWTResourceManager;

public class MarkdownTextEditor extends TextEditor{
	public MarkdownTextEditor() {

	}
    
    private ColorManager colorManager;
	private ExpandBarZone expandBar;
	private TemplateType templateType;
	private String className;
	private IProject project;
	private ColorizedItemsList colorizedItems;
	private MarkdownTextEditorConfiguration textEditorConfig;
	
	protected String getSpecificOperation(String operation,String strTemplate){
		KMask mask=new KMask(strTemplate);
		String result=mask.getOperation(operation, "{#", "#}");
		
		if(result!=null&&!"".equals(result)){
			result=KString.cleanJSONValue(result);
		}
		return result;
	}
	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		colorizedItems=new ColorizedItemsList();
		colorManager = new ColorManager();
		textEditorConfig=new MarkdownTextEditorConfiguration(this,colorManager);
		setSourceViewerConfiguration(textEditorConfig);
		setDocumentProvider(new MarkdownTextDocumentProvider());
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		final Composite compoParent=parent;
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		final SashForm sashForm = new SashForm(parent, SWT.SMOOTH);
		expandBar = new ExpandBarZone(sashForm, SWT.NONE);
		expandBar.setBackgroundMode(SWT.INHERIT_DEFAULT);
		expandBar.setBackgroundImage(null);
		expandBar.setBackground(SWTResourceManager.getColor(105, 105, 105));
		parent.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event e) {
				int percent = 150*100 / compoParent.getSize().x;
				if (percent <= 100) {
					sashForm.setWeights(new int[] {percent, 100-percent});
				}
				expandBar.reajustContent();
			}});
		try {
			expandBar.setProject(getProject());
			setFileName(getEditorInput().getName());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		super.createPartControl(sashForm);
		
	}
	public ColorizedItemsList getColorizedItems() {
		return colorizedItems;
	}
	public void setColorizedItems(ColorizedItemsList colorizedItems) {
		this.colorizedItems = colorizedItems;
	}
	public void setFileName(String fileName) throws IOException{
		String templateTypeName="custom";
		IDocument doc=getDocumentProvider().getDocument(getEditorInput());
		String strClassName=getSpecificOperation("className", doc.get());
		if(strClassName!=null&&!"".equals(strClassName)){
			className=strClassName;
		}else{
			if(fileName.endsWith(".view")||fileName.endsWith(".list")||fileName.endsWith(".show")){
				className=getPackageName()+".K"+KString.capitalizeFirstLetter(fileName.substring(0,fileName.lastIndexOf(".")));
			}else{
				className="";
			}
		}
		templateTypeName=fileName.substring(fileName.lastIndexOf(".")+1);
		templateType=TemplateType.getType(templateTypeName);
		expandBar.setTemplateType(templateType);
		expandBar.setClassName(className);
	}
	private String getPackageName() throws IOException{
		String result="";
		String strFolder="";
		if(WorkbenchUtils.isDynamicWebProject(getProject()))
			strFolder="WebContent/";
		strFolder=getProject().getLocation().toOSString()+"/"+strFolder;
		KProperties kp=new KProperties();
		kp.loadFromFile(strFolder+"config.ko");
		result=kp.getProperty("package");
		return result;
	}

	public IProject getProject() {
		if(project==null)
			project=WorkbenchUtils.getActiveProject(this);
		return project;
	}
	public void insertZoneFrom(ZoneXml z,Table table){

		String value=z.getValue();
		if(table.getData("zt").equals(ZoneType.ztField)&&templateType.equals(TemplateType.ttList)){
			if(!searchAndReplace("{#mask:", "{#mask:<td>"+value+"</td>"))
				insertInText("{#mask:<td>"+value+"</td>#}");
		}else{
			insertInText(z);
			setFocus();
		}
	}
	private void insertInText(String text){
		IDocument doc=getDocumentProvider().getDocument(getEditorInput());
		int offset=0;
		try {
		StyledText st = (StyledText) getAdapter(Control.class);
		offset = st.getCaretOffset();
		int l=doc.getLength();
		doc.replace(offset, 0, text);
	} catch (BadLocationException e) {
		e.printStackTrace();
	}
	}
	private void insertInText(ZoneXml z){
		int offset=0;
		StyledText st = (StyledText) getAdapter(Control.class);
		if(st!=null)
			offset = st.getCaretOffset();
		MarkdownContentAssistProcessor sharedProcessor = (MarkdownContentAssistProcessor) textEditorConfig.getContentAssistant(getSourceViewer()).getContentAssistProcessor(MarkdownTextPartitionScanner.MARKDOWN_HTML);
		TemplateProposal cp= sharedProcessor.createProposal(getSourceViewer(), offset, z.getTemplate());
		
		IDocument doc=getDocumentProvider().getDocument(getEditorInput());
		int l=doc.getLength();
		if(offset==l){
			try {
				doc.replace(offset, 0, "\n");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		cp.apply(getSourceViewer(), (char)0, 0, offset);
		if(st!=null)
			if(st.getSelectionCount()==0)
			st.setCaretOffset(offset+z.getContent().length());
	}
	private boolean searchAndReplace(String search,String replace) {
		boolean find=false;
		try{
			IDocument doc=getDocumentProvider().getDocument(getEditorInput());
			int offset = 0;
			IRegion region=null;
			FindReplaceDocumentAdapter frDoc=new FindReplaceDocumentAdapter(doc);
			do {
				region=frDoc.find(offset, search, true, false, false, false);
				if (region==null)
					break;
				frDoc.replace(replace,false);
				offset= region.getOffset()+region.getLength();
				find=true;
			}while(region!=null);
		}catch(BadLocationException e){}
		return find;
	}
	public ZoneList getZones(){
		return expandBar.getZoneList();
	}
	public void addCaretListener(CaretListener listener){
		StyledText st = (StyledText) getAdapter(Control.class);
		if(st!=null)
			st.addCaretListener(listener);
	}
}
