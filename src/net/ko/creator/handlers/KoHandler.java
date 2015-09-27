package net.ko.creator.handlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

import net.ko.creator.Activator;
import net.ko.creator.editors.MyElementTreeSelectionDialog;
import net.ko.creator.utils.KZip;
import net.ko.utils.KTextFile;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.osgi.framework.Bundle;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class KoHandler extends AbstractHandler {
	private static IProject project;

	/**
	 * The constructor.
	 */
	public KoHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MyElementTreeSelectionDialog dialog = new MyElementTreeSelectionDialog(window.getShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
		dialog.setTitle("Add KObject Facet");
		dialog.setMessage("Sélectionner un projet java :");
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setAllowMultiple(false);
		dialog.addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IProject) {
					IProject p = (IProject) element;
					try {
						if (p.hasNature("org.eclipse.jdt.core.javanature")) {
							return true;
						}
					} catch (CoreException e1) {
						return false;
					}
				}
				return false;
			}
		});
		project = null;
		if (dialog.open() == ResourceSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			project = (IProject) result[0];
			String folder = "/";
			boolean isWebApp = false;
			try {
				Properties newProp = new Properties();
				// if(project.hasNature("org.eclipse.wst.common.modulecore.ModuleCoreNature")){
				IFolder webFolder = project.getFolder("/WebContent/");
				if (webFolder.exists()) {
					isWebApp = true;
					newProp.put("webApp", "true");
					folder = "/WebContent/";
					IFolder logFolder = project.getFolder("/WebContent/WEB-INF/log/");
					if (!logFolder.exists())
						logFolder.create(IResource.FORCE, true, null);
				}
				IFile file = project.getFile(folder + "config.ko");
				if (dialog.isCacheFileSelected()) {
					copyFileFromJarToProject(project, "net/ko/cache", "ehcache-fs.xml", "/src/ehcache.xml", true);
				}
				if (dialog.isErFileSelected()) {
					copyFileFromJarToProject(project, "net/ko/controller", "er.properties", folder + "conf/validation/er.properties", true);
					newProp.put("erFile", "conf/validation/er.properties");
				}
				if (dialog.isMessagesFileSelected()) {
					copyFileFromJarToProject(project, "net/ko/controller", "messages.properties", folder + "conf/validation/messages.properties", true);
					newProp.put("messagesFile", "conf/validation/messages.properties");
				}

				if (isWebApp) {
					if (dialog.isControllerFileSelected()) {
						copyFileFromJarToProject(project, "net/ko/mapping", "mox.xml", folder + "conf/mox.xml", true);
						newProp.put("mappingFile", "conf/mox.xml");
					}
					if (dialog.isCssHTMLFileSelected()) {
						newProp.put("cssFile", "css/css.properties");
						if (copyFileFromJarToProject(project, "net/ko/http/files", "header.jsp", folder + "WEB-INF/header.jsp", false))
							newProp.put("headerURL", "WEB-INF/header.jsp");
						if (copyFileFromJarToProject(project, "net/ko/http/files", "footer.jsp", folder + "WEB-INF/footer.jsp", false))
							newProp.put("footerURL", "WEB-INF/footer.jsp");

						String zipDest = project.getLocationURI().getPath().toString() + folder + "/";
						KZip.unzipFromRessource("net/ko/http/files", "webContent.zip", zipDest);
					}
				}
				if (!file.exists()) {
					newProp.put("host", "127.0.0.1");
					newProp.put("port", "3306");
					newProp.put("user", "root");
					newProp.put("package", "net.kernel");
					newProp.put("sqlDateFormat", "yyyy-MM-dd");
					newProp.put("koDateFormat", "dd/MM/yyyy");
					newProp.put("dbType", "mysql");
					newProp.put("nullValue", "&nbsp;");
					newProp.put("debug", "true");
					newProp.put("controlClass", "");
					newProp.put("validationFile", "");
					newProp.put("useSetters", "false");
					newProp.put("constraintsDepth", "1");
					newProp.put("useCache", "false");
					newProp.put("cacheType", "1");
					newProp.put("useLog", "true");
					newProp.put("logFormat", "html");
					newProp.put("actionsName", "action.do");
					newProp.put("schema.ddl", "validate");
					newProp.put("clientDebug", "false");
					newProp.put("clientDebugOptions", "true");
					newProp.put("pluginVersion", Activator.VERSION);
				}
				newProp = updatePropertyFile(file, newProp);
				InputStream oIn = new ByteArrayInputStream(propertiesToString(newProp).getBytes(Charset.defaultCharset()));
				if (!file.exists())
					file.create(oIn, IFile.FORCE, null);
				else
					file.setContents(oIn, IFile.KEEP_HISTORY, null);
				// file.refreshLocal(IResource.DEPTH_ZERO, null);
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
				// file.refreshLocal(IResource.DEPTH_ZERO, null);
				// Display.getDefault().syncExec(new Runnable(){
				// public void run(){
				// try {
				// project.refreshLocal(IResource.DEPTH_INFINITE, null);
				// } catch (CoreException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				// });
				openEditor(file, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	protected Properties updatePropertyFile(IFile file, Properties newProp) {
		if (file.exists()) {
			Properties actualProp = new Properties();
			try {
				actualProp.load(file.getContents());
				newProp.putAll(actualProp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return newProp;
	}

	protected boolean copyFileFromJarToProject(IProject project, String srcLocationInJar, String srcFileName, String destCompleteFileName, boolean force) {
		IFile theFile = project.getFile(destCompleteFileName);
		boolean result = false;
		if (!theFile.exists() || force) {
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			Path path = new Path(srcLocationInJar);
			URL fileURL = FileLocator.find(bundle, path, null);
			String fileContent = "";
			try {
				File dir = new File(theFile.getLocation().removeLastSegments(1).toString());
				if (!dir.exists())
					dir.mkdirs();
				fileContent = KTextFile.openRessource(fileURL.toURI().toString() + srcFileName);
				KTextFile.save(project.getLocationURI().getPath().toString() + destCompleteFileName, fileContent);
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result = false;
				e.printStackTrace();
			}
		}
		return result;
	}

	protected void openEditor(IFile file, String editorID) throws PartInitException {
		IEditorRegistry editorRegistry = PlatformUI.getWorkbench().getEditorRegistry();
		if (editorID == null || editorRegistry.findEditor(editorID) == null) {
			editorID = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getFullPath().toString()).getId();
		}

		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IDE.openEditor(page, file);
	}

	private String propertiesToString(Properties prop) {
		String result = "";
		for (Map.Entry<Object, Object> e : prop.entrySet())
			result += e.getKey() + "=" + e.getValue() + "\n";
		return result;
	}
}
