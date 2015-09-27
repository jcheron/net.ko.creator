package net.ko.creator.editors.templates;

import net.ko.bean.ZoneList;
import net.ko.bean.ZoneXml;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class MarkdownTextOutlinePage extends ContentOutlinePage implements CaretListener,ISelectionChangedListener{
	private ZoneList zones;
	private IDocument input;
	private MarkdownTextEditor editor;
	private int caretLine=-1;
	private boolean syncToOutline;
	private boolean syncFromOutline;
	
	private ZoneXml getZone(ITypedRegion region) throws BadLocationException{
		String text=input.get(region.getOffset(), region.getLength());
		return zones.getZoneMatch(text);
	}
	private class SimpleLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			String result="";
			if(element instanceof ITypedRegion){
				ITypedRegion region =(ITypedRegion)element;
				
				try {
					result=input.get(region.getOffset(), region.getLength());
					result=result.replaceFirst("^\\{#?(.*?)#?\\}$", "$1");
					return result;
				} catch (BadLocationException e) {}
			}
			return super.getText(element);
		}
		@Override
		public Image getImage(Object element) {
			String imgName="/icons/bZone.png";
			if(element instanceof ITypedRegion){
				try{
					ITypedRegion region =(ITypedRegion)element;
					ZoneXml zone=getZone(region);
					if(zone!=null)
						imgName="/icons/"+zone.getType().getImage();
				}catch(Exception e){}
			}
			return new Image(Display.getCurrent(),getClass().getResourceAsStream(imgName));
		}
	}
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer treeViewer = getTreeViewer();
		treeViewer.setContentProvider(new MarkdownSyntaxContentProvider(zones));
		treeViewer.setLabelProvider(new SimpleLabelProvider());
		treeViewer.setInput(this.input);
		getSite().setSelectionProvider(treeViewer);
	}
	public void setInput(IDocument element) {
		this.input = element;
		if (getTreeViewer()!=null) {
			getTreeViewer().setInput(element);
		}
	}
	/**
	 * Updates the outline page.
	 */
	public void update() {
		TreeViewer viewer= getTreeViewer();

		if (viewer != null) {
			Control control= viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(input);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}
	@Override
	public void caretMoved(CaretEvent event) {
		if (syncFromOutline) {
			syncFromOutline = false;
			return;
		}
		syncToOutline = true;
		 try {
			int caretLine = input.getLineOfOffset(event.caretOffset);
			if(caretLine!=this.caretLine){
				ITypedRegion region = input.getPartition(event.caretOffset);
				updateTreeViewer(region);
				this.caretLine=caretLine;
			}
		} catch (BadLocationException e1) {
		}
		 finally{
			 syncToOutline = false;
		 }
	}
	private void updateTreeViewer(ITypedRegion region){
		if (getTreeViewer() != null) {
			ISelection selection = new StructuredSelection(region);
			if (!selection.equals(getTreeViewer().getSelection())) {
				getTreeViewer().getTree().setFocus();
				getTreeViewer().setSelection(selection);
				getTreeViewer().reveal(region);
				getTreeViewer().expandToLevel(region, TreeViewer.ALL_LEVELS);
				getTreeViewer().getTree().setFocus();
				editor.setFocus();
			}
}
	}
	public ZoneList getZones() {
		return zones;
	}
	public void setZones(ZoneList zones) {
		this.zones = zones;
	}
	public void setEditor(MarkdownTextEditor editor) {
		this.editor=editor;
		
	}
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if(syncToOutline)
			return;
		syncFromOutline = true;
        	if(event.getSelection() instanceof IStructuredSelection){
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (!selection.isEmpty()) {
                        ITypedRegion region = (ITypedRegion) selection.getFirstElement();
                        editor.setHighlightRange(region.getOffset(), region.getLength(), true);
                }
             }
    	}
}