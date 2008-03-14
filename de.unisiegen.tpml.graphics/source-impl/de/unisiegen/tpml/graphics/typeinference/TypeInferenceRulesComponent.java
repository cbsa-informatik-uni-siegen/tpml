package de.unisiegen.tpml.graphics.typeinference;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.graphics.components.MenuButton;


/**
 * Component containing the RuleItems.<br>
 * <br>
 * The Rule items of a <i>TypeInferenceRulesComponnent</i> are
 * {@link TypeInferenceRuleLabel}s showing the rules already applied to the
 * node, a {@link MenuButton} showing a rule that has not yet been evaluated.
 * <br>
 * The following image demonstrates a usual appearance of a
 * <code>TypeInferenceRulesComponent</code>. <br>
 * <img src="../../../../../../images/rulescomponent_typei.png" /><br>
 * <br>
 * It is nearly the same as the RulesComponent of the small step view but
 * without grouped sub-rules and with an equals sign instead of the arrow. (@see
 * de.unisiegen.tpml.graphics.smallstep.SmallStepRulesComponent <br>
 * <br>
 * The imported information about the layout on this is shown in
 * {@link #getNeededSize(MouseMotionAdapter)}. The calling of this method not
 * only returns the needed space for this component it also arranges the placing
 * of all labels within it.<br>
 * <br>
 * <b><code>IMPORTANT:</code></b> When assigned space for this component by
 * calling {@link java.awt.Component#setBounds(int, int, int, int)} the vertical
 * alignment must be done manualy. <b>This component is always top-aligned</br>.
 * <br>
 * 
 * @author michael
 * @version $Id$
 */
