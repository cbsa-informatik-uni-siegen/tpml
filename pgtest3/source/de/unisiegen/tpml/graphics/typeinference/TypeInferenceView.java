package de.unisiegen.tpml.graphics.typeinference;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JScrollPane;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.graphics.AbstractProofView;

/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView} interface for the small
 * step interpreter user interface.
 *
 * @author Marcell Fischbach
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepComponent
 */
public class TypeInferenceView extends AbstractProofView {
	//
	// Constants
	//
	
	/**
	 * The unique serialization identifier for this class.
	 */
	private static final long serialVersionUID = -8529052541636149376L;
	
	
	
	//
	// Attributes
	//
	
	/**
	 * The small step component.
	 */
	private TypeInferenceComponent			component;
	
	/**
	 * The scroll pane for the <code>component</code>.
	 */
	private JScrollPane							scrollPane;

	
	
	//
	// Constructor
	//
	
	/**
	 * Allocates a new <code>SmallStepView</code> for the specified <code>model</code>.
	 * 
	 * @param model the proof model for the small step view.
	 * 
	 * @throws NullPointerException if <code>model</code> is <code>null</code>.
	 */
	//TODO TypeInferenceProofMelde oder TypeInfrecneModel
	public TypeInferenceView (TypeInferenceProofModel model) {
		if (model == null) {
			throw new NullPointerException("model is null");
		}
		
		setLayout (new BorderLayout ());
		
		this.scrollPane = new JScrollPane ();
		//TODO advanced wieder einbauen
		this.component = new TypeInferenceComponent (model, isAdvanced() );
		
		add (this.scrollPane, BorderLayout.CENTER);
		
		this.scrollPane.setViewportView(this.component);
		this.scrollPane.getViewport().setBackground(Color.WHITE);
		
		this.scrollPane.addComponentListener(new ComponentAdapter () {
			@Override
			public void componentResized (ComponentEvent event) {
				TypeInferenceView.this.component.setAvailableWidth(TypeInferenceView.this.scrollPane.getViewport ().getWidth());
			}
		});
	}
	
	//
	// Primitives
	//
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.graphics.ProofView#guess()
	 */
	public void guess() throws IllegalStateException, ProofGuessException {
		this.component.guess ();
	}
	
	/**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.AbstractProofView#setAdvanced(boolean)
   */
  @ Override
  public void setAdvanced ( boolean advanced )
  {
  	System.out.println("jetzt bekommt der TypeInference-View den advaced-Wert: "+advanced);
    super.setAdvanced ( advanced ) ;
    this.component.setAdvanced ( isAdvanced ( ) ) ;
  }
}
