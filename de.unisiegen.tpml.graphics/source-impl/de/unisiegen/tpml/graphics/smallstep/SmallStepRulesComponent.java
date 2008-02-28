package de.unisiegen.tpml.graphics.smallstep;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.smallstep.SmallStepProofNode;
import de.unisiegen.tpml.core.smallstep.SmallStepProofRule;
import de.unisiegen.tpml.graphics.components.MenuButton;


/**
 * Component containing the RuleItems.<br>
 * <br>
 * The Rule items of a <i>SmallStepRulesComponnent</i> are
 * {@link SmallStepRuleLabel}s showing the rules already applied to the node, a
 * {@link MenuButton} showing a rule that has not yet been evaluated and the
 * arrow separating the <i>MetaRules</i> from the <i>AxiomRule</i>.<br>
 * <br>
 * The following image demonstrates a usual appearance of a
 * <code>SmallStepRulesComponent</code> with a few <i>MetaRules</i> (two of
 * the them are grouped together) and the applied <i>AxiomRule</i>:<br>
 * <br>
 * <img src="../../../../../../images/rulescomponent.png" /><br>
 * <br>
 * In the following image you just can see the boundings of all rule labels.<br>
 * <img src="../../../../../../images/rulescomponent_scheme.png" /><br>
 * <br>
 * There is no <code>spacing/code> between the labels. But there is the 
 * an amount of {@link #spacing} between the centering line
 * (the one of the arrow) and the bottom of the <i>MetaRules</i>
 * and the top of the <i>AxiomRule</i>.<br>
 * <br> 
 * The imported information about the layout on this 
 * is shown in {@link #getNeededSize(MouseMotionAdapter)}. The calling
 * of this method not only returns the needed space for this component
 * it also arranges the placing of all labels within it.<br>
 * <br>
 * <b><code>IMPORTANT:</code></b> When assigned space for this component by calling
 * {@link java.awt.Component#setBounds(int, int, int, int)} the vertical alignment
 * must be done manualy. <b>This component is always top-aligned</br>.
 * <br>
 * 
 *  
 * @author marcell
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepView
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepComponent
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepRuleLabel
 */
