package de.unisiegen.tpml.graphics.outline ;


import java.util.ArrayList ;
import java.util.Enumeration ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;


/**
 * In this class the breaks of the {@link OutlineNode}s are saved.
 * 
 * @author Christian Fehler
 */
public final class OutlineBreak
{
  /**
   * The list of breaks.
   */
  private ArrayList < Integer > breakList ;


  /**
   * The list of child {@link OutlineBreak}s.
   */
  private ArrayList < OutlineBreak > outlineBreakList ;


  /**
   * Initilizes the {@link OutlineBreak}.
   */
  public OutlineBreak ( )
  {
    this.breakList = new ArrayList < Integer > ( ) ;
    this.outlineBreakList = new ArrayList < OutlineBreak > ( ) ;
  }


  /**
   * Initilizes the {@link OutlineBreak} with the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   */
  public OutlineBreak ( Expression pExpression )
  {
    this.breakList = new ArrayList < Integer > ( ) ;
    this.outlineBreakList = new ArrayList < OutlineBreak > ( ) ;
    calculate ( pExpression ) ;
  }


  /**
   * Adds the given offset to the own breaks and the breaks of all children
   * {@link OutlineBreak}s.
   * 
   * @param pOffset The offset, which should be added.
   */
  private final void addOffset ( int pOffset )
  {
    for ( int i = 0 ; i < this.breakList.size ( ) ; i ++ )
    {
      this.breakList.set ( i , new Integer ( this.breakList.get ( i )
          .intValue ( )
          + pOffset ) ) ;
    }
    for ( int i = 0 ; i < this.outlineBreakList.size ( ) ; i ++ )
    {
      this.outlineBreakList.get ( i ).addOffset ( pOffset ) ;
    }
  }


  /**
   * Returns the number of breaks of this {@link OutlineBreak}, without the
   * breaks of children {@link OutlineBreak}s.
   * 
   * @return The number of breaks of this {@link OutlineBreak}, without the
   *         breaks of children {@link OutlineBreak}s.
   */
  public final int getBreakCountOwn ( )
  {
    return this.breakList.size ( ) ;
  }


  /**
   * Returns the number of breaks of this {@link OutlineBreak}, including the
   * breaks of children {@link OutlineBreak}s.
   * 
   * @return The number of breaks of this {@link OutlineBreak}, including the
   *         breaks of children {@link OutlineBreak}s.
   */
  public final int getBreakCountAll ( )
  {
    int result = 0 ;
    result += this.breakList.size ( ) ;
    for ( int i = 0 ; i < this.outlineBreakList.size ( ) ; i ++ )
    {
      result += this.outlineBreakList.get ( i ).getBreakCountAll ( ) ;
    }
    return result ;
  }


  /**
   * Calculates the breaks.
   * 
   * @param pExpression The input {@link Expression}.
   */
  private final void calculate ( Expression pExpression )
  {
    PrettyAnnotation prettyAnnotation = pExpression.toPrettyString ( )
        .getAnnotationForPrintable ( pExpression ) ;
    int [ ] breaks = prettyAnnotation.getBreakOffsets ( ) ;
    for ( int i = 0 ; i < breaks.length ; i ++ )
    {
      this.breakList.add ( new Integer ( breaks [ i ] ) ) ;
    }
    Enumeration < Expression > children = pExpression.children ( ) ;
    while ( children.hasMoreElements ( ) )
    {
      Expression expr = children.nextElement ( ) ;
      PrettyAnnotation prettyAnnotationChild ;
      try
      {
        prettyAnnotationChild = pExpression.toPrettyString ( )
            .getAnnotationForPrintable ( expr ) ;
      }
      catch ( IllegalArgumentException e )
      {
        continue ;
      }
      OutlineBreak outlineBreakChild = new OutlineBreak ( expr ) ;
      outlineBreakChild.addOffset ( prettyAnnotationChild.getStartOffset ( ) ) ;
      this.outlineBreakList.add ( outlineBreakChild ) ;
    }
  }


  /**
   * Returns a new {@link OutlineBreak}, which has the breaks of the children
   * of this {@link OutlineBreak}, but only children within the depth.
   * 
   * @param pDepth The depth.
   * @return A new {@link OutlineBreak}, which has the breaks of the children
   *         of this {@link OutlineBreak}, but only children within the depth.
   */
  public final OutlineBreak getBreaks ( int pDepth )
  {
    OutlineBreak result = new OutlineBreak ( ) ;
    if ( pDepth == 0 )
    {
      return result ;
    }
    if ( pDepth == 1 )
    {
      result.breakList.addAll ( this.breakList ) ;
      result.outlineBreakList.addAll ( this.outlineBreakList ) ;
      return result ;
    }
    result.breakList.addAll ( this.breakList ) ;
    for ( int i = 0 ; i < this.outlineBreakList.size ( ) ; i ++ )
    {
      OutlineBreak child = this.outlineBreakList.get ( i ).getBreaks (
          pDepth - 1 ) ;
      result.breakList.addAll ( child.breakList ) ;
      result.outlineBreakList.addAll ( child.outlineBreakList ) ;
    }
    return result ;
  }


  /**
   * Returns true, if this {@link OutlineBreak} has a break, or some children
   * has a break. Otherwise false.
   * 
   * @return True, if this {@link OutlineBreak} has a break, or some children
   *         has a break. Otherwise false.
   */
  public final boolean hasBreaksAll ( )
  {
    if ( this.breakList.size ( ) > 0 )
    {
      return true ;
    }
    boolean found ;
    for ( int i = 0 ; i < this.outlineBreakList.size ( ) ; i ++ )
    {
      found = this.outlineBreakList.get ( i ).hasBreaksAll ( ) ;
      if ( found )
      {
        return true ;
      }
    }
    return false ;
  }


  /**
   * Returns true, if there is a break at the given char index. Otherwise false.
   * 
   * @param pCharIndex The char index.
   * @return True, if there is a break at the given char index. Otherwise false.
   */
  public final boolean isBreak ( int pCharIndex )
  {
    return this.breakList.contains ( new Integer ( pCharIndex ) ) ;
  }
}
