package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import smallstep.*;

public class SmallStepComboRules extends JComponent {

	private class MouseBounding {
		private int 	x;
		private int 	y;
		private int 	w;
		private int 	h;
		private Rule 	r;
		public MouseBounding(Rule rule, int x, int y, int width, int height) {
			this.r = rule;
			this.x = x;
			this.y = y;
			this.w = width;
			this.h = height;
		}
		public Rule check (int x, int y) {
			if (x >= this.x && y >= this.y && x < this.x + this.w && y < this.y + this.h)
				return r;
			return null;
		}
		public Rule getRule () {
			return r;
		}
		public void resize (int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		public int getX() { 
			return x;
		}
		public int getY() {
			return y;
		}
		public int getWidth() {
			return w;
		}
		public int getHeight() {
			return h;
		}
		
	}
	/**
	 * All rules needed for this ComboBox component 
	 */
	private RuleChain 	ruleChain;
	
	/**
	 * The correct selection of the rule combobox
	 */
	private String		correctRule;
	
	/**
	 * We only need one ComboBox
	 */
	private JComboBox	ruleComboBox;
	
	/**
	 * The font that is used to render the correct selected rules
	 */
	private Font		font;
	
	/**
	 * The fonts metrics
	 */
	private FontMetrics	fontMetrics;
	
	/**
	 * Specifies the width of the combo box
	 */
	private int			comboSizeWidth;
	/**
	 * Specifies the height of the combo box
	 */
	private int			comboSizeHeight;
	
	/**
	 * Specifies the space between two ComboBoxes/Rule texts
	 */
	private int			horizontalSpace;
	
	/**
	 * The id of the current current rule in the rule chain that has to be evaluated correctly.
	 */
	private int			currentRule;
	
	private int			preferredSize;
	
	private int			maxWidth;
	
	private int			maxHeight;
	
	private int			center;
	
	private Rule		ruleBelowMouse;
	
	private LinkedList<MouseBounding> 	mouseBoundings = new LinkedList<MouseBounding>();
	

	private EventListenerList			listenerList = new EventListenerList();
	
