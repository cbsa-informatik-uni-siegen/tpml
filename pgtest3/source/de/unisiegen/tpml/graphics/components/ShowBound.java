package de.unisiegen.tpml.graphics.components;

import java.util.Enumeration;
import java.util.LinkedList;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;

public class ShowBound
{
	// TODO
	int count = 0;
	static Expression holeExpression;

	private static ShowBound bound = null;

	public static ShowBound getInstance(Expression e)
	{
		if (bound == null)
		{
			bound = new ShowBound();
			holeExpression = e;
			return bound;
		}
		else
		{
			holeExpression = e;
			return bound;
		}
	}

	public void check(Expression pExpression)
	{

		{
			if (pExpression instanceof Application)
			{
				checkapp((Application) pExpression);
			}

			else if (pExpression instanceof Lambda)
			{
				checkBoundLambda((Lambda) pExpression);
			}

		}

	}

	public void checkBoundLambda(Lambda pLambda)
	{
		LinkedList<String> e1 = new LinkedList();
		LinkedList<String> e2 = new LinkedList();
		Expression e = pLambda.getE();

		// CHANGE BENJAMIN TEST
		check(e);

		Object[] a = pLambda.free().toArray();
		Object[] b = e.free().toArray();

		for (int i = 0; i < a.length; i++)
		{

			e1.add((String) a[i]);
		}
		for (int j = 0; j < b.length; j++)
		{

			e2.add((String) b[j]);
		}

		for (int i = 0; i < e1.size(); i++)
		{
			if (e2.contains(e1.get(i)))
			{
				e2.remove(i);
				i--;
			}
		}

		childCheck(pLambda.children(), e2, pLambda);

	}

	public void checkapp(Application pApp)
	{
		Expression e1 = pApp.getE1();
		Expression e2 = pApp.getE2();

		check(e1);
		check(e2);
	}

	public void childCheck(Enumeration child, LinkedList e2, Lambda pLambda)
	{
		count++;
		//Prints number off call
		//System.err.println(count + ". Aufruf");

		while (child.hasMoreElements())
		{

			Expression actualExpression = (Expression) child.nextElement();

			if (actualExpression.free().toArray().length > 0)
			{

				if (actualExpression instanceof Identifier)
				{

					Identifier id = (Identifier) actualExpression;

					for (int i = 0; i < e2.size(); i++)
					{

						if (id.getName().equals(e2.get(i)))
						{
							PrettyString ps1 = holeExpression.toPrettyString();
							PrettyAnnotation mark1 = ps1.getAnnotationForPrintable(pLambda);
							PrettyString ps2 = holeExpression.toPrettyString();
							PrettyAnnotation mark2 = ps2.getAnnotationForPrintable(id);
							
							
							int start = mark1.getStartOffset()+1;
							int length =mark1.getStartOffset()+id.toString().length();
							
							System.err.println(pLambda.toString());
							
							System.err.println("Für den Identifier: Startoffset: "+start  +"Endoffset" +length);
							System.err.println("Für die Variable " + id.getName()
									+ " Startoffset: " + mark2.getStartOffset() + " Endoffset: "
									+ mark2.getEndOffset());
						}
					}

				}
				else
					childCheck(actualExpression.children(), e2, pLambda);
			}

		}
	}
}
