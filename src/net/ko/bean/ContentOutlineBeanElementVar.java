package net.ko.bean;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

public class ContentOutlineBeanElementVar extends ContentOutlineBeanElement {
	private Object variable;
	public ContentOutlineBeanElementVar(String img, Object widget,Object variable) {
		this("", img, widget, variable);
	}
	public ContentOutlineBeanElementVar(String name,String img, Object widget,Object variable) {
		super(name, img, widget);
		this.variable=variable;
		if(variable instanceof CssVar)
			((CssVar) variable).setCob(this);
	}
	@Override
	public String getName() {
		return variable.toString();
	}
	@Override
	public void select() {
		if(parent!=null)
			parent.select();
		if(widget instanceof TableViewer){
			TableViewer tv=((TableViewer) widget);
			tv.setSelection(new StructuredSelection(variable));
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ContentOutlineBeanElementVar)
			return ((ContentOutlineBeanElementVar) obj).getVariable().equals(this.variable);
		return super.equals(obj);
	}
	public Object getVariable() {
		return variable;
	}
	public void setVariable(Object variable) {
		this.variable = variable;
	}
}
