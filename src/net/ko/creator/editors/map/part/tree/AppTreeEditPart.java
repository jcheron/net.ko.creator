package net.ko.creator.editors.map.part.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import net.ko.creator.editors.images.Images;
import net.ko.creator.editors.map.model.Node;
import net.ko.creator.editors.map.model.ToggleNode;
import net.ko.creator.editors.map.part.IGeneralPart;
import net.ko.creator.editors.map.policies.AppDeletePolicy;
import net.ko.creator.editors.map.policies.NodeRolePolicy;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class AppTreeEditPart extends AbstractTreeEditPart
		implements
		PropertyChangeListener, IGeneralPart {
	protected int count = 0;
	private boolean expanded;

	public void activate() {
		super.activate();
		((Node) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		((Node) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		switch (prop) {
		case "triggerEvent":
		case "triggerId":
		case "targetId":
		case "targetURL":
			refreshVisuals();
			break;

		default:
			break;
		}
		if (evt.getPropertyName().equals(Node.PROPERTY_ADD))
			refreshChildren();
		if (evt.getPropertyName().equals(Node.PROPERTY_REMOVE))
			refreshChildren();
	}

	@Override
	protected List<Node> getModelChildren() {
		return ((Node) getModel()).getChildrenArray();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new AppDeletePolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeRolePolicy());
	}

	public void refreshVisuals() {
		Node model = (Node) getModel();
		setWidgetText(model.getName());
		setWidgetImage(Images.getImage(model.getImage()));
	}

	@Override
	public Image getImage() {
		Node model = (Node) getModel();
		return Images.getImage(model.getImage());
	}

	@Override
	public String getCaption() {
		Node model = (Node) getModel();
		return model.getName();
	}

	@Override
	public DragTracker getDragTracker(Request req) {
		return new SelectEditPartTracker(this);
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				page.showView(IPageLayout.ID_PROP_SHEET);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (!(childEditPart.getModel() instanceof ToggleNode)) {
			// super.addChildVisual(childEditPart, count++);
			Widget widget = getWidget();
			TreeItem item;
			if (widget instanceof Tree)
				item = new TreeItem((Tree) widget, 0);
			else
				item = new TreeItem((TreeItem) widget, 0);
			((TreeEditPart) childEditPart).setWidget(item);
		}
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		if (!(childEditPart.getModel() instanceof ToggleNode)) {
			count--;
		}
		super.removeChildVisual(childEditPart);
	}

	public void setWidget(Widget widget) {
		List children = getChildren();
		if (widget != null) {
			widget.setData(this);
			if (widget instanceof TreeItem) {
				final TreeItem item = (TreeItem) widget;
				item.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						expanded = item.getExpanded();
					}
				});
			}
			for (int i = 0; i < children.size(); i++) {
				TreeEditPart tep = (TreeEditPart) children.get(i);
				if (!(tep.getModel() instanceof ToggleNode)) {
					if (widget instanceof TreeItem)
						tep.setWidget(new TreeItem((TreeItem) widget, 0));
					else
						tep.setWidget(new TreeItem((Tree) widget, 0));

					// We have just assigned a new TreeItem to the EditPart
					tep.refresh();
				}
			}

			if (widget instanceof TreeItem)
				((TreeItem) widget).setExpanded(expanded);
		} else {
			Iterator iter = getChildren().iterator();
			while (iter.hasNext())
				((TreeEditPart) iter.next()).setWidget(null);
		}
		this.widget = widget;
	}
}
