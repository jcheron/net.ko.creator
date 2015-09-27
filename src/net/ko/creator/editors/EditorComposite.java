package net.ko.creator.editors;

import java.util.ArrayList;

import net.ko.bean.ContentOutlineBean;

import org.eclipse.swt.widgets.Composite;

public abstract class EditorComposite extends Composite {
	protected int pageIndex=-1;
	protected boolean isDirty;
	protected boolean updated=true;
	protected MultiPageEditor multiPageEditor;
	protected ArrayList<ContentOutlineBean> cobElements;
	protected ContentOutlineBean cob;
	
	public EditorComposite(Composite parent, int style) {
		super(parent, style);
		cobElements=new ArrayList<ContentOutlineBean>();
	}
	public void setMultiPageEditor(MultiPageEditor multiPageEditor){
		this.multiPageEditor=multiPageEditor;
	}
	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		if(isDirty)
			this.updated = isDirty;
		multiPageEditor.firePropDirty();
	}
	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public MultiPageEditor getMultiPageEditor() {
		return multiPageEditor;
	}
	public ArrayList<ContentOutlineBean> getCobElements() {
		return cobElements;
	}
	public void setCobElements(ArrayList<ContentOutlineBean> cobElements) {
		this.cobElements = cobElements;
	}
	public abstract void updateCobElements();
	public void setCob(ContentOutlineBean cob) {
		this.cob=cob;	
	}
	public ContentOutlineBean getCob() {
		return cob;
	}
	public void updateOutlinePage(){
		if(multiPageEditor!=null)
			if(multiPageEditor.getOutlinePage()!=null)
				multiPageEditor.getOutlinePage().update();
	}
}
