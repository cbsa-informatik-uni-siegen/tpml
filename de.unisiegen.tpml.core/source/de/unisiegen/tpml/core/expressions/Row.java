package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.exceptions.RowSubstitutionException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.languages.MultipleIdentifier ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Instances of this class represent row expressions.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Row extends Expression implements DefaultExpressions
{
  /**
   * String for the case that the expressions are null.
   */
  private static final String EXPRESSIONS_NULL = "expressions is null" ; //$NON-NLS-1$


  /**
   * String for the case that one expression are null.
   */
  private static final String EXPRESSION_NULL = "one expression is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Row.class ) ;


  /**
   * Returns the union of the given first and second {@link Row}.
   * 
   * @param pR1 The first {@link Row}.
   * @param pR2 The second {@link Row}.
   * @return The union of the given first and second {@link Row}.
   */
  public static Row union ( Row pR1 , Row pR2 )
  {
    Expression [ ] newExpressions = new Expression [ pR1.getExpressions ( ).length
        + pR2.getExpressions ( ).length ] ;
    for ( int i = 0 ; i < pR1.getExpressions ( ).length ; i ++ )
    {
      newExpressions [ i ] = pR1.getExpressions ( ) [ i ] ;
    }
    for ( int i = 0 ; i < pR2.getExpressions ( ).length ; i ++ )
    {
      newExpressions [ pR1.getExpressions ( ).length + i ] = pR2
          .getExpressions ( ) [ i ] ;
    }
    Row row = new Row ( newExpressions ) ;
    MultipleIdentifier.check ( row ) ;
    return row ;
  }


  /**
   * Indeces of the child {@link Expression}s.
   */
  private int [ ] indicesE ;


  /**
   * The expressions.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new {@link Row}.
   * 
   * @param pExpressions The child {@link Expression}.
   */
  public Row ( Expression [ ] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( EXPRESSIONS_NULL ) ;
    }
    for ( Expression e : pExpressions )
    {
      if ( e == null )
      {
        throw new NullPointerException ( EXPRESSION_NULL ) ;
      }
    }
    this.expressions = pExpressions ;
    this.indicesE = new int [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      this.expressions [ i ].setParent ( this ) ;
      this.indicesE [ i ] = i + 1 ;
    }
    for ( Expression child : this.expressions )
    {
      if ( child instanceof Attribute )
      {
        ( ( Attribute ) child ).getIdentifiersBound ( ) ;
      }
    }
    checkDisjunction ( ) ;
  }


  /**
   * Allocates a new {@link Row}.
   * 
   * @param pExpressions The child {@link Expression}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Row ( Expression [ ] pExpressions , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pExpressions ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Checks the disjunction of the {@link Identifier} sets.
   */
  private void checkDisjunction ( )
  {
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    ArrayList < Identifier > allIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( this.expressions [ i ] instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) this.expressions [ i ] ;
        allIdentifiers.clear ( ) ;
        negativeIdentifiers.clear ( ) ;
        for ( int j = i + 1 ; j < this.expressions.length ; j ++ )
        {
          allIdentifiers.addAll ( this.expressions [ j ].getIdentifiersAll ( ) ) ;
        }
        for ( Identifier allId : allIdentifiers )
        {
          if ( ( attribute.getId ( ).equals ( allId ) )
              && ( ! ( Identifier.Set.ATTRIBUTE.equals ( allId.getSet ( ) ) ) ) )
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
            if ( attribute.getId ( ).equals ( allId ) )
            {
              negativeIdentifiers.add ( allId ) ;
            }
          }
          negativeIdentifiers.add ( attribute.getId ( ) ) ;
          LanguageParserMultiException
              .throwExceptionDisjunction ( negativeIdentifiers ) ;
        }
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row clone ( )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new Row ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Row )
    {
      Row other = ( Row ) pObject ;
      return Arrays.equals ( this.expressions , other.expressions ) ;
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
    return this.indicesE ;
  }


  /**
   * Returns a list of to the given {@link Attribute} bound {@link Identifier}s.
   * 
   * @param pAttribute The input {@link Attribute}.
   * @return A list of to the given {@link Attribute} bound {@link Identifier}s.
   */
  protected ArrayList < Identifier > getIdentifiersBound ( Attribute pAttribute )
  {
    ArrayList < Identifier > boundId = new ArrayList < Identifier > ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( pAttribute == this.expressions [ i ] )
      {
        Attribute attribute = ( Attribute ) this.expressions [ i ] ;
        for ( int j = i + 1 ; j < this.expressions.length ; j ++ )
        {
          Expression child = this.expressions [ j ] ;
          ArrayList < Identifier > freeIdentifiers = child
              .getIdentifiersFree ( ) ;
          for ( Identifier freeId : freeIdentifiers )
          {
            if ( attribute.getId ( ).equals ( freeId ) )
            {
              freeId.setBoundTo ( attribute , attribute.getId ( ) ) ;
              boundId.add ( freeId ) ;
            }
          }
        }
        return boundId ;
      }
    }
    return boundId ;
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
      ArrayList < Identifier > newBound = new ArrayList < Identifier > ( ) ;
      for ( Expression expr : this.expressions )
      {
        ArrayList < Identifier > freeCurrent = new ArrayList < Identifier > ( ) ;
        freeCurrent.addAll ( expr.getIdentifiersFree ( ) ) ;
        while ( freeCurrent.removeAll ( newBound ) )
        {
          // Remove all Identifiers with the same name
        }
        this.identifiersFree.addAll ( freeCurrent ) ;
        if ( expr instanceof Attribute )
        {
          Attribute attribute = ( Attribute ) expr ;
          newBound.add ( attribute.getId ( ) ) ;
        }
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
    commands.add ( new DefaultLatexCommand ( LATEX_ROW , 1 , "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}#1" , //$NON-NLS-1$
        "epsilon | val a = e; r1 | method m : τ = e ; r1" ) ) ; //$NON-NLS-1$
    return commands ;
  }


  /**
   * Returns the number of child {@link Attribute}s.
   * 
   * @return The number of child {@link Attribute}s.
   */
  public int getNumberOfAttributes ( )
  {
    int count = 0 ;
    for ( Expression expression : this.expressions )
    {
      if ( expression instanceof Attribute )
      {
        count ++ ;
      }
    }
    return count ;
  }


  /**
   * Returns the number of child {@link Method}s or {@link CurriedMethod}s.
   * 
   * @return The number of child {@link Method}s or {@link CurriedMethod}s.
   */
  public int getNumberOfDifferentMethods ( )
  {
    int count = 0 ;
    ArrayList < Identifier > list = new ArrayList < Identifier > ( ) ;
    for ( Expression expression : this.expressions )
    {
      if ( expression instanceof Method )
      {
        Method method = ( Method ) expression ;
        if ( ! list.contains ( method.getId ( ) ) )
        {
          list.add ( method.getId ( ) ) ;
          count ++ ;
        }
      }
      else if ( expression instanceof CurriedMethod )
      {
        CurriedMethod curriedMethod = ( CurriedMethod ) expression ;
        if ( ! list.contains ( curriedMethod.getIdentifiers ( ) [ 0 ] ) )
        {
          list.add ( curriedMethod.getIdentifiers ( ) [ 0 ] ) ;
          count ++ ;
        }
      }
    }
    return count ;
  }


  /**
   * Returns the number of child {@link Method}s or {@link CurriedMethod}s.
   * 
   * @return The number of child {@link Method}s or {@link CurriedMethod}s.
   */
  public int getNumberOfMethods ( )
  {
    int count = 0 ;
    for ( Expression expression : this.expressions )
    {
      if ( ( expression instanceof Method )
          || ( expression instanceof CurriedMethod ) )
      {
        count ++ ;
      }
    }
    return count ;
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
      if ( ( this.parent instanceof Class )
          || ( this.parent instanceof Inherit ) )
      {
        this.prefix = PREFIX_BODY ;
      }
      else
      {
        if ( this.isValue ( ) )
        {
          this.prefix = PREFIX_ROW_VALUE ;
        }
        else
        {
          this.prefix = PREFIX_ROW ;
        }
      }
    }
    return this.prefix ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    for ( Expression expr : this.expressions )
    {
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        /*
         * If an Attribute is not a value, this Row is not a value.
         */
        if ( ! attribute.isValue ( ) )
        {
          return false ;
        }
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ] ;
    }
    if ( this.getIdentifiersFree ( ).contains ( pId ) )
    {
      for ( int i = 0 ; i < newExpressions.length ; i ++ )
      {
        if ( newExpressions [ i ] instanceof Attribute )
        {
          Attribute attribute = ( Attribute ) newExpressions [ i ] ;
          if ( pId.equals ( attribute.getId ( ) ) )
          {
            newExpressions [ i ] = newExpressions [ i ].substitute ( pId ,
                pExpression ) ;
            break ;
          }
          BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
          for ( int j = i + 1 ; j < newExpressions.length ; j ++ )
          {
            boundRenaming.add ( newExpressions [ j ].getIdentifiersFree ( ) ) ;
          }
          boundRenaming.remove ( attribute.getId ( ) ) ;
          boundRenaming.add ( pExpression.getIdentifiersFree ( ) ) ;
          boundRenaming.add ( pId ) ;
          Identifier newId = boundRenaming.newIdentifier ( attribute.getId ( ) ) ;
          if ( ! attribute.getId ( ).equals ( newId ) )
          {
            for ( int j = i + 1 ; j < newExpressions.length ; j ++ )
            {
              newExpressions [ j ] = newExpressions [ j ].substitute (
                  attribute.getId ( ) , newId ) ;
            }
          }
          newExpressions [ i ] = new Attribute ( newId , attribute.getE ( )
              .substitute ( pId , pExpression ) ) ;
        }
        else
        {
          newExpressions [ i ] = newExpressions [ i ].substitute ( pId ,
              pExpression ) ;
        }
      }
    }
    return new Row ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Row substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] newExpr = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpr [ i ] = this.expressions [ i ].substitute ( pTypeSubstitution ) ;
    }
    return new Row ( newExpr ) ;
  }


  /**
   * Performs the row substitution.
   * 
   * @param pId The {@link Identifier}.
   * @param pExpression The {@link Expression} to substitute.
   * @return The new {@link Row}.
   */
  public Row substituteRow ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    boolean found = false ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      if ( newExpressions [ i ] instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) newExpressions [ i ] ;
        if ( pId.equals ( attribute.getId ( ) ) )
        {
          newExpressions [ i ] = new Attribute ( attribute.getId ( ) ,
              pExpression ) ;
          found = true ;
          break ;
        }
      }
    }
    if ( ! found )
    {
      throw new RowSubstitutionException ( ) ;
    }
    return new Row ( newExpressions ) ;
  }


  /**
   * Returns the tail <code>Row</code>, the current <code>Row</code>
   * without the first {@link Expression}.
   * 
   * @return The tail <code>Row</code>.
   */
  public Row tailRow ( )
  {
    Expression [ ] newRowExpressions = new Expression [ this.expressions.length - 1 ] ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      newRowExpressions [ i ] = this.expressions [ i + 1 ] ;
    }
    return new Row ( newRowExpressions ) ;
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
    StringBuilder body = new StringBuilder ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      body.append ( this.expressions [ i ].toPrettyString ( ).toString ( ) ) ;
      if ( i != this.expressions.length - 1 )
      {
        body.append ( PRETTY_SPACE ) ;
      }
    }
    if ( this.expressions.length == 0 )
    {
      body.append ( PRETTY_EPSILON ) ;
    }
    String descriptions[] = new String [ 2 + this.expressions.length ] ;
    descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
    descriptions [ 1 ] = body.toString ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      descriptions [ 2 + i ] = this.expressions [ i ].toPrettyString ( )
          .toString ( ) ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_ROW , LATEX_ROW , pIndent , descriptions ) ;
    builder.addBuilderBegin ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addBuilder ( this.expressions [ i ].toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) ,
          PRIO_ROW_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT )
            + LATEX_SPACE ) ;
        builder.addBreak ( ) ;
      }
    }
    if ( this.expressions.length == 0 )
    {
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
      builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
          + LATEX_INDENT ) ) ;
      builder.addText ( LATEX_EPSILON ) ;
    }
    builder.addBuilderEnd ( ) ;
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
          PRIO_ROW ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.prettyStringBuilder
            .addBuilder ( this.expressions [ i ]
                .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
                PRIO_ROW_E ) ;
        if ( i != this.expressions.length - 1 )
        {
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
          this.prettyStringBuilder.addBreak ( ) ;
        }
      }
      if ( this.expressions.length == 0 )
      {
        this.prettyStringBuilder.addText ( PRETTY_EPSILON ) ;
      }
    }
    return this.prettyStringBuilder ;
  }
}
