package de.unisiegen.tpml.graphics.subtyping;


import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException;
import de.unisiegen.tpml.core.exceptions.LanguageParserReplaceException;
import de.unisiegen.tpml.core.exceptions.LanguageParserWarningException;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.AbstractLanguageTypeScanner;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageScannerException;
import de.unisiegen.tpml.core.languages.LanguageSymbol;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.languages.LanguageTypeScanner;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeName;
import de.unisiegen.tpml.core.util.Theme;
import de.unisiegen.tpml.graphics.StyledLanguageDocument;


/**
 * An implementation of the {@link javax.swing.text.StyledDocument} interface to
 * enable syntax highlighting using the lexer of the current
 * {@link de.unisiegen.tpml.core.languages.Language}.
 * 
 * @author Benjamin Mies
 * @version $Id$
 * @see javax.swing.text.DefaultStyledDocument
 */
public class StyledTypeEnterField extends StyledLanguageDocument
{

  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = 4465272372914939214L;


  /**
   * Allocates a new <code>StyledTypeEnterDocument</code> for the given
   * <code>language</code>, where the <code>language</code> is used to determine
   * the scanner (aka lexer) for the documents content and thereby dictates the
   * syntax highlighting.
   * 
   * @param pLanguage the {@link Language} for which to allocate a document.
   * @throws NullPointerException if the <code>language</code> is
   *           <code>null</code>.
   */
  public StyledTypeEnterField ( Language pLanguage )
  {
    super ( pLanguage );
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
  public MonoType getType () throws Exception
  {
    try
    {
      return this.language.newTypeParser (
          new StringReader ( getText ( 0, getLength () ) ) ).parse ();
    }
    catch ( LanguageParserException e )
    {
      return null;
    }
  }


  /**
   * Processes the document content after a change.
   * 
   * @throws BadLocationException if the processing failed.
   */
  @Override
  @SuppressWarnings (
  { "unused" } )
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
      final LanguageTypeScanner scanner = this.language
          .newTypeScanner ( new StringReader ( content ) );
      // collect the tokens returned by the scanner
      final LinkedList < LanguageSymbol > symbols = new LinkedList < LanguageSymbol > ();
      // determine the tokens for the content
      for ( ; ; )
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
        LanguageTypeParser parser = this.language
            .newTypeParser ( new AbstractLanguageTypeScanner ()
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
                return ( ( AbstractLanguageTypeScanner ) scanner )
                    .getStyleBySymbolId ( id );
              }
            } );
        // ...and try to parse the token stream
        try
        {
          MonoType type = parser.parse ();
          for ( TypeName typeName : type.getTypeNamesFree () )
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
        catch ( LanguageParserReplaceException e )
        {
          String [] messageRename = e.getMessagesReplace ();
          int [] startOffsetRename = e.getParserStartOffsetReplace ();
          int [] endOffsetRename = e.getParserEndOffsetReplace ();
          String [] messageNegative = e.getMessagesNegative ();
          int [] startOffsetNegative = e.getParserStartOffsetNegative ();
          int [] endOffsetNegative = e.getParserEndOffsetNegative ();
          tmpExceptions = new LanguageParserException [ startOffsetRename.length
              + startOffsetNegative.length ];
          for ( int i = 0 ; i < startOffsetRename.length ; i++ )
          {
            tmpExceptions [ i ] = new LanguageParserReplaceException (
                messageRename [ i ], startOffsetRename [ i ],
                endOffsetRename [ i ], e.getReplaceText () );
            SimpleAttributeSet errorSet = new SimpleAttributeSet ();
            StyleConstants.setForeground ( errorSet, Color.BLUE );
            StyleConstants.setUnderline ( errorSet, true );
            errorSet.addAttribute ( "exception", tmpExceptions [ i ] ); //$NON-NLS-1$
            setCharacterAttributes ( startOffsetRename [ i ],
                endOffsetRename [ i ] - startOffsetRename [ i ], errorSet,
                false );
          }
          for ( int i = 0 ; i < startOffsetNegative.length ; i++ )
          {
            tmpExceptions [ startOffsetRename.length + i ] = new LanguageParserException (
                messageNegative [ i ], startOffsetNegative [ i ],
                endOffsetNegative [ i ] );
            SimpleAttributeSet errorSet = new SimpleAttributeSet ();
            StyleConstants.setForeground ( errorSet, Color.RED );
            StyleConstants.setUnderline ( errorSet, true );
            errorSet.addAttribute (
                "exception", tmpExceptions [ startOffsetRename.length + i ] ); //$NON-NLS-1$
            setCharacterAttributes ( startOffsetNegative [ i ],
                endOffsetNegative [ i ] - startOffsetNegative [ i ], errorSet,
                false );
          }
        }
        catch ( LanguageParserMultiException e )
        {
          String [] message = e.getMessages ();
          int [] startOffset = e.getParserStartOffset ();
          int [] endOffset = e.getParserEndOffset ();
          tmpExceptions = new LanguageParserException [ startOffset.length ];
          for ( int i = 0 ; i < startOffset.length ; i++ )
          {
            tmpExceptions [ i ] = new LanguageParserException ( message [ i ],
                startOffset [ i ], endOffset [ i ] );
            SimpleAttributeSet errorSet = new SimpleAttributeSet ();
            StyleConstants.setForeground ( errorSet, Color.RED );
            StyleConstants.setUnderline ( errorSet, true );
            errorSet.addAttribute ( "exception", tmpExceptions [ i ] ); //$NON-NLS-1$
            setCharacterAttributes ( startOffset [ i ], endOffset [ i ]
                - startOffset [ i ], errorSet, false );
          }
        }
        catch ( LanguageParserWarningException e )
        {
          // setup the warning attribute set
          SimpleAttributeSet errorSet = new SimpleAttributeSet ();
          StyleConstants.setBackground ( errorSet, Theme.currentTheme ()
              .getParserWarningColor () );
          errorSet.addAttribute ( "warning", e ); //$NON-NLS-1$
          // check if this is unexpected end of file
          if ( ( e.getLeft () < 0 ) && ( e.getRight () < 0 ) )
          {
            setCharacterAttributes ( getLength (), getLength (), errorSet,
                false );
          }
          else
          {
            // apply the error character attribute set to indicate the syntax
            // error
            setCharacterAttributes ( e.getLeft (),
                e.getRight () - e.getLeft (), errorSet, false );
          }
          // add the exception to our list
          if ( tmpExceptions == null )
          {
            tmpExceptions = new LanguageScannerException []
            { new LanguageParserWarningException ( e.getMessage (), e
                .getRight (), e.getRight (), e.getInsertText () ) };
          }
          else
          {
            LanguageScannerException [] newExceptions = new LanguageScannerException [ tmpExceptions.length + 1 ];
            System.arraycopy ( tmpExceptions, 0, newExceptions, 0,
                tmpExceptions.length );
            newExceptions [ tmpExceptions.length ] = new LanguageParserWarningException (
                e.getMessage (), e.getRight (), e.getRight (), e
                    .getInsertText () );
            tmpExceptions = newExceptions;
          }
        }
        catch ( LanguageParserException e )
        {
          // setup the error attribute set
          SimpleAttributeSet errorSet = new SimpleAttributeSet ();
          StyleConstants.setForeground ( errorSet, Color.RED );
          StyleConstants.setUnderline ( errorSet, true );
          errorSet.addAttribute ( "exception", e ); //$NON-NLS-1$
          // check if this is unexpected end of file
          if ( ( e.getLeft () < 0 ) && ( e.getRight () < 0 ) )
          {
            setCharacterAttributes ( getLength (), getLength (), errorSet,
                false );
          }
          else
          {
            // apply the error character attribute set to indicate the syntax
            // error
            setCharacterAttributes ( e.getLeft (),
                e.getRight () - e.getLeft (), errorSet, false );
          }
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


  /**
   * set the actual language for this styled document
   * 
   * @param pLanguage Language
   */
  public void setLanguage ( Language pLanguage )
  {
    this.language = pLanguage;
  }
}
