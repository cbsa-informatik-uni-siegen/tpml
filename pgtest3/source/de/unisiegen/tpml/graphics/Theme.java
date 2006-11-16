package de.unisiegen.tpml.graphics;

import java.awt.Color;
import java.awt.Font;
import java.util.prefs.Preferences;

import javax.swing.JComboBox;

import de.unisiegen.tpml.core.util.beans.AbstractBean;

/**
 * The theme settings that control the color and the font of the renderers and the editor. The
 * theme settings can be changed from the preferences dialog. The settings are implemented as
 * bean properties and the renderers and editors should stay informed about changes to the
 * theme via {@link java.beans.PropertyChangeListener}s.
 * 
 * The <code>Theme</code> class has only a single instance, which is allocated on-demand and
 * returned by the {@link #currentTheme()} class method.
 *
 * @author Marcell Fischbach
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev: 526 $
 *
 * @see de.unisiegen.tpml.core.util.beans.Bean
 * @see java.beans.PropertyChangeListener
 */
public final class Theme extends AbstractBean {
	//
	// Attributes
	//

	/**
	 * The {@link Color} used to render comments.
	 * 
	 * @see #getCommentColor()
	 * @see #setCommentColor(Color)
	 */
	private Color commentColor;
	
	/**
	 * The {@link Color} used to render constants.
	 * 
	 * @see #getConstantColor()
	 * @see #setConstantColor(Color)
	 */
	private Color constantColor;
	
	/**
	 * The {@link Color} used to render environments, i.e. the type environment and the stores for the
	 * big and small step interpreters.
	 * 
	 * @see #getEnvironmentColor()
	 * @see #setEnvironmentColor(Color)
	 */
	private Color environmentColor;
	
	/**
	 * The {@link Color} used to render expressions.
	 * 
	 * @see #getExpressionColor()
	 * @see #setExpressionColor(Color)
	 */
	private Color expressionColor;
	
	/**
	 * The global font setting, which is used for the renderers and the editors.
	 * 
	 * @see #getFont()
	 * @see #setFont(Font)
	 */
	private Font font;
	
	/**
	 * The {@link Color} used to render keywords.
	 * 
	 * @see #getKeywordColor()
	 * @see #setKeywordColor(Color)
	 */
	private Color keywordColor;
  
  /**
   * The {@link Color} used to render selected expressions.
   * 
   * @see #getSelectedColor()
   * @see #setSelectedColor(Color)
   */ 
  private Color selectedColor;
  
  /**
   * The {@link Color} used to render bindings.
   * 
   * @see #getBindingColor()
   * @see #setBindingColor(Color)
   */  
  private Color bindingColor;
	
	/**
	 * The {@link Preferences} used to store the settings.
	 * 
	 * @see Preferences
	 */
	private Preferences preferences;
	
	/**
	 * The {@link Color} which is used to render rules.
	 * 
	 * @see #getRuleColor()
	 * @see #setRuleColor(Color)
	 */
	private Color ruleColor;
	
	/**
	 * The {@link Color} which is used to underline in the small stepper.
	 * 
	 * @see #getUnderlineColor()
	 * @see #setUnderlineColor(Color)
	 */
	private Color underlineColor;
	
	/**
	 * The {@link Color} which is used to render types.
	 * 
	 * @see #getTypeColor()
	 * @see #setTypeColor(Color)
	 */
	private Color typeColor;
	
	
	
	//
	// Class attributes
	//
	
	/**
	 * The single instance of the <code>Theme</code> class, as returned by the
	 * {@link #currentTheme()} class method.
	 * 
	 * @see #currentTheme()
	 */
	private static Theme currentTheme;
	
	
	
	//
	// Constructor (private)
	//
	
