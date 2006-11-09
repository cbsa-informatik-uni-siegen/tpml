package de.unisiegen.tpml.graphics.components;

import java.util.Enumeration;
import java.util.LinkedList;

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
	
	private static ShowBound bound = null ;
	
	public static ShowBound getInstance ( )
	  {
	    if ( bound == null )
	    {
	      bound = new ShowBound ( ) ;
	      return bound ;
	    }
	    else
	    {
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
			     // System.err.println("L�nge e1: "+e1.size()+"L�nge e2: " +e2.size());
			      
			     
			      
			      for (int i=0; i<e1.size();i++)
			      {
			    	  if (e2.contains(e1.get(i)))
			    	  {
			    		  e2.remove(i);
			    		  i--;
			    	  }
			      }
			      
			      Enumeration child =pLambda.children();
			      
			      while (child.hasMoreElements())
			      {
			    	  Expression actualExpression = (Expression) child.nextElement();
			    	  
			    	  if( actualExpression.free ( ).toArray ( ).length >0)
			    	  {
			    		  if (actualExpression instanceof Identifier)
			    		  {
			    			  Identifier id = (Identifier) actualExpression;
			    			  
			    			  for (int i=0; i<e2.size(); i++)
			    			  {
			    				  System.err.println("Identifier: "+id.getName()+", aus Liste: " + e2.get(i));
			    				  if (id.getName().equals(e2.get(i)))
			    				 {
			    					  	PrettyString ps =pLambda.toPrettyString();
			    					  	PrettyAnnotation mark = ps.getAnnotationForPrintable(e);
			    					  	System.err.println("Startoffset: "+ mark.getStartOffset() +" Endoffset: "+ mark.getEndOffset());  
			    				 }
			    			  }
			    			  
			    		  }
			    	  }
			    	  
			    	 
			    	 
			      }
			      
			  	//PrettyString ps =pLambda.toPrettyString();
			  	//PrettyAnnotation mark = ps.getAnnotationForPrintable(e);
			  	//System.err.println("Startoffset: "+ mark.getStartOffset() +" Endoffset: "+ mark.getEndOffset());
			    
			      System.err.println("L�nge e1: "+e1.size()+"L�nge e2: " +e2.size());
			      for (String s : e2)
			      {
			    	  System.err.println("Test3 "+e1.size());
			    	  System.err.println(s);
			      }
			}

			
			
			public void checkapp (Application pApp)
			{
				Expression e1 = pApp.getE1();
				Expression e2 = pApp.getE2();
				
				check(e1);
				check(e2);
			}
}
