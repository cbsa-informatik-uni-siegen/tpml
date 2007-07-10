package de.unisiegen.tpml.graphics.outline.listener ;


import java.util.ArrayList ;
import javax.swing.event.TreeModelEvent ;
import javax.swing.event.TreeModelListener ;
import de.unisiegen.tpml.core.ExpressionProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeFormula ;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel ;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode ;
import de.unisiegen.tpml.core.typeinference.TypeJudgement ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.subtyping.NewSubTypingView ;


/**
 * Sets the new {@link Expression} in the {@link Outline}, if a node changed.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineTreeModelListener implements TreeModelListener
{
  /**
   * The unused <code>String</code> for the <code>SuppressWarnings</code>.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline = null ;


  /**
   * The {@link ExpressionProofModel}.
   */
  private ExpressionProofModel expressionProofModel = null ;


  /**
   * The {@link TypeInferenceProofModel}.
   */
  private TypeInferenceProofModel typeInferenceProofModel = null ;


  /**
   * The {@link NewSubTypingView}.
   */
  private NewSubTypingView newSubTypingView = null ;


  /**
   * Initializes the {@link OutlineTreeModelListener} with the given
   * {@link Outline} and the {@link ExpressionProofModel}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   * @param pExpressionProofModel The {@link ExpressionProofModel}.
   */
  public OutlineTreeModelListener ( DefaultOutline pDefaultOutline ,
      ExpressionProofModel pExpressionProofModel )
  {
    this.defaultOutline = pDefaultOutline ;
    this.expressionProofModel = pExpressionProofModel ;
  }


  /**
   * Initializes the {@link OutlineTreeModelListener} with the given
   * {@link Outline} and the {@link NewSubTypingView}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   * @param pNewSubTypingView The {@link NewSubTypingView}.
   */
  public OutlineTreeModelListener ( DefaultOutline pDefaultOutline ,
      NewSubTypingView pNewSubTypingView )
  {
    this.defaultOutline = pDefaultOutline ;
    this.newSubTypingView = pNewSubTypingView ;
  }


  /**
   * Initializes the {@link OutlineTreeModelListener} with the given
   * {@link Outline} and the {@link TypeInferenceProofModel}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   * @param pTypeInferenceProofModel The {@link TypeInferenceProofModel}.
   */
  public OutlineTreeModelListener ( DefaultOutline pDefaultOutline ,
      TypeInferenceProofModel pTypeInferenceProofModel )
  {
    this.defaultOutline = pDefaultOutline ;
    this.typeInferenceProofModel = pTypeInferenceProofModel ;
  }


  /**
   * Sets the new {@link Expression} in the {@link Outline}, if a node changed.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   */
  private final void handleEvent ( TreeModelEvent pTreeModelEvent )
  {
    Object source = pTreeModelEvent.getSource ( ) ;
    if ( source instanceof SmallStepProofModel )
    {
      this.defaultOutline.load ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) ,
          Outline.ExecuteAutoChange.SMALLSTEP ) ;
    }
    else if ( source instanceof BigStepProofModel )
    {
      this.defaultOutline.load ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) ,
          Outline.ExecuteAutoChange.BIGSTEP ) ;
    }
    else if ( source instanceof TypeCheckerProofModel )
    {
      this.defaultOutline.load ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) ,
          Outline.ExecuteAutoChange.TYPECHECKER ) ;
    }
    else if ( source instanceof MinimalTypingProofModel )
    {
      this.defaultOutline.load ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) ,
          Outline.ExecuteAutoChange.MINIMALTYPING ) ;
    }
    else if ( source instanceof SubTypingProofModel )
    {
      if ( this.defaultOutline == this.newSubTypingView.getOutline1 ( ) )
      {
        this.defaultOutline.load (
            this.newSubTypingView.getSubTypingProofModel ( ).getRoot ( )
                .getLastLeaf ( ).getType ( ) ,
            Outline.ExecuteAutoChange.SUBTYPING ) ;
      }
      else
      {
        this.defaultOutline.load (
            this.newSubTypingView.getSubTypingProofModel ( ).getRoot ( )
                .getLastLeaf ( ).getType2 ( ) ,
            Outline.ExecuteAutoChange.SUBTYPING ) ;
      }
    }
    else if ( source instanceof TypeInferenceProofModel )
    {
      ArrayList < TypeFormula > list = ( ( TypeInferenceProofNode ) this.typeInferenceProofModel
          .getRoot ( ).getLastLeaf ( ) ).getAllFormulas ( ) ;
      if ( list.size ( ) > 0 )
      {
        TypeFormula typeFormula = list.get ( 0 ) ;
        if ( typeFormula instanceof TypeJudgement )
        {
          this.defaultOutline.load ( ( ( TypeJudgement ) typeFormula )
              .getExpression ( ) , Outline.ExecuteAutoChange.TYPEINFERENCE ) ;
        }
        else if ( typeFormula instanceof TypeEquationTypeInference )
        {
          this.defaultOutline.load (
              ( ( TypeEquationTypeInference ) typeFormula ).getLeft ( ) ,
              Outline.ExecuteAutoChange.TYPEINFERENCE ) ;
        }
      }
    }
  }


  /**
   * Sets the new {@link Expression} in the {@link Outline}, if a node changed.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeNodesChanged(TreeModelEvent)
   */
  public final void treeNodesChanged ( @ SuppressWarnings ( UNUSED )
  TreeModelEvent pTreeModelEvent )
  {
    handleEvent ( pTreeModelEvent ) ;
  }


  /**
   * A node is inserted.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeNodesInserted(TreeModelEvent)
   */
  public final void treeNodesInserted ( @ SuppressWarnings ( UNUSED )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  /**
   * A node is removed.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeNodesRemoved(TreeModelEvent)
   */
  public final void treeNodesRemoved ( @ SuppressWarnings ( UNUSED )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  /**
   * The structure has changed.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeStructureChanged(TreeModelEvent)
   */
  public final void treeStructureChanged ( @ SuppressWarnings ( UNUSED )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }
}
