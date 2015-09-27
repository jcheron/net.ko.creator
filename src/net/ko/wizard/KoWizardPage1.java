package net.ko.wizard;

import net.ko.bean.TemplateType;
import net.ko.creator.WorkbenchUtils;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class KoWizardPage1 extends KoWizardPage {

	private KoWizardCompo1 container;
	private boolean koConfigExist=false;
	private boolean classGenerated=false;
	
	public KoWizardPage1() {
		super("First");
		setTitle("Création de Templates");
		setDescription("Choisir un type de template puis sélectionner la classe d'objets à utiliser");
	}
	@Override
	public void dialogChanged(){
		IFolder f=WorkbenchUtils.getAbsoluteFolder(container.getTxtFolder().getText());
		if(f==null){
			updateStatus("Le dossier choisi n'existe pas");
			return;
		}
		if(!koConfigExist){
			updateStatus("Le fichier config.ko est absent.\nChoisir add Kobject dans le menu.");
			return;
		}
		if(!classGenerated){
			updateStatus("Les classes KObject ne sont pas générées, ou la variable package n'est pas renseignée.\ndans le fichier config.ko");
			return;
		}
		super.dialogChanged();
		if(!container.getTemplateType().equals(TemplateType.ttCustom) && "".equals(container.getClassName())){
			updateStatus("Vous devez sélectionner une classe");
			return;
		}

		updateStatus(null);
	}
	@Override
	public void createControl(Composite parent) {
		container = new KoWizardCompo1(parent, SWT.NONE);
		container.setWizardPage(this);
		//container.init(project);
		dialogChanged();
		// Required to avoid an error in the system
		setControl(container);
		container.getTxtFolder().setText(getContainerName());
		setPageComplete(false);
	}


	@Override
	public boolean isPageComplete() {
		return (container.getTemplateType().equals(TemplateType.ttCustom) || !"".equals(container.getClassName()));
	}

	@Override
	public IWizardPage getNextPage() {
		KoWizardPage2 page=((KoWizard)getWizard()).getTow();
		page.setProject(project);
		page.setTemplate(container.getTemplateType());
		if(!container.getTemplateType().equals(TemplateType.ttCustom))
			page.SetClassName(container.getClassName());
		else
			page.SetClassName("");
		return page;
	}
	public boolean isKoConfigExist() {
		return koConfigExist;
	}
	public void setKoConfigExist(boolean koConfigExist) {
		this.koConfigExist = koConfigExist;
	}
	public boolean isClassGenerated() {
		return classGenerated;
	}
	public void setClassGenerated(boolean classGenerated) {
		this.classGenerated = classGenerated;
	}
	public String getFolderName() {
		return container.getTxtFolder().getText();
	}
}
