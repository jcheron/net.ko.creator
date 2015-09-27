package net.ko.creator.editors.map.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.part.AppAbstractEditPart;
import net.ko.creator.editors.map.part.tree.AppTreeEditPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;

public class VisibleAction extends SelectionAction {
	private enum VisibleType {
		ALL_VISIBLES, ALL_INVISIBLES, INV_OR_VISIBLES, NONE
	};

	public static String ID = "map.node.visible";

	public VisibleAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
		setChecked(true);
	}

	@Override
	protected void init() {
		setToolTipText("Afficher/masquer l'inclusion ajax");
		setId(ID);

		// Ajout d'une icone pour l'action. N'oubliez pas d'ajouter une icone
		// dans le dossier "icones"
		// du plugin :)
		ImageDescriptor icon = Images.getImageDescriptor(Images.VISIBLE);
		if (icon != null)
			setImageDescriptor(icon);
		setEnabled(false);
	}

	@Override
	protected boolean calculateEnabled() {
		// On laisse les EditPolicy decider si la commande est disponible ou non
		Command cmd = testVisibleCommand(true);
		if (cmd == null)
			return false;
		return true;
	}

	public Command createVisibleCommand(boolean visible, EditPart part) {
		Command cmd = null;
		Request visibleReq = new Request(ID);

		HashMap<String, Boolean> reqData = new HashMap<>();
		reqData.put("visible", visible);
		visibleReq.setExtendedData(reqData);

		if (part != null && (part instanceof AppAbstractEditPart || part instanceof AppTreeEditPart)) {
			cmd = part.getCommand(visibleReq);
			Node node = (Node) part.getModel();
			if (node.isVisible())
				setText("Masquer");
			else
				setText("Afficher");
		}
		return cmd;
	}

	public Command testVisibleCommand(boolean visible) {
		Command cmd = null;
		Request visibleReq = new Request(ID);

		HashMap<String, Boolean> reqData = new HashMap<>();
		reqData.put("visible", visible);
		visibleReq.setExtendedData(reqData);
		List<EditPart> selectedParts = getSelectedParts();
		boolean isPartOk = false;
		for (EditPart part : selectedParts) {
			if ((part instanceof AppAbstractEditPart || part instanceof AppTreeEditPart)) {
				isPartOk = true;
				break;
			}
		}
		if (isPartOk) {
			cmd = selectedParts.get(0).getCommand(visibleReq);
			VisibleType visibleType = getStatusSelectedParts(selectedParts);
			switch (visibleType) {
			case ALL_VISIBLES:
				setText("Masquer");
				break;
			case ALL_INVISIBLES:
				setText("Afficher");
				break;
			default:
				setText("Afficher/masquer");
				break;
			}
		}
		return cmd;
	}

	public void run() {
		List<EditPart> selectedParts = getSelectedParts();
		for (EditPart part : selectedParts) {
			if ((part instanceof AppAbstractEditPart || part instanceof AppTreeEditPart)) {
				execute(createVisibleCommand(true, part));
			}
		}
	}

	// Helper
	private EditPart getSelectedPart() {
		List objects = getSelectedObjects();
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof EditPart))
			return null;
		return (EditPart) objects.get(0);
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

	private VisibleType getStatusSelectedParts(List<EditPart> selectedParts) {
		VisibleType result = VisibleType.NONE;
		for (EditPart part : selectedParts) {
			Node n = (Node) part.getModel();
			result = getNewVisibleType(result, n.isVisible());
		}
		return result;
	}

	private VisibleType getNewVisibleType(VisibleType oldType, boolean visible) {
		VisibleType result = VisibleType.NONE;
		switch (oldType) {
		case NONE:
			if (visible)
				result = VisibleType.ALL_VISIBLES;
			else
				result = VisibleType.ALL_INVISIBLES;
			break;
		case ALL_VISIBLES:
			if (visible)
				result = oldType;
			else
				result = VisibleType.INV_OR_VISIBLES;
			break;
		case ALL_INVISIBLES:
			if (visible)
				result = VisibleType.INV_OR_VISIBLES;
			else
				result = oldType;
			break;
		default:
			result = VisibleType.INV_OR_VISIBLES;
			break;
		}
		return result;
	}
}
