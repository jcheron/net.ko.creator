package net.ko.bean;


import org.eclipse.swt.SWT;

public enum ZoneType {
	ztFunc("func","bFunc.png",SWT.COLOR_DARK_YELLOW),ztField("field","bField.png",SWT.COLOR_BLUE),ztZone("zone","bZone.png",SWT.COLOR_DARK_GREEN),ztHTML("html","bHtml.png",SWT.COLOR_MAGENTA);
	private String label;
	private String image;
	private int color=SWT.COLOR_BLACK;
	private ZoneType(String label,String image,int color){
		this.label=label;
		this.image=image;
		this.color=color;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public static ZoneType getType(String zType){
		ZoneType result=null;
		for(ZoneType ct:ZoneType.values())
			if(ct.getLabel().equals(zType)){
				result=ct;
				break;
			}
		return result;
	}
}
