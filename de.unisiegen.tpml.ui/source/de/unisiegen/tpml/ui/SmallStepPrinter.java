package de.unisiegen.tpml.ui;


import java.awt.Component;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import de.unisiegen.tpml.graphics.AbstractProofComponent;


/**
 * @version $Id$
 */
public class SmallStepPrinter
{

  /**
   * TODO
   */
  private Document document;


  /**
   * TODO
   */
  private JPanel caller;


  /**
   * TODO
   */
  private Component [] components;


  /**
   * TODO
   */
  private Rectangle pageFormat;


  /**
   * TODO
   */
  private java.awt.Graphics2D g2;


  /**
   * TODO
   */
  LinkedList < LinkedList < Component >> pages;


  /**
   * TODO
   */
  private double scale = .5;


  /**
   * TODO
   */
  private int right = 30;


  /**
   * TODO
   */
  private int above = 30;


  /**
   * TODO
   * 
   * @param caller
   */
  public SmallStepPrinter ( JPanel caller )
  {
    // document = new Document(PageSize.A4);
    // document = new Document(PageSize.A4.rotate());
    this.caller = caller;
  }


  /**
   * TODO
   * 
   * @param comp
   * @return TODO
   */
  public boolean print ( AbstractProofComponent comp )
  {
    // step 1
    JOptionPane.showMessageDialog ( this.caller, "Put Layoutchooser here." ); //$NON-NLS-1$
    this.pageFormat = PageSize.A4.rotate ();

    try
    {
      comp.setAvailableWidth ( ( int ) ( this.pageFormat.getWidth () )
          * ( int ) ( 1 / this.scale ) - 2 * this.right );
      comp.validate ();
      comp.doLayout ();

      // move the temporary pdf to the chosen directory
      JOptionPane.showMessageDialog ( this.caller, "Put Filechooser here!" ); //$NON-NLS-1$
      this.components = comp.getComponents ();

      // Number Of Pages
      int nop;
      int i = 0;
      do
      {
        this.document = new Document ( this.pageFormat, 0, 0, 0, 0 );
        PdfWriter writer = PdfWriter.getInstance ( this.document,
            new FileOutputStream ( "tmp" + i + ".pdf" ) ); //$NON-NLS-1$//$NON-NLS-2$
        this.document.open ();
        PdfContentByte cb = writer.getDirectContent ();
        // do not use the scale factor in the next one!
        this.g2 = cb.createGraphicsShapes ( this.pageFormat.getWidth (),
            this.pageFormat.getHeight () );
        this.g2.scale ( this.scale, this.scale );

        if ( this.pages == null )
        {
          buildPageLists ( this.g2.getClipBounds ().height );
        }
        nop = this.pages.size ();
        // g2.draw(new java.awt.Rectangle(0,0,(int)g2.getClipBounds().width,
        // (int)g2.getClipBounds().height/2));
        printPage ( this.pages.get ( i ), this.g2 );

        this.g2.dispose ();
        this.document.close ();
        i++ ;
      }
      while ( i < nop );

      // concatenate the temporary pages
      concatenatePages ( nop );

      // remove the temporary pages now
      deleteFiles ( nop );

      JOptionPane
          .showMessageDialog ( this.caller, "Document has been printed!" ); //$NON-NLS-1$

    }
    catch ( Exception de )
    {
      de.printStackTrace ();
    }
    return true;
  }


