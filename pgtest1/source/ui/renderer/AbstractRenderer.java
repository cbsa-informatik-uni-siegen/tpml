package ui.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

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
	
	
	public static float BRACE_LEFT			= 1.0f;
	public static float BRACE_RIGHT			= -1.0f;
	
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

	public void render (int x, int y, int width, int height, float pos, Graphics g) {
		
		g.setColor(Color.BLACK);
		float lx = width;
		float ly = height / 2;
		
		float cx = x + width;
		float cy = y + height / 2;
		
		float rotationStep = (float)Math.PI / 32 * pos;
		float angle0 = (float)Math.PI / 2.0f;
		for (int i=0; i<31; i++) {
			float angle1 = angle0 + rotationStep;
			
			float x0 = cx + (float)Math.cos(angle0) * lx;
			float y0 = cy + (float)Math.sin(angle0) * ly;
			
			float x1 = cx + (float)Math.cos(angle1) * lx;
			float y1 = cy + (float)Math.sin(angle1) * ly;
			
			g.drawLine((int)x0, (int)y0, (int)x1, (int)y1);
			
			angle0 += rotationStep;
		}
		
	}
}
