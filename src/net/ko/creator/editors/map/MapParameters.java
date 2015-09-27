package net.ko.creator.editors.map;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ko.creator.Activator;
import net.ko.creator.editors.map.figure.appearence.AppearenceConfig;
import net.ko.utils.KStrings;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class MapParameters {
	public final static String REQUEST_BG_COLOR = "ko.map.request_background_color";
	public final static String DEFAULT_FONT = "default_font";
	public final static String REQUEST_FONT = "ko.map.request_font";
	public final static String REQUEST_FONT_COLOR = "ko.map.request_font_color";
	public final static String JS_FONT = "ko.map.js_font";
	public final static String JS_FONT_COLOR = "ko.map.js_font_color";
	public final static String AJAX_OBJECT_FONT = "ko.map.ajax_object_font";
	public final static String AJAX_OBJECT_FONT_COLOR = "ko.map.ajax_object_font_color";
	public final static String CONNEXION_COLOR = "ko.map.connexion_color";
	public final static String CONNEXION_LINE_WIDTH = "ko.map.connexion_line_width";
	public final static String CONNEXION_LINE_STYLE = "ko.map.connexion_line_style";
	public final static String CONNEXION_HIGHLIGHT_COLOR = "ko.map.connexion_highlight_color";
	public final static String CONNEXION_HIGHLIGHT_DARK_COLOR = "ko.map.connexion_highlight_dark_color";
	public final static String ALL = "all_properties";
	public static final String CONNEXION_VISIBLE = "ko.map.connexion_visible";

	public static Color requestBGColor = FigureUtilities.mixColors(
			ColorConstants.buttonDarker, ColorConstants.button);
	public static ElementParameters requestFont = new ElementParameters();
	public static ElementParameters JsFont = new ElementParameters(new Font(null, "Tahoma", 9, SWT.BOLD), ColorConstants.darkGreen);
	public static ElementParameters ajaxObjectFont = new ElementParameters(new Font(null, "Tahoma", 8, SWT.NORMAL), ColorConstants.darkGray);
	public static Color connexionColor = FigureUtilities.mixColors(ColorConstants.darkGreen, ColorConstants.darkGray);
	public static int connexionLineWidth = 1;
	public static int connexionLineStyle = SWT.LINE_DOT;
	public static Color highlightColor = new Color(Display.getCurrent(), 203, 232, 246);
	public static Color highlightDarkColor = new Color(Display.getCurrent(), 38, 160, 218);
	public static Border highlightBorder = new LineBorder(MapParameters.highlightDarkColor, 1, Graphics.LINE_SOLID);

	public static Color selectedColor = new Color(Display.getCurrent(), 247, 247, 247);
	public static Color selectedDarkColor = new Color(Display.getCurrent(), 222, 222, 222);
	public static Border selectedBorder = new LineBorder(MapParameters.selectedDarkColor, 1, Graphics.LINE_SOLID);

	public static List<String> mappingColumns = Arrays.asList(new String[] { "mainControl", "classControl", "method", "queryString", "defaultTargetId" });
	public static List<String> mappingColumnsValues = Arrays.asList(new String[] { "mainControl", "classControl", "method", "queryString", "defaultTargetId" });

	protected static PropertyChangeSupport listeners = new PropertyChangeSupport(MapParameters.class);
	public static boolean areConnectionsVisibles = true;

	public static void loadDefault() {
		requestBGColor = FigureUtilities.mixColors(
				ColorConstants.buttonDarker, ColorConstants.button);
		requestFont = new ElementParameters();
		JsFont = new ElementParameters(new Font(null, "Tahoma", 9, SWT.BOLD), ColorConstants.darkGreen);
		ajaxObjectFont = new ElementParameters(new Font(null, "Tahoma", 9, SWT.NORMAL), ColorConstants.darkGray);
		connexionColor = FigureUtilities.mixColors(ColorConstants.darkGreen, ColorConstants.darkGray);
		connexionLineWidth = 1;
		connexionLineStyle = SWT.LINE_DOT;
		highlightColor = new Color(Display.getCurrent(), 203, 232, 246);
		System.out.println(highlightColor);
		highlightDarkColor = new Color(Display.getCurrent(), 38, 160, 218);
		highlightBorder = new LineBorder(MapParameters.highlightDarkColor, connexionLineWidth, Graphics.LINE_SOLID);

		selectedColor = new Color(Display.getCurrent(), 247, 247, 247);
		selectedDarkColor = new Color(Display.getCurrent(), 222, 222, 222);
		selectedBorder = new LineBorder(MapParameters.selectedDarkColor, connexionLineWidth, Graphics.LINE_SOLID);
		mappingColumns = new ArrayList<>(mappingColumnsValues);
		areConnectionsVisibles = true;
	}

	/**
	 * 
	 */
	public static void load() {
		IPreferenceStore myStore = getMyStore();
		IPreferenceStore store = PlatformUI.getWorkbench().getPreferenceStore();
		if (store.contains(REQUEST_BG_COLOR)) {
			Display display = Display.getCurrent();
			requestBGColor = new Color(display, PreferenceConverter.getColor(store, REQUEST_BG_COLOR));
			requestFont.setFont(new Font(display, PreferenceConverter.getFontData(store, REQUEST_FONT)));
			requestFont.setForegroundColor(new Color(display, PreferenceConverter.getColor(store, REQUEST_FONT_COLOR)));
			JsFont.setFont(new Font(display, PreferenceConverter.getFontData(store, JS_FONT)));
			JsFont.setForegroundColor(new Color(display, PreferenceConverter.getColor(store, JS_FONT_COLOR)));
			ajaxObjectFont.setFont(new Font(display, PreferenceConverter.getFontData(store, AJAX_OBJECT_FONT)));
			ajaxObjectFont.setForegroundColor(new Color(display, PreferenceConverter.getColor(store, AJAX_OBJECT_FONT_COLOR)));
			connexionColor = new Color(display, PreferenceConverter.getColor(store, CONNEXION_COLOR));
			connexionLineWidth = myStore.getInt(CONNEXION_LINE_WIDTH);
			connexionLineStyle = myStore.getInt(CONNEXION_LINE_STYLE);
			areConnectionsVisibles = myStore.getBoolean(CONNEXION_VISIBLE);
			highlightColor = new Color(display, PreferenceConverter.getColor(store, CONNEXION_HIGHLIGHT_COLOR));
			highlightDarkColor = new Color(display, PreferenceConverter.getColor(store, CONNEXION_HIGHLIGHT_DARK_COLOR));
			highlightBorder = new LineBorder(highlightDarkColor, connexionLineWidth, Graphics.LINE_SOLID);
			selectedBorder = new LineBorder(selectedDarkColor, connexionLineWidth, Graphics.LINE_SOLID);
			loadMappingColums(myStore);
			AppearenceConfig.defaultJs();
			AppearenceConfig.defaultRequest();
			AppearenceConfig.defaultAjaxObject();
		} else
			loadDefault();
		getListeners().firePropertyChange(REQUEST_BG_COLOR, null, null);
		getListeners().firePropertyChange(ALL, null, null);
	}

	public static void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public static PropertyChangeSupport getListeners() {
		return listeners;
	}

	public static void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public static IPreferenceStore getMyStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static void loadMappingColums(IPreferenceStore store) {
		mappingColumns = Arrays.asList(new String[] { "mainControl", "classControl", "method", "queryString", "defaultTargetId" });
		String strs = store.getString("mappingColumns");
		if (strs != null) {
			mappingColumns = Arrays.asList(strs.split(","));
		}
	}

	public static void saveMappingColumns(List<String> cols) {
		String strs = KStrings.implode(",", cols, "");
		IPreferenceStore myStore = getMyStore();
		myStore.setValue("mappingColumns", strs);
	}

	public static void saveConnexionInfo(int elementLineStyle, int elementLineWidth) {
		IPreferenceStore myStore = getMyStore();
		myStore.setValue(CONNEXION_LINE_STYLE, elementLineStyle);
		myStore.setValue(CONNEXION_LINE_WIDTH, elementLineWidth);
	}
}
