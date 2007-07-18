package de.unisiegen.tpml.ui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.graphics.Theme;

public class FileWizardLogic
{
	private FileWizard myfw;
	
	public FileWizardLogic (FileWizard pFileWizard)
	{
		myfw = pFileWizard;
	}
	public void getLanguages ()
	{
		//	determine the list of available languages
	  LanguageFactory factory = LanguageFactory.newInstance();
	  Language[] available = factory.getAvailableLanguages();

	  // setup the list model with the available languages
	  //DefaultTreeModel languagesModel = new DefaultListModel();
	  DefaultMutableTreeNode root = new DefaultMutableTreeNode("L");
	  //DefaultMutableTreeNode first = new DefaultMutableTreeNode("0");
	  //root.add(first);
	  int aktuell=-1;
	  
	  for (Language language : available) 
	  {
	  	String name = language.getName();
	  	int newAktuell=Integer.valueOf(""+name.toCharArray()[1]);
	  	if (aktuell!=newAktuell)
	  	{
	  		DefaultMutableTreeNode next = new DefaultMutableTreeNode(""+name.toCharArray()[1]);
	  		root.add(next);
	  		aktuell=newAktuell;
	  		DefaultMutableTreeNode add = new DefaultMutableTreeNode(name);
		  	next.add(add);
	  	}
	  	else
	  	{
	  		DefaultMutableTreeNode add = new DefaultMutableTreeNode(name);
		  	// put it to the last leaf 
		  	((DefaultMutableTreeNode)root.getLastLeaf().getParent()).add(add);
	  	}
	  	
	  	
	  }
	  //((DefaultTreeModel) this.jTreeOutline.getModel ( )).setRoot ( root )
	  
	  ((DefaultTreeModel)myfw.languagesTree.getModel( ) ).setRoot(root);
	  // schön machen
	  DefaultTreeCellRenderer cr = ((DefaultTreeCellRenderer)myfw.languagesTree.getCellRenderer ( ));
	  cr.setIcon ( null ) ;
	  cr.setLeafIcon ( null ) ;
	  cr.setOpenIcon ( null ) ;
	  cr.setClosedIcon ( null ) ;
	  cr.setDisabledIcon ( null ) ;
	  cr.setBackground ( Color.WHITE ) ;
	  cr.setBackgroundNonSelectionColor ( Color.WHITE ) ;
	  Color BORDER = new Color (235, 235, 255);
	  cr.setBackgroundSelectionColor ( BORDER ) ;
    cr.setFont ( Theme.currentTheme ( ).getFont ( ) ) ;
    cr.setBorderSelectionColor ( Color.BLUE ) ;
    cr.setTextSelectionColor ( Color.BLACK ) ;
    cr.setTextNonSelectionColor ( Color.BLACK ) ;
    cr.setBorder ( new LineBorder ( Color.WHITE ) ) ;

	  //myfw.languagesTree = new JTree(top);
	  
	  myfw.languagesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent event)
			{
				treeSelectionHanlder(event);
			}
		});
	  
	  myfw.languagesTree.addKeyListener(new KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent event)
			{
				keyPressedHandler(event);
			}
		});
	  
	  myfw.languagesTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked (MouseEvent event)
			{
				mouseClickHAndler(event);
			}
		});
	  
	  
	  // TODO Sinnfreie Testoperationen...
	  myfw.languagesTree.collapseRow(3);
	  myfw.setTitle("DOOFO");
	}
	
	protected void keyPressedHandler(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if (myfw.language != null)
			{
				myfw.dispose();
			}
		}
		else if (event.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			myfw.language = null;
			myfw.dispose();
		}
	}
	
	protected void mouseClickHAndler (MouseEvent event)
	{
		if (event.getClickCount() == 2) 
		{
			if ( myfw.language != null)
			{
				myfw.dispose();
			}
      
		}
	}
	
	
	/**
	 * The Handler for the treeSelectionListner
	 * 
	 * @param event
	 */
	protected void treeSelectionHanlder(TreeSelectionEvent event)
	{
		TreePath tp = event.getNewLeadSelectionPath();
		if (tp != null)
		{
			// TODO TESTAUSGABE System.out.println("  Selektiert: " + tp.toString());
			// languages finden...
			// get all availables...
			Language newLanguage = null;
			LanguageFactory factory = LanguageFactory.newInstance();
			Language[] available = factory.getAvailableLanguages();
			for (Language language : available) 
			  {
				 	if ((tp.toString()).contains(language.getName()))
				 	{
				 		newLanguage = language;
				 	} 	
			  }
			if (newLanguage != null)
			{
				myfw.language = newLanguage;
				myfw.descriptionTextArea.setText(myfw.language.getDescription());
				myfw.okButton.setEnabled(true);
			}
			else
			{
				myfw.language = null;
				myfw.descriptionTextArea.setText("");
				myfw.okButton.setEnabled(false);
			}
		}
		else
		{
			myfw.language = null;
			myfw.descriptionTextArea.setText("");
			myfw.okButton.setEnabled(false);
		}
		
	}

	

}
