package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Represents a multi lambda abstract, which takes a single tuple argument as
 * parameter and splits the tuple items to various identifiers.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Lambda
 * @see MultiLet
 * @see Value
 */
public final class MultiLambda extends Value implements BoundIdentifiers ,
    DefaultTypes , DefaultExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * String for the case that the identifiers are null.
   */
  private static final String IDENTIFIERS_NULL = "identifiers is null" ; //$NON-NLS-1$


  /**
   * String for the case that one identifier are null.
   */
  private static final String IDENTIFIER_NULL = "one identifier is null" ; //$NON-NLS-1$


  /**
   * String for the case that the identifiers are empty.
   */
  private static final String IDENTIFIERS_EMPTY = "identifiers is empty" ; //$NON-NLS-1$


  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET = "the set of the identifier has to be 'variable'" ; //$NON-NLS-1$


  /**
   * String for the case that the expression is null.
   */
  private static final String EXPRESSION_NULL = "expression is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( MultiLambda.class ) ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [ ] indicesId ;


  /**
   * The tuple parameter identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   */
  private MonoType [ ] types ;


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new <code>MultiLambda</code> expression with the specified
   * <code>identifiers</code> and the function body <code>e</code>.
   * 
   * @param pIdentifiers non-empty set of identifiers.
   * @param pTau the type of the identifiers or <code>null</code>.
   * @param pExpression the function body.
   * @throws IllegalArgumentException if the <code>identifiers</code> list is
   *           empty.
   * @throws NullPointerException if <code>identifiers</code> or
   *           <code>e</code> is <code>null</code>.
   */
  public MultiLambda ( Identifier [ ] pIdentifiers , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( IDENTIFIERS_NULL ) ;
    }
    if ( pIdentifiers.length == 0 )
    {
      throw new IllegalArgumentException ( IDENTIFIERS_EMPTY ) ;
    }
    for ( Identifier id : pIdentifiers )
    {
      if ( id == null )
      {
        throw new NullPointerException ( IDENTIFIER_NULL ) ;
      }
      if ( ! Identifier.Set.VARIABLE.equals ( id.getSet ( ) ) )
      {
        throw new IllegalArgumentException ( WRONG_SET ) ;
      }
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( EXPRESSION_NULL ) ;
    }
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
    }
    // Type
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pTau ;
    if ( this.types [ 0 ] != null )
    {
      this.types [ 0 ].setParent ( this ) ;
    }
    // Expression
    this.expressions = new Expression [ ]
    { pExpression } ;
    this.expressions [ 0 ].setParent ( this ) ;
    checkDisjunction ( ) ;
  }


  /**
   * Allocates a new <code>MultiLambda</code> expression with the specified
   * <code>identifiers</code> and the function body <code>e</code>.
   * 
   * @param pIdentifiers non-empty set of identifiers.
   * @param pTau the type of the identifiers or <code>null</code>.
   * @param pExpression the function body.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws IllegalArgumentException if the <code>identifiers</code> list is
   *           empty.
   * @throws NullPointerException if <code>identifiers</code> or
   *           <code>e</code> is <code>null</code>.
   */
  public MultiLambda ( Identifier [ ] pIdentifiers , MonoType pTau ,
      Expression pExpression , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifiers , pTau , pExpression ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  private void checkDisjunction ( )
  {
    ArrayList < Identifier > allIdentifiers = this.expressions [ 0 ]
        .getIdentifiersAll ( ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier current : this.identifiers )
    {
      negativeIdentifiers.clear ( ) ;
      for ( Identifier allId : allIdentifiers )
      {
        if ( ( current.equals ( allId ) )
            && ( ! ( ( Identifier.Set.VARIABLE.equals ( allId.getSet ( ) ) || ( Identifier.Set.METHOD
                .equals ( allId.getSet ( ) ) ) ) ) ) )
        {
          negativeIdentifiers.add ( allId ) ;
        }
      }
      /*
       * Throw an exception, if the negative identifier list contains one or
       * more identifiers. If this happens, all Identifiers are added.
       */
      if ( negativeIdentifiers.size ( ) > 0 )
      {
        negativeIdentifiers.clear ( ) ;
        for ( Identifier allId : allIdentifiers )
        {
          if ( current.equals ( allId ) )
          {
            negativeIdentifiers.add ( allId ) ;
          }
        }
        negativeIdentifiers.add ( current ) ;
        LanguageParserMultiException
            .throwExceptionDisjunction ( negativeIdentifiers ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public MultiLambda clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new MultiLambda ( newIdentifiers , this.types [ 0 ] == null ? null
        : this.types [ 0 ].clone ( ) , this.expressions [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof MultiLambda )
    {
      MultiLambda other = ( MultiLambda ) pObject ;
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
          : ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return CAPTION ;
  }


  /**
   * Returns the function body expression.
   * 
   * @return the function body expression.
   */
  public Expression getE ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * Returns the indices of the child {@link Expression}s.
   * 
   * @return The indices of the child {@link Expression}s.
   */
  public int [ ] getExpressionsIndex ( )
  {
    return INDICES_E ;
  }


  /**
   * Returns the identifiers for the tuple parameter.
   * 
   * @return the identifiers for the tuple parameter.
   * @see #getIdentifiers()
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns a list of lists of in this {@link Expression} bound
   * {@link Identifier}s.
   * 
   * @return A list of lists of in this {@link Expression} bound
   *         {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ( )
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( 1 ) ;
      ArrayList < Identifier > boundE = this.expressions [ 0 ]
          .getIdentifiersFree ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        /*
         * An Identifier has no binding, if an Identifier after him has the same
         * name. Example: (λ(x, x).x) (1, 2).
         */
        boolean hasBinding = true ;
        for ( int j = i + 1 ; j < this.identifiers.length ; j ++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            hasBinding = false ;
            break ;
          }
        }
        ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
        if ( hasBinding )
        {
          for ( Identifier freeId : boundE )
          {
            if ( this.identifiers [ i ].equals ( freeId ) )
            {
              freeId.setBoundTo ( this , this.identifiers [ i ] ) ;
              boundIdList.add ( freeId ) ;
            }
          }
        }
        this.boundIdentifiers.add ( boundIdList ) ;
      }
    }
    return this.boundIdentifiers ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#getIdentifiersFree()
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        while ( this.identifiersFree.remove ( this.identifiers [ i ] ) )
        {
          // Remove all Identifiers with the same name
        }
      }
    }
    return this.identifiersFree ;
  }


  /**
   * Returns the indices of the child {@link Identifier}s.
   * 
   * @return The indices of the child {@link Identifier}s.
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return this.indicesId ;
  }


  /**
   * Returns the type of the <code>identifiers</code> or <code>null</code>
   * if no type was specified.
   * 
   * @return the type of the identifiers;
   */
  public MonoType getTau ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the types for the <code>identifiers</code>.
   * 
   * @return the types.
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypesIndex ( )
  {
    return INDICES_TYPE ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( )
        + ( ( this.types [ 0 ] == null ) ? 0 : this.types [ 0 ].hashCode ( ) )
        + this.expressions [ 0 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public MultiLambda substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        return this ;
      }
    }
    Expression newE = this.expressions [ 0 ] ;
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ] ;
    }
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
      boundRenaming.add ( this.getIdentifiersFree ( ) ) ;
      boundRenaming.add ( pExpression.getIdentifiersFree ( ) ) ;
      boundRenaming.add ( pId ) ;
      /*
       * The new Identifier should not be equal to an other Identifier.
       */
      if ( boundRenaming.contains ( newIdentifiers [ i ] ) )
      {
        for ( int j = 0 ; j < newIdentifiers.length ; j ++ )
        {
          if ( i != j )
          {
            boundRenaming.add ( newIdentifiers [ j ] ) ;
          }
        }
      }
      Identifier newId = boundRenaming.newIdentifier ( newIdentifiers [ i ] ) ;
      /*
       * Search for an Identifier before the current Identifier with the same
       * name. For example: "let a = b in lambda (b,b).a".
       */
      for ( int j = 0 ; j < i ; j ++ )
      {
        if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
        {
          newId = newIdentifiers [ j ] ;
        }
      }
      /*
       * Substitute the old Identifier only with the new Identifier, if they are
       * different.
       */
      if ( ! newIdentifiers [ i ].equals ( newId ) )
      {
        newE = newE.substitute ( newIdentifiers [ i ] , newId ) ;
        newIdentifiers [ i ] = newId ;
      }
    }
    /*
     * Perform the substitution.
     */
    newE = newE.substitute ( pId , pExpression ) ;
    return new MultiLambda ( newIdentifiers , this.types [ 0 ] , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public MultiLambda substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    return new MultiLambda ( this.identifiers , newTau , this.expressions [ 0 ]
        .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEYWORD_LAMBDA , 0 ,
        "\\textbf{$\\lambda$}" ) ) ; //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_MULTI_LAMBDA , 3 ,
        "\\ifthenelse{\\equal{#2}{}}{\\" + LATEX_KEYWORD_LAMBDA + "(#1).#3}" //$NON-NLS-1$//$NON-NLS-2$
            + "{\\" + LATEX_KEYWORD_LAMBDA + "(#1)\\colon\\ #2.#3}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    for ( Identifier id : this.identifiers )
    {
      for ( LatexCommand command : id.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    if ( this.types [ 0 ] != null )
    {
      for ( LatexCommand command : this.types [ 0 ].getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    for ( LatexCommand command : this.expressions [ 0 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  @ Override
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    packages.add ( new DefaultLatexPackage ( "ifthen" ) ) ; //$NON-NLS-1$
    for ( Identifier id : this.identifiers )
    {
      for ( LatexPackage pack : id.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    if ( this.types [ 0 ] != null )
    {
      for ( LatexPackage pack : this.types [ 0 ].getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    for ( LatexPackage pack : this.expressions [ 0 ].getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_LAMBDA , LATEX_MULTI_LAMBDA ) ;
      this.latexStringBuilder.addBuilderBegin ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; ++ i )
      {
        if ( i > 0 )
        {
          this.latexStringBuilder.addText ( LATEX_COMMA ) ;
          this.latexStringBuilder.addText ( LATEX_SPACE ) ;
        }
        this.latexStringBuilder.addBuilder ( this.identifiers [ i ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory ) , PRIO_ID ) ;
      }
      this.latexStringBuilder.addBuilderEnd ( ) ;
      if ( this.types [ 0 ] == null )
      {
        this.latexStringBuilder.addEmptyBuilder ( ) ;
      }
      else
      {
        this.latexStringBuilder.addBuilder ( this.types [ 0 ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
            PRIO_LAMBDA_TAU ) ;
      }
      this.latexStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) , PRIO_LAMBDA_E ) ;
    }
    return this.latexStringBuilder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LAMBDA ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_LAMBDA ) ;
      this.prettyStringBuilder.addText ( PRETTY_LPAREN ) ;
      for ( int i = 0 ; i < this.identifiers.length ; ++ i )
      {
        if ( i > 0 )
        {
          this.prettyStringBuilder.addText ( PRETTY_COMMA ) ;
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      }
      this.prettyStringBuilder.addText ( PRETTY_RPAREN ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( PRETTY_COLON ) ;
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LAMBDA_TAU ) ;
      }
      this.prettyStringBuilder.addText ( PRETTY_DOT ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_LAMBDA_E ) ;
    }
    return this.prettyStringBuilder ;
  }
}
