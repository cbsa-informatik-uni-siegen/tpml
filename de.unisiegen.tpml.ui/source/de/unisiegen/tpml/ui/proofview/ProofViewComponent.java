package de.unisiegen.tpml.ui.proofview;


import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.CannotRedoException;
import de.unisiegen.tpml.core.CannotUndoException;
import de.unisiegen.tpml.core.ProofModel;
import de.unisiegen.tpml.graphics.EditorComponent;
import de.unisiegen.tpml.graphics.ProofView;


/**
 * Editor Component that displays a Proof. It unites undo / redo functions of
 * the model (core) and visualization.
 * 
 * @author Christoph Fehling
 * @version $Rev$
 */
public class ProofViewComponent extends JComponent implements EditorComponent
{

  //
  // Constants
  //

  /**
   * The {@link Logger} for this class.
   */
  private static final Logger logger = Logger
      .getLogger ( ProofViewComponent.class );


  /**
   * The unique serialization identifier.
   */
  private static final long serialVersionUID = 8218146393722855647L;


  //
  // Attributes
  //

  /**
   * TODO Add documentation here.
   */
  private ProofView view;


  /**
   * TODO Add documentation here.
   */
  private ProofModel model;


  /**
   * TODO Add documentation here.
   */
  private boolean nextStatus;


  /**
   * TODO Add documentation here.
   */
  private boolean pongStatus;


  /**
   * TODO Add documentation here.
   */
  private boolean redoStatus;


  /**
   * TODO Add documentation here.
   */
  private boolean undoStatus;


  //
  // Constructor
  //

  /**
   * TODO Add documentation here.
   * 
   * @param view The {@link ProofView}.
   * @param model The {@link ProofModel}.
   */
  @SuppressWarnings ( "synthetic-access" )
  public ProofViewComponent ( ProofView view, ProofModel model )
  {
    if ( model == null || view == null )
    {
      throw new NullPointerException ( "model or view are null" ); //$NON-NLS-1$
    }
    setLayout ( new BorderLayout () );
    this.view = view;
    this.model = model;

    this.model.addPropertyChangeListener ( new ModelChangeListener () );
    add ( ( JComponent ) view, BorderLayout.CENTER );
  }


  /**
   * sets the model of the ProofViewComponent
   * 
   * @param m the modle
   */
  @SuppressWarnings ( "synthetic-access" )
  public void setModel ( ProofModel m )
  {
    this.model = m;
    this.model.addPropertyChangeListener ( new ModelChangeListener () );
    add ( ( JComponent ) this.view, BorderLayout.CENTER );
    setDefaultStates ();
    // setPongStatus ( false );

    // setPongStatus ( false );
    // setRedoStatus ( false );
    // setUndoStatus ( false );

  }


  //
  // Accessors
  //

  /**
   * Returns the Next Status of the Component. {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#isNextStatus()
   */
  public boolean isNextStatus ()
  {
    return this.nextStatus;
  }


