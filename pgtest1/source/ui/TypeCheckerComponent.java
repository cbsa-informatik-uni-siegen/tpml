package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.EventObject;
import java.util.Vector;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import smallstep.Constant;
import smallstep.Expression;
import smallstep.Identifier;
import smallstep.Projection;
import typing.MonoType;
import typing.ProofNode;
import typing.ProofTree;
import typing.Rule;
import ui.beans.MenuButton;
import ui.beans.MenuButtonListener;

/**
 * Implementation of the TypeCheckerComponent.  
 * @author marcell
 *
 */
public class TypeCheckerComponent extends JComponent {
	
	/**
	 * The Node item represents a single TypeJudgement line.
	 * Containing of the position where to render the Judgement.
	 * 
	 * @author marcell
	 *
	 */
	private class NodeItem {
		/**
		 * The x position where the Judgement should be drawn to
		 */
		public int 			x;
		/**
		 * The y position where the Judgement should be drawn to
		 */
		public int 			y;
		/**
		 * The x position of the parent where the vertical line of
		 * the Tree begins.
		 */
		public int 			parentX;
		/**
		 * The y position of the parent where the vertical line of
		 * the Tree begins.
		 */
		public int 			parentY;
		/**
		 * True when the Judgement has a parent.
		 */
		public boolean 		hasParent;
		/**
		 * The node from the proof tree that should be rendered.
		 */
		public ProofNode 	node;	
	}

	/**
	 * The Tree model that is used to display the expression tree
	 */
	private ProofTree			model;
	
	/**
	 * The number of pixels the tree will be indented every childstep
	 */
	private	int					indentionDepth;
	
	/**
	 * Counter that is needed to mark the Judgements
	 */
	private int					ruleId;
	
	/**
	 * Helper variable that is used to decide which ComboBox 
	 * from the vector of comboboxes should be set when needed
	 */
	private int					whichComboBox;
	
	/**
	 * The font that is used to render the judgement
	 */
	private Font				expressionFont;
	private FontMetrics			expressionFM;
	
	/**
	 * 
	 */
	private Font				ruleFont;
	private FontMetrics			ruleFM;
	
	/**
	 * 
	 */
	private Vector<MenuButton>	menuButtons;
	
	/**
	 * 
	 */
	private Vector<ProofNode>	selectionRelation;
	
	/**
	 * 
	 */
	private HashMap<String, Rule>			rules;
	
	private Dimension						selectionSize;
	
	private Dimension						maxSize;
	
	
	
	private enum ActionType {
		ActionTypeTranslateToCoresyntax,
		ActionTypeEnterType,
	}
	
	private class RuleMenuItem extends JMenuItem {
		private Rule	rule;
		
		public RuleMenuItem (Rule rule) {
			super (rule.getName());
			this.rule = rule;
		}
		
		public Rule getRule () {
			return rule;
		}
	};
	
	private static class ActionMenuItem extends JMenuItem {
		private ActionType	type;
		
		public ActionMenuItem (ActionType type) {
			super (ActionMenuItem.getString(type));
			this.type = type;
		}
		
		private static String getString(ActionType  type) {
			switch (type) {
			case ActionTypeTranslateToCoresyntax:
				return "Translate to coresyntax";
			case ActionTypeEnterType:
				return "Enter type";
			}
			return "narf";
		}
		
		public ActionType getActionType () {
			return type;
		}
	}
	
	
	public TypeCheckerComponent () {
		super ();
		this.setLayout(null);
		this.indentionDepth = 25;
	
		Font font = new JComboBox ().getFont();
		this.expressionFont = font.deriveFont(font.getSize2D() * 1.3f);
		this.expressionFM = getFontMetrics (this.expressionFont);
		
		this.ruleFont = new JComboBox().getFont();
		this.ruleFM = getFontMetrics (this.ruleFont);
		
		this.menuButtons = new Vector<MenuButton>();
		this.selectionRelation = new Vector<ProofNode> ();
		this.rules = new HashMap<String, Rule>();
		
		this.maxSize = new Dimension ();
		
		FontMetrics fm = getFontMetrics (font);
		this.selectionSize = new Dimension (fm.stringWidth("DAS IST NE RULE"), fm.getHeight());
		
		for (Rule r : Rule.getAllRules()) {
			rules.put(r.getName(), r);
		}
		
	}
	
