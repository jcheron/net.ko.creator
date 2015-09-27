package net.ko.creator.editors.map.policies;

import net.ko.creator.editors.map.figure.ConnexionFigure;
import net.ko.creator.editors.map.model.Connection;
import net.ko.creator.editors.map.part.ConnectionPart;

import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

public class ContainerHighlightEditPolicy extends GraphicalEditPolicy {

	@Override
	public void showTargetFeedback(Request request) {
		super.showTargetFeedback(request);
		Connection conn = getConnection();
		conn.getSourceNode().highlight(true);
		conn.getTargetNode().highlight(true);
		if (getHostFigure() instanceof ConnexionFigure) {
			((ConnexionFigure) getHostFigure()).highlight(true);
		}
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		// TODO Auto-generated method stub
		super.eraseTargetFeedback(request);
		if (getHostFigure() instanceof ConnexionFigure) {
			((ConnexionFigure) getHostFigure()).highlight(false);
		}
		Connection conn = getConnection();
		conn.getSourceNode().highlight(false);
		conn.getTargetNode().highlight(false);
	}

	private Connection getConnection() {
		ConnectionPart part = (ConnectionPart) getHost();
		return (Connection) part.getModel();
	}

}
