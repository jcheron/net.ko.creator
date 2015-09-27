package net.ko.creator.editors;

import net.ko.bean.ContentOutlineBean;

import org.eclipse.wst.sse.ui.StructuredTextEditor;


public class XMLStructuredTextEditor extends StructuredTextEditor {
	protected ContentOutlineBean cob;
	protected MultiPageEditor multipageEditor;
	public XMLStructuredTextEditor(MultiPageEditor multipageEditor) {
		super();
		this.multipageEditor = multipageEditor;
	}

	public ContentOutlineBean getCob() {
		return cob;
	}

	public void setCob(ContentOutlineBean cob) {
		this.cob = cob;
	}

	public MultiPageEditor getMultipageEditor() {
		return multipageEditor;
	}

	public void setMultipageEditor(MultiPageEditor multipageEditor) {
		this.multipageEditor = multipageEditor;
	}
}
