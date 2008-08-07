package de.unisiegen.tpml.graphics.components;


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import de.unisiegen.tpml.core.entities.TypeEquationTransferableObject;


/**
 * Transferable for {@link CompoundEquationUnify}
 * 
 * @author Christian Uhrhan
 * @version $Id: CompoundEquationUnifyTransferable.java 728 2008-04-04 12:28:48Z
 *          uhrhan $
 */
public final class CompoundEquationUnifyTransferable implements Transferable
{

  /**
   * The {@link DataFlavor} used to identify data transfers of
   * {@link TypeEquationTransferableObject}.
   */
  public static final DataFlavor dataFlavor;

  static
  {
    try
    {
      dataFlavor = new DataFlavor ( DataFlavor.javaJVMLocalObjectMimeType
          + ";class=" //$NON-NLS-1$
          + TypeEquationTransferableObject.class.getCanonicalName () );
    }
    catch ( ClassNotFoundException e )
    {
      throw new RuntimeException ( e );
    }
  }


  /**
   * holds the source index of the type equation we're going to drag and the
   * hash code of the type equation list we're dragging from
   */
  private TypeEquationTransferableObject typeEquationTransferableObject;


  /**
   * Allocates a new {@link CompoundEquationUnifyTransferable} for the specified
   * type equation index and type equation list hash code
   * 
   * @param typeEquationTransferableObject the transferable object containing
   *          the source index of the dragged type equation and the hash code of
   *          the type equation list we're dragging from
   */
  public CompoundEquationUnifyTransferable (
      TypeEquationTransferableObject typeEquationTransferableObject )
  {
    this.typeEquationTransferableObject = typeEquationTransferableObject;
  }


  /**
   * {@inheritDoc}
   * 
   * @param flavor
   * @return a TypeEquationTransferableObject containing the source index and
   *         the hash code of the source type equation list
   * @throws UnsupportedFlavorException
   * @throws IOException
   * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
   */
  @SuppressWarnings ( "unused" )
  public Object getTransferData ( DataFlavor flavor )
      throws UnsupportedFlavorException, IOException
  {
    if ( isDataFlavorSupported ( flavor ) )
      return this.typeEquationTransferableObject;
    throw new UnsupportedFlavorException ( flavor );
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
   */
  public DataFlavor [] getTransferDataFlavors ()
  {
    return new DataFlavor []
    { dataFlavor };
  }


  /**
   * {@inheritDoc}
   * 
   * @see Transferable#isDataFlavorSupported(DataFlavor)
   */
  public boolean isDataFlavorSupported ( DataFlavor flavor )
  {
    for ( DataFlavor supportedFlavor : getTransferDataFlavors () )
    {
      if ( supportedFlavor.equals ( flavor ) )
      {
        return true;
      }
    }
    return false;
  }

}
