package de.unisiegen.tpml.graphics.outline ;


import java.awt.Color ;
import java.awt.Rectangle ;
import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.Timer ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.SwingUtilities ;
import javax.swing.border.LineBorder ;
import javax.swing.text.BadLocationException ;
import javax.swing.text.SimpleAttributeSet ;
import javax.swing.text.StyleConstants ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.graphics.subtyping.SubTypingEnterTypes ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeName ;
import de.unisiegen.tpml.graphics.StyledLanguageDocument ;
import de.unisiegen.tpml.graphics.StyledLanguageEditor ;
import de.unisiegen.tpml.graphics.Theme ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.minimaltyping.MinimalTypingView ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineActionListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineComponentListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineItemListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineKeyListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlinePropertyChangeListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeExpansionListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeModelListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeSelectionListener ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineDisplayTree ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineTimerTask ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView ;
import de.unisiegen.tpml.graphics.typeinference.TypeInferenceView ;
import de.unisiegen.tpml.ui.editor.TextEditorPanel ;


/**
 * This class is the main class of the {@link Outline}. It loads the
 * {@link OutlinePreferences}, creates the {@link OutlineUI} and loads new
 * {@link Expression}s.
 * 
 * @author Christian Fehler
 * @version $Rev: 1061 $
 */
public final class DefaultOutline implements Outline
{
  /**
   * Method name for getExpressionsIndex
   */
  private static final String GET_EXPRESSIONS_INDEX = "getExpressionsIndex" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiers
   */
  private static final String GET_IDENTIFIERS = "getIdentifiers" ; //$NON-NLS-1$


  /**
   * Method name for getTypeNames
   */
  private static final String GET_TYPE_NAMES = "getTypeNames" ; //$NON-NLS-1$


  /**
   * Method name for getTypeNamesBound
   */
  private static final String GET_TYPE_NAMES_BOUND = "getTypeNamesBound" ; //$NON-NLS-1$


  /**
   * Method name for getTypeNamesIndex
   */
  private static final String GET_TYPE_NAMES_INDEX = "getTypeNamesIndex" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiersIndex
   */
  private static final String GET_IDENTIFIERS_INDEX = "getIdentifiersIndex" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiersBound
   */
  private static final String GET_IDENTIFIERS_BOUND = "getIdentifiersBound" ; //$NON-NLS-1$


  /**
   * Method name for getTypes
   */
  private static final String GET_TYPES = "getTypes" ; //$NON-NLS-1$


  /**
   * Method name for getTypesIndex
   */
  private static final String GET_TYPES_INDEX = "getTypesIndex" ; //$NON-NLS-1$


  /**
   * Method name for getSortedChildren
   */
  private static final String GET_SORTED_CHILDREN = "getSortedChildren" ; //$NON-NLS-1$


  /**
   * The {@link OutlineUI}.
   * 
   * @see #getOutlineUI()
   */
  private OutlineUI outlineUI ;


  /**
   * The {@link OutlinePreferences}.
   * 
   * @see #getOutlinePreferences()
   */
  private OutlinePreferences outlinePreferences ;


  /**
   * The loaded input.
   */
  private Object loadedInput ;


  /**
   * The {@link OutlineUnbound}, in which the unbound {@link Identifier}s in
   * the given {@link Expression} are saved.
   */
  private OutlineUnbound outlineUnbound ;


  /**
   * The <code>Timer</code> for the executing.
   */
  private Timer outlineTimer ;


  /**
   * The root node.
   */
  private OutlineNode rootOutlineNode ;


  /**
   * The source code editor.
   */
  private TextEditorPanel textEditorPanel ;


  /**
   * The subTypingEnterTypes
   */
  private SubTypingEnterTypes subTypingEnterTypes ;


  /**
   * The {@link StyledLanguageEditor}.
   */
  private StyledLanguageEditor styledLanguageEditor ;


