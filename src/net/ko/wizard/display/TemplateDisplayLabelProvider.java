package net.ko.wizard.display;

import java.io.IOException;

import net.ko.creator.utils.FrameworkUtils;
import net.ko.utils.KString;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.model.WorkbenchLabelProvider;

@SuppressWarnings("restriction")
public class TemplateDisplayLabelProvider extends WorkbenchLabelProvider {

	@Override
	protected String decorateText(String input, Object element) {
		String result = super.decorateText(input, element);
		if (element instanceof File) {
			try {
				String content = FrameworkUtils.getFileContent((File) element);
				String display = KString.getFirstPart("{#koDisplay:", "#}", content);
				if (KString.isNotNull(display) && !content.equals(display))
					result = result + " -> " + display;
			} catch (CoreException | IOException e) {
			}
		}
		return result;
	}

}
