package de.unisiegen.tpml.ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import de.unisiegen.tpml.graphics.AbstractProofComponent;
import de.unisiegen.tpml.graphics.StyledLanguageEditor;
import de.unisiegen.tpml.graphics.editor.TypeEditorPanel;
import de.unisiegen.tpml.ui.netbeans.PdfDialog;


/**
 * @version $Id$
 */
public class GeneralPrinter
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
  private AbstractProofComponent comp;


  /**
   * TODO
   */
  private Rectangle pageFormat;


  /**
   * TODO
   */
  private java.awt.Graphics2D g1;


  /**
   * TODO
   */
  private java.awt.Graphics2D g2;


  /**
   * TODO
   */
  private java.awt.Graphics2D g3;


  /**
   * TODO
   */
  private java.awt.Graphics2D g4;


  /**
   * TODO
   */
  LinkedList < LinkedList < Component >> pages;


  /**
   * TODO
   */
  private double scale = .6;


  /**
   * TODO
   */
  private int right = 40;


  /**
   * TODO
   */
  private int above = 40;


  /**
   * TODO
   */
  private int naturalabove = 20;


  /**
   * TODO
   */
  private String tmpdir;


  /**
   * TODO
   */
  private String filename;


  /**
   * TODO
   * 
   * @param caller
   */
  public GeneralPrinter ( JPanel caller )
  {
    // document = new Document(PageSize.A4);
    // document = new Document(PageSize.A4.rotate());
    this.caller = caller;
  }


  /**
   * TODO
   * 
   * @param editorPanel
   */
  public void print ( TypeEditorPanel editorPanel )
  {
    // System.out.println("TypeEditorPanel print!");
    if ( !openDiaglog () )
    {
      return;
    }
    try
    {
      createSplitPage ( 0 );
      editorPanel.getEditor ().paint ( this.g3 );
      editorPanel.getEditor2 ().paint ( this.g4 );
      closePage ();
      concatenatePages ( 1 );
      deleteFiles ( 1 );
      // System.out.println("TypeEditorPanel print!");
      JOptionPane
          .showMessageDialog ( this.caller, "Document has been printed!" ); //$NON-NLS-1$
    }
    catch ( FileNotFoundException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace ();
    }
    catch ( DocumentException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace ();
    }
  }


  /**
   * TODO
   * 
   * @param editor
   */
  public void print ( StyledLanguageEditor editor )
  {
    if ( !openDiaglog () )
    {
      return;
    }
    boolean l33t = false;

    try
    {
      @SuppressWarnings ( "unused" )
      JPanel printarea = createPage ( 0 );
      Graphics remember = editor.getGraphics ();
      l33t = editor.getSize ().height > this.g2.getClipBounds ().height;
      editor.paint ( this.g2 );
      editor.paint ( remember );
      closePage ();

      // concatenate the temporary pages
      concatenatePages ( 1 );

      // remove the temporary pages now
      deleteFiles ( 1 );
      if ( l33t )
      {
        JOptionPane
            .showMessageDialog (
                this.caller,
                "Your Sourcecode is too long to be printed properly...\n" //$NON-NLS-1$
                    + "I give up, you are too elite.\n" //$NON-NLS-1$
                    + "Actually you are most likely Kurt or Ben...\n" //$NON-NLS-1$
                    + "It's lonely at the top. But you may get yourself a coffe now\n" //$NON-NLS-1$
                    + "and enjoy the comfort to look down upon everyone at the bottom." ); //$NON-NLS-1$
      }
      else
      {
        JOptionPane.showMessageDialog ( this.caller,
            "Document has been printed!" ); //$NON-NLS-1$
      }

    }
    catch ( FileNotFoundException e )
    {
      JOptionPane.showMessageDialog ( this.caller,
          "Can not access the file, might be open" ); //$NON-NLS-1$

      e.printStackTrace ();
    }
    catch ( DocumentException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace ();
    }

  }


  /**
   * TODO
   * 
   * @param icomp
   */
  public void print ( AbstractProofComponent icomp )
  {

    if ( !openDiaglog () )
    {
      return;
    }

    this.comp = icomp;
    this.comp.setBackground ( new Color ( 255, 255, 255 ) );
    // changed by Michael
    // comp.setOpaque(false);
    this.comp.setOpaque ( true );
    // xchanged by Michael

    try
    {
      // creating a temporary file to wirte to:
      // tmpdir = System.getProperty("java.io.tmpdir");
      // System.out.println(tmpdir);

      // Number Of Pages
      int nop = -1;
      int i = 0;

      do
      {
        // TODO remove the Jpanel it is not needed anny more!
        JPanel printarea = createPage ( i );

        if ( nop == -1 )
        {
          this.comp.setAvailableWidth ( this.g2.getClipBounds ().width );
          this.comp.setAvailableHeight ( this.g2.getClipBounds ().height );
          nop = ( this.comp.getHeight () / printarea.getHeight () + 1 );
        }
        printarea.add ( this.comp );

        // on the first page we will have to eliminate the natural top spacing
        // TODO setze hier _nicht_ die bounds von comp, sondern erstelle ein
        // entsprechend verschobenes
        // graphics auf g2 und printe darauf!

        // OLD:
        // if (i == 0) {
        // comp.setBounds(-naturalright, -naturalabove - i *
        // printarea.getHeight(), comp.getWidth(), comp.getHeight());
        // } else {
        // comp.setBounds(-naturalright, -i * printarea.getHeight(),
        // comp.getWidth(), comp.getHeight());
        // }
        // printarea.paint(g2);
        Graphics2D offsetted;
        if ( i == 0 )
        {
          offsetted = ( Graphics2D ) this.g2.create ( 0, -this.naturalabove - i
              * this.g2.getClipBounds ().height,
              this.g2.getClipBounds ().width, this.comp.getHeight () );
        }
        else
        {
          offsetted = ( Graphics2D ) this.g2.create ( 0, -i
              * this.g2.getClipBounds ().height,
              this.g2.getClipBounds ().width, this.comp.getHeight () );
        }
        this.comp.paint ( offsetted );

        closePage ();
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
  }


  /**
   * TODO
   */
  private void closePage ()
  {
    this.g2.dispose ();
    this.g1.dispose ();
    this.document.close ();
  }


  /**
   * TODO
   * 
   * @param i
   * @return TODO
   * @throws DocumentException
   * @throws FileNotFoundException
   */
  private JPanel createPage ( int i ) throws DocumentException,
      FileNotFoundException
  {
    this.document = new Document ( this.pageFormat );
    PdfWriter writer = PdfWriter.getInstance ( this.document,
        new FileOutputStream ( this.tmpdir + "/tmp" + i + ".pdf" ) ); //$NON-NLS-1$//$NON-NLS-2$
    this.document.open ();
    PdfContentByte cb = writer.getDirectContent ();
    // do not use the scale factor in the next one!
    this.g1 = cb.createGraphicsShapes ( this.pageFormat.getWidth (),
        this.pageFormat.getHeight () );
    this.g1.setBackground ( new Color ( 255, 255, 255 ) );
    this.g1.scale ( this.scale, this.scale );
    // g2.setClip(right, above,
    // g2.getClipBounds().width,g2.getClipBounds().height);
    this.g2 = ( Graphics2D ) this.g1.create ( this.right, this.above, this.g1
        .getClipBounds ().width
        - 2 * this.right, this.g1.getClipBounds ().height - 2 * this.above );
    this.g2.setBackground ( new Color ( 255, 255, 255 ) );
    JPanel j1 = new JPanel ();
    j1.setSize ( this.g2.getClipBounds ().width,
        this.g2.getClipBounds ().height );
    j1.setBackground ( new Color ( 255, 255, 255 ) );
    j1.setOpaque ( true );
    return j1;
  }


  /**
   * TODO
   * 
   * @param i
   * @throws DocumentException
   * @throws FileNotFoundException
   */
  private void createSplitPage ( int i ) throws DocumentException,
      FileNotFoundException
  {
    this.document = new Document ( this.pageFormat );
    PdfWriter writer = PdfWriter.getInstance ( this.document,
        new FileOutputStream ( this.tmpdir + "/tmp" + i + ".pdf" ) ); //$NON-NLS-1$//$NON-NLS-2$
    this.document.open ();
    PdfContentByte cb = writer.getDirectContent ();
    // do not use the scale factor in the next one!
    this.g1 = cb.createGraphicsShapes ( this.pageFormat.getWidth (),
        this.pageFormat.getHeight () );
    this.g1.setBackground ( new Color ( 255, 255, 255 ) );
    this.g1.scale ( this.scale, this.scale );
    // g2.setClip(right, above,
    // g2.getClipBounds().width,g2.getClipBounds().height);
    this.g2 = ( Graphics2D ) this.g1.create ( this.right, this.above, this.g1
        .getClipBounds ().width
        - 2 * this.right, this.g1.getClipBounds ().height - 2 * this.above );
    this.g3 = ( Graphics2D ) this.g2
        .create ( 0, 0, this.g2.getClipBounds ().width, this.g2
            .getClipBounds ().height / 2 - 5 );
    this.g4 = ( Graphics2D ) this.g2
        .create ( 0, this.g2.getClipBounds ().height / 2 + 5, this.g2
            .getClipBounds ().width, this.g2.getClipBounds ().height / 2 - 5 );
    this.g2.drawRect ( 0, this.g2.getClipBounds ().height / 2, this.g2
        .getClipBounds ().width, 1 );
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  private boolean openDiaglog ()
  {
    PdfDialog dialog = new PdfDialog ( ( JFrame ) this.caller
        .getTopLevelAncestor (), true );

    dialog.setLocationRelativeTo ( this.caller );
    dialog.setVisible ( true );
    // System.out.println(dialog.filechooser.getSelectedFile());
    // System.out.println(dialog.landscape);

    if ( dialog.cancelled )
    {
      return false;
    }

    this.filename = dialog.filechooser.getSelectedFile ().getAbsolutePath ();
    if ( !this.filename.substring ( this.filename.length () - 4 )
        .equalsIgnoreCase ( ".pdf" ) ) //$NON-NLS-1$
    {
      this.filename = this.filename + ".pdf"; //$NON-NLS-1$
    }
    this.tmpdir = dialog.filechooser.getSelectedFile ().getParent ();

    if ( dialog.landscape )
    {
      this.pageFormat = PageSize.A4.rotate ();
    }
    else
    {
      this.pageFormat = PageSize.A4;
    }
    return true;
  }


  /**
   * TODO
   * 
   * @param nop
   */
  private void concatenatePages ( int nop )
  {
    if ( nop < 1 )
    {
      return; // nothing to concatenate...
    }
    // concatenate temporary files here
    try
    {
      int pageOffset = 0;
      ArrayList < Object > master = new ArrayList < Object > ();
      int f = 0;
      String outFile = this.filename;
      Document newDocument = null;
      PdfCopy writer = null;
      while ( f < nop )
      {
        // we create a reader for a certain document
        PdfReader reader = new PdfReader ( this.tmpdir + "/tmp" + f + ".pdf" ); //$NON-NLS-1$ //$NON-NLS-2$
        reader.consolidateNamedDestinations ();
        // we retrieve the total number of pages
        int n = reader.getNumberOfPages ();

        pageOffset += n;

        if ( f == 0 )
        {
          // step 1: creation of a document-object
          newDocument = new Document ( reader.getPageSizeWithRotation ( 1 ) );
          // step 2: we create a writer that listens to the
          // document
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
    if ( nop < 1 )
    {
      return; // nothing to delete
    }
    for ( int i = 0 ; i < nop ; i++ )
    {
      File f = new File ( this.tmpdir + "/tmp" + i + ".pdf" ); //$NON-NLS-1$ //$NON-NLS-2$
      f.delete ();
    }
  }

}
