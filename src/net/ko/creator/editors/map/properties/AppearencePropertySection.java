package net.ko.creator.editors.map.properties;

import net.ko.creator.editors.map.figure.BaseFigure;
import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.part.AjaxRequestPart;
import net.ko.creator.editors.map.part.AppAbstractEditPart;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class AppearencePropertySection extends AbstractPropertySection {

	private Composite selfAppearenceComposite;
	private Button btnApparenceSpcifique;
	private AppearenceConfig appearenceConfig;
	private Canvas sampleCanvas;
	private Text txtExemple;
	private AppAbstractEditPart activePart;
	private Button btnGradient;
	private Button btnGradiantColor;
	private AppearenceConfig tmpAppearenceConfig;
	private Group grpBackground;

	public AppearencePropertySection() {
		super();
		appearenceConfig = new AppearenceConfig();
	}

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite compo = getWidgetFactory().createFlatFormComposite(parent);
		compo.setLayout(new GridLayout(1, false));
		compo.setBackgroundMode(SWT.INHERIT_DEFAULT);

		btnApparenceSpcifique = new Button(compo, SWT.CHECK);
		btnApparenceSpcifique.setText("Apparence spécifique");
		btnApparenceSpcifique.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selfAppearenceComposite.setEnabled(btnApparenceSpcifique.getSelection());

				setAppearenceEnabled(btnApparenceSpcifique.getSelection());
			}
		});

		selfAppearenceComposite = new Composite(compo, SWT.NONE);
		selfAppearenceComposite.setLayout(new GridLayout(1, false));
		selfAppearenceComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		selfAppearenceComposite.setEnabled(false);

		Group grpPolice = new Group(selfAppearenceComposite, SWT.NONE);
		grpPolice.setEnabled(true);
		grpPolice.setText("Police :");
		grpPolice.setLayout(new RowLayout(SWT.HORIZONTAL));
		grpPolice.setBackgroundMode(SWT.INHERIT_DEFAULT);

		txtExemple = new Text(grpPolice, SWT.BORDER);
		txtExemple.setEditable(false);
		txtExemple.setLayoutData(new RowData(SWT.DEFAULT, 23));
		txtExemple.setText("Exemple");

		Button btnPolice = new Button(grpPolice, SWT.NONE);
		btnPolice.setEnabled(true);
		btnPolice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FontDialog fontDialog = new FontDialog(Display.getCurrent().getActiveShell());
				fontDialog.setFontList(new FontData[] { appearenceConfig.getFontData() });
				fontDialog.setRGB(appearenceConfig.getFontColor().getRGB());
				fontDialog.setText("Police de caractères");
				FontData selectedFont = fontDialog.open();
				if (selectedFont != null) {
					RGB selectedColor = fontDialog.getRGB();
					txtExemple.setFont(new Font(Display.getCurrent(), selectedFont));
					Color fontColor = new Color(Display.getCurrent(), selectedColor);
					txtExemple.setForeground(fontColor);
					appearenceConfig.setFontData(selectedFont);
					appearenceConfig.setFontColor(fontColor);
					refreshVisuals();
				}
			}
		});
		btnPolice.setLayoutData(new RowData(105, 30));
		btnPolice.setText("Modifier...");

		grpBackground = new Group(selfAppearenceComposite, SWT.NONE);
		grpBackground.setText("Arrière plan :");
		grpBackground.setLayout(new RowLayout(SWT.HORIZONTAL));
		grpBackground.setBackgroundMode(SWT.INHERIT_DEFAULT);

		sampleCanvas = new Canvas(grpBackground, SWT.BORDER | SWT.NO_FOCUS);
		sampleCanvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent evt) {
				if (appearenceConfig.isGradient()) {
					evt.gc.setForeground(appearenceConfig.getGradientColor());
					evt.gc.fillGradientRectangle(0, 0, 50, sampleCanvas.getSize().y, false);
					evt.gc.dispose();
				}
			}
		});

		Button btnUpdateBackground = new Button(grpBackground, SWT.NONE);
		btnUpdateBackground.setLayoutData(new RowData(173, SWT.DEFAULT));
		btnUpdateBackground.setText("Couleur...");
		btnUpdateBackground.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
				colorDialog.setRGB(appearenceConfig.getBackgroundColor().getRGB());
				colorDialog.setText("Couleur de dégradé");
				RGB selectedColor = colorDialog.open();
				if (selectedColor != null) {
					Color myColor = new Color(Display.getCurrent(), selectedColor);
					sampleCanvas.setBackground(myColor);
					sampleCanvas.redraw();
					appearenceConfig.setBackgroundColor(myColor);
					refreshVisuals();
				}
			}
		});

		btnGradient = new Button(grpBackground, SWT.CHECK);

		btnGradient.setText("gradient");

		btnGradiantColor = new Button(grpBackground, SWT.NONE);
		btnGradiantColor.setLayoutData(new RowData(149, SWT.DEFAULT));
		btnGradiantColor.setText("Couleur...");
		btnGradiantColor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
				colorDialog.setRGB(appearenceConfig.getGradientColor().getRGB());
				colorDialog.setText("Couleur de dégradé");
				RGB selectedColor = colorDialog.open();
				if (selectedColor != null) {
					Color myColor = new Color(Display.getCurrent(), selectedColor);
					appearenceConfig.setGradientColor(myColor);
					sampleCanvas.redraw();
					refreshVisuals();
				}
			}
		});
		btnGradient.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnGradiantColor.setEnabled(btnGradient.getSelection());
				appearenceConfig.setGradient(btnGradient.getSelection());
				refreshVisuals();
			}
		});
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (!selection.isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object o = structuredSelection.getFirstElement();
			if (o instanceof AppAbstractEditPart) {
				activePart = ((AppAbstractEditPart) o);
				Node n = (Node) activePart.getModel();
				setAppearenceConfig(n.getAppearenceConfig());
				tmpAppearenceConfig = null;
				grpBackground.setVisible(o instanceof AjaxRequestPart);
			}
		}
	}

	public AppearenceConfig getAppearenceConfig() {
		return appearenceConfig;
	}

	public void setAppearenceConfig(AppearenceConfig appearenceConfig) {
		this.appearenceConfig = appearenceConfig;
		btnApparenceSpcifique.setSelection(appearenceConfig.isEnabled());
		selfAppearenceComposite.setEnabled(appearenceConfig.isEnabled());
		sampleCanvas.setBackground(appearenceConfig.getBackgroundColor());
		txtExemple.setForeground(appearenceConfig.getFontColor());
		txtExemple.setFont(appearenceConfig.getFont());
		btnGradient.setSelection(appearenceConfig.isGradient());
		btnGradiantColor.setEnabled(appearenceConfig.isGradient());
		sampleCanvas.redraw();
	}

	private void setAppearenceEnabled(boolean enabled) {
		Node n = (Node) activePart.getModel();
		if (enabled) {
			if (tmpAppearenceConfig != null) {
				tmpAppearenceConfig.setEnabled(true);
				setAppearenceConfig(tmpAppearenceConfig);
			}
			else
				setAppearenceConfig(new AppearenceConfig(appearenceConfig));
		} else {
			tmpAppearenceConfig = n.getAppearenceConfig();
			n.getAppearenceConfig().setEnabled(false);
			setAppearenceConfig(n.getAppearenceConfig());
		}

		BaseFigure bFigure = (BaseFigure) activePart.getFigure();
		n.setAppearenceConfig(appearenceConfig);
		bFigure.setAppearenceConfig(appearenceConfig);
		refreshVisuals();
	}

	public void refreshVisuals() {
		Node n = (Node) activePart.getModel();
		n.getListeners().firePropertyChange(Node.PROPERTY_APPEARENCE, null, true);
	}
}
