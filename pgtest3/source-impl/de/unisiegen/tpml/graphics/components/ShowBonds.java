package de.unisiegen.tpml.graphics.components ;


import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers;
import de.unisiegen.tpml.core.interfaces.BoundTypeNames;
import de.unisiegen.tpml.core.interfaces.DefaultTypes;
import de.unisiegen.tpml.core.interfaces.ShowBondsInput;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference;
import de.unisiegen.tpml.core.typeinference.TypeSubType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeName;


/**
 * Calculates the bound {@link Identifier}s.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public final class ShowBonds
{
  /**
   * The loaded {@link ShowBondsInput}.
   */
  private ShowBondsInput loaded = null ;


  /**
   * List of all Bonds in loaded {link Expression}.
   */
  private ArrayList < Bonds > result = null ;


  /**
   * Checks the given {@link Expression} for bound {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unchecked" )
  private final void check ( Expression pExpression )
  {
    if ( pExpression instanceof BoundIdentifiers )
    {
      Identifier [ ] id = ( ( BoundIdentifiers ) pExpression )
          .getIdentifiers ( ) ;
      ArrayList < ArrayList < Identifier >> bound = ( ( BoundIdentifiers ) pExpression )
          .getIdentifiersBound ( ) ;
      // Create Bonds
      if ( bound == null )
      {
        return ;
      }
      PrettyAnnotation current ;
      for ( int i = 0 ; i < bound.size ( ) ; i ++ )
      {
        if ( bound.get ( i ) == null )
        {
          continue ;
        }
        current = this.loaded.toPrettyString ( ).getAnnotationForPrintable (
            id [ i ] ) ;
        Bonds bonds = new Bonds ( current.getStartOffset ( ) , current
            .getEndOffset ( ) ) ;
        for ( Identifier boundId : bound.get ( i ) )
        {
          try
          {
            bonds.addPrettyAnnotation ( this.loaded.toPrettyString ( )
                .getAnnotationForPrintable ( boundId ) ) ;
          }
          catch ( IllegalArgumentException e )
          {
            /*
             * Happens if a bound Identifier is not in the PrettyString. For
             * example "object (self) val a = 0 ; method move = {< a = 2 >} ;
             * end". The "self" binds the free Identifier "self" in the
             * Duplication (method free in Duplication), but the free "self" in
             * the Duplication is not present in the PrettyString.
             */
          }
        }
        this.result.add ( bonds ) ;
      }
    }
    if ( pExpression instanceof DefaultTypes )
    {
      MonoType [ ] types = ( ( DefaultTypes ) pExpression ).getTypes ( ) ;
      for ( MonoType tau : types )
      {
        if ( tau != null )
        {
          check ( tau ) ;
        }
      }
    }
    for ( Expression expr : pExpression.children ( ) )
    {
      check ( expr ) ;
    }
  }


  /**
   * Checks the given {@link Type} for bound {@link TypeName}s.
   * 
   * @param pType The input {@link Type}.
   */
  @ SuppressWarnings ( "unchecked" )
  private final void check ( Type pType )
  {
    if ( pType instanceof BoundTypeNames )
    {
      TypeName [ ] typeNames = ( ( BoundTypeNames ) pType ).getTypeNames ( ) ;
      ArrayList < ArrayList < TypeName >> bound = ( ( BoundTypeNames ) pType )
          .getTypeNamesBound ( ) ;
      // Create Bonds
      if ( bound == null )
      {
        return ;
      }
      PrettyAnnotation current ;
      for ( int i = 0 ; i < bound.size ( ) ; i ++ )
      {
        if ( bound.get ( i ) == null )
        {
          continue ;
        }
        current = this.loaded.toPrettyString ( ).getAnnotationForPrintable (
            typeNames [ i ] ) ;
        Bonds bonds = new Bonds ( current.getStartOffset ( ) , current
            .getEndOffset ( ) ) ;
        for ( TypeName boundTypeNames : bound.get ( i ) )
        {
          try
          {
            bonds.addPrettyAnnotation ( this.loaded.toPrettyString ( )
                .getAnnotationForPrintable ( boundTypeNames ) ) ;
          }
          catch ( IllegalArgumentException e )
          {
            /*
             * Happens if a bound TypeName is not in the PrettyString.
             */
          }
        }
        this.result.add ( bonds ) ;
      }
    }
    for ( Type tau : pType.children ( ) )
    {
      check ( tau ) ;
    }
  }


  /**
   * Returns a list with all bonds in the loaded {@link Expression}.
   * 
   * @return A list with all bonds in the loaded {@link Expression}.
   */
  public final ArrayList < Bonds > getAnnotations ( )
  {
    if ( this.result == null )
    {
      this.result = new ArrayList < Bonds > ( ) ;
      if ( this.loaded != null )
      {
        if ( this.loaded instanceof Expression )
        {
          check ( ( Expression ) this.loaded ) ;
        }
        else if ( this.loaded instanceof Type )
        {
          check ( ( Type ) this.loaded ) ;
        }
        else if ( this.loaded instanceof TypeEquationTypeInference )
        {
          check ( ( ( TypeEquationTypeInference ) this.loaded ).getLeft ( ) ) ;
          check ( ( ( TypeEquationTypeInference ) this.loaded ).getRight ( ) ) ;
        }
        else if ( this.loaded instanceof TypeSubType )
        {
          check ( ( ( TypeSubType ) this.loaded ).getType ( ) ) ;
          check ( ( ( TypeSubType ) this.loaded ).getType2 ( ) ) ;
        }
      }
    }
    return this.result ;
  }


  /**
   * Loads the {@link ShowBondsInput} to get the
   * bonds.
   * 
   * @param pLoaded The input
   *          {@link ShowBondsInput}.
   */
  public final void load ( ShowBondsInput pLoaded )
  {
    this.loaded = pLoaded ;
  }


  /**
   * {@inheritDoc} Mainly useful for debugging purposes.
   * 
   * @see Object#toString()
   */
  @ Override
  public String toString ( )
  {
    if ( this.result == null )
    {
      return "" ; //$NON-NLS-1$
    }
    String s = "" ; //$NON-NLS-1$
    for ( Bonds item : this.result )
    {
      s += item + "\n" ; //$NON-NLS-1$
    }
    return s ;
  }
}
