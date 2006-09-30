package de.unisiegen.tpml.graphics.renderer;


import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import java.text.CharacterIterator;
import java.util.LinkedList;

import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;



public class PrettyStringRenderer extends AbstractRenderer {
	
	private class CheckerResult {
		public PrettyAnnotation		annotation;
		public int								rows;
		public Dimension					size;
		public CheckerResult() {
			this.size = new Dimension ();
		}
	}
	
	private LinkedList<CheckerResult>		results;

	private PrettyString								prettyString;
	
	private CheckerResult								result;
	
	public PrettyStringRenderer() {
		this.results 		= new LinkedList<CheckerResult>();
		this.result			= null;
	}
	
	public void setPrettyString (PrettyString prettyString) {
		this.prettyString = prettyString;
		
		checkLinewraps ();
	}

	
	/**
	 * Checks which of the previously calculated result is the best
	 * match for the given maxWidth.<br>
	 * <br>
	 * The size of the widest result that still fits in the maxWidth
	 * will be returned. If no result is stored that fits in the
	 * smallest size is returned; in this case the returned size
	 * is wider than the given maxWidth.
	 * 
	 * 
	 * @param maxWidth The maximum available size for this expression. 
	 * @return The dimensions that will be needed to perform the rendering
	 * later.
	 */
	public Dimension getNeededSize (int maxWidth) {

		// Find the biggest result, that is smaller than the given
		// maxWidth. If none could be found the smllest result available
		// will be used.
		CheckerResult smallestResult = null;
		CheckerResult biggestResult = null;
		
		for (CheckerResult r : this.results) {
			if (smallestResult == null || smallestResult.size.width > r.size.width) {
				smallestResult = r;
			}

			if (r.size.width < maxWidth) {
				if (biggestResult == null || biggestResult.size.width < r.size.width) {
					biggestResult = r;
				}
			}
		}
		if (biggestResult != null)  {
			this.result = biggestResult;
		}
		else {
			this.result = smallestResult;
		}
		
		return this.result.size;
	}

	/**
	 */
	private void checkLinewraps () {
		this.results.clear();
		
		this.results.add(checkLinewrap (null));
		
		for (PrettyAnnotation annotation : this.prettyString.getAnnotations()) {
			this.results.add(checkLinewrap (annotation));
		}
		
	}

	/**
	 * Checks the size that would be needed when the expression 
	 * would be rendered using the given Annotation.
	 * 
	 * @param annotation 
	 * @return
	 */
	private CheckerResult checkLinewrap (PrettyAnnotation annotation) {
		
		CheckerResult result = new CheckerResult ();
		result.rows = 1;
		
		
		int[] breakOffsets = null;
		if (annotation != null) {
			breakOffsets = annotation.getBreakOffsets();
		}
		else {
			breakOffsets = new int[0];
		}
		
		result.annotation = annotation;
		result.size.height = AbstractRenderer.fontHeight;
		
		PrettyCharIterator it = this.prettyString.toCharacterIterator();
		int i = 0;
		int w = 0;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			
			for (int j=0; j<breakOffsets.length; j++) {
				if (breakOffsets [j] == i) {
					result.size.height += AbstractRenderer.fontHeight;
					result.size.width = Math.max(w, result.size.width);
					result.rows++;
					
					// let the next line be to big fonts indentated
					w = AbstractRenderer.expFontMetrics.stringWidth("GG");
					break;
				}
			}
			
			switch (it.getStyle()) {
			case NONE:
				w += AbstractRenderer.expFontMetrics.charWidth(c);
				break;
			case KEYWORD:
				w += AbstractRenderer.keywordFontMetrics.charWidth(c);
				break;
			case CONSTANT:
				w += AbstractRenderer.constantFontMetrics.charWidth(c);
				break;
			case COMMENT:
				break;
			case TYPE:
				w += AbstractRenderer.typeFontMetrics.charWidth (c);
				break;
			}

		}
		result.size.width = Math.max(w, result.size.width);
		return result;
	}

	
	/**
	 * Renders the Prettystring.
	 * <br><br>
	 * The Prettystring will be rendered verticaly centered between the
	 * position y and (y + height).
	 * 
	 * @param x Left position where the rendering should take place
	 * @param y Top position where the rendering should take place
	 * @param height The height that is available for the rendering.
	 * @param gc The Graphics context that will be used to render
	 * @return The width of the expression will get returned.
	 */
	public void render (int x, int y, int height,  Graphics gc) {
		
		int[] breakOffsets = null;
		if (this.result.annotation != null) {
			breakOffsets = this.result.annotation.getBreakOffsets();
		}
		else {
			breakOffsets = new int[0];
		}
		
		
		// get the starting offsets x is just the left border
		// y will be the center of the space available minus the
		// propper amount of rows 
		int i = 0;
		int posX = x;
		int posY = y + height / 2;
		posY += AbstractRenderer.fontAscent / 2;
		
		float addY = (this.result.rows - 1) / 2.0f;
		addY *= AbstractRenderer.fontHeight;
		posY -= addY;
		
		
		// now we can start to render the expression
		PrettyCharIterator it = this.prettyString.toCharacterIterator();
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
		
			for (int j=0; j<breakOffsets.length; j++) {
				if (breakOffsets [j] == i) {
					posY += AbstractRenderer.fontHeight;
					
					posX = x;
				}
			}
			

			// select the proppert font and color for the character
			FontMetrics fm = null;
			switch (it.getStyle()) {
			case NONE:
				gc.setFont(AbstractRenderer.expFont);
				gc.setColor(AbstractRenderer.expColor);
				fm = AbstractRenderer.expFontMetrics;
				break;
			case KEYWORD:
				gc.setFont(AbstractRenderer.keywordFont);
				gc.setColor(AbstractRenderer.keywordColor);
				fm = AbstractRenderer.keywordFontMetrics;
				break;
			case CONSTANT:
				gc.setFont(AbstractRenderer.constantFont);
				gc.setColor(AbstractRenderer.constantColor);
				fm = AbstractRenderer.constantFontMetrics;
				break;
			default:
				continue;
			}
			
			if (this.alternativeColor != null) {
				gc.setColor(this.alternativeColor);
			}
			
			// draw the character and move the position
			gc.drawString("" + c, posX, posY);
			
			posX += fm.stringWidth("" + c);
			
			// go on to the next character
		}
		
		
	}
	
	
}


