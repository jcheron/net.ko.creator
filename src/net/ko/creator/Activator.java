package net.ko.creator;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.editors.map.MapParameters;
import net.ko.creator.editors.map.listeners.WorkspaceListener;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "net.ko.creator"; //$NON-NLS-1$
	public static final String ICON_TEMPLATE = "/icons/tpl.png";
	public final static String VERSION = "1.0.0.25f";
	public final static List<IWorkbenchPartReference> parts = new ArrayList<>();

	// The shared instance
	private static Activator plugin;
	private WorkspaceListener listener;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		System.setProperty("file.encoding", "UTF-8");
		MapParameters.load();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		listener = new WorkspaceListener();
		workspace.addResourceChangeListener(listener);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(
				new IPartListener2() {
					public void partOpened(IWorkbenchPartReference ref) {
						parts.add(ref);
					}

					public void partClosed(IWorkbenchPartReference ref) {
						parts.remove(ref);
					}

					/*
					 * you'll need to implement or stub out the other methods of
					 * IPartListener2
					 */
					public void partActivated(IWorkbenchPartReference ref) {
					}

					public void partDeactivated(IWorkbenchPartReference ref) {
					}

					/* etc */

					@Override
					public void partBroughtToTop(IWorkbenchPartReference arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partHidden(IWorkbenchPartReference arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partInputChanged(IWorkbenchPartReference arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partVisible(IWorkbenchPartReference arg0) {
						// TODO Auto-generated method stub

					}

				}
				);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
