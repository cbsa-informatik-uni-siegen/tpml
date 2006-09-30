package de.unisiegen.tpml.graphics.smallstep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.smallstep.SmallStepProofNode;
import de.unisiegen.tpml.core.smallstep.SmallStepProofRule;
import de.unisiegen.tpml.graphics.components.MenuButton;

public class SmallStepRulesComponent extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6291688672308787914L;

	private SmallStepProofNode								proofNode;
	
	private LinkedList<SmallStepRuleLabel>		ruleLabels;
	
	private MenuButton												menuButton;
	
	private int																spacing;
	
	private int																arrowSize;
	
	private Dimension													size;

	public SmallStepRulesComponent (SmallStepProofNode proofNode) {
		this.proofNode	= proofNode;
		
		this.ruleLabels	= new LinkedList<SmallStepRuleLabel> ();
		
		this.menuButton = new MenuButton ();
		add (this.menuButton);
		this.menuButton.setVisible (true);
		
		this.spacing = 6;
		this.arrowSize = 6;
		
	}
	
	public MenuButton getMenuButton () {
		return this.menuButton;
	}
	
	public void setWrongRule (ProofRule rule) {
		this.menuButton.setText("(" + rule.getName () + ")");
		this.menuButton.setTextColor(Color.RED);
	}
	
	public void setRightRule () {
		this.menuButton.setText("");
		this.menuButton.setTextColor(Color.BLACK);
	}
	
	public void setActualWidth (int width) {
		Dimension size = new Dimension (width, this.size.height);
		
		setSize (size);
		setPreferredSize (size);
		setMinimumSize (size);
		setMaximumSize (size);
	}
	
	public Dimension getNeededSize () {
		int labelHeight		= Math.max (SmallStepRuleLabel.getLabelHeight(),
																	this.menuButton.getHeight());
		
		System.out.println("labelHeiht: " + labelHeight);
		int neededHeight 	= 2 * labelHeight + 2 * this.spacing + this.arrowSize;
		int centerV 			= neededHeight / 2;
		this.size 				= new Dimension (0, neededHeight);
		
		// clear all the labels that are currently 
		for (SmallStepRuleLabel l : this.ruleLabels) {
			remove (l);
		}
		this.ruleLabels.clear();
		
		
		List<SmallStepProofRule> rules = this.proofNode.getRules();
		if (rules.size () > 0) {
			
			// first reference rule will be the first node
			SmallStepProofRule rule = rules.get(0);
			int count								= 1;
			
			for (int i=1; i<rules.size(); i++) {
				
				SmallStepProofRule cRule = rules.get (i);
				
				// when the next rule is of the same type just increment the
				// counter and wait until a different rule comes
				if (cRule.equals (rule)) {
					++count;
				}
				else {
					SmallStepRuleLabel label 	= new SmallStepRuleLabel (rule.getName (), count);
					
					// add the label to the gui and to the list of all labels
					add (label);
					this.ruleLabels.add(label);
					
					Dimension labelSize 			= label.getPreferredSize();
					
					// put the label with a bit spacing on top of the centering line
					label.setBounds(this.size.width, centerV - labelSize.height - this.spacing, labelSize.width, labelSize.height);
					this.size.width += labelSize.width;
					
					
					// the actual node this the new reference node
					rule 	= cRule;
					count	= 1;
				}
			}
			
			if (rule.isAxiom()) {
				SmallStepRuleLabel label 	= new SmallStepRuleLabel (rule.getName (), count);
				
				// add the label to the gui and to the list of all labels
				add (label);
				this.ruleLabels.add(label);
				
				Dimension labelSize 			= label.getPreferredSize();
				
				// put the label with a bit spacing at the bottom of the centering line
				label.setBounds(0, centerV + this.spacing, labelSize.width, labelSize.height);
				
				
				this.size.width = Math.max (this.size.width, labelSize.width);
				
				// we are through with this rulepack so its ot needed to display the menuButton
				this.menuButton.setVisible(false);
			}
			else {
				SmallStepRuleLabel label 	= new SmallStepRuleLabel (rule.getName (), count);
				
				// add the label to the gui and to the list of all labels
				add (label);
				this.ruleLabels.add(label);
				
				
				Dimension labelSize 			= label.getPreferredSize();
				label.setBounds(this.size.width, centerV - labelSize.height - this.spacing, labelSize.width, labelSize.height);
				this.size.width += labelSize.width;

				
				// this rulepack doesn't end with an axiom rule so we need to display the menuButton 
				Dimension buttonSize = this.menuButton.getNeededSize();
				this.menuButton.setBounds(this.size.width, centerV - this.spacing - buttonSize.height, buttonSize.width, buttonSize.height);
				this.menuButton.setVisible (true);
				this.size.width += buttonSize.width;
			}
		}
		else {
			// no rule at all evaluated
			Dimension buttonSize = this.menuButton.getNeededSize();
			this.menuButton.setBounds(this.size.width, centerV - this.spacing - buttonSize.height, buttonSize.width, buttonSize.height);
			this.menuButton.setVisible (true);
			this.size.width += buttonSize.width;
		}
		
		this.size.width += this.arrowSize*2;
		System.out.println("Size: " + this.size.width + ", " + this.size.height);
		return this.size;
	}
	
	@Override
	protected void paintComponent (Graphics gc) {
		int centerV = getHeight () / 2;
		
		gc.setColor(Color.BLACK);
		gc.drawLine (0, centerV, getWidth () - 1, centerV);
		gc.drawLine (getWidth () - 1, centerV, getWidth () - 1 - this.arrowSize, centerV - this.arrowSize);
		gc.drawLine (getWidth () - 1, centerV, getWidth () - 1 - this.arrowSize, centerV + this.arrowSize);
	}
	
}
