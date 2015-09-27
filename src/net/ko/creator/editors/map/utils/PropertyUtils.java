package net.ko.creator.editors.map.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import net.ko.creator.WorkbenchUtils;
import net.ko.creator.editors.map.model.MoxFile;
import net.ko.kobject.KObject;
import net.ko.mapping.KAbstractFilterMapping;
import net.ko.persistence.annotation.KProcessAnnotation;
import net.ko.utils.KString;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class PropertyUtils {
	public final static String[] events = { "blur", "change", "click", "contextmenu", "copy", "cut",
			"dblclick", "error", "focus", "focusin", "focusout", "keydown", "keypress", "keyup",
			"load", "mousedown", "mouseenter", "mouseleave", "mouseout", "mouseover", "mouseup", "mousewheel",
			"paste", "reset", "resize", "scroll", "select", "unload",
			"listcomplete", "itemchange", "updated", "pagechange" };
	public static String[] controlTypes = { "text", "hidden", "cmb", "list", "datalist", "radio", "checkbox", "date",
			"file", "image", "textarea", "password", "label", "email", "url", "tel", "datetime", "date", "month", "week",
			"time", "datetime-local", "number", "range", "color", "search", "kpagelist", "listform", "checkedlist", "radiolist", "checkeddatalist", "radiodatalist", "checkedajaxlist",
			"radioajaxlist", "listformmany", "readonlytext", "readonlyvalue" };

	private final static List<Integer> ids = new ArrayList<>();

	public static IPropertyDescriptor getPropertyDescriptor(PropertyDescriptor pd, String category) {
		pd.setCategory(category);
		return pd;
	}

	public static void removeProperty(String propertyName, List<IPropertyDescriptor> properties) {
		IPropertyDescriptor result = getProperty(propertyName, properties);
		if (result != null)
			properties.remove(result);
	}

	public static IPropertyDescriptor getProperty(String propertyName, List<IPropertyDescriptor> properties) {
		for (IPropertyDescriptor pd : properties) {
			if (pd.getId().equals(propertyName)) {
				return pd;
			}
		}
		return null;
	}

	public static String[] getURLs(MoxFile moxFile, boolean all) {
		List<String> urls = new ArrayList<>();
		List<String> actualRequests = new ArrayList<>();
		if (!all)
			actualRequests = getRequestURLs(moxFile);
		for (KAbstractFilterMapping mapping : moxFile.getMappings().getItems()) {
			if (actualRequests.indexOf(mapping.getRequestURL()) == -1 || all)
				urls.add(mapping.getRequestURL());
		}
		return urls.toArray(new String[0]);
	}

	public static String[] getURLs(MoxFile moxFile) {
		return getURLs(moxFile, true);
	}

	public static List<String> getRequestURLs(MoxFile moxFile) {
		List<String> urls = new ArrayList<>();
		for (KAbstractFilterMapping mapping : moxFile.getajaxIncludes().getItems()) {
			urls.add(mapping.getRequestURL());
		}
		return urls;
	}

	public static String[] getClasses(MoxFile moxfile) {
		List<String> result = new ArrayList<>();
		IProject project = WorkbenchUtils.getActiveProject();
		IFolder folder = project.getFolder("src/" + WorkbenchUtils.packageToFolder(moxfile.getPackageName()));
		IResource resources[];
		if (folder.exists()) {
			try {
				resources = folder.members();
				for (int i = 0; i < resources.length; i++) {
					if (resources[i].exists()) {
						if (resources[i].getFileExtension() != null && resources[i].getFileExtension().equals("java")) {
							String simpleName = resources[i].getName();
							simpleName = KString.getBefore(simpleName, ".");
							result.add(simpleName);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result.toArray(new String[0]);
	}

	private static List<Class> getKClasses(MoxFile moxfile) {
		ArrayList<Class> classes = new ArrayList<Class>();
		try {
			String packageName = moxfile.getPackageName();
			ClassLoader classLoader = WorkbenchUtils.getURLClassPathLoader(WorkbenchUtils.getJavaProject());
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(KString.urlDecode(resource.getFile())));
			}
			for (File directory : dirs) {
				classes.addAll(findClasses(directory, packageName, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<Class> findClasses(File directory, String packageName, boolean recursive) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory() && recursive) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName(), recursive));
			} else if (file.getName().endsWith(".class")) {
				Class clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				if (KObject.class.isAssignableFrom(clazz)) {
					if (KProcessAnnotation.isEntity((Class<? extends KObject>) clazz))
						classes.add(clazz);
				}
			}
		}
		return classes;
	}

	public static int getRandomId() {
		int id = 0;
		do {
			Random r = new Random();
			double randomValue = 1 + (1000 - 1) * r.nextDouble();
			id = (int) Math.round(randomValue);
		} while (ids.indexOf(Integer.valueOf(id)) != -1);
		ids.add(Integer.valueOf(id));
		return id;
	}
}
