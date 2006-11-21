package de.unisiegen.tpml.graphics.components;

import java.util.LinkedList;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;

public class Bound
{
	
	private int startOffset;
	private int endOffset;
	private LinkedList<PrettyAnnotation> marks = new LinkedList<PrettyAnnotation>();
	private LinkedList<Expression>expressions=new LinkedList<Expression>();
	
	public Bound( int start, int end)
	{
		
		startOffset =start;
		endOffset=end;
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
}
