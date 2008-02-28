package de.unisiegen.tpml.graphics.outline;


import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Timer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.TreePath;

import de.unisiegen.tpml.core.expressions.Attribute;
import de.unisiegen.tpml.core.expressions.CurriedMethod;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Method;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers;
import de.unisiegen.tpml.core.interfaces.BoundTypeNames;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers;
import de.unisiegen.tpml.core.interfaces.DefaultTypeNames;
import de.unisiegen.tpml.core.interfaces.DefaultTypes;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType;
import de.unisiegen.tpml.core.interfaces.SortedChildren;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeName;
import de.unisiegen.tpml.core.util.Theme;
import de.unisiegen.tpml.graphics.StyledLanguageDocument;
import de.unisiegen.tpml.graphics.bigstep.BigStepView;
import de.unisiegen.tpml.graphics.editor.TextEditorPanel;
import de.unisiegen.tpml.graphics.editor.TypeEditorPanel;
import de.unisiegen.tpml.graphics.minimaltyping.MinimalTypingView;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound;
import de.unisiegen.tpml.graphics.outline.listener.OutlineActionListener;
import de.unisiegen.tpml.graphics.outline.listener.OutlineComponentListener;
import de.unisiegen.tpml.graphics.outline.listener.OutlineItemListener;
import de.unisiegen.tpml.graphics.outline.listener.OutlineKeyListener;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener;
import de.unisiegen.tpml.graphics.outline.listener.OutlinePropertyChangeListener;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeExpansionListener;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeModelListener;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeSelectionListener;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode;
import de.unisiegen.tpml.graphics.outline.ui.OutlineDisplayTree;
import de.unisiegen.tpml.graphics.outline.ui.OutlineTimerTask;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView;
import de.unisiegen.tpml.graphics.subtyping.SubTypingView;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView;
import de.unisiegen.tpml.graphics.typeinference.TypeInferenceView;


