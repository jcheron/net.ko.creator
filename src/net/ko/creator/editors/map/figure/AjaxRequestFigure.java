package net.ko.creator.editors.map.figure;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.MapParameters;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.geometry.Rectangle;

public class AjaxRequestFigure extends BaseFigure {
	protected Border initialBorder;

	public AjaxRequestFigure() {
		super();
		initialBorder = new CompoundBorder(new SimpleRaisedBorder(), new MarginBorder(4));
		lblCaption.setIcon(Images.getImage(Images.REQUEST));
		lblCaption.setBorder(new MarginBorder(10, 4, 0, 0));
		setBorder(initialBorder);
		setOpaque(true);
	}

	@Override
	protected MarginBorder getInitialCaptionBorder() {
		return new MarginBorder(10, 4, 0, 0);
	}

	@Override
	protected MarginBorder getCollapseCaptionBorder() {
		return new MarginBorder(10, 20, 0, 0);
	}

	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		if (appearenceConfig.isGradient()) {
			Rectangle r = Rectangle.SINGLETON;
			r.setBounds(getBounds());
			r.width = Math.min(50, r.width);
			g.setForegroundColor(appearenceConfig.getGradientColor());
			g.fillGradient(Rectangle.SINGLETON, false);
			g.restoreState();
		}
	}

	public ConnectionAnchor getAnchor()
	{
		return new ChopboxAnchor(this) {
			protected Rectangle getBox()
			{
				Rectangle base = super.getBox();
				return base.getResized(-4, -4).getTranslated(4, 4);
			}
		};
	}

	@Override
	public void highlight(Boolean newValue) {
		if (newValue) {
			setBorder(new CompoundBorder(MapParameters.highlightBorder, initialBorder));
		} else if (!selected)
			setBorder(initialBorder);
	}

	@Override
	public void select(boolean newValue) {
		super.select(newValue);
		if (newValue) {
			setBorder(new CompoundBorder(MapParameters.selectedBorder, initialBorder));
		} else
			setBorder(initialBorder);
	}

	@Override
	public void refreshParameters() {
		lblCaption.setFont(appearenceConfig.getFont());
		lblCaption.setForegroundColor(appearenceConfig.getFontColor());
		setBackgroundColor(appearenceConfig.getBackgroundColor());
	}

	@Override
	public void refreshAppearence() {
		super.refreshAppearence();
		setBackgroundColor(appearenceConfig.getBackgroundColor());
		repaint();
	}
}