  /**
   * The {@link OutlineItemListener}.
   */
  private OutlineItemListener outlineItemListener ;


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pBigStepView The {@link BigStepView}.
   */
  public DefaultOutline ( BigStepView pBigStepView )
  {
    this.loadedInput = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    this.textEditorPanel = null ;
    this.subTypingEnterTypes = null ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( false ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( pBigStepView.getJSplitPane ( ) , this ) ) ;
    // PropertyChangeListener
    pBigStepView.addPropertyChangeListener ( new OutlinePropertyChangeListener (
        pBigStepView.getJSplitPane ( ) , this ) ) ;
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
    // TreeModelListener
    pBigStepView.getBigStepProofModel ( ).addTreeModelListener (
        new OutlineTreeModelListener ( this , pBigStepView
            .getBigStepProofModel ( ) ) ) ;
    // MouseListener
    this.outlineUI.getJTreeOutline ( ).addMouseListener (
        new OutlineMouseListener ( this ) ) ;
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this ) ;
    this.outlineUI.getJMenuItemExpand ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapse ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemClose ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCopy ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuPreferences ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemSelection ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemBinding ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemFree ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemReplace ( ).addActionListener (
        outlineActionListener ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    // ItemListener
    this.outlineItemListener = new OutlineItemListener ( this ) ;
    this.outlineUI.getJCheckBoxSelection ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxBinding ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxFree ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxReplace ( ).addItemListener (
        this.outlineItemListener ) ;
    // KeyListener
    this.outlineUI.getJTreeOutline ( ).addKeyListener (
        new OutlineKeyListener ( this ) ) ;
    // TreeExpansionListener
    this.outlineUI.getJTreeOutline ( ).addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) ) ;
    // TreeSelectionListener
    this.outlineUI.getJTreeOutline ( ).getSelectionModel ( )
        .addTreeSelectionListener ( new OutlineTreeSelectionListener ( this ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pMinimalTypingView The {@link MinimalTypingView}.
   */
  public DefaultOutline ( MinimalTypingView pMinimalTypingView )
  {
    this.loadedInput = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    this.textEditorPanel = null ;
    this.subTypingEnterTypes = null ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( false ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( pMinimalTypingView.getJSplitPane ( ) ,
            this ) ) ;
    // PropertyChangeListener
    pMinimalTypingView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pMinimalTypingView.getJSplitPane ( ) , this ) ) ;
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
    // TreeModelListener
    pMinimalTypingView.getMinimalTypingProofModel ( ).addTreeModelListener (
        new OutlineTreeModelListener ( this , pMinimalTypingView
            .getMinimalTypingProofModel ( ) ) ) ;
    // MouseListener
    this.outlineUI.getJTreeOutline ( ).addMouseListener (
        new OutlineMouseListener ( this ) ) ;
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this ) ;
    this.outlineUI.getJMenuItemExpand ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapse ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemClose ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCopy ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuPreferences ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemSelection ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemBinding ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemFree ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemReplace ( ).addActionListener (
        outlineActionListener ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    // ItemListener
    this.outlineItemListener = new OutlineItemListener ( this ) ;
    this.outlineUI.getJCheckBoxSelection ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxBinding ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxFree ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxReplace ( ).addItemListener (
        this.outlineItemListener ) ;
    // KeyListener
    this.outlineUI.getJTreeOutline ( ).addKeyListener (
        new OutlineKeyListener ( this ) ) ;
    // TreeExpansionListener
    this.outlineUI.getJTreeOutline ( ).addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) ) ;
    // TreeSelectionListener
    this.outlineUI.getJTreeOutline ( ).getSelectionModel ( )
        .addTreeSelectionListener ( new OutlineTreeSelectionListener ( this ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pSmallStepView {@link SmallStepView}.
   */
  public DefaultOutline ( SmallStepView pSmallStepView )
  {
    this.loadedInput = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    this.textEditorPanel = null ;
    this.subTypingEnterTypes = null ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setSelected ( false ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( )
        .addComponentListener (
            new OutlineComponentListener ( pSmallStepView.getJSplitPane ( ) ,
                this ) ) ;
    // PropertyChangeListener
    pSmallStepView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pSmallStepView.getJSplitPane ( ) , this ) ) ;
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
    // TreeModelListener
    pSmallStepView.getSmallStepProofModel ( ).addTreeModelListener (
        new OutlineTreeModelListener ( this , pSmallStepView
            .getSmallStepProofModel ( ) ) ) ;
    // MouseListener
    this.outlineUI.getJTreeOutline ( ).addMouseListener (
        new OutlineMouseListener ( this ) ) ;
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this ) ;
    this.outlineUI.getJMenuItemExpand ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapse ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemClose ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCopy ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuPreferences ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemSelection ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemBinding ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemFree ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemReplace ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).addActionListener (
        outlineActionListener ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    // ItemListener
    this.outlineItemListener = new OutlineItemListener ( this ) ;
    this.outlineUI.getJCheckBoxSelection ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxBinding ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxFree ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxReplace ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).addItemListener (
        this.outlineItemListener ) ;
    // KeyListener
    this.outlineUI.getJTreeOutline ( ).addKeyListener (
        new OutlineKeyListener ( this ) ) ;
    // TreeExpansionListener
    this.outlineUI.getJTreeOutline ( ).addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) ) ;
    // TreeSelectionListener
    this.outlineUI.getJTreeOutline ( ).getSelectionModel ( )
        .addTreeSelectionListener ( new OutlineTreeSelectionListener ( this ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pSubTypingEnterTypes The {@link SubTypingEnterTypes}.
   * @param pStyledLanguageEditor The {@link StyledLanguageEditor}.
   */
  public DefaultOutline ( SubTypingEnterTypes pSubTypingEnterTypes ,
      StyledLanguageEditor pStyledLanguageEditor )
  {
    this.loadedInput = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    this.textEditorPanel = null ;
    this.subTypingEnterTypes = pSubTypingEnterTypes ;
    this.styledLanguageEditor = pStyledLanguageEditor ;
    this.outlineUI.getJCheckBoxBinding ( ).setVisible ( false ) ;
    this.outlineUI.getJCheckBoxFree ( ).setVisible ( false ) ;
    this.outlineUI.getJMenuItemBinding ( ).setVisible ( false ) ;
    this.outlineUI.getJMenuItemFree ( ).setVisible ( false ) ;
    // PropertyChangeListener
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
    // MouseListener
    pStyledLanguageEditor.addMouseListener ( new OutlineMouseListener (
        pStyledLanguageEditor , this ) ) ;
    this.outlineUI.getJTreeOutline ( ).addMouseListener (
        new OutlineMouseListener ( this ) ) ;
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this ) ;
    this.outlineUI.getJMenuItemExpand ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapse ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemClose ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCopy ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuPreferences ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemSelection ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemReplace ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).addActionListener (
        outlineActionListener ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    // ItemListener
    this.outlineItemListener = new OutlineItemListener ( this ) ;
    this.outlineUI.getJCheckBoxSelection ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxReplace ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).addItemListener (
        this.outlineItemListener ) ;
    // KeyListener
    this.outlineUI.getJTreeOutline ( ).addKeyListener (
        new OutlineKeyListener ( this ) ) ;
    // TreeExpansionListener
    this.outlineUI.getJTreeOutline ( ).addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) ) ;
    // TreeSelectionListener
    this.outlineUI.getJTreeOutline ( ).getSelectionModel ( )
        .addTreeSelectionListener ( new OutlineTreeSelectionListener ( this ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pTextEditorPanel The {@link TextEditorPanel}.
   */
  public DefaultOutline ( TextEditorPanel pTextEditorPanel )
  {
    this.loadedInput = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    this.textEditorPanel = pTextEditorPanel ;
    this.subTypingEnterTypes = null ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( pTextEditorPanel.getJSplitPane ( ) ,
            this ) ) ;
    // MouseListener
    this.textEditorPanel.getEditor ( ).addMouseListener (
        new OutlineMouseListener ( pTextEditorPanel ) ) ;
    this.outlineUI.getJTreeOutline ( ).addMouseListener (
        new OutlineMouseListener ( this ) ) ;
    // PropertyChangeListener
    this.textEditorPanel
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            this.textEditorPanel.getJSplitPane ( ) , this ) ) ;
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this ) ;
    this.outlineUI.getJMenuItemExpand ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapse ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemClose ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCopy ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuPreferences ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemSelection ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemBinding ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemFree ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemReplace ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).addActionListener (
        outlineActionListener ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    // ItemListener
    this.outlineItemListener = new OutlineItemListener ( this ) ;
    this.outlineUI.getJCheckBoxSelection ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxBinding ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxFree ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxReplace ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).addItemListener (
        this.outlineItemListener ) ;
    // KeyListener
    this.outlineUI.getJTreeOutline ( ).addKeyListener (
        new OutlineKeyListener ( this ) ) ;
    // TreeExpansionListener
    this.outlineUI.getJTreeOutline ( ).addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) ) ;
    // TreeSelectionListener
    this.outlineUI.getJTreeOutline ( ).getSelectionModel ( )
        .addTreeSelectionListener ( new OutlineTreeSelectionListener ( this ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pTypeCheckerView The {@link TypeCheckerView}.
   */
  public DefaultOutline ( TypeCheckerView pTypeCheckerView )
  {
    this.loadedInput = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    this.textEditorPanel = null ;
    this.subTypingEnterTypes = null ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( false ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( pTypeCheckerView.getJSplitPane ( ) ,
            this ) ) ;
    // PropertyChangeListener
    pTypeCheckerView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pTypeCheckerView.getJSplitPane ( ) , this ) ) ;
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
    // TreeModelListener
    pTypeCheckerView.getTypeCheckerProofModel ( ).addTreeModelListener (
        new OutlineTreeModelListener ( this , pTypeCheckerView
            .getTypeCheckerProofModel ( ) ) ) ;
    // MouseListener
    this.outlineUI.getJTreeOutline ( ).addMouseListener (
        new OutlineMouseListener ( this ) ) ;
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this ) ;
    this.outlineUI.getJMenuItemExpand ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapse ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemClose ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCopy ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuPreferences ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemSelection ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemBinding ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemFree ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemReplace ( ).addActionListener (
        outlineActionListener ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    // ItemListener
    this.outlineItemListener = new OutlineItemListener ( this ) ;
    this.outlineUI.getJCheckBoxSelection ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxBinding ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxFree ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxReplace ( ).addItemListener (
        this.outlineItemListener ) ;
    // KeyListener
    this.outlineUI.getJTreeOutline ( ).addKeyListener (
        new OutlineKeyListener ( this ) ) ;
    // TreeExpansionListener
    this.outlineUI.getJTreeOutline ( ).addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) ) ;
    // TreeSelectionListener
    this.outlineUI.getJTreeOutline ( ).getSelectionModel ( )
        .addTreeSelectionListener ( new OutlineTreeSelectionListener ( this ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pTypeInferenceView The {@link TypeInferenceView}.
   */
  public DefaultOutline ( TypeInferenceView pTypeInferenceView )
  {
    this.loadedInput = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    this.textEditorPanel = null ;
    this.subTypingEnterTypes = null ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemHighlightSourceCode ( ).setSelected ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( false ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( pTypeInferenceView.getJSplitPane ( ) ,
            this ) ) ;
    // PropertyChangeListener
    pTypeInferenceView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pTypeInferenceView.getJSplitPane ( ) , this ) ) ;
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
    // TreeModelListener
    pTypeInferenceView.getTypeInferenceProofModel ( ).addTreeModelListener (
        new OutlineTreeModelListener ( this , pTypeInferenceView
            .getTypeInferenceProofModel ( ) ) ) ;
    this.outlineUI.getJTreeOutline ( ).addMouseListener (
        new OutlineMouseListener ( this ) ) ;
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this ) ;
    this.outlineUI.getJMenuItemExpand ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapse ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemClose ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCopy ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuPreferences ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemSelection ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemBinding ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemFree ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemReplace ( ).addActionListener (
        outlineActionListener ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    // ItemListener
    this.outlineItemListener = new OutlineItemListener ( this ) ;
    this.outlineUI.getJCheckBoxSelection ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxBinding ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxFree ( ).addItemListener (
        this.outlineItemListener ) ;
    this.outlineUI.getJCheckBoxReplace ( ).addItemListener (
        this.outlineItemListener ) ;
    // KeyListener
    this.outlineUI.getJTreeOutline ( ).addKeyListener (
        new OutlineKeyListener ( this ) ) ;
    // TreeExpansionListener
    this.outlineUI.getJTreeOutline ( ).addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) ) ;
    // TreeSelectionListener
    this.outlineUI.getJTreeOutline ( ).getSelectionModel ( )
        .addTreeSelectionListener ( new OutlineTreeSelectionListener ( this ) ) ;
  }


  /**
   * Creates the children with the given {@link Expression} and adds them to the
   * given node.
   * 
   * @param pExpression The {@link Expression}, with which the children should
   *          be created.
   * @param pOutlineNode The node where the children should be added.
   */
  @ SuppressWarnings ( "unchecked" )
  private final void createExpression ( Expression pExpression ,
      OutlineNode pOutlineNode )
  {
    // Child Expression
    int [ ] expressionsIndex = null ;
    // Identifier
    Identifier [ ] identifiers = null ;
    int [ ] identifiersIndex = null ;
    // Bound Identifier
    ArrayList < ArrayList < Identifier >> identifiersBound = null ;
    // Type
    MonoType [ ] types = null ;
    int [ ] typesIndex = null ;
    // Sorted Children
    PrettyPrintable [ ] sortedChildren = null ;
    // Invoke methods
    for ( Class < ? > currentInterface : pExpression.getClass ( )
        .getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultExpressions.class ) )
      {
        expressionsIndex = getIndex ( pExpression , GET_EXPRESSIONS_INDEX ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultIdentifiers.class ) )
      {
        identifiersIndex = getIndex ( pExpression , GET_IDENTIFIERS_INDEX ) ;
        identifiers = getIdentifiers ( pExpression ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.BoundIdentifiers.class ) )
      {
        identifiersIndex = getIndex ( pExpression , GET_IDENTIFIERS_INDEX ) ;
        identifiers = getIdentifiers ( pExpression ) ;
        identifiersBound = getIdentifiersBound ( pExpression ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypes.class ) )
      {
        typesIndex = getIndex ( pExpression , GET_TYPES_INDEX ) ;
        types = getTypes ( pExpression ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.SortedChildren.class ) )
      {
        sortedChildren = getSortedChildren ( pExpression ) ;
      }
    }
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    OutlineNode outlineNodeE ;
    OutlineBinding outlineBinding ;
    // No sorted children
    if ( sortedChildren == null )
    {
      // Identifier
      if ( ( identifiers != null ) && ( identifiersIndex != null ) )
      {
        for ( int i = 0 ; i < identifiers.length ; i ++ )
        {
          if ( identifiersBound == null )
          {
            outlineBinding = null ;
          }
          else
          {
            outlineBinding = new OutlineBinding ( identifiersBound.get ( i ) ) ;
          }
          outlineNodeId = new OutlineNode ( identifiers [ i ] ,
              identifiersIndex [ i ] , outlineBinding ) ;
          // Identifier - Type
          if ( ( types != null ) && ( typesIndex != null ) )
          {
            for ( int j = 0 ; j < types.length ; j ++ )
            {
              if ( ( types [ j ] != null ) && ( typesIndex [ j ] != - 1 )
                  && ( typesIndex [ j ] == identifiersIndex [ i ] ) )
              {
                outlineNodeType = new OutlineNode ( types [ j ] ,
                    this.outlineUnbound , typesIndex [ j ] ) ;
                createType ( types [ j ] , outlineNodeType ) ;
                outlineNodeId.add ( outlineNodeType ) ;
              }
            }
          }
          pOutlineNode.add ( outlineNodeId ) ;
        }
      }
      // Type
      if ( ( types != null ) && ( typesIndex != null ) )
      {
        for ( int i = 0 ; i < types.length ; i ++ )
        {
          if ( ( types [ i ] != null ) && ( typesIndex [ i ] == - 1 ) )
          {
            outlineNodeType = new OutlineNode ( types [ i ] ,
                this.outlineUnbound , typesIndex [ i ] ) ;
            createType ( types [ i ] , outlineNodeType ) ;
            pOutlineNode.add ( outlineNodeType ) ;
          }
        }
      }
      // Expression
      if ( expressionsIndex != null )
      {
        ArrayList < Expression > children = pExpression.children ( ) ;
        for ( int i = 0 ; i < children.size ( ) ; i ++ )
        {
          Expression child = children.get ( i ) ;
          outlineNodeE = new OutlineNode ( child , this.outlineUnbound ,
              expressionsIndex [ i ] ) ;
          createExpression ( child , outlineNodeE ) ;
          pOutlineNode.add ( outlineNodeE ) ;
        }
      }
    }
    // Sorted children
    else
    {
      ArrayList < PrettyPrintable > notFound = new ArrayList < PrettyPrintable > (
          sortedChildren.length ) ;
      for ( int i = 0 ; i < sortedChildren.length ; i ++ )
      {
        PrettyPrintable current = sortedChildren [ i ] ;
        boolean found = false ;
        // Identifier
        if ( ( identifiers != null ) && ( identifiersIndex != null ) )
        {
          for ( int j = 0 ; j < identifiers.length ; j ++ )
          {
            if ( current == identifiers [ j ] )
            {
              if ( identifiersBound == null )
              {
                outlineBinding = null ;
              }
              else
              {
                outlineBinding = new OutlineBinding ( identifiersBound.get ( i ) ) ;
              }
              outlineNodeId = new OutlineNode ( identifiers [ j ] ,
                  identifiersIndex [ j ] , outlineBinding ) ;
              pOutlineNode.add ( outlineNodeId ) ;
              found = true ;
              break ;
            }
          }
        }
        // Type
        if ( ( ! found ) && ( types != null ) && ( typesIndex != null ) )
        {
          for ( int j = 0 ; j < types.length ; j ++ )
          {
            if ( current == types [ j ] )
            {
              outlineNodeType = new OutlineNode ( types [ j ] ,
                  this.outlineUnbound , typesIndex [ j ] ) ;
              createType ( types [ j ] , outlineNodeType ) ;
              pOutlineNode.add ( outlineNodeType ) ;
              found = true ;
              break ;
            }
          }
        }
        // Expression
        if ( ( ! found ) && ( expressionsIndex != null ) )
        {
          ArrayList < Expression > children = pExpression.children ( ) ;
          for ( int j = 0 ; j < children.size ( ) ; j ++ )
          {
            if ( current == children.get ( j ) )
            {
              Expression child = children.get ( j ) ;
              outlineNodeE = new OutlineNode ( child , this.outlineUnbound ,
                  expressionsIndex [ j ] ) ;
              createExpression ( child , outlineNodeE ) ;
              pOutlineNode.add ( outlineNodeE ) ;
              found = true ;
              break ;
            }
          }
        }
        if ( ! found )
        {
          notFound.add ( current ) ;
        }
      }
      // Not found PrettyPrintables
      if ( notFound.size ( ) > 0 )
      {
        for ( PrettyPrintable current : notFound )
        {
          if ( current instanceof Identifier )
          {
            outlineNodeId = new OutlineNode ( ( Identifier ) current , - 1 ,
                null ) ;
            pOutlineNode.add ( outlineNodeId ) ;
          }
          else if ( current instanceof Type )
          {
            outlineNodeType = new OutlineNode ( ( Type ) current ,
                this.outlineUnbound , - 1 ) ;
            createType ( ( Type ) current , outlineNodeType ) ;
            pOutlineNode.add ( outlineNodeType ) ;
          }
          else if ( current instanceof Expression )
          {
            outlineNodeE = new OutlineNode ( ( Expression ) current ,
                this.outlineUnbound , - 1 ) ;
            createExpression ( ( Expression ) current , outlineNodeE ) ;
            pOutlineNode.add ( outlineNodeE ) ;
          }
        }
      }
    }
  }


  /**
   * Creates the children with the given {@link Type} and adds them to the given
   * node.
   * 
   * @param pType The {@link Type}, with which the children should be created.
   * @param pOutlineNode The node where the children should be added.
   */
  private final void createType ( Type pType , OutlineNode pOutlineNode )
  {
    // Type
    MonoType [ ] types = null ;
    int [ ] typesIndex = null ;
    // Identifier
    Identifier [ ] identifiers = null ;
    int [ ] identifiersIndex = null ;
    // Sorted children
    PrettyPrintable [ ] sortedChildren = null ;
    // TypeName
    TypeName [ ] typeNames = null ;
    int [ ] typeNamesIndex = null ;
    // Bound TypeName
    ArrayList < ArrayList < TypeName >> typeNamesBound = null ;
    for ( Class < ? > currentInterface : pType.getClass ( ).getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypes.class ) )
      {
        typesIndex = getIndex ( pType , GET_TYPES_INDEX ) ;
        types = getTypes ( pType ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultIdentifiers.class ) )
      {
        identifiersIndex = getIndex ( pType , GET_IDENTIFIERS_INDEX ) ;
        identifiers = getIdentifiers ( pType ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.SortedChildren.class ) )
      {
        sortedChildren = getSortedChildren ( pType ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypeNames.class ) )
      {
        typeNamesIndex = getIndex ( pType , GET_TYPE_NAMES_INDEX ) ;
        typeNames = getTypeNames ( pType ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.BoundTypeNames.class ) )
      {
        typeNamesIndex = getIndex ( pType , GET_TYPE_NAMES_INDEX ) ;
        typeNames = getTypeNames ( pType ) ;
        typeNamesBound = getTypeNamesBound ( pType ) ;
      }
    }
    OutlineNode outlineNodeType ;
    OutlineNode outlineNodeTypeName ;
    OutlineNode outlineNodeId ;
    OutlineBinding < TypeName > outlineBinding ;
    // No sorted children
    if ( sortedChildren == null )
    {
      // Identifier
      if ( ( identifiers != null ) && ( identifiersIndex != null ) )
      {
        for ( int i = 0 ; i < identifiers.length ; i ++ )
        {
          outlineNodeId = new OutlineNode ( identifiers [ i ] ,
              identifiersIndex [ i ] , null ) ;
          pOutlineNode.add ( outlineNodeId ) ;
        }
      }
      // TypeName
      if ( ( typeNames != null ) && ( typeNamesIndex != null ) )
      {
        for ( int i = 0 ; i < typeNames.length ; i ++ )
        {
          if ( typeNamesBound == null )
          {
            outlineBinding = null ;
          }
          else
          {
            outlineBinding = new OutlineBinding < TypeName > ( typeNamesBound
                .get ( i ) ) ;
          }
          outlineNodeTypeName = new OutlineNode ( typeNames [ i ] ,
              typeNamesIndex [ i ] , outlineBinding ) ;
          pOutlineNode.add ( outlineNodeTypeName ) ;
        }
      }
      // Type
      if ( ( types != null ) && ( typesIndex != null ) )
      {
        for ( int i = 0 ; i < types.length ; i ++ )
        {
          outlineNodeType = new OutlineNode ( types [ i ] ,
              this.outlineUnbound , typesIndex [ i ] ) ;
          createType ( types [ i ] , outlineNodeType ) ;
          pOutlineNode.add ( outlineNodeType ) ;
        }
      }
    }
    // Sorted children
    else
    {
      ArrayList < PrettyPrintable > notFound = new ArrayList < PrettyPrintable > (
          sortedChildren.length ) ;
      for ( int i = 0 ; i < sortedChildren.length ; i ++ )
      {
        PrettyPrintable current = sortedChildren [ i ] ;
        boolean found = false ;
        // Identifier
        if ( ( identifiers != null ) && ( identifiersIndex != null ) )
        {
          for ( int j = 0 ; j < identifiers.length ; j ++ )
          {
            if ( current == identifiers [ j ] )
            {
              outlineNodeId = new OutlineNode ( identifiers [ j ] ,
                  identifiersIndex [ j ] , null ) ;
              pOutlineNode.add ( outlineNodeId ) ;
              found = true ;
              break ;
            }
          }
        }
        // TypeName
        if ( ( ! found ) && ( typeNames != null ) && ( typeNamesIndex != null ) )
        {
          for ( int j = 0 ; j < typeNames.length ; j ++ )
          {
            if ( current == typeNames [ j ] )
            {
              if ( typeNamesBound == null )
              {
                outlineBinding = null ;
              }
              else
              {
                outlineBinding = new OutlineBinding < TypeName > (
                    typeNamesBound.get ( j ) ) ;
              }
              outlineNodeTypeName = new OutlineNode ( typeNames [ j ] ,
                  typeNamesIndex [ j ] , outlineBinding ) ;
              pOutlineNode.add ( outlineNodeTypeName ) ;
            }
          }
        }
        // Type
        if ( ( ! found ) && ( types != null ) && ( typesIndex != null ) )
        {
          for ( int j = 0 ; j < types.length ; j ++ )
          {
            if ( current == types [ j ] )
            {
              outlineNodeType = new OutlineNode ( types [ j ] ,
                  this.outlineUnbound , typesIndex [ j ] ) ;
              createType ( types [ j ] , outlineNodeType ) ;
              pOutlineNode.add ( outlineNodeType ) ;
              found = true ;
              break ;
            }
          }
        }
        if ( ! found )
        {
          notFound.add ( current ) ;
        }
      }
      // Not found PrettyPrintables
      if ( notFound.size ( ) > 0 )
      {
        for ( PrettyPrintable current : notFound )
        {
          if ( current instanceof Identifier )
          {
            outlineNodeId = new OutlineNode ( ( Identifier ) current , - 1 ,
                null ) ;
            pOutlineNode.add ( outlineNodeId ) ;
          }
          else if ( current instanceof TypeName )
          {
            outlineNodeId = new OutlineNode ( ( TypeName ) current , - 1 , null ) ;
            pOutlineNode.add ( outlineNodeId ) ;
          }
          else if ( current instanceof Type )
          {
            outlineNodeType = new OutlineNode ( ( Type ) current ,
                this.outlineUnbound , - 1 ) ;
            createType ( ( Type ) current , outlineNodeType ) ;
            pOutlineNode.add ( outlineNodeType ) ;
          }
        }
      }
    }
  }


  /**
   * Execute the rebuild of a new tree in the {@link Outline}.
   */
  public final void execute ( )
  {
    if ( this.loadedInput == null )
    {
      return ;
    }
    if ( this.loadedInput instanceof Expression )
    {
      Expression expression = ( Expression ) this.loadedInput ;
      this.outlineUnbound = new OutlineUnbound ( expression ) ;
      this.rootOutlineNode = new OutlineNode ( expression ,
          this.outlineUnbound , OutlineNode.NO_CHILD_INDEX ) ;
      createExpression ( expression , this.rootOutlineNode ) ;
    }
    else if ( this.loadedInput instanceof Type )
    {
      Type type = ( Type ) this.loadedInput ;
      this.outlineUnbound = new OutlineUnbound ( type ) ;
      this.rootOutlineNode = new OutlineNode ( type , this.outlineUnbound ,
          OutlineNode.NO_CHILD_INDEX ) ;
      createType ( type , this.rootOutlineNode ) ;
    }
    else
    {
      throw new IllegalArgumentException (
          "Outline: The input is not an Expression or Type!" ) ; //$NON-NLS-1$
    }
    repaint ( this.rootOutlineNode ) ;
    setError ( false ) ;
    SwingUtilities.invokeLater ( new OutlineDisplayTree ( this ) ) ;
  }


  /**
   * Cancels the execute <code>Timer</code>.
   */
  private final void executeTimerCancel ( )
  {
    if ( this.outlineTimer != null )
    {
      this.outlineTimer.cancel ( ) ;
      this.outlineTimer = null ;
    }
  }


  /**
   * Starts the execute <code>Timer</code>, which will execute the rebuild of
   * a new tree in the {@link Outline} after the given delay, if it is not
   * canceled during this time.
   * 
   * @param pDelay Delay in milliseconds before task is to be executed.
   */
  private final void executeTimerStart ( int pDelay )
  {
    if ( pDelay < 0 )
    {
      throw new RuntimeException ( "Delay is smaller than 0" ) ; //$NON-NLS-1$
    }
    this.outlineTimer = new Timer ( ) ;
    this.outlineTimer.schedule ( new OutlineTimerTask ( this ) , pDelay ) ;
  }


  /**
   * Returns the array of {@link Identifier}s from the given {@link Expression}
   * or {@link Type}.
   * 
   * @param pInvokedFrom The {@link Expression} or {@link Type}.
   * @return The array of {@link Identifier}s.
   */
  private final Identifier [ ] getIdentifiers ( Object pInvokedFrom )
  {
    try
    {
      return ( Identifier [ ] ) pInvokedFrom.getClass ( ).getMethod (
          GET_IDENTIFIERS , new Class [ 0 ] ).invoke ( pInvokedFrom ,
          new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * Returns the array of bound {@link Identifier}s from the given
   * {@link Expression} or {@link Type}.
   * 
   * @param pInvokedFrom The {@link Expression} or {@link Type}.
   * @return The array of bound {@link Identifier}s.
   */
  @ SuppressWarnings ( "unchecked" )
  private final ArrayList < ArrayList < Identifier >> getIdentifiersBound (
      Object pInvokedFrom )
  {
    try
    {
      return ( ArrayList < ArrayList < Identifier >> ) pInvokedFrom.getClass ( )
          .getMethod ( GET_IDENTIFIERS_BOUND , new Class [ 0 ] ).invoke (
              pInvokedFrom , new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * Returns the array of indices from the given {@link Expression} or
   * {@link Type}.
   * 
   * @param pInvokedFrom The {@link Expression} or {@link Type}.
   * @param pMethod The name of the method which should be invoked.
   * @return The array of indices.
   */
  private final int [ ] getIndex ( Object pInvokedFrom , String pMethod )
  {
    try
    {
      return ( int [ ] ) pInvokedFrom.getClass ( ).getMethod ( pMethod ,
          new Class [ 0 ] ).invoke ( pInvokedFrom , new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * Returns the outlineItemListener.
   * 
   * @return The outlineItemListener.
   * @see #outlineItemListener
   */
  public OutlineItemListener getOutlineItemListener ( )
  {
    return this.outlineItemListener ;
  }


  /**
   * Returns the {@link OutlinePreferences}.
   * 
   * @return The {@link OutlinePreferences}.
   * @see #outlinePreferences
   */
  public final OutlinePreferences getOutlinePreferences ( )
  {
    return this.outlinePreferences ;
  }


  /**
   * Returns the {@link OutlineUI}.
   * 
   * @return The {@link OutlineUI}.
   * @see #outlineUI
   */
  public final OutlineUI getOutlineUI ( )
  {
    return this.outlineUI ;
  }


  /**
   * Returns the <code>JPanel</code> of the {@link OutlineUI}.
   * 
   * @return The <code>JPanel</code> of the {@link OutlineUI}.
   * @see de.unisiegen.tpml.graphics.outline.Outline#getPanel()
   */
  public final JPanel getPanel ( )
  {
    return this.outlineUI.getJPanelMain ( ) ;
  }


  /**
   * Returns the array of the sorted children from the given {@link Expression}
   * or {@link Type}.
   * 
   * @param pInvokedFrom The {@link Expression} or {@link Type}.
   * @return The array of the sorted children.
   */
  private final PrettyPrintable [ ] getSortedChildren ( Object pInvokedFrom )
  {
    try
    {
      return ( PrettyPrintable [ ] ) pInvokedFrom.getClass ( ).getMethod (
          GET_SORTED_CHILDREN , new Class [ 0 ] ).invoke ( pInvokedFrom ,
          new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * Returns the styledLanguageEditor.
   * 
   * @return The styledLanguageEditor.
   * @see #styledLanguageEditor
   */
  public StyledLanguageEditor getStyledLanguageEditor ( )
  {
    return this.styledLanguageEditor ;
  }


  /**
   * Returns the subTypingEnterTypes.
   * 
   * @return The subTypingEnterTypes.
   * @see #subTypingEnterTypes
   */
  public SubTypingEnterTypes getSubTypingEnterTypes ( )
  {
    return this.subTypingEnterTypes ;
  }


  /**
   * Returns the textEditorPanel.
   * 
   * @return The textEditorPanel.
   * @see #textEditorPanel
   */
  public TextEditorPanel getTextEditorPanel ( )
  {
    return this.textEditorPanel ;
  }


  /**
   * Returns the array of {@link TypeName}s from the given {@link Expression}
   * or {@link Type}.
   * 
   * @param pInvokedFrom The {@link Expression} or {@link Type}.
   * @return The array of {@link TypeName}s.
   */
  private final TypeName [ ] getTypeNames ( Object pInvokedFrom )
  {
    try
    {
      return ( TypeName [ ] ) pInvokedFrom.getClass ( ).getMethod (
          GET_TYPE_NAMES , new Class [ 0 ] ).invoke ( pInvokedFrom ,
          new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * Returns the array of bound {@link TypeName}s from the given {@link Type}.
   * 
   * @param pInvokedFrom The {@link Type}.
   * @return The array of bound {@link TypeName}s.
   */
  @ SuppressWarnings ( "unchecked" )
  private final ArrayList < ArrayList < TypeName >> getTypeNamesBound (
      Object pInvokedFrom )
  {
    try
    {
      return ( ArrayList < ArrayList < TypeName >> ) pInvokedFrom.getClass ( )
          .getMethod ( GET_TYPE_NAMES_BOUND , new Class [ 0 ] ).invoke (
              pInvokedFrom , new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * Returns the array of the types from the given {@link Expression} or
   * {@link Type}.
   * 
   * @param pInvokedFrom The {@link Expression} or {@link Type}.
   * @return The array of the types.
   */
  private final MonoType [ ] getTypes ( Object pInvokedFrom )
  {
    try
    {
      return ( MonoType [ ] ) pInvokedFrom.getClass ( ).getMethod ( GET_TYPES ,
          new Class [ 0 ] ).invoke ( pInvokedFrom , new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * This method loads a new {@link Expression} into the {@link Outline}. It
   * does nothing if the auto update is disabled and the change does not come
   * from a <code>MouseEvent</code>.
   * 
   * @param pExpression The new {@link Expression}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public final synchronized void loadExpression ( Expression pExpression ,
      Outline.Execute pExecute )
  {
    loadInput ( pExpression , pExecute ) ;
  }


  /**
   * This method loads a new input into the {@link Outline}. It does nothing if
   * the auto update is disabled and the change does not come from a
   * <code>MouseEvent</code>.
   * 
   * @param pInput The new input.
   * @param pExecute The {@link Outline.Execute}.
   */
  private final void loadInput ( Object pInput , Outline.Execute pExecute )
  {
    /*
     * If the invoke comes from a mouse click on the editor or the auto change
     * is active, the error is set and nothing is loaded.
     */
    if ( pInput == null )
    {
      executeTimerCancel ( ) ;
      if ( ( this.outlinePreferences.isAutoUpdate ( ) )
          || ( pExecute.equals ( Outline.ExecuteMouseClick.EDITOR ) ) )
      {
        setError ( true ) ;
      }
      return ;
    }
    if ( pExecute instanceof Outline.ExecuteAutoChange )
    {
      Outline.ExecuteAutoChange execute = ( Outline.ExecuteAutoChange ) pExecute ;
      switch ( execute )
      {
        case EDITOR :
        case SMALLSTEP :
        case SUBTYPING :
        {
          if ( ! this.outlinePreferences.isAutoUpdate ( ) )
          {
            return ;
          }
          break ;
        }
        case BIGSTEP :
        case TYPECHECKER :
        case TYPEINFERENCE :
        case MINIMALTYPING :
        {
          return ;
        }
      }
    }
    setError ( false ) ;
    this.loadedInput = pInput ;
    executeTimerCancel ( ) ;
    /*
     * Execute the new load of the Expression or the Type immediately, if the
     * change is an init change or a change because of a mouse click.
     */
    if ( pExecute instanceof Outline.ExecuteInit )
    {
      Outline.ExecuteInit execute = ( Outline.ExecuteInit ) pExecute ;
      switch ( execute )
      {
        case EDITOR :
        case SMALLSTEP :
        case BIGSTEP :
        case TYPECHECKER :
        case TYPEINFERENCE :
        case SUBTYPING :
        case MINIMALTYPING :
        {
          execute ( ) ;
          break ;
        }
      }
    }
    else if ( pExecute instanceof Outline.ExecuteMouseClick )
    {
      Outline.ExecuteMouseClick execute = ( Outline.ExecuteMouseClick ) pExecute ;
      switch ( execute )
      {
        case EDITOR :
        case SMALLSTEP :
        case BIGSTEP :
        case TYPECHECKER :
        case TYPEINFERENCE :
        case SUBTYPING :
        case MINIMALTYPING :
        {
          execute ( ) ;
          break ;
        }
      }
    }
    else if ( pExecute instanceof Outline.ExecuteAutoChange )
    {
      Outline.ExecuteAutoChange execute = ( Outline.ExecuteAutoChange ) pExecute ;
      switch ( execute )
      {
        case EDITOR :
        {
          executeTimerStart ( 500 ) ;
          break ;
        }
        case SMALLSTEP :
        case BIGSTEP :
        case TYPECHECKER :
        case TYPEINFERENCE :
        case SUBTYPING :
        case MINIMALTYPING :
        {
          executeTimerStart ( 250 ) ;
          break ;
        }
      }
    }
  }


  /**
   * This method loads a new {@link Type} into the {@link Outline}. It does
   * nothing if the auto update is disabled and the change does not come from a
   * <code>MouseEvent</code>.
   * 
   * @param pType The new {@link Type}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public final synchronized void loadType ( Type pType ,
      Outline.Execute pExecute )
  {
    loadInput ( pType , pExecute ) ;
  }


  /**
   * Repaints the root node and all of its children with the new color settings.
   */
  public final void propertyChanged ( )
  {
    propertyChanged ( ( OutlineNode ) this.outlineUI.getTreeModel ( )
        .getRoot ( ) ) ;
  }


  /**
   * Repaints the root node and all of its children with the new color settings
   * and resets the caption.
   * 
   * @param pOutlineNode The node, which should be repainted.
   */
  private final void propertyChanged ( OutlineNode pOutlineNode )
  {
    if ( pOutlineNode == null )
    {
      return ;
    }
    pOutlineNode.propertyChanged ( ) ;
    pOutlineNode.updateCaption ( ) ;
    this.outlineUI.getTreeModel ( ).nodeChanged ( pOutlineNode ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      propertyChanged ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Repaints the given node and all of its children and resets the caption.
   * 
   * @param pOutlineNode The node, which should be repainted.
   */
  private final void repaint ( OutlineNode pOutlineNode )
  {
    pOutlineNode.updateCaption ( ) ;
    this.outlineUI.getTreeModel ( ).nodeChanged ( pOutlineNode ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Repaints the given node and all its children.
   */
  private final void repaintNode ( )
  {
    OutlineNode rootNode = ( OutlineNode ) this.outlineUI.getTreeModel ( )
        .getRoot ( ) ;
    repaintNode ( rootNode ) ;
  }


  /**
   * Repaints the given node and all its children.
   * 
   * @param pOutlineNode The node, which should be repainted.
   */
  private final void repaintNode ( OutlineNode pOutlineNode )
  {
    this.outlineUI.getTreeModel ( ).nodeChanged ( pOutlineNode ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      repaintNode ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Resets the root node and all its children.
   */
  public final void resetNode ( )
  {
    OutlineNode outlineNode = ( OutlineNode ) this.outlineUI.getTreeModel ( )
        .getRoot ( ) ;
    if ( outlineNode == null )
    {
      return ;
    }
    resetNode ( outlineNode ) ;
  }


  /**
   * Resets the given node and all its children.
   * 
   * @param pOutlineNode The node, which should be reseted.
   */
  private final void resetNode ( OutlineNode pOutlineNode )
  {
    pOutlineNode.setReplaceInThisNode ( false ) ;
    pOutlineNode.setOutlineBinding ( null ) ;
    pOutlineNode.setBindingIdentifier ( null ) ;
    pOutlineNode.updateCaption ( ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      resetNode ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Updates the UI mit or without an error.
   * 
   * @param pStatus True, if the error should be set.
   */
  public final void setError ( boolean pStatus )
  {
    if ( pStatus )
    {
      this.outlineUI.getJScrollPaneOutline ( ).setBorder (
          new LineBorder ( Color.RED , 3 ) ) ;
    }
    else
    {
      this.outlineUI.getJScrollPaneOutline ( ).setBorder (
          new LineBorder ( Color.WHITE , 3 ) ) ;
    }
  }


  /**
   * Sets the root node in the {@link OutlineUI}.
   */
  public final void setRootNode ( )
  {
    this.outlineUI.setRootNode ( this.rootOutlineNode ) ;
    updateBreaks ( ) ;
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pTreePath The selected <code>TreePath</code>.
   */
  public final void update ( TreePath pTreePath )
  {
    OutlineNode rootNode = ( OutlineNode ) this.outlineUI.getTreeModel ( )
        .getRoot ( ) ;
    if ( rootNode == null )
    {
      return ;
    }
    resetNode ( ) ;
    if ( pTreePath == null )
    {
      repaintNode ( ) ;
      return ;
    }
    ArrayList < OutlineNode > list = new ArrayList < OutlineNode > ( ) ;
    Object [ ] path = pTreePath.getPath ( ) ;
    for ( int i = 0 ; i < pTreePath.getPathCount ( ) ; i ++ )
    {
      list.add ( ( OutlineNode ) path [ i ] ) ;
    }
    OutlineNode selectedNode = list.get ( list.size ( ) - 1 ) ;
    // Expression
    if ( selectedNode.isExpression ( ) )
    {
      updateExpression ( list , pTreePath ) ;
    }
    // Expression
    else if ( selectedNode.isIdentifier ( ) )
    {
      updateIdentifier ( list , pTreePath ) ;
    }
    // Type
    else if ( selectedNode.isType ( ) )
    {
      updateType ( list , pTreePath ) ;
    }
    // Type
    else if ( selectedNode.isTypeName ( ) )
    {
      updateTypeName ( list , pTreePath ) ;
    }
    updateBreaks ( ) ;
  }


  /**
   * Updates the breaks in the {@link OutlineNode}.
   */
  public final void updateBreaks ( )
  {
    if ( this.rootOutlineNode == null )
    {
      return ;
    }
    final int distance = 20 ;
    JScrollPane jScrollPaneOutline = this.outlineUI.getJScrollPaneOutline ( ) ;
    OutlineNode currentNode ;
    TreePath currentTreePath ;
    Rectangle rectangle ;
    Enumeration < ? > enumeration = this.rootOutlineNode
        .breadthFirstEnumeration ( ) ;
    while ( enumeration.hasMoreElements ( ) )
    {
      currentNode = ( OutlineNode ) enumeration.nextElement ( ) ;
      currentTreePath = new TreePath ( currentNode.getPath ( ) ) ;
      rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
          currentTreePath ) ;
      if ( rectangle != null )
      {
        /*
         * Remove a break from the node, if it is to small and a break can be
         * removed. If the node is after the remove to big, a break is added.
         */
        boolean removed = false ;
        while ( ( currentNode.breaksCanRemove ( ) )
            && ( ( rectangle.x + rectangle.width ) < ( jScrollPaneOutline
                .getSize ( ).width - distance ) ) )
        {
          currentNode.breakCountRemove ( ) ;
          this.outlineUI.getTreeModel ( ).nodeChanged ( currentNode ) ;
          rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
              currentTreePath ) ;
          /*
           * If the node is after the remove to big, a break is added.
           */
          if ( ( rectangle.x + rectangle.width ) > ( jScrollPaneOutline
              .getSize ( ).width - distance ) )
          {
            currentNode.breakCountAdd ( ) ;
            this.outlineUI.getTreeModel ( ).nodeChanged ( currentNode ) ;
            rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
                currentTreePath ) ;
            break ;
          }
          removed = true ;
        }
        /*
         * Add a break to the node, if it is to big and more breaks can be
         * added.
         */
        while ( ( ! removed )
            && ( currentNode.breaksCanAdd ( ) )
            && ( ( rectangle.x + rectangle.width ) > ( jScrollPaneOutline
                .getSize ( ).width - distance ) ) )
        {
          currentNode.breakCountAdd ( ) ;
          this.outlineUI.getTreeModel ( ).nodeChanged ( currentNode ) ;
          rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
              currentTreePath ) ;
        }
      }
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateExpression ( ArrayList < OutlineNode > pList ,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    for ( int i = 0 ; i < pList.size ( ) ; i ++ )
    {
      if ( ( selectedNode.getPrettyPrintable ( ) instanceof Identifier )
          && ( i < pList.size ( ) - 1 )
          && ( ( ( Identifier ) selectedNode.getPrettyPrintable ( ) )
              .getBoundToExpression ( ) != null ) )
      {
        try
        {
          Identifier identifier = ( Identifier ) selectedNode
              .getPrettyPrintable ( ) ;
          /*
           * Highlight the bound Identifiers in the other childs of a parent
           * row.
           */
          if ( ( pList.get ( i ).getPrettyPrintable ( ) instanceof Attribute )
              || ( pList.get ( i ).getPrettyPrintable ( ) instanceof Method )
              || ( pList.get ( i ).getPrettyPrintable ( ) instanceof CurriedMethod ) )
          {
            OutlineNode nodeRowChild = ( OutlineNode ) pTreePath.getPath ( ) [ i ] ;
            OutlineNode nodeRow = ( OutlineNode ) pTreePath.getPath ( ) [ i - 1 ] ;
            for ( int j = nodeRow.getIndex ( nodeRowChild ) ; j >= 0 ; j -- )
            {
              OutlineNode currentOutlineNode = ( OutlineNode ) nodeRow
                  .getChildAt ( j ) ;
              if ( currentOutlineNode.getPrettyPrintable ( ) == identifier
                  .getBoundToExpression ( ) )
              {
                /*
                 * Highlight the first identifier
                 */
                currentOutlineNode.setBindingIdentifier ( identifier
                    .getBoundToIdentifier ( ) ) ;
                currentOutlineNode.updateCaption ( ) ;
                /*
                 * Highlight the Identifier in the first child
                 */
                for ( int k = 0 ; k < currentOutlineNode.getChildCount ( ) ; k ++ )
                {
                  OutlineNode nodeId = ( OutlineNode ) currentOutlineNode
                      .getChildAt ( k ) ;
                  if ( nodeId.getPrettyPrintable ( ) == identifier
                      .getBoundToIdentifier ( ) )
                  {
                    nodeId.setBindingIdentifier ( identifier
                        .getBoundToIdentifier ( ) ) ;
                    nodeId.updateCaption ( ) ;
                    break ;
                  }
                }
              }
            }
          }
          else
          {
            /*
             * Highlight the Identifier in the child node with the bound
             * Identifier index.
             */
            if ( pList.get ( i ).getPrettyPrintable ( ) == identifier
                .getBoundToExpression ( ) )
            {
              for ( int j = 0 ; j < pList.get ( i ).getChildCount ( ) ; j ++ )
              {
                OutlineNode nodeId = ( OutlineNode ) pList.get ( i )
                    .getChildAt ( j ) ;
                if ( nodeId.getPrettyPrintable ( ) == identifier
                    .getBoundToIdentifier ( ) )
                {
                  nodeId.setBindingIdentifier ( identifier
                      .getBoundToIdentifier ( ) ) ;
                  nodeId.updateCaption ( ) ;
                  break ;
                }
              }
            }
            /*
             * Highlight the Identifier in the node.
             */
            pList.get ( i ).setBindingIdentifier (
                identifier.getBoundToIdentifier ( ) ) ;
          }
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
      }
      /*
       * It should be replaced in higher nodes, but not the selected node
       */
      if ( i < pList.size ( ) - 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( true ) ;
      }
      /*
       * If only the root is selected, there should not be replaced
       */
      if ( pList.size ( ) == 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( false ) ;
      }
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getPrettyPrintable ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              selectedNode.getPrettyPrintable ( ) ) ;
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
          prettyAnnotation.getEndOffset ( ) ) ;
      /*
       * Node has changed and can be repainted
       */
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
  }


  /**
   * Updates the highlighting of the source code.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   */
  public final void updateHighlighSourceCode ( boolean pSelected )
  {
    StyledLanguageDocument document ;
    if ( this.textEditorPanel != null )
    {
      document = this.textEditorPanel.getDocument ( ) ;
    }
    else if ( this.styledLanguageEditor != null )
    {
      document = ( StyledLanguageDocument ) this.styledLanguageEditor
          .getDocument ( ) ;
    }
    else
    {
      return ;
    }
    try
    {
      document.processChanged ( ) ;
    }
    catch ( BadLocationException e )
    {
      // Do nothing
    }
    if ( pSelected )
    {
      TreePath treePath = this.outlineUI.getJTreeOutline ( )
          .getSelectionPath ( ) ;
      if ( treePath == null )
      {
        return ;
      }
      OutlineNode outlineNode = ( OutlineNode ) treePath
          .getLastPathComponent ( ) ;
      if ( outlineNode.getPrettyPrintable ( ) instanceof Expression )
      {
        Expression expression = ( Expression ) outlineNode
            .getPrettyPrintable ( ) ;
        SimpleAttributeSet freeSet = new SimpleAttributeSet ( ) ;
        StyleConstants.setBackground ( freeSet , Color.YELLOW ) ;
        freeSet.addAttribute ( "selected" , "selected" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        document.setCharacterAttributes ( expression.getParserStartOffset ( ) ,
            expression.getParserEndOffset ( )
                - expression.getParserStartOffset ( ) , freeSet , false ) ;
      }
      else if ( outlineNode.getPrettyPrintable ( ) instanceof Type )
      {
        Type type = ( Type ) outlineNode.getPrettyPrintable ( ) ;
        SimpleAttributeSet freeSet = new SimpleAttributeSet ( ) ;
        StyleConstants.setBackground ( freeSet , Color.YELLOW ) ;
        freeSet.addAttribute ( "selected" , "selected" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        document.setCharacterAttributes ( type.getParserStartOffset ( ) , type
            .getParserEndOffset ( )
            - type.getParserStartOffset ( ) , freeSet , false ) ;
      }
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateIdentifier ( ArrayList < OutlineNode > pList ,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    OutlineNode nodeAttribute = pList.get ( pList.size ( ) - 2 ) ;
    /*
     * Highlight the bound Identifiers of an Attribute in the other childs of
     * the parent row.
     */
    if ( nodeAttribute.getPrettyPrintable ( ) instanceof Attribute )
    {
      OutlineNode nodeRow = pList.get ( pList.size ( ) - 3 ) ;
      for ( int i = nodeRow.getIndex ( nodeAttribute ) + 1 ; i < nodeRow
          .getChildCount ( ) ; i ++ )
      {
        OutlineNode currentRowChild = ( OutlineNode ) nodeRow.getChildAt ( i ) ;
        currentRowChild.setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
        currentRowChild.updateCaption ( ) ;
      }
    }
    for ( int i = 0 ; i < pList.size ( ) ; i ++ )
    {
      /*
       * Sets the new binding in higher nodes
       */
      pList.get ( i ).setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
      /*
       * Sets the BoundToIdentifier value.
       */
      pList.get ( i ).setBindingIdentifier (
          ( ( Identifier ) selectedNode.getPrettyPrintable ( ) )
              .getBoundToIdentifier ( ) ) ;
      /*
       * It should be replaced in higher nodes
       */
      pList.get ( i ).setReplaceInThisNode ( true ) ;
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getPrettyPrintable ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              selectedNode.getPrettyPrintable ( ) ) ;
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
          prettyAnnotation.getEndOffset ( ) ) ;
      /*
       * Node has changed and can be repainted
       */
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateType ( ArrayList < OutlineNode > pList ,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    for ( int i = 0 ; i < pList.size ( ) ; i ++ )
    {
      if ( ( selectedNode.getPrettyPrintable ( ) instanceof TypeName )
          && ( i < pList.size ( ) - 1 )
          && ( ( ( TypeName ) selectedNode.getPrettyPrintable ( ) )
              .getBoundToType ( ) != null ) )
      {
        try
        {
          TypeName typeName = ( TypeName ) selectedNode.getPrettyPrintable ( ) ;
          /*
           * Highlight the TypeName in the child node with the bound TypeName
           * index.
           */
          if ( pList.get ( i ).getPrettyPrintable ( ) == typeName
              .getBoundToType ( ) )
          {
            for ( int j = 0 ; j < pList.get ( i ).getChildCount ( ) ; j ++ )
            {
              OutlineNode nodeTypeName = ( OutlineNode ) pList.get ( i )
                  .getChildAt ( j ) ;
              if ( nodeTypeName.getPrettyPrintable ( ) == typeName
                  .getBoundToTypeName ( ) )
              {
                nodeTypeName.setBindingTypeName ( typeName
                    .getBoundToTypeName ( ) ) ;
                nodeTypeName.updateCaption ( ) ;
                break ;
              }
            }
          }
          /*
           * Highlight the TypeName in the node.
           */
          pList.get ( i ).setBindingTypeName ( typeName.getBoundToTypeName ( ) ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
      }
      /*
       * It should be replaced in higher nodes, but not the selected node
       */
      if ( i < pList.size ( ) - 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( true ) ;
      }
      /*
       * If only the root is selected, there should not be replaced
       */
      if ( pList.size ( ) == 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( false ) ;
      }
      /*
       * Update the caption of the node
       */
      if ( pList.get ( i ).isIdentifier ( ) )
      {
        continue ;
      }
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getPrettyPrintable ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              selectedNode.getPrettyPrintable ( ) ) ;
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
          prettyAnnotation.getEndOffset ( ) ) ;
      /*
       * Node has changed and can be repainted
       */
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateTypeName ( ArrayList < OutlineNode > pList ,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    for ( int i = 0 ; i < pList.size ( ) ; i ++ )
    {
      /*
       * Sets the new binding in higher nodes
       */
      pList.get ( i ).setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
      /*
       * Sets the setBindingTypeName value.
       */
      pList.get ( i ).setBindingTypeName (
          ( ( TypeName ) selectedNode.getPrettyPrintable ( ) )
              .getBoundToTypeName ( ) ) ;
      /*
       * It should be replaced in higher nodes
       */
      pList.get ( i ).setReplaceInThisNode ( true ) ;
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getPrettyPrintable ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              selectedNode.getPrettyPrintable ( ) ) ;
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
          prettyAnnotation.getEndOffset ( ) ) ;
      /*
       * Node has changed and can be repainted
       */
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
  }
}
