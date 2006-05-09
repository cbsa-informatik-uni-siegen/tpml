package ui.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.CharacterIterator;

import javax.swing.JComboBox;


import expressions.Expression;
import expressions.PrettyAnnotation;
import expressions.PrettyCharIterator;
import expressions.PrettyString;

/**
 * Class providing support to render an expression.
 * 
 * @author marcell
 *
 */
public class ExpressionRenderer {
	
	private class CheckerReturn {
		public Dimension				size;
		public int					rows;
		public PrettyAnnotation		annotation;
		public CheckerReturn(Dimension s, int r, PrettyAnnotation a) {
			this.size 		= s; 
			this.rows 		= r; 
			this.annotation	= a;
		}
	}
	
	private 	Expression			expression;
	
	private 	Font				textFont;
	private		FontMetrics			textFontMetrics;
	
	private		Font				keywordFont;
	private		FontMetrics			keywordFontMetrics;
	
	private		Font				constantFont;
	private		FontMetrics			constantFontMetrics;
	
	
	private		Color				textColor;
	private		Color				keywordColor;
	private		Color				constantColor;
	private		Color				underlineColor;	
	
	private 	int					fontHeight;
	private		int					fontAscent;
	private		int					fontDescent;
	
	private 	CheckerReturn		bestCheckerReturn;
	
	
	private CheckerReturn checkExpression (PrettyString prettyString, PrettyAnnotation annotation) {
		Dimension d = new Dimension ();
		int width 	= 0;
		int height	= 0;
		PrettyCharIterator it = prettyString.toCharacterIterator();
		int[] annoBreaks = null;
		if (annotation != null)
			annoBreaks = annotation.getBreakOffsets();
		int i = 0;
		int rows = 0;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			
			// first check if annotation are given
			// that way I can check the raw string itself
			if (annotation != null) {
				
				// check if the one of the breakpoints is set at the current cursor pos 
				for (int j=0; j<annoBreaks.length; j++) {
					if (annoBreaks[j] == i) {
						height += this.fontHeight;
						if (width > d.width) d.width = width;
						// next line will be indentated by 10 pixels
						width = 10;
						rows++;
						// finished to check the annotations for this string position
						break;
					}
				}
			}
			
			switch (it.getStyle()) {
			case NONE: 		width += this.textFontMetrics.stringWidth("" + c); 		break;
			case KEYWORD: 	width += this.keywordFontMetrics.stringWidth("" + c); 	break;
			case CONSTANT:	width += this.constantFontMetrics.stringWidth("" + c); 	break;
			}
			
		}
		// check the width and the height for the last line. there wouldn't be any breakpoint
		if (width > d.width) d.width = width;
		height += this.fontHeight;
		d.height = height;
		rows++;

