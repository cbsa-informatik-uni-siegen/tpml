package de.unisiegen.tpml.graphics.minimaltyping;

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
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.graphics.Messages;
import de.unisiegen.tpml.graphics.components.CompoundExpression;
import de.unisiegen.tpml.graphics.components.CompoundExpressionSubTyping;
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
 * Graphical representation of a {@link MinimalTypingProofNode }.<br>
 * <br>
 * A usual look of a node may be given in the following pictur. It shows the
 * second node within the tree of the MinimalTyping of the Expression:
 * <code>let rec f = lambda x. if x = 0 then 1 else x * f (x-1) in f 3</code><br>
 * <img src="../../../../../../images/MinimalTypingnode.png" /><br>
 * <br>
 * This node is actualy build using 4 components. The following scheme
 * illustrates the layouting of the single components.<br>
 * <img src="../../../../../../images/MinimalTypingnode_scheme.png" /><br>
 * <br>
 * The first rectangle represents the {@link #indexLabel} The second rectanle
 * represents the entire {@link #compoundExpression} including the
 * typeenvironment. The last element in the first row is the {@link #typeLabel}
 * containing the resulting type, it also is containing the <code>" :: "</code>.
 * If the node is not completly evaluated only the four dots are drawn.<br>
 * In the next row there is only one rectangle containing the rule. In the case
 * of the previous image the {@link #ruleLabel} is shown, but as long as the
 * node has not been evaluated with a rule there would be located the
 * {@link #ruleButton}. Another possible component that could be located at
 * this position is the {@link #typeEnter}. That could be used to give the user
 * the possibilty to enter a type by himself.<br>
 * The bit of free space between the top and the bottom row aswell as between
 * the indexLabel and the expression is given in pixels in the {@link #spacing}.
 * <br>
 * Within the {@link MinimalTypingComponent} the
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
 * @see de.unisiegen.tpml.graphics.MinimalTyping.MinimalTypingView
 * @see de.unisiegen.tpml.graphics.MinimalTyping.MinimalTypingComponent
 * @see de.unisiegen.tpml.graphics.tree.TreeNodeComponent
 * @see de.unisiegen.tpml.graphics.MinimalTyping.MinimalTypingEnterType
 */
public class MinimalTypingNodeComponent extends JComponent implements
		TreeNodeComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6671706090992083026L;

	/**
	 * The Model this node can work with to guess, enter type or do Coresyntax
	 * transaltion
	 */
	private MinimalTypingProofModel proofModel;

	/**
	 * The Origin {@link MinimalTypingProofNode} this node represents.
	 */
	private MinimalTypingProofNode proofNode;

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
	 * The {@link CompoundExpression} containing the expression of this node.
	 */
	private CompoundExpression < Identifier, Type > compoundExpression;

	//private CompoundExpressionSubTyping expression;

	//private CompoundExpression < Location, Expression > resultExpression;

	/**
	 * The typeComponent containing the type of this node
	 * 
	 */
	private TypeComponent typeComponent;
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
	 * The menuTranslate is one element within the menu. <br>
	 * Needs to get handled separatly because it can be enabled and disabled
	 * whether the expression is containing Syntactical Sugar.
	 */
	private MenuTranslateItem menuTranslateItem;

	/**
	 * The translator will be used to determin whether the expression contains
	 * syntactical sugor.
	 */
	private LanguageTranslator translator;

	/**
	 * Constructor for a MinimalTypingNode<br>
	 * <br>
	 * All elements needed within the node will be created and added to the
	 * component. Some of them will be hidden at first (the {@link #typeEnter},
	 * or the {@link #ruleLabel}) because they are not needed but they are always
	 * there.<br>
	 * 
	 * @param node The origin ProofNode
	 * @param model The model
	 * @param translator The translator of the model for the selected language
	 */
	public MinimalTypingNodeComponent ( MinimalTypingProofNode node,
			MinimalTypingProofModel model, LanguageTranslator translator ) {
		super ( );
		this.proofNode = node;
		this.proofModel = model;
		this.translator = translator;
		this.dimension = new Dimension ( 0, 0 );
		this.spacing = 10;
		this.indexLabel = new JLabel ( );
		this.indexLabel.addMouseListener ( new OutlineMouseListener ( this ) );
		add ( this.indexLabel );
		if ( proofNode.getEnvironment ( ) != null ) { // proofnode is an expression proof node
			
			
			this.compoundExpression = new CompoundExpression < Identifier, Type > ( );
			this.compoundExpression.addMouseListener ( new OutlineMouseListener (
					this ) );
			add ( this.compoundExpression );
			this.typeComponent = new TypeComponent ( );
			this.typeComponent
					.addMouseListener ( new OutlineMouseListener ( this ) );
			add ( this.typeComponent );
			
			this.typeLabel = new JLabel ( );
			add ( this.typeLabel );
			this.typeLabel.setText ( " :: " ); //$NON-NLS-1$
			
			
		} else { // proof node is a type proof node
			
			
			/*this.expression = new CompoundExpressionSubTyping ( );
			add ( this.expression );

			this.resultExpression = new CompoundExpression < Location, Expression > ( );
			add ( this.resultExpression );
			this.resultExpression.setAlternativeColor ( Color.LIGHT_GRAY );*/
			
			this.typeComponent = new TypeComponent ( );
			this.typeComponent
					.addMouseListener ( new OutlineMouseListener ( this ) );
			add ( this.typeComponent );
			this.typeComponent.setText ( "" );
			
			

		    this.typeComponent2 = new TypeComponent ( );
				this.typeComponent2
						.addMouseListener ( new OutlineMouseListener ( this ) );
				add ( this.typeComponent2 );
			    this.typeComponent2.setText ( "<:   " );
				
		}
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
		
		

		// this.typeLabel.addMouseListener ( new OutlineMouseListener ( this ) ) ;
		/*
		 * Create the PopupMenu for the menu button
		 */
		JPopupMenu menu = new JPopupMenu ( );
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
		menu.add ( this.menuTranslateItem = new MenuTranslateItem ( ) );
		this.ruleButton.setMenu ( menu );
		/*
		 * Connect the handling of the ruleButton
		 */
		this.ruleButton.addMenuButtonListener ( new MenuButtonListener ( ) {
			public void menuClosed ( MenuButton button ) {}

			public void menuItemActivated ( MenuButton button,
					final JMenuItem source ) {
				// setup a wait cursor for the toplevel ancestor
				final Container toplevel = getTopLevelAncestor ( );
				final Cursor cursor = toplevel.getCursor ( );
				toplevel.setCursor ( new Cursor ( Cursor.WAIT_CURSOR ) );
				// avoid blocking the popup menu
				SwingUtilities.invokeLater ( new Runnable ( ) {
					public void run ( ) {
						// handle the menu action
						MinimalTypingNodeComponent.this.handleMenuActivated ( source );
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
	 * Causes the expression and the resultexpression to recalculate their layout.
	 */
	public void reset ( ) {
		this.compoundExpression.reset ( );
		this.typeComponent.reset ( );
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
	 * Causes the {@link #compoundExpression} to updated theire expression and
	 * environment.
	 */
	public void changeNode ( ) {
		if ( proofNode.getEnvironment ( ) != null ) {
			this.compoundExpression.setExpression ( this.proofNode
					.getExpression ( ) );
			this.compoundExpression.setEnvironment ( this.proofNode
					.getEnvironment ( ) );
			this.typeComponent.setType ( this.proofNode.getType ( ) );
		} else {
			MinimalTypingTypesProofNode typeNode = ( MinimalTypingTypesProofNode ) proofNode;
			//this.expression.setType1 ( typeNode.getType ( ) );
			//this.expression.setType2 ( typeNode.getType2 ( ) );
			
			this.typeComponent.setType ( typeNode.getType ( ) );
			this.typeComponent2.setType ( typeNode.getType2 ( ) );
			
		}

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
	private void placeElements ( int maxWidth ) {
		if ( proofNode.getEnvironment ( ) != null ) { // proofNode instance of expression proof node
			// get the size for the index at the beginning: (x)
			FontMetrics fm = AbstractRenderer.getTextFontMetrics ( );
			Dimension labelSize = new Dimension ( fm.stringWidth ( this.indexLabel
					.getText ( ) ), fm.getHeight ( ) );
			this.dimension.setSize ( labelSize.width, labelSize.height );
			// there will be a bit spacing between the index label and the expression
			this.dimension.width += this.spacing;
			// the index shrinkens the max size for the expression
			maxWidth -= labelSize.width;

			// get the needed size for the expression
			Dimension expSize = this.compoundExpression.getNeededSize ( maxWidth );
			this.dimension.width += expSize.width;
			this.dimension.height = Math.max ( expSize.height,
					this.dimension.height );
			// get the neede size for the type
			// changes benjamin to see the type in typechecker
			// TODO insert second condition
			//typeRenderer = new PrettyStringRenderer();
			if ( this.proofNode.getType ( ) != null /*&& this.proofNode.isFinished()*/) {

				//typeRenderer.setPrettyString (this.proofNode.getType ().toPrettyString ());
				//FontMetrics fm = AbstractRenderer.getTextFontMetrics ();

				this.proofNode.getType ( ).toPrettyString ( );
				this.typeLabel.setText ( " :: " + this.proofNode.getType ( ) ); //$NON-NLS-1$
			} else {
				this.typeLabel.setText ( " :: " ); //$NON-NLS-1$
			}
			//Dimension typeSize = this.typeLabel.getPreferredSize ( ) ;
			Dimension typeSize = typeComponent.getNeededSize ( maxWidth );

			this.dimension.width += typeSize.width;
			this.dimension.height = Math.max ( typeSize.height,
					this.dimension.height );
			// now place the components
			int posX = 0;
			this.indexLabel.setBounds ( posX, 0, labelSize.width,
					this.dimension.height );
			posX += labelSize.width + this.spacing;
			this.compoundExpression.setBounds ( posX, 0, expSize.width,
					this.dimension.height );
			posX += expSize.width;
			//this.typeLabel.setBounds ( posX , 0 , typeSize.width , this.dimension.height ) ;
			//

			//this.typeLabel.setBounds(posX, 0, typeSize.width, this.dimension.height);
			//ShowBonds sb = new ShowBonds();
			//sb.setType (this.proofNode.getType() );
			//ToListenForMouseContainer tlfmc = new ToListenForMouseContainer();

			this.typeComponent.setBounds ( posX, 0, typeSize.width,
					typeSize.height );
			//typeRenderer.render (typePosition, 0,typeRenderer.getNeededSize (maxWidth).width ,typeRenderer.getNeededSize (maxWidth).height, this.getGraphics (), sb, tlfmc);

			posX += typeSize.width;
			//posX += typeSize.width;

			/*
			 * Check whether this is node is evaluated. If it is evaluated only the
			 * Label needs to get placed, if it is not evaluated yet the MenuButton
			 * needs to get placed.
			 */
			posX = labelSize.width + this.spacing;
			if ( this.proofNode.isProven ( ) ) {
				// place the menu label
				this.ruleLabel.setText ( "(" + this.proofNode.getRule ( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
				Dimension ruleLabelSize = this.ruleLabel.getPreferredSize ( );
				this.ruleLabel.setBounds ( posX, this.dimension.height
						+ this.spacing, ruleLabelSize.width, ruleLabelSize.height );
				this.dimension.height += this.spacing + ruleLabelSize.height;
				this.dimension.width = Math.max ( this.dimension.width,
						ruleLabelSize.width + posX );
				// display only the label not the button
				this.ruleLabel.setVisible ( true );
				this.ruleButton.setVisible ( false );
			} else {
				// place the menu button
				Dimension buttonSize = this.ruleButton.getNeededSize ( );
				this.ruleButton.setBounds ( posX, this.dimension.height
						+ this.spacing, buttonSize.width, buttonSize.height );
				this.dimension.height += this.spacing + buttonSize.height;
				this.dimension.width = Math.max ( this.dimension.width,
						buttonSize.width + posX );
				// display only the button not the label
				this.ruleLabel.setVisible ( false );
				this.ruleButton.setVisible ( true );

			}
			
			
		} else { // proofNode instance of type proof node
			
			
			//		 get the size for the index at the beginning: (x)
			FontMetrics fm = AbstractRenderer.getTextFontMetrics ( );
			Dimension labelSize = new Dimension ( fm.stringWidth ( this.indexLabel
					.getText ( ) ), fm.getHeight ( ) );
			this.dimension.setSize ( labelSize.width, labelSize.height );

			// there will be a bit spacing between the index label and the expression
			this.dimension.width += this.spacing;

			// the index shrinkens the max size for the expression
			maxWidth -= labelSize.width;

			//  get the needed size for the expression
			Dimension expSize = this.typeComponent.getNeededSize ( maxWidth );
			this.dimension.width += expSize.width;
			this.dimension.height = Math.max ( expSize.height,
					this.dimension.height );

			// the result should never be wrapped so we use 
			// the Integer.MAX_VALUE to prevent linewrapping
			Dimension resultSize = this.typeComponent2
					.getNeededSize ( Integer.MAX_VALUE );
			this.dimension.width += resultSize.width;
			this.dimension.height = Math.max ( resultSize.height,
					this.dimension.height );

			// now place the elements
			int posX = 0;

			this.indexLabel.setBounds ( posX, 0, labelSize.width,
					this.dimension.height );
			posX += labelSize.width + this.spacing;

			this.typeComponent.setBounds ( posX, 0, expSize.width,
					this.dimension.height );
			posX += expSize.width;

			this.typeComponent2.setBounds ( posX, 0, resultSize.width,
					this.dimension.height );
			

			/*
			 * Check whether this is node is evaluated.
			 * 
			 * If it is evaluated only the Label needs to get placed,
			 * if it is not evaluated yet the MenuButton needs to get placed.
			 */
			posX = labelSize.width + this.spacing;

			//TODO wieder einbauen
			//if (this.proofNode.getRule() != null) {
			if ( this.proofNode.isProven ( ) ) {
				this.ruleLabel.setText ( "(" + this.proofNode.getRule ( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
				Dimension ruleLabelSize = this.ruleLabel.getPreferredSize ( );

				this.ruleLabel.setBounds ( posX, this.dimension.height
						+ this.spacing, ruleLabelSize.width, ruleLabelSize.height );

				this.dimension.height += this.spacing + ruleLabelSize.height;
				this.dimension.width = Math.max ( this.dimension.width,
						ruleLabelSize.width + posX );

				//  display only the label not the button
				this.ruleLabel.setVisible ( true );
				this.ruleButton.setVisible ( false );
			} else {
				// place the menu button
				Dimension buttonSize = this.ruleButton.getNeededSize ( );
				this.ruleButton.setBounds ( posX, this.dimension.height
						+ this.spacing, buttonSize.width, buttonSize.height );

				this.dimension.height += this.spacing + buttonSize.height;
				this.dimension.width = Math.max ( this.dimension.width,
						buttonSize.width + posX );

				// display only the button not the label
				this.ruleLabel.setVisible ( false );
				this.ruleButton.setVisible ( true );
			}

		}
	}

	/*
	 * Implementation of the eventhandling
	 */

	public void addMinimalTypingNodeListener ( MinimalTypingNodeListener listener ) {
		this.listenerList.add ( MinimalTypingNodeListener.class, listener );
	}

	public void removeMinimalTypingNodeListener (
			MinimalTypingNodeListener listener ) {
		this.listenerList.remove ( MinimalTypingNodeListener.class, listener );
	}

	private void fireNodeChanged ( ) {
		Object[] listeners = this.listenerList.getListenerList ( );
		for ( int i = 0; i < listeners.length; i += 2 ) {
			if ( listeners[i] != MinimalTypingNodeListener.class ) {
				continue;
			}
			( ( MinimalTypingNodeListener ) listeners[i + 1] ).nodeChanged ( this );
		}
	}

	private void fireRequestJumpToNode ( ProofNode node ) {
		Object[] listeners = this.listenerList.getListenerList ( );
		for ( int i = 0; i < listeners.length; i += 2 ) {
			if ( listeners[i] != MinimalTypingNodeListener.class ) {
				continue;
			}
			( ( MinimalTypingNodeListener ) listeners[i + 1] )
					.requestJumpToNode ( node );
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
		} else if ( item instanceof MenuTranslateItem ) {
			int answer = 1;
			if ( this.proofModel.containsSyntacticSugar ( this.proofNode, false ) ) {
				String[] answers = {
						Messages.getString ( "NodeComponent.0" ), Messages.getString ( "NodeComponent.1" ), Messages.getString ( "NodeComponent.2" ) }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				answer = JOptionPane.showOptionDialog (
						getTopLevelAncestor ( ),
						Messages.getString ( "NodeComponent.3" ), //$NON-NLS-1$
						Messages.getString ( "NodeComponent.4" ), //$NON-NLS-1$
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, answers, answers[0] );
			}
			switch ( answer) {
				case 0:
					this.proofModel.translateToCoreSyntax ( this.proofNode, false );
					break;
				case 1:
					this.proofModel.translateToCoreSyntax ( this.proofNode, true );
					break;
				case 2:
					break;
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
										MessageFormat
												.format (
														Messages
																.getString ( "NodeComponent.5" ), e.getMessage ( ) ), Messages.getString ( "NodeComponent.6" ), JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} );
			}
			fireNodeChanged ( );
		} else if ( item instanceof MenuGuessTreeItem ) {
			try {
				this.proofModel.complete ( this.proofNode );
			} catch ( final ProofGuessException e ) {
				//e.printStackTrace ( );
				//e.getCause ( ).printStackTrace ( );
				fireRequestJumpToNode ( e.getNode ( ) );
				SwingUtilities.invokeLater ( new Runnable ( ) {
					public void run ( ) {
						JOptionPane
								.showMessageDialog (
										getTopLevelAncestor ( ),
										MessageFormat
												.format (
														Messages
																.getString ( "NodeComponent.7" ), e.getMessage ( ) ), Messages.getString ( "NodeComponent.8" ), JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$ //$NON-NLS-2$
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
		this.menuTranslateItem.setEnabled ( this.translator
				.containsSyntacticSugar ( this.proofNode.getExpression ( ), true ) );
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
	 * Returns the point at the bottom of the node where the layout should attach
	 * the arrow.
	 */
	public Point getBottomArrowConnection ( ) {
		return new Point ( this.getX ( ) + this.indexLabel.getWidth ( ) / 2, this
				.getY ( )
				+ this.indexLabel.getHeight ( ) );
	}

	/**
	 * Returns the point at the left of the node where the layout should attach
	 * the line to its parent.
	 */
	public Point getLeftArrowConnection ( ) {
		return new Point ( this.getX ( ), this.getY ( ) + this.indexLabel.getY ( )
				+ this.indexLabel.getHeight ( ) / 2 );
	}

	/**
	 * Just calls setBounds of the super class.
	 */
	@Override
	public void setBounds ( int x, int y, int width, int height ) {
		super.setBounds ( x, y, width, height );
	}

	/**
	 * Returns the typeLabel.
	 * 
	 * @return The typeLabel.
	 * @see #typeLabel
	 */
	public TypeComponent getTypeComponent ( ) {
		return this.typeComponent;
	}

	/**
	 * Returns the proofNode.
	 * 
	 * @return The proofNode.
	 * @see #proofNode
	 */
	public MinimalTypingProofNode getProofNode ( ) {
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
	 * Returns the compoundExpression.
	 * 
	 * @return The compoundExpression.
	 * @see #compoundExpression
	 */
	public CompoundExpression < Identifier, Type > getCompoundExpression ( ) {
		return this.compoundExpression;
	}
	//  
	//  public void paintComponent (Graphics gc)
	//  {
	//    super.paintComponent (gc);
	//    System.out.println("Auch Scheiße!!!");
	//    ShowBonds sb = new ShowBonds();
	//    sb.setType (this.proofNode.getType() );
	//    ToListenForMouseContainer tlfmc = new ToListenForMouseContainer();
	//    
	//    //System.out.println ("Scheiße!");
	//   
	//    typeRenderer.render (typePosition, 0,typeRenderer.getNeededSize (Integer.MAX_VALUE).width ,typeRenderer.getNeededSize (Integer.MAX_VALUE).height, this.getGraphics (), sb, tlfmc);
	//   
	//    
	//  }

	public TypeComponent getTypeComponent2 ( ) {
		return this.typeComponent2;
	}
}
