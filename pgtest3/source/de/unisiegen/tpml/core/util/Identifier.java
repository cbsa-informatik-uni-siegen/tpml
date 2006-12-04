package de.unisiegen.tpml.core.util;



	public class Identifier
	{
		private String id;
		private int startOffset;
		private int endOffset;
		private int idNumber;
		public Identifier(String pId, int start, int end, int number)
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


