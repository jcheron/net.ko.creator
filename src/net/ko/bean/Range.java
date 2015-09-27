package net.ko.bean;

import org.eclipse.swt.graphics.Point;

public class Range {
	public int x;
	public int y;
	private String toolTip;
	public Range(int x, int y, String toolTip) {
		super();
		this.x = x;
		this.y = y;
		this.toolTip = toolTip;
	}
	public Range(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.toolTip="";
	}
	public Range(Point point){
		this(point.x, point.y);
	}
	public String getToolTip() {
		return toolTip;
	}
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	public boolean equals(Point point){
		return (x==point.x && y==point.y);
	}
}
