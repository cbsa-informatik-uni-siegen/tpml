package de.unisiegen.tpml.core.expressions ;


import java.lang.reflect.InvocationTargetException ;
import java.text.DecimalFormat ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
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
public final class Identifier extends Value
{
  /**
   * TODO
   * 
   * @author Christian Fehler
   */
  public enum Set
  {
    /**
     * TODO
     */
    VARIABLE ,
    /**
     * TODO
     */
    ATTRIBUTE ,
    /**
     * TODO
     */
    MESSAGE ,
    /**
     * TODO
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
   * TODO Only for debugging
   */
  // private static int debugId = 0 ;
  /**
   * The name of the {@link Identifier}.
   * 
   * @see #getName()
   */
  private String name ;


  /**
   * TODO
   */
  @ SuppressWarnings ( "unused" )
  private String identity = new DecimalFormat ( "000" ).format ( ( int ) ( Math //$NON-NLS-1$
      .random ( ) * 1000 ) ) ;


  /**
   * TODO
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
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
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
   * TODO
   * 
   * @param pInvokedFrom TODO
   * @return TODO
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
   * TODO
   * 
   * @return TODO
   */
  @ Override
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      Identifier [ ] identifiers = null ;
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
            identifiers = getParentIdentifiers ( this.parent ) ;
            boolean found = false ;
            for ( Identifier id : identifiers )
            {
              if ( id == this )
              {
                found = true ;
                break ;
              }
            }
            if ( found )
            {
              break ;
            }
            this.prefix = super.getPrefix ( ) ;
            return this.prefix ;
          }
        }
      }
      switch ( this.set )
      {
        case VARIABLE :
        {
          this.prefix = PREFIX_ID ;
          break ;
        }
        case ATTRIBUTE :
        {
          this.prefix = PREFIX_ID_A ;
          break ;
        }
        case MESSAGE :
        {
          this.prefix = PREFIX_ID_M ;
          break ;
        }
        case SELF :
        {
          this.prefix = PREFIX_ID_S ;
          break ;
        }
      }
    }
    return this.prefix ;
  }


  /**
   * Returns the set.
   * 
   * @return The set.
   * @see #set
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
      Debug.err
          .println (
              "An Identifier can not be bound to more than one Expression!" , Debug.CHRISTIAN ) ; //$NON-NLS-1$
      Debug.err.println ( "Identifier: " + this , Debug.CHRISTIAN ) ; //$NON-NLS-1$
      Debug.err.println ( "Old boundToExpression: " //$NON-NLS-1$
          + this.boundToExpression , Debug.CHRISTIAN ) ;
      Debug.err.println (
          "New boundToExpression: " + pBoundToExpression , Debug.CHRISTIAN ) ; //$NON-NLS-1$
      Debug.err.println ( "Old boundToIdentifier: " //$NON-NLS-1$
          + this.boundToIdentifier , Debug.CHRISTIAN ) ;
      Debug.err.println (
          "New boundToIdentifier: " + pBoundToIdentifier , Debug.CHRISTIAN ) ; //$NON-NLS-1$
    }
    this.boundToExpression = pBoundToExpression ;
    this.boundToIdentifier = pBoundToIdentifier ;
  }


  /**
   * TODO
   * 
   * @param pSet The set to set
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
      /*
       * if ( Debug.isUserName ( Debug.CHRISTIAN ) ) {
       * this.prettyStringBuilder.addText ( "{" ) ; }
       */
      this.prettyStringBuilder.addIdentifier ( this.name ) ;
      /*
       * if ( Debug.isUserName ( Debug.CHRISTIAN ) ) {
       * this.prettyStringBuilder.addText ( "|" + new DecimalFormat ( "00"
       * ).format ( debugId ++ ) + "|" + ( this.set == Set.VARIABLE ? "V" :
       * this.set == Set.ATTRIBUTE ? "A" : this.set == Set.MESSAGE ? "M" :
       * this.set == Set.SELF ? "S" : "NOTHING" ) + "}" ) ; }
       */
    }
    return this.prettyStringBuilder ;
  }
}
