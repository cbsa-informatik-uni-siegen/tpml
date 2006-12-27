package de.unisiegen.tpml.graphics.outline.ui ;


import java.awt.Color ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.event.InputEvent ;
import java.awt.event.KeyEvent ;
import java.util.ResourceBundle ;
import javax.swing.BorderFactory ;
import javax.swing.ImageIcon ;
import javax.swing.JCheckBox ;
import javax.swing.JCheckBoxMenuItem ;
import javax.swing.JMenu ;
import javax.swing.JMenuItem ;
import javax.swing.JPanel ;
import javax.swing.JPopupMenu ;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.KeyStroke ;
import javax.swing.border.TitledBorder ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel ;
import de.unisiegen.tpml.graphics.outline.AbstractOutline ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineActionListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineItemListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineKeyListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeSelectionListener ;


/**
 * This class creates the UI of the <code>Outline</code>.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineUI
{
  /**
   * The <code>GridBagConstraints</code>.
   */
  private GridBagConstraints gridBagConstraints ;


  /**
   * The <code>GridBagLayout</code>.
   */
  private GridBagLayout gridBagLayout ;


  /**
   * The tree.
   * 
   * @see #getJTreeAbstractSyntaxTree()
   */
  private JTree jTreeAbstractSyntaxTree ;


  /**
   * The root node.
   * 
   * @see #setRootNode(DefaultMutableTreeNode)
   */
  private DefaultMutableTreeNode rootNode ;


  /**
   * The <code>TreeModel</code>.
   * 
   * @see #getTreeModel()
   */
  private DefaultTreeModel treeModel ;


  /**
   * The <code>JScrollPane</code> of the <code>Outline</code>.
   */
  private JScrollPane jScrollPaneAbstractSyntaxTree ;


  /**
   * The preferences <code>JPanel</code>.
   */
  private JPanel jPanelPreferences ;


  /**
   * The main <code>JPanel</code>.
   * 
   * @see #getJPanelMain()
   */
  private JPanel jPanelMain ;


  /**
   * The <code>JCheckBox</code> replace.
   * 
   * @see #getJCheckBoxReplace()
   */
  private JCheckBox jCheckBoxReplace ;


  /**
   * The <code>JCheckBox</code> binding.
   * 
   * @see #getJCheckBoxBinding()
   */
  private JCheckBox jCheckBoxBinding ;


  /**
   * The <code>JCheckBox</code> unbound.
   * 
   * @see #getJCheckBoxUnbound()
   */
  private JCheckBox jCheckBoxUnbound ;


  /**
   * The <code>JCheckBox</code> selection.
   * 
   * @see #getJCheckBoxSelection()
   */
  private JCheckBox jCheckBoxSelection ;


  /**
   * The <code>JCheckBox</code> auto update.
   * 
   * @see #getJCheckBoxUnbound()
   */
  private JCheckBox jCheckBoxAutoUpdate ;


  /**
   * The <code>OutlineTreeSelectionListener</code>.
   * 
   * @see #getOutlineTreeSelectionListener()
   */
  private OutlineTreeSelectionListener outlineTreeSelectionListener ;


  /**
   * The <code>OutlineActionListener</code>.
   * 
   * @see #getOutlineActionListener()
   */
  private OutlineActionListener outlineActionListener ;


  /**
   * The <code>OutlineItemListener</code>.
   * 
   * @see #getOutlineItemListener()
   */
  private OutlineItemListener outlineItemListener ;


  /**
   * The <code>AbstractOutline</code>.
   * 
   * @see #getAbstractOutline()
   */
  private AbstractOutline abstractOutline ;


  /**
   * The <code>OutlineMouseListener</code>.
   * 
   * @see #getOutlineMouseListener()
   */
  private OutlineMouseListener outlineMouseListener ;


  /**
   * The <code>ResourceBundle</code>.
   */
  private ResourceBundle resourceBundle ;


  /**
   * The <code>JPopupMenu</code>.
   * 
   * @see #getJPopupMenu()
   */
  private JPopupMenu jPopupMenu ;


  /**
   * The <code>JMenuItem</code> expand.
   * 
   * @see #getJMenuItemExpand()
   */
  private JMenuItem jMenuItemExpand ;


  /**
   * The <code>JMenuItem</code> expand all.
   * 
   * @see #getJMenuItemExpandAll()
   */
  private JMenuItem jMenuItemExpandAll ;


  /**
   * The <code>JMenuItem</code> collapse.
   * 
   * @see #getJMenuItemCollapse()
   */
  private JMenuItem jMenuItemCollapse ;


  /**
   * The <code>JMenuItem</code> collapse all.
   * 
   * @see #getJMenuItemCollapseAll()
   */
  private JMenuItem jMenuItemCollapseAll ;


  /**
   * The <code>JMenuItem</code> close.
   * 
   * @see #getJMenuItemClose()
   */
  private JMenuItem jMenuItemClose ;


  /**
   * The <code>JMenuItem</code> close all.
   * 
   * @see #getJMenuItemCloseAll()
   */
  private JMenuItem jMenuItemCloseAll ;


  /**
   * The <code>JMenuItem</code> copy.
   * 
   * @see #getJMenuItemCopy()
   */
  private JMenuItem jMenuItemCopy ;


  /**
   * The <code>JCheckBoxMenuItem</code> binding.
   * 
   * @see #getJCheckBoxBinding()
   */
  private JCheckBoxMenuItem jMenuItemBinding ;


  /**
   * The <code>JCheckBoxMenuItem</code> unbound.
   * 
   * @see #getJCheckBoxUnbound()
   */
  private JCheckBoxMenuItem jMenuItemUnbound ;


  /**
   * The <code>JCheckBoxMenuItem</code> auto update.
   * 
   * @see #getJCheckBoxAutoUpdate()
   */
  private JCheckBoxMenuItem jMenuItemAutoUpdate ;


  /**
   * The </code>JMenu</code> preferences.
   */
  private JMenu jMenuPreferences ;


  /**
   * The <code>JCheckBoxMenuItem</code> replace.
   * 
   * @see #getJCheckBoxReplace()
   */
  private JCheckBoxMenuItem jMenuItemReplace ;


  /**
   * The <code>JCheckBoxMenuItem</code> selection.
   * 
   * @see #getJCheckBoxSelection()
   */
  private JCheckBoxMenuItem jMenuItemSelection ;


  /**
   * The <code>Insets</code>.
   */
  private Insets insets ;


  /**
   * This constructor creates the UI of the <code>Outline</code>.
   * 
   * @param pAbstractOutline The <code>Outline</code>.
   */
  public OutlineUI ( AbstractOutline pAbstractOutline )
  {
    this.abstractOutline = pAbstractOutline ;
    // Insets
    this.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    // Preferences
    this.resourceBundle = ResourceBundle
        .getBundle ( "de/unisiegen/tpml/graphics/outline/outline" ) ; //$NON-NLS-1$
    // Listener
    this.outlineItemListener = new OutlineItemListener ( this ) ;
    this.outlineActionListener = new OutlineActionListener ( this ) ;
    this.outlineTreeSelectionListener = new OutlineTreeSelectionListener ( this ) ;
    this.outlineMouseListener = new OutlineMouseListener ( this ) ;
    // PopupMenu
    createPopupMenu ( ) ;
    // Layout
    this.gridBagLayout = new GridBagLayout ( ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    // Panel Preferences
    this.jPanelPreferences = new JPanel ( ) ;
    this.jPanelPreferences.setLayout ( this.gridBagLayout ) ;
    this.jPanelPreferences.setBorder ( new TitledBorder ( BorderFactory
        .createLineBorder ( Color.black , 1 ) , "" , //$NON-NLS-1$
        TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
            "SansSerif" , Font.PLAIN , 12 ) ) ) ; //$NON-NLS-1$
    // CheckBox Selection
    this.jCheckBoxSelection = new JCheckBox ( this.resourceBundle
        .getString ( "selection" ) ) ; //$NON-NLS-1$
    this.jCheckBoxSelection.setMnemonic ( this.resourceBundle.getString (
        "selectionMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxSelection.setToolTipText ( this.resourceBundle
        .getString ( "selectionToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxSelection.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isSelection ( ) ) ;
    this.jCheckBoxSelection.setFocusable ( false ) ;
    this.jCheckBoxSelection.addItemListener ( this.outlineItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxSelection ,
        this.gridBagConstraints ) ;
    // CheckBox Binding
    this.jCheckBoxBinding = new JCheckBox ( this.resourceBundle
        .getString ( "binding" ) ) ; //$NON-NLS-1$
    this.jCheckBoxBinding.setMnemonic ( this.resourceBundle.getString (
        "bindingMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxBinding.setToolTipText ( this.resourceBundle
        .getString ( "bindingToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxBinding.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isBinding ( ) ) ;
    this.jCheckBoxBinding.setFocusable ( false ) ;
    this.jCheckBoxBinding.addItemListener ( this.outlineItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxBinding ,
        this.gridBagConstraints ) ;
    // CheckBox Unbound
    this.jCheckBoxUnbound = new JCheckBox ( this.resourceBundle
        .getString ( "unbound" ) ) ; //$NON-NLS-1$
    this.jCheckBoxUnbound.setMnemonic ( this.resourceBundle.getString (
        "unboundMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxUnbound.setToolTipText ( this.resourceBundle
        .getString ( "unboundToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxUnbound.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isUnbound ( ) ) ;
    this.jCheckBoxUnbound.setFocusable ( false ) ;
    this.jCheckBoxUnbound.addItemListener ( this.outlineItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxUnbound ,
        this.gridBagConstraints ) ;
    // CheckBox Replace
    this.jCheckBoxReplace = new JCheckBox ( this.resourceBundle
        .getString ( "replace" ) ) ; //$NON-NLS-1$
    this.jCheckBoxReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxReplace.setToolTipText ( this.resourceBundle
        .getString ( "replaceToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxReplace.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isReplace ( ) ) ;
    this.jCheckBoxReplace.setFocusable ( false ) ;
    this.jCheckBoxReplace.addItemListener ( this.outlineItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 3 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxReplace ,
        this.gridBagConstraints ) ;
    // CheckBox AutoUpdate
    this.jCheckBoxAutoUpdate = new JCheckBox ( this.resourceBundle
        .getString ( "autoUpdate" ) ) ; //$NON-NLS-1$
    this.jCheckBoxAutoUpdate.setMnemonic ( this.resourceBundle.getString (
        "autoUpdateMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxAutoUpdate.setToolTipText ( this.resourceBundle
        .getString ( "autoUpdateToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxAutoUpdate.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isAutoUpdate ( ) ) ;
    this.jCheckBoxAutoUpdate.setFocusable ( false ) ;
    this.jCheckBoxAutoUpdate.addItemListener ( this.outlineItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 4 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxAutoUpdate ,
        this.gridBagConstraints ) ;
    // Panel Main
    this.jPanelMain = new JPanel ( ) ;
    this.jPanelMain.setLayout ( this.gridBagLayout ) ;
    // TreeModel
    this.treeModel = new DefaultTreeModel ( this.rootNode ) ;
    // Tree AbstractOutline
    this.jTreeAbstractSyntaxTree = new JTree ( this.treeModel ) ;
    /*
     * ToolTipManager.sharedInstance ( ).registerComponent (
     * this.jTreeAbstractSyntaxTree ) ;
     */
    this.jTreeAbstractSyntaxTree.setCellRenderer ( new OutlineCellRenderer ( ) ) ;
    this.jTreeAbstractSyntaxTree.getSelectionModel ( )
        .addTreeSelectionListener ( this.outlineTreeSelectionListener ) ;
    this.jTreeAbstractSyntaxTree.setRowHeight ( 20 ) ;
    this.jTreeAbstractSyntaxTree.addMouseListener ( this.outlineMouseListener ) ;
    this.jTreeAbstractSyntaxTree
        .addKeyListener ( new OutlineKeyListener ( this ) ) ;
    // ScrollPane AbstractSyntax
    this.jScrollPaneAbstractSyntaxTree = new JScrollPane (
        this.jTreeAbstractSyntaxTree ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 0 , 1 , 0 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelMain.add ( this.jScrollPaneAbstractSyntaxTree ,
        this.gridBagConstraints ) ;
    // Panel Preferences
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 1 , 0 , 0 , 1 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 0 ;
    this.jPanelMain.add ( this.jPanelPreferences , this.gridBagConstraints ) ;
  }


  /**
   * Creates the <code>JPopupMenu</code> of the <code>Outline</code>.
   */
  private void createPopupMenu ( )
  {
    // PopupMenu
    this.jPopupMenu = new JPopupMenu ( ) ;
    // MenuItem Expand
    this.jMenuItemExpand = new JMenuItem ( this.resourceBundle
        .getString ( "expand" ) ) ; //$NON-NLS-1$
    this.jMenuItemExpand.setMnemonic ( this.resourceBundle.getString (
        "expandMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemExpand.setToolTipText ( this.resourceBundle
        .getString ( "expandToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemExpand.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemExpand.setActionCommand ( "expand" ) ; //$NON-NLS-1$
    this.jMenuItemExpand.addActionListener ( this.outlineActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemExpand ) ;
    // MenuItem ExpandAll
    this.jMenuItemExpandAll = new JMenuItem ( this.resourceBundle
        .getString ( "expandAll" ) ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.setMnemonic ( this.resourceBundle.getString (
        "expandAllMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.setToolTipText ( this.resourceBundle
        .getString ( "expandAllToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.setActionCommand ( "expandAll" ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.addActionListener ( this.outlineActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemExpandAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Collapse
    this.jMenuItemCollapse = new JMenuItem ( this.resourceBundle
        .getString ( "collapse" ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.setMnemonic ( this.resourceBundle.getString (
        "collapseMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.setToolTipText ( this.resourceBundle
        .getString ( "collapseToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.setActionCommand ( "collapse" ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.addActionListener ( this.outlineActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapse ) ;
    // MenuItem CollapseAll
    this.jMenuItemCollapseAll = new JMenuItem ( this.resourceBundle
        .getString ( "collapseAll" ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.setMnemonic ( this.resourceBundle.getString (
        "collapseAllMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.setToolTipText ( this.resourceBundle
        .getString ( "collapseAllToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.setIcon ( new ImageIcon ( getClass ( )
        .getResource ( "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.setActionCommand ( "collapseAll" ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.addActionListener ( this.outlineActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapseAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Close
    this.jMenuItemClose = new JMenuItem ( this.resourceBundle
        .getString ( "close" ) ) ; //$NON-NLS-1$
    this.jMenuItemClose.setMnemonic ( this.resourceBundle.getString (
        "closeMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemClose.setToolTipText ( this.resourceBundle
        .getString ( "closeToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemClose.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemClose.setActionCommand ( "close" ) ; //$NON-NLS-1$
    this.jMenuItemClose.addActionListener ( this.outlineActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemClose ) ;
    // MenuItem CloseAll
    this.jMenuItemCloseAll = new JMenuItem ( this.resourceBundle
        .getString ( "closeAll" ) ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.setMnemonic ( this.resourceBundle.getString (
        "closeAllMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.setToolTipText ( this.resourceBundle
        .getString ( "closeAllToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.setActionCommand ( "closeAll" ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.addActionListener ( this.outlineActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCloseAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Copy
    this.jMenuItemCopy = new JMenuItem ( this.resourceBundle
        .getString ( "copy" ) ) ; //$NON-NLS-1$
    this.jMenuItemCopy.setMnemonic ( this.resourceBundle.getString (
        "copyMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemCopy.setToolTipText ( this.resourceBundle
        .getString ( "copyToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemCopy.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/copy16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemCopy.setActionCommand ( "copy" ) ; //$NON-NLS-1$
    this.jMenuItemCopy.addActionListener ( this.outlineActionListener ) ;
    this.jMenuItemCopy.setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_C ,
        InputEvent.CTRL_MASK ) ) ;
    this.jPopupMenu.add ( this.jMenuItemCopy ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Preferences
    this.jMenuPreferences = new JMenu ( this.resourceBundle
        .getString ( "preferences" ) ) ; //$NON-NLS-1$
    this.jMenuPreferences.setMnemonic ( this.resourceBundle.getString (
        "preferencesMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuPreferences.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$  
    this.jMenuPreferences.setActionCommand ( "preferences" ) ; //$NON-NLS-1$
    this.jMenuPreferences.addActionListener ( this.outlineActionListener ) ;
    this.jPopupMenu.add ( this.jMenuPreferences ) ;
    // MenuItem Selection
    this.jMenuItemSelection = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "selection" ) ) ; //$NON-NLS-1$
    this.jMenuItemSelection.setMnemonic ( this.resourceBundle.getString (
        "selectionMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemSelection.setToolTipText ( this.resourceBundle
        .getString ( "selectionToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemSelection.setActionCommand ( "selection" ) ; //$NON-NLS-1$
    this.jMenuItemSelection.addActionListener ( this.outlineActionListener ) ;
    this.jMenuItemSelection.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isSelection ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemSelection ) ;
    // MenuItem Binding
    this.jMenuItemBinding = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "binding" ) ) ; //$NON-NLS-1$
    this.jMenuItemBinding.setMnemonic ( this.resourceBundle.getString (
        "bindingMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemBinding.setToolTipText ( this.resourceBundle
        .getString ( "bindingToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemBinding.setActionCommand ( "binding" ) ; //$NON-NLS-1$
    this.jMenuItemBinding.addActionListener ( this.outlineActionListener ) ;
    this.jMenuItemBinding.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isBinding ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemBinding ) ;
    // MenuItem Unbound
    this.jMenuItemUnbound = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "unbound" ) ) ; //$NON-NLS-1$
    this.jMenuItemUnbound.setMnemonic ( this.resourceBundle.getString (
        "unboundMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemUnbound.setToolTipText ( this.resourceBundle
        .getString ( "unboundToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemUnbound.setActionCommand ( "unbound" ) ; //$NON-NLS-1$
    this.jMenuItemUnbound.addActionListener ( this.outlineActionListener ) ;
    this.jMenuItemUnbound.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isUnbound ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemUnbound ) ;
    // MenuItem Replace
    this.jMenuItemReplace = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "replace" ) ) ; //$NON-NLS-1$
    this.jMenuItemReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemReplace.setToolTipText ( this.resourceBundle
        .getString ( "replaceToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemReplace.setActionCommand ( "replace" ) ; //$NON-NLS-1$
    this.jMenuItemReplace.addActionListener ( this.outlineActionListener ) ;
    this.jMenuItemReplace.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isReplace ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemReplace ) ;
    // MenuItem AutoUpdate
    this.jMenuItemAutoUpdate = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "autoUpdate" ) ) ; //$NON-NLS-1$
    this.jMenuItemAutoUpdate.setMnemonic ( this.resourceBundle.getString (
        "autoUpdateMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemAutoUpdate.setToolTipText ( this.resourceBundle
        .getString ( "autoUpdateToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemAutoUpdate.setActionCommand ( "autoUpdate" ) ; //$NON-NLS-1$
    this.jMenuItemAutoUpdate.addActionListener ( this.outlineActionListener ) ;
    this.jMenuItemAutoUpdate.setSelected ( this.abstractOutline
        .getOutlinePreferences ( ).isAutoUpdate ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemAutoUpdate ) ;
  }


  /**
   * Returns the <code>AbstractOutline</code>.
   * 
   * @return The <code>AbstractOutline</code>.
   * @see #abstractOutline
   */
  public AbstractOutline getAbstractOutline ( )
  {
    return this.abstractOutline ;
  }


  /**
   * Returns the <code>OutlineActionListener</code>.
   * 
   * @return The <code>OutlineActionListener</code>.
   * @see #outlineActionListener
   */
  public OutlineActionListener getOutlineActionListener ( )
  {
    return this.outlineActionListener ;
  }


  /**
   * Returns the <code>OutlineItemListener</code>.
   * 
   * @return The <code>OutlineItemListener</code>.
   * @see #outlineItemListener
   */
  public OutlineItemListener getOutlineItemListener ( )
  {
    return this.outlineItemListener ;
  }


  /**
   * Returns the <code>OutlineMouseListener</code>.
   * 
   * @return The <code>OutlineMouseListener</code>.
   * @see #outlineMouseListener
   */
  public OutlineMouseListener getOutlineMouseListener ( )
  {
    return this.outlineMouseListener ;
  }


  /**
   * Returns the <code>OutlineTreeSelectionListener</code>.
   * 
   * @return The <code>OutlineTreeSelectionListener</code>.
   * @see #outlineTreeSelectionListener
   */
  public OutlineTreeSelectionListener getOutlineTreeSelectionListener ( )
  {
    return this.outlineTreeSelectionListener ;
  }


  /**
   * Returns the <code>jCheckBoxAutoUpdate</code>.
   * 
   * @return The <code>jCheckBoxAutoUpdate</code>.
   * @see #jCheckBoxAutoUpdate
   */
  public JCheckBox getJCheckBoxAutoUpdate ( )
  {
    return this.jCheckBoxAutoUpdate ;
  }


  /**
   * Returns the <code>jCheckBoxBinding</code>.
   * 
   * @return The <code>jCheckBoxBinding</code>.
   * @see #jCheckBoxBinding
   */
  public JCheckBox getJCheckBoxBinding ( )
  {
    return this.jCheckBoxBinding ;
  }


  /**
   * Returns the <code>jCheckBoxReplace</code>.
   * 
   * @return The <code>jCheckBoxReplace</code>.
   * @see #jCheckBoxReplace
   */
  public JCheckBox getJCheckBoxReplace ( )
  {
    return this.jCheckBoxReplace ;
  }


  /**
   * Returns the <code>jCheckBoxSelection</code>.
   * 
   * @return The <code>jCheckBoxSelection</code>.
   * @see #jCheckBoxSelection
   */
  public JCheckBox getJCheckBoxSelection ( )
  {
    return this.jCheckBoxSelection ;
  }


  /**
   * Returns the <code>jCheckBoxUnbound</code>.
   * 
   * @return The <code>jCheckBoxUnbound</code>.
   * @see #jCheckBoxUnbound
   */
  public JCheckBox getJCheckBoxUnbound ( )
  {
    return this.jCheckBoxUnbound ;
  }


  /**
   * Returns the <code>jMenuItemAutoUpdate</code>.
   * 
   * @return The <code>jMenuItemAutoUpdate</code>.
   * @see #jMenuItemAutoUpdate
   */
  public JCheckBoxMenuItem getJMenuItemAutoUpdate ( )
  {
    return this.jMenuItemAutoUpdate ;
  }


  /**
   * Returns the <code>jMenuItemBinding</code>.
   * 
   * @return The <code>jMenuItemBinding</code>.
   * @see #jMenuItemBinding
   */
  public JCheckBoxMenuItem getJMenuItemBinding ( )
  {
    return this.jMenuItemBinding ;
  }


  /**
   * Returns the <code>jMenuItemClose</code>.
   * 
   * @return The <code>jMenuItemClose</code>.
   * @see #jMenuItemClose
   */
  public JMenuItem getJMenuItemClose ( )
  {
    return this.jMenuItemClose ;
  }


  /**
   * Returns the <code>jMenuItemCloseAll</code>.
   * 
   * @return The <code>jMenuItemCloseAll</code>.
   * @see #jMenuItemCloseAll
   */
  public JMenuItem getJMenuItemCloseAll ( )
  {
    return this.jMenuItemCloseAll ;
  }


  /**
   * Returns the <code>jMenuItemCollapse</code>.
   * 
   * @return The <code>jMenuItemCollapse</code>.
   * @see #jMenuItemCollapse
   */
  public JMenuItem getJMenuItemCollapse ( )
  {
    return this.jMenuItemCollapse ;
  }


  /**
   * Returns the <code>jMenuItemCollapseAll</code>.
   * 
   * @return The <code>jMenuItemCollapseAll</code>.
   * @see #jMenuItemCollapseAll
   */
  public JMenuItem getJMenuItemCollapseAll ( )
  {
    return this.jMenuItemCollapseAll ;
  }


  /**
   * Returns the <code>jMenuItemCopy</code>.
   * 
   * @return The <code>jMenuItemCopy</code>.
   * @see #jMenuItemCopy
   */
  public JMenuItem getJMenuItemCopy ( )
  {
    return this.jMenuItemCopy ;
  }


  /**
   * Returns the <code>jMenuItemExpand</code>.
   * 
   * @return The <code>jMenuItemExpand</code>.
   * @see #jMenuItemExpand
   */
  public JMenuItem getJMenuItemExpand ( )
  {
    return this.jMenuItemExpand ;
  }


  /**
   * Returns the <code>jMenuItemExpandAll</code>.
   * 
   * @return The <code>jMenuItemExpandAll</code>.
   * @see #jMenuItemExpandAll
   */
  public JMenuItem getJMenuItemExpandAll ( )
  {
    return this.jMenuItemExpandAll ;
  }


  /**
   * Returns the <code>jMenuItemReplace</code>.
   * 
   * @return The <code>jMenuItemReplace</code>.
   * @see #jMenuItemReplace
   */
  public JCheckBoxMenuItem getJMenuItemReplace ( )
  {
    return this.jMenuItemReplace ;
  }


  /**
   * Returns the <code>jMenuItemSelection</code>.
   * 
   * @return The <code>jMenuItemSelection</code>.
   * @see #jMenuItemSelection
   */
  public JCheckBoxMenuItem getJMenuItemSelection ( )
  {
    return this.jMenuItemSelection ;
  }


  /**
   * Returns the <code>jMenuItemUnbound</code>.
   * 
   * @return The <code>jMenuItemUnbound</code>.
   * @see #jMenuItemUnbound
   */
  public JCheckBoxMenuItem getJMenuItemUnbound ( )
  {
    return this.jMenuItemUnbound ;
  }


  /**
   * Returns the <code>jPanelMain</code>.
   * 
   * @return The <code>jPanelMain</code>.
   * @see #jPanelMain
   */
  public JPanel getJPanelMain ( )
  {
    return this.jPanelMain ;
  }


  /**
   * Returns the <code>jPopupMenu</code>.
   * 
   * @return The <code>jPopupMenu</code>.
   * @see #jPopupMenu
   */
  public JPopupMenu getJPopupMenu ( )
  {
    return this.jPopupMenu ;
  }


  /**
   * Returns the <code>jTreeAbstractSyntaxTree</code>.
   * 
   * @return The <code>jTreeAbstractSyntaxTree</code>.
   * @see #jTreeAbstractSyntaxTree
   */
  public JTree getJTreeAbstractSyntaxTree ( )
  {
    return this.jTreeAbstractSyntaxTree ;
  }


  /**
   * Returns the </code>treeModel</code>.
   * 
   * @return The </code>treeModel</code>.
   * @see #treeModel
   */
  public DefaultTreeModel getTreeModel ( )
  {
    return this.treeModel ;
  }


  /**
   * Sets the new root node.
   * 
   * @param pRootNode The new root node.
   */
  public void setRootNode ( DefaultMutableTreeNode pRootNode )
  {
    this.rootNode = pRootNode ;
    this.treeModel.setRoot ( this.rootNode ) ;
  }
}
