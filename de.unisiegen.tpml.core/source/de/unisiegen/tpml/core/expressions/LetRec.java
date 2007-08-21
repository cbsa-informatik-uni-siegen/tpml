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
   * The keyword <code>let</code>.
   */
  private static final String LET = "let" ; //$NON-NLS-1$


  /**
   * The keyword <code>in</code>.
   */
  private static final String IN = "in" ; //$NON-NLS-1$  


  /**
   * The space string.
   */
  private static final String SPACE = " " ; //$NON-NLS-1$


  /**
   * The keyword <code>:</code>.
   */
  private static final String COLON = ":" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( LetRec.class ) ;


  /**
   * The equal string.
   */
  private static final String EQUAL = "=" ; //$NON-NLS-1$


  /**
   * The keyword <code>rec</code>.
   */
  private static final String REC = "rec" ; //$NON-NLS-1$ 


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
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( "boldLet" , 0 , "\\textbf{let}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( "boldRec" , 0 , "\\textbf{rec}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( "boldIn" , 0 , "\\textbf{in}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_LET_REC ,
            4 ,
            "\\ifthenelse{\\equal{#2}{}}" //$NON-NLS-1$
                + "{\\boldLet\\ \\boldRec\\ #1\\ =\\ #3\\ \\boldIn\\ #4}" //$NON-NLS-1$
                + "{\\boldLet\\ \\boldRec\\ #1\\colon\\ #2\\ =\\ #3\\ \\boldIn\\ #4}" ) ) ; //$NON-NLS-1$
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
    for ( LatexCommand command : this.expressions [ 1 ].getLatexCommands ( ) )
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
  public TreeSet < String > getLatexPackages ( )
  {
    TreeSet < String > packages = new TreeSet < String > ( ) ;
    packages.add ( "\\usepackage{ifthen}" ) ; //$NON-NLS-1$
    for ( String pack : this.identifiers [ 0 ].getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    if ( this.types [ 0 ] != null )
    {
      for ( String pack : this.types [ 0 ].getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    for ( String pack : this.expressions [ 0 ].getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( String pack : this.expressions [ 1 ].getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
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
   * @see Let#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_LET , LATEX_LET_REC ) ;
      this.latexStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.types [ 0 ] == null )
      {
        this.latexStringBuilder.addEmptyBuilder ( ) ;
      }
      else
      {
        this.latexStringBuilder
            .addBuilder ( this.types [ 0 ]
                .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
                PRIO_LET_TAU ) ;
      }
      this.latexStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.latexStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.latexStringBuilder ;
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
      this.prettyStringBuilder.addKeyword ( LET ) ;
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addKeyword ( REC ) ;
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( COLON ) ;
        this.prettyStringBuilder.addText ( SPACE ) ;
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addText ( EQUAL ) ;
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( IN ) ;
      this.prettyStringBuilder.addText ( SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
