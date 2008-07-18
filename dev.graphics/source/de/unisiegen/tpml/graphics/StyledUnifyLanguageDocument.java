package de.unisiegen.tpml.graphics;


import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.languages.AbstractLanguageScanner;
import de.unisiegen.tpml.core.languages.AbstractLanguageUnifyScanner;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageScannerException;
import de.unisiegen.tpml.core.languages.LanguageSymbol;
import de.unisiegen.tpml.core.languages.LanguageUnifyParser;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeName;
import de.unisiegen.tpml.core.util.Theme;
import de.unisiegen.tpml.core.util.beans.Bean;


/**
 * An implementation of the {@link javax.swing.text.StyledDocument} interface to
 * enable syntax highlighting using the lexer of the current
 * {@link de.unisiegen.tpml.core.languages.Language}.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see javax.swing.text.DefaultStyledDocument
 */
public class StyledUnifyLanguageDocument extends StyledLanguageDocument
{
  /**
   * TODO
   */
  private static final long serialVersionUID = -789052711165268877L;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>StyledLanguageDocument</code> for the given
   * <code>language</code>, where the <code>language</code> is used to
   * determine the scanner (aka lexer) for the documents content and thereby
   * dictates the syntax highlighting.
   * 
   * @param pLanguage the {@link Language} for which to allocate a document.
   * @throws NullPointerException if the <code>language</code> is
   *           <code>null</code>.
   */
  public StyledUnifyLanguageDocument ( Language pLanguage )
  {
    super(pLanguage);
  }


  //
  // Primitives
  //
  /**
   * Returns the {@link TypeEquationList} for the program text within this
   * document. Throws an exception if a parsing error occurred.
   * 
   * @return the {@link TypeEquationList} for the program text.
   * @throws Exception
   */
  public TypeEquationList getTypeEquation () throws Exception
  {
    return this.language.newUnifyParser (
        new StringReader ( getText ( 0, getLength () ) ) ).parse ();
  }


