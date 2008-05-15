package de.unisiegen.tpml.core.languages.l1unify;


import de.unisiegen.tpml.core.entities.DefaultTypeEquation;
import de.unisiegen.tpml.core.entities.DefaultTypeEquationList;
import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
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
   * checks wheather the (EMPTY) rule is appliable or not
   * 
   * @param node the node we want to apply (EMPTY)
   * @return true if (EMPTY) is appliable false otherwise
   */
  private boolean checkEmptyRuleAppliable ( final UnifyProofNode node )
  {
    return node.getTypeEquationList ().isEmpty ();
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
    if ( !checkEmptyRuleAppliable ( pNode ) )
      throw new RuntimeException (
          "non empty type equation list within applyEmpty" ); //$NON-NLS-1$
  }


  /**
   * checks wheather the (TRIV) rule is appliable or not
   * 
   * @param node the node we want to apply (TRIV)
   * @return true if (TRIV) is appliable false otherwise
   */
  private boolean checkTrivRuleAppliable ( final UnifyProofNode node )
  {
    DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) node
        .getTypeEquationList ();
    return dtel.getFirst ().getLeft ().equals ( dtel.getFirst ().getRight () );
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
    if ( checkTrivRuleAppliable ( pNode ) )
    {
      /*
       * TODO: remove after testing the new error checking code
       * DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) pNode
       * .getTypeEquationList (); MonoType left = dtel.getFirst ().getLeft ();
       * MonoType right = dtel.getFirst ().getRight (); if ( left.equals ( right ) )
       */
      context.addProofNode ( pNode, pNode.getTypeSubstitutions (), pNode
          .getTypeEquationList ().getRemaining () );
    }
    else
      throw new RuntimeException ( "type mismatch in applyTriv" ); //$NON-NLS-1$
  }


  /**
   * checks wheather the (ARROW) rule is appliable or not
   * 
   * @param node the node we want to apply (ARROW)
   * @return true if (ARROW) is appliable false otherwise
   */
  private boolean checkArrowRuleAppliable ( final UnifyProofNode node )
  {
    DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) node
        .getTypeEquationList ();
    return !dtel.getFirst ().getLeft ().equals ( dtel.getFirst ().getRight () );
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
    if ( checkArrowRuleAppliable ( pNode ) )
    {
      DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) pNode
          .getTypeEquationList ();

      // get left and right side of the equation
      ArrowType left = ( ArrowType ) dtel.getFirst ().getLeft ();
      ArrowType right = ( ArrowType ) dtel.getFirst ().getRight ();

      // add new type equations
      dtel = ( DefaultTypeEquationList ) dtel
          .extend ( new DefaultTypeEquation ( left.getTau1 (),
              right.getTau1 (), new SeenTypes < TypeEquation > () ) );
      dtel = ( DefaultTypeEquationList ) dtel
          .extend ( new DefaultTypeEquation ( left.getTau2 (),
              right.getTau2 (), new SeenTypes < TypeEquation > () ) );

      context.addProofNode ( pNode, pNode.getTypeSubstitutions (), dtel
          .getRemaining () );
    }
    else
      throw new RuntimeException ( "(ARROW) not applieable " ); //$NON-NLS-1$
  }


  /**
   * checks wheather the type variable <code>tau</code> is part of
   * <code>type</code>
   * 
   * @param tau the type variable
   * @param type a type
   * @return true if tau appears in type, false otherwise
   */
  private boolean typevarRecrusion ( TypeVariable tau, Type type )
  {
    if ( tau.equals ( type ) )
      return true;
    return type.getTypeVariablesFree ().contains ( tau );
  }


  /**
   * checks wheather the (VAR) rule is appliable or not
   * 
   * @param node the node we want to apply (VAR)
   * @return true if (VAR) is appliable false otherwise
   */
  private boolean checkVarRuleAppliable ( final UnifyProofNode node )
  {
    DefaultTypeEquationList dtel = ( DefaultTypeEquationList ) node
        .getTypeEquationList ();
    MonoType left = dtel.getFirst ().getLeft ();
    MonoType right = dtel.getFirst ().getRight ();
    return !left.equals ( right )
        && ( left instanceof TypeVariable || right instanceof TypeVariable );
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
    if ( checkVarRuleAppliable ( pNode ) )
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

      if ( typevarRecrusion ( typevar, type ) )
        context.addProofNode ( pNode );
      else
      {
        /*
         * ok, we have our type variable and monotype now we (1) create a new
         * type substitution (2) apply the substitution to the remaining type
         * substitution list and extend those list and (3) apply the substitution
         * to the remaining type equation list and (4) create a new proof node
         */

        // (1)
        TypeSubstitutionList dts = pNode.getTypeSubstitutions ();
        DefaultTypeSubstitution s = new DefaultTypeSubstitution ( typevar, type );
        
        // (2)
        //TODO: { 'a -> int = int -> 'b, 'a = int }  will result in a list with two
        //      int/'a substitutions; we have to correct TypeSubstitutionList.substitute !!!
        dts = dts.substitute ( s );
        dts = dts.extend ( s );

        // (3) and (4)
        context.addProofNode ( pNode, dts, dtel.getRemaining ().substitute ( s ) );
      }
    }
    else
      throw new RuntimeException ( "either left nor right is a type variable" ); //$NON-NLS-1$
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
    if ( !checkEmptyRuleAppliable ( pNode ) && !checkTrivRuleAppliable ( pNode )
        && checkArrowRuleAppliable ( pNode ) && !checkVarRuleAppliable ( pNode ) )
      context.addProofNode ( pNode );
    else
      throw new RuntimeException ( "struct is not appliable" ); //$NON-NLS-1$
  }
}
