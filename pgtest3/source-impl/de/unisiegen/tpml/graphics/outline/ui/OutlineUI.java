package de.unisiegen.tpml.graphics.outline.ui ;


import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.event.InputEvent ;
import java.awt.event.KeyEvent ;
import java.util.ResourceBundle ;
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
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * This class creates the UI of the {@link Outline}.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineUI
{
  /**
   * The <code>String</code> preferences, used for the
   * <code>ActionCommand</code> and the <code>ResourceBundle</code>.
   */
  private static final String PREFERENCES = "preferences" ; //$NON-NLS-1$


  /**
   * The empty <code>Icon</code> path.
   */
  private static final String EMPTYICON = "/de/unisiegen/tpml/ui/icons/empty16.gif" ; //$NON-NLS-1$


  /**
   * The copy <code>Icon</code> path.
   */
  private static final String COPYICON = "/de/unisiegen/tpml/ui/icons/copy16.gif" ; //$NON-NLS-1$


  /**
   * The <code>String</code> copy, used for the <code>ActionCommand</code>
   * and the <code>ResourceBundle</code>.
   */
  public static final String COPY = "copy" ; //$NON-NLS-1$


  /**
   * The <code>String</code> closeAll, used for the <code>ActionCommand</code>
   * and the <code>ResourceBundle</code>.
   */
  public static final String CLOSEALL = "closeAll" ; //$NON-NLS-1$


  /**
   * The <code>String</code> close, used for the <code>ActionCommand</code>
   * and the <code>ResourceBundle</code>.
   */
  public static final String CLOSE = "close" ; //$NON-NLS-1$


  /**
   * The <code>String</code> collapseAll, used for the
   * <code>ActionCommand</code> and the <code>ResourceBundle</code>.
   */
  public static final String COLLAPSEALL = "collapseAll" ; //$NON-NLS-1$


  /**
   * The <code>String</code> collapse, used for the <code>ActionCommand</code>
   * and the <code>ResourceBundle</code>.
   */
  public static final String COLLAPSE = "collapse" ; //$NON-NLS-1$


  /**
   * The <code>String</code> expandAll, used for the
   * <code>ActionCommand</code> and the <code>ResourceBundle</code>.
   */
  public static final String EXPANDALL = "expandAll" ; //$NON-NLS-1$


  /**
   * The <code>String</code> expand, used for the <code>ActionCommand</code>
   * and the <code>ResourceBundle</code>.
   */
  public static final String EXPAND = "expand" ; //$NON-NLS-1$


  /**
   * The <code>ResourceBundle</code> path.
   */
  private static final String RESOURCE = "de/unisiegen/tpml/graphics/outline/outline" ; //$NON-NLS-1$


  /**
   * The <code>String</code> binding, used for the <code>ActionCommand</code>
   * and the <code>ResourceBundle</code>.
   */
  public static final String BINDING = "binding" ; //$NON-NLS-1$


  /**
   * The <code>String</code> selection, used for the
   * <code>ActionCommand</code> and the <code>ResourceBundle</code>.
   */
  public static final String SELECTION = "selection" ; //$NON-NLS-1$


  /**
   * The <code>String</code> free, used for the <code>ActionCommand</code>
   * and the <code>ResourceBundle</code>.
   */
  public static final String FREE = "free" ; //$NON-NLS-1$


  /**
   * The <code>String</code> replace, used for the <code>ActionCommand</code>
   * and the <code>ResourceBundle</code>.
   */
  public static final String REPLACE = "replace" ; //$NON-NLS-1$


  /**
   * The <code>String</code> autoUpdate, used for the
   * <code>ActionCommand</code> and the <code>ResourceBundle</code>.
   */
  public static final String AUTOUPDATE = "autoUpdate" ; //$NON-NLS-1$


  /**
   * The mnemonic <code>String</code>.
   */
  private static final String MNEMONIC = "Mnemonic" ; //$NON-NLS-1$


  /**
   * The tool tip <code>String</code>.
   */
  private static final String TOOLTIP = "ToolTip" ; //$NON-NLS-1$


  /**
   * The <code>GridBagConstraints</code>.
   */
  private GridBagConstraints gridBagConstraints ;


  /**
   * The <code>GridBagLayout</code>.
   */
  private GridBagLayout gridBagLayout ;


  /**
   * The <code>JTree</code>.
   * 
   * @see #getJTreeOutline()
   */
  private JTree jTreeOutline ;


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
   * The <code>JScrollPane</code> of the {@link Outline}.
   * 
   * @see #getJScrollPaneOutline()
   */
  private JScrollPane jScrollPaneOutline ;


  /**
   * The preferences <code>JPanel</code>.
   * 
   * @see #getJPanelPreferences()
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
   * The <code>JCheckBox</code> free.
   * 
   * @see #getJCheckBoxFree()
   */
  private JCheckBox jCheckBoxFree ;


  /**
   * The <code>JCheckBox</code> selection.
   * 
   * @see #getJCheckBoxSelection()
   */
  private JCheckBox jCheckBoxSelection ;


  /**
   * The <code>JCheckBox</code> auto update.
   * 
   * @see #getJCheckBoxFree()
   */
  private JCheckBox jCheckBoxAutoUpdate ;


  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline ;


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
   * The <code>JCheckBoxMenuItem</code> free.
   * 
   * @see #getJCheckBoxFree()
   */
  private JCheckBoxMenuItem jMenuItemFree ;


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
   * This constructor creates the UI of the {@link Outline}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineUI ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
    // Insets
    this.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    // Preferences
    this.resourceBundle = ResourceBundle.getBundle ( RESOURCE ) ;
    // PopupMenu
    createPopupMenu ( ) ;
    // Layout
    this.gridBagLayout = new GridBagLayout ( ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    // Panel Preferences
    this.jPanelPreferences = new JPanel ( ) ;
    this.jPanelPreferences.setLayout ( this.gridBagLayout ) ;
    // CheckBox Selection
    this.jCheckBoxSelection = new JCheckBox ( this.resourceBundle
        .getString ( SELECTION ) ) ;
    this.jCheckBoxSelection.setMnemonic ( this.resourceBundle.getString (
        SELECTION + MNEMONIC ).charAt ( 0 ) ) ;
    this.jCheckBoxSelection.setToolTipText ( this.resourceBundle
        .getString ( SELECTION + TOOLTIP ) ) ;
    this.jCheckBoxSelection.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isSelection ( ) ) ;
    this.jCheckBoxSelection.setFocusable ( false ) ;
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
        .getString ( BINDING ) ) ;
    this.jCheckBoxBinding.setMnemonic ( this.resourceBundle.getString (
        BINDING + MNEMONIC ).charAt ( 0 ) ) ;
    this.jCheckBoxBinding.setToolTipText ( this.resourceBundle
        .getString ( BINDING + TOOLTIP ) ) ;
    this.jCheckBoxBinding.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isBinding ( ) ) ;
    this.jCheckBoxBinding.setFocusable ( false ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxBinding ,
        this.gridBagConstraints ) ;
    // CheckBox Free
    this.jCheckBoxFree = new JCheckBox ( this.resourceBundle.getString ( FREE ) ) ;
    this.jCheckBoxFree.setMnemonic ( this.resourceBundle.getString (
        FREE + MNEMONIC ).charAt ( 0 ) ) ;
    this.jCheckBoxFree.setToolTipText ( this.resourceBundle.getString ( FREE
        + TOOLTIP ) ) ;
    this.jCheckBoxFree.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isFree ( ) ) ;
    this.jCheckBoxFree.setFocusable ( false ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxFree , this.gridBagConstraints ) ;
    // CheckBox Replace
    this.jCheckBoxReplace = new JCheckBox ( this.resourceBundle
        .getString ( REPLACE ) ) ;
    this.jCheckBoxReplace.setMnemonic ( this.resourceBundle.getString (
        REPLACE + MNEMONIC ).charAt ( 0 ) ) ;
    this.jCheckBoxReplace.setToolTipText ( this.resourceBundle
        .getString ( REPLACE + TOOLTIP ) ) ;
    this.jCheckBoxReplace.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isReplace ( ) ) ;
    this.jCheckBoxReplace.setFocusable ( false ) ;
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
        .getString ( AUTOUPDATE ) ) ;
    this.jCheckBoxAutoUpdate.setMnemonic ( this.resourceBundle.getString (
        AUTOUPDATE + MNEMONIC ).charAt ( 0 ) ) ;
    this.jCheckBoxAutoUpdate.setToolTipText ( this.resourceBundle
        .getString ( AUTOUPDATE + TOOLTIP ) ) ;
    this.jCheckBoxAutoUpdate.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isAutoUpdate ( ) ) ;
    this.jCheckBoxAutoUpdate.setFocusable ( false ) ;
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
    // Tree
    this.jTreeOutline = new JTree ( this.treeModel ) ;
    this.jTreeOutline.setDoubleBuffered ( true ) ;
    this.jTreeOutline.setCellRenderer ( new OutlineCellRenderer ( ) ) ;
    this.jTreeOutline.setRowHeight ( 0 ) ;
    // ScrollPane
    this.jScrollPaneOutline = new JScrollPane ( this.jTreeOutline ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 0 , 1 , 0 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelMain.add ( this.jScrollPaneOutline , this.gridBagConstraints ) ;
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
   * Creates the <code>JPopupMenu</code> of the {@link Outline}.
   */
  private final void createPopupMenu ( )
  {
    // PopupMenu
    this.jPopupMenu = new JPopupMenu ( ) ;
    // MenuItem Expand
    this.jMenuItemExpand = new JMenuItem ( this.resourceBundle
        .getString ( EXPAND ) ) ;
    this.jMenuItemExpand.setMnemonic ( this.resourceBundle.getString (
        EXPAND + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemExpand.setToolTipText ( this.resourceBundle
        .getString ( EXPAND + TOOLTIP ) ) ;
    this.jMenuItemExpand.setIcon ( new ImageIcon ( getClass ( ).getResource (
        EMPTYICON ) ) ) ;
    this.jMenuItemExpand.setActionCommand ( EXPAND ) ;
    this.jPopupMenu.add ( this.jMenuItemExpand ) ;
    // MenuItem ExpandAll
    this.jMenuItemExpandAll = new JMenuItem ( this.resourceBundle
        .getString ( EXPANDALL ) ) ;
    this.jMenuItemExpandAll.setMnemonic ( this.resourceBundle.getString (
        EXPANDALL + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemExpandAll.setToolTipText ( this.resourceBundle
        .getString ( EXPANDALL + TOOLTIP ) ) ;
    this.jMenuItemExpandAll.setIcon ( new ImageIcon ( getClass ( ).getResource (
        EMPTYICON ) ) ) ;
    this.jMenuItemExpandAll.setActionCommand ( EXPANDALL ) ;
    this.jPopupMenu.add ( this.jMenuItemExpandAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Collapse
    this.jMenuItemCollapse = new JMenuItem ( this.resourceBundle
        .getString ( COLLAPSE ) ) ;
    this.jMenuItemCollapse.setMnemonic ( this.resourceBundle.getString (
        COLLAPSE + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemCollapse.setToolTipText ( this.resourceBundle
        .getString ( COLLAPSE + TOOLTIP ) ) ;
    this.jMenuItemCollapse.setIcon ( new ImageIcon ( getClass ( ).getResource (
        EMPTYICON ) ) ) ;
    this.jMenuItemCollapse.setActionCommand ( COLLAPSE ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapse ) ;
    // MenuItem CollapseAll
    this.jMenuItemCollapseAll = new JMenuItem ( this.resourceBundle
        .getString ( COLLAPSEALL ) ) ;
    this.jMenuItemCollapseAll.setMnemonic ( this.resourceBundle.getString (
        COLLAPSEALL + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemCollapseAll.setToolTipText ( this.resourceBundle
        .getString ( COLLAPSEALL + TOOLTIP ) ) ;
    this.jMenuItemCollapseAll.setIcon ( new ImageIcon ( getClass ( )
        .getResource ( EMPTYICON ) ) ) ;
    this.jMenuItemCollapseAll.setActionCommand ( COLLAPSEALL ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapseAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Close
    this.jMenuItemClose = new JMenuItem ( this.resourceBundle
        .getString ( CLOSE ) ) ;
    this.jMenuItemClose.setMnemonic ( this.resourceBundle.getString (
        CLOSE + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemClose.setToolTipText ( this.resourceBundle.getString ( CLOSE
        + TOOLTIP ) ) ;
    this.jMenuItemClose.setIcon ( new ImageIcon ( getClass ( ).getResource (
        EMPTYICON ) ) ) ;
    this.jMenuItemClose.setActionCommand ( CLOSE ) ;
    this.jPopupMenu.add ( this.jMenuItemClose ) ;
    // MenuItem CloseAll
    this.jMenuItemCloseAll = new JMenuItem ( this.resourceBundle
        .getString ( CLOSEALL ) ) ;
    this.jMenuItemCloseAll.setMnemonic ( this.resourceBundle.getString (
        CLOSEALL + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemCloseAll.setToolTipText ( this.resourceBundle
        .getString ( CLOSEALL + TOOLTIP ) ) ;
    this.jMenuItemCloseAll.setIcon ( new ImageIcon ( getClass ( ).getResource (
        EMPTYICON ) ) ) ;
    this.jMenuItemCloseAll.setActionCommand ( CLOSEALL ) ;
    this.jPopupMenu.add ( this.jMenuItemCloseAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Copy
    this.jMenuItemCopy = new JMenuItem ( this.resourceBundle.getString ( COPY ) ) ;
    this.jMenuItemCopy.setMnemonic ( this.resourceBundle.getString (
        COPY + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemCopy.setToolTipText ( this.resourceBundle.getString ( COPY
        + TOOLTIP ) ) ;
    this.jMenuItemCopy.setIcon ( new ImageIcon ( getClass ( ).getResource (
        COPYICON ) ) ) ;
    this.jMenuItemCopy.setActionCommand ( COPY ) ;
    this.jMenuItemCopy.setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_C ,
        InputEvent.CTRL_MASK ) ) ;
    this.jPopupMenu.add ( this.jMenuItemCopy ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Preferences
    this.jMenuPreferences = new JMenu ( this.resourceBundle
        .getString ( PREFERENCES ) ) ;
    this.jMenuPreferences.setMnemonic ( this.resourceBundle.getString (
        PREFERENCES + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuPreferences.setIcon ( new ImageIcon ( getClass ( ).getResource (
        EMPTYICON ) ) ) ;
    this.jMenuPreferences.setActionCommand ( PREFERENCES ) ;
    this.jPopupMenu.add ( this.jMenuPreferences ) ;
    // MenuItem Selection
    this.jMenuItemSelection = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( SELECTION ) ) ;
    this.jMenuItemSelection.setMnemonic ( this.resourceBundle.getString (
        SELECTION + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemSelection.setToolTipText ( this.resourceBundle
        .getString ( SELECTION + TOOLTIP ) ) ;
    this.jMenuItemSelection.setActionCommand ( SELECTION ) ;
    this.jMenuItemSelection.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isSelection ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemSelection ) ;
    // MenuItem Binding
    this.jMenuItemBinding = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( BINDING ) ) ;
    this.jMenuItemBinding.setMnemonic ( this.resourceBundle.getString (
        BINDING + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemBinding.setToolTipText ( this.resourceBundle
        .getString ( BINDING + TOOLTIP ) ) ;
    this.jMenuItemBinding.setActionCommand ( BINDING ) ;
    this.jMenuItemBinding.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isBinding ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemBinding ) ;
    // MenuItem Unbound
    this.jMenuItemFree = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( FREE ) ) ;
    this.jMenuItemFree.setMnemonic ( this.resourceBundle.getString (
        FREE + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemFree.setToolTipText ( this.resourceBundle.getString ( FREE
        + TOOLTIP ) ) ;
    this.jMenuItemFree.setActionCommand ( FREE ) ;
    this.jMenuItemFree.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isFree ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemFree ) ;
    // MenuItem Replace
    this.jMenuItemReplace = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( REPLACE ) ) ;
    this.jMenuItemReplace.setMnemonic ( this.resourceBundle.getString (
        REPLACE + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemReplace.setToolTipText ( this.resourceBundle
        .getString ( REPLACE + TOOLTIP ) ) ;
    this.jMenuItemReplace.setActionCommand ( REPLACE ) ;
    this.jMenuItemReplace.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isReplace ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemReplace ) ;
    // MenuItem AutoUpdate
    this.jMenuItemAutoUpdate = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( AUTOUPDATE ) ) ;
    this.jMenuItemAutoUpdate.setMnemonic ( this.resourceBundle.getString (
        AUTOUPDATE + MNEMONIC ).charAt ( 0 ) ) ;
    this.jMenuItemAutoUpdate.setToolTipText ( this.resourceBundle
        .getString ( AUTOUPDATE + TOOLTIP ) ) ;
    this.jMenuItemAutoUpdate.setActionCommand ( AUTOUPDATE ) ;
    this.jMenuItemAutoUpdate.setSelected ( this.defaultOutline
        .getOutlinePreferences ( ).isAutoUpdate ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemAutoUpdate ) ;
  }


  /**
   * Returns the <code>jCheckBoxAutoUpdate</code>.
   * 
   * @return The <code>jCheckBoxAutoUpdate</code>.
   * @see #jCheckBoxAutoUpdate
   */
  public final JCheckBox getJCheckBoxAutoUpdate ( )
  {
    return this.jCheckBoxAutoUpdate ;
  }


  /**
   * Returns the <code>jCheckBoxBinding</code>.
   * 
   * @return The <code>jCheckBoxBinding</code>.
   * @see #jCheckBoxBinding
   */
  public final JCheckBox getJCheckBoxBinding ( )
  {
    return this.jCheckBoxBinding ;
  }


  /**
   * Returns the <code>jCheckBoxFree</code>.
   * 
   * @return The <code>jCheckBoxFree</code>.
   * @see #jCheckBoxFree
   */
  public final JCheckBox getJCheckBoxFree ( )
  {
    return this.jCheckBoxFree ;
  }


  /**
   * Returns the <code>jCheckBoxReplace</code>.
   * 
   * @return The <code>jCheckBoxReplace</code>.
   * @see #jCheckBoxReplace
   */
  public final JCheckBox getJCheckBoxReplace ( )
  {
    return this.jCheckBoxReplace ;
  }


  /**
   * Returns the <code>jCheckBoxSelection</code>.
   * 
   * @return The <code>jCheckBoxSelection</code>.
   * @see #jCheckBoxSelection
   */
  public final JCheckBox getJCheckBoxSelection ( )
  {
    return this.jCheckBoxSelection ;
  }


  /**
   * Returns the <code>jMenuItemAutoUpdate</code>.
   * 
   * @return The <code>jMenuItemAutoUpdate</code>.
   * @see #jMenuItemAutoUpdate
   */
  public final JCheckBoxMenuItem getJMenuItemAutoUpdate ( )
  {
    return this.jMenuItemAutoUpdate ;
  }


  /**
   * Returns the <code>jMenuItemBinding</code>.
   * 
   * @return The <code>jMenuItemBinding</code>.
   * @see #jMenuItemBinding
   */
  public final JCheckBoxMenuItem getJMenuItemBinding ( )
  {
    return this.jMenuItemBinding ;
  }


  /**
   * Returns the <code>jMenuItemClose</code>.
   * 
   * @return The <code>jMenuItemClose</code>.
   * @see #jMenuItemClose
   */
  public final JMenuItem getJMenuItemClose ( )
  {
    return this.jMenuItemClose ;
  }


  /**
   * Returns the <code>jMenuItemCloseAll</code>.
   * 
   * @return The <code>jMenuItemCloseAll</code>.
   * @see #jMenuItemCloseAll
   */
  public final JMenuItem getJMenuItemCloseAll ( )
  {
    return this.jMenuItemCloseAll ;
  }


  /**
   * Returns the <code>jMenuItemCollapse</code>.
   * 
   * @return The <code>jMenuItemCollapse</code>.
   * @see #jMenuItemCollapse
   */
  public final JMenuItem getJMenuItemCollapse ( )
  {
    return this.jMenuItemCollapse ;
  }


  /**
   * Returns the <code>jMenuItemCollapseAll</code>.
   * 
   * @return The <code>jMenuItemCollapseAll</code>.
   * @see #jMenuItemCollapseAll
   */
  public final JMenuItem getJMenuItemCollapseAll ( )
  {
    return this.jMenuItemCollapseAll ;
  }


  /**
   * Returns the <code>jMenuItemCopy</code>.
   * 
   * @return The <code>jMenuItemCopy</code>.
   * @see #jMenuItemCopy
   */
  public final JMenuItem getJMenuItemCopy ( )
  {
    return this.jMenuItemCopy ;
  }


  /**
   * Returns the <code>jMenuItemExpand</code>.
   * 
   * @return The <code>jMenuItemExpand</code>.
   * @see #jMenuItemExpand
   */
  public final JMenuItem getJMenuItemExpand ( )
  {
    return this.jMenuItemExpand ;
  }


  /**
   * Returns the <code>jMenuItemExpandAll</code>.
   * 
   * @return The <code>jMenuItemExpandAll</code>.
   * @see #jMenuItemExpandAll
   */
  public final JMenuItem getJMenuItemExpandAll ( )
  {
    return this.jMenuItemExpandAll ;
  }


  /**
   * Returns the <code>jMenuItemUnbound</code>.
   * 
   * @return The <code>jMenuItemUnbound</code>.
   * @see #jMenuItemFree
   */
  public final JCheckBoxMenuItem getJMenuItemFree ( )
  {
    return this.jMenuItemFree ;
  }


  /**
   * Returns the <code>jMenuItemReplace</code>.
   * 
   * @return The <code>jMenuItemReplace</code>.
   * @see #jMenuItemReplace
   */
  public final JCheckBoxMenuItem getJMenuItemReplace ( )
  {
    return this.jMenuItemReplace ;
  }


  /**
   * Returns the <code>jMenuItemSelection</code>.
   * 
   * @return The <code>jMenuItemSelection</code>.
   * @see #jMenuItemSelection
   */
  public final JCheckBoxMenuItem getJMenuItemSelection ( )
  {
    return this.jMenuItemSelection ;
  }


  /**
   * Returns the <code>jMenuPreferences</code>.
   * 
   * @return The <code>jMenuPreferences</code>.
   * @see #jMenuPreferences
   */
  public JMenu getJMenuPreferences ( )
  {
    return this.jMenuPreferences ;
  }


  /**
   * Returns the <code>jPanelMain</code>.
   * 
   * @return The <code>jPanelMain</code>.
   * @see #jPanelMain
   */
  public final JPanel getJPanelMain ( )
  {
    return this.jPanelMain ;
  }


  /**
   * Returns the <code>jPanelPreferences</code>.
   * 
   * @return The <code>jPanelPreferences</code>.
   * @see #jPanelPreferences
   */
  public JPanel getJPanelPreferences ( )
  {
    return this.jPanelPreferences ;
  }


  /**
   * Returns the <code>jPopupMenu</code>.
   * 
   * @return The <code>jPopupMenu</code>.
   * @see #jPopupMenu
   */
  public final JPopupMenu getJPopupMenu ( )
  {
    return this.jPopupMenu ;
  }


  /**
   * Returns the jScrollPaneOutline.
   * 
   * @return The jScrollPaneOutline.
   * @see #jScrollPaneOutline
   */
  public JScrollPane getJScrollPaneOutline ( )
  {
    return this.jScrollPaneOutline ;
  }


  /**
   * Returns the <code>jTreeAbstractSyntaxTree</code>.
   * 
   * @return The <code>jTreeAbstractSyntaxTree</code>.
   * @see #jTreeOutline
   */
  public final JTree getJTreeOutline ( )
  {
    return this.jTreeOutline ;
  }


  /**
   * Returns the </code>treeModel</code>.
   * 
   * @return The </code>treeModel</code>.
   * @see #treeModel
   */
  public final DefaultTreeModel getTreeModel ( )
  {
    return this.treeModel ;
  }


  /**
   * Sets the new root node.
   * 
   * @param pRootNode The new root node.
   */
  public final void setRootNode ( DefaultMutableTreeNode pRootNode )
  {
    this.rootNode = pRootNode ;
    this.treeModel.setRoot ( this.rootNode ) ;
  }
}
