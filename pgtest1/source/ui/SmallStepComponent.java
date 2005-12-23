package ui;

import java.util.*;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.event.EventListenerList;

import java.awt.*;
import smallstep.*;

public class SmallStepComponent extends JComponent {

	private SmallStepModel			model;
	
	private LinkedList<SmallStep>	smallStepSteps = new LinkedList<SmallStep>();
	
	private boolean					justAxioms;
	
	private boolean					underlineExpressions;
	
	private EventListenerList		listenerList = new EventListenerList();

	public SmallStepComponent(SmallStepModel model, boolean justAxioms, boolean underlineExpressions) {
		super();
		setLayout(null);
		
		this.model = model;
		this.justAxioms = justAxioms;
		this.underlineExpressions = underlineExpressions;
		
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
	
	public int evaluateNextStep() {
		int res = model.evaluateNextStep();
		if (res == 0) {
			// this is a proper next step
			SmallStep parent = smallStepSteps.getLast();
			SmallStep step = new SmallStep(parent, model.getCurrentExpression(), model.getCurrentRuleChain());
			if (this.justAxioms) {
				step.resolveMetaRules();
			}
			
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
		fireSmallStepResolved();
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
