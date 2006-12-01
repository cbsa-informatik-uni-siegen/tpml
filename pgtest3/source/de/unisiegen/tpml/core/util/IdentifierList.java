package de.unisiegen.tpml.core.util;

import java.util.ArrayList;

public class IdentifierList
{
	ArrayList<Identifier> identifierList= new ArrayList<Identifier>();
	
	
	public void addIdentifier(String pId, int start, int end, int idCount)
	{
		Identifier newIdentifier=new Identifier(pId, start, end, idCount);
		identifierList.add(newIdentifier);
	}
	
	public int getIdentifierStartoffset(int index)
	{
		return identifierList.get(index).getStartOffset();
	}
	
	public int getIdentifierEndoffset(int index)
	{
		return identifierList.get(index).getEndOffset();
	}
	
	public String getIdentifier(int index)
	{
		return identifierList.get(index).getId();
	}
	
	public int getIdentifierNumber(int index)
	{
		return identifierList.get(index).getIdNumber();

	}
	
	public int length()
	{
		return identifierList.size();
	}
	
	
	
	
	
	
	
	
	private class Identifier
	{
		private String id;
		private int startOffset;
		private int endOffset;
		private int idNumber;
		private Identifier(String pId, int start, int end, int number)
		{
			id=pId;
			startOffset=start;
			endOffset=end;
			idNumber=number;
		}
		public int getEndOffset()
		{
			return endOffset;
		}
		public String getId()
		{
			return id;
		}
		public int getStartOffset()
		{
			return startOffset;
		}
		public int getIdNumber()
		{
			return idNumber;
		}
	}

}
