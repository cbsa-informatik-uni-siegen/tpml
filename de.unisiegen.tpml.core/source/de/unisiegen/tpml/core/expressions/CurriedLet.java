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
 * Instances of this class represents curried let expressions, which are
 * syntactic sugar for {@link Let} and {@link Lambda}.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see Let
 * @see Lambda
 */
public class CurriedLet extends Expression implements BoundIdentifiers,
    DefaultTypes, DefaultExpressions
{

  /**
   * String for the case that the arity of identifiers and types doesn´t match.
   */
  private static final String ARITY = "the arity of identifiers and types must match"; //$NON-NLS-1$


  /**
   * String for the case that the identifiers are to small.
   */
  private static final String TO_SMALL = "identifiers must contain at least two items"; //$NON-NLS-1$


  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [] INDICES_E = new int []
  { 1, 2 };


  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null"; //$NON-NLS-1$


  /**
   * String for the case that e2 is null.
   */
  private static final String E2_NULL = "e2 is null"; //$NON-NLS-1$


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
  private static final String WRONG_SET = "the set of the identifier has to be 'variable'"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( CurriedLet.class );


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_LET, 0,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{let}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_IN, 0,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{in}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_CURRIED_LET, 4,
        "\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{\\color{" + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_LET //$NON-NLS-1$ //$NON-NLS-2$
            + "\\ #1\\ =\\ #3\\ \\" + LATEX_KEY_IN + "\\ #4}" //$NON-NLS-1$ //$NON-NLS-2$
            + LATEX_LINE_BREAK_NEW_COMMAND + "{\\color{" //$NON-NLS-1$
            + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_LET //$NON-NLS-1$
            + "\\ #1\\colon\\ #2\\ =\\ #3\\ \\" + LATEX_KEY_IN + "\\ #4}", //$NON-NLS-1$//$NON-NLS-2$
        "id (id1: tau1) ... (idn: taun)", "tau", "e1", "e2" ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  protected Identifier [] identifiers;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   */
  protected MonoType [] types;


  /**
   * The expressions.
   */
  protected Expression [] expressions;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  protected int [] indicesId;


  /**
   * Indeces of the child {@link Type}s.
   */
  protected int [] indicesType;


  /**
   * Allocates a new <code>CurriedLet</code> instance.
   * 
   * @param pIdentifiers an array with atleast two identifiers, where the first
   *          identifier is the name to use for the function and the remaining
   *          identifiers specify the parameters for the function.
   * @param pTypes the types for the <code>identifiers</code>, see
   *          {@link #getTypes()} for an extensive description of the meaning of
   *          <code>types</code>.
   * @param pExpression1 the function body.
   * @param pExpression2 the second expression.
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *           contains less than two identifiers, or the arity of
   *           <code>identifiers</code> and <code>types</code> doesn't
   *           match.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>types</code>, <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public CurriedLet ( Identifier [] pIdentifiers, MonoType [] pTypes,
      Expression pExpression1, Expression pExpression2 )
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( E1_NULL );
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( E2_NULL );
    }
    if ( pTypes == null )
    {
      throw new NullPointerException ( TYPES_NULL );
    }
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( IDENTIFIERS_NULL );
    }
    for ( Identifier id : pIdentifiers )
    {
      if ( id == null )
      {
        throw new NullPointerException ( IDENTIFIER_NULL );
      }
      if ( !Identifier.Set.VARIABLE.equals ( id.getSet () ) )
      {
        throw new IllegalArgumentException ( WRONG_SET );
      }
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
    for ( int i = 1 ; i < this.types.length ; i++ )
    {
      this.indicesType [ i ] = i;
      if ( this.types [ i ] != null )
      {
        this.types [ i ].setParent ( this );
      }
    }
    // Expression
    this.expressions = new Expression []
    { pExpression1, pExpression2 };
    this.expressions [ 0 ].setParent ( this );
    this.expressions [ 1 ].setParent ( this );
    checkDisjunction ();
  }


  /**
   * Allocates a new <code>CurriedLet</code> instance.
   * 
   * @param pIdentifiers an array with atleast two identifiers, where the first
   *          identifier is the name to use for the function and the remaining
   *          identifiers specify the parameters for the function.
   * @param pTypes the types for the <code>identifiers</code>, see
   *          {@link #getTypes()} for an extensive description of the meaning of
   *          <code>types</code>.
   * @param pExpression1 the function body.
   * @param pExpression2 the second expression.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *           contains less than two identifiers, or the arity of
   *           <code>identifiers</code> and <code>types</code> doesn't
   *           match.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>types</code>, <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public CurriedLet ( Identifier [] pIdentifiers, MonoType [] pTypes,
      Expression pExpression1, Expression pExpression2, int pParserStartOffset,
      int pParserEndOffset )
  {
    this ( pIdentifiers, pTypes, pExpression1, pExpression2 );
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  protected void checkDisjunction ()
  {
    // Identifier 0
    ArrayList < Identifier > allIdentifiers = this.expressions [ 1 ]
        .getIdentifiersAll ();
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ();
    for ( Identifier allId : allIdentifiers )
    {
      if ( ( this.identifiers [ 0 ].equals ( allId ) )
          && ( ! ( ( Identifier.Set.VARIABLE.equals ( allId.getSet () ) || ( Identifier.Set.METHOD
              .equals ( allId.getSet () ) ) ) ) ) )
      {
        negativeIdentifiers.add ( allId );
      }
    }
    /*
     * Throw an exception, if the negative identifier list contains one or more
     * identifiers. If this happens, all Identifiers are added.
     */
    if ( negativeIdentifiers.size () > 0 )
    {
      negativeIdentifiers.clear ();
      for ( Identifier allId : allIdentifiers )
      {
        if ( this.identifiers [ 0 ].equals ( allId ) )
        {
          negativeIdentifiers.add ( allId );
        }
      }
      negativeIdentifiers.add ( this.identifiers [ 0 ] );
      LanguageParserMultiException
          .throwExceptionDisjunction ( negativeIdentifiers );
    }
    // Identifier 1-n
    allIdentifiers = this.expressions [ 0 ].getIdentifiersAll ();
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
   * 
   * @see Expression#clone()
   */
  @Override
  public CurriedLet clone ()
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
    return new CurriedLet ( newIdentifiers, newTypes, this.expressions [ 0 ]
        .clone (), this.expressions [ 1 ].clone () );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @Override
  public boolean equals ( Object pObject )
  {
    if ( ( pObject instanceof CurriedLet )
        && ( this.getClass ().equals ( pObject.getClass () ) ) )
    {
      CurriedLet other = ( CurriedLet ) pObject;
      return ( ( Arrays.equals ( this.identifiers, other.identifiers ) )
          && ( Arrays.equals ( this.types, other.types ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.expressions [ 1 ]
          .equals ( other.expressions [ 1 ] ) ) );
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
   * Returns the first expression.
   * 
   * @return the first expression.
   */
  public Expression getE1 ()
  {
    return this.expressions [ 0 ];
  }


  /**
   * Returns the second expression.
   * 
   * @return the second expression.
   */
  public Expression getE2 ()
  {
    return this.expressions [ 1 ];
  }


  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
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
   * Returns the identifiers, where the first identifier is the name of the
   * function and the remaining identifiers name the parameters of the
   * functions, in a curried fashion.
   * 
   * @return the identifiers.
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
      ArrayList < Identifier > boundE1 = this.expressions [ 0 ]
          .getIdentifiersFree ();
      ArrayList < Identifier > boundE2 = this.expressions [ 1 ]
          .getIdentifiersFree ();
      for ( int i = 0 ; i < this.identifiers.length ; i++ )
      {
        if ( i == 0 )
        {
          ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ();
          for ( Identifier freeId : boundE2 )
          {
            if ( this.identifiers [ 0 ].equals ( freeId ) )
            {
              freeId.setBoundTo ( this, this.identifiers [ 0 ] );
              boundIdList.add ( freeId );
            }
          }
          this.boundIdentifiers.add ( boundIdList );
        }
        else
        {
          /*
           * An Identifier has no binding, if an Identifier after him has the
           * same name. Example: let f x x = x + 1 in f 1 2.
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
            for ( Identifier freeId : boundE1 )
            {
              if ( this.identifiers [ i ].equals ( freeId ) )
              {
                boundIdList.add ( freeId );
              }
            }
            for ( Identifier boundId : boundIdList )
            {
              boundId.setBoundTo ( this, this.identifiers [ i ] );
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
   * 
   * @see Expression#getIdentifiersFree()
   */
  @Override
  public ArrayList < Identifier > getIdentifiersFree ()
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ();
      ArrayList < Identifier > freeE1 = new ArrayList < Identifier > ();
      ArrayList < Identifier > freeE2 = new ArrayList < Identifier > ();
      freeE1.addAll ( this.expressions [ 0 ].getIdentifiersFree () );
      for ( int i = 1 ; i < this.identifiers.length ; i++ )
      {
        while ( freeE1.remove ( this.identifiers [ i ] ) )
        {
          // Remove all Identifiers with the same name
        }
      }
      freeE2.addAll ( this.expressions [ 1 ].getIdentifiersFree () );
      while ( freeE2.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
      this.identifiersFree.addAll ( freeE1 );
      this.identifiersFree.addAll ( freeE2 );
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
   * Returns the types for the <code>identifiers</code>.
   * <code>let id (id1:tau1)...(idn:taun): tau = e1 in e2</code> is translated
   * to <code>let id = lambda id1:tau1...lambda idn:taun.e1 in e2</code> which
   * means <code>types[0]</code> is used for <code>identifiers[0]</code> and
   * so on (where <code>types[0]</code> corresponds to <code>tau</code> in
   * the example above, and <code>identifiers[0]</code> to <code>id</code>).
   * Any of the types may be null, in which case the type will be inferred in
   * the type checker, while the <code>types</code> itself may not be
   * <code>null</code>. For recursion (see {@link CurriedLetRec}) the
   * <code>tau</code> is used to build the type of the recursive identifier.
   * 
   * @return the types.
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
   * 
   * @see Expression#hashCode()
   */
  @Override
  public int hashCode ()
  {
    return this.identifiers.hashCode () + this.expressions [ 0 ].hashCode ()
        + this.expressions [ 1 ].hashCode ()
        + ( ( this.types == null ) ? 0 : this.types.hashCode () );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @Override
  public CurriedLet substitute ( Identifier pId, Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable () )
    {
      throw new NotOnlyFreeVariableException ();
    }
    Identifier [] newIdentifiers = new Identifier [ this.identifiers.length ];
    for ( int i = 0 ; i < newIdentifiers.length ; i++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ];
    }
    Expression newE1 = this.expressions [ 0 ];
    Expression newE2 = this.expressions [ 1 ];
    boolean found = false;
    /*
     * Do not substitute in e1, if the Identifiers are equal.
     */
    for ( int i = 1 ; i < this.identifiers.length ; i++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        found = true;
        break;
      }
    }
    if ( !found )
    {
      for ( int i = 1 ; i < newIdentifiers.length ; i++ )
      {
        BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ();
        boundRenaming.add ( this.expressions [ 0 ].getIdentifiersFree () );
        boundRenaming.remove ( newIdentifiers [ i ] );
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
         * name. For example: "let a = b in let f b b = b a in f".
         */
        for ( int j = 1 ; j < i ; j++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            newId = newIdentifiers [ j ];
          }
        }
        /*
         * Substitute the old Identifier only with the new Identifier, if they
         * are different.
         */
        if ( !newIdentifiers [ i ].equals ( newId ) )
        {
          newE1 = newE1.substitute ( newIdentifiers [ i ], newId );
          newIdentifiers [ i ] = newId;
        }
      }
      /*
       * Perform the substitution in e1.
       */
      newE1 = newE1.substitute ( pId, pExpression );
    }
    if ( ! ( this.identifiers [ 0 ].equals ( pId ) ) )
    {
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ();
      boundRenaming.add ( this.expressions [ 1 ].getIdentifiersFree () );
      boundRenaming.remove ( this.identifiers [ 0 ] );
      boundRenaming.add ( pExpression.getIdentifiersFree () );
      boundRenaming.add ( pId );
      Identifier newId = boundRenaming.newIdentifier ( this.identifiers [ 0 ] );
      /*
       * Substitute the old Identifier only with the new Identifier, if they are
       * different.
       */
      if ( !this.identifiers [ 0 ].equals ( newId ) )
      {
        newE2 = newE2.substitute ( this.identifiers [ 0 ], newId );
        newIdentifiers [ 0 ] = newId;
      }
      /*
       * Perform the substitution in e2.
       */
      newE2 = newE2.substitute ( pId, pExpression );
    }
    return new CurriedLet ( newIdentifiers, this.types, newE1, newE2 );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @Override
  public CurriedLet substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType [] newTypes = new MonoType [ this.types.length ];
    for ( int i = 0 ; i < newTypes.length ; i++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .substitute ( pTypeSubstitution );
    }
    Expression newE1 = this.expressions [ 0 ].substitute ( pTypeSubstitution );
    Expression newE2 = this.expressions [ 1 ].substitute ( pTypeSubstitution );
    return new CurriedLet ( this.identifiers, newTypes, newE1, newE2 );
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
    descriptions [ descriptions.length - 3 ] = this.types [ 0 ] == null ? LATEX_NO_TYPE
        : this.types [ 0 ].toPrettyString ().toString ();
    descriptions [ descriptions.length - 2 ] = this.expressions [ 0 ]
        .toPrettyString ().toString ();
    descriptions [ descriptions.length - 1 ] = this.expressions [ 1 ]
        .toPrettyString ().toString ();
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_LET, LATEX_CURRIED_LET, pIndent, descriptions );
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
            PRIO_LET_TAU );
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
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), PRIO_LET_E1 );
    builder.addBreak ();
    builder.addBuilder ( this.expressions [ 1 ].toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), PRIO_LET_E2 );
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
          PRIO_LET );
      this.prettyStringBuilder.addKeyword ( PRETTY_LET );
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
              PRIO_LET_TAU );
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
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), PRIO_LET_E1 );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBreak ();
      this.prettyStringBuilder.addText ( PRETTY_IN );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), PRIO_LET_E2 );
    }
    return this.prettyStringBuilder;
  }
}
