package net.ko.creator.editors.images;

import net.ko.creator.Activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;

public class Images {

	public static String BASE_FOLDER = "/net/ko/creator/editors/images/";
	public static String PLUGIN = BASE_FOLDER + "plugin.png";
	public static String KO_LOGO = BASE_FOLDER + "logo-kobject-java.png";
	public static String VALIDATION = BASE_FOLDER + "validation.png";
	public static String CONTROLLER = BASE_FOLDER + "controller.png";
	public static String CSS = BASE_FOLDER + "css.png";
	public static String TPL = BASE_FOLDER + "tpl.png";
	public static String EXCLAMATION = BASE_FOLDER + "exclamation.png";
	public static String SUCCESS = BASE_FOLDER + "success.png";
	public static String DB_CONNECT = BASE_FOLDER + "dbConnect.png";
	public static String ORM = BASE_FOLDER + "orm.png";
	public static String CHECK = BASE_FOLDER + "check.png";
	public static String DB_CACHE = BASE_FOLDER + "dbCache.png";
	public static String DB_TABLE = BASE_FOLDER + "dbTable.png";
	public static String BULLET_BLUE = BASE_FOLDER + "bullet_blue.png";
	public static String BULLET_GRAY = BASE_FOLDER + "bullet_gray.png";
	public static String BULLET = BASE_FOLDER + "bullet_green.png";
	public static final String PACKAGE = BASE_FOLDER + "package.png";
	public static final String XML = BASE_FOLDER + "xml.png";
	public static final String KO_CONFIG = BASE_FOLDER + "koConfig.png";
	public static final String CLASS = BASE_FOLDER + "classe.png";
	public static final String MAPPING = BASE_FOLDER + "mapping.png";
	public static final String VIRTUAL_MAPPING = BASE_FOLDER + "virtualMapping.png";
	public static final String CHECKED_YES = BASE_FOLDER + "checkbox_yes.png";
	public static final String CHECKED_NO = BASE_FOLDER + "checkbox_no.png";
	public static final String VISIBLE = BASE_FOLDER + "visible.png";
	public static final String COLLAPSE = BASE_FOLDER + "collapse.png";
	public static final String EXPAND = BASE_FOLDER + "expand.png";

	public static final String DIALOGS = BASE_FOLDER + "map/dialogs.png";
	public static final String CONNECTORS = BASE_FOLDER + "map/connectors.png";
	public static final String ELEMENTS = BASE_FOLDER + "map/elements.png";

	public static final String REQUEST = BASE_FOLDER + "map/request.png";
	public static final String JS = BASE_FOLDER + "map/js.png";
	public static final String INCLUDE = BASE_FOLDER + "map/include.png";
	public static final String MESSAGE = BASE_FOLDER + "map/message.png";
	public static final String MESSAGE_DIALOG = BASE_FOLDER + "map/messageDialog.png";
	public static final String SELECTOR = BASE_FOLDER + "map/selector.png";
	public static final String FIRE_EVENT = BASE_FOLDER + "map/fireEvent.png";
	public static final String CONNECTION = BASE_FOLDER + "map/connection.png";
	public static final String UPDATE_ONE = BASE_FOLDER + "map/updateOne.png";
	public static final String UPDATE_ONE_FIELD = BASE_FOLDER + "map/oneField.png";

	public static final String DELETE_ONE = BASE_FOLDER + "map/deleteOne.png";
	public static final String DELETE_MULTI = BASE_FOLDER + "map/deleteMulti.png";
	public static final String FUNCTION = BASE_FOLDER + "map/function.png";
	public static final String INCLUDE_DIALOG = BASE_FOLDER + "map/includeDialog.png";
	public static final String SHOW_HIDE = BASE_FOLDER + "map/showhide.png";
	public static final String SUBMIT_FORM = BASE_FOLDER + "map/submitForm.png";
	public static final String REFRESH_FORM_VALUES = BASE_FOLDER + "map/refreshFormValues.png";
	public static final String REFRESH_CONTROL = BASE_FOLDER + "map/refreshControl.png";
	public static final String ACCORDION = BASE_FOLDER + "map/accordion.png";
	public static final String DIALOG_BUTTON = BASE_FOLDER + "map/buttons.png";
	public static final String TRANSITION = BASE_FOLDER + "map/transition.png";
	public static final String ONE_TRANSITION = BASE_FOLDER + "map/oneTransition.png";

	public static Image getImage(String image) {
		return SWTResourceManager.getImage(Activator.class, image);
	}

	public static ImageDescriptor getImageDescriptor(String image) {
		return ImageDescriptor.createFromImage(getImage(image));
	}
}
