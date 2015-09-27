package net.ko.creator.editors.map.properties;

import net.ko.creator.editors.map.model.MoxFile;
import net.ko.creator.editors.map.utils.PropertyUtils;

import org.eclipse.gmf.runtime.common.ui.services.properties.extended.ExtendedBooleanPropertyDescriptor;
import org.eclipse.gmf.runtime.common.ui.services.properties.extended.ExtendedComboboxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class PropertiesDescriptor {
	public static IPropertyDescriptor getPropertyEvents(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, PropertyUtils.events, "click", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyControlTypes(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, PropertyUtils.controlTypes, "text", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyMethod(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, new String[] { "POST", "GET" }, "", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyBoolean(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedBooleanPropertyDescriptor(id, id), "Infos");
	}

	public static IPropertyDescriptor getPropertyText(String id) {
		return PropertyUtils.getPropertyDescriptor(new TextPropertyDescriptor(id, id), "Infos");
	}

	public static IPropertyDescriptor getPropertyInt(String id) {
		IPropertyDescriptor result = getPropertyText(id);
		((PropertyDescriptor) result).setValidator(PropertiesValidators.intValidator);
		return result;
	}

	public static IPropertyDescriptor getPropertyURLs(String id, MoxFile moxFile) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, PropertyUtils.getURLs(moxFile, false), "", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyAllURLs(String id, MoxFile moxFile) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, PropertyUtils.getURLs(moxFile, true), "", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyKClasses(String id, MoxFile moxfile) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, PropertyUtils.getClasses(moxfile), "", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyUpdateOneOp(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, new String[] { "add", "update" }, "add", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyMultiLineText(String id, String title, String message) {
		return PropertyUtils.getPropertyDescriptor(new TextDataPropertyDescriptor(id, id, title, message), "Infos");
	}

	public static IPropertyDescriptor getPropertyContentAssistText(String id) {
		return PropertyUtils.getPropertyDescriptor(new ContentAssistPropertyDescriptor(id, id), "Infos");
	}

	public static IPropertyDescriptor getPropertyContentAssistField(String id) {
		return PropertyUtils.getPropertyDescriptor(new ContentAssistFieldDescriptor(id, id, null, null), "Infos");
	}

	public static IPropertyDescriptor getPropertyAccordionType(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, new String[] { "checkbox", "radio" }, "radio", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyTransition(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, new String[] { "opacityShow", "opacityHide", "rotation10", "rotation120", "rotation180", "deflate", "inflate", "skew", "translate", "background", "shadow" }, "opacityShow", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyCssProperty(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, new String[] { "opacity", "transform", "width", "height", "top", "left", "color", "background-color", "font-size" }, "opacity", true), "Infos");
	}

	public static IPropertyDescriptor getPropertyCssTiming(String id) {
		return PropertyUtils.getPropertyDescriptor(new ExtendedComboboxPropertyDescriptor(id, id, new String[] { "linear", "ease", "ease-in", "ease-out", "ease-in-out", "step-start", "step-end" }, "linear", true), "Infos");
	}
}
