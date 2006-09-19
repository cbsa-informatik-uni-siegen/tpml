package de.unisiegen.tpml.graphics.components;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.util.Environment;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.EnvironmentRenderer;
import de.unisiegen.tpml.graphics.renderer.ExpressionRenderer;

public class CompoundExpression<S, E> extends JComponent {
	
	private ExpressionRenderer					expressionRenderer;
	
	private Expression									expression;
	
	private Dimension										expressionSize;
	
	
	private Environment<S, E> 					environment;
	
	private EnvironmentRenderer<S, E>		environmentRenderer;
	
	private Dimension										environmentSize;
	
	private	int													braceSize;
	
	private boolean											noLineWrapping;
	
	private static String								arrowStr = " \u22b3 ";
	
	public CompoundExpression () {
		super ();
		
		this.braceSize = 10;
	}
	
	public void setNoLineWrapping (boolean noLineWrapping) {
		this.noLineWrapping = true;
	}
	
	public void setExpression (Expression expression) {
		this.expression = expression;
		
		if (this.expressionRenderer == null) {
			this.expressionRenderer = new ExpressionRenderer ();
		}
		
		this.expressionRenderer.setPrettyString(this.expression.toPrettyString());
	}
	
	public void setEnvironment (Environment<S, E> environment) {
		this.environment = environment;
		if (this.environment == null) {
			this.environmentRenderer = null;
			return;
		}
		
		if (this.environmentRenderer == null) {
			this.environmentRenderer = new EnvironmentRenderer ();
		}
		
		this.environmentRenderer.setEnvironment(this.environment);
	}
	
	
	/**
	 * Calculates the size needed to propperly render the compoundExpression<br>
	 * <br>
	 *   
	 * @param maxWidth
	 * @return
	 */
	public Dimension getNeededSize (int maxWidth) {
		Dimension result = new Dimension (0, 0);
		
		if (this.noLineWrapping) {
			// to guaranty that no line wrapping should be performed
			// set the maxWidth = MAX_INT
			maxWidth = Integer.MAX_VALUE;
		}
		
		// check whether there is an environment. 
		if (this.environment != null) {
			this.environmentSize = this.environmentRenderer.getNeededSize();
			
			result.height = environmentSize.height;
			
			if (this.environment instanceof Store) {
				/*
				 * the store will be used within the smallstep and bigstep
				 * interpreter
				 * the rendering will appear like this:
				 *      (expression [store])
				 */

				result.width += 2 * this.braceSize;
				
				// there will be a bit of free space between the environment
				// and the expression
				result.width += this.braceSize;
				
				// and the environment size will be missing asswell
				result.width += environmentSize.width;
				
				
				/*
				 * XXX: Decision how the expression should be wrapped
				 */
				maxWidth -= result.width; // ???
			}
			else if (this.environment instanceof TypeEnvironment) {
				/*
				 * the typeenvironment will be used within the typechecker
				 * the rendering will appear like this:
				 * 		  [type] |> expression
				 */

				// the resulting size contains the environment 
				result.width += environmentSize.width;
				
				// and the |> character 
				result.width += AbstractRenderer.getTextFontMetrics().stringWidth(CompoundExpression.arrowStr);
				
				maxWidth -= result.width;
			}

		}
		
		// now check the size still available for the expression
		this.expressionSize = this.expressionRenderer.getNeededSize(maxWidth);
		result.width += expressionSize.width;
		
		if (expressionSize.width > result.width) {
			result.width = expressionSize.width;
		}
		
		return result;
	}
	
	@Override
	protected void paintComponent (Graphics gc) {
		// assuming the size of the component will suffice, no testing
		// of any sizes will happen.
		
		/*
		 * just to get reminded:
		 * 
		 * no environment:       expression
		 * storeenvironment:		(expression [env])
		 * typeenvironment:			[env] |> expression
		 */
		
		int posX = 0;
		int posY = 0;
		// if there is an environment and it is of type Store
		// draw the braces around the entire expression with environment
		if (this.environment instanceof Store) {
			posX += this.braceSize;
			gc.drawArc(0, 0, this.braceSize, getHeight (), 90, 180);
			gc.drawArc(getWidth () - 1 - this.braceSize, 0, this.braceSize, getHeight (), 270, 180);
		}
		

		// if there is no environment or the environment is of type
		// Store, the entire expression (with environment) will begin
		// with the expression
		if (this.environment == null || this.environment instanceof Store) {
			this.expressionRenderer.render(posX, posY, getHeight(), gc);
			posX += this.expressionSize.width;
			
			// if there is an environment render it now
			if (this.environment != null) {
				
				this.environmentRenderer.renderer(posX, posY, this.environmentSize.width, getHeight (), gc);
			}
		}
		else if (this.environment instanceof TypeEnvironment) {
			
			// draw the environment first
			this.environmentRenderer.renderer(posX, posY, this.environmentSize.width, getHeight (), gc);
			
			
			// draw the arrow character in the vertical center
			int centerV = getHeight () / 2;
			centerV += AbstractRenderer.getTextFontMetrics().getAscent() / 2;
			gc.setFont(AbstractRenderer.getTextFont());
			gc.drawString(CompoundExpression.arrowStr, posX, centerV);
			posX += AbstractRenderer.getTextFontMetrics().stringWidth(CompoundExpression.arrowStr);
			
			// draw the expression at the last position.
			this.expressionRenderer.render(posX, posY, getHeight (), gc);
		}
		
		
		
	}

}
