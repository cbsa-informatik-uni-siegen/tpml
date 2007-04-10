package de.unisiegen.tpml.core.languages.l1 ;


import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.BooleanConstant ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.Or ;
import de.unisiegen.tpml.core.languages.AbstractLanguageTranslator ;
import de.unisiegen.tpml.core.languages.l0.L0LanguageTranslator ;


/**
 * Language translator for the <code>L1</code> language.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:822 $
 * @see AbstractLanguageTranslator
 * @see L1Language
 * @see L1LanguageTranslator
 */
public class L1LanguageTranslator extends L0LanguageTranslator
{
  /**
   * Allocates a new <code>L1LanguageTranslator</code>.
   */
  public L1LanguageTranslator ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  @ Override
  public Expression translateToCoreSyntax ( Expression expression ,
      boolean recursive )
  {
    if ( expression instanceof And )
    {
      // determine the sub expressions
      And and = ( And ) expression ;
      Expression e1 = and.getE1 ( ) ;
      Expression e2 = and.getE2 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        e1 = translateToCoreSyntax ( e1 , true ) ;
        e2 = translateToCoreSyntax ( e2 , true ) ;
      }
      // generate the condition
      return new Condition ( e1 , e2 , new BooleanConstant ( false ) ) ;
    }
    else if ( expression instanceof Condition && recursive )
    {
      // determine the sub expressions
      Condition condition = ( Condition ) expression ;
      Expression e0 = condition.getE0 ( ) ;
      Expression e1 = condition.getE1 ( ) ;
      Expression e2 = condition.getE2 ( ) ;
      // translate the sub expressions
      e0 = translateToCoreSyntax ( e0 , true ) ;
      e1 = translateToCoreSyntax ( e1 , true ) ;
      e2 = translateToCoreSyntax ( e2 , true ) ;
      // generate the condition
      return new Condition ( e0 , e1 , e2 ) ;
    }
    else if ( expression instanceof CurriedLet )
    {
      // translate to: let id1 = lambda id2...lambda idn.e1 in e2
      CurriedLet curriedLet = ( CurriedLet ) expression ;
      Expression e1 = curriedLet.getE1 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        e1 = translateToCoreSyntax ( e1 , true ) ;
      }
      // add the lambdas
      for ( int n = curriedLet.getIdentifiers ( ).length - 1 ; n > 0 ; -- n )
      {
        e1 = new Lambda ( curriedLet.getIdentifiers ( n ).clone ( ) ,
            curriedLet.getTypes ( n ) , e1 ) ;
      }
      // generate the let expression
      return new Let ( curriedLet.getIdentifiers ( 0 ).clone ( ) , curriedLet
          .getTypes ( 0 ) , e1 , curriedLet.getE2 ( ) ) ;
    }
    else if ( expression instanceof InfixOperation )
    {
      // determine the sub expressions
      InfixOperation infixOperation = ( InfixOperation ) expression ;
      Expression op = infixOperation.getOp ( ) ;
      Expression e1 = infixOperation.getE1 ( ) ;
      Expression e2 = infixOperation.getE2 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        op = translateToCoreSyntax ( op , true ) ;
        e1 = translateToCoreSyntax ( e1 , true ) ;
        e2 = translateToCoreSyntax ( e2 , true ) ;
      }
      else
      {
        op = op.clone ( ) ;
        e1 = e1.clone ( ) ;
        e2 = e2.clone ( ) ;
      }
      // generate the applications
      Application application = new Application ( new Application ( op , e1 ) ,
          e2 ) ;
      // special case: BinaryCons requires another run
      return translateToCoreSyntax ( application , recursive ) ;
    }
    else if ( expression instanceof Let && recursive )
    {
      // determine the sub expressions
      Let let = ( Let ) expression ;
      Expression e1 = let.getE1 ( ) ;
      Expression e2 = let.getE2 ( ) ;
      // translate the sub expressions
      e1 = translateToCoreSyntax ( e1 , true ) ;
      e2 = translateToCoreSyntax ( e2 , true ) ;
      // generate the let expression
      return new Let ( let.getId ( ) , let.getTau ( ) , e1 , e2 ) ;
    }
    else if ( expression instanceof Or )
    {
      // determine the sub expressions
      Or or = ( Or ) expression ;
      Expression e1 = or.getE1 ( ) ;
      Expression e2 = or.getE2 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        e1 = translateToCoreSyntax ( e1 , true ) ;
        e2 = translateToCoreSyntax ( e2 , true ) ;
      }
      // generate the condition
      return new Condition ( e1 , new BooleanConstant ( true ) , e2 ) ;
    }
    else
    {
      // dunno, let the parent class handle it
      return super.translateToCoreSyntax ( expression , recursive ) ;
    }
  }
}
