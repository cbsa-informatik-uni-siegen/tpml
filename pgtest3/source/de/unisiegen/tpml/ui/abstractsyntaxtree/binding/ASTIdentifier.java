package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


import java.util.ArrayList ;
import java.util.Enumeration ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;


/**
 * This class has a static method, which returns a list of ASTPair, in which the
 * start and the end index of the Identifiers is saved.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public abstract class ASTIdentifier
{
  /**
   * Returns a list of ASTPair, in which the start and the end index of the
   * Identifiers is saved.
   * 
   * @param pExpression The Expression in which the Identifiers should be
   *          searched for.
   * @return A list of ASTPair, in which the start and the end index of the
   *         Identifiers is saved.
   */
  public final static ArrayList < ASTPair > getIndex ( Expression pExpression )
  {
    ArrayList < ASTPair > list = new ArrayList < ASTPair > ( ) ;
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
        list.add ( new ASTPair ( start , end ) ) ;
      }
      charIndex ++ ;
      prettyCharIterator.next ( ) ;
    }
    return list ;
  }
}
