package de.unisiegen.tpml.core.typechecker ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.ListType ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.ObjectType ;
import de.unisiegen.tpml.core.types.RecType ;
import de.unisiegen.tpml.core.types.RefType ;
import de.unisiegen.tpml.core.types.RowType ;
import de.unisiegen.tpml.core.types.TupleType ;
import de.unisiegen.tpml.core.types.TypeVariable ;


/**
 * A list of <code>TypeEquation</code>s, in the same fashion as a list in
 * OCaml.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:926M $
 * @see de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker
 */
public final class TypeEquationListTypeChecker implements PrettyPrintable ,
    LatexPrintable
{
  /**
   * The empty equation list.
   * 
   * @see #TypeEquationListTypeChecker()
   */
  public static final TypeEquationListTypeChecker EMPTY_LIST = new TypeEquationListTypeChecker ( ) ;


  /**
   * The first equation in the list.
   */
  public TypeEquationTypeChecker first ;


  /**
   * The remaining equations or <code>null</code>.
   */
  public TypeEquationListTypeChecker remaining ;


  /**
   * Allocates a new empty equation list.
   * 
   * @see #EMPTY_LIST
   */
  private TypeEquationListTypeChecker ( )
  {
    super ( ) ;
  }


  /**
   * Allocates a new equation list, which basicly extends <code>remaining</code>
   * with a new {@link TypeEquationTypeChecker} <code>first</code>.
   * 
   * @param pFirst the new {@link TypeEquationTypeChecker}.
   * @param pRemaining an existing {@link TypeEquationListTypeChecker}
   * @throws NullPointerException if <code>first</code> or
   *           <code>remaining</code> is <code>null</code>.
   */
  private TypeEquationListTypeChecker ( TypeEquationTypeChecker pFirst ,
      TypeEquationListTypeChecker pRemaining )
  {
    if ( pFirst == null )
    {
      throw new NullPointerException ( "first is null" ) ; //$NON-NLS-1$
    }
    if ( pRemaining == null )
    {
      throw new NullPointerException ( "remaining is null" ) ; //$NON-NLS-1$
    }
    this.first = pFirst ;
    this.remaining = pRemaining ;
  }


  /**
   * Allocates a new {@link TypeEquationListTypeChecker}, which extends this
   * equation list with a new {@link TypeEquationTypeChecker} for
   * <code>left</code> and <code>right</code>.
   * 
   * @param pTypeEquationTypeChecker The new equation.
   * @return the extended {@link TypeEquationListTypeChecker}.
   * @throws NullPointerException if <code>left</code> or <code>right</code>
   *           is <code>null</code>.
   */
  public TypeEquationListTypeChecker extend (
      TypeEquationTypeChecker pTypeEquationTypeChecker )
  {
    return new TypeEquationListTypeChecker ( pTypeEquationTypeChecker , this ) ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand (
        LATEX_TYPE_EQUATION_LIST_TYPE_CHECKER , 1 , "\\{#1\\}" , //$NON-NLS-1$
        "teqn1, ... , teqnn" ) ) ; //$NON-NLS-1$
    for ( TypeEquationListTypeChecker list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexCommand command : list.first.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public TreeSet < LatexInstruction > getLatexInstructions ( )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( TypeEquationListTypeChecker list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexInstruction instruction : list.first.getLatexInstructions ( ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( TypeEquationListTypeChecker list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexPackage pack : list.first.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * Applies the {@link TypeSubstitution} <code>s</code> to all equations
   * contained within this list.
   * 
   * @param s the {@link TypeSubstitution} to apply.
   * @return the resulting list of equations.
   */
  public TypeEquationListTypeChecker substitute ( TypeSubstitution s )
  {
    // nothing to substitute on the empty list
    if ( this == EMPTY_LIST )
    {
      return this ;
    }
    // apply the substitution to the first and the remaining equations
    return new TypeEquationListTypeChecker ( this.first.substitute ( s ) ,
        this.remaining.substitute ( s ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    StringBuilder body = new StringBuilder ( ) ;
    body.append ( PRETTY_CLPAREN ) ;
    int count = 0 ;
    for ( TypeEquationListTypeChecker list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        body.append ( PRETTY_COMMA ) ;
        body.append ( PRETTY_SPACE ) ;
      }
      body.append ( list.first.toPrettyString ( ).toString ( ) ) ;
      count ++ ;
    }
    body.append ( PRETTY_CRPAREN ) ;
    String descriptions[] = new String [ 2 + count ] ;
    descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
    descriptions [ 1 ] = body.toString ( ) ;
    count = 0 ;
    for ( TypeEquationListTypeChecker list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      descriptions [ 2 + count ] = list.first.toPrettyString ( ).toString ( ) ;
      count ++ ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_TYPE_EQUATION_LIST_TYPE_CHECKER , pIndent , descriptions ) ;
    builder.addBuilderBegin ( ) ;
    for ( TypeEquationListTypeChecker list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) ) ;
        builder.addText ( LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
        builder.addBreak ( ) ;
      }
      builder.addBuilder ( list.first.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , 0 ) ;
    builder.addText ( PRETTY_CLPAREN ) ;
    for ( TypeEquationListTypeChecker list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.addText ( PRETTY_COMMA ) ;
        builder.addText ( PRETTY_SPACE ) ;
      }
      builder.addBuilder ( list.first
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    }
    builder.addText ( PRETTY_CRPAREN ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this type equation list. This method
   * is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type equation
   *         list.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }


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
    // EMPTY
    if ( this == EMPTY_LIST )
    {
      return DefaultTypeSubstitution.EMPTY_SUBSTITUTION ;
    }
    MonoType left = this.first.getLeft ( ) ;
    MonoType right = this.first.getRight ( ) ;
    // ASSUME
    if ( this.first.getSeenTypes ( ).contains ( this.first ) )
    {
      return this.remaining.unify ( ) ;
    }
    // TRIV
    else if ( left.equals ( right ) )
    {
      // the types equal, just unify the remaining equations then
      return this.remaining.unify ( ) ;
    }
    // MU-LEFT
    else if ( left instanceof RecType )
    {
      RecType recType = ( RecType ) left ;
      TypeEquationListTypeChecker eqns = this.remaining ;
      eqns = eqns.extend ( new TypeEquationTypeChecker ( recType.getTau ( )
          .substitute ( recType.getTypeName ( ) , recType ) , right ,
          this.first.getSeenTypes ( ).clone ( ) ) ) ;
      return eqns.unify ( ) ;
    }
    // MU-RIGHT
    else if ( right instanceof RecType )
    {
      RecType recType = ( RecType ) right ;
      TypeEquationListTypeChecker eqns = this.remaining ;
      eqns = eqns.extend ( new TypeEquationTypeChecker ( left , recType
          .getTau ( ).substitute ( recType.getTypeName ( ) , recType ) ,
          this.first.getSeenTypes ( ).clone ( ) ) ) ;
      return eqns.unify ( ) ;
    }
    // VAR
    else if ( ( left instanceof TypeVariable )
        || ( right instanceof TypeVariable ) )
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
      // Error, because of a recursive type like: alpha1 = int -> alpha1
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "UnificationException.1" ) , tvar , tau ) ) ; //$NON-NLS-1$
    }
    // ARROW
    else if ( ( left instanceof ArrowType ) && ( right instanceof ArrowType ) )
    {
      // cast to ArrowType instances (tau and tau')
      ArrowType taul = ( ArrowType ) left ;
      ArrowType taur = ( ArrowType ) right ;
      // we need to check {tau1 = tau1', tau2 = tau2'} as well
      TypeEquationListTypeChecker eqns = this.remaining ;
      SeenTypes < TypeEquationTypeChecker > seenTypes1 = this.first
          .getSeenTypes ( ).clone ( ) ;
      seenTypes1.add ( this.first ) ;
      eqns = eqns.extend ( new TypeEquationTypeChecker ( taul.getTau2 ( ) ,
          taur.getTau2 ( ) , seenTypes1 ) ) ;
      SeenTypes < TypeEquationTypeChecker > seenTypes2 = this.first
          .getSeenTypes ( ).clone ( ) ;
      seenTypes2.add ( this.first ) ;
      eqns = eqns.extend ( new TypeEquationTypeChecker ( taul.getTau1 ( ) ,
          taur.getTau1 ( ) , seenTypes2 ) ) ;
      return eqns.unify ( ) ;
    }
    // TUPLE
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
        TypeEquationListTypeChecker eqns = this.remaining ;
        for ( int n = 0 ; n < typesl.length ; ++ n )
        {
          SeenTypes < TypeEquationTypeChecker > seenTypes = this.first
              .getSeenTypes ( ).clone ( ) ;
          seenTypes.add ( this.first ) ;
          eqns = eqns.extend ( new TypeEquationTypeChecker ( typesl [ n ] ,
              typesr [ n ] , seenTypes ) ) ;
        }
        return eqns.unify ( ) ;
      }
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "UnificationException.4" ) , left , right ) ) ; //$NON-NLS-1$
    }
    // LIST
    else if ( ( left instanceof ListType ) && ( right instanceof ListType ) )
    {
      // cast to ListType instances (tau and tau')
      ListType taul = ( ListType ) left ;
      ListType taur = ( ListType ) right ;
      // we need to check {tau = tau'} as well
      TypeEquationListTypeChecker eqns = this.remaining ;
      SeenTypes < TypeEquationTypeChecker > seenTypes = this.first
          .getSeenTypes ( ).clone ( ) ;
      seenTypes.add ( this.first ) ;
      eqns = eqns.extend ( new TypeEquationTypeChecker ( taul.getTau ( ) , taur
          .getTau ( ) , seenTypes ) ) ;
      return eqns.unify ( ) ;
    }
    // REF
    else if ( ( left instanceof RefType ) && ( right instanceof RefType ) )
    {
      // cast to RefType instances (tau and tau')
      RefType taul = ( RefType ) left ;
      RefType taur = ( RefType ) right ;
      // we need to check {tau = tau'} as well
      TypeEquationListTypeChecker eqns = this.remaining ;
      SeenTypes < TypeEquationTypeChecker > seenTypes = this.first
          .getSeenTypes ( ).clone ( ) ;
      seenTypes.add ( this.first ) ;
      eqns = eqns.extend ( new TypeEquationTypeChecker ( taul.getTau ( ) , taur
          .getTau ( ) , seenTypes ) ) ;
      return eqns.unify ( ) ;
    }
    // OBJECT
    else if ( ( left instanceof ObjectType ) && ( right instanceof ObjectType ) )
    {
      ObjectType tau1 = ( ObjectType ) left ;
      ObjectType tau2 = ( ObjectType ) right ;
      TypeEquationListTypeChecker eqns = this.remaining ;
      SeenTypes < TypeEquationTypeChecker > seenTypes = this.first
          .getSeenTypes ( ).clone ( ) ;
      seenTypes.add ( this.first ) ;
      eqns = eqns.extend ( new TypeEquationTypeChecker ( tau1.getPhi ( ) , tau2
          .getPhi ( ) , seenTypes ) ) ;
      return eqns.unify ( ) ;
    }
    // ROW
    else if ( ( left instanceof RowType ) && ( right instanceof RowType ) )
    {
      RowType tau1 = ( RowType ) left ;
      RowType tau2 = ( RowType ) right ;
      TypeEquationListTypeChecker eqns = this.remaining ;
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
            SeenTypes < TypeEquationTypeChecker > seenTypes = this.first
                .getSeenTypes ( ).clone ( ) ;
            seenTypes.add ( this.first ) ;
            eqns = eqns.extend ( new TypeEquationTypeChecker ( tau1Types
                .get ( i ) , tau2Types.get ( j ) , seenTypes ) ) ;
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
        SeenTypes < TypeEquationTypeChecker > seenTypes = this.first
            .getSeenTypes ( ).clone ( ) ;
        seenTypes.add ( this.first ) ;
        eqns = eqns.extend ( new TypeEquationTypeChecker ( tau1RemainingRow ,
            tau2RemainingRow , seenTypes ) ) ;
        if ( ( tau1Identifiers.size ( ) > 0 )
            || ( tau2Identifiers.size ( ) > 0 ) )
        {
          throw new RuntimeException ( MessageFormat.format ( Messages
              .getString ( "UnificationException.3" ) , left , right ) ) ; //$NON-NLS-1$
        }
        return eqns.unify ( ) ;
      }
      // Only first remaining RowType
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
          SeenTypes < TypeEquationTypeChecker > seenTypes = this.first
              .getSeenTypes ( ).clone ( ) ;
          seenTypes.add ( this.first ) ;
          eqns = eqns.extend ( new TypeEquationTypeChecker ( tau1RemainingRow ,
              newRowType , seenTypes ) ) ;
        }
      }
      // Only second remaining RowType
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
          SeenTypes < TypeEquationTypeChecker > seenTypes = this.first
              .getSeenTypes ( ).clone ( ) ;
          seenTypes.add ( this.first ) ;
          eqns = eqns.extend ( new TypeEquationTypeChecker ( newRowType ,
              tau2RemainingRow , seenTypes ) ) ;
        }
      }
      // Cannot unify, because of the different methods
      if ( ( tau1Identifiers.size ( ) > 0 ) || ( tau2Identifiers.size ( ) > 0 ) )
      {
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "UnificationException.3" ) , left , right ) ) ; //$NON-NLS-1$
      }
      return eqns.unify ( ) ;
    }
    // STRUCT
    throw new RuntimeException ( MessageFormat.format ( Messages
        .getString ( "UnificationException.0" ) , left , right ) ) ; //$NON-NLS-1$
  }
}
