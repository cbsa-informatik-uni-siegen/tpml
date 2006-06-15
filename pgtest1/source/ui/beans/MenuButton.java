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
	
	private float		inborder;
	
	private Color		textColor;
	
	private Color		markerColor;
	
	private ActionListener	listener;
	
	private JPopupMenu	menu				= null;
	
	public MenuButton () {
		this.font = new JComboBox ().getFont();
		this.text = new String("");
		setDefaultColors ();
		this.margin = 1;
		this.inborder = 0.2f;
		calcPreferredSize();
		addMouseListener(new MouseAdapter () {
			public void mousePressed(MouseEvent evt) {
				handleMouseClicked (evt);
			}
			
		});
		
		listener = new ActionListener () {
			public void actionPerformed (ActionEvent event) {
				handleMenuAction ((JMenuItem)event.getSource());
			}
		};
	}
	
	public void setDefaultColors() {
		this.textColor = new Color (0.0f, 0.0f, 0.0f);
		this.markerColor = new Color (0.5f, 0.5f, 0.5f);
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
	
	public Color getTextColor() {
		return textColor;
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	
	public Color getMarkerColor () {
		return markerColor;
	}
	
	public void setMarkerColor(Color markerColor) {
		this.markerColor = markerColor;
	}
	
	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}
	
	public void setInborder (float inborder) {
		this.inborder = inborder;
	}

	public float getInborder() {
		return inborder;
	}
	
	public JPopupMenu getMenu() {
		return this.menu;
	}
	
	public int getNeededWidth () {
		FontMetrics fm = getFontMetrics (this.font);
		int width = fm.stringWidth(this.text);
		int addSize = fm.getHeight () - 2*margin;
		if (this.text.length() != 0)
			addSize += fm.getAscent () + 2*margin;
		
		return (width + addSize);
	}
	
	public int getNeededHeight () {
		FontMetrics fm = getFontMetrics (this.font);
		return fm.getHeight();
	}
	
	private void installElementListener (MenuElement element) {
		if (element instanceof JMenuItem) {
			JMenuItem item = (JMenuItem)element;

			item.addActionListener(listener);
		}
		else {
			MenuElement[] subElements = element.getSubElements();
			for (MenuElement e : subElements) {
				installElementListener (e);
			}
		}
	}
	
	private void uninstallElementListener (MenuElement element) {
		if (element instanceof JMenuItem) {
			JMenuItem item = (JMenuItem)element;
			
			item.removeActionListener(listener);
			
		}
		else {
			MenuElement[] subElements = element.getSubElements();
			for (MenuElement e : subElements) {
				uninstallElementListener (e);
			}
		}
	}
	
	public void setMenu(JPopupMenu menu) {
		if (this.menu != null) {
			uninstallElementListener(menu);
		}
		this.menu = menu;
	
		this.menu.addPopupMenuListener(new PopupMenuListener () {
			public void popupMenuCanceled (PopupMenuEvent e) { }
			public void popupMenuWillBecomeInvisible (PopupMenuEvent e) {
				fireMenuClosed ();
			}
			public void popupMenuWillBecomeVisible (PopupMenuEvent e) { }
		});
		installElementListener(menu);
	}
	
	public void setInborder(int inborder) {
		this.inborder = inborder;
	}
	
	public void addMenuButtonListener(MenuButtonListener listener) {
		listenerList.add(MenuButtonListener.class, listener);
	}
	
	private void handleMenuAction (JMenuItem item) {
		setText (item.getText());
		Object[] listeners = listenerList.getListenerList();
		
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==MenuButtonListener.class) {
	             // Lazily create the event:
	             ((MenuButtonListener)listeners[i+1]).menuItemActivated(this, item);
	         }
	     }

	}
	
	private void fireMenuClosed () {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==MenuButtonListener.class) {
				((MenuButtonListener)listeners[i+1]).menuClosed(this);
			}
		}
	}
	
	private void handleMouseClicked (MouseEvent evt) {
		if (this.menu == null) {
			return;
		}
		
		this.menu.show(this, this.pos.x, this.pos.y);
	}
	
	private void calcPreferredSize() {
		FontMetrics fm = getFontMetrics (this.font);
		int width = fm.stringWidth(text) +  fm.getHeight() + fm.getAscent();
		int height = fm.getHeight();
		this.setPreferredSize(new Dimension (width, height));
	}
	
	public void paintComponent (Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		// clear the back
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight ());
		
		// draw the text
		FontMetrics fm = getFontMetrics (font);
		g2d.setColor(new Color(0.0f, 0.0f, 0.0f));
		
		int vcenter = getHeight () / 2 + fm.getAscent() / 3;
		g2d.setColor(this.textColor);
		g2d.drawString(text, 0, vcenter);
		int width = fm.stringWidth(text);
		
		if (this.text.length() != 0) {
			this.pos = new Point(width + fm.getAscent() + 2*margin, margin);
		}
		else {
			this.pos = new Point(width, margin);
		}
		
		size = new Dimension (getHeight () - 1 - 2*margin, getHeight() - 1 - 2*margin);

		int inborder = (int)((float)size.width * this.inborder); 
		
		// draw the marker
		g2d.setColor(this.markerColor);
		g2d.drawRect(pos.x, pos.y, size.width, size.height);
		Polygon poly = new Polygon ();
		poly.addPoint(pos.x + inborder, pos.y + inborder);
		poly.addPoint(pos.x + size.width - inborder, pos.y + inborder);
		poly.addPoint(pos.x + size.width / 2, pos.y + size.height - inborder);
		g2d.fill(poly);
	}


}
