package net.ko.creator.editors;

import net.ko.bean.ContentOutlineBean;
import net.ko.creator.editors.images.Images;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class ContentOutLineEditor extends ContentOutlinePage {
	private IDocument input;
	private MultiPageEditor editor;
	private class SimpleLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if(element instanceof ContentOutlineBean){
				ContentOutlineBean elem =(ContentOutlineBean)element;
				return elem.getName();
			}
			return super.getText(element);
		}
		@Override
		public Image getImage(Object element) {
			String imgName=Images.BULLET_GRAY;
			if(element instanceof ContentOutlineBean){
				imgName=((ContentOutlineBean) element).getImg();
			}
			return new Image(Display.getCurrent(),getClass().getResourceAsStream(imgName));
		}
	}	
	public void setInput(IDocument element) {
		this.input = element;
		if (getTreeViewer()!=null) {
			getTreeViewer().setInput(element);
		}
	}
	public void setEditor(MultiPageEditor editor) {
		this.editor = editor;
	}
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer treeViewer = getTreeViewer();
		treeViewer.setContentProvider(new EditorContentProvider(editor.getCobElements()));
		treeViewer.setLabelProvider(new SimpleLabelProvider());
		treeViewer.setInput(this.input);
		getSite().setSelectionProvider(treeViewer);
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
}
