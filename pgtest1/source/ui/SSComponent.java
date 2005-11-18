package ui;

import smallstep.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.*;
import javax.swing.event.*;


public class SSComponent extends JComponent implements Scrollable {

	
	private class SmallStep {
		/**
		 * List of all ComboBoxes for the MetaRules
		 */
		private LinkedList<JComboBox> 	metaRules;
		/**
		 * One ComboBox of the AxiomRule
		 */
		private JComboBox				axiomRules;
		/**
		 * The expression-string this SmallStep is representing
		 */
		private String					expression;
		/**
		 * The RuleChain of the	SmallStep
		 */
		private RuleChain				ruleChain;
		/**
		 * Real starting position of the ComboBoxes and the centering arrow
		 */
		private int						cx;
		/**
		 * X position of the SmallStep BoundingBox
		 */
		private int						x;
		/**
		 * Y position of the SmallStep BoundingBox
		 */
		private int						y;
		/**
		 * Width of the SmallStep BoundingBox
		 */
		private int						width;
		/**
		 * Height of the SmallStep BoundingBox
		 */
		private int						height;
		/**
		 * Whether this SmallStep is handled correctly
		 */
		private boolean					correct;
				
		public SmallStep(RuleChain rules, String expression, int x, int y) {
			this.x 			= x;
			this.y 			= y;
			this.ruleChain	= rules;
			this.expression	= expression;
			metaRules 		= new LinkedList<JComboBox>();
			correct 		= false;
			cx				= x + bl;
			
			// check the size of a single combo box
			checkComboSize();
			
			// check the height of the small step
			height			= bt * 2 + bh * 2 + bvi;
			
			buildSmallStep();
		}
				
		public void buildSmallStep() {
			for (int i = 0; i<ruleChain.getRules().size()-1; i++)
				addMetaComboBox();
			addAxiomComboBox();
			if (metaRules.size () == 0)
				axiomRules.setVisible(true);
			recalculateSizes();
		}
		
		
		private void addMetaComboBox() {
			JComboBox c = new JComboBox();
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
			c.setModel(model);
			add(c);
			c.setBounds(cx, y + bt, bw, bh);
			c.setName("" + metaRules.size());
			cx += bl + bhi + bw;
			
			c.addPopupMenuListener(new PopupMenuListener () {
				public void popupMenuCanceled(PopupMenuEvent e) { }
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					JComboBox b = (JComboBox)e.getSource();
					DefaultComboBoxModel m = (DefaultComboBoxModel)b.getModel();
					int i = m.getIndexOf(m.getSelectedItem());
					try {
						int num = Integer.decode(b.getName());
						handleMetaRuleChanged(num, i);
					} catch (Exception exc) { }
				}
			});
			c.setVisible(metaRules.size() == 0);
			metaRules.add(c);
		}
		
