package ui.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import expressions.Expression;

public class ExpressionComponent extends JComponent {

	
	private ExpressionRenderer	renderer		= null;
	
	private Expression					expression	= null;
	
	public ExpressionComponent (Expression expression) {
		if (expression == null) {
			return;
		}
		
		setExpression (expression);
		
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
		this.renderer = new ExpressionRenderer (expression);
		this.renderer.checkFonts();
		this.renderer.checkAnnotationSizes();
	}
	
	public Expression getExpression() {
		return this.expression;
	}
	
	
	
	public Dimension getSizeForWidth (int width) {
		if (this.renderer == null) {
			return new Dimension (0, 0);
		}
		return this.renderer.getNeededSize(width);
	}
	
	public int getRowCount() {
		if (this.renderer == null) {
			return 1;
		}
		
		return this.renderer.getRowCount();
	}
	
	
	@Override
	public void paintComponent (Graphics graphics) {
		if (this.expression == null || this.renderer == null) {
			return;
		}
		this.renderer.render(0, 0, null, graphics, getWidth ());
	}
	
	@Override
	public Dimension getPreferredSize () {
		if (this.renderer == null || this.expression == null) {
			return new Dimension (0, 0);
		}
		return renderer.getMaxSize();
	}
	
	@Override
	public Dimension getMaximumSize() {
		if (this.renderer == null || this.expression == null) {
			return new Dimension (0, 0);
		}
		
		return renderer.getMaxSize();
	}
	
	@Override
	public Dimension getMinimumSize () {
		if (this.renderer == null || this.expression == null) {
			return new Dimension (0, 0);
		}
		
		return renderer.getMinSize();
	}
}
