package de.unisiegen.tpml.graphics.typeinference;


import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.unisiegen.tpml.core.expressions.Expression;


/**
 * Just a simple Component showing the name of the rule in braces. <br>
 * It is nearly the same as the SmallStepRuleLabel but without an exponent.
 * 
 * @author michael
 * @version $Id$
 */
public class TypeInferenceRuleLabel extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = 2850026835245921469L;


  /**
   * The expression that may be associated with this rule.<br>
   * <br>
   * This expression is used to determin the part of the current expression of
   * the {@link TypeInferenceNodeComponent} that needs to get underlines.
   */
  private Expression stepExpression;


  /**
   * Creates a new TypeInferenceRuleLabel with a name and a value for the
   * exponent (the ruleCount).
   * 
   * @param ruleName The name of the rule
   * @param ruleCount The number that should be shown in the exponent.
   */
  public TypeInferenceRuleLabel ( String ruleName,
      @SuppressWarnings ( "unused" )
      int ruleCount )
  {
    super ();

    // this.ruleCount = ruleCount;

    setLayout ( null );

    JLabel ruleLabel = new JLabel ();
    add ( ruleLabel );
    ruleLabel.setText ( "(" + ruleName + ")" ); //$NON-NLS-1$//$NON-NLS-2$

    Dimension size = null;

    Dimension ruleSize = ruleLabel.getPreferredSize ();

    ruleLabel.setBounds ( 0, 0, ruleSize.width, ruleSize.height );

    size = new Dimension ( ruleSize.width, ruleSize.height );

    ruleLabel.setVisible ( true );

    this.stepExpression = null;

    // set the size for this component
    setSize ( size );
    setMinimumSize ( size );
    setMaximumSize ( size );
    setPreferredSize ( size );
  }


  /**
   * Sets a new expression this rule should be associated to.
   * 
   * @param pStepExpression
   * @see #stepExpression
   */
  public void setStepExpression ( Expression pStepExpression )
  {
    this.stepExpression = pStepExpression;
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
    TypeInferenceRuleLabel l = new TypeInferenceRuleLabel ( "RULE", 1 ); //$NON-NLS-1$
    return l.getHeight ();
  }
}
