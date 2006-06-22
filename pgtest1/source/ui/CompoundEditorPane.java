package ui;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledEditorKit;

import languages.LanguageScannerException;


public class CompoundEditorPane extends JPanel {
	
	private class Mark {
		
		public int 		position;
		
		public String	message;
		
		public int		start;
		
		public int 		end;
		
		public Mark (int position, String message, int start, int end) {
			this.position 	= position;
			this.message	= message;
			this.start		= start;
			this.end		= end;
		}
	};

	private class SideBar extends JComponent {
		
		
		private JScrollPane scrollPane;
		
		private int 		fontHeight;
		
		private	int			adjust;
		
		private int			inset;
		
		private Mark[]		marks = null;
		
		private JTextComponent textComponent = null;
		
		
		public SideBar (JScrollPane scrollPane, Font font, int inset) {
			super ();
			adjust = 0;
			this.inset = inset;

			scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				
				public void adjustmentValueChanged(AdjustmentEvent e) {
					Adjustable adjustable = e.getAdjustable();
					adjust = adjustable.getValue();
					repaint ();
				}
			});
			
			this.scrollPane = scrollPane;
			FontMetrics fm 	= getFontMetrics (font);
			this.fontHeight = fm.getHeight();
			
			this.addMouseListener(new MouseAdapter() {
				public void mousePressed (MouseEvent event) {
					if (marks != null) {
						for (Mark m : marks) {
							int y = m.position * fontHeight;
							Rectangle r = new Rectangle (0, y - fontHeight / 2, 16, fontHeight);
							if (r.contains(event.getX(), event.getY())) {
								textComponent.setSelectionStart (m.start);
								textComponent.setSelectionEnd(m.end);
								return;
							}
						}
					}
					
				}
			});
		}
		
		public void setTextComponent (JTextComponent textComponent) {
			this.textComponent = textComponent;
		}

		public void setMarks (Mark[] marks) {
			this.marks = marks;
		}
		public void paintMark (Graphics gr, int y) {
			y -= adjust + inset;
			gr.drawImage(errorIcon.getImage(), 0, y - 8, getBackground(), this);
		}
		
		public void paintComponent (Graphics gr) {
			gr.setColor(getBackground ());
			gr.fillRect(0, 0, getWidth(), getHeight ());
			
			if (marks != null) {
				for (Mark mark : marks) {
					paintMark (gr, mark.position * fontHeight);
				}
			}
		}
		
		@Override
		public String getToolTipText(MouseEvent event) {
			if (marks != null) {
				for (Mark m : marks) {
					int y = m.position * fontHeight;
					Rectangle r = new Rectangle (0, y - fontHeight / 2, 16, fontHeight);
					if (r.contains(event.getX(), event.getY())) {
						return m.message;
					}
				}
			}
			return super.getToolTipText(event);
		}

		
	}
	
	private SideBar 			sideBar;
	
	private JEditorPane			editorPane;
	
	private JScrollPane			scrollPane;
	
	private MLStyledDocument	document;
	
	public static ImageIcon 	errorIcon= null;
	
	public CompoundEditorPane() {
		super ();
			
		setLayout (null);
		
		if (CompoundEditorPane.errorIcon == null) {
			try {
				URL url = CompoundEditorPane.class.getResource("icons/error.gif");
				errorIcon = new ImageIcon (url);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.scrollPane = new JScrollPane ();
		this.editorPane = new MLStyledEditor();
		this.editorPane.setEditorKit(new StyledEditorKit());
		document = new MLStyledDocument ();
		this.editorPane.setDocument(document);
		this.editorPane.setAutoscrolls(false);
		
		int inset = this.scrollPane.getInsets().top + 
					this.scrollPane.getViewport().getInsets().top + 
					this.editorPane.getInsets().top;

		this.sideBar = new SideBar (this.scrollPane, this.editorPane.getFont(), inset);
		this.sideBar.setTextComponent(editorPane);
		ToolTipManager.sharedInstance().registerComponent(this.sideBar);
		
		add (sideBar);
		add (scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		this.scrollPane.setViewportView (this.editorPane);
		this.scrollPane.setBackground(Color.WHITE);
		this.scrollPane.getViewport().setBackground(Color.WHITE);
		
		addComponentListener (new ComponentAdapter () {
			public void componentResized (ComponentEvent event) {
				CompoundEditorPane.this.sideBar.setBounds(0, 
						0, 16, CompoundEditorPane.this.getHeight ());
				CompoundEditorPane.this.scrollPane.setBounds (17, 0, CompoundEditorPane.this.getWidth () - 16 , CompoundEditorPane.this.getHeight ());
			}
		});
		
		
		document.addPropertyChangeListener("exceptions", new PropertyChangeListener() {
			public void propertyChange (PropertyChangeEvent event) {
				buildMarks ();
			}
		});
		
		buildMarks ();
	}
	
	private void buildMarks () {
		LanguageScannerException[] excs = document.getExceptions();
		int i = -1;
		int count = 0;
		Element root = document.getDefaultRootElement();
		for (LanguageScannerException e : excs) {
			int line = root.getElementIndex(e.getLeft());
			if (i == line) continue;
			++count;
			i = line;
		}
		
		Mark[] marks = new Mark[count];
		i = -1;
		count = 0;
		for (LanguageScannerException e : excs) {
			int line = root.getElementIndex(e.getLeft ());
			if (i == line) continue;
			
			marks [count++] = new Mark (line + 1, e.getMessage(), e.getLeft(), e.getRight ());
		}
		sideBar.setMarks(marks);
		sideBar.repaint();
	}
	
	public JEditorPane getEditorPane () {
		return this.editorPane;
	}
	
}
