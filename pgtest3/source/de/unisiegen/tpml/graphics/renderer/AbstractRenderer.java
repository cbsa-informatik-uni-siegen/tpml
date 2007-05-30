package de.unisiegen.tpml.graphics.renderer ;


import java.awt.Color ;
import java.awt.Component ;
import java.awt.Font ;
import java.awt.FontMetrics ;
import java.beans.PropertyChangeEvent ;
import java.beans.PropertyChangeListener ;

import javax.swing.JComboBox;
import javax.swing.JLabel ;
import de.unisiegen.tpml.graphics.Theme ;


/**
 * Abstract base class for the {@link PrettyStringRenderer} providing fonts,
 * colors and fontmetrices to perform the rendering.<br>
 * <br>
 * All the Colors and Fonts coms from the current {@link Theme}.
 * 
 * @author marcell
 * @autor michael
 */
public abstract class AbstractRenderer
{
  protected static Font expFont ;


  protected static FontMetrics expFontMetrics ;


  protected static Color expColor ;


  protected static Font identifierFont ;


  protected static FontMetrics identifierFontMetrics ;


  protected static Color identifierColor ;


  protected static Font keywordFont ;


  protected static FontMetrics keywordFontMetrics ;


  protected static Color keywordColor ;


  protected static Font constantFont ;


  protected static FontMetrics constantFontMetrics ;


  protected static Color constantColor ;


  protected static Font envFont ;


  protected static FontMetrics envFontMetrics ;


  protected static Color envColor ;


  protected static Font typeFont ;


  protected static FontMetrics typeFontMetrics ;


  protected static Color typeColor ;


  protected static Color underlineColor ;


  protected static int fontHeight ;


  protected static int fontAscent ;


  protected static int fontDescent ;
  
  protected static int fontLeading ;


  protected static Font exponentFont ;


  protected static FontMetrics exponentFontMetrics ;


  protected static Color exponentColor ;


  private static Theme theme ;
  static
  {
    // connect to the theme
    theme = Theme.currentTheme ( ) ;
    setTheme ( theme , new JLabel ( ) ) ;
    theme.addPropertyChangeListener ( new PropertyChangeListener ( )
    {
      public void propertyChange ( PropertyChangeEvent evt )
      {
        setTheme ( theme , new JLabel ( ) ) ;
      }
    } ) ;
  }


  public static Color getExponentColor ( )
  {
    return AbstractRenderer.exponentColor ;
  }


  public static Font getExponentFont ( )
  {
    return AbstractRenderer.exponentFont ;
  }


  public static FontMetrics getExponentFontMetrics ( )
  {
    return AbstractRenderer.exponentFontMetrics ;
  }


  public static Color getTextColor ( )
  {
    return AbstractRenderer.expColor ;
  }


  public static Font getTextFont ( )
  {
    return AbstractRenderer.expFont ;
  }


  public static FontMetrics getTextFontMetrics ( )
  {
    return AbstractRenderer.expFontMetrics ;
  }


