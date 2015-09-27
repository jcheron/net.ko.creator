package net.ko.creator.editors.map.model;

import net.ko.creator.editors.images.Images;

public class AjaxDeleteMulti extends AjaxDeleteOne {

	private static final long serialVersionUID = 1L;

	public AjaxDeleteMulti() {
		super();
	}

	@Override
	public String getImage() {
		return Images.DELETE_MULTI;
	}

}
