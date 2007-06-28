package de.unisiegen.tpml.core.languages ;


import java.text.MessageFormat ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.exceptions.LanguageParserWarningException ;


/**
 * A helper class for the parser.
 * 
 * @author Christian Fehler
 */
public class Error
{
  /**
   * Throws an {@link LanguageParserWarningException}.
   * 
   * @param pSymbol The symbol which should be completed.
   * @param pLeft The left position in the source code.
   * @param pRight The right position in the source code.
   * @param pTokenSequence The token sequence which should be added.
   */
  public static void expect ( String pSymbol , int pLeft , int pRight ,
      String ... pTokenSequence )
  {
    expect ( pSymbol , " " //$NON-NLS-1$
        + pTokenSequence [ 0 ].replaceAll ( "<sub>" , "" ).replaceAll ( //$NON-NLS-1$//$NON-NLS-2$
            "</sub>" , "" ) , pLeft , pRight , pTokenSequence ) ; //$NON-NLS-1$//$NON-NLS-2$
  }


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
  public static void expect ( String pSymbol , String pInsertText , int pLeft ,
      int pRight , String ... pTokenSequence )
  {
    String or = Messages.getString ( "Parser.15" ) ; //$NON-NLS-1$
    String result = "" ; //$NON-NLS-1$
    for ( int i = 0 ; i < pTokenSequence.length ; i ++ )
    {
      if ( i == 0 )
      {
        result += "\"" + pTokenSequence [ i ] + "\"" ; //$NON-NLS-1$//$NON-NLS-2$
      }
      else if ( i == pTokenSequence.length - 1 )
      {
        result += " " + or + " \"" + pTokenSequence [ i ] + "\"" ; //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
      }
      else
      {
        result += ", \"" + pTokenSequence [ i ] + "\"" ; //$NON-NLS-1$//$NON-NLS-2$
      }
    }
    throw new LanguageParserWarningException ( MessageFormat.format ( "<html>" //$NON-NLS-1$
        + Messages.getString ( "Parser.14" ) + "<br>" + "(" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        + Messages.getString ( "Parser.17" ) + ")" + "</html>" , result , //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
        pSymbol ) , pLeft , pRight , pInsertText ) ;
  }
}
