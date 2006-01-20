package ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ListIterator;
import java.util.Vector;
import java.util.EventObject;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.swing.event.EventListenerList;

import smallstep.*;


public class SmallStepModel {
	
	public static final int EVAL_FINAL				= 1;
	public static final int EVAL_EMPTY_CHAIN		= -1;
	public static final int EVAL_OK					= 0;
	
	public static final int ROLE_RULE			= 0;
	public static final int ROLE_EXPR			= 1;
	public static final int ROLE_KEYWORD		= 2;
	public static final int ROLE_CONSTANT		= 4;
	public static final int ROLE_UNDERLINE		= 5;
	public static final int ROLE_RULE_EXP		= 6;
	
	public class Step {
		private Expression		expression;
		
		private Expression		underlineExpression;
		
		private RuleChain		ruleChain;
		
		private PrettyString	prettyString;
		
		private int				evaluatedMetaRules;
		
		private boolean			evaluatedAxiomRule;
		
		private Rectangle[]		rectangles;
		
		public Step (Expression expression, RuleChain ruleChain) {
			this.expression = expression;
			this.underlineExpression = null;
			this.ruleChain 	= ruleChain;
			this.prettyString = expression.toPrettyString();
			this.evaluatedAxiomRule = false;
			this.evaluatedMetaRules = 0;
			this.rectangles = new Rectangle [this.ruleChain.getRules().size()];
			for (int i=0; i<this.rectangles.length; i++) {
				this.rectangles[i] = new Rectangle (0, 0, 0, 0);
			}
		}
		
		public void setUnderlineExpression(Expression expression) {
			this.underlineExpression = expression;
		}
		
		public Expression getUnderlineExpression() {
			return this.underlineExpression;
		}
		
		public Expression getExpression() {
			return this.expression;
		}
		
		public RuleChain getRuleChain() {
			return this.ruleChain;
		}
		
		public PrettyString getPrettyString() {
			return this.prettyString;
		}
		
		public void setEvaluatedMetaRules(int evaluatedMetaRules) {
			this.evaluatedMetaRules = evaluatedMetaRules;
		}
		
		public int getEvaluatedMetaRules() {
			return this.evaluatedMetaRules;
		}
		
		public void setEvaluatedAxiomRules(boolean evaluatedAxiomRules) {
			this.evaluatedAxiomRule = evaluatedAxiomRules;
		}
		
		public boolean getEvaluatedAxiomRules() {
			return this.evaluatedAxiomRule;
		}
		
		public int getNumberOfMetaRules () {
			if (this.ruleChain.isEmpty() || this.ruleChain.getRules().size() == 1) {
				return 0;
			}
			
			return this.ruleChain.getRules().size() -1;
		}
		
		public void setRectangle(int idx, Rectangle rect) {
			if (idx >= 0 && idx < this.rectangles.length) {
				this.rectangles[idx] = rect;
			}
		}
		
		public Rectangle getRectangle(int idx) {
			if (idx >= 0 && idx < this.rectangles.length) {
				return this.rectangles[idx];
			}
			return new Rectangle(0, 0, 0, 0);
		}
		
		public int getNumberOfRectangles() {
			return this.rectangles.length;
		}
	}
	
	private Vector<Step>	steps = new Vector<Step>();
	/**
	 * This is the absolut origin expression still containing the syntactical
	 * sugar.
	 */
	private Expression 	originExpression;
	
	/**
	 * True then  the user desided to release the syntactical sugar.
	 */
	private boolean		syntacticalSugarReleased;
	
	/**
	 * 
	 */
	private boolean		justAxioms;
	
	/**
	 * 
	 */
	private Font		expressionFont;
	
	/**
	 */
	private Font		keywordFont;
	
	/**
	 */
	private Font		constantFont;
	
	/**
	 * 
	 */
	private Font		ruleExpFont;
	
	/**
	 * 
	 */
	private Font		origFont;

