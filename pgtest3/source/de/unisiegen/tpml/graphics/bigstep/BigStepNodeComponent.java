package de.unisiegen.tpml.graphics.bigstep;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.smallstep.SmallStepProofRule;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.components.CompoundExpression;
import de.unisiegen.tpml.graphics.components.MenuButton;
import de.unisiegen.tpml.graphics.components.MenuButtonListener;
import de.unisiegen.tpml.graphics.components.MenuGuessItem;
import de.unisiegen.tpml.graphics.components.MenuGuessTreeItem;
import de.unisiegen.tpml.graphics.components.MenuRuleItem;
import de.unisiegen.tpml.graphics.components.MenuTranslateItem;
import de.unisiegen.tpml.graphics.components.RulesMenu;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;


/**
 * Graphics representation of a {@link BigStepProofNode} <br>
 * <br>
 * A usual look of a node may be given in the following pictur.
 * It shows the second node within the tree of the BigStepper of
 * the Expression:
 * <code>let rec f = lambda x. if x = 0 then 1 else x * f (x-1) in f 3</code><br>
 * <img src="../../../../../../images/bigstepnode.png" /><br>
 * <br>
 * This node is actualy build using 5 components. The following
 * scheme illustrates the layouting of the single components.<br>
 * <img src="../../../../../../images/bigstepnode_scheme.png" /><br>
 * <br>
 * The first rectangle represents the {@link #indexLabel}. The second
 * rectanle represents the {@link #expression}. The third is the 
 * down-directed arrow this is the {@link #downArrowLabel}. The last
 * element in the first row is the {@link #resultExpression} containing
 * the resulting expression, this is not visible until the node is
 * complete evaluated.<br>
 * In the next row there is only one rectangle containing the 
 * rule. In the case of the previous image the {@link #ruleLabel} is shown,
 * but as long as the node has not been evaluated with a rule there would
 * be located the {@link #ruleButton}.<br>
 * The bit of free space between the top and the bottom row aswell as between
 * the indexLabel and the expression is given in pixels in the {@link #spacing}.
 * <br>
 * Within the {@link BigStepComponent} the {@link de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer} will
 * be used to draw the lines and the arrow of the tree. The TreeArrowRenderer
 * uses {@link TreeNodeComponent}s to located the points where the lines and
 * the arrow will be located. Therefore this component implements this 
 * interface. So the method {@link #getLeftArrowConnection()} returns the
 * point marked in red in the scheme and the method {@link #getBottomArrowConnection()}
 * return the point marked in blue. Those points are absolut positions, not relative to
 * this component.<br>
 * <br>
 * The entire layouting or placing of the nodes of this component is
 * done in the method {@link #placeElements(int)}.<br>
 * 
 * 
 * @author marcell
 * 
 * @see de.unisiegen.tpml.graphics.bigstep.BigStepView
 * @see de.unisiegen.tpml.graphics.bigstep.BigStepComponent
 * @see de.unisiegen.tpml.graphics.tree.TreeNodeComponent
 * @see de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer 
 */
public class BigStepNodeComponent extends JComponent implements TreeNodeComponent {

  
  /**
   * 
   */
  private static final long serialVersionUID = -2050381804392542081L;

  /**
   * The {@link BigStepProofModel} that will get used to apply the 
   * actions on.
   */
  private BigStepProofModel                           proofModel;
  
  /**
   * The origin {@link BigStepProofNode}. Contains the information this
   * node gives a graphics representation.
   */
  private BigStepProofNode                            proofNode;
  
  /**
   * Contains the information of the size of the Component.
   * When {@link #placeElements(int)} is called <i>dimension</i> is
   * filled with proppert values.
   */
  private Dimension                                   dimension;
  
  /**
   * Amount of pixels that will be left free between the elements of the
   * node.
   */
  private int                                         spacing;
  
  /**
   * Label that will contain the index at the front.
   */
  private JLabel                                      indexLabel;
  
  /**
   * Component containing the expression and the store.
   */
  private CompoundExpression<Location, Expression>    expression;
  
  /**
   * Label containing the down-directed double-arrow separating the 
   * Expression and the Resul-Expression.
   */
  private JLabel                                      downArrowLabel;
  
  /**
   * Component containing the result-expression and the result-store.
   */
  private CompoundExpression<Location, Expression>    resultExpression;
  
  /**
   * The Button with its DropDownMenu used to perform the userinteraction.
   */
  private MenuButton                                  ruleButton;

