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
	private Font			constantFont;
	private FontMetrics		constantFontMetrics;
	
	private	int 			fontAsc;
	private int				fontDesc;
	private int				fontHeight;
	
	public Renderer (Graphics2D g2d, FontMetrics tfm, FontMetrics kfm, FontMetrics cfm) {
		this.g2d 					= g2d;
		this.textFont 				= tfm.getFont();
		this.textFontMetrics		= tfm;
		this.keywordFont			= kfm.getFont();
		this.keywordFontMetrics		= kfm;
		this.constantFont			= cfm.getFont();
		this.constantFontMetrics	= cfm;
		
		
		fontAsc = keywordFontMetrics.getAscent();
		fontDesc = keywordFontMetrics.getDescent();
		fontHeight = keywordFontMetrics.getHeight();
		if (fontAsc < textFontMetrics.getAscent()) fontAsc = textFontMetrics.getAscent();
		if (fontDesc< textFontMetrics.getDescent()) fontDesc = textFontMetrics.getDescent();
		if (fontHeight < textFontMetrics.getHeight()) fontHeight = textFontMetrics.getHeight();
		
		if (fontAsc < constantFontMetrics.getAscent()) fontAsc = constantFontMetrics.getAscent();
		if (fontDesc < constantFontMetrics.getDescent()) fontDesc = constantFontMetrics.getDescent();
		if (fontHeight < constantFontMetrics.getHeight()) fontHeight = constantFontMetrics.getHeight();
	}
	
	public void renderHighlightedExpression (int x, int y, int maxWidth, int maxHeight, PrettyString s, Expression expr) {
		int posX = x;
		int posY = y + maxHeight / 2 + (int)(fontAsc / 3.0f);
		PrettyAnnotation annotation = null;
		if (expr != null) {
			try {
				annotation = s.getAnnotationForExpression(expr);
			} catch (Exception e) {
				annotation = null;
			}
		}
		PrettyCharIterator it = s.toCharacterIterator();
		int i = 0;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			int length = 0;
			switch (it.getStyle()) {
			case NONE:
				g2d.setFont(this.textFont);
				g2d.drawString("" + c, posX, posY);
				length = this.textFontMetrics.stringWidth("" + c);
				break;
			case KEYWORD:
				g2d.setFont(this.keywordFont);
				g2d.drawString("" + c, posX, posY);
				length = this.keywordFontMetrics.stringWidth("" + c);
				break;
			case CONSTANT:
				g2d.setColor(new Color(0, 127, 0));
				g2d.setFont(this.constantFont);
				g2d.drawString("" + c, posX, posY);
				length = this.constantFontMetrics.stringWidth("" + c);
				g2d.setColor(Color.BLACK);
				break;
			}
			if (annotation != null) {
				if (i >= annotation.getStartOffset() && i <= annotation.getEndOffset()) {
					g2d.setColor(Color.RED);
					g2d.drawLine(posX, posY + 3, posX + length, posY + 3);
					g2d.drawLine(posX, posY + 4, posX + length, posY + 4);
					g2d.setColor(Color.BLACK);
				}
			}
			posX += length;
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
			switch(it.getStyle()) {
			case NONE:
				posX += this.textFontMetrics.stringWidth("" + c);
				break;
			case KEYWORD:
				posX += this.keywordFontMetrics.stringWidth("" + c);
				break;
			case CONSTANT:
				posX += this.constantFontMetrics.stringWidth("" + c);
				break;
			}
		}
		return new Dimension(posX, maxFontHeight);
	}
}
