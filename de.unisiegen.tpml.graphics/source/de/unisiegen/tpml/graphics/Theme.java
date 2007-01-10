package de.unisiegen.tpml.graphics ;


import java.awt.Color ;
import java.awt.Font ;
import java.util.prefs.Preferences ;
import javax.swing.JComboBox ;
import de.unisiegen.tpml.core.util.beans.AbstractBean ;


/**
 * The theme settings that control the color and the font of the renderers and
 * the editor. The theme settings can be changed from the preferences dialog.
 * The settings are implemented as bean properties and the renderers and editors
 * should stay informed about changes to the theme via
 * {@link java.beans.PropertyChangeListener}s. The <code>Theme</code> class
 * has only a single instance, which is allocated on-demand and returned by the
 * {@link #currentTheme()} class method.
 * 
 * @author Marcell Fischbach
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.core.util.beans.Bean
 * @see java.beans.PropertyChangeListener
 */
public final class Theme extends AbstractBean
{
  //
  // Attributes
  //
  /**
   * The {@link Color} used to render comments.
   * 
   * @see #getCommentColor()
   * @see #setCommentColor(Color)
   */
  private Color commentColor ;


  /**
   * The {@link Color} used to render constants.
   * 
   * @see #getConstantColor()
   * @see #setConstantColor(Color)
   */
  private Color constantColor ;


  /**
   * The {@link Color} used to render environments, i.e. the type environment
   * and the stores for the big and small step interpreters.
   * 
   * @see #getEnvironmentColor()
   * @see #setEnvironmentColor(Color)
   */
  private Color environmentColor ;


  /**
   * The {@link Color} used to render expressions.
   * 
   * @see #getExpressionColor()
   * @see #setExpressionColor(Color)
   */
  private Color expressionColor ;


  /**
   * The global font setting, which is used for the renderers and the editors.
   * 
   * @see #getFont()
   * @see #setFont(Font)
   */
  private Font font ;


  /**
   * The {@link Color} used to render keywords.
   * 
   * @see #getKeywordColor()
   * @see #setKeywordColor(Color)
   */
  private Color keywordColor ;


  /**
   * The {@link Color} used to render selected expressions.
   * 
   * @see #getSelectionColor()
   * @see #setSelectionColor(Color)
   */
  private Color selectionColor ;


  /**
   * The {@link Color} used to render bindings.
   * 
   * @see #getBindingColor()
   * @see #setBindingColor(Color)
   */
  private Color bindingColor ;


  /**
   * The {@link Color} used to render unbound Identifier.
   * 
   * @see #getUnboundColor()
   * @see #setUnboundColor(Color)
   */
  private Color unboundColor ;


  /**
   * The {@link Color} used to render first Identifier.
   * 
   * @see #getIdColor()
   * @see #setIdColor(Color)
   */
  private Color idColor ;


  /**
   * The {@link Preferences} used to store the settings.
   * 
   * @see Preferences
   */
  private Preferences preferences ;


  /**
   * The {@link Color} which is used to render rules.
   * 
   * @see #getRuleColor()
   * @see #setRuleColor(Color)
   */
  private Color ruleColor ;


  /**
   * The {@link Color} which is used to underline in the small stepper.
   * 
   * @see #getUnderlineColor()
   * @see #setUnderlineColor(Color)
   */
  private Color underlineColor ;


  /**
   * The {@link Color} which is used to render types.
   * 
   * @see #getTypeColor()
   * @see #setTypeColor(Color)
   */
  private Color typeColor ;


  //
  // Class attributes
  //
  /**
   * The single instance of the <code>Theme</code> class, as returned by the
   * {@link #currentTheme()} class method.
   * 
   * @see #currentTheme()
   */
  private static Theme currentTheme ;


