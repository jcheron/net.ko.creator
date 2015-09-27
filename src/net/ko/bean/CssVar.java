package net.ko.bean;

public class CssVar implements Comparable<CssVar>{
	private String id;
	private String value;
	private ContentOutlineBean cob;
	public CssVar() {
		this("newId", "");
	}
	public CssVar(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return id+" : ["+value+"]";
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CssVar)
			return ((CssVar) obj).getId().equals(id);
		return super.equals(obj);
	}
	public ContentOutlineBean getCob() {
		return cob;
	}
	public void setCob(ContentOutlineBean cob) {
		this.cob = cob;
	}
	@Override
	public int compareTo(CssVar aCssVar) {
		return this.getId().compareTo(aCssVar.getId());

	}
}
