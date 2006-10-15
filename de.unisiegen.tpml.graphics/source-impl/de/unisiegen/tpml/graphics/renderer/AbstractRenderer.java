package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;

import de.unisiegen.tpml.graphics.theme.Theme;


/**
 * Abstract base class for the {@link PrettyStringRenderer} providing 
 * fonts, colors and fontmetrices to perform the rendering.<br>
 * <br>
 * All the Colors and Fonts coms from the current {@link Theme}.
 * 
 * 
 * @author marcell
 *
 */
public abstract class AbstractRenderer {
	

	protected 	static Font						expFont;
	protected		static FontMetrics		expFontMetrics;
	protected		static Color					expColor;
	
	
	protected		static Font						keywordFont;
	protected		static FontMetrics		keywordFontMetrics;
	protected		static Color					keywordColor;
	
	protected		static Font						constantFont;
	protected		static FontMetrics		constantFontMetrics;
	protected		static Color					constantColor;
	
	protected		static Font						envFont;
	protected		static FontMetrics		envFontMetrics;
	protected		static Color					envColor;
	
	protected 	static Font						typeFont;
	protected		static FontMetrics		typeFontMetrics;
	protected		static Color					typeColor;
	
	protected		static Color					underlineColor;	
	
	protected 	static int						fontHeight;
	protected		static int						fontAscent;
	protected		static int						fontDescent;

	protected 	static Font						exponentFont;
	protected		static FontMetrics		exponentFontMetrics;
	protected		static Color					exponentColor;

	protected		Color									alternativeColor;

	/**
	 * Initializes the colors, fonts and fontmetrics.
	 * 
	 * @param theme			The that that should be used to retrieve the
	 * 									information for the stuff. 
	 * @param reference Any object subclassing {@link Component} used
	 * 									to call the {@link Component#getFontMetrics(java.awt.Font)}-Method.
	 */
	public static void setTheme (Theme theme, Component reference) { 
		
		AbstractRenderer.expColor							= theme.getItemColor(Theme.TYPE_EXPRESSION);
		AbstractRenderer.expFont							= theme.getItemFont(Theme.TYPE_EXPRESSION);
		AbstractRenderer.expFontMetrics				= reference.getFontMetrics(AbstractRenderer.expFont);
		
		AbstractRenderer.keywordColor					= theme.getItemColor(Theme.TYPE_KEYWORD);
		AbstractRenderer.keywordFont					= theme.getItemFont(Theme.TYPE_KEYWORD);
		AbstractRenderer.keywordFontMetrics		= reference.getFontMetrics(AbstractRenderer.keywordFont);
		
		AbstractRenderer.constantColor				= theme.getItemColor(Theme.TYPE_CONSTANT);
		AbstractRenderer.constantFont					= theme.getItemFont(Theme.TYPE_CONSTANT);
		AbstractRenderer.constantFontMetrics	= reference.getFontMetrics(AbstractRenderer.constantFont);
		
		AbstractRenderer.envColor							= theme.getItemColor(Theme.TYPE_ENVIRONMENT);
		AbstractRenderer.envFont							= theme.getItemFont(Theme.TYPE_ENVIRONMENT);
		AbstractRenderer.envFontMetrics				= reference.getFontMetrics(AbstractRenderer.envFont);
		
		AbstractRenderer.typeColor						= theme.getItemColor(Theme.TYPE_TYPE);
		AbstractRenderer.typeFont							= theme.getItemFont(Theme.TYPE_TYPE);
		AbstractRenderer.typeFontMetrics			= reference.getFontMetrics(AbstractRenderer.typeFont);
		
		
		AbstractRenderer.underlineColor				= theme.getItemColor(Theme.TYPE_UNDERLINE);
		
		
		
		AbstractRenderer.fontHeight = Math.max(AbstractRenderer.expFontMetrics.getHeight(),
				Math.max(AbstractRenderer.keywordFontMetrics.getHeight(),
				Math.max(AbstractRenderer.constantFontMetrics.getHeight(),
				Math.max(AbstractRenderer.envFontMetrics.getHeight(),
						     AbstractRenderer.typeFontMetrics.getHeight()))));
		
		AbstractRenderer.fontAscent = Math.max(AbstractRenderer.expFontMetrics.getAscent(),
				Math.max(AbstractRenderer.keywordFontMetrics.getAscent(),
				Math.max(AbstractRenderer.constantFontMetrics.getAscent(),
				Math.max(AbstractRenderer.envFontMetrics.getAscent(),
								 AbstractRenderer.typeFontMetrics.getAscent()))));

		AbstractRenderer.fontDescent = Math.max(AbstractRenderer.expFontMetrics.getDescent(),
				Math.max(AbstractRenderer.keywordFontMetrics.getDescent(),
				Math.max(AbstractRenderer.constantFontMetrics.getDescent(),
				Math.max(AbstractRenderer.envFontMetrics.getDescent(),
								 AbstractRenderer.typeFontMetrics.getDescent()))));

	}
	
	public static FontMetrics getTextFontMetrics () {
		return AbstractRenderer.expFontMetrics;
	}
	
	public static Font getTextFont () {
		return AbstractRenderer.expFont;
	}
	
	
	public static Color getTextColor () {
		return AbstractRenderer.expColor;
	}
	
	public static Font getExponentFont () {
		return AbstractRenderer.exponentFont;
	}
	
	public static Color getExponentColor () {
		return AbstractRenderer.exponentColor;
	}
	
	public static FontMetrics getExponentFontMetrics () {
		return AbstractRenderer.exponentFontMetrics;
	}
	
	public AbstractRenderer() {
		this.alternativeColor = null;
	}
	
	public void setAlternativeColor (Color color) {
		this.alternativeColor = color;
	}
	
	
}