/**
 * This class is the main class of the {@link Outline}. It loads the
 * {@link OutlinePreferences}, creates the {@link OutlineUI} and loads new
 * {@link Expression}s.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public final class DefaultOutline implements Outline
{

  /**
   * The free attribute set name.
   */
  private static final String SELECTED = "selected"; //$NON-NLS-1$


  /**
   * The bound identifier attribute set name.
   */
  private static final String IDENTIFER = "identifier"; //$NON-NLS-1$


  /**
   * The bound type name attribute set name.
   */
  private static final String TYPE_NAME = "type_name"; //$NON-NLS-1$


  /**
   * The {@link OutlineUI}.
   * 
   * @see #getUI()
   */
  private OutlineUI uI = null;


  /**
   * The {@link OutlineItemListener}.
   */
  private OutlineItemListener itemListener = null;


  /**
   * The {@link OutlinePreferences}.
   * 
   * @see #getPreferences()
   */
  private OutlinePreferences preferences = null;


  /**
   * The loaded {@link ExpressionOrType}.
   */
  private ExpressionOrType loadedExpressionOrType = null;


  /**
   * The {@link OutlineUnbound}, in which the unbound {@link Identifier}s in
   * the given {@link Expression} are saved.
   */
  private OutlineUnbound outlineUnbound = null;


  /**
   * The <code>Timer</code> for the executing.
   */
  private Timer outlineTimer = null;


  /**
   * The root {@link OutlineNode}.
   */
  private OutlineNode rootNode = null;


  /**
   * The {@link TextEditorPanel}.
   */
  private TextEditorPanel sourceView = null;


  /**
   * The {@link TypeEditorPanel}.
   */
  private TypeEditorPanel typeEditorPanel = null;


  /**
   * The sync outline.
   */
  private DefaultOutline syncOutline = null;


  /**
   * The error status of the {@link Outline}.
   */
  private boolean error = false;


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pBigStepView The {@link BigStepView}.
   */
  public DefaultOutline ( BigStepView pBigStepView )
  {
    this.preferences = new OutlinePreferences ();
    this.uI = new OutlineUI ( this );
    this.uI.deactivateAutoUpdate ();
    this.uI.deactivateHighlightSourceCode ();
    // ComponentListener
    this.uI.getJScrollPaneOutline ().addComponentListener (
        new OutlineComponentListener ( pBigStepView.getJSplitPane (), this ) );
    // PropertyChangeListener
    pBigStepView.addPropertyChangeListener ( new OutlinePropertyChangeListener (
        pBigStepView.getJSplitPane (), this ) );
    Theme.currentTheme ().addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) );
    // TreeModelListener
    pBigStepView.getBigStepProofModel ().addTreeModelListener (
        new OutlineTreeModelListener ( this, pBigStepView
            .getBigStepProofModel () ) );
    // MouseListener
    this.uI.getJTreeOutline ().addMouseListener (
        new OutlineMouseListener ( this ) );
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this );
    this.uI.getJMenuItemExpand ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemExpandAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapse ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapseAll ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemClose ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCloseAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCopy ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemSelection ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemBinding ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemFree ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemReplace ().addActionListener ( outlineActionListener );
    // ComponentListener
    this.uI.getJPanelMain ().addComponentListener (
        new OutlineComponentListener ( this ) );
    // ItemListener
    this.itemListener = new OutlineItemListener ( this );
    this.uI.getJCheckBoxSelection ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxBinding ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxFree ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxReplace ().addItemListener ( this.itemListener );
    // KeyListener
    this.uI.getJTreeOutline ()
        .addKeyListener ( new OutlineKeyListener ( this ) );
    // TreeExpansionListener
    this.uI.getJTreeOutline ().addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) );
    // TreeSelectionListener
    this.uI.getJTreeOutline ().getSelectionModel ().addTreeSelectionListener (
        new OutlineTreeSelectionListener ( this ) );
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pMinimalTypingView The {@link MinimalTypingView}.
   */
  public DefaultOutline ( MinimalTypingView pMinimalTypingView )
  {
    this.preferences = new OutlinePreferences ();
    this.uI = new OutlineUI ( this );
    this.uI.deactivateAutoUpdate ();
    this.uI.deactivateHighlightSourceCode ();
    // ComponentListener
    this.uI.getJScrollPaneOutline ().addComponentListener (
        new OutlineComponentListener ( pMinimalTypingView.getJSplitPane (),
            this ) );
    // PropertyChangeListener
    pMinimalTypingView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pMinimalTypingView.getJSplitPane (), this ) );
    Theme.currentTheme ().addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) );
    // TreeModelListener
    pMinimalTypingView.getMinimalTypingProofModel ().addTreeModelListener (
        new OutlineTreeModelListener ( this, pMinimalTypingView
            .getMinimalTypingProofModel () ) );
    // MouseListener
    this.uI.getJTreeOutline ().addMouseListener (
        new OutlineMouseListener ( this ) );
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this );
    this.uI.getJMenuItemExpand ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemExpandAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapse ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapseAll ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemClose ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCloseAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCopy ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemSelection ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemBinding ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemFree ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemReplace ().addActionListener ( outlineActionListener );
    // ComponentListener
    this.uI.getJPanelMain ().addComponentListener (
        new OutlineComponentListener ( this ) );
    // ItemListener
    this.itemListener = new OutlineItemListener ( this );
    this.uI.getJCheckBoxSelection ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxBinding ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxFree ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxReplace ().addItemListener ( this.itemListener );
    // KeyListener
    this.uI.getJTreeOutline ()
        .addKeyListener ( new OutlineKeyListener ( this ) );
    // TreeExpansionListener
    this.uI.getJTreeOutline ().addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) );
    // TreeSelectionListener
    this.uI.getJTreeOutline ().getSelectionModel ().addTreeSelectionListener (
        new OutlineTreeSelectionListener ( this ) );
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pSmallStepView {@link SmallStepView}.
   */
  public DefaultOutline ( SmallStepView pSmallStepView )
  {
    this.preferences = new OutlinePreferences ();
    this.uI = new OutlineUI ( this );
    this.uI.deactivateHighlightSourceCode ();
    // ComponentListener
    this.uI.getJScrollPaneOutline ().addComponentListener (
        new OutlineComponentListener ( pSmallStepView.getJSplitPane (), this ) );
    // PropertyChangeListener
    pSmallStepView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pSmallStepView.getJSplitPane (), this ) );
    Theme.currentTheme ().addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) );
    // TreeModelListener
    pSmallStepView.getSmallStepProofModel ().addTreeModelListener (
        new OutlineTreeModelListener ( this, pSmallStepView
            .getSmallStepProofModel () ) );
    // MouseListener
    this.uI.getJTreeOutline ().addMouseListener (
        new OutlineMouseListener ( this ) );
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this );
    this.uI.getJMenuItemExpand ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemExpandAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapse ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapseAll ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemClose ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCloseAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCopy ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemSelection ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemBinding ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemFree ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemReplace ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemAutoUpdate ()
        .addActionListener ( outlineActionListener );
    // ComponentListener
    this.uI.getJPanelMain ().addComponentListener (
        new OutlineComponentListener ( this ) );
    // ItemListener
    this.itemListener = new OutlineItemListener ( this );
    this.uI.getJCheckBoxSelection ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxBinding ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxFree ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxReplace ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxAutoUpdate ().addItemListener ( this.itemListener );
    // KeyListener
    this.uI.getJTreeOutline ()
        .addKeyListener ( new OutlineKeyListener ( this ) );
    // TreeExpansionListener
    this.uI.getJTreeOutline ().addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) );
    // TreeSelectionListener
    this.uI.getJTreeOutline ().getSelectionModel ().addTreeSelectionListener (
        new OutlineTreeSelectionListener ( this ) );
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pSubTypingSourceView The {@link TypeEditorPanel}.
   * @param pModus The {@link Outline.Modus} of this {@link Outline}.
   */
  public DefaultOutline ( TypeEditorPanel pSubTypingSourceView,
      Outline.Modus pModus )
  {
    this.preferences = new OutlinePreferences ();
    this.uI = new OutlineUI ( this );
    this.typeEditorPanel = pSubTypingSourceView;
    // ComponentListener
    this.uI.getJScrollPaneOutline ().addComponentListener (
        new OutlineComponentListener ( pSubTypingSourceView.getJSplitPane (),
            this ) );
    // MouseListener
    switch ( pModus )
    {
      case FIRST :
      {
        pSubTypingSourceView.getEditor ().addMouseListener (
            new OutlineMouseListener ( this, pSubTypingSourceView ) );
        break;
      }
      case SECOND :
      {
        pSubTypingSourceView.getEditor2 ().addMouseListener (
            new OutlineMouseListener ( this, pSubTypingSourceView ) );
        break;
      }
    }
    this.uI.getJTreeOutline ().addMouseListener (
        new OutlineMouseListener ( this ) );
    // PropertyChangeListener
    pSubTypingSourceView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pSubTypingSourceView.getJSplitPane (), this ) );
    Theme.currentTheme ().addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) );
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this );
    this.uI.getJMenuItemExpand ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemExpandAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapse ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapseAll ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemClose ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCloseAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCopy ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemSelection ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemBinding ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemFree ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemReplace ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemHighlightSourceCode ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemAutoUpdate ()
        .addActionListener ( outlineActionListener );
    // ComponentListener
    this.uI.getJPanelMain ().addComponentListener (
        new OutlineComponentListener ( this ) );
    // ItemListener
    this.itemListener = new OutlineItemListener ( this );
    this.uI.getJCheckBoxSelection ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxBinding ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxFree ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxReplace ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxHighlightSourceCode ().addItemListener (
        this.itemListener );
    this.uI.getJCheckBoxAutoUpdate ().addItemListener ( this.itemListener );
    // KeyListener
    this.uI.getJTreeOutline ()
        .addKeyListener ( new OutlineKeyListener ( this ) );
    // TreeExpansionListener
    this.uI.getJTreeOutline ().addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) );
    // TreeSelectionListener
    this.uI.getJTreeOutline ().getSelectionModel ().addTreeSelectionListener (
        new OutlineTreeSelectionListener ( this ) );
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pSubTypingView The {@link SubTypingView}.
   */
  public DefaultOutline ( SubTypingView pSubTypingView )
  {
    this.preferences = new OutlinePreferences ();
    this.uI = new OutlineUI ( this );
    this.uI.deactivateAutoUpdate ();
    this.uI.deactivateHighlightSourceCode ();
    // ComponentListener
    this.uI.getJScrollPaneOutline ().addComponentListener (
        new OutlineComponentListener ( pSubTypingView.getJSplitPane (), this ) );
    // PropertyChangeListener
    pSubTypingView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pSubTypingView.getJSplitPane (), this ) );
    Theme.currentTheme ().addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) );
    // TreeModelListener
    pSubTypingView.getSubTypingModel ().addTreeModelListener (
        new OutlineTreeModelListener ( this, pSubTypingView ) );
    // MouseListener
    this.uI.getJTreeOutline ().addMouseListener (
        new OutlineMouseListener ( this ) );
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this );
    this.uI.getJMenuItemExpand ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemExpandAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapse ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapseAll ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemClose ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCloseAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCopy ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemSelection ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemBinding ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemFree ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemReplace ().addActionListener ( outlineActionListener );
    // ComponentListener
    this.uI.getJPanelMain ().addComponentListener (
        new OutlineComponentListener ( this ) );
    // ItemListener
    this.itemListener = new OutlineItemListener ( this );
    this.uI.getJCheckBoxSelection ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxBinding ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxFree ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxReplace ().addItemListener ( this.itemListener );
    // KeyListener
    this.uI.getJTreeOutline ()
        .addKeyListener ( new OutlineKeyListener ( this ) );
    // TreeExpansionListener
    this.uI.getJTreeOutline ().addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) );
    // TreeSelectionListener
    this.uI.getJTreeOutline ().getSelectionModel ().addTreeSelectionListener (
        new OutlineTreeSelectionListener ( this ) );
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pSourceView The {@link TextEditorPanel}.
   */
  public DefaultOutline ( TextEditorPanel pSourceView )
  {
    this.preferences = new OutlinePreferences ();
    this.uI = new OutlineUI ( this );
    this.sourceView = pSourceView;
    // ComponentListener
    this.uI.getJScrollPaneOutline ().addComponentListener (
        new OutlineComponentListener ( pSourceView.getJSplitPane (), this ) );
    // MouseListener
    this.sourceView.getEditor ().addMouseListener (
        new OutlineMouseListener ( this, pSourceView ) );
    this.uI.getJTreeOutline ().addMouseListener (
        new OutlineMouseListener ( this ) );
    // PropertyChangeListener
    this.sourceView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            this.sourceView.getJSplitPane (), this ) );
    Theme.currentTheme ().addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) );
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this );
    this.uI.getJMenuItemExpand ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemExpandAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapse ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapseAll ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemClose ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCloseAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCopy ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemSelection ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemBinding ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemFree ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemReplace ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemHighlightSourceCode ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemAutoUpdate ()
        .addActionListener ( outlineActionListener );
    // ComponentListener
    this.uI.getJPanelMain ().addComponentListener (
        new OutlineComponentListener ( this ) );
    // ItemListener
    this.itemListener = new OutlineItemListener ( this );
    this.uI.getJCheckBoxSelection ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxBinding ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxFree ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxReplace ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxHighlightSourceCode ().addItemListener (
        this.itemListener );
    this.uI.getJCheckBoxAutoUpdate ().addItemListener ( this.itemListener );
    // KeyListener
    this.uI.getJTreeOutline ()
        .addKeyListener ( new OutlineKeyListener ( this ) );
    // TreeExpansionListener
    this.uI.getJTreeOutline ().addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) );
    // TreeSelectionListener
    this.uI.getJTreeOutline ().getSelectionModel ().addTreeSelectionListener (
        new OutlineTreeSelectionListener ( this ) );
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pTypeCheckerView The {@link TypeCheckerView}.
   */
  public DefaultOutline ( TypeCheckerView pTypeCheckerView )
  {
    this.preferences = new OutlinePreferences ();
    this.uI = new OutlineUI ( this );
    this.uI.deactivateAutoUpdate ();
    this.uI.deactivateHighlightSourceCode ();
    // ComponentListener
    this.uI.getJScrollPaneOutline ()
        .addComponentListener (
            new OutlineComponentListener ( pTypeCheckerView.getJSplitPane (),
                this ) );
    // PropertyChangeListener
    pTypeCheckerView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pTypeCheckerView.getJSplitPane (), this ) );
    Theme.currentTheme ().addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) );
    // TreeModelListener
    pTypeCheckerView.getTypeCheckerProofModel ().addTreeModelListener (
        new OutlineTreeModelListener ( this, pTypeCheckerView
            .getTypeCheckerProofModel () ) );
    // MouseListener
    this.uI.getJTreeOutline ().addMouseListener (
        new OutlineMouseListener ( this ) );
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this );
    this.uI.getJMenuItemExpand ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemExpandAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapse ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapseAll ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemClose ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCloseAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCopy ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemSelection ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemBinding ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemFree ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemReplace ().addActionListener ( outlineActionListener );
    // ComponentListener
    this.uI.getJPanelMain ().addComponentListener (
        new OutlineComponentListener ( this ) );
    // ItemListener
    this.itemListener = new OutlineItemListener ( this );
    this.uI.getJCheckBoxSelection ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxBinding ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxFree ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxReplace ().addItemListener ( this.itemListener );
    // KeyListener
    this.uI.getJTreeOutline ()
        .addKeyListener ( new OutlineKeyListener ( this ) );
    // TreeExpansionListener
    this.uI.getJTreeOutline ().addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) );
    // TreeSelectionListener
    this.uI.getJTreeOutline ().getSelectionModel ().addTreeSelectionListener (
        new OutlineTreeSelectionListener ( this ) );
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pTypeInferenceView The {@link TypeInferenceView}.
   */
  public DefaultOutline ( TypeInferenceView pTypeInferenceView )
  {
    this.preferences = new OutlinePreferences ();
    this.uI = new OutlineUI ( this );
    this.uI.deactivateAutoUpdate ();
    this.uI.deactivateHighlightSourceCode ();
    // ComponentListener
    this.uI.getJScrollPaneOutline ().addComponentListener (
        new OutlineComponentListener ( pTypeInferenceView.getJSplitPane (),
            this ) );
    // PropertyChangeListener
    pTypeInferenceView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pTypeInferenceView.getJSplitPane (), this ) );
    Theme.currentTheme ().addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) );
    // TreeModelListener
    pTypeInferenceView.getTypeInferenceProofModel ().addTreeModelListener (
        new OutlineTreeModelListener ( this, pTypeInferenceView
            .getTypeInferenceProofModel () ) );
    this.uI.getJTreeOutline ().addMouseListener (
        new OutlineMouseListener ( this ) );
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this );
    this.uI.getJMenuItemExpand ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemExpandAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapse ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCollapseAll ().addActionListener (
        outlineActionListener );
    this.uI.getJMenuItemClose ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCloseAll ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemCopy ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemSelection ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemBinding ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemFree ().addActionListener ( outlineActionListener );
    this.uI.getJMenuItemReplace ().addActionListener ( outlineActionListener );
    // ComponentListener
    this.uI.getJPanelMain ().addComponentListener (
        new OutlineComponentListener ( this ) );
    // ItemListener
    this.itemListener = new OutlineItemListener ( this );
    this.uI.getJCheckBoxSelection ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxBinding ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxFree ().addItemListener ( this.itemListener );
    this.uI.getJCheckBoxReplace ().addItemListener ( this.itemListener );
    // KeyListener
    this.uI.getJTreeOutline ()
        .addKeyListener ( new OutlineKeyListener ( this ) );
    // TreeExpansionListener
    this.uI.getJTreeOutline ().addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) );
    // TreeSelectionListener
    this.uI.getJTreeOutline ().getSelectionModel ().addTreeSelectionListener (
        new OutlineTreeSelectionListener ( this ) );
  }


  /**
   * Creates the children with the given {@link Expression} and adds them to the
   * given {@link OutlineNode}.
   * 
   * @param pExpression The {@link Expression}, with which the children should
   *          be created.
   * @param pParent The {@link OutlineNode} where the children should be added.
   */
  private final void createExpression ( Expression pExpression,
      OutlineNode pParent )
  {
    // Child Expression
    int [] expressionsIndex = null;
    // Identifier
    Identifier [] identifiers = null;
    int [] identifiersIndex = null;
    // Bound Identifier
    ArrayList < ArrayList < Identifier >> identifiersBound = null;
    // Type
    MonoType [] types = null;
    int [] typesIndex = null;
    // Sorted Children
    ExpressionOrType [] sortedChildren = null;
    // The Expression has one or more child Expressions
    if ( pExpression instanceof DefaultExpressions )
    {
      expressionsIndex = ( ( DefaultExpressions ) pExpression )
          .getExpressionsIndex ();
    }
    // The Expresion has one or more Identifiers
    if ( pExpression instanceof DefaultIdentifiers )
    {
      identifiersIndex = ( ( DefaultIdentifiers ) pExpression )
          .getIdentifiersIndex ();
      identifiers = ( ( DefaultIdentifiers ) pExpression ).getIdentifiers ();
    }
    // The Expression has one or more Identifiers which bind other Identifiers
    if ( pExpression instanceof BoundIdentifiers )
    {
      identifiersBound = ( ( BoundIdentifiers ) pExpression )
          .getIdentifiersBound ();
    }
    // The Expression has one or more Types.
    if ( pExpression instanceof DefaultTypes )
    {
      typesIndex = ( ( DefaultTypes ) pExpression ).getTypesIndex ();
      types = ( ( DefaultTypes ) pExpression ).getTypes ();
    }
    // The Expression has sorted children.
    if ( pExpression instanceof SortedChildren )
    {
      sortedChildren = ( ( SortedChildren ) pExpression ).getSortedChildren ();
    }
    OutlineNode outlineNodeId;
    OutlineNode outlineNodeType;
    OutlineNode outlineNodeE;
    OutlineBinding < Identifier > outlineBinding;
    // No sorted children
    if ( sortedChildren == null )
    {
      // Identifier
      if ( ( identifiers != null ) && ( identifiersIndex != null ) )
      {
        for ( int i = 0 ; i < identifiers.length ; i++ )
        {
          if ( identifiersBound == null )
          {
            outlineBinding = null;
          }
          else
          {
            outlineBinding = new OutlineBinding < Identifier > (
                identifiersBound.get ( i ) );
          }
          outlineNodeId = new OutlineNode ( identifiers [ i ],
              identifiersIndex [ i ], outlineBinding );
          // Identifier - Type
          if ( ( types != null ) && ( typesIndex != null ) )
          {
            for ( int j = 0 ; j < types.length ; j++ )
            {
              if ( ( types [ j ] != null ) && ( typesIndex [ j ] != -1 )
                  && ( typesIndex [ j ] == identifiersIndex [ i ] ) )
              {
                outlineNodeType = new OutlineNode ( types [ j ],
                    this.outlineUnbound, typesIndex [ j ] );
                createType ( types [ j ], outlineNodeType );
                outlineNodeId.add ( outlineNodeType );
              }
            }
          }
          pParent.add ( outlineNodeId );
        }
      }
      // Type
      if ( ( types != null ) && ( typesIndex != null ) )
      {
        for ( int i = 0 ; i < types.length ; i++ )
        {
          if ( ( types [ i ] != null ) && ( typesIndex [ i ] == -1 ) )
          {
            outlineNodeType = new OutlineNode ( types [ i ],
                this.outlineUnbound, typesIndex [ i ] );
            createType ( types [ i ], outlineNodeType );
            pParent.add ( outlineNodeType );
          }
        }
      }
      // Expression
      if ( expressionsIndex != null )
      {
        ArrayList < Expression > children = pExpression.children ();
        for ( int i = 0 ; i < children.size () ; i++ )
        {
          Expression child = children.get ( i );
          outlineNodeE = new OutlineNode ( child, this.outlineUnbound,
              expressionsIndex [ i ] );
          createExpression ( child, outlineNodeE );
          pParent.add ( outlineNodeE );
        }
      }
    }
    // Sorted children
    else
    {
      ArrayList < ExpressionOrType > notFound = new ArrayList < ExpressionOrType > (
          sortedChildren.length );
      for ( int i = 0 ; i < sortedChildren.length ; i++ )
      {
        ExpressionOrType current = sortedChildren [ i ];
        boolean found = false;
        // Identifier
        if ( ( identifiers != null ) && ( identifiersIndex != null ) )
        {
          for ( int j = 0 ; j < identifiers.length ; j++ )
          {
            if ( current == identifiers [ j ] )
            {
              if ( identifiersBound == null )
              {
                outlineBinding = null;
              }
              else
              {
                outlineBinding = new OutlineBinding < Identifier > (
                    identifiersBound.get ( i ) );
              }
              outlineNodeId = new OutlineNode ( identifiers [ j ],
                  identifiersIndex [ j ], outlineBinding );
              pParent.add ( outlineNodeId );
              found = true;
              break;
            }
          }
        }
        // Type
        if ( ( !found ) && ( types != null ) && ( typesIndex != null ) )
        {
          for ( int j = 0 ; j < types.length ; j++ )
          {
            if ( current == types [ j ] )
            {
              outlineNodeType = new OutlineNode ( types [ j ],
                  this.outlineUnbound, typesIndex [ j ] );
              createType ( types [ j ], outlineNodeType );
              pParent.add ( outlineNodeType );
              found = true;
              break;
            }
          }
        }
        // Expression
        if ( ( !found ) && ( expressionsIndex != null ) )
        {
          ArrayList < Expression > children = pExpression.children ();
          for ( int j = 0 ; j < children.size () ; j++ )
          {
            if ( current == children.get ( j ) )
            {
              Expression child = children.get ( j );
              outlineNodeE = new OutlineNode ( child, this.outlineUnbound,
                  expressionsIndex [ j ] );
              createExpression ( child, outlineNodeE );
              pParent.add ( outlineNodeE );
              found = true;
              break;
            }
          }
        }
        if ( !found )
        {
          notFound.add ( current );
        }
      }
      // Not found PrettyPrintables
      if ( notFound.size () > 0 )
      {
        for ( ExpressionOrType current : notFound )
        {
          if ( current instanceof Identifier )
          {
            outlineNodeId = new OutlineNode ( ( Identifier ) current, -1, null );
            pParent.add ( outlineNodeId );
          }
          else if ( current instanceof Type )
          {
            outlineNodeType = new OutlineNode ( ( Type ) current,
                this.outlineUnbound, -1 );
            createType ( ( Type ) current, outlineNodeType );
            pParent.add ( outlineNodeType );
          }
          else if ( current instanceof Expression )
          {
            outlineNodeE = new OutlineNode ( ( Expression ) current,
                this.outlineUnbound, -1 );
            createExpression ( ( Expression ) current, outlineNodeE );
            pParent.add ( outlineNodeE );
          }
        }
      }
    }
  }


  /**
   * Creates the children with the given {@link Type} and adds them to the given
   * {@link OutlineNode}.
   * 
   * @param pType The {@link Type}, with which the children should be created.
   * @param pParent The {@link OutlineNode} where the children should be added.
   */
  private final void createType ( Type pType, OutlineNode pParent )
  {
    // Type
    MonoType [] types = null;
    int [] typesIndex = null;
    // Identifier
    Identifier [] identifiers = null;
    int [] identifiersIndex = null;
    // Sorted children
    ExpressionOrType [] sortedChildren = null;
    // TypeName
    TypeName [] typeNames = null;
    int [] typeNamesIndex = null;
    // Bound TypeName
    ArrayList < ArrayList < TypeName >> typeNamesBound = null;
    // The Type has one or more Types.
    if ( pType instanceof DefaultTypes )
    {
      typesIndex = ( ( DefaultTypes ) pType ).getTypesIndex ();
      types = ( ( DefaultTypes ) pType ).getTypes ();
    }
    // The Type has one or more Identifiers
    if ( pType instanceof DefaultIdentifiers )
    {
      identifiersIndex = ( ( DefaultIdentifiers ) pType )
          .getIdentifiersIndex ();
      identifiers = ( ( DefaultIdentifiers ) pType ).getIdentifiers ();
    }
    // The Type has one or more TypeNames
    if ( pType instanceof DefaultTypeNames )
    {
      typeNamesIndex = ( ( DefaultTypeNames ) pType ).getTypeNamesIndex ();
      typeNames = ( ( DefaultTypeNames ) pType ).getTypeNames ();
    }
    // The Type has one or more TypeNames which bind other TypeNames
    if ( pType instanceof BoundTypeNames )
    {
      typeNamesBound = ( ( BoundTypeNames ) pType ).getTypeNamesBound ();
    }
    // The Type has sorted children.
    if ( pType instanceof SortedChildren )
    {
      sortedChildren = ( ( SortedChildren ) pType ).getSortedChildren ();
    }
    OutlineNode outlineNodeType;
    OutlineNode outlineNodeTypeName;
    OutlineNode outlineNodeId;
    OutlineBinding < TypeName > outlineBinding;
    // No sorted children
    if ( sortedChildren == null )
    {
      // Identifier
      if ( ( identifiers != null ) && ( identifiersIndex != null ) )
      {
        for ( int i = 0 ; i < identifiers.length ; i++ )
        {
          outlineNodeId = new OutlineNode ( identifiers [ i ],
              identifiersIndex [ i ], null );
          pParent.add ( outlineNodeId );
        }
      }
      // TypeName
      if ( ( typeNames != null ) && ( typeNamesIndex != null ) )
      {
        for ( int i = 0 ; i < typeNames.length ; i++ )
        {
          if ( typeNamesBound == null )
          {
            outlineBinding = null;
          }
          else
          {
            outlineBinding = new OutlineBinding < TypeName > ( typeNamesBound
                .get ( i ) );
          }
          outlineNodeTypeName = new OutlineNode ( typeNames [ i ],
              typeNamesIndex [ i ], outlineBinding );
          pParent.add ( outlineNodeTypeName );
        }
      }
      // Type
      if ( ( types != null ) && ( typesIndex != null ) )
      {
        for ( int i = 0 ; i < types.length ; i++ )
        {
          outlineNodeType = new OutlineNode ( types [ i ], this.outlineUnbound,
              typesIndex [ i ] );
          createType ( types [ i ], outlineNodeType );
          pParent.add ( outlineNodeType );
        }
      }
    }
    // Sorted children
    else
    {
      ArrayList < ExpressionOrType > notFound = new ArrayList < ExpressionOrType > (
          sortedChildren.length );
      for ( int i = 0 ; i < sortedChildren.length ; i++ )
      {
        ExpressionOrType current = sortedChildren [ i ];
        boolean found = false;
        // Identifier
        if ( ( identifiers != null ) && ( identifiersIndex != null ) )
        {
          for ( int j = 0 ; j < identifiers.length ; j++ )
          {
            if ( current == identifiers [ j ] )
            {
              outlineNodeId = new OutlineNode ( identifiers [ j ],
                  identifiersIndex [ j ], null );
              pParent.add ( outlineNodeId );
              found = true;
              break;
            }
          }
        }
        // TypeName
        if ( ( !found ) && ( typeNames != null ) && ( typeNamesIndex != null ) )
        {
          for ( int j = 0 ; j < typeNames.length ; j++ )
          {
            if ( current == typeNames [ j ] )
            {
              if ( typeNamesBound == null )
              {
                outlineBinding = null;
              }
              else
              {
                outlineBinding = new OutlineBinding < TypeName > (
                    typeNamesBound.get ( j ) );
              }
              outlineNodeTypeName = new OutlineNode ( typeNames [ j ],
                  typeNamesIndex [ j ], outlineBinding );
              pParent.add ( outlineNodeTypeName );
            }
          }
        }
        // Type
        if ( ( !found ) && ( types != null ) && ( typesIndex != null ) )
        {
          for ( int j = 0 ; j < types.length ; j++ )
          {
            if ( current == types [ j ] )
            {
              outlineNodeType = new OutlineNode ( types [ j ],
                  this.outlineUnbound, typesIndex [ j ] );
              createType ( types [ j ], outlineNodeType );
              pParent.add ( outlineNodeType );
              found = true;
              break;
            }
          }
        }
        if ( !found )
        {
          notFound.add ( current );
        }
      }
      // Not found PrettyPrintables
      if ( notFound.size () > 0 )
      {
        for ( ExpressionOrType current : notFound )
        {
          if ( current instanceof Identifier )
          {
            outlineNodeId = new OutlineNode ( ( Identifier ) current, -1, null );
            pParent.add ( outlineNodeId );
          }
          else if ( current instanceof TypeName )
          {
            outlineNodeId = new OutlineNode ( ( TypeName ) current, -1, null );
            pParent.add ( outlineNodeId );
          }
          else if ( current instanceof Type )
          {
            outlineNodeType = new OutlineNode ( ( Type ) current,
                this.outlineUnbound, -1 );
            createType ( ( Type ) current, outlineNodeType );
            pParent.add ( outlineNodeType );
          }
        }
      }
    }
  }


  /**
   * Execute the rebuild of a new tree in the {@link Outline}.
   */
  public final synchronized void execute ()
  {
    // If nothing is loaded, nothing is done
    if ( this.loadedExpressionOrType == null )
    {
      return;
    }
    // Load a new Expression into the outline
    if ( this.loadedExpressionOrType instanceof Expression )
    {
      Expression expression = ( Expression ) this.loadedExpressionOrType;
      this.outlineUnbound = new OutlineUnbound ( expression );
      this.rootNode = new OutlineNode ( expression, this.outlineUnbound,
          OutlineNode.NO_CHILD_INDEX );
      createExpression ( expression, this.rootNode );
    }
    // Load a new Type into the outline
    else if ( this.loadedExpressionOrType instanceof Type )
    {
      Type type = ( Type ) this.loadedExpressionOrType;
      this.outlineUnbound = new OutlineUnbound ( type );
      this.rootNode = new OutlineNode ( type, this.outlineUnbound,
          OutlineNode.NO_CHILD_INDEX );
      createType ( type, this.rootNode );
    }
    // Throw an exception if something different should be loaded.
    else
    {
      throw new IllegalArgumentException (
          "Outline: The input is not an Expression or Type!" ); //$NON-NLS-1$
    }
    updateCaption ( this.rootNode );
    setError ( false );
    SwingUtilities.invokeLater ( new OutlineDisplayTree ( this ) );
  }


  /**
   * Cancels the execute <code>Timer</code>.
   */
  private final synchronized void executeTimerCancel ()
  {
    if ( this.outlineTimer != null )
    {
      this.outlineTimer.cancel ();
      this.outlineTimer = null;
    }
  }


  /**
   * Starts the execute <code>Timer</code>, which will execute the rebuild of
   * a new tree in the {@link Outline} after the given delay, if it is not
   * canceled during this time.
   * 
   * @param pDelay Delay in milliseconds before task is to be executed.
   */
  private final synchronized void executeTimerStart ( int pDelay )
  {
    if ( pDelay < 0 )
    {
      throw new IllegalArgumentException ( "Delay is smaller than 0" ); //$NON-NLS-1$
    }
    this.outlineTimer = new Timer ();
    this.outlineTimer.schedule ( new OutlineTimerTask ( this ), pDelay );
  }


  /**
   * Returns the outlineItemListener.
   * 
   * @return The outlineItemListener.
   * @see #itemListener
   */
  public final OutlineItemListener getItemListener ()
  {
    return this.itemListener;
  }


  /**
   * Returns the <code>JPanel</code> of the {@link OutlineUI}.
   * 
   * @return The <code>JPanel</code> of the {@link OutlineUI}.
   * @see de.unisiegen.tpml.graphics.outline.Outline#getPanel()
   */
  public final JPanel getPanel ()
  {
    return this.uI.getJPanelMain ();
  }


  /**
   * Returns the <code>JPanel</code> of the {@link OutlineUI} preferences.
   * 
   * @return The <code>JPanel</code> of the {@link OutlineUI} preferences.
   */
  public final JPanel getPanelPreferences ()
  {
    return this.uI.getJPanelPreferences ();
  }


  /**
   * Returns the {@link OutlinePreferences}.
   * 
   * @return The {@link OutlinePreferences}.
   * @see #preferences
   */
  public final OutlinePreferences getPreferences ()
  {
    return this.preferences;
  }


  /**
   * Returns the syncOutline.
   * 
   * @return The syncOutline.
   * @see #syncOutline
   */
  public final DefaultOutline getSyncOutline ()
  {
    return this.syncOutline;
  }


  /**
   * Returns the <code>JScrollPane</code> of the {@link OutlineUI} tree.
   * 
   * @return The <code>JScrollPane</code> of the {@link OutlineUI} tree.
   */
  public final JScrollPane getTree ()
  {
    return this.uI.getJScrollPaneOutline ();
  }


  /**
   * Returns the {@link OutlineUI}.
   * 
   * @return The {@link OutlineUI}.
   * @see #uI
   */
  public final OutlineUI getUI ()
  {
    return this.uI;
  }


  /**
   * This method loads a new {@link ExpressionOrType} into the {@link Outline}.
   * It does nothing if the auto update is disabled and the change does not come
   * from a <code>MouseEvent</code>.
   * 
   * @param pExpressionOrType The new {@link ExpressionOrType}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public final void load ( ExpressionOrType pExpressionOrType,
      Outline.Execute pExecute )
  {
    /*
     * If the invoke comes from a mouse click on the editor or the auto change
     * is active, the error is set and nothing is loaded.
     */
    if ( pExpressionOrType == null )
    {
      executeTimerCancel ();
      if ( ( this.preferences.isAutoUpdate () )
          || ( Outline.ExecuteMouseClick.EDITOR.equals ( pExecute ) )
          || ( Outline.ExecuteMouseClick.SUBTYPING_SOURCE.equals ( pExecute ) ) )
      {
        setError ( true );
      }
      return;
    }
    if ( pExecute instanceof Outline.ExecuteAutoChange )
    {
      Outline.ExecuteAutoChange execute = ( Outline.ExecuteAutoChange ) pExecute;
      switch ( execute )
      {
        case EDITOR :
        case SMALLSTEP :
        case SUBTYPING_SOURCE :
        {
          if ( !this.preferences.isAutoUpdate () )
          {
            return;
          }
          break;
        }
        case BIGSTEP :
        case TYPECHECKER :
        case TYPEINFERENCE :
        case MINIMALTYPING :
        case SUBTYPING :
        {
          return;
        }
      }
    }
    // Cancel the maybe running timer
    executeTimerCancel ();
    setError ( false );
    this.loadedExpressionOrType = pExpressionOrType;
    /*
     * Execute the new load of the Expression or the Type immediately, if the
     * change is an init change or a change because of a mouse click.
     */
    if ( pExecute instanceof Outline.ExecuteInit )
    {
      Outline.ExecuteInit execute = ( Outline.ExecuteInit ) pExecute;
      switch ( execute )
      {
        case EDITOR :
        case SMALLSTEP :
        case BIGSTEP :
        case TYPECHECKER :
        case TYPEINFERENCE :
        case MINIMALTYPING :
        case SUBTYPING_SOURCE :
        case SUBTYPING :
        {
          execute ();
          break;
        }
      }
    }
    else if ( pExecute instanceof Outline.ExecuteMouseClick )
    {
      Outline.ExecuteMouseClick execute = ( Outline.ExecuteMouseClick ) pExecute;
      switch ( execute )
      {
        case EDITOR :
        case SMALLSTEP :
        case BIGSTEP :
        case TYPECHECKER :
        case TYPEINFERENCE :
        case MINIMALTYPING :
        case SUBTYPING_SOURCE :
        case SUBTYPING :
        {
          execute ();
          break;
        }
      }
    }
    else if ( pExecute instanceof Outline.ExecuteAutoChange )
    {
      Outline.ExecuteAutoChange execute = ( Outline.ExecuteAutoChange ) pExecute;
      switch ( execute )
      {
        case EDITOR :
        case SUBTYPING_SOURCE :
        {
          executeTimerStart ( 500 );
          break;
        }
        case SMALLSTEP :
        case BIGSTEP :
        case TYPECHECKER :
        case TYPEINFERENCE :
        case MINIMALTYPING :
        case SUBTYPING :
        {
          executeTimerStart ( 250 );
          break;
        }
      }
    }
  }


  /**
   * Repaints the root node and all of its children with the new color settings.
   */
  public final void propertyChanged ()
  {
    updateHighlighSourceCode ( true );
    propertyChanged ( this.rootNode );
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
      return;
    }
    pOutlineNode.propertyChanged ();
    pOutlineNode.updateCaption ();
    this.uI.getTreeModel ().nodeChanged ( pOutlineNode );
    for ( int i = 0 ; i < pOutlineNode.getChildCount () ; i++ )
    {
      propertyChanged ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) );
    }
  }


  /**
   * Repaints the given node and all its children.
   * 
   * @param pOutlineNode The node, which should be repainted.
   */
  private final void repaintNode ( OutlineNode pOutlineNode )
  {
    this.uI.getTreeModel ().nodeChanged ( pOutlineNode );
    for ( int i = 0 ; i < pOutlineNode.getChildCount () ; i++ )
    {
      repaintNode ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) );
    }
  }


  /**
   * Resets the root node and all its children.
   */
  public final void resetNode ()
  {
    if ( this.rootNode == null )
    {
      return;
    }
    resetNode ( this.rootNode );
  }


  /**
   * Resets the given node and all its children.
   * 
   * @param pOutlineNode The node, which should be reseted.
   */
  private final void resetNode ( OutlineNode pOutlineNode )
  {
    pOutlineNode.setReplaceInThisNode ( false );
    pOutlineNode.setOutlineBinding ( null );
    pOutlineNode.setBindingIdentifier ( null );
    pOutlineNode.updateCaption ();
    for ( int i = 0 ; i < pOutlineNode.getChildCount () ; i++ )
    {
      resetNode ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) );
    }
  }


  /**
   * Updates the UI mit or without an error.
   * 
   * @param pStatus True, if the error should be set.
   */
  private final void setError ( boolean pStatus )
  {
    this.error = pStatus;
    if ( pStatus )
    {
      this.uI.getJScrollPaneOutline ().setBorder (
          new LineBorder ( Color.RED, 3 ) );
      // updateHighlighSourceCode ( false ) ;
    }
    else
    {
      this.uI.getJScrollPaneOutline ().setBorder (
          new LineBorder ( Color.WHITE, 3 ) );
    }
  }


  /**
   * Sets the root node in the {@link OutlineUI}.
   */
  public final synchronized void setRootNode ()
  {
    this.uI.setRootNode ( this.rootNode );
    updateBreaks ();
  }


  /**
   * Sets the sync outline.
   * 
   * @param pSyncOutline The sync outline.
   */
  public final void setSyncOutline ( DefaultOutline pSyncOutline )
  {
    if ( pSyncOutline == null )
    {
      throw new IllegalArgumentException (
          "The sync outline should not be null" ); //$NON-NLS-1$
    }
    if ( this.syncOutline != pSyncOutline )
    {
      this.syncOutline = pSyncOutline;
      this.syncOutline.setSyncOutline ( this );
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pTreePath The selected <code>TreePath</code>.
   */
  public final void update ( TreePath pTreePath )
  {
    if ( this.rootNode == null )
    {
      return;
    }
    resetNode ();
    if ( pTreePath == null )
    {
      repaintNode ( this.rootNode );
      return;
    }
    ArrayList < OutlineNode > list = new ArrayList < OutlineNode > ();
    Object [] path = pTreePath.getPath ();
    for ( int i = 0 ; i < pTreePath.getPathCount () ; i++ )
    {
      list.add ( ( OutlineNode ) path [ i ] );
    }
    OutlineNode selectedNode = list.get ( list.size () - 1 );
    // Expression
    if ( selectedNode.isExpression () )
    {
      updateExpression ( list, pTreePath );
    }
    // Identifier
    else if ( selectedNode.isIdentifier () )
    {
      updateIdentifier ( list, pTreePath );
    }
    // Type
    else if ( selectedNode.isType () )
    {
      updateType ( list, pTreePath );
    }
    // TypeName
    else if ( selectedNode.isTypeName () )
    {
      updateTypeName ( list, pTreePath );
    }
    updateBreaks ();
  }


  /**
   * Updates the breaks in the {@link OutlineNode}.
   */
  public final void updateBreaks ()
  {
    if ( this.rootNode == null )
    {
      return;
    }
    final int distance = 20;
    JScrollPane jScrollPaneOutline = this.uI.getJScrollPaneOutline ();
    OutlineNode currentNode;
    TreePath currentTreePath;
    Rectangle rectangle;
    Enumeration < ? > enumeration = this.rootNode.breadthFirstEnumeration ();
    while ( enumeration.hasMoreElements () )
    {
      currentNode = ( OutlineNode ) enumeration.nextElement ();
      currentTreePath = new TreePath ( currentNode.getPath () );
      rectangle = this.uI.getJTreeOutline ().getPathBounds ( currentTreePath );
      if ( rectangle != null )
      {
        /*
         * Remove a break from the node, if it is to small and a break can be
         * removed. If the node is after the remove to big, a break is added.
         */
        boolean removed = false;
        while ( ( currentNode.breaksCanRemove () )
            && ( ( rectangle.x + rectangle.width ) < ( jScrollPaneOutline
                .getSize ().width - distance ) ) )
        {
          currentNode.breakCountRemove ();
          this.uI.getTreeModel ().nodeChanged ( currentNode );
          rectangle = this.uI.getJTreeOutline ().getPathBounds (
              currentTreePath );
          /*
           * If the node is after the remove to big, a break is added.
           */
          if ( ( rectangle.x + rectangle.width ) > ( jScrollPaneOutline
              .getSize ().width - distance ) )
          {
            currentNode.breakCountAdd ();
            this.uI.getTreeModel ().nodeChanged ( currentNode );
            rectangle = this.uI.getJTreeOutline ().getPathBounds (
                currentTreePath );
            break;
          }
          removed = true;
        }
        /*
         * Add a break to the node, if it is to big and more breaks can be
         * added.
         */
        while ( ( !removed )
            && ( currentNode.breaksCanAdd () )
            && ( ( rectangle.x + rectangle.width ) > ( jScrollPaneOutline
                .getSize ().width - distance ) ) )
        {
          currentNode.breakCountAdd ();
          this.uI.getTreeModel ().nodeChanged ( currentNode );
          rectangle = this.uI.getJTreeOutline ().getPathBounds (
              currentTreePath );
        }
      }
    }
  }


  /**
   * Repaints the given node and all of its children and resets the caption.
   * 
   * @param pOutlineNode The node, which should be repainted.
   */
  private final void updateCaption ( OutlineNode pOutlineNode )
  {
    pOutlineNode.updateCaption ();
    this.uI.getTreeModel ().nodeChanged ( pOutlineNode );
    for ( int i = 0 ; i < pOutlineNode.getChildCount () ; i++ )
    {
      updateCaption ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) );
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateExpression ( ArrayList < OutlineNode > pList,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size () - 1 );
    for ( int i = 0 ; i < pList.size () ; i++ )
    {
      if ( ( selectedNode.getExpressionOrType () instanceof Identifier )
          && ( i < pList.size () - 1 )
          && ( ( ( Identifier ) selectedNode.getExpressionOrType () )
              .getBoundToExpression () != null ) )
      {
        try
        {
          Identifier identifier = ( Identifier ) selectedNode
              .getExpressionOrType ();
          /*
           * Highlight the bound Identifiers in the other childs of a parent
           * row.
           */
          if ( ( pList.get ( i ).getExpressionOrType () instanceof Attribute )
              || ( pList.get ( i ).getExpressionOrType () instanceof Method )
              || ( pList.get ( i ).getExpressionOrType () instanceof CurriedMethod ) )
          {
            OutlineNode nodeRowChild = ( OutlineNode ) pTreePath.getPath () [ i ];
            OutlineNode nodeRow = ( OutlineNode ) pTreePath.getPath () [ i - 1 ];
            for ( int j = nodeRow.getIndex ( nodeRowChild ) ; j >= 0 ; j-- )
            {
              OutlineNode currentOutlineNode = ( OutlineNode ) nodeRow
                  .getChildAt ( j );
              if ( currentOutlineNode.getExpressionOrType () == identifier
                  .getBoundToExpression () )
              {
                /*
                 * Highlight the first identifier
                 */
                currentOutlineNode.setBindingIdentifier ( identifier
                    .getBoundToIdentifier () );
                currentOutlineNode.updateCaption ();
                /*
                 * Highlight the Identifier in the first child
                 */
                for ( int k = 0 ; k < currentOutlineNode.getChildCount () ; k++ )
                {
                  OutlineNode nodeId = ( OutlineNode ) currentOutlineNode
                      .getChildAt ( k );
                  if ( nodeId.getExpressionOrType () == identifier
                      .getBoundToIdentifier () )
                  {
                    nodeId.setBindingIdentifier ( identifier
                        .getBoundToIdentifier () );
                    nodeId.updateCaption ();
                    break;
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
            if ( pList.get ( i ).getExpressionOrType () == identifier
                .getBoundToExpression () )
            {
              for ( int j = 0 ; j < pList.get ( i ).getChildCount () ; j++ )
              {
                OutlineNode nodeId = ( OutlineNode ) pList.get ( i )
                    .getChildAt ( j );
                if ( nodeId.getExpressionOrType () == identifier
                    .getBoundToIdentifier () )
                {
                  nodeId.setBindingIdentifier ( identifier
                      .getBoundToIdentifier () );
                  nodeId.updateCaption ();
                  break;
                }
              }
            }
            /*
             * Highlight the Identifier in the node.
             */
            pList.get ( i ).setBindingIdentifier (
                identifier.getBoundToIdentifier () );
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
      if ( i < pList.size () - 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( true );
      }
      /*
       * If only the root is selected, there should not be replaced
       */
      if ( pList.size () == 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( false );
      }
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i )
          .getExpressionOrType ().toPrettyString ().getAnnotationForPrintable (
              selectedNode.getExpressionOrType () );
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset (),
          prettyAnnotation.getEndOffset () );
      /*
       * Node has changed and can be repainted
       */
      this.uI.getTreeModel ().nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath () [ i ] ) );
    }
  }


  /**
   * Updates the highlighting of the source code.
   * 
   * @param pHighlight True, if the source code should be highlighted. False, if
   *          if the source code should be reseted.
   */
  public final void updateHighlighSourceCode ( boolean pHighlight )
  {
    StyledLanguageDocument document;
    if ( this.sourceView != null )
    {
      document = this.sourceView.getDocument ();
    }
    else if ( this.typeEditorPanel != null )
    {
      if ( this.typeEditorPanel.getOutline1 () == this )
      {
        document = ( StyledLanguageDocument ) this.typeEditorPanel.getEditor ()
            .getDocument ();
      }
      else if ( this.typeEditorPanel.getOutline2 () == this )
      {
        document = ( StyledLanguageDocument ) this.typeEditorPanel
            .getEditor2 ().getDocument ();
      }
      else
      {
        return;
      }
    }
    else
    {
      return;
    }
    try
    {
      document.processChanged ();
    }
    catch ( BadLocationException e )
    {
      // Do nothing
    }
    if ( ( pHighlight ) && ( !this.error ) )
    {
      TreePath treePath = this.uI.getJTreeOutline ().getSelectionPath ();
      if ( treePath == null )
      {
        return;
      }
      OutlineNode outlineNode = ( OutlineNode ) treePath
          .getLastPathComponent ();
      if ( outlineNode.getExpressionOrType () instanceof Identifier )
      {
        Identifier identifier = ( Identifier ) outlineNode
            .getExpressionOrType ();
        if ( identifier.getBoundToIdentifier () != null )
        {
          identifier = identifier.getBoundToIdentifier ();
        }
        if ( ( identifier.getParent () ) != null
            && ( identifier.getParent () instanceof BoundIdentifiers ) )
        {
          // Binding
          SimpleAttributeSet bindingSet = new SimpleAttributeSet ();
          StyleConstants.setForeground ( bindingSet, Theme.currentTheme ()
              .getBindingIdColor () );
          StyleConstants.setBold ( bindingSet, true );
          bindingSet.addAttribute ( IDENTIFER, IDENTIFER );
          document.setCharacterAttributes ( identifier.getParserStartOffset (),
              identifier.getParserEndOffset ()
                  - identifier.getParserStartOffset (), bindingSet, false );
          BoundIdentifiers parent = ( BoundIdentifiers ) identifier
              .getParent ();
          ArrayList < ArrayList < Identifier >> identifiersBound = parent
              .getIdentifiersBound ();
          for ( int i = 0 ; i < parent.getIdentifiers ().length ; i++ )
          {
            if ( identifier == parent.getIdentifiers () [ i ] )
            {
              for ( Identifier current : identifiersBound.get ( i ) )
              {
                // Bound
                SimpleAttributeSet boundSet = new SimpleAttributeSet ();
                StyleConstants.setForeground ( boundSet, Theme.currentTheme ()
                    .getBoundIdColor () );
                StyleConstants.setBold ( boundSet, true );
                boundSet.addAttribute ( IDENTIFER, IDENTIFER );
                document.setCharacterAttributes ( current
                    .getParserStartOffset (), current.getParserEndOffset ()
                    - current.getParserStartOffset (), boundSet, false );

              }
              break;
            }
          }
        }
      }
      if ( outlineNode.getExpressionOrType () instanceof Expression )
      {
        Expression expression = ( Expression ) outlineNode
            .getExpressionOrType ();
        SimpleAttributeSet freeSet = new SimpleAttributeSet ();
        StyleConstants.setBackground ( freeSet, Theme.currentTheme ()
            .getHighlightSourceCodeColor () );
        freeSet.addAttribute ( SELECTED, SELECTED );
        document.setCharacterAttributes ( expression.getParserStartOffset (),
            expression.getParserEndOffset ()
                - expression.getParserStartOffset (), freeSet, false );
      }
      if ( outlineNode.getExpressionOrType () instanceof TypeName )
      {
        TypeName typeName = ( TypeName ) outlineNode.getExpressionOrType ();
        if ( typeName.getBoundToTypeName () != null )
        {
          typeName = typeName.getBoundToTypeName ();
        }
        if ( ( typeName.getParent () ) != null
            && ( typeName.getParent () instanceof BoundTypeNames ) )
        {
          // Binding
          SimpleAttributeSet bindingSet = new SimpleAttributeSet ();
          StyleConstants.setForeground ( bindingSet, Theme.currentTheme ()
              .getBindingIdColor () );
          StyleConstants.setBold ( bindingSet, true );
          bindingSet.addAttribute ( TYPE_NAME, TYPE_NAME );
          document
              .setCharacterAttributes ( typeName.getParserStartOffset (),
                  typeName.getParserEndOffset ()
                      - typeName.getParserStartOffset (), bindingSet, false );
          BoundTypeNames parent = ( BoundTypeNames ) typeName.getParent ();
          ArrayList < ArrayList < TypeName >> typeNamesBound = parent
              .getTypeNamesBound ();
          for ( int i = 0 ; i < parent.getTypeNames ().length ; i++ )
          {
            if ( typeName == parent.getTypeNames () [ i ] )
            {
              for ( TypeName current : typeNamesBound.get ( i ) )
              {
                // Bound
                SimpleAttributeSet boundSet = new SimpleAttributeSet ();
                StyleConstants.setForeground ( boundSet, Theme.currentTheme ()
                    .getBoundIdColor () );
                StyleConstants.setBold ( boundSet, true );
                boundSet.addAttribute ( TYPE_NAME, TYPE_NAME );
                document.setCharacterAttributes ( current
                    .getParserStartOffset (), current.getParserEndOffset ()
                    - current.getParserStartOffset (), boundSet, false );

              }
              break;
            }
          }
        }
      }
      if ( outlineNode.getExpressionOrType () instanceof Type )
      {
        Type type = ( Type ) outlineNode.getExpressionOrType ();
        SimpleAttributeSet freeSet = new SimpleAttributeSet ();
        StyleConstants.setBackground ( freeSet, Theme.currentTheme ()
            .getHighlightSourceCodeColor () );
        freeSet.addAttribute ( SELECTED, SELECTED );
        document.setCharacterAttributes ( type.getParserStartOffset (), type
            .getParserEndOffset ()
            - type.getParserStartOffset (), freeSet, false );
      }
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateIdentifier ( ArrayList < OutlineNode > pList,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size () - 1 );
    OutlineNode nodeAttribute = pList.get ( pList.size () - 2 );
    /*
     * Highlight the bound Identifiers of an Attribute in the other childs of
     * the parent row.
     */
    if ( nodeAttribute.getExpressionOrType () instanceof Attribute )
    {
      OutlineNode nodeRow = pList.get ( pList.size () - 3 );
      for ( int i = nodeRow.getIndex ( nodeAttribute ) + 1 ; i < nodeRow
          .getChildCount () ; i++ )
      {
        OutlineNode currentRowChild = ( OutlineNode ) nodeRow.getChildAt ( i );
        currentRowChild.setOutlineBinding ( selectedNode.getOutlineBinding () );
        currentRowChild.updateCaption ();
      }
    }
    for ( int i = 0 ; i < pList.size () ; i++ )
    {
      /*
       * Sets the new binding in higher nodes
       */
      pList.get ( i ).setOutlineBinding ( selectedNode.getOutlineBinding () );
      /*
       * Sets the BoundToIdentifier value.
       */
      pList.get ( i ).setBindingIdentifier (
          ( ( Identifier ) selectedNode.getExpressionOrType () )
              .getBoundToIdentifier () );
      /*
       * It should be replaced in higher nodes
       */
      pList.get ( i ).setReplaceInThisNode ( true );
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i )
          .getExpressionOrType ().toPrettyString ().getAnnotationForPrintable (
              selectedNode.getExpressionOrType () );
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset (),
          prettyAnnotation.getEndOffset () );
      /*
       * Node has changed and can be repainted
       */
      this.uI.getTreeModel ().nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath () [ i ] ) );
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateType ( ArrayList < OutlineNode > pList,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size () - 1 );
    for ( int i = 0 ; i < pList.size () ; i++ )
    {
      if ( ( selectedNode.getExpressionOrType () instanceof TypeName )
          && ( i < pList.size () - 1 )
          && ( ( ( TypeName ) selectedNode.getExpressionOrType () )
              .getBoundToType () != null ) )
      {
        try
        {
          TypeName typeName = ( TypeName ) selectedNode.getExpressionOrType ();
          /*
           * Highlight the TypeName in the child node with the bound TypeName
           * index.
           */
          if ( pList.get ( i ).getExpressionOrType () == typeName
              .getBoundToType () )
          {
            for ( int j = 0 ; j < pList.get ( i ).getChildCount () ; j++ )
            {
              OutlineNode nodeTypeName = ( OutlineNode ) pList.get ( i )
                  .getChildAt ( j );
              if ( nodeTypeName.getExpressionOrType () == typeName
                  .getBoundToTypeName () )
              {
                nodeTypeName.setBindingTypeName ( typeName
                    .getBoundToTypeName () );
                nodeTypeName.updateCaption ();
                break;
              }
            }
          }
          /*
           * Highlight the TypeName in the node.
           */
          pList.get ( i ).setBindingTypeName ( typeName.getBoundToTypeName () );
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
      }
      /*
       * It should be replaced in higher nodes, but not the selected node
       */
      if ( i < pList.size () - 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( true );
      }
      /*
       * If only the root is selected, there should not be replaced
       */
      if ( pList.size () == 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( false );
      }
      /*
       * Update the caption of the node
       */
      if ( pList.get ( i ).isIdentifier () )
      {
        continue;
      }
      PrettyAnnotation prettyAnnotation = pList.get ( i )
          .getExpressionOrType ().toPrettyString ().getAnnotationForPrintable (
              selectedNode.getExpressionOrType () );
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset (),
          prettyAnnotation.getEndOffset () );
      /*
       * Node has changed and can be repainted
       */
      this.uI.getTreeModel ().nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath () [ i ] ) );
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateTypeName ( ArrayList < OutlineNode > pList,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size () - 1 );
    for ( int i = 0 ; i < pList.size () ; i++ )
    {
      /*
       * Sets the new binding in higher nodes
       */
      pList.get ( i ).setOutlineBinding ( selectedNode.getOutlineBinding () );
      /*
       * Sets the setBindingTypeName value.
       */
      pList.get ( i ).setBindingTypeName (
          ( ( TypeName ) selectedNode.getExpressionOrType () )
              .getBoundToTypeName () );
      /*
       * It should be replaced in higher nodes
       */
      pList.get ( i ).setReplaceInThisNode ( true );
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i )
          .getExpressionOrType ().toPrettyString ().getAnnotationForPrintable (
              selectedNode.getExpressionOrType () );
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset (),
          prettyAnnotation.getEndOffset () );
      /*
       * Node has changed and can be repainted
       */
      this.uI.getTreeModel ().nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath () [ i ] ) );
    }
  }
}
