import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.StringReader;
import java.util.LinkedList;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.LanguageParser;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.theme.Theme;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerComponent;

public class Main {
	
	private static String expression = "let f = lambda x. if x = 0 then 1 else f * (x - 1) in f 3";
	
	
	private static ProofNode nextNode(TypeCheckerProofModel model) {
	    LinkedList<ProofNode> nodes = new LinkedList<ProofNode>();
	    nodes.add(model.getRoot());
	    while (!nodes.isEmpty()) {
	      ProofNode node = nodes.poll();
	      if (node.getSteps().length == 0) {
	        return node;
	      }
	      for (int n = 0; n < node.getChildCount(); ++n) {
	        nodes.add(node.getChildAt(n));
	      }
	    }
	    throw new IllegalStateException("Unable to find next node");
	}
	
	public static void main (String[] args) {
		try {
			LanguageFactory lf = LanguageFactory.newInstance();	
			Language language = lf.getLanguageById("L2");
			
			LanguageParser parser = language.newParser(new StringReader(Main.expression));
			
			TypeCheckerProofModel model = language.newTypeCheckerProofModel(parser.parse());

			try {
				do {
					ProofNode node = Main.nextNode(model);
					model.guess(node);
					
				} while (true);
			}
			catch (Exception e) {
				
			}
			
			TestDialog dialog = new TestDialog ();
			// generate a default theme
			Theme theme = new Theme ();
			AbstractRenderer.setTheme(theme, dialog);
			
			TypeCheckerComponent component = new TypeCheckerComponent (model);
			dialog.setContent(component);
			
			
			dialog.setSize(new Dimension (640, 480));
			dialog.setVisible(true);
			dialog.addWindowListener(new WindowAdapter () {
				public void windowClosing (WindowEvent e) {
					System.exit (0);
				}
			});
			component.setAvailableWidth(dialog.getWidth());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
		
		
	}
}
