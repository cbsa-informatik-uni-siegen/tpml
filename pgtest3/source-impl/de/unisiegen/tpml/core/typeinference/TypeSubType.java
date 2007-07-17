package de.unisiegen.tpml.core.typeinference;

import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interfaces.ShowBondsInput;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;

public class TypeSubType implements ShowBondsInput, TypeFormula, PrettyPrintable , PrettyPrintPriorities {
	 
	//
	// Attributes
	//
	
	private MonoType type;
	
	private MonoType type2;
	
	public TypeSubType( MonoType pType, MonoType pType2){
		this.type = pType;
		this.type2 = pType2;
	}

	  /**
	   * {@inheritDoc}
	   * 
	   * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getEnvironment()
	   */
	  public DefaultTypeEnvironment getEnvironment ( )
	  {
	    return new DefaultTypeEnvironment ( ) ;
	  }

	  /**
	   * {@inheritDoc}
	   * 
	   * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getExpression()
	   */
	  public Expression getExpression ( )
	  {
	    return null ;
	  }

	public MonoType getType ( ) {
		return this.type;
	}
	
	public MonoType getType2 ( ) {
		return this.type2;
	}

	public TypeFormula substitute ( ArrayList < DefaultTypeSubstitution > s ) {
		return this;
	}
	
	  //
	  // Pretty printing
	  //
	  /**
	   * {@inheritDoc}
	   * 
	   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
	   */
	  public final PrettyString toPrettyString ( )
	  {
	    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
	        .toPrettyString ( ) ;
	  }
	
	  /**
	   * {@inheritDoc} Returns the string representation for the type equation,
	   * which is primarily useful for debugging.
	   * 
	   * @see java.lang.Object#toString()
	   */
	  @ Override
	  public String toString ( )
	  {
	    
	    return  this.type + " <: " + this.type2 ; //$NON-NLS-1$ 
	  }
	  
	  /**
	   * Returns the {@link PrettyStringBuilder}.
	   * 
	   * @param factory The {@link PrettyStringBuilderFactory}.
	   * @return The {@link PrettyStringBuilder}.
	   */
	  private PrettyStringBuilder toPrettyStringBuilder (
	      PrettyStringBuilderFactory factory )
	  {
	    PrettyStringBuilder builder = factory.newBuilder ( this , PRIO_EQUATION ) ;
	    builder.addBuilder ( this.type.toPrettyStringBuilder ( factory ) ,
	        PRIO_EQUATION ) ;
	    builder.addText ( " <: " ) ; //$NON-NLS-1$
	    builder.addBuilder ( this.type2.toPrettyStringBuilder ( factory ) ,
	        PRIO_EQUATION ) ;
	    return builder ;
	  }

}
