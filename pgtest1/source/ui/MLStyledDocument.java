package ui;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.StringReader;
import java.util.HashMap;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import common.prettyprinter.PrettyStyle;

import languages.Language;
import languages.LanguageFactory;
import languages.LanguageScanner;
import languages.LanguageScannerException;
import languages.LanguageSymbol;
import languages.NoSuchLanguageException;
import expressions.Expression;

/**
 * 
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class MLStyledDocument extends DefaultStyledDocument {
  //
  // Constants
  //
  
  /**
   * Empty array of language exceptions.
   */
  private static final LanguageScannerException[] EMPTY_ARRAY = new LanguageScannerException[0];
  
  
  
  //
  // Attributes
  //

  /**
   * If any <code>PropertyChangeListeners</code> have been registered,
   * the <code>changeSupport</code> field describes them.
   *
   * @serial
   * @see #addPropertyChangeListener(PropertyChangeListener)
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   * @see #removePropertyChangeListener(PropertyChangeListener)
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   * @see #firePropertyChange(String, boolean, boolean)
   * @see #firePropertyChange(String, int, int)
   * @see #firePropertyChange(String, Object, Object)
   */
  protected PropertyChangeSupport changeSupport;
  
  private Language language;
  private LanguageScannerException exceptions[];
  private SimpleAttributeSet normalSet = new SimpleAttributeSet();
  private HashMap<PrettyStyle, SimpleAttributeSet> attributes = new HashMap<PrettyStyle, SimpleAttributeSet>();
  private static final long serialVersionUID = -1779640687489585648L;
  
  
  
  //
  // Constructor
  //
  
  /**
   * 
   */
  public MLStyledDocument() {
    try {
      // try to determine the language
      LanguageFactory languageFactory = LanguageFactory.newInstance();
      this.language = languageFactory.getLanguageById("l1");
    }
    catch (NoSuchLanguageException e) {
      throw new RuntimeException(e);
    }
  
    // setup the normal attribute set
    StyleConstants.setForeground(this.normalSet, Color.BLACK);
    StyleConstants.setBold(this.normalSet, false);

    // setup the comment set
    SimpleAttributeSet commentSet = new SimpleAttributeSet();
    StyleConstants.setForeground(commentSet, new Color(0.0f, 0.6f, 0.0f));
    StyleConstants.setItalic(commentSet, true);
    this.attributes.put(PrettyStyle.COMMENT, commentSet);

    // setup the constant set
    SimpleAttributeSet constantSet = new SimpleAttributeSet();
    StyleConstants.setForeground(constantSet, new Color(0.0f, 0.0f, 0.6f));
    StyleConstants.setBold(constantSet, true);
    this.attributes.put(PrettyStyle.CONSTANT, constantSet);

    // setup the keyword set
    SimpleAttributeSet keywordSet = new SimpleAttributeSet();
    StyleConstants.setForeground(keywordSet, new Color(0.6f, 0.0f, 0.0f));
    StyleConstants.setBold(keywordSet, true);
    this.attributes.put(PrettyStyle.KEYWORD, keywordSet);
  }

  

  //
  // Accessors
  //
  
  /**
   * Returns the current {@link LanguageScannerException}s that
   * were detected while trying to interpret the token stream.
   * 
   * @return the exceptions.
   */
  public LanguageScannerException[] getExceptions() {
    return (this.exceptions != null) ? this.exceptions : EMPTY_ARRAY;
  }
  
  /**
   * Returns the exception at the given <code>index</code>.
   * 
   * @param index the index of the exception to return.
   * 
   * @return the exception at the <code>index</code>.
   * 
   * @throws ArrayIndexOutOfBoundsException if the <code>index</code>
   *                                        is out of bounds.
   */
  public LanguageScannerException getExceptions(int index) {
    return getExceptions()[index];
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the {@link Expression} for the program text within
   * this document. Throws an exception if a parsing error occurred.
   *  
   * @return the {@link Expression} for the program text.
   * 
   * @throws Exception  
   */
  public Expression getExpression() throws Exception {
    LanguageFactory languageFactory = LanguageFactory.newInstance();
    Language language = languageFactory.getLanguageById("l1");
    return language.newParser(new StringReader(getText(0, getLength()))).parse();
  }
  
  
  
  //
  // Change handling
  //
  
  public void insertString(int offset, String str, AttributeSet set) throws BadLocationException {
    super.insertString(offset, str, set);
    processChanged();
  }
  
  public void remove(int offset, int length) throws BadLocationException {
    super.remove(offset, length);
    processChanged();
  }
  
  public void processChanged() throws BadLocationException {
    // reset the character attributes
    setCharacterAttributes(0, getLength(), this.normalSet, true);
    
    // allocate a list to collect the exceptions
    LanguageScannerException[] exceptions = null;

    try {
      // start with first character
      int offset = 0;
      
      // determine the document content
      String content = getText(offset, getLength());
      
      // allocate the scanner (initially)
      LanguageScanner scanner = this.language.newScanner(new StringReader(content));
      
      // determine the tokens for the content
      for (;;) {
        try {
          // read the next token from the scanner
          LanguageSymbol symbol = scanner.nextSymbol();
          if (symbol == null)
            break;
          
          // check if we have an attribute set for the token
          SimpleAttributeSet set = this.attributes.get(scanner.getStyleBySymbol(symbol));
          if (set == null)
            set = this.normalSet;
          
          // apply the character attribute set
          setCharacterAttributes(offset + symbol.getLeft(), symbol.getRight() - symbol.getLeft(), set, true);
          
          // add the token to the token list (skip comments)
          /*FIXME:PARSER:if (scanner.getStyleBySymbol(symbol) != PrettyStyle.COMMENT)
            this.symbols.add(symbol);*/
        }
        catch (LanguageScannerException e) {
          // setup the error attribute set
          SimpleAttributeSet errorSet = new SimpleAttributeSet();
          StyleConstants.setForeground(errorSet, Color.RED);
          StyleConstants.setUnderline(errorSet, true);
          errorSet.addAttribute("exception", e);
          
          // apply the error character attribute set to indicate the syntax error
          setCharacterAttributes(offset + e.getLeft(), e.getRight() - e.getLeft(), errorSet, false);
          
          // adjust the offset to point after the error
          offset += e.getRight();
          
          // skip the problematic characters
          content = content.substring(e.getRight());
          
          // restart the scanner after the error
          scanner.restart(new StringReader(content));
          
          // add the exception to our list
          if (exceptions == null) {
            exceptions = new LanguageScannerException[] { e };
          }
          else {
            LanguageScannerException[] newExceptions = new LanguageScannerException[exceptions.length + 1];
            System.arraycopy(exceptions, 0, newExceptions, 0, exceptions.length);
            newExceptions[exceptions.length] = e;
            exceptions = newExceptions;
          }
        }
      }
      
      /*FIXME:PARSER// check if we completed without errors
      if (numErrors == 0) {
        try {
          // try to parse the generated tokens
          LanguageParser parser = this.language.newParser(new TokenStream(this.symbols));
          parser.parse();
        }
        catch (LanguageParserException e) {
          // setup the error attribute set
          SimpleAttributeSet errorSet = new SimpleAttributeSet();
          StyleConstants.setForeground(errorSet, Color.RED);
          StyleConstants.setUnderline(errorSet, true);
          errorSet.addAttribute("exception", e);
          
          // determine the error offset and the length
          int errorOffset = (e.getLeft() < 0) ? getLength() - 1 : e.getLeft();
          int errorLength = (e.getRight() < 0) ? 1 : (e.getRight() - errorOffset);
          
          // apply the error character attribute set to indicate the syntax error
          setCharacterAttributes(errorOffset, errorLength, errorSet, false);
        }
      }*/
    }
    catch (Exception e) {
      // FIXME
      e.printStackTrace();
    }
    
    // update the exceptions property if necessary
    if (this.exceptions != exceptions) {
      LanguageScannerException[] oldExceptions = this.exceptions;
      this.exceptions = exceptions;
      firePropertyChange("exceptions", oldExceptions, this.exceptions);
    }
  }

  
  
  //
  // Inner classes
  //
  
  /**
   * TODO
   */
  /*FIXME:PARSER:private static final class TokenStream extends AbstractLanguageScanner {
    private Iterator<LanguageSymbol> iterator;

    TokenStream(LinkedList<LanguageSymbol> symbols) {
      this.iterator = symbols.iterator();
    }
    
    @Override
    protected PrettyStyle getStyleBySymbolId(int id) {
      return PrettyStyle.NONE;
    }

    public LanguageSymbol nextSymbol() throws IOException, LanguageScannerException {
      return this.iterator.hasNext() ? this.iterator.next() : null;
    }
    
    public void restart(Reader reader) {
      throw new UnsupportedOperationException("not supported");
    }
  }*/



  //
  // Listener registration
  //
  
  /**
   * Adds a {@link PropertyChangeListener} to the listener list. The listener
   * is registered for all bound properties of the derived class.
   * 
   * If <code>listener</code> is <code>null</code>, no exception is thrown
   * and no action is performed.
   * 
   * @param listener the {@link PropertyChangeListener} to be added.
   * 
   * @see #getPropertyChangeListeners()
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (this.changeSupport == null) {
      this.changeSupport = new PropertyChangeSupport(this);
    }
    this.changeSupport.addPropertyChangeListener(listener);
  }
  
  /**
   * Removes a {@link PropertyChangeListener} from the listener list. This method
   * should be used to remove PropertyChangeListeners that were registered for all
   * bound properties of this class.
   * 
   * If <code>listener</code> is <code>null</code>, no exception is thrown and
   * no action is performed.
   * 
   * @param listener the {@link PropertyChangeListener} to be removed.
   * 
   * @see #addPropertyChangeListener(PropertyChangeListener)
   * @see #getPropertyChangeListeners()
   */
  public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    if (listener == null || this.changeSupport == null) {
      return;
    }
    this.changeSupport.removePropertyChangeListener(listener);
  }
  
  /**
   * Returns an array of all the property change listeners registered
   * on this object.
   * 
   * @return all of this object's {@link PropertyChangeListener}s or an
   *         empty array if no property change listeners are currently
   *         registered.
   *
   * @see #addPropertyChangeListener(PropertyChangeListener)
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
    if (this.changeSupport == null) {
      return new PropertyChangeListener[0];
    }
    return this.changeSupport.getPropertyChangeListeners();
  }
  
  /**
   * Adds a {@link PropertyChangeListener} to the listener list for a specific
   * property. The specified property may be user-defined, or one of the properties
   * provided by the object.
   * 
   * If <code>listener</code> is <code>null</code>, no exception is thrown and
   * no action is performed.
   * 
   * @param propertyName one of the property names of the object.
   * @param listener the {@link PropertyChangeListener} to be added.
   * 
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners(String)
   */
  public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (this.changeSupport == null) {
      this.changeSupport = new PropertyChangeSupport(this);
    }
    this.changeSupport.addPropertyChangeListener(propertyName, listener);
  }
  
  /**
   * Removes a {@link PropertyChangeListener} from the listener list for a specific
   * property. This method should be used to remove {@link PropertyChangeListener}s
   * that were registered for a specific bound property.
   *
   * If <code>listener</code> is <code>null</code>, no exception is thrown and no
   * action is performed.
   * 
   * @param propertyName a valid property name.
   * @param listener the {@link PropertyChangeListener} to be removed.
   * 
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners(String)
   */
  public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    if (listener == null || this.changeSupport == null) {
      return;
    }
    this.changeSupport.removePropertyChangeListener(propertyName, listener);
  }
  
  /**
   * Returns an array of all the listeners which have been associated 
   * with the named property.
   *
   * @param propertyName a valid property name.
   * 
   * @return all of the {@link PropertyChangeListeners} associated with
   *         the named property or an empty array if no listeners have 
   *         been added
   */
  public synchronized PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
    if (this.changeSupport == null) {
      return new PropertyChangeListener[0];
    }
    return this.changeSupport.getPropertyChangeListeners(propertyName);
  }
  
  
  
  //
  // Listener invocation
  //
  
  /**
   * Support for reporting bound property changes for Object properties. 
   * This method can be called when a bound property has changed and it will
   * send the appropriate {@link PropertyChangeEvent} to any registered
   * {@link PropertyChangeListener}s.
   *
   * @param propertyName the property whose value has changed.
   * @param oldValue the property's previous value.
   * @param newValue the property's new value.
   */
  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeSupport changeSupport = this.changeSupport;
    if (changeSupport == null) {
      return;
    }
    changeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Support for reporting bound property changes for boolean properties. 
   * This method can be called when a bound property has changed and it will
   * send the appropriate {@link PropertyChangeEvent} to any registered
   * {@link PropertyChangeListener}s.
   *
   * @param propertyName the propery whose value has changed.
   * @param oldValue the property's previous value.
   * @param newValue the property's new value.
   */
  protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    PropertyChangeSupport changeSupport = this.changeSupport;
    if (changeSupport == null) {
      return;
    }
    changeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }
  
  /**
   * Support for reporting bound property changes for boolean properties. 
   * This method can be called when a bound property has changed and it will
   * send the appropriate {@link PropertyChangeEvent} to any registered
   * {@link PropertyChangeListener}s.
   *
   * @param propertyName the propery whose value has changed.
   * @param oldValue the property's previous value.
   * @param newValue the property's new value.
   */
  protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
    PropertyChangeSupport changeSupport = this.changeSupport;
    if (changeSupport == null) {
      return;
    }
    changeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }
}
