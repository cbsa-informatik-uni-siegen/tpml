package ui;

import java.util.EventObject;
import java.util.Vector;
import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

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
	
	private Vector<ProofTree>			proofTree;
	
	private int							currentProofTreeIndex;
	
	private JScrollPane					scrollPane;
	
	private JButton						buttonClose;
	
	private JButton						buttonUndo;
	
	private JButton						buttonRedo;
	
	public TypeCheckerGUI (JFrame owner, String title, boolean modal) {
		super (owner, title, modal);
		getContentPane().setLayout (new BoxLayout (getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel mainPanel	= new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		
		this.scrollPane = new JScrollPane();
		this.typeCheckerComponent = new TypeCheckerComponent ();
		scrollPane.setViewportView(this.typeCheckerComponent);
		mainPanel.add (scrollPane);
		
		
		this.buttonClose = new JButton ("Close");
		this.buttonUndo = new JButton ("Undo");
		this.buttonRedo = new JButton ("Redo");
		
		JPanel buttonPanel = new JPanel ();
		buttonPanel.setLayout (new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(buttonUndo);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(buttonRedo);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(buttonClose);

		mainPanel.add(buttonPanel);
		
		
		getContentPane().add(mainPanel);
		
		setSize (new Dimension (700, 700));
		
		this.proofTree = new Vector<ProofTree> ();
		this.typeCheckerComponent.addTypeCheckerEventListener(new TypeCheckerEventListener() {
			public void applyRule (EventObject o, ProofNode node, Rule rule) {
				applyTypeCheckerRule (node, rule);
			}
		});
		
		
		this.buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose ();
			}
		});
		
		this.buttonUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleUndo ();
			};
		});
		this.buttonRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleRedo ();
			};
		});
	}
	
	
	public void startTypeChecking (Expression expression) {
		
		ProofTree proof = ProofTreeFactory.getFactory().createProofTree(expression);
		proofTree.add(proof);
		this.currentProofTreeIndex = 1;
		this.typeCheckerComponent.setModel (proof);
		this.buttonUndo.setEnabled(false);
		this.buttonRedo.setEnabled(false);
	}
	
	
	public void applyTypeCheckerRule (ProofNode node, Rule rule) {
		try {
			// the user has clicked undo so we have to delete the tailing proofs
			while (this.currentProofTreeIndex < this.proofTree.size()) {
				this.proofTree.remove(this.currentProofTreeIndex);
			}
			ProofTree currentProof = proofTree.lastElement();	
			ProofTree proof = currentProof.apply(rule, node);
			this.proofTree.add(proof);
			this.typeCheckerComponent.setModel(proof);
			++this.currentProofTreeIndex;
			checkButtonStates ();
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
	
	private void checkButtonStates() {
		if (this.currentProofTreeIndex <= 1) {
			this.buttonUndo.setEnabled (false);
		}
		if (this.currentProofTreeIndex >= this.proofTree.size ()) {
			this.buttonRedo.setEnabled (false);
		}
		
		if (this.currentProofTreeIndex < this.proofTree.size ()) {
			this.buttonRedo.setEnabled (true);
		}
		if (this.currentProofTreeIndex > 1) {
			this.buttonUndo.setEnabled (true);
		}
	}
	
	private void handleUndo () {
		--this.currentProofTreeIndex;
		ProofTree currentProof = proofTree.elementAt(this.currentProofTreeIndex-1);
		this.typeCheckerComponent.setModel(currentProof);
		checkButtonStates ();
	}
	
	private void handleRedo () {
		++this.currentProofTreeIndex;
		ProofTree currentProof = proofTree.elementAt(this.currentProofTreeIndex-1);
		this.typeCheckerComponent.setModel(currentProof);
		checkButtonStates ();
	}
	
}


