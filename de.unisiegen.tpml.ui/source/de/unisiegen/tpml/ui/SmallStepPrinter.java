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


public class SmallStepPrinter
{

  private Document document;


  private JPanel caller;


  private Component [] components;


  private Rectangle pageFormat;


  private java.awt.Graphics2D g2;


  LinkedList < LinkedList < Component >> pages;


  private double scale = .5;


  private int right = 30;


  private int above = 30;


  public SmallStepPrinter ( JPanel caller )
  {
    // document = new Document(PageSize.A4);
    // document = new Document(PageSize.A4.rotate());
    this.caller = caller;
  }


  public boolean print ( AbstractProofComponent comp )
  {
    // step 1
    JOptionPane.showMessageDialog ( caller, "Put Layoutchooser here." );
    pageFormat = PageSize.A4.rotate ();

    try
    {
      // creating a temporary file to wirte to:
      String tmpdir = System.getProperty ( "java.io.tmpdir" );
      // System.out.println(tmpdir);

      comp.setAvailableWidth ( ( int ) ( pageFormat.getWidth () )
          * ( int ) ( 1 / scale ) - 2 * right );
      comp.validate ();
      comp.doLayout ();

      // move the temporary pdf to the chosen directory
      JOptionPane.showMessageDialog ( caller, "Put Filechooser here!" );
      components = comp.getComponents ();

      // Number Of Pages
      int nop;
      int i = 0;
      do
      {
        document = new Document ( pageFormat, 0, 0, 0, 0 );
        PdfWriter writer = PdfWriter.getInstance ( document,
            new FileOutputStream ( "tmp" + i + ".pdf" ) );
        document.open ();
        PdfContentByte cb = writer.getDirectContent ();
        // do not use the scale factor in the next one!
        g2 = cb.createGraphicsShapes ( pageFormat.getWidth (), pageFormat
            .getHeight () );
        g2.scale ( scale, scale );

        if ( pages == null )
          buildPageLists ( ( int ) g2.getClipBounds ().height );
        nop = pages.size ();
        // g2.draw(new java.awt.Rectangle(0,0,(int)g2.getClipBounds().width,
        // (int)g2.getClipBounds().height/2));
        printPage ( pages.get ( i ), g2 );

        g2.dispose ();
        document.close ();
        i++ ;
      }
      while ( i < nop );

      // concatenate the temporary pages
      this.concatenatePages ( nop );

      // remove the temporary pages now
      this.deleteFiles ( nop );

      JOptionPane.showMessageDialog ( caller, "Document has been printed!" );

    }
    catch ( Exception de )
    {
      de.printStackTrace ();
    }
    return true;
  }


  private void printPage ( LinkedList < Component > components, Graphics2D g )
  {
    // System.out.println("printPage was called");
    Component last = components.get ( 0 );
    // the y position of the last drawn component
    int y_origin = above;
    Graphics2D area = ( Graphics2D ) g.create ( right + last.getX () - 20,
        y_origin, last.getWidth (), last.getHeight () );
    last.paint ( area );
    Component current;
    for ( int i = 1 ; i < components.size () ; i++ )
    {
      current = components.get ( i );
      // new area to draw to y position is calculated from the last
      // position and the difference between the y positions of the last
      // and the current component.
      area = ( Graphics2D ) g.create ( right + current.getX () - 20, y_origin
          + current.getY () - last.getY (), current.getWidth (), current
          .getHeight () );
      current.paint ( area );

      y_origin = y_origin + current.getY () - last.getY ();
      last = current;
    }

  }


  private void buildPageLists ( int maxheight )
  {
    // System.out.println("buildPageLists was called");
    pages = new LinkedList < LinkedList < Component >> ();
    // System.out.println("Maxheight is: "+ maxheight);
    LinkedList < Component > thispage = new LinkedList < Component > ();
    Component last = components [ 0 ];
    thispage.add ( last );
    // the y position of the last drawn component
    int y_origin = above;
    Component current;
    for ( int i = 1 ; i < components.length ; i++ )
    {
      current = components [ i ];
      int current_y = y_origin + current.getY () - last.getY ();
      System.out.println ( "Componentspositon: y:" + current.getY () + " x:"
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
        pages.add ( thispage );
        thispage = new LinkedList < Component > ();
        y_origin = above;
        i-- ;
      }

    }
    pages.add ( thispage );

  }


  private void concatenatePages ( int nop )
  {
    // concatenate temporary files here
    try
    {
      int pageOffset = 0;
      ArrayList master = new ArrayList ();
      int f = 0;
      String outFile = "out.pdf";
      Document document = null;
      PdfCopy writer = null;
      while ( f < nop )
      {
        // we create a reader for a certain document
        PdfReader reader = new PdfReader ( "tmp" + f + ".pdf" );
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
          document = new Document ( reader.getPageSizeWithRotation ( 1 ) );
          // step 2: we create a writer that listens to the document
          writer = new PdfCopy ( document, new FileOutputStream ( outFile ) );
          // step 3: we open the document
          document.open ();
        }
        // step 4: we add content
        PdfImportedPage page;
        for ( int i = 0 ; i < n ; )
        {
          ++i;
          page = writer.getImportedPage ( reader, i );
          writer.addPage ( page );
        }
        PRAcroForm form = reader.getAcroForm ();
        if ( form != null )
          writer.copyAcroForm ( reader );
        f++ ;
      }
      if ( !master.isEmpty () )
        writer.setOutlines ( master );
      // step 5: we close the document
      document.close ();
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
    }
  }


  private void deleteFiles ( int nop )
  {
    for ( int i = 0 ; i < nop ; i++ )
    {
      File f = new File ( "tmp" + i + ".pdf" );
      f.delete ();
    }
  }

}
