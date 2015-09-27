package net.ko.creator.editors.map.figure.appearence;

import java.io.IOException;
import java.io.Serializable;

import net.ko.creator.editors.map.MapParameters;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class AppearenceConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected FontData fontData;
	protected Color fontColor;
	protected boolean enabled;
	protected boolean gradient;
	protected Color gradientColor;
	protected Color backgroundColor;
	private static transient AppearenceConfig defaultRequest;
	private static transient AppearenceConfig defaultJs;
	private static transient AppearenceConfig defaultAjaxObject;
	private transient Font font;
	private transient boolean fontUpdated;

	private void writeObject(java.io.ObjectOutputStream stream)
			throws IOException {
		write(stream, fontData);
		stream.writeObject(fontColor.getRGB());
		stream.writeObject(gradientColor.getRGB());
		stream.writeObject(backgroundColor.getRGB());
		stream.writeBoolean(enabled);
		stream.writeBoolean(gradient);
	}

	private void readObject(java.io.ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		Device device = Display.getCurrent();
		fontData = read(stream);
		RGB rgb = (RGB) stream.readObject();
		fontColor = new Color(device, rgb);
		rgb = (RGB) stream.readObject();
		gradientColor = new Color(device, rgb);
		rgb = (RGB) stream.readObject();
		backgroundColor = new Color(device, rgb);
		enabled = stream.readBoolean();
		gradient = stream.readBoolean();
	}

	private void write(java.io.ObjectOutputStream stream, FontData fontData) throws IOException {
		stream.writeObject(fontData.getName());
		stream.writeInt(fontData.getHeight());
		stream.writeInt(fontData.getStyle());
	}

	private FontData read(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		String name = (String) stream.readObject();
		int height = stream.readInt();
		int style = stream.readInt();
		return new FontData(name, height, style);
	}

	public AppearenceConfig() {
		enabled = false;
		fontData = new FontData();
		fontColor = new Color(null, 0, 0, 0);
		backgroundColor = new Color(null, 255, 255, 255);
		gradient = false;
		gradientColor = new Color(null, 122, 122, 122);
		fontUpdated = false;
		font = null;
	}

	public AppearenceConfig(AppearenceConfig appConfig) {
		enabled = true;
		fontData = new FontData(appConfig.getFontData().getName(), appConfig.getFontData().getHeight(), appConfig.getFontData().getStyle());
		fontColor = appConfig.getFontColor();
		backgroundColor = appConfig.getBackgroundColor();
		gradient = appConfig.isGradient();
		gradientColor = appConfig.getGradientColor();
		fontUpdated = false;
		font = null;
	}

	public FontData getFontData() {
		return fontData;
	}

	public void setFontData(FontData fontData) {
		this.fontData = fontData;
		fontUpdated = true;
	}

	public void setFont(Font font) {
		fontData = font.getFontData()[0];
	}

	public Font getFont() {
		if (font == null || fontUpdated) {
			font = new Font(Display.getCurrent(), fontData);
			fontUpdated = false;
		}
		return font;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isGradient() {
		return gradient;
	}

	public void setGradient(boolean gradient) {
		this.gradient = gradient;
	}

	public Color getGradientColor() {
		return gradientColor;
	}

	public void setGradientColor(Color gradientColor) {
		this.gradientColor = gradientColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public static AppearenceConfig defaultRequest() {
		if (defaultRequest == null) {
			defaultRequest = new AppearenceConfig();
		}
		defaultRequest.setGradientColor(MapParameters.requestBGColor);
		defaultRequest.setGradient(true);
		defaultRequest.setFont(MapParameters.requestFont.getFont());
		defaultRequest.setFontColor(MapParameters.requestFont.getForegroundColor());

		return defaultRequest;
	}

	public static AppearenceConfig defaultJs() {
		if (defaultJs == null) {
			defaultJs = new AppearenceConfig();
		}
		defaultJs.setGradient(false);
		defaultJs.setFont(MapParameters.JsFont.getFont());
		defaultJs.setFontColor(MapParameters.JsFont.getForegroundColor());

		return defaultJs;
	}

	public static AppearenceConfig defaultAjaxObject() {
		if (defaultAjaxObject == null) {
			defaultAjaxObject = new AppearenceConfig();
		}
		defaultAjaxObject.setGradient(false);
		defaultAjaxObject.setFont(MapParameters.ajaxObjectFont.getFont());
		defaultAjaxObject.setFontColor(MapParameters.ajaxObjectFont.getForegroundColor());

		return defaultAjaxObject;
	}
}
