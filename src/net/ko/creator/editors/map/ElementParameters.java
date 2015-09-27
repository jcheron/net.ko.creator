package net.ko.creator.editors.map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class ElementParameters {
	private Font font = new Font(null, "Tahoma", 9, SWT.BOLD);
	private Color foregroundColor = ColorConstants.black;

	public ElementParameters() {
		super();
	}

	public ElementParameters(Font font, Color foregroundColor) {
		super();
		this.font = font;
		this.foregroundColor = foregroundColor;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

}
