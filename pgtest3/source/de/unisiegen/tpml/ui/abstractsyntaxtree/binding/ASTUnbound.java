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
   * @param pList
   * @return TODO
   */
  private LinkedList < String > copyList ( LinkedList < String > pList )
  {
    LinkedList < String > list = new LinkedList < String > ( ) ;
    for ( String tmp : pList )
    {
      list.add ( tmp ) ;
    }
    return list ;
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
      LinkedList < String > unbound1 = copyList ( pUnbound ) ;
      LinkedList < String > unbound2 = copyList ( pUnbound ) ;
      // New bindings in E1, Identifier 0 to n
      for ( int i = 0 ; i < curriedLetRec.getIdentifiers ( ).length ; i ++ )
      {
        unbound1.add ( curriedLetRec.getIdentifiers ( i ) ) ;
      }
      findUnbound ( pResult , unbound1 , curriedLetRec.getE1 ( ) ) ;
      // New bindings in E2, Identifier 0
      unbound2.add ( curriedLetRec.getIdentifiers ( 0 ) ) ;
      findUnbound ( pResult , unbound2 , curriedLetRec.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof CurriedLet )
    {
      CurriedLet curriedLet = ( CurriedLet ) pExpression ;
      LinkedList < String > unbound1 = copyList ( pUnbound ) ;
      LinkedList < String > unbound2 = copyList ( pUnbound ) ;
      // New bindings in E1, Identifier 1 to n
      for ( int i = 1 ; i < curriedLet.getIdentifiers ( ).length ; i ++ )
      {
        unbound1.add ( curriedLet.getIdentifiers ( i ) ) ;
      }
      findUnbound ( pResult , unbound1 , curriedLet.getE1 ( ) ) ;
      // New bindings in E2, Identifier 0
      unbound2.add ( curriedLet.getIdentifiers ( 0 ) ) ;
      findUnbound ( pResult , unbound2 , curriedLet.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof MultiLet )
    {
      MultiLet multiLet = ( MultiLet ) pExpression ;
      LinkedList < String > unbound1 = copyList ( pUnbound ) ;
      LinkedList < String > unbound2 = copyList ( pUnbound ) ;
      // No new binding in E1
      findUnbound ( pResult , unbound1 , multiLet.getE1 ( ) ) ;
      // New bindings in E2
      for ( int i = 0 ; i < multiLet.getIdentifiers ( ).length ; i ++ )
      {
        unbound2.add ( multiLet.getIdentifiers ( i ) ) ;
      }
      findUnbound ( pResult , unbound2 , multiLet.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof MultiLambda )
    {
      MultiLambda multiLambda = ( MultiLambda ) pExpression ;
      LinkedList < String > unbound = copyList ( pUnbound ) ;
      // New bindings in E
      for ( int i = 0 ; i < multiLambda.getIdentifiers ( ).length ; i ++ )
      {
        unbound.add ( multiLambda.getIdentifiers ( i ) ) ;
      }
      findUnbound ( pResult , unbound , multiLambda.getE ( ) ) ;
    }
    else if ( pExpression instanceof LetRec )
    {
      LetRec letRec = ( LetRec ) pExpression ;
      LinkedList < String > unbound1 = copyList ( pUnbound ) ;
      LinkedList < String > unbound2 = copyList ( pUnbound ) ;
      // New binding in E1
      unbound1.add ( letRec.getId ( ) ) ;
      findUnbound ( pResult , unbound1 , letRec.getE1 ( ) ) ;
      // New binding in E2
      unbound2.add ( letRec.getId ( ) ) ;
      findUnbound ( pResult , unbound2 , letRec.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof Let )
    {
      Let let = ( Let ) pExpression ;
      LinkedList < String > unbound = copyList ( pUnbound ) ;
      // No new binding in E1
      findUnbound ( pResult , pUnbound , let.getE1 ( ) ) ;
      // New binding in E2
      unbound.add ( let.getId ( ) ) ;
      findUnbound ( pResult , unbound , let.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof Lambda )
    {
      Lambda lambda = ( Lambda ) pExpression ;
      LinkedList < String > unbound = copyList ( pUnbound ) ;
      // New binding in E
      unbound.add ( lambda.getId ( ) ) ;
      findUnbound ( pResult , unbound , lambda.getE ( ) ) ;
    }
    else if ( pExpression instanceof Recursion )
    {
      Recursion recursion = ( Recursion ) pExpression ;
      LinkedList < String > unbound = copyList ( pUnbound ) ;
      // New binding in E2
      unbound.add ( recursion.getId ( ) ) ;
      findUnbound ( pResult , unbound , recursion.getE ( ) ) ;
    }
    else
    {
      Enumeration < Expression > children = pExpression.children ( ) ;
      while ( children.hasMoreElements ( ) )
      {
        findUnbound ( pResult , pUnbound , children.nextElement ( ) ) ;
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
