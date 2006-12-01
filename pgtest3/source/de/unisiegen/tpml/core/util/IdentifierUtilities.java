package de.unisiegen.tpml.core.util;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;

public final class IdentifierUtilities
{
	
	private IdentifierUtilities()
	{}
	
	public static IdentifierList getIdentifierPositions(Expression pExpression)
	{
		
		IdentifierList list= new IdentifierList();
		
		 PrettyCharIterator prettyCharIterator = pExpression.toPrettyString ( )
     .toCharacterIterator ( ) ;
		
		 int idCount=0;
		 int charIndex = 0 ;
		 boolean found=false;
		
	    while ( charIndex < pExpression.toString().length ( ) )
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
	    		
	    		list.addIdentifier(tmp, charIndex-length, charIndex, idCount);
	    		idCount++;
	    		found=false;
	    	}
	    	
	    	prettyCharIterator.next();
	    	charIndex++;
	    }
	    
	    return list;
	}
}
