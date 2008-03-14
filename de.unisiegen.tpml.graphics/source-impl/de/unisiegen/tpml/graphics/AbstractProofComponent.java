package de.unisiegen.tpml.graphics;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.util.Theme;


/**
 * @version $Id$
 */
public abstract class AbstractProofComponent extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = 4038129211367836077L;


  /**
   * TODO
   */
  protected AbstractProofModel proofModel;


  /**
   * TODO
   */
  protected LanguageTranslator translator;


  /**
   * TODO
   */
  protected boolean currentlyLayouting;


  /**
   * TODO
   */
  protected int availableWidth;


  /**
   * this is only needed when printing. The availableHeight stands for the
   * height available eache side and comes form the dialoge from the gui
   */
  protected int availableHeight;


  /**
   * to place the singel components on the singel pages
   */
  protected int actualPageSpaceCounter = 0;


  /**
   * TODO
   */
  private Theme theme;


  /**
   * TODO
   * 
   * @param proofModel
   */
  public AbstractProofComponent ( AbstractProofModel proofModel )
  {
    super ();
    // if nobody is printing prevent pagebreak by setting the availableHeight to
    // Integer.MAX_VALUE;
    this.availableHeight = Integer.MAX_VALUE;
    this.proofModel = proofModel;
    this.currentlyLayouting = false;
    this.translator = this.proofModel.getLanguage ().newTranslator ();

    this.proofModel.addTreeModelListener ( new TreeModelListener ()
    {

      public void treeNodesChanged ( TreeModelEvent e )
      {
        AbstractProofComponent.this.nodesChanged ( e );
        AbstractProofComponent.this.relayout ();
      }


      public void treeNodesInserted ( TreeModelEvent e )
      {
        AbstractProofComponent.this.nodesInserted ( e );
        AbstractProofComponent.this.treeContentChanged ();
      }


      public void treeNodesRemoved ( TreeModelEvent e )
      {
        AbstractProofComponent.this.nodesRemoved ( e );
        AbstractProofComponent.this.treeContentChanged ();
      }


      public void treeStructureChanged ( @SuppressWarnings ( "unused" )
      TreeModelEvent e )
      {
        AbstractProofComponent.this.treeContentChanged ();
      }
    } );

    // reset the layout whenever the font changes
    this.theme = Theme.currentTheme ();
    this.theme.addPropertyChangeListener ( "font", //$NON-NLS-1$
        new PropertyChangeListener ()
        {

          public void propertyChange ( @SuppressWarnings ( "unused" )
          PropertyChangeEvent evt )
          {
            SwingUtilities.invokeLater ( new Runnable ()
            {

              public void run ()
              {
                resetLayout ();
                relayout ();
              }
            } );
          }
        } );
  }


  /**
   * TODO
   * 
   * @param event
   */
  protected abstract void nodesInserted ( TreeModelEvent event );


  /**
   * TODO
   * 
   * @param event
   */
  protected abstract void nodesChanged ( TreeModelEvent event );


  /**
   * TODO
   * 
   * @param event
   */
  protected abstract void nodesRemoved ( TreeModelEvent event );


  /**
   * TODO
   */
  protected abstract void treeContentChanged ();


  /**
   * TODO
   */
  protected abstract void relayout ();


  /**
   * TODO
   */
  protected abstract void forcedRelayout ();


  /**
   * TODO
   */
  protected abstract void resetLayout ();


  /**
   * TODO
   * 
   * @param availableWidth
   */
  public void setAvailableWidth ( int availableWidth )
  {
    this.availableWidth = availableWidth;
    relayout ();
  }


  /**
   * set the availableHeight. It is used while printing.
   * 
   * @param pAvailableHeight
   */
  public void setAvailableHeight ( int pAvailableHeight )
  {
    this.availableHeight = pAvailableHeight;
    // the relayout must be performed immediately
    forcedRelayout ();
  }


  /**
   * resets the availableHeight
   */
  public void resetAvailableHeight ()
  {
    this.availableHeight = Integer.MAX_VALUE;
    // TODO test
    // forcedRelayout();
    relayout ();
  }


  /**
   * @param currentlyLayouting the currentlyLayouting to set
   */
  public void setCurrentlyLayouting ( boolean currentlyLayouting )
  {
    this.currentlyLayouting = currentlyLayouting;
  }


  /**
   * @return the proofModel
   */
  public AbstractProofModel getProofModel ()
  {
    return this.proofModel;
  }


  /**
   * @return the availableWidth
   */
  public int getAvailableWidth ()
  {
    return this.availableWidth;
  }


  /**
   * @return the availableHeight
   */
  public int getAvailableHeight ()
  {
    return this.availableHeight;
  }

}
