/**
 * TODO
 */
package de.unisiegen.tpml.core.subtyping;

import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import de.unisiegen.tpml.core.exceptions.CheckDisjunctionException;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.AbstractLanguageScanner;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageParser;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageScannerException;
import de.unisiegen.tpml.core.languages.LanguageSymbol;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.graphics.StyledLanguageDocument;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class StyledTypeEnterField extends StyledLanguageDocument {

	/**
	 * TODO
	 *
	 * @param language
	 */
	public StyledTypeEnterField(Language language) {
		super(language);
	}
	
  //
  // Primitives
  //
  /**
   * Returns the {@link Expression} for the program text within this document.
   * Throws an exception if a parsing error occurred.
   * 
   * @return the {@link Expression} for the program text.
   * @throws Exception
   */
  public MonoType getType ( ) throws Exception
  {
    return this.language.newTypeParser (
        new StringReader ( getText ( 0 , getLength ( ) ) ) ).parse ( ) ;
  	

  }
  
  
  /**
   * Processes the document content after a change.
   * 
   * @throws BadLocationException if the processing failed.
   */
  public void processChanged ( ) throws BadLocationException
  {
    // reset the character attributes
    setCharacterAttributes ( 0 , getLength ( ) , this.normalSet , true ) ;
    // allocate a list to collect the exceptions
    LanguageScannerException [ ] exceptions = null ;
    try
    {
      // start with first character
      int offset = 0 ;
      // determine the document content
      String content = getText ( offset , getLength ( ) ) ;
      // allocate the scanner (initially)
      final LanguageScanner scanner = this.language
          .newScanner ( new StringReader ( content ) ) ;
      // collect the tokens returned by the scanner
      final LinkedList < LanguageSymbol > symbols = new LinkedList < LanguageSymbol > ( ) ;
      // determine the tokens for the content
      for ( ; ; )
      {
        try
        {
          // read the next token from the scanner
          LanguageSymbol symbol = scanner.nextSymbol ( ) ;
          if ( symbol == null ) break ;
          // add the token to our list
          symbols.add ( symbol ) ;
          // check if we have an attribute set for the token
          SimpleAttributeSet set = this.attributes.get ( scanner
              .getStyleBySymbol ( symbol ) ) ;
          if ( set == null ) set = this.normalSet ;
          // apply the character attribute set
          setCharacterAttributes ( offset + symbol.getLeft ( ) , symbol
              .getRight ( )
              - symbol.getLeft ( ) , set , true ) ;
        }
        catch ( LanguageScannerException e )
        {
          // calculate the new offset
          int newOffset = offset + e.getRight ( ) ;
          // skip the problematic characters
          content = content.substring ( e.getRight ( ) ) ;
          // adjust the exception according to the offset
          e = new LanguageScannerException ( offset + e.getLeft ( ) , offset
              + e.getRight ( ) , e.getMessage ( ) , e.getCause ( ) ) ;
          // setup the error attribute set
          SimpleAttributeSet errorSet = new SimpleAttributeSet ( ) ;
          StyleConstants.setFontFamily ( errorSet , this.theme.getFont ( )
              .getFamily ( ) ) ;
          StyleConstants.setFontSize ( errorSet , this.theme.getFont ( )
              .getSize ( ) ) ;
          StyleConstants.setForeground ( errorSet , Color.RED ) ;
          StyleConstants.setUnderline ( errorSet , true ) ;
          errorSet.addAttribute ( "exception" , e ) ;
          // apply the error character attribute set to indicate the syntax
          // error
          setCharacterAttributes ( e.getLeft ( ) , e.getRight ( )
              - e.getLeft ( ) , errorSet , false ) ;
          // adjust the offset to point after the error
          offset = newOffset ;
          // restart the scanner after the error
          scanner.restart ( new StringReader ( content ) ) ;
          // add the exception to our list
          if ( exceptions == null )
          {
            exceptions = new LanguageScannerException [ ]
            { e } ;
          }
          else
          {
            LanguageScannerException [ ] newExceptions = new LanguageScannerException [ exceptions.length + 1 ] ;
            System.arraycopy ( exceptions , 0 , newExceptions , 0 ,
                exceptions.length ) ;
            newExceptions [ exceptions.length ] = e ;
            exceptions = newExceptions ;
          }
        }
      }
      // check if the scanner is happy
      if ( exceptions == null )
      {
        // allocate a parser based on a scanner that operates on the previously
        // collected
        // tokens from the scanner step above...
        LanguageParser parser = this.language
            .newParser ( new AbstractLanguageScanner ( )
            {
              public void restart ( Reader reader )
              {
                throw new UnsupportedOperationException ( ) ;
              }


              public LanguageSymbol nextSymbol ( ) throws IOException ,
                  LanguageScannerException
              {
                return ( ! symbols.isEmpty ( ) ) ? symbols.poll ( ) : null ;
              }


              @ Override
              public PrettyStyle getStyleBySymbolId ( int id )
              {
                return ( ( AbstractLanguageScanner ) scanner )
                    .getStyleBySymbolId ( id ) ;
              }
            } ) ;
        // ...and try to parse the token stream
        // TODO
        try
        {
          Expression expression = parser.parse ( ) ;
          // expression.checkDisjunction ( expression ) ;
        }
        catch ( CheckDisjunctionException e )
        {
          int [ ] startOffset = e.startOffset ;
          int [ ] endOffset = e.endOffset ;
          for ( int i = 0 ; i < startOffset.length ; i ++ )
          {
            SimpleAttributeSet errorSet = new SimpleAttributeSet ( ) ;
            StyleConstants.setForeground ( errorSet , Color.RED ) ;
            StyleConstants.setUnderline ( errorSet , true ) ;
            errorSet.addAttribute ( "exception" , e ) ; //$NON-NLS-1$
            setCharacterAttributes ( startOffset [ i ] , endOffset [ i ]
                - startOffset [ i ] , errorSet , false ) ;
          }
          exceptions = new LanguageScannerException [ ]
          { e } ;
        }
        catch ( LanguageParserException e )
        {
          // setup the error attribute set
          SimpleAttributeSet errorSet = new SimpleAttributeSet ( ) ;
          StyleConstants.setForeground ( errorSet , Color.RED ) ;
          StyleConstants.setUnderline ( errorSet , true ) ;
          errorSet.addAttribute ( "exception" , e ) ; //$NON-NLS-1$
          // check if this is unexpected end of file
          if ( e.getLeft ( ) < 0 && e.getRight ( ) < 0 )
          {
            setCharacterAttributes ( getLength ( ) , getLength ( ) , errorSet ,
                false ) ;
          }
          else
          {
            // apply the error character attribute set to indicate the syntax
            // error
            setCharacterAttributes ( e.getLeft ( ) , e.getRight ( )
                - e.getLeft ( ) , errorSet , false ) ;
          }
          // add the exception to our list
          exceptions = new LanguageScannerException [ ]
          { e } ;
        }
      }
    }
    catch ( Exception e )
    {
      logger.warn (
          "Failed to process changes in the styled language document" , e ) ; //$NON-NLS-1$
    }
    // update the exceptions property if necessary
    if ( this.exceptions != exceptions )
    {
      LanguageScannerException [ ] oldExceptions = this.exceptions ;
      this.exceptions = exceptions ;
      firePropertyChange ( "exceptions" , oldExceptions , this.exceptions ) ; //$NON-NLS-1$
    }
  }


}
