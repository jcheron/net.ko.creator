package net.ko.creator.preferencepage;

import org.eclipse.jface.text.templates.GlobalTemplateVariables;


public class KoTemplateContextType extends 
org.eclipse.jface.text.templates.TemplateContextType {

public static final String CONTEXT_TYPE  = "net.ko.creator.preference.contextType.ko";


public KoTemplateContextType() {
	addResolver(new GlobalTemplateVariables.Cursor());
	addResolver(new GlobalTemplateVariables.WordSelection());
	addResolver(new GlobalTemplateVariables.LineSelection());
	addResolver(new GlobalTemplateVariables.Dollar());
}

}