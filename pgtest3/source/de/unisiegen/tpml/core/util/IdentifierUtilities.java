package de.unisiegen.tpml.core.util;

import java.util.ArrayList;
import java.util.Enumeration;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;

public final class IdentifierUtilities
{
	
	private IdentifierUtilities()
	{}
	
	public static ArrayList<IdentifierListItem> getIdentifierPositions(Expression pExpression)
	{
		
		ArrayList<IdentifierListItem> list= new ArrayList<IdentifierListItem>();
		
		 PrettyCharIterator prettyCharIterator = pExpression.toPrettyString ( )
     .toCharacterIterator ( ) ;
		
		 int idCount=0;
		 int charIndex = 0 ;
		 int end= pExpression.toString().length();
		 boolean found=false;
		 
		 
		 Enumeration<Expression> children= pExpression.children();
		 
		 if (children.hasMoreElements())
		 {
			 Expression e =  children.nextElement();
			 PrettyString ps1 = pExpression.toPrettyString();
			 PrettyAnnotation mark1 = ps1.getAnnotationForPrintable(e);
			 end=mark1.getStartOffset();
				
				
		 }
		 
		 
		
	    while ( charIndex < end )
	    {
	    	
	    	String tmp="";
	    	int length=0;
	    	while ( prettyCharIterator.getStyle ( ) == PrettyStyle.IDENTIFIER )
		 		{
			 		tmp+=prettyCharIterator.current();
			 		prettyCharIterator.next();
			 		charIndex++;
			 		length++;
			 		found=true;
					
		 		}
	    	if (found)
	    	{
	    		
	    		IdentifierListItem addToList = new IdentifierListItem(tmp, charIndex-length, charIndex, idCount);
	    		list.add(addToList);
	    		idCount++;
	    		found=false;
	    	}
	    	
	    	prettyCharIterator.next();
	    	charIndex++;
	    }
	    
	    return list;
	}
}
