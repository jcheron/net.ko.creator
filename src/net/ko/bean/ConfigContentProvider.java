package net.ko.bean;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ConfigContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viever, Object element, Object value) {
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Object[] result=null;
		if (parentElement instanceof List)
			result=((List<?>) parentElement).toArray();
		else{
			ConfigBean cb=(ConfigBean) parentElement;
			if(cb!=null)
				result=cb.getChildren();}
		return result;
	}

	@Override
	public Object[] getElements(Object variables) {
		return getChildren(variables);
	}

	@Override
	public Object getParent(Object element) {
		ConfigBean cb=(ConfigBean) element;
		ConfigBean result=null;
		if(cb!=null)
			result=cb.getParent();
		return result;
	}

	@Override
	public boolean hasChildren(Object element) {
		ConfigBean cb=(ConfigBean) element;
		boolean result=false;
		if(cb!=null)
			result=cb.hasChildren();
		return result;
	}

}
