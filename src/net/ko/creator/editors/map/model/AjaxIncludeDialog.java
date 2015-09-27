package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.Arrays;

import net.ko.creator.editors.images.Images;
import net.ko.mapping.IAjaxObject;
import net.ko.mapping.KAjaxInclude;
import net.ko.mapping.KAjaxIncludeDialog;
import net.ko.mapping.KAjaxMessageDialog;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class AjaxIncludeDialog extends AjaxObject {

	private static final long serialVersionUID = 1L;

	public AjaxIncludeDialog() {
		super();
		addToogle();
	}

	@Override
	public void setAjaxObject(IAjaxObject ajaxObject) {
		super.setAjaxObject(ajaxObject);
		setName(ajaxObject.getDisplayCaption());
		KAjaxIncludeDialog includeDialog = (KAjaxIncludeDialog) ajaxObject;
		addChilds(Arrays.asList(new IAjaxObject[] { includeDialog.getInclude(), includeDialog.getDialog() }));
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public String getImage() {
		return Images.INCLUDE_DIALOG;
	}

	@Override
	public Object getPropertyValue(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		super.updateProperty(id, oldValue, value);
	}

	public KAjaxIncludeDialog getAjaxIncludeDialog() {
		return (KAjaxIncludeDialog) ajaxObject;
	}

	@Override
	public boolean addChild(Node child, int index) {
		boolean result = true;
		if (child instanceof AjaxMessageDialog) {
			result = replaceFirstChild((AjaxObject) child);
			KAjaxIncludeDialog ajaxIncludeDialog = getAjaxIncludeDialog();
			if (ajaxIncludeDialog != null)
				ajaxIncludeDialog.setDialog((KAjaxMessageDialog) ((AjaxMessageDialog) child).getAjaxObject());
		}
		if (child instanceof AjaxInclude) {
			result = replaceFirstChild((AjaxObject) child);
			KAjaxIncludeDialog ajaxIncludeDialog = getAjaxIncludeDialog();
			if (ajaxIncludeDialog != null)
				ajaxIncludeDialog.setInclude((KAjaxInclude) ((AjaxInclude) child).getAjaxObject());
		}
		if (child instanceof ToggleNode)
			return super.addChild(child, index);
		return !result;
	}

	@Override
	public boolean removeChild(Node child) {
		boolean possible = false;
		if (parent != null)
			possible = parent.isToRemove();
		if (possible)
			return super.removeChild(child);
		return false;
	}

	public void updateXmlChildren() {
		if (ajaxObject != null) {
			getAjaxIncludeDialog().setInclude((KAjaxInclude) getFirstChild(AjaxInclude.class).getAjaxObject());
			getAjaxIncludeDialog().setDialog((KAjaxMessageDialog) getFirstChild(AjaxMessageDialog.class).getAjaxObject());
		}
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

}
