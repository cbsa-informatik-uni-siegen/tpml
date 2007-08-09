package de.unisiegen.tpml.graphics.smallstep;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofNode;
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

/**
 * The graphical representation of a
 * {@link de.unisiegen.tpml.core.smallstep.SmallStepProofNode}.<br>
 * <br>
 * The <code>SmallStepNodeComponent</code> is a bit more complicated than the
 * node from the other two GUIs because the rules applied to this node are
 * pointing on the expression of the next node.<br>
 * To handle this right, <b>one</b> <code>SmallStepNodeComponent</code> is
 * build like the following image shows:<br>
 * <img src="../../../../../../images/smallstepnode.png" /><br>
 * <br>
 * Actualy there are only two elements to handle here (that is they are handled
 * in the
 * {@link de.unisiegen.tpml.graphics.smallstep.SmallStepComponent#placeNode(SmallStepProofNode, int, int)}-method).
 * Those two elements are the {@link #compoundExpression} at the top right and
 * the {@link #rules} at the bottom left. <img
 * src="../../../../../../images/smallstepnode_scheme.png" /><br>
 * <br>
 * Because the elements of this node need to get layed out in an arrangement
 * with the elements of the parent-node and the child-node, both components have
 * a dimension containing the size they need for themeself and an additional
 * information on the actual size they need to fill(this is alway more or equal
 * as the size of itself). The <code>rules</code> of the parent node needs to
 * get verticaly centered and aligned with the <code>expression</code> of this
 * node. Usualy the <code>rules</code> are higher than the
 * <code>expression</code>s. So the height of the
 * {@link #expressionDimension} is less than the {@link #actualExpressionHeight}.
 * When the <code>expression</code> gets placed the
 * <code>actualExpressionHeight</code> is used for the height. The
 * {@link #actualRuleHeight} of the parent node than is the same as this
 * <code>actualExpressionHeight</code>. <br>
 * Analog the same is done for the <code>rules</code> of this node and the
 * <code>expression</code> of the child-node. The only difference is: when the
 * <code>rules</code> are placed theire vertical-center-alignment needs to get
 * calculated menualy, because the
 * {@link de.unisiegen.tpml.graphics.smallstep.SmallStepRulesComponent} is
 * always top-aligned.<br>
 * 
 * @author Marcell Fischbach
 * @author Benedikt Meurer
 * @author Michael Oeste
 * @version $Rev$
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepView
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepComponent
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepRulesComponent
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepRuleLabel
 * @see CompoundExpression
 */
