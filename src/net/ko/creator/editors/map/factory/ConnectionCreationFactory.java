package net.ko.creator.editors.map.factory;

import net.ko.creator.editors.map.model.Connection;

import org.eclipse.gef.requests.CreationFactory;

public class ConnectionCreationFactory implements CreationFactory {

	public ConnectionCreationFactory() {
	}

	@Override
	public Object getNewObject() {
		return null;
	}

	@Override
	public Object getObjectType() {
		return Connection.class;
	}
}
