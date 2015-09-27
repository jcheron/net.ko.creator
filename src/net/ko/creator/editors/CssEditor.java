package net.ko.creator.editors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import net.ko.bean.ContentOutlineBean;
import net.ko.bean.ContentOutlineBeanElementVar;
import net.ko.bean.CssVar;
import net.ko.creator.SwtUtils;
import net.ko.creator.WorkbenchUtils;
import net.ko.creator.editors.images.Images;
import net.ko.java.inheritance.SortedProperties;
import net.ko.utils.KColor;
import net.ko.utils.KProperties;
import net.ko.utils.KString;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wb.swt.SWTResourceManager;

public class CssEditor extends EditorComposite {

	private FormToolkit formToolkit = null; // @jve:decl-index=0:visual-constraint=""
	private Form form;
	private Composite compoMain = null;
	private Table table;
	private String cssFile = "";
	private TableViewer tableViewer;
	private Hyperlink hprlnkCssVar;
	private Label lblCssFileImage;
	private Button btnSupprimer;
	private Hyperlink hprlnkCouleur;
	private Hyperlink hprlnkFont;
	private Label lblCssColor;

	public CssEditor(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		this.setBounds(new Rectangle(0, 0, 900, 600));
		createForm();
		createCompoMain();
	}

	/**
	 * This method initializes formToolkit
	 * 
	 * @return org.eclipse.ui.forms.widgets.FormToolkit
	 */
	private FormToolkit getFormToolkit() {
		if (formToolkit == null) {
			formToolkit = new FormToolkit(Display.getCurrent());
		}
		return formToolkit;
	}

	private void createForm() {
		form = getFormToolkit().createForm(this);
		form.getBody().setLayout(new FillLayout());
		form.setImage(Images.getImage(Images.CSS));
		form.setText("Variables Css");
		getFormToolkit().decorateFormHeading(form);
		form.setFont(new Font(Display.getDefault(), "Segoe UI", 10, SWT.BOLD));
		form.addMessageHyperlinkListener(new HyperlinkAdapter());
	}

