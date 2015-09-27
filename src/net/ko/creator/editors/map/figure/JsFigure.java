package net.ko.creator.editors.map.figure;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;

public class JsFigure extends BaseFigure implements HasCompartment {

	public JsFigure() {
		super();
		// lblCaption.setFont(MapParameters.JsFont.getFont());
		// lblCaption.setForegroundColor(MapParameters.JsFont.getForegroundColor());
		lblCaption.setIcon(Images.getImage(Images.JS));
	}

	@Override
	public void refreshParameters() {
		AppearenceConfig appConfig = getAppearenceConfig();
		lblCaption.setFont(appConfig.getFont());
		lblCaption.setForegroundColor(appConfig.getFontColor());
	}
}