	/**
	 * Allocates a new <code>Theme</code>. There's exactly one instance of the
	 * <code>Theme</code> class, which is returned by the {@link #currentTheme()}
	 * class method.
	 * 
	 * @see #currentTheme()
	 */
	private Theme() {
		// connect to the preferences
		this.preferences = Preferences.userNodeForPackage(Theme.class);

		// load the commentColor setting
		this.commentColor = Color.decode(this.preferences.get("commentColor", "#1a991a"));
		
		// load the constantColor setting
		this.constantColor = Color.decode(this.preferences.get("constantColor", "#00007f"));
		
		// load the environmentColor setting
		this.environmentColor = Color.decode(this.preferences.get("environmentColor", "#7f7f7f"));
		
		// load the expressionColor setting
		this.expressionColor = Color.decode(this.preferences.get("expressionColor", "#000000"));
		
    // load the selectedColor setting
    this.selectedColor = Color.decode(this.preferences.get("selectedColor", "#FF0000"));
    
    // load the bindingColor setting
    this.bindingColor = Color.decode(this.preferences.get("bindingColor", "#FFAA33"));
    
		// load the font setting
		/* FIXME: This doesn't work on Windows! (*surprise*)
		Font defaultFont = new JLabel().getFont();
		this.font = new Font(this.preferences.get("fontName", defaultFont.getName()),
				this.preferences.getInt("fontStyle", defaultFont.getStyle()),
				this.preferences.getInt("fontSize", defaultFont.getSize()));
	  */
		this.font = new JComboBox().getFont();
		
		// load the keywordColor setting
		this.keywordColor = Color.decode(this.preferences.get("keywordColor", "#7f0000"));
		
		// load the ruleColor setting
		this.ruleColor = Color.decode(this.preferences.get("ruleColor", "#000000"));
		
		// load the underlineColor setting
		this.underlineColor = Color.decode(this.preferences.get("underlineColor", "#ff0000"));
		
		// load the typeColor setting
		this.typeColor = Color.decode(this.preferences.get("typeColor", "#009900"));
	}
	
	
	
	//
	// Class methods
	//
	
	/**
	 * Returns the single instance of the <code>Theme</code> class, which is allocated
	 * on-demand, that is, on the first invocation of this class method, and stays
	 * alive for the live time of the application.
	 * 
	 * @return the current theme.
	 */
	public static Theme currentTheme() {
		if (currentTheme == null) {
			currentTheme = new Theme();
		}
		return currentTheme;
	}
	
	
	
	//
	// Accessors
	//
	
	/**
	 * Returns the color that should be used to render comments.
	 * 
	 * @return the color for comments.
	 * 
	 * @see #setCommentColor(Color)
	 * @see Color
	 */
	public Color getCommentColor() {
		return this.commentColor;
	}
	
	/**
	 * Sets the color for comments to the specified <code>commentColor</code>.
	 * 
	 * @param commentColor the new color for comments.
	 * 
	 * @throws NullPointerException if <code>commentColor</code> is <code>null</code>.
	 * 
	 * @see #getCommentColor()
	 * @see Color
	 */
	public void setCommentColor(Color commentColor) {
		if (commentColor == null) {
			throw new NullPointerException("commentColor is null");
		}
		if (!this.commentColor.equals(commentColor)) {
			// update the commentColor
			Color oldCommentColor = this.commentColor;
			this.commentColor = commentColor;
			firePropertyChange("commentColor", oldCommentColor, commentColor);
			
			// save the new setting
			this.preferences.put("commentColor", encodeColor(commentColor));
		}
	}
	
	/**
	 * Returns the color that should be used to render constants.
	 * 
	 * @return the color for constants.
	 * 
	 * @see #setConstantColor(Color)
	 * @see Color
	 */
	public Color getConstantColor() {
		return this.constantColor;
	}
	
