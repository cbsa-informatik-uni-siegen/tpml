package de.unisiegen.tpml.graphics.components;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.IntegerConstant;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;



public class ShowBound 
{
	//TODO
	int count=0;
	static Expression holeExpression;
	
	private static ShowBound bound = null ;
	
	public static ShowBound getInstance (Expression e )
	  {
	    if ( bound == null )
	    {
	      bound = new ShowBound ( ) ;
	      holeExpression=e;
	      return bound ;
	    }
	    else
	    {
	      holeExpression=e;
	      return bound ;
	    }
	  }

	public void check (Expression pExpression)
	{
	
		
		    {
		        if ( pExpression instanceof Application )
		        {
		          checkapp ( (Application ) pExpression ) ;
		        }
		        
		        else if ( pExpression instanceof Lambda )
		        {
		        	checkBoundLambda( (Lambda) pExpression) ;
		        }
		        
		    }
		    
	 }
	
	  
			public void checkBoundLambda( Lambda pLambda)
			{
				 LinkedList<String> e1 = new LinkedList();
			     LinkedList<String> e2 = new LinkedList();
			      Expression e = pLambda.getE ( ) ;
			      
			      //CHANGE BENJAMIN TEST
			      check(e);
			      
			      Object [] a = pLambda.free ( ).toArray ( ) ;
			      Object [] b = e.free ( ).toArray ( ) ;
			      
			      
			      for ( int i = 0 ; i< a.length ; i++) 
			      { 
			    	//  System.err.println ( "Free Lambda:" + a[i] ) ;
			    	  e1.add((String)a[i]);
			      } 
			      for ( int j = 0 ; j< b.length ; j++) 
			      {
			       	  //System.err.println ( "Free e:" + b[i] ) ;
			    	  e2.add((String)b[j]);
			      }
			      
			      // CHANGE BENJAMIN TESTPRINT 
			     // System.err.println("Länge e1: "+e1.size()+"Länge e2: " +e2.size());
			      
			     
			      
			      for (int i=0; i<e1.size();i++)
			      {
			    	  if (e2.contains(e1.get(i)))
			    	  {
			    		  e2.remove(i);
			    		  i--;
			    	  }
			      }
			      
			      childCheck(pLambda.children(), e2, pLambda);
			     
			      
			  	//PrettyString ps =pLambda.toPrettyString();
			  	//PrettyAnnotation mark = ps.getAnnotationForPrintable(e);
			  	//System.err.println("Startoffset: "+ mark.getStartOffset() +" Endoffset: "+ mark.getEndOffset());
			    
			      //System.err.println("Länge e1: "+e1.size()+"Länge e2: " +e2.size());
			      /**for (String s : e2)
			      {
			    	  
			    	  System.err.println(s);
			      }*/
			}

			
			
			public void checkapp (Application pApp)
			{
				Expression e1 = pApp.getE1();
				Expression e2 = pApp.getE2();
				
				check(e1);
				check(e2);
			}
			
			
			public void childCheck (Enumeration child, LinkedList e2, Lambda pLambda)
			{
				count++;
				System.err.println(count+". Aufruf");
				
				while (child.hasMoreElements())
				{
					
					Expression actualExpression = (Expression) child.nextElement();
					
					 if( actualExpression.free ( ).toArray ( ).length >0)
					 {
						//System.err.println("Länge Array von Free: "+actualExpression.free ( ).toArray ( ).length );
						 
						 if (actualExpression instanceof Identifier)
			    		  {
			    			   
			    			  Identifier id = (Identifier) actualExpression;
			    			  
			    			  for (int i=0; i<e2.size(); i++)
			    			  {
			    				   
			    				 
			    				  if (id.getName().equals(e2.get(i)))
			    				 {
			    					  	PrettyString ps =holeExpression.toPrettyString();
			    					  	PrettyAnnotation mark = ps.getAnnotationForPrintable(id);
			    					  	System.err.println("Für den Identifier "+ id.getName()+" Startoffset: "+ mark.getStartOffset() +" Endoffset: "+ mark.getEndOffset());  
			    				 }
			    			  }
			    			  
			    		  }
						 else childCheck(actualExpression.children(),e2,pLambda);
					 }
					 
					 
				}
			}
}
