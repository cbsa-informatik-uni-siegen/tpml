package ui;

import java.util.EventObject;
import java.awt.*;
import javax.swing.*;
import smallstep.Expression;
import typing.ProofTree;
import typing.ProofTreeFactory;
import typing.ProofNode;
import typing.Rule;
import typing.InvalidRuleException;
import typing.UnificationException;
import typing.UnknownIdentifierException;

public class TypeCheckerGUI extends JDialog {

	private TypeCheckerComponent 		typeCheckerComponent;
	
	private ProofTree					proofTree;
	
	public TypeCheckerGUI (JFrame owner, String title, boolean modal) {
		super (owner, title, modal);
		
		getContentPane().setLayout (new BoxLayout (getContentPane(), BoxLayout.PAGE_AXIS));
		
		
		JScrollPane scrollPane = new JScrollPane();
		this.typeCheckerComponent = new TypeCheckerComponent ();
		scrollPane.setViewportView(this.typeCheckerComponent);
		add (scrollPane);
		
		setSize (new Dimension (400, 300));
		
		this.typeCheckerComponent.addTypeCheckerEventListener(new TypeCheckerEventListener() {
			public void applyRule (EventObject o, ProofNode node, Rule rule) {
				applyTypeCheckerRule (node, rule);
			}
		});
	}
	
	
	public void startTypeChecking (Expression expression) {
		proofTree = ProofTreeFactory.getFactory().createProofTree(expression);
		this.typeCheckerComponent.setModel (proofTree);
	}
	
	
	public void applyTypeCheckerRule (ProofNode node, Rule rule) {
		try {
			System.out.println("TypeCheckerGUI.applyTypeCheckerRule");
			proofTree = proofTree.apply(rule, node);
			this.typeCheckerComponent.setModel(proofTree);
		}
		catch (InvalidRuleException ruleExc) {
			ruleExc.printStackTrace();
		}
		catch (UnificationException unifyExc) {
			unifyExc.printStackTrace();
		}
		catch (UnknownIdentifierException idExc) {
			idExc.printStackTrace();
		}
	}
	
}
