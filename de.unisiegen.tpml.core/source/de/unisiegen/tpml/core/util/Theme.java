package de.unisiegen.tpml.core.util ;


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
 * @author michael
 * @version $Rev:1175 $
 * @see de.unisiegen.tpml.core.util.beans.Bean
 * @see java.beans.PropertyChangeListener
 */
/**
 * @author Feivel
 */
/**
 * @author Feivel
 */
/**
 * @author Feivel
 */
/**
 * @author Feivel
 */
/**
 * @author Feivel
 */
/**
 * @author Feivel
 */
/**
 * @author Feivel
 */
public final class Theme extends AbstractBean
{
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
   * The {@link Color} used to render identifier.
   * 
   * @see #getIdentifierColor()
   * @see #setIdentifierColor(Color)
   */
  private Color identifierColor ;


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
   * @see #getBoundIdColor()
   * @see #setBoundIdColor(Color)
   */
  private Color boundIdColor ;


  /**
   * The {@link Color} used to render free Identifier.
   * 
   * @see #getFreeIdColor()
   * @see #setFreeIdColor(Color)
   */
  private Color freeIdColor ;


  /**
   * The {@link Color} used to render binding Identifier.
   * 
   * @see #getBindingIdColor()
   * @see #setBindingIdColor(Color)
   */
  private Color bindingIdColor ;


  /**
   * The {@link Color} used to render highlighted source code.
   * 
   * @see #getHighlightSourceCodeColor()
   * @see #setHighlightSourceCodeColor(Color)
   */
  private Color highlightSourceCodeColor ;


