package ui;

import java.awt.*;
import java.text.*;
import smallstep.*;

public class Renderer {
	
	/**
	 * The graphics context
	 */
	private Graphics2D 		g2d;
	
	private Font			textFont;
	private FontMetrics 	textFontMetrics;
	private Font			keywordFont;
	private FontMetrics		keywordFontMetrics;
	
	private	int 			fontAsc;
	private int				fontDesc;
	private int				fontHeight;
	
	public Renderer (Graphics2D g2d, FontMetrics tfm, FontMetrics kwfm) {
		this.g2d 				= g2d;
		this.textFont 			= tfm.getFont();
		this.textFontMetrics	= tfm;
		this.keywordFont		= kwfm.getFont();
		this.keywordFontMetrics	= kwfm;
		
		fontAsc = keywordFontMetrics.getAscent();
		fontDesc = keywordFontMetrics.getDescent();
		fontHeight = keywordFontMetrics.getHeight();
		if (fontAsc < textFontMetrics.getAscent()) fontAsc = textFontMetrics.getAscent();
		if (fontDesc< textFontMetrics.getDescent()) fontDesc = textFontMetrics.getDescent();
		if (fontHeight < textFontMetrics.getHeight()) fontHeight = textFontMetrics.getHeight();
	}
	
	public void renderHighlightedExpression (int x, int y, int maxWidth, int maxHeight, PrettyString s) {
		int textY = y + maxHeight / 2 + (int)(fontAsc / 3.0f);
		
		PrettyCharIterator it = s.toCharacterIterator();
		int posX = x;
		int posY = y + maxHeight - fontDesc;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
			if (it.isKeyword()) {
				g2d.setFont(this.keywordFont);
				g2d.drawString("" + c, posX, textY);
				posX += this.keywordFontMetrics.stringWidth("" + c);
			}
			else {
				g2d.setFont(this.textFont);
				g2d.drawString("" + c, posX, textY);
				posX += this.textFontMetrics.stringWidth("" + c);
			}
		}
	}
	
	public Dimension checkRenderSize (PrettyString s, int x, int y) {
		PrettyCharIterator it = s.toCharacterIterator();
		int maxFontHeight = keywordFontMetrics.getHeight();
		if (maxFontHeight < textFontMetrics.getHeight())
			maxFontHeight = textFontMetrics.getHeight();
		// XXX
		int posX = x;
		int posY = y;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
			if (it.isKeyword()) {
				posX += this.keywordFontMetrics.stringWidth("" + c);
			}
			else {
				posX += this.textFontMetrics.stringWidth("" + c);
			}
		}
		return new Dimension(posX, maxFontHeight);
	}
}
