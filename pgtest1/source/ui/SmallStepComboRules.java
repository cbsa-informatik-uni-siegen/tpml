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
		
		this.ruleComboBox.addPopupMenuListener(new PopupMenuListener () {
			public void popupMenuCanceled(PopupMenuEvent e) { }
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				comboSelectionChanged();
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
	private String s = "Narf";
	
	private void checkMouse(int x, int y) {
		ListIterator<MouseBounding> it = mouseBoundings.listIterator();
		String t = "Narf";
		while (it.hasNext()) {
			MouseBounding b = it.next();
			Rule r = b.check(x, y);
			if (r != null) {
				t = r.getName ();
				break;
			}
		}
		if (!t.equals(s) && !t.equals("Narf")) {
			s = t;
		}
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
					break;
				}
				else {
					this.ruleComboBox.setVisible(true);
					this.ruleComboBox.setBounds(0, comboSizeHeight * 2, comboSizeWidth, comboSizeHeight);
					if (comboSizeWidth + this.horizontalSpace > posx) { 
						posx = comboSizeWidth + this.horizontalSpace;
					}
					break;					
				}
			}
			id++;
			int w = posx - oldX;
			resizeMouseBounding(r, oldX, oldY, w, comboSizeHeight);
			posx += this.horizontalSpace;
		}
		posx += this.horizontalSpace;
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
		int hor2 = this.horizontalSpace / 2;
		
		int h2 = getMaxHeight() / 2;
		g2d.drawLine(0, h2, center, h2);
		g2d.drawLine(center, h2, center - hor2, h2 - hor2);
		g2d.drawLine(center, h2, center - hor2, h2 + hor2);
		
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
		
	
}
