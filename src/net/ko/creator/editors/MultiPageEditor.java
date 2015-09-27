package net.ko.creator.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;

import net.ko.bean.ConfigRubriqueList;
import net.ko.bean.ConfigVariable;
import net.ko.bean.ContentOutlineBean;
import net.ko.bean.ContentOutlineBeanPage;
import net.ko.creator.Activator;
import net.ko.creator.editors.images.Images;
import net.ko.java.inheritance.SortedProperties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;

@SuppressWarnings("restriction")
public class MultiPageEditor extends MultiPageEditorPart implements
		IResourceChangeListener {

	private KoTextEditor editor;
	private XMLStructuredTextEditor koxEditor = null;
	private XMLStructuredTextEditor moxEditor = null;
	private PageMainEditor pageMainEditor = null;
	private IntroEditor introEditor = null;
	private CssEditor cssEditor = null;
	private PageControllerEditor pageController = null;
	private int activePage = -1;
	private String mappingFile = "";
	private ContentOutLineEditor outlinePage;
	private ArrayList<ContentOutlineBean> cobElements;

	private Properties properties;
	private ConfigRubriqueList rubriques;

	public PageMainEditor getPage() {
		return pageMainEditor;
	}

	public int getTextEditorIndex() {
		return getPageCount() - 1;
	}

	public IEditorPart getTextEditor() {
		return getEditor(getTextEditorIndex());
	}

	public void gotoPage(int index) {
		setActivePage(index);
	}

	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		properties = new SortedProperties();
		rubriques = new ConfigRubriqueList();
		cobElements = new ArrayList<ContentOutlineBean>();
	}

	public String getProperty(String property) {
		return properties.getProperty(property);
	}

	void createPageTextEditor() {
		try {
			editor = new KoTextEditor(this);
			ContentOutlineBeanPage cob = new ContentOutlineBeanPage("Config.ko", Images.KO_CONFIG, editor);
			editor.setCob(cob);
			cobElements.add(cob);
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
		pageMainEditor.setPageIndex(index);
		setPageText(index, "ORM");
		addCobElements("ORM", Images.ORM, pageMainEditor);
	}

	void createPageController() {
		pageController = new PageControllerEditor(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		pageController.setLayout(layout);
		pageController.setMultiPageEditor(this);
		int index = addPage(pageController);
		pageController.setPageIndex(index);
		setPageText(index, "Validation Files");
		addCobElements("Validation Files", Images.VALIDATION, pageController);
	}

	void createIntroEditor() {
		introEditor = new IntroEditor(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		introEditor.setLayout(layout);
		introEditor.setMultiPageEditor(this);
		int index = addPage(introEditor);
		introEditor.setPageIndex(index);
		setPageText(index, "Overview");
		addCobElements("Overview", Images.PLUGIN, introEditor);
	}

	void createCssEditor() {
		cssEditor = new CssEditor(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		cssEditor.setLayout(layout);
		cssEditor.setMultiPageEditor(this);
		int index = addPage(cssEditor);
		cssEditor.setPageIndex(index);
		setPageText(index, "Css variables");
		addCobElements("Css variables", Images.CSS, cssEditor);
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createIntroEditor();
		createPageMainEditor();
		createPageController();
		createCssEditor();
		createPageTextEditor();
		refreshPageMainEditor();
		refreshPageControllerEditor();
		refreshCssEditor();
		setDirty(false);
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
		if (activePage == 0 && (introEditor.isUpdated() || introEditor.isDirty()))
			updateIntroEditor();
		if (activePage == 1 && (pageMainEditor.isUpdated() || pageMainEditor.isDirty()))
			updateFile();
		if (activePage == 3 && (cssEditor.isUpdated() || cssEditor.isDirty()))
			updateCssEditor();
		getTextEditor().doSave(monitor);
		if (pageController.isDirty())
			pageController.saveErMessages();
		if (cssEditor.isDirty())
			cssEditor.saveCssVars();
		if (koxEditor != null && koxEditor.isDirty())
			koxEditor.doSave(monitor);
		if (moxEditor != null && moxEditor.isDirty())
			moxEditor.doSave(monitor);
		setDirty(false);
	}

	public IAction getAction(String id) {
		IEditorPart editor = getTextEditor();
		return ((ITextEditor) editor).getAction(id);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs() {
		if (activePage == 0 && introEditor.isUpdated() || introEditor.isDirty())
			updateIntroEditor();
		if (activePage == 1 && (pageMainEditor.isUpdated() || pageMainEditor.isDirty()))
			updateFile();
		IEditorPart editor = getTextEditor();
		editor.doSaveAs();
		setPageText(getTextEditorIndex(), editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(getTextEditorIndex());
		IDE.gotoMarker(getTextEditor(), marker);
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

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public boolean isDirty() {
		return super.isDirty() || pageController.isDirty() || introEditor.isDirty() || pageMainEditor.isDirty() || cssEditor.isDirty();
	}

	public PageControllerEditor getPageController() {
		return this.pageController;
	}

	protected void pageChange(int newPageIndex) {
		if (activePage == 0 && introEditor.isUpdated())
			updateIntroEditor();
		if (activePage == 1 && pageMainEditor.isUpdated())
			updateFile();

		if (activePage == 3 && cssEditor.isUpdated())
			updateCssEditor();

		if (activePage == getPageCount() - 1) {
			// updateProperties();
			refreshPageControllerEditor();
			setMoxFile(properties.getProperty("mappingFile"));
			cssEditor.setCssFile(properties.getProperty("cssFile"));
		}

		if (newPageIndex == 0)
			refreshIntroEditor();
		if (newPageIndex == 1)
			refreshPageMainEditor();
		if (newPageIndex == 3)
			refreshCssEditor();

		super.pageChange(newPageIndex);
		activePage = newPageIndex;
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			if (event.getDelta() != null) {
				if (event.getDelta().getKind() == IResourceDelta.CHANGED || event.getDelta().getKind() == IResourceDelta.REMOVED) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							pageMainEditor.refreshLstClasses();
							pageMainEditor.updateBuildPathStatus(getEditor(1));
							pageMainEditor.checkCache();
						}
					});
				}
			}
		}
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput()).getFile().getProject().equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	private void refreshIntroEditor() {

		rubriques.setRubriqueValue("Cache", "cacheType", properties.getProperty("cacheType"));
		rubriques.setRubriqueValue("Cache", "useCache", properties.getProperty("useCache"));

		rubriques.setRubriqueValue("Divers", "useSetters", properties.getProperty("useSetters"));
		rubriques.setRubriqueValue("Divers", "constraintsDepth", properties.getProperty("constraintsDepth"));
		rubriques.setRubriqueValue("Divers", "koDateFormat", properties.getProperty("koDateFormat"));
		rubriques.setRubriqueValue("Divers", "sqlDateFormat", properties.getProperty("sqlDateFormat"));
		rubriques.setRubriqueValue("Divers", "nullValue", properties.getProperty("nullValue"));
		rubriques.setRubriqueValue("Divers", "pluginVersion", properties.getProperty("pluginVersion", "?.?.?.?"));
		rubriques.setRubriqueValue("Divers", "debug", properties.getProperty("debug"));
		rubriques.setRubriqueValue("Divers", "clientDebug", properties.getProperty("clientDebug"));
		rubriques.setRubriqueValue("Divers", "clientDebugOptions", properties.getProperty("clientDebugOptions"));
		rubriques.setRubriqueValue("Vues", "headerURL", properties.getProperty("headerURL"));
		rubriques.setRubriqueValue("Vues", "footerURL", properties.getProperty("footerURL"));
		rubriques.setRubriqueValue("Vues", "cssFile", properties.getProperty("cssFile"));
		rubriques.setRubriqueValue("Validation", "validationFile", properties.getProperty("validationFile"));
		rubriques.setRubriqueValue("Validation", "erFile", properties.getProperty("erFile"));
		rubriques.setRubriqueValue("Validation", "messagesFile", properties.getProperty("messagesFile"));
		rubriques.setRubriqueValue("Contrôleur", "controlClass", properties.getProperty("controlClass"));
		rubriques.setRubriqueValue("Contrôleur", "mappingFile", properties.getProperty("mappingFile"));
		rubriques.setRubriqueValue("Base de données", "dbOptions", properties.getProperty("dbOptions"));
		rubriques.setRubriqueValue("Base de données", "base", properties.getProperty("base"));
		rubriques.setRubriqueValue("Base de données", "host", properties.getProperty("host"));
		rubriques.setRubriqueValue("Base de données", "port", properties.getProperty("port"));
		rubriques.setRubriqueValue("Base de données", "user", properties.getProperty("user"));
		rubriques.setRubriqueValue("Base de données", "password", properties.getProperty("password"));
		rubriques.setRubriqueValue("ORM", "package", properties.getProperty("package"));
		introEditor.setRubriques(rubriques.getRubriques());
	}

	private void updateIntroEditor() {
		for (ConfigVariable cV : rubriques.getVariables()) {
			if (cV.getValue() != null)
				properties.put(cV.getName(), cV.getValue());
		}
		OutputStream o = new ByteArrayOutputStream();
		try {
			properties.store(o, "Fichier de configuration KoCreator version " + Activator.VERSION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(o.toString());
		setMoxFile(properties.getProperty("mappingFile"));
		introEditor.setUpdated(false);
	}

	public void updateCssEditor() {
		properties.put("cssFile", cssEditor.getCssFile());
		OutputStream o = new ByteArrayOutputStream();
		try {
			properties.store(o, "Fichier de configuration KoCreator version " + Activator.VERSION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(o.toString());
		cssEditor.setUpdated(false);
	}

	public void updateFile() {
		PageMainEditor p = (PageMainEditor) pageMainEditor;
		properties.put("base", p.getBase());
		properties.put("port", p.getPort());
		properties.put("host", p.getHost());
		properties.put("user", p.getUser());
		properties.put("password", p.getPassword());
		properties.put("package", p.getPackage());
		properties.put("classes", p.getClasses());
		properties.put("dbOptions", p.getOptions());
		properties.put("pluginVersion", Activator.VERSION);
		properties.put("validationFile", pageController.getValidationFile());
		properties.put("messagesFile", pageController.getMessagesFile());
		properties.put("erFile", pageController.getErFile());
		OutputStream o = new ByteArrayOutputStream();
		try {
			properties.store(o, "Fichier de configuration KoCreator version " + Activator.VERSION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(o.toString());
		pageController.setUpdated(false);
	}

	private void refreshPageMainEditor() {
		String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		try {
			properties.load(new ByteArrayInputStream(editorText.getBytes(Charset.defaultCharset())));
			setMoxFile(properties.getProperty("mappingFile"));
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
		pageMainEditor.updateBuildPathStatus(getTextEditor());
		pageMainEditor.setUpdated(false);
	}

	private void refreshPageControllerEditor() {
		String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		try {
			properties.load(new ByteArrayInputStream(editorText.getBytes(Charset.defaultCharset())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pageController.setValidationFile(properties.getProperty("validationFile"));
		pageController.setMessagesFile(properties.getProperty("messagesFile"));
		pageController.setErFile(properties.getProperty("erFile"));
		pageController.setUpdated(false);
	}

	private void refreshCssEditor() {
		String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		try {
			properties.load(new ByteArrayInputStream(editorText.getBytes(Charset.defaultCharset())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cssEditor.setCssFile(properties.getProperty("cssFile"));
		cssEditor.setUpdated(false);
	}

	@Override
	protected IEditorSite createSite(IEditorPart page) {
		IEditorSite site = null;
		if (page.equals(koxEditor) || page.equals(moxEditor)) {
			site = new MultiPageEditorSite(this, page) {
				public String getId() {
					// Sets this ID so nested editor is configured for XML
					// source
					return ContentTypeIdForXML.ContentTypeID_XML + ".source"; //$NON-NLS-1$;
				}
			};
		}
		else {
			site = super.createSite(page);
		}
		return site;
	}

	public void setKoxFile(String koxFileName) {

		IFile fileToBeOpened = pageController.getIFile(koxFileName);
		if (koxEditor == null) {
			try {
				koxEditor = new XMLStructuredTextEditor(this);

				addPage(4, koxEditor, new FileEditorInput(fileToBeOpened));
				setPageText(4, "Validation (kox.xml)");
				addCobElements("Validation (kox.xml)", Images.XML, koxEditor);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			koxEditor.setInput(new FileEditorInput(fileToBeOpened));
	}

	public void setMoxFile(String moxFileName) {
		if (moxFileName != null && !moxFileName.equals(mappingFile)) {
			IFile fileToBeOpened = pageController.getIFile(moxFileName);
			if (moxEditor == null) {
				try {
					moxEditor = new XMLStructuredTextEditor(this);
					addPage(4, moxEditor, new FileEditorInput(fileToBeOpened));
					setPageText(4, "Mappings (mox.xml)");
					addCobElements("Mappings (mox.xml)", Images.XML, moxEditor);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				moxEditor.setInput(new FileEditorInput(fileToBeOpened));
		}
		mappingFile = moxFileName;
	}

	public void gotoPageMainEditor() {
		gotoPage(pageMainEditor.getPageIndex());

	}

	public void gotoPageControllerEditor() {
		gotoPage(pageController.getPageIndex());
	}

	public void gotoPageMox() {
		setActiveEditor(moxEditor);
	}

	public void gotoPageKox() {
		// gotoPage(koxEditor.getPageIndex());
		setActiveEditor(koxEditor);

	}

	public void gotoCssEditor() {
		gotoPage(cssEditor.getPageIndex());

	}

	private void setDirty(boolean dirty) {
		pageController.setDirty(dirty);
		pageMainEditor.setDirty(dirty);
		introEditor.setDirty(dirty);
	}

	public void firePropDirty() {
		firePropertyChange(IEditorPart.PROP_DIRTY);

	}

	private void addCobElements(String title, String imgName, EditorComposite compoEditor) {
		ContentOutlineBeanPage cobPage = new ContentOutlineBeanPage(title, imgName, compoEditor);
		compoEditor.setCob(cobPage);
		cobPage.setChilds(compoEditor.getCobElements());
		cobElements.add(cobPage);
	}

	private void addCobElements(String title, String imgName, XMLStructuredTextEditor xmlEditor) {
		ContentOutlineBeanPage cobPage = new ContentOutlineBeanPage(title, imgName, xmlEditor);
		xmlEditor.setCob(cobPage);
		cobElements.add(cobPage);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new ContentOutLineEditor();
				outlinePage.setInput(editor.getDocumentProvider().getDocument(getEditorInput()));
				outlinePage.setEditor(this);
				outlinePage.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						if (event.getSelection() instanceof IStructuredSelection) {
							IStructuredSelection selection = (IStructuredSelection) event.getSelection();
							if (!selection.isEmpty()) {
								ContentOutlineBean cob = (ContentOutlineBean) selection.getFirstElement();
								cob.select();
							}
						}
					}
				});
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}

	public ArrayList<ContentOutlineBean> getCobElements() {
		return cobElements;
	}

	public ContentOutLineEditor getOutlinePage() {
		return outlinePage;
	}
}