	public void setModel(ProofTree model) {
		this.model = model;
		
		checkNumberOfNeededSelections();
		
		repaint ();
	}
	
	public void setIndentionDepth(int depth) {
		this.indentionDepth = depth;
	}
	
	private boolean isLeaf (Expression exp) {
		return (	(exp instanceof Constant) 
				||	(exp instanceof Identifier)
				||  (exp instanceof Projection));
	}
		
	public void handleButtonChanged (MenuButton button, JMenuItem item) {
		if (item instanceof RuleMenuItem) {
			RuleMenuItem ruleItem = (RuleMenuItem)item;
			Rule rule = ruleItem.getRule();
			if (rule == null)
				return;
			
			
			int p = this.menuButtons.indexOf(button);
			if (p == -1)
				return;
			
			ProofNode proofNode = this.selectionRelation.elementAt(p);
			fireEvent (proofNode, rule);
		}
		else if (item instanceof ActionMenuItem) {
			ActionMenuItem actionItem = (ActionMenuItem)item;
			switch (actionItem.getActionType()) {
			case ActionTypeEnterType:
				handleEnterType (button);
				break;
			case ActionTypeTranslateToCoresyntax:
				handleTranslteToCoreSyntax(button);
				break;
			}
		}
	}
	
	private void handleEnterType (MenuButton button) {
		button.setVisible(false);
		
		int p = this.menuButtons.indexOf(button);
		if (p == -1)
			return;
		
		ProofNode proofNode = this.selectionRelation.elementAt(p);
		
		FontMetrics fm = getFontMetrics (new JTextField().getFont());
		TypeEnterGUI gui = new TypeEnterGUI (proofNode);
		gui.addTypeEnterListener(new TypeEnterListener() {
			public void typeAccepted (JComponent gui, String typeString, ProofNode node, MonoType type) {
				fireGuess(node, type);
				remove (gui);
			}
			
			public void typeRejected (JComponent gui) {
				remove (gui);
			}

		});
		
		add(gui);
		gui.setBounds(button.getX(), button.getY(), 250, fm.getHeight() + 8);
		gui.setVisible(true);
	}
	
	private void handleTranslteToCoreSyntax(MenuButton button) {
		
	}
	
	private Vector<Expression>	tempExpressions;
	
	/**
	 * Checks a ProofNode (with its childnodes) to decide how many 
	 * JComboBoxes are needed to allow the user to interact.
	 * 
	 * @param node The node that should be checked
	 * @param currentlyNeeded The number of selections that are needed to
	 * 			the current stage.
	 * @return Return the number of selections that are needed for this
	 * 			branch of the ProofTree.
	 */
	private int checkNextNode (ProofNode node, int currentlyNeeded) {
		
		// every node that is currently not marked with a rule has 
		// still needs to get evaluated by the user so we need a 
		// selection box here.
		if (node.getRule() == null) {
			currentlyNeeded++;
			tempExpressions.add(node.getJudgement().getExpression());
		}
		else {
			for (int i=0; i<node.getChildCount(); i++) {
				ProofNode child = (ProofNode)node.getChildAt(i);
				currentlyNeeded = checkNextNode (child, currentlyNeeded);
			}
		}
		return currentlyNeeded;
	}
	
