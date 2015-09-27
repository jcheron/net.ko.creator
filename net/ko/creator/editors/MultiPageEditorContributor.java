package net.ko.creator.editors;

import java.net.URL;

import net.ko.creator.Activator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;

/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 */
public class MultiPageEditorContributor extends MultiPageEditorActionBarContributor {


	@Override
	public void setActiveEditor(IEditorPart part) {
		// TODO Auto-generated method stub
		super.setActiveEditor(part);
		if(part instanceof MultiPageEditor){
			((MultiPageEditor)part).getPage().getForm().getToolBarManager().removeAll();
			((MultiPageEditor)part).getPage().getForm().getToolBarManager().add(selectAllTables);
			((MultiPageEditor)part).getPage().getForm().getToolBarManager().add(connectToDbAction);
			((MultiPageEditor)part).getPage().getForm().getToolBarManager().add(generateClasses);
			((MultiPageEditor)part).getPage().getForm().getToolBarManager().update(true);
		}
	}
	private IEditorPart activeEditorPart;
	private Action connectToDbAction;
	private Action selectAllTables;
	private Action generateClasses;
	/**
	 * Creates a multi-page contributor.
	 */
	public MultiPageEditorContributor() {
		super();
		createActions();
	}
	/**
	 * Returns the action registed with the given text editor.
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(ITextEditor editor, String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}
	/* (non-JavaDoc)
	 * Method declared in AbstractMultiPageEditorActionBarContributor.
	 */

	public void setActivePage(IEditorPart part) {
		if (activeEditorPart == part)
			return;

		activeEditorPart = part;

		IActionBars actionBars = getActionBars();
		if (actionBars != null) {

			ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor) part : null;
			actionBars.setGlobalActionHandler(connectToDbAction.getId(), connectToDbAction);
			actionBars.setGlobalActionHandler(selectAllTables.getId(), selectAllTables);
			actionBars.setGlobalActionHandler(generateClasses.getId(), generateClasses);
			actionBars.setGlobalActionHandler(
				ActionFactory.DELETE.getId(),
				getAction(editor, ITextEditorActionConstants.DELETE));
			actionBars.setGlobalActionHandler(
				ActionFactory.UNDO.getId(),
				getAction(editor, ITextEditorActionConstants.UNDO));
			actionBars.setGlobalActionHandler(
				ActionFactory.REDO.getId(),
				getAction(editor, ITextEditorActionConstants.REDO));
			actionBars.setGlobalActionHandler(
				ActionFactory.CUT.getId(),
				getAction(editor, ITextEditorActionConstants.CUT));
			actionBars.setGlobalActionHandler(
				ActionFactory.COPY.getId(),
				getAction(editor, ITextEditorActionConstants.COPY));
			actionBars.setGlobalActionHandler(
				ActionFactory.PASTE.getId(),
				getAction(editor, ITextEditorActionConstants.PASTE));
			actionBars.setGlobalActionHandler(
				ActionFactory.SELECT_ALL.getId(),
				getAction(editor, ITextEditorActionConstants.SELECT_ALL));
			actionBars.setGlobalActionHandler(
				ActionFactory.FIND.getId(),
				getAction(editor, ITextEditorActionConstants.FIND));
			actionBars.setGlobalActionHandler(
				IDEActionFactory.BOOKMARK.getId(),
				getAction(editor, IDEActionFactory.BOOKMARK.getId()));
			actionBars.updateActionBars();
		}
	}
	private IEditorPart getEditor(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}
	private void createActions() {
		connectToDbAction = new Action() {
			public void run() {
				IEditorPart editor= getEditor();
				if(editor!=null){
					((MultiPageEditor)(editor)).getPage().connectToDbAndAddTables();
					((MultiPageEditor)(editor)).gotoPage(0);
				}
			}
		};
		setActionAttributes(connectToDbAction, "connectToDb", "Connexion à la base de données", "Connexion à la base de données", "lightning_go.jpg");
		
		selectAllTables=new Action() {
			public void run(){
				IEditorPart editor= getEditor();
				if(editor!=null){
					if(((MultiPageEditor)(editor)).getActivePage()==1)
						((MultiPageEditor)(editor)).gotoPage(0);
					((MultiPageEditor)(editor)).getPage().selectAllTables(selectAllTables.isChecked());
				}
			}
		};
		selectAllTables.setChecked(false);
		setActionAttributes(selectAllTables, "selectAllTables", "Dé/Sélectionner toutes les tables", "Dé/Sélectionner toutes les tables", "check.png");
		generateClasses=new Action() {
			public void run(){
				IEditorPart editor= getEditor();
				if(editor!=null){
					if(((MultiPageEditor)(editor)).getActivePage()==1)
						((MultiPageEditor)(editor)).gotoPage(0);
					((MultiPageEditor)(editor)).getPage().createClasses();
				}				
			}
		};
		setActionAttributes(generateClasses, "generateAllClasses", "Générer", "générer les classes", "brick_go.png");
	}
	private void setActionAttributes(Action a,String id,String text,String toolTip,String image){
		a.setId(id);
		a.setText(text);
		a.setToolTipText(toolTip);
		a.setImageDescriptor(ImageDescriptor.createFromURL(Platform.getBundle(Activator.PLUGIN_ID).getResource("/net/ko/creator/editors/"+image)));		
	}
	public void contributeToMenu(IMenuManager manager) {
		IMenuManager menu = new MenuManager("&kobject Editor");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		menu.add(connectToDbAction);
		menu.add(selectAllTables);
		menu.add(generateClasses);
	}
	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		manager.add(connectToDbAction);
		manager.add(selectAllTables);
		manager.add(generateClasses);
	}
}
