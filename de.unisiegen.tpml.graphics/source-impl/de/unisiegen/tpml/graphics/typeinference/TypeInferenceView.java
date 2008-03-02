package de.unisiegen.tpml.graphics.typeinference;


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
import de.unisiegen.tpml.core.typeinference.TypeFormula;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.core.typeinference.TypeJudgement;
import de.unisiegen.tpml.graphics.AbstractProofView;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the typeinference interpreter user interface.
 * 
 * @author michael
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 * @see de.unisiegen.tpml.graphics.typeinference.TypeInferenceComponent
 */
public class TypeInferenceView extends AbstractProofView
{

  //
  // Constants
  //
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = -8529052541636149376L;


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


  //
  // Attributes
  //
  /**
   * The TypeInferenceComponent.
   */
  private TypeInferenceComponent component;


  /**
   * The scroll pane for the <code>component</code>.
   */
  private JScrollPane scrollPane;


  /**
   * The {@link TypeInferenceProofModel}.
   */
  private TypeInferenceProofModel typeInferenceProofModel;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>TypeInferenceView</code> for the specified
   * <code>model</code>.
   * 
   * @param pTypeInferenceProofModel the proof model for the TypeInferenceView.
   * @throws NullPointerException if <code>model</code> is <code>null</code>.
   */
  public TypeInferenceView ( TypeInferenceProofModel pTypeInferenceProofModel )
  {
    if ( pTypeInferenceProofModel == null )
    {
      throw new NullPointerException ( "model is null" ); //$NON-NLS-1$
    }
    this.typeInferenceProofModel = pTypeInferenceProofModel;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ();
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT );
    setLayout ( new GridBagLayout () );
    this.scrollPane = new JScrollPane ();
    this.component = new TypeInferenceComponent ( this.typeInferenceProofModel,
        isAdvanced () );
    this.scrollPane.setViewportView ( this.component );
    this.scrollPane.getViewport ().setBackground ( Color.WHITE );
    this.scrollPane.addComponentListener ( new ComponentAdapter ()
    {

      @SuppressWarnings ( "synthetic-access" )
      @Override
      public void componentResized ( @SuppressWarnings ( "unused" )
      ComponentEvent event )
      {
        TypeInferenceView.this.component
            .setAvailableWidth ( TypeInferenceView.this.scrollPane
                .getViewport ().getWidth () );
      }
    } );
    this.outline = new DefaultOutline ( this );
    TypeFormula t = ( ( TypeInferenceProofNode ) this.typeInferenceProofModel
        .getRoot ().getLastLeaf () ).getAllFormulas ().get ( 0 );
    if ( t instanceof TypeJudgement )
    {
      TypeJudgement typeJudgement = ( TypeJudgement ) t;
      this.outline.load ( typeJudgement.getExpression (),
          Outline.ExecuteInit.TYPEINFERENCE );
    }
    JPanel jPanelOutline = this.outline.getPanel ();
    jPanelOutline.getPreferredSize ().getHeight ();
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
   * Returns the {@link Outline} of this view.
   * 
   * @return The {@link Outline} of this view.
   */
  public Outline getOutline ()
  {
    return this.outline;
  }


  /**
   * Returns the typeInferenceProofModel.
   * 
   * @return The typeInferenceProofModel.
   * @see #typeInferenceProofModel
   */
  public TypeInferenceProofModel getTypeInferenceProofModel ()
  {
    return this.typeInferenceProofModel;
  }


  //
  // Primitives
  //
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
