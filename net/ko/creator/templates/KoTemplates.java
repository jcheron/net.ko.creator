package net.ko.creator.templates;

import java.net.URL;
import java.util.ResourceBundle;

import net.ko.creator.Activator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.ui.templates.OptionTemplateSection;


public class KoTemplates extends OptionTemplateSection {

	@Override
	protected URL getInstallURL() {
		return Activator.getDefault().getBundle().getEntry("/");
	}

	@Override
	public String getSectionId() {
		// TODO Auto-generated method stub
		return "templates";
	}

	@Override
	protected ResourceBundle getPluginResourceBundle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateModel(IProgressMonitor monitor) throws CoreException {
		IPluginBase plugin = model.getPluginBase();
		IPluginModelFactory factory = model.getPluginFactory();
		
		IPluginExtension extension = createExtension("net.ko.creator", true);
		IPluginElement element = factory.createElement(extension);
		String classFullName = "net.ko.creator.templates.KoTemplates";
		
		element.setName("templates");
		element.setAttribute("class", classFullName);
		element.setAttribute("id", classFullName.toLowerCase());
		element.setAttribute("name", "kobject templates");
		extension.add(element);
		
		plugin.add(extension);
	}

	@Override
	public void addPages(Wizard wizard) {
		WizardPage page = this.createPage(0);
		page.setTitle("Perspective Configuration Page");
		page.setDescription("This page is used to config perspective");
		wizard.addPage(page);
		this.markPagesAdded();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.pde.ui.templates.ITemplateSection#getNewFiles()
	 */
	public String[] getNewFiles() {
		// TODO Auto-generated method stub
		return new String[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.pde.ui.templates.ITemplateSection#getUsedExtensionPoint()
	 */
	public String getUsedExtensionPoint() {
		return "org.eclipse.ui.views";
	}

	@Override
	public boolean isDependentOnParentWizard() {
		return true;
	}

	@Override
	protected void generateFiles(IProgressMonitor monitor, URL locationUrl) throws CoreException {
		super.generateFiles(monitor, locationUrl);
	}

}
