package de.unisiegen.tpml.graphics.smallstep;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofNode;
import de.unisiegen.tpml.graphics.RuleComparator;
import de.unisiegen.tpml.graphics.components.CompoundExpression;
import de.unisiegen.tpml.graphics.components.MenuButton;
import de.unisiegen.tpml.graphics.components.MenuButtonListener;
import de.unisiegen.tpml.graphics.components.MenuGuessItem;
import de.unisiegen.tpml.graphics.components.MenuRuleItem;
import de.unisiegen.tpml.graphics.components.MenuTranslateItem;

public class SmallStepNodeComponent extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5536947349690384851L;
	
	private SmallStepProofNode												proofNode;
	
	private SmallStepProofModel												proofModel;
	
	private SmallStepRulesComponent										rules;
	
	private Dimension																	ruleDimension;
	
	private int																				actualRuleHeight;
	
	private CompoundExpression<Location, Expression>	expression;
	
	private Dimension																	expressionDimension;
	
	private int																				actualExpressionHeight;
	
	private Point																			origin;

	private boolean																		memoryEnabled;
	
	private int																				spacing;
	
	private MenuTranslateItem													translateItem;
	
	private LanguageTranslator												translator;

	public SmallStepNodeComponent (SmallStepProofNode 	proofNode, 
																 SmallStepProofModel 	proofModel,
																 LanguageTranslator		translator,
																 int spacing) {
		
		this.proofNode 						= proofNode;
		
		this.proofModel						= proofModel;
		
		this.translator						= translator;
		
		// the dimension for the rules initialy (0, 0)
		this.ruleDimension				= new Dimension (0, 0);
		
		// the dimension for the expression initialy (0, 0)
		this.expressionDimension	= new Dimension (0, 0);
		
		
		this.expression = new CompoundExpression<Location, Expression> ();
		add (this.expression);
		
		this.rules			= new SmallStepRulesComponent (proofNode);
		add (this.rules);
		
		this.memoryEnabled 	= this.proofModel.isMemoryEnabled();
		this.spacing				= 10;
		
		
		/*
		 * Fill the menu with menuitems
		 */
		
		JPopupMenu menu = new JPopupMenu ();
		ProofRule[] rules = this.proofModel.getRules();
		Arrays.<ProofRule>sort(rules, new RuleComparator());
		for (ProofRule r : rules) {
			menu.add(new MenuRuleItem (r));
		}
		menu.addSeparator();
		menu.add(new MenuGuessItem());
		menu.add(this.translateItem = new MenuTranslateItem());
		
		this.rules.getMenuButton().setMenu(menu);
		
		this.rules.getMenuButton().addMenuButtonListener(new MenuButtonListener () {
			public void menuClosed (MenuButton source) { }
			public void menuItemActivated (MenuButton source, JMenuItem item) {
				SmallStepNodeComponent.this.menuItemActivated(item);
			}
		});
	}
	
	private void menuItemActivated (JMenuItem item) {
		if (item instanceof MenuRuleItem) {
			MenuRuleItem ruleItem = (MenuRuleItem) item;
			ProofRule rule = ruleItem.getRule();
			
			try {
				this.proofModel.prove(rule, this.proofNode);
				this.rules.setRightRule();
			}
			catch (Exception exc) {
				this.rules.setWrongRule(rule);
				
			}
			fireNodeChanged ();
		}
		else if (item instanceof MenuGuessItem) {
			try {
				this.proofModel.guess(this.proofNode);
			} catch (Exception e) {
				
			}
		}
		else if (item instanceof MenuTranslateItem) {
			this.proofModel.translateToCoreSyntax(this.proofNode, false);
		}
	}
	
	/**
	 * Sets the top-left position where the stuff should be appear
	 * @param origin
	 */
	public void setOrigion (Point origin) {
		this.origin = origin;
	}
	
	public void setBounds () {
		setBounds (this.origin.x, this.origin.y, 
				this.ruleDimension.width + this.expressionDimension.width, 
				this.actualRuleHeight + this.actualExpressionHeight + this.spacing);
	}
	
	public void update () {
		
		this.expression.setExpression(this.proofNode.getExpression());
		if (this.memoryEnabled) {
			this.expression.setEnvironment(this.proofNode.getStore());
		}
		else {
			this.expression.setEnvironment(null);
		}
		
		
		this.translateItem.setEnabled(this.translator.containsSyntacticSugar(this.proofNode.getExpression(), false));
	}

	//
	// Stuff for the rules
	// 
	public Dimension getMinRuleSize () {
		this.ruleDimension = this.rules.getNeededSize();
		this.actualRuleHeight = this.ruleDimension.height;
		return this.ruleDimension;
	}
	
	public Dimension getRuleSize () {
		return this.ruleDimension;
	}
	
	public void setMaxRuleWidth (int maxRuleWidth) {
		this.rules.setActualWidth(maxRuleWidth);
		this.ruleDimension.width = maxRuleWidth;
	}
	
	public void setActualRuleHeight (int actualRuleHeight) {
		this.actualRuleHeight = actualRuleHeight;
	}
	
	public int getActualRuleHeight () {
		return this.actualRuleHeight;
	}
	
	public int getRuleTop () {
		return this.actualExpressionHeight + this.spacing;
	}
	
	public void hideRules () {
		this.rules.setVisible(false);
	}
	
	public void showRules () {
		this.rules.setVisible(true);
	}
	
	public void placeRules () {
		
		int top = getRuleTop() + (this.actualRuleHeight - this.ruleDimension.height) / 2;
		this.rules.setBounds(0, top, this.ruleDimension.width, this.ruleDimension.height);
	}
	
	
	//
	// Stuff for the expressions
	// 
	
	/**
	 * 
	 * @param maxWidth Max width is given for the entire component.
	 * @return
	 */
	public Dimension checkNeededExpressionSize (int maxWidth) {
		maxWidth -= this.ruleDimension.width + this.spacing;
		
		this.expressionDimension = this.expression.getNeededSize(maxWidth);
		this.expressionDimension.width += this.spacing;
		
		// use the calculated expression height for the actual height
		// until it will be changed by the SmallStepComponent
		this.actualExpressionHeight = this.expressionDimension.height;
		
		return this.expressionDimension;
	}
	
	public Dimension getExpressionSize () {
		return this.expressionDimension;
	}
	
	public void setActualExpressionHeight (int actualExpressionHeight) {
		this.actualExpressionHeight = actualExpressionHeight;
	}
	
	public int getActualExpressionHeight () {
		return this.actualExpressionHeight;
	}
	
	
	public void placeExpression () {
		this.expression.setBounds(this.ruleDimension.width + this.spacing, 0, this.expressionDimension.width, this.actualExpressionHeight);
	}
	
	
	public void addSmallStepNodeListener (SmallStepNodeListener listener) {
		this.listenerList.add(SmallStepNodeListener.class, listener);
	}
	
	public void removeSmallStepNodeListener (SmallStepNodeListener listener) {
		this.listenerList.remove(SmallStepNodeListener.class, listener);
	}
	
	private void fireNodeChanged () {
		Object[] listeners = this.listenerList.getListenerList();
		for (int i=0; i<listeners.length; i+=2) {
			if (listeners [i] != SmallStepNodeListener.class) {
				continue;
			}
			
			((SmallStepNodeListener)listeners [i+1]).nodeChanged(this);
		}
	}
	
}
