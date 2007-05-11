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
import de.unisiegen.tpml.graphics.StyledLanguageDocument;

/**
 * An implementation of the {@link javax.swing.text.StyledDocument} interface to
 * enable syntax highlighting using the lexer of the current
 * {@link de.unisiegen.tpml.core.languages.Language}.
 * 
 * @author Benjamin Mies
 * @see javax.swing.text.DefaultStyledDocument
 */
public class StyledTypeEnterField extends StyledLanguageDocument {

	/**
	 * The unique serialization identifier of this class.
	 */
	private static final long serialVersionUID = 4465272372914939214L;

	/**
	 * Allocates a new <code>StyledTypeEnterDocument</code> for the given
	 * <code>language</code>, where the <code>language</code> is used to
	 * determine the scanner (aka lexer) for the documents content and thereby
	 * dictates the syntax highlighting.
	 * 
	 * @param language the {@link Language} for which to allocate a document.
	 * @throws NullPointerException if the <code>language</code> is
	 *           <code>null</code>.
	 */
	public StyledTypeEnterField(Language language) {
		super ( language );
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
	public MonoType getType() throws Exception {
		return this.language.newTypeParser (
				new StringReader ( getText ( 0, getLength ( ) ) ) ).parse ( );
	}

	/**
	 * Processes the document content after a change.
	 * 
	 * @throws BadLocationException if the processing failed.
	 */
	@SuppressWarnings("null")
	public void processChanged() throws BadLocationException {
		// reset the character attributes
		setCharacterAttributes ( 0, getLength ( ), this.normalSet, true );
		// allocate a list to collect the exceptions
		LanguageScannerException[] exceptions = null;
		try {
			// start with first character
			int offset = 0;
			// determine the document content
			String content = getText ( offset, getLength ( ) );
			// allocate the scanner (initially)
			final LanguageTypeScanner scanner = this.language
					.newTypeScanner ( new StringReader ( content ) );
			// collect the tokens returned by the scanner
			final LinkedList<LanguageSymbol> symbols = new LinkedList<LanguageSymbol> ( );
			// determine the tokens for the content
			for (;;) {
				try {
					// read the next token from the scanner
					LanguageSymbol symbol = scanner.nextSymbol ( );
					if (symbol == null)
						break;
					// add the token to our list
					symbols.add ( symbol );
					// check if we have an attribute set for the token
					SimpleAttributeSet set = this.attributes.get ( scanner
							.getStyleBySymbol ( symbol ) );
					if (set == null)
						set = this.normalSet;
					// apply the character attribute set
					setCharacterAttributes ( offset + symbol.getLeft ( ), symbol
							.getRight ( )
							- symbol.getLeft ( ), set, true );
				} catch (LanguageScannerException e) {
					// calculate the new offset
					int newOffset = offset + e.getRight ( );
					// skip the problematic characters
					content = content.substring ( e.getRight ( ) );
					// adjust the exception according to the offset
					e = new LanguageScannerException ( offset + e.getLeft ( ), offset
							+ e.getRight ( ), e.getMessage ( ), e.getCause ( ) );
					// setup the error attribute set
					SimpleAttributeSet errorSet = new SimpleAttributeSet ( );
					StyleConstants.setFontFamily ( errorSet, this.theme.getFont ( )
							.getFamily ( ) );
					StyleConstants.setFontSize ( errorSet, this.theme.getFont ( )
							.getSize ( ) );
					StyleConstants.setForeground ( errorSet, Color.RED );
					StyleConstants.setUnderline ( errorSet, true );
					errorSet.addAttribute ( "exception", e ); //$NON-NLS-1$
					// apply the error character attribute set to indicate the syntax
					// error
					setCharacterAttributes ( e.getLeft ( ),
							e.getRight ( ) - e.getLeft ( ), errorSet, false );
					// adjust the offset to point after the error
					offset = newOffset;
					// restart the scanner after the error
					scanner.restart ( new StringReader ( content ) );
					// add the exception to our list
					if (exceptions == null) {
						exceptions = new LanguageScannerException[] { e };
					} else {
						LanguageScannerException[] newExceptions = new LanguageScannerException[exceptions.length + 1];
						System.arraycopy ( exceptions, 0, newExceptions, 0,
								exceptions.length );
						newExceptions[exceptions.length] = e;
						exceptions = newExceptions;
					}
				}
			}
			// check if the scanner is happy
			if (exceptions == null) {
				// allocate a parser based on a scanner that operates on the previously
				// collected
				// tokens from the scanner step above...
				LanguageTypeParser parser = this.language
						.newTypeParser ( new AbstractLanguageTypeScanner ( ) {
							public void restart(Reader reader) {
								throw new UnsupportedOperationException ( );
							}

							public LanguageSymbol nextSymbol() throws IOException,
									LanguageScannerException {
								return ( !symbols.isEmpty ( ) ) ? symbols.poll ( ) : null;
							}

							@Override
							public PrettyStyle getStyleBySymbolId(int id) {
								return ( ( AbstractLanguageTypeScanner ) scanner )
										.getStyleBySymbolId ( id );
							}
						} );
				// ...and try to parse the token stream
				try {
					MonoType type = parser.parse ( );
				} catch (LanguageParserMultiException e) {
					String[] message = e.getMessages ( );
					int[] startOffset = e.getParserStartOffset ( );
					int[] endOffset = e.getParserEndOffset ( );
					exceptions = new LanguageParserException[startOffset.length];
					for (int i = 0; i < startOffset.length; i++ ) {
						exceptions[i] = new LanguageParserException ( message[i],
								startOffset[i], endOffset[i] );
						SimpleAttributeSet errorSet = new SimpleAttributeSet ( );
						StyleConstants.setForeground ( errorSet, Color.RED );
						StyleConstants.setUnderline ( errorSet, true );
						errorSet.addAttribute ( "exception", exceptions[i] ); //$NON-NLS-1$
						setCharacterAttributes ( startOffset[i], endOffset[i]
								- startOffset[i], errorSet, false );
					}
				} catch (LanguageParserException e) {
					// setup the error attribute set
					SimpleAttributeSet errorSet = new SimpleAttributeSet ( );
					StyleConstants.setForeground ( errorSet, Color.RED );
					StyleConstants.setUnderline ( errorSet, true );
					errorSet.addAttribute ( "exception", e ); //$NON-NLS-1$
					// check if this is unexpected end of file
					if (e.getLeft ( ) < 0 && e.getRight ( ) < 0) {
						setCharacterAttributes ( getLength ( ), getLength ( ), errorSet,
								false );
					} else {
						// apply the error character attribute set to indicate the syntax
						// error
						setCharacterAttributes ( e.getLeft ( ), e.getRight ( )
								- e.getLeft ( ), errorSet, false );
					}
					// add the exception to our list
					exceptions = new LanguageScannerException[] { e };
				}
			}
		} catch (Exception e) {
			logger.warn (
					"Failed to process changes in the styled language document", e ); //$NON-NLS-1$
		}
		// update the exceptions property if necessary
		if (this.exceptions != exceptions) {
			LanguageScannerException[] oldExceptions = this.exceptions;
			this.exceptions = exceptions;
			firePropertyChange ( "exceptions", oldExceptions, this.exceptions ); //$NON-NLS-1$
		}
	}

	/**
	 * set the actual language for this styled document
	 *
	 * @param language Language
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

}
