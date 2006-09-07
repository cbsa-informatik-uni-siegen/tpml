package ui.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.CharacterIterator;
import java.util.LinkedList;

import common.prettyprinter.PrettyAnnotation;
import common.prettyprinter.PrettyCharIterator;
import common.prettyprinter.PrettyString;

import expressions.Expression;

/**
 * Class providing support to render an expression.
 * 
 * @author marcell
 *
 */
public class ExpressionRenderer extends AbstractRenderer {
	
	private class CheckerResult {
		public Dimension			size;
		public int					rows;
		public PrettyAnnotation		annotation;
		public CheckerResult(Dimension s, int r, PrettyAnnotation a) {
			this.size 		= s; 
			this.rows 		= r; 
			this.annotation	= a;
		}
	}
	
	private 	Color						alternativeColor = null;
	
	private 	Dimension				minSize;
	
	private 	Dimension				maxSize;
	
	private 	Expression			expression;
	
	private 	CheckerResult		bestCheckerReturn;
	
	private 	LinkedList<CheckerResult>	checkerResults = new LinkedList<CheckerResult>();
	
	
	public void setAlternativeColor (Color alternativeColor) {
		this.alternativeColor = alternativeColor;
	}
	
	public Dimension getMinSize () {
		return this.minSize;
	}
	
	public Dimension getMaxSize() {
		return this.maxSize;
	}
	
	private CheckerResult checkExpression (PrettyString prettyString, PrettyAnnotation annotation) {
		Dimension d = new Dimension ();
		int width 	= 0;
		int height	= 0;
		PrettyCharIterator it = prettyString.toCharacterIterator();
		int[] annoBreaks = null;
		if (annotation != null) {
			annoBreaks = annotation.getBreakOffsets();
		}
		int i = 0;
		int rows = 0;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			
			// first check if annotation are given
			// that way I can check the raw string itself
			if (annotation != null) {
				
				// check if the one of the breakpoints is set at the current cursor pos 
				for (int j=0; j<annoBreaks.length; j++) {
					if (annoBreaks[j] == i) {
						height += fontHeight;
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
			case NONE: 		width += textFontMetrics.stringWidth("" + c); 		break;
			case KEYWORD: 	width += keywordFontMetrics.stringWidth("" + c); 	break;
			case CONSTANT:	width += constantFontMetrics.stringWidth("" + c); 	break;
			}
			
		}
		// check the width and the height for the last line. there wouldn't be any breakpoint
		if (width > d.width) d.width = width;
		height += fontHeight;
		d.height = height;
		rows++;
		
		if (this.minSize == null || this.minSize.width > d.width) {
			this.minSize = d;
		}
		
		if (this.maxSize == null || this.maxSize.width < d.width) {
			this.maxSize = d;
		}

		return new CheckerResult (d, rows, annotation);
	}
	
	/**
	 * 
	 * @param gc
	 * @param font
	 */
	public ExpressionRenderer (Expression expression) {
		this.expression	= expression;	
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
		CheckerResult smallest 	= null;
		CheckerResult biggest	= null;
		for (CheckerResult cr : this.checkerResults) {
			if (smallest == null || cr.size.width < smallest.size.width) {
				smallest = cr;
			}
			if (cr.size.width <= maxWidth && (biggest == null || cr.size.width > biggest.size.width)) {
				biggest = cr;
			}
		}
		
		this.bestCheckerReturn = biggest;
		if (this.bestCheckerReturn == null) {
			this.bestCheckerReturn = smallest;
		}
		return new Dimension (this.bestCheckerReturn.size);
	}
	
	
	public int getRowCount () {
		if (this.bestCheckerReturn == null)
			return 1;
		return this.bestCheckerReturn.rows;
	}
	
	public void checkAnnotationSizes() {
		this.checkerResults.clear();
		PrettyString prettyString = this.expression.toPrettyString();
		CheckerResult cr = checkExpression(prettyString, null);
		this.checkerResults.add(cr);
		for (PrettyAnnotation pa : prettyString.getAnnotations()) {
			cr = checkExpression (prettyString, pa);
			this.checkerResults.add(cr);
		}
	}
	
	
	public void render(int x, int y, Expression underline, Graphics gc, int maxWidth) {
		getNeededSize(maxWidth);
		
		render (x, y, underline, gc);
	}
	
	/**
	 * Renders the Expression at the position given by (x, y).
	 * <br>
	 * It is guarenteed that the size needed for the rendering is exactly the
	 * Size calculated by {@link #getNeededSize(Expression, Dimension)}.
	 * 
	 * @param	x			The horizontal coordinate to render
	 * @param	y			The vertical coordinate to render
	 * @param underline The Expression that should be underlined.
	 * @param	gc			The graphics context used to render the content.
	 */
	public void render(int x, int y, Expression underline, Graphics gc) {
		
		
		
		PrettyString prettyString = this.expression.toPrettyString();
		PrettyCharIterator it = prettyString.toCharacterIterator();
		int[] annoBreaks = null;
		
		if (this.bestCheckerReturn != null && this.bestCheckerReturn.annotation != null) {
			annoBreaks = this.bestCheckerReturn.annotation.getBreakOffsets();
		}
		
		// get the offsets of the underline expression
		int uStart 	= -1;
		int uEnd  	= -1;
		if (underline != null) {
			try {
				PrettyAnnotation ua = prettyString.getAnnotationForPrintable(underline);
				uStart	= ua.getStartOffset();
				uEnd	= ua.getEndOffset();
			} catch (Exception e) { }
		}
		int i = 0;
		int posX = x;
		int posY = y + fontHeight - fontDescent;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			
			if (this.bestCheckerReturn != null && this.bestCheckerReturn.annotation != null) {
				
				for (int j=0; j<annoBreaks.length; j++) {
					if (annoBreaks[j] == i) {
						posY += fontHeight;
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
				fontColor 	= textColor;
				fontMetrics = textFontMetrics; 	
				font 		= textFont;
				break;
			case KEYWORD:
				fontColor	= keywordColor;
				fontMetrics = keywordFontMetrics;
				font 		= keywordFont;
				break;
			case CONSTANT:
				fontColor	= constantColor;
				fontMetrics = constantFontMetrics;
				font 		= constantFont;
				break;
			}
			if (alternativeColor != null) {
				fontColor = alternativeColor;
			}
			
			int sx = posX;
			posX += fontMetrics.stringWidth("" + c);
			if (i >= uStart && i <= uEnd) {
				gc.setColor(underlineColor);
				gc.drawLine(sx,posY + 1, posX, posY + 1);
			}
			gc.setColor(fontColor);
			gc.setFont(font);
			gc.drawString("" + c, sx, posY);
			
		}
	}
}
