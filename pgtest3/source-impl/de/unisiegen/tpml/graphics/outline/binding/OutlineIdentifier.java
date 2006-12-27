package de.unisiegen.tpml.graphics.outline.binding ;


import java.util.ArrayList ;
import java.util.Enumeration ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;


/**
 * This class has a static method, which returns a list of
 * <code>OutlinePair</code>, in which the start and the end index of the
 * <code>Identifiers</code> is saved.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public abstract class OutlineIdentifier
{
  /**
   * Returns a list of <code>OutlinePair</code>, in which the start and the
   * end index of the <code>Identifiers</code> is saved.
   * 
   * @param pExpression The <code>Expression</code> in which the
   *          <code>Identifiers</code> should be searched for.
   * @return A list of <code>OutlinePair</code>, in which the start and the
   *         end index of the <code>Identifiers</code> is saved.
   */
  public final static ArrayList < OutlinePair > getIndex (
      Expression pExpression )
  {
    ArrayList < OutlinePair > list = new ArrayList < OutlinePair > ( ) ;
    PrettyCharIterator prettyCharIterator = pExpression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    int beginChild = pExpression.toPrettyString ( ).toString ( ).length ( ) ;
    Enumeration < Expression > children = pExpression.children ( ) ;
    if ( children.hasMoreElements ( ) )
    {
      beginChild = pExpression.toPrettyString ( ).getAnnotationForPrintable (
          children.nextElement ( ) ).getStartOffset ( ) ;
    }
    int charIndex = 0 ;
    int start = 0 ;
    int end = 0 ;
    while ( charIndex < beginChild )
    {
      if ( prettyCharIterator.getStyle ( ) == PrettyStyle.IDENTIFIER )
      {
        start = charIndex ;
        while ( prettyCharIterator.getStyle ( ) == PrettyStyle.IDENTIFIER )
        {
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        end = charIndex - 1 ;
        list.add ( new OutlinePair ( start , end ) ) ;
      }
      charIndex ++ ;
      prettyCharIterator.next ( ) ;
    }
    return list ;
  }
}
