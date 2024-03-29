package de.unisiegen.tpml.core.languages.l1 ;


import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.BooleanConstant ;
import de.unisiegen.tpml.core.expressions.Coercion ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.Or ;
import de.unisiegen.tpml.core.languages.AbstractLanguageTranslator ;
import de.unisiegen.tpml.core.languages.l0.L0LanguageTranslator ;
import de.unisiegen.tpml.core.types.MonoType ;


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
  public Expression translateToCoreSyntax ( final Expression expression ,
      final boolean recursive )
  {
    if ( expression instanceof And )
    {
      // determine the sub expressions
      final And and = ( And ) expression ;
      Expression e1 = and.getE1 ( ) ;
      Expression e2 = and.getE2 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        e1 = this.translateToCoreSyntax ( e1 , true ) ;
        e2 = this.translateToCoreSyntax ( e2 , true ) ;
      }
      // generate the condition
      return new Condition ( e1 , e2 , new BooleanConstant ( false ) ) ;
    }
    else if ( ( expression instanceof Condition ) && recursive )
    {
      // determine the sub expressions
      final Condition condition = ( Condition ) expression ;
      Expression e0 = condition.getE0 ( ) ;
      Expression e1 = condition.getE1 ( ) ;
      Expression e2 = condition.getE2 ( ) ;
      // translate the sub expressions
      e0 = this.translateToCoreSyntax ( e0 , true ) ;
      e1 = this.translateToCoreSyntax ( e1 , true ) ;
      e2 = this.translateToCoreSyntax ( e2 , true ) ;
      // generate the condition
      return new Condition ( e0 , e1 , e2 ) ;
    }
    else if ( expression instanceof CurriedLet )
    {
      // translate to: let id1 = lambda id2...lambda idn.e1 in e2
      final CurriedLet curriedLet = ( CurriedLet ) expression ;
      Expression e1 = curriedLet.getE1 ( ) ;
      Expression e2 = curriedLet.getE2 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        e1 = this.translateToCoreSyntax ( e1 , true ) ;
        e2 = this.translateToCoreSyntax ( e2 , true ) ;
      }
      // add the lambdas
      Identifier [ ] identifiers = curriedLet.getIdentifiers ( ) ;
      MonoType [ ] types = curriedLet.getTypes ( ) ;
      for ( int n = identifiers.length - 1 ; n > 0 ; -- n )
      {
        e1 = new Lambda ( identifiers [ n ] , types [ n ] , e1 ) ;
      }
      // generate the let expression
      return new Let ( identifiers [ 0 ] , types [ 0 ] , e1 , e2 ) ;
    }
    else if ( expression instanceof InfixOperation )
    {
      // determine the sub expressions
      final InfixOperation infixOperation = ( InfixOperation ) expression ;
      Expression op = infixOperation.getOp ( ) ;
      Expression e1 = infixOperation.getE1 ( ) ;
      Expression e2 = infixOperation.getE2 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        op = this.translateToCoreSyntax ( op , true ) ;
        e1 = this.translateToCoreSyntax ( e1 , true ) ;
        e2 = this.translateToCoreSyntax ( e2 , true ) ;
      }
      // generate the applications
      final Application application = new Application ( new Application ( op ,
          e1 ) , e2 ) ;
      // special case: BinaryCons requires another run
      return this.translateToCoreSyntax ( application , recursive ) ;
    }
    else if ( ( expression instanceof Let ) && recursive )
    {
      // determine the sub expressions
      final Let let = ( Let ) expression ;
      Expression e1 = let.getE1 ( ) ;
      Expression e2 = let.getE2 ( ) ;
      // translate the sub expressions
      e1 = this.translateToCoreSyntax ( e1 , true ) ;
      e2 = this.translateToCoreSyntax ( e2 , true ) ;
      // generate the let expression
      return new Let ( let.getId ( ) , let.getTau ( ) , e1 , e2 ) ;
    }
    else if ( expression instanceof Or )
    {
      // determine the sub expressions
      final Or or = ( Or ) expression ;
      Expression e1 = or.getE1 ( ) ;
      Expression e2 = or.getE2 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        e1 = this.translateToCoreSyntax ( e1 , true ) ;
        e2 = this.translateToCoreSyntax ( e2 , true ) ;
      }
      // generate the condition
      return new Condition ( e1 , new BooleanConstant ( true ) , e2 ) ;
    }
    else if ( expression instanceof Coercion )
    {
      if ( recursive )
      {
        Coercion coercion = ( Coercion ) expression ;
        return new Coercion (
            translateToCoreSyntax ( coercion.getE ( ) , true ) , coercion
                .getTau1 ( ) , coercion.getTau2 ( ) ) ;
      }
      return expression ;
    }
    else
    {
      return super.translateToCoreSyntax ( expression , recursive ) ;
    }
  }
}
