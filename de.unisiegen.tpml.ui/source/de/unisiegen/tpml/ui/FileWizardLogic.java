package de.unisiegen.tpml.ui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

import javax.swing.border.LineBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.util.Theme;

/**
 * This organizes the logic of the {@link FileWizard} <br>
 * It also hadels to save the expandedstaes of the tree-elements
 * 
 * @author michael
 *
 */
public class FileWizardLogic
{
	/**
	 * the instance of the {@link FileWizard}
	 */
	private FileWizard fileWizard;
	
	
	/**
	 * the preferences for this class saving the expand
	 */
	private Preferences preferences;
	
	/**
	 * the vonstructor
	 *
	 * @param pFileWizard the actual {@link FileWizard}
	 */
	public FileWizardLogic (FileWizard pFileWizard)
	{
		this.fileWizard = pFileWizard;
		this.preferences = Preferences.userNodeForPackage ( FileWizard.class ) ;
	}
	/**
	 * this methode dose the job. 
	 * first it fills up the jTree with the languages
	 * then handels the actionson the tree
	 */
	public void getLanguages ()
	{
		//	determine the list of available languages
	  LanguageFactory factory = LanguageFactory.newInstance();
	  Language[] available = factory.getAvailableLanguages();

	  // setup the tree with the available languages
	  // every languageclass gets its own category
	  // every class gets a leaf
	  DefaultMutableTreeNode root = new DefaultMutableTreeNode("Languages");
	  
	  // save the actual category. If it changes, a new category will be created
	  int actualCategory=-1;
	  
	  for (Language language : available) 
	  {
	  	// buil up the name: "l0 (balbalba)"
	  	String name = language.getName();
	  	name += " (";
	  	name += language.getTitle();
	  	name += ")";
	  	
	  	// get the new Category: it will be parsed out of the 2nd digit of the name.
	  	// if this is not a number it will crash
	  	
	  	int newCategory=-1;
	  	try
	  	{
	  		newCategory = Integer.parseInt(""+name.toCharArray()[1]);
	  	}
	  	catch (IndexOutOfBoundsException iofb)
	  	{
	  		System.out.println("Language Name dose not start with LX for X is a number.");
	  	}
	  	catch (NumberFormatException nfe)
	  	{
	  		System.out.println("Language Name dose not start with LX for X is a number.");
	  	}
	  	
	  	// if the category is different, create a new one
	  	if (actualCategory!=newCategory)
	  	{
	  		DefaultMutableTreeNode next = new DefaultMutableTreeNode("L"+name.toCharArray()[1]);
	  		root.add(next);
	  		actualCategory=newCategory;
	  		DefaultMutableTreeNode add = new DefaultMutableTreeNode(name);
		  	next.add(add);
	  	}
	  	else // if the catogory is the same as the last one only add the new entry
	  	{
	  		DefaultMutableTreeNode add = new DefaultMutableTreeNode(name);
		  	// put it to the last leaf 
		  	((DefaultMutableTreeNode)root.getLastLeaf().getParent()).add(add);
	  	}
	  }
	  
	  // set the build root to the root of the File Wizard
	  ((DefaultTreeModel)this.fileWizard.languagesTree.getModel( ) ).setRoot(root);
	  
	  // makes the tree loooking nice
	  DefaultTreeCellRenderer cr = ((DefaultTreeCellRenderer)this.fileWizard.languagesTree.getCellRenderer ( ));
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
    
    
    // Bugfix for Linux
    this.fileWizard.languagesTree.setRowHeight(0);
    
    ((DefaultTreeModel) this.fileWizard.languagesTree.getModel()).nodeChanged(root);
    
    //this.fileWizard.languagesTree.setRootVisible(false);

    
    // expand all expanded last expanded. The first time the programm is started everey node will be expanded.
    int i = 0 ;
    while ( i < this.fileWizard.languagesTree.getRowCount ( ) )
    {
    	boolean expand;
    	expand = this.preferences.getBoolean ( this.fileWizard.languagesTree.getPathForRow(i).toString() , true ) ;
    	if (expand)
    	{
    		this.fileWizard.languagesTree.expandRow ( i ) ;
    	}
      i ++ ;
    }
	  
    // get a selectionListener on the tree
    // teh ok-button will be enabled and the selection will be saved
		this.fileWizard.languagesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent event)
			{
				treeSelectionHanlder(event);
			}
		});
	  
	  // get a expansionListener on the tree
		// the expansion will be saved
		this.fileWizard.languagesTree.addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent event)
			{
				treeCollapesedHandler(event);
			}

			public void treeExpanded(TreeExpansionEvent event)
			{
				treeCollapesedHandler(event);
			}
		});
	 
		// get a keyListener on the tree
		// with ESC the windows will be disposed, with ENTER the same will happen like clicking ok
	  this.fileWizard.languagesTree.addKeyListener(new KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent event)
			{
				keyPressedHandler(event);
			}
		});
	  
	  // get a mouseListener on the tree
	  // a double-click will do the same like cklicking the ok-button
	  this.fileWizard.languagesTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked (MouseEvent event)
			{
				mouseClickHandler(event);
			}
		});
	  
	  // set the selected element. The first time the nothing will be selced.
		// This will only function if the collapsed state is reorganized
		int selected = this.preferences.getInt("SelectedElement", 0);
		
		// -1 stand for nothing selected. If the a row is selected that is bigger than the rowCount
		// of the tree nothing will be selected
		if (selected != -1 && selected <= this.fileWizard.languagesTree.getRowCount())
		{
			this.fileWizard.languagesTree.setSelectionRow(selected);

			// scroll to the selected one
			this.fileWizard.languagesTree.scrollRowToVisible(selected);
		}    
    //((DefaultTreeModel) this.fileWizard.languagesTree.getModel()).nodeChanged(root);

	}
	
	/**
	 * The handler for the treeCollapesListener and the treeExpandedListener. <br>
	 * Simply save the expanded states of all treeentries.
	 *
	 * @param event
	 */
	protected void treeCollapesedHandler(TreeExpansionEvent event)
	{
		// save the expanded states
		int i = 0 ;
    while ( i < this.fileWizard.languagesTree.getRowCount ( ) )
    {
    	boolean expand;
    	//expand = ((DefaultMutableTreeNode) this.fileWizard.languagesTree.getPathForRow(i).getPath()).is
    	expand = this.fileWizard.languagesTree.isExpanded(i);
    	this.preferences.putBoolean ( this.fileWizard.languagesTree.getPathForRow(i).toString() , expand ) ;
      i ++ ;
    }
    
    // save the selected one once again because the rownumbaer has changed
    try
    {
    		this.preferences.putInt("SelectedElement", this.fileWizard.languagesTree.getSelectionRows()[0]);    	
    }
    catch (NullPointerException npe)
    {
    	// if theN ullPointerException is throughn there was no element selected
    	this.preferences.putInt("SelectedElement", -1);
    }
    
	}
	
	/**
	 * The handler for the KeyListener. <br>
	 * React on ENTER and ESC. If the key is Enter and a languages is selected, the window will dispose.
	 * If the key is ESC, the selected language will be reset to null and the window will dispose. 
	 *
	 * @param event
	 */
	protected void keyPressedHandler(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if (this.fileWizard.language != null)
			{
				this.fileWizard.dispose();
			}
		}
		else if (event.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			this.fileWizard.language = null;
			this.fileWizard.dispose();
		}
	}
	
	/**
	 * The handler for the mouseClickedListener. <br>
	 * If a doubleclick is recognized the window will dispose if a language is selected.
	 *
	 * @param event
	 */
	protected void mouseClickHandler (MouseEvent event)
	{
		if (event.getClickCount() == 2) 
		{
			if ( this.fileWizard.language != null)
			{
				this.fileWizard.dispose();
			}
		}
	}
	
	
	/**
	 * The Handler for the treeSelectionListner. <br>
	 * If a element is selected and it is a language, the language is set. The selected element of the 
	 * tree is saved.  
	 * 
	 * @param event
	 */
	protected void treeSelectionHanlder(TreeSelectionEvent event)
	{
		TreePath tp = event.getNewLeadSelectionPath();
		// sroll to the selected element
		this.fileWizard.languagesTree.scrollPathToVisible(tp);
		//
		if (tp != null && ((DefaultMutableTreeNode)tp.getLastPathComponent()).isLeaf())
		{
			// save the selceted element...
			//this.preferences.putInt("SelectedElement", tp.getPathCount());
			this.preferences.putInt("SelectedElement", this.fileWizard.languagesTree.getSelectionRows()[0]);
			
			// find the correct language
			// get all available languages
			Language newLanguage = null;
			LanguageFactory factory = LanguageFactory.newInstance();
			Language[] available = factory.getAvailableLanguages();
			// old version
//			for (Language language : available) 
//			  {
//				 	if ((tp.toString()).contains(language.getName()))
//				 	{
//				 		newLanguage = language;
//				 	} 	
//			  }
			
			for (Language language : available) 
		  {
				String toCompare = "";
				String [] tmp = tp.toString().split(",");
				toCompare = tmp[2];
				toCompare = toCompare.trim();
			 	if (toCompare.equals(language.getName()+" ("+language.getTitle()+")]"))
			 	{
			 		newLanguage = language;
			 		break;
			 	} 	
		  }

			if (newLanguage != null)
			{
				this.fileWizard.language = newLanguage;
				this.fileWizard.descriptionTextArea.setText(this.fileWizard.language.getDescription());
				this.fileWizard.okButton.setEnabled(true);
			}
			else
			{
				this.fileWizard.language = null;
				this.fileWizard.descriptionTextArea.setText("");
				this.fileWizard.okButton.setEnabled(false);
			}
		}
		else
		{
			this.fileWizard.language = null;
			this.fileWizard.descriptionTextArea.setText("");
			this.fileWizard.okButton.setEnabled(false);
		}
		
	}

	

}
