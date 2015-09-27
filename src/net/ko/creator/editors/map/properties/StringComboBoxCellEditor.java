package net.ko.creator.editors.map.properties;

import net.ko.utils.KString;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

/**
 * An extended ComboBoxCellEditor that selects and returns Strings. The code
 * comes from
 * org.eclipse.wst.xml.ui.internal.properties.StringComboBoxCellEditor and
 * manages "add item in the combo when it is not found."
 */

public class StringComboBoxCellEditor extends ComboBoxCellEditor {

	private boolean fSettingValue = false;
	private boolean addItemWhenNotFound;

	/**
     * 
     */
	public StringComboBoxCellEditor() {
		super();
		setAddItemWhenNotFound(true);
	}

	/**
	 * @param parent
	 * @param items
	 */
	public StringComboBoxCellEditor(Composite parent, String[] items) {
		super(parent, items);
		setAddItemWhenNotFound(true);
	}

	/**
	 * @param parent
	 * @param items
	 * @param style
	 */
	public StringComboBoxCellEditor(Composite parent, String[] items, int style) {
		super(parent, items, style);
		setAddItemWhenNotFound(true);
	}

	protected Object doGetValue() {
		// otherwise limits to set of valid values
		Object index = super.doGetValue();
		int selection = -1;
		if (index instanceof Integer) {
			selection = ((Integer) index).intValue();
		}
		if (selection >= 0) {
			return getItems()[selection];
		} else if (getControl() instanceof CCombo) {
			// retrieve the actual text as the list of valid items doesn't
			// contain the value
			return ((CCombo) getControl()).getText();
		}
		return null;
	}

	protected void doSetValue(Object value) {
		if (fSettingValue) {
			return;
		}
		try {
			fSettingValue = true;
			if (value instanceof Integer) {
				super.doSetValue(value);
			} else {
				String stringValue = value.toString();
				int selection = -1;
				String[] items = getItems();
				for (int i = 0; i < items.length; i++) {
					if (stringValue.equals(items[i])) {
						selection = i;
					}
				}
				if (selection >= 0) {
					super.doSetValue(new Integer(selection));
				} else {
					if (mustAddNewItem(stringValue)) {
						String[] newItems = new String[items.length + 1];
						newItems[0] = stringValue;
						for (int i = 0; i < items.length; i++) {
							newItems[i + 1] = items[i];
						}
						super.setItems(newItems);
						super.doSetValue(new Integer(0));
					} else {
						super.doSetValue(new Integer(-1));
						if ((getControl() instanceof CCombo)
								&& !stringValue.equals(((CCombo) getControl())
										.getText())) { // update the Text widget
							((CCombo) getControl()).setText(stringValue);
						}
					}

				}
			}
		} finally {
			fSettingValue = false;
		}
	}

	/**
	 * Returne true if the given no existing item should be added and false
	 * otherwise.
	 * 
	 * @param item
	 * @return
	 */
	private boolean mustAddNewItem(String item) {
		return addItemWhenNotFound && KString.isNotNull(item);
	}

	public void setItems(String[] newItems) {
		if ((getControl() == null) || getControl().isDisposed()) {
			//           Logger.log(Logger.ERROR, "Attempted to update item list for disposed cell editor"); //$NON-NLS-1$
			return;
		}

		// keep selection if possible
		Object previousSelectedValue = getValue();
		super.setItems(newItems);
		if ((previousSelectedValue != null) && (getControl() instanceof CCombo)) {
			for (int i = 0; i < newItems.length; i++) {
				if (newItems[i].equals(previousSelectedValue)) {
					setValue(previousSelectedValue);
				}
			}
		}
	}

	/**
	 * Set true if item which is not found in the combo must be added and false
	 * otherwise.
	 * 
	 * @param addItemWhenNotFound
	 */
	public void setAddItemWhenNotFound(boolean addItemWhenNotFound) {
		this.addItemWhenNotFound = addItemWhenNotFound;
	}

	/**
	 * Returns true if item which is not found in the combo must be added and
	 * false otherwise.
	 * 
	 * @return true if item which is not found in the combo must be added and
	 *         false otherwise.
	 */
	public boolean isAddItemWhenNotFound() {
		return addItemWhenNotFound;
	}
}