package net.ko.creator.editors.map.factory;

import net.ko.creator.editors.map.model.AjaxAccordion;
import net.ko.creator.editors.map.model.AjaxDeleteMulti;
import net.ko.creator.editors.map.model.AjaxDeleteOne;
import net.ko.creator.editors.map.model.AjaxDialogButton;
import net.ko.creator.editors.map.model.AjaxFireEvent;
import net.ko.creator.editors.map.model.AjaxFunction;
import net.ko.creator.editors.map.model.AjaxInclude;
import net.ko.creator.editors.map.model.AjaxIncludeDialog;
import net.ko.creator.editors.map.model.AjaxMessage;
import net.ko.creator.editors.map.model.AjaxMessageDialog;
import net.ko.creator.editors.map.model.AjaxRefreshControl;
import net.ko.creator.editors.map.model.AjaxRefreshFormValues;
import net.ko.creator.editors.map.model.AjaxRequest;
import net.ko.creator.editors.map.model.AjaxSelector;
import net.ko.creator.editors.map.model.AjaxShowhide;
import net.ko.creator.editors.map.model.AjaxSubmitForm;
import net.ko.creator.editors.map.model.AjaxUpdateOne;
import net.ko.creator.editors.map.model.AjaxUpdateOneField;
import net.ko.creator.editors.map.model.CssOneTransition;
import net.ko.creator.editors.map.model.CssTransition;
import net.ko.creator.editors.map.model.JS;
import net.ko.design.KCssOneTransition;
import net.ko.design.KCssTransition;
import net.ko.mapping.KAjaxAccordion;
import net.ko.mapping.KAjaxDeleteMulti;
import net.ko.mapping.KAjaxDeleteOne;
import net.ko.mapping.KAjaxDialogButton;
import net.ko.mapping.KAjaxEvent;
import net.ko.mapping.KAjaxFunction;
import net.ko.mapping.KAjaxInclude;
import net.ko.mapping.KAjaxIncludeDialog;
import net.ko.mapping.KAjaxJs;
import net.ko.mapping.KAjaxMessage;
import net.ko.mapping.KAjaxMessageDialog;
import net.ko.mapping.KAjaxRefreshControl;
import net.ko.mapping.KAjaxRefreshFormValues;
import net.ko.mapping.KAjaxRequest;
import net.ko.mapping.KAjaxSelector;
import net.ko.mapping.KAjaxShowHide;
import net.ko.mapping.KAjaxSubmitForm;
import net.ko.mapping.KAjaxUpdateOne;
import net.ko.mapping.KAjaxUpdateOneField;

import org.eclipse.gef.requests.CreationFactory;

public class NodeCreationFactory implements CreationFactory {
	private Class<?> template;

	public NodeCreationFactory(Class<?> t) {
		this.template = t;
	}

