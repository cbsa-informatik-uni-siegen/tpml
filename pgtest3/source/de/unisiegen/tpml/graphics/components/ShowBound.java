package de.unisiegen.tpml.graphics.components;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.graphics.components.Bound;

public class ShowBound
{
static Expression holeExpression;
	

	private static ShowBound bound = null;
	private static LinkedList<Bound> tmp = new LinkedList();
	private boolean debugOutput=false;
	//public LinkedList<Bound> result = new LinkedList();

	public static ShowBound getInstance()
	{
		if (bound == null)
		{
			bound = new ShowBound();
			return bound;
		}
		else
		{
			
			return bound;
		}
	}
	
	
	public void check(Expression pExpression)
	{
	
		{

			if (pExpression instanceof Lambda)
			{

				checkLambda((Lambda) pExpression);
			}
			else if (pExpression instanceof MultiLambda)
			{

				checkMultiLambda((MultiLambda) pExpression);
			}
			else if (pExpression instanceof MultiLet)
			{

				checkMultiLet((MultiLet) pExpression);
			}
			else if (pExpression instanceof CurriedLetRec)
			{
			
				checkCurriedLetRec((CurriedLetRec) pExpression);
			}
			else if (pExpression instanceof CurriedLet)
			{
				
				checkCurriedLet((CurriedLet) pExpression);
			}
			else if (pExpression instanceof Recursion)
			{

				checkRecursion((Recursion) pExpression);
			}
			else if (pExpression instanceof Let)
			{

				checkLet((Let) pExpression);
			}
			else
			{
				Enumeration<Expression> child = pExpression.children();
				while (child.hasMoreElements())
				{

					Expression actualExpression = (Expression) child.nextElement();

					check(actualExpression);

				}
			}

		}

	}
	



	//TODO working end
	private void checkLambda(Lambda lambda)
	{
		// rekursiver Aufruf für Ausdruck e von lambda
		Expression e = lambda.getE();
		check(e);
		
		// anlegen von Arrays mit den frei vorkommenden Namen der beiden expressions von lambda
		Object[] a = lambda.free().toArray();
		Object[] b = e.free().toArray();
		
		LinkedList list = listWithBounds(a, b);
		
	checkRec(lambda.children(),lambda,list);
	

	}
	
	
	private void checkMultiLambda(MultiLambda lambda)
	{
		
		// rekursiver Aufruf für Ausdruck e von lambda
		Expression e = lambda.getE();
		check(e);
		
		// anlegen von Arrays mit den frei vorkommenden Namen der beiden expressions von lambda
		Object[] a = lambda.free().toArray();
		Object[] b = e.free().toArray();
		
		LinkedList list = listWithBounds(a, b);
		
checkRec(lambda.children(),lambda,list);
	
		
	}


	private void checkLet(Let let)
	{
		// anlegen von Arrays mit den frei vorkommenden Namen der beiden expressions von lambda
		Object[] a = let.getE1().free().toArray();
		Object[] b = let.getE2().free().toArray();
		
		LinkedList list = listWithBounds(a, b);
		
		
		checkRec(let.children(),let,list);

		
	}
	
	private void checkMultiLet(MultiLet let)
	{
		// anlegen von Arrays mit den frei vorkommenden Namen der beiden expressions von lambda
		Object[] a = let.getE1().free().toArray();
		Object[] b = let.getE2().free().toArray();
		
		LinkedList list = listWithBounds(a, b);
		
		
		checkRec(let.children(),let,list);
		
				
	}
	

	
	
	private void checkCurriedLet(CurriedLet let)
	{
//	 anlegen von Arrays mit den frei vorkommenden Namen der beiden expressions von lambda
		Object[] b = let.getE1().free().toArray();
		Object[] a = let.getE2().free().toArray();
		
			
		LinkedList list = listWithBounds(a, b);
		
		
		checkRec(let.children(),let,list);
	}
	

