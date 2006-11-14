package de.unisiegen.tpml.graphics.components;

import java.util.LinkedList;

import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;

public class Bound
{
	
	public int startOffset;
	public int endOffset;
	public LinkedList<PrettyAnnotation> marks = new LinkedList();
	
	public Bound( int start, int end)
	{
		
		startOffset =start;
		endOffset=end;
	}
}
