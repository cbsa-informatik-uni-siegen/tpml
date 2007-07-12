package de.unisiegen.tpml.graphics.typechecker ;


import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageParser;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.Theme;
import de.unisiegen.tpml.graphics.components.CompoundExpression;
import de.unisiegen.tpml.graphics.components.MenuButton;
import de.unisiegen.tpml.graphics.components.MenuButtonListener;
import de.unisiegen.tpml.graphics.components.MenuEnterTypeItem;
import de.unisiegen.tpml.graphics.components.MenuGuessItem;
import de.unisiegen.tpml.graphics.components.MenuGuessTreeItem;
import de.unisiegen.tpml.graphics.components.MenuRuleItem;
import de.unisiegen.tpml.graphics.components.MenuTranslateItem;
import de.unisiegen.tpml.graphics.components.TypeComponent;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;


/**
 * Graphical representation of a {@link TypeCheckerProofNode }.<br>
 * <br>
 * A usual look of a node may be given in the following pictur. It shows the
 * second node within the tree of the TypeChecker of the Expression:
 * <code>let rec f = lambda x. if x = 0 then 1 else x * f (x-1) in f 3</code><br>
 * <img src="../../../../../../images/typecheckernode.png" /><br>
 * <br>
 * This node is actualy build using 4 components. The following scheme
 * illustrates the layouting of the single components.<br>
 * <img src="../../../../../../images/typecheckernode_scheme.png" /><br>
 * <br>
 * The first rectangle represents the {@link #indexLabel} The second rectanle
 * represents the entire {@link #compoundExpression} including the
 * typeenvironment. Then ther will be the {@link #doubleColonLabel} containing the <code>"  ::  "</code>. 
 * The last element in the first row is the {@link #typeComponent}
 * containing the resulting type.
 * If the node is not completly evaluated or the view is not in advanced mode only the four dots are drawn.<br>
 * In the next row there is only one rectangle containing the rule. In the case
 * of the previous image the {@link #ruleLabel} is shown, but as long as the
 * node has not been evaluated with a rule there would be located the
 * {@link #ruleButton}. Another possible component that could be located at
 * this position is the {@link #typeEnter}. That could be used to give the user
 * the possibilty to enter a type by himself.<br>
 * The bit of free space between the top and the bottom row aswell as between
 * the indexLabel and the expression is given in pixels in the {@link #spacing}.
 * <br>
 * Within the {@link TypeCheckerComponent} the
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
 * @author michael
 * 
 * @see de.unisiegen.tpml.graphics.typechecker.TypeCheckerView
 * @see de.unisiegen.tpml.graphics.typechecker.TypeCheckerComponent
 * @see de.unisiegen.tpml.graphics.tree.TreeNodeComponent
 * @see de.unisiegen.tpml.graphics.typechecker.TypeCheckerEnterType
 */
