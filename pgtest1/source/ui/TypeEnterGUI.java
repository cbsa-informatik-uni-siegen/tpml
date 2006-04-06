package ui;



import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PushbackReader;
import java.io.StringReader;

import typer.Translator;
import typer.lexer.Lexer;
import typer.node.Start;
import typer.parser.Parser;
import typing.MonoType;
import typing.ProofNode;

public class TypeEnterGUI extends JComponent {
	
	private JTextField 		textField = null;
	
	private ProofNode 		node = null;
	
	public TypeEnterGUI (ProofNode node) {
		this.node = node;
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		JPanel panel = new JPanel ();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		
		JLabel label = new JLabel("Type: ");
		panel.add (label);
		
		textField = new JTextField ();
		panel.add(textField);
		
		add(panel);
		panel.setBorder (new BevelBorder (BevelBorder.RAISED));
		setBorder(new LineBorder (java.awt.Color.BLACK, 1));
		
		textField.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				handleTypeEntered ();
			}
		});
	}

	public void addTypeEnterListener (TypeEnterListener listener) {
		this.listenerList.add(TypeEnterListener.class, listener);
	}
	
	public void removeTypeEnterListener (TypeEnterListener listener) {
		this.listenerList.remove(TypeEnterListener.class, listener);
	}
	
	private void handleTypeEntered () {
		try {
			// Allocate the parser
			Parser parser = new Parser(new Lexer(new PushbackReader(new StringReader(
					textField.getText()), 1024)));
			
			// Parse the input
			Start tree = parser.parse();

			// translate the AST to a small step expression
			Translator translator = new Translator();
			tree.apply(translator);
			
			fireAccepted (textField.getText(), translator.getType());
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	private void fireAccepted (String typeString, MonoType type) {
		Object objects[] = this.listenerList.getListenerList();
		for (int i=objects.length-2; i>=0; i-=2) {
			if (objects[i] == TypeEnterListener.class) {
				((TypeEnterListener)objects[i+1]).typeAccepted(this, typeString, node, type);
			}
		}
	}
	
	private void fireRejected (String typeString) {
		Object objects[] = this.listenerList.getListenerList();
		for (int i=objects.length-2; i>=0; i-=2) {
			if (objects[i] == TypeEnterListener.class) {
				((TypeEnterListener)objects[i+1]).typeRejected(this);
			}
		}
	}
}
