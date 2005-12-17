package ui;

import java.util.*;
import java.awt.Dimension;
import javax.swing.JComponent;
import java.awt.*;
import smallstep.*;

public class SmallStepComponent extends JComponent {

	private SmallStepModel			model;
	
	private LinkedList<SmallStep>	smallStepSteps = new LinkedList<SmallStep>();
	
	public SmallStepComponent(SmallStepModel model) {
		super();
		setLayout(null);
		
		this.model = model;
		
		// create the first Step;
		SmallStep step = new SmallStep(null, model.getOriginExpression(), null);
		add(step);
		step.setBounds(10, 10, step.getWidth(), step.getHeight());	
		smallStepSteps.add(step);
		
		// and now evaluate the first "real" step
		evaluateNextStep();
	}
	
	public int evaluateNextStep() {
		int res = model.evaluateNextStep();
		if (res == 0) {
			// this is a proper next step
			SmallStep parent = smallStepSteps.getLast();
			SmallStep step = new SmallStep(parent, model.getCurrentExpression(), model.getCurrentRuleChain());
			add(step);
			step.setBounds(10, parent.getY() + parent.getHeight() + 20, step.getWidth(), step.getHeight());
			smallStepSteps.add(step);
			
			step.addSmallStepEventListener(new SmallStepEventListener() {
				public void smallStepResized(EventObject o) { calculateCenter(); }
				public void smallStepResolved(EventObject o) { evaluateNextStep(); }
				public void mouseFocusEvent(SmallStepEvent e) {
					underlineSequence((SmallStep)e.getSource(), e.getRule());
				};
			});
			this.underlineSequence(null, null);
		}
		calculateCenter();
		return res;
	}
	
	public void calculateCenter() {
		int center = 0;
		int maxWidth = 0;
		int maxHeight = 0;
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
			if (s.getX() + s.getWidth() > maxWidth) {
				maxWidth = s.getX () + s.getWidth();
			}
			if (s.getY () * s.getHeight () > maxHeight) {
				maxHeight = s.getY () + s.getHeight();
			}
		}
		repaint();
		setPreferredSize (new Dimension (maxWidth + 20, maxHeight + 20));
	}
	
	public void paintComponent(Graphics g) {
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
}