	/**
	 * Sets the color for constants to the specified <code>constantColor</code>.
	 * 
	 * @param constantColor the new color for constants.
	 * 
	 * @throws NullPointerException if <code>constantColor</code> is <code>null</code>.
	 * 
	 * @see #getConstantColor()
	 * @see Color
	 */
	public void setConstantColor(Color constantColor) {
		if (constantColor == null) {
			throw new NullPointerException("constantColor is null");
		}
		if (!this.constantColor.equals(constantColor)) {
			// update the constantColor
			Color oldConstantColor = this.constantColor;
			this.constantColor = constantColor;
			firePropertyChange("constantColor", oldConstantColor, constantColor);
			
			// save the new setting
			this.preferences.put("constantColor", encodeColor(constantColor));
		}
	}
	
	/**
	 * Returns the color that should be used to render environments, like the type environment for the
	 * type checker and the stores for the big and small step interpreters.
	 * 
	 * @return the color for environments.
	 * 
	 * @see #setEnvironmentColor(Color)
	 * @see Color
	 */
	public Color getEnvironmentColor() {
		return this.environmentColor;
	}
	
	/**
	 * Sets the color for environments to the specified <code>environmentColor</code>.
	 * 
	 * @param environmentColor the new color for environments.
	 * 
	 * @throws NullPointerException if <code>environmentColor</code> is <code>null</code>.
	 * 
	 * @see #getEnvironmentColor()
	 * @see Color
	 */
	public void setEnvironmentColor(Color environmentColor) {
		if (environmentColor == null) {
			throw new NullPointerException("environmentColor is null");
		}
		if (!this.environmentColor.equals(environmentColor)) {
			// update the environmentColor
			Color oldEnvironmentColor = this.environmentColor;
			this.environmentColor = environmentColor;
			firePropertyChange("environmentColor", oldEnvironmentColor, environmentColor);
			
			// save the new setting
			this.preferences.put("environmentColor", encodeColor(environmentColor));
		}
	}
	
	/**
	 * Returns the color that should be used to render expressions.
	 * 
	 * @return the color for expressions.
	 * 
	 * @see #setExpressionColor(Color)
	 * @see Color
	 */
	public Color getExpressionColor() {
		return this.expressionColor;
	}
	
	/**
	 * Sets the color for expressions to the specified <code>expressionColor</code>.
	 * 
	 * @param expressionColor the new color for expressions.
	 * 
	 * @throws NullPointerException if <code>expressionColor</code> is <code>null</code>.
	 * 
	 * @see #getExpressionColor()
	 * @see Color
	 */
	public void setExpressionColor(Color expressionColor) {
		if (expressionColor == null) {
			throw new NullPointerException("expressionColor is null");
		}
		if (!this.expressionColor.equals(expressionColor)) {
			// update the expressionColor
			Color oldExpressionColor = this.expressionColor;
			this.expressionColor = expressionColor;
			firePropertyChange("expressionColor", oldExpressionColor, expressionColor);
			
			// save the new setting
			this.preferences.put("expressionColor", encodeColor(expressionColor));
		}
	}
	
	/**
	 * Returns the global font that should be used for the renderers and editors.
	 * 
	 * @return the font for the renderers and editors.
	 * 
	 * @see #setFont(Font)
	 * @see Font
	 */
	public Font getFont() {
		return this.font;
	}
	
	/**
	 * Changes the global font that is used for the renderers and editors to the specified
	 * <code>font</code>.
	 * 
	 * @param font the new global font setting.
	 * 
	 * @throws NullPointerException if <code>font</code> is <code>null</code>.
	 * 
	 * @see #getFont()
	 * @see Font
	 */
	public void setFont(Font font) {
		/* FIXME: Windows, meh...
		if (font == null) {
			throw new NullPointerException("font is null");
		}
		if (!this.font.equals(font)) {
			// update the font
			Font oldFont = this.font;
			this.font = font;
			firePropertyChange("font", oldFont, font);
			
			// save the new setting
			this.preferences.put("fontName", font.getName());
			this.preferences.putInt("fontSize", font.getSize());
			this.preferences.putInt("fontStyle", font.getStyle());
		}
		*/
		throw new UnsupportedOperationException("Setting custom fonts mess up TPML on Windows!");
	}
	
