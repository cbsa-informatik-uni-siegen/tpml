package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Instances of this class represent curried let rec expressions in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see LetRec
 * @see CurriedLet
 */
public final class CurriedLetRec extends CurriedLet implements
    BoundIdentifiers , DefaultTypes , DefaultExpressions
{
  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( CurriedLetRec.class ) ;


  /**
   * Allocates a new <code>CurriedLetRec</code> instance.
   * 
   * @param pIdentifiers an array with atleast two identifiers, where the first
   *          identifier is the name to use for the function and the remaining
   *          identifiers specify the parameters for the function.
   * @param pTypes the types for the <code>identifiers</code>, see
   *          {@link CurriedLet#getTypes()} for an extensive description.
   * @param pExpression1 the function body.
   * @param pExpression2 the second expression.
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *           contains less than two identifiers, or the arity of
   *           <code>identifiers</code> and <code>types</code> does not
   *           match.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>types</code>, <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   * @see CurriedLet#CurriedLet(Identifier[], MonoType[], Expression,
   *      Expression)
   */
  public CurriedLetRec ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes ,
      Expression pExpression1 , Expression pExpression2 )
  {
    super ( pIdentifiers , pTypes , pExpression1 , pExpression2 ) ;
  }


  /**
   * Allocates a new <code>CurriedLetRec</code> instance.
   * 
   * @param pIdentifiers an array with atleast two identifiers, where the first
   *          identifier is the name to use for the function and the remaining
   *          identifiers specify the parameters for the function.
   * @param pTypes the types for the <code>identifiers</code>, see
   *          {@link CurriedLet#getTypes()} for an extensive description.
   * @param pExpression1 the function body.
   * @param pExpression2 the second expression.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *           contains less than two identifiers, or the arity of
   *           <code>identifiers</code> and <code>types</code> does not
   *           match.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>types</code>, <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   * @see CurriedLet#CurriedLet(Identifier[], MonoType[], Expression,
   *      Expression)
   */
  public CurriedLetRec ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes ,
      Expression pExpression1 , Expression pExpression2 ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifiers , pTypes , pExpression1 , pExpression2 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  @ Override
  protected void checkDisjunction ( )
  {
    // Identifier 0
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
    // Identifier 1-n
    allIdentifiers = this.expressions [ 0 ].getIdentifiersAll ( ) ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      negativeIdentifiers.clear ( ) ;
      for ( Identifier allId : allIdentifiers )
      {
        if ( ( this.identifiers [ i ].equals ( allId ) )
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
          if ( this.identifiers [ i ].equals ( allId ) )
          {
            negativeIdentifiers.add ( allId ) ;
          }
        }
        negativeIdentifiers.add ( this.identifiers [ i ] ) ;
        LanguageParserMultiException
            .throwExceptionDisjunction ( negativeIdentifiers ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#clone()
   */
  @ Override
  public CurriedLetRec clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .clone ( ) ;
    }
    return new CurriedLetRec ( newIdentifiers , newTypes ,
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
   * Returns a list of lists of in this {@link Expression} bound
   * {@link Identifier}s.
   * 
   * @return A list of lists of in this {@link Expression} bound
   *         {@link Identifier}s.
   */
  @ Override
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ( )
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> (
          this.identifiers.length ) ;
      ArrayList < Identifier > boundE1 = this.expressions [ 0 ]
          .getIdentifiersFree ( ) ;
      ArrayList < Identifier > boundE2 = this.expressions [ 1 ]
          .getIdentifiersFree ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        if ( i == 0 )
        {
          /*
           * Check, if the bindings should be searched in E1 and E2 or only in
           * E2. Example: let x x = x in x.
           */
          boolean searchInE1 = true ;
          for ( int j = 1 ; j < this.identifiers.length ; j ++ )
          {
            if ( this.identifiers [ 0 ].equals ( this.identifiers [ j ] ) )
            {
              searchInE1 = false ;
              break ;
            }
          }
          ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
          if ( searchInE1 )
          {
            for ( Identifier freeId : boundE1 )
            {
              if ( this.identifiers [ 0 ].equals ( freeId ) )
              {
                freeId.setBoundTo ( this , this.identifiers [ 0 ] ) ;
                boundIdList.add ( freeId ) ;
              }
            }
          }
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
        else
        {
          /*
           * An Identifier has no binding, if an Identifier after him has the
           * same name. Example: let rec f x x = x + 1 in f 1 2.
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
            for ( Identifier freeId : boundE1 )
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
    }
    return this.boundIdentifiers ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#getIdentifiersFree()
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > freeE1 = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > freeE2 = new ArrayList < Identifier > ( ) ;
      freeE1.addAll ( this.expressions [ 0 ].getIdentifiersFree ( ) ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        while ( freeE1.remove ( this.identifiers [ i ] ) )
        {
          // Remove all Identifiers with the same name
        }
      }
      freeE2.addAll ( this.expressions [ 1 ].getIdentifiersFree ( ) ) ;
      while ( freeE2.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
      this.identifiersFree.addAll ( freeE1 ) ;
      this.identifiersFree.addAll ( freeE2 ) ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_LET , 0 ,
        "\\textbf{let}" ) ) ; //$NON-NLS-1$ 
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_REC , 0 ,
        "\\textbf{rec}" ) ) ; //$NON-NLS-1$ 
    commands
        .add ( new DefaultLatexCommand ( LATEX_KEY_IN , 0 , "\\textbf{in}" ) ) ; //$NON-NLS-1$ 
    commands.add ( new DefaultLatexCommand ( LATEX_CURRIED_LET_REC , 4 ,
        "\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND + "{\\" //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_KEY_LET + "\\ \\" + LATEX_KEY_REC + "\\ #1\\ =\\ #3\\ \\" //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_KEY_IN + "\\ #4}" + LATEX_LINE_BREAK_NEW_COMMAND + "{\\" //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_KEY_LET + "\\ \\" + LATEX_KEY_REC //$NON-NLS-1$
            + "\\ #1\\colon\\ #2\\ =\\ #3\\ \\" + LATEX_KEY_IN + "\\ #4}" , //$NON-NLS-1$ //$NON-NLS-2$
        "id (id1: tau1) ... (idn: taun)" , "tau" , "e1" , "e2" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    return commands ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#substitute(Identifier, Expression)
   */
  @ Override
  public CurriedLetRec substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    if ( this.identifiers [ 0 ].equals ( pId ) )
    {
      return this ;
    }
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ] ;
    }
    Expression newE1 = this.expressions [ 0 ] ;
    Expression newE2 = this.expressions [ 1 ] ;
    boolean sameIdAs0 = false ;
    boolean substInE1 = true ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        substInE1 = false ;
        break ;
      }
    }
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( this.identifiers [ 0 ] ) )
      {
        sameIdAs0 = true ;
        break ;
      }
    }
    if ( substInE1 )
    {
      for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
      {
        BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
        boundRenaming.add ( this.expressions [ 0 ].getIdentifiersFree ( ) ) ;
        boundRenaming.remove ( newIdentifiers [ i ] ) ;
        boundRenaming.add ( pExpression.getIdentifiersFree ( ) ) ;
        boundRenaming.add ( pId ) ;
        /*
         * The new Identifier should not be equal to an other Identifier.
         */
        if ( boundRenaming.contains ( newIdentifiers [ i ] ) )
        {
          for ( int j = 1 ; j < newIdentifiers.length ; j ++ )
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
         * name. For example: "let a = b in let rec f b b = b a in f".
         */
        for ( int j = 1 ; j < i ; j ++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            newId = newIdentifiers [ j ] ;
          }
        }
        /*
         * Substitute the old Identifier only with the new Identifier, if they
         * are different.
         */
        if ( ! newIdentifiers [ i ].equals ( newId ) )
        {
          newE1 = newE1.substitute ( newIdentifiers [ i ] , newId ) ;
          newIdentifiers [ i ] = newId ;
        }
      }
    }
    BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
    boundRenaming.add ( this.getIdentifiersFree ( ) ) ;
    boundRenaming.add ( pExpression.getIdentifiersFree ( ) ) ;
    boundRenaming.add ( pId ) ;
    if ( ! sameIdAs0 )
    {
      for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
      {
        boundRenaming.add ( this.identifiers [ i ] ) ;
      }
    }
    Identifier newId = boundRenaming.newIdentifier ( this.identifiers [ 0 ] ) ;
    if ( ! this.identifiers [ 0 ].equals ( newId ) )
    {
      if ( ! sameIdAs0 )
      {
        newE1 = newE1.substitute ( this.identifiers [ 0 ] , newId ) ;
        newIdentifiers [ 0 ] = newId ;
      }
      newE2 = newE2.substitute ( this.identifiers [ 0 ] , newId ) ;
      newIdentifiers [ 0 ] = newId ;
    }
    if ( ( ! sameIdAs0 ) || ( substInE1 ) )
    {
      newE1 = newE1.substitute ( pId , pExpression ) ;
    }
    /*
     * Perform the substitution in e2.
     */
    newE2 = newE2.substitute ( pId , pExpression ) ;
    return new CurriedLetRec ( newIdentifiers , this.types , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#substitute(TypeSubstitution)
   */
  @ Override
  public CurriedLetRec substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    return new CurriedLetRec ( this.identifiers , newTypes ,
        this.expressions [ 0 ].substitute ( pTypeSubstitution ) ,
        this.expressions [ 1 ].substitute ( pTypeSubstitution ) ) ;
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
    if ( this.latexStringBuilder == null )
    {
      StringBuilder identifier = new StringBuilder ( ) ;
      identifier
          .append ( this.identifiers [ 0 ].toPrettyString ( ).toString ( ) ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        identifier.append ( PRETTY_SPACE ) ;
        if ( this.types [ i ] != null )
        {
          identifier.append ( PRETTY_LPAREN ) ;
        }
        identifier.append ( this.identifiers [ i ].toPrettyString ( )
            .toString ( ) ) ;
        if ( this.types [ i ] != null )
        {
          identifier.append ( PRETTY_COLON ) ;
          identifier.append ( PRETTY_SPACE ) ;
          identifier.append ( this.types [ i ].toPrettyString ( ).toString ( ) ) ;
          identifier.append ( PRETTY_RPAREN ) ;
        }
      }
      String descriptions[] = new String [ 2 + this.identifiers.length
          + this.types.length + this.expressions.length ] ;
      descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
      descriptions [ 1 ] = identifier.toString ( ) ;
      descriptions [ 2 ] = this.identifiers [ 0 ].toString ( ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        descriptions [ 1 + i * 2 ] = this.identifiers [ i ].toPrettyString ( )
            .toString ( ) ;
        descriptions [ 2 + i * 2 ] = this.types [ i ] == null ? LATEX_EMPTY_STRING
            : this.types [ i ].toPrettyString ( ).toString ( ) ;
      }
      descriptions [ descriptions.length - 3 ] = this.types [ 0 ] == null ? LATEX_EMPTY_STRING
          : this.types [ 0 ].toPrettyString ( ).toString ( ) ;
      descriptions [ descriptions.length - 2 ] = this.expressions [ 0 ]
          .toPrettyString ( ).toString ( ) ;
      descriptions [ descriptions.length - 1 ] = this.expressions [ 1 ]
          .toPrettyString ( ).toString ( ) ;
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder (
          PRIO_LET , LATEX_CURRIED_LET_REC , pIndent , descriptions ) ;
      this.latexStringBuilder.addBuilderBegin ( ) ;
      this.latexStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
              + LATEX_INDENT * 2 ) , PRIO_ID ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        this.latexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT )
            + LATEX_SPACE ) ;
        if ( this.types [ i ] != null )
        {
          this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
          this.latexStringBuilder.addText ( DefaultLatexStringBuilder
              .getIndent ( pIndent + LATEX_INDENT )
              + LATEX_LPAREN ) ;
        }
        this.latexStringBuilder.addBuilder ( this.identifiers [ i ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
                + LATEX_INDENT * 2 ) , PRIO_ID ) ;
        if ( this.types [ i ] != null )
        {
          this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
          this.latexStringBuilder.addText ( DefaultLatexStringBuilder
              .getIndent ( pIndent + LATEX_INDENT )
              + LATEX_COLON ) ;
          this.latexStringBuilder.addText ( LATEX_SPACE ) ;
          this.latexStringBuilder.addBuilder ( this.types [ i ]
              .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
                  + LATEX_INDENT * 2 ) , PRIO_LET_TAU ) ;
          this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
          this.latexStringBuilder.addText ( DefaultLatexStringBuilder
              .getIndent ( pIndent + LATEX_INDENT )
              + LATEX_RPAREN ) ;
        }
      }
      this.latexStringBuilder.addBuilderEnd ( ) ;
      if ( this.types [ 0 ] == null )
      {
        this.latexStringBuilder.addEmptyBuilder ( ) ;
      }
      else
      {
        this.latexStringBuilder.addBuilder ( this.types [ 0 ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
                + LATEX_INDENT ) , PRIO_LET_TAU ) ;
      }
      this.latexStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
              + LATEX_INDENT ) , PRIO_LET_E1 ) ;
      this.latexStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
              + LATEX_INDENT ) , PRIO_LET_E2 ) ;
    }
    return this.latexStringBuilder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#toPrettyStringBuilder(PrettyStringBuilderFactory)
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
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( PRETTY_LPAREN ) ;
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( PRETTY_COLON ) ;
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
          this.prettyStringBuilder.addBuilder ( this.types [ i ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_LET_TAU ) ;
          this.prettyStringBuilder.addText ( PRETTY_RPAREN ) ;
        }
      }
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
      this.prettyStringBuilder.addBreak ( ) ;
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
