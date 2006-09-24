package de.unisiegen.tpml.graphics.components;

import javax.swing.JMenuItem;

import de.unisiegen.tpml.core.ProofRule;

public class MenuRuleItem extends JMenuItem {

	private ProofRule		rule;
	
	public MenuRuleItem (ProofRule rule) {
		super ();
		this.rule = rule;
		
		init (this.rule.getName(), null);
	}
	
	public ProofRule getRule () {
		return this.rule;
	}
}