	/**
	 * Returns the {@link Color} used to render keywords in the interpreters, type checker and the editor.
	 * 
	 * @return the color for keywords.
	 * 
	 * @see #setKeywordColor(Color)
	 * @see Color
	 */
	public Color getKeywordColor() {
		return this.keywordColor;
	}
	
  /**
   * Returns the {@link Color} used to render selected expressions.
   * 
   * @return the color for selected expressions.
   * 
   * @see #setSelectedColor(Color)
   * @see Color
   */
  public Color getSelectedColor() {
    return this.selectedColor;
  }
  
  /**
   * Returns the {@link Color} used to render bindings.
   * 
   * @return the color for binding.
   * 
   * @see #setBindingColor(Color)
   * @see Color
   */
  public Color getBindingColor() {
    return this.bindingColor;
  }
  
  
	/**
	 * Sets the color that should be used to render keywords to the specified <code>keywordColor</code>.
	 * 
	 * @param keywordColor the color for keywords.
	 * 
	 * @see #getKeywordColor()
	 * @see Color
	 * 
	 * @throws NullPointerException if <code>keywordColor</code> is <code>null</code>.
	 */
	public void setKeywordColor(Color keywordColor) {
		if (keywordColor == null) {
			throw new NullPointerException("keywordColor is null");
		}
		if (!this.keywordColor.equals(keywordColor)) {
			// update the keywordColor
			Color oldKeywordColor = this.keywordColor;
			this.keywordColor = keywordColor;
			firePropertyChange("keywordColor", oldKeywordColor, keywordColor);
			
			// save the new setting
			this.preferences.put("keywordColor", encodeColor(keywordColor));
		}
	}
	
  /**
   * Sets the color that should be used to render selected expressions to the specified <code>selectedColor</code>.
   * 
   * @param selectedColor the color for selected expressions.
   * 
   * @see #getSelectedColor()
   * @see Color
   * 
   * @throws NullPointerException if <code>selectedColor</code> is <code>null</code>.
   */
  public void setSelectedColor(Color selectedColor) {
    if (selectedColor == null) {
      throw new NullPointerException("selectedColor is null");
    }
    if (!this.selectedColor.equals(selectedColor)) {
      // update the keywordColor
      Color oldKeywordColor = this.selectedColor;
      this.selectedColor = selectedColor;
      firePropertyChange("selectedColor", oldKeywordColor, selectedColor);
      
      // save the new setting
      this.preferences.put("selectedColor", encodeColor(selectedColor));
    }
  }
  
  /**
   * Sets the color that should be used to render bindings to the specified <code>bindingsColor</code>.
   * 
   * @param bindingColor the color for bindings.
   * 
   * @see #getBindingColor()
   * @see Color
   * 
   * @throws NullPointerException if <code>selectedColor</code> is <code>null</code>.
   */
  public void setBindingColor(Color bindingColor) {
    if (bindingColor == null) {
      throw new NullPointerException("selectedColor is null");
    }
    if (!this.bindingColor.equals(bindingColor)) {
      // update the keywordColor
      Color oldKeywordColor = this.bindingColor;
      this.bindingColor = bindingColor;
      firePropertyChange("bindingColor", oldKeywordColor, bindingColor);
      
      // save the new setting
      this.preferences.put("bindingColor", encodeColor(bindingColor));
    }
  }
  
	/**
	 * Returns the {@link Color} that should be used to render rules in the small and big step interpreters
	 * and the type checker.
	 * 
	 * @return the color for rules.
	 * 
	 * @see #setRuleColor(Color)
	 * @see Color
	 */
	public Color getRuleColor() {
		return this.ruleColor;
	}
	
