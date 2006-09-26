package de.unisiegen.tpml.graphics.typechecker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.graphics.RuleComparator;
import de.unisiegen.tpml.graphics.components.CompoundExpression;
import de.unisiegen.tpml.graphics.components.MenuButton;
import de.unisiegen.tpml.graphics.components.MenuButtonListener;
import de.unisiegen.tpml.graphics.components.MenuEnterTypeItem;
import de.unisiegen.tpml.graphics.components.MenuGuessItem;
import de.unisiegen.tpml.graphics.components.MenuRuleItem;
import de.unisiegen.tpml.graphics.components.MenuTranslateItem;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;

public class TypeCheckerNodeComponent extends JComponent  implements TreeNodeComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6671706090992083026L;
	
	private TypeCheckerProofModel								proofModel;
	
	private TypeCheckerProofNode								proofNode;
	
	private Dimension														dimension;
	
	private int																	spacing;
	
	private JLabel															indexLabel;
	
	private CompoundExpression<String, Type>		expression;
	
	private MenuButton													ruleButton;
	
	private JLabel															typeLabel;
	
	private JLabel															ruleLabel;
	
	private MenuTranslateItem										menuTranslateItem;
	
	private LanguageTranslator									translator;
	


	/**
	 * Constructor for a TypeCheckerNode 
	 * 
	 * @param node The origin ProofNode
	 * @param model The model
	 * @param translator The translator of the model for the selected language
	 */
	public TypeCheckerNodeComponent (TypeCheckerProofNode 	node,
																	 TypeCheckerProofModel 	model,
																	 LanguageTranslator			translator) {
		super ();
		
		this.proofNode 	= node;
		this.proofModel = model;
		this.translator = translator;
		
		this.dimension	= new Dimension (0, 0);
		this.spacing = 10;
		this.indexLabel	= new JLabel ();
		add (this.indexLabel);
		
		this.expression = new CompoundExpression<String, Type> ();
		add (this.expression);
		changeNode ();

		
		/*
		 * Create both, the ruleButton for selecting the rule
		 * and the label, that will be displayed later
		 */
		this.ruleButton = new MenuButton ();
		add (this.ruleButton);
		this.ruleButton.setVisible(true);
		
		this.ruleLabel = new JLabel ();
		add (this.ruleLabel);
		this.ruleLabel.setVisible(false);
		
		this.typeLabel = new JLabel ();
		add (this.typeLabel);
		this.typeLabel.setText (" :: ");
		
		
		/*
		 * Create the PopupMenu for the menu button
		 */
		ProofRule[] rule = this.proofModel.getRules().clone ();
		Arrays.<ProofRule>sort (rule, new RuleComparator ());
		
		JPopupMenu menu = new JPopupMenu ();
		for (int i=0; i<rule.length; i++) {
			menu.add (new MenuRuleItem (rule[i]));
		}
		menu.addSeparator();
		menu.add (new MenuEnterTypeItem ());
		menu.add (new MenuGuessItem ());
		menu.add (this.menuTranslateItem = new MenuTranslateItem ());
		
		this.ruleButton.setMenu(menu);
		
		/*
		 * Connect the handling of the ruleButton
		 */
		this.ruleButton.addMenuButtonListener(new MenuButtonListener() {
			public void menuClosed (MenuButton button) { } 
			public void menuItemActivated (MenuButton button, JMenuItem source) {
				TypeCheckerNodeComponent.this.handleMenuActivated (source);
			}
		});
		
	}
	
	/**
	 * Sets the index that will be displayed in front of the node
	 * 
	 * @param index The index
	 */
	public void setIndex (int index) {
		this.indexLabel.setText("(" + index  + ")");
	}
	
	
	public void changeNode () {
		this.expression.setExpression(this.proofNode.getExpression());
		this.expression.setEnvironment(this.proofNode.getEnvironment());
	}
		
	private void placeElements (int maxWidth) {
		
		// get the size for the index at the beginning: (x)
		FontMetrics fm = AbstractRenderer.getTextFontMetrics();
		Dimension labelSize = new Dimension (fm.stringWidth(this.indexLabel.getText()), fm.getHeight());
		this.dimension.setSize(labelSize.width, labelSize.height);

		// there will be a bit spacing between the index label and the expression
		dimension.width += this.spacing;
		
		// the index shrinkens the max size for the expression
		maxWidth -= labelSize.width;
		
		
		
		//  get the needed size for the expression
		Dimension expSize = this.expression.getNeededSize(maxWidth);
		this.dimension.width += expSize.width;
		this.dimension.height = Math.max(expSize.height, this.dimension.height);
		
		// get the neede size for the type
		if (this.proofNode.getType () != null && this.proofNode.isFinished()) {
			this.typeLabel.setText(" :: " + this.proofNode.getType());
		}
		else {
			this.typeLabel.setText(" :: ");
		}
		Dimension typeSize = this.typeLabel.getPreferredSize();
		this.dimension.width += typeSize.width;
		this.dimension.height = Math.max(typeSize.height, this.dimension.height);
		
		// now place the components
		int posX = 0;
		this.indexLabel.setBounds(posX, 0, labelSize.width, this.dimension.height);
		posX += labelSize.width + this.spacing;
		
		this.expression.setBounds(posX, 0, expSize.width, this.dimension.height);
		posX += expSize.width;
		
		this.typeLabel.setBounds(posX, 0, typeSize.width, this.dimension.height);
		posX += typeSize.width;
		
		

		/*
		 * Check whether this is node is evaluated.
		 * 
		 * If it is evaluated only the Label needs to get placed,
		 * if it is not evaluated yet the MenuButton needs to get placed.
		 */
		if (this.proofNode.isProven()) {
			// place the menu label
			posX = labelSize.width + this.spacing;
			this.ruleLabel.setText ("(" + this.proofNode.getRule() + ")");
			
			Dimension ruleLabelSize = this.ruleLabel.getPreferredSize();
			this.ruleLabel.setBounds(posX, this.dimension.height + spacing, ruleLabelSize.width, ruleLabelSize.height);
			
			this.dimension.height += spacing + ruleLabelSize.height;
			this.dimension.width = Math.max(this.dimension.width, ruleLabelSize.width);
			
			//  display only the label not the button
			this.ruleLabel.setVisible (true);
			this.ruleButton.setVisible (false);
		}
		else {
			// place the menu button
			posX = labelSize.width + this.spacing;
			Dimension buttonSize = this.ruleButton.getNeededSize();
			this.ruleButton.setBounds(posX, this.dimension.height + spacing, buttonSize.width, buttonSize.height);
			
			this.dimension.height += spacing + buttonSize.height;
			this.dimension.width = Math.max(this.dimension.width, buttonSize.width);
			
			// display only the button not the label
			this.ruleLabel.setVisible (false);
			this.ruleButton.setVisible (true);
		}
	}
	
	
	/*
	 * Implementation of the eventhandling
	 */
	
	public void addTypeCheckerNodeListener (TypeCheckerNodeListener listener) {
		listenerList.add (TypeCheckerNodeListener.class, listener);
	}
	
	public void removeTypeCheckerNodeListener (TypeCheckerNodeListener listener) {
		listenerList.remove(TypeCheckerNodeListener.class, listener);
	}
	
	private void fireNodeChanged () {
		Object[] listeners = listenerList.getListenerList();
		for (int i=0; i<listeners.length; i+=2) {
			if (listeners [i] != TypeCheckerNodeListener.class) {
				continue;
			}
			
			((TypeCheckerNodeListener)listeners [i+1]).nodeChanged(this);
		}
	}

	private void handleMenuActivated (JMenuItem item) {
		if (item instanceof MenuRuleItem) {
			MenuRuleItem ruleItem = (MenuRuleItem)item;
			ProofRule rule = ruleItem.getRule();
			
			try {
				this.proofModel.prove(rule, this.proofNode);
				this.ruleButton.setToolTipText(null);
			} catch (Exception exc) {
				
				// when the node could not be prooven with the selected
				// rule the menu button gets labeled with the given rule 
				// and will be displayed in red
				this.ruleButton.setText("(" + rule.getName() + ")");
				this.ruleButton.setTextColor(Color.RED);
				
				Throwable cause = exc.getCause();
				if (cause != null) {
					this.ruleButton.setToolTipText(cause.getMessage());
				}
				else {
					this.ruleButton.setToolTipText(exc.getMessage());
				}
			}
			
			fireNodeChanged();
		}
		else if (item instanceof MenuTranslateItem) {
			this.proofModel.translateToCoreSyntax(this.proofNode, false);
			fireNodeChanged();
		}
		else if (item instanceof MenuGuessItem) {
			try {
				this.proofModel.guess(this.proofNode);
			}
			catch (Exception exc) {
				exc.printStackTrace();
			}
			fireNodeChanged ();
		}
		

	}

	/*
	 *	Implementation of the TreeNodeComponent Interface 
	 */
	/**
	 * Performs an update for the Entire Node.
	 * 
	 * All elements get rearanged based on the given maximum with, 
	 * the menu items will be checked if they are still available.
	 */
	public Dimension update (int maxWidth) {
		placeElements (maxWidth);
			
		this.menuTranslateItem.setEnabled(this.translator.containsSyntacticSugar(this.proofNode.getExpression(), false));
		
		
		return dimension;
	}
	
	public int getIndentationWidth () {
		// XXX: calculate the indentation
		return this.indexLabel.getWidth();
	}

	public Point getBottomArrowConnection () {
		return new Point (this.getX() + this.indexLabel.getWidth() / 2, this.getY() + this.indexLabel.getHeight());
	}
	
	public Point getLeftArrowConnection () {
		return new Point (this.getX (), this.getY() + this.indexLabel.getY() + this.indexLabel.getHeight() / 2);
	}
	
	public void setBounds (int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
}
