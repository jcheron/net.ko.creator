package net.ko.creator.editors.map.model;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxInclude;
import net.ko.utils.KString;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxInclude extends AjaxObject implements IHasConnectorURL {
	private static final long serialVersionUID = 1L;

	public AjaxInclude() {
		super();
		addToogle();
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		KAjaxInclude ajaxInclude = (KAjaxInclude) ajaxObject;
		setName(ajaxInclude.getDisplayCaption());
		addChilds(ajaxInclude.getChilds());
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyAllURLs("targetURL", getMoxFile()));
		properties.add(PropertiesDescriptor.getPropertyText("targetId"));
		properties.add(PropertiesDescriptor.getPropertyMethod("method"));
		properties.add(PropertiesDescriptor.getPropertyText("targetParams"));
		properties.add(PropertiesDescriptor.getPropertyText("formName"));
		properties.add(PropertiesDescriptor.getPropertyText("targetFunction"));
		properties.add(PropertiesDescriptor.getPropertyText("interval"));
		properties.add(PropertiesDescriptor.getPropertyTransition("transition"));
		properties.add(PropertiesDescriptor.getPropertyText("title"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(String id) {
		KAjaxInclude ajaxInclude = (KAjaxInclude) ajaxObject;
		Object result = "";
		switch (id) {
		case "targetURL":
			result = ajaxInclude.getTargetURL();
			break;
		case "targetId":
			result = ajaxInclude.getTargetId();
			break;
		case "method":
			result = ajaxInclude.getMethod();
			break;
		case "targetParams":
			result = ajaxInclude.getTargetParams();
			break;
		case "formName":
			result = ajaxInclude.getFormName();
			break;
		case "targetFunction":
			result = ajaxInclude.getTargetFunction();
			break;
		case "title":
			result = ajaxInclude.getTitle();
			break;
		case "interval":
			result = ajaxInclude.getInterval();
			break;
		case "transition":
			result = ajaxInclude.getTransition();
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		Object oldValue = getPropertyValue(id);
		KAjaxInclude ajaxInclude = (KAjaxInclude) ajaxObject;
		switch (id) {
		case "targetId":
			ajaxInclude.setTargetId(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "targetURL":
			setURL(value + "");
			break;
		case "method":
			ajaxInclude.setMethod(value + "");
			updateProperty(id, oldValue, value);
			break;
		case "targetParams":
			ajaxInclude.setTargetParams(value + "");
			break;
		case "formName":
			ajaxInclude.setFormName(value + "");
			break;
		case "targetFunction":
			ajaxInclude.setTargetFunction(value + "");
			break;
		case "interval":
			ajaxInclude.setInterval(value + "");
			break;
		case "transition":
			ajaxInclude.setTransition(value + "");
			break;
		case "title":
			ajaxInclude.setTitle(KString.addSlashes(value + ""));
		default:
			break;
		}

	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		KAjaxInclude ajaxInclude = (KAjaxInclude) ajaxObject;
		String name = ajaxInclude.getTargetURL();
		if (ajaxInclude.getDOMSelector() != null)
			name += "->[" + ajaxInclude.getDOMSelector() + "]";
		setName(name);
		getListeners().firePropertyChange(id, oldValue, value);
	}

	@Override
	public String getImage() {
		return Images.INCLUDE;
	}

	private KAjaxInclude getAjaxInclude() {
		return (KAjaxInclude) ajaxObject;
	}

	@Override
	public void setURL(String newURL) {
		setURL(newURL, false);
	}

	@Override
	public String getURL() {
		return getAjaxInclude().getTargetURL();
	}

	@Override
	public void setURL(String newURL, boolean fromRequest) {
		String oldURL = getURL();
		if (!newURL.equals(oldURL)) {
			getAjaxInclude().setTargetURL(newURL);
			updateProperty("targetURL", oldURL, newURL);
			if (!fromRequest)
				updateConnection(newURL);
			else
				refreshSourceConnection();
		}
	}
}
