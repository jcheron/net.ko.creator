package net.ko.creator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class WorkbenchUtils {

	public static IProject getActiveProject(IEditorPart editor) {
		IProject project = null;
		if (editor != null) {
			IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);
			project = resource.getProject();
		}
		return project;
	}

	public static IEditorPart getActiveEditor() {
		IEditorPart result = null;
		IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (win != null) {
			IWorkbenchPage page = win.getActivePage();
			if (page != null)
				result = page.getActiveEditor();
		}
		return result;
	}

	public static IContainer getSelectedContainer() {
		ISelection select = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		IContainer result = null;
		if (select instanceof IStructuredSelection) {
			IStructuredSelection sselect = (IStructuredSelection) select;
			Object obj = sselect.getFirstElement();
			if (obj != null) {
				result = (IContainer) Platform.getAdapterManager().getAdapter(obj, IContainer.class);
				if (result == null) {
					if (obj instanceof IAdaptable) {
						result = (IContainer) ((IAdaptable) obj).getAdapter(IContainer.class);
					}
					if (result == null) {
						if (obj instanceof IPackageFragmentRoot)
							result = (IContainer) ((IPackageFragmentRoot) obj).getParent();
					}
				}
			}
		}
		return result;
	}

	public static IProject getActiveProject() {
		IProject result = null;
		try {
			IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if (editor != null)
				result = getActiveProject(editor);
			else {
				ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();

				ISelection selection = selectionService.getSelection();

				if (selection instanceof IStructuredSelection) {
					Object element = ((IStructuredSelection) selection).getFirstElement();

					if (element instanceof IResource) {
						result = ((IResource) element).getProject();
					} else if (element instanceof PackageFragmentRootContainer) {
						IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
						result = jProject.getProject();
					}
					else if (element instanceof IJavaElement) {
						IJavaProject jProject = ((IJavaElement) element).getJavaProject();
						result = jProject.getProject();
					} else {
						if (!(element instanceof IAdaptable))
							return null;
						IAdaptable adaptable = (IAdaptable) element;
						Object adapter = adaptable.getAdapter(IResource.class);
						if (adapter != null) {
							IResource res = (IResource) adapter;
							return res.getProject();
						} else {
							Object adapter2 = adaptable.getAdapter(IJavaProject.class);
							if (adapter2 != null) {
								Project res = (Project) adapter2;
								return res.getProject();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static IProject getProject(String folderName) {
		IProject result = null;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		try {
			IFolder iFolder = root.getFolder(new Path(folderName));
			if (iFolder.exists()) {
				result = iFolder.getProject();
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static URLClassLoader getURLClassPathLoader(IJavaProject project) throws CoreException, MalformedURLException {
		String[] classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(project);
		List<URL> urlList = new ArrayList<URL>();
		for (int i = 0; i < classPathEntries.length; i++) {
			String entry = classPathEntries[i];
			IPath path = new Path(entry);
			URL url = url = path.toFile().toURI().toURL();
			urlList.add(url);
		}
		ClassLoader parentClassLoader = project.getClass().getClassLoader();
		URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
		URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);
		return classLoader;
	}

	public static IFolder getAbsoluteFolder(String folderName) {
		IFolder result = null;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		try {
			result = root.getFolder(new Path(folderName));
			if (!result.exists())
				result = null;
		} catch (Exception e) {
		}
		return result;
	}

	public static boolean isDynamicWebProject(IProject project) {
		try {
			IFolder folder = project.getFolder("/WebContent/");
			return folder.exists();
			// return project.hasNature("org.eclipse.wst.jsdt.core.jsNature");
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDynamicWebProject() {
		return isDynamicWebProject(getActiveProject());
	}

	public static String packageToFolder(String pack) {
		return pack.replace(".", "/");
	}

	public static IFolder getFolder(String path) {
		IFolder folder = null;
		IProject project = getActiveProject();
		if (project != null)
			folder = project.getFolder(path);
		return folder;
	}

	public static IJavaProject getJavaProject(IEditorPart editor) {
		return JavaCore.create(getActiveProject(editor));
	}

	public static IJavaProject getJavaProject() {
		return JavaCore.create(getActiveProject());
	}

	public static void addClassPathEntry(String path) {
		IJavaProject jp = getJavaProject();
		if (jp != null) {
			try {
				IClasspathEntry cpath[] = jp.getRawClasspath();
				IClasspathEntry libEntry = JavaCore.newLibraryEntry(new Path(path), // library
																					// location
						null, // source archive location
						new Path("src"), // source archive root path
						true); // exported
				IClasspathEntry newCpath[];
				int index = getKobjectEntryIndex();
				if (index == -1) {
					newCpath = new IClasspathEntry[cpath.length + 1];
					System.arraycopy(cpath, 0, newCpath, 0, cpath.length);
					newCpath[cpath.length] = libEntry;
				} else {
					newCpath = new IClasspathEntry[cpath.length];
					System.arraycopy(cpath, 0, newCpath, 0, cpath.length);
					newCpath[index] = libEntry;
				}
				jp.setRawClasspath(newCpath, null);
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static int getKobjectEntryIndex() {
		IJavaProject jp = getJavaProject();
		boolean trouve = false;
		int i = 0;
		if (jp != null) {
			try {
				IClasspathEntry cpath[] = jp.getRawClasspath();
				while (i < cpath.length && !trouve) {
					if (cpath[i].getPath().toFile().getName().contains("koLibrary"))
						trouve = true;
					else
						i++;
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (trouve)
			return i;
		else
			return -1;
	}

	public static Object getKobjectEntry(IEditorPart editor) {
		IJavaProject jp = getJavaProject(editor);
		Object result = null;
		int i = 0;
		if (jp != null) {
			try {
				IClasspathEntry cpath[] = jp.getRawClasspath();
				while (i < cpath.length && result == null) {
					if (cpath[i].getPath().toPortableString().contains("koLibrary"))
						result = cpath[i];
					else {
						IPackageFragmentRoot[] pkgFragmentRoots = jp.findPackageFragmentRoots(cpath[i]);
						if (pkgFragmentRoots != null) {
							for (IPackageFragmentRoot pfr : pkgFragmentRoots) {
								if (pfr.getPath().toPortableString().contains("koLibrary")) {
									result = pfr;
									break;
								}
							}
						}
					}
					i++;
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String getKobjectEntryName(IEditorPart editor) {
		String result = "";
		Object cpE = getKobjectEntry(editor);
		if (cpE != null) {
			if (cpE instanceof IClasspathEntry)
				result = ((IClasspathEntry) cpE).getPath().toFile().getName();
			else if (cpE instanceof IPackageFragmentRoot)
				result = ((IPackageFragmentRoot) cpE).getPath().toFile().getName();
		}
		return result;
	}

	public static boolean classExists(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException exception) {
			return false;
		}
	}

	public static boolean classExists(String className, ClassLoader classLoader) {
		try {
			Class.forName(className, false, classLoader);
			return true;
		} catch (ClassNotFoundException exception) {
			return false;
		}
	}

	public static boolean packageExists(String name) {
		return Package.getPackage(name) != null;
	}

	public static IAction getAction(String idAction) {
		IAction result = null;
		try {
			// IEditorSite site=(IEditorSite)
			// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite();
			IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if (editor != null) {
				IEditorSite site = editor.getEditorSite();
				result = site.getActionBars().getGlobalActionHandler(idAction);
			}
		} catch (Exception ex) {
		}
		return result;
	}

	public static String pathSeparator() {
		return File.separator;
	}

	public static String cPath(String path) {
		if (path != null && !"".equals(path))
			return path.replace("\\", File.separator).replace("/", File.separator);
		else
			return "";
	}
}
