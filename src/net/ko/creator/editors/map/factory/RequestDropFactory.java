package net.ko.creator.editors.map.factory;

import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.mapping.KAjaxRequest;

import org.eclipse.gef.requests.CreationFactory;

public class RequestDropFactory implements CreationFactory {

	private String requestURL;

	@Override
	public Object getNewObject() {
		AjaxRequest result = new AjaxRequest();
		result.setAjaxRequest(new KAjaxRequest(requestURL));
		return result;
	}

	@Override
	public Object getObjectType() {
		return AjaxRequest.class;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

}