	public SmallStepComboRules(RuleChain chain) {
		super ();
		setLayout(null);
		this.ruleChain 			= chain;
		this.ruleComboBox 		= new JComboBox ();
		this.currentRule		= -1;
		this.font				= this.ruleComboBox.getFont();
		this.fontMetrics		= getFontMetrics(font);
		// XXX FIXME
		this.comboSizeWidth		= 150;
		this.comboSizeHeight	= this.fontMetrics.getHeight() + 2;
		this.horizontalSpace	= this.comboSizeHeight / 2;
		this.ruleBelowMouse		= null;
		
		this.ruleComboBox.addPopupMenuListener(new PopupMenuListener () {
			public void popupMenuCanceled(PopupMenuEvent e) { }
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				comboSelectionChanged();
			}
		});
		this.ruleComboBox.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) { }
			public void mouseMoved(MouseEvent e) {
				JComboBox b = (JComboBox)e.getSource();
				checkMouse (e.getX() + b.getX(), e.getY() + b.getY());
			}
		});
		add(this.ruleComboBox);
		evaluateRule();
		
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) { }
			public void mouseMoved(MouseEvent e) {
				checkMouse (e.getX(), e.getY());
			}
		});
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) { }
			public void mouseEntered(MouseEvent e) { }
			public void mouseExited(MouseEvent e) {
				if (ruleBelowMouse != null) {
					fireSmallStepMouseFocusEvent(null);
				}
				ruleBelowMouse = null;
				repaint();
			}
			public void mousePressed(MouseEvent e) { }
			public void mouseReleased(MouseEvent e) { }
		});
		int numCombos = this.ruleChain.getRules().size();
		if (numCombos > 1) {
			numCombos--;
		}
		preferredSize = 0;
		this.maxHeight	= this.comboSizeHeight * 3;
		this.maxWidth	= this.comboSizeWidth * numCombos + this.horizontalSpace;
		setSize (maxWidth, maxHeight);
		calculateSize();
	}
	
	private void resizeMouseBounding(Rule r, int x, int y, int w, int h) {
		ListIterator<MouseBounding> it = mouseBoundings.listIterator();
		while (it.hasNext()) {
			MouseBounding b = it.next();
			if (b.getRule() == r) {
				b.resize(x, y, w, h);
				return;
			}
		}
		MouseBounding b = new MouseBounding(r, x, y, w, h);
		mouseBoundings.add(b);
	}
	
	private void checkMouse(int x, int y) {
		ListIterator<MouseBounding> it = mouseBoundings.listIterator();
		Rule thisRule = null;
		while (it.hasNext()) {
			MouseBounding b = it.next();
			thisRule = b.check(x, y);
			if (thisRule != null) {
				break;
			}
		}
		this.ruleBelowMouse = thisRule;
		this.fireSmallStepMouseFocusEvent(this.ruleBelowMouse);
		repaint();
	}

	private void comboSelectionChanged () {
		String selection = (String)ruleComboBox.getSelectedItem();
		if (selection.equals(this.correctRule)) {
			evaluateRule();
		}
	}
	
	private void evaluateRule() {
		this.currentRule++;
		if (this.currentRule == this.ruleChain.getRules().size()) {
			this.ruleComboBox.setVisible (false);
			fireSmallStepResolved();
			// thats all; now the rest of the application has bo notified
		}
		else {
			Rule r = this.ruleChain.getRules().get(this.currentRule);
			if (r.isAxiom()) {
				createAxiomRule();
			}
			else {
				createMetaRule();
			}
			this.correctRule = r.getName();
		}
		calculateSize();
	}
	
	private void calculateSize() {
		int posx = 0;
		int id   = 0;
		ListIterator<Rule> it = this.ruleChain.listIterator();
		while (it.hasNext()) {
			Rule r = it.next();
			int oldX = posx;
			int oldY = 0;
			if (!r.isAxiom()) {
				if (this.currentRule > id) {
					posx += this.fontMetrics.stringWidth("(" + r.getName() + ")");
				}
				else {
					this.ruleComboBox.setVisible(true);
					this.ruleComboBox.setBounds(posx, 0, comboSizeWidth, comboSizeHeight);
					resizeMouseBounding(r, posx, 0, comboSizeWidth, comboSizeHeight);
					posx += comboSizeWidth;
					posx += this.horizontalSpace;
					break;
				}
			}
			else {
				oldY = comboSizeHeight * 2;
				if (this.currentRule > id) {
					int x = this.fontMetrics.stringWidth("(" + r.getName() + ")") + this.horizontalSpace;
					if (x > posx) {
						posx = x;
					}
					resizeMouseBounding(r, 0, oldY, posx, comboSizeHeight);
					break;
				}
				else {
					this.ruleComboBox.setVisible(true);
					this.ruleComboBox.setBounds(0, comboSizeHeight * 2, comboSizeWidth, comboSizeHeight);
					if (comboSizeWidth + this.horizontalSpace > posx) { 
						posx = comboSizeWidth + this.horizontalSpace;
					}
					resizeMouseBounding(r, 0, oldY, posx, comboSizeHeight);
					break;					
				}
			}
			id++;
			int w = posx - oldX;
			resizeMouseBounding(r, oldX, oldY, w, comboSizeHeight);
			posx += this.horizontalSpace;
		}
		posx += this.horizontalSpace * 2;
		preferredSize = posx;
		setPreferredSize (new Dimension (posx, comboSizeHeight * 3));
		setSize (new Dimension (posx, comboSizeHeight * 3));
		revalidate();
		fireSmallStepResized();
	}
	
	public int getPreferredComboSize () {
		return this.preferredSize;
	}
	
	public int getMaxWidth() {
		return this.maxWidth;
	}
	
	public int getMaxHeight() {
		return this.maxHeight;
	}
	
	public void setCenter(int center) {
		this.center = center;
	}
	
	public void setHighlight(Rule r) {
		this.ruleBelowMouse = r;
		repaint();
	}
	public void unsetHighlight() {
		this.ruleBelowMouse = null;
		repaint();
	}
	
	private void createMetaRule() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement("---");
		model.addElement("APP-LEFT");
		model.addElement("APP-RIGHT");
		model.addElement("COND-EVAL");
		model.addElement("LET-EVAL");
		model.addElement("APP-LEFT-EXN");
		model.addElement("APP-RIGHT-EXN");
		model.addElement("COND-EVAL-EXN");
		model.addElement("LET-EVAL-EXN");
		ruleComboBox.setModel(model);
	}
	
	private void createAxiomRule() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement("---");
		model.addElement("OP");
		model.addElement("BETA-V");
		model.addElement("COND-TRUE");
		model.addElement("COND-FALSE");
		model.addElement("LET-EXEC");
		model.addElement("UNFOLD");
		ruleComboBox.setModel(model);
	}
	
	public void resolve() {
		this.currentRule = ruleChain.getRules().size();
		ruleComboBox.setVisible(false);
		calculateSize();
	}
	public void paintComponent(Graphics g) {		
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.BLACK);
		
		g2d.setFont(this.font);
		int id   = 0;
		int posx = 0;
		
		ListIterator<Rule> it = this.ruleChain.listIterator();
		while (it.hasNext()) {
			Rule r = it.next();
			if (r == this.ruleBelowMouse) {
				g2d.setColor(Color.RED);
			}
			else {
				g2d.setColor(Color.BLACK);
			}
			if (!r.isAxiom()) {
				if (this.currentRule > id) {
					g2d.drawString("(" + r.getName() + ")", posx, 0 + fontMetrics.getHeight());
					posx += this.fontMetrics.stringWidth("(" + r.getName() + ")");
					posx += this.horizontalSpace;
				}
				else {
					break;
				}
			}
			else {
				if (this.currentRule > id) {
					g2d.drawString("(" + r.getName() + ")", 0, comboSizeHeight * 2 + fontMetrics.getHeight());
					break;
				}
			}
			id++;
		}
		g2d.setColor(Color.BLACK);
		int hor2 = this.horizontalSpace / 2;
		int h2 = getMaxHeight() / 2;
		int right = center - this.horizontalSpace;
		g2d.drawLine(0, h2, right, h2);
		g2d.drawLine(right, h2, right - hor2, h2 - hor2);
		g2d.drawLine(right, h2, right - hor2, h2 + hor2);
		
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
