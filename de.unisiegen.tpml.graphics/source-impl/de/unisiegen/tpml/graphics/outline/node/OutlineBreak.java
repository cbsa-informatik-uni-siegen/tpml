package de.unisiegen.tpml.graphics.outline.node ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;


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
   * Initilizes the {@link OutlineBreak} with the given {@link ExpressionOrType}.
   * 
   * @param pExpressionOrType The input {@link ExpressionOrType}.
   */
  public OutlineBreak ( ExpressionOrType pExpressionOrType )
  {
    this.breakList = new ArrayList < Integer > ( ) ;
    this.outlineBreakList = new ArrayList < OutlineBreak > ( ) ;
    if ( pExpressionOrType instanceof Expression )
    {
      calculate ( ( Expression ) pExpressionOrType ) ;
    }
    else if ( pExpressionOrType instanceof Type )
    {
      calculate ( ( Type ) pExpressionOrType ) ;
    }
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
    ArrayList < Expression > children = pExpression.children ( ) ;
    for ( Expression expr : children )
    {
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
      if ( breaks.length > 0 )
      {
        this.outlineBreakList.add ( outlineBreakChild ) ;
      }
      else
      {
        for ( int i = 0 ; i < outlineBreakChild.breakList.size ( ) ; i ++ )
        {
          this.breakList.add ( outlineBreakChild.breakList.get ( i ) ) ;
        }
        for ( int i = 0 ; i < outlineBreakChild.outlineBreakList.size ( ) ; i ++ )
        {
          this.outlineBreakList.add ( outlineBreakChild.outlineBreakList
              .get ( i ) ) ;
        }
      }
    }
    if ( pExpression instanceof DefaultTypes )
    {
      MonoType [ ] types = ( ( DefaultTypes ) pExpression ).getTypes ( ) ;
      for ( MonoType tau : types )
      {
        PrettyAnnotation prettyAnnotationChild ;
        try
        {
          prettyAnnotationChild = pExpression.toPrettyString ( )
              .getAnnotationForPrintable ( tau ) ;
        }
        catch ( IllegalArgumentException e )
        {
          continue ;
        }
        OutlineBreak outlineBreakChild = new OutlineBreak ( tau ) ;
        outlineBreakChild.addOffset ( prettyAnnotationChild.getStartOffset ( ) ) ;
        if ( breaks.length > 0 )
        {
          this.outlineBreakList.add ( outlineBreakChild ) ;
        }
        else
        {
          for ( int i = 0 ; i < outlineBreakChild.breakList.size ( ) ; i ++ )
          {
            this.breakList.add ( outlineBreakChild.breakList.get ( i ) ) ;
          }
          for ( int i = 0 ; i < outlineBreakChild.outlineBreakList.size ( ) ; i ++ )
          {
            this.outlineBreakList.add ( outlineBreakChild.outlineBreakList
                .get ( i ) ) ;
          }
        }
      }
    }
  }


  /**
   * Calculates the breaks.
   * 
   * @param pType The input {@link Type}.
   */
  private final void calculate ( Type pType )
  {
    PrettyAnnotation prettyAnnotation = pType.toPrettyString ( )
        .getAnnotationForPrintable ( pType ) ;
    int [ ] breaks = prettyAnnotation.getBreakOffsets ( ) ;
    for ( int i = 0 ; i < breaks.length ; i ++ )
    {
      this.breakList.add ( new Integer ( breaks [ i ] ) ) ;
    }
    ArrayList < Type > children = pType.children ( ) ;
    for ( Type child : children )
    {
      PrettyAnnotation prettyAnnotationChild ;
      try
      {
        prettyAnnotationChild = pType.toPrettyString ( )
            .getAnnotationForPrintable ( child ) ;
      }
      catch ( IllegalArgumentException e )
      {
        continue ;
      }
      OutlineBreak outlineBreakChild = new OutlineBreak ( child ) ;
      outlineBreakChild.addOffset ( prettyAnnotationChild.getStartOffset ( ) ) ;
      if ( breaks.length > 0 )
      {
        this.outlineBreakList.add ( outlineBreakChild ) ;
      }
      else
      {
        for ( int i = 0 ; i < outlineBreakChild.breakList.size ( ) ; i ++ )
        {
          this.breakList.add ( outlineBreakChild.breakList.get ( i ) ) ;
        }
        for ( int i = 0 ; i < outlineBreakChild.outlineBreakList.size ( ) ; i ++ )
        {
          this.outlineBreakList.add ( outlineBreakChild.outlineBreakList
              .get ( i ) ) ;
        }
      }
    }
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