	/**
	 * Initiates the recursive check how much selection JComboBoxes are
	 * needed to allow the user to interact. <br>
	 * 
	 */
	private void checkNumberOfNeededSelections() {
		
		if (model == null)
			return;
	
		tempExpressions = new Vector<Expression>();
		int selectionCount = checkNextNode ((ProofNode)model.getRoot(), 0);

		for (MenuButton b : menuButtons) {
			this.remove(b);
		}
		
		this.menuButtons.clear ();
		this.selectionRelation.clear ();
		for (int i=0; i<selectionCount; i++) {
			
			MenuButton menuButton = new MenuButton();
			this.menuButtons.add(menuButton);
			menuButton.setVisible(false);
			
			JPopupMenu menu = new JPopupMenu();
			JMenuItem item;
			
			for (Rule r : Rule.getAllRules()) {
				item = new RuleMenuItem (r);
				item.setFont(item.getFont().deriveFont(Font.PLAIN));
				menu.add(item);
			}
			Expression exp = tempExpressions.elementAt(i);
			
			menu.addSeparator();
			item = new ActionMenuItem(ActionType.ActionTypeTranslateToCoresyntax);
			item.setEnabled(exp.containsSyntacticSugar());
			menu.add(item);
			
			item = new ActionMenuItem(ActionType.ActionTypeEnterType);
			menu.add(item);
						
			
			
			menuButton.setMenu(menu);
			
			menuButton.addMenuButtonListener(new MenuButtonListener () {
				public void menuItemActivated (MenuButton button, JMenuItem item) {
					handleButtonChanged (button, item);
				}
			});
	
		}
		
	}
	
	/**
	 * Renders a ProofNode encapsulates within the NodeItem with the
	 * Environment, the Expression and the resulting Type.
	 *   
	 * @param g2d  The Rendering context used to draw the text and the 
	 * 				TreeLines
	 * @param node The NodeItem containing the positions the parentPosition
	 * 				and the ProofNode that should be rendered.
	 * @return The vertical position where the TreeNode ends on the screen.
	 */
	public int renderTreeNode (Graphics2D g2d, NodeItem node, String sindention) {
		g2d.setFont(this.expressionFont);
		
		
		// build the judgement for this line in the way
		// (<ID>) [<ENVIRONMENT>] |> <EXPRESSION> :: <TYPE>
		String judgement = "(" + this.ruleId + ") " + 
			node.node.getJudgement().getEnvironment() + " \u22b3 " + 
			node.node.getJudgement().getExpression().toString() +
			" : : " +
			node.node.getJudgement().getType() + 
			"    ";
		
		int identifierSize = this.expressionFM.stringWidth ("(" + this.ruleId + ")");
		
		// Draw the two orthogonal lines from the front of this judgement
		// line to the bottom of the leading parent judgement when a parent is
		// is present.
		if (node.hasParent) {
			int nx = node.x - 2;
			int ny = node.y - this.expressionFM.getDescent();
			g2d.drawLine(node.parentX, node.parentY, node.parentX, ny);
			g2d.drawLine(node.parentX, ny, nx, ny);
			
			Polygon poly = new Polygon ();
			poly.addPoint (node.parentX, node.parentY);
			poly.addPoint (node.parentX + this.expressionFM.getAscent()/2, node.parentY + this.expressionFM.getAscent());
			poly.addPoint (node.parentX, node.parentY + this.expressionFM.getAscent() / 2);
			poly.addPoint (node.parentX - this.expressionFM.getAscent()/2, node.parentY + this.expressionFM.getAscent());

			g2d.fill (poly);
		}
		
		
		// find the position in the center of the (<ID>) below the judgement
		int parentX = node.x + identifierSize / 2;
		int parentY = node.y + 5;
		
		// render the judgement
		g2d.drawString(judgement, node.x, node.y);
		
		// find the max size behind the judgement line
		int size = node.x + this.expressionFM.stringWidth(judgement);
		if (size > this.maxSize.width) {
			this.maxSize.width = size;
		}

		// check wether we have to put a combobox 
		Expression exp = node.node.getJudgement().getExpression();
		boolean idConst = isLeaf (exp); 
//		(exp instanceof Constant) || (exp instanceof Identifier);
		if (node.node.getRule() == null) {
			// find the correct combobox with the
			MenuButton button = this.menuButtons.elementAt(this.whichComboBox);
			if (!button.isVisible()) {
				this.add(button);
				this.selectionRelation.add(node.node);
				button.setVisible(true);
			}
			
			this.whichComboBox++;
			this.selectionSize.height = button.getHeight();
			this.selectionSize.width = button.getWidth();
			
			// check whether this judgement is based on CONST or ID
			// if so, the combobox will be placed behind the judgement line.
			// if not, the combobox will be placed below.
			if (idConst) {
				int boxX = node.x + this.expressionFM.stringWidth(judgement);
				int boxY = node.y;
				boxY -= selectionSize.height - this.expressionFM.getDescent();
				button.setBounds(boxX, boxY, selectionSize.width, selectionSize.height);
			}
			else {
				int boxX = node.x + identifierSize;
				int boxY = node.y + this.expressionFM.getDescent() + selectionSize.height / 2;
				button.setBounds(boxX, boxY, selectionSize.width, selectionSize.height);
				node.y += selectionSize.height + selectionSize.height / 2 + this.expressionFM.getDescent();
				
			}
			
			if (button.getX() + button.getWidth() > this.maxSize.width) {
				this.maxSize.width = button.getX() + button.getWidth();
			}
		}
		else {
			g2d.setFont(this.ruleFont);
			g2d.setColor(new Color (255, 0, 0));
			int ruleX = 0;
			int ruleY = 0;
			if (idConst) {
				ruleX = node.x + this.expressionFM.stringWidth(judgement);
				ruleY = node.y;
			}
			else {
				ruleX = node.x + identifierSize;
				ruleY = node.y + this.ruleFM.getHeight () + selectionSize.height / 2;
				node.y = ruleY;
			}
			
			// find the max size behind the evaluated rule
			size = ruleX + this.ruleFM.stringWidth(node.node.getRule ().toString());
			if (size > this.maxSize.width) {
				this.maxSize.width = size;
			}
			
			g2d.drawString(node.node.getRule().toString(), ruleX, ruleY);
			g2d.setColor(new Color (0, 0, 0));
		}
		
		
		// increase the judgment id
		this.ruleId++;
		
		// continue with the child judgements.
		node.y += this.expressionFM.getHeight() + 15;
		for (int i=0; i<node.node.getChildCount(); i++) {
			
			// create the NodeItem that is used to draw 
			NodeItem newNode = new NodeItem();
			newNode.hasParent = true;
			newNode.node = node.node.getChildAt(i);
			newNode.x = node.x + this.indentionDepth;
			newNode.y = node.y;
			newNode.parentX = parentX;
			newNode.parentY = parentY;
			
			// proceed with the children
			node.y = renderTreeNode (g2d, newNode, sindention + "    ");
			
		}
		return (node.y);
	}

