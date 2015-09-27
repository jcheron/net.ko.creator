package net.ko.bean.mapping;

import net.ko.utils.KString;

import org.w3c.dom.Element;

public class MoxController {
	private String requestURL;
	private String responseURL;
	private boolean mainControl;
	private String classControl;
	private Element element;

	public MoxController(Element element, String requestURL, String responseURL, boolean mainControl, String classControl) {
		super();
		this.element = element;
		this.requestURL = requestURL;
		this.responseURL = responseURL;
		this.mainControl = mainControl;
		this.classControl = classControl;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getResponseURL() {
		return responseURL;
	}

	public void setResponseURL(String responseURL) {
		this.responseURL = responseURL;
	}

	public boolean isMainControl() {
		return mainControl;
	}

	public void setMainControl(boolean mainControl) {
		this.mainControl = mainControl;
	}

	public String getClassControl() {
		return classControl;
	}

	public void setClassControl(String classControl) {
		this.classControl = classControl;
	}

	public boolean isVirtual() {
		boolean result = false;
		if (responseURL != null && responseURL.contains("["))
			result = true;
		return result;
	}

	@Override
	public String toString() {
		String result = requestURL + "->" + responseURL;
		if (KString.isNotNull(classControl))
			result += " : " + classControl;
		if (mainControl)
			result += " (*)";
		return result;
	}

	public boolean compareTo(Element e) {
		if (e.getNodeName().equals("mapping"))
			return requestURL.equals(e.getAttribute("requestURL")) && responseURL.equals(e.getAttribute("responseURL"));
		else
			return requestURL.equals(e.getAttribute("requestURL")) && responseURL.equals("[" + e.getAttribute("mappingFor") + "]");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		MoxController mox = (MoxController) obj;
		return (requestURL == mox.requestURL || (requestURL != null && requestURL.equals(mox.getRequestURL()))) &&
				(responseURL == mox.responseURL || (responseURL != null && responseURL.equals(mox.getResponseURL())));

	}

	@Override
	public int hashCode() {
		int result = 1;
		result += (requestURL == null) ? 0 : requestURL.hashCode();
		result += result * ((responseURL == null) ? 0 : responseURL.hashCode());
		return result;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
