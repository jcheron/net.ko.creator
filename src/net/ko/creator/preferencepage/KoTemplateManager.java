package net.ko.creator.preferencepage;

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;

import net.ko.creator.Activator;

public class KoTemplateManager  {
	 private static final String CUSTOM_TEMPLATES_KEY= "net.ko.creator.kotemplates";
	 private static KoTemplateManager instance;
	 private TemplateStore fStore;
	 private ContributionContextTypeRegistry fRegistry;
	 private TemplatePersistenceData[] templateData;
	 private KoTemplateManager(){}

	public static KoTemplateManager getInstance(){
		if(instance==null){
			instance = new KoTemplateManager();
		}
		return instance;
	}
	public TemplateStore getTemplateStore(){
		if (fStore == null){
			fStore = new ContributionTemplateStore(getContextTypeRegistry(), 
			Activator.getDefault().getPreferenceStore(), CUSTOM_TEMPLATES_KEY);
			try {
				fStore.load();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return fStore;
	}
	public ContextTypeRegistry getContextTypeRegistry(){
		if (fRegistry == null){
			fRegistry = new ContributionContextTypeRegistry();
		}
		fRegistry.addContextType(KoTemplateContextType.CONTEXT_TYPE);
		return fRegistry;
	}

	public IPreferenceStore getPreferenceStore(){
		return Activator.getDefault().getPreferenceStore();
	}

	public void savePluginPreferences(){
		Activator.getDefault().savePluginPreferences();
	}
}