package de.unisiegen.tpml.graphics.typeinference;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.MessageFormat;
import java.util.ArrayList;

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
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typeinference.TypeFormula;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.components.CompoundExpressionTypeInference;
import de.unisiegen.tpml.graphics.components.MenuButton;
import de.unisiegen.tpml.graphics.components.MenuButtonListener;
import de.unisiegen.tpml.graphics.components.MenuGuessItem;
import de.unisiegen.tpml.graphics.components.MenuGuessTreeItem;
import de.unisiegen.tpml.graphics.components.MenuRuleItem;
import de.unisiegen.tpml.graphics.components.MenuTranslateItem;

/**
 * The graphical representation of a {@link TypeInferenceProofNode}.<br>
 * <br>
 * The <code>TypeInferenceCompnent</code> is a as complicated as the the node from the Smallstepper-GUI because of
 * the same reason: the rules applied to this node are pointing on the expression of the next node.<br>
 * Because the elements of this node need to get layed out in an arrangement with the elements of the parent-node
 * and the child-node, both components have a dimension containing the size they need for themeself and an
 * additional information on the actual size they need to fill(this is alway more or equal as the size of itself).
 * The <code>rules</code> of the parent node needs to get verticaly centered and aligned with the
 * <code>expression</code> of this node. Usualy the <code>rules</code> are higher than the
 * <code>expression</code>s. So the height of the {@link #expressionDimension} is less than the
 * {@link #actualExpressionHeight}. When the <code>expression</code> gets placed the
 * <code>actualExpressionHeight</code> is used for the height. The {@link #actualRuleHeight} of the parent node
 * than is the same as this <code>actualExpressionHeight</code>. <br>
 * 
 * 
 * @author michael
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.graphics.typeiference.TypeInferenceView
 * @see de.unisiegen.tpml.graphics.typeiference.TypeInferenceComponent
 * @see de.unisiegen.tpml.graphics.typeiference.TypeInferenceRulesComponent
 * @see de.unisiegen.tpml.graphics.typeiference.TypeInferenceRuleLabel
 * @see de.unisiegen.tpml.graphics.components.CompoundExpressionTypeInference
 */
