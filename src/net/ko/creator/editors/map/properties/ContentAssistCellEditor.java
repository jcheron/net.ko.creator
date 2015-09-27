package net.ko.creator.editors.map.properties;

import net.ko.creator.WorkbenchUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.StubTypeContext;
import org.eclipse.jdt.internal.corext.refactoring.TypeContextChecker;
import org.eclipse.jdt.internal.ui.dialogs.TextFieldNavigationHandler;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.CompletionContextRequestor;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.contentassist.ContentAssistHandler;

@SuppressWarnings({ "restriction", "deprecation" })
public class ContentAssistCellEditor extends TextCellEditor {
	private StubTypeContext fSuperClassStubTypeContext;
	private IProject project;
	private SubjectControlContentAssistant contentAssistant;

	public ContentAssistCellEditor() {
		super();
	}

	public ContentAssistCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Control createControl(Composite parent) {
		Control ctrl = super.createControl(parent);
		project = WorkbenchUtils.getActiveProject();
		Text text = (Text) ctrl;
		JavaTypeCompletionProcessor superClassCompletionProcessor = new JavaTypeCompletionProcessor(false, false, true);
		superClassCompletionProcessor.setCompletionContextRequestor(new CompletionContextRequestor() {
			public StubTypeContext getStubTypeContext() {
				return getSuperClassStubTypeContext();
			}

		});
		SubjectControlContentAssistant contentAssistant = ControlContentAssistHelper.createJavaContentAssistant(superClassCompletionProcessor);

		// ControlContentAssistHelper.createTextContentAssistant(text,
		// superClassCompletionProcessor);
		ContentAssistHandler.createHandlerForText(text, contentAssistant);
		TextFieldNavigationHandler.install(text);
		setContentAssistant(contentAssistant);
		return ctrl;
	}

	private StubTypeContext getSuperClassStubTypeContext() {
		if (fSuperClassStubTypeContext == null) {
			fSuperClassStubTypeContext = TypeContextChecker.createSuperClassStubTypeContext(JavaTypeCompletionProcessor.DUMMY_CLASS_NAME, null, getDefaultPackage(getJavaProject()));
		}
		return fSuperClassStubTypeContext;
	}

	private IJavaProject getJavaProject() {
		if (project != null)
			return JavaCore.create(project);
		else
			return WorkbenchUtils.getJavaProject();
	}

	private IPackageFragment getDefaultPackage(IJavaProject proj) {
		try {
			return proj.getPackageFragmentRoots()[0].createPackageFragment("", true, null);
		} catch (JavaModelException e) {
		}
		return null;
	}

	@Override
	protected boolean dependsOnExternalFocusListener() {
		return false;
	}

	@Override
	protected void focusLost() {
		if (contentAssistant != null && contentAssistant.hasProposalPopupFocus()) {
			// skip focus lost if it went to the content assist popup
		} else {
			super.focusLost();
		}
	}

	public void setContentAssistant(SubjectControlContentAssistant contentAssistant) {
		this.contentAssistant = contentAssistant;
	}
}
