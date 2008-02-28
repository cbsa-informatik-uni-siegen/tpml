package de.unisiegen.tpml.core.languages;


import java.awt.Color;
import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.exceptions.LanguageParserWarningException;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.util.Theme;


/**
 * A helper class for the parser.
 * 
 * @author Christian Fehler
 */
public abstract class Error
{

  /**
   * The {@link Expression} color.
   */
  private static String expressionColor;


  /**
   * The {@link Identifier} color.
   */
  private static String identifierColor;


  /**
   * The keyword color.
   */
  private static String keywordColor;


  /**
   * The constant color.
   */
  private static String constantColor;


  /**
   * The type color.
   */
  private static String typeColor;


  /**
   * The keywords.
   */
  private static String [] keywords = new String []
  { "lambda", "\u03BB", "let", "in", "if", "then", "else", "&&", "\\|\\|", //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
      "mu", "\u03BC", "rec", "while", "do", "object", "end", "val",//$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
      "method", "class", "new", "inherit", "from", "as" };//$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$


  /**
   * The types.
   */
  private static String [] types = new String []
  { "bool", "int", "unit", "[\u03b1-\u03c1\u03c3-\u03c9]", "'[a-x]", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
      "list" }; //$NON-NLS-1$


  /**
   * The normal identifiers.
   */
  private static String identifier1 = "[a-zA-Z][a-zA-Z0-9_]*'*"; //$NON-NLS-1$


  /**
   * The identifiers with a number index.
   */
  private static String identifier2 = "[a-zA-Z][a-zA-Z0-9_]*'*[0-9]"; //$NON-NLS-1$


  /**
   * The identifiers with a index.
   */
  private static String identifier3 = "[a-zA-Z][a-zA-Z0-9_]*'*_[a-zA-Z0-9]"; //$NON-NLS-1$


  /**
   * The constants
   */
  private static String [] constants = new String []
  { "true", "false", "[0-9]+", "()", "mod", "not", "fst", "snd", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$//$NON-NLS-7$//$NON-NLS-8$
      "#[0-9]+_[0-9]+", "cons", "is_empty", "hd", "tl", "\\[\\]", ":=", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$//$NON-NLS-7$
      "ref" }; //$NON-NLS-1$


  /**
   * Throws an {@link LanguageParserWarningException}.
   * 
   * @param pSymbol The symbol which should be completed.
   * @param pInsertText The text which should be inserted to complete the source
   *          code.
   * @param pLeft The left position in the source code.
   * @param pRight The right position in the source code.
   * @param pTokenSequence The token sequence which should be added.
   */
  public static final void expect ( String pSymbol, String pInsertText,
      int pLeft, int pRight, String ... pTokenSequence )
  {
    expressionColor = getHexadecimalColor ( Theme.currentTheme ()
        .getExpressionColor () );
    identifierColor = getHexadecimalColor ( Theme.currentTheme ()
        .getIdentifierColor () );
    keywordColor = getHexadecimalColor ( Theme.currentTheme ()
        .getKeywordColor () );
    constantColor = getHexadecimalColor ( Theme.currentTheme ()
        .getConstantColor () );
    typeColor = getHexadecimalColor ( Theme.currentTheme ().getTypeColor () );
    StringBuilder result = new StringBuilder ();
    result.append ( "\"" ); //$NON-NLS-1$
    for ( int i = 0 ; i < pTokenSequence.length ; i++ )
    {
      String token = pTokenSequence [ i ];
      token = token.replaceAll ( "&", "&amp" ); //$NON-NLS-1$ //$NON-NLS-2$
      token = token.replaceAll ( "<", "&lt" ); //$NON-NLS-1$//$NON-NLS-2$
      token = token.replaceAll ( ">", "&gt" ); //$NON-NLS-1$ //$NON-NLS-2$
      result.append ( syntaxHighlighting ( token ) );
    }
    result.append ( "\"" ); //$NON-NLS-1$
    throw new LanguageParserWarningException (
        MessageFormat
            .format (
                "<html>" //$NON-NLS-1$
                    + Messages.getString ( "Parser.14" ) + "<br>" + "(" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    + Messages.getString ( "Parser.17" ) + ")" + "</html>", result.toString (), //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
                "<b>" + pSymbol + "</b>" ), pLeft, pRight, pInsertText ); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Returns the color in hexadecimal formatting.
   * 
   * @param pColor The color which should be returned.
   * @return The color in hexadecimal formatting.
   */
  private static final String getHexadecimalColor ( Color pColor )
  {
    String red = Integer.toHexString ( pColor.getRed () );
    red = red.length () == 1 ? red = "0" + red : red; //$NON-NLS-1$
    String green = Integer.toHexString ( pColor.getGreen () );
    green = green.length () == 1 ? green = "0" + green : green; //$NON-NLS-1$
    String blue = Integer.toHexString ( pColor.getBlue () );
    blue = blue.length () == 1 ? blue = "0" + blue : blue; //$NON-NLS-1$
    return red + green + blue;
  }


  /**
   * Highlights the syntax of the given input string.
   * 
   * @param pInput The input string.
   * @return The highlighted the syntax string of the given input string.
   */
  private static final String syntaxHighlighting ( String pInput )
  {
    for ( String k : keywords )
    {
      if ( pInput.matches ( k ) )
      {
        return "<b><font color=\"#" + keywordColor + "\">" + pInput //$NON-NLS-1$ //$NON-NLS-2$
            + "</font></b>"; //$NON-NLS-1$
      }
    }
    for ( String t : types )
    {
      if ( pInput.matches ( t ) )
      {
        return "<b><font color=\"#" + typeColor + "\">" + pInput //$NON-NLS-1$//$NON-NLS-2$
            + "</font></b>"; //$NON-NLS-1$
      }
    }
    for ( String c : constants )
    {
      if ( pInput.matches ( c ) )
      {
        return "<b><font color=\"#" + constantColor + "\">" + pInput //$NON-NLS-1$//$NON-NLS-2$
            + "</font></b>"; //$NON-NLS-1$
      }
    }
    if ( pInput.matches ( identifier3 ) )
    {
      String withIndex = pInput;
      withIndex = withIndex.substring ( 0, withIndex.length () - 2 );
      withIndex += "<font size = \"-2\"><sub>" + pInput.charAt ( pInput.length () - 1 ) + "</sub></font>"; //$NON-NLS-1$ //$NON-NLS-2$
      return "<font color=\"#" + identifierColor + "\">" + withIndex //$NON-NLS-1$ //$NON-NLS-2$
          + "</font>"; //$NON-NLS-1$
    }
    if ( pInput.matches ( identifier2 ) )
    {
      String withIndex = pInput;
      withIndex = withIndex.substring ( 0, withIndex.length () - 1 );
      withIndex += "<font size = \"-2\"><sub>" + pInput.charAt ( pInput.length () - 1 ) + "</sub></font>"; //$NON-NLS-1$ //$NON-NLS-2$
      return "<font color=\"#" + identifierColor + "\">" + withIndex //$NON-NLS-1$ //$NON-NLS-2$
          + "</font>"; //$NON-NLS-1$
    }
    if ( pInput.matches ( identifier1 ) )
    {
      return "<font color=\"#" + identifierColor + "\">" + pInput //$NON-NLS-1$ //$NON-NLS-2$
          + "</font>"; //$NON-NLS-1$
    }
    return "<font color=\"#" + expressionColor + "\">" + pInput + "</font>"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
  }
}