  /**
   * Initializes the colors, fonts and fontmetrics.
   * 
   * @param theme The that that should be used to retrieve the information for
   *          the stuff.
   * @param reference Any object subclassing {@link Component} used to call the
   *          {@link Component#getFontMetrics(java.awt.Font)}-Method.
   */
  private static void setTheme ( Theme theme , Component reference )
  {
    AbstractRenderer.expColor = theme.getExpressionColor ( ) ;
    AbstractRenderer.expFont = theme.getFont ( );//;.deriveFont ( Font.PLAIN ) ;
    AbstractRenderer.expFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.expFont ) ;
    AbstractRenderer.identifierColor = theme.getIdentifierColor ( ) ;
    AbstractRenderer.identifierFont = theme.getFont();// ( ).deriveFont ( Font.PLAIN ) ;
    //AbstractRenderer.identifierFont = f;
    AbstractRenderer.identifierFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.identifierFont ) ;
    AbstractRenderer.keywordColor = theme.getKeywordColor ( ) ;
    AbstractRenderer.keywordFont = theme.getFont ( ).deriveFont ( Font.BOLD ) ;
    //.deriveFont ( Font.BOLD ) ;
    AbstractRenderer.keywordFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.keywordFont ) ;
    AbstractRenderer.constantColor = theme.getConstantColor ( ) ;
    AbstractRenderer.constantFont = theme.getFont ( ).deriveFont ( Font.BOLD ) ;
    AbstractRenderer.constantFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.constantFont ) ;
    AbstractRenderer.envColor = theme.getEnvironmentColor ( ) ;
    AbstractRenderer.envFont = theme.getFont ( ) ;
    AbstractRenderer.envFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.envFont ) ;
    AbstractRenderer.typeColor = theme.getTypeColor ( ) ;
    AbstractRenderer.typeFont = theme.getFont ( ).deriveFont ( Font.BOLD ) ;
    AbstractRenderer.typeFontMetrics = reference
        .getFontMetrics ( AbstractRenderer.typeFont ) ;
    AbstractRenderer.underlineColor = theme.getUnderlineColor ( ) ;
    
    //TODO patching up the fonthight
    AbstractRenderer.fontHeight = Math
        .max ( AbstractRenderer.expFontMetrics.getHeight ( ) , Math
            .max ( AbstractRenderer.keywordFontMetrics.getHeight ( ) , Math
                .max ( AbstractRenderer.identifierFontMetrics.getHeight ( ) ,
                    Math.max ( AbstractRenderer.constantFontMetrics
                        .getHeight ( ) , Math.max (
                        AbstractRenderer.envFontMetrics.getHeight ( ) ,
                        AbstractRenderer.typeFontMetrics.getHeight ( ) ) ) ) ) ) ;
    AbstractRenderer.fontAscent = Math
        .max ( AbstractRenderer.expFontMetrics.getAscent ( ) , Math
            .max ( AbstractRenderer.keywordFontMetrics.getAscent ( ) , Math
                .max ( AbstractRenderer.identifierFontMetrics.getAscent ( ) ,
                    Math.max ( AbstractRenderer.constantFontMetrics
                        .getAscent ( ) , Math.max (
                        AbstractRenderer.envFontMetrics.getAscent ( ) ,
                        AbstractRenderer.typeFontMetrics.getAscent ( ) ) ) ) ) ) ;
    AbstractRenderer.fontDescent = Math.max ( AbstractRenderer.expFontMetrics
        .getDescent ( ) , Math
        .max ( AbstractRenderer.keywordFontMetrics.getDescent ( ) , Math.max (
            AbstractRenderer.identifierFontMetrics.getDescent ( ) , Math.max (
                AbstractRenderer.constantFontMetrics.getDescent ( ) , Math.max (
                    AbstractRenderer.envFontMetrics.getDescent ( ) ,
                    AbstractRenderer.typeFontMetrics.getDescent ( ) ) ) ) ) ) ;
    
    //TODO Patching....
    
    AbstractRenderer.fontLeading = Math.max ( 2, 
    	Math.max ( AbstractRenderer.expFontMetrics
        .getLeading ( ) , Math
        .max ( AbstractRenderer.keywordFontMetrics.getLeading ( )  , Math.max (
            AbstractRenderer.identifierFontMetrics.getLeading ( )  , Math.max (
                AbstractRenderer.constantFontMetrics.getLeading ( )  , Math.max (
                    AbstractRenderer.envFontMetrics.getLeading ( )  ,
                    AbstractRenderer.typeFontMetrics.getLeading ( ) ) ) ) ) )  ) ;
  }


  protected Color alternativeColor ;


  public AbstractRenderer ( )
  {
    this.alternativeColor = null ;
  }


  public void setAlternativeColor ( Color color )
  {
    this.alternativeColor = color ;
  }


	/**
	 * @return the fontAscent
	 */
	public static int getFontAscent()
	{
		return fontAscent;
	}
}
