package de.unisiegen.tpml.graphics.bigstepclosure;


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
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofModel;
import de.unisiegen.tpml.graphics.AbstractProofView;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the big step interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Id: BigStepView.java 2796 2008-03-14 19:13:11Z fehler $
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class BigStepClosureView extends AbstractProofView
{

  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = -8529052541636149376L;


  /**
   * The <code>BigStep</code> component.
   */
  protected BigStepClosureComponent component;


  /**
   * The <code>JScrollPane</code> for the <code>component</code>.
   */
  protected JScrollPane scrollPane;


  /**
   * The <code>JSplitPane</code> for the <code>component</code>.
   */
  private JSplitPane jSplitPane;


  /**
   * The {@link Outline} of this view.
   * 
   * @see #getOutline()
   */
  private Outline outline;


  /**
   * The {@link BigStepClosureProofModel}.
   */
  private BigStepClosureProofModel bigStepClosureProofModel;


  /**
   * Allocates a new <code>BigStepView</code> for the specified
   * {@link BigStepClosureProofModel}.
   * 
   * @param pBigStepClosureProofModel The {@link BigStepClosureProofModel} for the
   *          <code>BigStepView</code>.
   */
  public BigStepClosureView ( BigStepClosureProofModel pBigStepClosureProofModel )
  {
    super ();
    this.bigStepClosureProofModel = pBigStepClosureProofModel;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ();
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT );
    setLayout ( new GridBagLayout () );
    this.scrollPane = new JScrollPane ();
    this.component = new BigStepClosureComponent ( this.bigStepClosureProofModel );
    this.scrollPane.setViewportView ( this.component );
    // this.scrollPane.getViewport ( ).setBackground ( Color.WHITE ) ;
    this.scrollPane.getViewport ().setBackground ( Color.WHITE );
    this.scrollPane.getViewport ().setOpaque ( true );
    this.scrollPane.addComponentListener ( new ComponentAdapter ()
    {

      @Override
      public void componentResized ( @SuppressWarnings ( "unused" )
      ComponentEvent event )
      {
        BigStepClosureView.this.component
            .setAvailableWidth ( BigStepClosureView.this.scrollPane.getViewport ()
                .getWidth () );
      }
    } );
    this.outline = new DefaultOutline ( this );
    this.outline.load ( this.bigStepClosureProofModel.getRoot ().getLastLeaf ()
        .getExpression (), Outline.ExecuteInit.BIGSTEP );
    JPanel jPanelOutline = this.outline.getPanel ();
    this.jSplitPane.setLeftComponent ( this.scrollPane );
    this.jSplitPane.setRightComponent ( jPanelOutline );
    this.jSplitPane.setOneTouchExpandable ( true );
    this.jSplitPane.setResizeWeight ( 0.5 );
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.insets = new Insets ( 0, 0, 0, 0 );
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 10;
    gridBagConstraints.weighty = 10;
    this.add ( this.jSplitPane, gridBagConstraints );
  }


  /**
   * Returns the bigStepProofModel.
   * 
   * @return The bigStepProofModel.
   * @see #bigStepClosureProofModel
   */
  public BigStepClosureProofModel getBigStepProofModel ()
  {
    return this.bigStepClosureProofModel;
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
   * Returns the <code>Outline</code> of this view.
   * 
   * @return The <code>Outline</code> of this view.
   */
  public Outline getOutline ()
  {
    return this.outline;
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
}
