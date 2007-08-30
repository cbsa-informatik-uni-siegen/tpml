package de.unisiegen.tpml.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import de.unisiegen.tpml.core.latex.LatexException;
import de.unisiegen.tpml.core.latex.LatexExport;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.ui.netbeans.PdfDialog;
import de.unisiegen.tpml.ui.netbeans.TexDialog;

public class GeneralLaTex
{
	
	//Klasse, die die Datei baut, hier wird eine staitsche MEhtode aufgerufen, die das File übergeben bekommt, und den
	//latexprintable

	//wir brauchen die view
	//private ProofViewComponent ourProofView;
	private LatexPrintable laTexPrintable;
	
	private JPanel parent;
	
	private JDialog status;
	
	private JTextArea text;
	
	private JProgressBar progress;
	
	public GeneralLaTex ()
	{

	}
	
	public GeneralLaTex (LatexPrintable pLaTexPrintable, JPanel pParent)
	{
		this.laTexPrintable = pLaTexPrintable;
		
		this.parent = pParent;

	}

	public File showFileDialog ()
	{
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		fc.setDialogTitle("LaTex - Export");
		fc.setDragEnabled(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter ( new FileFilter ( )
		{
			@Override
			public boolean accept ( File f )
			{
				return f.getName ( ).toLowerCase ( ).endsWith ( ".tex" ) //$NON-NLS-1$
						|| f.isDirectory ( );
			}

			@Override
			public String getDescription ( )
			{
				return "TEX-Files (*.tex)"; //$NON-NLS-1$
			}
		} );
		fc.showDialog(this.parent, "Export");
		return fc.getSelectedFile();
	}
	
	/**
	 * TODO only for testing
	 *
	 * @param args
	 */
	public static void main (String [] args )
	{
		GeneralLaTex test = new GeneralLaTex();
		File file = test.showFileDialog();
		System.out.println("selected File: "+file);
		
		
		String filename = file.getAbsolutePath();
		if (! filename.substring(filename.length()-4).equalsIgnoreCase(".tex")){
		    filename = filename+".tex";
		    file = new File (filename);
		}
		
		System.out.println("selected File: "+file);
		
		
		//tatischeKlasse.statischemethode (test.ourProofView.getModel().getLatexPrintable(), file);
		
		 JOptionPane.showMessageDialog(test.parent, "LaTex-Export done!");
		
		
	}

	public void export()
	{
		TexDialog dialog = new TexDialog((JFrame)parent.getTopLevelAncestor(), true);
		
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		
		if (dialog.cancelled)
		{
			return;
		}
		
		File file = dialog.filechooser.getSelectedFile();
		int overlapping = dialog.overlappingInt;
		boolean all = dialog.all;
		System.out.println("Infos: "+file+" - "+overlapping+" - "+all);
		
		//File file = showFileDialog();
		if (file != null)
		{
			System.out.println("selected File: "+file);
			
			// fix the filename if the user has not entered a filename ending with .tex
			String filename = file.getAbsolutePath();
			if (! filename.substring(filename.length()-4).equalsIgnoreCase(".tex"))
			{
			    filename = filename+".tex";
			    file = new File (filename);
			}
			
			//LatexTest.exportLatexPrintable((SmallStepProofModel)this.ourProofView.getModel(), file);
			try
			{
				status = new JDialog ();
				status.setTitle("Stautus");
				status.setModal(false);
				
				text = new JTextArea ( "LaTex-Export will be done. Pleas wait.");
				status.add(text);
				status.setSize(150, 100);
				
//			 Größe des Bildschirms ermitteln
	      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	      // Position des JFrames errechnen
	      int top = (screenSize.height - status.getHeight()) / 2;
	      int left = (screenSize.width - status.getWidth()) / 2;

	      // Position zuordnen
	      status.setLocation(left, top); 
				
				
				status.setVisible(true);
				
				LatexExport.export(laTexPrintable, file);
				
				status.dispose();
				
				JOptionPane.showMessageDialog(this.parent, "LaTex-Export done!");
			}
			catch (LatexException e)
			{
				// TODO "Error" in die Properties packen
				JOptionPane.showMessageDialog(this.parent, e.toString(), "Error", JOptionPane.ERROR_MESSAGE); 
			}
						
		}
				
	}

}
