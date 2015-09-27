package net.ko.creator.preferencepage;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.Activator;
import net.ko.creator.controls.linecmb.LineStyleCombo;
import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.MapParameters;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.pde.internal.ui.SWTFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.eclipse.wb.swt.FieldLayoutPreferencePage;

public class MapsPreferencePage extends FieldLayoutPreferencePage implements
		IWorkbenchPreferencePage {
	private ColorFieldEditor feColor;
	private FontFieldEditor feFont;
	private TableViewer mappingColumnsTableViewer;
	private LineStyleCombo connexionStyleCmb;
	private Spinner spSize;

	/**
	 * Create the preference page.
	 */
	public MapsPreferencePage() {
	}

	/**
	 * Create contents of the preference page.
	 * 
	 * @param parent
	 */
	@Override
	public Control createPageContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		PreferenceLinkArea colorsAndFontsArea = new PreferenceLinkArea(container, SWT.NONE,
				"org.eclipse.ui.preferencePages.ColorsAndFonts", "<a>Elements colors and fonts</a>",
				(IWorkbenchPreferenceContainer) getContainer(), null);
		GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		colorsAndFontsArea.getControl().setLayoutData(data);
		createConnexionParams("Connexions URL :", container);
		mappingColumnsTableViewer = createTable(container);
		initializeValues();
		return container;
	}

	private void createConnexionParams(String groupName, Composite container) {
		Group group = SWTFactory.createGroup(container, groupName, 1, 1, GridData.FILL_HORIZONTAL);
		Composite spacer = SWTFactory.createComposite(group, 1, 1, GridData.FILL_HORIZONTAL);
		Label textLabel = new Label(spacer, SWT.NONE);
		GridData gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
		gd.widthHint = 80;
		textLabel.setLayoutData(gd);
		textLabel.setText("Type de trait :"); //$NON-NLS-1$
		connexionStyleCmb = new LineStyleCombo(spacer, SWT.READ_ONLY | SWT.BORDER);

		connexionStyleCmb.add("solid", null);
		connexionStyleCmb.add("dotted", null);
		connexionStyleCmb.add("dashed", null);
		connexionStyleCmb.add("dash dot", null);
		connexionStyleCmb.add("dash dot dot", null);
		connexionStyleCmb.setElementColor(MapParameters.connexionColor);
		connexionStyleCmb.setElementSize(MapParameters.connexionLineWidth);
		connexionStyleCmb.select(MapParameters.connexionLineStyle - 1);
		connexionStyleCmb.setLayoutData(gd);
		Label textLabelSize = new Label(spacer, SWT.NONE);
		textLabelSize.setText("Epaisseur :");
		spSize = new Spinner(spacer, SWT.BORDER);
		spSize.setLayoutData(gd);
		spSize.setValues(MapParameters.connexionLineWidth, 1, 5, 0, 1, 1);
		spSize.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				connexionStyleCmb.setElementSize(Integer.valueOf(spSize.getText()));
			}
		});
		FieldEditor edit = new BooleanFieldEditor(MapParameters.CONNEXION_VISIBLE, "Afficher les connexions", SWT.NONE, spacer);
		edit.fillIntoGrid(spacer, 2);
		addField(edit);
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return Images.getImage(Images.KO_CONFIG);
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "KObject maps préférences";
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	private void initializeValues() {
		mappingColumnsTableViewer.setInput(MapParameters.mappingColumnsValues);
		for (TableItem item : mappingColumnsTableViewer.getTable().getItems()) {
			if (MapParameters.mappingColumns.contains(item.getText()))
				item.setChecked(true);
		}
	}

	@Override
	public boolean performOk() {
		boolean result = super.performOk();
		List<String> mappingColumns = new ArrayList<>();
		for (TableItem item : mappingColumnsTableViewer.getTable().getItems()) {
			if (item.getChecked())
				mappingColumns.add(item.getText());
		}
		MapParameters.saveMappingColumns(mappingColumns);
		MapParameters.saveConnexionInfo(connexionStyleCmb.getElementLineStyle(), connexionStyleCmb.getElementSize());
		MapParameters.load();
		return result;

	}

	public TableViewer createTable(Composite container) {
		Group group = SWTFactory.createGroup(container, "Virtual/Mappings properties editor columns :", 1, 1, GridData.FILL_HORIZONTAL);
		Composite spacer = SWTFactory.createComposite(group, 1, 1, GridData.FILL_HORIZONTAL);
		GridData gridDataTable = new GridData();
		gridDataTable.widthHint = 300;
		gridDataTable.horizontalIndent = 1;
		gridDataTable.heightHint = 100;
		Table table = new Table(spacer, SWT.MULTI | SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		table.setHeaderVisible(false);
		table.setLayoutData(gridDataTable);
		table.setLinesVisible(false);
		TableViewer tableViewer = new TableViewer(table);
		String[] COLUMNS = new String[] { "Table" };
		for (String element : COLUMNS) {
			TableColumn col = new TableColumn(tableViewer.getTable(), SWT.CENTER);
			col.setText(element);
		}
		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100));
		table.setLayout(tlayout);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new LabelProvider());
		return tableViewer;
	}
}
