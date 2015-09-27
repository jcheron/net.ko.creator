package net.ko.creator.editors.map.figure;

import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class BaseFigure extends Figure {
	protected Label lblCaption = new Label();
	protected int originalHeight;
	public final static int DEF_WIDTH = 300;
	public final static int DEF_HEIGHT = 100;
	private IFigure parentFigure;
	protected boolean selected;
	protected CompartmentFigure compartment = new CompartmentFigure();
	protected ToggleFigure pinFigure;
	protected TooltipFigure tooltip;
	protected Figure title;
	protected AppearenceConfig appearenceConfig;

	public BaseFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		tooltip = new TooltipFigure();
		setToolTip(tooltip);
		selected = false;
		title = new Figure();
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHorizontalSpacing(2);
		title.setLayoutManager(borderLayout);
		title.add(lblCaption, BorderLayout.CENTER);
		lblCaption.setLabelAlignment(PositionConstants.LEFT);
		add(title);
		add(compartment);
	}

	public void setCaption(String caption) {
		lblCaption.setText(caption);
	}

	public void setLayout(Rectangle rect) {
		getParent().setConstraint(this, rect);
	}

	public void setTooltipText(String tooltipText) {
		tooltip.setMessage(tooltipText);
	}

	@Override
	public void add(IFigure figure, Object constraint, int index) {
		if (figure instanceof ImageFigure)
			addToggle((ToggleFigure) figure);
		else if (!(figure instanceof HasCompartment))
			super.add(figure, constraint, index);
		else
			compartment.add(figure, constraint);
	}

	private void addToggle(ToggleFigure toggleFigure) {
		pinFigure = toggleFigure;
		title.add(pinFigure, BorderLayout.LEFT, 0);
	}

	public CompartmentFigure getCompartment() {
		return compartment;
	}

	public void setCompartment(CompartmentFigure compartment) {
		this.compartment = compartment;
	}

	public Image getImage() {
		return lblCaption.getIcon();
	}

	@Override
	public void remove(IFigure figure) {
		if (!(figure instanceof HasCompartment))
			super.remove(figure);
		else
			compartment.remove(figure);
	}

	public boolean isExpanded() {
		if (pinFigure != null)
			return pinFigure.isExpanded();
		return true;
	}

	protected void handleExpandStateChanged() {
		boolean isExpanded = isExpanded();
		if (!isExpanded) {
			if (compartment.getParent() != this) {
				add(compartment);
			}
		} else {
			if (compartment.getParent() == this) {
				remove(compartment);
			}
		}
		setAutoHeight();
		pinFigure.expandCollapse();
	}

	public void setAutoHeight() {
		Rectangle r = getBounds().getCopy();
		r.setHeight(-1);
		setLayout(r);
		BaseFigure parent = getVisibleParent();
		if (parent != null && parent != this)
			parent.setAutoHeight();
	}

	public void showCollapse(boolean hasChildren) {
		boolean visible = (pinFigure != null) && hasChildren && (this instanceof HasCompartment || this instanceof AjaxRequestFigure);
		if (visible)
			lblCaption.setBorder(getInitialCaptionBorder());
		else
			lblCaption.setBorder(getCollapseCaptionBorder());
		if (pinFigure != null)
			pinFigure.setVisible(visible);
	}

	protected MarginBorder getInitialCaptionBorder() {
		return new MarginBorder(0, 0, 0, 0);
	}

	protected MarginBorder getCollapseCaptionBorder() {
		return new MarginBorder(0, 16, 0, 0);
	}

	public void setExpanded(boolean expanded) {
		if (expanded != isExpanded()) {
			if (pinFigure != null)
				handleExpandStateChanged();
		}
	}

	@Override
	public boolean isVisible() {
		boolean result = super.isVisible();
		if (!result)
			return result;
		return result;
	}

	public boolean isCollapsedFromParent() {
		boolean result = isCollapsedFromParent(this);
		return result;
	}

	protected Boolean isCollapsedFromParent(IFigure figure) {
		IFigure parentFigure = figure.getParent();
		if (parentFigure != null && !(parentFigure instanceof MoxFileFigure))
			return isCollapsedFromParent(parentFigure);
		else if (figure instanceof BaseFigure) {
			if (figure instanceof AjaxRequestFigure)
				return false;
			else
				return ((BaseFigure) figure).isExpanded();
		}
		return true;
	}

	public BaseFigure getVisibleParent() {
		if (isVisible() && !isCollapsedFromParent())
			return this;
		return getVisibleParent(this);
	}

	public BaseFigure getVisibleParent(IFigure figure) {
		IFigure parentFigure = figure.getParent();
		if (parentFigure != null && !(parentFigure instanceof MoxFileFigure)) {
			if (parentFigure instanceof BaseFigure)
				return ((BaseFigure) parentFigure).getVisibleParent();
			else {
				if (parentFigure instanceof CompartmentFigure) {
					figure = ((CompartmentFigure) parentFigure).getParentFigure();
					return ((BaseFigure) figure).getVisibleParent();
				}
			}
		}
		if (figure instanceof BaseFigure)
			return (BaseFigure) figure;
		return null;
	}

	public void highlight(Boolean newValue) {
		// TODO Auto-generated method stub

	}

	public void select(boolean newValue) {
		this.selected = newValue;
	}

	public void setTitleBackgroundColor(Color color) {
		title.setBackgroundColor(color);
	}

	public void refreshParameters() {

	}

	public AppearenceConfig getAppearenceConfig() {
		return appearenceConfig;
	}

	public void setAppearenceConfig(AppearenceConfig appearenceConfig) {
		this.appearenceConfig = appearenceConfig;
	}

	public void refreshAppearence() {
		lblCaption.setFont(appearenceConfig.getFont());
		lblCaption.setForegroundColor(appearenceConfig.getFontColor());
	}
}