	private void createCompoMain() {
		compoMain = getFormToolkit().createComposite(form.getBody(), SWT.NONE);
		compoMain.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compoMain.setLayout(new FormLayout());

		Section sctnFichiers = getFormToolkit().createSection(compoMain, Section.CLIENT_INDENT | Section.TWISTIE | Section.TITLE_BAR);
		FormData fd_sctnFichiers = new FormData();
		fd_sctnFichiers.top = new FormAttachment(0, 10);
		fd_sctnFichiers.left = new FormAttachment(0, 10);
		fd_sctnFichiers.bottom = new FormAttachment(0, 178);
		fd_sctnFichiers.right = new FormAttachment(0, 250);
		sctnFichiers.setLayoutData(fd_sctnFichiers);
		getFormToolkit().paintBordersFor(sctnFichiers);
		sctnFichiers.setText("Fichiers");
		sctnFichiers.setExpanded(true);

		Composite composite = new Composite(sctnFichiers, SWT.NONE);
		getFormToolkit().adapt(composite);
		getFormToolkit().paintBordersFor(composite);
		sctnFichiers.setClient(composite);
		composite.setLayout(new GridLayout(3, false));

		lblCssFileImage = new Label(composite, SWT.NONE);
		lblCssFileImage.setImage(SWTResourceManager.getImage(CssEditor.class, "/net/ko/creator/editors/images/css.png"));
		getFormToolkit().adapt(lblCssFileImage, true, true);

		hprlnkCssVar = getFormToolkit().createHyperlink(composite, "cssVar.properties", SWT.NONE);
		getFormToolkit().paintBordersFor(hprlnkCssVar);

		Button btnNewButton = getFormToolkit().createButton(composite, "...", SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String rep = showFileDialog("Fichier de variables css", "*.properties", cssFile);
				if (rep != null) {
					setCssFile(rep);
					multiPageEditor.updateCssEditor();
					// setValidationFile(getValidationFile());
				}
			}
		});
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 60;
		btnNewButton.setLayoutData(gd_btnNewButton);

		Section sctnNewSection = getFormToolkit().createSection(compoMain, Section.TWISTIE | Section.TITLE_BAR);
		FormData fd_sctnNewSection = new FormData();
		fd_sctnNewSection.top = new FormAttachment(0, 10);
		fd_sctnNewSection.bottom = new FormAttachment(100, -20);
		fd_sctnNewSection.left = new FormAttachment(sctnFichiers, 6);
		fd_sctnNewSection.right = new FormAttachment(100, -20);
		sctnNewSection.setLayoutData(fd_sctnNewSection);
		getFormToolkit().paintBordersFor(sctnNewSection);
		sctnNewSection.setText("Variables css");
		sctnNewSection.setExpanded(true);

		Composite composite_1 = new Composite(sctnNewSection, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		getFormToolkit().adapt(composite_1);
		getFormToolkit().paintBordersFor(composite_1);
		sctnNewSection.setClient(composite_1);
		TableColumnLayout layout = new TableColumnLayout();

		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateTableSelection();
			}
		});
		table.setHeaderVisible(true);
		getFormToolkit().paintBordersFor(table);

		TableViewerColumn tableViewerColumnId = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tableColumnId = tableViewerColumnId.getColumn();
		tableColumnId.setWidth(115);
		tableColumnId.setText("Id");
		tableViewerColumnId.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof CssVar) {
					CssVar cssV = ((CssVar) element);
					if (!cssV.getId().equals(value)) {
						replaceValuesWhenKeyUpdated(cssV, value + "");
						cssV.setId(value + "");
						setDirty(true);
						tableViewer.refresh(element);
						updateOutlinePage();
					}
				}
			}

			@Override
			protected Object getValue(Object element) {
				Object result = null;
				if (element instanceof CssVar)
					result = ((CssVar) element).getId();
				return result;
			}

			@Override
			protected CellEditor getCellEditor(Object arg0) {
				return new TextCellEditor(table, SWT.NONE);
			}

			@Override
			protected boolean canEdit(Object element) {
				boolean result = false;
				if (element instanceof CssVar)
					result = true;
				return result;
			}
		});

		TableViewerColumn tableViewerColumnValeur = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tableColumnValeur = tableViewerColumnValeur.getColumn();
		tableColumnValeur.setWidth(464);
		tableColumnValeur.setText("Valeur");
		tableViewerColumnValeur.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof CssVar) {
					CssVar cssV = (CssVar) element;
					if (!(cssV.getValue().equals(value))) {
						cssV.setValue(value + "");
						setDirty(true);
						tableViewer.refresh(element);
						updateColors(cssV);
						colorChange();
						updateOutlinePage();
					}
				}
			}

			@Override
			protected Object getValue(Object element) {
				Object result = null;
				if (element instanceof CssVar)
					result = ((CssVar) element).getValue();
				return result;
			}

			@Override
			protected CellEditor getCellEditor(Object arg0) {
				return new TextCellEditor(table, SWT.NONE);
			}

			@Override
			protected boolean canEdit(Object element) {
				boolean result = false;
				if (element instanceof CssVar)
					result = true;
				return result;
			}
		});
		layout.setColumnData(tableColumnId, new ColumnWeightData(20));
		layout.setColumnData(tableColumnValeur, new ColumnWeightData(80));
		tableViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener arg0) {
			}

			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void addListener(ILabelProviderListener arg0) {
			}

			@Override
			public String getColumnText(Object o, int index) {
				String text = "";
				switch (index) {
				case 0:
					text = ((CssVar) o).getId();
					break;
				case 1:
					text = ((CssVar) o).getValue();
					break;
				default:
					break;
				}
				return text;
			}

			@Override
			public Image getColumnImage(Object element, int index) {
				// Image img = Images.getImage(Images.BULLET);
				Image img = null;
				if (index == 0) {
					Color col = getColor((CssVar) element);
					if (col != null) {
						img = new Image(getDisplay(), 8, 8);
						GC gc = new GC(img);
						gc.drawRectangle(0, 0, 8, 8);
						gc.setBackground(col);
						gc.fillRectangle(1, 1, 9, 9);
						gc.dispose();
					}
				}
				return img;
			}
		});
		tableViewer.setContentProvider(new ArrayContentProvider());
		composite_1.setLayout(layout);

		Section sctnEdition = getFormToolkit().createSection(compoMain, Section.CLIENT_INDENT | Section.TWISTIE | Section.TITLE_BAR);
		FormData fd_sctnEdition = new FormData();
		fd_sctnEdition.bottom = new FormAttachment(sctnFichiers, 203, SWT.BOTTOM);
		fd_sctnEdition.top = new FormAttachment(sctnFichiers, 6);
		fd_sctnEdition.left = new FormAttachment(sctnFichiers, 0, SWT.LEFT);
		fd_sctnEdition.right = new FormAttachment(sctnNewSection, -6);
		sctnEdition.setLayoutData(fd_sctnEdition);
		getFormToolkit().paintBordersFor(sctnEdition);
		sctnEdition.setText("Edition");
		sctnEdition.setExpanded(true);

		Composite composite_2 = new Composite(sctnEdition, SWT.NONE);
		getFormToolkit().adapt(composite_2);
		getFormToolkit().paintBordersFor(composite_2);
		sctnEdition.setClient(composite_2);
		composite_2.setLayout(new FormLayout());

		Button btnAjouterUneVariable = new Button(composite_2, SWT.FLAT);
		btnAjouterUneVariable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addCssVar();
			}
		});
		btnAjouterUneVariable.setText("Ajouter une variable");
		FormData fd_hprlnkAjouterUneVariable = new FormData();
		fd_hprlnkAjouterUneVariable.right = new FormAttachment(100, -10);
		fd_hprlnkAjouterUneVariable.left = new FormAttachment(0, 30);
		fd_hprlnkAjouterUneVariable.top = new FormAttachment(0, 10);
		btnAjouterUneVariable.setLayoutData(fd_hprlnkAjouterUneVariable);

		btnSupprimer = new Button(composite_2, SWT.FLAT);
		btnSupprimer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteVar();
			}
		});
		btnSupprimer.setText("Supprimer");
		FormData fd_hprlnkSupprimer = new FormData();
		fd_hprlnkSupprimer.bottom = new FormAttachment(btnAjouterUneVariable, 31, SWT.BOTTOM);
		fd_hprlnkSupprimer.top = new FormAttachment(btnAjouterUneVariable, 6);
		fd_hprlnkSupprimer.right = new FormAttachment(btnAjouterUneVariable, 0, SWT.RIGHT);
		fd_hprlnkSupprimer.left = new FormAttachment(btnAjouterUneVariable, 0, SWT.LEFT);
		btnSupprimer.setLayoutData(fd_hprlnkSupprimer);
		btnSupprimer.setEnabled(false);

		lblCssColor = getFormToolkit().createLabel(composite_2, "  ", SWT.BORDER | SWT.SHADOW_IN);
		FormData fd_lblCssColor = new FormData();
		fd_lblCssColor.left = new FormAttachment(0);
		lblCssColor.setLayoutData(fd_lblCssColor);
		lblCssColor.setSize(new Point(16, 16));

		hprlnkCouleur = getFormToolkit().createHyperlink(composite_2, "Couleur...", SWT.NONE);
		hprlnkCouleur.addHyperlinkListener(new IHyperlinkListener() {
			public void linkActivated(HyperlinkEvent e) {
				ColorDialog cd = new ColorDialog(getDisplay().getActiveShell(), SWT.NONE);
				Color col = getActiveColor();
				if (col == null)
					col = getBackground();
				cd.setRGB(col.getRGB());
				cd.setText("Choix de couleur");
				if (cd.open() != null) {
					if (table.getSelectionIndex() != -1) {
						Object o = table.getItem(table.getSelectionIndex()).getData();
						if (o instanceof CssVar) {
							CssVar cssV = (CssVar) o;
							cssV.setValue(SwtUtils.RGBToHex(cd.getRGB()));
							tableViewer.refresh(cssV);
							colorChange();
							setDirty(true);
							updateColors(cssV);
							updateOutlinePage();
						}
					}
				}
			}

			public void linkEntered(HyperlinkEvent arg0) {
			}

			public void linkExited(HyperlinkEvent arg0) {
			}
		});
		fd_lblCssColor.bottom = new FormAttachment(hprlnkCouleur, 4, SWT.BOTTOM);
		fd_lblCssColor.top = new FormAttachment(hprlnkCouleur, 0, SWT.TOP);
		fd_lblCssColor.right = new FormAttachment(hprlnkCouleur, -1);
		FormData fd_hprlnkCouleur = new FormData();
		fd_hprlnkCouleur.left = new FormAttachment(0, 30);
		fd_hprlnkCouleur.top = new FormAttachment(0, 74);
		hprlnkCouleur.setLayoutData(fd_hprlnkCouleur);
		hprlnkCouleur.setEnabled(false);
		getFormToolkit().paintBordersFor(hprlnkCouleur);

		hprlnkFont = getFormToolkit().createHyperlink(composite_2, "Font...", SWT.NONE);
		FormData fd_hprlnkFont = new FormData();
		fd_hprlnkFont.top = new FormAttachment(lblCssColor, 6);
		fd_hprlnkFont.left = new FormAttachment(btnAjouterUneVariable, 0, SWT.LEFT);
		hprlnkFont.setLayoutData(fd_hprlnkFont);
		hprlnkFont.setEnabled(false);
		getFormToolkit().paintBordersFor(hprlnkFont);
	}

	protected void updateTableSelection() {
		boolean selected = (table.getSelectionIndex() != -1);
		btnSupprimer.setEnabled(selected);
		hprlnkCouleur.setEnabled(selected);
		hprlnkFont.setEnabled(selected);
		colorChange();
	}

	private Color getActiveColor() {
		Color col = new Color(getDisplay(), 255, 255, 255);
		if (table.getSelectionIndex() != -1) {
			col = getColor((CssVar) table.getItem(table.getSelectionIndex()).getData());
		}
		return col;
	}

	private Color getColor(CssVar cssV) {
		Color col = new Color(getDisplay(), 255, 255, 255);
		try {
			col = SwtUtils.hex2RGB(getDisplay(), getRealValue(cssV));
		} catch (Exception e) {
			col = null;
		}
		return col;
	}

	public void showColorDialog() {
		ColorDialog cd = new ColorDialog(getDisplay().getActiveShell(), SWT.NONE);
		cd.setRGB(getActiveColor().getRGB());
		cd.setText("Choix de couleur");
		if (cd.open() != null) {
			if (table.getSelectionIndex() != -1) {
				Object o = table.getItem(table.getSelectionIndex()).getData();
				if (o instanceof CssVar) {
					CssVar cssV = (CssVar) o;
					cssV.setValue(SwtUtils.RGBToHex(cd.getRGB()));
					tableViewer.refresh(cssV);
					colorChange();
					updateColors(cssV);
				}
			}
		}
	}

	public void deleteVar() {
		int index = tableViewer.getTable().getSelectionIndex();
		CssVar cssV = (CssVar) table.getItem(index).getData();
		table.remove(index);
		table.select(index - 1);
		updateTableSelection();
		setDirty(true);
		cobElements.remove(cssV.getCob());
		updateOutlinePage();
	}

	private void colorChange() {
		Color col = getActiveColor();
		if (col != null)
			lblCssColor.setBackground(getActiveColor());
		else
			lblCssColor.setBackground(this.getBackground());
	}

	protected void updateFile(Hyperlink lk, Label img, String fileName) {
		boolean noFile = false;
		IFile f = null;
		try {
			f = WorkbenchUtils.getActiveProject().getFile(fileName);
			noFile = !f.exists();
		} catch (Exception e) {
			noFile = true;
		}
		if (!noFile && f.exists()) {
			lk.setText(f.getName());
			lk.setToolTipText("Ouvrir le fichier " + fileName);
			img.setImage(Images.getImage(Images.SUCCESS));
			lk.setEnabled(true);
		} else {
			lk.setText("Fichier absent");
			lk.setToolTipText("fichier absent");
			img.setImage(Images.getImage(Images.EXCLAMATION));
			lk.setEnabled(false);
		}
		loadCssVars();
	}

	protected void loadCssVars() {
		table.clearAll();
		tableViewer.refresh();
		IProject project = WorkbenchUtils.getActiveProject();
		String baseFolder = getBaseFolder();
		if (project != null) {
			try {
				if (project.getFile(baseFolder + cssFile).exists()) {
					for (CssVar cssV : openCssVars()) {
						tableViewer.add(cssV);
						ContentOutlineBean c = new ContentOutlineBeanElementVar(Images.BULLET, tableViewer, cssV);
						c.setParent(cob);
						cobElements.add(c);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	protected Properties loadPropertyFile(String relativePath) {
		KProperties kp = new KProperties(true);
		try {
			kp.loadFromFile(WorkbenchUtils.getActiveProject().getLocation().toOSString() + "/" + relativePath);
		} catch (IOException e) {
			return new Properties();
		}
		return kp.getProperties();
	}

	protected ArrayList<CssVar> openCssVars() {
		ArrayList<CssVar> cssVars = new ArrayList<CssVar>();
		Properties cssVarsP;
		String baseFolder = getBaseFolder();
		cssVarsP = loadPropertyFile(baseFolder + cssFile);
		for (Map.Entry<Object, Object> entry : cssVarsP.entrySet()) {
			cssVars.add(new CssVar((String) entry.getKey(), (String) entry.getValue()));
		}
		Collections.sort(cssVars);
		return cssVars;
	}

	protected String showFileDialog(String text, String filter, String fileName) {
		String result = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IProject project = WorkbenchUtils.getActiveProject();
		MyFileElementTreeSelectionDialog mf = new MyFileElementTreeSelectionDialog(window.getShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
		mf.setParams(project, text, filter);
		if (mf.open() == ResourceSelectionDialog.OK) {
			Object[] selected = mf.getResult();
			IFile file = (IFile) selected[0];
			result = file.getProjectRelativePath().toString();
		}
		return result;
	}

	public String getBaseFolder() {
		String result = "";
		if (WorkbenchUtils.isDynamicWebProject())
			result = "WebContent/";
		return result;
	}

	public void saveCssVars() {
		Properties pCssVars = new SortedProperties();
		for (TableItem item : tableViewer.getTable().getItems()) {
			CssVar cssV = (CssVar) item.getData();
			pCssVars.put(cssV.getId(), cssV.getValue());
		}
		KProperties kpCssVars = new KProperties(pCssVars);
		try {
			IProject project = WorkbenchUtils.getActiveProject();
			String baseFolder = getBaseFolder();
			IFile f = project.getFile(baseFolder + cssFile);
			kpCssVars.saveAs(f.getLocation().toOSString());
			setDirty(false);
			f.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void addCssVar() {
		CssVar cssV = new CssVar("newId", "");
		tableViewer.add(cssV);
		table.setSelection(table.getItemCount() - 1);
		updateTableSelection();
		setDirty(true);
		ContentOutlineBean c = new ContentOutlineBeanElementVar(Images.BULLET, tableViewer, cssV);
		c.setParent(cob);
		cobElements.add(c);
		updateOutlinePage();
	}

	public void setCssFile(String fileName) {
		if (fileName != null && WorkbenchUtils.getActiveProject() != null) {
			String baseFolder = getBaseFolder();
			String newFileName = fileName.replace(baseFolder, "");
			if (!cssFile.equals(newFileName)) {
				cssFile = newFileName;
				updateFile(hprlnkCssVar, lblCssFileImage, baseFolder + cssFile);
				setDirty(false);
			}
		}
	}

	public String getCssFile() {
		return cssFile;
	}

	@Override
	public void updateCobElements() {
		// TODO Auto-generated method stub

	}

	public String getRealValue(CssVar cssVar) {
		String result = cssVar.getValue();
		String value = result;
		String valueClean = getParentKey(value);
		CssVar tmpCssVar = getCssVar(valueClean);
		if (tmpCssVar != null) {
			String originalValue = tmpCssVar.getValue();
			if (originalValue.startsWith("#")) {
				int darkerValue = KColor.matchCount(value, "\\+");
				int brighterValue = KColor.matchCount(value, "\\-");
				if (darkerValue > 0)
					result = KColor.colorDarker(originalValue, darkerValue);
				else if (brighterValue > 0)
					result = KColor.colorBrighter(originalValue, brighterValue);
				else
					result = originalValue;
			}
		}
		return result;
	}

	private String getParentKey(String key) {
		String result = KString.getBefore(key, "+");
		result = KString.getBefore(result, "-");
		return result;
	}

	private CssVar getCssVar(String key) {
		CssVar result = null;
		for (TableItem item : tableViewer.getTable().getItems()) {
			CssVar cssV = (CssVar) item.getData();
			if (cssV.getId().equals(key)) {
				result = cssV;
				break;
			}
		}
		return result;
	}

	private void replaceValuesWhenKeyUpdated(CssVar cssVarUpdated, String newKey) {
		String oldKey = cssVarUpdated.getId();
		for (TableItem item : tableViewer.getTable().getItems()) {
			CssVar cssV = (CssVar) item.getData();
			if (cssV != null && !cssV.equals(cssVarUpdated)) {
				if (replaceValueWhenKeyUpdated(oldKey, newKey, cssV))
					tableViewer.refresh(cssV);
			}
		}
	}

	private void updateColors(CssVar cssVarUpdated) {
		String key = cssVarUpdated.getId();
		for (TableItem item : tableViewer.getTable().getItems()) {
			CssVar cssV = (CssVar) item.getData();
			if (cssV != null && !cssV.equals(cssVarUpdated)) {
				if (getParentKey(cssV.getValue()).equals(key))
					tableViewer.refresh(cssV);
			}
		}
	}

	private boolean replaceValueWhenKeyUpdated(String oldKey, String newKey, CssVar cssVar) {
		boolean result = false;
		if (getParentKey(cssVar.getValue()).equals(oldKey)) {
			result = true;
			String newValue = cssVar.getValue().replace(oldKey, newKey);
			cssVar.setValue(newValue);
		}
		return result;
	}
}
