package net.ko.creator.editors;

import net.ko.bean.ContentOutlineBean;

import org.eclipse.ui.editors.text.TextEditor;


public class KoTextEditor extends TextEditor {
	protected ContentOutlineBean cob;
	protected MultiPageEditor multipageEditor;
	public KoTextEditor(MultiPageEditor multipageEditor) {
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
