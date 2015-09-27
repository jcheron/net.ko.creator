package net.ko.creator.editors.map;

import net.ko.creator.editors.map.part.IGeneralPart;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;

public class SectionPropertiesLabelProvider implements ILabelProvider {

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
	public Image getImage(Object obj) {
		IGeneralPart part = getPart(obj);
		if (part != null)
			return part.getImage();
		return null;
	}

	@Override
	public String getText(Object obj) {
		IGeneralPart part = getPart(obj);
		if (part != null)
			return part.getCaption();
		return null;
	}

	private IGeneralPart getPart(Object obj) {
		if (obj instanceof StructuredSelection) {
			Object sel = ((StructuredSelection) obj).getFirstElement();
			if (sel instanceof IGeneralPart)
				return (IGeneralPart) sel;
		}
		return null;
	}

}
