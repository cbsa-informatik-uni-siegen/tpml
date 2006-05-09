package ui.smallstep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import common.ProofModel;
import common.ProofRule;
import common.ProofRuleException;
import common.ProofStep;

import smallstep.SmallStepProofModel;
import smallstep.SmallStepProofNode;
import ui.AbstractNode;
import ui.beans.MenuButton;
import ui.beans.MenuButtonListener;
import ui.renderer.EnvironmentRenderer;
import ui.renderer.ExpressionRenderer;



class SmallStepNode extends AbstractNode {
	
	private enum ActionType {
		TranslateToCoresyntax,
	}

	private class RuleMenuItem extends JMenuItem {
		private ProofRule	rule;
		
		public RuleMenuItem (ProofRule rule) {
			super (rule.getName());
			this.rule = rule;
		}
		
		public ProofRule getRule () {
			return rule;
		}
	};
	
	private static class ActionMenuItem extends JMenuItem {
		private ActionType	type;
		
		public ActionMenuItem (ActionType type) {
			super (ActionMenuItem.getString(type));
			this.type = type;
		}
		
		private static String getString(ActionType  type) {
			switch (type) {
			case TranslateToCoresyntax:
				return "Translate to coresyntax";
			}
			return "narf";
		}
		
		public ActionType getActionType () {
			return type;
		}
	}

	private static int 				center;
	
	private EnvironmentRenderer		envRenderer;
	
	private Dimension				envSize;
	
	private ExpressionRenderer		expRenderer;
	
	private Dimension				expSize;
	
	private Dimension				ruleSize;
	
	private MenuButton				ruleButton;
	
	private Font					ruleFont;
	
	private FontMetrics				ruleFontMetrics;
	
	private ActionMenuItem			translateToCoreSyntax = null;
		
	public SmallStepNode(SmallStepProofNode proofNode) {
		super();
		
		setLayout(null);
		
		this.proofNode		= proofNode;
		
		Font fnt = new JComboBox().getFont();
		FontMetrics fntMetrics = getFontMetrics(fnt);
		
		
		this.ruleFont 			= fnt;
		this.ruleFontMetrics	= fntMetrics;
		
		this.ruleButton = new MenuButton();
		add (ruleButton);
		this.ruleButton.addMenuButtonListener(new MenuButtonListener() {
			public void menuItemActivated (MenuButton button, JMenuItem item) {
				if (item instanceof RuleMenuItem) {
					evaluateRule(((RuleMenuItem)item).getRule());
				}
				else if (item instanceof ActionMenuItem) {
					ActionMenuItem ami = (ActionMenuItem)item;
					switch (ami.getActionType()) {
					case TranslateToCoresyntax:
						translateToCoreSyntax();
						break;
					}
				}
			}
		});
	}
	
	public void reset () {
		resetExpressionRenderer();
		
		if (this.translateToCoreSyntax != null) {
			this.translateToCoreSyntax.setEnabled(this.proofNode.containsSyntacticSugar());
		}
	}
	
	private void resetExpressionRenderer() {
		this.expRenderer	= new ExpressionRenderer (this.proofNode.getExpression());
		
		Font fnt = new JComboBox().getFont();
		FontMetrics fntMetrics = getFontMetrics(fnt);
		
		this.expRenderer.setTextStyle(fnt, fntMetrics, Color.BLACK);
		this.expRenderer.setKeywordStyle(fnt, fntMetrics, Color.RED);
		this.expRenderer.setConstantStyle(fnt, fntMetrics, Color.BLUE);
		this.expRenderer.setUnderlineColor(Color.RED);
		this.expRenderer.checkFonts();
		
	}
	
	
	private void initiateButtonMenu() {
		JPopupMenu menu = new JPopupMenu();
		ProofRule rules[] = this.model.getRules();
		for (ProofRule r : rules) {
			RuleMenuItem item = new RuleMenuItem (r);
			menu.add(item);
		}
		
		menu.addSeparator();
		
		this.translateToCoreSyntax = new ActionMenuItem (ActionType.TranslateToCoresyntax);
		menu.add(this.translateToCoreSyntax);
		if (!this.proofNode.containsSyntacticSugar()) {
			this.translateToCoreSyntax.setEnabled(false);
		}
		
		this.ruleButton.setMenu(menu);
	}
	
	public void setModel (ProofModel model) {
		super.setModel(model);
		
		initiateButtonMenu();
	}
	
	/**
	 * Returns the size needed for the entire
	 * 
	 * @return
	 */
	public Dimension getRequiredSize(int maxWidth) {
		expSize = getExpressionSize(maxWidth);
		ruleSize = getRuleSize ();
		
		return new Dimension (expSize.width + SmallStepNode.center + this.ruleFontMetrics.getDescent(), 
				expSize.height > ruleSize.height ? expSize.height : ruleSize.height);
		
	}

	
	public void placeMenuButtons() {
		if (this.proofNode.isProven()) {
			this.ruleButton.setVisible(false);
			return;
		}
		int heightDiv2 = getHeight() / 2;
		int posX = 0;
	 	for (ProofStep step : this.proofNode.getSteps()) {
			ProofRule r = step.getRule();
			if (r.isAxiom()) {
				posX = 0;
			}
			posX += ruleFontMetrics.stringWidth("(" + r.getName () + ")");
		}
	 	
		this.ruleButton.setBounds(posX, heightDiv2 - this.ruleButton.getNeededHeight(),
				this.ruleButton.getNeededWidth(), this.ruleButton.getNeededHeight());
		this.ruleButton.setVisible(true);
	}
	
