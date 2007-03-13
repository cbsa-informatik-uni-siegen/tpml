package de.unisiegen.tpml.graphics.components;

import java.util.ArrayList;
import java.util.Enumeration;
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
import de.unisiegen.tpml.core.util.IdentifierListItem;
import de.unisiegen.tpml.core.util.IdentifierUtilities;
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
	private ArrayList<Bonds> result = new ArrayList<Bonds>();


	/**
	 * first check what kind of expression is given. next call right method to
	 * handle this expressiontype
	 * 
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
				if (pExpression!=null)
				checkChild(pExpression);

			}

		}
		
	}

	/**
	 * recursive call of check for children
	 * 
	 * @param pExpression
	 */
	private void checkChild(Expression pExpression)
	{
		/**
		 * list with all childs of the expression
		 */
		ArrayList<Expression> child = new ArrayList<Expression>();

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

			}
		}

	}

	/**
	 * handling if Expression is instance of Expression type Lambda
	 * 
	 * @param pLambda
	 */
	private void checkLambda(Lambda pLambda)
	{
		/**
		 * check the body for other expression with bounds
		 */
		checkChild(pLambda);

		/**
		 * get the body of the expression
		 */
		Expression e = pLambda.getE();

		/**
		 * this array contains all free Variables of the lambda body
		 */
		Object[] a = e.free().toArray();

		ArrayList<String> b = new ArrayList<String>();
		b.add(pLambda.getId());

		
		ArrayList<String> list = listWithBounds(a, b);

		/**
		 * list with all childs of the expression
		 */
		ArrayList<Expression> child = new ArrayList<Expression>();
		child.add(pLambda.getE());

		
		checkRec(child, pLambda, list, false);

	}
	
	/**
	 * handling if Expression is instance of Expression type Multi Lambda
	 * 
	 * @param pLambda
	 */
	private void checkMultiLambda(MultiLambda pLambda)
	{

		/**
		 * check the body for other expression with bounds
		 */
		checkChild(pLambda);

		/**
		 * get the body of the expression
		 */
		Expression e = pLambda.getE();
		
		/**
		 * this array contains all free Variables of the Lambda body
		 */
		Object[] a = e.free().toArray();

		ArrayList<String> b = castArray(pLambda.getIdentifiers());

		
		ArrayList<String> list = listWithBounds(a, b);

		/**
		 * list with all childs of the expression
		 */
		ArrayList<Expression> child = new ArrayList<Expression>();
		child.add(pLambda.getE());

		checkRec(child, pLambda, list, false);

	}
	
	/**
	 * handling if Expression is instance of Expression type Let
	 * 
	 * @param pLet
	 */
	private void checkLet(Let pLet)
	{
		/**
		 * check the body for other expression with bounds
		 */
		checkChild(pLet);
		
		/**
		 * this array contains all free Variables of second Expression
		 */
		Object[] a = pLet.getE2().free().toArray();

		ArrayList<String> b = new ArrayList<String>();
		b.add(pLet.getId());

		ArrayList<String> list = listWithBounds(a, b);

		/**
		 * list with all childs of the expression
		 */
		ArrayList<Expression> child = new ArrayList<Expression>();

		child.add(pLet.getE2());

		checkRec(child, pLet, list, false);

	}
	
	/**
	 * handling if Expression is instance of Expression type MultiLet
	 * 
	 * @param pLet
	 */
	private void checkMultiLet(MultiLet pLet)
	{
		/**
		 * check the body for other expression with bounds
		 */
		checkChild(pLet);

		/**
		 * this array contains all free Variables of the second Expression
		 */
		Object[] a = pLet.getE2().free().toArray();

		ArrayList<String> b = castArray(pLet.getIdentifiers());

		ArrayList<String> list = listWithBounds(a, b);

		/**
		 * list with all childs of the expression
		 */
		ArrayList<Expression> child = new ArrayList<Expression>();

		child.add(pLet.getE2());

		checkRec(child, pLet, list, false);

	}


	
	/**
	 * handling if Expression is instance of Expression type CurriedLet
	 * 
	 * @param pLet
	 */
	private void checkCurriedLet(CurriedLet pLet)
	{
		/**
		 * check the body for other expression with bounds
		 */
		checkChild(pLet);
		
		/**
		 * this array contains all free Variables of second Expression
		 */
		Object[] a = pLet.getE1().free().toArray();

		/**
		 * this array contains all free Variables of the first Expression
		 */
		Object[] b = pLet.getE2().free().toArray();

		ArrayList<String> c = castArray(pLet.getIdentifiers());
		
		
		ArrayList<String> d = new ArrayList<String>();
		d.add(c.get(0));

		c.remove(0);	

		/**
		 * in this method two different lists are needed for the two different
		 * Expressions E1 and E2
		 */
		ArrayList<String> list2 = listWithBounds(b, c);
		ArrayList<String> list = new ArrayList<String>();
		list.add(pLet.getIdentifiers(0));

	
		
		ArrayList<Expression> child = new ArrayList<Expression>();

		child.add(pLet.getE1());

		
		ArrayList<Expression> child2 = new ArrayList<Expression>();
		child2.add(pLet.getE2());

	

		/**
		 * different recursive calls for E1 and E2 with a different list of bounds
		 */
		checkRec(child2, pLet, list2, true);

		checkRec(child, pLet, list, false);

	}

	/**
	 * handling if Expression is instance of Expression type LetRec
	 * 
	 * @param pRec
	 */
	private void checkLetRec(LetRec pRec)
	{
		/**
		 * check the body for other expression with bounds
		 */
		checkChild(pRec);

		/**
		 * this array contains all free Variables of the second Expression
		 */
		Object[] a = pRec.getE2().free().toArray();
		Object[] c = pRec.getE1().free().toArray();

		ArrayList<String> b = new ArrayList<String>();
		b.add(pRec.getId());

		ArrayList<String> list = listWithBounds(a, b);
		ArrayList<String> list2 = listWithBounds (c, b);
		

		/**
		 * list with all childs of the expression
		 */
		ArrayList<Expression> child = new ArrayList<Expression>();
		child.add(pRec.getE1());
		child.add(pRec.getE2());
		
		checkRec(child, pRec, list2, false);
		checkRec(child, pRec, list, false);
		

	}
	
	/**
	 * handling if Expression is instance of Expression type CurriedLetRec
	 * 
	 * @param pRec
	 */
	private void checkCurriedLetRec(CurriedLetRec pRec)
	{
		/**
		 * check the body for other expression with bounds
		 */
		checkChild(pRec);
		
		/**
		 * this array contains all free Variables of the second Expression
		 */
		Object[] a = pRec.getE1().free().toArray();

		/**
		 * this array contains all free Variables of the first Expression
		 */
		//Object[] b = pRec.getE2().free().toArray();

		ArrayList<String> c = castArray(pRec.getIdentifiers());

	

		/**
		 * in this method two different lists are needed for the two different
		 * Expressions E1 and E2
		 */
		ArrayList<String> list = listWithBounds(a, c);

		/**
		 * list with all childs of the Expression
		 */
		ArrayList<Expression> child = new ArrayList<Expression>();
		child.add(pRec.getE1());

		/**
		 * list with just the last child of the Expression
		 */
		ArrayList<Expression> child2 = new ArrayList<Expression>();
		child2.add(pRec.getE2());

		ArrayList<String> list2 = new ArrayList<String>();
		list2.add(pRec.getIdentifiers(0));

		/**
		 * check if there are duplicate Identifiers
		 */
		boolean duplicate = false;
		for (int i = 1; i < list.size(); i++)
		{

			if (list.get(i).equals(list.get(0)))
			{
				duplicate = true;
				break;
			}

		}

		/**
		 * different calls for E1 and E2 with a different list of bounds
		 */
		checkRec(child2, pRec, list2, duplicate);

		checkRec(child, pRec, list, false);

	}
	
	/**
	 * handling if Expression is instance of Expression type Recursion
	 * 
	 * @param pRec
	 */
	private void checkRecursion(Recursion pRec)
	{
		/**
		 * check the body for other expression with bounds
		 */
		checkChild(pRec);

		/**
		 * this array contains all free Variables of the Recursion body
		 */
		Object[] a = pRec.getE().free().toArray();

		ArrayList<String> b = new ArrayList<String>();
		b.add(pRec.getId());

		ArrayList<String> list = listWithBounds(a, b);

		/**
		 * list with all childs of the Expression
		 */
		ArrayList<Expression> child = new ArrayList<Expression>();
		child.add(pRec.getE());

		checkRec(child, pRec, list, false);
	}

	

	/**
	 * the recursive method to check for bonds to the Identifier of the expression
	 * in inner expressions
	 * 
	 * @param child
	 * @param e
	 * @param list
	 * @return
	 */
	private void checkRec(ArrayList<Expression> child, Expression e,
			ArrayList<String> list, boolean different)
	{
		
		boolean inList = false;

		/**
		 * this lopp is needed to search recursive in every child
		 */
		for (int j = 0; j < child.size(); j++)
		{
			inList = false;

			/**
			 * the actual child is now actualExpression
			 */
			Expression actualExpression = child.get(j);

			/**
			 * check if in the actual Expression is something free. if anything is
			 * free nothing could be bound
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
					
						if (id.getName().equals(list.get(i)))
						{
							/**
							 * create Pretty Annotations to get the Offset
							 */
							PrettyString ps1 = holeExpression.toPrettyString();
							PrettyAnnotation mark1 = ps1.getAnnotationForPrintable(e);
							PrettyAnnotation mark2 = ps1.getAnnotationForPrintable(id);
							if (result.size() > 0)
							{
								for (int z = 0; z < result.size(); z++)
								{
									Bonds tmpBound2 = result.get(z);

									/**
									 * Prior we have to check if the variable is bond in an inner term
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
								int start = getStartOffset(e, id, mark1, actualExpression,
										different);

								int length = start + id.toString().length() - 1;

								boolean exists = false;

								/**
								 * check if there had been another bond to same Identifier. if
								 * there was such a bond add to this
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
									Bonds addToList = new Bonds(start, length, e, id.toString());
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
					ArrayList<Expression> childtmp = new ArrayList<Expression>();
					Enumeration tmpChild = actualExpression.children();
					while (tmpChild.hasMoreElements())
					{
						child.add((Expression) tmpChild.nextElement());
					}
					checkRec(childtmp, e, list, different);
				}
			}

		}

		
	}

	/**
	 * This method compares to Arrays and puts just this variables into a Linked
	 * List which are bound in the expression
	 * 
	 * @param a
	 * @param e2 
	 * @param b
	 * @return
	 */
	public ArrayList<String> listWithBounds(Object[] a, ArrayList<String> e2)
	{
		ArrayList<String> e1 = new ArrayList<String>();

		e1 = castArray(a);
		
			
		for (int i = 0; i < e2.size(); i++)
		{
			
			if (!e1.contains(e2.get(i)))
			{
				e2.remove(i);
				i--;
			}

		}

		return e2;
	}

	/**
	 * just take an Array and put it into a Linked List
	 * 
	 * @param a
	 * @return ArrayList tmp
	 */
	private ArrayList<String> castArray(Object[] a)
	{
		ArrayList<String> tmp = new ArrayList<String>();

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
	 * is not an Expression itself. So we can't go over
	 * PrettyAnnotation.getStartOffset
	 * 
	 * @param e
	 * @param id
	 * @param mark1
	 * @return
	 */
	private int getStartOffset(Expression pExpression, Identifier id,
			PrettyAnnotation mark1, Expression child, boolean different)
	{
		int last = 0;
		ArrayList<IdentifierListItem> ids = IdentifierUtilities
				.getIdentifierPositions(pExpression);

		if (different)
		{
			for (int i = 0; i < ids.size(); i++)
			{
				if (ids.get(i).getId().equals(id.toString()))
				{
					return mark1.getStartOffset() + ids.get(i).getStartOffset();
				}
			}

		}

		for (int i = 0; i < ids.size(); i++)
		{
			if (ids.get(i).getId().equals(id.toString()))
			{
				last = i;
			}
		}
		return mark1.getStartOffset() + ids.get(last).getStartOffset();
	}

	/**
	 * set the Expression to get the bonds
	 * 
	 * @param pExpression
	 */
	public void setHoleExpression(Expression pExpression)
	{
		holeExpression = pExpression;

		result = new ArrayList<Bonds>();

	}

	/**
	 * returns a list with all bonds in the actual Expression
	 * 
	 * @return
	 */
	public ArrayList<Bonds> getAnnotations()
	{
		return result;
	}

}