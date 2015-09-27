package net.ko.creator.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class EditorUtils {
	public static final String WARNING = "warning.png";
	public static final String ERROR = "error.gif";

	public static void setImage(Label lbl, String text) {
		Display display = Display.getCurrent();
		Image img = new Image(display, EditorUtils.class.getResourceAsStream("title.jpg"));

		Font font = new Font(display, "Tahoma", 8, SWT.BOLD);
		GC gc = new GC(img);
		gc.setFont(font);
		gc.setForeground(new Color(display, 69, 76, 127));
		gc.drawText(text, 10, 5, true);
		lbl.setImage(img);
		gc.dispose();
		font.dispose();
	}

	public static void setTitleImage(Composite compo, String imgFilename) {
		Image img = new Image(Display.getCurrent(), EditorUtils.class.getResourceAsStream(imgFilename));
		compo.setBackgroundImage(img);
		compo.setBackgroundMode(SWT.INHERIT_FORCE);
	}

}
