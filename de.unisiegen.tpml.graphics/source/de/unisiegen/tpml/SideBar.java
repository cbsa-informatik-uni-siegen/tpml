/**
 * 
 */
package de.unisiegen.tpml ;


import java.awt.Cursor ;
import java.awt.Dimension ;
import java.awt.Graphics ;
import java.awt.Rectangle ;
import java.awt.event.AdjustmentEvent ;
import java.awt.event.AdjustmentListener ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseMotionAdapter ;
import java.beans.PropertyChangeEvent ;
import java.beans.PropertyChangeListener ;
import java.util.ArrayList ;
import javax.swing.ImageIcon ;
import javax.swing.JComponent ;
import javax.swing.JScrollBar ;
import javax.swing.JScrollPane ;
import javax.swing.text.JTextComponent ;
import de.unisiegen.tpml.core.exceptions.LanguageParserReplaceException ;
import de.unisiegen.tpml.core.exceptions.LanguageParserWarningException ;
import de.unisiegen.tpml.core.languages.LanguageScannerException ;
import de.unisiegen.tpml.graphics.StyledLanguageDocument ;


/**
 * This class implements the sidebar which is used in the source views.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 */
public class SideBar extends JComponent
{
  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = - 4668570581006435967L ;


  /**
   * The parser normal error icon.
   */
  private ImageIcon errorIcon = null ;


  /**
   * The parser warning icon.
   */
  private ImageIcon warningIcon = null ;


  /**
   * The parser replace icon.
   */
  private ImageIcon replaceIcon = null ;


  /**
   * The vertical positions of the errors.
   */
  private int [ ] verticalPositions ;


  /**
   * The vertical {@link JScrollBar}.
   */
  private JScrollBar verticalScrollBar ;


  /**
   * The horizontal {@link JScrollBar}.
   */
  private JScrollBar horizontalScrollBar ;


  /**
   * The used {@link JScrollPane}.
   */
  private JScrollPane scrollPane ;


  /**
   * The array of {@link LanguageScannerException}.
   */
  private LanguageScannerException [ ] exceptions ;


  /**
   * The used {@link StyledLanguageDocument}.
   */
  private StyledLanguageDocument document ;


  /**
   * The used text component.
   */
  private JTextComponent textComponent ;


  /**
   * The status if something has changed.
   */
  private boolean proppertyChanged ;


  /**
   * The left offset of the current exception.
   */
  private int currentLeft ;


  /**
   * The right offset of the current exception.
   */
  private int currentRight ;


