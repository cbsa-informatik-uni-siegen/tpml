package de.unisiegen.tpml.graphics.subtyping;

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

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.subtyping.SubTypingModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.components.LabelComponent;
import de.unisiegen.tpml.graphics.components.MenuButton;
import de.unisiegen.tpml.graphics.components.MenuButtonListener;
import de.unisiegen.tpml.graphics.components.MenuEnterTypeItem;
import de.unisiegen.tpml.graphics.components.MenuGuessItem;
import de.unisiegen.tpml.graphics.components.MenuGuessTreeItem;
import de.unisiegen.tpml.graphics.components.MenuRuleItem;
import de.unisiegen.tpml.graphics.components.NailSymbolComponent;
import de.unisiegen.tpml.graphics.components.RulesMenu;
import de.unisiegen.tpml.graphics.components.TypeComponent;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringToHTML;
import de.unisiegen.tpml.graphics.smallstep.SmallStepComponent;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;
import de.unisiegen.tpml.graphics.typeinference.TypeInferenceComponent;

/**
 * Graphical representation of a {@link SubTypingProofNode }.<br>
 * <br>
 * A usual look of a node may be given in the following pictur. It shows the
 * second node within the tree of the SubTyping of the Expression:
 * <code>let rec f = lambda x. if x = 0 then 1 else x * f (x-1) in f 3</code><br>
 * <img src="../../../../../../images/SubTypingnode.png" /><br>
 * <br>
 * This node is actualy build using 4 components. The following scheme
 * illustrates the layouting of the single components.<br>
 * <img src="../../../../../../images/SubTypingnode_scheme.png" /><br>
 * <br>
 * The first rectangle represents the {@link #indexLabel} The second rectanle
 * represents the entire {@link #typeComponent}. The last element in the first 
 * row is the {@link #typeComponent2}.
 * If the node is not completly evaluated only the four dots are drawn.<br>
 * In the next row there is only one rectangle containing the rule. In the case
 * of the previous image the {@link #ruleLabel} is shown, but as long as the
 * node has not been evaluated with a rule there would be located the
 * {@link #ruleButton}.That could be used to give the user
 * the possibilty to enter a type by himself.<br>
 * The bit of free space between the top and the bottom row aswell as between
 * the indexLabel and the expression is given in pixels in the {@link #spacing}.
 * <br>
 * Within the {@link SubTypingComponent} the
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
 * @see de.unisiegen.tpml.graphics.subtyping.SubTypingView
 * @see de.unisiegen.tpml.graphics.subtyping.SubTypingComponent
 * @see de.unisiegen.tpml.graphics.tree.TreeNodeComponent
 */
