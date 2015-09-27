package net.ko.wizard.display;

import net.ko.bean.display.KoxDisplay;
import net.ko.creator.editors.images.Images;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class DisplayClassLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof KoxDisplay)
			return Images.getImage(Images.CLASS);
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		return element.toString();
	}

}
