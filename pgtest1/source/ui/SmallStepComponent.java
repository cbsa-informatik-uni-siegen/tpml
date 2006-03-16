package ui;

import java.util.*;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.EventListenerList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import java.awt.*;
import java.awt.event.*;
import smallstep.*;

public class SmallStepComponent extends JComponent {

	private SmallStepModel			model;
	
	public boolean 					underlineExpressions;
	
	/**
	 * A list of all horizontal lines
	 * 
	 * The horizontal line is the top line over every expression
	 */
	private int[]					verticalSizes;
	
	/**
	 * 
	 */
	private int[]					usedAnnotation;
	
	/**
	 * The center is describing the very far right end of the expression
	 * combos and the beginning of the expression string.
	 */
	private int						center;
	
	/**
	 * 
	 */
	private int						rightOutline;
	
	/**
	 */
	private Dimension 				ruleIntersection;
	
	
	/**
	 */
	private Dimension				comboSize;
	

	/**
	 */
	private Dimension				border;
	
	/**
	 */
	private JComboBox				comboBox;
	
	/**
	 */
	private boolean					upToDate;
	
	/**
	 *
	 */
	private int						knownNumberOfSteps;
	
	/**
	 * 
	 */
	private Renderer				renderer;
	
	/**
	 * 
	 */
	private int 					maxWidth;
	
	/**
	 * 
	 */
	private int 					lastSelectedStep = -1;
	
	/**
	 * 
	 */
	private int 					lastSelectedRule = -1;
	
		
	/**
	 *
	 */
	private boolean 				groupRules = true;
	