	public Dimension getRuleSize() {
		int maxX = 0;
		int cX = 0;
		SmallStepProofModel pModel = (SmallStepProofModel)model;
		int w = 0;
		String 	lastName 	= null;
		int		count		= 0;
		boolean	addExp		= false;
		
		Font 		expF	= this.ruleFont.deriveFont(this.ruleFont.getSize2D() / 2.0f);
		FontMetrics expFM	= getFontMetrics(expF);
		
		ProofStep steps [] = this.proofNode.getSteps();
		for (int i=0; i<steps.length;) {
			ProofRule r = steps [i].getRule();
			if (r.isAxiom()) {
				w = this.ruleFontMetrics.stringWidth("(" + r.getName() + ")");
				break;
			}
			else {
				String name = r.getName();
				i++;
				int j=0;
				for (; (j + i) <steps.length; j++) {
					ProofRule tmpRule = steps [i+j].getRule();
					if (!tmpRule.getName().equals(r.getName())) {
						break;
					}
				}
				if (j >= 1) {
					addExp = true;
					w += expFM.stringWidth("" + (j+1));
				}
				w += this.ruleFontMetrics.stringWidth("(" + r.getName() + ")");
				w += this.ruleFontMetrics.getDescent();
				i += j;
			}
			if (w > maxX) maxX = w;
		}
		
		if (!this.proofNode.isProven()) {
			maxX += this.ruleButton.getNeededWidth();
		}
		int height = ruleFontMetrics.getHeight() * 2 + 10;
		if (addExp) {
			height += expFM.getDescent() * 2;
		}
		return new Dimension(maxX + 10, height);
	}

	public Dimension getExpressionSize(int maxWidth) {
		return expRenderer.getNeededSize(maxWidth);
	}

	/**
	 * Render the content of the SmallStepNode.
	 * <br>
	 * The centering arrow will get rendered manualy,
	 * the expression and maybe the memory environment will
	 * get Rendered by using the ExpressionRenderer and the
	 *  
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		// fill the background white
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
				
		// draw a black arrow on the base
		int heightDiv2 = getHeight() / 2;
		g2d.setColor(Color.BLACK);
		g2d.drawLine(0, heightDiv2, center, heightDiv2);
		g2d.drawLine(center, heightDiv2, center - 5, heightDiv2 - 5);
		g2d.drawLine(center, heightDiv2, center - 5, heightDiv2 + 5);
		
		// now draw the expression
		int posX = center;
		int posY = heightDiv2 - expSize.height / 2;
		expRenderer.render(posX + this.ruleFontMetrics.getDescent(), posY, g2d);
		
		posY = getHeight() / 2;
		
		// now draw the evaluated rules
		g2d.setFont(this.ruleFont);
		posX = 0;
		String 	lastName 	= null;
		int		count		= 0;
		
		g2d.setColor(Color.BLACK);
		Font		expF	= this.ruleFont.deriveFont(this.ruleFont.getSize2D());
		FontMetrics	expFM	= getFontMetrics (expF);
		ProofStep steps [] = this.proofNode.getSteps();
		for (int i=0; i<steps.length;) {
			ProofRule r = steps [i].getRule();
			if (r.isAxiom()) {
				posY = heightDiv2 + ruleFontMetrics.getAscent();
				posX = 0;
				g2d.drawString("(" + r.getName() + ")", posX, posY + 5);
				break;
			}
			else {
				i++;
				int j=0;
				for (; (j+i)<steps.length; j++) {
					ProofRule tmpRule = steps [i+j].getRule();
					if (!tmpRule.getName().equals(r.getName())) {
						break;
					}
				}
				String ruleString = "(" + r.getName() + ")";
				g2d.drawString(ruleString, posX, posY - 5);
				posX += ruleFontMetrics.stringWidth(ruleString);
				
				if (j >= 1) {
					ruleString = "" + (j + 1);
					int tmpY = posY - this.ruleFontMetrics.getAscent() + expFM.getAscent() - expFM.getDescent() - 5;
					g2d.setFont(expF);
					g2d.drawString (ruleString, posX, tmpY);
					posX += expFM.stringWidth(ruleString);
					g2d.setFont(this.ruleFont);
				}
				posX += ruleFontMetrics.getDescent();
				i += j;
			}
		}
		
	}

	public static void setCenter(int center) {
		SmallStepNode.center = center;
	}
	
	public static int getCenter() {
		return SmallStepNode.center;
	}
	
	private void evaluateRule (ProofRule rule) {
		try {
			this.model.prove(rule, this.proofNode);
			this.ruleButton.setText("");
			this.ruleButton.setTextColor(Color.BLACK);
		} catch (ProofRuleException exc) {
			this.ruleButton.setTextColor(Color.RED);
		}
	}
	
	private void translateToCoreSyntax () {
		try {
			this.model.translateToCoreSyntax(this.proofNode);
		}
		catch (IllegalArgumentException exc) {
			System.out.println("already CoreSyntax");
		}
		this.ruleButton.setText("");
	}
}


