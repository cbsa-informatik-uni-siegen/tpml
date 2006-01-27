package ui;

import java.awt.*;
import java.text.*;
import java.util.Collection;
import java.util.Iterator;

import smallstep.*;

public class Renderer {
	
	
	private Font			textFont;
	private FontMetrics 	textFontMetrics;
	private Font			keywordFont;
	private FontMetrics		keywordFontMetrics;
	private Font			constantFont;
	private FontMetrics		constantFontMetrics;
	
	private Color			textColor;
	private Color			keywordColor;
	private Color			constantColor;
	
	private	int 			fontAsc;
	private int				fontDesc;
	private int				fontHeight;
	private int				neededRows;
	private int				selectedAnnotation;
	
	public Renderer (FontMetrics tfm, FontMetrics kfm, FontMetrics cfm, Color tc, Color kc, Color cc) {
		this.textFont 				= tfm.getFont();
		this.textFontMetrics		= tfm;
		this.keywordFont			= kfm.getFont();
		this.keywordFontMetrics		= kfm;
		this.constantFont			= cfm.getFont();
		this.constantFontMetrics	= cfm;
		this.neededRows				= 1;
		this.textColor				= tc;
		this.keywordColor			= kc;
		this.constantColor			= cc;
		
		
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
	
	public int getSelectedAnnotation() {
		return this.selectedAnnotation;
	}
	
	public int getRenderHeight() {
		return this.fontHeight;
	}
	
	private Dimension checkExpression (PrettyString prettyString, PrettyAnnotation annotation) {
		Dimension d = new Dimension ();
		int width = 0;
		int height = 0;
		PrettyCharIterator it = prettyString.toCharacterIterator();
		int i = 0;
		this.neededRows = 1;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			if (annotation != null) {
				for (int j=0; j<annotation.getBreakOffsets().length; j++) {
					if (annotation.getBreakOffsets()[j] == i) {
						height += this.fontHeight;
						if (width > d.width) {
							d.width = width;
						}
						width = 10;
						this.neededRows++;
						break;
					}
				}
			}
			switch (it.getStyle()) {
			case NONE:
				width += this.textFontMetrics.stringWidth("" + c);
				break;
			case KEYWORD:
				width += this.keywordFontMetrics.stringWidth("" + c);
				break;
			case CONSTANT:
				width += this.constantFontMetrics.stringWidth("" + c);
				break;
			}
		}
		if (width > d.width) {
			d.width = width;
		}
		height += this.textFontMetrics.getHeight();
		d.height = height;	
		
		return d;
	}
	
	public Dimension getBestSize (PrettyString prettyString, int maxWidth) {
		Dimension d = checkExpression (prettyString, null);
		Dimension bestSize = d;
		
		this.selectedAnnotation = -1;
		int bestDiff = d.width - maxWidth;
		
		if (d.width > maxWidth) {
			Iterator<PrettyAnnotation> anit = prettyString.getAnnotations().iterator();
			int annotationNr = 0;
			while (anit.hasNext()) {
				
				PrettyAnnotation anno = anit.next();
				if (anno.getBreakOffsets().length != 0) {
					
					boolean switchSize = false;
					d = checkExpression (prettyString, anno);
					int diff = d.width - maxWidth;
					if (diff <= 0) {
						if (bestDiff < 0 && diff > bestDiff || bestDiff > 0) {
							switchSize = true;
						}
					}
					else {
						if (bestDiff > diff) {
							switchSize = true;
						}
					}
					
					if (switchSize) {
						bestDiff = diff;
						bestSize = d;
						this.selectedAnnotation = annotationNr;
					}
					
				}
				annotationNr++;
			}
		}
		return bestSize;
	}
	
	public void renderHighlightedExpression (Graphics2D g2d, int x, int y, int w, int h,
			PrettyString expressionString, PrettyAnnotation annotation,
			Expression underlineExpression) {

		int posx = x;
		int posy = y + h / 2;
		posy += (this.fontAsc - this.fontDesc) / 2;
		
		if (annotation != null) {
			posy -= annotation.getBreakOffsets().length * this.fontHeight / 2;
		}
		
		PrettyAnnotation underlineAnnotation = null;
		if (underlineExpression != null) {
			try {
				underlineAnnotation = expressionString.getAnnotationForExpression(underlineExpression);
			} catch (Exception e) {
				underlineAnnotation = null;
			}
		}
		
		
		PrettyCharIterator it = expressionString.toCharacterIterator();
		int i = 0;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			int length = 0;
			if (annotation != null) {
				for (int j=0; j<annotation.getBreakOffsets().length; j++) {
					if (annotation.getBreakOffsets()[j] == i) {
						posy += this.fontHeight;
						posx = x + 10;
					}
				}
			}
			switch (it.getStyle()) {
			case NONE:
				g2d.setColor(this.textColor);
				g2d.setFont(this.textFont);
				g2d.drawString("" + c, posx, posy);
				length = this.textFontMetrics.stringWidth("" + c);
				break;
			case KEYWORD:
				g2d.setColor(this.keywordColor);
				g2d.setFont(this.keywordFont);
				g2d.drawString("" + c, posx, posy);
				length = this.keywordFontMetrics.stringWidth("" + c);
				break;
			case CONSTANT:
				g2d.setColor(this.constantColor);
				g2d.setFont(this.constantFont);
				g2d.drawString("" + c, posx, posy);
				length = this.constantFontMetrics.stringWidth("" + c);
				break;
			}
			if (underlineAnnotation != null) {
				if (i >= underlineAnnotation.getStartOffset() && i <= underlineAnnotation.getEndOffset()) {
					g2d.setColor(Color.RED);
					g2d.drawLine(posx, posy + 3, posx + length, posy + 3);
					g2d.drawLine(posx, posy + 4, posx + length, posy + 4);
					g2d.setColor(Color.BLACK);
				}
			}
			posx += length;
		}
		g2d.setColor(Color.BLACK);
		
	}
	
}