  //
  // Constructor (private)
  //
  /**
   * Allocates a new <code>Theme</code>. There's exactly one instance of the
   * <code>Theme</code> class, which is returned by the
   * {@link #currentTheme()} class method.
   * 
   * @see #currentTheme()
   */
  private Theme ( )
  {
    // connect to the preferences
    this.preferences = Preferences.userNodeForPackage ( Theme.class ) ;
    // load the commentColor setting
    this.commentColor = Color.decode ( this.preferences.get ( "commentColor" , //$NON-NLS-1$
        "#1a991a" ) ) ; //$NON-NLS-1$
    // load the constantColor setting
    this.constantColor = Color.decode ( this.preferences.get ( "constantColor" , //$NON-NLS-1$
        "#00007f" ) ) ; //$NON-NLS-1$
    // load the environmentColor setting
    this.environmentColor = Color.decode ( this.preferences.get (
        "environmentColor" , "#7f7f7f" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    // load the expressionColor setting
    this.expressionColor = Color.decode ( this.preferences.get (
        "expressionColor" , "#000000" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    // load the selectionColor setting
    this.selectionColor = Color.decode ( this.preferences.get (
        "selectionColor" , "#FF0000" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    // load the bindingColor setting
    this.bindingColor = Color.decode ( this.preferences.get ( "bindingColor" , //$NON-NLS-1$
        "#FFAA33" ) ) ; //$NON-NLS-1$
    // load the unboundColor setting
    this.unboundColor = Color.decode ( this.preferences.get ( "unboundColor" , //$NON-NLS-1$
        "#3333FF" ) ) ; //$NON-NLS-1$
    // load the idColor setting
    this.idColor = Color
        .decode ( this.preferences.get ( "idColor" , "#FF0000" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    // load the font setting
    /*
     * FIXME: This doesn't work on Windows! (*surprise*) Font defaultFont = new
     * JLabel().getFont(); this.font = new Font(this.preferences.get("fontName",
     * defaultFont.getName()), this.preferences.getInt("fontStyle",
     * defaultFont.getStyle()), this.preferences.getInt("fontSize",
     * defaultFont.getSize()));
     */
    this.font = new JComboBox ( ).getFont ( ) ;
    // load the keywordColor setting
    this.keywordColor = Color.decode ( this.preferences.get ( "keywordColor" , //$NON-NLS-1$
        "#7f0000" ) ) ; //$NON-NLS-1$
    // load the ruleColor setting
    this.ruleColor = Color.decode ( this.preferences.get ( "ruleColor" , //$NON-NLS-1$
        "#000000" ) ) ; //$NON-NLS-1$
    // load the underlineColor setting
    this.underlineColor = Color.decode ( this.preferences.get (
        "underlineColor" , "#ff0000" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    // load the typeColor setting
    this.typeColor = Color.decode ( this.preferences.get ( "typeColor" , //$NON-NLS-1$
        "#009900" ) ) ; //$NON-NLS-1$
  }


  //
  // Class methods
  //
  /**
   * Returns the single instance of the <code>Theme</code> class, which is
   * allocated on-demand, that is, on the first invocation of this class method,
   * and stays alive for the live time of the application.
   * 
   * @return the current theme.
   */
  public static Theme currentTheme ( )
  {
    if ( currentTheme == null )
    {
      currentTheme = new Theme ( ) ;
    }
    return currentTheme ;
  }


  //
  // Accessors
  //
  /**
   * Returns the color that should be used to render comments.
   * 
   * @return the color for comments.
   * @see #setCommentColor(Color)
   * @see Color
   */
  public Color getCommentColor ( )
  {
    return this.commentColor ;
  }


  /**
   * Sets the color for comments to the specified <code>commentColor</code>.
   * 
   * @param pCommentColor the new color for comments.
   * @throws NullPointerException if <code>commentColor</code> is
   *           <code>null</code>.
   * @see #getCommentColor()
   * @see Color
   */
  public void setCommentColor ( Color pCommentColor )
  {
    if ( pCommentColor == null )
    {
      throw new NullPointerException ( "commentColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.commentColor.equals ( pCommentColor ) )
    {
      // update the commentColor
      Color oldCommentColor = this.commentColor ;
      this.commentColor = pCommentColor ;
      firePropertyChange ( "commentColor" , oldCommentColor , pCommentColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences.put ( "commentColor" , encodeColor ( pCommentColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Returns the color that should be used to render constants.
   * 
   * @return the color for constants.
   * @see #setConstantColor(Color)
   * @see Color
   */
  public Color getConstantColor ( )
  {
    return this.constantColor ;
  }


  /**
   * Sets the color for constants to the specified <code>constantColor</code>.
   * 
   * @param pConstantColor the new color for constants.
   * @throws NullPointerException if <code>constantColor</code> is
   *           <code>null</code>.
   * @see #getConstantColor()
   * @see Color
   */
  public void setConstantColor ( Color pConstantColor )
  {
    if ( pConstantColor == null )
    {
      throw new NullPointerException ( "constantColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.constantColor.equals ( pConstantColor ) )
    {
      // update the constantColor
      Color oldConstantColor = this.constantColor ;
      this.constantColor = pConstantColor ;
      firePropertyChange ( "constantColor" , oldConstantColor , pConstantColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences.put ( "constantColor" , encodeColor ( pConstantColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Returns the color that should be used to render environments, like the type
   * environment for the type checker and the stores for the big and small step
   * interpreters.
   * 
   * @return the color for environments.
   * @see #setEnvironmentColor(Color)
   * @see Color
   */
  public Color getEnvironmentColor ( )
  {
    return this.environmentColor ;
  }


  /**
   * Sets the color for environments to the specified
   * <code>environmentColor</code>.
   * 
   * @param pEnvironmentColor the new color for environments.
   * @throws NullPointerException if <code>environmentColor</code> is
   *           <code>null</code>.
   * @see #getEnvironmentColor()
   * @see Color
   */
  public void setEnvironmentColor ( Color pEnvironmentColor )
  {
    if ( pEnvironmentColor == null )
    {
      throw new NullPointerException ( "environmentColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.environmentColor.equals ( pEnvironmentColor ) )
    {
      // update the environmentColor
      Color oldEnvironmentColor = this.environmentColor ;
      this.environmentColor = pEnvironmentColor ;
      firePropertyChange ( "environmentColor" , oldEnvironmentColor , //$NON-NLS-1$
          pEnvironmentColor ) ;
      // save the new setting
      this.preferences.put ( "environmentColor" , //$NON-NLS-1$
          encodeColor ( pEnvironmentColor ) ) ;
    }
  }


  /**
   * Returns the color that should be used to render expressions.
   * 
   * @return the color for expressions.
   * @see #setExpressionColor(Color)
   * @see Color
   */
  public Color getExpressionColor ( )
  {
    return this.expressionColor ;
  }


  /**
   * Sets the color for expressions to the specified
   * <code>expressionColor</code>.
   * 
   * @param pExpressionColor the new color for expressions.
   * @throws NullPointerException if <code>expressionColor</code> is
   *           <code>null</code>.
   * @see #getExpressionColor()
   * @see Color
   */
  public void setExpressionColor ( Color pExpressionColor )
  {
    if ( pExpressionColor == null )
    {
      throw new NullPointerException ( "expressionColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.expressionColor.equals ( pExpressionColor ) )
    {
      // update the expressionColor
      Color oldExpressionColor = this.expressionColor ;
      this.expressionColor = pExpressionColor ;
      firePropertyChange ( "expressionColor" , oldExpressionColor , //$NON-NLS-1$
          pExpressionColor ) ;
      // save the new setting
      this.preferences.put (
          "expressionColor" , encodeColor ( pExpressionColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Returns the global font that should be used for the renderers and editors.
   * 
   * @return the font for the renderers and editors.
   * @see #setFont(Font)
   * @see Font
   */
  public Font getFont ( )
  {
    return this.font ;
  }


  /**
   * Changes the global font that is used for the renderers and editors to the
   * specified <code>font</code>.
   * 
   * @param pFont the new global font setting.
   * @throws NullPointerException if <code>font</code> is <code>null</code>.
   * @see #getFont()
   * @see Font
   */
  public void setFont ( @ SuppressWarnings ( "unused" )
  Font pFont )
  {
    /*
     * FIXME: Windows, meh... if (font == null) { throw new
     * NullPointerException("font is null"); } if (!this.font.equals(font)) { //
     * update the font Font oldFont = this.font; this.font = font;
     * firePropertyChange("font", oldFont, font); // save the new setting
     * this.preferences.put("fontName", font.getName());
     * this.preferences.putInt("fontSize", font.getSize());
     * this.preferences.putInt("fontStyle", font.getStyle()); }
     */
    throw new UnsupportedOperationException (
        "Setting custom fonts mess up TPML on Windows!" ) ; //$NON-NLS-1$
  }


  /**
   * Returns the {@link Color} used to render keywords in the interpreters, type
   * checker and the editor.
   * 
   * @return the color for keywords.
   * @see #setKeywordColor(Color)
   * @see Color
   */
  public Color getKeywordColor ( )
  {
    return this.keywordColor ;
  }


  /**
   * Returns the {@link Color} used to render selected expressions.
   * 
   * @return the color for selected expressions.
   * @see #setSelectionColor(Color)
   * @see Color
   */
  public Color getSelectionColor ( )
  {
    return this.selectionColor ;
  }


  /**
   * Returns the {@link Color} used to render bindings.
   * 
   * @return the color for binding.
   * @see #setBindingColor(Color)
   * @see Color
   */
  public Color getBindingColor ( )
  {
    return this.bindingColor ;
  }


  /**
   * Returns the {@link Color} used to render unbound Identifier.
   * 
   * @return the color for the unbound Identifier.
   * @see #setUnboundColor(Color)
   * @see Color
   */
  public Color getUnboundColor ( )
  {
    return this.unboundColor ;
  }


  /**
   * Returns the {@link Color} used to render first Identifier.
   * 
   * @return the color for the first Identifier.
   * @see #setIdColor(Color)
   * @see Color
   */
  public Color getIdColor ( )
  {
    return this.idColor ;
  }


  /**
   * Sets the color that should be used to render keywords to the specified
   * <code>keywordColor</code>.
   * 
   * @param pKeywordColor the color for keywords.
   * @see #getKeywordColor()
   * @see Color
   * @throws NullPointerException if <code>keywordColor</code> is
   *           <code>null</code>.
   */
  public void setKeywordColor ( Color pKeywordColor )
  {
    if ( pKeywordColor == null )
    {
      throw new NullPointerException ( "keywordColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.keywordColor.equals ( pKeywordColor ) )
    {
      // update the keywordColor
      Color oldKeywordColor = this.keywordColor ;
      this.keywordColor = pKeywordColor ;
      firePropertyChange ( "keywordColor" , oldKeywordColor , pKeywordColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences.put ( "keywordColor" , encodeColor ( pKeywordColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Sets the color that should be used to render selected expressions to the
   * specified <code>selectionColor</code>.
   * 
   * @param pSelectionColor the color for selected expressions.
   * @see #getSelectionColor()
   * @see Color
   * @throws NullPointerException if <code>selectionColor</code> is
   *           <code>null</code>.
   */
  public void setSelectionColor ( Color pSelectionColor )
  {
    if ( pSelectionColor == null )
    {
      throw new NullPointerException ( "selectionColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.selectionColor.equals ( pSelectionColor ) )
    {
      // update the keywordColor
      Color oldKeywordColor = this.selectionColor ;
      this.selectionColor = pSelectionColor ;
      firePropertyChange ( "selectionColor" , oldKeywordColor , pSelectionColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences
          .put ( "selectionColor" , encodeColor ( pSelectionColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Sets the color that should be used to render bindings to the specified
   * <code>bindingsColor</code>.
   * 
   * @param pBindingColor the color for bindings.
   * @see #getBindingColor()
   * @see Color
   * @throws NullPointerException if <code>bindingColor</code> is
   *           <code>null</code>.
   */
  public void setBindingColor ( Color pBindingColor )
  {
    if ( pBindingColor == null )
    {
      throw new NullPointerException ( "selectionColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.bindingColor.equals ( pBindingColor ) )
    {
      // update the keywordColor
      Color oldKeywordColor = this.bindingColor ;
      this.bindingColor = pBindingColor ;
      firePropertyChange ( "bindingColor" , oldKeywordColor , pBindingColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences.put ( "bindingColor" , encodeColor ( pBindingColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Sets the color that should be used to render unbound Identifier to the
   * specified <code>unboundColor</code>.
   * 
   * @param pUnboundColor the color for unbound Identifier.
   * @see #getUnboundColor()
   * @see Color
   * @throws NullPointerException if <code>unboundColor</code> is
   *           <code>null</code>.
   */
  public void setUnboundColor ( Color pUnboundColor )
  {
    if ( pUnboundColor == null )
    {
      throw new NullPointerException ( "unboundColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.unboundColor.equals ( pUnboundColor ) )
    {
      // update the keywordColor
      Color oldKeywordColor = this.unboundColor ;
      this.unboundColor = pUnboundColor ;
      firePropertyChange ( "unboundColor" , oldKeywordColor , pUnboundColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences.put ( "unboundColor" , encodeColor ( pUnboundColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Sets the color that should be used to render first Identifier to the
   * specified <code>idColor</code>.
   * 
   * @param pIDColor the color for first Identifier.
   * @see #getIdColor()
   * @see Color
   * @throws NullPointerException if <code>idColor</code> is <code>null</code>.
   */
  public void setIdColor ( Color pIDColor )
  {
    if ( pIDColor == null )
    {
      throw new NullPointerException ( "idColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.idColor.equals ( pIDColor ) )
    {
      // update the keywordColorid
      Color oldKeywordColor = this.idColor ;
      this.idColor = pIDColor ;
      firePropertyChange ( "idColor" , oldKeywordColor , pIDColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences.put ( "idColor" , encodeColor ( pIDColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Returns the {@link Color} that should be used to render rules in the small
   * and big step interpreters and the type checker.
   * 
   * @return the color for rules.
   * @see #setRuleColor(Color)
   * @see Color
   */
  public Color getRuleColor ( )
  {
    return this.ruleColor ;
  }


  /**
   * Sets the color that should be used to render rules to the specified
   * <code>ruleColor</code>.
   * 
   * @param pRuleColor the color that should be used to render rules.
   * @throws NullPointerException if <code>ruleColor</code> is
   *           <code>null</code>.
   * @see #getRuleColor()
   * @see Color
   */
  public void setRuleColor ( Color pRuleColor )
  {
    if ( pRuleColor == null )
    {
      throw new NullPointerException ( "ruleColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.ruleColor.equals ( pRuleColor ) )
    {
      // update the ruleColor
      Color oldRuleColor = this.ruleColor ;
      this.ruleColor = pRuleColor ;
      firePropertyChange ( "ruleColor" , oldRuleColor , pRuleColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences.put ( "ruleColor" , encodeColor ( pRuleColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Returns the color used to underline in the small stepper.
   * 
   * @return the underline color for the small stepper.
   * @see #setUnderlineColor(Color)
   * @see Color
   */
  public Color getUnderlineColor ( )
  {
    return this.underlineColor ;
  }


  /**
   * Sets the color for underlining in the small stepper to the specified
   * <code>underlineColor</code>.
   * 
   * @param pUnderlineColor the new color to underline.
   * @throws NullPointerException if <code>underlineColor</code> is
   *           <code>null</code>.
   * @see #getUnderlineColor()
   * @see Color
   */
  public void setUnderlineColor ( Color pUnderlineColor )
  {
    if ( pUnderlineColor == null )
    {
      throw new NullPointerException ( "underlineColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.underlineColor.equals ( pUnderlineColor ) )
    {
      // update the underlineColor
      Color oldUnderlineColor = this.underlineColor ;
      this.underlineColor = pUnderlineColor ;
      firePropertyChange ( "underlineColor" , oldUnderlineColor , //$NON-NLS-1$
          pUnderlineColor ) ;
      // save the new setting
      this.preferences
          .put ( "underlineColor" , encodeColor ( pUnderlineColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Returns the color used to render types.
   * 
   * @return the color for types.
   * @see #setTypeColor(Color)
   * @see Color
   */
  public Color getTypeColor ( )
  {
    return this.typeColor ;
  }


  /**
   * Sets the color used to render types to the specified <code>typeColor</code>.
   * 
   * @param pTypeColor the new color for types.
   * @throws NullPointerException if <code>typeColor</code> is
   *           <code>null</code>.
   * @see #getTypeColor()
   * @see Color
   */
  public void setTypeColor ( Color pTypeColor )
  {
    if ( pTypeColor == null )
    {
      throw new NullPointerException ( "typeColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.typeColor.equals ( pTypeColor ) )
    {
      // update the typeColor
      Color oldTypeColor = this.typeColor ;
      this.typeColor = pTypeColor ;
      firePropertyChange ( "typeColor" , oldTypeColor , pTypeColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences.put ( "typeColor" , encodeColor ( pTypeColor ) ) ; //$NON-NLS-1$
    }
  }


  //
  // Helpers
  //
  /**
   * Encodes the <code>color</code> in its hexadecimal string representation,
   * which is <tt>#[red][green][blue]</tt>, where the channels are encoded as
   * 2 hex characters.
   * 
   * @param color the {@link Color} to encode
   * @return the string representation of <code>color</code>.
   * @throws NullPointerException if <code>color</code> is <code>null</code>.
   */
  private static final String encodeColor ( Color color )
  {
    // encode the red channel
    String red = Integer.toHexString ( color.getRed ( ) ) ;
    if ( red.length ( ) < 2 )
    {
      red = "0" + red ; //$NON-NLS-1$
    }
    // encode the green channel
    String green = Integer.toHexString ( color.getGreen ( ) ) ;
    if ( green.length ( ) < 2 )
    {
      green = "0" + green ; //$NON-NLS-1$
    }
    // encode the blue channel
    String blue = Integer.toHexString ( color.getBlue ( ) ) ;
    if ( blue.length ( ) < 2 )
    {
      blue = "0" + blue ; //$NON-NLS-1$
    }
    // combine the channels
    return "#" + red + green + blue ; //$NON-NLS-1$
  }
}
