package net.ko.creator.controls;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Point;

public class ColorizedItem {
	private String regExp;
	private int color;
	public ColorizedItem(String regExp, int color) {
		super();
		this.regExp = regExp;
		this.color = color;
	}
	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public ArrayList<Point> match(String value){
		ArrayList<Point> result=new ArrayList<>();
		value=Matcher.quoteReplacement(value);
		Pattern p = Pattern.compile(regExp+"(?:\\s*?)",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(value);
		while(m.find()){
				result.add(new Point(m.start(), m.end()-m.start()));
		}
		return result;
	}
	
}
