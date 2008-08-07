package de.unisiegen.tpml.graphics.components;


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import de.unisiegen.tpml.core.entities.TypeEquationTransferableObject;


/**
 * Drag and Drop transfer handler class for
 * {@link TypeEquationTransferableObject}s
 */
public abstract class CompoundEquationUnifyTransferHandler extends
    TransferHandler
{

  /**
   * The source actions supported for dragging using this
   * {@link CompoundEquationUnifyTransferHandler}.
   * 
   * @see #getSourceActions(JComponent)
   */
  private final int sourceActions;


  /**
   * Allocates a new {@link CompoundEquationUnifyTransferHandler}
   * 
   * @param sourceActions The actions to support for dragging using this
   *          {@link CompoundEquationUnifyTransferHandler}
   */
  public CompoundEquationUnifyTransferHandler ( final int sourceActions )
  {
    super ();
    this.sourceActions = sourceActions;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TransferHandler#canImport(JComponent, DataFlavor[])
   */
  @Override
  public final boolean canImport ( JComponent jComponent,
      DataFlavor [] dataFlavor )
  {
    if ( jComponent instanceof CompoundEquationUnify )
    {
      for ( DataFlavor transferFlavor : dataFlavor )
      {
        if ( transferFlavor
            .equals ( CompoundEquationUnifyTransferable.dataFlavor ) )
        {
          return true;
        }
      }
    }
    return super.canImport ( jComponent, dataFlavor );
  }


  /**
   * {@inheritDoc}
   * 
   * @see TransferHandler#createTransferable(JComponent)
   */
  @Override
  protected final Transferable createTransferable ( JComponent jComponent )
  {
    CompoundEquationUnify compoundEquationUnify = ( CompoundEquationUnify ) jComponent;
    return new CompoundEquationUnifyTransferable (
        new TypeEquationTransferableObject ( compoundEquationUnify
            .getTypeEquationListDNDSourceIndex (), compoundEquationUnify
            .getTypeEquationListHashCode () ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see TransferHandler#getSourceActions(JComponent)
   */
  @Override
  public final int getSourceActions ( @SuppressWarnings ( "unused" )
  JComponent jComponent )
  {
    return this.sourceActions;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TransferHandler#importData(JComponent, Transferable)
   */
  @Override
  public final boolean importData ( JComponent jComponent,
      Transferable transferable )
  {
    // JGTITabbedPane jGTITabbedPane = ( JGTITabbedPane ) jComponent;
    // try
    // {
    // JGTITabbedPaneComponent component = ( JGTITabbedPaneComponent )
    // transferable
    // .getTransferData ( JGTITabbedPaneTransferable.dataFlavor );
    //
    // if ( importComponent ( component.getSource (), jGTITabbedPane, component
    // .getComponent () ) )
    // {
    // return true;
    // }
    // return super.importData ( jComponent, transferable );
    // }
    // catch ( IOException e )
    // {
    // throw new RuntimeException ( e );
    // }
    // catch ( UnsupportedFlavorException e )
    // {
    // throw new RuntimeException ( e );
    // }
    return false;
  }
}
