package net.sf.psstools.lang.ui.contentassist;

import javax.naming.CompositeName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Group;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.TypeRef;
import org.eclipse.xtext.nodemodel.impl.CompositeNode;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import net.sf.psstools.lang.pSS.PSSPackage;
import net.sf.psstools.lang.pSS.exec_block_stmt;
import net.sf.psstools.lang.pSS.hierarchical_id;
import net.sf.psstools.lang.ui.contentassist.AbstractPSSProposalProvider;

public class BasePSSProposalProvider extends AbstractPSSProposalProvider {


	@Override
	public void createProposals(ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (context.getCurrentModel() instanceof exec_block_stmt &&
				context.getCurrentNode() instanceof CompositeNode) {
			// TODO: Add in exec-block content assist
			EObject obj = context.getCurrentModel();
			while (obj != null) {
				System.out.println("obj: " + obj);
				obj = obj.eContainer();
			}

		} else {
			super.createProposals(context, acceptor);
		}
	};

	@Override
	public void complete_hierarchical_id(
			EObject model, 
			RuleCall ruleCall, 
			ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
//		System.out.println("current model: " + context.getCurrentModel());
//		System.out.println("current node: " + context.getCurrentNode());
//		System.out.println("previous model: " + context.getPreviousModel());
//		System.out.println("prefix: " + context.getPrefix());
//		TypeRef t;
//		Group g = (Group)ruleCall.getRule().eContents().get(1);
//		System.out.println("elements: " + g.getElements());
//	
//		System.out.println("complete_hierarchical_id: " + model + " " + ruleCall.getRule());
//		// TODO Auto-generated method stub
		super.complete_hierarchical_id(model, ruleCall, context, acceptor);
	}



	@Override
	public void completeHierarchical_id_Path(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
//		System.out.println("Assignment: " + assignment.getClass());
//		System.out.println("completeHierarchical_id " + assignment.getFeature() + " " +
//			assignment.getCardinality() + " " + assignment.getTerminal());
//		// TODO Auto-generated method stub
//		assignment.eGet(feature)
		super.completeHierarchical_id_Path(model, assignment, context, acceptor);
	}

	@Override
	public void complete_primary(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
//		System.out.println("complete_primary: " + model + " " + ruleCall);
		// TODO Auto-generated method stub
		super.complete_primary(model, ruleCall, context, acceptor);
	}
	
	
}
