package ui;

import java.awt.Color;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.text.*;

import l1.lexer.*;
import l1.node.*;

public class MLStyledDocument extends DefaultStyledDocument {
  private SimpleAttributeSet errorSet = new SimpleAttributeSet(); 
  private SimpleAttributeSet normalSet = new SimpleAttributeSet(); 
  private HashMap<String, SimpleAttributeSet> attributes = new HashMap<String, SimpleAttributeSet>();
  private static final long serialVersionUID = -1779640687489585648L;
  
  public MLStyledDocument() {
    // setup the error attribute set
    StyleConstants.setForeground(this.errorSet, Color.RED);
    StyleConstants.setUnderline(this.errorSet, true);
    
    // setup the normal attribute set
    StyleConstants.setForeground(this.normalSet, Color.BLACK);
    StyleConstants.setBold(this.normalSet, false);

    // setup the keyword set
    SimpleAttributeSet keywordSet = new SimpleAttributeSet();
    StyleConstants.setForeground(keywordSet, new Color(0.6f, 0.0f, 0.0f));
    StyleConstants.setBold(keywordSet, true);
    
    this.attributes.put("Lambda", keywordSet);
    this.attributes.put("Let", keywordSet);
    this.attributes.put("In", keywordSet);
    this.attributes.put("If", keywordSet);
    this.attributes.put("Then", keywordSet);
    this.attributes.put("Else", keywordSet);
    this.attributes.put("Rec", keywordSet);
    this.attributes.put("Projection", keywordSet);
    this.attributes.put("Fst", keywordSet);
    this.attributes.put("Snd", keywordSet);
    
    // setup the value set
    SimpleAttributeSet valueSet = new SimpleAttributeSet();
    StyleConstants.setForeground(valueSet, new Color(0.0f, 0.0f, 0.6f));
    StyleConstants.setBold(valueSet, true);
    
    this.attributes.put("False", valueSet);
    this.attributes.put("Number", valueSet);
    this.attributes.put("True", valueSet);
    this.attributes.put("Unit", valueSet);
  }
  
  public void insertString (int offset, String str, AttributeSet set)
      throws BadLocationException {
    super.insertString (offset, str, set);
    processChanged (offset, str.length ());
  }
  
  public void remove (int offset, int length)
      throws BadLocationException {
    super.remove (offset, length);
    processChanged (offset, length);
  }
  
  public void processChanged (int offset, int length)
      throws BadLocationException {
    String content = getText(0, getLength());
    
    Element root = getDefaultRootElement();
    int startLine = root.getElementIndex( offset );
    int endLine = root.getElementIndex( offset + length );
    
    for (int i = startLine; i <= endLine; i++)
    {
      int startOffset = root.getElement( i ).getStartOffset();
      int endOffset = root.getElement( i ).getEndOffset();
      applyHighlighting(content, startOffset, endOffset);
    }
  }
  
  private void applyHighlighting (String content, int offsetStart, int offsetEnd)
      throws BadLocationException {
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
  }
}
