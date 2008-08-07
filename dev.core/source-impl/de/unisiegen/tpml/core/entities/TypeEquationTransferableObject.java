package de.unisiegen.tpml.core.entities;


/**
 * holds the source index of the dragged type equation and the hash code of the
 * type equation list we're dragging from
 */
public final class TypeEquationTransferableObject
{

  /**
   * source index of the type equation
   */
  public int sourceIndex;


  /**
   * hash code of the type equation list
   */
  public int typeEquationListHashCode;


  /**
   * Allocates a new source index/type equation list hash code pair
   * 
   * @param sourceIndex source index of the type equation
   * @param typeEquationListHashCode hash code of the type equation list
   */
  public TypeEquationTransferableObject ( final int sourceIndex,
      final int typeEquationListHashCode )
  {
    this.sourceIndex = sourceIndex;
    this.typeEquationListHashCode = typeEquationListHashCode;
  }
}
