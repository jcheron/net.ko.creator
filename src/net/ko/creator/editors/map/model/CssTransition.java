package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.design.KCssOneTransition;
import net.ko.design.KCssTransition;
import net.ko.mapping.IAjaxObject;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class CssTransition extends AjaxObject {

	private static final long serialVersionUID = 1L;

	public CssTransition() {
		super();
		addToogle();
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
		List<IAjaxObject> transitions = Arrays.asList((((KCssTransition) ajaxObject).getTransitions().toArray(new IAjaxObject[0])));
		addChilds(transitions);
	}

	@Override
	public Object getPropertyValue(String id) {
		KCssTransition cssTransition = (KCssTransition) ajaxObject;
		Object result = "";
		switch (id) {
		case "targetId":
			result = cssTransition.getTargetId();
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KCssTransition cssTransition = (KCssTransition) ajaxObject;
		switch (id) {
		case "targetId":
			cssTransition.setTargetId(value + "");
			updateProperty(id, oldValue, value);
			break;
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyText("targetId"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.TRANSITION;
	}

	@Override
	protected boolean _addChild(Node child, int index) {
		boolean result = super._addChild(child, index);
		if (result) {
			if (ajaxObject != null) {
				IAjaxObject ajaxTransition = ((AjaxObject) child).getAjaxObject();
				if (ajaxTransition != null) {
					KCssOneTransition cssOneTransition = (KCssOneTransition) ajaxTransition;
					if (((KCssTransition) ajaxObject).getTransitions().indexOf(cssOneTransition) == -1)
						((KCssTransition) ajaxObject).getTransitions().add(cssOneTransition);
				}
			}
		}
		return result;
	}

	@Override
	public boolean removeChild(Node child) {
		boolean result = false;
		if (ajaxObject != null) {
			IAjaxObject ajaxTransition = ((AjaxObject) child).getAjaxObject();
			if (ajaxTransition != null) {
				KCssOneTransition cssOneTransition = (KCssOneTransition) ajaxTransition;
				int indexToRemove = (((KCssTransition) ajaxObject).getTransitions().indexOf(cssOneTransition));
				if (indexToRemove != -1) {
					((KCssTransition) ajaxObject).getTransitions().remove(indexToRemove);
					result = true;
				}
			}
		}
		if (result) {
			result = super.removeChild(child);
		}
		return result;
	}

}
