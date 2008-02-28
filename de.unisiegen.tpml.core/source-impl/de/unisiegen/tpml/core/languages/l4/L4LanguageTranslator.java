package de.unisiegen.tpml.core.languages.l4;


import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.BinaryCons;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.expressions.UnaryCons;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator;
import de.unisiegen.tpml.core.languages.l3.L3Language;
import de.unisiegen.tpml.core.languages.l3.L3LanguageTranslator;
import de.unisiegen.tpml.core.types.UnitType;
import de.unisiegen.tpml.core.util.BoundRenaming;


/**
 * Language translator for the <code>L4</code> language.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev: 287 $
 * @see L3Language
 * @see L3LanguageTranslator
 * @see L4Language
 */
public class L4LanguageTranslator extends L3LanguageTranslator
{

  /**
   * Allocates a new <code>L4LanguageTranslator</code>.
   */
  public L4LanguageTranslator ()
  {
    super ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see L2LanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  @Override
  public Expression translateToCoreSyntax ( final Expression expression,
      final boolean recursive )
  {
    if ( ( expression instanceof Application )
        && ( ( ( Application ) expression ).getE1 () instanceof Application )
        && ( ( ( Application ) ( ( Application ) expression ).getE1 () )
            .getE1 () instanceof BinaryCons ) )
    {
      // this is: ((::) e1) e2
      Expression e1 = ( ( Application ) ( ( Application ) expression ).getE1 () )
          .getE2 ();
      Expression e2 = ( ( Application ) expression ).getE2 ();
      // check if we should recurse
      if ( recursive )
      {
        // translate e1 and e2
        e1 = this.translateToCoreSyntax ( e1, true );
        e2 = this.translateToCoreSyntax ( e2, true );
      }
      // generate: cons (e1,2)
      return new Application ( new UnaryCons (), new Tuple ( new Expression []
      { e1, e2 } ) );
    }
    else if ( expression instanceof Condition1 )
    {
      // translate to: if e0 then e1 else ()
      final Condition1 condition1 = ( Condition1 ) expression;
      Expression e0 = condition1.getE0 ();
      Expression e1 = condition1.getE1 ();
      // check if we should recurse
      if ( recursive )
      {
        e0 = this.translateToCoreSyntax ( e0, true );
        e1 = this.translateToCoreSyntax ( e1, true );
      }
      // generate the condition
      return new Condition ( e0, e1, new UnitConstant () );
    }
    else if ( expression instanceof Sequence )
    {
      // translate to: let u = e1 in e2
      final Sequence sequence = ( Sequence ) expression;
      Expression e1 = sequence.getE1 ();
      Expression e2 = sequence.getE2 ();
      // check if we should recurse
      if ( recursive )
      {
        e1 = this.translateToCoreSyntax ( e1, true );
        e2 = this.translateToCoreSyntax ( e2, true );
      }
      // determine a new unique identifier
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ();
      boundRenaming.add ( e1.getIdentifiersFree () );
      boundRenaming.add ( e2.getIdentifiersFree () );
      Identifier newId = boundRenaming.newIdentifier ( new Identifier ( "u", //$NON-NLS-1$
          Identifier.Set.VARIABLE ) );
      // generate the let expression
      return new Let ( newId, null, e1, e2 );
    }
    else if ( expression instanceof While )
    {
      // translate to: rec w:unit.if e1 then e2;w
      final While loop = ( While ) expression;
      Expression e1 = loop.getE1 ();
      Expression e2 = loop.getE2 ();
      // check if we should recurse
      if ( recursive )
      {
        e1 = this.translateToCoreSyntax ( e1, true );
        e2 = this.translateToCoreSyntax ( e2, true );
      }
      // determine a new unique identifier
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ();
      boundRenaming.add ( e1.getIdentifiersFree () );
      boundRenaming.add ( e2.getIdentifiersFree () );
      Identifier newId = boundRenaming.newIdentifier ( new Identifier ( "w", //$NON-NLS-1$
          Identifier.Set.VARIABLE ) );
      // generate the recursion body
      Expression body = new Condition1 ( e1, new Sequence ( e2, newId ) );
      // check if we should recurse
      if ( recursive )
      {
        body = this.translateToCoreSyntax ( body, true );
      }
      // generate the recursion
      return new Recursion ( newId, new UnitType (), body );
    }
    else
    {
      return super.translateToCoreSyntax ( expression, recursive );
    }
  }
}
