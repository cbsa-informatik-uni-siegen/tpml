package de.unisiegen.tpml.core.typechecker ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.ListType ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.ObjectType ;
import de.unisiegen.tpml.core.types.RefType ;
import de.unisiegen.tpml.core.types.RowType ;
import de.unisiegen.tpml.core.types.TupleType ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.core.util.Debug ;


/**
 * A list of <code>TypeEquation</code>s, in the same fashion as a list in
 * OCaml.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:926M $
 * @see de.unisiegen.tpml.core.typechecker.TypeEquation
 */
public final class TypeEquationList
{
  //
  // Constants
  //
  /**
   * The empty equation list.
   * 
   * @see #TypeEquationList()
   */
  public static final TypeEquationList EMPTY_LIST = new TypeEquationList ( ) ;


  //
  // Attributes
  //
  /**
   * The first equation in the list.
   */
  public TypeEquation first ;


  /**
   * The remaining equations or <code>null</code>.
   */
  public TypeEquationList remaining ;


  //
  // Constructors (private)
  //
  /**
   * Allocates a new empty equation list.
   * 
   * @see #EMPTY_LIST
   */
  private TypeEquationList ( )
  {
    super ( ) ;
  }


  /**
   * Allocates a new equation list, which basicly extends <code>remaining</code>
   * with a new {@link TypeEquation} <code>first</code>.
   * 
   * @param pFirst the new {@link TypeEquation}.
   * @param pRemaining an existing {@link TypeEquationList}
   * @throws NullPointerException if <code>first</code> or
   *           <code>remaining</code> is <code>null</code>.
   */
  private TypeEquationList ( TypeEquation pFirst , TypeEquationList pRemaining )
  {
    if ( pFirst == null )
    {
      throw new NullPointerException ( "First is null" ) ; //$NON-NLS-1$
    }
    if ( pRemaining == null )
    {
      throw new NullPointerException ( "Remaining is null" ) ; //$NON-NLS-1$
    }
    this.first = pFirst ;
    this.remaining = pRemaining ;
  }


  //
  // Primitives
  //
  /**
   * Allocates a new {@link TypeEquationList}, which extends this equation list
   * with a new {@link TypeEquation} for <code>left</code> and
   * <code>right</code>.
   * 
   * @param left the left side of the new equation.
   * @param right the right side of the new equation.
   * @return the extended {@link TypeEquationList}.
   * @throws NullPointerException if <code>left</code> or <code>right</code>
   *           is <code>null</code>.
   */
  public TypeEquationList extend ( MonoType left , MonoType right )
  {
    return new TypeEquationList ( new TypeEquation ( left , right ) , this ) ;
  }


  /**
   * Applies the {@link TypeSubstitution} <code>s</code> to all equations
   * contained within this list.
   * 
   * @param s the {@link TypeSubstitution} to apply.
   * @return the resulting list of equations.
   */
  public TypeEquationList substitute ( TypeSubstitution s )
  {
    // nothing to substitute on the empty list
    if ( this == EMPTY_LIST )
    {
      return this ;
    }
    // apply the substitution to the first and the remaining equations
    return new TypeEquationList ( this.first.substitute ( s ) , this.remaining
        .substitute ( s ) ) ;
  }