	@Override
	public Object getNewObject() {
		if (template == null)
			return null;
		if (template == AjaxRequest.class)
		{
			AjaxRequest ajaxRequest = new AjaxRequest();
			ajaxRequest.setAjaxRequest(new KAjaxRequest("newURL.do"));
			return ajaxRequest;
		}
		if (template == JS.class)
		{
			JS js = new JS();
			js.setAjaxJs(new KAjaxJs("triggerSelector", "", "click"));
			return js;
		}
		if (template == AjaxInclude.class) {
			AjaxInclude ajaxInclude = new AjaxInclude();
			ajaxInclude.setAjaxObject(new KAjaxInclude("targetId", "targetURL", "get", "", "", ""));
			return ajaxInclude;
		}
		if (template == AjaxSelector.class) {
			AjaxSelector ajaxSelector = new AjaxSelector();
			ajaxSelector.setAjaxObject(new KAjaxSelector("#selector", "click"));
			return ajaxSelector;
		}
		if (template == AjaxMessage.class) {
			AjaxMessage ajaxMessage = new AjaxMessage();
			ajaxMessage.setAjaxObject(new KAjaxMessage("targetId", "''"));
			return ajaxMessage;
		}
		if (template == AjaxFireEvent.class) {
			AjaxFireEvent ajaxFireEvent = new AjaxFireEvent();
			ajaxFireEvent.setAjaxObject(new KAjaxEvent("triggerId", "click"));
			return ajaxFireEvent;
		}
		if (template == AjaxUpdateOne.class) {
			AjaxUpdateOne ajaxUpdateOne = new AjaxUpdateOne();
			ajaxUpdateOne.setAjaxObject(new KAjaxUpdateOne("virtualURL"));
			return ajaxUpdateOne;
		}
		if (template == AjaxUpdateOneField.class) {
			AjaxUpdateOneField ajaxUpdateOneField = new AjaxUpdateOneField();
			ajaxUpdateOneField.setAjaxObject(new KAjaxUpdateOneField());
			return ajaxUpdateOneField;
		}
		if (template == AjaxDeleteOne.class) {
			AjaxDeleteOne ajaxDeleteOne = new AjaxDeleteOne();
			ajaxDeleteOne.setAjaxObject(new KAjaxDeleteOne("virtualURL"));
			return ajaxDeleteOne;
		}
		if (template == AjaxDeleteMulti.class) {
			AjaxDeleteMulti ajaxDeleteMulti = new AjaxDeleteMulti();
			ajaxDeleteMulti.setAjaxObject(new KAjaxDeleteMulti("virtualURL"));
			return ajaxDeleteMulti;
		}
		if (template == AjaxShowhide.class) {
			AjaxShowhide ajaxShowhide = new AjaxShowhide();
			ajaxShowhide.setAjaxObject(new KAjaxShowHide("targetSelector"));
			return ajaxShowhide;
		}
		if (template == AjaxFunction.class) {
			AjaxFunction ajaxFunction = new AjaxFunction();
			ajaxFunction.setAjaxObject(new KAjaxFunction(""));
			return ajaxFunction;
		}
		if (template == AjaxSubmitForm.class) {
			AjaxSubmitForm ajaxSubmitForm = new AjaxSubmitForm();
			ajaxSubmitForm.setAjaxObject(new KAjaxSubmitForm("virtualURL"));
			return ajaxSubmitForm;
		}
		if (template == AjaxRefreshControl.class) {
			AjaxRefreshControl ajaxRefreshControl = new AjaxRefreshControl();
			ajaxRefreshControl.setAjaxObject(new KAjaxRefreshControl("virtualURL"));
			return ajaxRefreshControl;
		}
		if (template == AjaxRefreshFormValues.class) {
			AjaxRefreshFormValues ajaxRefreshFormValues = new AjaxRefreshFormValues();
			ajaxRefreshFormValues.setAjaxObject(new KAjaxRefreshFormValues("targetId", "targetURL"));
			return ajaxRefreshFormValues;
		}
		if (template == AjaxAccordion.class) {
			AjaxAccordion ajaxAccordion = new AjaxAccordion();
			ajaxAccordion.setAjaxObject(new KAjaxAccordion("containerId"));
			return ajaxAccordion;
		}

		if (template == AjaxMessageDialog.class) {
			AjaxMessageDialog ajaxMessageDialog = new AjaxMessageDialog();
			ajaxMessageDialog.setAjaxObject(new KAjaxMessageDialog("title"));
			return ajaxMessageDialog;
		}

		if (template == AjaxDialogButton.class) {
			AjaxDialogButton ajaxDialogButton = new AjaxDialogButton();
			ajaxDialogButton.setAjaxObject(new KAjaxDialogButton("caption"));
			return ajaxDialogButton;
		}
		if (template == AjaxIncludeDialog.class) {
			AjaxIncludeDialog ajaxIncludeDialog = new AjaxIncludeDialog();
			ajaxIncludeDialog.setAjaxObject(new KAjaxIncludeDialog("title", "targetURL"));
			return ajaxIncludeDialog;
		}
		if (template == CssTransition.class) {
			CssTransition cssTransition = new CssTransition();
			cssTransition.setAjaxObject(new KCssTransition("targetId", "opacity", "0", "1", "1", "linear"));
			return cssTransition;
		}
		if (template == CssOneTransition.class) {
			CssOneTransition cssOneTransition = new CssOneTransition();
			cssOneTransition.setAjaxObject(new KCssOneTransition("opacity", "0", "1", "1", "linear"));
			return cssOneTransition;
		}
		return null;
	}

	@Override
	public Object getObjectType() {
		return template;
	}
}
