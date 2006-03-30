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
	
	/*
	enum SmallStepRole {
		ROLE_RULE,
		ROLE_EXPR,
		ROLE_KEYWORD,
		ROLE_CONSTANT,
		ROLE_UNDERLINE,
		ROLE_RULE_EXPR,
	}
	*/
	public static final int ROLE_RULE			= 0;
	public static final int ROLE_EXPR			= 1;
	public static final int ROLE_KEYWORD		= 2;
	public static final int ROLE_CONSTANT		= 3;
	public static final int ROLE_UNDERLINE		= 4;
	public static final int ROLE_RULE_EXP		= 5;
	
	/*
	public static final int ROLE_RULE			= 0;
	public static final int ROLE_EXPR			= 1;
	public static final int ROLE_KEYWORD		= 2;
	public static final int ROLE_CONSTANT		= 4;
	public static final int ROLE_UNDERLINE		= 5;
	public static final int ROLE_RULE_EXP		= 6;
	*/
	
	/**
	 * A single step represents the combination of the expression,
	 * the expression that represetnts the part that should be underlined.
	 * 
	 * @author marcell
	 *
	 */
	public class Step {
		/**
		 * The expression that should be drawn right of the arrow
		 */
		private Expression		expression;
		
		/**
		 * The expression that should be underlined or it is null.
		 */
		private Expression		underlineExpression;
		
		/**
		 * The rule that have to be selected correctly
		 */
		private RuleChain		ruleChain;
		
		/**
		 * The string that should be drawn right the arrow.
		 * This comes directly out of the expression.
		 */
		private PrettyString	prettyString;
		
		/**
		 * The number of evaluated meta rules. These rules will be rendered
		 * in text. The next meta rule, when one exists, will be shown as a
		 * comboBox with which the user can evaluate the meta rule. 
		 */
		private int				evaluatedMetaRules;
		
		/**
		 * Whether the axiom rule was correctly solved
		 */
		private boolean			evaluatedAxiomRule;
		
		/**
		 * One rectangle for each rule (meta rules and axiom).
		 * 
		 * This value is set by the SmallStepComponent.
		 */
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
		
		/**
		 * Sets the expression that should be underlined.
		 * 
		 * @param expression The expression to be underlined or null if no
		 * 					 expression should be underlined.
		 */
		public void setUnderlineExpression(Expression expression) {
			this.underlineExpression = expression;
		}
		
		/**
		 * Returns the expression that should be underlined.
		 * @return The expression that should be underlined. 
		 */
		public Expression getUnderlineExpression() {
			return this.underlineExpression;
		}
		
		/**
		 * Returns the expression that is bound to this step
		 * @return The expression that is bound to this step
		 */
		public Expression getExpression() {
			return this.expression;
		}
		
		/**
		 * Returns the entire rule chain.
		 * @return The entire rule chain
		 */
		public RuleChain getRuleChain() {
			return this.ruleChain;
		}
		
		/**
		 * Returns the pretty formated string of this step
		 * @return The pretty formated string of this step
		 */
		public PrettyString getPrettyString() {
			return this.prettyString;
		}
		
		/**
		 * Sets the number of evaluated meta rules this valua
		 * @param evaluatedMetaRules
		 */
		public void setEvaluatedMetaRules(int evaluatedMetaRules) {
			this.evaluatedMetaRules = evaluatedMetaRules;
		}

		/**
		 * Returns the number of meta rules that have been evaluated already
		 * @return The number of evaluated meta rules.
		 */
		public int getEvaluatedMetaRules() {
			return this.evaluatedMetaRules;
		}
		
		/**
		 * Sets whether the axiom has been evaluated 
		 * @param evaluatedAxiomRules Whether the axiom 
		 */
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
	 * True then the user has desided to release the syntactical sugar.
	 */
	private boolean		syntacticalSugarReleased;
	
	/**
	 * When the user has set that only axiom rules should be selected by the user.
	 * The this value has to be true else false.
	 * 
	 * When justAxioms is true then the evaluatedMetaRules of a Step is set to
	 * the maximum of possible meta rules (that should be ruleChain.getRules().size()
	 */
	private boolean		justAxioms;
	
	/**
	 * The font that should be used to render the expressions
	 */
	private Font		expressionFont;
	
	/**
	 * The font that should be used to rencer the keywords like: let, in, if, then, else ...
	 */
	private Font		keywordFont;
	
	/**
	 * The font that should be used to render constants
	 */
	private Font		constantFont;
	
	/**
	 * The font that should be used to render the evaluated rules on the left side
	 */
	private Font		ruleExpFont;
	
	/**
	 * The base font. All other fonts used in the SmallStep are derived from this font.
	 * But this one is never actualy used directly. 
	 */
	private Font		origFont;

	/**
	 * The listener list. All listeners that needed to be informed when things change.
	 */
	private EventListenerList			listenerList = new EventListenerList();
	
	/**
	 * Constructor. Adds the first step to the list of all steps, checks the
	 * preferences for the "justAxioms" flag and evaluates the first step.
	 * 
	 * @param e The origin base expressions
	 */
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

	/**
	 * This function releases the last of the steps 
	 * @return
	 */
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
		
		if (expr.isValue() || (expr instanceof Exn)) {
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
		String[] rules = new String [15];
		
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
        rules[13] = "TUPLE";
        rules[14] = "TUPLE-EXN";
		
		return rules;
	}
	
	public String[] getAxiomRules() {
		String[] rules = new String[14];
		
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
        rules[11] = "PROJ";
        rules[12] = "FST";
        rules[13] = "SND";
		
		return rules;
	}
	
	public Font getFontRole(int role) {
		ThemeManager themeManager = ThemeManager.get();
		Theme theme = themeManager.getCurrentTheme();
		
		Font font = theme.getItemFont(role);
		/*
		switch (role) {
		case ROLE_RULE:
			font = theme.getItemFont(ROLE_RULE);
			break;
		case ROLE_EXPR:
			font = theme.getItemFont(RULE_EXPR);
			break;
		case ROLE_KEYWORD:
			font = theme.getItemFont(R)
			break;
		case ROLE_CONSTANT:
			font = this.constantFont;
			break;
		case ROLE_RULE_EXP:
			font = this.ruleExpFont;
		}
		*/
		return font;
	}
	
	public Color getColorRole(int role) {
		ThemeManager themeManager = ThemeManager.get();
		Theme theme = themeManager.getCurrentTheme();
		
		Color color = theme.getItemColor(role);
		/*
		
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
		
		*/
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