  //
  // Unification
  //
  /**
   * This method is the heart of the unification algorithm implementation. It
   * returns the unificator for this type equation list.
   * 
   * @return the unificator for this type equation.
   * @throws UnificationException if one of the equations contained within this
   *           list could not be unified.
   */
  public DefaultTypeSubstitution unify ( ) throws UnificationException
  {
    // an empty type equation list is easy to unify
    if ( this == EMPTY_LIST )
    {
      return DefaultTypeSubstitution.EMPTY_SUBSTITUTION ;
    }
    // otherwise, we examine the first equation in the list
    MonoType left = this.first.getLeft ( ) ;
    MonoType right = this.first.getRight ( ) ;
    Debug.out.println ( "Unify: " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
    // different actions, depending on the exact types
    if ( ( left instanceof TypeVariable ) || ( right instanceof TypeVariable ) )
    {
      // the left or right side of the equation is a type variable
      TypeVariable tvar = ( TypeVariable ) ( left instanceof TypeVariable ? left
          : right ) ;
      MonoType tau = ( left instanceof TypeVariable ? right : left ) ;
      // either tvar equals tau or tvar is not present in tau
      if ( ( tvar.equals ( tau ) )
          || ( ! tau.getTypeVariablesFree ( ).contains ( tvar ) ) )
      {
        DefaultTypeSubstitution s1 = new DefaultTypeSubstitution ( tvar , tau ) ;
        DefaultTypeSubstitution s2 = this.remaining.substitute ( s1 ).unify ( ) ;
        return s1.compose ( s2 ) ;
      }
      // Recursive types
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "UnificationException.1" ) , tvar , tau ) ) ; //$NON-NLS-1$
    }
    else if ( ( left instanceof ArrowType ) && ( right instanceof ArrowType ) )
    {
      // cast to ArrowType instances (tau and tau')
      ArrowType taul = ( ArrowType ) left ;
      ArrowType taur = ( ArrowType ) right ;
      // we need to check {tau1 = tau1', tau2 = tau2'} as well
      TypeEquationList eqns = this.remaining ;
      eqns = eqns.extend ( taul.getTau2 ( ) , taur.getTau2 ( ) ) ;
      eqns = eqns.extend ( taul.getTau1 ( ) , taur.getTau1 ( ) ) ;
      // try to unify the new list
      return eqns.unify ( ) ;
    }
    else if ( ( left instanceof TupleType ) && ( right instanceof TupleType ) )
    {
      // cast to TupleType instances (tau and tau')
      TupleType taul = ( TupleType ) left ;
      TupleType taur = ( TupleType ) right ;
      // determine the sub types
      MonoType [ ] typesl = taul.getTypes ( ) ;
      MonoType [ ] typesr = taur.getTypes ( ) ;
      // check if the arities match
      if ( typesl.length == typesr.length )
      {
        // check all sub types
        TypeEquationList eqns = this.remaining ;
        for ( int n = 0 ; n < typesl.length ; ++ n )
        {
          eqns = eqns.extend ( typesl [ n ] , typesr [ n ] ) ;
        }
        // try to unify the new list
        return eqns.unify ( ) ;
      }
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "UnificationException.4" ) , left , right ) ) ; //$NON-NLS-1$
    }
    else if ( ( left instanceof RefType ) && ( right instanceof RefType ) )
    {
      // cast to RefType instances (tau and tau')
      RefType taul = ( RefType ) left ;
      RefType taur = ( RefType ) right ;
      // we need to check {tau = tau'} as well
      TypeEquationList eqns = this.remaining ;
      eqns = eqns.extend ( taul.getTau ( ) , taur.getTau ( ) ) ;
      // try to unify the new list
      return eqns.unify ( ) ;
    }
    else if ( ( left instanceof ListType ) && ( right instanceof ListType ) )
    {
      // cast to ListType instances (tau and tau')
      ListType taul = ( ListType ) left ;
      ListType taur = ( ListType ) right ;
      // we need to check {tau = tau'} as well
      TypeEquationList eqns = this.remaining ;
      eqns = eqns.extend ( taul.getTau ( ) , taur.getTau ( ) ) ;
      // try to unify the new list
      return eqns.unify ( ) ;
    }
    else if ( left.equals ( right ) )
    {
      // the types equal, just unify the remaining equations then
      return this.remaining.unify ( ) ;
    }
    else if ( ( left instanceof ObjectType ) && ( right instanceof ObjectType ) )
    {
      ObjectType tau1 = ( ObjectType ) left ;
      ObjectType tau2 = ( ObjectType ) right ;
      return this.remaining.extend ( tau1.getPhi ( ) , tau2.getPhi ( ) )
          .unify ( ) ;
    }
    else if ( ( left instanceof RowType ) && ( right instanceof RowType ) )
    {
      RowType tau1 = ( RowType ) left ;
      RowType tau2 = ( RowType ) right ;
      TypeEquationList eqns = this.remaining ;
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
            eqns = eqns.extend ( tau1Types.get ( i ) , tau2Types.get ( j ) ) ;
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
        eqns = eqns.extend ( tau1RemainingRow , tau2RemainingRow ) ;
        return eqns.unify ( ) ;
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
          eqns = eqns.extend ( tau1RemainingRow , newRowType ) ;
        }
        else
        {
          throw new RuntimeException ( MessageFormat.format ( Messages
              .getString ( "UnificationException.2" ) , left , right ) ) ; //$NON-NLS-1$
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
          eqns = eqns.extend ( tau2RemainingRow , newRowType ) ;
        }
        else
        {
          throw new RuntimeException ( MessageFormat.format ( Messages
              .getString ( "UnificationException.2" ) , left , right ) ) ; //$NON-NLS-1$
        }
      }
      if ( ( tau1Identifiers.size ( ) > 0 ) || ( tau2Identifiers.size ( ) > 0 ) )
      {
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "UnificationException.3" ) , left , right ) ) ; //$NON-NLS-1$
      }
      return eqns.unify ( ) ;
    }
    // (left = right) cannot be unified
    throw new RuntimeException ( MessageFormat.format ( Messages
        .getString ( "UnificationException.0" ) , left , right ) ) ; //$NON-NLS-1$
  }


  //
  // Base methods
  //
  /**
   * Returns the string representation of the equations contained in this list.
   * This method is mainly useful for debugging purposes.
   * 
   * @return the string representation.
   * @see TypeEquation#toString()
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    StringBuilder builder = new StringBuilder ( 128 ) ;
    builder.append ( '{' ) ;
    for ( TypeEquationList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this ) builder.append ( ", " ) ; //$NON-NLS-1$
      builder.append ( list.first ) ;
    }
    builder.append ( '}' ) ;
    return builder.toString ( ) ;
  }
}
