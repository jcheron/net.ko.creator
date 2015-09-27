package net.ko.creator.controls;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class ColorizedItemsList implements Iterable<ColorizedItem>{
	private KstyledText kstyledText;

	private ArrayList<ColorizedItem> colorizedItemsList;

	public ColorizedItemsList(KstyledText kstyledText) {
		colorizedItemsList=new ArrayList<>();
		this.kstyledText=kstyledText;
	}
	public void colorize(int start,String value){
		Display display=Display.getCurrent();
		for(ColorizedItem item:colorizedItemsList){
			ArrayList<Point> pts=item.match(value);
			if(pts.size()>0){
				for(Point pt:pts){
					pt.x=pt.x+start;
					kstyledText.setRangeColor(display.getSystemColor(item.getColor()), pt);
				}
			}
		}
	}
	public void add(String regExp,int color){
		colorizedItemsList.add(new ColorizedItem(regExp, color));
	}
	@Override
	public Iterator<ColorizedItem> iterator() {
		return colorizedItemsList.iterator();
	}
}
