package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Font;
import java.text.CharacterIterator;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.graphics.Theme;


/**
 * This class provides the translation of PrettyStrings into HTML-Code
 * This is used by the @link CompoundExpressionTypeInference to provide the tooltip of
 * the A. This calss realy only translats the prettystring into html, the <html> and
 * </html> are missing and must be added. 
 * @author Feivel
 *
 */
public class PrettyStringToHTML
{
  
  /**
   * The hex values.
   */
  private static final String [ ] HEX_VALUES =
  { "0" , "1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "A" , "B" , //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
      "C" , "D" , "E" , "F" , "00" } ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
  
  /**
   * Ampersand replace.
   */
  private static final String AMPERSAND_THAN_REPLACE = "&amp" ; //$NON-NLS-1$
  
  /**
   * Lower than replace.
   */
  private static final String LOWER_THAN_REPLACE = "&lt" ; //$NON-NLS-1$
  
  /**
   * Greater than replace.
   */
  private static final String GREATER_THAN_REPLACE = "&gt" ; //$NON-NLS-1$
  
  
  /**
   * translates the given PrettyString to a HTML-Coded String
   * 
   * @param s The PrettyString to translate
   * @return hte HTML-coded Sring
   */
  public static String toHTMLString (PrettyString s)
  {
    //build the result
    String result = "";
    
    //for performance, use the Stringbuilder
    StringBuilder rb = new StringBuilder ( ) ;

    //the first font will be clesed directly 
    rb.append ("<font color=\"#FFFFFF\">");
    
    //these Strings will be inserted everytime a char is inserted
    //if the Font is with style BOLD, they will get <b> and </b>
    String boldAnte ="";
    String boldPost ="";
    
    //lets walk throug the PrettySting, char by char
    int charIndex = 0;
    //svae the last Syle: if the style change the color will be close (</font>)
    PrettyStyle lastStyle = PrettyStyle.NONE;
    PrettyCharIterator it = s.toCharacterIterator ();
    for ( char c = it.setIndex ( charIndex) ; c != CharacterIterator.DONE ; c = it.next ( ) , charIndex++ )
    {
      //get the style
      switch ( it.getStyle ( ) )
      {
        case KEYWORD :
          //if nothing changed
          if (lastStyle == it.getStyle ())
          {
            //add the actual char, surrounded with <b> and </b> if needed
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          //if the style changed, setup the new one
          else
          {
            //save the new style
            lastStyle = it.getStyle ();
            //buld up the color from theme
            rb.append ("</font>");
            Color col = Theme.currentTheme ().getKeywordColor ();
            String colH = getHTMLColor(col);
            rb.append ("<font color=\"#"+colH+"\">");
            //set the boldString if the style ist BOLD
            if (AbstractRenderer.keywordFont.getStyle ()== Font.BOLD)
            {
              boldAnte="<b>";
              boldPost="</b>";
            }
            //reset the BoldStrings if the style is not BOLD
            else
            {
              boldAnte="";
              boldPost="";
            }
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
            
          }
          break ;
          //The same preseture with ervery style...
        case IDENTIFIER :
          if (lastStyle == it.getStyle ())
          {
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          else
          {
            rb.append ("</font>");
            lastStyle = it.getStyle ();
            Color col = Theme.currentTheme ().getIdentifierColor ();
            String colH = getHTMLColor(col);
            rb.append ("<font color=\"#"+colH+"\">");
            if (AbstractRenderer.identifierFont.getStyle ()== Font.BOLD)
            {
              boldAnte="<b>";
              boldPost="</b>";
            }
            else
            {
              boldAnte="";
              boldPost="";
            }
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          break ;
        case NONE :
          if (lastStyle == it.getStyle ())
          {
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          else
          {
            rb.append ("</font>");
            lastStyle = it.getStyle ();
            Color col = Theme.currentTheme ().getExpressionColor ();
            String colH = getHTMLColor(col);
            rb.append ("<font color=\"#"+colH+"\">");
            if (AbstractRenderer.expFont.getStyle ()== Font.BOLD)
            {
              boldAnte="<b>";
              boldPost="</b>";
            }
            else
            {
              boldAnte="";
              boldPost="";
            }
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          break ;
        case CONSTANT :
          if (lastStyle == it.getStyle ())
          {
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          else
          {
            rb.append ("</font>");
            lastStyle = it.getStyle ();
            Color col = Theme.currentTheme ().getConstantColor ();
            String colH = getHTMLColor(col);
            rb.append ("<font color=\"#"+colH+"\">");
            if (AbstractRenderer.constantFont.getStyle ()== Font.BOLD)
            {
              boldAnte="<b>";
              boldPost="</b>";
            }
            else
            {
              boldAnte="";
              boldPost="";
            }
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          break ;
        case COMMENT :
          continue ;
        case TYPE :
          if (lastStyle == it.getStyle ())
          {
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          else
          {
            rb.append ("</font>");
            lastStyle = it.getStyle ();
            Color col = Theme.currentTheme ().getTypeColor ();
            String colH = getHTMLColor(col);
            rb.append ("<font color=\"#"+colH+"\">");
            if (AbstractRenderer.typeFont.getStyle ()== Font.BOLD)
            {
              boldAnte="<b>";
              boldPost="</b>";
            }
            else
            {
              boldAnte="";
              boldPost="";
            }
            rb.append (boldAnte+getHTMLCode(c)+boldPost);
          }
          break ;
      } 
    }
    
    rb.append ("</font>");
      
      result = rb.toString ();
      
      return result;     
  }
  
  /**
   * Returns the color in HTML formatting.
   * 
   * @param pColor The color which should be returned.
   * @return The color in HTML formatting.
   */
  public static final String getHTMLColor ( Color pColor )
  {
    return ( getHex ( pColor.getRed ( ) ) + getHex ( pColor.getGreen ( ) ) + getHex ( pColor
        .getBlue ( ) ) ) ;
  }
  
  /**
   * Returns the hex value of a given integer.
   * 
   * @param pNumber The input integer value.
   * @return The hex value of a given integer.
   */
  private static final String getHex ( int pNumber )
  {
    StringBuilder result = new StringBuilder ( ) ;
    int remainder = Math.abs ( pNumber ) ;
    int base = 0 ;
    if ( remainder > 0 )
    {
      while ( remainder > 0 )
      {
        base = remainder % 16 ;
        remainder = ( remainder / 16 ) ;
        result.insert ( 0 , HEX_VALUES [ base ] ) ;
      }
    }
    else
    {
      return HEX_VALUES [ 16 ] ;
    }
    return result.toString ( ) ;
  }
  
  /**
   * Returns the replaced <code>char</code>.
   * 
   * @param pChar Input <code>char</code>.
   * @return The replaced <code>char</code>.
   */
  private static final String getHTMLCode ( char pChar )
  {
    if ( pChar == '&' )
    {
      return AMPERSAND_THAN_REPLACE ;
    }
    if ( pChar == '<' )
    {
      return LOWER_THAN_REPLACE ;
    }
    if ( pChar == '>' )
    {
      return GREATER_THAN_REPLACE ;
    }
    return String.valueOf ( pChar ) ;
  }
}
