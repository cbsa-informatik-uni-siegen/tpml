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


public abstract class AbstractProofComponent extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = 4038129211367836077L;


  protected AbstractProofModel proofModel;


  protected LanguageTranslator translator;


  protected boolean currentlyLayouting;


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


  private Theme theme;


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


      public void treeStructureChanged ( TreeModelEvent e )
      {
        AbstractProofComponent.this.treeContentChanged ();
      }
    } );

    // reset the layout whenever the font changes
    this.theme = Theme.currentTheme ();
    this.theme.addPropertyChangeListener ( "font",
        new PropertyChangeListener ()
        {

          public void propertyChange ( PropertyChangeEvent evt )
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


  protected abstract void nodesInserted ( TreeModelEvent event );


  protected abstract void nodesChanged ( TreeModelEvent event );


  protected abstract void nodesRemoved ( TreeModelEvent event );


  protected abstract void treeContentChanged ();


  protected abstract void relayout ();


  protected abstract void forcedRelayout ();


  protected abstract void resetLayout ();


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
   * 
   * @param pAvailableHeight
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