	/**
	 * Reimplemented function from the JComponent.<br><br>
	 * 
	 * Clears the background white and renders the TypingTree 
	 * @param g The graphics context that will be used to render
	 * 			the Tree
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.black);
		if (model != null) {
			NodeItem newNode = new NodeItem();
			newNode.hasParent = false;
			newNode.node = (ProofNode)model.getRoot();
			newNode.x = 25;
			newNode.y = 25;
			this.ruleId = 1;
			this.whichComboBox = 0;
			this.maxSize.width = 0;
			this.maxSize.height = renderTreeNode (g2d, newNode, "");
			
		}
		setPreferredSize(this.maxSize);
	}
	
	public void addTypeCheckerEventListener (TypeCheckerEventListener listener) {
		listenerList.add(TypeCheckerEventListener.class, listener);
	}
	
	public void fireEvent(ProofNode node, Rule rule) {
		Object[] listeners = listenerList.getListenerList();
		
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==TypeCheckerEventListener.class) {
	             // Lazily create the event:
	             ((TypeCheckerEventListener)listeners[i+1]).applyRule(new EventObject(this), node, rule);
	         }
	     }
	}
	
	public void fireGuess (ProofNode node, MonoType type) {
		Object[] listeners = listenerList.getListenerList();
		
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==TypeCheckerEventListener.class) {
	             // Lazily create the event:
	             ((TypeCheckerEventListener)listeners[i+1]).guessType(new EventObject(this), node, type);
	         }
	     }

	}

}