		return new CheckerReturn (d, rows, annotation);
	}
	
	/**
	 * 
	 * @param gc
	 * @param font
	 */
	public ExpressionRenderer (Expression expression) {
		this.expression	= expression;	
	}
	

	public void setTextStyle (Font font, FontMetrics fontMetrics, Color fontColor) {
		this.textFont 			= font;
		this.textFontMetrics	= fontMetrics;
		this.textColor			= fontColor;
	}
	
	public void setKeywordStyle (Font font, FontMetrics fontMetrics, Color fontColor) {
		this.keywordFont 			= font;
		this.keywordFontMetrics		= fontMetrics;
		this.keywordColor			= fontColor;
	}
	
	public void setConstantStyle (Font font, FontMetrics fontMetrics, Color fontColor) {
		this.constantFont 			= font;
		this.constantFontMetrics	= fontMetrics;
		this.constantColor			= fontColor;
	}
	
	public void setUnderlineColor (Color color) {
		this.underlineColor	= color;
	}
	
	public void checkFonts() {
		this.fontHeight 	= this.textFontMetrics.getHeight();
		this.fontAscent		= this.textFontMetrics.getAscent();
		this.fontDescent	= this.textFontMetrics.getDescent();
		if (this.fontHeight < this.keywordFontMetrics.getHeight()) { 
			this.fontHeight 	= this.keywordFontMetrics.getHeight();
			this.fontAscent		= this.keywordFontMetrics.getAscent();
			this.fontDescent	= this.keywordFontMetrics.getDescent();
		}
		if (this.fontHeight < this.constantFontMetrics.getHeight()) {
			this.fontHeight 	= this.constantFontMetrics.getHeight();
			this.fontAscent		= this.constantFontMetrics.getAscent();
			this.fontDescent	= this.constantFontMetrics.getDescent();
		}
	}
	
	/**
	 * Calculates the "best" size needed for the Expression.
	 * <br>
	 * The returned Dimension may not fit the hint size.
	 * 
	 * @param expression	The environment containing the key-value-pairs 
	 * @param hint			A width the gui "wants" to spend for the environment
	 * @return				The actual needed size for the environment
	 */
	public Dimension getNeededSize (int maxWidth) {
		// first check the expression with no line breaks
		PrettyString prettyString = this.expression.toPrettyString();
		CheckerReturn smallest	= null;
		CheckerReturn biggest 	= null;
		
		// put the first checkExpression in all the used CheckerReturn
		CheckerReturn current = checkExpression (prettyString, null);
		if (current.size.width <= maxWidth) {
			this.bestCheckerReturn = current;
			return bestCheckerReturn.size;
		}
		
		// now check for the biggest style to render the expression that
		// is smaller than the maxWidth
		// and check for the real smallest style
		for (PrettyAnnotation pa : prettyString.getAnnotations()) {
			current = checkExpression (prettyString, pa);
			if (smallest == null || current.size.width < smallest.size.width) {
				smallest = current;
			}
			if (current.size.width <= maxWidth && (biggest == null || current.size.width > biggest.size.width)) {
				biggest = current;
			}
		}
		
		this.bestCheckerReturn = biggest;
		if (this.bestCheckerReturn == null) {
			this.bestCheckerReturn = smallest;
		}
		return this.bestCheckerReturn.size;
	}
	
	/**
	 * Renders the Expression at the position given by (x, y).
	 * <br>
	 * It is guarenteed that the size needed for the rendering is exactly the
	 * Size calculated by {@link #getNeededSize(Expression, Dimension)}.
	 * 
	 * @param	x			The horizontal coordinate to render
	 * @param	y			The vertical coordinate to render
	 * @param 	environment	The environment that should be rendered.
	 * @param	gc			The graphics context used to render the content.
	 */
	public void render(int x, int y, Graphics2D gc) {
		
		PrettyString prettyString = this.expression.toPrettyString();
		PrettyCharIterator it = prettyString.toCharacterIterator();
		int[] annoBreaks = null;
		if (this.bestCheckerReturn.annotation != null) {
			annoBreaks = this.bestCheckerReturn.annotation.getBreakOffsets();
		}
		int i = 0;
		int posX = x;
		int posY = y + this.fontHeight - this.fontDescent;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			
			if (this.bestCheckerReturn.annotation != null) {
				
				for (int j=0; j<annoBreaks.length; j++) {
					if (annoBreaks[j] == i) {
						posY += this.fontHeight;
						posX = x + 10;
						break;
					}
				}
			}
			Color		fontColor	= null;
			FontMetrics fontMetrics = null;
			Font		font 		= null;
			
			switch (it.getStyle()) {
			case NONE:
				fontColor 	= this.textColor;
				fontMetrics = this.textFontMetrics; 	
				font 		= this.textFont;
				break;
			case KEYWORD:
				fontColor	= this.keywordColor;
				fontMetrics = this.keywordFontMetrics;
				font 		= this.keywordFont;
				break;
			case CONSTANT:
				fontColor	= this.constantColor;
				fontMetrics = this.constantFontMetrics;
				font 		= this.constantFont;
				break;
			}
			gc.setColor(fontColor);
			gc.setFont(font);
			gc.drawString("" + c, posX, posY);
			posX += fontMetrics.stringWidth("" + c);
			
		}
	}
}
