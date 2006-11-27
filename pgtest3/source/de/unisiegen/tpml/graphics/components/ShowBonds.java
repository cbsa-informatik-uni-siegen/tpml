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
import de.unisiegen.tpml.core.types.MonoType;
/**
 * 
 * @author Benjamin
 *
 */
public class ShowBonds
{
	/**
	 * the actual expression in the GUI
	 */
	private Expression holeExpression;

	/**
	 * List of all Bounds in holeExpression
	 */
	private LinkedList<Bonds> result = new LinkedList<Bonds>();
	
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
		tmpChild.nextElement();
		
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
		if (true)

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
		LinkedList<String> list = listWithBounds(a, c);
		LinkedList<String> list2 = listWithBounds(a, b);
		
		for( int i=1; i<pLet.getIdentifiers().length;i++)
		{
			list.add(pLet.getIdentifiers(i));
		}
		
		
		
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

	/**
	 * the recursive method to check for bonds to the Identifier of the expression
	 * in inner expressions
	 * @param child
	 * @param e
	 * @param list
	 * @return
	 */
	private Bonds checkRec(LinkedList<Expression> child, Expression e,
			LinkedList<String> list)
	{
		Bonds tmpBound = null;
		boolean inList = false;

		/**
		 * this lopp is needed to search in every child
		 */
		for (int j = 0; j < child.size(); j++)
		{
			inList = false;
			
			/**
			 * the actual child is now actualExpression
			 */
			Expression actualExpression = child.get(j);
			
			/**
			 * check if in the actual Expression is something free.
			 * if anything is free nothing could be bound
			 */
			if (actualExpression.free().toArray().length > 0)
			{
				/**
				 * if the child is a leaf we have to check if it is an Identifier
				 */
				if (actualExpression instanceof Identifier)
				{
					
					/**
					 * the actual child is casted to Identifier
					 */
					Identifier id = (Identifier) actualExpression;

					/**
					 * now we check if the identifier is in the list of bound variables
					 */
					for (int i = 0; i < list.size(); i++)
					{
						/**
						 * if it is bound we create an Object of Bond an put it to the list
						 * of all bonds
						 */
						if (id.getName().equals(list.get(i)))
						{
							PrettyString ps1 = holeExpression.toPrettyString();
							PrettyAnnotation mark1 = ps1.getAnnotationForPrintable(e);
							PrettyAnnotation mark2 = ps1.getAnnotationForPrintable(id);
							if (result.size() > 0)
							{
								for (int z = 0; z < result.size(); z++)
								{
									Bonds tmpBound2 = result.get(z);
									
									/**
									 * befor it is added to list, we have to check if the variable is
									 * bond to another identifier. It just makes sense if we have different
									 * Identifiers with the same name in different expressions
									 */
									for (int y = 0; y < tmpBound2.getMarks().size(); y++)
									{

										if (mark2.getStartOffset() == tmpBound2.getMark(y)
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
								
								/**
								 * check if there had been another bond to same Identifier.
								 * if there was another bond add to this
								 */
								for (int k = 0; k < result.size(); k++)
								{
									if (start == result.get(k).getStartOffset())
									{
										exists = true;
										result.get(k).getMarks().add(mark2);
										result.get(k).getExpressions().add(e);
									}
								}
								/**
								 * else create a new bond
								 */
								if (!exists)
								{
									Bonds addToList = new Bonds(start, length, e, id
											.toString());
									addToList.getMarks().add(mark2);
									addToList.getExpressions().add(e);
									result.add(addToList);
								}

							}
						}

					}
				}
				/**
				 * just do the same for all childrens of the actual child
				 */
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

	/**
	 * This method compares to Arrays and puts just this variables into a Linked List 
	 * which are bound in the expression
	 * @param a
	 * @param b
	 * @return
	 */
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

	/**
	 * just take an Array and put it into a Linked List
	 * @param a
	 * @return LinkedList tmp
	 */
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

	/**
	 * This method calculates the right Startoffset of the Identifier for every
	 * type of Expression. This is needed because the Identifier of the Expression
	 * is not an Expression itself. So we can't go over PrettyAnnotation.getStartOffset
	 * @param e
	 * @param id
	 * @param mark1
	 * @return
	 */
	private int getStartOffset(Expression e, Identifier id, PrettyAnnotation mark1)
	{
		

		String exp = holeExpression.toPrettyString().toString();
		int start=exp.indexOf(id.toString());
		
		return start;
	}

	/**
	 * set the Expression to get the bonds
	 * @param pExpression
	 */
	public void setHoleExpression(Expression pExpression)
	{
		holeExpression = pExpression;


		result = new LinkedList<Bonds>();

	}

	/**
	 * returns a list with all bonds in the actual Expression
	 * @return 
	 */
	public LinkedList<Bonds> getAnnotations()
	{
		return result;
	}

}
