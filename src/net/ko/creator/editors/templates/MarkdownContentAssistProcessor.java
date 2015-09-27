package net.ko.creator.editors.templates;

import java.util.ArrayList;
import java.util.List;

import net.ko.bean.TemplateElement;
import net.ko.creator.preferencepage.KoTemplateAssistProcessor;
import net.ko.creator.preferencepage.KoTemplateContextType;
import net.ko.creator.preferencepage.KoTemplateManager;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class MarkdownContentAssistProcessor extends KoTemplateAssistProcessor {

	private final static String[] PROPOSALS = {};
	public MarkdownContentAssistProcessor(MarkdownTextEditor editor) {
		super(editor);
	}

	public boolean enableTemplate(){
		return true;
	}
	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,int offset) {

		List<Object> modList = new ArrayList<Object>();
		if(enableTemplate()){
			ICompletionProposal[] templates = super.computeCompletionProposals(viewer, offset);
			if(templates!=null){
				for(int i=0;i<templates.length;i++){
					modList.add(templates[i]);
				}
			}
		}
		for (int i = 0; i < PROPOSALS.length; i++) {
			modList.add(new CompletionProposal(PROPOSALS[i], offset, 0, PROPOSALS[i].length()));
		}
		return (ICompletionProposal[])modList.toArray(new ICompletionProposal[modList.size()]);
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
            return new char[] {};
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
            return null;
    }

    @Override
    public String getErrorMessage() {
            return null;
    }

	@Override
	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		return KoTemplateManager.getInstance().getContextTypeRegistry().getContextType(KoTemplateContextType.CONTEXT_TYPE);
	}

	@Override
	protected Image getImage(Template template) {
		String img="bZone.png";
		if(template instanceof TemplateElement)
			img=((TemplateElement)template).getzType().getImage();
		return new Image(Display.getCurrent(),getClass().getResourceAsStream("/icons/"+img));
	}
}
