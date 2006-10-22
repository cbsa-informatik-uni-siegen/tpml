package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import de.unisiegen.tpml.graphics.Theme;


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

	private     static Theme				  theme;
	
	static {
		// connect to the theme
		theme = Theme.currentTheme();
		setTheme(theme, new JLabel());
		theme.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				setTheme(theme, new JLabel());
			}
		});
	}
	
	/**
	 * Initializes the colors, fonts and fontmetrics.
	 * 
	 * @param theme			The that that should be used to retrieve the
	 * 									information for the stuff. 
	 * @param reference Any object subclassing {@link Component} used
	 * 									to call the {@link Component#getFontMetrics(java.awt.Font)}-Method.
	 */
	private static void setTheme (Theme theme, Component reference) { 
		
		AbstractRenderer.expColor							= theme.getExpressionColor();
		AbstractRenderer.expFont							= theme.getFont();
		AbstractRenderer.expFontMetrics				= reference.getFontMetrics(AbstractRenderer.expFont);
		
		AbstractRenderer.keywordColor					= theme.getKeywordColor();
		AbstractRenderer.keywordFont					= theme.getFont();
		AbstractRenderer.keywordFontMetrics		= reference.getFontMetrics(AbstractRenderer.keywordFont);
		
		AbstractRenderer.constantColor				= theme.getConstantColor();
		AbstractRenderer.constantFont					= theme.getFont();
		AbstractRenderer.constantFontMetrics	= reference.getFontMetrics(AbstractRenderer.constantFont);
		
		AbstractRenderer.envColor							= theme.getEnvironmentColor();
		AbstractRenderer.envFont							= theme.getFont();
		AbstractRenderer.envFontMetrics				= reference.getFontMetrics(AbstractRenderer.envFont);
		
		AbstractRenderer.typeColor						= theme.getTypeColor();
		AbstractRenderer.typeFont							= theme.getFont();
		AbstractRenderer.typeFontMetrics			= reference.getFontMetrics(AbstractRenderer.typeFont);
		
		
		AbstractRenderer.underlineColor				= theme.getUnderlineColor();
		
		
		
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