  /**
   * Processes the document content after a change.
   * 
   * @throws BadLocationException if the processing failed.
   */
  @Override
  @SuppressWarnings (
  { "unused", "null" } )
  public void processChanged () throws BadLocationException
  {
    // reset the character attributes
    setCharacterAttributes ( 0, getLength (), this.normalSet, true );
    // allocate a list to collect the exceptions
    LanguageScannerException [] tmpExceptions = null;
    try
    {
      // start with first character
      int offset = 0;
      // determine the document content
      String content = getText ( offset, getLength () );
      // allocate the scanner (initially)
      final LanguageScanner scanner = this.language
          .newUnifyScanner ( new StringReader ( content ) );
      // collect the tokens returned by the scanner
      final LinkedList < LanguageSymbol > symbols = new LinkedList < LanguageSymbol > ();
      // determine the tokens for the content
      while ( true )
      {
        try
        {
          // read the next token from the scanner
          LanguageSymbol symbol = scanner.nextSymbol ();
          if ( symbol == null )
          {
            break;
          }
          // add the token to our list
          symbols.add ( symbol );
          // check if we have an attribute set for the token
          SimpleAttributeSet set = this.attributes.get ( scanner
              .getStyleBySymbol ( symbol ) );
          if ( set == null )
          {
            set = this.normalSet;
          }
          // apply the character attribute set
          setCharacterAttributes ( offset + symbol.getLeft (), symbol
              .getRight ()
              - symbol.getLeft (), set, true );
        }
        catch ( LanguageScannerException e )
        {
          // calculate the new offset
          int newOffset = offset + e.getRight ();
          // skip the problematic characters
          content = content.substring ( e.getRight () );
          // adjust the exception according to the offset
          e = new LanguageScannerException ( offset + e.getLeft (), offset
              + e.getRight (), e.getMessage (), e.getCause () );
          // setup the error attribute set
          SimpleAttributeSet errorSet = new SimpleAttributeSet ();
          StyleConstants.setFontFamily ( errorSet, this.theme.getFont ()
              .getFamily () );
          StyleConstants.setFontSize ( errorSet, this.theme.getFont ()
              .getSize () );
          StyleConstants.setForeground ( errorSet, Color.RED );
          StyleConstants.setUnderline ( errorSet, true );
          errorSet.addAttribute ( "exception", e ); //$NON-NLS-1$
          // apply the error character attribute set to indicate the syntax
          // error
          setCharacterAttributes ( e.getLeft (), e.getRight () - e.getLeft (),
              errorSet, false );
          // adjust the offset to point after the error
          offset = newOffset;
          // restart the scanner after the error
          scanner.restart ( new StringReader ( content ) );
          // add the exception to our list
          if ( tmpExceptions == null )
          {
            tmpExceptions = new LanguageScannerException []
            { e };
          }
          else
          {
            LanguageScannerException [] newExceptions = new LanguageScannerException [ tmpExceptions.length + 1 ];
            System.arraycopy ( tmpExceptions, 0, newExceptions, 0,
                tmpExceptions.length );
            newExceptions [ tmpExceptions.length ] = e;
            tmpExceptions = newExceptions;
          }
        }
      }
      // Parse only if the scanner is happy
      if ( tmpExceptions == null )
      {
        // allocate a parser based on a scanner that operates on the previously
        // collected
        // tokens from the scanner step above...
        LanguageUnifyParser parser = this.language
            .newUnifyParser ( new AbstractLanguageUnifyScanner ()
            {

              public void restart ( Reader reader )
              {
                throw new UnsupportedOperationException ();
              }


              public LanguageSymbol nextSymbol () throws IOException,
                  LanguageScannerException
              {
                return ( !symbols.isEmpty () ) ? symbols.poll () : null;
              }


              @Override
              public PrettyStyle getStyleBySymbolId ( int id )
              {
                return ( ( AbstractLanguageScanner ) scanner )
                    .getStyleBySymbolId ( id );
              }
            } );

        TypeEquationList equations = parser.parse ();
        for ( TypeEquation eqn : equations )
        {
          MonoType left = eqn.getLeft ();
          MonoType right = eqn.getRight ();
          
          for ( TypeName typeName : left.getTypeNamesFree () )
          {
            SimpleAttributeSet freeSet = new SimpleAttributeSet ();
            StyleConstants.setForeground ( freeSet, Theme.currentTheme ()
                .getFreeIdColor () );
            StyleConstants.setBold ( freeSet, true );
            freeSet.addAttribute ( "Free TypeName", "Free TypeName" ); //$NON-NLS-1$ //$NON-NLS-2$
            setCharacterAttributes ( typeName.getParserStartOffset (), typeName
                .getParserEndOffset ()
                - typeName.getParserStartOffset (), freeSet, false );
          }
          
          for ( TypeName typeName : right.getTypeNamesFree () )
          {
            SimpleAttributeSet freeSet = new SimpleAttributeSet ();
            StyleConstants.setForeground ( freeSet, Theme.currentTheme ()
                .getFreeIdColor () );
            StyleConstants.setBold ( freeSet, true );
            freeSet.addAttribute ( "Free TypeName", "Free TypeName" ); //$NON-NLS-1$ //$NON-NLS-2$
            setCharacterAttributes ( typeName.getParserStartOffset (), typeName
                .getParserEndOffset ()
                - typeName.getParserStartOffset (), freeSet, false );
          }
        }
      }
    }
    catch ( Exception e )
    {
      logger.warn (
          "Failed to process changes in the styled language document", e ); //$NON-NLS-1$
    }
    // update the exceptions property if necessary
    if ( this.exceptions != tmpExceptions )
    {
      LanguageScannerException [] oldExceptions = this.exceptions;
      this.exceptions = tmpExceptions;
      firePropertyChange ( "exceptions", oldExceptions, this.exceptions ); //$NON-NLS-1$
    }

  }
}
