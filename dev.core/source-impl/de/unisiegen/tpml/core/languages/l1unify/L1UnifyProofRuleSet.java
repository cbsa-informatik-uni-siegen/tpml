package de.unisiegen.tpml.core.languages.l1unify;


import de.unisiegen.tpml.core.entities.DefaultTypeEquation;
import de.unisiegen.tpml.core.entities.DefaultTypeEquationList;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.unify.AbstractUnifyProofRuleSet;
import de.unisiegen.tpml.core.unify.UnifyProofContext;
import de.unisiegen.tpml.core.unify.UnifyProofNode;


/**
 * The type proof rules for the <code>L1</code> language.
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see AbstractUnifyProofRuleSet
 */
public class L1UnifyProofRuleSet extends AbstractUnifyProofRuleSet
{

  /**
   * Allocates a new <code>L1UnifyProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L1UnifyProofRuleSet ( L1UNIFYLanguage language )
  {
    super ( language );
    // register the type rules
    registerByMethodName ( L1UNIFYLanguage.L1_UNIFY, "EMPTY", "applyEmpty" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1UNIFYLanguage.L1_UNIFY, "TRIV", "applyTriv" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1UNIFYLanguage.L1_UNIFY, "ARROW", "applyArrow" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1UNIFYLanguage.L1_UNIFY, "VAR", "applyVar" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1UNIFYLanguage.L1_UNIFY, "STRUCT", "applyStruct" ); //$NON-NLS-1$ //$NON-NLS-2$

  }


  /**
   * Applies the <b>(EMPTY)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the unify proof context.
   * @param pNode the unify proof node.
   */
  public void applyEmpty ( UnifyProofContext context, UnifyProofNode pNode )
  {
    if ( pNode.getTypeEquationList ().isEmpty () )
      context.addProofNode ( pNode, pNode.getTypeSubstitutions () );
    throw new RuntimeException (
        "non empty type substitutionlist within applyEmpty" ); //$NON-NLS-1$
  }


  /**
   * Applies the <b>(TRIV)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the unify proof context.
   * @param pNode the unify proof node.
   */
  public void applyTriv ( UnifyProofContext context, UnifyProofNode pNode )
  {
    DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) pNode
        .getTypeEquationList ();
    if ( dtel.getFirst ().getLeft ().equals ( dtel.getFirst ().getRight () ) )
      context.addProofNode ( pNode, pNode.getTypeSubstitutions (), pNode
          .getTypeEquationList ().getRemaining () );
    throw new RuntimeException ( "type mismatch in applyTriv" ); //$NON-NLS-1$
  }


  /**
   * Applies the <b>(ARROW)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the unify proof context.
   * @param pNode the unify proof node.
   */
  public void applyArrow ( UnifyProofContext context, UnifyProofNode pNode )
  {
    DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) pNode
        .getTypeEquationList ();

    // get left and right side of the equation
    ArrowType left = ( ArrowType ) dtel.getFirst ().getLeft ();
    ArrowType right = ( ArrowType ) dtel.getFirst ().getRight ();

    // add new type equations
    dtel.extend ( new DefaultTypeEquation ( left.getTau1 (), right.getTau1 (),
        null ) );
    dtel.extend ( new DefaultTypeEquation ( left.getTau2 (), right.getTau2 (),
        null ) );

    context.addProofNode ( pNode, pNode.getTypeSubstitutions (), dtel
        .getRemaining () );
  }


  /**
   * Applies the <b>(VAR)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the unify proof context.
   * @param pNode the unify proof node.
   */
  public void applyVar ( UnifyProofContext context, UnifyProofNode pNode )
  {
    DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) pNode
        .getTypeEquationList ();

    MonoType left = dtel.getFirst ().getLeft ();
    MonoType right = dtel.getFirst ().getRight ();

    /*
     * now we check if we have something like tau = type_variable or
     * type_variable = tau
     */
    TypeVariable typevar;
    MonoType type;
    if ( left instanceof TypeVariable )
    {
      typevar = ( TypeVariable ) left;
      type = right;
    }
    else if ( right instanceof TypeVariable )
    {
      typevar = ( TypeVariable ) right;
      type = left;
    }
    else
      throw new RuntimeException ( "either left nor right is a type variable" ); //$NON-NLS-1$

    /*
     * ok, we have our type variable and monotype now we (1) create a new type
     * substitution (2) apply the substitution to the remaining type equation
     * list and (3) create a new proof node
     */

    // (1)
    TypeSubstitutionList dts = pNode.getTypeSubstitutions ();
    DefaultTypeSubstitution s = new DefaultTypeSubstitution ( typevar, type );
    dts.extend ( s );

    // (2) and (3)
    context.addProofNode ( pNode, dts, dtel.getRemaining ().substitute ( s ) );
  }


  /**
   * Applies the <b>(STRUCT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the unify proof context.
   * @param pNode the unify proof node.
   */
  public void applyStruct ( UnifyProofContext context, UnifyProofNode pNode )
  {
    DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) pNode
        .getTypeEquationList ();

  }
}
