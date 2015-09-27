package net.ko.creator.editors.templates;

import java.util.ArrayList;

import net.ko.bean.ZTypedRegion;
import net.ko.bean.ZoneList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class MarkdownSyntaxContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY = {};
	private TreeViewer viewer;
	private IDocument document;
	private ZoneList zones;

	private IDocumentListener documentListener = new IDocumentListener() {
			@Override
			public void documentChanged(DocumentEvent event) {
				if (!MarkdownSyntaxContentProvider.this.viewer.getControl().isDisposed()) {
					MarkdownSyntaxContentProvider.this.viewer.refresh();
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
	public Object[] getElements(Object inputElement) {
			IDocument document = getDocument(inputElement);
			if (document != null) {
					ArrayList<ZTypedRegion> result = new ArrayList<ZTypedRegion>();
					try {
							ITypedRegion[] regions = document.computePartitioning(0, document.getLength());
							for(ITypedRegion region : regions) {
								String type=region.getType();
								if (type.equals(MarkdownTextPartitionScanner.MARKDOWN_H1)||type.equals(MarkdownTextPartitionScanner.MARKDOWN_H2)) {
									ZTypedRegion zRegion=new ZTypedRegion(region);
									String text=document.get(region.getOffset(), region.getLength());
									zRegion.setExpression(text);
									zRegion.setZone(zones.getZoneMatch(text));
									zRegion.setDocument(document);
									result.add(zRegion);
								}
							}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					return result.toArray();
			}
			
			return EMPTY;
			
	}

	private IDocument getDocument(Object inputElement) {
			if (inputElement instanceof IDocument) {
					return (IDocument) inputElement;
			}
			return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		ArrayList<ITypedRegion> result = new ArrayList<ITypedRegion>();
		if(parentElement instanceof ITypedRegion){
			ITypedRegion parentRegion = (ITypedRegion) parentElement;
			if (parentRegion.equals(MarkdownTextPartitionScanner.MARKDOWN_H1)) {
							ITypedRegion[] regions;
							try {
								regions = document.computePartitioning(parentRegion.getOffset()+parentRegion.getLength(), document.getLength()-parentRegion.getOffset()-parentRegion.getLength());
									for(ITypedRegion h2region : regions) {
										if (h2region.getType().equals(MarkdownTextPartitionScanner.MARKDOWN_FIELD)) {
											String text=document.get(h2region.getOffset(), h2region.getLength());
											ZTypedRegion zRegion=new ZTypedRegion(h2region);
											zRegion.setExpression(text);
											zRegion.setZone(zones.getZoneMatch(text));
											zRegion.setDocument(document);
											result.add(zRegion);
										}
										if (h2region.getType().equals(MarkdownTextPartitionScanner.MARKDOWN_H1)) {
												break;
										}
									}
									
									return result.toArray();
							} catch (BadLocationException e) {
									e.printStackTrace();
							}
					}
			}
			return EMPTY;
	}

	public MarkdownSyntaxContentProvider(ZoneList zones) {
		super();
		this.zones = zones;
	}

	@Override
	public Object getParent(Object element) {
			return null;
	}

	@Override
	public boolean hasChildren(Object element) {
			if (element instanceof ITypedRegion) {
					ITypedRegion region = (ITypedRegion)element;
					if (region.getType().equals(MarkdownTextPartitionScanner.MARKDOWN_H1)) {
							return true;
					}
			}
			return false;
	}

	@Override
	public void dispose() {
	}
}