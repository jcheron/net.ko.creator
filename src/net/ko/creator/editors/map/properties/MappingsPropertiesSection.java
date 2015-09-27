package net.ko.creator.editors.map.properties;

import net.ko.mapping.KMapping;
import net.ko.mapping.KVirtualMapping;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;

public class MappingsPropertiesSection extends
		AbstractMappingsPropertiesSection {

	public MappingsPropertiesSection() {
		super();
		tableColumnMappingList.add(new TableColumnSectionMapping("requestURL", 40) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new TextCellEditor(parent);
			}
		});
		tableColumnMappingList.add(new TableColumnSectionMapping("responseURL", 40) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new ResourceCellEditor(parent, new String[] { "list", "view", "show", "jsp", "html", "htm" });
			}
		});
		tableColumnMappingList.add(new TableColumnSectionMapping("mainControl", 5) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new CheckboxCellEditor(parent);
			}
		});
		tableColumnMappingList.add(new TableColumnSectionMapping("classControl", 15) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new StringComboBoxCellEditor(parent, new String[] {});
			}
		});
		tableColumnMappingList.add(new TableColumnSectionMapping("method", 5) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new StringComboBoxCellEditor(parent, new String[] { "POST", "GET" });
			}
		});
		tableColumnMappingList.add(new TableColumnSectionMapping("queryString", 20) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new TextCellEditor(parent);
			}
		});
		tableColumnMappingList.add(new TableColumnSectionMapping("defaultTargetId", 10) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new TextCellEditor(parent);
			}
		});
	}

	@Override
	protected KMapping getNewMapping() {
		return new KMapping("GET", "requestURL.do", "", false, "", "");
	}

	@Override
	public String getColumnText(Object o, int index) {
		String result = super.getColumnText(o, index);
		if (index == 1)
			result = ((KMapping) o).getResponseURL();
		return result;
	}

	@Override
	public ViewerFilter getViewerFilter() {
		return new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof KVirtualMapping)
					return false;
				return true;
			}
		};
	}

	@Override
	public String getBtnAddCaption() {
		return "Ajouter un mapping";
	}
}
