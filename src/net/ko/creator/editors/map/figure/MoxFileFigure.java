package net.ko.creator.editors.map.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

public class MoxFileFigure extends BaseFigure {
	public MoxFileFigure() {
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		lblCaption.setForegroundColor(ColorConstants.darkGray);
		add(lblCaption);
		setConstraint(lblCaption, new Rectangle(5, 5, -1, -1));
		setForegroundColor(ColorConstants.black);
		setBorder(new LineBorder(1));
		setToolTip(null);
	}

	@Override
	public void setLayout(Rectangle rect) {
		setBounds(rect);
	}
}
