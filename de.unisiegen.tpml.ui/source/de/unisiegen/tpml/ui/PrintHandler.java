package de.unisiegen.tpml.ui;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.LinkedList;

import javax.swing.JComponent;


/**
 * @version $Id$
 */
public class PrintHandler
{

  /**
   * TODO
   */
  JComponent comp;


  /**
   * TODO
   * 
   * @param comp
   */
  public PrintHandler ( JComponent comp )
  {
    this.comp = comp;
  }


  /**
   * TODO
   */
  public void print ()
  {

    PrinterJob job = PrinterJob.getPrinterJob ();

    // It is first called to tell it what object will print each page.
    job.setPrintable ( new PrintObject ( this.comp ) );

    // Then it is called to display the standard print options dialog.
    if ( job.printDialog () )
    {
      // If the user has pressed OK (printDialog returns true), then go
      // ahead with the printing. This is started by the simple call to
      // the job print() method. When it runs, it calls the page print
      // object for page index 0. Then page index 1, 2, and so on
      // until NO_SUCH_PAGE is returned.
      try
      {
        job.print ();
      }
      catch ( PrinterException e )
      {
        System.out.println ( e );
      }
    }
  }

}


/**
 * TODO
 */
class PrintObject implements Printable
{

  /**
   * TODO
   */
  JComponent comp;


  // fuck my brain up:
  /**
   * TODO
   */
  LinkedList < LinkedList < Component >> pages;


  /**
   * TODO
   * 
   * @param comp
   */
  public PrintObject ( JComponent comp )
  {
    this.comp = comp;
  }


  /**
   * TODO
   * 
   * @param g
   * @param f
   * @param pageIndex
   * @return TODO
   * @see java.awt.print.Printable#print(java.awt.Graphics,
   *      java.awt.print.PageFormat, int)
   */
  public int print ( Graphics g, PageFormat f, int pageIndex )
  {
    System.out.println ( "Method print was called" ); //$NON-NLS-1$
    System.out.println ( pageIndex );

    // create new grafic area to print on:
    Graphics2D g2 = ( Graphics2D ) g;
    Graphics2D visible = ( Graphics2D ) g2.create ( ( int ) f.getImageableX (),
        ( int ) f.getImageableY (), ( int ) ( f.getImageableWidth () * 2 ),
        ( int ) ( f.getImageableHeight () * 2 ) );
    visible.scale ( 0.5, 0.5 );
    // Graphics2D visible = (Graphics2D) g2.create((int) f.getImageableX(),
    // (int) f.getImageableY(), (int) (f.getImageableWidth() * 2),
    // (int) (f.getImageableHeight() * 2));
    // visible.scale(0.5, 0.5);

    // if this is the first call get and sort all components of the view:
    if ( this.pages == null )
    {
      buildPageLists ( visible.getClipBounds ().height );

    }

    if ( pageIndex >= this.pages.size () )
    {
      return NO_SUCH_PAGE;
    }
    printPage ( this.pages.get ( pageIndex ), visible, pageIndex );
    return PAGE_EXISTS;
  }


  /**
   * TODO
   * 
   * @param components
   * @param g
   * @param PageIndex
   */
  private void printPage ( LinkedList < Component > components, Graphics2D g,
      @SuppressWarnings ( "unused" )
      int PageIndex )
  {
    Component last = components.get ( 0 );
    // the y position of the last drawn component
    int y_origin = 0;
    Graphics2D area = ( Graphics2D ) g.create ( 0, y_origin, last.getWidth (),
        last.getHeight () );
    last.print ( area );
    System.out.println ( components.get ( 0 ).getLocation ().y );
    Component current;
    for ( int i = 1 ; i < components.size () ; i++ )
    {
      current = components.get ( i );
      // new area to draw to y position is calculated from the last
      // position and the difference between the y positions of the last
      // and the current component.
      area = ( Graphics2D ) g.create ( 0, y_origin + current.getY ()
          - last.getY (), current.getWidth (), current.getHeight () );
      current.print ( area );

      y_origin = y_origin + current.getY () - last.getY ();
      last = current;
    }

  }


  /**
   * TODO
   * 
   * @param maxheight
   */
  private void buildPageLists ( int maxheight )
  {
    Component [] components = this.comp.getComponents ();
    LinkedList < Component > children = new LinkedList < Component > ();
    this.pages = new LinkedList < LinkedList < Component >> ();

    for ( Component element : components )
    {
      int size = children.size ();
      Component tosort = element;
      for ( int ii = 0 ; ii <= size ; ii++ )
      {
        if ( ii == children.size () )
        {
          children.add ( element );
          break;
        }
        if ( tosort.getLocation ().y < children.get ( ii ).getLocation ().y )
        {
          children.add ( ii - 1, tosort );
          break;
        }
      }
    }
    System.out.println ( "Size of Children: " + children.size () ); //$NON-NLS-1$

    LinkedList < Component > thispage = new LinkedList < Component > ();
    for ( int outer = 0 ; outer < children.size () ; outer++ )
    {
      Component tmp = children.get ( outer );
      if ( tmp.getLocation ().y + tmp.getHeight () < ( this.pages.size () + 1 )
          * maxheight )
      {
        thispage.addLast ( tmp );
      }
      else
      {
        this.pages.add ( thispage );
        thispage = new LinkedList < Component > ();
        outer-- ;
      }

    }
    this.pages.add ( thispage );
  }
}
