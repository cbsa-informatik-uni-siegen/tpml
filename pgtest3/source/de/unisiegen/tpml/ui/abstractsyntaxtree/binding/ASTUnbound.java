package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


import java.util.Enumeration ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTUnbound
{
  /**
   * TODO
   */
  private LinkedList < Expression > unboundList ;


  /**
   * TODO
   * 
   * @param pExpression
   */
  public ASTUnbound ( Expression pExpression )
  {
    this.unboundList = new LinkedList < Expression > ( ) ;
    this.unboundList = findUnbound ( pExpression ) ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  public LinkedList < Expression > findUnbound ( Expression pExpression )
  {
    LinkedList < Expression > result = new LinkedList < Expression > ( ) ;
    LinkedList < String > unbound = new LinkedList < String > ( ) ;
    findUnbound ( result , unbound , pExpression ) ;
    return result ;
  }


  /**
   * TODO
   * 
   * @param pResult
   * @param pUnbound
   * @param pExpression
   */
  private void findUnbound ( LinkedList < Expression > pResult ,
      LinkedList < String > pUnbound , Expression pExpression )
  {
    if ( pExpression.free ( ).size ( ) == 0 )
    {
      return ;
    }
    if ( ( pExpression instanceof Identifier )
        && ( ! pUnbound.contains ( ( ( Identifier ) pExpression ).getName ( ) ) ) )
    {
      pResult.add ( pExpression ) ;
      return ;
    }
    if ( pExpression instanceof CurriedLetRec )
    {
      CurriedLetRec curriedLetRec = ( CurriedLetRec ) pExpression ;
      LinkedList < String > newUnbound1 = new LinkedList < String > ( ) ;
      LinkedList < String > newUnbound2 = new LinkedList < String > ( ) ;
      for ( String unbound : pUnbound )
      {
        newUnbound1.add ( unbound ) ;
        newUnbound2.add ( unbound ) ;
      }
      for ( int i = 0 ; i < curriedLetRec.getIdentifiers ( ).length ; i ++ )
      {
        newUnbound1.add ( curriedLetRec.getIdentifiers ( i ) ) ;
      }
      newUnbound2.add ( curriedLetRec.getIdentifiers ( 0 ) ) ;
      findUnbound ( pResult , newUnbound1 , curriedLetRec.getE1 ( ) ) ;
      findUnbound ( pResult , newUnbound2 , curriedLetRec.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof CurriedLet )
    {
      CurriedLet curriedLet = ( CurriedLet ) pExpression ;
      LinkedList < String > newUnbound1 = new LinkedList < String > ( ) ;
      LinkedList < String > newUnbound2 = new LinkedList < String > ( ) ;
      for ( String unbound : pUnbound )
      {
        newUnbound1.add ( unbound ) ;
        newUnbound2.add ( unbound ) ;
      }
      for ( int i = 1 ; i < curriedLet.getIdentifiers ( ).length ; i ++ )
      {
        newUnbound1.add ( curriedLet.getIdentifiers ( i ) ) ;
      }
      newUnbound2.add ( curriedLet.getIdentifiers ( 0 ) ) ;
      findUnbound ( pResult , newUnbound1 , curriedLet.getE1 ( ) ) ;
      findUnbound ( pResult , newUnbound2 , curriedLet.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof MultiLet )
    {
      MultiLet multiLet = ( MultiLet ) pExpression ;
      LinkedList < String > newUnbound1 = new LinkedList < String > ( ) ;
      LinkedList < String > newUnbound2 = new LinkedList < String > ( ) ;
      for ( String unbound : pUnbound )
      {
        newUnbound1.add ( unbound ) ;
        newUnbound2.add ( unbound ) ;
      }
      for ( int i = 0 ; i < multiLet.getIdentifiers ( ).length ; i ++ )
      {
        newUnbound2.add ( multiLet.getIdentifiers ( i ) ) ;
      }
      findUnbound ( pResult , newUnbound1 , multiLet.getE1 ( ) ) ;
      findUnbound ( pResult , newUnbound2 , multiLet.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof MultiLambda )
    {
      MultiLambda multiLambda = ( MultiLambda ) pExpression ;
      LinkedList < String > newUnbound = new LinkedList < String > ( ) ;
      for ( String unbound : pUnbound )
      {
        newUnbound.add ( unbound ) ;
      }
      for ( int i = 0 ; i < multiLambda.getIdentifiers ( ).length ; i ++ )
      {
        newUnbound.add ( multiLambda.getIdentifiers ( i ) ) ;
      }
      findUnbound ( pResult , newUnbound , multiLambda.getE ( ) ) ;
    }
    else if ( pExpression instanceof LetRec )
    {
      LetRec letRec = ( LetRec ) pExpression ;
      LinkedList < String > newUnbound1 = new LinkedList < String > ( ) ;
      LinkedList < String > newUnbound2 = new LinkedList < String > ( ) ;
      for ( String unbound : pUnbound )
      {
        newUnbound1.add ( unbound ) ;
        newUnbound2.add ( unbound ) ;
      }
      newUnbound1.add ( letRec.getId ( ) ) ;
      newUnbound2.add ( letRec.getId ( ) ) ;
      findUnbound ( pResult , newUnbound1 , letRec.getE1 ( ) ) ;
      findUnbound ( pResult , newUnbound2 , letRec.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof Let )
    {
      Let let = ( Let ) pExpression ;
      LinkedList < String > newUnbound = new LinkedList < String > ( ) ;
      for ( String unbound : pUnbound )
      {
        newUnbound.add ( unbound ) ;
      }
      newUnbound.add ( let.getId ( ) ) ;
      findUnbound ( pResult , pUnbound , let.getE1 ( ) ) ;
      findUnbound ( pResult , newUnbound , let.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof Lambda )
    {
      Lambda lambda = ( Lambda ) pExpression ;
      LinkedList < String > newUnbound = new LinkedList < String > ( ) ;
      for ( String unbound : pUnbound )
      {
        newUnbound.add ( unbound ) ;
      }
      newUnbound.add ( lambda.getId ( ) ) ;
      findUnbound ( pResult , newUnbound , lambda.getE ( ) ) ;
    }
    else if ( pExpression instanceof Recursion )
    {
      Recursion recursion = ( Recursion ) pExpression ;
      LinkedList < String > newUnbound = new LinkedList < String > ( ) ;
      for ( String unbound : pUnbound )
      {
        newUnbound.add ( unbound ) ;
      }
      newUnbound.add ( recursion.getId ( ) ) ;
      findUnbound ( pResult , newUnbound , recursion.getE ( ) ) ;
    }
    else
    {
      Enumeration < Expression > children = pExpression.children ( ) ;
      while ( children.hasMoreElements ( ) )
      {
        Expression child = children.nextElement ( ) ;
        findUnbound ( pResult , pUnbound , child ) ;
      }
    }
  }


  /**
   * TODO
   * 
   * @param pIndex
   * @return TODO
   */
  public Expression get ( int pIndex )
  {
    return this.unboundList.get ( pIndex ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int size ( )
  {
    return this.unboundList.size ( ) ;
  }
}