  /**
   * TODO
   * 
   * @param componentList
   * @param g
   */
  private void printPage ( LinkedList < Component > componentList, Graphics2D g )
  {
    // System.out.println("printPage was called");
    Component last = componentList.get ( 0 );
    // the y position of the last drawn component
    int y_origin = this.above;
    Graphics2D area = ( Graphics2D ) g.create ( this.right + last.getX () - 20,
        y_origin, last.getWidth (), last.getHeight () );
    last.paint ( area );
    Component current;
    for ( int i = 1 ; i < componentList.size () ; i++ )
    {
      current = componentList.get ( i );
      // new area to draw to y position is calculated from the last
      // position and the difference between the y positions of the last
      // and the current component.
      area = ( Graphics2D ) g.create ( this.right + current.getX () - 20,
          y_origin + current.getY () - last.getY (), current.getWidth (),
          current.getHeight () );
      current.paint ( area );

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
    // System.out.println("buildPageLists was called");
    this.pages = new LinkedList < LinkedList < Component >> ();
    // System.out.println("Maxheight is: "+ maxheight);
    LinkedList < Component > thispage = new LinkedList < Component > ();
    Component last = this.components [ 0 ];
    thispage.add ( last );
    // the y position of the last drawn component
    int y_origin = this.above;
    Component current;
    for ( int i = 1 ; i < this.components.length ; i++ )
    {
      current = this.components [ i ];
      int current_y = y_origin + current.getY () - last.getY ();
      System.out.println ( "Componentspositon: y:" + current.getY () + " x:"  //$NON-NLS-1$//$NON-NLS-2$
          + current.getX () );
      // new area to draw to y position is calculated from the last
      // position and the difference between the y positions of the last
      // and the current component.
      if ( current_y + current.getHeight () <= maxheight )
      {
        // //System.out.println("Component was added currentheight is:
        // "+current_y+current.getHeight());
        thispage.addLast ( current );
        y_origin = y_origin + current.getY () - last.getY ();
        last = current;
      }
      else
      {
        // System.out.println("Component was NOT added currentheight is:
        // "+current_y+current.getHeight());
        this.pages.add ( thispage );
        thispage = new LinkedList < Component > ();
        y_origin = this.above;
        i-- ;
      }

    }
    this.pages.add ( thispage );

  }


  /**
   * TODO
   * 
   * @param nop
   */
  @SuppressWarnings ( "unchecked" )
  private void concatenatePages ( int nop )
  {
    // concatenate temporary files here
    try
    {
      int pageOffset = 0;
      ArrayList master = new ArrayList ();
      int f = 0;
      String outFile = "out.pdf"; //$NON-NLS-1$
      Document newDocument = null;
      PdfCopy writer = null;
      while ( f < nop )
      {
        // we create a reader for a certain document
        PdfReader reader = new PdfReader ( "tmp" + f + ".pdf" ); //$NON-NLS-1$ //$NON-NLS-2$
        reader.consolidateNamedDestinations ();
        // we retrieve the total number of pages
        int n = reader.getNumberOfPages ();
        // List bookmarks = SimpleBookmark.getBookmark(reader);
        // if (bookmarks != null) {
        // if (pageOffset != 0)
        // SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
        // master.addAll(bookmarks);
        // }
        pageOffset += n;

        if ( f == 0 )
        {
          // step 1: creation of a document-object
          newDocument = new Document ( reader.getPageSizeWithRotation ( 1 ) );
          // step 2: we create a writer that listens to the document
          writer = new PdfCopy ( newDocument, new FileOutputStream ( outFile ) );
          // step 3: we open the document
          newDocument.open ();
        }
        // step 4: we add content
        PdfImportedPage page;
        for ( int i = 0 ; i < n ; )
        {
          ++i;
          if ( writer != null )
          {
            page = writer.getImportedPage ( reader, i );
            writer.addPage ( page );
          }
        }
        PRAcroForm form = reader.getAcroForm ();
        if ( form != null )
        {
          if ( writer != null )
          {
            writer.copyAcroForm ( reader );
          }
        }
        f++ ;
      }
      if ( !master.isEmpty () )
      {
        if ( writer != null )
        {
          writer.setOutlines ( master );
        }
      }
      // step 5: we close the document
      if ( newDocument != null )
      {
        newDocument.close ();
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
    }
  }


  /**
   * TODO
   * 
   * @param nop
   */
  private void deleteFiles ( int nop )
  {
    for ( int i = 0 ; i < nop ; i++ )
    {
      File f = new File ( "tmp" + i + ".pdf" ); //$NON-NLS-1$//$NON-NLS-2$
      f.delete ();
    }
  }
}
