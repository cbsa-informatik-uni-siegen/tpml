package de.unisiegen.tpml.core.expressions ;


import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
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
public final class Identifier extends Value
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
     * The set of message {@link Identifier}s.
     */
    MESSAGE ,
    /**
     * The set of self {@link Identifier}s.
     */
    SELF
  }


  /**
   * Method name for getIdentifiers
   */
  private static final String GET_IDENTIFIERS = "getIdentifiers" ; //$NON-NLS-1$


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
    this.parserStartOffset = - 1 ;
    this.parserEndOffset = - 1 ;
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
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Identifier clone ( )
  {
    return new Identifier ( this.name ) ;
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
   * Returns the array of {@link Identifier}s from the parent.
   * 
   * @param pInvokedFrom The parent.
   * @return The array of {@link Identifier}s from the parent.
   */
  private final Identifier [ ] getParentIdentifiers ( Object pInvokedFrom )
  {
    try
    {
      return ( Identifier [ ] ) pInvokedFrom.getClass ( ).getMethod (
          GET_IDENTIFIERS , new Class [ 0 ] ).invoke ( pInvokedFrom ,
          new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "Identifier: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "Identifier: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "Identifier: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "Identifier: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "Identifier: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
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
      if ( this.parent != null )
      {
        for ( Class < Object > currentInterface : this.parent.getClass ( )
            .getInterfaces ( ) )
        {
          if ( ( currentInterface
              .equals ( de.unisiegen.tpml.core.interfaces.DefaultIdentifiers.class ) )
              || ( currentInterface
                  .equals ( de.unisiegen.tpml.core.interfaces.BoundIdentifiers.class ) ) )
          {
            for ( Identifier id : getParentIdentifiers ( this.parent ) )
            {
              if ( id == this )
              {
                switch ( this.set )
                {
                  case VARIABLE :
                  {
                    this.prefix = PREFIX_ID ;
                    return this.prefix ;
                  }
                  case ATTRIBUTE :
                  {
                    this.prefix = PREFIX_ID_A ;
                    return this.prefix ;
                  }
                  case MESSAGE :
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
      this.prettyStringBuilder.addIdentifier ( this.name ) ;
    }
    return this.prettyStringBuilder ;
  }
}
