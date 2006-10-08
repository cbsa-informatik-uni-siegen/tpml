package de.unisiegen.tpml.graphics.smallstep;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofStep;
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
	
	private Expression																currentUnderlineExpression;
	
	private MouseMotionAdapter												underlineThisAdapter;
	
	private MouseMotionAdapter												underlineRuleAdapter;
	
	private enum Direction {
		DIRECTION_PARENT,
		DIRECTION_CHILD,
	}

	public SmallStepNodeComponent (SmallStepProofNode 	proofNode, 
																 SmallStepProofModel 	proofModel,
																 LanguageTranslator		translator,
																 int spacing) {
		
		this.proofNode 									= proofNode;
		
		this.proofModel									= proofModel;
		
		this.translator									= translator;
		
		this.currentUnderlineExpression	= null;
		
		// the dimension for the rules initialy (0, 0)
		this.ruleDimension							= new Dimension (0, 0);
		
		// the dimension for the expression initialy (0, 0)
		this.expressionDimension				= new Dimension (0, 0);
		
		
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

		
		
		// create the adapters that will be used to determine 
		// whether an expression needs to get underlined
		this.underlineThisAdapter = new MouseMotionAdapter () {
			@Override
			public void mouseMoved (MouseEvent event) {
				SmallStepNodeComponent.this.updateUnderlineExpression((Expression)null);
			}
		};
		
		this.underlineRuleAdapter = new MouseMotionAdapter () {
			@Override
			public void mouseMoved (MouseEvent event) {
				if (event.getSource () instanceof SmallStepRuleLabel) {
					SmallStepRuleLabel label = (SmallStepRuleLabel)event.getSource();
					SmallStepNodeComponent.this.updateUnderlineExpression(label);
				}
				else if (event.getSource () instanceof MenuButton) {
					MenuButton button = (MenuButton)event.getSource ();
					SmallStepNodeComponent.this.updateUnderlineExpression(button);
				}
			}
		};
		
		this.addMouseMotionListener(this.underlineThisAdapter);
		this.expression.addMouseMotionListener(this.underlineThisAdapter);
		this.rules.getMenuButton().addMouseMotionListener(this.underlineRuleAdapter);
	}
	
	private void updateUnderlineExpression (Expression expression) {
		if (this.currentUnderlineExpression == expression) {
			return;
		}
		
		this.currentUnderlineExpression = expression;
		
		this.expression.setUnderlineExpression(this.currentUnderlineExpression);
		
		// free all the other nodes
		freeUnderliningSibling(true, Direction.DIRECTION_CHILD);
		freeUnderliningSibling(true, Direction.DIRECTION_PARENT);
		
	}
	
	private void updateUnderlineExpression (SmallStepRuleLabel label) {
		updateUnderlineExpression (label.getStepExpression());
	}
	
	private void updateUnderlineExpression (MenuButton button) {
		ProofStep[] steps = this.proofModel.remaining(this.proofNode);
		
		if (steps.length == 0) {
			return;
		}
		
		updateUnderlineExpression (steps [0].getExpression());
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
			
			String[] answers = { "Just outer", "All", "Cancel" };
			int n = JOptionPane.showOptionDialog(getParent(), "Translate to core syntax just " +
					"on the outermost expression or on the entire Expression?",
					"Translate to core syntax",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					answers,
					answers[0]);
			switch (n) {
			case 0:
				freeUnderliningSibling(false, Direction.DIRECTION_CHILD);
				freeUnderliningSibling(false, Direction.DIRECTION_PARENT);
				this.proofModel.translateToCoreSyntax(this.proofNode, false);
				break;
			case 1:
				freeUnderliningSibling(false, Direction.DIRECTION_CHILD);
				freeUnderliningSibling(false, Direction.DIRECTION_PARENT);
				this.proofModel.translateToCoreSyntax(this.proofNode, true);
				break;
			case 2:
				break;
			}
					
					
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
		this.ruleDimension = this.rules.getNeededSize(this.underlineRuleAdapter);
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
	
	private void freeUnderliningSibling (boolean ignoreThis, Direction direction) {
		if (!ignoreThis) {
			this.expression.setUnderlineExpression(null);
		}

		SmallStepProofNode nextNode = null;
		switch (direction) {
		case DIRECTION_CHILD:
			try {
				nextNode = this.proofNode.getFirstChild();
			} catch (Exception e) {
			}
			break;
		case DIRECTION_PARENT:
			nextNode = this.proofNode.getParent();
			break;
		}
		
		if (nextNode == null) {
			// no next node, so we're done here
			return;
		}
		
		SmallStepNodeComponent nextNodeComponent = (SmallStepNodeComponent)nextNode.getUserObject();
		nextNodeComponent.freeUnderliningSibling(false, direction);
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
	
	/*
	private void fireRepaintAll () {
		Object[] listeners = this.listenerList.getListenerList();
		for (int i=0; i<listeners.length; i+=2) {
			if (listeners [i] != SmallStepNodeListener.class) {
				continue;
			}
			
			((SmallStepNodeListener)listeners [i+1]).repaintAll();
		}
	}
	*/
	
	
}
