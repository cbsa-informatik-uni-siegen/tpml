package de.unisiegen.tpml.graphics.bigstepclosure;


import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofModel;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.bigstep.BigStepNodeListener;
import de.unisiegen.tpml.graphics.components.CompoundExpressionBigStepClosure;
import de.unisiegen.tpml.graphics.components.MenuButton;
import de.unisiegen.tpml.graphics.components.MenuButtonListener;
import de.unisiegen.tpml.graphics.components.MenuGuessItem;
import de.unisiegen.tpml.graphics.components.MenuGuessTreeItem;
import de.unisiegen.tpml.graphics.components.MenuRuleItem;
import de.unisiegen.tpml.graphics.components.MenuTranslateItem;
import de.unisiegen.tpml.graphics.components.RulesMenu;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;


/**
 * Graphics representation of a {@link BigStepClosureProofNode} <br>
 * <br>
 * A usual look of a node may be given in the following picture. It shows the
 * second node within the tree of the BigStepper of the Expression:
 * <code>let rec f = lambda x. if x = 0 then 1 else x * f (x-1) in f 3</code><br>
 * <img src="../../../../../../images/bigstepnode.png" /><br>
 * <br>
 * This node is actualy build using 5 components. The following scheme
 * illustrates the layouting of the single components.<br>
 * <img src="../../../../../../images/bigstepnode_scheme.png" /><br>
 * <br>
 * The first rectangle represents the {@link #indexLabel}. The second rectangle
 * represents the {@link #compoundExpression}. The third is the down-directed
 * arrow this is the {@link #downArrowLabel}. The last element in the first row
 * is the {@link #resultCompoundExpression} containing the resulting expression,
 * this is not visible until the node is complete evaluated.<br>
 * In the next row there is only one rectangle containing the rule. In the case
 * of the previous image the {@link #ruleLabel} is shown, but as long as the
 * node has not been evaluated with a rule there would be located the
 * {@link #ruleButton}.<br>
 * The bit of free space between the top and the bottom row aswell as between
 * the indexLabel and the expression is given in pixels in the {@link #spacing}. <br>
 * Within the {@link BigStepClosureComponent} the
 * {@link de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer} will be used to
 * draw the lines and the arrow of the tree. The TreeArrowRenderer uses
 * {@link TreeNodeComponent}s to located the points where the lines and the
 * arrow will be located. Therefore this component implements this interface. So
 * the method {@link #getLeftArrowConnection()} returns the point marked in red
 * in the scheme and the method {@link #getBottomArrowConnection()} return the
 * point marked in blue. Those points are absolut positions, not relative to
 * this component.<br>
 * <br>
 * The entire layouting or placing of the nodes of this component is done in the
 * method {@link #placeElements(int)}.<br>
 * 
 * @author marcell
 * @version $Id: BigStepNodeComponent.java 2796 2008-03-14 19:13:11Z fehler $
 * @see de.unisiegen.tpml.graphics.bigstepclosure.BigStepClosureView
 * @see de.unisiegen.tpml.graphics.bigstepclosure.BigStepClosureComponent
 * @see de.unisiegen.tpml.graphics.tree.TreeNodeComponent
 * @see de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer
 */
