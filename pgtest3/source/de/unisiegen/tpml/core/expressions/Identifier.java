package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultName ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.util.Debug ;


/**
 * Represents an identifier in the expression hierarchy. Identifiers are values
 * in the semantics of the various languages.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1056 $
 * @see Value
 */
public final class Identifier extends Value implements DefaultName
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
   * The {@link Expression} in which this {@link Identifier} is bound.
   * 
   * @see #getBoundToExpression()
   * @see #setBoundTo(Expression,Identifier)
   */
  private Expression boundToExpression ;


  /**
   * The {@link Identifier} to which this {@link Identifier} is bound.
   * 
   * @see #getBoundToIdentifier()
   * @see #setBoundTo(Expression,Identifier)
   */
  private Identifier boundToIdentifier ;


  /**
   * The name of the {@link Identifier}.
   * 
   * @see #getName()
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
   */
  public Identifier ( String pName )
  {
    this.name = pName ;
    this.boundToExpression = null ;
    this.boundToIdentifier = null ;
    if ( pName.equals ( "self" ) ) //$NON-NLS-1$
    {
      this.set = Set.SELF ;
    }
    else
    {
      this.set = Set.VARIABLE ;
    }
  }


  /**
   * Allocates a new {@link Identifier} with the given <code>name</code>.
   * 
   * @param pName the name of the identifier.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Identifier ( String pName , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pName ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Allocates a new {@link Identifier} with the given <code>name</code>.
   * 
   * @param pName the name of the identifier.
   * @param pSet The set of this {@link Identifier}.
   */
  public Identifier ( String pName , Set pSet )
  {
    this ( pName ) ;
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
  public Identifier clone ( )
  {
    return new Identifier ( this.name , this.set ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object obj )
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
  public Expression getBoundToExpression ( )
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
  public Identifier getBoundToIdentifier ( )
  {
    return this.boundToIdentifier ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Identifier" ; //$NON-NLS-1$
  }


  /**
   * Returns a set that contains exacty one element, which is the name of the
   * identifier.
   * 
   * @return a set which contains the name of the identifier.
   * @see #getName()
   * @see Expression#getIdentifiersFree()
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.add ( this ) ;
    }
    return this.identifiersFree ;
  }


  /**
   * Returns the name of the identifier.
   * 
   * @return the name of the identifier.
   */
  public String getName ( )
  {
    return this.name ;
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
  public Set getSet ( )
  {
    return this.set ;
  }


  /**
   * Returns the set of this {@link Identifier} as a debug string.
   * 
   * @return The set of this {@link Identifier} as a debug string.
   * @see #set
   * @see #getSet()
   * @see #setSet(Set)
   */
  public String getSetDebug ( )
  {
    switch ( this.set )
    {
      case VARIABLE :
      {
        return "V" ; //$NON-NLS-1$
      }
      case ATTRIBUTE :
      {
        return "A" ; //$NON-NLS-1$
      }
      case METHOD :
      {
        return "M" ; //$NON-NLS-1$
      }
      case SELF :
      {
        return "S" ; //$NON-NLS-1$
      }
    }
    return "" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
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
  public void setBoundTo ( Expression pBoundToExpression ,
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
  public void setSet ( Set pSet )
  {
    this.set = pSet ;
  }


  /**
   * Returns <code>e</code> if <code>id</code> equals the name of the
   * identifier. Else the identifier itself is returned.
   * 
   * @return <code>e</code> if <code>id</code> equals the name of the
   *         identifier, else the identifier itself.
   * @see #getName()
   */
  @ Override
  public Expression substitute ( Identifier pId , Expression pExpression )
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
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = factory.newBuilder ( this , PRIO_IDENTIFIER ) ;
      if ( Debug.isUserName ( Debug.CHRISTIAN ) )
      {
        // this.prettyStringBuilder.addText ( "{" ) ;
      }
      this.prettyStringBuilder.addIdentifier ( this.name ) ;
      if ( Debug.isUserName ( Debug.CHRISTIAN ) )
      {
        // this.prettyStringBuilder.addText ( " | " + getSetDebug ( ) + "}" ) ;
      }
    }
    return this.prettyStringBuilder ;
  }
}
