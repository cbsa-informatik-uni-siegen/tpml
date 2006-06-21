package ui;

import java.awt.Color;
import java.io.StringReader;
import java.util.HashMap;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import languages.Language;
import languages.LanguageFactory;
import languages.LanguageScanner;
import languages.LanguageScannerException;
import languages.LanguageSymbol;
import expressions.PrettyStyle;

public class MLStyledDocument extends DefaultStyledDocument {
  private SimpleAttributeSet errorSet = new SimpleAttributeSet();
  private SimpleAttributeSet normalSet = new SimpleAttributeSet();
  private HashMap<PrettyStyle, SimpleAttributeSet> attributes = new HashMap<PrettyStyle, SimpleAttributeSet>();
  private static final long serialVersionUID = -1779640687489585648L;
  
  public MLStyledDocument() {
    // setup the error attribute set
    StyleConstants.setForeground(this.errorSet, Color.RED);
    StyleConstants.setUnderline(this.errorSet, true);
    
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
    
    try {
      // start with first character
      int offset = 0;
      
      // determine the document content
      String content = getText(offset, getLength());
      
      // determine the language 
      LanguageFactory languageFactory = LanguageFactory.newInstance();
      Language language = languageFactory.getLanguageById("l1");
    
      // allocate the scanner (initially)
      LanguageScanner scanner = language.newScanner(new StringReader(content));
      
      // determine the tokens for the content
      for (;;) {
        try {
          // read the next token from the scanner
          LanguageSymbol symbol = scanner.nextSymbol();
          if (symbol == null)
            return;
          
          // check if we have an attribute set for the token
          SimpleAttributeSet set = this.attributes.get(scanner.getStyleBySymbol(symbol));
          if (set == null)
            set = this.normalSet;
          
          // apply the character attribute set
          setCharacterAttributes(offset + symbol.getLeft(), symbol.getRight() - symbol.getLeft(), set, true);
        }
        catch (LanguageScannerException e) {
          // apply the error character attribute set to indicate the syntax error
          setCharacterAttributes(offset + e.getLeft(), e.getRight() - e.getLeft(), this.errorSet, true);
          
          // adjust the offset to point after the error
          offset += e.getRight();
          
          // skip the problematic characters
          content = content.substring(e.getRight());
          
          // restart the scanner after the error
          scanner.restart(new StringReader(content));
        }
      }
    }
    catch (Exception e) {
      // FIXME
      e.printStackTrace();
    }
  }

  /*
  private void applyHighlighting(String content, int offsetStart, int offsetEnd) throws BadLocationException {
    StringReader reader = new StringReader(content.substring(offsetStart, offsetEnd - 1));
    Lexer lexer = new Lexer(new PushbackReader(reader, 1024));
    
    try {
      for (;;) {
        try {
          Token token = lexer.next();
          if (token instanceof EOF)
            break;
          
          // determine the token name from the token class
          String tokenName = token.getClass().getSimpleName().substring(1);
          
          // check if we have an attribute set for the token
          SimpleAttributeSet set = this.attributes.get(tokenName);
          
          // fallback to the normal attribute set
          if (set == null)
            set = this.normalSet;
          
          // apply the character attribute set
          setCharacterAttributes(offsetStart + token.getPos() - 1, token.getText().length(), set, true);
        }
        catch (LexerException e) {
          // Parse the lexer exception text: "[<line>,<col>] Unknown token: <token>"
          Pattern pattern = Pattern.compile("^\\[(\\d)+,(\\d+)\\] Unknown token: (.+)$");
          Matcher matcher = pattern.matcher(e.getMessage());
          if (!matcher.find())
            throw e;
          
          // extract position and token
          int pos = Integer.valueOf(matcher.group(2));
          String token = matcher.group(3);
          
          // apply the error character set
          setCharacterAttributes(offsetStart + pos - 1, token.length(), this.errorSet, true);
          
          // setup the lexer to parse the remaining input
          offsetStart += pos + token.length() - 1;
          if (offsetStart >= offsetEnd)
            break;
          reader = new StringReader(content.substring(offsetStart, offsetEnd - 1));
          lexer = new Lexer(new PushbackReader(reader, 1024));
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }*/
}
