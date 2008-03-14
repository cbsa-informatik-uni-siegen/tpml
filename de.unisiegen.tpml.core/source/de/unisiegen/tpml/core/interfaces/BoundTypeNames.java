package de.unisiegen.tpml.core.interfaces;


import java.util.ArrayList;

import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeName;


/**
 * Interface for classes whose instances have child {@link TypeName}s which
 * bound other {@link TypeName}s.
 * 
 * @author Christian Fehler
 * @version $Id$
 */
public interface BoundTypeNames extends DefaultTypeNames
{

  /**
   * Returns a list of lists of in this {@link Type} bound {@link TypeName}s.
   * 
   * @return A list of lists of in this {@link Type} bound {@link TypeName}s.
   */
  public ArrayList < ArrayList < TypeName > > getTypeNamesBound ();
}
