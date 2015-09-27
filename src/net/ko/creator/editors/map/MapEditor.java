package net.ko.creator.editors.map;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.actions.RelatedNodesVisibleAction;
import net.ko.creator.editors.map.actions.VisibleAction;
import net.ko.creator.editors.map.commands.AppContextMenuProvider;
import net.ko.creator.editors.map.factory.ConnectionCreationFactory;
import net.ko.creator.editors.map.factory.NodeCreationFactory;
import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;
import net.ko.creator.editors.map.listeners.MappingTransferDropTargetListener;
import net.ko.creator.editors.map.listeners.MyTemplateTransferDropTargetListener;
import net.ko.creator.editors.map.model.AjaxAccordion;
import net.ko.creator.editors.map.model.AjaxDeleteMulti;
import net.ko.creator.editors.map.model.AjaxDeleteOne;
import net.ko.creator.editors.map.model.AjaxDialogButton;
import net.ko.creator.editors.map.model.AjaxFireEvent;
import net.ko.creator.editors.map.model.AjaxFunction;
import net.ko.creator.editors.map.model.AjaxInclude;
import net.ko.creator.editors.map.model.AjaxIncludeDialog;
import net.ko.creator.editors.map.model.AjaxMessage;
import net.ko.creator.editors.map.model.AjaxMessageDialog;
import net.ko.creator.editors.map.model.AjaxRefreshControl;
import net.ko.creator.editors.map.model.AjaxRefreshFormValues;
import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.creator.editors.map.model.AjaxSelector;
import net.ko.creator.editors.map.model.AjaxShowhide;
import net.ko.creator.editors.map.model.AjaxSubmitForm;
import net.ko.creator.editors.map.model.AjaxUpdateOne;
import net.ko.creator.editors.map.model.AjaxUpdateOneField;
import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.model.CssOneTransition;
import net.ko.creator.editors.map.model.CssTransition;
import net.ko.creator.editors.map.model.JS;
import net.ko.creator.editors.map.model.MoxFile;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.part.TogglePart;
import net.ko.creator.editors.map.part.tree.AppTreeEditPartFactory;
import net.ko.creator.utils.FrameworkUtils;
import net.ko.mapping.KXmlMappings;
import net.ko.utils.KProperties;
import net.ko.xml.KMoxPersistance;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class MapEditor extends GraphicalEditorWithPalette implements
		ITabbedPropertySheetPageContributor, PropertyChangeListener,
		IPropertyChangeListener {
	public static final String ID = "net.ko.creator.map.editor";
	private boolean editorSaving;
	private IFile file;
	private MoxFile model;
	private Node fileNode;
	private KeyHandler keyHandler;
	private boolean dirty;

	protected class OutlinePage extends ContentOutlinePage implements
			IPropertyListener {

		private SashForm sash;
		private MapEditor mapEditor;
		private ScrollableThumbnail thumbnail;
		private DisposeListener disposeListener;

		public OutlinePage(MapEditor mapEditor) {
			super(new TreeViewer());
			this.mapEditor = mapEditor;
		}

		public void createControl(Composite parent) {
			sash = new SashForm(parent, SWT.VERTICAL);

			getViewer().createControl(sash);

			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new AppTreeEditPartFactory());
			getViewer().setContents(model);
			getSelectionSynchronizer().addViewer(getViewer());
			Canvas canvas = new Canvas(sash, SWT.BORDER);
			LightweightSystem lws = new LightweightSystem(canvas);

			thumbnail = new ScrollableThumbnail(
					(Viewport) ((ScalableRootEditPart) getGraphicalViewer()
							.getRootEditPart()).getFigure());
			thumbnail.setSource(((ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart())
					.getLayer(LayerConstants.PRINTABLE_LAYERS));

			lws.setContents(thumbnail);

			disposeListener = new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					if (thumbnail != null) {
						thumbnail.deactivate();
						thumbnail = null;
					}
				}
			};
			getGraphicalViewer().getControl().addDisposeListener(disposeListener);

		}

		public void init(IPageSite pageSite) {
			super.init(pageSite);

			// On hook les actions de l'editeur sur la toolbar

			IActionBars bars = getSite().getActionBars();
			bars.setGlobalActionHandler(ActionFactory.UNDO.getId(),
					getActionRegistry().getAction(ActionFactory.UNDO.getId()));
			bars.setGlobalActionHandler(ActionFactory.REDO.getId(),
					getActionRegistry().getAction(ActionFactory.REDO.getId()));
			bars.setGlobalActionHandler(ActionFactory.DELETE.getId(),
					getActionRegistry().getAction(ActionFactory.DELETE.getId()));
			bars.updateActionBars();

			getViewer().setKeyHandler(keyHandler);
			addContextualMenu(getViewer());
			mapEditor.addPropertyListener(this);

		}

		public Control getControl() {
			return sash;
		}

		public void dispose() {
			getSelectionSynchronizer().removeViewer(getViewer());
			if (getGraphicalViewer().getControl() != null
					&& !getGraphicalViewer().getControl().isDisposed())
				getGraphicalViewer().getControl().removeDisposeListener(disposeListener);
			mapEditor.removePropertyListener(this);
			super.dispose();
		}

		public void refresh() {
			final EditPartViewer viewer = getViewer();
			final EditPart contents = viewer.getContents();
			if (contents != null) {
				contents.refresh();
			}
		}

		@Override
		public void propertyChanged(Object source, int propId) {
			refresh();
		}
	}

	public MapEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}

	private void loadModel() throws IOException {
		KProperties properties = FrameworkUtils.koConfigFile();
		String moxFileName = properties.getProperty("mappingFile", "conf/mox.xml");
		model = new MoxFile(this);
		model.setFileName(moxFileName);
		model.setPackageName(properties.getProperty("package", "net.kernel"));
		List<Node> allNodes = fileNode.getAllChildrenArray();
		List<Node> allRequestNodes = new ArrayList<>(model.getChildrenArray());
		int autoPos = 20;
		if (allNodes != null) {
			for (Node n : allNodes) {
				Node search = model.getNodeById(n.getId());
				if (search != null) {
					allRequestNodes.remove(search);
					search.setLayout(n.getLayout());
					search.setVisible(n.isVisible());
					search.setExpanded(n.isExpanded());
					AppearenceConfig appConfig = n.getAppearenceConfig();
					if (appConfig != null)
						search.setAppearenceConfig(n.getAppearenceConfig());
					Connection connSource = search.getConnectionSource();
					Connection connSourceN = n.getConnectionSource();
					if (connSource != null && connSourceN != null)
						connSource.setBendPoints(connSourceN.getBendpoints());
				}
			}
		}
		for (Node requestNode : allRequestNodes) {
			requestNode.setLayout(new Rectangle(autoPos, autoPos, -1, -1));
			autoPos += 20;
		}
	}

	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(model);
		viewer.addDropTargetListener(new MyTemplateTransferDropTargetListener(viewer));
		viewer.addDropTargetListener(new MappingTransferDropTargetListener(viewer));
		MapParameters.addPropertyChangeListener(this);
		PlatformUI.getWorkbench().getPreferenceStore().addPropertyChangeListener(this);
		/*
		 * InstanceScope.INSTANCE.getNode(
		 * "org.eclipse.ui.preferencePages.ColorsAndFonts"
		 * ).addPreferenceChangeListener(new
		 * IEclipsePreferences.IPreferenceChangeListener() {
		 * 
		 * @Override public void preferenceChange(PreferenceChangeEvent evt) {
		 * System.out.println(evt.getKey());
		 * 
		 * } });
		 */
		PreferenceManager pm =
				PlatformUI.getWorkbench().getPreferenceManager();
		List<IPreferenceNode> l =
				pm.getElements(PreferenceManager.PRE_ORDER);
		for (IPreferenceNode node : l) {
			System.out.println("Label:" +
					node.getLabelText() + " ID:" + node.getId());
		}

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		editorSaving = true;
		if (file != null) {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				public void execute(final IProgressMonitor monitor) {
					try {
						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(outStream);
						out.writeObject(getModel());
						file.setContents(new ByteArrayInputStream(outStream.toByteArray()), true, false, monitor);
						getCommandStack().markSaveLocation();
						out.close();
						KXmlMappings xmlMappings = (KXmlMappings) model.getXmlObject();
						KMoxPersistance moxPersist = new KMoxPersistance(xmlMappings);
						moxPersist.save();
						IPath myPath = new Path(xmlMappings.getXmlFile().getFileName());
						IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(myPath);
						if (file.exists()) {
							file.refreshLocal(IResource.DEPTH_ONE, null);
						}
						setDirty(false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			try {
				new ProgressMonitorDialog(getSite().getWorkbenchWindow()
						.getShell()).run(false, true, op);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		editorSaving = false;
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new AppEditorPartFactory());
		addZoom(viewer);
		addKeyHandler(viewer);
		addContextualMenu(viewer);
	}

	private void addContextualMenu(EditPartViewer viewer) {
		ContextMenuProvider provider = new AppContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(provider);
	}

	private void addKeyHandler(GraphicalViewer viewer) {
		keyHandler = new KeyHandler();

		keyHandler.put(
				KeyStroke.getPressed(SWT.DEL, 127, 0),
				getActionRegistry().getAction(ActionFactory.DELETE.getId()));

		keyHandler.put(
				KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0),
				getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));

		keyHandler.put(
				KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0),
				getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));

		viewer.setProperty(
				MouseWheelHandler.KeyGenerator.getKey(SWT.NONE),
				MouseWheelZoomHandler.SINGLETON);

		viewer.setKeyHandler(keyHandler);
	}

	private void addZoom(GraphicalViewer viewer) {
		double[] zoomLevels;
		ArrayList<String> zoomContributions;
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		viewer.setRootEditPart(rootEditPart);

		ZoomManager manager = rootEditPart.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));

		zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
		manager.setZoomLevels(zoomLevels);

		zoomContributions = new ArrayList<String>();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		file = ((FileEditorInput) input).getFile();
		try {

			InputStream is = file.getContents();
			ObjectInputStream ois = new ObjectInputStream(is);
			fileNode = (Node) ois.readObject();
			ois.close();
			setPartName(file.getName());
		} catch (Exception e) {
			fileNode = new MoxFile(this);
			e.printStackTrace();
		}
		try {
			if (fileNode == null)
				fileNode = new MoxFile(this);
			loadModel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refreshInput() {
		setInput(getEditorInput());
		getGraphicalViewer().setContents(model);
		// model.fireRefreshVisuals(true);
	}

	public MoxFile getModel() {
		return model;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public String getContributorId() {
		// TODO Auto-generated method stub
		return getSite().getId();
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class)
			return new TabbedPropertySheetPage(this);
		if (adapter == IContentOutlinePage.class) {
			return new OutlinePage(this);
		}
		if (adapter == ZoomManager.class)
			return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
		return super.getAdapter(adapter);
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		// Racine de la palette
		PaletteRoot root = new PaletteRoot();

		// Creation d'un groupe (pour organiser un peu la palette)
		PaletteGroup manipGroup = new PaletteGroup("Manipulation d'objets");
		root.add(manipGroup);

		// Ajout de l'outil de selection et de l'outil de selection groupe
		SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
		manipGroup.add(selectionToolEntry);
		manipGroup.add(new MarqueeToolEntry());

		// Definition l'entree dans la palette qui sera utilise par defaut :
		// 1.lors de la premiere ouverture de la palette
		// 2.lorsqu'un element de la palette rend la main
		PaletteSeparator sep2 = new PaletteSeparator();
		root.add(sep2);

		// PaletteGroup instGroup = new PaletteGroup("Creation d'elements");
		// root.add(instGroup);
		PaletteDrawer instGroup = new PaletteDrawer("Eléments", Images.getImageDescriptor(Images.ELEMENTS));
		root.add(instGroup);
		instGroup.add(new CreationToolEntry("Request", "Creation d'une inclusion KAjaxRequest",
				new NodeCreationFactory(AjaxRequest.class),
				Images.getImageDescriptor(Images.REQUEST), null));
		instGroup.add(new CombinedTemplateCreationEntry("Js", "Creation d'une inclusion KAjaxJS",
				JS.class, new NodeCreationFactory(JS.class),
				Images.getImageDescriptor(Images.JS), null));
		instGroup.add(new CombinedTemplateCreationEntry("Include", "Creation d'une inclusion KAjaxInclude",
				AjaxInclude.class, new NodeCreationFactory(AjaxInclude.class),
				Images.getImageDescriptor(Images.INCLUDE), null));
		instGroup.add(new CombinedTemplateCreationEntry("Selector", "Creation d'une inclusion KAjaxSelector",
				AjaxSelector.class, new NodeCreationFactory(AjaxSelector.class),
				Images.getImageDescriptor(Images.SELECTOR), null));
		instGroup.add(new CombinedTemplateCreationEntry("Message", "Creation d'une inclusion KAjaxMessage",
				AjaxMessage.class, new NodeCreationFactory(AjaxMessage.class),
				Images.getImageDescriptor(Images.MESSAGE), null));
		instGroup.add(new CombinedTemplateCreationEntry("ShowHide", "Creation d'une inclusion KAjaxShowHide",
				AjaxShowhide.class, new NodeCreationFactory(AjaxShowhide.class),
				Images.getImageDescriptor(Images.SHOW_HIDE), null));
		instGroup.add(new CombinedTemplateCreationEntry("FireEvent", "Creation d'une inclusion KAjaxEvent",
				AjaxFireEvent.class, new NodeCreationFactory(AjaxFireEvent.class),
				Images.getImageDescriptor(Images.FIRE_EVENT), null));
		instGroup.add(new CombinedTemplateCreationEntry("Function", "Creation d'une inclusion KAjaxFunction",
				AjaxFunction.class, new NodeCreationFactory(AjaxFunction.class),
				Images.getImageDescriptor(Images.FUNCTION), null));
		instGroup.add(new CombinedTemplateCreationEntry("UpdateOne", "Creation d'une inclusion KAjaxUpdateOne",
				AjaxUpdateOne.class, new NodeCreationFactory(AjaxUpdateOne.class),
				Images.getImageDescriptor(Images.UPDATE_ONE), null));
		instGroup.add(new CombinedTemplateCreationEntry("UpdateOneField", "Creation d'une inclusion KAjaxUpdateOneField",
				AjaxUpdateOneField.class, new NodeCreationFactory(AjaxUpdateOneField.class),
				Images.getImageDescriptor(Images.UPDATE_ONE_FIELD), null));
		instGroup.add(new CombinedTemplateCreationEntry("DeleteOne", "Creation d'une inclusion KAjaxDeleteOne",
				AjaxDeleteOne.class, new NodeCreationFactory(AjaxDeleteOne.class),
				Images.getImageDescriptor(Images.DELETE_ONE), null));
		instGroup.add(new CombinedTemplateCreationEntry("DeleteMulti", "Creation d'une inclusion KAjaxDeleteMulti",
				AjaxDeleteMulti.class, new NodeCreationFactory(AjaxDeleteMulti.class),
				Images.getImageDescriptor(Images.DELETE_MULTI), null));
		instGroup.add(new CombinedTemplateCreationEntry("SubmitForm", "Creation d'une inclusion KAjaxSubmitForm",
				AjaxSubmitForm.class, new NodeCreationFactory(AjaxSubmitForm.class),
				Images.getImageDescriptor(Images.SUBMIT_FORM), null));
		instGroup.add(new CombinedTemplateCreationEntry("RefreshControl", "Creation d'une inclusion KAjaxRefreshControl",
				AjaxRefreshControl.class, new NodeCreationFactory(AjaxRefreshControl.class),
				Images.getImageDescriptor(Images.REFRESH_CONTROL), null));
		instGroup.add(new CombinedTemplateCreationEntry("RefreshFormValues", "Creation d'une inclusion KAjaxRefreshFormValues",
				AjaxRefreshFormValues.class, new NodeCreationFactory(AjaxRefreshFormValues.class),
				Images.getImageDescriptor(Images.REFRESH_FORM_VALUES), null));
		instGroup.add(new CombinedTemplateCreationEntry("Accordion", "Creation d'une inclusion KAjaxAccordion",
				AjaxAccordion.class, new NodeCreationFactory(AjaxAccordion.class),
				Images.getImageDescriptor(Images.ACCORDION), null));
		PaletteSeparator sep3 = new PaletteSeparator();
		root.add(sep3);
		instGroup.add(new CombinedTemplateCreationEntry("TransitionGroupe", "Creation d'une inclusion CssTransition",
				CssTransition.class, new NodeCreationFactory(CssTransition.class),
				Images.getImageDescriptor(Images.TRANSITION), null));
		instGroup.add(new CombinedTemplateCreationEntry("Transition", "Creation d'une inclusion CssOneTransition",
				CssOneTransition.class, new NodeCreationFactory(CssOneTransition.class),
				Images.getImageDescriptor(Images.ONE_TRANSITION), null));
		PaletteDrawer dialogElements = new PaletteDrawer("Dialogs", Images.getImageDescriptor(Images.DIALOGS));
		root.add(dialogElements);
		dialogElements.add(new CombinedTemplateCreationEntry("MessageDialog", "Creation d'une inclusion KAjaxMessageDialog",
				AjaxMessageDialog.class, new NodeCreationFactory(AjaxMessageDialog.class),
				Images.getImageDescriptor(Images.MESSAGE_DIALOG), null));
		dialogElements.add(new CombinedTemplateCreationEntry("IncludeDialog", "Creation d'une inclusion KAjaxIncludeDialog",
				AjaxIncludeDialog.class, new NodeCreationFactory(AjaxIncludeDialog.class),
				Images.getImageDescriptor(Images.INCLUDE_DIALOG), null));
		dialogElements.add(new CombinedTemplateCreationEntry("ButtonDialog", "Creation d'une inclusion KAjaxButtonDialog",
				AjaxDialogButton.class, new NodeCreationFactory(AjaxDialogButton.class),
				Images.getImageDescriptor(Images.DIALOG_BUTTON), null));

		PaletteDrawer connectionElements = new PaletteDrawer("Liens", Images.getImageDescriptor(Images.CONNECTORS));
		root.add(connectionElements);

		connectionElements.add(new ConnectionCreationToolEntry("URL connector", "Connecteur d'urls",
				new ConnectionCreationFactory(),
				Images.getImageDescriptor(Images.CONNECTION),
				null));
		root.setDefaultEntry(selectionToolEntry);
		return root;
	}

	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action = new VisibleAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		action = new RelatedNodesVisibleAction(this, false);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		action = new RelatedNodesVisibleAction(this, true);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		boolean isToggle = false;
		if (selection instanceof IStructuredSelection) {
			List<Object> selecteds = ((IStructuredSelection) selection).toList();
			if (selecteds.size() == 1) {
				Object o = selecteds.get(0);
				if (o instanceof TogglePart) {
					TogglePart togglePart = ((TogglePart) o);
					togglePart.expandCollapse();
					getSite().getSelectionProvider().setSelection(new StructuredSelection(togglePart.getParent()));
					isToggle = true;
				}
			}
		}
		if (!isToggle)
			super.selectionChanged(part, selection);
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	protected void initializePaletteViewer() {
		super.initializePaletteViewer();
		getPaletteViewer().addDragSourceListener(
				new TemplateTransferDragSourceListener(getPaletteViewer()));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MapParameters.REQUEST_BG_COLOR))
			getGraphicalViewer().getControl().redraw();
		else if (evt.getPropertyName().startsWith("ko.") || evt.getPropertyName().equals(MapParameters.ALL)) {
			if (model != null)
				model.refreshParameters(true);
		}
	}

	@Override
	public void dispose() {
		MapParameters.removePropertyChangeListener(this);
		PlatformUI.getWorkbench().getPreferenceStore().removePropertyChangeListener(this);
		super.dispose();
	}

	@Override
	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent evt) {
		if (evt.getProperty().startsWith("ko."))
			MapParameters.load();
	}

	public String getXmlFileName() {
		String result = "";
		if (model != null)
			result = model.getFileName();
		return result;
	}
}
