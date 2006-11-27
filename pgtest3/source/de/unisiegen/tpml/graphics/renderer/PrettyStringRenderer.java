package de.unisiegen.tpml.graphics.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.text.CharacterIterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.swing.text.StyledEditorKit.ItalicAction;

import de.unisiegen.tpml.Debug;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.graphics.Theme;
import de.unisiegen.tpml.graphics.components.Bonds;
import de.unisiegen.tpml.graphics.components.ShowBonds;


/**
 * Subclass of the {@link AbstractRenderer} providing the rendering of 
 * a {@link de.unisiegen.tpml.core.prettyprinter.PrettyString}
 * @author marcell
 *
 */
public class PrettyStringRenderer extends AbstractRenderer {
	
	/**
	 * Inner private class the store information for the line wrapping
	 * 
	 * @author marcell
	 *
	 */
	private class CheckerResult {
		/// The annotation used for the linewrapping
		public PrettyAnnotation		annotation;
		/// The rows used to 
		public int								rows;
		public Dimension					size;
		public CheckerResult() {
			this.size = new Dimension ();
		}
	}
	
	/**
	 * Results containg the information of all possible 
	 * linewrappings.
	 */
	private LinkedList<CheckerResult>		results;

	/**
	 * The pretty string that should be rendered.
	 */
	private PrettyString								prettyString;
	
	/**
	 * The result of all results that actually will be used
	 * during the rendering. If <i>result</i> is <i>null</i> 
	 * no line wrapping is done.
	 */
	private CheckerResult								result;
	
	/**
	 * The pretty printable that will be underline during the rendering.
	 */
	private PrettyPrintable							underlinePrettyPrintable;
	
	
	/**
	 * TODO
	 * Hier werden die Positionen gespeichert, auf denen die Maus reagieren muss...
	 */
	//private Vector<Integer> 											toListenForMouse;
	//public Vector getToListenForMouse()
	//{
	//	return toListenForMouse;
	//}
	private ToListenForMouseContainer					toListenForMouse;
	
	/**
	 * The annotation containing the information where the underline 
	 * appears within the string.
	 */
	private PrettyAnnotation						underlineAnnotation;
	
	public PrettyStringRenderer() {
		this.results 									= new LinkedList<CheckerResult>();
		this.result										= null;
		this.underlinePrettyPrintable	= null;
		this.underlineAnnotation			= null;
	}
	
	/**
	 * Sets the PrettyString 
	 * 
	 * @param prettyString
	 */
	public void setPrettyString (PrettyString prettyString) {
		this.prettyString = prettyString;
		
		if (this.prettyString != null && this.underlinePrettyPrintable != null) {
			this.underlineAnnotation = this.prettyString.getAnnotationForPrintable(this.underlinePrettyPrintable);
		}
		else {
			this.underlineAnnotation = null;
		}
		
		checkLinewraps ();
	}
	
	
	/**
	 * Sets the prettyPrintable that should be underlined.
	 * @param prettyPrintable
	 */
	public void setUndelinePrettyPrintable (PrettyPrintable prettyPrintable) {
		this.underlinePrettyPrintable = prettyPrintable;
	
		if (this.prettyString != null && this.underlinePrettyPrintable != null) {
			this.underlineAnnotation = this.prettyString.getAnnotationForPrintable(this.underlinePrettyPrintable);
		}
		else {
			this.underlineAnnotation = null;
		}
	}

	
	/**
	 * Checks which of the previously calculated result is the best
	 * match for the given maxWidth.<br>
	 * <br>
	 * The size of the widest result that still fits in the maxWidth
	 * will be returned. If no result is stored that fits in the
	 * smallest size is returned; in this case the returned size
	 * is wider than the given maxWidth.
	 * 
	 * 
	 * @param maxWidth The maximum available size for this expression. 
	 * @return The dimensions that will be needed to perform the rendering
	 * later.
	 */
	public Dimension getNeededSize (int maxWidth) {

		// Find the biggest result, that is smaller than the given
		// maxWidth. If none could be found the smllest result available
		// will be used.
		CheckerResult smallestResult = null;
		CheckerResult biggestResult = null;
		
		for (CheckerResult r : this.results) {
			if (smallestResult == null || smallestResult.size.width > r.size.width) {
				smallestResult = r;
			}

			if (r.size.width < maxWidth) {
				if (biggestResult == null || biggestResult.size.width < r.size.width) {
					biggestResult = r;
				}
			}
		}
		if (biggestResult != null)  {
			this.result = biggestResult;
		}
		else {
			this.result = smallestResult;
		}
		
		return this.result.size;
	}

