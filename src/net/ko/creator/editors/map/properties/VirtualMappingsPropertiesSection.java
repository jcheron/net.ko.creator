package net.ko.creator.editors.map.properties;

import net.ko.mapping.KMapping;
import net.ko.mapping.KVirtualMapping;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;

public class VirtualMappingsPropertiesSection extends
		AbstractMappingsPropertiesSection {

	public VirtualMappingsPropertiesSection() {
		super();
		tableColumnMappingList.add(new TableColumnSectionMapping("requestURL", 40) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new TextCellEditor(parent);
			}
		});
		tableColumnMappingList.add(new TableColumnSectionMapping("mappingFor", 40) {
			@Override
			public CellEditor getCellEditor(Composite parent) {
				return new StringComboBoxCellEditor(parent, new String[] { "refreshControl", "refreshFormValues", "submitForm", "deleteOne", "deleteMulti", "updateOne" });
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
				return new ContentAssistCellEditor(parent);
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
		return new KVirtualMapping("POST", "requestURL.do", "", false, "", "");
	}

	@Override
	public String getColumnText(Object o, int index) {
		String result = super.getColumnText(o, index);
		if (index == 1)
			if (o instanceof KVirtualMapping)
				result = ((KVirtualMapping) o).getMappingFor();
		return result;
	}

	@Override
	public ViewerFilter getViewerFilter() {
		return new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof KVirtualMapping)
					return true;
				return false;
			}
		};
	}

	@Override
	public String getBtnAddCaption() {
		return "Ajouter un virtualMapping";
	}

}