public class SubTypingNodeComponent extends JComponent implements TreeNodeComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6671706090992083026L;

	/**
	 * The Model this node can work with to guess, enter type or do Coresyntax
	 * transaltion
	 */
	private SubTypingModel proofModel;

	/**
	 * The Origin {@link SubTypingProofNode} this node represents.
	 */
	private SubTypingProofNode proofNode;

	/**
	 * The calculated dimension for this node in pixels.
	 */
	private Dimension dimension;

	/**
	 * The spacing that should be set free between to components within the node.
	 */
	private int spacing;

	/**
	 * The label containing the <i>(x)</i> text at the beginning.
	 */
	private JLabel indexLabel;

	/**
	 * The label containing the A and the Nail needed for RecSubTypingNodes
	 */
	private JLabel subTypingRecA;

	/**
	 * The Component drawing the label subTypingRecA
	 */
	private LabelComponent label;
	
	/**
	 * The nail Symbol of the A
	 */
	private NailSymbolComponent nail;

	/**
	 * The sub type sign "<:"
	 */
	private JLabel subType;

	/**
	 * The label component containing the subtype sign
	 */
	private LabelComponent lcSubType;

	/**
	 * The {@link TypeComponent} containing the left type of this node
	 * 
	 */
	private TypeComponent typeComponent;

	/**
	 * The {@link TypeComponent} containing the right type of this node
	 * 
	 */
	private TypeComponent typeComponent2;

	/**
	 * The {@link MenuButton} the user can use to do the actions.
	 */
	private MenuButton ruleButton;

	/**
	 * The {@link JLabel} showing the resulting type of this node, once the node
	 * has been evaluated.
	 */
	private JLabel typeLabel;

	/**
	 * The {@link JLabel} showing the information about the rule, once the rule
	 * has been evaluated.
	 */
	private JLabel ruleLabel;
	
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
	 * Constructor for a SubTypingNodeComponent<br>
	 * <br>
	 * All elements needed within the node will be created and added to the
	 * component. Some of them will be hidden at first ( the {@link #ruleLabel}) 
	 * because they are not needed but they are always there.<br>
	 * 
	 * @param node The origin ProofNode
	 * @param model The model
	 */
	public SubTypingNodeComponent ( SubTypingProofNode node, SubTypingModel model ) {
		super ( );
		this.proofNode = node;
		this.proofModel = model;
		this.dimension = new Dimension ( 0, 0 );
		this.spacing = 10;
		this.indexLabel = new JLabel ( );
		this.indexLabel.addMouseListener ( new OutlineMouseListener ( this ) );
		add ( this.indexLabel );
		this.subTypingRecA = new JLabel ( );
		this.label = new LabelComponent ( );
		add ( this.label );
		this.nail = new NailSymbolComponent();
		add(this.nail);
		this.typeComponent = new TypeComponent ( );
		this.typeComponent.setText ( "" ); //$NON-NLS-1$
		this.typeComponent.addMouseListener ( new OutlineMouseListener ( this ) );
		this.subType = new JLabel ( "<:" ); //$NON-NLS-1$
		this.lcSubType = new LabelComponent ( );
		add ( this.lcSubType );
		add ( this.typeComponent );
		this.typeComponent2 = new TypeComponent ( );
		this.typeComponent2.setText ( "" ); //$NON-NLS-1$
		this.typeComponent2.addMouseListener ( new OutlineMouseListener ( this ) );
		add ( this.typeComponent2 );
		changeNode ( );
		/*
		 * Create both, the ruleButton for selecting the rule and the label, that
		 * will be displayed later
		 */
		this.ruleButton = new MenuButton ( );
		add ( this.ruleButton );
		this.ruleButton.setVisible ( true );
		this.ruleLabel = new JLabel ( );
		add ( this.ruleLabel );
		this.ruleLabel.setVisible ( false );
		this.typeLabel = new JLabel ( );
		add ( this.typeLabel );
		this.typeLabel.setText ( " <: " ); //$NON-NLS-1$
		this.typeLabel.addMouseListener ( new OutlineMouseListener ( this ) );

		/*
		 * Create the PopupMenu for the menu button
		 */
		menu = new JPopupMenu ( );
		ProofRule[] rules = this.proofModel.getRules ( );
		if ( rules.length > 0 ) {
			int group = rules[0].getGroup ( );
			for ( ProofRule r : rules ) {
				if ( r.getGroup ( ) != group ) {
					menu.addSeparator ( );
				}
				menu.add ( new MenuRuleItem ( r ) );
				group = r.getGroup ( );
			}
		}
		menu.addSeparator ( );
		menu.add ( new MenuEnterTypeItem ( ) );
		menu.add ( new MenuGuessItem ( ) );
		menu.add ( new MenuGuessTreeItem ( ) );
		this.ruleButton.setMenu ( menu );
		/*
		 * Connect the handling of the ruleButton
		 */
		this.ruleButton.addMenuButtonListener ( new MenuButtonListener ( ) {
			public void menuClosed ( @SuppressWarnings ( "unused" )
			MenuButton button ) {
			// Nothing to do
			}

			public void menuItemActivated ( @SuppressWarnings ( "unused" )
			MenuButton button, final JMenuItem source ) {
				// setup a wait cursor for the toplevel ancestor
				final Container toplevel = getTopLevelAncestor ( );
				final Cursor cursor = toplevel.getCursor ( );
				toplevel.setCursor ( new Cursor ( Cursor.WAIT_CURSOR ) );
				// avoid blocking the popup menu
				SwingUtilities.invokeLater ( new Runnable ( ) {
					@SuppressWarnings ( "synthetic-access" )
					public void run ( ) {
						// handle the menu action
						SubTypingNodeComponent.this.handleMenuActivated ( source );
						// wait for the repaint before resetting the cursor
						SwingUtilities.invokeLater ( new Runnable ( ) {
							public void run ( ) {
								// reset the cursor
								toplevel.setCursor ( cursor );
							}
						} );
					}
				} );
			}
		} );

	}

	/**
	 * Causes the types recalculate their layout.
	 */
	public void reset ( ) {
		this.typeComponent.reset ( );
		this.typeComponent2.reset ( );
	}

	/**
	 * Sets the index that will be displayed in front of the node
	 * 
	 * @param index The index
	 */
	public void setIndex ( int index ) {
		this.indexLabel.setText ( "(" + index + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Causes the SubTypingNodeComponent to updated theire expression and
	 * environment.
	 */
	public void changeNode ( ) {
		this.subTypingRecA.setText ( "" ); //$NON-NLS-1$
		this.label.setLabel ( this.subTypingRecA );

		// if the node is RecSubTypingNode we have to render the A and set the Tooltip
		if ( this.proofNode instanceof RecSubTypingProofNode ) {
			this.subTypingRecA.setText ( "A" ); //$NON-NLS-1$
			this.label.setLabel ( this.subTypingRecA );
			RecSubTypingProofNode node = ( RecSubTypingProofNode ) this.proofNode;

			//build the tooltip
			String tooltip = "<html>{"; //$NON-NLS-1$
			SeenTypes < DefaultSubType > seenTypes = node.getSeenTypes ( );
			for ( DefaultSubType s : seenTypes ) {
				tooltip += "<font color=\"#FF0000\"> (</font>"; //$NON-NLS-1$
				tooltip += PrettyStringToHTML.toHTMLString ( s.toPrettyString ( ) );
				tooltip += "<font color=\"#FF0000\">) </font>"; //$NON-NLS-1$
			}
			tooltip += "}<html>"; //$NON-NLS-1$
			this.label.setToolTipText ( tooltip );

		}

		this.typeComponent.setType ( this.proofNode.getLeft ( ) );
		this.lcSubType.setLabel ( this.subType );
		this.typeComponent2.setType ( this.proofNode.getRight ( ) );
	}

	/**
	 * Places all elements one after another.<br>
	 * <br>
	 * First the label, the expression and the "::" will be placed, if the node is
	 * already prooven the {@link #typeLabel} will be placed aswell. The
	 * {@link #dimension} will be rescaled with every item that is placed and with
	 * all items, the height of the dimension will set to the current maximum.<br>
	 * <br>
	 * When all item of the top row are placed the {@link #ruleButton} or
	 * {@link #ruleLabel} will be placed depending whether
	 * the node is evaluated, it is not evaluated or the user previously selected
	 * <i>Enter type</i>.
	 * 
	 * @param pMaxWidth The maximum amount of pixels available to place the
	 *          elements.
	 */
	private void placeElements ( int pMaxWidth ) {
		int maxWidth = pMaxWidth;
		// get the size for the index at the beginning: (x)
		FontMetrics fm = AbstractRenderer.getTextFontMetrics ( );
		Dimension labelSize = new Dimension ( fm.stringWidth ( this.indexLabel.getText ( ) ), fm.getHeight ( ) );
		this.dimension.setSize ( labelSize.width, labelSize.height );
		Dimension labelComponentSize = new Dimension ( fm.stringWidth ( this.subTypingRecA.getText ( ) ), fm.getHeight ( ) );
		this.dimension.width += labelComponentSize.width + this.spacing;
		Dimension lcSubtypeSize = new Dimension ( fm.stringWidth ( this.subType.getText ( ) ), fm.getHeight ( ) );
		Dimension nailComponentSize = new Dimension (fm.stringWidth ( "--" ), fm.getHeight ( ));
		this.dimension.width += nailComponentSize.width + this.spacing;
		this.dimension.width += lcSubtypeSize.width + this.spacing;
		// there will be a bit spacing between the index label and first type
		this.dimension.width += this.spacing;
		// the index shrinkens the max size for the types
		maxWidth -= labelSize.width;
		// get the needed size for the types
		Dimension expSize = this.typeComponent.getNeededSize ( maxWidth );
		this.dimension.width += expSize.width + this.spacing;
		this.dimension.height = Math.max ( expSize.height, this.dimension.height );
		/*Dimension expSize2 = this.typeComponent2.getNeededSize ( maxWidth );
		this.dimension.width += expSize2.width + this.spacing;*/

		// get the neede size for the type

		this.proofNode.getRight ( ).toPrettyString ( );
		this.typeLabel.setText ( "" + this.proofNode.getLeft ( ) ); //$NON-NLS-1$
		//Dimension typeSize = this.typeLabel.getPreferredSize ( ) ;
		Dimension typeSize = this.typeComponent2.getNeededSize ( maxWidth );

		boolean broke = false;
		if ( ( this.dimension.width + typeSize.width + this.spacing ) > maxWidth ) //passt nicht mehr
		{
			this.dimension.width = Math.max ( this.dimension.width, expSize.width );
			this.dimension.height += typeSize.height;
			this.dimension.height += AbstractRenderer.getAbsoluteHeight ( );
			broke = true;
		} else {
			this.dimension.width += typeSize.width + this.spacing;
			this.dimension.height = Math.max ( typeSize.height, this.dimension.height );
		}

		// now place the components
		int posX = 0;
		this.indexLabel.setBounds ( posX, 0, labelSize.width, this.dimension.height );
		posX += labelSize.width + this.spacing;
		if ( this.proofNode instanceof RecSubTypingProofNode ) {
			this.label.setBounds ( posX, 0, labelComponentSize.width, this.dimension.height );
			posX += labelComponentSize.width + this.spacing;
			this.nail.setBounds ( posX, 0, nailComponentSize.width, this.dimension.height );
			posX += nailComponentSize.width + this.spacing;
		}
		this.typeComponent.setBounds ( posX, 0, expSize.width, this.dimension.height );
		int posXfront = posX;
		posX += expSize.width + this.spacing;
		this.lcSubType.setBounds ( posX, 0, lcSubtypeSize.width, this.dimension.height );
		posX += lcSubtypeSize.width + this.spacing;
		if ( broke ) {
			this.typeComponent2.setBounds ( posXfront, 0 + expSize.height + AbstractRenderer.getAbsoluteHeight ( ),
					typeSize.width, typeSize.height );
		} else {
			this.typeComponent2.setBounds ( posX, 0, typeSize.width, typeSize.height );
		}

		posX += typeSize.width;

		/*
		 * Check whether this is node is evaluated. If it is evaluated only the
		 * Label needs to get placed, if it is not evaluated yet the MenuButton
		 * needs to get placed.
		 */
		posX = labelSize.width + this.spacing;
		if ( this.proofNode.isProven ( ) ) {
			// place the menu label
			this.ruleLabel.setText ( this.proofNode.getRule ( ).toString ( ) );
			Dimension ruleLabelSize = this.ruleLabel.getPreferredSize ( );
			this.ruleLabel.setBounds ( posX, this.dimension.height + this.spacing, ruleLabelSize.width,
					ruleLabelSize.height );
			this.dimension.height += this.spacing + ruleLabelSize.height;
			this.dimension.width = Math.max ( this.dimension.width, ruleLabelSize.width + posX );
			// display only the label not the button
			this.ruleLabel.setVisible ( true );
			this.ruleButton.setVisible ( false );
		} else {
			// place the menu button
			Dimension buttonSize = this.ruleButton.getNeededSize ( );
			this.ruleButton.setBounds ( posX, this.dimension.height + this.spacing, buttonSize.width, buttonSize.height );

			this.dimension.height += this.spacing + buttonSize.height;
			this.dimension.width = Math.max ( this.dimension.width, buttonSize.width + posX );

			// display only the button not the label
			this.ruleLabel.setVisible ( false );
			this.ruleButton.setVisible ( true );
		}

	}

	/*
	 * Implementation of the eventhandling
	 */

	/** 
	 * Add the given node listener.
	 * 
	 * @param listener the new node listener
	 */
	public void addSubTypingNodeListener ( SubTypingNodeListener listener ) {
		this.listenerList.add ( SubTypingNodeListener.class, listener );
	}

	/**
	 * 
	 * Remove the given listener.
	 *
	 * @param listener the node listener which should be removed
	 */
	public void removeSubTypingNodeListener ( SubTypingNodeListener listener ) {
		this.listenerList.remove ( SubTypingNodeListener.class, listener );
	}

	private void fireNodeChanged ( ) {
		Object[] listeners = this.listenerList.getListenerList ( );
		for ( int i = 0; i < listeners.length; i += 2 ) {
			if ( listeners[i] != SubTypingNodeListener.class ) {
				continue;
			}
			( ( SubTypingNodeListener ) listeners[i + 1] ).nodeChanged ( this );
		}
	}

	private void fireRequestJumpToNode ( ProofNode node ) {
		Object[] listeners = this.listenerList.getListenerList ( );
		for ( int i = 0; i < listeners.length; i += 2 ) {
			if ( listeners[i] != SubTypingNodeListener.class ) {
				continue;
			}
			( ( SubTypingNodeListener ) listeners[i + 1] ).requestJumpToNode ( node );
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
	private void handleMenuActivated ( JMenuItem item ) {
		if ( item instanceof MenuRuleItem ) {
			MenuRuleItem ruleItem = ( MenuRuleItem ) item;
			ProofRule rule = ruleItem.getRule ( );
			try {
				this.proofModel.prove ( rule, this.proofNode );
				this.ruleButton.setToolTipText ( null );
			} catch ( Exception exc ) {
				// when the node could not be prooven with the selected
				// rule the menu button gets labeled with the given rule
				// and will be displayed in red
				this.ruleButton.setText ( "(" + rule.getName ( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
				this.ruleButton.setTextColor ( Color.RED );
				// determine the error text for the tooltip
				this.ruleButton.setToolTipText ( exc.getMessage ( ) );
			}
			fireNodeChanged ( );
		} else if ( item instanceof MenuGuessItem ) {
			try {
				this.proofModel.guess ( this.proofNode );
			} catch ( final ProofGuessException e ) {
				fireRequestJumpToNode ( e.getNode ( ) );
				SwingUtilities.invokeLater ( new Runnable ( ) {
					public void run ( ) {
						JOptionPane
								.showMessageDialog (
										getTopLevelAncestor ( ),
										MessageFormat.format ( Messages.getString ( "NodeComponent.5" ), e.getMessage ( ) ), Messages.getString ( "NodeComponent.6" ), JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} );
			}
			fireNodeChanged ( );
		} else if ( item instanceof MenuGuessTreeItem ) {
			try {
				this.proofModel.complete ( this.proofNode );
			} catch ( final ProofGuessException e ) {
				fireRequestJumpToNode ( e.getNode ( ) );
				SwingUtilities.invokeLater ( new Runnable ( ) {
					public void run ( ) {
						JOptionPane
								.showMessageDialog (
										getTopLevelAncestor ( ),
										MessageFormat.format ( Messages.getString ( "NodeComponent.7" ), e.getMessage ( ) ), Messages.getString ( "NodeComponent.8" ), JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} );
			}
			fireNodeChanged ( );
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
	public Dimension update ( int maxWidth ) {
		placeElements ( maxWidth );
		//this.menuTranslateItem.setEnabled ( this.translator.containsSyntacticSugar ( this.proofNode.getExpression ( ),
		//	true ) );
		return this.dimension;
	}

	/**
	 * Returns the number of pixels the children should be displayed indentated.
	 */
	public int getIndentationWidth ( ) {
		// XXX: calculate the indentation
		return this.indexLabel.getWidth ( );
	}

	/**
	 * Returns the point at the bottom of the node where the layout should attach the arrow.
	 */
	public Point getBottomArrowConnection ( ) {
		return new Point ( this.getX ( ) + this.indexLabel.getWidth ( ) / 2, this.getY ( ) + ( this.dimension.height / 2 ) );
	}

	/**
	 * Returns the point at the left of the node where the layout should attach the line to its parent.
	 */
	public Point getLeftArrowConnection ( ) {
		return new Point ( this.getX ( ), this.getY ( ) + this.indexLabel.getY ( ) + this.indexLabel.getHeight ( ) / 2 );
	}

	/**
	 * Just calls setBounds of the super class.
	 */
	@Override
	public void setBounds ( int x, int y, int width, int height ) {
		super.setBounds ( x, y, width, height );
	}

	/**
	 * Returns the typeComponent.
	 * 
	 * @return The typeComponent.
	 * @see #typeComponent
	 */
	public TypeComponent getTypeComponent ( ) {
		return this.typeComponent;
	}

	/**
	 * Returns the typeComponent2.
	 * 
	 * @return The typeComponent2.
	 * @see #typeComponent2
	 */
	public TypeComponent getTypeComponent2 ( ) {
		return this.typeComponent2;
	}

	/**
	 * Returns the proofNode.
	 * 
	 * @return The proofNode.
	 * @see #proofNode
	 */
	public SubTypingProofNode getProofNode ( ) {
		return this.proofNode;
	}

	/**
	 * Returns the indexLabel.
	 * 
	 * @return The indexLabel.
	 * @see #indexLabel
	 */
	public JLabel getIndexLabel ( ) {
		return this.indexLabel;
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
		((SubTypingModel) this.proofModel).setAdvanced ( pAdvanced );
		// Fill the menu with menuitems
		JPopupMenu newMenu = new JPopupMenu();
		ProofRule[] rulesOfModel = this.proofModel.getRules();
		if (rulesOfModel.length > 0)
		{
			int group = rulesOfModel[0].getGroup();
			for (ProofRule r : rulesOfModel)
			{
				{
					if (r.getGroup() != group)
					{
						newMenu.addSeparator();
					}

					newMenu.add(new MenuRuleItem(r));
					group = r.getGroup();
				}
			}
		}
		newMenu.addSeparator();
		newMenu.add(new MenuGuessItem());
		newMenu.add(new MenuGuessTreeItem());//this.rules.getMenuButton().setMenu(this.menu);
		//this.rules.getMenuButton().setMenu(menu);
		this.ruleButton.setMenu ( newMenu );
	}
}