  /**
   * Label that will be used to show the evaluated rule.
   */
  private JLabel                                      ruleLabel;
  
  /**
   * The "Translate to core syntax" item in the context menu.
   */
  private MenuTranslateItem                           menuTranslateItem;
  
  /**
   * The translator will be used to determine whether the expression 
   * contains syntactical sugar.
   */
  private LanguageTranslator                          translator;
  
  /**
   * containing the rules
   * it may contain submenus if to many (set in TOMANY) rules are in the popupmenu
   */
  private JPopupMenu menu;
  
  /**
   * until TOMANY rules are in the menu, no submenus will be crated
   */
  private static final int TOMANY = 15;
  
  /**
   * saves the last used elements MAX elements will be saved
   */
  private ArrayList<MenuRuleItem> lastUsedElements = new ArrayList<MenuRuleItem>();
  
  /**
   * defines how many rules will be displayed as last used elements
   */
  private static final int MAX = 10;
  
  /**
   * saves the state of the menu to be able to revert it. It will be used if the selected
   * rule throws an exception. By this rules will not be set at the top of the menu if
   * they are wrong
   */
  private ArrayList <MenuRuleItem> revertMenu = new  ArrayList <MenuRuleItem> ();
 
  /**
   * look at revertMenu
   */
  private ArrayList<MenuRuleItem> revertLastUsedElements = new ArrayList <MenuRuleItem>();;

  /**
   * saves the state of the menu in the preferences to restor at the next start
   */
  private Preferences preferences;

