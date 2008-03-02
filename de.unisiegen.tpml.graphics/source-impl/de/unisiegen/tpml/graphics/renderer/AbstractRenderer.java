package de.unisiegen.tpml.graphics.renderer;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import de.unisiegen.tpml.core.util.Theme;


/**
 * Abstract base class for the {@link PrettyStringRenderer} providing fonts,
 * colors and fontmetrices to perform the rendering.<br>
 * <br>
 * All the Colors and Fonts coms from the current {@link Theme}.
 * 
 * @author marcell
 * @author michael
 */
public abstract class AbstractRenderer
{

  /**
   * the <code> Font </code> used for expressions and everything else
   */
  protected static Font expFont;


  /**
   * the <code> FontMetrics </code> used by expressions and everything else
   */
  protected static FontMetrics expFontMetrics;


  /**
   * the <code> Color </code> used to render the expression and everything else
   */
  protected static Color expColor;


  /**
   * the <code> Font </code> used to render identifiers
   */
  protected static Font identifierFont;


  /**
   * the <code> FontMetrics </code> used to render identifiers
   */
  protected static FontMetrics identifierFontMetrics;


  /**
   * the <code> Color </code> used to render identifiers
   */
  protected static Color identifierColor;


  /**
   * the <code> Font </code> used to render the keywords
   */
  protected static Font keywordFont;


  /**
   * the <code> FontMetrics </code> used to render the keywords
   */
  protected static FontMetrics keywordFontMetrics;


  /**
   * the <code> Color </code> used to render the keywords
   */
  protected static Color keywordColor;


  /**
   * the <code> Font </code> used to render the constants
   */
  protected static Font constantFont;


  /**
   * the <code> FontMetrics </code> used to render the constants
   */
  protected static FontMetrics constantFontMetrics;


  /**
   * the <code> Color </code> used to render the constants
   */
  protected static Color constantColor;


  /**
   * the <code> Font </code> used to render the environments
   */
  protected static Font envFont;


  /**
   * the <code> FontMetrics </code> used to render the environments
   */
  protected static FontMetrics envFontMetrics;


  /**
   * the <code> Color </code> used to render the environments
   */
  protected static Color envColor;


  /**
   * the <code> Font </code> used to render the types
   */
  protected static Font typeFont;


  /**
   * the <code> FontMetrics </code> used to render the types
   */
  protected static FontMetrics typeFontMetrics;


  /**
   * the <code> Color </code> used to render the types
   */
  protected static Color typeColor;


  /**
   * the <code> Font </code> used to render the exponents
   */
  protected static Font exponentFont;


  /**
   * the <code> FontMetrics </code> used to render the exponents
   */
  protected static FontMetrics exponentFontMetrics;


  /**
   * the <code> Color </code> used to render the exponents
   */
  protected static Color exponentColor;


  /**
   * the <code> Color </code> used to render the underlins
   */
  protected static Color underlineColor;


  /**
   * the fontHeight
   */
  protected static int fontHeight;


  /**
   * the fontAscent: it is the height form the groundline to the top of the
   * biggest chars. for exampel, the height of a i is ascent
   */
  protected static int fontAscent;


  /**
   * the fontDescent: it is the height from the deepest position of a char like
   * the g or the y to the groundline
   */
  protected static int fontDescent;


  /**
   * the fontLeading: it is the height of the space between to lines.
   */
  protected static int fontLeading;


  /**
   * the current theme
   */
  private static Theme theme;
  static
  {
    // connect to the theme
    theme = Theme.currentTheme ();
    setTheme ( theme, new JLabel () );
    theme.addPropertyChangeListener ( new PropertyChangeListener ()
    {

      public void propertyChange ( @SuppressWarnings ( "unused" )
      PropertyChangeEvent evt )
      {
        setTheme ( getTheme (), new JLabel () );
      }
    } );
  }


  /**
   * returns the the <code> Color </code> used to render the exponents
   * 
   * @return exponentColor
   */
  public static Color getExponentColor ()
  {
    return AbstractRenderer.exponentColor;
  }


  /**
   * returns the <code> Font </code> used to render the exponents
   * 
   * @return exponentFont
   */
  public static Font getExponentFont ()
  {
    return AbstractRenderer.exponentFont;
  }


  /**
   * returns the <code> FontMetrics </code> used to render the exponents
   * 
   * @return exponentFontMetrics
   */
  public static FontMetrics getExponentFontMetrics ()
  {
    return AbstractRenderer.exponentFontMetrics;
  }


  /**
   * returns the <code> Color </code> used to render the expression and
   * everything else
   * 
   * @return expColor
   */
  public static Color getTextColor ()
  {
    return AbstractRenderer.expColor;
  }


  /**
   * returns the <code> Font </code> used for expressions and everything else
   * 
   * @return expFont
   */
  public static Font getTextFont ()
  {
    return AbstractRenderer.expFont;
  }


