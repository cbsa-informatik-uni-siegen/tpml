package de.unisiegen.tpml.graphics.components;


import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;


/**
 * Stores the start and end offsets of the bounde{@link Identifier}s.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @version $Id$
 */
public final class Bonds
{

  /**
   * Start offset of the {@link Identifier}.
   */
  private int startOffset;


  /**
   * End offset of the {@link Identifier}.
   */
  private int endOffset;


  /**
   * List of Annotations with all Variables which are bound to the
   * {@link Identifier}.
   */
  private ArrayList < PrettyAnnotation > prettyAnnotation;


  /**
   * Initializes the Bonds.
   * 
   * @param pStartOffset Start offset of the {@link Identifier}.
   * @param pEndOffset End offset of the {@link Identifier}.
   */
  public Bonds ( int pStartOffset, int pEndOffset )
  {
    this.prettyAnnotation = new ArrayList < PrettyAnnotation > ();
    this.startOffset = pStartOffset;
    this.endOffset = pEndOffset;
  }


  /**
   * Adds a {@link PrettyAnnotation} to the list.
   * 
   * @param pPrettyAnnotation The new {@link PrettyAnnotation}.
   */
  public final void addPrettyAnnotation ( PrettyAnnotation pPrettyAnnotation )
  {
    this.prettyAnnotation.add ( pPrettyAnnotation );
  }


  /**
   * Returns the end offset of the {@link Identifier}.
   * 
   * @return The end offset of the {@link Identifier}.
   */
  public final int getEndOffset ()
  {
    return this.endOffset;
  }


  /**
   * Returns a list of {@link PrettyAnnotation}s of {@link Identifier}.
   * 
   * @return A list of {@link PrettyAnnotation}s of {@link Identifier}.
   */
  public final ArrayList < PrettyAnnotation > getPrettyAnnotation ()
  {
    return this.prettyAnnotation;
  }


  /**
   * Returns the start offset of the {@link Identifier}.
   * 
   * @return The end offset of the {@link Identifier}.
   */
  public final int getStartOffset ()
  {
    return this.startOffset;
  }


  /**
   * {@inheritDoc} Mainly useful for debugging purposes.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString ()
  {
    String result = this.startOffset + "-" + this.endOffset; //$NON-NLS-1$
    for ( PrettyAnnotation item : this.prettyAnnotation )
    {
      result += "; " + item.getStartOffset () + "-" + item.getEndOffset (); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return result;
  }
}
