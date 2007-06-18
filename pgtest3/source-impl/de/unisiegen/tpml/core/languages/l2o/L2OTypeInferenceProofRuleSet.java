package de.unisiegen.tpml.core.languages.l2o ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode ;
import de.unisiegen.tpml.core.typeinference.DefaultTypeEquationProofNode ;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofContext ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.typeinference.UnifyException ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.ObjectType ;
import de.unisiegen.tpml.core.types.RecType ;
import de.unisiegen.tpml.core.types.RowType ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.core.util.Debug ;


/**
 * The type inference rules for the <code>L2O</code> language.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public class L2OTypeInferenceProofRuleSet extends L2OTypeCheckerProofRuleSet
{
  /**
   * Allocates a new <code>L2OTypeInferenceProofRuleSet</code> for the
   * specified <code>language</code>.
   * 
   * @param language The <code>L2O</code> or a derived language.
   */
  public L2OTypeInferenceProofRuleSet ( L2OLanguage language )
  {
    super ( language ) ;
    unregister ( "ABSTR" ) ; //$NON-NLS-1$ 
    unregister ( "AND" ) ;//$NON-NLS-1$ 
    unregister ( "APP" ) ;//$NON-NLS-1$ 
    unregister ( "COND" ) ;//$NON-NLS-1$ 
    unregister ( "CONST" ) ;//$NON-NLS-1$ 
    unregister ( "ID" ) ;//$NON-NLS-1$ 
    unregister ( "LET" ) ;//$NON-NLS-1$ 
    unregister ( "OR" ) ;//$NON-NLS-1$ 
    unregister ( "REC" ) ; //$NON-NLS-1$ 
    unregister ( "SEND" ) ; //$NON-NLS-1$ 
    unregister ( "OBJECT" ) ; //$NON-NLS-1$ 
    unregister ( "DUPL" ) ; //$NON-NLS-1$
    unregister ( "EMPTY" ) ; //$NON-NLS-1$ 
    unregister ( "ATTR" ) ; //$NON-NLS-1$ 
    unregister ( "METHOD" ) ; //$NON-NLS-1$ 
    // register the type rules
    registerByMethodName ( L1Language.L1 , "UNIFY" , "applyUnify" ) ; //$NON-NLS-1$//$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "ABSTR" , "applyAbstr" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "AND" , "applyAnd" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "APP" , "applyApp" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "COND" , "applyCond" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "CONST" , "applyConst" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "ID" , "applyId" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "LET" , "applyLet" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "OR" , "applyOr" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "REC" , "applyRec" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "SEND" , "applySend" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "OBJECT" , "applyObject" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "DUPL" , "applyDupl" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "EMPTY" , "applyEmpty" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "ATTR" , "applyAttr" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "METHOD" , "applyMethod" ) ; //$NON-NLS-1$ //$NON-NLS-2$ 
  }


  /**
   * Applies the <b>(UNIFY)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext the type inference proof context.
   * @param pNode the type inference proof node.
   * @throws UnifyException
   */
  public void applyUnify ( TypeCheckerProofContext pContext ,
      TypeCheckerProofNode pNode ) throws UnifyException
  {
    // convert in needed types
    DefaultTypeInferenceProofContext context = ( DefaultTypeInferenceProofContext ) pContext ;
    DefaultTypeEquationProofNode node = ( DefaultTypeEquationProofNode ) pNode ;
    TypeEquationTypeInference eqn = node.getEquation ( ) ;
    unify ( context , node , eqn ) ;
  }


  /**
   * internal implementation of the unify rule now we are able to call unify
   * recursive if needed so we get different handling for beginner or advanced
   * user
   * 
   * @param context the casted default type inference proof context.
   * @param node the casted type equation proof node.
   * @param eqn the actual type equation
   * @throws UnifyException
   */
  public void unify ( DefaultTypeInferenceProofContext context ,
      DefaultTypeEquationProofNode node , TypeEquationTypeInference eqn )
      throws UnifyException
  {
    // empty equation is not longer possible so this rule is not implemented
    MonoType left = eqn.getLeft ( ) ;
    MonoType right = eqn.getRight ( ) ;
    // ASSUME
    if ( context.getSeenTypes ( ).contains ( eqn ) )
    {
      Debug.out.println (
          "Unify - ASSUME:     " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      return ;
    }
    // TRIV
    else if ( left.equals ( right ) )
    {
      Debug.out.println (
          "Unify - TRIV        " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      context.addSeenType ( eqn ) ;
      return ;
    }
    // MU-LEFT
    else if ( left instanceof RecType )
    {
      RecType recType = ( RecType ) left ;
      Debug.out.println (
          "Unify - MU-LEFT:    " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      context.addEquation ( recType.getTau ( ).substitute (
          recType.getTypeName ( ) , recType ) , right ) ;
      context.addSeenType ( eqn ) ;
      return ;
    }
    // MU-RIGHT
    else if ( right instanceof RecType )
    {
      RecType recType = ( RecType ) right ;
      Debug.out.println (
          "Unify - MU-RIGHT:   " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      context.addEquation ( left , recType.getTau ( ).substitute (
          recType.getTypeName ( ) , recType ) ) ;
      context.addSeenType ( eqn ) ;
      return ;
    }
    // VAR
    else if ( left instanceof TypeVariable || right instanceof TypeVariable )
    {
      // the left or right side of the equation is a type variable
      TypeVariable tvar = ( TypeVariable ) ( left instanceof TypeVariable ? left
          : right ) ;
      MonoType tau = ( left instanceof TypeVariable ? right : left ) ;
      // either tvar equals tau or tvar is not present in tau
      if ( ! tvar.equals ( tau )
          && ! tau.getTypeVariablesFree ( ).contains ( tvar ) )
      {
        context.addSeenType ( eqn ) ;
        DefaultTypeSubstitution s = new DefaultTypeSubstitution ( tvar , tau ) ;
        context.addSubstitution ( s ) ;
        return ;
      }
      // Error, because of a recursive type like: alpha1 = int -> alpha1
    }
    // ARROW
    else if ( left instanceof ArrowType && right instanceof ArrowType )
    {
      ArrowType taul = ( ArrowType ) left ;
      ArrowType taur = ( ArrowType ) right ;
      // check which mode is choosen
      if ( node.getMode ( ) )
      {
        // advanced mode is choosen
        // unify tau1 = tau1', tau2 = tau2'
        context.addSeenType ( eqn ) ;
        unify ( context , node , new TypeEquationTypeInference ( taul
            .getTau2 ( ) , taur.getTau2 ( ) ) ) ;
        TypeEquationTypeInference eqn2 = new TypeEquationTypeInference ( taul
            .getTau1 ( ) , taur.getTau1 ( ) ) ;
        eqn2 = ( TypeEquationTypeInference ) eqn2.substitute ( context
            .getSubstitution ( ) ) ;
        unify ( context , node , eqn2 ) ;
      }
      else
      {
        // beginner mode is choosen
        // equations are added to list and will be unified later
        context.addSeenType ( eqn ) ;
        context.addEquation ( taul.getTau2 ( ) , taur.getTau2 ( ) ) ;
        context.addEquation ( taul.getTau1 ( ) , taur.getTau1 ( ) ) ;
      }
      return ;
    }
    // OBJECT
    else if ( ( left instanceof ObjectType ) && ( right instanceof ObjectType ) )
    {
      ObjectType tau1 = ( ObjectType ) left ;
      ObjectType tau2 = ( ObjectType ) right ;
      context.addEquation ( tau1.getPhi ( ) , tau2.getPhi ( ) ) ;
      context.addSeenType ( eqn ) ;
      return ;
    }
    // ROW
    else if ( ( left instanceof RowType ) && ( right instanceof RowType ) )
    {
      RowType tau1 = ( RowType ) left ;
      RowType tau2 = ( RowType ) right ;
      ArrayList < Identifier > tau1Identifiers = new ArrayList < Identifier > ( ) ;
      for ( Identifier id : tau1.getIdentifiers ( ) )
      {
        tau1Identifiers.add ( id ) ;
      }
      ArrayList < Identifier > tau2Identifiers = new ArrayList < Identifier > ( ) ;
      for ( Identifier id : tau2.getIdentifiers ( ) )
      {
        tau2Identifiers.add ( id ) ;
      }
      ArrayList < MonoType > tau1Types = new ArrayList < MonoType > ( ) ;
      for ( MonoType tau : tau1.getTypes ( ) )
      {
        tau1Types.add ( tau ) ;
      }
      ArrayList < MonoType > tau2Types = new ArrayList < MonoType > ( ) ;
      for ( MonoType tau : tau2.getTypes ( ) )
      {
        tau2Types.add ( tau ) ;
      }
      // Unify child types
      for ( int i = tau1Identifiers.size ( ) - 1 ; i >= 0 ; i -- )
      {
        for ( int j = tau2Identifiers.size ( ) - 1 ; j >= 0 ; j -- )
        {
          if ( tau1Identifiers.get ( i ).equals ( tau2Identifiers.get ( j ) ) )
          {
            context.addEquation ( tau1Types.get ( i ) , tau2Types.get ( j ) ) ;
            tau1Identifiers.remove ( i ) ;
            tau1Types.remove ( i ) ;
            tau2Identifiers.remove ( j ) ;
            tau2Types.remove ( j ) ;
            break ;
          }
        }
      }
      // Remaining RowType
      MonoType tau1RemainingRow = tau1.getRemainingRowType ( ) ;
      MonoType tau2RemainingRow = tau2.getRemainingRowType ( ) ;
      // First and second remaining RowTypes
      if ( ( tau1RemainingRow != null ) && ( tau2RemainingRow != null ) )
      {
        context.addEquation ( tau1RemainingRow , tau2RemainingRow ) ;
        if ( ( tau1Identifiers.size ( ) > 0 )
            || ( tau2Identifiers.size ( ) > 0 ) )
        {
          throw new RuntimeException ( MessageFormat.format ( Messages
              .getString ( "UnificationException.3" ) , left , right ) ) ; //$NON-NLS-1$
        }
        context.addSeenType ( eqn ) ;
        return ;
      }
      // First remaining RowType
      if ( tau1RemainingRow != null )
      {
        if ( tau2Identifiers.size ( ) > 0 )
        {
          Identifier [ ] newIdentifiers = new Identifier [ tau2Identifiers
              .size ( ) ] ;
          MonoType [ ] newTypes = new MonoType [ tau2Types.size ( ) ] ;
          for ( int i = tau2Identifiers.size ( ) - 1 ; i >= 0 ; i -- )
          {
            newIdentifiers [ i ] = tau2Identifiers.get ( i ) ;
            newTypes [ i ] = tau2Types.get ( i ) ;
            tau2Identifiers.remove ( i ) ;
            tau2Types.remove ( i ) ;
          }
          RowType newRowType = new RowType ( newIdentifiers , newTypes ) ;
          context.addEquation ( tau1RemainingRow , newRowType ) ;
        }
      }
      // Second remaining RowType
      if ( tau2RemainingRow != null )
      {
        if ( tau1Identifiers.size ( ) > 0 )
        {
          Identifier [ ] newIdentifiers = new Identifier [ tau1Identifiers
              .size ( ) ] ;
          MonoType [ ] newTypes = new MonoType [ tau1Types.size ( ) ] ;
          for ( int i = tau1Identifiers.size ( ) - 1 ; i >= 0 ; i -- )
          {
            newIdentifiers [ i ] = tau1Identifiers.get ( i ) ;
            newTypes [ i ] = tau1Types.get ( i ) ;
            tau1Identifiers.remove ( i ) ;
            tau1Types.remove ( i ) ;
          }
          RowType newRowType = new RowType ( newIdentifiers , newTypes ) ;
          context.addEquation ( tau2RemainingRow , newRowType ) ;
        }
      }
      if ( ( tau1Identifiers.size ( ) > 0 ) || ( tau2Identifiers.size ( ) > 0 ) )
      {
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "UnificationException.3" ) , left , right ) ) ; //$NON-NLS-1$
      }
      context.addSeenType ( eqn ) ;
      return ;
    }
    throw new UnifyException ( eqn ) ;
  }
}
