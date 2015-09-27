package net.ko.creator.editors.map;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.part.AjaxIncludePart;
import net.ko.creator.editors.map.part.AjaxMessageDialogPart;
import net.ko.creator.editors.map.part.AjaxObjectPart;
import net.ko.creator.editors.map.part.AjaxRequestPart;
import net.ko.creator.editors.map.part.ConnectionPart;
import net.ko.creator.editors.map.part.CssTransitionPart;
import net.ko.creator.editors.map.part.JsPart;
import net.ko.creator.editors.map.part.MoxFilePart;
import net.ko.creator.editors.map.part.TogglePart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class AppEditorPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		AbstractGraphicalEditPart part = null;
		String modelClass = model.getClass().getSimpleName();
		switch (modelClass) {
		case "MoxFile":
			part = new MoxFilePart();
			break;
		case "JS":
			part = new JsPart();
			break;
		case "AjaxRequest":
			part = new AjaxRequestPart();
			break;
		case "AjaxInclude":
			part = new AjaxIncludePart(Images.INCLUDE);
			break;
		case "AjaxMessage":
			part = new AjaxObjectPart(Images.MESSAGE);
			break;

		case "AjaxSelector":
			part = new AjaxObjectPart(Images.SELECTOR);
			break;

		case "AjaxFireEvent":
			part = new AjaxObjectPart(Images.FIRE_EVENT);
			break;

		case "Connection":
			part = new ConnectionPart();
			break;

		case "ToggleNode":
			part = new TogglePart();
			break;

		case "AjaxDeleteOne":
			part = new AjaxObjectPart(Images.DELETE_ONE);
			break;

		case "AjaxDeleteMulti":
			part = new AjaxObjectPart(Images.DELETE_MULTI);
			break;

		case "AjaxUpdateOne":
			part = new AjaxObjectPart(Images.UPDATE_ONE);
			break;

		case "AjaxUpdateOneField":
			part = new AjaxObjectPart(Images.UPDATE_ONE_FIELD);
			break;

		case "AjaxShowhide":
			part = new AjaxObjectPart(Images.SHOW_HIDE);
			break;

		case "AjaxFunction":
			part = new AjaxObjectPart(Images.FUNCTION);
			break;

		case "AjaxSubmitForm":
			part = new AjaxObjectPart(Images.SUBMIT_FORM);
			break;

		case "AjaxRefreshControl":
			part = new AjaxObjectPart(Images.REFRESH_CONTROL);
			break;

		case "AjaxRefreshFormValues":
			part = new AjaxObjectPart(Images.REFRESH_FORM_VALUES);
			break;

		case "AjaxAccordion":
			part = new AjaxObjectPart(Images.ACCORDION);
			break;

		case "AjaxMessageDialog":
			part = new AjaxMessageDialogPart(Images.MESSAGE_DIALOG);
			break;

		case "AjaxDialogButton":
			part = new AjaxObjectPart(Images.DIALOG_BUTTON);
			break;
		case "AjaxIncludeDialog":
			part = new AjaxObjectPart(Images.INCLUDE_DIALOG);
			break;

		case "CssTransition":
			part = new CssTransitionPart(Images.TRANSITION);
			break;

		case "CssOneTransition":
			part = new AjaxObjectPart(Images.ONE_TRANSITION);
			break;

		default:
			break;
		}

		part.setModel(model);
		return part;
	}
}
