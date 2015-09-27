package net.ko.bean;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class ContentOutlineBeanElement extends ContentOutlineBean {
	protected Object widget;
	public ContentOutlineBeanElement(String name, String img) {
		this(name, img, null);
	}
	public ContentOutlineBeanElement(String name, String img,Object widget) {
		super(name, img);
		this.widget=widget;
	}
	public Object getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	@Override
	public String getName() {
		String result=super.getName();
		if(widget!=null){
			if(widget instanceof Text)
				result+=" ("+((Text)widget).getText()+")";
			if(widget instanceof Combo)
				result+=" ("+((Combo)widget).getText()+")";
		}
		return result;
	}
	@Override
	public void select() {
		if(widget!=null)
			if(widget instanceof Control){
				if(parent!=null)
					parent.select();
				((Control)widget).setFocus();
			}
		
	}

}
