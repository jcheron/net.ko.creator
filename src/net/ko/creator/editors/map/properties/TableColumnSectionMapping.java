package net.ko.creator.editors.map.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public abstract class TableColumnSectionMapping {
	private String caption;
	private int width;

	public TableColumnSectionMapping(String caption, int width) {
		super();
		this.caption = caption;
		this.width = width;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public abstract CellEditor getCellEditor(Composite parent);

}