	/**
	 * Sets the color that should be used to render rules to the specified <code>ruleColor</code>.
	 * 
	 * @param ruleColor the color that should be used to render rules.
	 * 
	 * @throws NullPointerException if <code>ruleColor</code> is <code>null</code>.
	 * 
	 * @see #getRuleColor()
	 * @see Color
	 */
	public void setRuleColor(Color ruleColor) {
		if (ruleColor == null) {
			throw new NullPointerException("ruleColor is null");
		}
		if (!this.ruleColor.equals(ruleColor)) {
			// update the ruleColor
			Color oldRuleColor = this.ruleColor;
			this.ruleColor = ruleColor;
			firePropertyChange("ruleColor", oldRuleColor, ruleColor);
			
			// save the new setting
			this.preferences.put("ruleColor", encodeColor(ruleColor));
		}
	}
	
	/**
	 * Returns the color used to underline in the small stepper.
	 * 
	 * @return the underline color for the small stepper.
	 * 
	 * @see #setUnderlineColor(Color)
	 * @see Color
	 */
	public Color getUnderlineColor() {
		return this.underlineColor;
	}
	
	/**
	 * Sets the color for underlining in the small stepper to the specified <code>underlineColor</code>.
	 * 
	 * @param underlineColor the new color to underline.
	 * 
	 * @throws NullPointerException if <code>underlineColor</code> is <code>null</code>.
	 * 
	 * @see #getUnderlineColor()
	 * @see Color
	 */
	public void setUnderlineColor(Color underlineColor) {
		if (underlineColor == null) {
			throw new NullPointerException("underlineColor is null");
		}
		if (!this.underlineColor.equals(underlineColor)) {
			// update the underlineColor
			Color oldUnderlineColor = this.underlineColor;
			this.underlineColor = underlineColor;
			firePropertyChange("underlineColor", oldUnderlineColor, underlineColor);
			
			// save the new setting
			this.preferences.put("underlineColor", encodeColor(underlineColor));
		}
	}
	
	/**
	 * Returns the color used to render types.
	 * 
	 * @return the color for types.
	 * 
	 * @see #setTypeColor(Color)
	 * @see Color
	 */
	public Color getTypeColor() {
		return this.typeColor;
	}
	
	/**
	 * Sets the color used to render types to the specified <code>typeColor</code>.
	 * 
	 * @param typeColor the new color for types.
	 * 
	 * @throws NullPointerException if <code>typeColor</code> is <code>null</code>.
	 * 
	 * @see #getTypeColor()
	 * @see Color
	 */
	public void setTypeColor(Color typeColor) {
		if (typeColor == null) {
			throw new NullPointerException("typeColor is null");
		}
		if (!this.typeColor.equals(typeColor)) {
			// update the typeColor
			Color oldTypeColor = this.typeColor;
			this.typeColor = typeColor;
			firePropertyChange("typeColor", oldTypeColor, typeColor);
			
			// save the new setting
			this.preferences.put("typeColor", encodeColor(typeColor));
		}
	}
	
	
	
	//
	// Helpers
	//
	
	/**
	 * Encodes the <code>color</code> in its hexadecimal string representation, which
	 * is <tt>#[red][green][blue]</tt>, where the channels are encoded as 2 hex characters.
	 * 
	 * @param color the {@link Color} to encode
	 * 
	 * @return the string representation of <code>color</code>.
	 * 
	 * @throws NullPointerException if <code>color</code> is <code>null</code>.
	 */
	private static final String encodeColor(Color color) {
		// encode the red channel
		String red = Integer.toHexString(color.getRed());
		if (red.length() < 2) {
			red = "0" + red;
		}
		
		// encode the green channel
		String green = Integer.toHexString(color.getGreen());
		if (green.length() < 2) {
			green = "0" + green;
		}
		
		// encode the blue channel
		String blue = Integer.toHexString(color.getBlue());
		if (blue.length() < 2) {
			blue = "0" + blue;
		}
		
		// combine the channels
		return "#" + red + green + blue;
	}
}