public class TypeInferenceRulesComponent extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = -6291688672308787914L;


  /** 
   *
   */
  private TypeInferenceProofNode proofNode;


  /**
   * The list of {@link TypeInferenceRuleLabel}s containing the information of
   * all rules that have been evaluated.
   */
  private LinkedList < TypeInferenceRuleLabel > ruleLabels;


  /**
   * The {@link MenuButton} for the Rule(s) that have not yet been evaluated.
   */
  private MenuButton menuButton;


  /**
   * The spacing in pixels between the labels/menuButton itself and between the
   * labales/menuButton and the equal sign.
   */
  private int spacing;


  /**
   * The size in pixels for the equal sign .
   */
  private int equalSignHight;


  /**
   * The size in pixels for the equal sign .
   */
  private int equalSignWidth;


  /**
   * After the layouting of the {@link TypeInferenceComponent} the size contains
   * the actual size of this component.
   */
  private Dimension size;


  /**
   * Create a new {@link TypeInferenceRulesComponent} for the given proofNode.<br>
   * <br>
   * The {@link #menuButton} is already created but not yet shown.
   * 
   * @param pProofNode The {@link TypeInferenceProofNode} containing the origin
   *          informations.
   */
  public TypeInferenceRulesComponent ( TypeInferenceProofNode pProofNode )
  {
    this.proofNode = pProofNode;

    this.ruleLabels = new LinkedList < TypeInferenceRuleLabel > ();

    this.menuButton = new MenuButton ();
    add ( this.menuButton );
    this.menuButton.setVisible ( true );

    this.spacing = 6;
    this.equalSignHight = 4;
    this.equalSignWidth = 13;

  }


  /**
   * Returns the {@link #menuButton}
   * 
   * @return The menuButton.
   */
  public MenuButton getMenuButton ()
  {
    return this.menuButton;
  }


  /**
   * Causes the name of the given rule be set for the text of the
   * {@link #menuButton} and the test will be shown in red.
   * 
   * @param rule The wrong rule.
   */
  public void setWrongRule ( ProofRule rule )
  {
    this.menuButton.setText ( "(" + rule.getName () + ")" ); //$NON-NLS-1$//$NON-NLS-2$
    this.menuButton.setTextColor ( Color.RED );
  }


  /**
   * Resets the name and the color (Black) of the {@link #menuButton}.
   */
  public void setRightRule ()
  {
    this.menuButton.setText ( "" ); //$NON-NLS-1$
    this.menuButton.setTextColor ( Color.BLACK );
  }


  /**
   * Sets the actual width for the component.<br>
   * <br>
   * The actual width comes directly from the {@link TypeInferenceComponent} and
   * is the maximum width of all rule components within the entire proofTree.
   * 
   * @param width
   */
  public void setActualWidth ( int width )
  {
    Dimension actualSize = new Dimension ( width, this.size.height );

    setSize ( actualSize );
    setPreferredSize ( actualSize );
    setMinimumSize ( actualSize );
    setMaximumSize ( actualSize );
  }


  /**
   * Places all {@link #ruleLabels} and the {@link #menuButton} and returns the
   * minimum needed size.<br>
   * <br>
   * First of this function all currently added labels will be removed and
   * theire mouse motion adapter removed aswell. So then we have a clean
   * component. <br>
   * This functions iterates through all steps and just places them. The rules
   * will be placed on top of the euqals sign and the.<br>
   * The {@link #menuButton} will be placed above the uequlas sign like the
   * {@link #menuButton} is placed in the SmallStepRulesComponent.
   * 
   * @param adapter
   * @return TODO
   */
  public Dimension getNeededSize ( MouseMotionAdapter adapter )
  {
    int labelHeight = Math.max ( TypeInferenceRuleLabel.getLabelHeight (),
        this.menuButton.getHeight () );

    int neededHeight = 2 * labelHeight + 2 * this.spacing + this.equalSignWidth;
    int centerV = neededHeight / 2;
    this.size = new Dimension ( 0, neededHeight );

    // clear all the labels that are currently
    for ( TypeInferenceRuleLabel l : this.ruleLabels )
    {
      l.removeMouseMotionListener ( adapter );
      remove ( l );
    }
    this.ruleLabels.clear ();

    ProofStep [] steps = this.proofNode.getSteps ();
    if ( steps.length > 0 )
    {

      // first reference rule will be the first node
      ProofStep step = steps [ 0 ];
      int count = 1;

      for ( int i = 1 ; i < steps.length ; i++ )
      {
        ProofStep cStep = steps [ i ];

        // when the next rule is of the same type just increment the
        // counter and wait until a different rule comes
        if ( cStep.getRule ().equals ( step.getRule () ) )
        {
          ++count;
        }
        else
        {
          TypeInferenceRuleLabel label = new TypeInferenceRuleLabel ( step
              .getRule ().getName (), count );
          label.addMouseMotionListener ( adapter );
          if ( count == 1 )
          {
            label.setStepExpression ( step.getExpression () );
          }

          // add the label to the gui and to the list of all labels
          add ( label );
          this.ruleLabels.add ( label );

          Dimension labelSize = label.getPreferredSize ();

          // put the label with a bit spacing on top of the centering line
          label.setBounds ( this.size.width, centerV - labelSize.height
              - this.spacing, labelSize.width, labelSize.height );
          this.size.width += labelSize.width;

          // the actual node this the new reference node
          step = cStep;
          count = 1;
        }
      }

      TypeInferenceRuleLabel label = new TypeInferenceRuleLabel ( step
          .getRule ().getName (), count );
      label.addMouseMotionListener ( adapter );
      if ( count == 1 )
      {
        label.setStepExpression ( step.getExpression () );
      }

      // add the label to the gui and to the list of all labels
      add ( label );
      this.ruleLabels.add ( label );

      Dimension labelSize = label.getPreferredSize ();
      label.setBounds ( this.size.width, centerV - labelSize.height
          - this.spacing, labelSize.width, labelSize.height );
      this.size.width += labelSize.width;

      // this rulepack doesn't end with an axiom rule so we need to display the
      // menuButton
      Dimension buttonSize = this.menuButton.getNeededSize ();
      this.menuButton.setBounds ( this.size.width, centerV - this.spacing
          - buttonSize.height, buttonSize.width, buttonSize.height );
      this.menuButton.setVisible ( false );
      this.size.width += buttonSize.width;

    }
    else
    {
      // no rule at all evaluated
      Dimension buttonSize = this.menuButton.getNeededSize ();
      this.menuButton.setBounds ( this.size.width, centerV - this.spacing
          - buttonSize.height, buttonSize.width, buttonSize.height );
      this.menuButton.setVisible ( true );
      this.size.width += buttonSize.width;
    }

    this.size.width += this.equalSignWidth * 2;
    return this.size;
  }


  /**
   * Just paints the equals sign; all other components draw themself.
   */
  @Override
  protected void paintComponent ( Graphics gc )
  {
    // go to the center
    int centerV = getHeight () / 2;

    gc.setColor ( Color.BLACK );
    gc.drawLine ( 0, centerV - this.equalSignHight / 2, this.equalSignWidth,
        centerV - this.equalSignHight / 2 );
    gc.drawLine ( 0, centerV + this.equalSignHight / 2, this.equalSignWidth,
        centerV + this.equalSignHight / 2 );
  }

}
