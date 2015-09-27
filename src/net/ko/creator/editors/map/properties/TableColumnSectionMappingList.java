package net.ko.creator.editors.map.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class TableColumnSectionMappingList implements
		Iterable<TableColumnSectionMapping> {
	private List<TableColumnSectionMapping> tableColumns;

	public TableColumnSectionMappingList() {
		super();
		tableColumns = new ArrayList<>();
	}

	public void add(TableColumnSectionMapping tableComumnSectionMapping) {
		tableColumns.add(tableComumnSectionMapping);
	}

	public String[] getCaptions() {
		List<String> result = new ArrayList<>();
		for (TableColumnSectionMapping tbl : tableColumns) {
			result.add(tbl.getCaption());
		}
		return result.toArray(new String[0]);
	}

	public CellEditor[] getCellEditors(Composite parent) {
		List<CellEditor> result = new ArrayList<>();
		for (TableColumnSectionMapping tbl : tableColumns) {
			result.add(tbl.getCellEditor(parent));
		}
		return result.toArray(new CellEditor[0]);
	}

	@Override
	public Iterator<TableColumnSectionMapping> iterator() {
		return tableColumns.iterator();
	}
}
