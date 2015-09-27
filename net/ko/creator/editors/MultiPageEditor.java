package net.ko.creator.editors;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import net.ko.creator.editors.PageControllerEditor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.ide.IDE;

public class MultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener{

	private static TextEditor editor;
	private PageMainEditor pageMainEditor=null;
	private PageControllerEditor pageController=null;

	private Properties properties;
	
	public PageMainEditor getPage(){
		return pageMainEditor;
	}	
	
	public void gotoPage(int index){
		setActivePage(index);
	}
	
	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		properties=new Properties();
	}
	public String getProperty(String property){
		return properties.getProperty(property);
	}

	void createPageTextEditor() {
		try {
			editor = new TextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}

	void createPageMainEditor() {
		pageMainEditor = new PageMainEditor(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		pageMainEditor.setLayout(layout);
		pageMainEditor.setMultiPageEditor(this);
		int index = addPage(pageMainEditor);
		setPageText(index, "KoCreator properties");
	}	
	void createPageController() {
		pageController = new PageControllerEditor(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		pageController.setLayout(layout);
		pageController.setMultiPageEditor(this);
		int index = addPage(pageController);
		setPageText(index, "KoCreator validation");
	}	
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		
		createPageMainEditor();
		createPageController();
		createPageTextEditor();
		updateProperties();
		updateControllerProperties();
		
	}
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(2).doSave(monitor);
		if(pageController.isDirty())
			pageController.saveErMessages();
	}
	public IAction getAction(String id){
		IEditorPart editor = getEditor(2);
		return ((ITextEditor)editor).getAction(id);
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		
		IEditorPart editor = getEditor(2);
		editor.doSaveAs();
		setPageText(2, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(2);
		IDE.gotoMarker(getEditor(2), marker);
	}
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return super.isDirty();
	}
	public PageControllerEditor getPageController(){
		return this.pageController;
	}
	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		//if(getActivePage()==0 || getActivePage()==1)
		//	updateFile();
		super.pageChange(newPageIndex);
		if (newPageIndex == 2) {
			updateFile();
		}
		if (newPageIndex == 1) {
			updateControllerProperties();
		}
		if (newPageIndex == 0) {
			updateProperties();
		}
	}
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType()==IResourceChangeEvent.POST_CHANGE){
			if(event.getDelta()!=null){
				if(event.getDelta().getKind()==IResourceDelta.CHANGED || event.getDelta().getKind()==IResourceDelta.REMOVED){
					Display.getDefault().syncExec(new Runnable(){
						public void run(){
							pageMainEditor.refreshLstClasses();
							pageMainEditor.updateBuildPathStatus(editor);
							pageMainEditor.checkCache();
						}
					});
					}
				}
		}
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}
			});
		}
	}

	void updateFile(){
		PageMainEditor p=(PageMainEditor)pageMainEditor;
		properties.put("base", p.getBase());
		properties.put("port", p.getPort());
		properties.put("host", p.getHost());
		properties.put("user", p.getUser());
		properties.put("password", p.getPassword());
		properties.put("package", p.getPackage());
		properties.put("classes", p.getClasses());
		properties.put("dbOptions", p.getOptions());
		
		properties.put("validationFile", pageController.getValidationFile());
		properties.put("messagesFile", pageController.getMessagesFile());
		properties.put("erFile", pageController.getErFile());
		OutputStream o=new ByteArrayOutputStream();
		try {
			properties.store(o,"Fichier de configuration KoCreator");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(o.toString());
	}
	private void updateProperties(){
		String editorText =	editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		try {
			properties.load(new ByteArrayInputStream(editorText.getBytes(Charset.defaultCharset())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pageMainEditor.setHost(properties.getProperty("host"));
		pageMainEditor.setPort(properties.getProperty("port"));
		pageMainEditor.setBase(properties.getProperty("base"));
		pageMainEditor.setUser(properties.getProperty("user"));
		pageMainEditor.setPassword(properties.getProperty("password"));
		pageMainEditor.setPackage(properties.getProperty("package"));
		pageMainEditor.setClasses(properties.getProperty("classes"));
		pageMainEditor.setOptions(properties.getProperty("dbOptions"));
		pageMainEditor.updateBuildPathStatus(getEditor(2));
	}
	private void updateControllerProperties(){
		String editorText=editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		try {
			properties.load(new ByteArrayInputStream(editorText.getBytes(Charset.defaultCharset())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pageController.setValidationFile(properties.getProperty("validationFile"));
		pageController.setMessagesFile(properties.getProperty("messagesFile"));
		pageController.setErFile(properties.getProperty("erFile"));
	}	
}
