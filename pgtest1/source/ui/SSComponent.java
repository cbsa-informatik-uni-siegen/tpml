package ui;

import smallstep.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.*;


public class SSComponent extends JComponent {

	
	private class SmallStep {
		private LinkedList<JComboBox> 	metaRules;
		private JComboBox				axiomRules;
		private String					expression;
		private RuleChain				ruleChain;
		private JComponent				parent;
		private int 					bl, bt, bw, bh, bvi, bhi, cx;
		private int						x, y;
		private int						width, height;
		private int 					comboWidth;
		private int						comboHeight;
		private boolean					correct;
				
		public SmallStep(RuleChain rules, String expression, JComponent parent, int x, int y) {
			this.x 			= x;
			this.y 			= y;
			this.ruleChain	= rules;
			this.parent		= parent;
			this.expression	= expression;
			metaRules 		= new LinkedList<JComboBox>();
			correct 		= false;
			cx				= x + bl; 

			checkComboSize();
			calculateSize();
			buildSmallStep();
		}
		
		public void calculateSize() {
			/*
			 * the last combo will be the axiom rule combo box
			 * and it will apear in the next line
			int numCombos = ruleChain.getRules().size()-1;
			width = numCombos * comboWidth;
			 */
						
			/*
			 * The height for the Expression text will take place later
			 */
			height = bt * 2 + bh * 2 + bvi;
		}
		
		public void buildSmallStep() {
			for (int i = 0; i<ruleChain.getRules().size()-1; i++)
				addMetaComboBox(parent);
			addAxiomComboBox(parent);
			if (metaRules.size () == 0)
				axiomRules.setVisible(true);
			checkMaxWidth();
		}
		
		public void unbuildSmallStep() {
			for (int i=0; i<metaRules.size(); i++) {
				metaRules.get(i).setEnabled(false);
			}
			axiomRules.setEnabled(false);
			correct = true;
		}
				
		public void checkComboSize() {
			FontMetrics fm = getFontMetrics(comboFont);			
			bw = 160;
			bh = fm.getHeight() + 2;
			bt = bvi = fm.getHeight();
			bvi *= 1.5f;
			bhi = 10;
		}
		
		public void addMetaComboBox(JComponent parent) {
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
			parent.add(c);
			c.setBounds(cx, y + bt, bw, bh);
			c.setName("" + metaRules.size());
			cx += bl + bhi + bw;
			
			c.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
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
		
		public void addAxiomComboBox(JComponent parent) {
			axiomRules = new JComboBox();
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			model.addElement("---");
			model.addElement("OP");
			model.addElement("BETA-V");
			model.addElement("COND-TRUE");
			model.addElement("COND-FALSE");
			model.addElement("LET-EXEC");
			axiomRules.setModel(model);
			parent.add(axiomRules);
			axiomRules.setBounds(x + bl, y + bt + bh + bvi, bw, bh);
			axiomRules.setVisible(false);
			axiomRules.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					JComboBox b = (JComboBox)e.getSource();
					DefaultComboBoxModel m = (DefaultComboBoxModel)b.getModel();
					int i = m.getIndexOf(m.getSelectedItem());
					handleAxiomChanged(i);
				}
			});
			axiomRules.setVisible(false);
		}
		
		public void handleMetaRuleChanged(int n, int index) {
			for (int i=0; i<metaRules.size(); i++) {
				JComboBox b = metaRules.get(i);
				boolean v = i == 0 ||(i<=n+1 && index != 0) || (1<=n && index == 0);
				b.setVisible(v);
			}
			boolean v = n == metaRules.size()-1 && index != 0;
			axiomRules.setVisible(v);
			repaint();
		}
		public void handleAxiomChanged(int index) {
			if (index != 0)
				checkCorrectness ();
			repaint();
		}
		
		public void checkCorrectness() {
			LinkedList<Rule> rules = ruleChain.getRules();
			for (int i=0; i<metaRules.size(); i++) {
				JComboBox b = metaRules.get(i);
				DefaultComboBoxModel m = (DefaultComboBoxModel)b.getModel();
				Rule r = rules.get(i);
				if (!r.getName().equals (m.getSelectedItem())) 
					return;
			}
			Rule axiom = rules.getLast();
			DefaultComboBoxModel m = (DefaultComboBoxModel)axiomRules.getModel();
			if (!axiom.getName().equals(m.getSelectedItem()))
				return;
			unbuildSmallStep();
			evaluateNextStep();
		}
		
		public void checkMaxWidth() {
			ListIterator<JComboBox> it = metaRules.listIterator();
			while (it.hasNext()) {
				JComboBox b = it.next();
				if (b.isVisible()) {
					if ((b.getX() + b.getWidth()) > maxWidth)
						maxWidth = b.getX() + b.getWidth();
				}
			}
			if (axiomRules.isVisible()) {
				if ((axiomRules.getX() + axiomRules.getWidth()) > maxWidth)
					maxWidth = axiomRules.getX() + axiomRules.getWidth();
			}
		}
		
		public void render(Graphics2D g2d, int right) {
			FontMetrics fm = getFontMetrics(textFont);
			
			int ly = y + bt + bh + bvi / 2;

			g2d.setColor(Color.black);
			g2d.drawLine(x + bl, ly, right, ly);
			g2d.drawLine(right, ly, right - bvi / 4, ly - bhi / 4);
			g2d.drawLine(right, ly, right - bvi / 4, ly + bhi / 4);
			
			if (!correct)
				return;
			
			g2d.setFont(textFont);
			g2d.drawString(expression, right + bhi, y + bt + bh + bvi/2 + fm.getHeight () / 2);
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
		
		evaluateNextStep();
	}
	
	public void evaluateNextStep() {
		int result = model.evaluateNextStep();
		if (result != 0)
			return;
		
		Expression e = model.getCurrentExpression();
		RuleChain rc = model.getCurrentRuleChain();
		
		SmallStep ss = new SmallStep(rc, "" + e, this, expressionPosX, currentPosY);
		currentPosY += ss.getHeight() + 10;
		
		steps.add(ss);
		repaint();
		
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		Expression e = model.getOriginExpression();
		g2d.setColor(Color.black);
		g2d.setFont(textFont);
		g2d.drawString(""+ e, expressionPosX, expressionPosY);
		
		ListIterator<SmallStep> it = steps.listIterator();
		while (it.hasNext()) {
			it.next().checkMaxWidth();
		}
		it = steps.listIterator();
		while (it.hasNext()) {
			it.next().render(g2d, maxWidth);
		}
	}

}