  /**
   * 
   * @param node        The node that should be represented
   * @param model       The model 
   * @param translator  The translator from the model
   */
  public BigStepNodeComponent (BigStepProofNode   node,
                               BigStepProofModel  model,
                               LanguageTranslator translator) {
    super ();
    
    this.proofNode          = node;
    this.proofModel         = model;
    this.translator         = translator;
    
    this.dimension          = new Dimension (0, 0);
    this.spacing            = 10;
    
    
    /*
     * Create and add the components needed to render this node
     */
    this.indexLabel         = new JLabel ();
    add (this.indexLabel);
    
    this.expression         = new CompoundExpression<Location, Expression> ();
    add (this.expression);
    
    // CHANGE CHRISTIAN
    this.indexLabel.addMouseListener ( new OutlineMouseListener ( this.expression ) ) ;
    // CHANGE CHRISTIAN END
     
    this.downArrowLabel     = new JLabel ();

    add (this.downArrowLabel);
    this.downArrowLabel.setText(" \u21d3 "); // \u21d3 is the double arrow down //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
    
    this.resultExpression   = new CompoundExpression<Location, Expression>();
    add (this.resultExpression);
    this.resultExpression.setAlternativeColor(Color.LIGHT_GRAY);
    
    this.ruleButton         = new MenuButton ();
    add (this.ruleButton);
    this.ruleButton.setVisible (true);
    
    this.ruleLabel          = new JLabel ();
    add (this.ruleLabel);
    this.ruleLabel.setVisible(false);
    
    /*
     * Create the PopupMenu for the menu button
     */
//  Fill the menu with menuitems

    ProofRule[] rules = this.proofModel.getRules();
    Language lang = proofModel.getLanguage();

    menu = new JPopupMenu();
    
    //TODO test der neuen klasse
    RulesMenu rm = new RulesMenu();
    menu = rm.getMenu(rules, rules, lang, this, "bigstep" );

    //if to many rules we will devide in menu and submenus, otherwise there will be only seperators 
    //between the rules coming from the different languages
    //if (rules.length > TOMANY)
    if (false)
    {
      if (rules.length > 0)
      {

        //first get the lastUsedRules of the preferences (last state of the programm)

        //get the names from the preferences, compare each with the list of all usable rules, add them
        preferences = Preferences.userNodeForPackage(BigStepNodeComponent.class);
        //backwards to save the ordering
        for (int i = MAX - 1; i >= 0; i--)
        {
          String name = preferences.get("rule" + i, "");
          //TODO Testausgabe
          //System.out.println("Auslesen: rule"+i+" = "+name);

          if (name.equalsIgnoreCase(""))
          {
            // do nothing if the rule has no name, the rule dose not exist
          }
          else
          {
            ProofRule[] allRules = proofModel.getRules();
            for (ProofRule a : allRules)
            {
              //if (new MenuRuleItem(a).getText().equalsIgnoreCase(name))
            	if (new MenuRuleItem(a).getText().equalsIgnoreCase(name))
              {
                //add at the beginning of the list to save the order
            		//TODO Baustelle, Menüs gehen nicht
            		boolean isIn=isIn(name, lastUsedElements);
            		
                if(!isIn) 
                	{
		                lastUsedElements.add(0, new MenuRuleItem(a));
		                	
		                MenuRuleItem tmp = new MenuRuleItem(a);
		                //the actionlistener ist needed to be able to set the position of a selected 
		                //rule
		                ActionListener al = new ActionListener() {
		                  public void actionPerformed(ActionEvent e)
		                  {
		                    //to be able to revert the changes in the menu if the rule throws an exception
		                  	saveToRevert();
		                  	//if the rule is selected it will be moved to the top of the menu
		                  	moveToTop(((MenuRuleItem) e.getSource()).getText(), MAX);
		                  	//save this state of the menu to the preferences
		                  	save();
		                  }
		                };
		                tmp.addActionListener(al);
		                //inset at the top of the meun (the preferences are walked throu 
		                menu.insert(tmp, 0);
                	}
              }
            }
          }
        }
        save();
        

        //build the submenu
        int group = rules[0].getGroup();
        //a seperator ist needed if there are last used elements
        if (lastUsedElements.size() > 0)
        {
          menu.addSeparator();
        }

        // JMenu Smenu=new JMenu(Messages.getString("Language.0")+ " "
        // +rules[0].getGroup());
        JMenu subMenu;
        //Language lang = proofModel.getLanguage();
        
        //the hasmap contains teh names of the languages connected to the group-number
        HashMap <Number,String>names = getLanguageNames(lang);
        subMenu = new JMenu(names.get(rules[0].getGroup()));
        
        // Jede Regel
        for (final ProofRule r : rules)
        {
          //if (((SmallStepProofRule) r).isAxiom() || !advanced)
          {
            if (r.getGroup() != group)
            {
              if (subMenu != null)
              {
                menu.add(subMenu);
              }              
              subMenu = new JMenu(names.get(r.getGroup()));
            }
            MenuRuleItem tmp = new MenuRuleItem(r);
            ActionListener al = new ActionListener() {
              public void actionPerformed(ActionEvent e)
              {
              	//look if the list is full
                if (lastUsedElements.size() < MAX)
                {
                	
                  MenuRuleItem lastUsed = new MenuRuleItem(r);
                  //check if the element is in the list
//                  boolean isIn = false;
//                  for (int i = 0; i < MAX; i++)
//                  {
//                    int schleife = Math.min(MAX, lastUsedElements.size());
//                    for (int j = 0; j < schleife; j++)
//                    {
//                      if (lastUsedElements.get(j).getText().equals(lastUsed.getText()))
//                      {
//                        isIn = true;
//                      }
//                    }
//                  }
                  boolean isIn = isIn (lastUsed.getText(), lastUsedElements);
                  
                  ActionListener al = new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                      //move to to
                    	moveToTop(((MenuRuleItem) e.getSource()).getText(), MAX);
                 
                      //the action must be called manualy if the element is in a submenu
                      //menuItemActivated((JMenuItem) e.getSource());
                    	handleMenuActivated((JMenuItem) e.getSource());
                    }
                  };
                  lastUsed.addActionListener(al);
                  if (!isIn)
                  {
                  	//TODO Testuasgabe
                  	//System.out.println();
                    //System.out.println("Die Liste enthälten den Eintrag bisher: "+isIn);
                  	saveToRevert();
                    menu.insert(lastUsed, 0);
                    lastUsedElements.add(0, lastUsed);
                    //TODO Testausgabe
                    //System.out.println("jetzt sollte sie drin sein! "+isIn(lastUsed.getText(), lastUsedElements));
                  }
                  //may be we want to move it to top
                  // else
                  // {
                  // menu.remove(lastUsed);
                  // last10Elements.remove(lastUsed);
                  // menu.insert(lastUsed,0);
                  // last10Elements.add(0, lastUsed);
                  // }

                  //save the preferences to be able to reorganize
                  save();
                }
                //if the list is allrady full the last element must be removed
                else
                {
                  MenuRuleItem lastUsed = new MenuRuleItem(r);
                  ActionListener al = new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                      moveToTop(((MenuRuleItem) e.getSource()).getText(), MAX);
                      //menuItemActivated((JMenuItem) e.getSource());
                    }
                  };
                  lastUsed.addActionListener(al);
                  boolean isIn = false;
                  for (int i = 0; i < MAX; i++)
                  {
                    //check if it is already in the list
                    int schleife = Math.min(MAX, lastUsedElements.size());
                    for (int j = 0; j < schleife; j++)
                    {
                      if (lastUsedElements.get(j).getText().equals(lastUsed.getText()))
                      {
                        isIn = true;
                      }
                    }
                  }
                  //if it is not in the list it will be added at the top and the last element will be deleted
                  if (!isIn)
                  {
                  	saveToRevert();
                    lastUsedElements.add(0, lastUsed);
                    menu.insert(lastUsed, 0);
                    lastUsedElements.remove(MAX);
                    menu.remove(MAX);
                  }
                  //maybe we want to set it to the top position if it is allrady in the list
                  // else
                  // {
                  // last10Elements.remove(lastUsed);
                  // menu.remove(lastUsed);
                  // last10Elements.add(0, lastUsed );
                  // menu.insert(lastUsed,0);
                  // }
                  //just save it in the preferences to be able to reorganize
                 save();
                }
                
                //the action must be called manualy if the element is in a submenu
                //menuItemActivated((JMenuItem) e.getSource());
                handleMenuActivated((JMenuItem) e.getSource());

              }
            };
            tmp.addActionListener(al);
            subMenu.add(tmp);
            group = r.getGroup();
          }
          menu.add(subMenu);
        }
      }
    }
    //if ther are less than TOMANY rules ther will be no submenus, only seperators
    //with this variable you would also be able to disable the submenufunction
    else if (false)
    {
      if (rules.length > 0)
      {
        int group = rules[0].getGroup();
        for (ProofRule r : rules)
        {
          //if (((SmallStepProofRule) r).isAxiom() || !advanced)
          {
            if (r.getGroup() != group)
            {
              menu.addSeparator();
            }
            menu.add(new MenuRuleItem(r));
            group = r.getGroup();
          }
        }
      }
    }

    //JPopupMenu menu = new JPopupMenu ();
    
    //ProofRule[] rules = this.proofModel.getRules();
    //if (rules.length > 0) {
    //  int group = rules[0].getGroup();
    //  for (ProofRule r : rules) {
    //    if (r.getGroup() != group) {
    //      menu.addSeparator();
    //    }
    //    menu.add(new MenuRuleItem(r));
    //    group = r.getGroup();
    //  }
    //}
    menu.addSeparator();
    menu.add (new MenuGuessItem ());
    menu.add (new MenuGuessTreeItem ());
    menu.add (this.menuTranslateItem = new MenuTranslateItem ());
    
    this.ruleButton.setMenu(menu);

    /*
     * Connect the handling of the ruleButton
     */
    this.ruleButton.addMenuButtonListener(new MenuButtonListener() {
      public void menuClosed (MenuButton button) { } 
      public void menuItemActivated (MenuButton button, final JMenuItem source) {
        // setup a wait cursor for the toplevel ancestor
        final Container toplevel = getTopLevelAncestor();
        final Cursor cursor = toplevel.getCursor();
        toplevel.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        // avoid blocking the popup menu
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            // handle the menu action
            BigStepNodeComponent.this.handleMenuActivated (source);
            
            // wait for the repaint before resetting the cursor
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                // reset the cursor
                toplevel.setCursor(cursor);
              }
            });
          }
        });
      }
    });
    
    changeNode ();
  }
  
  /**
   * Causes the expression and the resultexpression
   * to recalculate their layout.
   *
   */
  public void reset () {
    this.expression.reset();
    this.resultExpression.reset();
  }
  
  /**
   * Adds a new {@link BigStepNodeListener} to the <i>SmallStepNodeComponent</i>
   * 
   * @param listener The listener to be added
   */
  public void addBigStepNodeListener (BigStepNodeListener listener) {
    this.listenerList.add (BigStepNodeListener.class, listener);
  }
  
  /**
   * Removes a {@link BigStepNodeListener} from the <i>SmallStepNodeComponent</i>
   * 
   * @param listener The listener to be removed.
   */
  
  public void removeBigStepNodeListener (BigStepNodeListener listener) {
    this.listenerList.remove(BigStepNodeListener.class, listener);
  }
  
  /**
   * Calls the {@link BigStepNodeListener#nodeChanged(BigStepNodeComponent)} of
   * all listeners.
   */
  private void fireNodeChanged () {
    Object[] listeners = this.listenerList.getListenerList();
    for (int i=0; i<listeners.length; i+=2) {
      if (listeners [i] != BigStepNodeListener.class) {
        continue;
      }
      
      ((BigStepNodeListener)listeners [i+1]).nodeChanged(this);
    }
  }
  
  private void fireRequestJumpToNode (ProofNode node) {
    Object[] listeners = this.listenerList.getListenerList();
    for (int i=0; i<listeners.length; i+=2) {
      if (listeners [i] != BigStepNodeListener.class) {
        continue;
      }
      
      ((BigStepNodeListener)listeners [i+1]).requestJumpToNode(node);
    }
  }
  
  /**
   * Handles the actions that should be done when an item from the MenuButton
   * was selected.<br>
   * <br>
   * Tree possible actions can be done here: 1st Selection of a rule to apply to
   * the node. 2nd Translation of the expression into core syntax. And 3rd Guess
   * of the rules for the current node.<br>
   * <br>
   * If the current outermost expression does not contain any syntactical sugar,
   * the translation will be done directly recersivly on the entire expresion.<br>
   * If the current outermost expression does contain syntactical sugar, a
   * messagebox will be shown whether the translation should be done only on the 
   * outermost expression or if it should be done on the entire expression.
   * 
   * @param item
   */
  public void handleMenuActivated (JMenuItem item) {
    if (item instanceof MenuRuleItem) {
      MenuRuleItem ruleItem = (MenuRuleItem)item;
      ProofRule rule = ruleItem.getRule();
      
      try {
        this.proofModel.prove(rule, this.proofNode);
        this.ruleButton.setToolTipText(null);
      } catch (Throwable e) {
      	//revert the menu
      	revertMenu();
        
        // when the node could not be prooven with the selected
        // rule the menu button gets labeled with the given rule 
        // and will be displayed in red
        this.ruleButton.setText("(" + rule.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        this.ruleButton.setTextColor(Color.RED);
        
        // determine the error message for the tooltip
        this.ruleButton.setToolTipText(e.getMessage());
      }
    }
    else if (item instanceof MenuTranslateItem) {
      int answer = 1;
      if (this.proofModel.containsSyntacticSugar(this.proofNode, false)) {
        String[] answers = { Messages.getString("NodeComponent.0"), Messages.getString("NodeComponent.1"), Messages.getString("NodeComponent.2") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        answer = JOptionPane.showOptionDialog(getTopLevelAncestor(), Messages.getString("NodeComponent.3"), //$NON-NLS-1$
            Messages.getString("NodeComponent.4"), //$NON-NLS-1$
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            answers,
            answers[0]);
      }
      switch (answer) {
      case 0:
        this.proofModel.translateToCoreSyntax(this.proofNode, false);
        break;
      case 1:
        this.proofModel.translateToCoreSyntax(this.proofNode, true);
        break;
      case 2:
        break;
      }
      fireNodeChanged();
    }
    else if (item instanceof MenuGuessItem) {
      try {
        this.proofModel.guess(this.proofNode);
      }
      catch (final ProofGuessException e) {
        fireRequestJumpToNode(e.getNode());
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            JOptionPane.showMessageDialog(getTopLevelAncestor(), MessageFormat.format(Messages.getString("NodeComponent.5"), e.getMessage()), Messages.getString("NodeComponent.6"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
          }
        });
      }
    }
    else if (item instanceof MenuGuessTreeItem) {
      try {
        this.proofModel.complete(this.proofNode);
      }
      catch (final ProofGuessException e) {
        fireRequestJumpToNode(e.getNode());
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            JOptionPane.showMessageDialog(getTopLevelAncestor(), MessageFormat.format(Messages.getString("NodeComponent.7"), e.getMessage()), Messages.getString("NodeComponent.8"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
          }
        });
      }
    }
    
    fireNodeChanged();
    

  }
  
  /**
   * Sets the index of the current node.
   * 
   * @param index 
   */
  public void setIndex (int index) {
    this.indexLabel.setText("(" + index + ")"); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  
  /**
   * Does an update on the compound expression.<br>
   * Resets the expression and the environment (if there is one).
   * That causes the PrettyStringRenderer to recheck the breakoints. 
   */
  public void changeNode () {
    
    this.expression.setExpression (this.proofNode.getExpression());
    
    // only if memory is enabled set the store because the
    // store is always valid
    if (this.proofModel.isMemoryEnabled()) {
      this.expression.setEnvironment (this.proofNode.getStore());
    }
    else {
      this.expression.setEnvironment (null);
    }

    if (this.proofNode.getResult() != null) {
      this.resultExpression.setExpression(this.proofNode.getResult().getValue());
      if (this.proofModel.isMemoryEnabled()) {
        this.resultExpression.setEnvironment(this.proofNode.getResult().getStore());
      }
      else {
        this.resultExpression.setEnvironment(null);
      }
    }
    else {
      this.resultExpression.setExpression(null);
      this.resultExpression.setEnvironment(null);
    }
  }
  
  /**
   * Places all elements of the current node.<br>
   * Just places one element after the other. 1st the index, 2nd the compoundExpression
   * 3rd the double-sidded-down-directed arrow. When the node is proven (that is if there
   * is already an evaluated result, it will be placed behind the arrow.<br>
   * <br>
   * If the the nodes is evaluated the ruleLabel is placed with a bit spacing below the
   * expression. If the node is not evaluated the menuButton is placed at the same size.<br>
   * <br>
   * After the placing is done the {@link #dimension} contains the needed size of this node.
   * 
   * @param maxWidth The maximum width that is available for the current node.
   */
  private void placeElements (int maxWidth) {
    // get the size for the index at the beginning: (x)
    FontMetrics fm = AbstractRenderer.getTextFontMetrics();
    Dimension labelSize = new Dimension (fm.stringWidth(this.indexLabel.getText()), fm.getHeight());
    this.dimension.setSize(labelSize.width, labelSize.height);

    // there will be a bit spacing between the index label and the expression
    this.dimension.width += this.spacing;
    
    // the index shrinkens the max size for the expression
    maxWidth -= labelSize.width;
    
    //  get the needed size for the expression
    //Dimension expSize = this.expression.getNeededSize(maxWidth);
    Dimension expSize = this.expression.getNeededSize(maxWidth-this.dimension.width);
    this.dimension.width += expSize.width;
    this.dimension.height = Math.max(expSize.height, this.dimension.height);

    Dimension arrowSize = this.downArrowLabel.getPreferredSize();
    this.dimension.width += arrowSize.width;
    this.dimension.height = Math.max(arrowSize.height, this.dimension.height);

    // the result should never be wrapped so we use 
    // the Integer.MAX_VALUE to prevent linewrapping
    //wenn die von maxWidht jetzt nicht mehr übrig ist, dann muss umgebrochen werden...
    boolean breakNeeded = false;
    //if the expression is braken the result will be meshed mesPix pixels
    int meshPix=50;
    if (maxWidth-this.dimension.width < 10)
    {
    	breakNeeded = true;
    }
    Dimension resultSize=new Dimension(0,0);
    if (!breakNeeded)
    {
    	resultSize = this.resultExpression.getNeededSize(maxWidth-this.dimension.width);
    	//if the result is higher than the hight of one line the result will be in the next line
    	
    	if (resultSize.height > fm.getHeight())
    	{
    		breakNeeded = true;
    		//TODO Testausgabe
    		//System.out.println("hier");
    	}
    }
    if (breakNeeded)
    {
    	//TODO Testausgabe
    	//System.out.println("Er bircht um!");
    	//we have to calculate the new size
    	//-meshPix gives the pixels to mesh in the new line
//    TODO Testausgabe    	
    	//System.out.println("Bisherige GEsamthöhe: "+this.dimension.height);
    	//System.out.println("Höhe des Ausdrucks: "+expSize.height);
    	resultSize = this.resultExpression.getNeededSize(maxWidth-meshPix);
//    TODO Testausgabe
    	//System.out.println("result hat die Höhe:"+resultSize.height);
    	//if the renderer breaks the expression will be higher
    	this.dimension.height = Math.max ( this.dimension.height, (expSize.height + resultSize.height));

//    TODO Testausgabe
    	//System.out.println("nun ist die Gesamthöhe: "+this.dimension.height);
    }
       
    //this.dimension.width += resultSize.width;
    if (!breakNeeded)
    {
    	this.dimension.width += resultSize.width;
    	this.dimension.height = Math.max(resultSize.height, this.dimension.height);
    }
    else
    {
    	this.dimension.width = Math.max((resultSize.width+meshPix), dimension.width);
    }
        
    // now place the elements
    int posX = 0;
    
    this.indexLabel.setBounds(posX, 0, labelSize.width, this.dimension.height);
    posX += labelSize.width + this.spacing;
    
    //this.expression.setBounds(posX, 0, expSize.width, this.dimension.height);
    this.expression.setBounds(posX, 0, expSize.width, expSize.height);
    posX += expSize.width;
    
    //this.downArrowLabel.setBounds (posX, 0, arrowSize.width, this.dimension.height);
    this.downArrowLabel.setBounds (posX, 0, arrowSize.width, expSize.height);
    posX += arrowSize.width;
    
    if (!breakNeeded)
    {
    	//this.resultExpression.setBounds(posX, 0, resultSize.width, this.dimension.height);	
    	this.resultExpression.setBounds(posX, 0, resultSize.width, resultSize.height);
    }
    else 
    {
//    TODO Testausgabe
    	//System.out.println("Der Umbruch wird vollzogen, das Ergebnis beginnt jetzt: "+(this.dimension.height-resultSize.height)+", die Gesamtgröße ist jetzt: "+this.dimension.height);
    	this.resultExpression.setBounds(meshPix, (this.dimension.height-resultSize.height) , resultSize.width, resultSize.height);
    }
    
    
    
    /*
     * Check whether this is node is evaluated.
     * 
     * If it is evaluated only the Label needs to get placed,
     * if it is not evaluated yet the MenuButton needs to get placed.
     */
    posX = labelSize.width + this.spacing;
    
    if (this.proofNode.getRule() != null) {
      this.ruleLabel.setText ("(" + this.proofNode.getRule() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
      Dimension ruleLabelSize = this.ruleLabel.getPreferredSize();
      
      this.ruleLabel.setBounds(posX, this.dimension.height + this.spacing, ruleLabelSize.width, ruleLabelSize.height);
      
      this.dimension.height += this.spacing + ruleLabelSize.height;
      this.dimension.width = Math.max(this.dimension.width, ruleLabelSize.width + posX);
      
      //  display only the label not the button
      this.ruleLabel.setVisible (true);
      this.ruleButton.setVisible (false);
    }
    else {
      // place the menu button
      Dimension buttonSize = this.ruleButton.getNeededSize();
      this.ruleButton.setBounds(posX, this.dimension.height + this.spacing, buttonSize.width, buttonSize.height);
      
      this.dimension.height += this.spacing + buttonSize.height;
      this.dimension.width = Math.max(this.dimension.width, buttonSize.width + posX);
      
      // display only the button not the label
      this.ruleLabel.setVisible (false);
      this.ruleButton.setVisible (true);
    }

  }
  
  /* 
   * Implementation of the TreeNodeComponent interface
   */
  public Dimension update(int maxWidth) {
    placeElements (maxWidth);
    this.menuTranslateItem.setEnabled(this.translator.containsSyntacticSugar(this.proofNode.getExpression(), true));
    
    return this.dimension;

  }
  
  /**
   * Returns the point at the bottom of the node where
   * the layout should attach the arrow.
   */
  public Point getBottomArrowConnection() {
    //return new Point (this.getX() + this.indexLabel.getWidth() / 2, this.getY() + this.indexLabel.getHeight());
  	return new Point (this.getX() + this.indexLabel.getWidth() / 2, this.getY() +  (this.dimension.height/2) );
  }

  /**
   * Returns the point at the left of the node where
   * the layout should attach the line to its parent.
   */
  public Point getLeftArrowConnection() {
    return new Point (this.getX (), this.getY() + this.indexLabel.getY() + this.indexLabel.getHeight() / 2);
  }


  /**
   * Returns the number of pixels the children should be displayed 
   * indentated.
   */
  public int getIndentationWidth() {
    // XXX: calculate the indentation
    return this.indexLabel.getWidth();
  }
  

  /**
   * Just calls setBounds of the super class.
   */
  @Override
  public void setBounds (int x, int y, int width, int height) {
    super.setBounds (x, y, width, height);
  }
  
  /**
   * gets the names of the languages connected to the group of all languages including the given
   * language and every extended one 
   *
   * @param language
   * 						the language of wich the group should start
   * @return		the HashMap containing the LanguageName and the group
   */
  private HashMap getLanguageNames(Language language)
	{
		HashMap <Number,String> result = new HashMap<Number,String>();
		while ( language.getId ( ) > 0 )
    {
      //System.out.println ( language.getId ( ) + " " + language.getName ( ) ) ;
      result.put(language.getId ( ), language.getName ( ));
      try
      {
        language = ( Language ) language.getClass ( ).getSuperclass ( )
            .newInstance ( ) ;
      }
      catch ( InstantiationException e )
      {
        // Do nothing
      }
      catch ( IllegalAccessException e )
      {
        // Do nothing
      }
    }
    try
    {
      language = ( Language ) language.getClass ( ).getSuperclass ( )
          .newInstance ( ) ;
    }
    catch ( InstantiationException e )
    {
      // Do nothing
    }
    catch ( IllegalAccessException e )
    {
      // Do nothing
    }
    //System.out.println ( language.getId ( ) + " " + language.getName ( ) ) ;
    result.put(language.getId ( ), language.getName ( ));
		
		return result;
	}

	/**
   * Moves the first element of the menu to top, corresponding to the label.
   * only elements from 0 to max will be recognized.
   *
   * @param label
   * 					the label of the element should be moved
   * @param max
   * 					the index of the last element should be moved.
   */
  private void moveToTop(String label, int max)
	{
		for (int i = 0; i < max; i++)
    {
      try
      {
        MenuRuleItem toCompare = (MenuRuleItem) menu.getComponent(i);
        MenuRuleItem tmp2 = lastUsedElements.get(i);
        // vergleiche die Namen, wenn sie übereinstimmen
        if (toCompare.getText().equals(label))
        {
          //System.out.println("wieder nach oben!");
          // nach oeben schieben
          menu.add(menu.getComponent(i), 0);
          // die anderen sind uninteressant, wenn wir einen
          // Treffer hatten
          //break;
        }
        
        if (tmp2.getText().equals(label))
        {
        	lastUsedElements.remove(i);
        	lastUsedElements.add(0, tmp2);        	
        } 
      }
      catch (ClassCastException ex)
      {
        // Sollte eigentlich nie ausgeführt werden...
      	//System.out.println("NEIN!");
      }
      save();
    }
	}

	/**
	 * saves the state of the menu (last 10 elements) to the windows regestry
	 *
	 */
	private void save()
	{
		for (int i = 0; i < lastUsedElements.size(); i++)
    {
      //TODO Testausgabe
			//System.out.println("rule"+i+" = "+lastUsedElements.get(i).getText());
      preferences.put("rule" + i, lastUsedElements.get(i).getText());
    }
	}
	
	/**
	 * saves the state of the menu to be able to revert changes
	 *
	 */
	private void saveToRevert()
	{
		//als erstes das Menü durchlaufen und in die Liste packen
		if (revertMenu.size()>0)
			{
				revertMenu.clear();
			}
		
		boolean isRuleItem = false;
		
		int i = 0;
		
		if (menu.getComponent(i) instanceof MenuRuleItem)
		{
			isRuleItem = true;
		}
		
		while (isRuleItem)
		{
			revertMenu.add(i, (MenuRuleItem)menu.getComponent(i));
			i++;
			if (menu.getComponent(i) instanceof MenuRuleItem)
			{
				isRuleItem = true;
			}
			else
			{
				isRuleItem = false;
			}
		}
		
		//jetzt die Last10Elements sichern
		revertLastUsedElements.clear();
		revertLastUsedElements.addAll(lastUsedElements);
	}
	
	/**
	 * reverts the changes in the menu
	 *
	 */
	private void revertMenu()
	{
		//TODO Testausgabe
		//System.out.println();
    //System.out.println();
		//System.out.println("Revert ist aufgerufen!");
		//als erstes das Menü von den Einträgen befreien
		boolean isRuleItem = false;
		
		int i = 0;
		
		if (menu.getComponent(i) instanceof MenuRuleItem)
		{
			isRuleItem = true;
		}
		
		while (isRuleItem)
		{
			menu.remove(i);
			if (menu.getComponent(i) instanceof MenuRuleItem)
			{
				isRuleItem = true;
			}
			else
			{
				isRuleItem = false;
			}
		}
		
		//Die Einträge wieder hinzufügen
		for (i=0; i<revertMenu.size(); i++)
		{
			menu.insert(revertMenu.get(i),i);
		}
		
		//last10Elements zurücksetzen
		lastUsedElements.clear();
		lastUsedElements.addAll(revertLastUsedElements);
	}
	
	private boolean isIn ( String label, ArrayList list )
	{
		boolean isIn = false;
		//TODO Testausgabe
		//System.out.println("in der Liste sind...");
		for (int l = 0; l<list.size(); l++)
		{
			//TODO Testausgabe
			//System.out.print(lastUsedElements.get(l).getText()+", ");
			if (label.equalsIgnoreCase(lastUsedElements.get(l).getText()))
			{
				isIn=true;
			}
		}
		return isIn;
	}

}
