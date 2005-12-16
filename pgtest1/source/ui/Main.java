package ui;

import java.awt.event.*;
import java.io.PushbackReader;
import java.io.StringReader;
import java.text.*;
import java.util.*;

import javax.swing.JOptionPane;

import l1.Translator;
import l1.lexer.Lexer;
import l1.node.Start;
import l1.parser.Parser;
import smallstep.*;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		AttributedCharacterIterator fit = format.formatToCharacterIterator(arguments);
		Set<AttributedCharacterIterator.Attribute> attr = fit.getAllAttributeKeys();
		 
		Iterator it = attr.iterator();
		while (it.hasNext()) {
			AttributedCharacterIterator.Attribute a = (AttributedCharacterIterator.Attribute)it.next();
			Object o = fit.getAttribute(a);
			System.out.println("attr: " + a + " = " + o);
		}
	
		System.out.println(format.format(arguments));
		 
		*/
		
		Mainwindow mw = new Mainwindow();
		mw.addWindowListener(new WindowAdapter () {
			public void windowClosing(WindowEvent e) { System.exit(0); };
		});
		mw.setVisible(true);
		

		/*
	    try {
	      // Allocate the parser
	      Parser parser = new Parser(new Lexer(new PushbackReader(new StringReader(
	          "let x = lambda x.if = x 0 then 1 else 0 in x 1"), 1024)));

	      // Parse the input
	      Start tree = parser.parse();

	      // translate the AST to a small step expression
	      Translator translator = new Translator();
	      tree.apply(translator);

	      RuleChain chain = new RuleChain ();
	      Expression e = translator.getExpression();
	      e.evaluate(chain);
	      SmallStepPrettyPrinter.createExpressionString(e, chain);
	      
	      chain = new RuleChain();
	      e = e.evaluate (chain);
	      
	      chain = new RuleChain();
	      e.evaluate(chain);
	      System.out.println ("======================================");
	      SmallStepPrettyPrinter.createExpressionString(e, chain);
	      
	      chain = new RuleChain();
	      e = e.evaluate (chain);
	      
	      chain = new RuleChain();
	      e.evaluate(chain);
	      System.out.println ("======================================");
	      SmallStepPrettyPrinter.createExpressionString(e, chain);
	      
	      
	      // evaluate the resulting small step expression
	      // SmallStepGUI gui = new SmallStepGUI(this, "SmallStep", true,
	      //    new SmallStepModel(translator.getExpression()));
	      //gui.setVisible(true);
	      
	    } catch (Exception e) {
	      //JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    */
	}

}
