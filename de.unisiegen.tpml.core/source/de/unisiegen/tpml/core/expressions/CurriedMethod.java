package de.unisiegen.tpml.core.expressions;


import java.util.ArrayList;
import java.util.Arrays;

import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions;
import de.unisiegen.tpml.core.interfaces.DefaultTypes;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.util.BoundRenaming;


/**
 * Instances of this class represent curried method expressions.
 * 
 * @author Christian Fehler
 * @version $Id$
 */
public final class CurriedMethod extends Expression implements
    BoundIdentifiers, DefaultTypes, DefaultExpressions
{

  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [] INDICES_E = new int []
  { -1 };


  /**
   * String for the case that the expression is null.
   */
  private static final String EXPRESSION_NULL = "expression is null"; //$NON-NLS-1$


  /**
   * String for the case that the identifiers are null.
   */
  private static final String IDENTIFIERS_NULL = "identifiers is null"; //$NON-NLS-1$


  /**
   * String for the case that one identifier are null.
   */
  private static final String IDENTIFIER_NULL = "one identifier is null"; //$NON-NLS-1$


  /**
   * String for the case that the types are null.
   */
  private static final String TYPES_NULL = "types is null"; //$NON-NLS-1$


  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET_M = "the set of the identifier has to be 'method'"; //$NON-NLS-1$


  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET_V = "the set of the identifier has to be 'variable'"; //$NON-NLS-1$


  /**
   * String for the case that the arity of identifiers and types doesn´t match.
   */
  private static final String ARITY = "the arity of identifiers and types must match"; //$NON-NLS-1$


  /**
   * String for the case that the identifiers are to small.
   */
  private static final String TO_SMALL = "identifiers must contain at least two items"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( CurriedMethod.class );


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_METHOD, 0,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{method}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_CURRIED_METHOD, 3,
        "\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{\\color{" + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_METHOD //$NON-NLS-1$//$NON-NLS-2$
            + "\\ #1\\ =\\ #3\\ ;}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{\\color{" + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_METHOD //$NON-NLS-1$ //$NON-NLS-2$
            + "\\ #1\\colon\\ #2\\ =\\ #3\\ ;}", //$NON-NLS-1$
        "m (id1: tau1) ... (idn: taun)", "tau", "e" ) ); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
    return commands;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( LatexPackage.IFTHEN );
    return packages;
  }


  /**
   * The {@link Identifier}s of this {@link Expression}.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [] identifiers;


  /**
   * The sub types.
   * 
   * @see #getTypes()
   */
  private MonoType [] types;


  /**
   * The sub expressions.
   */
  private Expression [] expressions;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [] indicesId;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [] indicesType;


  /**
   * Allocates a new {@link CurriedMethod}.
   * 
   * @param pIdentifiers The {@link Identifier}s.
   * @param pTypes The {@link Type}s.
   * @param pExpression The child {@link Expression}.
   */
  public CurriedMethod ( Identifier [] pIdentifiers, MonoType [] pTypes,
      Expression pExpression )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( IDENTIFIERS_NULL );
    }
    for ( int i = 0 ; i < pIdentifiers.length ; i++ )
    {
      if ( pIdentifiers [ i ] == null )
      {
        throw new NullPointerException ( IDENTIFIER_NULL );
      }
      if ( i == 0 )
      {
        if ( !Identifier.Set.METHOD.equals ( pIdentifiers [ i ].getSet () ) )
        {
          throw new IllegalArgumentException ( WRONG_SET_M );
        }
      }
      else
      {
        if ( !Identifier.Set.VARIABLE.equals ( pIdentifiers [ i ].getSet () ) )
        {
          throw new IllegalArgumentException ( WRONG_SET_V );
        }
      }
    }
    if ( pTypes == null )
    {
      throw new NullPointerException ( TYPES_NULL );
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( EXPRESSION_NULL );
    }
    if ( pIdentifiers.length < 2 )
    {
      throw new IllegalArgumentException ( TO_SMALL );
    }
    if ( pIdentifiers.length != pTypes.length )
    {
      throw new IllegalArgumentException ( ARITY );
    }
    // Identifier
    this.identifiers = pIdentifiers;
    this.indicesId = new int [ this.identifiers.length ];
    this.indicesId [ 0 ] = -1;
    this.identifiers [ 0 ].setParent ( this );
    for ( int i = 1 ; i < this.identifiers.length ; i++ )
    {
      this.identifiers [ i ].setParent ( this );
      this.indicesId [ i ] = i;
    }
    // Type
    this.types = pTypes;
    this.indicesType = new int [ this.types.length ];
    this.indicesType [ 0 ] = -1;
    if ( this.types [ 0 ] != null )
    {
      this.types [ 0 ].setParent ( this );
    }
    for ( int i = 1 ; i < this.identifiers.length ; i++ )
    {
      if ( this.types [ i ] != null )
      {
        this.types [ i ].setParent ( this );
      }
      this.indicesType [ i ] = i;
    }
    // Expression
    this.expressions = new Expression []
    { pExpression };
    this.expressions [ 0 ].setParent ( this );
    checkDisjunction ();
  }


  /**
   * Allocates a new {@link CurriedMethod}.
   * 
   * @param pIdentifiers The {@link Identifier}s.
   * @param pTypes The {@link Type}s.
   * @param pExpression The child {@link Expression}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public CurriedMethod ( Identifier [] pIdentifiers, MonoType [] pTypes,
      Expression pExpression, int pParserStartOffset, int pParserEndOffset )
  {
    this ( pIdentifiers, pTypes, pExpression );
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  private void checkDisjunction ()
  {
    // Identifier 0
    ArrayList < Identifier > allIdentifiers = this.expressions [ 0 ]
        .getIdentifiersAll ();
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ();
    for ( int i = 1 ; i < this.identifiers.length ; i++ )
    {
      negativeIdentifiers.clear ();
      for ( Identifier allId : allIdentifiers )
      {
        if ( ( this.identifiers [ i ].equals ( allId ) )
            && ( ! ( ( Identifier.Set.VARIABLE.equals ( allId.getSet () ) || ( Identifier.Set.METHOD
                .equals ( allId.getSet () ) ) ) ) ) )
        {
          negativeIdentifiers.add ( allId );
        }
      }
      /*
       * Throw an exception, if the negative identifier list contains one or
       * more identifiers. If this happens, all Identifiers are added.
       */
      if ( negativeIdentifiers.size () > 0 )
      {
        negativeIdentifiers.clear ();
        for ( Identifier allId : allIdentifiers )
        {
          if ( this.identifiers [ i ].equals ( allId ) )
          {
            negativeIdentifiers.add ( allId );
          }
        }
        negativeIdentifiers.add ( this.identifiers [ i ] );
        LanguageParserMultiException
            .throwExceptionDisjunction ( negativeIdentifiers );
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public CurriedMethod clone ()
  {
    Identifier [] newIdentifiers = new Identifier [ this.identifiers.length ];
    for ( int i = 0 ; i < newIdentifiers.length ; i++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ();
    }
    MonoType [] newTypes = new MonoType [ this.types.length ];
    for ( int i = 0 ; i < newTypes.length ; i++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .clone ();
    }
    return new CurriedMethod ( newIdentifiers, newTypes, this.expressions [ 0 ]
        .clone () );
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof CurriedMethod )
    {
      CurriedMethod other = ( CurriedMethod ) pObject;
      return ( ( Arrays.equals ( this.identifiers, other.identifiers ) )
          && ( Arrays.equals ( this.types, other.types ) ) && ( this.expressions [ 0 ]
          .equals ( other.expressions [ 0 ] ) ) );
    }
    return false;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption ()
  {
    return CAPTION;
  }


  /**
   * Returns the sub expression.
   * 
   * @return the sub expression.
   */
  public Expression getE ()
  {
    return this.expressions [ 0 ];
  }


  /**
   * Returns the sub {@link Expression}s.
   * 
   * @return the sub {@link Expression}s.
   */
  public Expression [] getExpressions ()
  {
    return this.expressions;
  }


  /**
   * Returns the indices of the child {@link Expression}s.
   * 
   * @return The indices of the child {@link Expression}s.
   */
  public int [] getExpressionsIndex ()
  {
    return INDICES_E;
  }


  /**
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   */
  public Identifier [] getIdentifiers ()
  {
    return this.identifiers;
  }


  /**
   * Returns a list of lists of in this {@link Expression} bound
   * {@link Identifier}s.
   * 
   * @return A list of lists of in this {@link Expression} bound
   *         {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ()
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> (
          this.identifiers.length );
      ArrayList < Identifier > boundE = this.expressions [ 0 ]
          .getIdentifiersFree ();
      this.boundIdentifiers.add ( null );
      for ( int i = 1 ; i < this.identifiers.length ; i++ )
      {
        /*
         * An Identifier has no binding, if an Identifier after him has the same
         * name. Example: object method add x x = x ; end.
         */
        boolean hasBinding = true;
        for ( int j = i + 1 ; j < this.identifiers.length ; j++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            hasBinding = false;
            break;
          }
        }
        ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ();
        if ( hasBinding )
        {
          for ( Identifier freeId : boundE )
          {
            if ( this.identifiers [ i ].equals ( freeId ) )
            {
              freeId.setBoundTo ( this, this.identifiers [ i ] );
              boundIdList.add ( freeId );
            }
          }
          this.boundIdentifiers.add ( boundIdList );
        }
      }
    }
    return this.boundIdentifiers;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public ArrayList < Identifier > getIdentifiersFree ()
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ();
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree () );
      for ( int i = 1 ; i < this.identifiers.length ; i++ )
      {
        while ( this.identifiersFree.remove ( this.identifiers [ i ] ) )
        {
          // Remove all Identifiers with the same name
        }
      }
    }
    return this.identifiersFree;
  }


  /**
   * Returns the indices of the child {@link Identifier}s.
   * 
   * @return The indices of the child {@link Identifier}s.
   */
  public int [] getIdentifiersIndex ()
  {
    return this.indicesId;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @Override
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = super.getLatexCommands ();
    commands.add ( getLatexCommandsStatic () );
    return commands;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  @Override
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = super.getLatexPackages ();
    packages.add ( getLatexPackagesStatic () );
    return packages;
  }


  /**
   * Returns the sub {@link Type}s.
   * 
   * @return the sub {@link Type}s.
   */
  public MonoType [] getTypes ()
  {
    return this.types;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [] getTypesIndex ()
  {
    return this.indicesType;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode ()
  {
    return this.identifiers.hashCode () + this.expressions [ 0 ].hashCode ()
        + this.types.hashCode ();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValue ()
  {
    return this.expressions [ 0 ].isValue ();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public CurriedMethod substitute ( Identifier pId, Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable () )
    {
      throw new NotOnlyFreeVariableException ();
    }
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    for ( int i = 1 ; i < this.identifiers.length ; i++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        return this;
      }
    }
    Expression newE = this.expressions [ 0 ];
    Identifier [] newIdentifiers = new Identifier [ this.identifiers.length ];
    for ( int i = 0 ; i < newIdentifiers.length ; i++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ];
    }
    for ( int i = 1 ; i < newIdentifiers.length ; i++ )
    {
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ();
      boundRenaming.add ( this.getIdentifiersFree () );
      boundRenaming.add ( pExpression.getIdentifiersFree () );
      boundRenaming.add ( pId );
      /*
       * The new Identifier should not be equal to an other Identifier.
       */
      if ( boundRenaming.contains ( newIdentifiers [ i ] ) )
      {
        for ( int j = 1 ; j < newIdentifiers.length ; j++ )
        {
          if ( i != j )
          {
            boundRenaming.add ( newIdentifiers [ j ] );
          }
        }
      }
      Identifier newId = boundRenaming.newIdentifier ( newIdentifiers [ i ] );
      /*
       * Search for an Identifier before the current Identifier with the same
       * name. For example: "let a = b in object (self) method add b b = b a ;
       * end".
       */
      for ( int j = 1 ; j < i ; j++ )
      {
        if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
        {
          newId = newIdentifiers [ j ];
        }
      }
      /*
       * Substitute the old Identifier only with the new Identifier, if they are
       * different.
       */
      if ( !newIdentifiers [ i ].equals ( newId ) )
      {
        newE = newE.substitute ( newIdentifiers [ i ], newId );
        newIdentifiers [ i ] = newId;
      }
    }
    /*
     * Perform the substitution.
     */
    newE = newE.substitute ( pId, pExpression );
    return new CurriedMethod ( newIdentifiers, this.types, newE );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @Override
  public CurriedMethod substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType [] newTypes = new MonoType [ this.types.length ];
    for ( int i = 0 ; i < newTypes.length ; i++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .substitute ( pTypeSubstitution );
    }
    return new CurriedMethod ( this.identifiers, newTypes,
        this.expressions [ 0 ].substitute ( pTypeSubstitution ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    StringBuilder identifier = new StringBuilder ();
    identifier.append ( this.identifiers [ 0 ].toPrettyString ().toString () );
    for ( int i = 1 ; i < this.identifiers.length ; i++ )
    {
      identifier.append ( PRETTY_SPACE );
      if ( this.types [ i ] != null )
      {
        identifier.append ( PRETTY_LPAREN );
      }
      identifier.append ( this.identifiers [ i ].toPrettyString ().toString () );
      if ( this.types [ i ] != null )
      {
        identifier.append ( PRETTY_COLON );
        identifier.append ( PRETTY_SPACE );
        identifier.append ( this.types [ i ].toPrettyString ().toString () );
        identifier.append ( PRETTY_RPAREN );
      }
    }
    String descriptions[] = new String [ 2 + this.identifiers.length
        + this.types.length + this.expressions.length ];
    descriptions [ 0 ] = this.toPrettyString ().toString ();
    descriptions [ 1 ] = identifier.toString ();
    descriptions [ 2 ] = this.identifiers [ 0 ].toString ();
    for ( int i = 1 ; i < this.identifiers.length ; i++ )
    {
      descriptions [ 1 + i * 2 ] = this.identifiers [ i ].toPrettyString ()
          .toString ();
      descriptions [ 2 + i * 2 ] = this.types [ i ] == null ? LATEX_NO_TYPE
          : this.types [ i ].toPrettyString ().toString ();
    }
    descriptions [ descriptions.length - 2 ] = this.types [ 0 ] == null ? LATEX_NO_TYPE
        : this.types [ 0 ].toPrettyString ().toString ();
    descriptions [ descriptions.length - 1 ] = this.expressions [ 0 ]
        .toPrettyString ().toString ();
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_CURRIED_METHOD, LATEX_CURRIED_METHOD, pIndent, descriptions );
    builder.addBuilderBegin ();
    builder.addBuilder ( this.identifiers [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 2 ), PRIO_ID );
    for ( int i = 1 ; i < this.identifiers.length ; i++ )
    {
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
          + LATEX_INDENT )
          + LATEX_SPACE );
      if ( this.types [ i ] != null )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT )
            + LATEX_LPAREN );
      }
      builder.addBuilder ( this.identifiers [ i ].toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 2 ), PRIO_ID );
      if ( this.types [ i ] != null )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT )
            + LATEX_COLON );
        builder.addText ( LATEX_SPACE );
        builder.addBuilder ( this.types [ i ].toLatexStringBuilder (
            pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 2 ),
            PRIO_CURRIED_METHOD_TAU );
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT )
            + LATEX_RPAREN );
      }
    }
    builder.addBuilderEnd ();
    if ( this.types [ 0 ] == null )
    {
      builder.addEmptyBuilder ();
    }
    else
    {
      builder.addBuilder ( this.types [ 0 ].toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), PRIO_LET_TAU );
    }
    builder.addBreak ();
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ),
        PRIO_CURRIED_METHOD_E );
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this,
          PRIO_CURRIED_METHOD );
      this.prettyStringBuilder.addKeyword ( PRETTY_METHOD );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), PRIO_ID );
      for ( int i = 1 ; i < this.identifiers.length ; i++ )
      {
        this.prettyStringBuilder.addText ( PRETTY_SPACE );
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( PRETTY_LPAREN );
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), PRIO_ID );
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( PRETTY_COLON );
          this.prettyStringBuilder.addText ( PRETTY_SPACE );
          this.prettyStringBuilder.addBuilder ( this.types [ i ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ),
              PRIO_CURRIED_METHOD_TAU );
          this.prettyStringBuilder.addText ( PRETTY_RPAREN );
        }
      }
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( PRETTY_COLON );
        this.prettyStringBuilder.addText ( PRETTY_SPACE );
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ),
            PRIO_LET_TAU );
      }
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addText ( PRETTY_EQUAL );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBreak ();
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ),
          PRIO_CURRIED_METHOD_E );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addText ( PRETTY_SEMI );
    }
    return this.prettyStringBuilder;
  }
}
