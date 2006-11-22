package de.unisiegen.tpml.graphics.components;

import java.util.LinkedList;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;

public class Bound
{
	
	private int startOffset;
	private int endOffset;
	private String identifier;
	private Expression expression;
	private LinkedList<PrettyAnnotation> marks = new LinkedList<PrettyAnnotation>();
	private LinkedList<Expression>expressions=new LinkedList<Expression>();
	
	public Bound( int start, int end, Expression pExpression,String id)
	{
		expression=pExpression;
		startOffset =start;
		endOffset=end;
		identifier=id;
	}
	public Bound( int start, int end,LinkedList<PrettyAnnotation> list)
	{
		
		startOffset =start;
		endOffset=end;
		marks=list;
	}

	public int getEndOffset()
	{
		return endOffset;
	}

	public LinkedList<PrettyAnnotation> getMarks()
	{
		return marks;
	}

	public int getStartOffset()
	{
		return startOffset;
	}
	public LinkedList<Expression> getExpressions()
	{
		return expressions;
	}
	public Expression getExpression()
	{
		return expression;
	}
	public String getIdentifier()
	{
		return identifier;
	}
}
