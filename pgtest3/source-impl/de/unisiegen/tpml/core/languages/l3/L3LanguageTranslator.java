package de.unisiegen.tpml.core.languages.l3 ;


import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.EmptyList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Fst ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.List ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Projection ;
import de.unisiegen.tpml.core.expressions.Snd ;
import de.unisiegen.tpml.core.expressions.Tuple ;
import de.unisiegen.tpml.core.expressions.UnaryCons ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Language translator for the <code>L3</code> language.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:822 $
 * @see L2Language
 * @see L2LanguageTranslator
 * @see L3Language
 */
public class L3LanguageTranslator extends L2LanguageTranslator
{
  /**
   * Allocates a new <code>L3LanguageTranslator</code>.
   */
  public L3LanguageTranslator ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see L2LanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  @ Override
  public Expression translateToCoreSyntax ( final Expression pExpression ,
      final boolean recursive )
  {
    Expression expression = pExpression ;
    if ( expression instanceof Fst )
    {
      final Fst fst = ( Fst ) expression ;
      return new Projection ( fst.getArity ( ) , fst.getIndex ( ) ) ;
    }
    else if ( expression instanceof List )
    {
      final List list = ( List ) expression ;
      final Expression [ ] expressions = list.getExpressions ( ) ;
      expression = new EmptyList ( ) ;
      for ( int n = expressions.length - 1 ; n >= 0 ; -- n )
      {
        Expression e = expressions [ n ] ;
        if ( recursive )
        {
          e = this.translateToCoreSyntax ( e , true ) ;
        }
        expression = new Application ( new UnaryCons ( ) , new Tuple (
            new Expression [ ]
            { e , expression } ) ) ;
      }
      return expression ;
    }
    else if ( expression instanceof MultiLambda )
    {
      // determine the MultiLambda components
      final MultiLambda multiLambda = ( MultiLambda ) expression ;
      final Identifier [ ] identifiers = multiLambda.getIdentifiers ( ) ;
      Expression e = multiLambda.getE ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        e = this.translateToCoreSyntax ( e , true ) ;
      }
      // generate a new unique identifier to be used for the tuple parameter
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
      boundRenaming.add ( e.getIdentifiersFree ( ) ) ;
      boundRenaming.add ( identifiers ) ;
      Identifier newId = boundRenaming.newIdentifier ( new Identifier ( "id" ) ) ; //$NON-NLS-1$
      // generate the required let's
      for ( int n = identifiers.length - 1 ; n >= 0 ; -- n )
      {
        e = new Let ( identifiers [ n ] , null , new Application (
            new Projection ( identifiers.length , n + 1 ) , newId ) , e ) ;
      }
      // and return the new lambda expression
      return new Lambda ( newId , multiLambda.getTau ( ) , e ) ;
    }
    else if ( expression instanceof MultiLet )
    {
      // determine the MultiLet components
      final MultiLet multiLet = ( MultiLet ) expression ;
      final Identifier [ ] identifiers = multiLet.getIdentifiers ( ) ;
      Expression e1 = multiLet.getE1 ( ) ;
      Expression e2 = multiLet.getE2 ( ) ;
      // check if we should recurse
      if ( recursive )
      {
        e1 = this.translateToCoreSyntax ( e1 , true ) ;
        e2 = this.translateToCoreSyntax ( e2 , true ) ;
      }
      // generate a new unique identifier to be used for the tuple parameter
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
      boundRenaming.add ( e1.getIdentifiersFree ( ) ) ;
      boundRenaming.add ( e2.getIdentifiersFree ( ) ) ;
      boundRenaming.add ( identifiers ) ;
      Identifier newId = boundRenaming.newIdentifier ( new Identifier ( "id" ) ) ; //$NON-NLS-1$
      // generate the required let's
      for ( int n = identifiers.length - 1 ; n >= 0 ; -- n )
      {
        e2 = new Let ( identifiers [ n ] , null , new Application (
            new Projection ( identifiers.length , n + 1 ) , newId ) , e2 ) ;
      }
      // and return the new let expression
      return new Let ( newId , multiLet.getTau ( ) , e1 , e2 ) ;
    }
    else if ( expression instanceof Snd )
    {
      final Snd snd = ( Snd ) expression ;
      return new Projection ( snd.getArity ( ) , snd.getIndex ( ) ) ;
    }
    else if ( ( expression instanceof Tuple ) && recursive )
    {
      final Expression [ ] oldExpressions = ( ( Tuple ) expression )
          .getExpressions ( ) ;
      final Expression [ ] newExpressions = new Expression [ oldExpressions.length ] ;
      for ( int n = 0 ; n < oldExpressions.length ; ++ n )
      {
        newExpressions [ n ] = this.translateToCoreSyntax (
            oldExpressions [ n ] , true ) ;
      }
      return new Tuple ( newExpressions ) ;
    }
    else
    {
      return super.translateToCoreSyntax ( expression , recursive ) ;
    }
  }
}
