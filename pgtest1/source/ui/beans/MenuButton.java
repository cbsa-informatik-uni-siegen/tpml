package ui.beans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import ui.TypeCheckerEventListener;

public class MenuButton extends JComponent {

	private Font		font;

	private String		text;
	
	private Dimension 	size;
	
	private Point		pos;
	
	private int			margin;
	
	private int			inborder;
	
	private JPopupMenu	menu				= null;
	
	public MenuButton () {
		this.font = new JComboBox ().getFont();
		this.text = new String("");
		this.margin = 1;
		this.inborder = 3;
		calcPreferredSize();
		addMouseListener(new MouseAdapter () {
			public void mouseClicked(MouseEvent evt) {
				handleMouseClicked (evt);
			}
			
		});
	}
	
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
		calcPreferredSize();
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		calcPreferredSize();
	}
	
	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getInborder() {
		return inborder;
	}
	
	public JPopupMenu getMenu() {
		return this.menu;
	}
	
	private void installElementListener (MenuElement element) {
		if (element instanceof JMenuItem) {
			JMenuItem item = (JMenuItem)element;

			item.addActionListener(new ActionListener() {
				public void  actionPerformed (ActionEvent event) {
					handleMenuAction ((JMenuItem)event.getSource());
				}
			});
		}
		else {
			MenuElement[] subElements = element.getSubElements();
			for (MenuElement e : subElements) {
				installElementListener (e);
			}
		}
	}
	
	public void setMenu(JPopupMenu menu) {
		this.menu = menu;
	
		installElementListener(menu);
	}
	
	public void setInborder(int inborder) {
		this.inborder = inborder;
	}
	
	public void addMenuButtonListener(MenuButtonListener listener) {
		listenerList.add(MenuButtonListener.class, listener);
	}
	private void handleMenuAction (JMenuItem item) {
		Object[] listeners = listenerList.getListenerList();
		
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==MenuButtonListener.class) {
	             // Lazily create the event:
	             ((MenuButtonListener)listeners[i+1]).menuItemActivated(this, item);
	         }
	     }

	}


	private void calcPreferredSize() {
		FontMetrics fm = getFontMetrics (this.font);
		int width = fm.stringWidth(text) +  fm.getHeight() + 2*this.margin;
		int height = fm.getHeight();
		this.setPreferredSize(new Dimension (width, height));
		this.setSize(new Dimension (width, height));
	}
	
	private void handleMouseClicked (MouseEvent evt) {
		if (this.menu == null) {
			return;
		}
				
		this.menu.show(this, this.pos.x, this.pos.y);
	}
	
	public void paintComponent (Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		// clear the back
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight ());
		
		// draw the text
		FontMetrics fm = getFontMetrics (font);
		g2d.setColor(new Color(0.0f, 0.0f, 0.0f));
		g2d.drawString(text, 0, fm.getAscent());
		int width = fm.stringWidth(text);

		this.pos = new Point(width + margin + 2*margin, margin);
		size = new Dimension (fm.getHeight() - 1 - 2*margin, fm.getHeight() - 1 - 2*margin);
		
		// draw the marker
		g2d.setColor(new Color(0.5f, 0.5f, 0.5f));
		g2d.drawRect(pos.x, pos.y, size.width, size.height);
		Polygon poly = new Polygon ();
		poly.addPoint(pos.x + inborder, pos.y + inborder);
		poly.addPoint(pos.x + size.width - inborder, pos.y + inborder);
		poly.addPoint(pos.x + size.width / 2, pos.y + size.height - inborder);
		g2d.fill(poly);
	}


}