public class BigStepClosureNodeComponent extends JComponent implements
    TreeNodeComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = -2050381804392542081L;


  /**
   * The {@link BigStepClosureProofModel} that will get used to apply the
   * actions on.
   */
  private BigStepClosureProofModel proofModel;


  /**
   * The origin {@link BigStepClosureProofNode}. Contains the information this
   * node gives a graphics representation.
   */
  private BigStepClosureProofNode proofNode;


  /**
   * Contains the information of the size of the Component. When
   * {@link #placeElements(int)} is called <i>dimension</i> is filled with
   * proppert values.
   */
  private Dimension dimension;


  /**
   * Amount of pixels that will be left free between the elements of the node.
   */
  private int spacing;


  /**
   * Amount of pixels that will be left free between the elements of the node.
   */
  private int meshPix = 50;


  /**
   * Label that will contain the index at the front.
   */
  private JLabel indexLabel;


  /**
   * Component containing the expression and the store.
   */
  private CompoundExpressionBigStepClosure compoundExpression;


  /**
   * Label containing the down-directed double-arrow separating the Expression
   * and the Resul-Expression.
   */
  private JLabel downArrowLabel;


  /**
   * Component containing the result-expression and the result-store.
   */
  private CompoundExpressionBigStepClosure resultCompoundExpression;


  /**
   * The Button with its DropDownMenu used to perform the userinteraction.
   */
  private MenuButton ruleButton;


  /**
   * Label that will be used to show the evaluated rule.
   */
  private JLabel ruleLabel;


  /**
   * Label that will be used to show the environments created.
   */
  private JLabel envLabel;


  /**
   * The "Translate to core syntax" item in the context menu.
   */
  private MenuTranslateItem menuTranslateItem;


  /**
   * The translator will be used to determine whether the expression contains
   * syntactical sugar.
   */
  private LanguageTranslator translator;


  /**
   * containing the rules it may contain submenus if to many (set in TOMANY)
   * rules are in the popupmenu
   */
  private JPopupMenu menu;


  /**
   * Manages the RulesMenu
   */
  private RulesMenu rm = new RulesMenu ();


  /**
   * @param node The node that should be represented
   * @param model The model
   * @param pTranslator The translator from the model
   */
  public BigStepClosureNodeComponent ( BigStepClosureProofNode node,
      BigStepClosureProofModel model, LanguageTranslator pTranslator )
  {
    super ();
    this.proofNode = node;
    this.proofModel = model;
    this.translator = pTranslator;
    this.dimension = new Dimension ( 0, 0 );
    this.spacing = 10;
    /*
     * Create and add the components needed to render this node
     */
    this.indexLabel = new JLabel ();
    this.indexLabel.addMouseListener ( new OutlineMouseListener ( this ) );
    add ( this.indexLabel );
    this.compoundExpression = new CompoundExpressionBigStepClosure ();
    this.compoundExpression
        .addMouseListener ( new OutlineMouseListener ( this ) );
    add ( this.compoundExpression );
    this.downArrowLabel = new JLabel ();
    add ( this.downArrowLabel );
    this.downArrowLabel.setText ( " \u21d3 " ); //$NON-NLS-1$
    this.resultCompoundExpression = new CompoundExpressionBigStepClosure ();
    this.resultCompoundExpression.addMouseListener ( new OutlineMouseListener (
        this ) );
    add ( this.resultCompoundExpression );
    this.resultCompoundExpression.setAlternativeColor ( Color.LIGHT_GRAY );
    this.ruleButton = new MenuButton ();
    add ( this.ruleButton );
    this.ruleButton.setVisible ( true );
    this.ruleLabel = new JLabel ();
    add ( this.ruleLabel );
    this.ruleLabel.setVisible ( false );

    // create the env label
    this.envLabel = new JLabel ();
    add ( this.envLabel );
    this.envLabel.setVisible ( false );

    /*
     * Create the PopupMenu for the menu button
     */
    // Fill the menu with menuitems
    ProofRule [] rules = this.proofModel.getRules ();
    Language lang = this.proofModel.getLanguage ();
    this.menu = new JPopupMenu ();

    this.menu = this.rm.getMenu ( rules, rules, lang, this, "bigstep", false ); //$NON-NLS-1$

    this.menu.addSeparator ();

    this.menu.add ( this.menuTranslateItem = new MenuTranslateItem () );
    this.ruleButton.setMenu ( this.menu );
    /*
     * Connect the handling of the ruleButton
     */
    this.ruleButton.addMenuButtonListener ( new MenuButtonListener ()
    {

      public void menuClosed ( @SuppressWarnings ( "unused" ) MenuButton button )
      {
        // empty block
      }


      public void menuItemActivated (
          @SuppressWarnings ( "unused" ) MenuButton button,
          final JMenuItem source )
      {
        // setup a wait cursor for the toplevel ancestor
        final Container toplevel = getTopLevelAncestor ();
        final Cursor cursor = toplevel.getCursor ();
        toplevel.setCursor ( new Cursor ( Cursor.WAIT_CURSOR ) );
        // avoid blocking the popup menu
        SwingUtilities.invokeLater ( new Runnable ()
        {

          public void run ()
          {
            // handle the menu action
            BigStepClosureNodeComponent.this.handleMenuActivated ( source );
            // wait for the repaint before resetting the cursor
            SwingUtilities.invokeLater ( new Runnable ()
            {

              public void run ()
              {
                // reset the cursor
                toplevel.setCursor ( cursor );
              }
            } );
          }
        } );
      }
    } );
    changeNode ();
  }


  /**
   * Causes the expression and the resultexpression to recalculate their layout.
   */
  public void reset ()
  {
    this.compoundExpression.reset ();
    this.resultCompoundExpression.reset ();
  }


  /**
   * Adds a new {@link BigStepClosureNodeListener} to the
   * <i>SmallStepNodeComponent</i>
   * 
   * @param listener The listener to be added
   */
  public void addBigStepNodeListener ( BigStepClosureNodeListener listener )
  {
    this.listenerList.add ( BigStepClosureNodeListener.class, listener );
  }


  /**
   * Removes a {@link BigStepNodeListener} from the
   * <i>SmallStepNodeComponent</i>
   * 
   * @param listener The listener to be removed.
   */
  public void removeBigStepNodeListener ( BigStepClosureNodeListener listener )
  {
    this.listenerList.remove ( BigStepClosureNodeListener.class, listener );
  }


  /**
   * Calls the
   * {@link BigStepClosureNodeListener#nodeChanged(BigStepClosureNodeComponent)}
   * of all listeners.
   */
  private void fireNodeChanged ()
  {
    Object [] listeners = this.listenerList.getListenerList ();
    for ( int i = 0 ; i < listeners.length ; i += 2 )
    {
      if ( listeners [ i ] != BigStepClosureNodeListener.class )
      {
        continue;
      }
      ( ( BigStepClosureNodeListener ) listeners [ i + 1 ] )
          .nodeChanged ( this );
    }
  }


  /**
   * TODO
   * 
   * @param node
   */
  private void fireRequestJumpToNode ( ProofNode node )
  {
    Object [] listeners = this.listenerList.getListenerList ();
    for ( int i = 0 ; i < listeners.length ; i += 2 )
    {
      if ( listeners [ i ] != BigStepClosureNodeListener.class )
      {
        continue;
      }
      ( ( BigStepClosureNodeListener ) listeners [ i + 1 ] )
          .requestJumpToNode ( node );
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
  public void handleMenuActivated ( JMenuItem item )
  {
    if ( item instanceof MenuRuleItem )
    {
      MenuRuleItem ruleItem = ( MenuRuleItem ) item;
      ProofRule rule = ruleItem.getRule ();
      try
      {
        this.proofModel.prove ( rule, this.proofNode );
        this.ruleButton.setToolTipText ( null );
      }
      catch ( Throwable e )
      {
        // revert the menu
        this.rm.revertMenu ();
        // when the node could not be prooven with the selected
        // rule the menu button gets labeled with the given rule
        // and will be displayed in red
        this.ruleButton.setText ( "(" + rule.getName () + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
        this.ruleButton.setTextColor ( Color.RED );
        // determine the error message for the tooltip
        this.ruleButton.setToolTipText ( e.getMessage () );
      }
    }
    else if ( item instanceof MenuTranslateItem )
    {
      int answer = 1;
      if ( this.proofModel.containsSyntacticSugar ( this.proofNode, false ) )
      {
        String [] answers =
        {
            Messages.getString ( "NodeComponent.0" ), Messages.getString ( "NodeComponent.1" ), Messages.getString ( "NodeComponent.2" ) }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        answer = JOptionPane.showOptionDialog (
            getTopLevelAncestor (),
            Messages.getString ( "NodeComponent.3" ), //$NON-NLS-1$
            Messages.getString ( "NodeComponent.4" ), //$NON-NLS-1$
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, answers, answers [ 0 ] );
      }
      switch ( answer )
      {
        case 0 :
          this.proofModel.translateToCoreSyntax ( this.proofNode, false );
          break;
        case 1 :
          this.proofModel.translateToCoreSyntax ( this.proofNode, true );
          break;
        case 2 :
          break;
      }
      fireNodeChanged ();
    }
    else if ( item instanceof MenuGuessItem )
    {
      try
      {
        this.proofModel.guess ( this.proofNode );
      }
      catch ( final ProofGuessException e )
      {
        fireRequestJumpToNode ( e.getNode () );
        SwingUtilities.invokeLater ( new Runnable ()
        {

          public void run ()
          {
            JOptionPane
                .showMessageDialog (
                    getTopLevelAncestor (),
                    MessageFormat.format ( Messages
                        .getString ( "NodeComponent.5" ), e.getMessage () ), Messages.getString ( "NodeComponent.6" ), JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$ //$NON-NLS-2$
          }
        } );
      }
    }
    else if ( item instanceof MenuGuessTreeItem )
    {
      try
      {
        this.proofModel.complete ( this.proofNode );
      }
      catch ( final ProofGuessException e )
      {
        fireRequestJumpToNode ( e.getNode () );
        SwingUtilities.invokeLater ( new Runnable ()
        {

          public void run ()
          {
            JOptionPane
                .showMessageDialog (
                    getTopLevelAncestor (),
                    MessageFormat.format ( Messages
                        .getString ( "NodeComponent.7" ), e.getMessage () ), Messages.getString ( "NodeComponent.8" ), JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$ //$NON-NLS-2$
          }
        } );
      }
    }
    fireNodeChanged ();
  }


  /**
   * Sets the index of the current node.
   * 
   * @param index
   */
  public void setIndex ( int index )
  {
    this.indexLabel.setText ( "(" + index + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Does an update on the compound expression.<br>
   * Resets the expression and the environment (if there is one). That causes
   * the PrettyStringRenderer to recheck the breakpoints.
   */
  public void changeNode ()
  {
    if ( this.proofNode.getClosure () != null )
      this.compoundExpression.setClosure ( this.proofNode.getClosure () );

    this.resultCompoundExpression
        .setClosure ( this.proofNode.getResult () == null ? null
            : this.proofNode.getResult ().getClosure () );
  }


  /**
   * Places all elements of the current node.<br>
   * Just places one element after the other. 1st the index, 2nd the
   * compoundExpression 3rd the double-sidded-down-directed arrow. When the node
   * is proven (that is if there is already an evaluated result, it will be
   * placed behind the arrow.<br>
   * <br>
   * If the the nodes is evaluated the ruleLabel is placed with a bit spacing
   * below the expression. If the node is not evaluated the menuButton is placed
   * at the same size.<br>
   * <br>
   * After the placing is done the {@link #dimension} contains the needed size
   * of this node.
   * 
   * @param pMaxWidth The maximum width that is available for the current node.
   */
  private void placeElements ( int pMaxWidth )
  {
    int maxWidth = pMaxWidth;
    // get the size for the index at the beginning: (x)
    FontMetrics fm = AbstractRenderer.getTextFontMetrics ();
    Dimension labelSize = new Dimension ( fm.stringWidth ( this.indexLabel
        .getText () ), fm.getHeight () );
    this.dimension.setSize ( labelSize.width, labelSize.height );
    // there will be a bit spacing between the index label and the expression
    this.dimension.width += this.spacing;
    // the index shrinkens the max size for the expression
    maxWidth -= labelSize.width;
    // get the needed size for the expression
    Dimension expSize = this.compoundExpression.getNeededSize ( maxWidth
        - this.dimension.width );
    this.dimension.width += expSize.width;
    this.dimension.height = Math.max ( expSize.height, this.dimension.height );
    Dimension arrowSize = this.downArrowLabel.getPreferredSize ();
    this.dimension.width += arrowSize.width;
    this.dimension.height = Math.max ( arrowSize.height, this.dimension.height );
    Dimension resultSize = new Dimension ( 0, 0 );
    // get the size of the Result if there is no linebrake
    resultSize = this.resultCompoundExpression.getNeededSize ( maxWidth
        - this.dimension.width );

    boolean breakNeeded = false;
    // if the expression is braken the result will be meshed mesPix pixels
    // break if the result dose not fit
    if ( ( maxWidth - this.dimension.width ) < resultSize.width )
    {
      breakNeeded = true;
    }

    if ( !breakNeeded )
    {
      // if the result is higher than the hight of one line the result will be
      // in the next line
      if ( resultSize.height > AbstractRenderer.getAbsoluteHeight () )
      {
        breakNeeded = true;
      }
    }
    if ( breakNeeded )
    {
      // get the new resultsize in the next line
      resultSize = this.resultCompoundExpression.getNeededSize ( maxWidth
          - this.meshPix );
      // the hight will be lager
      this.dimension.height = expSize.height + resultSize.height;
    }
    if ( !breakNeeded )
    {
      this.dimension.width += resultSize.width;
      this.dimension.height = Math.max ( resultSize.height,
          this.dimension.height );
    }
    else
    {
      this.dimension.width = Math.max ( ( resultSize.width + this.meshPix ),
          this.dimension.width );
    }

    // now place the elements
    int posX = 0;
    this.indexLabel
        .setBounds ( posX, 0, labelSize.width, this.dimension.height );
    posX += labelSize.width + this.spacing;
    this.compoundExpression.setBounds ( posX, 0, expSize.width, expSize.height );
    posX += expSize.width;
    this.downArrowLabel.setBounds ( posX, 0, arrowSize.width, expSize.height );
    posX += arrowSize.width;
    if ( !breakNeeded )
    {
      this.resultCompoundExpression.setBounds ( posX, 0, resultSize.width,
          resultSize.height );
    }
    else
    {
      this.resultCompoundExpression.setBounds ( this.meshPix,
          ( this.dimension.height - resultSize.height ), resultSize.width,
          resultSize.height );
    }
    /*
     * Check whether this is node is evaluated. If it is evaluated only the
     * Label needs to get placed, if it is not evaluated yet the MenuButton
     * needs to get placed.
     */
    posX = labelSize.width + this.spacing;
    if ( this.proofNode.getRule () != null )
    {
      this.ruleLabel.setText ( this.proofNode.getRule ().toString () );
      Dimension ruleLabelSize = this.ruleLabel.getPreferredSize ();
      this.ruleLabel.setBounds ( posX, this.dimension.height + this.spacing,
          ruleLabelSize.width, ruleLabelSize.height );
      this.dimension.height += this.spacing + ruleLabelSize.height;
      this.dimension.width = Math.max ( this.dimension.width,
          ruleLabelSize.width + posX );
      // display only the label not the button
      this.ruleLabel.setVisible ( true );
      this.ruleButton.setVisible ( false );
    }
    else
    {
      // place the menu button
      Dimension buttonSize = this.ruleButton.getNeededSize ();
      this.ruleButton.setBounds ( posX, this.dimension.height + this.spacing,
          buttonSize.width, buttonSize.height );
      this.dimension.height += this.spacing + buttonSize.height;
      this.dimension.width = Math.max ( this.dimension.width, buttonSize.width
          + posX );
      // display only the button not the label
      this.ruleLabel.setVisible ( false );
      this.ruleButton.setVisible ( true );
    }

    final ClosureEnvironment expEnv = this.proofNode.getClosure ()
        .getEnvironment ();
    if ( expEnv.isNotPrinted () )
    {
      this.envLabel.setText ( this.envLabel.getText () + ' '
          + expEnv.getName () + '=' + expEnv.toString () );
      this.envLabel.setVisible ( true );
    }

    if ( this.proofNode.getResult () != null )
    {
      final ClosureEnvironment resEnv = this.proofNode.getResult ()
          .getClosure ().getEnvironment ();
      if ( resEnv.isNotPrinted () )
      {
        this.envLabel.setText ( this.envLabel.getText () + ' '
            + resEnv.getName () + '=' + resEnv.toString () );
        this.envLabel.setVisible ( true );
      }
    }

    if ( this.envLabel.isVisible () )
    {
      final Dimension envLabelSize = this.envLabel.getPreferredSize ();
      this.envLabel.setBounds ( posX, this.dimension.height + this.spacing,
          envLabelSize.width, envLabelSize.height );
      this.dimension.width = Math.max ( this.dimension.width,
          envLabelSize.width + posX );
      this.dimension.height += this.spacing + this.envLabel.getHeight ();
    }
  }


  /**
   * Implementation of the TreeNodeComponent interface
   * 
   * @param maxWidth
   * @return TODO
   */
  public Dimension update ( int maxWidth )
  {
    placeElements ( maxWidth );
    this.menuTranslateItem.setEnabled ( this.translator.containsSyntacticSugar (
        this.proofNode.getExpression (), true ) );
    return this.dimension;
  }


  /**
   * Returns the point at the bottom of the node where the layout should attach
   * the arrow.
   * 
   * @return TODO
   */
  public Point getBottomArrowConnection ()
  {
    return new Point ( getX () + this.indexLabel.getWidth () / 2, getY ()
        + ( this.dimension.height / 2 ) );
  }


  /**
   * Returns the point at the left of the node where the layout should attach
   * the line to its parent.
   * 
   * @return TODO
   */
  public Point getLeftArrowConnection ()
  {
    return new Point ( getX (), getY () + this.indexLabel.getY ()
        + this.indexLabel.getHeight () / 2 );
  }


  /**
   * Returns the number of pixels the children should be displayed indentated.
   * 
   * @return TODO
   */
  public int getIndentationWidth ()
  {
    // XXX: calculate the indentation
    return this.indexLabel.getWidth ();
  }


  /**
   * Just calls setBounds of the super class.
   */
  @Override
  public void setBounds ( int x, int y, int width, int height )
  {
    super.setBounds ( x, y, width, height );
  }


  /**
   * Returns the indexLabel.
   * 
   * @return The indexLabel.
   * @see #indexLabel
   */
  public JLabel getIndexLabel ()
  {
    return this.indexLabel;
  }


  /**
   * Returns the compoundExpression.
   * 
   * @return The compoundExpression.
   * @see #compoundExpression
   */
  public CompoundExpressionBigStepClosure getCompoundExpression ()
  {
    return this.compoundExpression;
  }


  /**
   * Returns the resultCompoundExpression.
   * 
   * @return The resultCompoundExpression.
   * @see #resultCompoundExpression
   */
  public CompoundExpressionBigStepClosure getResultCompoundExpression ()
  {
    return this.resultCompoundExpression;
  }
}