	/**
	 * Checks the results for all possible annotations.
	 */
	public void checkLinewraps () {
		this.results.clear();
		
		this.results.add(checkLinewrap (null));
		
		for (PrettyAnnotation annotation : this.prettyString.getAnnotations()) {
			this.results.add(checkLinewrap (annotation));
		}
		
	}

	/**
	 * Checks the size that would be needed when the expression 
	 * would be rendered using the given Annotation.
	 * 
	 * @param annotation 
	 * @return
	 */
	private CheckerResult checkLinewrap (PrettyAnnotation annotation) {
		
		CheckerResult result = new CheckerResult ();
		result.rows = 1;
		
		int[] breakOffsets = null;
		if (annotation != null) {
			breakOffsets = annotation.getBreakOffsets();
		}
		else {
			breakOffsets = new int[0];
		}
		
		result.annotation = annotation;
		result.size.height = AbstractRenderer.fontHeight;
		
		PrettyCharIterator it = this.prettyString.toCharacterIterator();
		int i = 0;
		int w = 0;
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) {
			
			for (int j=0; j<breakOffsets.length; j++) {
				if (breakOffsets [j] == i) {
					result.size.height += AbstractRenderer.fontHeight;
					result.size.width = Math.max(w, result.size.width);
					result.rows++;
					
					// let the next line be to big fonts indentated
					w = AbstractRenderer.expFontMetrics.stringWidth("GG");
					break;
				}
			}
			
			switch (it.getStyle()) {
			case NONE:
				w += AbstractRenderer.expFontMetrics.charWidth(c);
				break;
			case KEYWORD:
				w += AbstractRenderer.keywordFontMetrics.charWidth(c);
				break;
			case CONSTANT:
				w += AbstractRenderer.constantFontMetrics.charWidth(c);
				break;
			case COMMENT:
				break;
			case TYPE:
				w += AbstractRenderer.typeFontMetrics.charWidth (c);
				break;
			}

		}
		result.size.width = Math.max(w, result.size.width);
		return result;
	}
	
	/**
	 * Hilfmethode, umd zu überprüfen, ob ein int in der Liste liegt
	 * 
	 *//*
	public static boolean isIn (int test, LinkedList <Bonds> list)
	{
		//System.out.println("Nun wird überprüft, ob die Zahl in der Liste steht...");
		//System.out.println("Länge der komischen Liste: "+list.size());
		boolean result = false;	//steht für false, positive Werte werden als Position und true missbraucht
		
		for (int i=0; i<list.size(); i++)
		{
			int min = list.get(i).getStartOffset();
			int max = list.get(i).getEndOffset();
			
			//LinkedList<PrettyAnnotation> other = list.get(i).marks;
			//System.out.println("alles zwischen "+min+" und "+max+ " wird makiert.");
			//list.get(i).
			//int tmp = 1;
			//System.out.println(""+tmp);
			if ((test <= max) && (test >= min)) 
				{
					return true;
				}
			else
			{
				int count=0;
				LinkedList <PrettyAnnotation> rest = list.get(i).getMarks();
				for (int j = 0 ; j<rest.size(); j++)
				{
					count++;
					PrettyAnnotation tmp = rest.get(j);
					int min1 = tmp.getStartOffset();
					int max1 = tmp.getEndOffset();
					if ((test <= max1) && (test >= min1)) 
					{
						return true;
					}
				}
			}
		}
		//nur nachsehen, ob diese Methode geht, damit tatsächlich was gemalt wird
		return result;
	}*/
	
	
	public static int isInList (int test, LinkedList <Bonds> list)
	{
		//System.out.println("Nun wird überprüft, ob die Zahl in der Liste steht...");
		//System.out.println("Länge der komischen Liste: "+list.size());
		int result = -1;
		
		for (int i=0; i<list.size(); i++)
		{
			int min = list.get(i).getStartOffset();
			int max = list.get(i).getEndOffset();
			
			//LinkedList<PrettyAnnotation> other = list.get(i).marks;
			//System.out.println("alles zwischen "+min+" und "+max+ " wird makiert.");
			//list.get(i).
			//int tmp = 1;
			//System.out.println(""+tmp);
			if ((test <= max) && (test >= min)) 
				{
					return i;
				}
			else
			{
				LinkedList <PrettyAnnotation> rest = list.get(i).getMarks();
				for (int j = 0 ; j<rest.size(); j++)
				{
					PrettyAnnotation tmp = rest.get(j);
					int min1 = tmp.getStartOffset();
					int max1 = tmp.getEndOffset();
					if ((test <= max1) && (test >= min1)) 
					{
						return i;
					}
				}
			}
		}
		return result;
	}

	
	
	
	
	/**
	 * Renders the Prettystring.
	 * <br><br>
	 * The Prettystring will be rendered verticaly centered between the
	 * position y and (y + height).
	 * 
	 * @param x Left position where the rendering should take place
	 * @param y Top position where the rendering should take place
	 * @param height The height that is available for the rendering.
	 * @param gc The Graphics context that will be used to render
	 * @return The width of the expression will get returned.
	 */
	public void render (int x, int y, int height,  Graphics gc, ShowBonds bound, ToListenForMouseContainer toListenForMouse) {
		//TODO Das muss noch schöner weerden, diese Forschleife ersetzt vernünftige Logik leider nicht...
		for (int schei = 0; schei<=2 ; schei++)
		{
				
		int[] breakOffsets = null;
		if (this.result.annotation != null) {
			breakOffsets = this.result.annotation.getBreakOffsets();
		}
		else {
			breakOffsets = new int[0];
		}
		
		// get the starting offsets x is just the left border
		// y will be the center of the space available minus the
		// propper amount of rows 
		int i = 0;
		int posX = x;
		
		int posY = y + height / 2;
		posY += AbstractRenderer.fontAscent / 2;
		
		float addY = (this.result.rows - 1) / 2.0f;
		addY *= AbstractRenderer.fontHeight;
		posY -= addY;
		
		
		// start and end position for the underlining
		int underlineStart 	= -1;
		int underlineEnd 		= -1;
		if (this.underlineAnnotation != null) {
			underlineStart	= this.underlineAnnotation.getStartOffset();
			underlineEnd		= this.underlineAnnotation.getEndOffset();
		}
		
		// now we can start to render the expression
		PrettyCharIterator it = this.prettyString.toCharacterIterator();
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next(), i++) 
		{
			for (int j=0; j<breakOffsets.length; j++) 
			{
				if (breakOffsets [j] == i) {
					posY += AbstractRenderer.fontHeight;
					
					posX = x;
				}
			}
			
			FontMetrics fm = null;
			
			fm = AbstractRenderer.expFontMetrics;
			int charWidth = fm.stringWidth("" + c);
			int charHighth = fm.getHeight();
			
			//Here we get the information where bindings exists in positions
			ShowBonds instanceOfShowBound = bound; 
			LinkedList annotationsList = instanceOfShowBound.getAnnotations();

			//look for aktual char is in this list (-1 stands for false)
			if (!(toListenForMouse.getMark()) && (isInList(i, annotationsList)) > -1)
			{
				//tell mouselistener in CompoundExpression to react at these positions
				toListenForMouse.add(posX);
				toListenForMouse.add(posX+charWidth);
				toListenForMouse.add(posY-fm.getAscent());
				toListenForMouse.add(posY+fm.getDescent());
			}
							
			//Wenn gemalt werden soll, also die Maus über einem Buchstaben steht
			//Damit die Liste mit jeder neuen Expression neu gesetzt wird, wird in CopoundExpression neu gesetz
			if (toListenForMouse.getMark() && isInList(i, annotationsList) != -1)
				{
		
				//get X-Pos of MousePointer
				int xPos = toListenForMouse.getHereIam()[0];
					
				//checks if MousePointer stands on the actual char
				if ((xPos >= posX) && (xPos <= posX+charWidth))
				{
					//TODO in diesem Fall muss er neu anfangen, er darf nicht mit den nächsten Buchstaben weiter machen...
					// - Vielleicht prüfen, ob die setRightList schon gesertzt, dann überspringen, sonst vorner anfangen. also die gleiche Funktion einfach nochmal aufrufen...
					toListenForMouse.setRightList(isInList(i, annotationsList));
		
				 }
				
				if (isInList(i, annotationsList) == toListenForMouse.getRightList())
				{
					Font orginalFont = gc.getFont();
					String fontName = orginalFont.getName();
					int fontSize = orginalFont.getSize();
					Font newFont = new Font(fontName, Font.BOLD|Font.ITALIC, fontSize);
					gc.setColor(Theme.currentTheme().getBindingColor());
					//gc.setColor(Color.orange);
					gc.setFont(newFont);
					//int charWidth = fm.stringWidth("" + c);
					gc.drawLine(posX, posY + 1, posX + charWidth, posY + 1);
				}
				
			}
			//manipulating font
			
			
			else
			{
//			 select the proppert font and color for the character
				
				switch (it.getStyle()) {
				case NONE:
					gc.setFont(AbstractRenderer.expFont);
					gc.setColor(AbstractRenderer.expColor);
					//fm = AbstractRenderer.expFontMetrics;
					break;
				case KEYWORD:
					gc.setFont(AbstractRenderer.keywordFont);
					gc.setColor(AbstractRenderer.keywordColor);
					//fm = AbstractRenderer.keywordFontMetrics;
					break;
				case CONSTANT:
					gc.setFont(AbstractRenderer.constantFont);
					gc.setColor(AbstractRenderer.constantColor);
					//fm = AbstractRenderer.constantFontMetrics;
					break;
				case COMMENT:
					continue;
				case TYPE:
					gc.setFont(AbstractRenderer.typeFont);
					gc.setColor(AbstractRenderer.typeColor);
					//fm = AbstractRenderer.typeFontMetrics;
					break;
				}
				
			}
			if (i >= underlineStart && i <= underlineEnd) {
				// the current character is in the range, where underlining
				// should happen
				
				// save the current color, it will become resetted later
				Color color = gc.getColor();
				gc.setColor(AbstractRenderer.underlineColor);
				
				
				// draw the line below the character 
				//int charWidth = fm.stringWidth("" + c);
				gc.drawLine(posX, posY + 1, posX + charWidth, posY + 1);
				
				// reset the color for the characters
				gc.setColor(color);
				
			}
			if (this.alternativeColor != null) {
				gc.setColor(this.alternativeColor);
			}
			
			// draw the character and move the position
			gc.drawString("" + c, posX, posY);
			//			Komische TEstausgabe TODO komische Testausgabe
			//System.out.println("Position in X-Richtung, in Y-Richtung ist: "+posX + " " + posY );
			
			posX += fm.stringWidth("" + c);
			
			// go on to the next character
		}		
	}
	}
	
}


