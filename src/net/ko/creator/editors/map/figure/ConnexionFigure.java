package net.ko.creator.editors.map.figure;

import net.ko.creator.editors.map.MapParameters;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

public class ConnexionFigure extends PolylineConnection {

	private Label label;
	protected boolean selected;
	protected TooltipFigure tooltip;

	public ConnexionFigure(String caption) {
		super();
		setLineWidth(MapParameters.connexionLineWidth);
		setForegroundColor(MapParameters.connexionColor);
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		setTargetDecoration(decoration);
		label = new Label();
		label.setText(caption);
		setLineStyle(MapParameters.connexionLineStyle);
		label.setBackgroundColor(ColorConstants.white);
		label.setOpaque(true);
		add(label, new MidpointLocator(this, 0));
		selected = false;
		tooltip = new TooltipFigure();
		setToolTip(tooltip);
		setConnectionRouter(new BendpointConnectionRouter());
	}

	public String getCaption() {
		return label.getText();
	}

	public void setCaption(String caption) {
		label.setText(caption);
	}

	public void setTooltipText(String tooltipText) {
		tooltip.setMessage(tooltipText);
	}

	public void setCaptionVisible(boolean visible) {
		label.setVisible(visible && isVisible());
	}

	public void select(boolean newValue) {
		selected = newValue;
		if (selected) {
			setLineWidth(MapParameters.connexionLineWidth + 1);
			setForegroundColor(MapParameters.highlightDarkColor);
		}
		else {
			setLineWidth(MapParameters.connexionLineWidth);
			setForegroundColor(MapParameters.connexionColor);
		}
	}

	public void highlight(boolean highlight) {
		if (highlight) {
			setLineWidth(MapParameters.connexionLineWidth + 1);
			setForegroundColor(MapParameters.highlightDarkColor);
		}
		else {
			if (!selected) {
				setLineWidth(MapParameters.connexionLineWidth);
				setForegroundColor(MapParameters.connexionColor);
			}
		}
	}

	public void refreshParameters() {
		setLineWidth(MapParameters.connexionLineWidth);
		setForegroundColor(MapParameters.connexionColor);
		setLineStyle(MapParameters.connexionLineStyle);

	}

	@Override
	public boolean isVisible() {
		return super.isVisible() && MapParameters.areConnectionsVisibles;

	}
}
