package de.unisiegen.tpml.core.types ;


import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interfaces.DefaultName ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Represents an type name in the type hierarchy.
 * 
 * @author Christian Fehler
 * @version $Rev:1056 $
 */
public final class TypeName extends MonoType implements DefaultName
{
  /**
   * The name of the {@link TypeName}.
   * 
   * @see #getName()
   */
  private String name ;


  /**
   * Allocates a new {@link TypeName} with the given <code>name</code>.
   * 
   * @param pName the name of the {@link TypeName}.
   */
  public TypeName ( String pName )
  {
    this.name = pName ;
  }


  /**
   * Allocates a new {@link TypeName} with the given <code>name</code>.
   * 
   * @param pName The name of the type name.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public TypeName ( String pName , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pName ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public TypeName clone ( )
  {
    return new TypeName ( this.name ) ;
  }


  /**
   * Method name for getTypeNames
   */
  private static final String GET_TYPE_NAMES = "getTypeNames" ; //$NON-NLS-1$


  /**
   * Returns the array of {@link TypeName}s from the parent.
   * 
   * @param pInvokedFrom The parent.
   * @return The array of {@link TypeName}s from the parent.
   */
  private final TypeName [ ] getParentTypeNames ( Object pInvokedFrom )
  {
    try
    {
      return ( TypeName [ ] ) pInvokedFrom.getClass ( ).getMethod (
          GET_TYPE_NAMES , new Class [ 0 ] ).invoke ( pInvokedFrom ,
          new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "TypeName: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "TypeName: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "TypeName: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "TypeName: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "TypeName: NoSuchMethodException" ) ; //$NON-NLS-1$
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
          if ( currentInterface
              .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypeNames.class ) )
          {
            for ( TypeName typeName : getParentTypeNames ( this.parent ) )
            {
              if ( typeName == this )
              {
                this.prefix = PREFIX_TYPE_NAME ;
                return this.prefix ;
              }
            }
          }
        }
      }
      this.prefix = PREFIX_TAU ;
    }
    return this.prefix ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#equals(Object)
   */
  @ Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof TypeName )
    {
      TypeName other = ( TypeName ) obj ;
      return this.name.equals ( other.name ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Type-Name" ; //$NON-NLS-1$
  }


  /**
   * Returns the name of the {@link TypeName}.
   * 
   * @return the name of the {@link TypeName}.
   */
  public String getName ( )
  {
    return this.name ;
  }


  /**
   * Returns a list of the free {@link TypeName}s in this {@link Type}.
   * 
   * @return A list of the free {@link TypeName}s in this {@link Type}.
   */
  @ Override
  public ArrayList < TypeName > getTypeNamesFree ( )
  {
    if ( this.typeNamesFree == null )
    {
      this.typeNamesFree = new ArrayList < TypeName > ( ) ;
      this.typeNamesFree.add ( this ) ;
    }
    return this.typeNamesFree ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) ;
  }


  /**
   * Substitutes the type <code>pTau</code> for the {@link TypeName}
   * <code>pTypeName</code> in this type, and returns the resulting type. The
   * resulting type may be a new <code>Type</code> object or if no
   * substitution took place, the same object. The method operates recursively.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link MonoType}.
   * @return The resulting {@link Type}.
   */
  @ Override
  public MonoType substitute ( TypeName pTypeName , MonoType pTau )
  {
    if ( pTypeName.equals ( this ) )
    {
      /*
       * We need to clone the type here to make sure we can distinguish an type
       * in the pretty printer that is substituted multiple times
       */
      return pTau.clone ( ) ;
    }
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public TypeName substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( "Substitution is null" ) ; //$NON-NLS-1$
    }
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = factory.newBuilder ( this , PRIO_TYPE_NAME ) ;
      this.prettyStringBuilder.addIdentifier ( this.name ) ;
    }
    return this.prettyStringBuilder ;
  }
}
