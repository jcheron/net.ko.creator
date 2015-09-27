package net.ko.creator.editors.map.model;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.MapEditor;
import net.ko.creator.editors.map.properties.PropertiesDescriptor;
import net.ko.creator.utils.FrameworkUtils;
import net.ko.mapping.KAbstractFilterMapping;
import net.ko.mapping.KAjaxIncludes;
import net.ko.mapping.KAjaxRequest;
import net.ko.mapping.KFilterMappings;
import net.ko.mapping.KXmlMappings;
import net.ko.utils.KString;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class MoxFile extends Node {
	private static final long serialVersionUID = 1L;
	private String fileName;
	private String packageName;
	private transient KXmlMappings xmlMappings;
	private transient MapEditor editor;

	public MoxFile(MapEditor editor) {
		super();
		this.editor = editor;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		if (KString.isNotNull(fileName))
			openXML();
	}

	private void openXML() {
		xmlMappings = new KXmlMappings(false);
		xmlMappings.loadFromFile(FrameworkUtils.getCompleteFileName(fileName), true);
		setName(fileName);
		setId(fileName);
		init();
	}

	private void init() {
		for (KAbstractFilterMapping inc : xmlMappings.getAjaxIncludes().getItems()) {
			if (inc instanceof KAjaxRequest) {
				KAjaxRequest kajaxRequest = (KAjaxRequest) inc;
				AjaxRequest ajaxRequest = new AjaxRequest();
				addChild(ajaxRequest);
				ajaxRequest.setAjaxRequest(kajaxRequest);
			}
		}
		for (Node n : getAllChildrenArray()) {
			if (n instanceof AjaxObject)
				((AjaxObject) n).addConnections();
		}
	}

	public void updateRequest(String oldValue, String newValue) {
		for (Node n : getChildrenArray()) {
			if (n instanceof AjaxRequest) {
				AjaxRequest ajxR = (AjaxRequest) n;
				if (ajxR.getAjaxRequest().getRequestURL().equals(oldValue))
					ajxR.setRequestURL(newValue);
			}
		}
	}

	public List<AjaxRequest> getAjaxRequestsByURL(String url) {
		List<AjaxRequest> result = new ArrayList<>();
		for (Node n : getChildrenArray()) {
			if (n instanceof AjaxRequest) {
				AjaxRequest ajxR = (AjaxRequest) n;
				if (ajxR.getAjaxRequest().getRequestURL().equals(url))
					result.add(ajxR);
			}
		}
		return result;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		properties.add(PropertiesDescriptor.getPropertyText("fichier"));
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(String id) {
		Object result = null;
		if ("fichier".equals(id))
			result = fileName;
		return result;
	}

	@Override
	public void setPropertyValue(String id, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateProperty(String id, Object oldValue, Object value) {
		// TODO Auto-generated method stub

	}

	public KFilterMappings getMappings() {
		return xmlMappings.getMappings();
	}

	public KAjaxIncludes getajaxIncludes() {
		return xmlMappings.getAjaxIncludes();
	}

	@Override
	public String getImage() {
		return Images.MAPPING;
	}

	@Override
	public Object getXmlObject() {
		return xmlMappings;
	}

	@Override
	public List<Object> getXmlChildren() {
		return new ArrayList<Object>(xmlMappings.getAjaxIncludes().getItems());
	}

	@Override
	public boolean removeXmlChild(Object child) {
		return xmlMappings.getAjaxIncludes().getItems().remove(child);
	}

	public void addXmlChild(Object element) {
		if (element instanceof KAjaxRequest)
			xmlMappings.addElement((KAjaxRequest) element);
	}

	public AjaxRequest getAjaxRequest(String requestUrl) {
		AjaxRequest result = null;
		for (Node n : children) {
			if (n instanceof AjaxRequest) {
				if (((AjaxRequest) n).getAjaxRequest().requestURLMatches(requestUrl))
					return (AjaxRequest) n;
			}
		}
		return result;
	}

	public AjaxRequest getAjaxRequest(AjaxRequest ajaxRequest) {
		AjaxRequest result = null;
		int index = children.indexOf(ajaxRequest);
		if (index != -1)
			result = (AjaxRequest) children.get(index);
		return result;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setDirty(boolean dirty) {
		editor.setDirty(dirty);
	}

	@Override
	public void setXmlObject(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTooltipMessage() {
		return "";
	}
}
