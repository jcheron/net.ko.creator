package net.ko.creator.preferencepage;

import net.ko.creator.Activator;

import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

public class KoPreferencePage extends TemplatePreferencePage implements
		IWorkbenchPreferencePage {

	public KoPreferencePage() {
		try {
			setPreferenceStore(Activator.getDefault().getPreferenceStore());
			setTemplateStore(KoTemplateManager.getInstance().getTemplateStore());
			setContextTypeRegistry(KoTemplateManager.getInstance()
					.getContextTypeRegistry());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected boolean isShowFormatterSetting() {
		return false;
	}

	public boolean performOk() {
		boolean ok = super.performOk();
		Activator.getDefault().savePluginPreferences();
		return ok;
	}

}