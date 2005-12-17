package ui;

import smallstep.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.JComponent;
import javax.swing.JComboBox;

/**
 * The SmallStep is the combination of the SmallStepComboRules on the left-hand-side
 * and the Renderer that is responsible for drawing the expression text on the right-hand-side
 * 
 * @author marcell
 *
 */
public class SmallStep extends JComponent {

	private Font					textFont;
	private Font					keywordFont;
	
	/**
	 * The expression
	 */
	private Expression				expression;

	/**
	 * Pretty String of the expression
	 */
	private PrettyString			prettyString;
	
	/**
	 * The Rule chain that has been evaluated
	 */
	private RuleChain				ruleChain;
	
	/**
	 * The combo boxes on the left-hand side of the small step component
	 */
	private SmallStepComboRules 		ruleCombos;
		
	/**
	 * The parent is the SmallStep on top of this one. It will be used to
	 * perform advanced highlightin etc...
	 */
	private SmallStep				parent;
	
	/**
	 * The center is the horizontal middle point between the ComboBoxes(incl. Arrow) and the expression string.
	 */
	private int						center;
	
	/**
	 * Specifies whether this single small step is entered correlty. So the expression can be drawn 
	 */
	private boolean					smallStepResolved;
	
	private int						expressionWidth;
	
	private int						expressionHeight;
	
	/**
	 * 
	 */
	private EventListenerList		listenerList = new EventListenerList();
	
	/**
	 * 
	 */
	private Expression				underlineExpression = null;
	/**
	 * 
	 * @param parent
	 * @param expression
	 * @param ruleChain
	 */
	public SmallStep(SmallStep parent, Expression expression, RuleChain ruleChain) {
		// call the super constructor and set the layout of this component
		super();
		setLayout(null);
		
		this.parent 			= parent;
		this.expression			= expression;
		this.ruleChain			= ruleChain;
		this.smallStepResolved	= false;
		this.prettyString		= expression.toPrettyString();
		
		Font comboFont = new JComboBox().getFont();
		textFont 	= comboFont.deriveFont(Font.PLAIN, comboFont.getSize2D() * 1.5f);
		keywordFont = textFont.deriveFont(Font.BOLD);
		
		Renderer renderer = new Renderer(null, getFontMetrics(textFont), getFontMetrics(keywordFont));
		
		Dimension d = renderer.checkRenderSize(expression.toPrettyString(), 0, 0);
		this.expressionWidth	= d.width;
		this.expressionHeight	= d.height;
		
				
		// if the ruleChain is null then this would be the top expression that has no rule chain
		if (ruleChain != null) {
			ruleCombos = new SmallStepComboRules(ruleChain);
			ruleCombos.setBounds(0, 0, ruleCombos.getWidth(), ruleCombos.getHeight ());
			add(ruleCombos);
			setSize(new Dimension (400, ruleCombos.getHeight()));
			setPreferredSize(new Dimension (400, ruleCombos.getHeight()));
			
			if (ruleCombos.getMaxHeight() > this.expressionHeight)
				this.expressionHeight = ruleCombos.getMaxHeight();
			
			ruleCombos.addSmallStepEventListener(new SmallStepEventListener() {
				public void smallStepResized(EventObject o) { fireSmallStepResized (); }
				public void smallStepResolved(EventObject o) { resolve(); }
				public void mouseFocusEvent(SmallStepEvent e) { fireSmallStepMouseFocusEvent (e.getRule()); };
			});
		}
		else {
			smallStepResolved 	= true;
			ruleCombos 			= null;
			// XXX FIXME
		}
		setCenter (0);
		
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) { }
			public void mouseMoved(MouseEvent e) { if (e.getX() >= center) fireSmallStepMouseFocusEvent(null); };
		});
	}
	
	public void setCenter(int center) {
		this.center = center;
		if (ruleCombos != null) {
			ruleCombos.setCenter(center);
			ruleCombos.setBounds(0, 0, center, getHeight());
		}
		setSize (center + this.expressionWidth, this.expressionHeight);
	}
	
	public int getCenter() {
		return this.center;
	}
	
	public SmallStep getSmallStepParent() {
		return this.parent;
	}
	
	public int getPreferredCenter() {
		if (ruleCombos != null)
			return ruleCombos.getPreferredComboSize();
		return 0;
	}
	
	public void resolve() {
		if (ruleCombos != null) {
			ruleCombos.resolve();
		}
		this.smallStepResolved = true;
		fireSmallStepResolved ();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.BLACK);
		if (this.smallStepResolved) {
			Renderer renderer = new Renderer(g2d, getFontMetrics(textFont), getFontMetrics(keywordFont));
			renderer.renderHighlightedExpression(center, 0, getWidth () - center, expressionHeight, this.prettyString, this.underlineExpression);
		}
	}
	
	public void setUnderlining(Rule r) {
		if (r != null)
			this.underlineExpression = r.getExpression();
		else
			this.underlineExpression = null;
	}
	
	public boolean clearUnderlining() {
		if (this.underlineExpression != null) {
			this.underlineExpression = null;
			return true;
		}
		return false;
	}
	
	public void addSmallStepEventListener(SmallStepEventListener e) {
		listenerList.add(SmallStepEventListener.class, e);
	}
	
	public void removeSmallStepEventListener(SmallStepEventListener e) {
		listenerList.remove(SmallStepEventListener.class, e);
	}
	
	private void fireSmallStepResized() {
		Object[] listeners = listenerList.getListenerList();
		
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SmallStepEventListener.class) {
	             // Lazily create the event:
	             ((SmallStepEventListener)listeners[i+1]).smallStepResized(new EventObject(this));
	         }
	     }
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
	private void fireSmallStepMouseFocusEvent(Rule rule) {
		Object[] listeners = listenerList.getListenerList();
		
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i] == SmallStepEventListener.class) {
				((SmallStepEventListener)listeners[i+1]).mouseFocusEvent(new SmallStepEvent (rule, this));
			}
		}
	}
}
