package de.unisiegen.tpml.graphics.components;


import javax.swing.JMenuItem;

import de.unisiegen.tpml.core.ProofRule;


/**
 * An item that can be added to the menu of the MenuButton.<br>
 * This item provides the entry text of the given rule.<br>
 * 
 * @author marcell
 */

public class MenuRuleItem extends JMenuItem
{

  /**
   * 
   */
  private static final long serialVersionUID = 2904952709239466196L;


  private ProofRule rule;


  public MenuRuleItem ( ProofRule rule )
  {
    super ();
    this.rule = rule;

    init ( this.rule.getName (), null );
  }


  public ProofRule getRule ()
  {
    return this.rule;
  }
}
