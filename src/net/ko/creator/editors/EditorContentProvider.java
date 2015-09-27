package net.ko.creator.editors;

import java.util.ArrayList;

import net.ko.bean.ContentOutlineBean;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class EditorContentProvider implements ITreeContentProvider {
	
	private static final Object[] EMPTY = {};
	private TreeViewer viewer;
	private IDocument document;
	private ArrayList<ContentOutlineBean> cobElements;
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	public EditorContentProvider(ArrayList<ContentOutlineBean> elements) {
		super();
		this.cobElements=elements;
	}
	private IDocumentListener documentListener = new IDocumentListener() {
		@Override
		public void documentChanged(DocumentEvent event) {
			if (!EditorContentProvider.this.viewer.getControl().isDisposed()) {
				EditorContentProvider.this.viewer.refresh();
			}
		}
		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}
	};
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.viewer = (TreeViewer) viewer;
			
			if (oldInput instanceof IDocument) {
					document.removeDocumentListener(documentListener);
			}
			
			if (newInput instanceof IDocument) {
					document = (IDocument) newInput;
					document.addDocumentListener(documentListener);
			}
	}

	@Override
	public Object[] getChildren(Object element) {
		if(element!=null&&element instanceof ContentOutlineBean){
			ContentOutlineBean coB=(ContentOutlineBean) element;
			return coB.getChilds().toArray();
		}
		return EMPTY;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		IDocument document = getDocument(inputElement);
		if (document != null) {
			if(cobElements!=null)
				return cobElements.toArray();
		}
		return EMPTY;
	}

	@Override
	public Object getParent(Object element) {
		ContentOutlineBean coB=(ContentOutlineBean) element;
		return coB.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		ContentOutlineBean coB=(ContentOutlineBean) element;
		return coB.getChilds().size()>0;
	}

	private IDocument getDocument(Object inputElement) {
			if (inputElement instanceof IDocument) {
					return (IDocument) inputElement;
			}
			return null;
	}

}
