package ui.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

public class AbstractRenderer {
	
	protected 		static Font				textFont;
	protected		static FontMetrics		textFontMetrics;
	
	protected		static Font				keywordFont;
	protected		static FontMetrics		keywordFontMetrics;
	
	protected		static Font				constantFont;
	protected		static FontMetrics		constantFontMetrics;
	
	protected		static Font				envFont;
	protected		static FontMetrics		envFontMetrics;
	
	
	protected		static Color			textColor;
	protected		static Color			keywordColor;
	protected		static Color			constantColor;
	protected		static Color			envColor;
	protected		static Color			underlineColor;	
	
	protected 		static int				fontHeight;
	protected		static int				fontAscent;
	protected		static int				fontDescent;
	
	
	public static void setTextStyle (Font font, FontMetrics fontMetrics, Color fontColor) {
		textFont 			= font;
		textFontMetrics	= fontMetrics;
		textColor			= fontColor;
	}
	
	public static void setKeywordStyle (Font font, FontMetrics fontMetrics, Color fontColor) {
		keywordFont 			= font;
		keywordFontMetrics		= fontMetrics;
		keywordColor			= fontColor;
	}
	
	public static void setConstantStyle (Font font, FontMetrics fontMetrics, Color fontColor) {
		constantFont 			= font;
		constantFontMetrics		= fontMetrics;
		constantColor			= fontColor;
	}
	
	public static void setEnvironmentStyle (Font font, FontMetrics fontMetrics, Color fontColor) {
		envFont					= font;
		envFontMetrics			= fontMetrics;
		envColor				= fontColor;
	}
	
	public static void setUnderlineColor (Color color) {
		underlineColor	= color;
	}
	
	public static void checkFonts() {
		fontHeight 	= textFontMetrics.getHeight();
		fontAscent		= textFontMetrics.getAscent();
		fontDescent	= textFontMetrics.getDescent();
		if (fontHeight < keywordFontMetrics.getHeight()) { 
			fontHeight 	= keywordFontMetrics.getHeight();
			fontAscent		= keywordFontMetrics.getAscent();
			fontDescent	= keywordFontMetrics.getDescent();
		}
		if (fontHeight < constantFontMetrics.getHeight()) {
			fontHeight 	= constantFontMetrics.getHeight();
			fontAscent		= constantFontMetrics.getAscent();
			fontDescent	= constantFontMetrics.getDescent();
		}
	}

}
