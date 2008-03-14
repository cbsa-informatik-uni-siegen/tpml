package de.unisiegen.tpml.core.entities;


import java.text.MessageFormat;
import java.util.ArrayList;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typechecker.UnificationException;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.RecType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.RowType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.TypeVariable;


/**
 * A list of <code>TypeEquation</code>s, in the same fashion as a list in
 * OCaml.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.entities.TypeEquation
 */
public final class DefaultTypeEquationList implements TypeEquationList,
    PrettyPrintable, LatexPrintable
{
  /**
   * Sets the parser end offset.
   * 
   * @param pParserEndOffset The new parser end offset.
   * @see #getParserEndOffset()
   * @see #parserEndOffset
   */
  public void setParserEndOffset ( int pParserEndOffset )
  {
    this.parserEndOffset = pParserEndOffset;
  }
  /**
   * Returns the parserEndOffset.
   * 
   * @return The parserEndOffset.
   * @see #parserEndOffset
   * @see #setParserEndOffset(int)
   */
  public int getParserEndOffset ()
  {
    return this.parserEndOffset;
  }

  /**
   * The start offset of this {@link DefaultTypeEquationList} in the source code.
   * 
   * @see #getParserStartOffset()
   * @see #setParserStartOffset(int)
   */
  protected int parserStartOffset = -1;


  /**
   * The end offset of this {@link DefaultTypeEquationList} in the source code.
   * 
   * @see #getParserEndOffset()
   * @see #setParserEndOffset(int)
   */
  protected int parserEndOffset = -1;
  /**
   * Returns the parserStartOffset.
   * 
   * @return The parserStartOffset.
   * @see #parserStartOffset
   * @see #setParserStartOffset(int)
   */
  public int getParserStartOffset ()
  {
    return this.parserStartOffset;
  }

  /**
   * Sets the parser start offset.
   * 
   * @param pParserStartOffset The new parser start offset.
   * @see #getParserStartOffset()
   * @see #parserStartOffset
   */
  public void setParserStartOffset ( int pParserStartOffset )
  {
    this.parserStartOffset = pParserStartOffset;
  }
  /**
   * The empty equation list.
   * 
   * @see #DefaultTypeEquationList()
   */
  public static final TypeEquationList EMPTY_LIST = new DefaultTypeEquationList ();


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand (
        LATEX_TYPE_EQUATION_LIST, 1, "\\color{" //$NON-NLS-1$
            + LATEX_COLOR_NONE + "}{\\{}#1\\color{" //$NON-NLS-1$
            + LATEX_COLOR_NONE + "}{\\}}", "teqn1, ... , teqnn" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}", //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ); //$NON-NLS-1$
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( LatexPackage.COLOR );
    return packages;
  }


  /**
   * The first equation in the list.
   */
  private TypeEquation first;


  /**
   * The remaining equations or <code>null</code>.
   */
  private TypeEquationList remaining;


  /**
   * Allocates a new empty equation list.
   * 
   * @see #EMPTY_LIST
   */
  private DefaultTypeEquationList ()
  {
    super ();
  }


  /**
   * Allocates a new equation list, which basicly extends <code>remaining</code>
   * with a new {@link TypeEquation} <code>first</code>.
   * 
   * @param pFirst the new {@link TypeEquation}.
   * @param pRemaining an existing {@link DefaultTypeEquationList}
   * @throws NullPointerException if <code>first</code> or
   *           <code>remaining</code> is <code>null</code>.
   */
  private DefaultTypeEquationList ( final TypeEquation pFirst,
      final TypeEquationList pRemaining )
  {
    if ( pFirst == null )
    {
      throw new NullPointerException ( "First is null" ); //$NON-NLS-1$
    }
    if ( pRemaining == null )
    {
      throw new NullPointerException ( "Remaining is null" ); //$NON-NLS-1$
    }
    this.first = pFirst;
    this.remaining = pRemaining;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEquationList#extend(TypeEquation)
   */
  public DefaultTypeEquationList extend (
      TypeEquation pTypeEquation )
  {
    return new DefaultTypeEquationList ( pTypeEquation, this );
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEquationList#getFirst()
   */
  public TypeEquation getFirst ()
  {
    return this.first;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    for ( TypeEquationList list = this ; list != EMPTY_LIST ; list = list
        .getRemaining () )
    {
      commands.add ( list.getFirst () );
    }
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    for ( TypeEquationList list = this ; list != EMPTY_LIST ; list = list
        .getRemaining () )
    {
      instructions.add ( list.getFirst () );
    }
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    for ( TypeEquationList list = this ; list != EMPTY_LIST ; list = list
        .getRemaining () )
    {
      packages.add ( list.getFirst () );
    }
    return packages;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEquationList#getRemaining()
   */
  public TypeEquationList getRemaining ()
  {
    return this.remaining;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEquationList#substitute(TypeSubstitution)
   */
  public TypeEquationList substitute ( TypeSubstitution s )
  {
    // nothing to substitute on the empty list
    if ( this == EMPTY_LIST )
    {
      return this;
    }
    // apply the substitution to the first and the remaining equations
    return new DefaultTypeEquationList ( this.first.substitute ( s ),
        this.remaining.substitute ( s ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    StringBuilder body = new StringBuilder ();
    body.append ( PRETTY_CLPAREN );
    int count = 0;
    for ( TypeEquationList list = this ; list != EMPTY_LIST ; list = list
        .getRemaining () )
    {
      if ( list != this )
      {
        body.append ( PRETTY_COMMA );
        body.append ( PRETTY_SPACE );
      }
      body.append ( list.getFirst ().toPrettyString ().toString () );
      count++ ;
    }
    body.append ( PRETTY_CRPAREN );
    String descriptions[] = new String [ 2 + count ];
    descriptions [ 0 ] = this.toPrettyString ().toString ();
    descriptions [ 1 ] = body.toString ();
    count = 0;
    for ( TypeEquationList list = this ; list != EMPTY_LIST ; list = list
        .getRemaining () )
    {
      descriptions [ 2 + count ] = list.getFirst ().toPrettyString ()
          .toString ();
      count++ ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_TYPE_EQUATION_LIST, pIndent, descriptions );
    builder.addBuilderBegin ();
    for ( TypeEquationList list = this ; list != EMPTY_LIST ; list = list
        .getRemaining () )
    {
      if ( list != this )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) );
        builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ); //$NON-NLS-1$ //$NON-NLS-2$
        builder.addText ( LATEX_COMMA );
        builder.addText ( LATEX_SPACE );
        builder.addText ( "}" ); //$NON-NLS-1$
        builder.addBreak ();
      }
      builder.addBuilder ( list.getFirst ().toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 2 ), 0 );
    }
    builder.addBuilderEnd ();
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
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
        this, 0 );
    builder.addText ( PRETTY_CLPAREN );
    for ( TypeEquationList list = this ; list != EMPTY_LIST ; list = list
        .getRemaining () )
    {
      if ( list != this )
      {
        builder.addText ( PRETTY_COMMA );
        builder.addText ( PRETTY_SPACE );
      }
      builder.addBuilder ( list.getFirst ().toPrettyStringBuilder (
          pPrettyStringBuilderFactory ), 0 );
    }
    builder.addText ( PRETTY_CRPAREN );
    return builder;
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
  @Override
  public final String toString ()
  {
    return toPrettyString ().toString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeEquationList#unify()
   */
  public TypeSubstitution unify () throws UnificationException
  {
    // EMPTY
    if ( this == EMPTY_LIST )
    {
      return DefaultTypeSubstitution.EMPTY_SUBSTITUTION;
    }
    MonoType left = this.first.getLeft ();
    MonoType right = this.first.getRight ();
    // ASSUME
    if ( this.first.getSeenTypes ().contains ( this.first ) )
    {
      return this.remaining.unify ();
    }
    // TRIV
    else if ( left.equals ( right ) )
    {
      // the types equal, just unify the remaining equations then
      return this.remaining.unify ();
    }
    // MU-LEFT
    else if ( left instanceof RecType )
    {
      RecType recType = ( RecType ) left;
      TypeEquationList eqns = this.remaining;
      eqns = eqns.extend ( new DefaultTypeEquation ( recType.getTau ()
          .substitute ( recType.getTypeName (), recType ), right, this.first
          .getSeenTypes ().clone () ) );
      return eqns.unify ();
    }
    // MU-RIGHT
    else if ( right instanceof RecType )
    {
      RecType recType = ( RecType ) right;
      TypeEquationList eqns = this.remaining;
      eqns = eqns.extend ( new DefaultTypeEquation ( left, recType.getTau ()
          .substitute ( recType.getTypeName (), recType ), this.first
          .getSeenTypes ().clone () ) );
      return eqns.unify ();
    }
    // VAR
    else if ( ( left instanceof TypeVariable )
        || ( right instanceof TypeVariable ) )
    {
      // the left or right side of the equation is a type variable
      TypeVariable tvar = ( TypeVariable ) ( left instanceof TypeVariable ? left
          : right );
      MonoType tau = ( left instanceof TypeVariable ? right : left );
      // either tvar equals tau or tvar is not present in tau
      if ( ( tvar.equals ( tau ) )
          || ( !tau.getTypeVariablesFree ().contains ( tvar ) ) )
      {
        TypeSubstitution s1 = new DefaultTypeSubstitution ( tvar, tau );
        TypeSubstitution s2 = this.remaining.substitute ( s1 ).unify ();
        return s1.compose ( s2 );
      }
      // Error, because of a recursive type like: alpha1 = int -> alpha1
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "UnificationException.1" ), tvar, tau ) ); //$NON-NLS-1$
    }
    // ARROW
    else if ( ( left instanceof ArrowType ) && ( right instanceof ArrowType ) )
    {
      // cast to ArrowType instances (tau and tau')
      ArrowType taul = ( ArrowType ) left;
      ArrowType taur = ( ArrowType ) right;
      // we need to check {tau1 = tau1', tau2 = tau2'} as well
      TypeEquationList eqns = this.remaining;
      SeenTypes < TypeEquation > seenTypes1 = this.first.getSeenTypes ()
          .clone ();
      seenTypes1.add ( this.first );
      eqns = eqns.extend ( new DefaultTypeEquation ( taul.getTau2 (), taur
          .getTau2 (), seenTypes1 ) );
      SeenTypes < TypeEquation > seenTypes2 = this.first.getSeenTypes ()
          .clone ();
      seenTypes2.add ( this.first );
      eqns = eqns.extend ( new DefaultTypeEquation ( taul.getTau1 (), taur
          .getTau1 (), seenTypes2 ) );
      return eqns.unify ();
    }
    // TUPLE
    else if ( ( left instanceof TupleType ) && ( right instanceof TupleType ) )
    {
      // cast to TupleType instances (tau and tau')
      TupleType taul = ( TupleType ) left;
      TupleType taur = ( TupleType ) right;
      // determine the sub types
      MonoType [] typesl = taul.getTypes ();
      MonoType [] typesr = taur.getTypes ();
      // check if the arities match
      if ( typesl.length == typesr.length )
      {
        // check all sub types
        TypeEquationList eqns = this.remaining;
        for ( int n = 0 ; n < typesl.length ; ++n )
        {
          SeenTypes < TypeEquation > seenTypes = this.first.getSeenTypes ()
              .clone ();
          seenTypes.add ( this.first );
          eqns = eqns.extend ( new DefaultTypeEquation ( typesl [ n ],
              typesr [ n ], seenTypes ) );
        }
        return eqns.unify ();
      }
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "UnificationException.4" ), left, right ) ); //$NON-NLS-1$
    }
    // LIST
    else if ( ( left instanceof ListType ) && ( right instanceof ListType ) )
    {
      // cast to ListType instances (tau and tau')
      ListType taul = ( ListType ) left;
      ListType taur = ( ListType ) right;
      // we need to check {tau = tau'} as well
      TypeEquationList eqns = this.remaining;
      SeenTypes < TypeEquation > seenTypes = this.first.getSeenTypes ()
          .clone ();
      seenTypes.add ( this.first );
      eqns = eqns.extend ( new DefaultTypeEquation ( taul.getTau (), taur
          .getTau (), seenTypes ) );
      return eqns.unify ();
    }
    // REF
    else if ( ( left instanceof RefType ) && ( right instanceof RefType ) )
    {
      // cast to RefType instances (tau and tau')
      RefType taul = ( RefType ) left;
      RefType taur = ( RefType ) right;
      // we need to check {tau = tau'} as well
      TypeEquationList eqns = this.remaining;
      SeenTypes < TypeEquation > seenTypes = this.first.getSeenTypes ()
          .clone ();
      seenTypes.add ( this.first );
      eqns = eqns.extend ( new DefaultTypeEquation ( taul.getTau (), taur
          .getTau (), seenTypes ) );
      return eqns.unify ();
    }
    // OBJECT
    else if ( ( left instanceof ObjectType ) && ( right instanceof ObjectType ) )
    {
      ObjectType tau1 = ( ObjectType ) left;
      ObjectType tau2 = ( ObjectType ) right;
      TypeEquationList eqns = this.remaining;
      SeenTypes < TypeEquation > seenTypes = this.first.getSeenTypes ()
          .clone ();
      seenTypes.add ( this.first );
      eqns = eqns.extend ( new DefaultTypeEquation ( tau1.getPhi (), tau2
          .getPhi (), seenTypes ) );
      return eqns.unify ();
    }
    // ROW
    else if ( ( left instanceof RowType ) && ( right instanceof RowType ) )
    {
      RowType tau1 = ( RowType ) left;
      RowType tau2 = ( RowType ) right;
      TypeEquationList eqns = this.remaining;
      ArrayList < Identifier > tau1Identifiers = new ArrayList < Identifier > ();
      for ( Identifier id : tau1.getIdentifiers () )
      {
        tau1Identifiers.add ( id );
      }
      ArrayList < Identifier > tau2Identifiers = new ArrayList < Identifier > ();
      for ( Identifier id : tau2.getIdentifiers () )
      {
        tau2Identifiers.add ( id );
      }
      ArrayList < MonoType > tau1Types = new ArrayList < MonoType > ();
      for ( MonoType tau : tau1.getTypes () )
      {
        tau1Types.add ( tau );
      }
      ArrayList < MonoType > tau2Types = new ArrayList < MonoType > ();
      for ( MonoType tau : tau2.getTypes () )
      {
        tau2Types.add ( tau );
      }
      // Unify child types
      for ( int i = tau1Identifiers.size () - 1 ; i >= 0 ; i-- )
      {
        for ( int j = tau2Identifiers.size () - 1 ; j >= 0 ; j-- )
        {
          if ( tau1Identifiers.get ( i ).equals ( tau2Identifiers.get ( j ) ) )
          {
            SeenTypes < TypeEquation > seenTypes = this.first.getSeenTypes ()
                .clone ();
            seenTypes.add ( this.first );
            eqns = eqns.extend ( new DefaultTypeEquation ( tau1Types.get ( i ),
                tau2Types.get ( j ), seenTypes ) );
            tau1Identifiers.remove ( i );
            tau1Types.remove ( i );
            tau2Identifiers.remove ( j );
            tau2Types.remove ( j );
            break;
          }
        }
      }
      // Remaining RowType
      MonoType tau1RemainingRow = tau1.getRemainingRowType ();
      MonoType tau2RemainingRow = tau2.getRemainingRowType ();
      // First and second remaining RowTypes
      if ( ( tau1RemainingRow != null ) && ( tau2RemainingRow != null ) )
      {
        SeenTypes < TypeEquation > seenTypes = this.first.getSeenTypes ()
            .clone ();
        seenTypes.add ( this.first );
        eqns = eqns.extend ( new DefaultTypeEquation ( tau1RemainingRow,
            tau2RemainingRow, seenTypes ) );
        if ( ( tau1Identifiers.size () > 0 ) || ( tau2Identifiers.size () > 0 ) )
        {
          throw new RuntimeException ( MessageFormat.format ( Messages
              .getString ( "UnificationException.3" ), left, right ) ); //$NON-NLS-1$
        }
        return eqns.unify ();
      }
      // Only first remaining RowType
      if ( tau1RemainingRow != null )
      {
        if ( tau2Identifiers.size () > 0 )
        {
          Identifier [] newIdentifiers = new Identifier [ tau2Identifiers
              .size () ];
          MonoType [] newTypes = new MonoType [ tau2Types.size () ];
          for ( int i = tau2Identifiers.size () - 1 ; i >= 0 ; i-- )
          {
            newIdentifiers [ i ] = tau2Identifiers.get ( i );
            newTypes [ i ] = tau2Types.get ( i );
            tau2Identifiers.remove ( i );
            tau2Types.remove ( i );
          }
          RowType newRowType = new RowType ( newIdentifiers, newTypes );
          SeenTypes < TypeEquation > seenTypes = this.first.getSeenTypes ()
              .clone ();
          seenTypes.add ( this.first );
          eqns = eqns.extend ( new DefaultTypeEquation ( tau1RemainingRow,
              newRowType, seenTypes ) );
        }
      }
      // Only second remaining RowType
      if ( tau2RemainingRow != null )
      {
        if ( tau1Identifiers.size () > 0 )
        {
          Identifier [] newIdentifiers = new Identifier [ tau1Identifiers
              .size () ];
          MonoType [] newTypes = new MonoType [ tau1Types.size () ];
          for ( int i = tau1Identifiers.size () - 1 ; i >= 0 ; i-- )
          {
            newIdentifiers [ i ] = tau1Identifiers.get ( i );
            newTypes [ i ] = tau1Types.get ( i );
            tau1Identifiers.remove ( i );
            tau1Types.remove ( i );
          }
          RowType newRowType = new RowType ( newIdentifiers, newTypes );
          SeenTypes < TypeEquation > seenTypes = this.first.getSeenTypes ()
              .clone ();
          seenTypes.add ( this.first );
          eqns = eqns.extend ( new DefaultTypeEquation ( newRowType,
              tau2RemainingRow, seenTypes ) );
        }
      }
      // Cannot unify, because of the different methods
      if ( ( tau1Identifiers.size () > 0 ) || ( tau2Identifiers.size () > 0 ) )
      {
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "UnificationException.3" ), left, right ) ); //$NON-NLS-1$
      }
      return eqns.unify ();
    }
    // STRUCT
    throw new RuntimeException ( MessageFormat.format ( Messages
        .getString ( "UnificationException.0" ), left, right ) ); //$NON-NLS-1$
  }
}
