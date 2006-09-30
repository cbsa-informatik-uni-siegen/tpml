package de.unisiegen.tpml.graphics.smallstep;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class SmallStepRuleLabel extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2850026835245921469L;

	private int						ruleCount;
	
	public SmallStepRuleLabel (String ruleName, int ruleCount) {
		super ();
		
		this.ruleCount 	= ruleCount;

		
		setLayout (null);
		
		JLabel ruleLabel = new JLabel ();
		add (ruleLabel);
		ruleLabel.setText("(" + ruleName + ")");
		
		JLabel exponentLabel	= new JLabel ();
		add (exponentLabel);
		exponentLabel.setText("" + ruleCount);
		
		// scale the font down to 0.75
		Font expFont = exponentLabel.getFont();
		expFont = expFont.deriveFont(expFont.getSize2D() * 0.75f);
		exponentLabel.setFont(expFont);
		
		Dimension size = null;
		// the exponen will be placed so that 1/2 of the size of the 
		// exponent label will appear on top of the
		Dimension ruleSize 	= ruleLabel.getPreferredSize();
		Dimension expSize 	= exponentLabel.getPreferredSize();
		int top = expSize.height/2;
		
		ruleLabel.setBounds(0, top, ruleSize.width, ruleSize.height);
		exponentLabel.setBounds(ruleSize.width, 0, expSize.width, expSize.height);
		
		size = new Dimension (ruleSize.width + expSize.width, ruleSize.height + top);
		
		ruleLabel.setVisible(true);
		
		if (ruleCount > 1) {
			exponentLabel.setVisible(true);
		}
		else {
			ruleSize.width -= expSize.width;
			exponentLabel.setVisible(false);
		}
		
		// set the size for this component
		setSize (size);
		setMinimumSize (size);
		setMaximumSize (size);
		setPreferredSize (size);
	}
	
	public int getRuleCount () {
		return this.ruleCount;
	}
	
	public static int getLabelHeight () {
		
		// just create a label that can calculate the height
		SmallStepRuleLabel l = new SmallStepRuleLabel ("RULE", 1);
		return l.getHeight();
	}
	
}
