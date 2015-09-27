package net.ko.bean.display;

import net.ko.utils.KString;

public class KoxDisplay {
	private String className;
	private String value;

	public KoxDisplay(String className, String value) {
		super();
		this.className = className;
		this.value = value;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		String result = className;
		if (KString.isNotNull(value))
			result += " (" + value + ")";
		return result;
	}
}
