package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.BorderLayout;
import java.awt.Color ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame ;
import javax.swing.JPanel;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.WindowConstants ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeCellRenderer ;
import javax.swing.tree.DefaultTreeModel ;
import javax.swing.tree.TreePath;


public class AbstractSyntaxTreeUI
{
  private JFrame jFrameAbstractSyntaxTree ;


  private GridBagConstraints gridBagConstraints ;


  private GridBagLayout gridBagLayout ;


  private JTree jTreeAbstractSyntax ;


  private DefaultMutableTreeNode rootNode ;


  private DefaultTreeModel treeModel ;


  private DefaultTreeCellRenderer cellRenderer ;


  private JScrollPane jScrollPaneAbstractSyntax ;
  
  private JPanel jPanel;
  
  private JButton jBexpand;
  private JButton jBclose;


  private BorderLayout borderLayout;
  


  public AbstractSyntaxTreeUI ( )
  {
    // Tree - Renderer
    this.cellRenderer = new DefaultTreeCellRenderer ( ) ;
    this.cellRenderer.setIcon ( null ) ;
    this.cellRenderer.setLeafIcon ( null ) ;
    this.cellRenderer.setOpenIcon ( null ) ;
    this.cellRenderer.setClosedIcon ( null ) ;
    this.cellRenderer.setDisabledIcon ( null ) ;
    this.cellRenderer.setFont ( new Font ( "SansSerif" , Font.PLAIN , 16 ) ) ;
    this.cellRenderer.setBackground ( Color.WHITE ) ;
    this.cellRenderer.setBackgroundNonSelectionColor ( Color.WHITE ) ;
    this.cellRenderer
        .setBackgroundSelectionColor ( new Color ( 225 , 225 , 255 ) ) ;
    this.cellRenderer.setBorderSelectionColor ( Color.BLUE ) ;
    this.cellRenderer.setTextSelectionColor ( Color.BLACK ) ;
    this.cellRenderer.setTextNonSelectionColor ( Color.BLACK ) ;
    // Tree - TreeModel
    this.treeModel = new DefaultTreeModel ( this.rootNode ) ;
    // Tree
    this.jTreeAbstractSyntax = new JTree ( this.treeModel ) ;
    this.jTreeAbstractSyntax.setCellRenderer ( this.cellRenderer ) ;
    this.jTreeAbstractSyntax.getSelectionModel ( ).addTreeSelectionListener (
        new AbstractSyntaxTreeListener ( this ) ) ;
    // Tree - ScrollPane
    this.jScrollPaneAbstractSyntax = new JScrollPane ( this.jTreeAbstractSyntax ) ;
    // GridBagLayout
    this.gridBagLayout = new GridBagLayout ( ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    //BorderLayout
    this.borderLayout = new BorderLayout();
    
    
    //Buttons
    this.jBclose = new JButton("close all");
    this.jBclose.addActionListener( new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            close();  // code to execute when button is pressed
        }
    });
    
    this.jBexpand = new JButton("expand");
    this.jBexpand.addActionListener( new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            expand();  // code to execute when button is pressed
        }
    });
    
    
    // Panel
    this.jPanel = new JPanel();
    this.jPanel.add(jBclose);
    this.jPanel.add(jBexpand);
        
    // Frame
    this.jFrameAbstractSyntaxTree = new JFrame ( ) ;
    //this.jFrameAbstractSyntaxTree.setLayout ( this.gridBagLayout ) ;
    this.jFrameAbstractSyntaxTree.setLayout ( this.borderLayout ) ;
    this.jFrameAbstractSyntaxTree.setTitle ( "AbstractSyntaxTree" ) ;
    this.jFrameAbstractSyntaxTree.setSize ( 600 , 450 ) ;
    this.jFrameAbstractSyntaxTree.setLocation ( 100 , 0 ) ;
    this.jFrameAbstractSyntaxTree
        .setDefaultCloseOperation ( WindowConstants.HIDE_ON_CLOSE ) ;
    // Build
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanel.add(jScrollPaneAbstractSyntax);
    //this.jFrameAbstractSyntaxTree.getContentPane ( ).add (this.jScrollPaneAbstractSyntax , this.gridBagConstraints ) ;
    this.jFrameAbstractSyntaxTree.getContentPane ( ).add (this.jScrollPaneAbstractSyntax , BorderLayout.CENTER ) ;

    
    this.jFrameAbstractSyntaxTree.getContentPane ( ).add (this.jPanel , BorderLayout.SOUTH ) ;
  }


  protected void expand() {
	// TODO Auto-generated method stub
	  
	  int start = this.jTreeAbstractSyntax.getSelectionRows()[0];
	  //this.jTreeAbstractSyntax.expa
	  while( start < this.jTreeAbstractSyntax.getRowCount() ) {
		  this.jTreeAbstractSyntax.expandRow( start );
	      start++;
	 }


	 //TreePath path = this.jTreeAbstractSyntax.getSelectionPath();
	 //int pathCount = path.get();
	 //for (int i = 0 ; i < path.getPathCount(); i++) 
	 //{
     //    path.(i);
     //}
	  //this.jTreeAbstractSyntax.expandPath(path);
	  //int [] selection = this.jTreeAbstractSyntax.getSelectionRows();
	  //for (int i = 0 ; i < selection.length ; i++)
	  //{
		//   System.out.println("Row: "+i);
	  //this.jTreeAbstractSyntax.expandRow(i);
	  //}
}


protected void close() {
	// TODO Auto-generated method stub
	  this.jTreeAbstractSyntax.collapseRow(0);
	
}


public void appendNode ( DefaultMutableTreeNode pChild ,
      DefaultMutableTreeNode pParent )
  {
    this.treeModel
        .insertNodeInto ( pChild , pParent , pParent.getChildCount ( ) ) ;
  }


  public void expandRow ( int pIndex )
  {
    this.jTreeAbstractSyntax.expandRow ( pIndex ) ;
  }


  public JTree getJAbstractSyntaxTree ( )
  {
    return this.jTreeAbstractSyntax ;
  }


  public void nodeChanged ( DefaultMutableTreeNode pNode )
  {
    this.treeModel.nodeChanged ( pNode ) ;
  }


  public void setRootNode ( DefaultMutableTreeNode pRootNode )
  {
    this.rootNode = pRootNode ;
    this.treeModel.setRoot ( this.rootNode ) ;
  }


  public void setVisible ( boolean pVisible )
  {
    this.jFrameAbstractSyntaxTree.setVisible ( pVisible ) ;
  }
}
