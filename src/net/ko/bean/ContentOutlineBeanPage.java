package net.ko.bean;

import org.eclipse.ui.IEditorPart;

import net.ko.creator.editors.EditorComposite;
import net.ko.creator.editors.KoTextEditor;
import net.ko.creator.editors.XMLStructuredTextEditor;

public class ContentOutlineBeanPage extends ContentOutlineBean {
	private Object page;
	public ContentOutlineBeanPage(String name, String img,Object page) {
		super(name, img);
		this.page=page;
	}
	public Object getPage() {
		return page;
	}
	public void setPage(Object page) {
		this.page = page;
	}
	@Override
	public void select() {
		if(page!=null){
			if(page instanceof EditorComposite){
				((EditorComposite) page).getMultiPageEditor().gotoPage(((EditorComposite)page).getPageIndex());
			}
			if(page instanceof XMLStructuredTextEditor){
				((XMLStructuredTextEditor) page).getMultipageEditor().setActiveEditor((IEditorPart) page);
			}
			if(page instanceof KoTextEditor){
				((KoTextEditor) page).getMultipageEditor().setActiveEditor((IEditorPart) page);
			}
		}
		
	}

}
