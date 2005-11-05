package ui;

import java.awt.Color;
import javax.swing.text.*;

public class MLStyledDocument extends DefaultStyledDocument {

	private SimpleAttributeSet normal;
	private SimpleAttributeSet keyword;
	
	public MLStyledDocument ()
	{
		Color keywordColor = new Color (0.6f, 0.0f, 0.0f);

		normal = new SimpleAttributeSet ();
		StyleConstants.setForeground (normal, Color.black);
		StyleConstants.setBold (normal, false);
		
		keyword = new SimpleAttributeSet ();
		StyleConstants.setForeground (keyword, keywordColor);
		StyleConstants.setBold (keyword, true);
		
		System.out.println ("MLStyledDocument");
	}
	
	public void insertString (int offset, String str, AttributeSet set)
	throws BadLocationException
	{
		super.insertString (offset, str, set);
		processChanged (offset, str.length ());
	}
	
	public void remove (int offset, int length)
	throws BadLocationException
	{
		super.remove (offset, length);
		processChanged (offset, length);
	}
	
	public void processChanged (int offset, int length)
	throws BadLocationException
	{
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
	
	public void applyHighlighting (String content, int offsetStart, 
			int offsetEnd)
	throws BadLocationException
	{
		if (offsetEnd > content.length ())
			offsetEnd = content.length ();
		
		int sequenceLength = offsetEnd - offsetStart;
		setCharacterAttributes (offsetStart, sequenceLength, normal, true);
		
		int startIndex = offsetStart;
		int endIndex = offsetStart;
		String checkString = "";
		
		while (endIndex < offsetEnd)
		{
			char ch = content.charAt (endIndex);
			if (isSplitter (ch))
			{
				if (isKeyword (checkString))
					setCharacterAttributes (
							endIndex - checkString.length(),
							checkString.length(), keyword, true);
				++endIndex;				
				startIndex = endIndex;
				checkString = "";
			}
			else
			{
				checkString += ch;
				endIndex++;
			}
		}
		if (isKeyword (checkString))
		{
			setCharacterAttributes (
					endIndex - checkString.length(),
					checkString.length (), keyword, true);
		}
		/*
		//--endIndex;
		String subString = "";
		try {
			subString = content.substring (startIndex, endIndex);
		}
		catch (Exception e)
		{
		}
		if (isKeyword (subString))
			setCharacterAttributes (startIndex,	endIndex-startIndex, 
					keyword, true);
		*/
		
	}
	
	boolean isSplitter (char character)
	{
		String splitCharacter = "+-*/=(),.:;<>{}[]";
		return (Character.isWhitespace (character) ||
				splitCharacter.indexOf (character) != -1);
	}
	boolean isKeyword (String word)
	{
		return (word.equals ("let")
			|| word.equals ("in")
			|| word.equals ("if")
			|| word.equals ("then")
			|| word.equals ("else")
			|| word.equals ("int")
			|| word.equals ("bool")
			|| word.equals ("unit")
			|| word.equals ("true")
			|| word.equals ("false")
			|| word.equals ("lambda"));
	}
}