  /**
   * Initializes the {@link SideBar}.
   * 
   * @param pScrollPane The used {@link JScrollPane}.
   * @param pDocument The used {@link StyledLanguageDocument}.
   * @param pTextComponent The used text component.
   */
  public SideBar ( JScrollPane pScrollPane , StyledLanguageDocument pDocument ,
      JTextComponent pTextComponent )
  {
    super ( ) ;
    this.currentLeft = - 1 ;
    this.currentRight = - 1 ;
    this.scrollPane = pScrollPane ;
    this.document = pDocument ;
    this.textComponent = pTextComponent ;
    this.errorIcon = new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/error.gif" ) ) ; //$NON-NLS-1$
    this.warningIcon = new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/warning.gif" ) ) ; //$NON-NLS-1$
    this.replaceIcon = new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/error_blue.gif" ) ) ; //$NON-NLS-1$
    int imageWidth = this.errorIcon.getIconWidth ( ) ;
    this.proppertyChanged = false ;
    setMinimumSize ( new Dimension ( imageWidth , imageWidth ) ) ;
    this.verticalScrollBar = this.scrollPane.getVerticalScrollBar ( ) ;
    this.horizontalScrollBar = this.scrollPane.getHorizontalScrollBar ( ) ;
    this.verticalScrollBar.addAdjustmentListener ( new AdjustmentListener ( )
    {
      public void adjustmentValueChanged ( @ SuppressWarnings ( "unused" )
      AdjustmentEvent event )
      {
        repaint ( ) ;
      }
    } ) ;
    this.document.addPropertyChangeListener ( "exceptions" , //$NON-NLS-1$
        new PropertyChangeListener ( )
        {
          @ SuppressWarnings ( "synthetic-access" )
          public void propertyChange ( @ SuppressWarnings ( "unused" )
          PropertyChangeEvent event )
          {
            SideBar.this.proppertyChanged = true ;
            repaint ( ) ;
          }
        } ) ;
    this.addMouseMotionListener ( new MouseMotionAdapter ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      @ Override
      public void mouseMoved ( MouseEvent event )
      {
        SideBar.this.mouseMoved ( event ) ;
      }
    } ) ;
    this.addMouseListener ( new MouseAdapter ( )
    {
      @ Override
      public void mouseClicked ( MouseEvent event )
      {
        SideBar.this.mouseClicked ( event ) ;
      }
    } ) ;
  }


  /**
   * Adds the given {@link SideBarListener}.
   * 
   * @param pSideBarListener The given {@link SideBarListener}.
   */
  public void addSideBarListener ( SideBarListener pSideBarListener )
  {
    this.listenerList.add ( SideBarListener.class , pSideBarListener ) ;
  }


  /**
   * Builds the marks.
   */
  private void buildMarks ( )
  {
    this.exceptions = this.document.getExceptions ( ) ;
    this.verticalPositions = new int [ this.exceptions.length ] ;
    for ( int i = 0 ; i < this.exceptions.length ; i ++ )
    {
      try
      {
        this.verticalPositions [ i ] = - 1 ;
        Rectangle rect = this.textComponent.modelToView ( this.exceptions [ i ]
            .getLeft ( ) ) ;
        if ( rect == null )
        {
          return ;
        }
        this.verticalPositions [ i ] = rect.y + rect.height / 2 ;
      }
      catch ( Exception e )
      {
        continue ;
      }
    }
    this.proppertyChanged = false ;
  }


  /**
   * Inserts a given text at the given index.
   * 
   * @param pInsertText The text which should be inserted.
   */
  private void fireInsertText ( String pInsertText )
  {
    Object listeners[] = this.listenerList.getListenerList ( ) ;
    for ( int i = 0 ; i < listeners.length ; i ++ )
    {
      if ( listeners [ i ] == SideBarListener.class )
      {
        ( ( SideBarListener ) listeners [ i + 1 ] ).insertText (
            this.currentRight , pInsertText ) ;
      }
    }
  }


  /**
   * Marks the text with the given offsets.
   */
  private void fireMarkText ( )
  {
    Object listeners[] = this.listenerList.getListenerList ( ) ;
    for ( int i = 0 ; i < listeners.length ; i ++ )
    {
      if ( listeners [ i ] == SideBarListener.class )
      {
        ( ( SideBarListener ) listeners [ i + 1 ] ).markText (
            this.currentLeft , this.currentRight ) ;
      }
    }
  }


  /**
   * Replaces the texts with the given start and end offsets with the replace
   * text.
   * 
   * @param pStart The start offsets of the texts which should be renamed.
   * @param pEnd The end offsets of the texts which should be renamed.
   * @param pReplaceText The replace text.
   */
  private void fireReplaceText ( int [ ] pStart , int [ ] pEnd ,
      String pReplaceText )
  {
    Object listeners[] = this.listenerList.getListenerList ( ) ;
    for ( int i = 0 ; i < listeners.length ; i ++ )
    {
      if ( listeners [ i ] == SideBarListener.class )
      {
        ( ( SideBarListener ) listeners [ i + 1 ] ).replaceText ( pStart ,
            pEnd , pReplaceText ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Dimension getPreferredSize ( )
  {
    return new Dimension ( this.errorIcon.getIconWidth ( ) , getHeight ( ) ) ;
  }


  /**
   * Handles the mouse clicked event.
   * 
   * @param pMouseEvent The {@link MouseEvent}.
   */
  public void mouseClicked ( MouseEvent pMouseEvent )
  {
    if ( this.currentLeft == - 1 || this.currentRight == - 1 )
    {
      return ;
    }
    int y = pMouseEvent.getY ( ) + this.verticalScrollBar.getValue ( ) ;
    int hh = this.errorIcon.getIconHeight ( ) / 2 ;
    for ( int i = 0 ; i < this.verticalPositions.length ; i ++ )
    {
      if ( y > this.verticalPositions [ i ] - hh
          && y <= this.verticalPositions [ i ] + hh )
      {
        if ( this.exceptions [ i ] instanceof LanguageParserWarningException )
        {
          LanguageParserWarningException e = ( LanguageParserWarningException ) this.exceptions [ i ] ;
          this.currentLeft = this.exceptions [ i ].getLeft ( ) ;
          this.currentRight = this.exceptions [ i ].getRight ( ) ;
          fireInsertText ( e.getInsertText ( ) ) ;
          return ;
        }
        if ( this.exceptions [ i ] instanceof LanguageParserReplaceException )
        {
          LanguageParserReplaceException e = ( LanguageParserReplaceException ) this.exceptions [ i ] ;
          this.currentLeft = this.exceptions [ i ].getLeft ( ) ;
          this.currentRight = this.exceptions [ i ].getRight ( ) ;
          ArrayList < LanguageParserReplaceException > list = new ArrayList < LanguageParserReplaceException > ( ) ;
          for ( int j = 0 ; j < this.exceptions.length ; j ++ )
          {
            if ( this.exceptions [ j ] instanceof LanguageParserReplaceException )
            {
              list
                  .add ( ( LanguageParserReplaceException ) this.exceptions [ j ] ) ;
            }
          }
          int [ ] start = new int [ list.size ( ) ] ;
          int [ ] end = new int [ list.size ( ) ] ;
          for ( int j = 0 ; j < list.size ( ) ; j ++ )
          {
            start [ j ] = list.get ( j ).getParserStartOffsetReplace ( ) [ 0 ] ;
            end [ j ] = list.get ( j ).getParserEndOffsetReplace ( ) [ 0 ] ;
          }
          fireReplaceText ( start , end , e.getReplaceText ( ) ) ;
          return ;
        }
      }
    }
    fireMarkText ( ) ;
  }


  /**
   * Handles the mouse move event.
   * 
   * @param pMouseEvent The {@link MouseEvent}.
   */
  private void mouseMoved ( MouseEvent pMouseEvent )
  {
    if ( this.verticalPositions == null )
    {
      return ;
    }
    int y = pMouseEvent.getY ( ) + this.verticalScrollBar.getValue ( ) ;
    int hh = this.errorIcon.getIconHeight ( ) / 2 ;
    for ( int i = 0 ; i < this.verticalPositions.length ; i ++ )
    {
      if ( y > this.verticalPositions [ i ] - hh
          && y <= this.verticalPositions [ i ] + hh )
      {
        this.currentLeft = this.exceptions [ i ].getLeft ( ) ;
        this.currentRight = this.exceptions [ i ].getRight ( ) ;
        setToolTipText ( this.exceptions [ i ].getMessage ( ) ) ;
        setCursor ( new Cursor ( Cursor.HAND_CURSOR ) ) ;
        return ;
      }
    }
    setCursor ( new Cursor ( Cursor.DEFAULT_CURSOR ) ) ;
    setToolTipText ( null ) ;
    this.currentLeft = - 1 ;
    this.currentRight = - 1 ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  protected void paintComponent ( Graphics gc )
  {
    if ( this.proppertyChanged )
    {
      buildMarks ( ) ;
    }
    gc.setColor ( getBackground ( ) ) ;
    gc.fillRect ( 0 , 0 , getWidth ( ) , getHeight ( ) ) ;
    if ( this.verticalPositions == null )
    {
      return ;
    }
    for ( int i = 0 ; i < this.verticalPositions.length ; i ++ )
    {
      if ( this.verticalPositions [ i ] == - 1 )
      {
        continue ;
      }
      int y0 = this.verticalPositions [ i ] - this.errorIcon.getIconHeight ( )
          / 2 - this.verticalScrollBar.getValue ( ) ;
      int y1 = y0 + this.errorIcon.getIconHeight ( ) ;
      if ( y1 < 0 || y0 > getHeight ( ) )
      {
        continue ;
      }
      if ( this.exceptions [ i ] instanceof LanguageParserWarningException )
      {
        gc.drawImage ( this.warningIcon.getImage ( ) , 0 , y0 , this ) ;
      }
      else if ( this.exceptions [ i ] instanceof LanguageParserReplaceException )
      {
        gc.drawImage ( this.replaceIcon.getImage ( ) , 0 , y0 , this ) ;
      }
      else
      {
        gc.drawImage ( this.errorIcon.getImage ( ) , 0 , y0 , this ) ;
      }
    }
    gc.fillRect ( 0 , getHeight ( ) - this.horizontalScrollBar.getHeight ( ) ,
        getWidth ( ) , this.horizontalScrollBar.getHeight ( ) ) ;
  }


  /**
   * Adds the given {@link SideBarListener}.
   * 
   * @param pSideBarListener The given {@link SideBarListener}.
   */
  public void removeSideBarListener ( SideBarListener pSideBarListener )
  {
    this.listenerList.remove ( SideBarListener.class , pSideBarListener ) ;
  }
}