public class SmallStepNodeComponent extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5536947349690384851L;

	/**
	 * The origin {@link SmallStepProofNode}
	 */
	private SmallStepProofNode proofNode;

	/**
	 * The {@link SmallStepProofModel}
	 */
	private SmallStepProofModel proofModel;

	/**
	 * The {@link SmallStepRulesComponent} containing the Labels of already
	 * evaluated Rules and the MenuButton for Rules not yet evaluated.
	 */
	private SmallStepRulesComponent rules;

	/**
	 * The {@link Dimension} the rules needs to get drawn correctly.
	 */
	private Dimension ruleDimension;

	/**
	 * The actual height the rules should use. This is >= ruleDimension.height.
	 * This height is determined by the expression height of the child node.
	 */
	private int actualRuleHeight;

	/**
	 * The Compound expression that is used to display the expression.
	 */
	CompoundExpression<Location, Expression> compoundExpression;

	/**
	 * The {@link Dimension} the expression needs to get drawn correctly.
	 */
	private Dimension expressionDimension;

	/**
	 * The actual height the expression should use. This is >=
	 * expressionDimension.height. This height is determined by the rule height of
	 * the parent node.
	 */
	private int actualExpressionHeight;

	/**
	 * The top-left-most position where the entire node should located.
	 */
	private Point origin;

	/**
	 * Flags that contains whether the expression has memory.
	 */
	private boolean memoryEnabled;

	/**
	 * The free space in pixels that should be hold between the components
	 */
	private int spacing;

	/**
	 * Entry within the context menu. Need to hold this value because is value can
	 * change. This item is enabled/disable in the {@link #update()}-Method.
	 * 
	 * @see #update()
	 */
	private MenuTranslateItem translateItem;

	/**
	 * Translator that is used to determine whether the expression contains
	 * syntactical sugar.
	 */
	private LanguageTranslator translator;

	/**
	 * The expression that should be underlined. When the user moves the mouse
	 * over a rule (not not grouped rules) or the MenuButton, that a part of the
	 * entire expressions needs to get underlined.
	 */
	private Expression currentUnderlineExpression;

	/**
	 * Adapter that is used to get added to every object that can cause the gui to
	 * underline an expression. It is bound to every {@link SmallStepRuleLabel}
	 * and to every {@link MenuButton}.
	 */
	private MouseMotionAdapter underlineRuleAdapter;

	/**
	 * containing the rules it may contain submenus if to many (set in TOMANY)
	 * rules are in the popupmenu
	 */
	private JPopupMenu menu;

	/**
	 * The Manager for teh RulesMenus
	 */
	private RulesMenu rulesMenu = new RulesMenu();

	/**
	 * Used internaly. When the underlining is cleared it will be done recursively
	 * over the entire tree. It needs to be done in two times one time directed to
	 * the parent and one time directed to the children. This enum is used by
	 * {@link SmallStepNodeComponent#freeUnderliningSibling(boolean, Direction)};
	 * 
	 * @author marcell
	 */
	private enum Direction {
		/***/
		DIRECTION_PARENT, 
		/***/
		DIRECTION_CHILD,
	}

	/**
	 * Constructs a SmallStepNodeComponent.<br>
	 * <br>
	 * All objects needed for one SmallStepNodeComponent are created and added to
	 * the {@link JComponent}.
	 * 
	 * @param pProofNode The origin node from the model.
	 * @param pProofModel The model.
	 * @param pTranslator The translator that should be used to determine whether
	 *          the Expression of this node contains syntactical sugar.
	 * @param pSpacing The spacing between the elements of the node.
	 * @param advanced Whether the small step view operates in advanced or
	 *          beginner mode.
	 */
	public SmallStepNodeComponent (SmallStepProofNode pProofNode, SmallStepProofModel pProofModel,
			LanguageTranslator pTranslator, int pSpacing, boolean advanced)
	{
		super();
		this.proofNode = pProofNode;
		this.proofModel = pProofModel;
		this.translator = pTranslator;
		this.currentUnderlineExpression = null;
		// the dimension for the rules initialy (0, 0)
		this.ruleDimension = new Dimension(0, 0);
		// the dimension for the expression initialy (0, 0)
		this.expressionDimension = new Dimension(0, 0);
		this.compoundExpression = new CompoundExpression<Location, Expression>();
		this.compoundExpression.addMouseListener(new OutlineMouseListener(this));
		add(this.compoundExpression);
		this.rules = new SmallStepRulesComponent(pProofNode);
		add(this.rules);
		this.memoryEnabled = this.proofModel.isMemoryEnabled();
		this.spacing = 10;
		this.translateItem = new MenuTranslateItem();
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		this.rules.getMenuButton().addMenuButtonListener(new MenuButtonListener() {
			public void menuClosed(MenuButton source)
			{
				// empty
			}

			public void menuItemActivated(MenuButton source, final JMenuItem item)
			{
				// setup a wait cursor for the toplevel ancestor
				final Container toplevel = getTopLevelAncestor();
				final Cursor cursor = toplevel.getCursor();
				toplevel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				// avoid blocking the popup menu
				SwingUtilities.invokeLater(new Runnable() {
					public void run()
					{
						// handle the menu action
						SmallStepNodeComponent.this.handleMenuActivated(item);
						// wait for the repaint before resetting the cursor
						SwingUtilities.invokeLater(new Runnable() {
							public void run()
							{
								// reset the cursor
								toplevel.setCursor(cursor);
							}
						});
					}
				});
			}
		});
		// create the adapters that will be used to determine
		// whether an expression needs to get underlined
		MouseMotionAdapter underlineThisAdapter = new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent event)
			{
				SmallStepNodeComponent.this.updateUnderlineExpression((Expression) null);
			}
		};
		this.underlineRuleAdapter = new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent event)
			{
				// System.out.println(" Event: "+event);
				// System.out.println("Typ: "+event.getSource());
				// System.out.println("Position "+event.getX() +", "+ event.getY());
				if (event.getSource() instanceof SmallStepRuleLabel)
				{
					SmallStepRuleLabel label = (SmallStepRuleLabel) event.getSource();
					SmallStepNodeComponent.this.updateUnderlineExpression(label);
				}
				else if (event.getSource() instanceof MenuButton)
				{
					MenuButton button = (MenuButton) event.getSource();
					SmallStepNodeComponent.this.updateUnderlineExpression(button);
				}
				else
				{
					SmallStepNodeComponent.this.compoundExpression.repaint();
				}
			}
		};
		this.addMouseMotionListener(underlineThisAdapter);
		this.compoundExpression.addMouseMotionListener(underlineThisAdapter);
		this.compoundExpression.addMouseMotionListener(this.underlineRuleAdapter);
		this.rules.getMenuButton().addMouseMotionListener(this.underlineRuleAdapter);
		this.addMouseMotionListener(underlineThisAdapter);
		// apply the advanced setting
		setAdvanced(advanced);
	}

	/**
	 * Just paints a Rect arround the silly {@link CompoundExpression} to see the overlapping
	 */
	@Override
	protected void paintComponent(Graphics gc)
	{
		// TODO just recomment these two lines to see the Components
		// gc.setColor(Color.black);
		// gc.drawRect(0, 0, getWidth()-1, getHeight()-1);
		super.paintComponent(gc);
	}

	// workaround: the single CompoundExpressions overlap the others. Thus it is not possible to 
	// react on MouseEvents so they must be handed out to the other CompoundExpressions 
	// WORKAROUND: START
	@Override
	protected void processMouseEvent(MouseEvent e)
	{
		// let this component handle the event first
		super.processMouseEvent(e);
		try
		{
			// check if we have a next SmallStepProofNode
			ProofNode node = this.proofNode.getChildAt(0);
			// determine the SmallStepNodeComponent for the next proof node
			SmallStepNodeComponent nextComponent = (SmallStepNodeComponent) node.getUserObject();
			if (nextComponent != null)
			{
				// translate x/y to world coordinates
				int x = e.getX() + getX();
				int y = e.getY() + getY();
				// translate x/y to nextComponent coordinates
				x -= nextComponent.getX();
				y -= nextComponent.getY();
				// check if we have a CompoundExpression at x/y
				Component c = nextComponent.getComponentAt(x, y);
				if (c != null)
				{
					// translate and dispatch the event for the CompoundExpression
					MouseEvent ne = new MouseEvent(c, e.getID(), e.getWhen(), e.getModifiers(), x - c.getX(), y - c.getY(), e
							.getClickCount(), e.isPopupTrigger(), e.getButton());
					c.dispatchEvent(ne);
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException exn)
		{
			// ignore, no child then
		}
	}

	@Override
	protected void processMouseMotionEvent(MouseEvent e)
	{
		// let this component handle the event first
		super.processMouseMotionEvent(e);
		try
		{
			// check if we have a next SmallStepProofNode
			ProofNode node = this.proofNode.getChildAt(0);
			// determine the SmallStepNodeComponent for the next proof node
			SmallStepNodeComponent nextComponent = (SmallStepNodeComponent) node.getUserObject();
			if (nextComponent != null)
			{
				// translate x/y to world coordinates
				int x = e.getX() + getX();
				int y = e.getY() + getY();
				// translate x/y to nextComponent coordinates
				x -= nextComponent.getX();
				y -= nextComponent.getY();
				// check if we have a CompoundExpression at x/y
				Component c = nextComponent.getComponentAt(x, y);
				if (c != null)
				{
					// translate and dispatch the event for the CompoundExpression
					MouseEvent ne = new MouseEvent(c, e.getID(), e.getWhen(), e.getModifiers(), x - c.getX(), y - c.getY(), e
							.getClickCount(), e.isPopupTrigger(), e.getButton());
					c.dispatchEvent(ne);
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException exn)
		{
			// ignore, no child then
		}
	}
	// WORKAROUND: END
	
	/**
	 * Causes the expression and the resultexpression to recalculate their layout.
	 */
	public void reset()
	{
		this.compoundExpression.reset();
	}

	/**
	 * Sets whether the small step view operates in advanced or beginner mode.
	 * 
	 * @param advanced <code>true</code> to display only axiom rules in the
	 *          menu.
	 * @see SmallStepComponent#setAdvanced(boolean)
	 */
	void setAdvanced(boolean advanced)
	{
		// Fill the menu with menuitems
		ProofRule[] rulesFromModel = this.proofModel.getRules();
		Language lang = this.proofModel.getLanguage();
		this.menu = new JPopupMenu();
		this.menu = this.rulesMenu.getMenu(rulesFromModel, rulesFromModel, lang, this, "smallstep", advanced);
		// menu = new JPopupMenu();
		// if to many rules we will devide in menu and submenus, otherwise there
		// will be only seperators
		// between the rules coming from the different languages
		// if (rules.length > TOMANY)
		// menu.addSeparator();
		// menu.add(new MenuGuessItem());
		// menu.add(new MenuGuessTreeItem());
		this.menu.add(this.translateItem);
		this.rules.getMenuButton().setMenu(this.menu);
	}

	/**
	 * Resets the expression of the {@link #currentUnderlineExpression}, if it
	 * has changed, and informs the {@link #compoundExpression}-Renderer that is
	 * has changed. Causes all other nodes within the tree to free theire
	 * underlining.
	 * 
	 * @param expression
	 */
	void updateUnderlineExpression(Expression expression)
	{
		if (this.currentUnderlineExpression == expression)
		{
			return;
		}
		this.currentUnderlineExpression = expression;
		this.compoundExpression.setUnderlineExpression(this.currentUnderlineExpression);
		// free all the other nodes
		freeUnderliningSibling(true, Direction.DIRECTION_CHILD);
		freeUnderliningSibling(true, Direction.DIRECTION_PARENT);
	}

	/**
	 * Delegates the updating of the underline to
	 * {@link #updateUnderlineExpression(Expression)} with the expression stored
	 * within the label.
	 * 
	 * @param label The label from the {@link SmallStepRulesComponent}.
	 */
	void updateUnderlineExpression(SmallStepRuleLabel label)
	{
		updateUnderlineExpression(label.getStepExpression());
	}

	/**
	 * Delegates the updating of the underline to
	 * {@link #updateUnderlineExpression(Expression)} with the expression of the
	 * first unproved {@link ProofStep}.
	 * 
	 * @param button The button from the {@link SmallStepRulesComponent}.
	 */
	void updateUnderlineExpression(MenuButton button)
	{
		ProofStep[] steps = this.proofModel.remaining(this.proofNode);
		if (steps.length == 0)
		{
			return;
		}
		updateUnderlineExpression(steps[0].getExpression());
	}

	/**
	 * Called when an {@link JMenuItem} from the Menu was selected.<br>
	 * <br>
	 * No matter what item was selected the underlining of <b>all</b> nodes is
	 * cleared.<br>
	 * There are four possible actions to be done when an item is selected.<br>
	 * 1. a rule could have been selected, that should be used to proov this step.<br>
	 * 2. the model should guess the current node. 3. the model should complete
	 * the entire expression 4. the model should translate the current expression
	 * into core-syntax. This item mab be disable if there is no syntactical sugar
	 * within the expression.
	 * 
	 * @param item
	 */
	public void handleMenuActivated(JMenuItem item)
	{
		freeUnderlining();
		if (item instanceof MenuRuleItem)
		{
			MenuRuleItem ruleItem = (MenuRuleItem) item;
			ProofRule rule = ruleItem.getRule();
			try
			{
				this.proofModel.prove(rule, this.proofNode);
				this.rules.setRightRule();
			}
			catch (Exception exc)
			{
				// revert the changes at the menu
				this.rulesMenu.revertMenu();
				this.rulesMenu.save();
				this.rules.setWrongRule(rule);
			}
			fireNodeChanged();
		}
		else if (item instanceof MenuGuessItem)
		{
			try
			{
				this.proofModel.guess(this.proofNode);
			}
			catch (final ProofGuessException e)
			{
				fireRequstJumpToNode(e.getNode());
				SwingUtilities.invokeLater(new Runnable() {
					public void run()
					{
						JOptionPane
								.showMessageDialog(
										getTopLevelAncestor(),
										MessageFormat.format(Messages.getString("NodeComponent.5"), e.getMessage()), Messages.getString("NodeComponent.6"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					}
				});
			}
		}
		else if (item instanceof MenuGuessTreeItem)
		{
			try
			{
				this.proofModel.complete(this.proofNode);
			}
			catch (final ProofGuessException e)
			{
				fireRequstJumpToNode(e.getNode());
				SwingUtilities.invokeLater(new Runnable() {
					public void run()
					{
						JOptionPane
								.showMessageDialog(
										getTopLevelAncestor(),
										MessageFormat.format(Messages.getString("NodeComponent.7"), e.getMessage()), Messages.getString("NodeComponent.8"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					}
				});
			}
		}

		else if (item instanceof MenuTranslateItem)
		{
			int answer = 1;
			if (this.proofModel.containsSyntacticSugar(this.proofNode, false))
			{
				String[] answers =
				{
						Messages.getString("NodeComponent.0"), Messages.getString("NodeComponent.1"), Messages.getString("NodeComponent.2") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				answer = JOptionPane.showOptionDialog(getTopLevelAncestor(), Messages.getString("NodeComponent.3"), //$NON-NLS-1$
						Messages.getString("NodeComponent.4"), //$NON-NLS-1$
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, answers, answers[0]);
			}
			switch (answer)
			{
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
	}

	/**
	 * Sets the top-left position where the stuff should be appear
	 * 
	 * @param pOrigin
	 */
	public void setOrigion(Point pOrigin)
	{
		this.origin = pOrigin;
	}

	/**
	 * Delegate the call to the {@link Component#setBounds(int, int, int, int)}
	 * method.<br>
	 * The position comes directly from the {@link #origin}. The size is a
	 * compined needed dimension of the {@link #ruleDimension} and the
	 * {@link #expressionDimension}.
	 */
	public void setBounds()
	{
		setBounds(this.origin.x, this.origin.y, this.ruleDimension.width + this.expressionDimension.width
				+ this.spacing, this.actualRuleHeight + this.actualExpressionHeight + this.spacing);
	}
	
	
	/**
	 * Delegate the call to the {@link Component#setBounds(int, int, int, int)}
	 * method.<br>
	 * The position comes directly from the {@link #origin}. The size is a
	 * compined needed dimension of the {@link #ruleDimension} and the
	 * {@link #expressionDimension}.
	 */
	public void setBounds(int space)
	{
		setBounds(this.origin.x, this.origin.y, this.ruleDimension.width + this.expressionDimension.width
				+ this.spacing, this.actualRuleHeight + this.actualExpressionHeight + this.spacing+space);
	}

	/**
	 * Causes an update of the {@link #compoundExpression} and the
	 * {@link #translateItem}.<br>
	 * Hands the store to the expression if memory is enabled.<br>
	 * Whether the expression of the {@link #proofNode} contains syntactical sugar
	 * the {@link #translateItem} is enabled or disabled at this point.
	 */
	public void update()
	{
		this.compoundExpression.setExpression(this.proofNode.getExpression());
		if (this.memoryEnabled)
		{
			this.compoundExpression.setEnvironment(this.proofNode.getStore());
		}
		else
		{
			this.compoundExpression.setEnvironment(null);
		}
		this.translateItem.setEnabled(this.translator.containsSyntacticSugar(this.proofNode.getExpression(), true));
	}

	//
	// Stuff for the rules
	// 
	/**
	 * Returns the minmal size the rules need to render themself.<br>
	 * The {@link #actualRuleHeight} is initiated at this point with the minimum
	 * height.
	 * 
	 * @return The minimum size the rules need to render themself.
	 */
	public Dimension getMinRuleSize()
	{
		this.ruleDimension = this.rules.getNeededSize(this.underlineRuleAdapter);
		this.actualRuleHeight = this.ruleDimension.height;
		return this.ruleDimension;
	}

	/**
	 * Returns the current {@link #ruleDimension}.<br>
	 * This is not necessarily the minimum Size. It may be altered by
	 * {@link #setActualRuleHeight(int)} and {@link #setMaxRuleWidth(int)}.
	 * 
	 * @return Returns the currently set size of the rules.
	 */
	public Dimension getRuleSize()
	{
		return this.ruleDimension;
	}

	/**
	 * Sets the width the {@link #rules} have to use.<br>
	 * This is needed because all rules, no matter how big they are, need to use
	 * all the same size.
	 * 
	 * @param maxRuleWidth The width of the biggest rule within the tree.
	 */
	public void setMaxRuleWidth(int maxRuleWidth)
	{
		this.rules.setActualWidth(maxRuleWidth);
		this.ruleDimension.width = maxRuleWidth;
	}

	/**
	 * Sets the actual height the rule should use.<br>
	 * This is needed because right of this {@link #rules} item the expression of
	 * the child nodes is located and they needed to be aligned.
	 * 
	 * @param pActualRuleHeight
	 */
	public void setActualRuleHeight(int pActualRuleHeight)
	{
		this.actualRuleHeight = pActualRuleHeight;
	}

	/**
	 * Returns the actual rule height.
	 * 
	 * @return The actual rule height.
	 */
	public int getActualRuleHeight()
	{
		return this.actualRuleHeight;
	}

	/**
	 * Returns the top position of the {@link #rules}.<br>
	 * That actualy is the bottom position of the {@link #compoundExpression}
	 * added with some {@link #spacing}.
	 * 
	 * @return
	 */
	public int getRuleTop()
	{
		return this.actualExpressionHeight + this.spacing;
	}

	/**
	 * Hides the rules.<br>
	 * This is done if the this node is the last node witin the tree. So no
	 * further rules could be applied.
	 */
	public void hideRules()
	{
		this.rules.setVisible(false);
	}

	/**
	 * Unhides the rules.
	 */
	public void showRules()
	{
		this.rules.setVisible(true);
	}

	/**
	 * Causes the rules to get places.<br>
	 * The top position of the rules is chosen that the {@link #rules}-item is
	 * verticaly centered in the space available.
	 */
	public void placeRules()
	{
		int top = getRuleTop() + (this.actualRuleHeight - this.ruleDimension.height) / 2;
		this.rules.setBounds(0, top, this.ruleDimension.width, this.ruleDimension.height);
	}
	
	/**
	 * Causes the rules to get places moved by y<br>
	 * 
	 * The top position of the rules is chosen that the {@link #rules}-item is
	 * verticaly centered in the space available.
	 * @param y the amount to move
	 */
	public void placeRules(int y)
	{
		int top = getRuleTop() + (this.actualRuleHeight - this.ruleDimension.height) / 2 +y;
		this.rules.setBounds(0, top, this.ruleDimension.width, this.ruleDimension.height);
	}

	/**
	 * Causes every node, including this one, to get freed from the underlining.
	 */
	private void freeUnderlining()
	{
		freeUnderliningSibling(false, Direction.DIRECTION_CHILD);
		freeUnderliningSibling(false, Direction.DIRECTION_PARENT);
	}

	/**
	 * Frees every node in the given direction to be freed from the underlining.
	 * If <i>ignoreThis</i> is <i>false</i> the current nodes is freed aswell.
	 * 
	 * @param ignoreThis Whether the current node should not be freed aswell.
	 * @param direction The direction how the freeing should be done.
	 */
	private void freeUnderliningSibling(boolean ignoreThis, Direction direction)
	{
		if (!ignoreThis)
		{
			this.compoundExpression.setUnderlineExpression(null);
		}
		SmallStepProofNode nextNode = null;
		switch (direction)
		{
			case DIRECTION_CHILD:
				try
				{
					nextNode = this.proofNode.getFirstChild();
				}
				catch (Exception e)
				{
					// nothing to do
				}
				break;
			case DIRECTION_PARENT:
				nextNode = this.proofNode.getParent();
				break;
		}
		if (nextNode == null)
		{
			// no next node, so we're done here
			return;
		}
		SmallStepNodeComponent nextNodeComponent = (SmallStepNodeComponent) nextNode.getUserObject();
		nextNodeComponent.freeUnderliningSibling(false, direction);
	}

	//
	// Stuff for the expressions
	// 
	/**
	 * Returns the minimum size needed to correctly render the {@link expression}.
	 * The {@link #actualExpressionHeight} is initiated with the heigth of the
	 * minimum size.
	 * 
	 * @param pMaxWidth Max width is given for the entire component.
	 * @return
	 */
	public Dimension checkNeededExpressionSize(int pMaxWidth)
	{
		int maxWidth = pMaxWidth;
		maxWidth -= this.ruleDimension.width + this.spacing;
		this.expressionDimension = this.compoundExpression.getNeededSize(maxWidth);
		// use the calculated expression height for the actual height
		// until it will be changed by the SmallStepComponent
		this.actualExpressionHeight = this.expressionDimension.height;
		return this.expressionDimension;
	}

	/**
	 * Returns the size of the expression.
	 * 
	 * @return
	 */
	public Dimension getExpressionSize()
	{
		return this.expressionDimension;
	}

	/**
	 * Sets the actual expression height.
	 * 
	 * @param pActualExpressionHeight
	 */
	public void setActualExpressionHeight(int pActualExpressionHeight)
	{
		this.actualExpressionHeight = pActualExpressionHeight;
	}

	/**
	 * Returns the actual height of the expression.
	 * 
	 * @return
	 */
	public int getActualExpressionHeight()
	{
		return this.actualExpressionHeight;
	}

	/**
	 * Causes the {@link #compoundExpression} to get placed.<br>
	 * The left position of the expression is actualy the width of the rules added
	 * with some {@link #spacing}. If the expression is lower than the rulelabels the
	 * expression will be centerd.
	 */
	public void placeExpression()
	{
		if (this.expressionDimension.height < this.actualExpressionHeight)
		{
			int diff = this.actualExpressionHeight - this.expressionDimension.height;
			this.compoundExpression.setBounds(this.ruleDimension.width + this.spacing, diff / 2,
					this.expressionDimension.width, this.expressionDimension.height);
		}
		else
		{
			this.compoundExpression.setBounds(this.ruleDimension.width + this.spacing, 0, this.expressionDimension.width,
					this.actualExpressionHeight);
		}
	}

	/**
	 * adds a {@link SmallStepNodeListener} to the listenerList of the component
	 *
	 * @param listener the SmallStepNodeListener to add
	 */
	public void addSmallStepNodeListener(SmallStepNodeListener listener)
	{
		this.listenerList.add(SmallStepNodeListener.class, listener);
	}

	/**
	 * removes a {@link SmallStepNodeListener} from the listenerList of the component
	 *
	 * @param listener
	 */
	public void removeSmallStepNodeListener(SmallStepNodeListener listener)
	{
		this.listenerList.remove(SmallStepNodeListener.class, listener);
	}

	/***/
	private void fireNodeChanged()
	{
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2)
		{
			if (listeners[i] != SmallStepNodeListener.class)
			{
				continue;
			}
			((SmallStepNodeListener) listeners[i + 1]).nodeChanged(this);
		}
	}

	/***/
	private void fireRequstJumpToNode(ProofNode node)
	{
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2)
		{
			if (listeners[i] != SmallStepNodeListener.class)
			{
				continue;
			}
			((SmallStepNodeListener) listeners[i + 1]).requestJumpToNode(node);
		}
	}

	/**
	 * Returns the compoundExpression.
	 * 
	 * @return The compoundExpression.
	 * @see #compoundExpression
	 */
	public CompoundExpression<Location, Expression> getCompoundExpression()
	{
		return this.compoundExpression;
	}
}
