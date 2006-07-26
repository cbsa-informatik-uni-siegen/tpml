package ui.bigstep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import common.ProofRule;
import common.ProofStep;

import bigstep.BigStepProofNode;


import ui.AbstractNode;
import ui.beans.MenuButton;
import ui.beans.MenuButtonListener;
import ui.renderer.ExpressionComponent;
import ui.renderer.ExpressionRenderer;

public class BigStepNode extends AbstractNode {
	private enum ActionType {
		TranslateToCoresyntax,
		Guess,
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
			case Guess:
				return "Guess";
			}
			return "narf";
		}
		
		public ActionType getActionType () {
			return type;
		}
	}
	
	private ActionMenuItem			translateToCoreSyntax = null;
	
	private ActionMenuItem			actionGuess						= null;

	private StringComponent			locationIdComponent 	= null;
	
	private StringComponent			downArrowComponent		= null;
	
	private ExpressionComponent	expressionComponent		= null;
	
	private ExpressionComponent	resultComponent				= null;
	
	private MenuButton					menuButton						= null;
	
	private StringComponent			ruleString						= null;
	
	private GridBagLayout				layout								= null;
	
	private BigStepProofNode 		proofNode;
	
	private int 								locationId;
	
	private ExpressionRenderer	expRenderer;	

	public BigStepNode (BigStepProofNode proofNode) {
		super ();
		
		this.proofNode = proofNode;
		this.locationIdComponent 	= new StringComponent("(x)");
		this.downArrowComponent		= new StringComponent("\u21d3");
		this.expressionComponent	= new ExpressionComponent(proofNode.getExpression());
		this.resultComponent			= new ExpressionComponent(null);
		this.menuButton						= new MenuButton();
		this.ruleString						= new StringComponent("Rule");
		
		setLayout (null);
		
		add (this.locationIdComponent);
		add (this.downArrowComponent);
		add (this.expressionComponent);
		add (this.resultComponent);
		add (this.menuButton);
		add (this.ruleString);
		
		this.menuButton.addMenuButtonListener(new MenuButtonListener() {
			public void menuItemActivated (MenuButton source, JMenuItem item) {
				if (item instanceof RuleMenuItem) {
					evaluateRule(((RuleMenuItem)item).getRule());
				}
				else if (item instanceof ActionMenuItem) {
					ActionMenuItem ami = (ActionMenuItem)item;
					switch (ami.getActionType()) {
					case TranslateToCoresyntax:
						translateToCoreSyntax();
						break;
					case Guess:
						guessNode ();
					}
				}

			}
			
			public void menuClosed (MenuButton source) { }

		});
	}
	
	public void buildMenu () {
		JPopupMenu menu = new JPopupMenu();
		
		ProofRule rules[] = this.model.getRules();
		for (ProofRule r : rules) {
			menu.add(new RuleMenuItem (r));
		}
		
		menu.addSeparator();
		

		this.translateToCoreSyntax = new ActionMenuItem (ActionType.TranslateToCoresyntax);
		menu.add(this.translateToCoreSyntax);
		if (!this.proofNode.containsSyntacticSugar()) {
			this.translateToCoreSyntax.setEnabled(false);
		}
		
		this.actionGuess = new ActionMenuItem (ActionType.Guess);
		menu.add(this.actionGuess);
		
		
		this.menuButton.setMenu(menu);

	}
	
	public void updateNode () {
		if (this.proofNode != null) {
			if (this.proofNode.getResult () != null) {
				this.resultComponent.setExpression(this.proofNode.getResult().getValue());
			}
			this.expressionComponent.setExpression(this.proofNode.getExpression());
		}
	}
	
	public Dimension layout (int width) {
		int margin = 4;
		int spacing = 4;
		
		
		// remainginWidth is the origin width minus the id 
		// the down arrow and the margins and spacings 
		int remainingWidth = width - this.locationIdComponent.getPreferredSize().width - this.downArrowComponent.getPreferredSize().width;
		remainingWidth -= 2 * margin + 3 * spacing;
		
		Dimension rd = this.resultComponent.getPreferredSize();
		Dimension ed = this.expressionComponent.getPreferredSize();
		
		if (rd.width + ed.width > width) {
			// XXX: When need looking... see here
			int mw = rd.width + ed.width;
			float fr = (float)rd.width / (float)mw;
			float fe = (float)ed.width / (float)mw;
			
			float factor = (float)remainingWidth / (fe * (float)ed.width + fr * (float)rd.width);
			
			float size = (float)rd.width * fr * factor;
			rd = this.resultComponent.getSizeForWidth((int)size);
			
			size = (float)ed.width * fe * factor;
			ed = this.expressionComponent.getSizeForWidth((int)size);
			
		}
		
		int maxHeight = rd.height > ed.height ? rd.height : ed.height;
		
		int rowCount = 1;
		if (rd.height> ed.height) {
			rowCount = this.resultComponent.getRowCount();
		}
		else {
			rowCount = this.expressionComponent.getRowCount();
		}
		
		rowCount = rowCount  / 2 + 1; 
		
		Font arrowFont = downArrowComponent.getFont();
		arrowFont = getFont().deriveFont(Font.PLAIN, getFont().getSize2D() * (float)rowCount);
		this.downArrowComponent.setFont(arrowFont);

		
		JComponent ruleComp;
		if (this.proofNode.getSteps().length == 0) {
			ruleComp = this.menuButton;
			this.menuButton.setVisible (true);
			this.ruleString.setVisible (false);
		}
		else {
			ruleComp = this.ruleString;
			this.menuButton.setVisible (false);
			this.ruleString.setVisible (true);
		}
		Dimension size = new Dimension (
				margin * 2 + 3 * spacing +
				this.locationIdComponent.getPreferredSize().width +
				ed.width + 
				this.downArrowComponent.getPreferredSize().width + 
				rd.width,
				maxHeight + 2*margin + spacing + ruleComp.getPreferredSize().height);
		
		if (ruleComp.getPreferredSize().width + margin + spacing + this.locationIdComponent.getPreferredSize().width > size.width) {
			size.width = ruleComp.getPreferredSize().width + margin + spacing + this.locationIdComponent.getPreferredSize().width;
		}
		
		setSize (size);
		setPreferredSize (size);
		setMinimumSize (size);
		setMaximumSize (size);
		
		this.locationIdComponent.setBounds(
				margin, 
				margin, 
				this.locationIdComponent.getPreferredSize().width,
				maxHeight);
		
		this.expressionComponent.setBounds(
				margin + spacing + this.locationIdComponent.getPreferredSize().width,
				margin,
				ed.width,
				maxHeight);
		
		this.downArrowComponent.setBounds(
				margin + 2*spacing + this.locationIdComponent.getPreferredSize().width + ed.width,
				margin,
				this.downArrowComponent.getPreferredSize().width,
				maxHeight);
		
		this.resultComponent.setBounds(
				margin + 3*spacing + this.locationIdComponent.getPreferredSize().width + ed.width + this.downArrowComponent.getPreferredSize().width,
				margin,
				rd.width,
				maxHeight);
		
		ruleComp.setBounds(
				margin + spacing + this.locationIdComponent.getPreferredSize().width,
				margin + maxHeight + spacing,
				ruleComp.getPreferredSize().width,
				ruleComp.getPreferredSize().height);

		
		return size;
		
	}
	
	public void setLocationId (int id) {
		this.locationId = id;
		this.locationIdComponent.setString("(" + id + ")");
	}
	
	public void evaluateRule (ProofRule rule) {
		try {
			// prepare the view that something is about to change next
			this.fireBigStepNodeAboutToProve();

			this.model.prove(rule, this.proofNode);
			
			ProofStep[] steps = this.proofNode.getSteps();
			if (steps.length != 0) {
				this.ruleString.setString("(" + steps [0].getRule().getName() + ")");
			}
			
		} catch (Exception e) { 

			this.menuButton.setTextColor(Color.RED);
		}
	}
	
	public void translateToCoreSyntax() {
		// prepare the view that something is about to change next
		this.fireBigStepNodeAboutToProve();
		this.model.translateToCoreSyntax(this.proofNode);
	}
	
	public void guessNode () {
		try {
			// prepare the view that something is about to change next
			this.fireBigStepNodeAboutToProve();
			
			this.model.guess(this.proofNode);
			
			ProofStep[] steps = this.proofNode.getSteps();
			if (steps.length != 0) {
				this.ruleString.setString("(" + steps [0].getRule().getName() + ")");
			}
			
		} catch (Exception e) {
			
		}
	}
	
	public Point getArrowPoint() {
		Rectangle rect = this.locationIdComponent.getBounds();
		return new Point (rect.x + rect.width / 2, rect.y + rect.height);
	}
	
	public Point getJointPoint() {
		Rectangle rect = this.locationIdComponent.getBounds();
		return new Point (rect.x, rect.y + rect.height / 2);
	}
	
	public void addBigStepNodeListener (BigStepNodeListener listener) {
		this.listenerList.add(BigStepNodeListener.class, listener);
	}
	
	public void fireBigStepNodeAboutToProve () {
		Object[] listeners = this.listenerList.getListenerList();
		
		for (int i=0;i<listeners.length; i+=2) {
			if (listeners[i+0] == BigStepNodeListener.class) {
				
				BigStepNodeListener listener = (BigStepNodeListener)listeners[i+1];
				if (listener == null) continue;
				
				listener.aboutToProve(this.proofNode);
			}
		}
		
	}
}
