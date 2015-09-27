package net.ko.creator.editors.map.listeners;

import java.util.ArrayList;

import net.ko.creator.Activator;
import net.ko.creator.WorkbenchUtils;
import net.ko.creator.editors.map.MapEditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPartReference;

public class WorkspaceListener implements IResourceChangeListener {

	public void resourceChanged(IResourceChangeEvent event) {
		IPath docPath = WorkbenchUtils.getActiveProject().getProjectRelativePath();
		// we are only interested in POST_CHANGE events
		if (event.getType() != IResourceChangeEvent.POST_CHANGE)
			return;
		IResourceDelta rootDelta = event.getDelta();
		// get the delta, if any, for the documentation directory
		IResourceDelta docDelta = rootDelta.findMember(docPath);
		if (docDelta == null)
			return;
		final ArrayList<IResource> changed = new ArrayList();
		IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
			public boolean visit(IResourceDelta delta) {
				// only interested in changed resources (not added or removed)
				if (delta.getKind() != IResourceDelta.CHANGED)
					return true;
				// only interested in content changes
				if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
					return true;
				IResource resource = delta.getResource();
				// only interested in files with the "txt" extension
				if (resource.getType() == IResource.FILE &&
						"xml".equalsIgnoreCase(resource.getFileExtension())) {
					changed.add(resource);
				}
				return true;
			}
		};
		try {
			docDelta.accept(visitor);
		} catch (CoreException e) {
			// open error dialog with syncExec or print to plugin log file
		}
		// nothing more to do if there were no changed text files
		if (changed.size() == 0)
			return;
		// post this update to the table
		for (IResource fileResource : changed) {
			for (IWorkbenchPartReference part : Activator.parts) {
				IEditorPart editorPart = getEditor(part);

				if (editorPart instanceof MapEditor) {
					MapEditor mapEditor = (MapEditor) editorPart;
					IFile file = (IFile) fileResource;
					if (file.getProjectRelativePath().toString().contains(mapEditor.getXmlFileName())) {
						try {
							file.refreshLocal(IResource.DEPTH_ZERO, null);
							mapEditor.refreshInput();
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private IEditorPart getEditor(IWorkbenchPartReference partRef) {
		if (partRef instanceof IEditorReference) {
			return ((IEditorReference) partRef).getEditor(false);
		}
		return null;
	}
}