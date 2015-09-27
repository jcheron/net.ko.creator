package net.ko.creator.editors.map.properties;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class PropertiesValidators {
	public static ICellEditorValidator intValidator = new ICellEditorValidator() {
		@Override
		public String isValid(Object value) {
			try {
				Integer.valueOf(value + "");
			}
			catch (Exception e) {
				return "Nombre entier invalide";
			}
			return null;
		}
	};
}
