package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.IdentifierOrTypeName ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Represents an identifier in the expression hierarchy. Identifiers are values
 * in the semantics of the various languages.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1056 $
 * @see Value
 */
public final class Identifier extends Value implements IdentifierOrTypeName
{
  /**
   * The set of {@link Identifier}s.
   * 
   * @author Christian Fehler
   */
  public enum Set
  {
    /**
     * The set of variable {@link Identifier}s.
     */
    VARIABLE ,
    /**
     * The set of attribute {@link Identifier}s.
     */
    ATTRIBUTE ,
    /**
     * The set of method {@link Identifier}s.
     */
    METHOD ,
    /**
     * The set of self {@link Identifier}s.
     */
    SELF
  }


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( Identifier.class ) ;


  /**
   * String for the case that the name is null.
   */
  private static final String NAME_NULL = "name is null" ; //$NON-NLS-1$


  /**
   * String for the case that the set is null.
   */
  private static final String SET_NULL = "set is null" ; //$NON-NLS-1$


  /**
   * The {@link Expression} in which this {@link Identifier} is bound.
   * 
   * @see #getBoundToExpression()
   * @see #setBoundTo(Expression,Identifier)
   */
  private Expression boundToExpression = null ;


  /**
   * The {@link Identifier} to which this {@link Identifier} is bound.
   * 
   * @see #getBoundToIdentifier()
   * @see #setBoundTo(Expression,Identifier)
   */
  private Identifier boundToIdentifier = null ;


  /**
   * The name of the {@link Identifier}.
   */
  private String name ;


  /**
   * The set of this {@link Identifier}.
   * 
   * @see #getSet()
   * @see #setSet(Set)
   */
  private Set set ;


  /**
   * Allocates a new {@link Identifier} with the given <code>name</code>.
   * 
   * @param pName the name of the identifier.
   * @param pSet The set of this {@link Identifier}.
   */
  public Identifier ( String pName , Set pSet )
  {
    if ( pName == null )
    {
      throw new NullPointerException ( NAME_NULL ) ;
    }
    if ( pSet == null )
    {
      throw new NullPointerException ( SET_NULL ) ;
    }
    this.name = pName ;
    this.set = pSet ;
  }


  /**
   * Allocates a new {@link Identifier} with the given <code>name</code>.
   * 
   * @param pName the name of the identifier.
   * @param pSet The set of this {@link Identifier}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Identifier ( String pName , Set pSet , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pName , pSet ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public final Identifier clone ( )
  {
    return new Identifier ( this.name , this.set ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public final boolean equals ( Object obj )
  {
    if ( obj instanceof Identifier )
    {
      Identifier other = ( Identifier ) obj ;
      return this.name.equals ( other.name ) ;
    }
    return false ;
  }


  /**
   * Returns the {@link Expression} in which this {@link Identifier} is bound.
   * 
   * @return The {@link Expression} in which this {@link Identifier} is bound.
   * @see #boundToExpression
   * @see #setBoundTo(Expression,Identifier)
   */
  public final Expression getBoundToExpression ( )
  {
    return this.boundToExpression ;
  }


  /**
   * Returns the {@link Identifier} to which this {@link Identifier} is bound.
   * 
   * @return The {@link Identifier} to which this {@link Identifier} is bound.
   * @see #boundToIdentifier
   * @see #setBoundTo(Expression,Identifier)
   */
  public final Identifier getBoundToIdentifier ( )
  {
    return this.boundToIdentifier ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public final String getCaption ( )
  {
    return CAPTION ;
  }


  /**
   * Returns a set that contains exacty one element, which is the name of the
   * identifier.
   * 
   * @return a set which contains the name of the identifier.
   * @see Expression#getIdentifiersFree()
   */
  @ Override
  public final ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.add ( this ) ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_IDENTIFIER , 1 ,
        "\\textit{#1}" , "name" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    return commands ;
  }


