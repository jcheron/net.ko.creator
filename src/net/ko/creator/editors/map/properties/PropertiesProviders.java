package net.ko.creator.editors.map.properties;

import net.ko.creator.editors.images.Images;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class PropertiesProviders {
	public static ILabelProvider imageLabelProvider(final String image) {
		return new ILabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener arg0) {
			}

			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void addListener(ILabelProviderListener arg0) {
			}

			@Override
			public String getText(Object id) {
				return id + "";
			}

			@Override
			public Image getImage(Object id) {
				return Images.getImage(image);
			}
		};
	}
}
