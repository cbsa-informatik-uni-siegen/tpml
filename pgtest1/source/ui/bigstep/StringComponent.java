package ui.bigstep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JComponent;

public class StringComponent extends JComponent {

	private String string;
	
	public StringComponent (String string) {
		this.string = string;
	}
	
	@Override
	public Dimension getPreferredSize () {
		FontMetrics fm = getFontMetrics (getFont ());
		return new Dimension (fm.stringWidth(string), fm.getHeight());
	}
	
	@Override
	public Dimension getMinimumSize() {
		FontMetrics fm = getFontMetrics (getFont ());
		return new Dimension (fm.stringWidth(string), fm.getHeight());
	}
	
	@Override
	public Dimension getMaximumSize() {
		FontMetrics fm = getFontMetrics (getFont ());
		return new Dimension (fm.stringWidth(string), fm.getHeight());
	}
	
	public void setString (String string) {
		this.string = string;
	}
	
	@Override
	public void paintComponent (Graphics graphics) {
		
		graphics.setColor(Color.BLACK);
		
		FontMetrics fm = getFontMetrics (getFont ());
		int width = fm.stringWidth(string);
		int posX = (getSize().width - width) / 2;
		int posY = (getSize().height + fm.getHeight() - fm.getDescent()) / 2;
		
		graphics.drawString(string, posX, posY);
		
	}
	
}
