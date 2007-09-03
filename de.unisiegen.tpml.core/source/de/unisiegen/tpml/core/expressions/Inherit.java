package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent inherit expressions.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Inherit extends Expression implements BoundIdentifiers ,
    DefaultExpressions
{
  /**
   * The string of an self identifier.
   */
  private static final String SELF = "self" ; //$NON-NLS-1$


  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 , - 1 } ;


  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET = "the set of the identifier has to be 'attribute'" ; //$NON-NLS-1$


  /**
   * String for the case that the identifiers are null.
   */
  private static final String IDENTIFIERS_NULL = "identifiers is null" ; //$NON-NLS-1$


  /**
   * String for the case that one identifier are null.
   */
  private static final String IDENTIFIER_NULL = "one identifier is null" ; //$NON-NLS-1$


  /**
   * String for the case that the expression is null.
   */
  private static final String EXPRESSION_NULL = "expression is null" ; //$NON-NLS-1$


  /**
   * String for the case that the body is null.
   */
  private static final String BODY_NULL = "body is null" ; //$NON-NLS-1$


  /**
   * String for the case that the sub body is not a {@link Inherit} and not a
   * {@link Row}.
   */
  private static final String BODY_INCORRECT = "the sub body is not a body and not a row" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Inherit.class ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static TreeSet < LatexCommand > getLatexCommandsStatic ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_INHERIT , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{inherit}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_FROM , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{from}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_INHERIT , 3 , "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_INHERIT + "\\ #1\\ \\" //$NON-NLS-1$//$NON-NLS-2$
        + LATEX_KEY_FROM + "\\ #2\\ ;\\ #3" , "a1, ... , ak" , "e" , "b" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    return commands ;
  }


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [ ] indicesId ;


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  protected Identifier [ ] identifiers ;


  /**
   * Allocates a new {@link Inherit}.
   * 
   * @param pIdentifiers The attribute {@link Identifier}s.
   * @param pExpression The child {@link Expression}.
   * @param pBody The child body.
   */
  public Inherit ( Identifier [ ] pIdentifiers , Expression pExpression ,
      Expression pBody )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( IDENTIFIERS_NULL ) ;
    }
    for ( Identifier id : pIdentifiers )
    {
      if ( id == null )
      {
        throw new NullPointerException ( IDENTIFIER_NULL ) ;
      }
      if ( ! Identifier.Set.ATTRIBUTE.equals ( id.getSet ( ) ) )
      {
        throw new IllegalArgumentException ( WRONG_SET ) ;
      }
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( EXPRESSION_NULL ) ;
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
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
    }
    // Expression
    this.expressions = new Expression [ ]
    { pExpression , pBody } ;
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ].setParent ( this ) ;
    // Check the disjunction
    getIdentifiersBound ( ) ;
    checkDisjunction ( ) ;
  }


  /**
   * Allocates a new {@link Inherit}.
   * 
   * @param pIdentifiers The attribute {@link Identifier}s.
   * @param pExpression The child {@link Expression}.
   * @param pBody The child body.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Inherit ( Identifier [ ] pIdentifiers , Expression pExpression ,
      Expression pBody , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifiers , pExpression , pBody ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  private void checkDisjunction ( )
  {
    /*
     * Check the disjunction of the attribute identifiers.
     */
    ArrayList < Identifier > allIdentifiers = this.expressions [ 1 ]
        .getIdentifiersAll ( ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier idAttribute : this.identifiers )
    {
      negativeIdentifiers.clear ( ) ;
      for ( Identifier allId : allIdentifiers )
      {
        if ( ( idAttribute.equals ( allId ) )
            && ( ! Identifier.Set.ATTRIBUTE.equals ( allId.getSet ( ) ) ) )
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
          if ( idAttribute.equals ( allId ) )
          {
            negativeIdentifiers.add ( allId ) ;
          }
        }
        negativeIdentifiers.add ( idAttribute ) ;
        LanguageParserMultiException
            .throwExceptionDisjunction ( negativeIdentifiers ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Inherit clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new Inherit ( newIdentifiers , this.expressions [ 0 ].clone ( ) ,
        this.expressions [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Inherit )
    {
      Inherit other = ( Inherit ) pObject ;
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.expressions [ 1 ]
          .equals ( other.expressions [ 1 ] ) ) ) ;
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
    return this.expressions [ 1 ] ;
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
   * Returns a list of all {@link Attribute} {@link Identifier}s in the domain
   * of this {@link Expression}.
   * 
   * @return A list of all {@link Attribute} {@link Identifier}s in the domain
   *         of this {@link Expression}.
   */
  @ Override
  public ArrayList < Identifier > getDomA ( )
  {
    if ( this.domA == null )
    {
      this.domA = new ArrayList < Identifier > ( ) ;
      for ( Identifier a : this.identifiers )
      {
        this.domA.add ( a ) ;
      }
      this.domA.addAll ( this.expressions [ 0 ].getDomA ( ) ) ;
      this.domA.addAll ( this.expressions [ 1 ].getDomA ( ) ) ;
    }
    return this.domA ;
  }


  /**
   * Returns the sub {@link Expression}.
   * 
   * @return the sub {@link Expression}.
   */
  public Expression getE ( )
  {
    return this.expressions [ 0 ] ;
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
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> (
          this.identifiers.length + 1 ) ;
      ArrayList < Identifier > boundExpressionBody = new ArrayList < Identifier > ( ) ;
      boundExpressionBody
          .addAll ( this.expressions [ 1 ].getIdentifiersFree ( ) ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
        for ( Identifier freeId : boundExpressionBody )
        {
          if ( this.identifiers [ i ].equals ( freeId ) )
          {
            freeId.setBoundTo ( this , this.identifiers [ i ] ) ;
            freeId.setSet ( Identifier.Set.ATTRIBUTE ) ;
            boundIdList.add ( freeId ) ;
          }
        }
        this.boundIdentifiers.add ( boundIdList ) ;
      }
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
      this.identifiersFree.add ( new Identifier ( SELF , Identifier.Set.SELF ) ) ;
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
      ArrayList < Identifier > freeB = new ArrayList < Identifier > ( ) ;
      freeB.addAll ( this.expressions [ 1 ].getIdentifiersFree ( ) ) ;
      for ( Identifier a : this.identifiers )
      {
        while ( freeB.remove ( a ) )
        {
          // Remove all Identifiers with the same name
        }
      }
      this.identifiersFree.addAll ( freeB ) ;
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
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    for ( LatexCommand command : getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }return commands ;
  }

  /**
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  @ Override
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      this.prefix = PREFIX_BODY ;
    }
    return this.prefix ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Inherit substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    boolean substituteBody = true ;
    for ( Identifier a : this.identifiers )
    {
      if ( pId.equals ( a ) )
      {
        substituteBody = false ;
        break ;
      }
    }
    /*
     * Perform the substitution.
     */
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    Expression newBody ;
    if ( substituteBody )
    {
      newBody = this.expressions [ 1 ].substitute ( pId , pExpression ) ;
    }
    else
    {
      newBody = this.expressions [ 1 ] ;
    }
    return new Inherit ( this.identifiers , newE , newBody ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Inherit substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    Expression newBody = this.expressions [ 1 ].substitute ( pTypeSubstitution ) ;
    return new Inherit ( this.identifiers , newE , newBody ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    StringBuilder identifier = new StringBuilder ( ) ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      identifier
          .append ( this.identifiers [ i ].toPrettyString ( ).toString ( ) ) ;
      if ( i != this.identifiers.length - 1 )
      {
        identifier.append ( PRETTY_COMMA ) ;
        identifier.append ( PRETTY_SPACE ) ;
      }
    }
    String descriptions[] = new String [ 2 + this.identifiers.length
        + this.expressions.length ] ;
    descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
    descriptions [ 1 ] = identifier.toString ( ) ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      descriptions [ 2 + i ] = this.identifiers [ i ].toPrettyString ( )
          .toString ( ) ;
    }
    descriptions [ descriptions.length - 2 ] = this.expressions [ 0 ]
        .toPrettyString ( ).toString ( ) ;
    descriptions [ descriptions.length - 1 ] = this.expressions [ 1 ]
        .toPrettyString ( ).toString ( ) ;
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_INHERIT , LATEX_INHERIT , pIndent , descriptions ) ;
    builder.addBuilderBegin ( ) ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      builder.addBuilder ( this.identifiers [ i ].toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , PRIO_ID ) ;
      if ( i != this.identifiers.length - 1 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT )
            + LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
      }
    }
    builder.addBreak ( ) ;
    builder.addBuilderEnd ( ) ;
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_INHERIT_E ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.expressions [ 1 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_INHERIT_B ) ;
    return builder ;
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
          PRIO_INHERIT ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_INHERIT ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        if ( i != this.identifiers.length - 1 )
        {
          this.prettyStringBuilder.addText ( PRETTY_COMMA ) ;
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        }
      }
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_FROM ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_INHERIT_E ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_SEMI ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_INHERIT_B ) ;
    }
    return this.prettyStringBuilder ;
  }
}