	/**
	 * 
	 */
	private EventListenerList			listenerList = new EventListenerList();
	
	public SmallStepModel(Expression e) {
		this.originExpression 			= e;
		this.syntacticalSugarReleased 	= false;
		this.justAxioms					= true;
		this.origFont					= null;
		
		// initiate the first step that just would be the origin expression
		// with an empty rulechain
		steps.add (new Step (this.originExpression, new RuleChain ()));
		
		
		Preferences prefs = Preferences.userNodeForPackage(SmallStepGUI.class);
		this.justAxioms = prefs.getBoolean("ssJustAxioms", true);
		prefs.addPreferenceChangeListener(new PreferenceChangeListener() {
			public void preferenceChange(PreferenceChangeEvent event) {
				if (event.getKey().equals("ssJustAxioms")) {
					SmallStepModel.this.justAxioms = event.getNewValue().equals("true");
				}
			}
		});

		// now the first step has be evaluated to produce some output
		evaluateNextStep ();
	}

	public boolean releaseSyntacticalSugar () {
		// when the first step is evaluated the used has desided not to
		// release the syntactical sugar.
		if (steps.size() != 2) {
			return false;
		}
		
		if (this.originExpression.containsSyntacticSugar() == false) {
			return false;
		}
		
		// now we can savely release the syntactical sugar. For that we 
		// remove the last entry of the list, release the sugar put it
		// in the list, and evaluate the last step again
		steps.remove(1);
		
		Expression expr = this.originExpression.translateSyntacticSugar();
		
		steps.add(new Step(expr, new RuleChain ()));
		
		// now we can re-evaluate the next step
		// XXX later we can save and reset all the already evaluated rules 
		// in the step
		evaluateNextStep();
		
		return true;
	}
	
	public int evaluateNextStep() {
		Step step = steps.lastElement();
		Expression expr = step.getExpression();
		
		if ((expr instanceof Value) || (expr instanceof Exn)) {
			fireStepEvaluated();
			return EVAL_FINAL;
		}
		
		RuleChain chain = new RuleChain();
		Expression expression = expr.evaluate(chain);
		
		if (chain.isEmpty()) 
			return EVAL_EMPTY_CHAIN;
		
		steps.add(new Step(expression, chain));
		if (this.justAxioms) {
			completeMetaRules();
		}
		fireStepEvaluated();
		return EVAL_OK;
	}
	
	public void setFont(Font font) {
		this.origFont = font;
		this.keywordFont = font.deriveFont(1.3f * font.getSize2D());
		this.constantFont = font.deriveFont(1.2f * font.getSize2D());
		this.expressionFont = font.deriveFont(1.2f * font.getSize2D());
		this.ruleExpFont = this.expressionFont.deriveFont(this.expressionFont.getSize2D() * 0.75f);
	}
	
	public void completeMetaRules() {
		Step step = steps.lastElement();
		if (!step.getRuleChain().isEmpty()) {
			step.setEvaluatedMetaRules(step.getRuleChain().getRules().size()-1);
		}
		fireContentsChanged();
	}
	
	public Font getOrigFont() {
		return this.origFont;
	}
	
	public Expression getOrigExpression() {
		return this.originExpression;
	}
	
	
	public int completeLastStep() {
		Step step = steps.lastElement();
		step.setEvaluatedAxiomRules(true);
		
		if (!step.getRuleChain().isEmpty()) {
			step.setEvaluatedMetaRules(step.getRuleChain().getRules().size()-1);
		}
	
		fireContentsChanged();
		return evaluateNextStep();
	}
	
	public void completeSteps(int numSteps) {
		while (numSteps > 0 && completeLastStep() == SmallStepModel.EVAL_OK) --numSteps;
	}
	
	public void completeAllSteps() {
		while (completeLastStep() == SmallStepModel.EVAL_OK);
	}
	
	public int getNumberOfSteps() {
		return this.steps.size();
	}
	
