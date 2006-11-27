package de.unisiegen.tpml.graphics.components;

import java.util.LinkedList;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;

/**
 * 
 * @author Benjamin
 *
 */
public class Bonds
{
	/**
	 * Startoffset of the Identifier
	 */ 
	private int startOffset;
	
	/**
	 * Endoffset of the Identifier
	 */
	private int endOffset;
	
	/**
	 * Identifier as String
	 */
	private String identifier;
	
	/**
	 * Identifier as Expression
	 */
	private Expression expression;
	
	/**
	 * List of Annotations with all Variables which are bound to the Identifier
	 */
	private LinkedList<PrettyAnnotation> marks = new LinkedList<PrettyAnnotation>();
	
	/**
	 * List of Expressions with all Variables which are bound to the Identifier
	 */
	private LinkedList<Expression>expressions=new LinkedList<Expression>();
	
	public Bonds( int start, int end, Expression pExpression,String id)
	{
		expression=pExpression;
		startOffset =start;
		endOffset=end;
		identifier=id;
	}
	public Bonds( int start, int end,LinkedList<PrettyAnnotation> list)
	{
		
		startOffset =start;
		endOffset=end;
		marks=list;
	}

	/**
	 * returns the Endoffset of the Identifier
	 * @return
	 */
	public int getEndOffset()
	{
		return endOffset;
	}

	/**
	 * returns a list of marks of Variables
	 * @return
	 */
	public LinkedList<PrettyAnnotation> getMarks()
	{
		return marks;
	}
	
	/**
	 * returns the PrettyAnnotation on position "index"
	 * @param index
	 * @return
	 */
	public PrettyAnnotation getMark(int index)
	{
		return marks.get(index);
	}

	/**
	 * returns the Startoffset of the Identifier
	 * @return
	 */
	public int getStartOffset()
	{
		return startOffset;
	}
	
	/**
	 * returns a list of Expressions with all Variables
	 * @return
	 */
	public LinkedList<Expression> getExpressions()
	{
		return expressions;
	}
	
	/**
	 * returns the Identifier as Expression
	 * @return
	 */
	public Expression getExpression()
	{
		return expression;
	}
	
	/**
	 * Returns the Identifier as String
	 * @return
	 */
	public String getIdentifier()
	{
		return identifier;
	}
}
