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
				System.out.println("contentsChanged");
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
				String rule = model.getMetaRule(i, j).getName();
				int width = fm.stringWidth(rule) + this.ruleIntersection.width;
				currentCenter += width; 
			}
			
			if (numRules < model.getNumberOfMetaRules(i)) { 
				// add the space for the combobox
				currentCenter += this.comboSize.width + this.ruleIntersection.width;
			}
			else {
				int w = 0;
				if (model.getAximoRulesEvaluted(i)) {
					String rule = model.getAxiomRule(i).getName();
					w = fm.stringWidth(rule) + this.ruleIntersection.width;
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
	public int drawRuleText (Graphics2D g2d, int x, int y, Rule rule, FontMetrics fm) {
		int posy = y + this.comboSize.height - fm.getDescent();
		
		g2d.setFont(model.getFontRole(SmallStepModel.ROLE_RULE));
		g2d.drawString(rule.getName(), x, posy);
		
		return (x + fm.stringWidth(rule.getName()));
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
		
		FontMetrics fmRule = this.getFontMetrics(model.getFontRole(SmallStepModel.ROLE_RULE));
		prepare ();
		
//		g2d.drawLine(this.center, 0, this.center, getHeight ());
//		for (int i=0; i<verticalSizes.length; i++) {
//			g2d.drawLine (0, this.verticalSizes[i], getWidth(), this.verticalSizes[i]);
//		}
//		g2d.drawLine(this.rightOutline, 0, this.rightOutline, getHeight());
		
		for (int i=0; i<model.getNumberOfSteps(); i++) {
			// draw the rules
			if (!model.getEmptyRules(i)) {
				int posx = this.border.width;
				int posy = this.verticalSizes[i] + this.border.height;
				
				for (int j=0; j<model.getNumberOfEvaluatedMetaRules(i); j++) {
					int newPosx = drawRuleText (g2d, posx, posy, model.getMetaRule(i, j), fmRule);
					int width = newPosx - posx;
					model.setRectangle(i, j, new Rectangle (posx, posy, width, fmRule.getHeight()));
					posx = newPosx + this.ruleIntersection.width;
				}
				if (model.getAximoRulesEvaluted(i)) {
					posx = this.border.width;
					posy = this.verticalSizes[i] + this.border.height + this.comboSize.height + this.ruleIntersection.height;
					int newPosx = drawRuleText (g2d, posx, posy, model.getAxiomRule(i), fmRule);
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
	
	
	private int lastSelectedStep = -1;
	private int lastSelectedRule = -1;
	
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
				System.out.println("(" + x + ", " + y + ")");
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
/*
	private LinkedList<SmallStep>	smallStepSteps = new LinkedList<SmallStep>();
	
	private boolean					justAxioms;
	
	private boolean					underlineExpressions;
	
	private EventListenerList		listenerList = new EventListenerList();
	
	private int						maxWidth;

	public SmallStepComponent(SmallStepModel model, boolean justAxioms, boolean underlineExpressions) {
		super();
		setLayout(null);
		
		this.model 					= model;
		this.justAxioms 			= justAxioms;
		this.underlineExpressions 	= underlineExpressions;
		this.maxWidth				= 300;
		
		// create the first Step;
		SmallStep step = new SmallStep(null, model.getOriginExpression(), null);
		add(step);
		step.setBounds(10, 10, step.getWidth(), step.getHeight());	
		smallStepSteps.add(step);
		
		// and now evaluate the first "real" step
		evaluateNextStep();
	}
	
	public void setJustAxioms(boolean justAxioms) {
		this.justAxioms = justAxioms;
	}
	
	public boolean getJustAxioms() {
		return this.justAxioms;
	}
	
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public int getMaxWidth() {
		return this.maxWidth;
	}
	
	public void setUnderlineExpressions(boolean underline) {
		this.underlineExpressions = underline;
	}
	
	public int evaluateNextStep() {
		int res = model.evaluateNextStep();
		if (res == 0) {
			// this is a proper next step
			SmallStep parent = smallStepSteps.getLast();
			System.out.println("EvaluateNextStep: ");
			System.out.println("  parent : " + parent.getExpression());
			System.out.println("  current: " + model.getCurrentExpression());
			SmallStep step = new SmallStep(parent, model.getCurrentExpression(), model.getCurrentRuleChain());
			if (this.justAxioms) {
				step.resolveMetaRules();
			}
			System.out.println("  pos:  " + parent.getX() + " " + parent.getY ());
			System.out.println("  size: " + parent.getWidth() + " " + parent.getHeight());
			System.out.println("  step: " + step.getWidth() + " " + step.getHeight());
			add(step);
			step.setBounds(10, parent.getY() + parent.getHeight() + 20, step.getWidth(), step.getHeight());
			smallStepSteps.add(step);
			
			step.addSmallStepEventListener(new SmallStepEventListener() {
				public void smallStepResized(EventObject o) { calculateCenter(); }
				public void smallStepResolved(EventObject o) { evaluateNextStep(); }
				public void mouseFocusEvent(SmallStepEvent e) {
					underlineSequence((SmallStep)e.getSource(), e.getRule());
				};
				public void releaseSyntacticalSugar(EventObject o) {
					resolveSyntacticalSugar();
				}
			});
			this.underlineSequence(null, null);
		}
		calculateSizes ();
		fireSmallStepResolved();
		repaint();
		return res;
	}
	
	public void resolveSyntacticalSugar() {
		SmallStep step = smallStepSteps.get(1);
		remove(step);
		smallStepSteps.remove(1);
		
		SmallStep parent = smallStepSteps.getFirst();
		SmallStep resolvedStep = new SmallStep(parent, model.getOriginExpression().translateSyntacticSugar(), null);
			
		add(resolvedStep);
		resolvedStep.setBounds(10, parent.getY() + parent.getHeight() + 20, resolvedStep.getWidth(), resolvedStep.getHeight());
		smallStepSteps.add(resolvedStep);
		
		
		model.resetModel(model.getOriginExpression().translateSyntacticSugar());
		
		calculateSizes();
		repaint();
		evaluateNextStep();
	}
	
	public void calculateSizes() {
		calculateCenter();
		int x = 10;
		int y = 10;
		int maxWidth = 0;
		int maxHeight = 0;
		ListIterator<SmallStep> it = smallStepSteps.listIterator();
		while (it.hasNext()) {
			SmallStep step = it.next();
			Dimension d = step.prepareSize(this.maxWidth);
			step.setBounds(x, y, d.width, d.height);
			step.setSize (d.width, d.height);
			
			if (x + d.width > maxWidth) {
				maxWidth = x + d.width;
			}
			if (y + d.height > maxHeight) {
				maxHeight = y + d.height;
			}
			
			y += d.height + 20;
		}
		setPreferredSize (new Dimension (maxWidth + 20, maxHeight + 20));
	}
	
	public void calculateCenter() {
		int center = 0;
		ListIterator<SmallStep> it = smallStepSteps.listIterator();
		while (it.hasNext()) {
			SmallStep s = it.next();
			if (s.getPreferredCenter() > center) {
				center = s.getPreferredCenter();
			}
		}
		
		it = smallStepSteps.listIterator();
		while (it.hasNext()) {
			SmallStep s = it.next();
			s.setCenter(center);
		}
	}
	
	public void paintComponent(Graphics g) {
//		System.out.println("SmallStepComponent.paintComponent");
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.BLACK);
	}
	
	public boolean completeCurrentStep() {
		int was = smallStepSteps.size ();
		smallStepSteps.getLast().resolve();
		return was != smallStepSteps.size ();
	}
	
	public void completeAllSteps() {
		while (completeCurrentStep()); 
	}
	
	public void underlineSequence(SmallStep smallStep, Rule rule) {
		ListIterator<SmallStep> it = smallStepSteps.listIterator();
		while (it.hasNext()) {
			SmallStep s = it.next();
			s.clearHighlighting();
			if (s.clearUnderlining()) {
				s.repaint();
			}
		}
		if (!this.underlineExpressions) {
			return;
		}
		it = smallStepSteps.listIterator();
		while (it.hasNext()) {
			SmallStep s = it.next();
			if (s == smallStep) {
				SmallStep parent = s.getSmallStepParent();
				if (parent != null) {
					parent.setUnderlining(rule);
					parent.repaint();
				}
				s.setHightlighting(rule);
			}
		}
	}
	
	public void addSmallStepEventListener(SmallStepEventListener e) {
		listenerList.add(SmallStepEventListener.class, e);
	}
	
	public void removeSmallStepEventListener(SmallStepEventListener e) {
		listenerList.remove(SmallStepEventListener.class, e);
	}
	
	private void fireSmallStepResolved() {
		Object[] listeners = listenerList.getListenerList();
		
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SmallStepEventListener.class) {
	             // Lazily create the event:
	             ((SmallStepEventListener)listeners[i+1]).smallStepResolved(new EventObject(this));
	         }
	     }
	}
}
*/