public class TypeCheckerNodeComponent extends JComponent implements
    TreeNodeComponent
{
  /**
   * 
   */
  private static final long serialVersionUID = - 6671706090992083026L ;


  /**
   * The Model this node can work with to guess, enter type or do Coresyntax
   * transaltion
   */
  private TypeCheckerProofModel proofModel ;


  /**
   * The Origin {@link TypeCheckerProofNode} this node represents.
   */
  private TypeCheckerProofNode proofNode ;


  /**
   * The calculated dimension for this node in pixels.
   */
  private Dimension dimension ;


  /**
   * The spacing that should be set free between to components within the node.
   */
  private int spacing ;


  /**
   * The label containing the <i>(x)</i> (the number of step) text at the beginning.
   */
  private JLabel indexLabel ;


  /**
   * The {@link CompoundExpression} containing the expression of this node.
   */
  private CompoundExpression < Identifier , Type > compoundExpression ;
  
  /**
   * The {@link JLabel} showing the resulting type of this node, once the node
   * has been evaluated or the view is in advaced mode. Otherwise the componet will not be shown.
   */
  private TypeComponent typeComponent ;


  /**
   * The {@link MenuButton} the user can use to do the actions.
   */
  private MenuButton ruleButton ;

  
  /**
   * The Label shown between the type and the expression. It will be
   * " :: "
   */
  private JLabel doubleColonLabel;
  
  /**
   * The String for the label
   */
  private static final String  doubleColonString = "  ::  ";


  /**
   * The {@link JLabel} showing the information about the rule, once the rule
   * has been evaluated.
   */
  private JLabel ruleLabel ;


  /**
   * The {@link TypeCheckerEnterType} element that will be shown once the user
   * select <i>"Enter type"</i> from the menu.
   */
  private TypeCheckerEnterType typeEnter ;


  /**
   * The menuTranslate is one element within the menu. <br>
   * Needs to get handled separatly because it can be enabled and disabled
   * whether the expression is containing Syntactical Sugar.
   */
  private MenuTranslateItem menuTranslateItem ;


  /**
   * The translator will be used to determin whether the expression contains
   * syntactical sugor.
   */
  private LanguageTranslator translator ;
  
  /**
   * the advaced mode
   */
  private boolean advanced;


	/**
	 * saves the last used MaxWidth
	 */
	private int lastMaxWidth;


  /**
   * Constructor for a TypeCheckerNode<br>
   * <br>
   * All elements needed within the node will be created and added to the
   * component. Some of them will be hidden at first (the {@link #typeEnter},
   * or the {@link #ruleLabel}) because they are not needed but they are always
   * there.<br>
   * 
   * @param node The origin ProofNode
   * @param model The model
   * @param translator The translator of the model for the selected language
   * @param advanced The advanced value
   */
  public TypeCheckerNodeComponent ( TypeCheckerProofNode node ,
      TypeCheckerProofModel model , LanguageTranslator translator, boolean advanced )
  {
    super ( ) ;
    this.advanced = advanced;
    this.proofNode = node ;
    this.proofModel = model ;
    this.translator = translator ;
    this.dimension = new Dimension ( 0 , 0 ) ;
    this.spacing = 10 ;
    this.indexLabel = new JLabel ( ) ;
    this.indexLabel.addMouseListener ( new OutlineMouseListener ( this ) ) ;
    add ( this.indexLabel ) ;
    this.compoundExpression = new CompoundExpression < Identifier , Type > ( ) ;
    this.compoundExpression
        .addMouseListener ( new OutlineMouseListener ( this ) ) ;
    add ( this.compoundExpression ) ;
    this.typeComponent = new TypeComponent ();
    this.typeComponent.addMouseListener ( new OutlineMouseListener (this));
    add (this.typeComponent);
    changeNode ( ) ;
    /*
     * Create both, the ruleButton for selecting the rule and the label, that
     * will be displayed later
     */
    this.ruleButton = new MenuButton ( ) ;
    add ( this.ruleButton ) ;
    this.ruleButton.setVisible ( true ) ;
    this.ruleLabel = new JLabel ( ) ;
    add ( this.ruleLabel ) ;
    this.ruleLabel.setVisible ( false ) ;
    this.doubleColonLabel = new JLabel();
    add (this.doubleColonLabel);
    this.doubleColonLabel.setFont(Theme.currentTheme().getFont());
    this.doubleColonLabel.setForeground(Theme.currentTheme().getExpressionColor());
    this.doubleColonLabel.setText (doubleColonString); //$NON-NLS-1$
    //this.typeLabel = new JLabel ( ) ;
    //add ( this.typeLabel ) ;
    //this.typeLabel.setText ( " !! " ) ; //$NON-NLS-1$
    //this.typeLabel.addMouseListener ( new OutlineMouseListener ( this ) ) ;

    this.typeEnter = new TypeCheckerEnterType ( ) ;
    add ( this.typeEnter ) ;
    /*
     * Create the PopupMenu for the menu button
     */
    JPopupMenu menu = new JPopupMenu ( ) ;
    ProofRule [ ] rules = this.proofModel.getRules ( ) ;
    if ( rules.length > 0 )
    {
      int group = rules [ 0 ].getGroup ( ) ;
      for ( ProofRule r : rules )
      {
        if ( r.getGroup ( ) != group )
        {
          menu.addSeparator ( ) ;
        }
        menu.add ( new MenuRuleItem ( r ) ) ;
        group = r.getGroup ( ) ;
      }
    }
    menu.addSeparator ( ) ;
    menu.add ( new MenuEnterTypeItem ( ) ) ;
    menu.add ( new MenuGuessItem ( ) ) ;
    menu.add ( new MenuGuessTreeItem ( ) ) ;
    menu.add ( this.menuTranslateItem = new MenuTranslateItem ( ) ) ;
    this.ruleButton.setMenu ( menu ) ;
    /*
     * Connect the handling of the ruleButton
     */
    this.ruleButton.addMenuButtonListener ( new MenuButtonListener ( )
    {
      public void menuClosed ( MenuButton button )
      {
      }


      public void menuItemActivated ( MenuButton button , final JMenuItem source )
      {
        // setup a wait cursor for the toplevel ancestor
        final Container toplevel = getTopLevelAncestor ( ) ;
        final Cursor cursor = toplevel.getCursor ( ) ;
        toplevel.setCursor ( new Cursor ( Cursor.WAIT_CURSOR ) ) ;
        // avoid blocking the popup menu
        SwingUtilities.invokeLater ( new Runnable ( )
        {
          public void run ( )
          {
            // handle the menu action
            TypeCheckerNodeComponent.this.handleMenuActivated ( source ) ;
            // wait for the repaint before resetting the cursor
            SwingUtilities.invokeLater ( new Runnable ( )
            {
              public void run ( )
              {
                // reset the cursor
                toplevel.setCursor ( cursor ) ;
              }
            } ) ;
          }
        } ) ;
      }
    } ) ;
    this.typeEnter
        .addTypeCheckerTypeEnterListener ( new TypeCheckerTypeEnterListener ( )
        {
          public void typeEntered ( final String type )
          {
            // setup a wait cursor for the toplevel ancestor
            final Container toplevel = getTopLevelAncestor ( ) ;
            final Cursor cursor = toplevel.getCursor ( ) ;
            toplevel.setCursor ( new Cursor ( Cursor.WAIT_CURSOR ) ) ;
            // avoid blocking the popup menu
            SwingUtilities.invokeLater ( new Runnable ( )
            {
              public void run ( )
              {
                // handle the entered type
                TypeCheckerNodeComponent.this.handleTypeEntered ( type ) ;
                // reset the cursor
                toplevel.setCursor ( cursor ) ;
              }
            } ) ;
          }


          public void canceled ( )
          {
            TypeCheckerNodeComponent.this.handleTypeEnterCanceled ( ) ;
          }
        } ) ;
  }


  /**
   * Causes the expression and the resultexpression to recalculate their layout.
   */
  public void reset ( )
  {
    this.compoundExpression.reset ( ) ;
    this.typeComponent.reset ();
  }


  /**
   * Sets the index that will be displayed in front of the node
   * 
   * @param index The index
   */
  public void setIndex ( int index )
  {
    this.indexLabel.setText ( "(" + index + ")" ) ; //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Causes the {@link #compoundExpression} to updated theire expression and
   * environment.
   */
  public void changeNode ( )
  {
    this.compoundExpression.setExpression ( this.proofNode.getExpression ( ) ) ;
    this.compoundExpression.setEnvironment ( this.proofNode.getEnvironment ( ) ) ;
    this.typeComponent.setType (this.proofNode.getType ());
  }


  /**
   * Places all elements one after another.<br>
   * <br>
   * First the label, the expression and the "::" will be placed, if the node is
   * already prooven the {@link #typeLabel} will be placed aswell. The
   * {@link #dimension} will be rescaled with every item that is placed and with
   * all items, the height of the dimension will set to the current maximum.<br>
   * <br>
   * When all item of the top row are placed the {@link #ruleButton},
   * {@link #ruleLabel} or {@link #typeEnter} will be placed depending whether
   * the node is evaluated, it is not evaluated or the user previously selected
   * <i>Enter type</i>.
   * 
   * @param maxWidth The maximum amount of pixels available to place the
   *          elements.
   */
  private void placeElements ( int maxWidth )
  {
  	// will save if the type is shown or not
    boolean showType = false;
    // save the maxWidht so the setAdvaced() is able to replace the elements
    lastMaxWidth = maxWidth;
    // get the size for the index at the beginning: (x)
    FontMetrics fm = AbstractRenderer.getTextFontMetrics();
    Dimension labelSize = new Dimension(fm.stringWidth(this.indexLabel.getText()), fm.getHeight());
    this.dimension.setSize(labelSize.width, labelSize.height);
    // there will be a bit spacing between the index label and the expression
    this.dimension.width += this.spacing;
    // the index shrinkens the max size for the expression
    maxWidth -= labelSize.width;
    // get the needed size for the expression
    Dimension expSize = this.compoundExpression.getNeededSize(maxWidth
        - AbstractRenderer.getTextFontMetrics().stringWidth(doubleColonString));
    this.dimension.width += expSize.width;
    this.dimension.width += AbstractRenderer.getTextFontMetrics().stringWidth(doubleColonString);
    this.dimension.height = Math.max(expSize.height, this.dimension.height);

    Dimension typeSize = typeComponent.getNeededSize(maxWidth);

    // get the neede size for the type
    if (this.proofNode.getType() != null && (this.proofNode.isFinished() || this.advanced))
    {
      // ok, we want to see the type
      showType = true;
    }
    else
    {
      // hide the type
      showType = false;
    }
    // Dimension typeSize = this.typeLabel.getPreferredSize ( ) ;

    // the type will be renderd behind the expression if ther is enough space available
    // remember if it is neccessary to break
    boolean broke = false;
    if ( (this.dimension.width + typeSize.width) > maxWidth) //dose not fit
    {
      //update the dimension
    	this.dimension.width = Math.max(dimension.width, typeSize.width);
    	this.dimension.height += typeSize.height;
    	this.dimension.height += AbstractRenderer.getAbsoluteHeight();
      //remember to break
    	broke = true;
    }
    else
    {
      //update the dimension
    	this.dimension.width += typeSize.width ;
      this.dimension.height = Math.max ( typeSize.height , this.dimension.height ) ;
    }
 
    // now place the components, every component will be placed at posX, posY
    int posX = 0;
    this.indexLabel.setBounds(posX, 0, labelSize.width, this.dimension.height);
    posX += labelSize.width + this.spacing;

    // save the psoition of the front wher the expression starts. Wher will also the type start.
    int posXfront = posX;

    // set the expression position
    this.compoundExpression.setBounds(posX, 0, expSize.width, expSize.height);
    posX += expSize.width;

    // set the ::
    this.doubleColonLabel.setBounds(posX, AbstractRenderer.getFontLeading(), AbstractRenderer.getTextFontMetrics()
        .stringWidth(doubleColonString), expSize.height);
    posX += doubleColonLabel.getSize().width;
   

    // if the expression and type are not in the same line the type will be placed in the next line
    // under the expression at the beginning og posXfront
    if (broke)
    {
      if (showType)
      {
        this.typeComponent.setBounds(posXfront, 0 + expSize.height, typeSize.width, typeSize.height);
        posX += typeSize.width;
      }
      else
      // do net shoe thy type
      {
        // siply hide it.
        this.typeComponent.setBounds(0, 0, 0, 0);
      }
    }
    else
    // place the Type behind the expression
    {
      if (showType)
      {
        this.typeComponent.setBounds(posX, 0, typeSize.width, typeSize.height);
        posX += typeSize.width;
      }
      else
      // do net shoe thy type
      {
        // siply hide it.
        this.typeComponent.setBounds(0, 0, 0, 0);
      }
    }
    
    /*
     * Check whether this is node is evaluated. If it is evaluated only the
     * Label needs to get placed, if it is not evaluated yet the MenuButton
     * needs to get placed.
     */
    posX = labelSize.width + this.spacing ;
    if ( this.proofNode.isProven ( ) )
    {
      // place the menu label
      this.ruleLabel.setText ( "(" + this.proofNode.getRule ( ) + ")" ) ; //$NON-NLS-1$ //$NON-NLS-2$
      Dimension ruleLabelSize = this.ruleLabel.getPreferredSize ( ) ;
      this.ruleLabel.setBounds ( posX , this.dimension.height + this.spacing ,
          ruleLabelSize.width , ruleLabelSize.height ) ;
      this.dimension.height += this.spacing + ruleLabelSize.height ;
      this.dimension.width = Math.max ( this.dimension.width ,
          ruleLabelSize.width + posX ) ;
      // display only the label not the button
      this.ruleLabel.setVisible ( true ) ;
      this.ruleButton.setVisible ( false ) ;
      this.typeEnter.setVisible ( false ) ;
    }
    else
    {
      if ( ! this.typeEnter.isActive ( ) )
      {
        // place the menu button
        Dimension buttonSize = this.ruleButton.getNeededSize ( ) ;
        this.ruleButton.setBounds ( posX ,
            this.dimension.height + this.spacing , buttonSize.width ,
            buttonSize.height ) ;
        this.dimension.height += this.spacing + buttonSize.height ;
        this.dimension.width = Math.max ( this.dimension.width ,
            buttonSize.width + posX ) ;
        // display only the button not the label
        this.ruleLabel.setVisible ( false ) ;
        this.ruleButton.setVisible ( true ) ;
        this.typeEnter.setVisible ( false ) ;
      }
      else
      {
        Dimension typeEnterSize = this.typeEnter.getPreferredSize ( ) ;
        this.typeEnter.setBounds ( posX , this.dimension.height + this.spacing ,
            typeEnterSize.width , typeEnterSize.height ) ;
        this.dimension.height += this.spacing + typeEnterSize.height ;
        this.dimension.width = Math.max ( this.dimension.width ,
            typeEnterSize.width + posX ) ;
        this.ruleLabel.setVisible ( false ) ;
        this.ruleButton.setVisible ( false ) ;
        this.typeEnter.setVisible ( true ) ;
      }    
    }
  }


  /*
   * Implementation of the eventhandling
   */
  /**
   * Will be called when the user has accepted the {@link TypeCheckerEnterType}
   * component with a type.<br>
   * The type string will be tried to get applied on the {@link #proofNode}
   * using a {@link Language} and a {@link LanguageParser} to get a
   * {@link MonoType} that will be applied to the node using
   * {@link TypeCheckerProofModel#guessWithType(ProofNode, MonoType)}.
   */
  private void handleTypeEntered ( String type )
  {
    Language language = this.proofModel.getLanguage ( ) ;
    LanguageTypeParser parser = language.newTypeParser ( new StringReader (
        type ) ) ;
    MonoType monoType = null ;
    try
    {
      monoType = parser.parse ( ) ;
    }
    catch ( Exception e )
    {
      this.typeEnter.selectAll ( ) ;
      JOptionPane
          .showMessageDialog ( getTopLevelAncestor ( ) , MessageFormat.format (
              Messages.getString ( "TypeCheckerNodeComponent.0" ) , type ) ) ; //$NON-NLS-1$
      return ;
    }
    try
    {
      this.proofModel.guessWithType ( this.proofNode , monoType ) ;
    }
    catch ( Exception e )
    {
      this.typeEnter.selectAll ( ) ;
      JOptionPane
          .showMessageDialog (
              getTopLevelAncestor ( ) ,
              MessageFormat.format (
                  Messages.getString ( "NodeComponent.5" ) , e.getMessage ( ) ) , Messages.getString ( "NodeComponent.6" ) , JOptionPane.ERROR_MESSAGE ) ; //$NON-NLS-1$ //$NON-NLS-2$
      return ;
    }
    this.typeEnter.setActive ( false ) ;
    fireNodeChanged ( ) ;
  }


  /**
   * Just set the type enter to <i>No active</i> and causes a node changed.<br>
   * That will lead to a propper layouting where the {@link #ruleButton} will be
   * displayed again.
   */
  private void handleTypeEnterCanceled ( )
  {
    this.typeEnter.setActive ( false ) ;
    fireNodeChanged ( ) ;
  }


  public void addTypeCheckerNodeListener ( TypeCheckerNodeListener listener )
  {
    this.listenerList.add ( TypeCheckerNodeListener.class , listener ) ;
  }


  public void removeTypeCheckerNodeListener ( TypeCheckerNodeListener listener )
  {
    this.listenerList.remove ( TypeCheckerNodeListener.class , listener ) ;
  }


  private void fireNodeChanged ( )
  {
    Object [ ] listeners = this.listenerList.getListenerList ( ) ;
    for ( int i = 0 ; i < listeners.length ; i += 2 )
    {
      if ( listeners [ i ] != TypeCheckerNodeListener.class )
      {
        continue ;
      }
      ( ( TypeCheckerNodeListener ) listeners [ i + 1 ] ).nodeChanged ( this ) ;
    }
  }


  private void fireRequestJumpToNode ( ProofNode node )
  {
    Object [ ] listeners = this.listenerList.getListenerList ( ) ;
    for ( int i = 0 ; i < listeners.length ; i += 2 )
    {
      if ( listeners [ i ] != TypeCheckerNodeListener.class )
      {
        continue ;
      }
      ( ( TypeCheckerNodeListener ) listeners [ i + 1 ] )
          .requestJumpToNode ( node ) ;
    }
  }


  /**
   * Handles every action, that is done via the menu of the {@link #ruleButton}.<br>
   * <br>
   * Because every item in the menu (except the Separatero :-) ) is one of our
   * own, the activated type of item can simply be identified by its class.<br>
   * 
   * @param item
   */
  private void handleMenuActivated ( JMenuItem item )
  {
    if ( item instanceof MenuRuleItem )
    {
      MenuRuleItem ruleItem = ( MenuRuleItem ) item ;
      ProofRule rule = ruleItem.getRule ( ) ;
      try
      {
        this.proofModel.prove ( rule , this.proofNode ) ;
        this.ruleButton.setToolTipText ( null ) ;
      }
      catch ( Exception exc )
      {
        // when the node could not be prooven with the selected
        // rule the menu button gets labeled with the given rule
        // and will be displayed in red
        this.ruleButton.setText ( "(" + rule.getName ( ) + ")" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        this.ruleButton.setTextColor ( Color.RED ) ;
        // determine the error text for the tooltip
        this.ruleButton.setToolTipText ( exc.getMessage ( ) ) ;
      }
      fireNodeChanged ( ) ;
    }
    else if ( item instanceof MenuTranslateItem )
    {
      int answer = 1 ;
      if ( this.proofModel.containsSyntacticSugar ( this.proofNode , false ) )
      {
        String [ ] answers =
        {
            Messages.getString ( "NodeComponent.0" ) , Messages.getString ( "NodeComponent.1" ) , Messages.getString ( "NodeComponent.2" ) } ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        answer = JOptionPane.showOptionDialog (
            getTopLevelAncestor ( ) ,
            Messages.getString ( "NodeComponent.3" ) , //$NON-NLS-1$
            Messages.getString ( "NodeComponent.4" ) , //$NON-NLS-1$
            JOptionPane.YES_NO_CANCEL_OPTION , JOptionPane.QUESTION_MESSAGE ,
            null , answers , answers [ 0 ] ) ;
      }
      switch ( answer )
      {
        case 0 :
          this.proofModel.translateToCoreSyntax ( this.proofNode , false ) ;
          break ;
        case 1 :
          this.proofModel.translateToCoreSyntax ( this.proofNode , true ) ;
          break ;
        case 2 :
          break ;
      }
      fireNodeChanged ( ) ;
    }
    else if ( item instanceof MenuGuessItem )
    {
      try
      {
        this.proofModel.guess ( this.proofNode ) ;
      }
      catch ( final ProofGuessException e )
      {
        fireRequestJumpToNode ( e.getNode ( ) ) ;
        SwingUtilities.invokeLater ( new Runnable ( )
        {
          public void run ( )
          {
            JOptionPane
                .showMessageDialog (
                    getTopLevelAncestor ( ) ,
                    MessageFormat.format ( Messages
                        .getString ( "NodeComponent.5" ) , e.getMessage ( ) ) , Messages.getString ( "NodeComponent.6" ) , JOptionPane.ERROR_MESSAGE ) ; //$NON-NLS-1$ //$NON-NLS-2$
          }
        } ) ;
      }
      fireNodeChanged ( ) ;
    }
    else if ( item instanceof MenuGuessTreeItem )
    {
      try
      {
        this.proofModel.complete ( this.proofNode ) ;
      }
      catch ( final ProofGuessException e )
      {
        fireRequestJumpToNode ( e.getNode ( ) ) ;
        SwingUtilities.invokeLater ( new Runnable ( )
        {
          public void run ( )
          {
            JOptionPane
                .showMessageDialog (
                    getTopLevelAncestor ( ) ,
                    MessageFormat.format ( Messages
                        .getString ( "NodeComponent.7" ) , e.getMessage ( ) ) , Messages.getString ( "NodeComponent.8" ) , JOptionPane.ERROR_MESSAGE ) ; //$NON-NLS-1$ //$NON-NLS-2$
          }
        } ) ;
      }
      fireNodeChanged ( ) ;
    }
    else if ( item instanceof MenuEnterTypeItem )
    {
      this.typeEnter.setActive ( true ) ;
      this.typeEnter.clear ( ) ;
      fireNodeChanged ( ) ;
    }
  }


  /*
   * Implementation of the TreeNodeComponent Interface
   */
  /**
   * Performs an update for the Entire Node. All elements get rearanged based on
   * the given maximum with, the menu items will be checked if they are still
   * available.
   */
  public Dimension update ( int maxWidth )
  {
    placeElements ( maxWidth ) ;
    this.menuTranslateItem.setEnabled ( this.translator.containsSyntacticSugar (
        this.proofNode.getExpression ( ) , true ) ) ;
    return this.dimension ;
  }


  /**
   * Returns the number of pixels the children should be displayed indentated.
   */
  public int getIndentationWidth ( )
  {
    // XXX: calculate the indentation
    return this.indexLabel.getWidth ( ) ;
  }


  /**
	 * Returns the point at the bottom of the node where the layout should attach the arrow.
	 */
	public Point getBottomArrowConnection()
	{
		return new Point(this.getX() + this.indexLabel.getWidth() / 2, this.getY() + (this.dimension.height / 2));
	}


  /**
	 * Returns the point at the left of the node where the layout should attach the line to its parent.
	 */
  public Point getLeftArrowConnection ( )
  {
    return new Point ( this.getX ( ) , this.getY ( ) + this.indexLabel.getY ( )
        + this.indexLabel.getHeight ( ) / 2 ) ;
  }


  /**
   * Just calls setBounds of the super class.
   */
  @ Override
  public void setBounds ( int x , int y , int width , int height )
  {
    super.setBounds ( x , y , width , height ) ;
  }


  /**
   * Returns the typeLabel.
   * 
   * @return The typeLabel.
   * @see #typeLabel
   */
  public TypeComponent getTypeComponent ( )
  {
    return this.typeComponent ;
  }


  /**
   * Returns the proofNode.
   * 
   * @return The proofNode.
   * @see #proofNode
   */
  public TypeCheckerProofNode getProofNode ( )
  {
    return this.proofNode ;
  }


  /**
   * Returns the indexLabel.
   * 
   * @return The indexLabel.
   * @see #indexLabel
   */
  public JLabel getIndexLabel ( )
  {
    return this.indexLabel ;
  }


  /**
   * Returns the compoundExpression.
   * 
   * @return The compoundExpression.
   * @see #compoundExpression
   */
  public CompoundExpression < Identifier , Type > getCompoundExpression ( )
  {
    return this.compoundExpression ;
  }


/**
 * sets the advanced
 * @param advancedP the advanced
 */
	public void setAdvanced(boolean advancedP)
	{
		this.advanced = advancedP;
		this.placeElements(lastMaxWidth);
		
	}
}
