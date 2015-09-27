package net.ko.creator.preferencepage;

import java.util.ArrayList;
import java.util.List;

import net.ko.creator.Activator;
import net.ko.creator.editors.templates.MarkdownTextEditor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

public class KoTemplateAssistProcessor extends TemplateCompletionProcessor{
	protected MarkdownTextEditor editor;
/*	@Override
	protected String extractPrefix(ITextViewer viewer, int offset) {
		IDocument document= viewer.getDocument();
		int i= offset;
		if (i > document.getLength())
			return ""; //$NON-NLS-1$
	
		try {
			while (i > 0) {
				char ch= document.getChar(i - 1);
				if (ch != '{' && !Character.isJavaIdentifierPart(ch))
					break;
				i--;
			}
			return document.get(i, offset - i);
		} catch (BadLocationException e) {
			return ""; //$NON-NLS-1$
		}
	}*/

	protected Template[] getTemplates(String contextTypeId) {
		KoTemplateManager manager = KoTemplateManager.getInstance();
		return manager.getTemplateStore().getTemplates();
	}

	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		KoTemplateManager manager = KoTemplateManager.getInstance();
		return manager.getContextTypeRegistry().getContextType(KoTemplateContextType.CONTEXT_TYPE);
	}

	protected Image getImage(Template template) {
		return Activator.getDefault().getImageRegistry().get(Activator.ICON_TEMPLATE);
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		ITextSelection selection= (ITextSelection) viewer.getSelectionProvider().getSelection();
		// adjust offset to end of normalized selection
		if (selection.getOffset() == offset)
			offset= selection.getOffset() + selection.getLength();
		String prefix= extractPrefix(viewer, offset);
		Region region= new Region(offset - prefix.length(), prefix.length());
		TemplateContext context= createContext(viewer, region);
		if (context == null)
			return new ICompletionProposal[0];
		context.setVariable("selection", selection.getText()); // name of the selection variables {line, word_selection //$NON-NLS-1$
		Template[] templates= getTemplates(context.getContextType().getId());
		List<ICompletionProposal> matches= new ArrayList<ICompletionProposal>();
		if(templates==null)
			return new ICompletionProposal[0];
		for (int i= 0; i < templates.length; i++) {
			Template template= templates[i];
			try {
				context.getContextType().validate(template.getPattern());
			} catch (TemplateException e) {
				continue;
			}
			if(!prefix.equals("") &&prefix.charAt(0)=='{')
				prefix=prefix.substring(1);
			if (prefix.equals("")||((template.getName().startsWith(prefix) && 
			template.matches(prefix, context.getContextType().getId()))))
				matches.add(createProposal(template, context, (IRegion) region, getRelevance(template, prefix)));
		}
		if(editor!=null){
			Template[] tmpls=editor.getZones().getTemplates();
			for(int i=0;i<tmpls.length;i++)
				matches.add(createProposal(tmpls[i], context, (IRegion) region, getRelevance(tmpls[i], prefix)));
		}
		return matches.toArray(new ICompletionProposal[matches.size()]);
	}
	public TemplateProposal createProposal(ITextViewer viewer,int offset,Template tmpl){
		ITextSelection selection= (ITextSelection) viewer.getSelectionProvider().getSelection();
		// adjust offset to end of normalized selection
		if (selection.getOffset() == offset)
			offset= selection.getOffset() + selection.getLength();
		String prefix= extractPrefix(viewer, offset);
		Region region= new Region(offset - prefix.length(), prefix.length());
		TemplateContext context= createContext(viewer, region);
		context.setVariable("selection", selection.getText());
		try {
			context.getContextType().validate(tmpl.getPattern());
		} catch (TemplateException e) {}
		return new TemplateProposal(tmpl, context, region, null);
				//createProposal(tmpl, context, (IRegion) region, getRelevance(tmpl, prefix));
	}
	public KoTemplateAssistProcessor(MarkdownTextEditor editor) {
		super();
		this.editor = editor;
	}

	}