	public SmallStepComponent() {
		super();
		setLayout(null);
		
		this.model 					= null;
		this.underlineExpressions 	= true;
		this.verticalSizes 			= null;
		this.center 				= 0;
		this.rightOutline			= 0;
		this.ruleIntersection		= new Dimension (10, 20);
		this.comboSize				= new Dimension (100, 15);
		this.border					= new Dimension (10, 20);
		this.comboBox				= new JComboBox();
		this.upToDate				= false;
		this.knownNumberOfSteps		= -1;
		this.renderer				= null;
			
		add(this.comboBox);
		
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) { }
			public void mouseMoved(MouseEvent e) {
				handleMouseMoved(e);
			}
		});
		
		this.comboBox.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) { }
			public void mouseMoved(MouseEvent e) {
				int x = comboBox.getX() + comboBox.getWidth() / 2;
				int y = comboBox.getY() + comboBox.getHeight() / 2;
				
				handleMouseMoved(new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), 
						e.getModifiers(), x, y, e.getClickCount(), false));
			}
		});
	}
	
	public void setModel (SmallStepModel ssmodel) {
		this.model = ssmodel;
		
		this.model.addSmallStepEventListener(new SmallStepEventListener() {
			public void stepEvaluated(EventObject o) { }
			public void contentsChanged(EventObject o) {
				upToDate = false;
				repaint();
//				invalidate();
			}
		});
		this.comboBox.addPopupMenuListener(new PopupMenuListener () {
			public void popupMenuCanceled(PopupMenuEvent e) { }
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				model.ruleSelectionChanged((String)comboBox.getSelectedItem());
			}
		});

		
		this.renderer = new Renderer (getFontMetrics(model.getFontRole(SmallStepModel.ROLE_EXPR)),
				getFontMetrics(model.getFontRole(SmallStepModel.ROLE_KEYWORD)),
				getFontMetrics(model.getFontRole(SmallStepModel.ROLE_CONSTANT)),
				model.getColorRole(SmallStepModel.ROLE_EXPR),
				model.getColorRole(SmallStepModel.ROLE_KEYWORD),
				model.getColorRole(SmallStepModel.ROLE_CONSTANT));
	}
	
	public void setMaxWidth (int maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public int getMaxWidth() {
		return this.maxWidth;
	}
	
	public SmallStepModel getModel() {
		return this.model;
	}
	
	public void setUnderlineExpressions(boolean underlineExpressioins) {
		this.underlineExpressions = underlineExpressioins;
	}
	
	public boolean getUnderlineExpressions() {
		return this.underlineExpressions;
	}
	
	public void setNotUpToDate () {
		this.upToDate = false;
	}
	
	private void prepare() {
		if (model == null 
				|| renderer == null  
				|| (this.upToDate && model.getNumberOfSteps() == this.knownNumberOfSteps) 
				|| model.getNumberOfSteps () == 0) {
			return;
		}
		
		
		FontMetrics fm = this.getFontMetrics(model.getFontRole(SmallStepModel.ROLE_EXPR));
		FontMetrics ruleFm = this.getFontMetrics(model.getFontRole(SmallStepModel.ROLE_RULE));
		FontMetrics expFm = this.getFontMetrics(model.getFontRole(SmallStepModel.ROLE_RULE_EXP));
		
		
		this.verticalSizes = new int[model.getNumberOfSteps()+1];
		this.usedAnnotation = new int [model.getNumberOfSteps()];
		
		
		// first we check the center of the all steps
		this.center = 0;
		for (int i=0; i<model.getNumberOfSteps(); i++) {
			
			int currentCenter = 0;
			if (model.getEmptyRules(i)) {
				continue;
			}
			int numRules = model.getNumberOfEvaluatedMetaRules(i);
			for (int j=0; j<numRules; j++) {
				Rule rule = model.getMetaRule(i, j);
				String ruleText = rule.getName();
				int multiplier = 1;
				if (this.groupRules) {
					int k = 1;
					for (k = 1; (k+j)<numRules; k++) {
						String rt = model.getMetaRule(i, j + k).getName();
						// check if the next rule equals the current rule
						if (ruleText.equals(rt)) {
							continue;
						}
						// when this rule does not equal the current rule k will hold the multiplier for
						// this rule
						break;
					}
					multiplier = k;
				}
				int width = this.getRuleTextWidth(rule, ruleFm, expFm, multiplier) + this.ruleIntersection.width;
				currentCenter += width;
				j += (multiplier - 1);
			}
			
			if (numRules < model.getNumberOfMetaRules(i)) { 
				// add the space for the combobox
				currentCenter += this.comboSize.width + this.ruleIntersection.width;
			}
			else {
				int w = 0;
				if (model.getAximoRulesEvaluted(i)) {
					String rule = model.getAxiomRule(i).getName();
					w = ruleFm.stringWidth(rule) + this.ruleIntersection.width;
				}
				else {
					w = this.comboSize.width + this.ruleIntersection.width;
				}
				if (w > currentCenter) {
					currentCenter = w;
				}
			}
			if (currentCenter > this.center) {
				this.center = currentCenter;
			}
		}
		
		center += border.width;
		
		
		verticalSizes[0] = border.height;
		// now based on the center we can check the vertical sizes
		for (int i=0; i<model.getNumberOfSteps(); i++) {
			// this will be changed later
			int ruleBox = 2 * this.comboSize.height + this.ruleIntersection.height + 2 * this.border.height;
			
			Dimension d = renderer.getBestSize(model.getPrettyString(i), this.maxWidth - this.center);
			int expression = 2 * this.border.height + d.height;
			this.usedAnnotation[i] = renderer.getSelectedAnnotation();
			
			
			if (d.width + this.center > this.rightOutline) {
				this.rightOutline = d.width + this.center;
			}
			
			if (expression > ruleBox) {
				this.verticalSizes[i+1] = this.verticalSizes[i] + expression;
			}
			else {
				this.verticalSizes[i+1] = this.verticalSizes[i] + ruleBox;
			}
		}
		
		// now we try to find out where we should put the combobox first we disable it
		// to begin with a clear defined situation
		this.comboBox.setVisible(false);
		
		int rule = model.getNumberOfSteps () - 1;
		int numEvaluatedRules = model.getNumberOfEvaluatedMetaRules(rule);
		int numMetaRules = model.getNumberOfMetaRules(rule);
		int posy = (this.verticalSizes[rule] + this.verticalSizes[rule+1]) / 2; 
		if (numEvaluatedRules < numMetaRules) {
			int posx = 0;
			for (int j=0; j<numEvaluatedRules; j++) {
				String strRule = model.getMetaRule(rule, j).getName();
				posx += fm.stringWidth(strRule) + this.ruleIntersection.width;
			}
			posx += this.border.width;
			this.comboBox.setBounds(posx, posy - this.ruleIntersection.height / 2  -  this.comboSize.height, this.comboSize.width, this.comboSize.height);
			this.comboBox.setVisible(true);
			this.comboBox.setModel(new DefaultComboBoxModel (model.getMetaRules()));
			model.setRectangle(rule, numEvaluatedRules, this.comboBox.getBounds());
		}
		else {
			// meta rule evaluated
			if (!model.getAximoRulesEvaluted(rule)) {
				this.comboBox.setBounds(this.border.width, posy + this.ruleIntersection.height / 2,
						this.comboSize.width, this.comboSize.height);
				this.comboBox.setVisible (true);
				this.comboBox.setModel(new DefaultComboBoxModel (model.getAxiomRules()));
				model.setRectangle(rule, numMetaRules, this.comboBox.getBounds());
			}
		}
		this.upToDate = true;
		this.knownNumberOfSteps = model.getNumberOfSteps();
		setPreferredSize(new Dimension (this.rightOutline, verticalSizes[verticalSizes.length-1]));
		setSize(new Dimension (this.rightOutline, verticalSizes [verticalSizes.length-1]));
	}
	
	/**
	 * 
	 * @param g2d The graphics context
	 * @param x The position one would give it as a comboBox
	 * @param y The position one would give it as a comboBox
	 * @param rule The string to draw
	 * @param fm The fontmetrics that is used to calculate the position of the text
	 * @return The position behind the drawn string
	 */
	public int drawRuleText (Graphics2D g2d, int x, int y, Rule rule, FontMetrics fm, FontMetrics expFm, int multiplier) {
		int posy = y + this.comboSize.height - fm.getDescent();

		String str = "(" + rule.getName() + ")";
		g2d.setFont(fm.getFont());
		g2d.drawString(str, x, posy);
		x += fm.stringWidth(str);
		Font multiplierFont = fm.getFont().deriveFont(fm.getFont().getSize2D() * 0.75f);
		FontMetrics multiplierFontMetrics = getFontMetrics (multiplierFont);
		
		if (multiplier > 1) {
			posy -= fm.getAscent() / 2;
			str = "" + multiplier;
			g2d.setFont(expFm.getFont());
			g2d.setFont(multiplierFont);
			g2d.drawString(str, x, posy);
			x += multiplierFontMetrics.stringWidth(str);
		}
		
		return (x);
	}
	
	public int getRuleTextWidth(Rule rule, FontMetrics fm, FontMetrics expFm, int multiplier) {
		String str = "(" + rule.getName() + ")";
		int width = fm.stringWidth(str);
		
		Font multiplierFont = fm.getFont().deriveFont(fm.getFont().getSize2D() * 0.75f);
		FontMetrics multiplierFontMetrics = getFontMetrics (multiplierFont);
		
		if (multiplier > 1) {
			str = "" + multiplier;
			width += multiplierFontMetrics.stringWidth(str);
		}
		
		return (width);
	}
	
	public void drawExpressionString(Graphics2D g2d, int x, int y, int w, int h, PrettyString string) {
		int posy = y + h/2;
		FontMetrics fm = getFontMetrics(model.getFontRole(SmallStepModel.ROLE_EXPR));
		
		posy += (fm.getAscent() - fm.getDescent()) / 2;
		g2d.setFont(model.getFontRole(SmallStepModel.ROLE_EXPR));
		g2d.drawString(string.toString(), x, posy);
	}
	
	public void drawExpressionString(Graphics2D g2d, int step) {
		PrettyString string = model.getPrettyString(step);
		Expression underlineExpression = model.getUnderlineExpression(step);
		
		PrettyAnnotation annotation = null;
		if (this.usedAnnotation[step] != -1) {
			Iterator<PrettyAnnotation> it = string.getAnnotations().iterator();
			int i = 0;
			while (it.hasNext()) {
				annotation = it.next();
				if (i >= this.usedAnnotation[step])
					break;
				
				i++;
			}
		}
		if (string.getAnnotations().size() < this.usedAnnotation[step]) {
			annotation = (PrettyAnnotation)string.getAnnotations().toArray()[this.usedAnnotation[step]];
		}
		
		int x = this.center;
		int y = this.verticalSizes[step];
		int w = getWidth() - this.center;
		int h = this.verticalSizes[step+1] - this.verticalSizes[step];
		x += renderer.getRenderHeight() / 2;
		w -= renderer.getRenderHeight() / 2;
		
		if (!this.underlineExpressions) {
			underlineExpression = null;
		}
		renderer.renderHighlightedExpression(g2d, x, y, w, h, string, annotation, underlineExpression);
		
	}
	
	public void paintComponent(Graphics g) {
		if (model == null) {
			return;
		}		
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.BLACK);
		
		renderer.checkThemeValues(this);
		
		FontMetrics fmRule = this.getFontMetrics(model.getFontRole(SmallStepModel.ROLE_RULE));
		FontMetrics fmRuleExp = this.getFontMetrics(model.getFontRole(SmallStepModel.ROLE_RULE_EXP));
		prepare ();
		
		boolean debugFrames = false;
		if (debugFrames) {
			g2d.drawLine(this.center, 0, this.center, getHeight ());
			for (int i=0; i<verticalSizes.length; i++) {
				g2d.drawLine (0, this.verticalSizes[i], getWidth(), this.verticalSizes[i]);
			}
			g2d.drawLine(this.rightOutline, 0, this.rightOutline, getHeight());
		}
		
		for (int i=0; i<model.getNumberOfSteps(); i++) {
			// draw the rules
			if (!model.getEmptyRules(i)) {
				int posx = this.border.width;
				int posy = this.verticalSizes[i] + this.border.height;
				int numEvaluatedRules = model.getNumberOfEvaluatedMetaRules(i);
				for (int j=0; j<numEvaluatedRules; j++) {
					Rule rule = model.getMetaRule(i, j);
					String ruleText = rule.getName();
					int multiplier = 1;
					if (this.groupRules) {
						int k = 1;
						for (k = 1; (k+j)<numEvaluatedRules; k++) {
							String rt = model.getMetaRule(i, j + k).getName();
							// check if the next rule equals the current rule
							if (ruleText.equals(rt)) {
								continue;
							}
							// when this rule does not equal the current rule k will hold the multiplier for
							// this rule
							break;
						}
						multiplier = k;
					}
					int newPosx = drawRuleText (g2d, posx, posy, model.getMetaRule(i, j), fmRule, fmRuleExp, multiplier);
					int width = newPosx - posx;
					model.setRectangle(i, j, new Rectangle (posx, posy, width, fmRule.getHeight()));
					if (multiplier > 1) {
						for (int k=0; k<multiplier; k++) {
							model.setRectangle(i, j+k, new Rectangle(0, 0, 0, 0));
						}
					}
					posx = newPosx + this.ruleIntersection.width;
					j+= (multiplier-1);
				}
				if (model.getAximoRulesEvaluted(i)) {
					posx = this.border.width;
					posy = this.verticalSizes[i] + this.border.height + this.comboSize.height + this.ruleIntersection.height;
					int newPosx = drawRuleText (g2d, posx, posy, model.getAxiomRule(i), fmRule, fmRuleExp, 1);
					int width = newPosx - posx;
					model.setRectangle(i, model.getNumberOfEvaluatedMetaRules(i), new Rectangle (posx, posy, width, fmRule.getHeight()));
				}
			}
			
			// find the very center of the rules box and render an arrow in it
			int posy = (this.verticalSizes[i] + this.verticalSizes[i+1]) / 2;
			g2d.drawLine(this.border.width, posy, this.center - 2, posy);
			g2d.drawLine(this.center - 2, posy, this.center-2 - this.ruleIntersection.width / 2, posy - this.ruleIntersection.height / 4);
			g2d.drawLine(this.center - 2, posy, this.center-2 - this.ruleIntersection.width / 2, posy + this.ruleIntersection.height / 4);
			
			// now render the expression string
			if (model.getEmptyRules(i) || model.getAximoRulesEvaluted(i)) {
				drawExpressionString(g2d, i);
			}
		}	
	}
	
	
	private void handleMouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (x>= this.center) {
			return;
		}
		int section = -1;
		int i=0;
		for (; i<this.verticalSizes.length-1; i++) {
			if (y > this.verticalSizes[i] && y <= this.verticalSizes[i+1]) {
				section = i;
				break;
			}
		}
		if (section <= 0) {
			if (lastSelectedStep != section) {
				model.clearUndelines();
				lastSelectedStep = section;
				repaint();
			}
			return;
		}
		i = 0;
		for (; i<model.getNumberOfRectangles(section); i++) {
			Rectangle rect = model.getRectangle(section, i);
			if (x >= rect.x && y >= rect.y && x < rect.x + rect.width && y < rect.y + rect.height) {
				if (lastSelectedStep != section || lastSelectedRule != i) {
					lastSelectedStep = section;
					lastSelectedRule = i;
					model.clearUndelines();
					Rule r = model.getRule(section, i);
					if (r == null) {
						return;
					}
					model.setUndelineExpression(section-1, r.getExpression());
					repaint();
					return;
				}
				return;
			}
		}
		lastSelectedStep = -1;
		
		model.clearUndelines();
		repaint();
	}
}
