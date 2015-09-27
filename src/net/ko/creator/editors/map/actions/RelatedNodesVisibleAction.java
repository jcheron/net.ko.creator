package net.ko.creator.editors.map.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.part.AppAbstractEditPart;
import net.ko.creator.editors.map.part.tree.AppTreeEditPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;

public class RelatedNodesVisibleAction extends SelectionAction {
	public static String ID_VISIBLE = "map.node.relatedNodesVisibles";
	public static String ID_INVISIBLE = "map.node.relatedNodesInvisibles";

	protected boolean visible;

	public RelatedNodesVisibleAction(IWorkbenchPart part, boolean visible) {
		super(part);
		setLazyEnablementCalculation(false);
		// setChecked(true);
		this.visible = visible;
		if (visible) {
			setId(ID_VISIBLE);
			setText("Afficher les inclusions ajax associées");
		}
		else {
			setId(ID_INVISIBLE);
			setText("Masquer les inclusions ajax associées");
		}
	}

	@Override
	protected void init() {
		setToolTipText("Afficher/masquer les inclusions ajax associées");
		ImageDescriptor icon = Images.getImageDescriptor(Images.VISIBLE);
		if (icon != null)
			setImageDescriptor(icon);
		setEnabled(false);
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	public Command createRelatedNodesVisiblesCommand(EditPart part) {
		Command cmd = null;
		Request visibleReq = new Request(getId());

		HashMap<String, Boolean> reqData = new HashMap<>();
		reqData.put("visible", visible);
		visibleReq.setExtendedData(reqData);

		if (part != null && (part instanceof AppAbstractEditPart || part instanceof AppTreeEditPart)) {
			cmd = part.getCommand(visibleReq);
		}
		return cmd;
	}

	public void run() {
		List<EditPart> selectedParts = getSelectedParts();
		for (EditPart part : selectedParts) {
			if ((part instanceof AppAbstractEditPart || part instanceof AppTreeEditPart)) {
				execute(createRelatedNodesVisiblesCommand(part));
			}
		}
	}

	private List<EditPart> getSelectedParts() {
		List objects = getSelectedObjects();
		List<EditPart> result = new ArrayList<>();
		for (Object o : objects) {
			if (o instanceof EditPart)
				result.add((EditPart) o);
		}
		return result;
	}
}
