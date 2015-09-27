package net.ko.creator.editors.map.figure;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.MapParameters;
import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;

import org.eclipse.draw2d.ColorConstants;

public class AjaxObjectFigure extends BaseFigure implements HasCompartment {
	protected String image;

	public AjaxObjectFigure(String image) {
		super();
		if (image != null)
			lblCaption.setIcon(Images.getImage(image));
	}

	@Override
	public void highlight(Boolean newValue) {
		if (newValue) {
			setBorder(MapParameters.highlightBorder);
		} else {
			if (!selected)
				setBorder(null);
		}
	}

	@Override
	public void select(boolean newValue) {
		super.select(newValue);
		if (newValue) {
			title.setBackgroundColor(MapParameters.highlightColor);
			title.setOpaque(true);
		} else {
			title.setBackgroundColor(ColorConstants.white);
			title.setOpaque(false);
			setBorder(null);
		}
	}

	@Override
	public void refreshParameters() {
		AppearenceConfig appConfig = getAppearenceConfig();
		lblCaption.setFont(appConfig.getFont());
		lblCaption.setForegroundColor(appConfig.getFontColor());
	}
}
