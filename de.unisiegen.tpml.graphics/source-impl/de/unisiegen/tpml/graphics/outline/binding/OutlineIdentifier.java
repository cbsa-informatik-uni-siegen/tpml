package de.unisiegen.tpml.graphics.outline.binding ;


import java.util.ArrayList ;
import java.util.Enumeration ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;


/**
 * This class has a static method, which returns a list of {@link OutlinePair},
 * in which the start and the end index of the {@link Identifier}s is saved.
 * 
 * @author Christian Fehler
 * @version $Rev: 1078 $
 */
public abstract class OutlineIdentifier
{
  /**
   * Returns a list of {@link OutlinePair}, in which the start and the end
   * index of the {@link Identifier}s is saved. This method searches for
   * {@link Identifier}s only to the beginning of the first child
   * {@link Expression}.
   * 
   * @param pExpression The {@link Expression} in which the {@link Identifier}s
   *          should be searched for.
   * @return A list of {@link OutlinePair}, in which the start and the end
   *         index of the {@link Identifier}s is saved.
   */
  public final static ArrayList < OutlinePair > getIndex (
      Expression pExpression )
  {
    ArrayList < OutlinePair > list = new ArrayList < OutlinePair > ( ) ;
    PrettyCharIterator prettyCharIterator = pExpression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    int end = pExpression.toPrettyString ( ).toString ( ).length ( ) ;
    Enumeration < Expression > children = pExpression.children ( ) ;
    if ( children.hasMoreElements ( ) )
    {
      end = pExpression.toPrettyString ( ).getAnnotationForPrintable (
          children.nextElement ( ) ).getStartOffset ( ) ;
    }
    int charIndex = 0 ;
    int startIndex = 0 ;
    int endIndex = 0 ;
    while ( charIndex < end )
    {
      if ( PrettyStyle.IDENTIFIER.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        startIndex = charIndex ;
        while ( PrettyStyle.IDENTIFIER
            .equals ( prettyCharIterator.getStyle ( ) ) )
        {
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        endIndex = charIndex - 1 ;
        list.add ( new OutlinePair ( startIndex , endIndex ) ) ;
      }
      charIndex ++ ;
      prettyCharIterator.next ( ) ;
    }
    return list ;
  }


  /**
   * Returns a list of {@link OutlinePair}, in which the start and the end
   * index of the {@link Identifier}s is saved. This method searches for
   * {@link Identifier}s from the start to the end index.
   * 
   * @param pExpression The {@link Expression} in which the {@link Identifier}s
   *          should be searched for.
   * @param pStartSearchIndex The start index of the search.
   * @param pEndSearchIndex The end index if the search.
   * @return A list of {@link OutlinePair}, in which the start and the end
   *         index of the {@link Identifier}s is saved.
   */
  public final static ArrayList < OutlinePair > getIndexBetween (
      Expression pExpression , int pStartSearchIndex , int pEndSearchIndex )
  {
    ArrayList < OutlinePair > list = new ArrayList < OutlinePair > ( ) ;
    PrettyCharIterator prettyCharIterator = pExpression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    int charIndex = pStartSearchIndex ;
    int startIndex ;
    int endIndex ;
    for ( int i = 0 ; i < charIndex ; i ++ )
    {
      prettyCharIterator.next ( ) ;
    }
    while ( charIndex < pEndSearchIndex )
    {
      if ( PrettyStyle.IDENTIFIER.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        startIndex = charIndex ;
        while ( ( charIndex < pEndSearchIndex )
            && ( PrettyStyle.IDENTIFIER
                .equals ( prettyCharIterator.getStyle ( ) ) ) )
        {
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        endIndex = charIndex - 1 ;
        list.add ( new OutlinePair ( startIndex , endIndex ) ) ;
      }
      charIndex ++ ;
      prettyCharIterator.next ( ) ;
    }
    return list ;
  }
}
