package net.ko.creator.editors.map.commands;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.creator.editors.map.model.MoxFile;
import net.ko.creator.editors.map.utils.PropertyUtils;
import net.ko.mapping.KAjaxRequest;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class AjaxRequestCreateCommand extends Command {
	private MoxFile moxFile;
	private AjaxRequest ajaxRequest;

	public void setMoxFile(Object obj) {
		if (obj instanceof MoxFile)
			this.moxFile = (MoxFile) obj;
	}

	public void setAjaxRequest(Object obj) {
		if (obj instanceof AjaxRequest)
			this.ajaxRequest = (AjaxRequest) obj;
	}

	public void setLayout(Rectangle r) {
		if (ajaxRequest == null)
			return;
		ajaxRequest.setLayout(r);
	}

	@Override
	public boolean canExecute() {
		if (moxFile == null || ajaxRequest == null)
			return false;
		return true;
	}

	@Override
	public void execute() {
		boolean exists = false;
		if (ajaxRequest.getAjaxRequest().getRequestURL().equals("newURL.do")) {
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), new ILabelProvider() {

				@Override
				public void removeListener(ILabelProviderListener arg0) {
				}

				@Override
				public boolean isLabelProperty(Object arg0, String arg1) {
					return false;
				}

				@Override
				public void dispose() {
				}

				@Override
				public void addListener(ILabelProviderListener arg0) {
				}

				@Override
				public String getText(Object obj) {
					return obj + "";
				}

				@Override
				public Image getImage(Object arg0) {
					return Images.getImage(Images.MAPPING);
				}
			});
			dialog.setTitle("Ajout d'une inclusion Request");
			dialog.setMessage("Sélectionner une URL");
			dialog.setElements(PropertyUtils.getURLs(moxFile, false));
			dialog.open();
			if (dialog.getFirstResult() != null) {
				ajaxRequest.setAjaxRequest(new KAjaxRequest(dialog.getFirstResult() + ""));
				exists = true;
			}
		} else {
			exists = true;
		}
		if (moxFile.contains(ajaxRequest)) {
			AjaxRequest ajxR = moxFile.getAjaxRequest(ajaxRequest);
			ajxR.setVisible(true);
			ajxR.setFocus();
		} else {
			moxFile.addChildAndFire(ajaxRequest);
			if (exists)
				ajaxRequest.refreshChildren();
		}
	}

	@Override
	public boolean canUndo() {
		if (moxFile == null || ajaxRequest == null)
			return false;
		return moxFile.contains(ajaxRequest);
	}

	@Override
	public void undo() {
		moxFile.removeChild(ajaxRequest);
	}
}
