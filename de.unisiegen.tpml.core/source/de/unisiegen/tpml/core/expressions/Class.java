package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.TreeSet ;
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


/**
 * Instances of this class represent class expressions.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Class extends Expression implements BoundIdentifiers ,
    DefaultTypes , DefaultExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private static final int [ ] INDICES_ID = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * String for the case that the identifier is null.
   */
  private static final String IDENTIFIER_NULL = "identifier is null" ; //$NON-NLS-1$


  /**
   * String for the case that the body is null.
   */
  private static final String BODY_NULL = "body is null" ; //$NON-NLS-1$


  /**
   * String for the case that the sub {@link Expression} is not a
   * {@link Inherit} and not a {@link Row}.
   */
  private static final String BODY_INCORRECT = "the sub body is not a body and not a row" ; //$NON-NLS-1$


  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET = "the set of the identifier has to be 'self'" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Class.class ) ;


  /**
   * The keyword <code>(</code>.
   */
  private static final String LPAREN = "(" ; //$NON-NLS-1$


  /**
   * The keyword <code>)</code>.
   */
  private static final String RPAREN = ")" ; //$NON-NLS-1$


  /**
   * The space string.
   */
  private static final String SPACE = " " ; //$NON-NLS-1$


  /**
   * The keyword <code>class</code>.
   */
  private static final String CLASS = "class" ; //$NON-NLS-1$


  /**
   * The keyword <code>end</code>.
   */
  private static final String END = "end" ; //$NON-NLS-1$


  /**
   * The keyword <code>:</code>.
   */
  private static final String COLON = ":" ; //$NON-NLS-1$


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * The list of identifiers.
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
   * Allocates a new {@link Class}.
   * 
   * @param pIdentifier The {@link Identifier}.
   * @param pTau The {@link Type}.
   * @param pBody The child body.
   */
  public Class ( Identifier pIdentifier , MonoType pTau , Expression pBody )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( IDENTIFIER_NULL ) ;
    }
    if ( ! Identifier.Set.SELF.equals ( pIdentifier.getSet ( ) ) )
    {
      throw new IllegalArgumentException ( WRONG_SET ) ;
    }
    if ( pBody == null )
    {
      throw new NullPointerException ( BODY_NULL ) ;
    }
    if ( ( ! ( pBody instanceof Inherit ) ) && ( ! ( pBody instanceof Row ) ) )
    {
      throw new IllegalArgumentException ( BODY_INCORRECT ) ;
    }
    // Identifier
    this.identifiers = new Identifier [ ]
    { pIdentifier } ;
    this.identifiers [ 0 ].setParent ( this ) ;
    // Type
    this.types = new MonoType [ ]
    { pTau } ;
    if ( this.types [ 0 ] != null )
    {
      this.types [ 0 ].setParent ( this ) ;
    }
    // Expression
    this.expressions = new Expression [ ]
    { pBody } ;
    this.expressions [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new {@link Class}.
   * 
   * @param pIdentifier The {@link Identifier}.
   * @param pTau The {@link Type}.
   * @param pBody The child body.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Class ( Identifier pIdentifier , MonoType pTau , Expression pBody ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifier , pTau , pBody ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Class clone ( )
  {
    return new Class ( this.identifiers [ 0 ].clone ( ) ,
        this.types [ 0 ] == null ? null : this.types [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Class )
    {
      Class other = ( Class ) pObject ;
      return ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) )
          && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
              : ( this.types [ 0 ].equals ( other.types [ 0 ] ) )
                  && ( this.identifiers [ 0 ].equals ( other.identifiers [ 0 ] ) ) ) ;
    }
    return false ;
  }


  /**
   * Returns the sub body.
   * 
   * @return the sub body.
   */
  public Expression getBody ( )
  {
    return this.expressions [ 0 ] ;
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
   * Returns the sub {@link Expression}s.
   * 
   * @return the sub {@link Expression}s.
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
   * Returns the {@link Identifier} of this {@link Expression}.
   * 
   * @return The {@link Identifier} of this {@link Expression}.
   */
  public Identifier getId ( )
  {
    return this.identifiers [ 0 ] ;
  }


  /**
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns a list of in this {@link Expression} bound {@link Identifier}s.
   * 
   * @return A list of in this {@link Expression} bound {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ( )
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( 1 ) ;
      ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > boundE = this.expressions [ 0 ]
          .getIdentifiersFree ( ) ;
      for ( Identifier freeId : boundE )
      {
        if ( this.identifiers [ 0 ].equals ( freeId ) )
        {
          freeId.setBoundTo ( this , this.identifiers [ 0 ] ) ;
          boundIdList.add ( freeId ) ;
        }
      }
      this.boundIdentifiers.add ( boundIdList ) ;
    }
    return this.boundIdentifiers ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
      while ( this.identifiersFree.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
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
    return INDICES_ID ;
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
    commands.add ( new DefaultLatexCommand (
        "boldClass" , 0 , "\\textbf{class}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( "boldEnd" , 0 , "\\textbf{end}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_CLASS , 3 ,
        "\\ifthenelse{\\equal{#2}{}}" //$NON-NLS-1$
            + "{\\boldClass\\ (#1)\\ #3\\ \\boldEnd}" //$NON-NLS-1$
            + "{\\boldClass\\ (#1\\colon\\ #2)\\ #3\\ \\boldEnd}" ) ) ; //$NON-NLS-1$
    for ( LatexCommand command : this.identifiers [ 0 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
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
    for ( LatexPackage pack : this.identifiers [ 0 ].getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
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
   * Returns the sub {@link Type}.
   * 
   * @return the sub {@link Type}.
   */
  public MonoType getTau ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the sub {@link Type}s.
   * 
   * @return the sub {@link Type}s.
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
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers [ 0 ].hashCode ( )
        + this.expressions [ 0 ].hashCode ( )
        + ( this.types [ 0 ] == null ? 0 : this.types [ 0 ].hashCode ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.expressions [ 0 ] instanceof Row ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Class substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    if ( Identifier.Set.SELF.equals ( pId.getSet ( ) ) )
    {
      return this ;
    }
    /*
     * Perform the substitution.
     */
    Expression body = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    return new Class ( this.identifiers [ 0 ] , this.types [ 0 ] , body ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Class substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    Expression body = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new Class ( this.identifiers [ 0 ] , newTau , body ) ;
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
          PRIO_CLASS , LATEX_CLASS ) ;
      this.latexStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.types [ 0 ] == null )
      {
        this.latexStringBuilder.addEmptyBuilder ( ) ;
      }
      else
      {
        this.latexStringBuilder.addBuilder ( this.types [ 0 ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
            PRIO_CLASS_TAU ) ;
      }
      this.latexStringBuilder.addBreak ( ) ;
      this.latexStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
          PRIO_CLASS_BODY ) ;
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
          PRIO_CLASS ) ;
      this.prettyStringBuilder.addKeyword ( CLASS ) ;
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addText ( LPAREN ) ;
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( COLON ) ;
        this.prettyStringBuilder.addText ( SPACE ) ;
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_CLASS_TAU ) ;
      }
      this.prettyStringBuilder.addText ( RPAREN ) ;
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CLASS_BODY ) ;
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( END ) ;
    }
    return this.prettyStringBuilder ;
  }
}
