package de.unisiegen.tpml.graphics.subtyping;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.subtyping.SubTypingModel;
import de.unisiegen.tpml.graphics.AbstractProofView;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the type checker interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class SubTypingView extends AbstractProofView
{

  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = -425214200136389228L;


  /**
   * The <code>SubTyping</code> component.
   */
  protected SubTypingComponent component;


  /**
   * The <code>JScrollPane</code> for the <code>component</code>.
   */
  protected JScrollPane scrollPane;


  /**
   * The <code>JSplitPane</code> for the <code>component</code>.
   */
  private JSplitPane jSplitPane;


  /**
   * The panel containing the outlines
   */
  private JPanel outlinePanel;


  /**
   * The first {@link Outline} of this view.
   * 
   * @see #getOutline1()
   */
  private DefaultOutline outline1;


  /**
   * The second {@link Outline} of this view.
   * 
   * @see #getOutline1()
   */
  private DefaultOutline outline2;


  /**
   * The {@link SubTypingModel}.
   */
  private SubTypingModel subTypingModel;


  /**
   * Allocates a new {@link SubTypingView} for the specified
   * {@link SubTypingModel}.
   * 
   * @param pSubTypingModel The {@link SubTypingModel} for the
   *          <code>SubTypingView</code>.
   */
  public SubTypingView ( SubTypingModel pSubTypingModel )
  {
    super ();
    this.subTypingModel = pSubTypingModel;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ();
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT );
    setLayout ( new GridBagLayout () );
    this.scrollPane = new JScrollPane ();
    this.component = new SubTypingComponent ( this.subTypingModel );
    this.scrollPane.setViewportView ( this.component );
    this.scrollPane.getViewport ().setBackground ( Color.WHITE );
    this.scrollPane.addComponentListener ( new ComponentAdapter ()
    {

      @Override
      public void componentResized ( @SuppressWarnings ( "unused" )
      ComponentEvent event )
      {
        SubTypingView.this.component
            .setAvailableWidth ( SubTypingView.this.scrollPane.getViewport ()
                .getWidth () );
      }
    } );
    this.outlinePanel = new JPanel ( new GridBagLayout () );

    this.outline1 = new DefaultOutline ( this );
    this.outline1.load ( this.subTypingModel.getRoot ().getLastLeaf ()
        .getLeft (), Outline.ExecuteInit.SUBTYPING );
    JScrollPane jPanelOutline1 = this.outline1.getTree ();
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.insets = new Insets ( 0, 0, 0, 2 );
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 10;
    gridBagConstraints.weighty = 10;
    gridBagConstraints.gridwidth = 1;

    this.outlinePanel.add ( jPanelOutline1, gridBagConstraints );

    this.outline2 = new DefaultOutline ( this );
    this.outline1.setSyncOutline ( this.outline2 );
    this.outline2.load ( this.subTypingModel.getRoot ().getLastLeaf ()
        .getRight (), Outline.ExecuteInit.SUBTYPING );

    JScrollPane jPanelOutline2 = this.outline2.getTree ();

    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.insets = new Insets ( 0, 2, 0, 0 );
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 10;
    gridBagConstraints.weighty = 10;
    gridBagConstraints.gridwidth = 1;

    this.outlinePanel.add ( jPanelOutline2, gridBagConstraints );

    JPanel preferences = this.outline1.getPanelPreferences ();

    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.insets = new Insets ( 0, 2, 0, 0 );
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 10;
    gridBagConstraints.weighty = 0;
    gridBagConstraints.gridwidth = 2;

    this.outlinePanel.add ( preferences, gridBagConstraints );

    this.jSplitPane.setLeftComponent ( this.scrollPane );
    this.jSplitPane.setRightComponent ( this.outlinePanel );
    this.jSplitPane.setOneTouchExpandable ( true );
    this.jSplitPane.setResizeWeight ( 0.5 );

    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.insets = new Insets ( 0, 0, 0, 0 );
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 10;
    gridBagConstraints.weighty = 10;
    gridBagConstraints.gridwidth = 1;

    this.add ( this.jSplitPane, gridBagConstraints );
  }


  /**
   * Returns the jSplitPane.
   * 
   * @return The jSplitPane.
   * @see #jSplitPane
   */
  public JSplitPane getJSplitPane ()
  {
    return this.jSplitPane;
  }


  /**
   * Returns the first {@link Outline} of this view.
   * 
   * @return The first {@link Outline} of this view.
   */
  public Outline getOutline1 ()
  {
    return this.outline1;
  }


  /**
   * Returns the second {@link Outline} of this view.
   * 
   * @return The second {@link Outline} of this view.
   */
  public Outline getOutline2 ()
  {
    return this.outline2;
  }


  /**
   * Returns the SubTypingModel.
   * 
   * @return The SubTypingModel.
   */
  public SubTypingModel getSubTypingModel ()
  {
    return this.subTypingModel;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.ProofView#guess()
   */
  public void guess () throws IllegalStateException, ProofGuessException
  {
    this.component.guess ();
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see de.unisiegen.tpml.graphics.ProofView#getPrintPart()
   */
  public JComponent getPrintPart ()
  {
    return this.component.clone ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.AbstractProofView#setAdvanced(boolean)
   */
  @Override
  public void setAdvanced ( boolean advanced )
  {
    super.setAdvanced ( advanced );
    this.component.setAdvanced ( isAdvanced () );
  }
}
