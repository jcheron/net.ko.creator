package net.ko.creator.properties;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;

public class ControlSelection implements ISelection,IAdaptable{

	protected Control control;
	
	
	public ControlSelection(Control control) {
		super();
		this.control = control;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelection#isEmpty()
	 */
	public boolean isEmpty() {
		return (control == null);
	}

	public Control getControl() {
		return control;
	}

	@Override
	public String toString() {
		return "ControlSelection [control=" + control + "]";
	}

	@Override
	public Object getAdapter(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