public class TypeInferenceNodeComponent extends JComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5536947349690384851L;

	/**
	 * The origin {@link TypeInferenceProofNode}
	 */
	private TypeInferenceProofNode proofNode;

	/**
	 * The {@link TypeInferenceProofModel}
	 */
	private TypeInferenceProofModel proofModel;

	/**
	 * The {@link TypeInferenceRulesComponent} containing the Labels of already evaluated Rules and the MenuButton for
	 * Rules not yet evaluated.
	 */
	private TypeInferenceRulesComponent rules;

	/**
	 * The {@link Dimension} the rules needs to get drawn correctly.
	 */
	private Dimension ruleDimension;

	/**
	 * The actual height the rules should use. This is >= ruleDimension.height. This height is determined by the
	 * expression height of the child node.
	 */
	private int actualRuleHeight;

	/**
	 * The compound expression that is used to display the TypeInferenceProofNode
	 */
	private CompoundExpressionTypeInference compoundExpression;

	/**
	 * an <code>ArrayList</code> containing all {@link TypeFormula}
	 */
	private ArrayList<TypeFormula> allFormulasList;

	/**
	 * an <code>ArrayList</code> containing all {@link TypeSubstitution}
	 */
	private ArrayList<TypeSubstitution> substitutionList;

	/**
	 * The {@link Dimension} the expression needs to get drawn correctly.
	 */
	private Dimension expressionDimension;

	/**
	 * The actual height the expression should use. This is >= expressionDimension.height. This height is determined
	 * by the rule height of the parent node.
	 */
	private int actualExpressionHeight;

	/**
	 * The top-left-most position where the entire node should located.
	 */
	private Point origin;

	/**
	 * The free space in pixels that should be hold between the components
	 */
	private int spacing;

	/**
	 * Entry within the context menu. Need to hold this value because is value can change. This item is
	 * enabled/disable in the {@link #update()}-Method.
	 * 
	 * @see #update()
	 */
	private MenuTranslateItem translateItem;

	/**
	 * Translator that is used to determine whether the expression contains syntactical sugar.
	 */
	private LanguageTranslator translator;

	/**
	 * Adapter that is used to get added to every object that can cause the gui to underline an expression. It is
	 * bound to every {@link TypeInferenceRuleLabel} and to every {@link MenuButton}.
	 */
	private MouseMotionAdapter underlineRuleAdapter;

	/**
	 * The Advanced Setting
	 */
	private boolean advanced;

	/**
	 * Used internaly. When the underlining is cleared it will be done recursively over the entire tree. It needs to
	 * be done in two times one time directed to the parent and one time directed to the children. This enum is used
	 * by {@link TypeInferenceNodeComponent#freeUnderliningSibling(boolean, Direction)};
	 * 
	 * @author marcell
	 * 
	 */
	private enum Direction {
		DIRECTION_PARENT, DIRECTION_CHILD,
	}

	/**
	 * Constructs a TypeInferenceNodeComponent.<br>
	 * <br>
	 * All objects needed for one TypeInferenceNodeComponent are created and added to the {@link JComponent}.
	 * 
	 * @param pProofNode
	 *          The origin node from the model.
	 * @param pProofModel
	 *          The model.
	 * @param pTranslator
	 *          The translator that should be used to determine whether the Expression of this node contains
	 *          syntactical sugar.
	 * @param pSpacing
	 *          The spacing between the elements of the node.
	 * @param pAdvaced
	 *          Whether the small step view operates in advanced or beginner mode.
	 */
	public TypeInferenceNodeComponent (TypeInferenceProofNode pProofNode, TypeInferenceProofModel pProofModel,
			LanguageTranslator pTranslator, int pSpacing, boolean pAdvaced)
	{
		super();

		this.advanced = pAdvaced;

		this.proofNode = pProofNode;

		this.proofModel = pProofModel;

		this.translator = pTranslator;

		// this.currentUnderlineExpression = null;

		// the dimension for the rules initialy (0, 0)
		this.ruleDimension = new Dimension(0, 0);

		// the dimension for the expression initialy (0, 0)
		this.expressionDimension = new Dimension(0, 0);

		this.compoundExpression = new CompoundExpressionTypeInference();
		add(this.compoundExpression);

		this.rules = new TypeInferenceRulesComponent(pProofNode);
		add(this.rules);

		this.spacing = 10;

		this.translateItem = new MenuTranslateItem();

		// WORAROUND belongs to Workaround coming later...
		// needed
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		// WORAROUND belongs to Workaround coming later...

		this.rules.getMenuButton().addMenuButtonListener(new MenuButtonListener() {
			public void menuClosed(MenuButton source)
			{
				// nothing todo
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
						TypeInferenceNodeComponent.this.menuItemActivated(item);

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

		// TODO normaly nobdy needs it, but if it is removed the tooltip dose not function...
		// create the adapters that will be used to determine
		// whether an expression needs to get underlined
		MouseMotionAdapter underlineThisAdapter	= new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent event)
			{
				//TypeInferenceNodeComponent.this.updateUnderlineExpression((Expression) null);
			}
		};


		this.addMouseMotionListener(underlineThisAdapter);
		this.compoundExpression.addMouseMotionListener(underlineThisAdapter);

		// apply the advanced setting
		setAdvanced(this.advanced);
	}

	/**
	 * Just paints a Rect arround the silly BopoundExpression to see failure and the overlapping of the single 
	 * compoundExpressions
	 */
	@Override
	protected void paintComponent(Graphics gc)
	{
		// TODO just recomment these lines to make the components visable
		// gc.setColor(Color.BLACK);
    // gc.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
		super.paintComponent(gc);
	}

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

			// determine the TypeInferenceNodeComponent for the next proof node
			TypeInferenceNodeComponent nextComponent = (TypeInferenceNodeComponent) node.getUserObject();
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

			// determine the TypeInferenceNodeComponent for the next proof node
			TypeInferenceNodeComponent nextComponent = (TypeInferenceNodeComponent) node.getUserObject();
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
	 * 
	 */
	public void reset()
	{
		this.compoundExpression.reset();
	}

	/**
	 * Sets whether the small step view operates in advanced or beginner mode.
	 * 
	 * @param pAdvanced
	 *          <code>true</code> to display only axiom rules in the menu.
	 * 
	 * @see TypeInferenceComponent#setAdvanced(boolean)
	 */
	void setAdvanced(boolean pAdvanced)
	{
		// Fill the menu with menuitems
		JPopupMenu menu = new JPopupMenu();
		ProofRule[] rulesOfModel = this.proofModel.getRules();
		if (rulesOfModel.length > 0)
		{
			int group = rulesOfModel[0].getGroup();
			for (ProofRule r : rulesOfModel)
			{
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
		menu.addSeparator();
		menu.add(new MenuGuessItem());
		menu.add(new MenuGuessTreeItem());
		menu.add(this.translateItem);

		this.rules.getMenuButton().setMenu(menu);
	}


	/**
	 * Called when an {@link JMenuItem} from the Menu was selected.<br>
	 * <br>
	 * No matter what item was selected the underlining of <b>all</b> nodes is cleared.<br>
	 * There are four possible actions to be done when an item is selected.<br>
	 * 1. a rule could have been selected, that should be used to proov this step.<br>
	 * 2. the model should guess the current node. 3. the model should complete the entire expression 4. the model
	 * should translate the current expression into core-syntax. This item mab be disable if there is no syntactical
	 * sugar within the expression.
	 * 
	 * @param item
	 */
	void menuItemActivated(JMenuItem item)
	{
		if (item instanceof MenuRuleItem)
		{
			MenuRuleItem ruleItem = (MenuRuleItem) item;
			ProofRule rule = ruleItem.getRule();

			try
			{
				this.proofModel.prove(rule, this.proofNode, ((TypeInferenceComponent) getParent()).isAdvanced());
				this.rules.setRightRule();
			}
			catch (Exception exc)
			{
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
			// TODO this dose not function on all systems but on my system so I can not find the error.
			int answer = 1;
			// test if ther are more than one Expression
			int count = 0;
			for (int i = 0; i < this.proofNode.getAllFormulas().size(); i++)
			{
				if (this.proofNode.getAllFormulas().get(i).getExpression() != null)
				{
					count++;
				}
			}
			int an = 1;
			boolean all = false;
			if (count > 1)
			{
				String[] a =
				{ Messages.getString("TI.All"), Messages.getString("TI.First"), Messages.getString("TI.Cancel") };
				an = JOptionPane.showOptionDialog(getTopLevelAncestor(),
						Messages.getString("TI.AllOrFirst"), 
						Messages.getString("NodeComponent.4"),
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, a, a[0]);
			}
			if (an == 0)
			{
				all = true;
			}
			else if (an == 2)
			{
				return;
			}
			if (all)
			{
				try
				{
					if (this.proofNode == null)
					{
						System.out.println("Null!!!!");
						return;
					}

					this.proofModel.translateToCoreSyntax(this.proofNode, true, true);
				}
				catch (IllegalArgumentException e)
				{
					System.out.println(e);
				}
			}
			else
			{
				// runn through all Formulas
				for (int i = 0; i < this.proofNode.getAllFormulas().size(); i++)
				{
					if (this.proofNode.getAllFormulas().get(i).getExpression() != null)
					{
						if (this.proofModel.containsSyntacticSugar(this.proofNode, this.proofNode.getAllFormulas().get(i)
								.getExpression(), false))
						{
							String[] answers =
							{
									Messages.getString("NodeComponent.0"), Messages.getString("NodeComponent.1"), Messages.getString("NodeComponent.2") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							answer = JOptionPane.showOptionDialog(getTopLevelAncestor(), Messages.getString("NodeComponent.3"), //$NON-NLS-1$
									Messages.getString("NodeComponent.4"), //$NON-NLS-1$
									JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, answers, answers[0]);
							switch (answer)
							{
								case 0:
									try
									{
										this.proofModel.translateToCoreSyntax(this.proofNode, false, false);
									}
									catch (IllegalArgumentException exp)
									{
										JOptionPane.showMessageDialog(getTopLevelAncestor(), exp.getMessage(), "Error",
												JOptionPane.ERROR_MESSAGE);
									}
									break;
								case 1:
									try
									{
										this.proofModel.translateToCoreSyntax(this.proofNode, true, false);
									}
									catch (IllegalArgumentException exp)
									{
										JOptionPane.showMessageDialog(getTopLevelAncestor(), exp.getMessage(), "Error",
												JOptionPane.ERROR_MESSAGE);
									}

									break;
								case 2:
									break;
							}

						}
					}
				}

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
	 * Delegate the call to the {@link Component#setBounds(int, int, int, int)} method.<br>
	 * The position comes directly from the {@link #origin}. The size is a compined needed dimension of the
	 * {@link #ruleDimension} and the {@link #expressionDimension}.
	 * 
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
//	public void setBounds(int space)
//	{
//		setBounds(this.origin.x, this.origin.y, this.ruleDimension.width + this.expressionDimension.width
//				+ this.spacing, this.actualRuleHeight + this.actualExpressionHeight + this.spacing+space);
//	}

	/**
	 * Causes an update of the {@link #compoundExpression} and the {@link #translateItem}.<br>
	 * Hands the store to the expression if memory is enabled.<br>
	 * Whether the expression of the {@link #proofNode} contains syntactical sugar the {@link #translateItem} is
	 * enabled or disabled at this point.
	 * 
	 */
	public void update()
	{
		this.allFormulasList = this.proofNode.getAllFormulas();
		this.substitutionList = this.proofNode.getSubstitution();

		this.compoundExpression.setDefaultTypeSubstitutionList(this.substitutionList);
		this.compoundExpression.setTypeFormulaList(this.allFormulasList);
		
		//TODO find out if there is an child or if it is proofen
		try
		{
			// check if we have a next SmallStepProofNode
			ProofNode node = this.proofNode.getChildAt(0);
			this.compoundExpression.setDragndropeabled(false);
		}
		catch (ArrayIndexOutOfBoundsException exn)
		{
			this.compoundExpression.setDragndropeabled(true);
		}
		
	

		// check if the expressions contain syntactic sugar

		// First, disable the Translator Item
		this.translateItem.setEnabled(false);

		for (int i = 0; i < this.allFormulasList.size(); i++)
		{
			Expression exp = this.allFormulasList.get(i).getExpression();
			if ((exp != null) && this.translator.containsSyntacticSugar(exp, true))
			{
				this.translateItem.setEnabled(true);
				break;
			}
		}

		this.compoundExpression.repaint();
		this.repaint();
	}

	//
	// Stuff for the rules
	// 

	/**
	 * Returns the minmal size the rules need to render themself.<br>
	 * The {@link #actualRuleHeight} is initiated at this point with the minimum height.
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
	 * This is not necessarily the minimum Size. It may be altered by {@link #setActualRuleHeight(int)} and
	 * {@link #setMaxRuleWidth(int)}.
	 * 
	 * @return Returns the currently set size of the rules.
	 */
	public Dimension getRuleSize()
	{
		return this.ruleDimension;
	}

	/**
	 * Sets the width the {@link #rules} have to use.<br>
	 * This is needed because all rules, no matter how big they are, need to use all the same size.
	 * 
	 * @param maxRuleWidth
	 *          The width of the biggest rule within the tree.
	 */
	public void setMaxRuleWidth(int maxRuleWidth)
	{
		this.rules.setActualWidth(maxRuleWidth);
		this.ruleDimension.width = maxRuleWidth;
	}

	/**
	 * Sets the actual height the rule should use.<br>
	 * This is needed because right of this {@link #rules} item the expression of the child nodes is located and they
	 * needed to be aligned.
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
	 * That actualy is the bottom position of the {@link #compoundExpression} added with some {@link #spacing}.
	 * 
	 * @return
	 */
	public int getRuleTop()
	{
		return this.actualExpressionHeight + this.spacing;
	}

	/**
	 * Hides the rules.<br>
	 * This is done if the this node is the last node witin the tree. So no further rules could be applied.
	 * 
	 */
	public void hideRules()
	{
		this.rules.setVisible(false);
	}

	/**
	 * Unhides the rules.
	 * 
	 */
	public void showRules()
	{
		this.rules.setVisible(true);
	}

	/**
	 * Causes the rules to get places.<br>
	 * The top position of the rules is chosen that the {@link #rules}-item is verticaly centered in the space
	 * available.
	 * 
	 */
	public void placeRules()
	{

		//int top = getRuleTop() + (this.actualRuleHeight - this.ruleDimension.height) / 2;
		int top = getRuleTop();
		this.rules.setBounds(0, top, this.ruleDimension.width, this.ruleDimension.height);
	}
	
	/**
	 * Causes the rules to get places moved by y<br>
	 * 
	 * The top position of the rules is chosen that the {@link #rules}-item is
	 * verticaly centered in the space available.
	 * @param y the amount to move
	 */
//	public void placeRules(int y)
//	{
//		int top = getRuleTop()+ y;
//		//+ (this.actualRuleHeight - this.ruleDimension.height) / 2;
//		this.rules.setBounds(0, top, this.ruleDimension.width, this.ruleDimension.height);
//	}
	/**
	 * Causes the rules to get places to a fix value y<br>
	 * 
	 * The top position of the rules is chosen that the {@link #rules}-item is
	 * verticaly centered in the space available.
	 * @param y the amount to move
	 */
//	public void placeRulesFix(int y)
//	{
//		//System.err.println("Diff"+(this.actualRuleHeight - this.ruleDimension.height));
//		//System.err.println("actual"+this.actualRuleHeight);
//		//System.err.println("dimension"+this.ruleDimension.height);
//		//System.err.println("Top: "+getRuleTop());
//		//System.err.println("y  : "+y);
//		int top = getRuleTop() + (this.actualRuleHeight+y - this.ruleDimension.height) / 2;
//		//System.err.println("top"+top);
//		//System.err.println("eigentlich:"+( getRuleTop() + (this.actualRuleHeight - this.ruleDimension.height) / 2));
//		this.rules.setBounds(0, top, this.ruleDimension.width, this.ruleDimension.height);
//	}

	/**
	 * Frees every node in the given direction to be freed from the underlining. If <i>ignoreThis</i> is <i>false</i>
	 * the current nodes is freed aswell.
	 * 
	 * @param ignoreThis
	 *          Whether the current node should not be freed aswell.
	 * @param direction
	 *          The direction how the freeing should be done.
	 */
	private void freeUnderliningSibling(boolean ignoreThis, Direction direction)
	{
		if (!ignoreThis)
		{
			// this.compoundExpression.setUnderlineExpression(null);
		}

		TypeInferenceProofNode nextNode = null;
		switch (direction)
		{
			case DIRECTION_CHILD:
				try
				{
					nextNode = this.proofNode.getFirstChild();
				}
				catch (Exception e)
				{
					// nothing todo
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

		TypeInferenceNodeComponent nextNodeComponent = (TypeInferenceNodeComponent) nextNode.getUserObject();
		nextNodeComponent.freeUnderliningSibling(false, direction);
	}

	//
	// Stuff for the expressions
	// 

	/**
	 * Returns the minimum size needed to correctly render the {@link expression}. The
	 * {@link #actualExpressionHeight} is initiated with the heigth of the minimum size.
	 * 
	 * @param pMaxWidth
	 *          Max width is given for the entire component.
	 * @return
	 */
	public Dimension checkNeededExpressionSize(int pMaxWidth)
	{
		int maxWidth = pMaxWidth;
		maxWidth -= this.ruleDimension.width + this.spacing;

		this.expressionDimension = this.compoundExpression.getNeededSize(maxWidth);

		// use the calculated expression height for the actual height
		// until it will be changed by the TypeInferenceComponent
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
	 * The left position of the expression is actualy the width of the rules added with some {@link #spacing}.
	 * 
	 */
	public void placeExpression()
	{
		if (this.expressionDimension.height < this.actualExpressionHeight )
		{
			int diff = this.actualExpressionHeight - this.expressionDimension.height;
			this.compoundExpression.setBounds(this.ruleDimension.width + this.spacing, 0+diff/2, this.expressionDimension.width,	this.expressionDimension.height);
		}
		else
		{
			this.compoundExpression.setBounds(this.ruleDimension.width + this.spacing, 0, this.expressionDimension.width,	this.actualExpressionHeight);
		}
	}

	public void addTypeInferenceNodeListener(TypeInferenceNodeListener listener)
	{
		this.listenerList.add(TypeInferenceNodeListener.class, listener);
	}

	public void removeTypeInferenceNodeListener(TypeInferenceNodeListener listener)
	{
		this.listenerList.remove(TypeInferenceNodeListener.class, listener);
	}

	private void fireNodeChanged()
	{
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2)
		{
			if (listeners[i] != TypeInferenceNodeListener.class)
			{
				continue;
			}

			((TypeInferenceNodeListener) listeners[i + 1]).nodeChanged(this);
		}
	}

	private void fireRequstJumpToNode(ProofNode node)
	{
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2)
		{
			if (listeners[i] != TypeInferenceNodeListener.class)
			{
				continue;
			}

			((TypeInferenceNodeListener) listeners[i + 1]).requestJumpToNode(node);
		}
	}

	/**
	 * @return the spacing
	 */
	public int getSpacing()
	{
		return this.spacing;
	}

	/**
	 * @param spacing the spacing to set
	 */
	public void setSpacing(int spacing)
	{
		this.spacing = spacing;
	}

}
