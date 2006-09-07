package ui.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import common.Environment;

import expressions.Expression;

/**
 * Compoment containing the expression aswell as an environment.
 * <br>
 * It is checked wether the environment should be rendered or if
 * it should be checked for the sizecalculation.
 * 
 * @author marcell
 *
 * @param <S> Symbol: Location for Big/SmallStep Identifier for Typechecker
 * @param <E> Entry: Expression for Big/SmallStep Store for the Typechecker
 */
public class CompoundExpression<S, E> extends JComponent{

	/**
	 * The environment assigned to this expression.
	 */
	private Environment<S, E>						environment						= null;
	/**
	 * Renderer that will be used to render the environment
	 * and to determin the needed size for it.
	 */
	private EnvironmentRenderer<S, E>   environmentRenderer		= null;
	/**
	 * Calculated size for the environment.
	 */
	private Dimension										environmentDimension	= null;
	
	/**
	 * The expression itself.
	 */
	private Expression									expression						= null;
	/**
	 * Renderer that will be used to render the expression
	 * and to determin the possible sizes for the expression.
	 */
	private ExpressionRenderer					expressionRenderer		= null;
	/**
	 * Calculated size for the expression.
	 */
	private Dimension										expressionDimension		= null;
	
	/**
	 * Expression that will be underlined. Only used in BigStep
	 */
	private Expression									underlineExpression		= null;
	
	/**
	 * Width for the parenthesis around the compound of expression and
	 * environment in pixels. When no environment is set this variable is ignored.
	 */
	private int													parenthesisWidth			= 10;
	/**
	 * Spacing between the expression and the environment in pixels. 
	 * When no environment is set this variable is ignored.
	 */
	private	int													spacing								= 10;

	/**
	 * Whether the expression/environment should be rendered using the
	 * colors given by the active Theme or just in a light gray.
	 * 
	 * When this CompountExpression is used as the result expression in
	 * the BigStep this should be FALSE else TRUE.
	 */
	private boolean											useColoring						= true;
	
	/**
	 * Default constructor initiates the expression aswell as the environment
	 * with null.
	 *
	 */
	public CompoundExpression () {
	}
	
	/**
	 * Initiates the expression with the given one. The environment will be
	 * set to null. setExpression will get called.
	 * 
	 * @param expression The expression that should be assigned to this CompoundExpression.
	 */
	public CompoundExpression (Expression expression) {
		setExpression (expression);
	}
	
	/**
	 * Initiates the expression and the environment with the given values.
	 * setExpression and setEnvironment will get called.
	 * 
	 * @param expression The expression that should be assigned to this CompoundExpression.
	 * @param environment The environment that should be assigned to this CompoundExpression.
	 */
	public CompoundExpression (Expression expression, Environment<S, E> environment) {
		setExpression (expression);
		setEnvironment (environment);
	}
	
	/**
	 * Sets whether the expression and the environment should be rendered using the
	 * colors from the active theme or just get rendered in a light gray.
	 * 
	 * When using this CompoundExpression as a return value of the BigStep this should
	 * be set FALSE else TRUE.
	 * 
	 * @param coloring TRUE when colors should be used else FALSE.
	 */
	public void setUseColoring (boolean coloring) {
		this.useColoring = coloring;
	}
	
	/**
	 * Assigns a new expression to the CompoundExpression. The possible line wraps
	 * are tested directly aswell as the propper Renderer is created for it.
	 * 
	 * @param expression The expression that should be assigned to this CompoundExpression.
	 */
	public void setExpression (Expression expression) {
		this.expression = expression;

		if (this.expression != null) {
			this.expressionRenderer = new ExpressionRenderer (this.expression);
			this.expressionRenderer.checkFonts();
			this.expressionRenderer.checkAnnotationSizes();
		}
		else {
			this.expressionRenderer = null;
		}
		
		
	}
	
	/**
	 * Sets the Expression that should get underlined during rendering.
	 * 
	 * This is only importend for the SmallStep.
	 * 
	 * @param underlineExpression
	 */
	public void setUnderlineExpression (Expression underlineExpression) {
		this.underlineExpression = underlineExpression;
	}
	
	/**
	 * Assigns a new environment to the CompoundExpression. The renderer that
	 * will be used to render the environment is created aswell.
	 * 
	 * @param environment
	 */
	public void setEnvironment (Environment<S, E> environment) {
		this.environment = environment;
		
		if (this.environment != null) {
			
			this.environmentRenderer = new EnvironmentRenderer<S, E>(this.environment);
			this.environmentRenderer.checkFonts();
			
		}
		else {
			this.environmentRenderer = null;
		}
		
	}
	
	/**
	 * Calculates the Dimension this Component needs to show all its content
	 * in respect to the width available. 
	 * <br>
	 * WARNING! The returned Dimension may be wider then the given width.
	 * 
	 * @param width The visible width of the parent view. 
	 * @return
	 */
	public Dimension getDimensions (int width) {
		this.environmentDimension 	= null;
		this.expressionDimension 	= null;
		int extendedWidth = 0;
	
		if (this.environment == null && this.expression == null) {
			return new Dimension (0, 0);
		}
		
		if (this.environment != null) {
			extendedWidth = 2 * this.parenthesisWidth + this.spacing;
			width -= extendedWidth;
			this.environmentDimension = this.environmentRenderer.getNeededSize();
		}
		else {
			this.environmentDimension = new Dimension (0, 0);
		}

		this.expressionDimension = this.expressionRenderer.getNeededSize(width);
		
		return new Dimension (expressionDimension.width + extendedWidth + environmentDimension.width, expressionDimension.height);
	}
	
	
	/**
	 * Returns the maximum width the expression (without the environment) needs
	 * when no line wrapping is enabled.
	 *  
	 * @return
	 */
	public Dimension getMaxExpressionDimension () {
		if (this.expression == null) {
			return new Dimension (0, 0);
		}
		
		return this.expressionRenderer.getMaxSize();
	}
	

	/**
	 * Paints the compoment. When an environment is given parenthesis will
	 * get rendered around the expression and the environment. 
	 * 
	 * @param gc 
	 */
	@Override
	protected void paintComponent (Graphics gc) {

		gc.setColor(Color.WHITE);
		gc.fillRect(0, 0, getWidth () - 1, getHeight () - 1);

		if (this.expression == null) {
			return;
		}
		
		int posX = 0;
		int posY = 0;
		
		
		if (this.environment != null) {
			// need space for the parenthesises
			posX += this.parenthesisWidth;
		}
		
		this.expressionRenderer.setAlternativeColor(this.useColoring ? null : Color.LIGHT_GRAY);
		this.expressionRenderer.render(posX, posY, this.underlineExpression, gc);
		posX += this.expressionDimension.width;
		
		if (this.environment != null) {
			
			// there will be a comma later or something like this.
			posX += this.spacing;
		
			// render the environment here
			this.environmentRenderer.setAlternativeColor(this.useColoring ? null : Color.LIGHT_GRAY);
			this.environmentRenderer.render(posX, posY, getHeight (), gc);
		}
		
	}
	
}