		private void addAxiomComboBox() {
			axiomRules = new JComboBox();
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			model.addElement("---");
			model.addElement("OP");
			model.addElement("BETA-V");
			model.addElement("COND-TRUE");
			model.addElement("COND-FALSE");
			model.addElement("LET-EXEC");
            model.addElement("UNFOLD");
			axiomRules.setModel(model);
			add(axiomRules);
			axiomRules.setBounds(x + bl, y + bt + bh + bvi, bw, bh);
			axiomRules.setVisible(false);
			axiomRules.addPopupMenuListener(new PopupMenuListener () {
				public void popupMenuCanceled(PopupMenuEvent e) { }
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					JComboBox b = (JComboBox)e.getSource();
					DefaultComboBoxModel m = (DefaultComboBoxModel)b.getModel();
					int i = m.getIndexOf(m.getSelectedItem());
					handleAxiomChanged(i);
				}
			});
			axiomRules.setVisible(false);
		}
		
		private void handleMetaRuleChanged(int comboId, int index) {
			if (comboId > metaRules.size())
				return;

			Rule r = ruleChain.getRules().get(comboId);
			JComboBox b = metaRules.get(comboId);
			DefaultComboBoxModel m = (DefaultComboBoxModel)b.getModel();
			if (r.getName().equals (m.getSelectedItem())) {
				b.setVisible(false);
				if (comboId < (metaRules.size() - 1)) 
					metaRules.get(comboId+1).setVisible(true);
				else if (comboId == (metaRules.size() - 1))
					axiomRules.setVisible(true);
			}
			repaint();
			revalidate();
		}
		private void handleAxiomChanged(int index) {
			DefaultComboBoxModel m = (DefaultComboBoxModel)axiomRules.getModel();
			Rule r = ruleChain.getRules().getLast();
			if (r.getName().equals(m.getSelectedItem())) {
				axiomRules.setVisible(false);
				correct = true;
				evaluateNextStep();
			}
			repaint();
			revalidate();
		}
		public void completeStep() {
			ListIterator<JComboBox> it = metaRules.listIterator();
			ListIterator<Rule> rit = ruleChain.listIterator();
			while (it.hasNext() && rit.hasNext()) {
				JComboBox b = it.next();
				Rule r = rit.next();
				b.getModel().setSelectedItem(r.getName());
				b.setVisible(false);
			}
			Rule axiom = ruleChain.getRules().getLast();
			axiomRules.getModel().setSelectedItem(axiom.getName());
			axiomRules.setVisible(false);
			correct = true;
		}
		
		/**
		 * Recalculates the sizes and the position of the comboboxes
		 * 
		 * All sizes of the comboBoxes/rule-strings are added together and compared
		 * with a global maxWidth to find the most right position for drawing the 
		 * centering arrow.  
		 *
		 */
		private void recalculateSizes() {
			ListIterator<JComboBox> it = metaRules.listIterator();
			FontMetrics fm = getFontMetrics(comboFont);
			int px = x;
			int relX = 0;
			while (it.hasNext()) {
				JComboBox b = it.next();
				DefaultComboBoxModel m = (DefaultComboBoxModel)b.getModel();
				if (!b.isVisible()) {
					px += fm.stringWidth("(" + m.getSelectedItem() + ")") + bhi;
					if (px > maxWidth)
						maxWidth = px;
				}
				else if (b.isVisible()) {
					relX = b.getX() - px;
					b.setBounds(b.getX() - relX, b.getY(), b.getWidth(), b.getHeight());
					if ((b.getX() + b.getWidth()) > maxWidth)
						maxWidth = b.getX() + b.getWidth();
					break;
				}
			}
			while (it.hasNext()) {
				JComboBox b = it.next();
				b.setBounds(b.getX() - relX, b.getY(), b.getWidth(), b.getHeight());
				if (b.isVisible()) {
					if ((b.getX() + b.getWidth()) > maxWidth)
						maxWidth = b.getX() + b.getWidth();
				}
			}
			
			if (axiomRules.isVisible()) {
				if ((axiomRules.getX() + axiomRules.getWidth()) > maxWidth)
					maxWidth = axiomRules.getX() + axiomRules.getWidth();
			}
			else if (correct) {
				DefaultComboBoxModel m = (DefaultComboBoxModel)axiomRules.getModel();
				if ((x + fm.stringWidth("(" + m.getSelectedItem() + ")") + bhi) > maxWidth)
					maxWidth = x + fm.stringWidth("(" + m.getSelectedItem() + ")") + bhi;
			}
			
		}
		
		public void render(Graphics2D g2d, int right) {
			FontMetrics fm = getFontMetrics(textFont);
					
			FontMetrics cfm = getFontMetrics(comboFont);
			int ly = y + bt + bh + bvi / 2;

			g2d.setColor(Color.black);
			g2d.setFont(comboFont);
			int px = x;
			int py = ly - cfm.getAscent();
			ListIterator<JComboBox> it = metaRules.listIterator();
			while (it.hasNext()) {
				JComboBox b = it.next();
				if (b.isVisible())
					break;
				DefaultComboBoxModel m = (DefaultComboBoxModel)b.getModel();
				g2d.drawString("(" + m.getSelectedItem() + ")", px, py);
				px += cfm.stringWidth("(" + m.getSelectedItem() + ")") + bhi;
			}
			
			g2d.drawLine(x + bl, ly, right, ly);
			g2d.drawLine(right, ly, right - bvi / 4, ly - bhi / 4);
			g2d.drawLine(right, ly, right - bvi / 4, ly + bhi / 4);
			
			width = right;
			if (!correct)
				return;
			
			py = ly + cfm.getHeight() + fm.getDescent();
			DefaultComboBoxModel m = (DefaultComboBoxModel)axiomRules.getModel();
			g2d.drawString("(" + m.getSelectedItem() + ")", x, py);
			
			g2d.setFont(textFont);
			g2d.drawString(expression, right + bhi, ly + (fm.getDescent() + fm.getAscent() / 2) / 2);
			width += fm.stringWidth(expression);
			
		}
		
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
		
	}
	
	private LinkedList<SmallStep> 	steps;
	private SmallStepModel			model;
	private Font					comboFont;
	private Font					textFont;
	private int						expressionPosX;
	private int						expressionPosY;
	private int						currentPosY;
	private int						maxWidth;
	private int						maxSizeWidth, maxSizeHeight;
	
	/**
	 * The free space between the left position and the ComboBoxes or the centering arrow
	 */
	private int 					bl;
	/**
	 * The free space between the top position and the MetaRule ComboBoxes; between the
	 * AxiomRuleComboBox and the bottom position 
	 */
	private int						bt;
	/**
	 * Width of the SmallStep ComboBox
	 */
	private int 					bw;
	/**
	 * Height of the SmallStep ComboBox
	 */
	private int 					bh;
	/**
	 * Vertical Intersection. The Space between the MetaRules and the AxiomRules ComboBox
	 */
	private int 					bvi;
	/**
	 * Horizontal Intersection. The Space between the Single MetaRule ComboBoxes
	 */
	private	int						bhi;
	
	/**
	 * Constructor of the SmallStepComponent
	 * 
	 * @param model A model the SmallStepComponent uses to display and interact with the 
	 * 				SmalStepInterpreter
	 */
	public SSComponent(SmallStepModel model) {
		super();
		this.model 		= model;
		comboFont		= new JComboBox().getFont();
		textFont 		= comboFont.deriveFont(comboFont.getSize2D()*2.0f);	
		expressionPosX	= 10;
		FontMetrics fm 	= getFontMetrics(textFont);
		expressionPosY 	= fm.getHeight () * 2;
		
		steps 			= new LinkedList<SmallStep>();
	
		currentPosY		= expressionPosY + fm.getHeight();
		
		maxWidth 		= 0;
		maxSizeWidth	= fm.stringWidth(""+(model.getCurrentExpression())) + expressionPosX;
		maxSizeHeight	= 2*currentPosY;
	
		checkComboSize ();
		evaluateNextStep();
	}
	
	/**
	 * Calculates the needed sizes to handle the correct display of the SmallSteps
	 */
	private void checkComboSize() {
		FontMetrics fm = getFontMetrics(comboFont);			
		bw = fm.stringWidth("COND-EVAL-EXN-EXN");
		bh = fm.getHeight() + 2;
		bt = bvi = fm.getHeight();
		bvi *= 1.5f;
		bhi = 10;
	}
	
	/**
	 * Requests the next SmallStep from the SmallStep interpreter.
	 * 
	 * @return True if the next Step could be requested, false if the expression if handled
	 * 			completely or the expressen ended up in an exception.
	 */
	private boolean evaluateNextStep() {
		int result = model.evaluateNextStep();
		if (result != 0)
			return false;
		
		Expression e = model.getCurrentExpression();
		RuleChain rc = model.getCurrentRuleChain();
		
		SmallStep ss = new SmallStep(rc, "" + e, expressionPosX, currentPosY);
		currentPosY += ss.getHeight() + 10;
		
		steps.add(ss);
		repaint();
		return (true);
	}
	
	/**
	 * Complets the current step. 
	 *
	 * All combo boxes will be set to the correct state, then get hided and replaced by a string.
	 */
	public void completeCurrentStep() {
		SmallStep ss = steps.getLast();
		ss.completeStep();
		evaluateNextStep();
		repaint();
		revalidate();
	}
	
	/**
	 * Completes the current and the remaining steps 
	 *
	 */
	public void completeAllSteps() {
		boolean comp = true;
		while (comp) {
			SmallStep ss = steps.getLast();
			ss.completeStep();
			comp = evaluateNextStep();
		}
		repaint();
		revalidate();
	}
	
	/**
	 * Paints the SmallStepComponent.
	 * 
	 * The background will be filled white and then all the current steps will be rendered
	 * on top of the plane.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		Expression e = model.getOriginExpression();
		FontMetrics fm = getFontMetrics(textFont);
		g2d.setColor(Color.black);
		g2d.setFont(textFont);
		g2d.drawString(""+ e, expressionPosX, expressionPosY);

		maxWidth = 0;
		maxSizeWidth = fm.stringWidth(""+e) + expressionPosX;
		maxSizeHeight = 0;
		ListIterator<SmallStep> it = steps.listIterator();
		while (it.hasNext()) {
			it.next().recalculateSizes();
		}
		it = steps.listIterator();
		while (it.hasNext()) {
			SmallStep step = it.next();
			step.render(g2d, maxWidth);
			if (maxSizeWidth < (step.getX() + step.getWidth()))
				maxSizeWidth = step.getX() + step.getWidth();
			if (maxSizeHeight < (step.getY() + step.getHeight()))
				maxSizeHeight = step.getY() + step.getHeight();
					
		}
		setPreferredSize (new Dimension (maxSizeWidth + 10, maxSizeHeight + fm.getHeight()));
		revalidate();
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return direction * 50;
	}
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return direction * 25;
	}
}