	public void setJustAxioms(boolean justAxioms) {
		this.justAxioms = justAxioms;
	}
	
	public boolean getJustAxioms() {
		return this.justAxioms;
	}
	
	public void ruleSelectionChanged(String string) {
		if (steps.size () == 0) {
			return;
		}
		
		Step step = steps.lastElement();
		if (step.getEvaluatedMetaRules() < step.getNumberOfMetaRules()) {
			// not all meta rules are evaluated
			
			// even if you not belive, this line check if the last, not evaluated,
			// rule of the meta rules matches the selected
			if (step.getRuleChain().getRules().get(step.getEvaluatedMetaRules()).getName().equals(string)) {
				step.setEvaluatedMetaRules(step.getEvaluatedMetaRules()+1);
				fireContentsChanged();
			}
		}
		else {
			// all meta rules are evaluated so it has to be an axiom rule
			if (step.getRuleChain().getRules().getLast().getName().equals(string)) {
				step.setEvaluatedAxiomRules(true);
				evaluateNextStep();
				fireContentsChanged();
			}
		}
	}
	
	public PrettyString getPrettyString(int idx) {
		if (idx >= steps.size ()) {
			return null;
		}
		
		Step step = steps.elementAt(idx);
		
		return step.getPrettyString();
	}
	
	public Expression getUnderlineExpression(int idx) {
		if (idx >= steps.size()) {
			return null;
		}
		
		Step step = steps.elementAt(idx);
		return step.getUnderlineExpression();
	}
	
	public int getNumberOfMetaRules(int idx) {
		if (idx >= steps.size ()) {
			return 0;
		}
		
		Step step = steps.elementAt(idx);
		
		int value = step.getRuleChain().getRules().size();
		if (value > 0) {
			value--;
		}
		
		return value;
	}
	
	public int getNumberOfEvaluatedMetaRules(int idx) {
		if (idx >= steps.size ()) {
			return 0;
		}
		
		Step step = steps.elementAt(idx);
		
		return step.getEvaluatedMetaRules();
	}
	
	
	public Rule getMetaRule(int stepIdx, int idx) {
		if (stepIdx >= steps.size ()) {
			return null;
		}
		
		Step step = steps.elementAt(stepIdx);
		
		int value = step.getRuleChain().getRules().size();
		if (value > 0) {
			value--;
		}
	
		if (idx >= value) {
			return null;
		}
		
		return step.getRuleChain().getRules().get(idx);
	}
	
	public Rule getAxiomRule(int stepIdx) {
		if (stepIdx >= steps.size ()) {
			return null;
		}
		
		Step step = steps.elementAt(stepIdx);
		
		if (step.getRuleChain().isEmpty()) {
			return null;
		}
		
		return step.getRuleChain().getRules().getLast();
	}
	
	public boolean getAximoRulesEvaluted(int stepIdx) {
		if (stepIdx >= steps.size ()) {
			return false;
		}
		
		Step step = steps.elementAt(stepIdx);
		
		return step.getEvaluatedAxiomRules();
	}
	
	public boolean getEmptyRules(int stepIdx) {
		if (stepIdx >= steps.size ()) {
			return false;
		}
		Step step = steps.elementAt(stepIdx);
		return (step.getRuleChain().isEmpty());
	}
	
	public String[] getMetaRules() {
		String[] rules = new String [13];
		
		rules[0] = "---";
		rules[1] = "APP-LEFT";
		rules[2] = "APP-RIGHT";
		rules[3] = "COND-EVAL";
		rules[4] = "AND-EVAL";
		rules[5] = "OR-EVAL";
		rules[6] = "LET-EVAL";
		rules[7] = "APP-LEFT-EXN";
		rules[8] = "APP-RIGHT-EXN";
		rules[9] = "COND-EVAL-EXN";
		rules[10] = "AND-EVAL-EXN";
		rules[11] = "OR-EVAL-EXN";
		rules[12] = "LET-EVAL-EXN";
		
		return rules;
	}
	