  /**
   * return the <code> FontMetrics </code> used by expressions and everything
   * else
   * 
   * @return expFontMetrics
   */
  public static FontMetrics getTextFontMetrics ()
  {
    return AbstractRenderer.expFontMetrics;
  }


  /**
   * Initializes the colors, fonts and fontmetrics.
   * 
   * @param pTheme The that that should be used to retrieve the information for
   *          the stuff.
   * @param reference Any object subclassing {@link Component} used to call the
   *          {@link Component#getFontMetrics(java.awt.Font)}-Method.
   */
  static void setTheme ( Theme pTheme, Component reference )
  {
    AbstractRenderer.expColor = pTheme.getExpressionColor ();
    AbstractRenderer.expFont = pTheme.getFont ();// ;.deriveFont ( Font.PLAIN )
    // ;
    AbstractRenderer.expFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.expFont );
    AbstractRenderer.identifierColor = pTheme.getIdentifierColor ();
    AbstractRenderer.identifierFont = pTheme.getFont ();// ( ).deriveFont (
    // Font.PLAIN ) ;
    AbstractRenderer.identifierFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.identifierFont );
    AbstractRenderer.keywordColor = pTheme.getKeywordColor ();
    AbstractRenderer.keywordFont = pTheme.getFont ().deriveFont ( Font.BOLD );
    AbstractRenderer.keywordFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.keywordFont );
    AbstractRenderer.constantColor = pTheme.getConstantColor ();
    AbstractRenderer.constantFont = pTheme.getFont ().deriveFont ( Font.BOLD );
    AbstractRenderer.constantFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.constantFont );
    AbstractRenderer.envColor = pTheme.getEnvironmentColor ();
    AbstractRenderer.envFont = pTheme.getFont ();
    AbstractRenderer.envFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.envFont );
    AbstractRenderer.typeColor = pTheme.getTypeColor ();
    AbstractRenderer.typeFont = pTheme.getFont ().deriveFont ( Font.BOLD );
    AbstractRenderer.typeFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.typeFont );
    AbstractRenderer.underlineColor = pTheme.getUnderlineColor ();

    fontHeight = realMax ( expFontMetrics.getHeight (), keywordFontMetrics
        .getHeight (), identifierFontMetrics.getHeight (), constantFontMetrics
        .getHeight (), envFontMetrics.getHeight (), typeFontMetrics
        .getHeight () );

    fontAscent = realMax ( expFontMetrics.getAscent (), keywordFontMetrics
        .getAscent (), AbstractRenderer.identifierFontMetrics.getAscent (),
        constantFontMetrics.getAscent (), envFontMetrics.getAscent (),
        typeFontMetrics.getAscent () );

    fontDescent = realMax ( expFontMetrics.getDescent (), keywordFontMetrics
        .getDescent (), identifierFontMetrics.getDescent (),
        constantFontMetrics.getDescent (), envFontMetrics.getDescent (),
        typeFontMetrics.getDescent () );

    // fontLeading = realMax(2, expFontMetrics.getLeading(),
    // keywordFontMetrics.getLeading(), identifierFontMetrics.getLeading(),
    // constantFontMetrics.getLeading(), envFontMetrics.getLeading(),
    // typeFontMetrics.getLeading());
    fontLeading = realMax ( expFontMetrics.getLeading (), keywordFontMetrics
        .getLeading (), identifierFontMetrics.getLeading (),
        constantFontMetrics.getLeading (), envFontMetrics.getLeading (),
        typeFontMetrics.getLeading () );
  }


  /**
   * the alternativeColor is used if it is set
   */
  protected Color alternativeColor;


  /**
   * the constructor, sets the alternave color to null
   */
  public AbstractRenderer ()
  {
    this.alternativeColor = null;
  }


  /**
   * set alternative color
   * 
   * @param color the alternaive color
   */
  public void setAlternativeColor ( Color color )
  {
    this.alternativeColor = color;
  }


  /**
   * get the real needed hieght for one line
   * 
   * @return the realy needed height
   */
  public static int getAbsoluteHeight ()
  {
    return fontHeight;
  }


  /**
   * get the fontLeading, the space between to lines
   * 
   * @return fontLeading
   */
  public static int getFontLeading ()
  {
    return fontLeading;
  }


  /**
   * returns the fontAscent, the space from groundline to top of a char
   * 
   * @return fontAscent
   */
  public static int getFontAscent ()
  {
    return fontAscent;
  }


  /**
   * @return the theme
   */
  public static Theme getTheme ()
  {
    return theme;
  }


  /**
   * @param vals
   * @return the max of all given vals;
   */
  private static int realMax ( int ... vals )
  {
    int result = vals [ 0 ];

    // element 0 is allready the result, start with 1
    for ( int i = 1 ; i < vals.length ; i++ )
    {
      result = Math.max ( result, vals [ i ] );
    }
    return result;
  }

}
