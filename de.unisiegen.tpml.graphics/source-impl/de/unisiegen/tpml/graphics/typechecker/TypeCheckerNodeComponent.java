package de.unisiegen.tpml.graphics.typechecker;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.graphics.components.CompoundExpression;
import de.unisiegen.tpml.graphics.components.MenuButton;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;

public class TypeCheckerNodeComponent extends JComponent  implements TreeNodeComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6671706090992083026L;

	private Dimension														dimension;
	
	private boolean															changed;
	
	private int																	spacing;
	
	private CompoundExpression<String, Type>		expression;
	
	private JLabel															indexLabel;
	
	private MenuButton													menuButton;
	

	// XXX: the result should be listed here aswell
	
	
	
	public TypeCheckerNodeComponent (TypeCheckerProofNode node) {
		super ();
		
		this.dimension	= new Dimension (0, 0);
		this.spacing = 10;
		this.indexLabel	= new JLabel ();
		add (this.indexLabel);
		
		this.expression = new CompoundExpression<String, Type> ();
		this.expression.setExpression(node.getExpression());
		this.expression.setEnvironment(node.getEnvironment());
		add (this.expression);
		
		
		this.menuButton = new MenuButton ();
		this.menuButton.setText("Das ist ein Text");
		add (this.menuButton);
	}
	
	public void setIndex (int index) {
		this.indexLabel.setText("(" + index  + ")");
	}
	
	public void markChanged (boolean changed) {
		this.changed = changed;
	}
	
	public boolean isChanged () {
		return this.changed;
	}
	
	private void placeElements (int maxWidth) {
		
		// get the size for the index at the beginning: (x)
		FontMetrics fm = AbstractRenderer.getTextFontMetrics();
		Dimension labelSize = new Dimension (fm.stringWidth(this.indexLabel.getText()), fm.getHeight());
		this.dimension.setSize(labelSize.width, labelSize.height);
		
		// the index shrinkens the max size for the expression
		maxWidth -= labelSize.width;
		
		
		//  get the needed size for the expression
		Dimension expSize = this.expression.getNeededSize(maxWidth);
		this.dimension.width += expSize.width;
		if (expSize.height > this.dimension.height) {
			this.dimension.height = expSize.height;
		}
		
		// now place the components
		int posX = 0;
		this.indexLabel.setBounds(posX, 0, labelSize.width, this.dimension.height);
		posX += labelSize.width;
		
		this.expression.setBounds(posX, 0, expSize.width, this.dimension.height);
		posX += expSize.width;
		
		// place the menu button
		posX = 0;
		Dimension buttonSize = this.menuButton.getNeededSize();
		this.menuButton.setBounds(posX, this.dimension.height + spacing, buttonSize.width, buttonSize.height);
		
		
		this.dimension.height += spacing + buttonSize.height;
		this.dimension.width = Math.max(this.dimension.width, buttonSize.width);
	}
	
	/*
	 *	Implementation of the TreeNodeComponent Interface 
	 */
	public Dimension placeElementsForMaxWidth (int maxWidth) {
		if (this.changed) {
			placeElements (maxWidth);
		}
		
		return dimension;
	}
	
	public int getIndentationWidth () {
		// XXX: calculate the indentation
		return this.indexLabel.getWidth();
	}

	public Point getBottomArrowConnection () {
		return new Point (this.getX() + this.indexLabel.getWidth() / 2, this.getY() + this.getHeight());
	}
	
	public Point getLeftArrowConnection () {
		return new Point (this.getX (), this.getY() + this.getHeight() / 2);
	}
	
	public void setBounds (int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
}
