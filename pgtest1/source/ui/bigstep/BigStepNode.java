package ui.bigstep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import common.ProofRule;
import common.ProofStep;
import common.interpreters.InterpreterProofModel;
import expressions.Expression;
import expressions.Location;

import bigstep.BigStepProofNode;


import ui.AbstractNode;
import ui.beans.MenuButton;
import ui.beans.MenuButtonListener;
import ui.beans.StringComponent;
import ui.renderer.CompoundExpression;

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
	
	private ActionMenuItem				translateToCoreSyntax 			= null;
	
	private ActionMenuItem				actionGuess									= null;

	private StringComponent				locationIdComponent 				= null;
	
	private StringComponent				downArrowComponent					= null;
		
	private CompoundExpression		expression									= null;
	
	private CompoundExpression		resultExpression						= null;
	
	private MenuButton						menuButton									= null;
	
	private StringComponent				ruleString									= null;
	
	private GridBagLayout					layout											= null;
	
	private BigStepProofNode 			proofNode;
	
	private int 									locationId;
	

	public BigStepNode (BigStepProofNode proofNode) {
		super ();
		
		this.proofNode = proofNode;
		this.locationIdComponent 				= new StringComponent("(x)");
		this.downArrowComponent					= new StringComponent("\u21d3");
		this.expression									= new CompoundExpression<Location, Expression>(proofNode.getExpression());
		this.resultExpression						= new CompoundExpression<Location, Expression>();
		
		this.menuButton									= new MenuButton();
		this.ruleString									= new StringComponent("Rule");
		
		setLayout (null);
		
		this.expression.setUseColoring(true);
		this.resultExpression.setUseColoring(false);
		add (this.locationIdComponent);
		add (this.downArrowComponent);
		add (this.expression);
		add (this.resultExpression);
		
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
				this.resultExpression.setExpression(this.proofNode.getResult().getValue());
			}
			this.expression.setExpression(this.proofNode.getExpression());
		}
		
		if (((InterpreterProofModel)model).isMemoryEnabled()) {
			if (this.proofNode.getResult() != null) {
				this.resultExpression.setEnvironment(
						((BigStepProofNode)this.proofNode).getResult().getStore());
			}
			this.expression.setEnvironment(
					((BigStepProofNode)this.proofNode).getStore());
		}
	}
	
	public Dimension layout (int width) {
		int margin = 4;
		int spacing = 4;
		

		int completeNeededWidth = this.expression.getMaxExpressionDimension().width + 
															this.resultExpression.getMaxExpressionDimension().width;
		
		
		// remainginWidth is the origin width minus the id 
		// the down arrow and the margins and spacings 
		int remainingWidth = width - this.locationIdComponent.getPreferredSize().width - this.downArrowComponent.getPreferredSize().width;

		float expFactor = (float)this.expression.getMaxExpressionDimension().width / (float)completeNeededWidth;
		float resFactor = 1.0f - expFactor;

		Dimension size = null;
		
		Dimension expDim = this.expression.getDimensions((int)(expFactor * (float)remainingWidth));
		Dimension resDim = this.resultExpression.getDimensions((int)(resFactor * (float)remainingWidth));
		
		int maxHeight = expDim.height > resDim.height ? expDim.height : resDim.height;
		
		JComponent ruleComp;
		if (this.proofNode.getSteps ().length == 0) {
			ruleComp = this.menuButton;
			this.menuButton.setVisible(true);
			this.ruleString.setVisible(false);
		}
		else {
			ruleComp = this.ruleString;
			this.menuButton.setVisible(false);
			this.ruleString.setVisible(true);
		}
		
		size = new Dimension (
				margin * 2 + this.locationIdComponent.getPreferredSize().width +
				expDim.width +
				this.downArrowComponent.getPreferredSize().width +
				resDim.width,
				maxHeight + 2*margin + spacing + ruleComp.getPreferredSize().height);
		
		setSize (size);
		setPreferredSize (size);
		setMinimumSize (size);
		setMaximumSize (size);
		
		int posX = margin;
		this.locationIdComponent.setBounds(posX, margin, this.locationIdComponent.getPreferredSize().width, maxHeight);
		posX += this.locationIdComponent.getPreferredSize().width;
		
		this.expression.setBounds(posX, margin, expDim.width, maxHeight);
		posX += expDim.width;
		
		this.downArrowComponent.setBounds(posX, margin, this.downArrowComponent.getPreferredSize().width, maxHeight);
		posX += this.downArrowComponent.getPreferredSize().width;
		
		this.resultExpression.setBounds(posX, margin, resDim.width, maxHeight);

		ruleComp.setBounds(this.locationIdComponent.getX() + this.locationIdComponent.getWidth(), margin + maxHeight + spacing, ruleComp.getPreferredSize().width, ruleComp.getPreferredSize().height);
		
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
