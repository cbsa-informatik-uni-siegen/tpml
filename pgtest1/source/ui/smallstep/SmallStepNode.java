package ui.smallstep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import common.ProofModel;
import common.ProofRule;
import common.ProofRuleException;
import common.ProofStep;
import expressions.Expression;

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
	
	private class RuleBound {
		public Rectangle	rect;
		public ProofRule	rule;
		public RuleBound (ProofRule rule, Rectangle rect) {
			this.rule = rule;
			this.rect = rect;
		}
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

	private static int 						center;
	
	private SmallStepView					view;
	
	private EnvironmentRenderer				envRenderer;
	
	private Dimension						envSize;
	
	private ExpressionRenderer				expRenderer;
	
	private Dimension						expSize;
	
	private Dimension						expEnvSize;
	
	private Dimension						expMaxSize;
	
	private Dimension						ruleSize;
	
	private Dimension						ruleMaxSize;
	
	private MenuButton						ruleButton;
	
	private Font							ruleFont;
	
	private FontMetrics						ruleFontMetrics;
	
	private ActionMenuItem					translateToCoreSyntax = null;
	
	private LinkedList<RuleBound>			ruleBounds;
	
	private Expression						underlineExpression = null;
		
	public SmallStepNode(SmallStepView view, SmallStepProofNode proofNode) {
		super();
		
		setLayout(null);
		
		this.ruleBounds = new LinkedList<RuleBound> ();
		this.view			= view;
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

		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved (MouseEvent event) {
				handleMouseMoved (event);
			}
		});
	}
	
	private void setUnderlineExpression (Expression expression, boolean applyToChildren) {
		if (this.underlineExpression != expression) {
			this.underlineExpression = expression;
			repaint();
		}
		if (applyToChildren) {
			SmallStepNode child = (SmallStepNode)getFirstChild ();
			if (child != null) {
				child.setUnderlineExpression (null, true);
			}
		}
		else {
			SmallStepNode parent = (SmallStepNode)getParentNode();
			if (parent != null) {
				parent.setUnderlineExpression (null, false);
			}
		}
	}
	
	private void handleMouseMoved (MouseEvent event) {
		for (RuleBound rb : this.ruleBounds) {
			if (rb.rect.contains(event.getX(), event.getY())) {
				ProofStep steps[] = this.proofNode.getSteps();
				for (ProofStep s : steps) {
					if (s.getRule() == rb.rule) {
						setUnderlineExpression(s.getExpression(), true);
						setUnderlineExpression(s.getExpression(), false);
						repaint();
						return;
					}
				}
				return;
			}
		}
		setUnderlineExpression(null, true);
		setUnderlineExpression(null, false);
	}
	
	public void reset () {
		resetRenderer();
		
		if (this.translateToCoreSyntax != null) {
			this.translateToCoreSyntax.setEnabled(this.proofNode.containsSyntacticSugar());
		}
	}
	
	private void resetRenderer() {
		this.expRenderer	= new ExpressionRenderer (this.proofNode.getExpression());
		
		Font fnt = new JComboBox().getFont();
		FontMetrics fntMetrics = getFontMetrics(fnt);
		
		this.expRenderer.setTextStyle(fnt, fntMetrics, Color.BLACK);
		this.expRenderer.setKeywordStyle(fnt, fntMetrics, Color.RED);
		this.expRenderer.setConstantStyle(fnt, fntMetrics, Color.BLUE);
		this.expRenderer.setUnderlineColor(Color.RED);
		this.expRenderer.checkFonts();
		this.expRenderer.checkAnnotationSizes();
		
		this.envRenderer	= new EnvironmentRenderer (this.proofNode.getStore());
		this.envRenderer.setFont(fnt, fntMetrics);
		
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
	
	
	public void placeMenuButtons() {
		if (this.proofNode.isProven()) {
			this.ruleButton.setVisible(false);
			return;
		}
		int heightDiv2 = this.expMaxSize.height + this.ruleMaxSize.height / 2;
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
	
	public Dimension getRuleSize () {
		return this.ruleSize;
	}
	
	public void prepareRuleSize() {
		int maxX = 0;
		int w = 0;
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
		this.ruleSize = new Dimension (maxX + 10 , height);
	}

	public Dimension getExpressionSize(int maxWidth) {
		return this.expSize;
	}
	
	public void prepareExpressionSize (int maxWidth) {
		if (model.isMemoryEnabled()) {
			this.envSize = envRenderer.getNeededSize();	
		}
		else {
			this.envSize = new Dimension (0, 0);
		}
		
		this.expSize = expRenderer.getNeededSize(maxWidth - this.envSize.width);
		
		this.expEnvSize = new Dimension (this.expSize);
		this.expEnvSize.width += this.envSize.width;
		if (this.envSize.height > this.expEnvSize.height) {
			this.expEnvSize.height = this.envSize.height;
		}

	}
	
	
	public int setTop (int posY) {
		int addHeight = this.ruleFontMetrics.getHeight ();
		AbstractNode aParent = getParentNode ();
		if (aParent != null) {
			SmallStepNode parent = (SmallStepNode)aParent;
			int height = this.expEnvSize.height;
			if (parent.ruleSize.height > height) {
				height = parent.ruleSize.height;
			}
			this.expMaxSize = new Dimension (this.expEnvSize.width, height + addHeight);
			parent.ruleMaxSize = new Dimension (SmallStepNode.center, height + addHeight);
			parent.finishSize(posY);
		}
		else {
			this.expMaxSize = new Dimension (this.expEnvSize.width, this.expEnvSize.height + addHeight);
		}
		
		AbstractNode aChild = getFirstChild();
		if (aChild == null) {
			this.ruleMaxSize = new Dimension (SmallStepNode.center, this.ruleSize.height + addHeight);
			finishSize (posY + this.expMaxSize.height);
		}
		return posY + this.expMaxSize.height;
	}
	
	private void finishSize (int posY) {
		int addWidth = this.ruleFontMetrics.getHeight ();
		int px = 25; //SmallStepNode.center - this.ruleMaxSize.width;
		int py = posY - this.expMaxSize.height;
		int dx = SmallStepNode.center + this.expMaxSize.width + addWidth;
		int dy = this.expMaxSize.height + this.ruleMaxSize.height;
		setBounds (px, py, dx, dy);
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
		
		// now draw the expression
		int heightDiv2 = this.expMaxSize.height / 2;
		int posX = center;
		int posY = heightDiv2 - expSize.height / 2;
		expRenderer.render(posX + this.ruleFontMetrics.getHeight(), posY, this.underlineExpression, g);
		if (model.isMemoryEnabled()) {
			posY = heightDiv2 - envSize.height / 2;
			envRenderer.render(posX + this.ruleFontMetrics.getHeight() + this.expSize.width, posY, g);
		}
					
		
		// draw a black arrow on the base
		heightDiv2 = this.expMaxSize.height + this.ruleMaxSize.height / 2;
		g.setColor(Color.BLACK);
		g.drawLine(0, heightDiv2, center, heightDiv2);
		g.drawLine(center, heightDiv2, center - 5, heightDiv2 - 5);
		g.drawLine(center, heightDiv2, center - 5, heightDiv2 + 5);
		
		posY = heightDiv2;
		
		// now draw the evaluated rules
		g.setFont(this.ruleFont);
		posX = 0;
		
		// clear the bounds
		this.ruleBounds.clear();
		
		g.setColor(Color.BLACK);
		Font		expF	= this.ruleFont.deriveFont(this.ruleFont.getSize2D());
		FontMetrics	expFM	= getFontMetrics (expF);
		ProofStep steps [] = this.proofNode.getSteps();
		for (int i=0; i<steps.length;) {
			ProofRule r = steps [i].getRule();
			if (r.isAxiom()) {
				posY = heightDiv2 + ruleFontMetrics.getAscent();
				posX = 0;
				int px = posX;
				int py = posY + 5 - ruleFontMetrics.getAscent();
				String ruleString = "(" + r.getName() + ")";
				g.drawString(ruleString, posX, posY + 5);
				posX += ruleFontMetrics.stringWidth(ruleString);
				int dx = posX - px;
				int dy = ruleFontMetrics.getHeight();
				this.ruleBounds.add(new RuleBound (r, new Rectangle (px, py, dx, dy)));
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
				int px = posX;
				int py = posY - 5 - ruleFontMetrics.getAscent();
				String ruleString = "(" + r.getName() + ")";
				g.drawString(ruleString, posX, posY - 5);
				posX += ruleFontMetrics.stringWidth(ruleString);
				int dx = posX - px;
				int dy = ruleFontMetrics.getHeight();
				if (j >= 1) {
					ruleString = "" + (j + 1);
					int tmpY = posY - this.ruleFontMetrics.getAscent() + expFM.getAscent() - expFM.getDescent() - 5;
					g.setFont(expF);
					g.drawString (ruleString, posX, tmpY);
					posX += expFM.stringWidth(ruleString);
					g.setFont(this.ruleFont);
				}
				else {
					this.ruleBounds.add(new RuleBound (r, new Rectangle (px, py, dx, dy)));
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
		String buttonText = this.ruleButton.getText();
		try {
			this.ruleButton.setText("");
			this.model.prove(rule, this.proofNode);
			this.ruleButton.setTextColor(Color.BLACK);
		} catch (ProofRuleException exc) {
			this.ruleButton.setText(buttonText);
			this.ruleButton.setTextColor(Color.RED);
			this.view.relayout();
		}
	}
	
	private void translateToCoreSyntax () {
		this.ruleButton.setText("");
		try {
			this.model.translateToCoreSyntax(this.proofNode);
		}
		catch (IllegalArgumentException exc) {
		}
	}
}


