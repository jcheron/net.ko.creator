package net.ko.creator.editors.templates;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

public class MarkdownTextEditorConfiguration extends SourceViewerConfiguration {
	private final ColorManager colorManager;
	private RuleBasedScanner markdownH1Scanner;
	private RuleBasedScanner markdownH2Scanner;
	private RuleBasedScanner markdownFIELDScanner;
	private RuleBasedScanner markdownHTMLScanner;
	private MarkdownTextEditor editor;
	public MarkdownTextEditorConfiguration(MarkdownTextEditor editor,ColorManager colorManager) {
		super();
		this.colorManager = colorManager;
		this.editor=editor;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
			return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
							MarkdownTextPartitionScanner.MARKDOWN_H1,
							MarkdownTextPartitionScanner.MARKDOWN_H2,
							MarkdownTextPartitionScanner.MARKDOWN_FIELD,
							MarkdownTextPartitionScanner.MARKDOWN_HTML };
	}
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr=null;
		dr=new DefaultDamagerRepairer(getMarkdownH1Scanner());
			reconciler.setDamager(dr, MarkdownTextPartitionScanner.MARKDOWN_H1);
			reconciler.setRepairer(dr, MarkdownTextPartitionScanner.MARKDOWN_H1);
			dr = new DefaultDamagerRepairer(getMarkdownH2Scanner());
			reconciler.setDamager(dr, MarkdownTextPartitionScanner.MARKDOWN_H2);
			reconciler.setRepairer(dr, MarkdownTextPartitionScanner.MARKDOWN_H2);
			dr = new DefaultDamagerRepairer(getMarkdownFIELDScanner());
			reconciler.setDamager(dr, MarkdownTextPartitionScanner.MARKDOWN_FIELD);
			reconciler.setRepairer(dr, MarkdownTextPartitionScanner.MARKDOWN_FIELD);
			dr = new DefaultDamagerRepairer(getMarkdownScanner(markdownHTMLScanner,IMarkdownTextColorConstants.HTML,IMarkdownTextColorConstants.HTML_BG,SWT.NONE));
			reconciler.setDamager(dr, MarkdownTextPartitionScanner.MARKDOWN_HTML);
			reconciler.setRepairer(dr, MarkdownTextPartitionScanner.MARKDOWN_HTML);
			/* ... */
			return reconciler;
	}

	private ITokenScanner getMarkdownScanner(RuleBasedScanner markdownScanner,final RGB foregroundColor,final RGB backgroundColor,final int style) {
		if (markdownScanner == null) {
			markdownScanner = new RuleBasedScanner() {
						{
								setDefaultReturnToken(new Token(new TextAttribute(
												colorManager.getColor(foregroundColor),
												null,
												style)));
						}
				};
		}
		return markdownScanner;
	}
	private ITokenScanner getMarkdownH1Scanner() {
	if (markdownH1Scanner == null) {
			  markdownH1Scanner = new RuleBasedScanner() {
					{
							setDefaultReturnToken(new Token(new TextAttribute(
											colorManager.getColor(IMarkdownTextColorConstants.H1),
											null,
											TextAttribute.UNDERLINE
											)));
					}
			};
	}
	return markdownH1Scanner;
	}
	private ITokenScanner getMarkdownH2Scanner() {
		if (markdownH2Scanner == null) {
				markdownH2Scanner = new RuleBasedScanner() {
						{
								setDefaultReturnToken(new Token(new TextAttribute(
												colorManager.getColor(IMarkdownTextColorConstants.H2),
												null,
												SWT.NONE)));
						}
				};
		}
		return markdownH2Scanner;
	}

	private ITokenScanner getMarkdownFIELDScanner() {
		if (markdownFIELDScanner == null) {
			markdownFIELDScanner = new RuleBasedScanner() {
						{
								setDefaultReturnToken(new Token(new TextAttribute(
												colorManager.getColor(IMarkdownTextColorConstants.FIELD),
												null,
												SWT.NONE)));
						}
				};
		}
		return markdownFIELDScanner;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor sharedProcessor = new MarkdownContentAssistProcessor(editor);
		assistant.setContentAssistProcessor(sharedProcessor, IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(sharedProcessor, MarkdownTextPartitionScanner.MARKDOWN_H1);
		assistant.setContentAssistProcessor(sharedProcessor, MarkdownTextPartitionScanner.MARKDOWN_H2);
		assistant.setContentAssistProcessor(sharedProcessor, MarkdownTextPartitionScanner.MARKDOWN_FIELD);
		assistant.setContentAssistProcessor(sharedProcessor, MarkdownTextPartitionScanner.MARKDOWN_HTML);
		
		assistant.setEmptyMessage("Sorry, no hint for you :-/");
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(500);
		return assistant;
	}
}
