package net.ko.creator.editors.map.properties;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.MapParameters;
import net.ko.creator.editors.map.model.AjaxInclude;
import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.creator.editors.map.model.MoxFile;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.part.MoxFilePart;
import net.ko.mapping.KMapping;
import net.ko.mapping.KVirtualMapping;
import net.ko.utils.KString;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public abstract class AbstractMappingsPropertiesSection extends
		AbstractPropertySection implements DragSourceListener,
		IPropertyChangeListener {
	private Composite composite;
	private MoxFile moxFile;
	private TableViewer tableViewer;
	protected TableColumnSectionMappingList tableColumnMappingList;
	private Button btnDeleteMapping;

	public AbstractMappingsPropertiesSection() {
		super();
		tableColumnMappingList = new TableColumnSectionMappingList();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new GridLayout(1, false));
		composite = getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Composite compoButtons = getWidgetFactory().createFlatFormComposite(composite);
		compoButtons.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button btnNewMapping = new Button(compoButtons, SWT.NONE);
		btnNewMapping.setText(getBtnAddCaption());
		btnDeleteMapping = new Button(compoButtons, SWT.NONE);
		btnDeleteMapping.setText("Supprimer");
		btnDeleteMapping.setEnabled(false);
		final Composite tableCompo = new Composite(composite, SWT.NONE);

		tableViewer = new TableViewer(tableCompo, SWT.BORDER | SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();
		GridData fd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		fd_table.widthHint = 1;
		tableCompo.setLayoutData(fd_table);

		initColumns();
		tableCompo.setLayout(new TableColumnLayout());
		GridData gridDataTable = new GridData(SWT.FILL, SWT.CENTER, false, true, 2, 1);
		// gridDataTable.heightHint = 1505;
		table.setHeaderVisible(true);
		table.setLayoutData(gridDataTable);
		table.setLinesVisible(true);
		setProviders();
		attachCellEditors(tableViewer, table);

		btnNewMapping.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				KMapping mapping = getNewMapping();
				int position = 0;
				if (table.getSelectionIndex() != -1)
					position = table.getSelectionIndex() - 1;
				moxFile.getMappings().insertElement(mapping, position);
				moxFile.setDirty(true);
				tableViewer.refresh();
				table.setSelection(position);
				tableCompo.layout(true);
				Node.addPropertySourceUpdated(new Class[] { AjaxRequest.class, AjaxInclude.class });
			}
		});
		btnDeleteMapping.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = tableViewer.getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					for (Iterator<KMapping> iterator = sel.iterator(); iterator.hasNext();) {
						KMapping mapping = iterator.next();
						moxFile.getMappings().remove(mapping);
					}
					tableViewer.refresh();
					moxFile.setDirty(true);
				}
				Node.addPropertySourceUpdated(new Class[] { AjaxRequest.class, AjaxInclude.class });
			}
		});
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				btnDeleteMapping.setEnabled(selection.size() > 0);
			}
		});
		int operations = DND.DROP_COPY;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		tableViewer.addDragSupport(operations, transferTypes, this);
		MapParameters.getMyStore().addPropertyChangeListener(this);
	}

	protected abstract KMapping getNewMapping();

	private void setProviders() {
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void addListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getColumnText(Object o, int index) {
				return AbstractMappingsPropertiesSection.this.getColumnText(o, index);
			}

			@Override
			public Image getColumnImage(Object o, int index) {
				return AbstractMappingsPropertiesSection.this.getColumnImage(o, index);
			}
		});
	}

	public String getColumnText(Object o, int index) {
		String text = "";
		switch (index) {
		case 0:
			text = ((KMapping) o).getRequestURL();
			break;
		case 2:
			text = (((KMapping) o).isMainControl()) ? "Oui" : "Non";
			break;
		case 3:
			text = ((KMapping) o).getClassControl();
			break;
		case 4:
			text = ((KMapping) o).getMethod();
			break;
		case 5:
			text = ((KMapping) o).getQueryString();
			break;
		case 6:
			text = ((KMapping) o).getDefaultTargetId();
			break;
		default:
			break;
		}
		return text;
	}

	public Image getColumnImage(Object o, int index) {
		Image img = null;
		switch (index) {
		case 0:
			img = Images.getImage(Images.MAPPING);
			break;
		case 2:
			if (((KMapping) o).isMainControl())
				img = Images.getImage(Images.CHECKED_YES);
			else
				img = Images.getImage(Images.CHECKED_NO);
			break;
		default:
			break;
		}
		return img;
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				KMapping mapping = (KMapping) element;
				Object result = null;
				switch (property) {
				case "requestURL":
					result = mapping.getRequestURL();
					break;

				case "mappingFor":
					result = ((KVirtualMapping) mapping).getMappingFor();
					break;
				case "responseURL":
					result = mapping.getResponseURL();
					break;

				case "mainControl":
					result = Boolean.valueOf(mapping.isMainControl());
					break;
				case "classControl":
					result = mapping.getClassControl();
					break;
				case "method":
					result = mapping.getMethod();
					break;
				case "queryString":
					result = mapping.getQueryString();
					break;
				case "defaultTargetId":
					result = mapping.getDefaultTargetId();
					break;
				}
				return result;
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				KMapping mapping = (KMapping) tableItem.getData();
				boolean updated = true;
				switch (property) {
				case "requestURL":
					String oldValue = mapping.getRequestURL();
					if (!value.equals(oldValue)) {
						if (moxFile.getAjaxRequestsByURL(oldValue).size() > 0) {
							MessageDialog dg = new MessageDialog(Display.getCurrent().getActiveShell(),
									"Changement d'URL", null,
									"Souhaitez vous renommer l'inclusion ajax existante de type KAjaxRequest [" + oldValue + "] ?",
									MessageDialog.QUESTION_WITH_CANCEL,
									new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
							switch (dg.open()) {
							case 0:
								mapping.setRequestURL(value + "");
								moxFile.updateRequest(oldValue, value + "");
								break;
							case 1:
								mapping.setRequestURL(value + "");
								break;
							case 2:
								updated = false;
								return;
							}
						} else
							mapping.setRequestURL(value + "");
					} else
						updated = false;
					break;
				case "responseURL":
					mapping.setResponseURL(value + "");
					break;
				case "mappingFor":
					((KVirtualMapping) mapping).setMappingFor(value + "");
					break;
				case "mainControl":
					mapping.setMainControl(KString.isBooleanTrue(value + ""));
					break;
				case "classControl":
					mapping.setClassControl(value + "");
					break;
				case "method":
					mapping.setMethod(value + "");
					break;
				case "queryString":
					mapping.setQueryString(value + "");
					break;
				case "defaultTargetId":
					mapping.setDefaultTargetId(value + "");
					break;
				default:
					updated = false;
					break;
				}
				if (updated) {
					moxFile.setDirty(true);
				}
				viewer.refresh(mapping);
			}
		});

		viewer.setCellEditors(tableColumnMappingList.getCellEditors(parent));

		viewer.setColumnProperties(tableColumnMappingList.getCaptions());
	}

	private void addColumn(Table table, TableColumnLayout layout, String caption, int width) {
		TableColumn col = new TableColumn(table, SWT.LEFT);
		if (width == 0) {
			layout.setColumnData(col, new ColumnPixelData(0, false));
		} else
			layout.setColumnData(col, new ColumnWeightData(width));

		col.setText(caption);
	}

	public void initColumns() {
		List<String> columnsVisibles = MapParameters.mappingColumns;
		TableColumnLayout layout = new TableColumnLayout();
		int i = 0;
		for (TableColumnSectionMapping tblColumn : tableColumnMappingList) {
			int width = 0;
			if (columnsVisibles.contains(tblColumn.getCaption()) || i < 2)
				width = tblColumn.getWidth();
			addColumn(tableViewer.getTable(), layout, tblColumn.getCaption(), width);
			i++;
		}
	}

	public void refreshColumns(Object o) {
		final Table table = tableViewer.getTable();
		table.setRedraw(false);
		List<String> columnsVisibles = MapParameters.mappingColumns;
		if (o != null) {
			columnsVisibles = Arrays.asList((o + "").split(","));
		}

		TableColumnLayout layout = new TableColumnLayout();
		TableColumn[] tableColumns = table.getColumns();
		int i = 0;
		for (TableColumnSectionMapping tblColumn : tableColumnMappingList) {
			if (columnsVisibles.contains(tblColumn.getCaption()) || i < 2)
				layout.setColumnData(tableColumns[i], new ColumnWeightData(tblColumn.getWidth()));
			else
				layout.setColumnData(tableColumns[i], new ColumnPixelData(0, false));
			i++;
		}

		table.setRedraw(true);
		table.getParent().layout(true);
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (!selection.isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object o = structuredSelection.getFirstElement();
			if (o instanceof MoxFilePart) {
				moxFile = (MoxFile) ((MoxFilePart) o).getModel();
				tableViewer.setInput(moxFile.getMappings().getItems());
				tableViewer.addFilter(getViewerFilter());
			}
		}
	}

	public abstract ViewerFilter getViewerFilter();

	public abstract String getBtnAddCaption();

	@Override
	public void dragFinished(DragSourceEvent event) {
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		KMapping firstElement = (KMapping) selection.getFirstElement();

		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = firstElement.getRequestURL();
		}

	}

	@Override
	public void dragStart(DragSourceEvent event) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getProperty().equals("mappingColumns"))
			refreshColumns(evt.getNewValue());
	}

	@Override
	public void dispose() {
		MapParameters.getMyStore().removePropertyChangeListener(this);
	}

}