  /**
   * The {@link Color} used to render parser warnings.
   * 
   * @see #getParserWarningColor()
   * @see #setParserWarningColor(Color)
   */
  private Color parserWarningColor ;


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
        "#009900" ) ) ; //$NON-NLS-1$
    // load the constantColor setting
    this.constantColor = Color.decode ( this.preferences.get ( "constantColor" , //$NON-NLS-1$
        "#00007F" ) ) ; //$NON-NLS-1$
    // load the environmentColor setting
    this.environmentColor = Color.decode ( this.preferences.get (
        "environmentColor" , "#7F7F7F" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    // load the expressionColor setting
    this.expressionColor = Color.decode ( this.preferences.get (
        "expressionColor" , "#000000" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    // load the selectionColor setting
    this.selectionColor = Color.decode ( this.preferences.get (
        "selectionColor" , "#FF0000" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    // load the boundIdColor setting
    this.boundIdColor = Color.decode ( this.preferences.get ( "boundIdColor" , //$NON-NLS-1$
        "#FFAA33" ) ) ; //$NON-NLS-1$
    // load the freeIdColor setting
    this.freeIdColor = Color.decode ( this.preferences.get ( "freeIdColor" , //$NON-NLS-1$
        "#3333FF" ) ) ; //$NON-NLS-1$
    // load the bindingIdColor setting
    this.bindingIdColor = Color.decode ( this.preferences.get (
        "bindingIdColor" , "#FF5519" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    this.highlightSourceCodeColor = Color.decode ( this.preferences.get (
        "highlightSourceCodeColor" , "#FFFF00" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    this.parserWarningColor = Color.decode ( this.preferences.get (
        "parserWarningColor" , "#E8F2FE" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    // load the font setting
    /*
     * FIXME: This doesn't work on Windows! (*surprise*) Font defaultFont = new
     * JLabel().getFont(); this.font = new Font(this.preferences.get("fontName",
     * defaultFont.getName()), this.preferences.getInt("fontStyle",
     * defaultFont.getStyle()), this.preferences.getInt("fontSize",
     * defaultFont.getSize()));
     */
    this.font = new JComboBox ( ).getFont ( ) ;
    // another Test: Under windows, the methode new JComboBox ( ).getFont ( ) ;
    // returns "Tahoma", nobody knows why,
    // but Tahoma dose not habe our UNI-Code-Chars. With this line, we get the
    // Font "Dialog" JAVA normaly uses. This
    // seems not to be a systemfont but works fine.
    this.font = new Font ( "Dialog" , Font.PLAIN , this.font.getSize ( ) + 2 ) ; //$NON-NLS-1$
    // load the keywordColor setting
    this.keywordColor = Color.decode ( this.preferences.get ( "keywordColor" , //$NON-NLS-1$
        "#7F0000" ) ) ; //$NON-NLS-1$
    this.identifierColor = Color.decode ( this.preferences.get (
        "identifierColor" , //$NON-NLS-1$
        "#000066" ) ) ; //$NON-NLS-1$
    // load the ruleColor setting
    this.ruleColor = Color.decode ( this.preferences.get ( "ruleColor" , //$NON-NLS-1$
        "#000000" ) ) ; //$NON-NLS-1$
    // load the underlineColor setting
    this.underlineColor = Color.decode ( this.preferences.get (
        "underlineColor" , "#FF0000" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    // load the typeColor setting
    this.typeColor = Color.decode ( this.preferences.get ( "typeColor" , //$NON-NLS-1$
        "#009900" ) ) ; //$NON-NLS-1$
  }


  /**
   * Returns the {@link Color} used to render binding Identifiers.
   * 
   * @return the color for the binding Identifiers.
   * @see #setBindingIdColor(Color)
   * @see Color
   */
  public Color getBindingIdColor ( )
  {
    return this.bindingIdColor ;
  }


  /**
   * Returns the {@link Color} used to render bound Identifiers.
   * 
   * @return the color for bound Identifier.s
   * @see #setBoundIdColor(Color)
   * @see Color
   */
  public Color getBoundIdColor ( )
  {
    return this.boundIdColor ;
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
   * Returns the {@link Color} used to render free Identifier.
   * 
   * @return the color for the free Identifier.
   * @see #setFreeIdColor(Color)
   * @see Color
   */
  public Color getFreeIdColor ( )
  {
    return this.freeIdColor ;
  }


  /**
   * Returns the {@link Color} used to render highlighted source code.
   * 
   * @return the color for the highlighted source code.
   * @see #setHighlightSourceCodeColor(Color)
   * @see Color
   */
  public Color getHighlightSourceCodeColor ( )
  {
    return this.highlightSourceCodeColor ;
  }


  /**
   * Returns the {@link Color} used to render Identifiers in the interpreters,
   * type checker and the editor.
   * 
   * @return the color for Identifiers.
   * @see #setIdentifierColor(Color)
   * @see Color
   */
  public Color getIdentifierColor ( )
  {
    return this.identifierColor ;
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
   * Returns the {@link Color} used to render parser warnings.
   * 
   * @return the color for the parser warnings.
   * @see #setParserWarningColor(Color)
   * @see Color
   */
  public Color getParserWarningColor ( )
  {
    return this.parserWarningColor ;
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
   * Sets the color that should be used to render binding Identifier to the
   * specified <code>bindingIdColor</code>.
   * 
   * @param pBindingIdColor the color for binding Identifier.
   * @see #getBindingIdColor()
   * @see Color
   * @throws NullPointerException if <code>bindingIdColor</code> is
   *           <code>null</code>.
   */
  public void setBindingIdColor ( Color pBindingIdColor )
  {
    if ( pBindingIdColor == null )
    {
      throw new NullPointerException ( "bindingIdColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.bindingIdColor.equals ( pBindingIdColor ) )
    {
      Color oldIdColor = this.bindingIdColor ;
      this.bindingIdColor = pBindingIdColor ;
      firePropertyChange ( "bindingIdColor" , oldIdColor , pBindingIdColor ) ; //$NON-NLS-1$
      this.preferences
          .put ( "bindingIdColor" , encodeColor ( pBindingIdColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Sets the color that should be used to render bindings to the specified
   * <code>bindingsColor</code>.
   * 
   * @param pBoundIdColor the color for bindings.
   * @see #getBoundIdColor()
   * @see Color
   * @throws NullPointerException if <code>boundIdColor</code> is
   *           <code>null</code>.
   */
  public void setBoundIdColor ( Color pBoundIdColor )
  {
    if ( pBoundIdColor == null )
    {
      throw new NullPointerException ( "pBindingColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.boundIdColor.equals ( pBoundIdColor ) )
    {
      Color oldBindingColor = this.boundIdColor ;
      this.boundIdColor = pBoundIdColor ;
      firePropertyChange ( "boundIdColor" , oldBindingColor , pBoundIdColor ) ; //$NON-NLS-1$
      this.preferences.put ( "boundIdColor" , encodeColor ( pBoundIdColor ) ) ; //$NON-NLS-1$
    }
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
      Color oldCommentColor = this.commentColor ;
      this.commentColor = pCommentColor ;
      firePropertyChange ( "commentColor" , oldCommentColor , pCommentColor ) ; //$NON-NLS-1$
      this.preferences.put ( "commentColor" , encodeColor ( pCommentColor ) ) ; //$NON-NLS-1$
    }
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
      Color oldConstantColor = this.constantColor ;
      this.constantColor = pConstantColor ;
      firePropertyChange ( "constantColor" , oldConstantColor , pConstantColor ) ; //$NON-NLS-1$
      this.preferences.put ( "constantColor" , encodeColor ( pConstantColor ) ) ; //$NON-NLS-1$
    }
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
      Color oldEnvironmentColor = this.environmentColor ;
      this.environmentColor = pEnvironmentColor ;
      firePropertyChange ( "environmentColor" , oldEnvironmentColor , //$NON-NLS-1$
          pEnvironmentColor ) ;
      this.preferences.put ( "environmentColor" , //$NON-NLS-1$
          encodeColor ( pEnvironmentColor ) ) ;
    }
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
      Color oldExpressionColor = this.expressionColor ;
      this.expressionColor = pExpressionColor ;
      firePropertyChange ( "expressionColor" , oldExpressionColor , //$NON-NLS-1$
          pExpressionColor ) ;
      this.preferences.put (
          "expressionColor" , encodeColor ( pExpressionColor ) ) ; //$NON-NLS-1$
    }
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
   * changes the global font-size that is used fot renders and editors to the
   * given size
   * 
   * @param size the new sizer;
   */
  public void setFontSize ( int size )
  {
    this.font = this.font.deriveFont ( ( float ) size ) ;
  }


  /**
   * Sets the color that should be used to render free Identifier to the
   * specified <code>freeIdColor</code>.
   * 
   * @param pFreeIdColor the color for free Identifier.
   * @see #getFreeIdColor()
   * @see Color
   * @throws NullPointerException if <code>freeIdColor</code> is
   *           <code>null</code>.
   */
  public void setFreeIdColor ( Color pFreeIdColor )
  {
    if ( pFreeIdColor == null )
    {
      throw new NullPointerException ( "freeIdColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.freeIdColor.equals ( pFreeIdColor ) )
    {
      Color oldUnboundColor = this.freeIdColor ;
      this.freeIdColor = pFreeIdColor ;
      firePropertyChange ( "freeIdColor" , oldUnboundColor , pFreeIdColor ) ; //$NON-NLS-1$
      this.preferences.put ( "freeIdColor" , encodeColor ( pFreeIdColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Sets the color that should be used to render highlighted source code to the
   * specified <code>pHighlightSourceCodeColor</code>.
   * 
   * @param pHighlightSourceCodeColor the color forhighlighted source code.
   * @see #getHighlightSourceCodeColor()
   * @see Color
   * @throws NullPointerException if <code>pHighlightSourceCodeColor</code> is
   *           <code>null</code>.
   */
  public void setHighlightSourceCodeColor ( Color pHighlightSourceCodeColor )
  {
    if ( pHighlightSourceCodeColor == null )
    {
      throw new NullPointerException ( "highlightSourceCodeColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.highlightSourceCodeColor.equals ( pHighlightSourceCodeColor ) )
    {
      Color oldSourceColor = this.highlightSourceCodeColor ;
      this.highlightSourceCodeColor = pHighlightSourceCodeColor ;
      firePropertyChange (
          "highlightSourceCodeColor" , oldSourceColor , pHighlightSourceCodeColor ) ; //$NON-NLS-1$
      this.preferences
          .put (
              "highlightSourceCodeColor" , encodeColor ( pHighlightSourceCodeColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Sets the color that should be used to render Identifiers to the specified
   * <code>identifierColor</code>.
   * 
   * @param pIdentifierColor the color for Identifiers.
   * @see #getIdentifierColor()
   * @see Color
   * @throws NullPointerException if <code>identifierColor</code> is
   *           <code>null</code>.
   */
  public void setIdentifierColor ( Color pIdentifierColor )
  {
    if ( pIdentifierColor == null )
    {
      throw new NullPointerException ( "identifierColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.identifierColor.equals ( pIdentifierColor ) )
    {
      Color oldIdentifierColor = this.identifierColor ;
      this.identifierColor = pIdentifierColor ;
      firePropertyChange (
          "identifierColor" , oldIdentifierColor , pIdentifierColor ) ; //$NON-NLS-1$
      this.preferences.put (
          "identifierColor" , encodeColor ( pIdentifierColor ) ) ; //$NON-NLS-1$
    }
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
      Color oldKeywordColor = this.keywordColor ;
      this.keywordColor = pKeywordColor ;
      firePropertyChange ( "keywordColor" , oldKeywordColor , pKeywordColor ) ; //$NON-NLS-1$
      this.preferences.put ( "keywordColor" , encodeColor ( pKeywordColor ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Sets the color that should be used to render parser warnings to the
   * specified <code>pParserWarningColor</code>.
   * 
   * @param pParserWarningColor the color for parser warnings.
   * @see #getParserWarningColor()
   * @see Color
   * @throws NullPointerException if <code>pParserWarningColor</code> is
   *           <code>null</code>.
   */
  public void setParserWarningColor ( Color pParserWarningColor )
  {
    if ( pParserWarningColor == null )
    {
      throw new NullPointerException ( "parserWarningColor is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.parserWarningColor.equals ( pParserWarningColor ) )
    {
      Color oldParserColor = this.parserWarningColor ;
      this.parserWarningColor = pParserWarningColor ;
      firePropertyChange (
          "parserWarningColor" , oldParserColor , pParserWarningColor ) ; //$NON-NLS-1$
      this.preferences.put (
          "parserWarningColor" , encodeColor ( pParserWarningColor ) ) ; //$NON-NLS-1$
    }
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
      Color oldRuleColor = this.ruleColor ;
      this.ruleColor = pRuleColor ;
      firePropertyChange ( "ruleColor" , oldRuleColor , pRuleColor ) ; //$NON-NLS-1$
      this.preferences.put ( "ruleColor" , encodeColor ( pRuleColor ) ) ; //$NON-NLS-1$
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
      // update the selectionColor
      Color oldSelectionColor = this.selectionColor ;
      this.selectionColor = pSelectionColor ;
      firePropertyChange (
          "selectionColor" , oldSelectionColor , pSelectionColor ) ; //$NON-NLS-1$
      // save the new setting
      this.preferences
          .put ( "selectionColor" , encodeColor ( pSelectionColor ) ) ; //$NON-NLS-1$
    }
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
}
