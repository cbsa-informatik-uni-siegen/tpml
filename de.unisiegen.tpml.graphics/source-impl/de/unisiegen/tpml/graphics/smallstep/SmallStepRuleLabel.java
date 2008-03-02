package de.unisiegen.tpml.graphics.smallstep;


import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.unisiegen.tpml.core.expressions.Expression;


/**
 * Just a simple Component combining two {@link JLabel}s together. One for the
 * name of the rules with a leading <i>(</i> and and a tailing <i>)</i>. And
 * one for a possible exponent, when the rules have been grouped together. When
 * the exponent is <i>1</i> it will not be shown.<br>
 * <br>
 * The following example will display those two possible renderings.<br>
 * <br>
 * <img src="../../../../../../images/rulelabel1.png" /><br>
 * <img src="../../../../../../images/rulelabel2.png" /><br>
 * <br>
 * But no matter if there is an exponent, that should be displayed, the label
 * for the exponent is always there. This way all rule labels have an equal
 * height. Showing here:<br>
 * <img src="../../../../../../images/rulelabel_scheme.png" /><br>
 * <br>
 * The height, this components need, is always containing the exponent even if
 * it isn't shown at all. It is easier to place them if they have a homogenouse
 * height.
 * 
 * @author marcell
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepView
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepComponent
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepRulesComponent
 */
public class SmallStepRuleLabel extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = 2850026835245921469L;


  /**
   * The exponent number
   */
  private int ruleCount;


  /**
   * The expression that may be associated with this rule.<br>
   * <br>
   * This expression is used to determin the part of the current expression of
   * the {@link SmallStepNodeComponent} that needs to get underlines.
   */
  private Expression stepExpression;


  /**
   * Creates a new SmallStepRuleLabel with a name and a value for the exponent
   * (the ruleCount).
   * 
   * @param ruleName The name of the rule
   * @param ruleCount The number that should be shown in the exponent.
   */
  public SmallStepRuleLabel ( String ruleName, int ruleCount )
  {
    super ();

    this.ruleCount = ruleCount;

    setLayout ( null );

    JLabel ruleLabel = new JLabel ();
    add ( ruleLabel );
    ruleLabel.setText ( "(" + ruleName + ")" ); //$NON-NLS-1$//$NON-NLS-2$

    JLabel exponentLabel = new JLabel ();
    add ( exponentLabel );
    exponentLabel.setText ( "" + ruleCount ); //$NON-NLS-1$

    // scale the font down to 0.75
    Font expFont = exponentLabel.getFont ();
    expFont = expFont.deriveFont ( expFont.getSize2D () * 0.75f );
    exponentLabel.setFont ( expFont );

    Dimension size = null;
    // the exponen will be placed so that 1/2 of the size of the
    // exponent label will appear on top of the
    Dimension ruleSize = ruleLabel.getPreferredSize ();
    Dimension expSize = exponentLabel.getPreferredSize ();
    int top = expSize.height / 2;

    ruleLabel.setBounds ( 0, top, ruleSize.width, ruleSize.height );
    exponentLabel.setBounds ( ruleSize.width, 0, expSize.width, expSize.height );

    size = new Dimension ( ruleSize.width + expSize.width, ruleSize.height
        + top );

    ruleLabel.setVisible ( true );

    if ( ruleCount > 1 )
    {
      exponentLabel.setVisible ( true );
    }
    else
    {
      ruleSize.width -= expSize.width;
      exponentLabel.setVisible ( false );
    }

    this.stepExpression = null;

    // set the size for this component
    setSize ( size );
    setMinimumSize ( size );
    setMaximumSize ( size );
    setPreferredSize ( size );
  }


  /**
   * Returns the number of the exponent
   * 
   * @return TODO
   */
  public int getRuleCount ()
  {
    return this.ruleCount;
  }


  /**
   * Sets a new expression this rule should be associated to.
   * 
   * @param stepExpression
   * @see #stepExpression
   */
  public void setStepExpression ( Expression stepExpression )
  {
    this.stepExpression = stepExpression;
  }


  /**
   * Returns the current {@link #stepExpression}
   * 
   * @return TODO
   */
  public Expression getStepExpression ()
  {
    return this.stepExpression;
  }


  /**
   * Creates a dummy Expression with a placeholder rule name <i>RULE</i> to
   * determine the height.
   * 
   * @return TODO
   */
  public static int getLabelHeight ()
  {

    // just create a label that can calculate the height
    SmallStepRuleLabel l = new SmallStepRuleLabel ( "RULE", 1 ); //$NON-NLS-1$
    return l.getHeight ();
  }

}