  /**
   * Sets the Next Status of the Component.
   * 
   * @param nextStatus the new setting for the <code>nextStatus</code>
   *          property.
   * @see #isNextStatus()
   */
  private void setNextStatus ( boolean nextStatus )
  {
    if ( this.nextStatus != nextStatus )
    {
      boolean oldNextStatus = this.nextStatus;
      this.nextStatus = nextStatus;
      firePropertyChange ( "nextStatus", oldNextStatus, nextStatus ); //$NON-NLS-1$
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#isPongStatus()
   */
  public boolean isPongStatus ()
  {
    return this.pongStatus;
  }


  /**
   * Sets the Pong Status of the Component.
   * 
   * @param pongStatus the new setting for the <code>pongStatus</code>
   *          property.
   * @see #isPongStatus()
   */
  public void setPongStatus ( boolean pongStatus )
  {
    if ( this.pongStatus != pongStatus )
    {
      boolean oldPongStatus = this.pongStatus;
      this.pongStatus = pongStatus;
      firePropertyChange ( "pongStatus", oldPongStatus, pongStatus ); //$NON-NLS-1$
    }
  }


  /**
   * Returns the Redo Status of the Component. {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#isRedoStatus()
   */
  public boolean isRedoStatus ()
  {
    return this.redoStatus;
  }


  /**
   * Sets the Redo Status of the Component.
   * 
   * @param redoStatus the new setting for the <code>redoStatus</code>
   *          property.
   * @see #isRedoStatus()
   */
  private void setRedoStatus ( boolean redoStatus )
  {
    if ( this.redoStatus != redoStatus )
    {
      boolean oldRedoStatus = this.redoStatus;
      this.redoStatus = redoStatus;
      firePropertyChange ( "redoStatus", oldRedoStatus, redoStatus ); //$NON-NLS-1$
    }
  }


  /**
   * Returns the Undo Status of the Component. {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#isUndoStatus()
   */
  public boolean isUndoStatus ()
  {
    return this.undoStatus;
  }


  /**
   * Sets the Undo Status of the Component.
   * 
   * @param undoStatus the new setting for the <code>undoStatus</code>
   *          property.
   * @see #isUndoStatus()
   */
  private void setUndoStatus ( boolean undoStatus )
  {
    if ( this.undoStatus != undoStatus )
    {
      boolean oldUndoStatus = this.undoStatus;
      this.undoStatus = undoStatus;
      firePropertyChange ( "undoStatus", oldUndoStatus, undoStatus ); //$NON-NLS-1$
    }
  }


  //
  // Primitives
  //

  /**
   * Sets the Default States of the Component's functions. Attention: For now
   * the NextStatus is alsways enabled. {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#setDefaultStates()
   */
  public void setDefaultStates ()
  {
    setNextStatus ( !this.model.isFinished () );
    setRedoStatus ( this.model.isRedoable () );
    setUndoStatus ( this.model.isUndoable () );
  }


  /**
   * Executes the next function on the view. {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#handleNext()
   */
  public void handleNext ()
  {
    try
    {
      this.view.guess ();
    }
    catch ( Exception e )
    {
      JOptionPane
          .showMessageDialog (
              getTopLevelAncestor (),
              MessageFormat.format ( java.util.ResourceBundle.getBundle (
                  "de/unisiegen/tpml/ui/ui" ).getString ( "NodeComponent.5" ), //$NON-NLS-1$//$NON-NLS-2$
                  e.getMessage () ),
              java.util.ResourceBundle
                  .getBundle ( "de/unisiegen/tpml/ui/ui" ).getString ( "NodeComponent.6" ), JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$ //$NON-NLS-2$
    }

  }


  /**
   * Executes the redo function on the view. {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#handleRedo()
   */
  public void handleRedo ()
  {
    try
    {
      this.model.redo ();
    }
    catch ( CannotRedoException e )
    {
      logger.error ( "Can not redo on this model", e ); //$NON-NLS-1$
    }
  }


  /**
   * Executes the undo function on the view. {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#handleUndo()
   */
  public void handleUndo ()
  {
    try
    {
      this.model.undo ();
    }
    catch ( CannotUndoException e )
    {
      logger.error ( "Can not undo on this model", e ); //$NON-NLS-1$
    }
  }


  /**
   * Handles Property Changes fired by the model. it supports: cheating,
   * undoable, redoable, and finished.
   */
  private class ModelChangeListener implements PropertyChangeListener
  {

    /**
     * TODO
     * 
     * @param evt
     * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    @SuppressWarnings ( "synthetic-access" )
    public void propertyChange ( PropertyChangeEvent evt )
    {
      // determine the name of the changed property
      String propertyName = evt.getPropertyName ().intern ();

      // the undo/redo/next stati
      if ( propertyName == "undoable" ) //$NON-NLS-1$
      {
        setUndoStatus ( ( ( Boolean ) evt.getNewValue () ).booleanValue () );
      }
      else if ( propertyName == "redoable" ) //$NON-NLS-1$
      {
        setRedoStatus ( ( ( Boolean ) evt.getNewValue () ).booleanValue () );
      }
      else if ( propertyName == "finished" ) //$NON-NLS-1$
      {
        setNextStatus ( ! ( ( Boolean ) evt.getNewValue () ).booleanValue () );
      }

      // the pong status
      if ( propertyName == "cheating" || propertyName == "finished" ) //$NON-NLS-1$ //$NON-NLS-2$
      {
        setPongStatus ( !ProofViewComponent.this.model.isCheating ()
            && ProofViewComponent.this.model.isFinished () );
      }
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.EditorComponent#setAdvanced(boolean)
   */
  public void setAdvanced ( boolean status )
  {
    this.view.setAdvanced ( status );
  }


  /**
   * Returns the model.
   * 
   * @return The model.
   */
  public ProofModel getModel ()
  {
    return this.model;
  }


  /**
   * Returns the print part.
   * 
   * @see EditorComponent#getPrintPart()
   */
  public JComponent getPrintPart ()
  {
    return this.view.getPrintPart ();
  }
}
