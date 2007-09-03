package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Represents the <code>let rec</code> expression, which is syntactic sugar
 * for <b>(LET)</b> and <b>(REC)</b>. The expression
 * <code>let rec id = e1 in e2</code> is equal to
 * <code>let id = rec id.e1 in e2</code>
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see de.unisiegen.tpml.core.expressions.Let
 */
public final class LetRec extends Let implements BoundIdentifiers ,
    DefaultTypes , DefaultExpressions
{
  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( LetRec.class ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static TreeSet < LatexCommand > getLatexCommandsStatic ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_LET , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{let}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_REC , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{rec}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_IN , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{in}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_LET_REC , 4 ,
        "\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{\\color{" + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_LET //$NON-NLS-1$ //$NON-NLS-2$
            + "\\ \\" + LATEX_KEY_REC + "\\ #1\\ =\\ #3\\ \\" + LATEX_KEY_IN //$NON-NLS-1$ //$NON-NLS-2$
            + "\\ #4}" + LATEX_LINE_BREAK_NEW_COMMAND + "{\\color{" //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_LET + "\\ \\" //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_KEY_REC + "\\ #1\\colon\\ #2\\ =\\ #3\\ \\" + LATEX_KEY_IN //$NON-NLS-1$
            + "\\ #4}" , "id" , "tau" , "e1" , "e2" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    return commands ;
  }


  /**
   * Allocates a new <code>LetRec</code> with the given <code>id</code>,
   * <code>tau</code>, <code>e1</code> and <code>e2</code>.
   * 
   * @param pId the name of the identifier.
   * @param pTau the type for <code>id</code> or <code>null</code>.
   * @param pExpression1 the first expression.
   * @param pExpression2 the second expression.
   * @throws NullPointerException if <code>id</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public LetRec ( Identifier pId , MonoType pTau , Expression pExpression1 ,
      Expression pExpression2 )
  {
    super ( pId , pTau , pExpression1 , pExpression2 ) ;
  }


  /**
   * Allocates a new <code>LetRec</code> with the given <code>id</code>,
   * <code>tau</code>, <code>e1</code> and <code>e2</code>.
   * 
   * @param pId the name of the identifier.
   * @param pTau the type for <code>id</code> or <code>null</code>.
   * @param pExpression1 the first expression.
   * @param pExpression2 the second expression.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws NullPointerException if <code>id</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public LetRec ( Identifier pId , MonoType pTau , Expression pExpression1 ,
      Expression pExpression2 , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pId , pTau , pExpression1 , pExpression2 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  @ Override
  protected void checkDisjunction ( )
  {
    ArrayList < Identifier > allIdentifiers = new ArrayList < Identifier > ( ) ;
    allIdentifiers.addAll ( this.expressions [ 0 ].getIdentifiersAll ( ) ) ;
    allIdentifiers.addAll ( this.expressions [ 1 ].getIdentifiersAll ( ) ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier allId : allIdentifiers )
    {
      if ( ( this.identifiers [ 0 ].equals ( allId ) )
          && ( ! ( ( Identifier.Set.VARIABLE.equals ( allId.getSet ( ) ) || ( Identifier.Set.METHOD
              .equals ( allId.getSet ( ) ) ) ) ) ) )
      {
        negativeIdentifiers.add ( allId ) ;
      }
    }
    /*
     * Throw an exception, if the negative identifier list contains one or more
     * identifiers. If this happens, all Identifiers are added.
     */
    if ( negativeIdentifiers.size ( ) > 0 )
    {
      negativeIdentifiers.clear ( ) ;
      for ( Identifier allId : allIdentifiers )
      {
        if ( this.identifiers [ 0 ].equals ( allId ) )
        {
          negativeIdentifiers.add ( allId ) ;
        }
      }
      negativeIdentifiers.add ( this.identifiers [ 0 ] ) ;
      LanguageParserMultiException
          .throwExceptionDisjunction ( negativeIdentifiers ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#clone()
   */
  @ Override
  public LetRec clone ( )
  {
    return new LetRec ( this.identifiers [ 0 ].clone ( ) ,
        this.types [ 0 ] == null ? null : this.types [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) , this.expressions [ 1 ].clone ( ) ) ;
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
   * Returns a list of in this {@link Expression} bound {@link Identifier}s.
   * 
   * @return A list of in this {@link Expression} bound {@link Identifier}s.
   */
  @ Override
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ( )
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( 1 ) ;
      ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > boundE1 = this.expressions [ 0 ]
          .getIdentifiersFree ( ) ;
      for ( Identifier freeId : boundE1 )
      {
        if ( this.identifiers [ 0 ].equals ( freeId ) )
        {
          freeId.setBoundTo ( this , this.identifiers [ 0 ] ) ;
          boundIdList.add ( freeId ) ;
        }
      }
      ArrayList < Identifier > boundE2 = this.expressions [ 1 ]
          .getIdentifiersFree ( ) ;
      for ( Identifier freeId : boundE2 )
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
   * 
   * @see Let#getIdentifiersFree()
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
      this.identifiersFree.addAll ( this.expressions [ 1 ]
          .getIdentifiersFree ( ) ) ;
      while ( this.identifiersFree.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
    }
    return this.identifiersFree ;
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
    }
    return commands ;
  }

  /**
   * {@inheritDoc}
   * 
   * @see Let#substitute(Identifier, Expression)
   */
  @ Override
  public LetRec substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    /*
     * Do not substitute , if the Identifiers are equal.
     */
    if ( this.identifiers [ 0 ].equals ( pId ) )
    {
      return this ;
    }
    /*
     * Perform the bound renaming if required.
     */
    BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
    boundRenaming.add ( this.getIdentifiersFree ( ) ) ;
    boundRenaming.add ( pExpression.getIdentifiersFree ( ) ) ;
    boundRenaming.add ( pId ) ;
    Identifier newId = boundRenaming.newIdentifier ( this.identifiers [ 0 ] ) ;
    /*
     * Substitute the old Identifier only with the new Identifier, if they are
     * different.
     */
    Expression newE1 = this.expressions [ 0 ] ;
    Expression newE2 = this.expressions [ 1 ] ;
    if ( ! this.identifiers [ 0 ].equals ( newId ) )
    {
      newE1 = newE1.substitute ( this.identifiers [ 0 ] , newId ) ;
      newE2 = newE2.substitute ( this.identifiers [ 0 ] , newId ) ;
    }
    /*
     * Perform the substitution.
     */
    newE1 = newE1.substitute ( pId , pExpression ) ;
    newE2 = newE2.substitute ( pId , pExpression ) ;
    return new LetRec ( newId , this.types [ 0 ] , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#substitute(TypeSubstitution)
   */
  @ Override
  public LetRec substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType pTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    Expression newE1 = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.expressions [ 1 ].substitute ( pTypeSubstitution ) ;
    return new LetRec ( this.identifiers [ 0 ] , pTau , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_LET , LATEX_LET_REC , pIndent , this.toPrettyString ( )
            .toString ( ) , this.identifiers [ 0 ].toPrettyString ( )
            .toString ( ) , this.types [ 0 ] == null ? LATEX_EMPTY_STRING
            : this.types [ 0 ].toPrettyString ( ).toString ( ) ,
        this.expressions [ 0 ].toPrettyString ( ).toString ( ) ,
        this.expressions [ 1 ].toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.identifiers [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_ID ) ;
    if ( this.types [ 0 ] == null )
    {
      builder.addEmptyBuilder ( ) ;
    }
    else
    {
      builder.addBuilder ( this.types [ 0 ].toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_LET_TAU ) ;
    }
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_LET_E1 ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.expressions [ 1 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_LET_E2 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LET ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_LET ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_REC ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( PRETTY_COLON ) ;
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_EQUAL ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_IN ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