	public String[] getAxiomRules() {
		String[] rules = new String[11];
		
		rules[0] = "---";
		rules[1] = "OP";
		rules[2] = "BETA-V";
		rules[3] = "COND-TRUE";
		rules[4] = "COND-FALSE";
		rules[5] = "AND-TRUE";
		rules[6] = "AND-FALSE";
		rules[7] = "OR-TRUE";
		rules[8] = "OR-FALSE";
		rules[9] = "LET-EXEC";
		rules[10] = "UNFOLD";
		
		return rules;
	}
	
	public Font getFontRole(int role) {
		Font font = this.expressionFont;
		switch (role) {
		case ROLE_RULE:
		case ROLE_EXPR:
			break;
		case ROLE_KEYWORD:
			font = this.keywordFont;
			break;
		case ROLE_CONSTANT:
			font = this.constantFont;
			break;
		case ROLE_RULE_EXP:
			font = this.ruleExpFont;
		}
		return font;
	}
	
	public Color getColorRole(int role) {
		Color color = null;
		switch (role) {
		case ROLE_RULE:
			color = new Color(0.0f, 0.0f, 0.0f);
			break;
		case ROLE_EXPR:
			color = new Color(0.0f, 0.0f, 0.0f);
			break;
		case ROLE_KEYWORD:
			color = new Color(0.5f, 0.0f, 0.0f);
			break;
		case ROLE_CONSTANT:
			color = new Color(0.0f, 0.0f, 0.5f);
			break;
		case ROLE_UNDERLINE:
			color = new Color(1.0f, 0.0f, 0.0f);
			break;
		}
		return color;
	}
	
	public void setRectangle(int stepId, int ruleId, Rectangle rect) {
		if (stepId >= 0 && stepId < steps.size()) {
			steps.get(stepId).setRectangle(ruleId, rect);
		}
	}
	
	public Rectangle getRectangle(int stepId, int ruleId) {
		if (stepId >= 0 && stepId < steps.size ()) {
			return steps.get(stepId).getRectangle(ruleId);
		}
		return new Rectangle (0, 0, 0, 0);
	}
	
	public int getNumberOfRectangles(int stepId) {
		if (stepId >= 0 && stepId < steps.size ()) {
			return steps.get(stepId).getNumberOfRectangles();
		}
		return 0;
	}
	
	public Rule getRule(int stepId, int ruleId) {
		if (stepId >= 0 && stepId < steps.size ()) {
			RuleChain chain = steps.get(stepId).getRuleChain();
			if (ruleId >= 0 && ruleId < chain.getRules().size()) {
				return chain.getRules().get(ruleId);
			}
		}
		return null;
	}
	
	public void setUndelineExpression(int stepId, Expression expression) {
		if (stepId >= 0 && stepId < steps.size ()) {
			steps.get(stepId).setUnderlineExpression(expression);
		}
	}
	public void clearUndelines() {
		ListIterator<Step> it = steps.listIterator();
		while (it.hasNext()) {
			Step step = it.next();
			step.setUnderlineExpression(null);
		}
	}
	
	public void addSmallStepEventListener(SmallStepEventListener e) {
		listenerList.add(SmallStepEventListener.class, e);
	}
	
	public void removeSmallStepEventListener(SmallStepEventListener e) {
		listenerList.remove(SmallStepEventListener.class, e);
	}

	public void fireContentsChanged() {
		Object[] listeners = listenerList.getListenerList();
		
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SmallStepEventListener.class) {
	             // Lazily create the event:
	             ((SmallStepEventListener)listeners[i+1]).contentsChanged(new EventObject(this));
	         }
	     }
	}
	
	public void fireStepEvaluated() {
		Object[] listeners = listenerList.getListenerList();
		
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SmallStepEventListener.class) {
	             // Lazily create the event:
	             ((SmallStepEventListener)listeners[i+1]).stepEvaluated(new EventObject(this));
	         }
	     }
	}
}