	private void checkRecursion(Recursion rec)
	{
//	 anlegen von Arrays mit den frei vorkommenden Namen der beiden expressions von lambda
		Object[] a = rec.free().toArray();
		Object[] b = rec.getE().free().toArray();
		
		LinkedList list = listWithBounds(a, b);
		
		
		checkRec(rec.children(),rec,list);
	}
	
	
	private void checkCurriedLetRec(CurriedLetRec rec)
	{
//	 anlegen von Arrays mit den frei vorkommenden Namen der beiden expressions von lambda
		Object[] b = rec.getE1().free().toArray();
		Object[] a = rec.getE2().free().toArray();

		
			
		LinkedList list = listWithBounds(a, b);
		
		
		checkRec(rec.children(),rec,list);
		
	}

	
	private Bound checkRec (Enumeration child,Expression e, LinkedList <String> list)
	{
		Bound result=null;
		
		
		while (child.hasMoreElements())
		{

			Expression actualExpression = (Expression) child.nextElement();

			if (actualExpression.free().toArray().length > 0)
			{

				if (actualExpression instanceof Identifier)
				{

					Identifier id = (Identifier) actualExpression;
				
					for (int i = 0; i < list.size(); i++)
					{
						
						if (id.getName().equals(list.get(i)))
						{
							
							PrettyString ps1 = holeExpression.toPrettyString();
							PrettyAnnotation mark1 = ps1.getAnnotationForPrintable(e);
							PrettyAnnotation mark2 = ps1.getAnnotationForPrintable(id);
							
							
							
							
							int start =getStartOffset(e, id, mark1);
							
						
							
							
							int length = start + id.toString().length()-1;
							if (debugOutput)
							{
							System.err.println("Für den Identifier: Startoffset: " + start
									+ "Endoffset" + length);
							System.err.println("Für die Variable " + id.getName()
									+ " Startoffset: " + mark2.getStartOffset() + " Endoffset: "
									+ mark2.getEndOffset());
							}
							Bound addToList = new Bound(start,length);
							addToList.getMarks().add(mark2);
							tmp.add(addToList);
					
							
							
					
							
							
							
						}
					}

				}
				else
					checkRec(actualExpression.children(),e, list);
			}

		}
		
		return result;
	}

	
	
	public LinkedList<String> listWithBounds ( Object[] a, Object[] b)
	{
		LinkedList<String> e1 = new LinkedList();
		LinkedList<String> e2 = new LinkedList();
		
		e1=castArray(a);
		e2=castArray(b);

		for (int i = 0; i < e1.size(); i++)
		{
			if (e2.contains(e1.get(i)))
			{
				e2.remove(i);
				i--;
			}
		}
		return e2;
	}
	
	private LinkedList<String> castArray(Object[] a)
	{
		LinkedList<String> tmp = new LinkedList();
		
		for (int i = 0; i < a.length; i++)
		{

			tmp.add((String) a[i]);
		}
		
		return tmp;
	}
	
	private int getStartOffset(Expression e, Identifier id, PrettyAnnotation mark1)
	{
		int start=0;
		
		if (e instanceof CurriedLetRec)
		{
			CurriedLetRec let= (CurriedLetRec)e;
			start=8;
			for (int z=0; z<let.getIdentifiers().length;z++)
				
			if (let.getIdentifiers(z).equals(id.toString()))
			{
				for (int y=0; y<z;y++)
				{
					start+=1+let.getIdentifiers(y).toString().length();
				}
			}
			return start;
		}
		else if (e instanceof Lambda)
		{
			start = mark1.getStartOffset() + 1;
			return start;
		}
		else if (e instanceof Let)
		{
			start = mark1.getStartOffset() + 4;
			return start;
			
		}
		else if (e instanceof MultiLambda)
		{
			MultiLambda lambda= (MultiLambda)e;
			start=2;
			for (int z=0; z<lambda.getIdentifiers().length;z++)
				
			if (lambda.getIdentifiers(z).equals(id.toString()))
			{
				for (int y=0; y<z;y++)
				{
					start+=2+lambda.getIdentifiers(y).toString().length();
				}
			}
			return start;
		}
		
		else if (e instanceof MultiLet)
		{
			MultiLet let= (MultiLet)e;
			start=5;
			for (int z=0; z<let.getIdentifiers().length;z++)
				
			if (let.getIdentifiers(z).equals(id.toString()))
			{
				for (int y=0; y<z;y++)
				{
					start+=2+let.getIdentifiers(y).toString().length();
				}
			}
			return start;
		}
		else if (e instanceof CurriedLet)
		{
			CurriedLet let= (CurriedLet)e;
			start=4;
			for (int z=0; z<let.getIdentifiers().length;z++)
				
			if (let.getIdentifiers(z).equals(id.toString()))
			{
				for (int y=0; y<z;y++)
				{
					start+=1+let.getIdentifiers(y).toString().length();
				}
			}
			return start;
		}
		else if (e instanceof Recursion)
		{
			start=4;
			return start;
		}
		return start;
	}

	public static void setHoleExpression(Expression holeExpression)
	{
		bound.holeExpression = holeExpression;
		tmp=new LinkedList();
	}
	
	public LinkedList getAnnotations()
	{
		return tmp;
	}
	

}