  /**
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  @ Override
  public final String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      if ( ( this.parent != null )
          && ( this.parent instanceof DefaultIdentifiers ) )
      {
        Identifier [ ] identifiers = ( ( DefaultIdentifiers ) this.parent )
            .getIdentifiers ( ) ;
        for ( Identifier id : identifiers )
        {
          if ( id == this )
          {
            switch ( this.set )
            {
              case VARIABLE :
              {
                this.prefix = PREFIX_ID_V ;
                return this.prefix ;
              }
              case ATTRIBUTE :
              {
                this.prefix = PREFIX_ID_A ;
                return this.prefix ;
              }
              case METHOD :
              {
                this.prefix = PREFIX_ID_M ;
                return this.prefix ;
              }
              case SELF :
              {
                this.prefix = PREFIX_ID_S ;
                return this.prefix ;
              }
            }
          }
        }
      }
      this.prefix = PREFIX_VALUE ;
    }
    return this.prefix ;
  }


  /**
   * Returns the set of this {@link Identifier}.
   * 
   * @return The set of this {@link Identifier}.
   * @see #set
   * @see #setSet(Set)
   */
  public final Set getSet ( )
  {
    return this.set ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public final int hashCode ( )
  {
    return this.name.hashCode ( ) ;
  }


  /**
   * Sets the {@link Identifier} to which this {@link Identifier} is bound and
   * the {@link Expression} in which this {@link Identifier} is bound.
   * 
   * @param pBoundToExpression The {@link Expression} in which this
   *          {@link Identifier} is bound.
   * @param pBoundToIdentifier The {@link Identifier} to which this
   *          {@link Identifier} is bound.
   * @see #boundToIdentifier
   * @see #getBoundToIdentifier()
   */
  public final void setBoundTo ( Expression pBoundToExpression ,
      Identifier pBoundToIdentifier )
  {
    if ( ( this.boundToIdentifier != null )
        && ( this.boundToIdentifier != pBoundToIdentifier ) )
    {
      System.err
          .println ( "An Identifier can not be bound to more than one Expression!" ) ; //$NON-NLS-1$
      System.err.println ( "Identifier: " + this ) ; //$NON-NLS-1$
      System.err.println ( "Old boundToExpression: " + this.boundToExpression ) ; //$NON-NLS-1$
      System.err.println ( "New boundToExpression: " + pBoundToExpression ) ; //$NON-NLS-1$
      System.err.println ( "Old boundToIdentifier: " + this.boundToIdentifier ) ; //$NON-NLS-1$
      System.err.println ( "New boundToIdentifier: " + pBoundToIdentifier ) ; //$NON-NLS-1$
    }
    this.boundToExpression = pBoundToExpression ;
    this.boundToIdentifier = pBoundToIdentifier ;
  }


  /**
   * Sets the set of this {@link Identifier}.
   * 
   * @param pSet The set to set.
   * @see #set
   * @see #getSet()
   */
  public final void setSet ( Set pSet )
  {
    this.set = pSet ;
  }


  /**
   * Returns <code>e</code> if <code>id</code> equals the name of the
   * identifier. Else the identifier itself is returned.
   * 
   * @return <code>e</code> if <code>id</code> equals the name of the
   *         identifier, else the identifier itself.
   */
  @ Override
  public final Expression substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    if ( pId.equals ( this ) )
    {
      /*
       * We need to clone the expression here to make sure we can distinguish an
       * expression in the pretty printer that is substituted multiple times
       */
      return pExpression.clone ( ) ;
    }
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_IDENTIFIER , LATEX_IDENTIFIER , pIndent ) ;
      this.latexStringBuilder.addText ( "{" //$NON-NLS-1$
          + this.name.replaceAll ( "_" , "\\\\_" ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }
    return this.latexStringBuilder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public final PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_IDENTIFIER ) ;
      this.prettyStringBuilder.addIdentifier ( this.name ) ;
    }
    return this.prettyStringBuilder ;
  }
}
