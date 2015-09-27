package net.ko.creator.editors.map.figure;

import net.ko.creator.editors.images.Images;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.swt.graphics.Image;

public class ToggleFigure extends ImageFigure {
	private final static Image IMAGE_COLLAPSE = Images.getImage(Images.COLLAPSE);
	private final static Image IMAGE_EXPAND = Images.getImage(Images.EXPAND);
	private boolean expanded = false;

	public ToggleFigure() {
		super();
		setExpanded(true);
		setRequestFocusEnabled(false);
		// activeImage = new ImageFigure(IMAGE_COLLAPSE);
		// setContents(activeImage);
		// setFocusTraversable(true);
		// setRolloverEnabled(true);
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		if (expanded != this.expanded) {
			this.expanded = expanded;
			if (expanded)
				setImage(IMAGE_COLLAPSE);
			else
				setImage(IMAGE_EXPAND);
		}
	}

	public void expandCollapse() {
		setExpanded(!expanded);
	}

}
