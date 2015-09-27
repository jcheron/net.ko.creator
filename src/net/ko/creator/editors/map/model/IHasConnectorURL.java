package net.ko.creator.editors.map.model;

public interface IHasConnectorURL {
	public void setURL(String newURL);

	public void setURL(String newURL, boolean fromRequest);

	public String getURL();
}
