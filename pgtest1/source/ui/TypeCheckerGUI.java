package ui;

import java.util.EventObject;
import java.util.Vector;
import java.awt.*;

import javax.swing.*;

import expressions.Expression;

import java.awt.event.*;

import typing.MonoType;
import typing.ProofTree;
import typing.ProofTreeFactory;
import typing.ProofNode;
import typing.Rule;
import typing.InvalidRuleException;
import typing.UnificationException;
import typing.UnknownIdentifierException;
import ui.annotations.EditorActionInfo;
import ui.newgui.AbstractEditorComponent;

public class TypeCheckerGUI extends AbstractEditorComponent {

	private TypeCheckerComponent typeCheckerComponent;

	private Vector<ProofTree> proofTree;

	private int currentProofTreeIndex;

	private JScrollPane scrollPane;

	// private JButton buttonClose;

	// private JButton buttonUndo;
	//	
	// private JButton buttonRedo;

	public TypeCheckerGUI(String title) {
		super(title);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.scrollPane = new JScrollPane();
		this.typeCheckerComponent = new TypeCheckerComponent();
		scrollPane.setViewportView(this.typeCheckerComponent);
		mainPanel.add(scrollPane);

		// this.buttonClose = new JButton ("Close");
		// this.buttonUndo = new JButton ("Undo");
		// this.buttonRedo = new JButton ("Redo");

		// JPanel buttonPanel = new JPanel ();
		// buttonPanel.setLayout (new BoxLayout(buttonPanel,
		// BoxLayout.LINE_AXIS));
		// buttonPanel.add(buttonUndo);
		// buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		// buttonPanel.add(buttonRedo);
		// buttonPanel.add(Box.createHorizontalGlue());
		// buttonPanel.add(buttonClose);

		// mainPanel.add(buttonPanel);

		add(mainPanel);

		this.proofTree = new Vector<ProofTree>();
		this.typeCheckerComponent
				.addTypeCheckerEventListener(new TypeCheckerEventListener() {
					public void applyRule(EventObject o, ProofNode node,
							Rule rule) {
						applyTypeCheckerRule(node, rule);
					}

					public void guessType(EventObject o, ProofNode node,
							MonoType type) {
						applyGuessType(node, type);
					}
				});

		// this.buttonUndo.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// handleUndo ();
		// };
		// });
		// this.buttonRedo.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// handleRedo ();
		// };
		// });
	}

	public void startTypeChecking(Expression expression) {

		ProofTree proof = ProofTreeFactory.getFactory().createProofTree(
				expression);
		proofTree.add(proof);
		this.currentProofTreeIndex = 1;
		this.typeCheckerComponent.setModel(proof);
		// this.buttonUndo.setEnabled(false);
		setActionStatus("Undo", false);
		// this.buttonRedo.setEnabled(false);
		setActionStatus("Redo", false);
	}

	public void applyTypeCheckerRule(ProofNode node, Rule rule) {
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
			checkButtonStates();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(),
					"Incorrect", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void applyGuessType(ProofNode node, MonoType type) {
		try {
			// the user has clicked undo so we have to delete the tailing proofs
			while (this.currentProofTreeIndex < this.proofTree.size()) {
				this.proofTree.remove(this.currentProofTreeIndex);
			}
			ProofTree currentProof = proofTree.lastElement();
			ProofTree proof = currentProof.guess(node, type);
			this.proofTree.add(proof);
			this.typeCheckerComponent.setModel(proof);
			++this.currentProofTreeIndex;
			checkButtonStates();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(),
					"Incorrect", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void checkButtonStates() {
		if (this.currentProofTreeIndex <= 1) {
			// this.buttonUndo.setEnabled (false);
			setActionStatus("Undo", false);
		}
		if (this.currentProofTreeIndex >= this.proofTree.size()) {
			// this.buttonRedo.setEnabled (false);
			setActionStatus("Redo", false);
		}

		if (this.currentProofTreeIndex < this.proofTree.size()) {
			// this.buttonRedo.setEnabled (true);
			setActionStatus("Redo", true);
		}
		if (this.currentProofTreeIndex > 1) {
			// this.buttonUndo.setEnabled (true);
			setActionStatus("Undo", true);
		}
	}

	@EditorActionInfo(visible = false, name = "Undo", icon = "icons/undo.gif", accelModifiers = KeyEvent.VK_UNDEFINED, accelKey = KeyEvent.VK_UNDEFINED)
	public void handleUndo() {
		--this.currentProofTreeIndex;
		ProofTree currentProof = proofTree
				.elementAt(this.currentProofTreeIndex - 1);
		this.typeCheckerComponent.setModel(currentProof);
		checkButtonStates();
	}

	@EditorActionInfo(visible = false, name = "Redo", icon = "icons/redo.gif",  accelModifiers = KeyEvent.VK_UNDEFINED, accelKey = KeyEvent.VK_UNDEFINED)
	public void handleRedo() {
		++this.currentProofTreeIndex;
		ProofTree currentProof = proofTree
				.elementAt(this.currentProofTreeIndex - 1);
		this.typeCheckerComponent.setModel(currentProof);
		checkButtonStates();
	}

}
