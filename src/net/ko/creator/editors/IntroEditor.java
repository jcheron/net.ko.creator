package net.ko.creator.editors;

import java.util.ArrayList;

import net.ko.bean.ConfigBean;
import net.ko.bean.ConfigContentProvider;
import net.ko.bean.ConfigRubrique;
import net.ko.bean.ConfigVariable;
import net.ko.creator.Activator;
import net.ko.creator.editors.images.Images;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.wb.swt.SWTResourceManager;

public class IntroEditor extends EditorComposite {
	private static class ViewerLabelProvider implements ITableLabelProvider {

		@Override
		public void addListener(ILabelProviderListener arg0) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			Image result = null;
			if (element instanceof ConfigVariable && columnIndex == 0) {
				ConfigVariable cv = (ConfigVariable) element;
				if (cv.getValue() != null && !"".equals(cv.getValue()))
					result = Images.getImage(Images.BULLET);
				else
					result = Images.getImage(Images.BULLET_GRAY);
			}
			return result;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			String result = "";
			switch (columnIndex) {
			case 0:
				if (element instanceof ConfigBean)
					result = ((ConfigBean) element).getName();
				break;
			case 1:
				if (element instanceof ConfigVariable)
					result = ((ConfigVariable) element).getValue();
			default:
				break;
			}
			return result;
		}

	}

	private FormToolkit formToolkit = null; // @jve:decl-index=0:visual-constraint=""
	private Form form_1;
	private Composite compoMain = null;
	private TreeViewer treeViewer;

	public IntroEditor(Composite parent, int style) {
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
		form_1 = getFormToolkit().createForm(this);
		form_1.getBody().setLayout(new FillLayout());
		form_1.setImage(Images.getImage(Images.PLUGIN));
		form_1.setText("Kobject framework");
		getFormToolkit().decorateFormHeading(form_1);
		form_1.setFont(new Font(Display.getDefault(), "Segoe UI", 10, SWT.BOLD));
		form_1.addMessageHyperlinkListener(new HyperlinkAdapter());
	}

	/**
	 * This method initializes compoMain
	 * 
	 */
	private void createCompoMain() {
		compoMain = getFormToolkit().createComposite(form_1.getBody(), SWT.NONE);
		compoMain.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compoMain.setLayout(new FormLayout());

		Label lblNewLabel = new Label(compoMain, SWT.NONE);
		lblNewLabel.setImage(Images.getImage(Images.KO_LOGO));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 10);
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		getFormToolkit().adapt(lblNewLabel, true, true);

		Section sctnModle = getFormToolkit().createSection(compoMain, Section.CLIENT_INDENT | Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		sctnModle.setDescription("Objets métier");
		FormData fd_sctnModle = new FormData();
		fd_sctnModle.left = new FormAttachment(0, 10);
		sctnModle.setLayoutData(fd_sctnModle);
		getFormToolkit().paintBordersFor(sctnModle);
		sctnModle.setText("Modèle");
		sctnModle.setExpanded(true);

		Composite composite = new Composite(sctnModle, SWT.NONE);
		getFormToolkit().adapt(composite);
		getFormToolkit().paintBordersFor(composite);
		sctnModle.setClient(composite);
		composite.setLayout(new GridLayout(2, false));

		ImageHyperlink mghprlnkOrm = getFormToolkit().createImageHyperlink(composite, SWT.NONE);
		mghprlnkOrm.addHyperlinkListener(new IHyperlinkListener() {
			public void linkActivated(HyperlinkEvent e) {
				multiPageEditor.gotoPageMainEditor();
			}

			public void linkEntered(HyperlinkEvent arg0) {
			}

			public void linkExited(HyperlinkEvent arg0) {
			}
		});
		mghprlnkOrm.setImage(Images.getImage(Images.ORM));
		getFormToolkit().paintBordersFor(mghprlnkOrm);
		mghprlnkOrm.setText("ORM");

		getFormToolkit().createLabel(composite, "Object Relational Mapper", SWT.NONE);

		ImageHyperlink mghprlnkValidation = getFormToolkit().createImageHyperlink(composite, SWT.NONE);
		mghprlnkValidation.setImage(Images.getImage(Images.VALIDATION));
		getFormToolkit().paintBordersFor(mghprlnkValidation);
		mghprlnkValidation.setText("Validation");
		mghprlnkValidation.addHyperlinkListener(new IHyperlinkListener() {
			public void linkActivated(HyperlinkEvent e) {
				multiPageEditor.gotoPageControllerEditor();
			}

			public void linkEntered(HyperlinkEvent arg0) {
			}

			public void linkExited(HyperlinkEvent arg0) {
			}
		});

		getFormToolkit().createLabel(composite, "Validation et contrôle des données (kox.xml)", SWT.NONE);

		Section sctnContrleur = getFormToolkit().createSection(compoMain, Section.CLIENT_INDENT | Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		fd_sctnModle.bottom = new FormAttachment(sctnContrleur, -8);
		sctnContrleur.setDescription("Logique applicative");
		FormData fd_sctnContrleur = new FormData();
		fd_sctnContrleur.right = new FormAttachment(sctnModle, 0, SWT.RIGHT);
		fd_sctnContrleur.bottom = new FormAttachment(0, 289);
		fd_sctnContrleur.top = new FormAttachment(0, 214);
		fd_sctnContrleur.left = new FormAttachment(lblNewLabel, 0, SWT.LEFT);
		sctnContrleur.setLayoutData(fd_sctnContrleur);
		getFormToolkit().paintBordersFor(sctnContrleur);
		sctnContrleur.setText("Contrôleur");
		sctnContrleur.setExpanded(true);

		Composite composite_1 = new Composite(sctnContrleur, SWT.NONE);
		getFormToolkit().adapt(composite_1);
		getFormToolkit().paintBordersFor(composite_1);
		sctnContrleur.setClient(composite_1);
		composite_1.setLayout(new GridLayout(2, false));

		ImageHyperlink mghprlnkContrleur = getFormToolkit().createImageHyperlink(composite_1, SWT.NONE);
		mghprlnkContrleur.setImage(Images.getImage(Images.CONTROLLER));
		getFormToolkit().paintBordersFor(mghprlnkContrleur);
		mghprlnkContrleur.setText("Contrôleur");
		mghprlnkContrleur.addHyperlinkListener(new IHyperlinkListener() {
			public void linkActivated(HyperlinkEvent e) {
				multiPageEditor.gotoPageMox();
			}

			public void linkEntered(HyperlinkEvent arg0) {
			}

			public void linkExited(HyperlinkEvent arg0) {
			}
		});

		getFormToolkit().createLabel(composite_1, "Mappings et filtres d'URL (mox.xml)", SWT.NONE);

		Section sctnVues = getFormToolkit().createSection(compoMain, Section.CLIENT_INDENT | Section.EXPANDED | Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		sctnVues.setDescription("Interface Homme Machine");
		FormData fd_sctnVues = new FormData();
		fd_sctnVues.right = new FormAttachment(sctnModle, 0, SWT.RIGHT);
		fd_sctnVues.bottom = new FormAttachment(100, -55);
		fd_sctnVues.top = new FormAttachment(sctnContrleur, 9);
		fd_sctnVues.left = new FormAttachment(0, 10);
		sctnVues.setLayoutData(fd_sctnVues);
		getFormToolkit().paintBordersFor(sctnVues);
		sctnVues.setText("Vues");

		Composite composite_2 = new Composite(sctnVues, SWT.NONE);
		getFormToolkit().adapt(composite_2);
		getFormToolkit().paintBordersFor(composite_2);
		sctnVues.setClient(composite_2);
		composite_2.setLayout(new GridLayout(2, false));

		ImageHyperlink mghprlnkCrerUnTemplate = getFormToolkit().createImageHyperlink(composite_2, SWT.NONE);
		mghprlnkCrerUnTemplate.addHyperlinkListener(new IHyperlinkListener() {
			public void linkActivated(HyperlinkEvent arg0) {
				String commandId = "net.ko.creator.commands.koTemplate";
				IHandlerService handlerService = (IHandlerService) (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
				try {
					handlerService.executeCommand(commandId, null);
				} catch (ExecutionException | NotDefinedException
						| NotEnabledException | NotHandledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			public void linkEntered(HyperlinkEvent arg0) {
			}

			public void linkExited(HyperlinkEvent arg0) {
			}
		});
		mghprlnkCrerUnTemplate.setImage(Images.getImage(Images.TPL));
		getFormToolkit().paintBordersFor(mghprlnkCrerUnTemplate);
		mghprlnkCrerUnTemplate.setText("Créer un template de vue");

		getFormToolkit().createLabel(composite_2, "(liste ou formulaire)", SWT.NONE);

		ImageHyperlink mghprlnkVariablesCss = getFormToolkit().createImageHyperlink(composite_2, SWT.NONE);
		mghprlnkVariablesCss.addHyperlinkListener(new IHyperlinkListener() {
			public void linkActivated(HyperlinkEvent e) {
				multiPageEditor.gotoCssEditor();
			}

			public void linkEntered(HyperlinkEvent arg0) {
			}

			public void linkExited(HyperlinkEvent arg0) {
			}
		});
		mghprlnkVariablesCss.setImage(Images.getImage(Images.CSS));
		getFormToolkit().paintBordersFor(mghprlnkVariablesCss);
		mghprlnkVariablesCss.setText("Variables CSS");
		new Label(composite_2, SWT.NONE);

		Section sctnVariables = getFormToolkit().createSection(compoMain, Section.TWISTIE | Section.TITLE_BAR);
		fd_sctnModle.right = new FormAttachment(sctnVariables, -6);
		FormData fd_sctnVariables = new FormData();
		fd_sctnVariables.left = new FormAttachment(0, 453);
		fd_sctnVariables.right = new FormAttachment(100, -20);
		fd_sctnVariables.top = new FormAttachment(lblNewLabel, 0, SWT.TOP);
		fd_sctnVariables.bottom = new FormAttachment(100, -20);
		sctnVariables.setLayoutData(fd_sctnVariables);
		getFormToolkit().paintBordersFor(sctnVariables);
		sctnVariables.setText("Variables");
		sctnVariables.setExpanded(true);

		Composite composite_3 = new Composite(sctnVariables, SWT.NONE);
		getFormToolkit().adapt(composite_3);
		getFormToolkit().paintBordersFor(composite_3);
		sctnVariables.setClient(composite_3);
		TreeColumnLayout tcl_composite_3 = new TreeColumnLayout();
		composite_3.setLayout(tcl_composite_3);

		treeViewer = new TreeViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		final Tree tree = treeViewer.getTree();
		tree.setTouchEnabled(true);
		tree.setHeaderVisible(true);
		getFormToolkit().paintBordersFor(tree);

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnVariable = treeViewerColumn_1.getColumn();
		tcl_composite_3.setColumnData(trclmnVariable, new ColumnPixelData(187, true, true));
		trclmnVariable.setText("Variable");

		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnValeur = treeViewerColumn_2.getColumn();
		tcl_composite_3.setColumnData(trclmnValeur, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		trclmnValeur.setText("Valeur");
		treeViewerColumn_2.setEditingSupport(new EditingSupport(treeViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof ConfigVariable) {
					((ConfigVariable) element).setValue(value + "");
					setDirty(true);
					treeViewer.refresh(element);
				}
			}

			@Override
			protected Object getValue(Object element) {
				Object result = null;
				if (element instanceof ConfigVariable)
					result = ((ConfigVariable) element).getValue();
				return result;
			}

			@Override
			protected CellEditor getCellEditor(Object arg0) {
				return new TextCellEditor(tree, SWT.NONE);
			}

			@Override
			protected boolean canEdit(Object element) {
				boolean result = false;
				if (element instanceof ConfigVariable)
					result = true;
				return result;
			}
		});

		Label lblVersion = new Label(compoMain, SWT.NONE);
		fd_sctnModle.top = new FormAttachment(lblVersion, 1);
		lblVersion.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblVersion.setFont(SWTResourceManager.getFont("Trebuchet MS", 7, SWT.NORMAL));
		FormData fd_lblVersion = new FormData();
		fd_lblVersion.right = new FormAttachment(sctnVariables, -65);
		fd_lblVersion.left = new FormAttachment(0, 10);
		fd_lblVersion.top = new FormAttachment(lblNewLabel, 6);
		lblVersion.setLayoutData(fd_lblVersion);
		getFormToolkit().adapt(lblVersion, true, true);
		lblVersion.setText("Version : " + Activator.VERSION);

		treeViewer.setLabelProvider(new ViewerLabelProvider());
		treeViewer.setContentProvider(new ConfigContentProvider());
	}

	public void setRubriques(ArrayList<ConfigRubrique> rubriques) {
		treeViewer.setInput(rubriques);
		treeViewer.expandAll();
	}

	@Override
	public void updateCobElements() {
		// TODO Auto-generated method stub

	}
}
