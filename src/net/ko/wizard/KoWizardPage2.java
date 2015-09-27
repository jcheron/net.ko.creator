package net.ko.wizard;

import net.ko.bean.TemplateType;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class KoWizardPage2 extends KoWizardPage {

	private KoWizardCompo2 container;

	public KoWizardPage2() {
		super("second");
		setTitle("Création de Templates");
		setDescription("Sélectionner les champs/membres ou zones à insérer");
	}

	@Override
	public void createControl(Composite parent) {
		container = new KoWizardCompo2(parent, SWT.NONE);
		//container.init(project);
		container.setWizardPage(this);
		// Required to avoid an error in the system
		
		setControl(container);
		setPageComplete(false);
	}

	public void setProject(IProject project){
		this.project=project;
	}
	public void setTemplate(TemplateType templateType){
		container.setTemplateType(templateType);
	}
	public void SetClassName(String className){
		container.setClassName(className);
	}

	@Override
	public boolean isPageComplete() {
		return !"".equals(container.getTextTemplate());
	}

	public void dialogChanged() {
		super.dialogChanged();
		if ("".equals(container.getTextTemplate())) {
			updateStatus("Le template est vide");
			return;
		}
		updateStatus(null);
	}
	public String getFileName(){
		return container.getFileName();
	}
	public String getTextTemplate(){
		return container.getTextTemplate();
	}

	public KoWizardCompo2 getComposite() {
		return container;
	}
}
