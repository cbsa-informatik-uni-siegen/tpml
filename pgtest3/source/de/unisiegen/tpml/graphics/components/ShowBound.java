package de.unisiegen.tpml.graphics.components;

import java.util.Enumeration;
import java.util.LinkedList;

import de.unisiegen.tpml.Debug;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;

public class ShowBound
{
	/**
	 * the actual expression in the GUI
	 */
	private Expression holeExpression;

	/**
	 * List of all Bounds in holeExpression
	 */
	private LinkedList<Bound> result = new LinkedList<Bound>();
	
	//CHANGE BENJAMIN just for Debug
	private String me = "Benjamin";
	
/**
 * first check what kind of expression is given.
 * next call right method to handle this expressiontype
 * @param pExpression
 */
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
			else if (pExpression instanceof LetRec)
			{

				checkLetRec((LetRec) pExpression);
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
				checkChild(pExpression);

			}

		}

	}

	/**
	 * recursive call of check for children
	 * @param pExpression
	 */
	private void checkChild(Expression pExpression)
	{
		/**
		 * list with all childs of the expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the expression
		 */
		Enumeration tmpChild = pExpression.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}

		
		/**
		 * calls the check method with any child of the expression
		 */
		{
			for (int i = 0; i < child.size(); i++)

			{
				check(child.get(i));

				/**
				 * Debug output. will be deleted if everything works fine
				 */
				if (false)
				{
					Debug.out.print(child.get(i).toString(), me);

					try
					{
						PrettyString ps = holeExpression.toPrettyString();
						PrettyAnnotation mark = ps.getAnnotationForPrintable(child.get(i));
						Debug.out.println("start:" + mark.getStartOffset(), me);
					}
					catch (Exception e)
					{
						Debug.out.println(
								"Kein Startoffset für diesen Ausdruck verfügbar!", me);
					}
				}
			}
		}

	}

	/**
	 * handling if Expression is instance of Expression type Lambda
	 * @param pLambda
	 */
	private void checkLambda(Lambda pLambda)
	{
		/**
		 * get the body of the lambda expression
		 */
		Expression e = pLambda.getE();
		
		/**
		 * check the body of lambda for other expression with bounds
		 */
		checkChild(pLambda);

		/**
		 * this array contains all free Variables of the lambda expression
		 */
		Object[] a = pLambda.free().toArray();
		
		/**
		 * this array contains all free Variables of the lambda body
		 */
		Object[] b = e.free().toArray();
		
		/**
		 * this list contains all bounded Varibles in the lambda expression
		 */
		LinkedList<String> list = listWithBounds(a, b);

		/**
		 * list with all childs of the expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the expression
		 */
		Enumeration tmpChild = pLambda.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}
	
		checkRec(child, pLambda, list);

	}

	/**
	 * handling if Expression is instance of  Expression type LetRec
	 * @param pRec
	 */
	private void checkLetRec(LetRec pRec)
	{
		
		/**
		 * this array contains all free Variables of the second Expression
		 */
		Object[] b = pRec.getE2().free().toArray();
		
		/**
		 * this array contains all free Variables of the LetRec Expression
		 */
		Object[] c = pRec.free().toArray();
		
		checkChild(pRec);

		LinkedList<String> list = listWithBounds(c, b);
		list.add(pRec.getId());

		/**
		 * list with all childs of the expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the expression
		 */
		Enumeration tmpChild = pRec.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}

		checkRec(child, pRec, list);

	}

	/**
	 * handling if Expression is instance of Expression type Multi Lambda
	 * @param pLambda
	 */
	private void checkMultiLambda(MultiLambda pLambda)
	{

		// rekursiver Aufruf für Ausdruck e von lambda
		Expression e = pLambda.getE();
		checkChild(pLambda);

		/**
		 * this array contains all free Variables of the Multi Lambda Expression
		 */
		Object[] a = pLambda.free().toArray();
		
		/**
		 * this array contains all free Variables of the Lambda body
		 */
		Object[] b = e.free().toArray();

		LinkedList<String> list = listWithBounds(a, b);

		/**
		 * list with all childs of the expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the expression
		 */
		Enumeration tmpChild = pLambda.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}

		checkRec(child, pLambda, list);

	}

	/**
	 * handling if Expression is instance of Expression type Let
	 * @param pLet
	 */
	private void checkLet(Let pLet)
	{
		/**
		 * this array contains all free Variables of second Expression
		 */
		Object[] b = pLet.getE2().free().toArray();
		
		/**
		 * this array contains all free Variables of the Let Expression
		 */
		Object[] c = pLet.free().toArray();

		checkChild(pLet);

		LinkedList<String> list = listWithBounds(c, b);
		list.add(pLet.getId());

		/**
		 * list with all childs of the expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the expression
		 */
		Enumeration tmpChild = pLet.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}

		checkRec(child, pLet, list);

	}

	/**
	 * handling if Expression is instance of Expression type MultiLet
	 * @param pLet
	 */
	private void checkMultiLet(MultiLet pLet)
	{
		/**
		 * this array contains all free Variables of the Let Expression
		 */
		Object[] a = pLet.free().toArray();
		
		/**
		 * this array contains all free Variables of the second Expression
		 */
		Object[] b = pLet.getE2().free().toArray();

		checkChild(pLet);
		LinkedList<String> list = listWithBounds(new Object[0], b);

		/**
		 * list with all childs of the expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the expression
		 */
		Enumeration tmpChild = pLet.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}

		checkRec(child, pLet, list);

	}

	/**
	 * handling if Expression is instance of Expression type CurriedLet
	 * @param pLet
	 */
	private void checkCurriedLet(CurriedLet pLet)
	{
		/**
		 * this array contains all free Variables of second Expression
		 */
		Object[] c = pLet.getE2().free().toArray();
		
		/**
		 * this array contains all free Variables of the first Expression
		 */
		Object[] b = pLet.getE1().free().toArray();
		
		/**
		 * this array contains all free Variables of the Let Expression
		 */
		Object[] a = pLet.free().toArray();

		checkChild(pLet);

		/**
		 * Debug output. will be deleted if everything works fine
		 */
		if (false)

		{
			Debug.out.println("a", me);
			for (int i = 0; i < a.length; i++)
			{
				Debug.out.println(a[i], me);
			}
			Debug.out.println("b", me);
			for (int i = 0; i < b.length; i++)
			{
				Debug.out.println(b[i], me);
			}
			Debug.out.println("c", me);
			for (int i = 0; i < c.length; i++)
			{
				Debug.out.println(c[i], me);
			}

		}
		
		/**
		 * in this method two different lists are needed for the two different Expressions
		 * E1 and E2
		 */
		LinkedList<String> list = listWithBounds(new Object[a.length], a);
		LinkedList<String> list2 = listWithBounds(c, b);
		
		/**
		 * the Identifier of the Expression is added to the list of E2, because Variables 
		 * in this Expression with the same name are bond to this Identifier
		 */
		list2.add(pLet.getIdentifiers(0));

		/**
		 * if the Identifier of the Expression is in E1 it is removed from the list
		 * because it is not bond to this Identifier
		 */
		if (list.contains(pLet.getIdentifiers(0)))
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).equals(pLet.getIdentifiers(0)))
				{
					list.remove(i);
					i--;
				}
			}
		}

		/**
		 * list with all childs of the Expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the Expression
		 */
		Enumeration tmpChild = pLet.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}
		
		/**
		 * list with just the last child of the Expression
		 */
		LinkedList<Expression> child2 = new LinkedList<Expression>();
		child2.add((child.getLast()));
		
		/**
		 * different calls for E1 and E2 with a different list of bounds
		 */
		checkRec(child2, pLet, list2);
		child.remove(child.size() - 1);
		checkRec(child, pLet, list);

	}

	/**
	 * handling if Expression is instance of Expression type Recursion
	 * @param pRec
	 */
	private void checkRecursion(Recursion pRec)
	{
		/**
		 * this array contains all free Variables of the Recursion Expression
		 */
		Object[] a = pRec.free().toArray();
		
		/**
		 * this array contains all free Variables of the Recursion body
		 */
		Object[] b = pRec.getE().free().toArray();

		checkChild(pRec);

		LinkedList<String> list = listWithBounds(a, b);

		/**
		 * list with all childs of the Expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the Expression
		 */
		Enumeration tmpChild = pRec.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}
		checkRec(child, pRec, list);
	}

	/**
	 * handling if Expression is instance of Expression type CurriedLetRec
	 * @param pRec
	 */
	private void checkCurriedLetRec(CurriedLetRec pRec)
	{
		/**
		 * this array contains all free Variables of the second Expression
		 */
		Object[] a = pRec.getE2().free().toArray();
		
		/**
		 * this array contains all free Variables of the first Expression
		 */
		Object[] b = pRec.getE1().free().toArray();
		
		/**
		 * this array contains all free Variables of the CurriedLetRec Expression
		 */
		Object[] c = pRec.free().toArray();

		/**
		 * Debug output. will be deleted if everything works fine
		 */
		if (false)

		{
			Debug.out.println("a", me);
			for (int i = 0; i < a.length; i++)
			{
				Debug.out.println(a[i], me);
			}
			Debug.out.println("b", me);
			for (int i = 0; i < b.length; i++)
			{
				Debug.out.println(b[i], me);
			}
			Debug.out.println("c", me);
			for (int i = 0; i < c.length; i++)
			{
				Debug.out.println(c[i], me);
			}

		}

		checkChild(pRec);

		/**
		 * in this method two different lists are needed for the two different Expressions
		 * E1 and E2
		 */
		LinkedList<String> list = listWithBounds(c, a);
		LinkedList<String> list2 = listWithBounds(c, b);
		
		/**
		 * the Identifier of the Expression is added to the list of E2, because Variables 
		 * in this Expression with the same name are bond to this Identifier
		 */
		list2.add(pRec.getIdentifiers(1));

		/**
		 * list with all childs of the Expression
		 */
		LinkedList<Expression> child = new LinkedList<Expression>();
		
		/**
		 * Enumeration with all childs of the Expression
		 */
		Enumeration tmpChild = pRec.children();
		
		/**
		 * converting from Enumeration to Linked List for better handling
		 */
		while (tmpChild.hasMoreElements())
		{

			child.add((Expression) tmpChild.nextElement());

		}
		
		/**
		 * list with just the last child of the Expression
		 */
		LinkedList<Expression> child2 = new LinkedList<Expression>();
		child2.add((child.getLast()));
		
		/**
		 * different calls for E1 and E2 with a different list of bounds
		 */
		checkRec(child2, pRec, list);
		child.remove(child.size() - 1);
		checkRec(child, pRec, list2);

	}

	private Bound checkRec(LinkedList<Expression> child, Expression e,
			LinkedList<String> list)
	{
		Bound tmpBound = null;
		boolean inList = false;

		for (int j = 0; j < child.size(); j++)
		{
			inList = false;
			Expression actualExpression = child.get(j);

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
							if (result.size() > 0)
							{
								for (int z = 0; z < result.size(); z++)
								{
									Bound tmpBound2 = result.get(z);
									for (int y = 0; y < tmpBound.getMarks().size(); y++)
									{

										if (mark2.getStartOffset() == tmpBound.getMark(y)
												.getStartOffset())
										{
											inList = true;
										}
										
									}
								}
							}
							if (!inList)

							{
								int start = getStartOffset(e, id, mark1);

								int length = start + id.toString().length() - 1;

								
								boolean exists = false;
								for (int k = 0; k < result.size(); k++)
								{
									if (start == result.get(k).getStartOffset())
									{
										exists = true;
										result.get(k).getMarks().add(mark2);
										result.get(k).getExpressions().add(e);
									}
								}
								if (!exists)
								{
									Bound addToList = new Bound(start, length, e, id
											.toString());
									addToList.getMarks().add(mark2);
									addToList.getExpressions().add(e);
									result.add(addToList);
								}

							}
						}

					}
				}
				else
				{
					LinkedList<Expression> childtmp = new LinkedList<Expression>();
					Enumeration tmpChild = actualExpression.children();
					while (tmpChild.hasMoreElements())
					{
						child.add((Expression) tmpChild.nextElement());
					}
					checkRec(childtmp, e, list);
				}
			}

		}

		return tmpBound;
	}

	public LinkedList<String> listWithBounds(Object[] a, Object[] b)
	{
		LinkedList<String> e1 = new LinkedList<String>();
		LinkedList<String> e2 = new LinkedList<String>();

		e1 = castArray(a);
		e2 = castArray(b);

		for (int i = 0; i < e1.size(); i++)
		{
			for (int j = 0; j < e2.size(); j++)
			{
				if (e1.get(i).equals(e2.get(j)))
				{
					e2.remove(j);
					j--;
				}
			}

		}

		return e2;
	}

	private LinkedList<String> castArray(Object[] a)
	{
		LinkedList<String> tmp = new LinkedList<String>();

		for (int i = 0; i < a.length; i++)
		{
			if (a[i] != null)
				tmp.add((String) a[i]);
		}

		return tmp;
	}

	private int getStartOffset(Expression e, Identifier id, PrettyAnnotation mark1)
	{
		int start = 0;

		if (e instanceof CurriedLetRec)
		{
			CurriedLetRec let = (CurriedLetRec) e;
			start = mark1.getStartOffset() + 8;
			for (int z = 0; z < let.getIdentifiers().length; z++)

				if (let.getIdentifiers(z).equals(id.toString()))
				{
					for (int y = 0; y < z; y++)
					{
						start += 1 + let.getIdentifiers(y).toString().length();
					}
				}
			return start;
		}
		else if (e instanceof LetRec)
		{
			start = mark1.getStartOffset() + 8;
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
			MultiLambda lambda = (MultiLambda) e;
			start = 2 + mark1.getStartOffset();
			for (int z = 0; z < lambda.getIdentifiers().length; z++)

				if (lambda.getIdentifiers(z).equals(id.toString()))
				{
					for (int y = 0; y < z; y++)
					{
						start += 2 + lambda.getIdentifiers(y).toString().length();
					}
				}
			return start;
		}

		else if (e instanceof MultiLet)
		{
			MultiLet let = (MultiLet) e;
			start = mark1.getStartOffset() + 5;
			for (int z = 0; z < let.getIdentifiers().length; z++)

				if (let.getIdentifiers(z).equals(id.toString()))
				{
					for (int y = 0; y < z; y++)
					{
						start += 2 + let.getIdentifiers(y).toString().length();
					}
				}
			return start;
		}
		else if (e instanceof CurriedLet)
		{
			CurriedLet let = (CurriedLet) e;
			start = mark1.getStartOffset() + 4;
			for (int z = 0; z < let.getIdentifiers().length; z++)

				if (let.getIdentifiers(z).equals(id.toString()))
				{
					for (int y = 0; y < z; y++)
					{
						start += 1 + let.getIdentifiers(y).toString().length();
					}
				}
			return start;
		}
		else if (e instanceof Recursion)
		{
			start = mark1.getStartOffset() + 4;
			return start;
		}
		return start;
	}

	public void setHoleExpression(Expression pExpression)
	{
		holeExpression = pExpression;

		result = new LinkedList<Bound>();

	}

	public LinkedList<Bound> getAnnotations()
	{
		return result;
	}

}
