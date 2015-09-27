package net.ko.wizard.mappingcontroller;

import net.ko.bean.mapping.MoxController;
import net.ko.creator.editors.images.Images;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class MappingClassLabelProvider extends LabelProvider {

	@Override
	public boolean isLabelProperty(Object object, String property) {
		return true;
	}

	@Override
	public Image getImage(Object element) {
		MoxController ctrl = (MoxController) element;
		if (ctrl.isVirtual())
			return Images.getImage(Images.VIRTUAL_MAPPING);
		return Images.getImage(Images.MAPPING);
	}

	@Override
	public String getText(Object element) {
		return element.toString();
	}

}
