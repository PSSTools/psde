package net.sf.psstools.lang.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.impl.AbstractElementImpl;
import org.eclipse.xtext.impl.AlternativesImpl;
import org.eclipse.xtext.impl.AssignmentImpl;
import org.eclipse.xtext.impl.GroupImpl;
import org.eclipse.xtext.impl.KeywordImpl;
import org.eclipse.xtext.impl.ParserRuleImpl;
import org.eclipse.xtext.impl.RuleCallImpl;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xtext.GrammarResource;

public class EBnfView extends ViewPart implements IPartListener {
	private StyledText				fText;
	IXtextDocument					fLastDoc;
	

	@Override
	public void createPartControl(Composite parent) {
		fText = new StyledText(parent, SWT.WRAP+SWT.V_SCROLL);
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		if (part instanceof XtextEditor) {
			XtextEditor xed = (XtextEditor)part;
			final IXtextDocument xd = xed.getDocument();

			
//			if (xd != fLastDoc) {
				xd.readOnly(new IUnitOfWork.Void<XtextResource>() {
					public void process(XtextResource resource) throws Exception {
						if (resource instanceof GrammarResource) {
							TextPresentationStringBuilder sb = new TextPresentationStringBuilder();
							traverse(sb, "", resource.getContents().get(0));
							fText.setText(sb.toString());
							TextPresentation.applyTextPresentation(
									sb.presentation(), fText);
						}
					}
				});
				fLastDoc = xd;
//			}
		}
	}
	
	
	private void traverse(TextPresentationStringBuilder sb, String ind, EObject obj) {
		if (obj instanceof ParserRuleImpl) {
			ParserRuleImpl ri = (ParserRuleImpl)obj;
			sb.append(ind);
			sb.append(ri.getName() + " ::= ");
			for (EObject eo : obj.eContents()) {
				production(sb, ind + "    ", eo);
			}
			sb.append("\n\n");
		} else {
			for (EObject eo : obj.eContents()) {
				traverse(sb, ind, eo);
			}
		}
	}
	
	private void production(TextPresentationStringBuilder sb, String ind, EObject obj) {
		AbstractElementImpl ai = (obj instanceof AbstractElementImpl)?(AbstractElementImpl)obj:null;
		String closing = "";
		
		if (ai != null && ai.getCardinality() != null) {
			sb.insertWS();
			if (ai.getCardinality().equals("*")) {
				sb.append("{");
				closing = "}";
			} else if (ai.getCardinality().equals("?")) {
				sb.append("[");
				closing = "]";
			}
		}
		
		if (obj instanceof AlternativesImpl) {
			boolean all_keywords = true;
			for (EObject eo : obj.eContents()) {
				if (!(eo instanceof KeywordImpl)) {
					all_keywords = false;
				}
			}
			
			if (all_keywords) {
				for (int i=0; i<obj.eContents().size(); i++) {
					EObject eo = obj.eContents().get(i);
					
					sb.insertWS();
					int old_len = sb.length();
					production(sb, ind, eo);
					if (old_len != sb.length() && i+1 < obj.eContents().size()) {
						sb.append(" | ");
					}
				}
			} else {
				sb.append("\n" + ind + "  ");
				for (int i=0; i<obj.eContents().size(); i++) {
					EObject eo = obj.eContents().get(i);
					int old_len = sb.length();
					production(sb, ind, eo);
					if (old_len != sb.length() && i+1 < obj.eContents().size()) {
						sb.append("\n" + ind + "| ");
					}
				}
			}
		} else if (obj instanceof AssignmentImpl) {
			production(sb, ind, obj.eContents().get(0));
		} else if (obj instanceof RuleCallImpl) {
			RuleCallImpl rc = (RuleCallImpl)obj;

			sb.insertWS();
			sb.append(rc.getRule().getName());
		} else if (obj instanceof GroupImpl) {
			for (EObject eo : obj.eContents()) {
				production(sb, ind, eo);
			}
		} else if (obj instanceof KeywordImpl) {
			KeywordImpl k = (KeywordImpl)obj;
			sb.insertWS();
			sb.keyword(k.getValue());
		}
	
		if (!closing.equals("")) {
			sb.insertWS();
			sb.append(closing);
		}
	}
	

	@Override
	public void partBroughtToTop(IWorkbenchPart part) { }
	@Override
	public void partClosed(IWorkbenchPart part) { } 
	@Override
	public void partDeactivated(IWorkbenchPart part) { }

	@Override
	public void partOpened(IWorkbenchPart part) { }

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getPartService().removePartListener(this);
		super.dispose();
	}

	
}