public class SmallStepRulesComponent extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = -6291688672308787914L;


  /** 
   *
   */
  private SmallStepProofNode proofNode;


  /**
   * The list of {@link SmallStepRuleLabel}s containing the information of all
   * rules that have been evaluated.
   */
  private LinkedList < SmallStepRuleLabel > ruleLabels;


  /**
   * The {@link MenuButton} for the Rule(s) that have not yet been evaluated.
   */
  private MenuButton menuButton;


  /**
   * The spacing in pixels between the labels/menuButton itself and between the
   * labales/menuButton and the arrow.
   */
  private int spacing;


  /**
   * The size in pixels for the arrowhead.
   */
  private int arrowSize;


  /**
   * After the layouting of the {@link SmallStepComponent} the size contains the
   * actual size of this component.
   */
  private Dimension size;


  /**
   * Create a new {@link SmallStepRulesComponent} for the given proofNode.<br>
   * <br>
   * The {@link #menuButton} is already created but not yet shown.
   * 
   * @param proofNode The {@link SmallStepProofNode} containing the origin
   *          informations.
   */
  public SmallStepRulesComponent ( SmallStepProofNode proofNode )
  {
    this.proofNode = proofNode;

    this.ruleLabels = new LinkedList < SmallStepRuleLabel > ();

    this.menuButton = new MenuButton ();
    add ( this.menuButton );
    this.menuButton.setVisible ( true );

    this.spacing = 6;
    this.arrowSize = 6;

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
    this.menuButton.setText ( "(" + rule.getName () + ")" );
    this.menuButton.setTextColor ( Color.RED );
  }


  /**
   * Resets the name and the color (Black) of the {@link #menuButton}.
   */
  public void setRightRule ()
  {
    this.menuButton.setText ( "" );
    this.menuButton.setTextColor ( Color.BLACK );
  }


  /**
   * Sets the actual width for the component.<br>
   * <br>
   * The actual width comes directly from the {@link SmallStepComponent} and is
   * the maximum width of all rule components within the entire proofTree.
   * 
   * @param width
   */
  public void setActualWidth ( int width )
  {
    Dimension size = new Dimension ( width, this.size.height );

    setSize ( size );
    setPreferredSize ( size );
    setMinimumSize ( size );
    setMaximumSize ( size );
  }


  /**
   * Places all {@link #ruleLabels} and the {@link #menuButton} and returns the
   * minimum needed size.<br>
   * <br>
   * First of this function all currently added labels will be removed and
   * theire mouse motion adapter removed aswell. So then we have a clean
   * component. <br>
   * This functions iterates through all steps and just places them. The
   * <i>MetaRule</i>s will be placed on top of the arrow-beam and the
   * <i>AxiomRule</i> will be placed below.<br>
   * If the currently evaluated steps don't end with an <i>AxiomRule</i> the
   * {@link #menuButton} will be placed behind the last <i>MetaRule</i> above
   * the arrow-beam. When it ends with an <i>AxiomRule</i> it will be placed at
   * the beginning of the row below the arrow-beam.<br>
   * If there is more than one Rule of a kind directly folowing another, those
   * Rules will be grouped together. The number of rules beeing grouped together
   * is shown in the exponent. See {@link SmallStepRuleLabel} for this.<br>
   * <br>
   * Every item (except the grouped items) in this component gets added the
   * given adapter to determin in the {@link SmallStepComponent} what rule is
   * below the mouse, so the corresponding expression could be underlined.
   * 
   * @param adapter
   * @return
   */
  public Dimension getNeededSize ( MouseMotionAdapter adapter )
  {
    int labelHeight = Math.max ( SmallStepRuleLabel.getLabelHeight (),
        this.menuButton.getHeight () );

    int neededHeight = 2 * labelHeight + 2 * this.spacing + this.arrowSize;
    int centerV = neededHeight / 2;
    this.size = new Dimension ( 0, neededHeight );

    // clear all the labels that are currently
    for ( SmallStepRuleLabel l : this.ruleLabels )
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
          SmallStepRuleLabel label = new SmallStepRuleLabel ( step.getRule ()
              .getName (), count );
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

      if ( ( ( SmallStepProofRule ) step.getRule () ).isAxiom () )
      {
        SmallStepRuleLabel label = new SmallStepRuleLabel ( step.getRule ()
            .getName (), count );
        label.addMouseMotionListener ( adapter );
        if ( count == 1 )
        {
          label.setStepExpression ( step.getExpression () );
        }

        // add the label to the gui and to the list of all labels
        add ( label );
        this.ruleLabels.add ( label );

        Dimension labelSize = label.getPreferredSize ();

        // put the label with a bit spacing at the bottom of the centering line
        label.setBounds ( 0, centerV + this.spacing, labelSize.width,
            labelSize.height );

        this.size.width = Math.max ( this.size.width, labelSize.width );

        // we are through with this rulepack so its ot needed to display the
        // menuButton
        this.menuButton.setVisible ( false );
      }
      else
      {
        SmallStepRuleLabel label = new SmallStepRuleLabel ( step.getRule ()
            .getName (), count );
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

        // this rulepack doesn't end with an axiom rule so we need to display
        // the menuButton
        Dimension buttonSize = this.menuButton.getNeededSize ();
        this.menuButton.setBounds ( this.size.width, centerV - this.spacing
            - buttonSize.height, buttonSize.width, buttonSize.height );
        this.menuButton.setVisible ( true );
        this.size.width += buttonSize.width;
      }
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

    this.size.width += this.arrowSize * 2;
    return this.size;
  }


  /**
   * Just paints the arrow; all other components draw themself.
   */
  @Override
  protected void paintComponent ( Graphics gc )
  {
    int centerV = getHeight () / 2;

    gc.setColor ( Color.BLACK );
    gc.drawLine ( 0, centerV, getWidth () - 1, centerV );
    gc.drawLine ( getWidth () - 1, centerV, getWidth () - 1 - this.arrowSize,
        centerV - this.arrowSize );
    gc.drawLine ( getWidth () - 1, centerV, getWidth () - 1 - this.arrowSize,
        centerV + this.arrowSize );
  }

}
