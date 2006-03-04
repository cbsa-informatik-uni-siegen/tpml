package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.EventObject;
import java.util.Vector;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import smallstep.Constant;
import smallstep.Expression;
import smallstep.Identifier;
import typing.ProofNode;
import typing.ProofTree;
import typing.Rule;

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

	/**
	 * The comboboxes that are used to let the user interact with.
	 */
	private Vector<JComboBox>	selections;
	
	/**
	 * 
	 */
	public Vector<ProofNode>	selectionRelation;
	
	/**
	 * 
	 */
	public HashMap<String, Rule>			rules;
	
	private Dimension			selectionSize;
	
	
	public TypeCheckerComponent () {
		super ();
		this.setLayout(null);
		this.indentionDepth = 25;
	
		this.expressionFont = new JComboBox().getFont();
		
		this.selections = new Vector<JComboBox>();
		this.selectionRelation = new Vector<ProofNode> ();
		this.rules = new HashMap<String, Rule>();
		
		FontMetrics fm = getFontMetrics(this.expressionFont);
		this.selectionSize = new Dimension (fm.stringWidth("DAS IST NE RULE"), fm.getHeight());
		
		for (Rule r : Rule.getAllRules()) {
			rules.put(r.getName(), r);
			System.out.println ("rules [" + r.getName()  + "] = " + r);
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
	
	public void handleRuleBoxChanged (JComboBox box) {
		Rule rule = this.rules.get((String)box.getSelectedItem());
		if (rule == null)
			return;
		
		int p = this.selections.indexOf(box);
		if (p == -1)
			return;
		ProofNode proofNode = this.selectionRelation.elementAt(p);
		
		fireEvent (proofNode, rule);
		
	}
	
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
		
		System.out.println("checkNumberOfSelections");
		if (model == null)
			return;
		
		int selectionCount = checkNextNode ((ProofNode)model.getRoot(), 0);
		System.out.println("selectionCount: " + selectionCount);
		
		for (JComboBox c : selections) {
			System.out.println(" remove (c)");
			this.remove(c);
		}
		
		this.selections.clear();
		this.selectionRelation.clear ();
		for (int i=0; i<selectionCount; i++) {
			JComboBox selection = new JComboBox ();
			this.selections.add(selection);
			selection.setVisible(false);
			
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			model.addElement("---");
			for (Rule r : Rule.getAllRules()) {
				model.addElement(r.getName());
			}
			selection.setModel(model);
			selection.addPopupMenuListener(new PopupMenuListener () {
				public void popupMenuCanceled(PopupMenuEvent e) { }
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					JComboBox comboBox = (JComboBox)e.getSource();
					handleRuleBoxChanged (comboBox);
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
		FontMetrics fm = getFontMetrics(this.expressionFont);
		
		
		// Draw the two orthogonal lines from the front of this judgement
		// line to the bottom of the leading parent judgement when a parent is
		// is present.
		if (node.hasParent) {
			int nx = node.x - 2;
			int ny = node.y - fm.getDescent();
			g2d.drawLine(node.parentX, node.parentY, node.parentX, ny);
			g2d.drawLine(node.parentX, ny, nx, ny);
		}
		
		// build the judgement for this line in the way
		// (<ID>) [<ENVIRONMENT>] |> <EXPRESSION> :: <TYPE>
		
		String judgement = "(" + this.ruleId + ") " + 
			node.node.getJudgement().getEnvironment() + " |> " + 
			node.node.getJudgement().getExpression().toString() +
			"     : :     " +
			node.node.getJudgement().getType() + 
			"    ";
		
		int identifierSize = fm.stringWidth ("(" + this.ruleId + ")");
		
		
		// find the position in the center of the (<ID>) below the judgement
		int parentX = node.x + identifierSize / 2;
		int parentY = node.y + 5;
		
		// render the judgement
		g2d.drawString(judgement, node.x, node.y);

		// check wether we have to put a combobox 
		Expression exp = node.node.getJudgement().getExpression();
		boolean idConst = (exp instanceof Constant) || (exp instanceof Identifier);
		if (node.node.getRule() == null) {
			// find the correct combobox with the
			JComboBox box = this.selections.elementAt(this.whichComboBox);
			if (!box.isVisible()) {
				this.add(box);
				this.selectionRelation.add(node.node);
				box.setVisible(true);
			}
			this.whichComboBox++;
			
			// check whether this judgement is based on CONST or ID
			// if so, the combobox will be placed behind the judgement line.
			// if not, the combobox will be placed below.
			if (idConst) {
				int boxX = node.x + fm.stringWidth(judgement);
				int boxY = node.y;
				boxY -= selectionSize.height - fm.getDescent();
				box.setBounds(boxX, boxY, selectionSize.width, selectionSize.height);
			}
			else {
				int boxX = node.x + identifierSize;
				int boxY = node.y + fm.getDescent() + selectionSize.height / 2;
				box.setBounds(boxX, boxY, selectionSize.width, selectionSize.height);
				node.y += selectionSize.height + selectionSize.height / 2 + fm.getDescent();
				
			}
		}
		else {
			
			if (idConst) {
				int ruleX = node.x + fm.stringWidth(judgement);
				int ruleY = node.y;
				g2d.drawString(node.node.getRule().toString(), ruleX, ruleY);
			}
			else {
				int ruleX = node.x + identifierSize;
				int ruleY = node.y + fm.getHeight () + selectionSize.height / 2;
				g2d.drawString(node.node.getRule().toString(), ruleX, ruleY);
				node.y = ruleY;
			}
		}
		
		
		// increase the judgment id
		this.ruleId++;
		
		// continue with the child judgements.
		node.y += fm.getHeight() + 15;
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
			renderTreeNode (g2d, newNode, "");
			
		}
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

}
