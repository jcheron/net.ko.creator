package net.ko.creator;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class KJavaProject {
	public static List<IJavaProject> getAllProjects() throws Exception
	{
		List<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			project.open(null /* IProgressMonitor */);
			IJavaProject javaProject = JavaCore.create(project);
			javaProjects.add(javaProject);
		}
		return javaProjects;
	}

	public static Object getInstance(String fqClassname) throws Exception
	{
		List<URLClassLoader> loaders = new ArrayList<URLClassLoader>();
		List<IJavaProject> javaProjects = getAllProjects();
		for (IJavaProject proj : javaProjects) {
			URL urls[] = getClasspathAsURLArray(proj);
			loaders.add(new URLClassLoader(urls));
		}
		
		for (URLClassLoader loader : loaders) {
			try {
				Class<?> clazz = loader.loadClass(fqClassname);
				if (!clazz.isInterface()  && !clazz.isEnum()) {
					return clazz.newInstance();
				} else {
					return clazz;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static Object getInstanceFromOutput(String fqClassname,IProject project){
		return getInstanceFromOutput(fqClassname, getJavaProject(project));
	}
	public static Object getInstanceFromOutput(String fqClassname,IJavaProject javaProject){
		Object result=null;
		IPath outPath = getJavaProjectOutputAbsoluteLocation(javaProject.getProject());
		outPath = outPath.addTrailingSeparator();
		URL out2 = createFileURL(outPath);
		
		try {
			URL out=new URL("file:\\D:\\workspaces\\runtime-EclipseApplication\\test\\bin\\");
			URL[] urls=new URL[]{out};
			ClassLoader cl = new URLClassLoader(urls);
			Class<?> cls = cl.loadClass(fqClassname);
			result=cls.newInstance();
		}
		    catch (Exception e) {
		    	e.printStackTrace();
		}
		return result;
	}
	public static Class<?> getClassInstance(String fqClassname,IProject project) throws Exception{
		return getClassInstance(fqClassname, getJavaProject(project));
	}
	public static Class<?> getClassInstance(String fqClassname,IJavaProject javaProject) throws Exception{
		URL urls[] = getClasspathAsURLArray(javaProject);
		URLClassLoader loader=new URLClassLoader(urls);
			try {
				Class<?> clazz = loader.loadClass(fqClassname);
				return clazz;
			} catch (ClassNotFoundException e) {
			}
		return null;
	}
	public static Object getInstance(String fqClassname,IProject project) throws Exception{
		return getInstance(fqClassname, getJavaProject(project));
	}
	public static Object getInstance(String fqClassname,IJavaProject javaProject) throws Exception
	{
			try {
				Class<?> clazz =getClassInstance(fqClassname, javaProject);
				if (!clazz.isInterface()  && !clazz.isEnum()) {
					return clazz.newInstance();
				} else {
					return clazz;
				}
			} catch (ClassNotFoundException e) {
			}
		return null;
	}

	public static URL[] getClasspathAsURLArray(IJavaProject javaProject)
	{
		if (javaProject == null)
			return null;
		Set<IJavaProject> visited = new HashSet<IJavaProject>();
		List<URL> urls = new ArrayList<URL>(20);
		collectClasspathURLs(javaProject, urls, visited, true);
		URL[] result = new URL[urls.size()];
		urls.toArray(result);
		return result;
	}

	private static void collectClasspathURLs(IJavaProject javaProject, List<URL> urls, Set<IJavaProject> visited,
			boolean isFirstProject)
	{
		if (visited.contains(javaProject))
			return;
		visited.add(javaProject);
		IPath outPath = getJavaProjectOutputAbsoluteLocation(javaProject.getProject());
		outPath = outPath.addTrailingSeparator();
		URL out = createFileURL(outPath);
		urls.add(out);
		IClasspathEntry[] entries = null;
		try {
			entries = javaProject.getResolvedClasspath(true);
		} catch (JavaModelException e) {
			return;
		}
		IClasspathEntry entry;
		for (int i = 0; i < entries.length; i++) {
			entry = entries[i];
			switch (entry.getEntryKind()) {
			case IClasspathEntry.CPE_LIBRARY:
			case IClasspathEntry.CPE_CONTAINER:
			case IClasspathEntry.CPE_VARIABLE:
				collectClasspathEntryURL(entry, urls);
				break;
			case IClasspathEntry.CPE_PROJECT: {
				if (isFirstProject || entry.isExported())

					collectClasspathURLs(getJavaProject(entry), urls, visited, false);

				break;
			}
			}
		}
	}

	private static URL createFileURL(IPath path)
	{
		URL url = null;
		try {
			url = new URL("file:/" + path.toOSString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	private static void collectClasspathEntryURL(IClasspathEntry entry, List<URL> urls)
	{
		URL url = createFileURL(entry.getPath());
		if (url != null)
			urls.add(url);
	}

	private static IJavaProject getJavaProject(IClasspathEntry entry)
	{
		IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(entry.getPath().segment(0));
		if (proj != null)
			return getJavaProject(proj);
		return null;
	}

	public static IJavaProject getJavaProject(IProject p)
	{
		try {
			return (IJavaProject) p.getNature(JavaCore.NATURE_ID);
		} catch (CoreException ignore) {
			return null;
		}
	}

	public static IPath getJavaProjectOutputAbsoluteLocation(IProject p)
	{
		IContainer container = getJavaProjectOutputContainer(p);
		if (container != null)
			return container.getLocation();
		return null;
	}
	
	public static IContainer getJavaProjectOutputContainer(IProject p) {
		IPath path = getJavaProjectOutputLocation(p);
		if (path == null)
			return null;
		if (path.segmentCount() == 1)
			return p;
		return p.getFolder(path.removeFirstSegments(1));
	}
	

	public static IPath getJavaProjectOutputLocation(IProject p) {
		try {
			IJavaProject javaProj = getJavaProject(p);
			if (javaProj == null)
				return null;
			if (!javaProj.isOpen())
				javaProj.open(null);
			return javaProj.getOutputLocation();
		} catch (JavaModelException e) {
			return null;
		}
	}
}
